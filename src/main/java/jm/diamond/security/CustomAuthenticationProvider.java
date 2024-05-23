package jm.diamond.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {

   // https://assu10.github.io/dev/2023/12/16/springsecurity-filter/
   private final CustomUserDetailService customUserDetailService;

   @Override
   public Authentication authenticate(Authentication authentication)
       throws AuthenticationException {

      UserDetails userDetails = customUserDetailService.loadUserByUsername(
          authentication.getName());

      String reqPassword = authentication.getCredentials().toString();

      // 패스워드 검증로직 추가

      UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
          userDetails, userDetails.getPassword(), userDetails.getAuthorities());

      return usernamePasswordAuthenticationToken;
   }

   @Override
   public boolean supports(Class<?> aClass) {
      return false;
   }
}
