package com.revolsys.reactive;

import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

public class ReactiveSchedulers {
  private static Scheduler blocking;

  private static Scheduler task;

  private static Scheduler nonBlocking;

  private static Scheduler limit;

  public static Scheduler blocking() {
    if (blocking == null) {
      blocking = Schedulers.boundedElastic();
    }
    return blocking;
  }

  public static Scheduler limit() {
    if (limit == null) {
      synchronized (ReactiveSchedulers.class) {
        if (limit == null) {
          final int parallelLimit = limitDefaultParallelLimit();
          final int queueSize = limitDefaultQueueSize();
          return ReactiveSchedulers.newLimit(parallelLimit, queueSize);
        }

      }
    }
    return limit;
  }

  public static int limitDefaultParallelLimit() {
    final Runtime runtime = Runtime.getRuntime();
    final int size = runtime.availableProcessors();
    long maxMemory = runtime.maxMemory();
    if (maxMemory == Long.MAX_VALUE) {
      maxMemory = 8527020032L; // 8GB
    }
    final int sizeByMemory = (int)Math.floorDiv(maxMemory, 1 * 1024 * 1204 * 1024);
    final int cpuCountEstimate = Math.floorDiv(size, 2);
    return Math.min(cpuCountEstimate, sizeByMemory);
  }

  public static int limitDefaultQueueSize() {
    final int queueSize = limitDefaultParallelLimit() * 100;
    return queueSize;
  }

  public static LimitScheduler newLimit(final int paralellLimit, final int queueSize) {
    final Scheduler scheduler = ReactiveSchedulers.blocking();
    return newLimit(scheduler, paralellLimit, queueSize);
  }

  public static LimitScheduler newLimit(final Scheduler scheduler, final int paralellLimit,
    final int queueSize) {
    final LimitScheduler limitScheduler = new LimitScheduler(scheduler, paralellLimit, queueSize);
    limitScheduler.start();
    return limitScheduler;
  }

  public static Scheduler nonBlocking() {
    if (nonBlocking == null) {
      nonBlocking = Schedulers.parallel();
    }
    return nonBlocking;
  }

  public static Scheduler task() {
    if (task == null) {
      task = Schedulers.boundedElastic();
    }
    return task;
  }

}
