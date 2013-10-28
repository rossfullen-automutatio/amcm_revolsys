package com.revolsys.collection;

import java.util.Enumeration;
import java.util.Iterator;

public class IteratorEnumeration<T> implements Enumeration<T> {

  @SuppressWarnings("unchecked")
  public static <V> Enumeration<V> create(final Iterable<V> iterable) {
    if (iterable instanceof Enumeration) {
      return (Enumeration<V>)iterable;
    } else {
      return create(iterable.iterator());
    }
  }

  @SuppressWarnings("unchecked")
  public static <V> Enumeration<V> create(final Iterator<V> iterator) {
    if (iterator instanceof Enumeration) {
      return (Enumeration<V>)iterator;
    } else {
      return new IteratorEnumeration<V>(iterator);
    }
  }

  private final Iterator<T> iterator;

  public IteratorEnumeration(final Iterator<T> iterator) {
    this.iterator = iterator;
  }

  @Override
  public boolean hasMoreElements() {
    return iterator.hasNext();
  }

  @Override
  public T nextElement() {
    return iterator.next();
  }
}
