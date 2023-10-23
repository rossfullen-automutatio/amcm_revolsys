package com.revolsys.function;

@FunctionalInterface
public interface Function4Double<R> {
  R accept(double parameter1, double parameter2, double parameter3, double parameter4);
}
