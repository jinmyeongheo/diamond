package jm.diamond;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@EnableBatchProcessing
@SpringBootApplication
public class DiamondApplication {

   public static void main(String[] args) {
      SpringApplication.run(DiamondApplication.class, args);
   }

}
