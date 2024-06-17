package jm.diamond.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider{

    private final CustomUserDetailService customUserDetailService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication)
        throws AuthenticationException {

        UserDetails userDetails = customUserDetailService.loadUserByUsername(
            authentication.getName());

        String reqPassword = authentication.getCredentials().toString();

        if(!passwordEncoder.matches(userDetails.getPassword(), reqPassword)){
            throw new BadCredentialsException("");
        }

        /**
         *  **Principal**은 인증된 사용자의 신원을 나타내는 객체로, 주로 사용자 이름이나 ID를 포함합니다.
         *  **Credential**은 사용자가 제공하는 증명 정보로, 주로 비밀번호와 같은 인증에 필요한 정보를 포함합니다.
         *  */

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
            new UsernamePasswordAuthenticationToken(
                userDetails,
                userDetails.getPassword(),
                userDetails.getAuthorities());

        return usernamePasswordAuthenticationToken;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}
