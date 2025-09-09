package jm.diamond.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableAutoConfiguration
@EnableBatchProcessing
@EntityScan("jm.diamond.dao.entity")
@EnableJpaRepositories("jm.diamond.dao.repository")
@EnableTransactionManagement
public class TestBatchConfig {
    // @SpringBootTest에서 Classes를 기입하면서 적은 class들의 관련된 세팅만 빈으로 올리게되면서 에러가 발생
}
