package jm.diamond.batch;

import jm.diamond.batch.reader.modn.reader.QuerydslNoOffsetPagingItemReader;
import jm.diamond.batch.reader.modn.reader.QuerydslPagingItemReader;
import jm.diamond.batch.reader.modn.reader.QuerydslZeroPagingItemReader;
import jm.diamond.batch.reader.modn.reader.expression.Expression;
import jm.diamond.batch.reader.modn.reader.options.QuerydslNoOffsetNumberOptions;
import jm.diamond.batch.reader.modn.reader.options.QuerydslNoOffsetOptions;
import jm.diamond.dao.entity.OrderInfo;
import jm.diamond.dao.entity.PaymentBaseInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JsonParseException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.listener.RetryListenerSupport;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import javax.persistence.EntityManagerFactory;
import java.net.SocketTimeoutException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static jm.diamond.dao.entity.QOrderInfo.orderInfo;

/**
 *
 * https://jonny-cho.github.io/spring/2025-07-27-spring-batch-chapter5-skip-retry-restart/
 * https://www.javacodegeeks.com/2025/02/robust-error-handling-in-spring-batch.html?utm_source=chatgpt.com
 * 파티션으로 나눈 구역을 개별 스레드에 할당해서 병렬처리
 * */

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SimpleChunkJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory emf;

    private final LocalDateParameter localDateParameter;

    int CHUNK_SIZE = 600;

    @Bean
    @JobScope
    public LocalDateParameter localDateParameter(
            @Value("#{jobParameters[requestDate]}") String requestDate) {
        return new LocalDateParameter(requestDate);
    }

    @Bean
    public CronTriggerFactoryBean exampleJob1Trigger() {
        return BatchHelper.cronTriggerFactoryBeanBuilder()
                .cronExpression("0 0/1 * 1/1 * ? *")
                .jobDetailFactoryBean(exampleJob1Schedule())
                .build();
    }

    @Bean
    public JobDetailFactoryBean exampleJob1Schedule() {
        return BatchHelper.jobDetailFactoryBeanBuilder()
                .job(simpleChunkJob())
                .build();
    }

    @Bean(name = "simpleChunkJob")
    public Job simpleChunkJob() {
        return jobBuilderFactory.get("simpleChunkJob")
                .start(simpleChunkStep())
                .build();
    }

    @Bean
    @JobScope // Late Binding -> thread safe
    public Step simpleChunkStep() {
        log.info("localDateParameter : {}", localDateParameter);
        return stepBuilderFactory.get("simpleChunkStep")
                .startLimit(3)	//	재시작 3번 가능
                .<OrderInfo, PaymentBaseInfo>chunk(CHUNK_SIZE) // 3개 단위로 처리
                .reader(itemReader())
                .processor(itemProcessor())
                .writer(itemWriter())
                .faultTolerant()
                .retryPolicy(transientOnlyPolicy())  // ✅ Retry: 일시적 오류만
                .backOffPolicy(exponentialBackoff()) // ✅ Backoff: 과도한 재시도 방지
                .skipPolicy(new MapBasedSkipPolicy()) // ❌ 데이터 오류는 retry 금지 → 필요 시 skip으로 전환
                .skipLimit(100)
                .listener(retryListener())   // 로깅/메트릭
                .noSkip(NullPointerException.class) //  NullPointerException 에 대해서는 skip 하지 않음
//                .transactionManager(new ResourcelessTransactionManager())
                .build();
    }



    @Bean
    public RetryPolicy transientOnlyPolicy() {
        Map<Class<? extends Throwable>, Boolean> map = new HashMap<>();
        map.put(HttpServerErrorException.class, true);
        map.put(SocketTimeoutException.class, true);
        map.put(DeadlockLoserDataAccessException.class, true);

        // 명시적으로 재시도 금지(데이터/비즈니스 오류)
        map.put(HttpClientErrorException.class, false);          // 4xx
        map.put(ConstraintViolationException.class, false);
        map.put(JsonParseException.class, false);
        map.put(IllegalArgumentException.class, false);

        return new SimpleRetryPolicy(3, map, true); // true=Subclass 매칭 허용
    }

    @Bean
    public BackOffPolicy exponentialBackoff() {
        ExponentialBackOffPolicy p = new ExponentialBackOffPolicy();
        p.setInitialInterval(500);    // 0.5s
        p.setMultiplier(2.0);         // 0.5s → 1s → 2s
        p.setMaxInterval(5000);       // 5s caps
        return p;
    }

    @Bean
    public RetryListener retryListener() {
        return new RetryListenerSupport() {
            @Override
            public <T, E extends Throwable> void onError(RetryContext ctx, RetryCallback<T, E> cb, Throwable t) {
                log.warn("retrying {}th for {} due to {}", ctx.getRetryCount(), ctx.getAttribute("context.name"), t.toString());
            }
        };
    }


//    @Bean
//    @StepScope
//    public QuerydslPagingItemReader<OrderInfo> itemReader() {
//
//        QuerydslNoOffsetNumberOptions<OrderInfo, Long> options =
//                new QuerydslNoOffsetNumberOptions<>(orderInfo.id, Expression.ASC);
//
//        BooleanBuilder where =
//                new BooleanBuilder()
//                        .and(orderInfo.id.in(1L));
//
//
//        int CHUNK_SIZE = 100;
//        return new QuerydslNoOffsetPagingItemReader<>(
//                emf, CHUNK_SIZE, options, q -> q.selectFrom(orderInfo));
//    }

    @Bean
    @StepScope
    public QuerydslPagingItemReader<OrderInfo> itemReader(){

        QuerydslNoOffsetNumberOptions<OrderInfo, Long> option = new QuerydslNoOffsetNumberOptions<>(orderInfo.id, Expression.ASC);
        QuerydslNoOffsetPagingItemReader<OrderInfo> orderInfoQuerydslNoOffsetPagingItemReader =
                new QuerydslNoOffsetPagingItemReader<>(emf, CHUNK_SIZE, option, jpaQueryFactory -> jpaQueryFactory
                .selectFrom(orderInfo)
                .where(orderInfo.orderDateTime
                        .between(LocalDateTime.of(2025, 9, 30, 22, 30, 0),
                                LocalDateTime.of(2025, 10, 30, 22, 30, 0)))
        );

        orderInfoQuerydslNoOffsetPagingItemReader.setSaveState(true); // ✅ ExecutionContext에 진행상태 저장

        return orderInfoQuerydslNoOffsetPagingItemReader;
    }

    @Bean
    public ItemProcessor<OrderInfo, PaymentBaseInfo> itemProcessor() {
        return item -> new PaymentBaseInfo(item.getId(), item.getAmount(),LocalDateTime.now()); // 간단히 대문자로 변환
    }

    @Bean
    @StepScope
    public JpaItemWriter<PaymentBaseInfo> itemWriter() {
        JpaItemWriter<PaymentBaseInfo> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(emf);
        return writer;
    }
}

