package com.revolsys.gis.data.io;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.namespace.QName;

import com.revolsys.collection.AbstractIterator;
import com.revolsys.gis.cs.BoundingBox;
import com.revolsys.gis.data.model.DataObject;
import com.revolsys.gis.data.model.DataObjectMetaData;
import com.revolsys.io.AbstractMultipleIteratorReader;

public class DataObjectStoreQueryReader extends
  AbstractMultipleIteratorReader<DataObject> implements DataObjectReader {

  private AbstractDataObjectStore dataStore;

  private final List<Query> queries = new ArrayList<Query>();

  private BoundingBox boundingBox;

  private List<QName> typeNames;

  private String whereClause;

  private int queryIndex = 0;

  public DataObjectStoreQueryReader() {
  }

  public DataObjectStoreQueryReader(final AbstractDataObjectStore dataStore) {
    this.dataStore = dataStore;
  }

  public void addQuery(final QName typeName, final String query) {
    addQuery(new Query(typeName, query));
  }

  public void addQuery(final QName typeName, final String query,
    final List<Object> parameters) {
    addQuery(new Query(typeName, query, parameters));
  }

  public void addQuery(final QName typeName, final String query,
    final Object... parameters) {
    addQuery(typeName, query, Arrays.asList(parameters));
  }

  public void addQuery(final Query query) {
    queries.add(query);
  }

  public AbstractDataObjectStore getDataStore() {
    return dataStore;
  }

  public DataObjectMetaData getMetaData() {
    return ((DataObjectIterator)iterator()).getMetaData();
  }

  @Override
  protected AbstractIterator<DataObject> getNextIterator() {
    if (queryIndex < queries.size()) {
      final Query query = queries.get(queryIndex);
      final AbstractIterator<DataObject> iterator = dataStore.createIterator(
        query, getProperties());
      queryIndex++;
      return iterator;
    }
    return null;
  }

  public String getWhereClause() {
    return whereClause;
  }

  @Override
  public void open() {
    if (typeNames != null) {
      for (final QName tableName : typeNames) {
        final DataObjectMetaData metaData = dataStore.getMetaData(tableName);
        if (metaData != null) {
          final Query query;
          if (boundingBox == null) {
            query = new Query(metaData);
            query.setWhereClause(whereClause);
          } else {
            final QName typeName = metaData.getName();
            query = dataStore.createQuery(typeName, whereClause, boundingBox);
          }
          addQuery(query);
        }
      }
    }
  }

  public void setBoundingBox(final BoundingBox boundingBox) {
    this.boundingBox = boundingBox;
  }

  public void setDataStore(final AbstractDataObjectStore dataStore) {
    this.dataStore = dataStore;
  }

  /**
   * @param queries the queries to set
   */
  public void setQueries(final List<Query> queries) {
    this.queries.clear();
    for (final Query query : queries) {
      addQuery(query);
    }
  }

  /**
   * @param typeNames the typeNames to set
   */
  public void setTypeNames(final List<QName> typeNames) {
    this.typeNames = typeNames;

  }

  public void setWhereClause(final String whereClause) {
    this.whereClause = whereClause;
  }
}
