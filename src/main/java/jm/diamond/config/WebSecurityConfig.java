package jm.diamond.config;

import jm.diamond.security.CustomAuthenticationFailureHandler;
import jm.diamond.security.CustomAuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http
//            .httpBasic().disable()
//            .csrf().disable() // 요청자의 의도치 않은 공격 방어. get요청은 검사하지않지만 나머지는 다 방어함.
//            .authorizeRequests()
//            .anyRequest().permitAll()
//        ;
        http.authorizeRequests()
            .antMatchers("/login.html").permitAll()
            .antMatchers("/signup.html").permitAll()
            .antMatchers("/index.html").permitAll()
            .antMatchers("/api/**").permitAll()
            .antMatchers("/lib/**").permitAll()
            .anyRequest().authenticated()
            .and().csrf().disable()
            .cors().disable()
            .httpBasic().disable()
        ;
        http.formLogin()
            .loginPage("/login.html")
            .loginProcessingUrl("/api/form/login")
//            .loginProcessingUrl("/api/login")
            .usernameParameter("loginId")
            .passwordParameter("password")
            .successHandler(customAuthenticationSuccessHandler)
            .failureHandler(customAuthenticationFailureHandler)
        ;
    }
}
