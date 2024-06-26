package jm.diamond.filter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
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

      try {

         /** request body는  inputStream은 1회성
          * ContentCachingRequestWrapper를 사용하지않은 이유
          * 캐싱될려면 한번은 읽혀야하는데(getIputStream)
          * controller에서 parameter매핑할때 getInputStream으로  데이터를 읽을 수 없게 된다.
          * controller에서 getContentAsByteArray()를 사용하게 할수 있나?
          * 그래서 HttpServletRequestWrapper를 상속한 클래스를 만들어서 getInputStream()호출 시 캐시된
          * 데이터를 리턴하도록 만든다.*/
         ContentCachingResponseWrapper contentCachingResponseWrapper = new ContentCachingResponseWrapper(
             httpServletResponse);

         CustomHttpServletRequestWrapper customReq = new CustomHttpServletRequestWrapper(
             httpServletRequest);


         long startTime = System.currentTimeMillis();
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
         printRequestBody(customReq);

         filterChain.doFilter(customReq, contentCachingResponseWrapper);

         long endTime = System.currentTimeMillis();
         HttpLogMessage httpLogMessage2 = HttpLogMessage.builder()
             .clientIp(httpServletRequest.getRequestURI())
             .httpMethod(HttpMethod.valueOf(httpServletRequest.getMethod()))
             .elapsedTime((endTime - startTime) / 1000.0)
             .build();

         log.info("res print : {}", httpLogMessage2);

         printResponseBody(contentCachingResponseWrapper);
         // why copyBodyToResponse  body값을 copy해서 캐시로 저장해두기 때문에 다시 읽을 수 있다.
         contentCachingResponseWrapper.copyBodyToResponse();
      } finally {
         // thread local의 request id 초기화
         MDC.remove("request_id");
      }
   }

   private void printRequestBody(CustomHttpServletRequestWrapper customReq) {
      String body = "[EMPTY]";
      String requestBody = getRequestBody(customReq);
      if (!StringUtils.isEmpty(requestBody)) {
         final Gson gson = new GsonBuilder().setPrettyPrinting().create();
         body = gson.toJson(gson.fromJson(requestBody, JsonObject.class));
      }
      log.info("request Body ::: {}", body);
   }

   private String getRequestBody(CustomHttpServletRequestWrapper customReq) {
      String requestBody = "";

      try {
         requestBody = IOUtils.toString(customReq.getInputStream(),
             StandardCharsets.UTF_8);
      } catch (IOException e) {
         log.warn("can not read request body: {}", e.getMessage(), e);
      }

      return requestBody;

   }

   private void printResponseBody(ContentCachingResponseWrapper response){
      String json = "[EMPTY]";
      byte[] buf = response.getContentAsByteArray();
      if (buf.length != 0) {
         try {
            json = IOUtils.toString(buf, StandardCharsets.UTF_8.name());
            final Gson gson = new GsonBuilder().setPrettyPrinting().create();
//            json = new String(buf, 0, buf.length, StandardCharsets.UTF_8);
            json = System.lineSeparator() + gson.toJson(gson.fromJson(json, JsonObject.class));
         } catch (JsonSyntaxException e) {
            json = "[NOT JSON]";
         }
      }
      log.info("Response Body: {}", json);
   }
}
