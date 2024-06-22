package jm.diamond.filter;

import java.io.IOException;
import java.util.UUID;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;


/**
 * https://beaniejoy.tistory.com/97 https://beaniejoy.tistory.com/96 why oncePerRequestFilter 1.
 * forward 처리 2. mvc 에러페이지 처리 중복 호출되는것을 막기 위해서. why filter 1. 가장먼저 request를 받고 가장마지막에 response를 처리할
 * 수 있어서. 2. 요청과 응답의 header 조작이 가능
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE) // security filter보다 먼저 실행하기 위해서
public class HttpLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
        HttpServletResponse httpServletResponse, FilterChain filterChain)
        throws ServletException, IOException {
        long startTime = System.currentTimeMillis();

        // todo : ReadableRequestBodyWrapper와 ContentCachingResponseWrapper를 사용하는 케이스가 있음.
        ContentCachingRequestWrapper contentCachingRequestWrapper = new ContentCachingRequestWrapper(
            httpServletRequest);
        ContentCachingResponseWrapper contentCachingResponseWrapper = new ContentCachingResponseWrapper(
            httpServletResponse);

        HttpLogMessage httpLogMessage = HttpLogMessage.builder()
            .clientIp(httpServletRequest.getRequestURI())
            .httpMethod(HttpMethod.valueOf(httpServletRequest.getMethod()))
            .build();
        /**
         * todo : proxy_set_header 를 사용해서 nginx에서 들어온 요청에 대한 id를 톰켓에 넘겨주는 것도 방법
         (HttpServletRequest)servletRequest).getHeader("X-RequestID");
         출처: https://mangkyu.tistory.com/266 [MangKyu's Diary:티스토리]
         */
        // mdc 적용, thread local에 접근해서 req id 공유가능.
        UUID uuid = UUID.randomUUID();
        MDC.put("request_id", uuid.toString());
        log.info("req print : {}", httpLogMessage);

        filterChain.doFilter(contentCachingRequestWrapper, contentCachingResponseWrapper);

        long endTime = System.currentTimeMillis();
        HttpLogMessage httpLogMessage2 = HttpLogMessage.builder()
            .clientIp(httpServletRequest.getRequestURI())
            .httpMethod(HttpMethod.valueOf(httpServletRequest.getMethod()))
            .elapsedTime((endTime - startTime) / 1000.0)
            .build();

        log.info("res print : {}", httpLogMessage2);

        // todo : why copyBodyToResponse
        contentCachingResponseWrapper.copyBodyToResponse();
        // thread local 초기화.
        MDC.clear();
    }
}
