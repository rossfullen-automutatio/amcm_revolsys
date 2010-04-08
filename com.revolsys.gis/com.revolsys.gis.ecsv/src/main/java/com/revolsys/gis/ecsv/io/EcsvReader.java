package com.revolsys.gis.ecsv.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

import com.revolsys.gis.data.io.AbstractReader;
import com.revolsys.gis.data.io.DataObjectReader;
import com.revolsys.gis.data.model.DataObject;
import com.revolsys.gis.data.model.DataObjectFactory;
import com.revolsys.gis.data.model.DataObjectMetaData;
import com.revolsys.io.FileUtil;
import com.vividsolutions.jts.geom.GeometryFactory;

public class EcsvReader extends AbstractReader<DataObject> implements
  DataObjectReader {
  public static DataObjectMetaData getMetaData(
    final InputStream in) {
    final EcsvDataObjectReaderFactory ecsvDataObjectReaderFactory = EcsvDataObjectReaderFactory.INSTANCE;
    final EcsvReader reader = (EcsvReader)ecsvDataObjectReaderFactory.createDataObjectReader(in);
    final DataObjectMetaData metaData = reader.getMetaData();
    reader.close();
    return metaData;
  }

  public static DataObjectMetaData getMetaData(
    final String resourceName) {
    final InputStream in = EcsvReader.class.getResourceAsStream(resourceName);
    return getMetaData(in);
  }

  private final DataObjectFactory dataObjectFactory;

  private GeometryFactory geometryFactory = new GeometryFactory();

  private final Reader in;

  private EcsvIterator iterator;

  public EcsvReader(
    final InputStream in,
    final DataObjectFactory dataObjectFactory) {
    this.in = new InputStreamReader(in, EcsvConstants.CHARACTER_SET);
    this.dataObjectFactory = dataObjectFactory;
  }

  public EcsvReader(
    final Reader in,
    final DataObjectFactory dataObjectFactory) {
    this.in = in;
    this.dataObjectFactory = dataObjectFactory;
  }

  public void close() {
    FileUtil.closeSilent(in);
  }

  public DataObjectFactory getDataObjectFactory() {
    return dataObjectFactory;
  }

  public GeometryFactory getGeometryFactory() {
    return geometryFactory;
  }

  public DataObjectMetaData getMetaData() {
    return iterator().getMetaData();
  }

  @Override
  public Map<String, Object> getProperties() {
    return iterator().getProperties();
  }

  public EcsvIterator iterator() {
    if (iterator == null) {
      try {
        iterator = new EcsvIterator(in, geometryFactory, dataObjectFactory);
      } catch (final IOException e) {
        throw new IllegalArgumentException("Unable to create Iterator:"
          + e.getMessage(), e);
      }
    }
    return iterator;
  }

  public void setGeometryFactory(
    final GeometryFactory geometryFactory) {
    this.geometryFactory = geometryFactory;
  }
}
