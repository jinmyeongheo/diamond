package jm.diamond.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.springframework.stereotype.Component;


public class AFilter implements Filter {

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    System.out.println("A필터 초기화!!!!");
  }

  @Override
  public void destroy() {
    System.out.println("A필터 파괴!!!!");
  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
      FilterChain filterChain) throws IOException, ServletException {
    System.out.println("Request >>> 필터 A !!!!!!!!!!!");
    filterChain.doFilter(servletRequest,servletResponse);
    System.out.println("Response <<< 필터 A !!!!!!!!!!!");

  }
}
