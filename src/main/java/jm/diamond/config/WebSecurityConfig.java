package jm.diamond.config;

import jm.diamond.controller.sso.LoginUser;
import jm.diamond.controller.sso.WrappedOidcUser;
import jm.diamond.dao.entity.PrivilegeInfo;
import jm.diamond.dao.entity.User;
import jm.diamond.dao.repository.UserRepository;
import jm.diamond.security.CustomAuthenticationFailureHandler;
import jm.diamond.security.CustomAuthenticationProvider;
import jm.diamond.security.CustomAuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.oidc.authentication.OidcIdTokenDecoderFactory;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoderFactory;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.http.HttpSession;
import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

//@EnableWebSecurity(debug = true)  // request가 올 떄마다 어떤 filter를 사용하고 있는지 출력을 해준다.
@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    private final CustomAuthenticationProvider customAuthenticationProvider;

    // ssp
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final AuthenticationFailureHandler authenticationFailureHandler;
    private final UserRepository userRepository;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()                     // CSRF 보호 비활성화
                .authorizeRequests()
                    .antMatchers(
                            "/login",                // custom login page
                            "/api/form/login",       // form login endpoint
                            "/oauth2/**")            // OIDC redirect + callback
                        .permitAll()
                    .anyRequest()
                        .authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/api/form/login")
                    .usernameParameter("userId")
                    .passwordParameter("password")
                    .successHandler(customAuthenticationSuccessHandler)
                    .failureHandler(customAuthenticationFailureHandler)
                    .and()
                .oauth2Login(oauth -> oauth
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                                .oidcUserService(this.oidcUserService()))
                        .failureHandler((request, response, exception) -> {
                            log.error("==== [OIDC LOGIN FAILURE] ====");
                            log.error("Exception: {}", exception.getMessage(), exception);
                        })
                        .loginPage("/login")
                        .defaultSuccessUrl("/home"))

                .logout(logout -> logout
                        .logoutSuccessHandler((request, response, authentication) -> {
                            log.info("==== [LOGOUT SUCCESS] ====");
                            response.sendRedirect("/login");
                        })
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutSuccessHandler(oidcLogoutSuccessHandler()))
                .httpBasic().disable();               // HTTP Basic 인증 비활성화


    }

    // Discovery
    //→ OP가 /.well-known/openid-configuration 에 자기 설정(JSON) 공개해 두고
    //→ 클라이언트는 issuer-uri만 넣어서 엔드포인트들을 자동으로 가져오는 방식.
    // keycloak.com/.well-known/openid-configuration
    // Authorization / Token / UserInfo / JWK / Logout 등 엔드포인트 주소를 전부 여기서 한 번에 알려줌.
    // Session Management
    //→ “로그인/로그아웃/SSO 세션 상태를 OP ↔ 클라이언트 간에 어떻게 동기화할지”를 정의한 OIDC 확장 스펙들.



    // Back-Channel Logout 로그아웃 구현 필요.

    private LogoutSuccessHandler oidcLogoutSuccessHandler() {
        OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler =
                new OidcClientInitiatedLogoutSuccessHandler(this.clientRegistrationRepository);

        // Sets the `URI` that the End-User's User Agent will be redirected to
        // after the logout has been performed at the Provider
        oidcLogoutSuccessHandler.setPostLogoutRedirectUri(URI.create("https://localhost:8080"));

        return oidcLogoutSuccessHandler;
    }


    private OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        // UserInfo 요청 전 처리(pre-processing)나 UserInfo 응답 후 처리(post-handling)를 커스터마이징
        log.info("호출!!!!");
        final OidcUserService delegate = new OidcUserService();

        return (userRequest) -> {

            // 기본 구현체(delegate)에 사용자 로딩을 위임
            OidcUser oidcUser = delegate.loadUser(userRequest);

            OAuth2AccessToken accessToken = userRequest.getAccessToken();
            OidcIdToken idToken = userRequest.getIdToken();
            log.info("accessToken: {}", accessToken);
            log.info("idToken: {}", idToken);



            User userInfo = userRepository.findByEmail(oidcUser.getEmail()).orElseThrow(() -> new RuntimeException("회원등록해라잇"));
            List<PrivilegeInfo> privilegeInfos = userInfo.getPrivilegeInfos();

            Set<GrantedAuthority> authoritySet= privilegeInfos.stream()
                    .map(p -> new SimpleGrantedAuthority(p.getName()))
                    .collect(Collectors.toSet());

            List<String> authorityList = privilegeInfos.stream()
                    .map(p -> p.getName())
                    .collect(Collectors.toList());

            // 3) 기존 oidcUser 를 복사하되, authorities 만 mappedAuthorities 로 교체한 새 객체 생성
            oidcUser = new DefaultOidcUser(
                    authoritySet,
                    oidcUser.getIdToken(),
                    oidcUser.getUserInfo()
            );

            LoginUser loginUser = new LoginUser(userInfo.getName(), authorityList);

            new WrappedOidcUser()

            return oidcUser;
        };
    }


    @Bean
    public JwtDecoderFactory<ClientRegistration> idTokenDecoderFactory() {
        // resolver는 전달받은 ClientRegistration 을 기준으로 어떤 알고리즘을 반환할지 분기해서 결정
        // 서명 검증 factory
        OidcIdTokenDecoderFactory idTokenDecoderFactory = new OidcIdTokenDecoderFactory();
        idTokenDecoderFactory.setJwsAlgorithmResolver(clientRegistration -> SignatureAlgorithm.RS256);
        return idTokenDecoderFactory;
    }



}
