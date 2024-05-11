package jm.diamond.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .httpBasic().disable()
            .csrf().disable() // 요청자의 의도치 않은 공격 방어. get요청은 검사하지않지만 나머지는 다 방어함.
            .authorizeRequests()
            .anyRequest().permitAll()
        ;
        http.formLogin()
//            .loginProcessingUrl("/api/login")
            .usernameParameter("loginId")
            .passwordParameter("password")
        ;
    }
}
