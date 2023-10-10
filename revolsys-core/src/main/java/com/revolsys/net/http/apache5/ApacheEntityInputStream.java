package com.revolsys.net.http.apache5;

import java.io.IOException;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.HttpEntity;
import org.jeometry.common.util.BaseCloseable;

import com.revolsys.io.DelegatingInputStream;

public class ApacheEntityInputStream extends DelegatingInputStream {

  private final CloseableHttpClient client;

  public ApacheEntityInputStream(final CloseableHttpClient client, final HttpEntity entity)
    throws IOException {
    super(entity.getContent());
    this.client = client;
  }

  @Override
  public void close() throws IOException {
    try {
      super.close();
    } finally {
      BaseCloseable.closeSilent(this.client);
    }
  }
}
