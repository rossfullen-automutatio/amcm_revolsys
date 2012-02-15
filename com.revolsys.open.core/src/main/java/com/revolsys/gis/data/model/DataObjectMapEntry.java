package com.revolsys.gis.data.model;

import java.util.Map.Entry;

public class DataObjectMapEntry implements Entry<String, Object> {
  private final DataObject object;

  private final int index;

  public DataObjectMapEntry(final DataObject object, final int index) {
    this.object = object;
    this.index = index;
  }

  public String getKey() {
    final DataObjectMetaData metaData = object.getMetaData();
    return metaData.getAttributeName(index);
  }

  public Object getValue() {
    return object.getValue(index);
  }

  public Object setValue(final Object value) {
    object.setValue(index, value);
    return value;
  }
}
