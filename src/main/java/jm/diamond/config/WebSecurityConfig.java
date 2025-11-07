package jm.diamond.config;

import jm.diamond.security.CustomAuthenticationFailureHandler;
import jm.diamond.security.CustomAuthenticationProvider;
import jm.diamond.security.CustomAuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

//@EnableWebSecurity(debug = true)  // request가 올 떄마다 어떤 filter를 사용하고 있는지 출력을 해준다.
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    private final CustomAuthenticationProvider customAuthenticationProvider;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()                     // CSRF 보호 비활성화
                .authorizeRequests()
                .anyRequest()
                    .permitAll()         // 모든 요청 허용
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/api/form/login")
                    .usernameParameter("userId")
                    .passwordParameter("password")
                    .successHandler(customAuthenticationSuccessHandler)
                    .failureHandler(customAuthenticationFailureHandler)
                    .and()
                .httpBasic().disable();               // HTTP Basic 인증 비활성화


//        http.formLogin().dis
//            .loginPage("/login")
//            .loginProcessingUrl("/api/form/login")
////            .loginProcessingUrl("/api/login")
//            .usernameParameter("loginId")
//            .passwordParameter("password")
//            .successHandler(customAuthenticationSuccessHandler)
//            .failureHandler(customAuthenticationFailureHandler)
        ;
    }
}
