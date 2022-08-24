package com.revolsys.core.reactive;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Test;

import com.revolsys.reactive.ReactiveByteBuf;
import com.revolsys.util.Debug;

import io.netty.buffer.ByteBuf;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ReactiveByteBufTest {
  private static final byte[] testData = new byte[100 * 8192 + 55];

  static {
    for (int i = 0; i < testData.length; i++) {
      testData[i] = (byte)(int)(Math.random() * 255);
    }

  }

  private void assertCollect(final Flux<ByteBuf> flux) {
    final int length = testData.length;
    ReactiveByteBuf.collect(length, flux).subscribe(data -> {
      assertTestData(data);
    });
  }

  private void assertTestData(final ByteBuffer data) {
    data.flip();
    for (int i = 0; i < testData.length; i++) {
      try {
        final byte expected = testData[i];
        final byte actual = data.get();
        Assert.assertEquals("index=" + i, expected, actual);
      } catch (final Exception e) {
        throw new IllegalStateException("Index=" + i, e);
      }
    }
  }

  @Test
  public void test_asByteBuf_bytea() {
    final Flux<ByteBuf> flux = ReactiveByteBuf.read(testData);
    assertCollect(flux);
  }

  @Test
  public void test_asByteBuf_InputStream() {
    final ByteArrayInputStream in = new ByteArrayInputStream(testData);
    final Flux<ByteBuf> flux = ReactiveByteBuf.read(in);
    assertCollect(flux);
  }

  @Test
  public void test_split() {
    final Flux<ByteBuf> source = ReactiveByteBuf.read(testData);
    final int blockSize = 8192 * 10;
    final ByteBuffer allData = ByteBuffer.allocate(testData.length);

    ReactiveByteBuf//
      .split(source, blockSize)
      .flatMap(data -> {
        final Mono<Long> r = Flux.from(data).reduce(0L, (count, buffer) -> {
          final int readableBytes = buffer.readableBytes();
          allData.limit(allData.position() + readableBytes);
          buffer.readBytes(allData);
          buffer.release();
          final long total = count + readableBytes;
          // System.out.println(total);
          return total;
        })
        // .doOnNext(count -> System.out.println(count))
        ;
        return r;
      })
      .reduce(0L, (t, c) -> t + c)
      .subscribe(count -> Assert.assertEquals("total", testData.length, count.intValue()));
    assertTestData(allData);
    Debug.noOp();
  }
}
