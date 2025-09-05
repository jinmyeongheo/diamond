package jm.diamond.batch;

import org.junit.jupiter.api.Test;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@SpringBatchTest
@ActiveProfiles("local")
class SimpleChunkJobConfigTest {

    @Test
    void test(){
        System.out.println("true = " + true);
    }
}