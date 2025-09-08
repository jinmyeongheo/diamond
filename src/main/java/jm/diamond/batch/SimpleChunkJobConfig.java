package jm.diamond.batch;

import com.querydsl.core.BooleanBuilder;
import jm.diamond.batch.reader.modn.reader.QuerydslNoOffsetPagingItemReader;
import jm.diamond.batch.reader.modn.reader.QuerydslPagingItemReader;
import jm.diamond.batch.reader.modn.reader.expression.Expression;
import jm.diamond.batch.reader.modn.reader.options.QuerydslNoOffsetNumberOptions;
import jm.diamond.batch.reader.modn.reader.options.QuerydslNoOffsetOptions;
import jm.diamond.batch.reader.modn.reader.options.QuerydslNoOffsetStringOptions;
import jm.diamond.dao.entity.OrderInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;

import static jm.diamond.dao.entity.QOrderInfo.orderInfo;

@Slf4j
//@Configuration
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
    public Step simpleChunkStep() {
        return stepBuilderFactory.get("simpleChunkStep")
                .<OrderInfo, OrderInfo>chunk(3) // 3개 단위로 처리
                .reader(itemReader())
                .processor(itemProcessor())
                .writer(itemWriter())
                .build();
    }

    @Bean
    public QuerydslPagingItemReader<OrderInfo> itemReader() {

        QuerydslNoOffsetNumberOptions<OrderInfo, Long> options =
                new QuerydslNoOffsetNumberOptions<>(orderInfo.id, Expression.ASC);

        BooleanBuilder where =
                new BooleanBuilder()
                        .and(orderInfo.id.in(123));


        int CHUNK_SIZE = 100;
        return new QuerydslNoOffsetPagingItemReader<>(
                emf, CHUNK_SIZE, options, q -> q.selectFrom(orderInfo).where(where));
    }

    @Bean
    public ItemProcessor<OrderInfo, OrderInfo> itemProcessor() {
        return item -> item; // 간단히 대문자로 변환
    }

    @Bean
    public ItemWriter<OrderInfo> itemWriter() {
        return items -> {
            log.info("쓰기: " + items.get(0).getId());
        };
    }
}

