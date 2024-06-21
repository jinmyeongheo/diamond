package jm.diamond.filter;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

@Builder
@Getter
public class HttpLogMessage {

   private HttpMethod httpMethod;
   private HttpStatus httpStatus;
   private String requestUri;
   private String clientIp;
   private String headers;
   private String requestParam;
   private String requestBody;
   private String responseBody;
   private Double elapsedTime;

   @Override
   public String toString() {
      return "HttpLogMessage{" +
          "httpMethod=" + httpMethod +
          ", httpStatus=" + httpStatus +
          ", requestUri='" + requestUri + '\'' +
          ", clientIp='" + clientIp + '\'' +
          ", headers='" + headers + '\'' +
          ", requestParam='" + requestParam + '\'' +
          ", requestBody='" + requestBody + '\'' +
          ", responseBody='" + responseBody + '\'' +
          ", elapsedTime=" + elapsedTime +
          '}';
   }
}
