package jm.diamond.filter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.apache.commons.io.IOUtils;

// todo : inputStream, byte[], apache Common IO, servletInputStream, byteArrayInputStream, httpMessageConverter
// todo :  HttpServletRequestWrapper를 커스텀한 이유
//  ServletInputStream은 단방향 스트림으로 설계되었있기 때문에 캐싱해서 여러번 사용가능 하도록 만든다.
public class CustomHttpServletRequestWrapper extends HttpServletRequestWrapper {

   private final byte[] cachedBytes;

   public CustomHttpServletRequestWrapper(HttpServletRequest request) throws IOException{
      super(request);
      InputStream inputStream = super.getInputStream();
      cachedBytes = IOUtils.toByteArray(inputStream);
   }

   @Override
   public ServletInputStream getInputStream() throws IOException {

      ByteArrayInputStream inputStream = new ByteArrayInputStream(cachedBytes);

      return new ServletInputStream() {
         @Override
         public boolean isFinished() {
            return inputStream.available() == 0;
         }

         @Override
         public boolean isReady() {
            // 읽을 때 blocking되는 케이스가 있는지?
            return true;
         }

         @Override
         public void setReadListener(ReadListener listener) {
            // 오버라이드할 때 모든 메소드를 사용하고 싶지 않을 때 사용.
            throw new UnsupportedOperationException();
         }

         @Override
         public int read() {
            return inputStream.read();
         }
      };
   }
}
