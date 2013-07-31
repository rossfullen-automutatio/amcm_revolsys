package com.revolsys.gis.data.model;

public abstract class AbstractDataObjectMetaDataProperty implements
  DataObjectMetaDataProperty {
  private DataObjectMetaData metaData;

  @Override
  public AbstractDataObjectMetaDataProperty clone() {
    try {
      final AbstractDataObjectMetaDataProperty clone = (AbstractDataObjectMetaDataProperty)super.clone();
      clone.metaData = null;
      return clone;
    } catch (final CloneNotSupportedException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public DataObjectMetaData getMetaData() {
    return metaData;
  }

  @Override
  public void setMetaData(final DataObjectMetaData metaData) {
    if (this.metaData != null) {
      this.metaData.setProperty(getPropertyName(), null);
    }
    this.metaData = metaData;
    if (metaData != null) {
      metaData.setProperty(getPropertyName(), this);
    }
  }

  public String getTypePath() {
    return getMetaData().getPath();
  }

}
