package com.revolsys.io.map;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import com.revolsys.collection.json.JsonObject;
import com.revolsys.collection.json.JsonObjectHash;
import com.revolsys.collection.map.MapEx;
import com.revolsys.exception.Exceptions;

public class InvokeConstructorMapObjectFactory extends AbstractMapObjectFactory
  implements MapSerializer {

  private final Class<?> typeClass;

  private Constructor<?> constructor;

  public InvokeConstructorMapObjectFactory(final String typeName, final String description,
    final Class<?> typeClass) {
    super(typeName, description);
    this.typeClass = typeClass;
    try {
      this.constructor = typeClass.getConstructor(Map.class);
    } catch (final NoSuchMethodException e) {
      throw Exceptions.wrap("Factory must have a Map<String, ? extends Object> constructor", e);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public <V> V mapToObject(final MapEx properties) {
    try {
      return (V)this.constructor.newInstance(properties);
    } catch (InstantiationException | IllegalArgumentException | IllegalAccessException e) {
      return Exceptions.throwUncheckedException(e);
    } catch (final InvocationTargetException e) {
      final Throwable targetException = e.getTargetException();
      return Exceptions.throwUncheckedException(targetException);
    }
  }

  @Override
  public JsonObject toMap() {
    final JsonObject map = new JsonObjectHash();
    map.put("typeName", getTypeName());
    map.put("description", getDescription());
    map.put("typeClass", this.typeClass);
    return map;
  }
}
