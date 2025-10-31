package jm.diamond.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration
public class AsyncConfig {

    // 빈 설정
    @Bean(name = "approvalTaskExecutor")
    public ThreadPoolTaskExecutor defaultTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(200); // corePoolSize 설정
        executor.setMaxPoolSize(200); // maxPoolSize 설정
        executor.setKeepAliveSeconds(10); // keepAliveTime 설정
        executor.setQueueCapacity(100); // queue 최대 크기 설정
        return executor;
    }

}
