package jm.diamond.filter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.apache.commons.io.IOUtils;

// todo : inputStream, byte[], apache Common IO, servletInputStream, byteArrayInputStream, httpMessageConverter
// inputStream
//
// todo :  HttpServletRequestWrapper를 커스텀한 이유
public class CustomHttpServletRequestWrapper extends HttpServletRequestWrapper {

   private final byte[] bytes;
   private final String requestBody;

   public CustomHttpServletRequestWrapper(HttpServletRequest request) throws IOException{
      super(request);

      InputStream inputStream = super.getInputStream();
      bytes = IOUtils.toByteArray(inputStream);
      requestBody = new String(bytes, StandardCharsets.UTF_8);
   }

   @Override
   public ServletInputStream getInputStream() throws IOException {

      ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);

      return new ServletInputStream() {
         @Override
         public boolean isFinished() {
            return inputStream.available() == 0;
         }

         @Override
         public boolean isReady() {
            return true;
         }

         @Override
         public void setReadListener(ReadListener listener) {
            throw new UnsupportedOperationException();
         }

         @Override
         public int read() {
            return inputStream.read();
         }
      };
   }
}
