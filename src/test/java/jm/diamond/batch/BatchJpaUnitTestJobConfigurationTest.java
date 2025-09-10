package jm.diamond.batch;

import jm.diamond.dao.entity.OrderInfo;
import jm.diamond.dao.repository.OrderInfoRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static java.time.format.DateTimeFormatter.ofPattern;

@RunWith(SpringRunner.class)
@SpringBatchTest
@SpringBootTest(classes={SimpleChunkJobConfig.class, TestBatchConfig.class})
public class BatchJpaUnitTestJobConfigurationTest {

    // https://github.com/jojoldu/spring-batch-in-action?tab=readme-ov-file

    public static final DateTimeFormatter FORMATTER = ofPattern("yyyy-MM-dd");

    @Autowired
    private JpaPagingItemReader<OrderInfo> reader;

    @Autowired
    private OrderInfoRepository orderInfoRepository;

    private static final LocalDate orderDate = LocalDate.of(2019,10,6);

    @After
    public void tearDown() throws Exception {
        orderInfoRepository.deleteAllInBatch();
    }

    public StepExecution getStepExecution() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("orderDate", orderDate.format(FORMATTER))
                .toJobParameters();

        return MetaDataInstanceFactory.createStepExecution(jobParameters);
    }

    @Test
    public void 기간내_Sales가_집계되어_SalesSum이된다() throws Exception {
        //given
        int amount1 = 1000;
        int amount2 = 500;
        int amount3 = 100;

//        saveSales(amount1, "1");
//        saveSales(amount2, "2");
//        saveSales(amount3, "3");

        reader.open(new ExecutionContext());

        //when & then
//        Assert.assertThat(reader.read().getAmount()).isEqualTo(100L);
//        Assert.assertThat(reader.read()).isNull(); // 더이상 읽을게 없어 null
    }

//    private Sales saveSales(long amount, String orderNo) {
//        return salesRepository.save(new Sales(orderDate, amount, orderNo));
//    }
}
