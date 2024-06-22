package jm.diamond.config;

import jm.diamond.security.CustomAuthenticationFailureHandler;
import jm.diamond.security.CustomAuthenticationProvider;
import jm.diamond.security.CustomAuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

//@EnableWebSecurity(debug = true)  // request가 올 떄마다 어떤 filter를 사용하고 있는지 출력을 해준다.
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
//        http
//            .httpBasic().disable()
//            .csrf().disable() // 요청자의 의도치 않은 공격 방어. get요청은 검사하지않지만 나머지는 다 방어함.
//            .authorizeRequests()
//            .anyRequest().permitAll()
//        ;


        http.authorizeRequests()
//            .antMatchers("/login.html").permitAll()
//            .antMatchers("/signup.html").permitAll()
//            .antMatchers("/index.html").permitAll()
//            .antMatchers("/api/**").permitAll()
//            .antMatchers("/lib/**").permitAll()
//            .anyRequest().authenticated()
            .anyRequest().permitAll()
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
