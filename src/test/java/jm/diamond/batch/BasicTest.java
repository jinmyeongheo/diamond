package jm.diamond.batch;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

@ActiveProfiles("local")
@SpringBootTest
@SpringBatchTest
public class BasicTest {


    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    @Qualifier("simpleChunkJob")   // ✅ 원하는 Job Bean 이름
    private Job simpleChunkJob;

    @BeforeEach
    void setUp() {
        // 원하는 Job을 지정해서 실행 준비
        jobLauncherTestUtils.setJob(simpleChunkJob);
    }

    @Test
    void testSimpleChunkJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("requestDate", LocalDateTime.now().toString())
                .toJobParameters();

        JobExecution execution = jobLauncherTestUtils.launchJob(jobParameters);
    }


}
