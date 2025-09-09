package jm.diamond.batch;

import com.querydsl.core.BooleanBuilder;
import jm.diamond.batch.reader.modn.reader.QuerydslNoOffsetPagingItemReader;
import jm.diamond.batch.reader.modn.reader.QuerydslPagingItemReader;
import jm.diamond.batch.reader.modn.reader.expression.Expression;
import jm.diamond.batch.reader.modn.reader.options.QuerydslNoOffsetNumberOptions;
import jm.diamond.dao.entity.OrderInfo;
import jm.diamond.dao.entity.PaymentBaseInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;


@Slf4j
@Configuration
@RequiredArgsConstructor
public class SimpleChunkJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory emf;


    @Bean
    public Job simpleChunkJob(Step simpleChunkStep) {
        return jobBuilderFactory.get("simpleChunkJob")
                .start(simpleChunkStep)
                .build();
    }

    @Bean
    @JobScope // Late Binding -> thread safe
    public Step simpleChunkStep(@Value("#{jobParameters['power']}") String param) {
        return stepBuilderFactory.get("simpleChunkStep")
                .<OrderInfo, PaymentBaseInfo>chunk(3) // 3개 단위로 처리
                .reader(itemReader())
                .processor(itemProcessor())
                .writer(itemWriter())
                .build();
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
    public JpaPagingItemReader<OrderInfo> itemReader(){
        int CHUNK_SIZE = 100;

        return new JpaPagingItemReaderBuilder<OrderInfo>()
                .name("customerJpaPagingItemReader")
                .queryString("SELECT o FROM OrderInfo AS o")
                .pageSize(CHUNK_SIZE)
                .entityManagerFactory(emf)
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<OrderInfo, PaymentBaseInfo> itemProcessor() {
        return item -> new PaymentBaseInfo(item.getId(), item.getAmount()); // 간단히 대문자로 변환
    }

    @Bean
    @StepScope
    public JpaItemWriter<PaymentBaseInfo> itemWriter() {
        JpaItemWriter<PaymentBaseInfo> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(emf);
        return writer;
    }
}

