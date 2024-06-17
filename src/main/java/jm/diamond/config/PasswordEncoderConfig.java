package jm.diamond.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/** encoder 순환참조 에러때문에 WebSecurityConfig 밖으로 뺌*/
@Configuration
public class PasswordEncoderConfig {

   @Bean
   public BCryptPasswordEncoder bCryptPasswordEncoder() {
      return new BCryptPasswordEncoder();
   }
}
