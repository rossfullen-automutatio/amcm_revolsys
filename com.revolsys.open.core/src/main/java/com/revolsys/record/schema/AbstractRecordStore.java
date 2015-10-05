package com.revolsys.record.schema;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.revolsys.collection.map.Maps;
import com.revolsys.geometry.model.GeometryFactory;
import com.revolsys.gis.io.Statistics;
import com.revolsys.gis.io.StatisticsMap;
import com.revolsys.io.PathName;
import com.revolsys.jdbc.io.RecordStoreIteratorFactory;
import com.revolsys.properties.BaseObjectWithProperties;
import com.revolsys.record.ArrayRecordFactory;
import com.revolsys.record.RecordFactory;
import com.revolsys.record.code.CodeTable;
import com.revolsys.record.io.RecordReader;
import com.revolsys.record.io.RecordStoreExtension;
import com.revolsys.record.property.RecordDefinitionProperty;
import com.revolsys.util.ExceptionUtil;
import com.revolsys.util.Property;

public abstract class AbstractRecordStore extends BaseObjectWithProperties implements RecordStore {
  private Map<String, List<String>> codeTableColumNames = new HashMap<String, List<String>>();

  private final Map<String, CodeTable> columnToTableMap = new HashMap<String, CodeTable>();

  private List<RecordDefinitionProperty> commonRecordDefinitionProperties = new ArrayList<RecordDefinitionProperty>();

  private Map<String, Object> connectionProperties = new HashMap<>();

  private GeometryFactory geometryFactory;

  private RecordStoreIteratorFactory iteratorFactory = new RecordStoreIteratorFactory();

  private String label;

  private boolean loadFullSchema = true;

  private RecordFactory recordFactory;

  private final Set<RecordStoreExtension> recordStoreExtensions = new LinkedHashSet<RecordStoreExtension>();

  private final RecordStoreSchema rootSchema = new RecordStoreSchema(this);

  private final StatisticsMap statistics = new StatisticsMap();

  private final Map<String, Map<String, Object>> typeRecordDefinitionProperties = new HashMap<String, Map<String, Object>>();

  public AbstractRecordStore() {
    this(new ArrayRecordFactory());
  }

  public AbstractRecordStore(final RecordFactory recordFactory) {
    this.recordFactory = recordFactory;
  }

  @Override
  public void addCodeTable(final CodeTable codeTable) {
    final String idColumn = codeTable.getIdFieldName();
    addCodeTable(idColumn, codeTable);
    final List<String> fieldAliases = codeTable.getFieldAliases();
    for (final String alias : fieldAliases) {
      addCodeTable(alias, codeTable);
    }
    final String codeTableName = codeTable.getName();
    final List<String> columnNames = this.codeTableColumNames.get(codeTableName);
    if (columnNames != null) {
      for (final String columnName : columnNames) {
        addCodeTable(columnName, codeTable);
      }
    }
  }

  public void addCodeTable(final String columnName, final CodeTable codeTable) {
    if (columnName != null && !columnName.equalsIgnoreCase("ID")) {
      this.columnToTableMap.put(columnName, codeTable);
      final RecordStoreSchema rootSchema = getRootSchema();
      addCodeTableColumns(rootSchema, codeTable, columnName);
    }
  }

  protected void addCodeTableColumns(final RecordStoreSchema schema, final CodeTable codeTable,
    final String columnName) {
    if (schema.isInitialized()) {
      for (final RecordStoreSchema childSchema : schema.getSchemas()) {
        addCodeTableColumns(childSchema, codeTable, columnName);
      }
      for (final RecordDefinition recordDefinition : schema.getRecordDefinitions()) {
        final String idFieldName = recordDefinition.getIdFieldName();
        for (final FieldDefinition field : recordDefinition.getFields()) {
          final String fieldName = field.getName();
          if (fieldName.equals(columnName) && !fieldName.equals(idFieldName)) {
            field.setCodeTable(codeTable);
          }
        }
      }
    }
  }

  protected void addRecordDefinition(final RecordDefinition recordDefinition) {
    final String idFieldName = recordDefinition.getIdFieldName();
    for (final FieldDefinition field : recordDefinition.getFields()) {
      final String fieldName = field.getName();
      if (!fieldName.equals(idFieldName)) {
        final CodeTable codeTable = getCodeTableByFieldName(fieldName);
        if (codeTable != null) {
          field.setCodeTable(codeTable);
        }
      }
    }
  }

  protected void addRecordDefinitionProperties(final RecordDefinitionImpl recordDefinition) {
    final String typePath = recordDefinition.getPath();
    for (final RecordDefinitionProperty property : this.commonRecordDefinitionProperties) {
      final RecordDefinitionProperty clonedProperty = property.clone();
      clonedProperty.setRecordDefinition(recordDefinition);
    }
    final Map<String, Object> properties = this.typeRecordDefinitionProperties.get(typePath);
    recordDefinition.setProperties(properties);
  }

  public void addRecordStoreExtension(final RecordStoreExtension extension) {
    if (extension != null) {
      try {
        final Map<String, Object> connectionProperties = getConnectionProperties();
        extension.initialize(this, connectionProperties);
        this.recordStoreExtensions.add(extension);
      } catch (final Throwable e) {
        ExceptionUtil.log(extension.getClass(), "Unable to initialize", e);
      }
    }
  }

