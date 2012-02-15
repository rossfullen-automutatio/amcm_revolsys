package com.revolsys.gis.data.model.types;

import javax.xml.namespace.QName;

public class CollectionDataType extends SimpleDataType {

  private final DataType contentType;

  public CollectionDataType(final QName name, final Class<?> javaClass,
    final DataType contentType) {
    super(name, javaClass);
    this.contentType = contentType;
  }

  public CollectionDataType(final String name, final Class<?> javaClass,
    final DataType contentType) {
    super(name, javaClass);
    this.contentType = contentType;
  }

  public DataType getContentType() {
    return contentType;
  }

}
