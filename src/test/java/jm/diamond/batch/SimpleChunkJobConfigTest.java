package jm.diamond.batch;

import java.util.List;
import jm.diamond.dao.entity.OrderInfo;
import jm.diamond.dao.repository.OrderInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
//@SpringBatchTest
@ActiveProfiles("local")
class SimpleChunkJobConfigTest {

//    @Autowired
//    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private OrderInfoRepository orderInfoRepository;

    @Test
    void JDBC_커넥션_테스트(){
        List<OrderInfo> all =
            orderInfoRepository.findAll();
    }

    @Test
    void checkTransactionManager() { // JpaTransactionManager는 같은 DataSource를 쓰는 plain JDBC 접근(= MyBatis/JdbcTemplate 등)을 동일 트랜잭션에 참여시킬 수 있습니다
        PlatformTransactionManager txManager =
            applicationContext.getBean(PlatformTransactionManager.class);

        System.out.println(">>> TransactionManager class = " + txManager.getClass().getName());
    }

//    @Test
//    void 한글로_테스트_해요() throws Exception {
//
//        // when
//        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
//
//        // then
//        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
//
//        System.out.println("true = " + true);
//    }
}