  @Override
  @PreDestroy
  public void close() {
    try {
      super.close();
      if (this.statistics != null) {
        this.statistics.disconnect();
      }
      getRootSchema().close();
    } finally {
      this.codeTableColumNames.clear();
      this.columnToTableMap.clear();
      this.commonRecordDefinitionProperties.clear();
      this.connectionProperties.clear();
      this.recordFactory = null;
      this.recordStoreExtensions.clear();
      this.iteratorFactory = null;
      this.label = "deleted";
      this.statistics.clear();
      this.typeRecordDefinitionProperties.clear();
    }
  }

  @Override
  public CodeTable getCodeTableByFieldName(final String columnName) {
    final CodeTable codeTable = this.columnToTableMap.get(columnName);
    return codeTable;
  }

  @Override
  public Map<String, CodeTable> getCodeTableByFieldNameMap() {
    return new HashMap<String, CodeTable>(this.columnToTableMap);
  }

  public Map<String, List<String>> getCodeTableColumNames() {
    return this.codeTableColumNames;
  }

  @Override
  public RecordStoreConnected getConnected() {
    return new RecordStoreConnected(this);
  }

  protected Map<String, Object> getConnectionProperties() {
    return this.connectionProperties;
  }

  @Override
  public GeometryFactory getGeometryFactory() {
    return this.geometryFactory;
  }

  @Override
  public RecordStoreIteratorFactory getIteratorFactory() {
    return this.iteratorFactory;
  }

  @Override
  public String getLabel() {
    return this.label;
  }

  @Override
  public RecordFactory getRecordFactory() {
    return this.recordFactory;
  }

  public Collection<RecordStoreExtension> getRecordStoreExtensions() {
    return this.recordStoreExtensions;
  }

  @Override
  public RecordStoreSchema getRootSchema() {
    return this.rootSchema;
  }

  @Override
  public StatisticsMap getStatistics() {
    return this.statistics;
  }

  @Override
  public Statistics getStatistics(final String name) {
    final StatisticsMap statistics = getStatistics();
    return statistics.getStatistics(name);
  }

  @Override
  public String getUrl() {
    return (String)this.connectionProperties.get("url");
  }

  @Override
  public String getUsername() {
    return (String)this.connectionProperties.get("username");
  }

  @Override
  @PostConstruct
  public void initialize() {
    getStatistics().connect();
  }

  @Override
  public boolean isLoadFullSchema() {
    return this.loadFullSchema;
  }

  protected void obtainConnected() {
  }

  @Override
  public RecordReader query(final List<?> queries) {
    return query((Iterable<?>)queries);
  }

  protected RecordDefinition refreshRecordDefinition(final RecordStoreSchema schema,
    final PathName typePath) {
    return null;
  }

  protected void refreshSchema() {
    getRootSchema().refresh();
  }

  protected void refreshSchema(final PathName schemaName) {
    final RecordStoreSchema schema = getSchema(schemaName);
    if (schema != null) {
      schema.refresh();
    }
  }

  protected Map<PathName, ? extends RecordStoreSchemaElement> refreshSchemaElements(
    final RecordStoreSchema schema) {
    return Collections.emptyMap();
  }

  protected void releaseConnected() {
  }

  public void setCodeTableColumNames(final Map<String, List<String>> domainColumNames) {
    this.codeTableColumNames = domainColumNames;
  }

  public void setCommonRecordDefinitionProperties(
    final List<RecordDefinitionProperty> commonRecordDefinitionProperties) {
    this.commonRecordDefinitionProperties = commonRecordDefinitionProperties;
  }

  protected void setConnectionProperties(final Map<String, ? extends Object> connectionProperties) {
    this.connectionProperties = Maps.newHash(connectionProperties);
  }

  public void setGeometryFactory(final GeometryFactory geometryFactory) {
    this.geometryFactory = geometryFactory;
  }

  public void setIteratorFactory(final RecordStoreIteratorFactory iteratorFactory) {
    this.iteratorFactory = iteratorFactory;
  }

  @Override
  public void setLabel(final String label) {
    this.label = label;
    getStatistics().setPrefix(label);
  }

  @Override
  public void setLoadFullSchema(final boolean loadFullSchema) {
    this.loadFullSchema = loadFullSchema;
  }

  @Override
  public void setLogCounts(final boolean logCounts) {
    getStatistics().setLogCounts(logCounts);
  }

  @Override
  public void setRecordFactory(final RecordFactory recordFactory) {
    this.recordFactory = recordFactory;
  }

  public void setTypeRecordDefinitionProperties(
    final Map<String, List<RecordDefinitionProperty>> typeRecordDefinitionProperties) {
    for (final Entry<String, List<RecordDefinitionProperty>> typeProperties : typeRecordDefinitionProperties
      .entrySet()) {
      final String typePath = typeProperties.getKey();
      Map<String, Object> currentProperties = this.typeRecordDefinitionProperties.get(typePath);
      if (currentProperties == null) {
        currentProperties = new LinkedHashMap<>();
        this.typeRecordDefinitionProperties.put(typePath, currentProperties);
      }
      final List<RecordDefinitionProperty> properties = typeProperties.getValue();
      for (final RecordDefinitionProperty property : properties) {
        final String name = property.getPropertyName();
        currentProperties.put(name, property);
      }
    }
  }

  @Override
  public String toString() {
    if (Property.hasValue(this.label)) {
      return this.label;
    } else {
      return super.toString();
    }
  }
}
