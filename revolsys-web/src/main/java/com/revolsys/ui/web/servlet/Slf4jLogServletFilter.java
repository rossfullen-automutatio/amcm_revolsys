package com.revolsys.ui.web.servlet;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

public class Slf4jLogServletFilter implements Filter {
  @Override
  public void destroy() {
  }

  @Override
  public void doFilter(final ServletRequest request, final ServletResponse response,
    final FilterChain chain) throws IOException, ServletException {
    try {
      chain.doFilter(request, response);
    } catch (final IOException e) {
      throw e;
    } catch (final ServletException e) {
      throw e;
    } catch (final RuntimeException e) {
      HttpServletLogUtil.logRequestException(this, request, e);
      throw e;
    } catch (final Error e) {
      HttpServletLogUtil.logRequestException(this, request, e);
      throw e;
    }
  }

  @Override
  public void init(final FilterConfig filterConfig) throws ServletException {
  }
}
