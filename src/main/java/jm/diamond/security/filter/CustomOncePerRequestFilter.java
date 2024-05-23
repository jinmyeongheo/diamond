package jm.diamond.security.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Slf4j
public class CustomOncePerRequestFilter extends OncePerRequestFilter {
   //https://assu10.github.io/dev/2023/12/16/springsecurity-filter/#4-%ED%95%84%ED%84%B0-%EC%B2%B4%EC%9D%B8%EC%9D%98-%EB%8B%A4%EB%A5%B8-%ED%95%84%ED%84%B0-%EC%9C%84%EC%B9%98%EC%97%90-%ED%95%84%ED%84%B0-%EC%B6%94%EA%B0%80
   @Override
   protected void doFilterInternal(HttpServletRequest httpServletRequest,
       HttpServletResponse httpServletResponse, FilterChain filterChain)
      throws ServletException, IOException {
      log.info("한번만 실행하는 필터");
      filterChain.doFilter(httpServletRequest,httpServletResponse);
   }
}
