package com.revolsys.parallel.channel;

public class Timer implements SelectableInput {
  private final long time;

  public Timer(final long time) {
    this.time = time;
  }

  public boolean disable() {
    return isTimeout();
  }

  public boolean enable(final MultiInputSelector alt) {
    return isTimeout();
  }

  public long getWaitTime() {
    final long waitTime = time - System.currentTimeMillis();
    return waitTime;
  }

  public boolean isClosed() {
    return false;
  }

  public boolean isTimeout() {
    final boolean timeout = System.currentTimeMillis() > time;
    return timeout;
  }
}
