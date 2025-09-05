package jm.diamond.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SimpleChunkJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;


    @Bean
    public Job simpleChunkJob(Step simpleChunkStep) {
        return jobBuilderFactory.get("simpleChunkJob")
                .start(simpleChunkStep)
                .build();
    }

    @Bean
    public Step simpleChunkStep() {
        return stepBuilderFactory.get("simpleChunkStep")
                .<String, String>chunk(3) // 3개 단위로 처리
                .reader(itemReader())
                .processor(itemProcessor())
                .writer(itemWriter())
                .build();
    }

    @Bean
    public ItemReader<String> itemReader() {
        return new ListItemReader<>(new ArrayList<>());
    }

    @Bean
    public ItemProcessor<String, String> itemProcessor() {
        return item -> item.toUpperCase(); // 간단히 대문자로 변환
    }

    @Bean
    public ItemWriter<String> itemWriter() {
        return items -> {
            System.out.println("쓰기: " + items);
        };
    }
}

