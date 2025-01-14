package com.revolsys.record.code;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.revolsys.collection.json.JsonObject;
import com.revolsys.collection.list.Lists;
import com.revolsys.data.identifier.Identifier;
import com.revolsys.data.identifier.ListIdentifier;
import com.revolsys.data.identifier.SingleIdentifier;
import com.revolsys.date.Dates;
import com.revolsys.exception.Exceptions;
import com.revolsys.io.PathName;
import com.revolsys.record.Record;
import com.revolsys.record.comparator.RecordFieldComparator;
import com.revolsys.record.io.RecordReader;
import com.revolsys.record.query.And;
import com.revolsys.record.query.Q;
import com.revolsys.record.query.Query;
import com.revolsys.record.schema.FieldDefinition;
import com.revolsys.record.schema.RecordDefinition;
import com.revolsys.record.schema.RecordDefinitionProxy;
import com.revolsys.record.schema.RecordStore;
import com.revolsys.util.Property;
import com.revolsys.util.count.CategoryLabelCountMap;

public class MultiValueRecordStoreCodeTable extends AbstractMultiValueCodeTable
  implements RecordDefinitionProxy {

  private static final List<String> DEFAULT_FIELD_NAMES = Lists.newArray("VALUE");

  private boolean createMissingCodes = true;

  private String creationTimestampFieldName;

  private List<String> fieldNameAliases = new ArrayList<>();

  private String idFieldName;

  private boolean loadAll = true;

  private boolean loaded = false;

  private boolean loading = false;

  private boolean loadMissingCodes = true;

  private String modificationTimestampFieldName;

  private List<String> orderBy = DEFAULT_FIELD_NAMES;

  private RecordDefinition recordDefinition;

  private RecordStore recordStore;

  private final ThreadLocal<Boolean> threadLoading = new ThreadLocal<>();

  private PathName typePath;

  private List<String> valueFieldNames = DEFAULT_FIELD_NAMES;

  private boolean allowNullValues = false;

  public MultiValueRecordStoreCodeTable() {
    super();
  }

  public MultiValueRecordStoreCodeTable(final boolean capitalizeWords) {
    super(capitalizeWords);
  }

  public void addFieldAlias(final String columnName) {
    this.fieldNameAliases.add(columnName);
  }

  @Override
  public void addValue(final Record code) {
    final String idFieldName = getIdFieldName();
    final Identifier id = code.getIdentifier(idFieldName);
    if (id == null) {
      throw new NullPointerException(idFieldName + "=null for " + code);
    } else {
      final List<Object> values = new ArrayList<>();
      for (final String fieldName : this.valueFieldNames) {
        Object value = code.getValue(fieldName);
        if (value instanceof SingleIdentifier) {
          final SingleIdentifier identifier = (SingleIdentifier)value;
          value = identifier.getValue(0);
        }
        if (value == null) {
          if (!this.allowNullValues) {
            throw new NullPointerException(this.valueFieldNames + "=null for " + code);
          }
        }
        values.add(value);
      }
      addValue(id, values);
    }
  }

  protected void addValues(final Iterable<Record> allCodes) {
    for (final Record code : allCodes) {
      addValue(code);
    }
  }

  @Override
  protected int calculateValueFieldLength() {
    int length = this.valueFieldNames.size();
    for (final String fieldName : this.valueFieldNames) {
      length += this.recordDefinition.getFieldLength(fieldName);
    }
    return length;
  }

  protected void clearRecordDefinition() {
  }

  @Override
  public MultiValueRecordStoreCodeTable clone() {
    final MultiValueRecordStoreCodeTable clone = (MultiValueRecordStoreCodeTable)super.clone();
    clone.recordDefinition = null;
    clone.fieldNameAliases = new ArrayList<>(this.fieldNameAliases);
    clone.valueFieldNames = new ArrayList<>(this.valueFieldNames);
    return clone;
  }

  @SuppressWarnings("unchecked")
  public <C extends CodeTable> C getCodeTable() {
    return (C)this;
  }

  public String getCreationTimestampFieldName() {
    return this.creationTimestampFieldName;
  }

  @Override
  public List<String> getFieldNameAliases() {
    return this.fieldNameAliases;
  }

  @Override
  public String getIdFieldName() {
    if (Property.hasValue(this.idFieldName)) {
      return this.idFieldName;
    } else if (this.recordDefinition == null) {
      return "";
    } else {
      final String idFieldName = this.recordDefinition.getIdFieldName();
      if (Property.hasValue(idFieldName)) {
        return idFieldName;
      } else {
        return this.recordDefinition.getFieldName(0);
      }
    }
  }

  @Override
  public JsonObject getMap(final Identifier id) {
    final List<Object> values = getValues(id);
    if (values == null) {
      return JsonObject.hash();
    } else {
      final JsonObject map = JsonObject.hash();
      int i = 0;
      for (final String name : this.valueFieldNames) {
        final Object value = values.get(i);
        map.addValue(name, value);
        i++;
      }
      return map;
    }
  }

  public String getModificationTimestampFieldName() {
    return this.modificationTimestampFieldName;
  }

  @Override
  public Record getRecord(final Identifier id) {
    return this.recordStore.getRecord(this.typePath, id);
  }

  @Override
  public RecordDefinition getRecordDefinition() {
    return this.recordDefinition;
  }

  @Override
  public RecordStore getRecordStore() {
    return this.recordStore;
  }

  public String getTypeName() {
    return this.typePath.getPath();
  }

  public PathName getTypePath() {
    return this.typePath;
  }

  @Override
  public <V> V getValue(final Object id) {
    if (id instanceof Identifier) {
      return getValue((Identifier)id);
    } else if (id instanceof List) {
      final List list = (List)id;
      return getValue(new ListIdentifier(list));
    } else {
      return getValue(Identifier.newIdentifier(id));
    }
  }

  @Override
  public List<String> getValueFieldNames() {
    return this.valueFieldNames;
  }

  public boolean isAllowNullValues() {
    return this.allowNullValues;
  }

  public boolean isCreateMissingCodes() {
    return this.createMissingCodes;
  }

  @Override
  public boolean isLoadAll() {
    return this.loadAll;
  }

  @Override
  public boolean isLoaded() {
    return this.loaded;
  }

  @Override
  public boolean isLoading() {
    return this.loading;
  }

  @Override
  public boolean isLoadMissingCodes() {
    return this.loadMissingCodes;
  }

  @Override
  public synchronized void loadAll() {
    final long time = System.currentTimeMillis();
    if (this.threadLoading.get() != Boolean.TRUE) {
      if (this.loading) {
        while (this.loading) {
          try {
            wait(1000);
          } catch (final InterruptedException e) {
            Exceptions.throwUncheckedException(e);
          }
        }
        return;
      } else {
        this.threadLoading.set(Boolean.TRUE);
        this.loading = true;
        try {
          if (this.recordStore != null) {
            final Query query = this.recordStore.newQuery(this.typePath);
            for (final String order : this.orderBy) {
              query.addOrderBy(order);
            }
            try (
              RecordReader reader = this.recordStore.getRecords(query)) {
              final List<Record> codes = reader.toList();
              final CategoryLabelCountMap statistics = this.recordStore.getStatistics();
              if (statistics != null) {
                statistics.getLabelCountMap("query").addCount(this.typePath, -codes.size());
              }
              Collections.sort(codes, new RecordFieldComparator(this.orderBy));
              addValues(codes);
            }
          }
        } finally {
          this.loading = false;
          this.loaded = true;
          this.threadLoading.set(null);
          notifyAll();
        }
        Property.firePropertyChange(this, "valuesChanged", false, true);
      }
    }
    Dates.debugEllapsedTime(this, "Load All: " + getTypePath(), time);
  }

  @Override
  protected synchronized Identifier loadId(final List<Object> values, final boolean createId) {
    if (this.loadAll && !this.loadMissingCodes && !isEmpty()) {
      return null;
    }
    Identifier id = null;
    if (createId && this.loadAll && !isLoaded()) {
      loadAll();
      id = getIdentifier(values, false);
    } else {
      final Query query = new Query(this.typePath);
      final And and = new And();
      if (!values.isEmpty()) {
        int i = 0;
        for (final String fieldName : this.valueFieldNames) {
          final Object value = values.get(i);
          if (value == null) {
            and.and(Q.isNull(fieldName));
          } else {
            final FieldDefinition fieldDefinition = this.recordDefinition.getField(fieldName);
            and.and(Q.equal(fieldDefinition, value));
          }
          i++;
        }
      }
      query.setWhereCondition(and);
      final RecordReader reader = this.recordStore.getRecords(query);
      try {
        final List<Record> codes = reader.toList();
        if (codes.size() > 0) {
          final CategoryLabelCountMap statistics = this.recordStore.getStatistics();
          if (statistics != null) {
            statistics.getLabelCountMap("query").addCount(this.typePath, -codes.size());
          }

          addValues(codes);
        }
        id = getIdByValue(values);
        Property.firePropertyChange(this, "valuesChanged", false, true);
      } finally {
        reader.close();
      }
    }
    if (createId && id == null) {
      return newIdentifier(values);
    } else {
      return id;
    }
  }

  @Override
  protected List<Object> loadValues(final Object id) {
    if (this.loadAll && !isLoaded()) {
      loadAll();
    } else if (!this.loadAll || this.loadMissingCodes) {
      try {
        final Record code;
        if (id instanceof Identifier) {
          final Identifier identifier = (Identifier)id;
          code = this.recordStore.getRecord(this.typePath, identifier);
        } else {
          code = this.recordStore.getRecord(this.typePath, id);
        }
        if (code != null) {
          addValue(code);
        }
      } catch (final Throwable e) {
        return null;
      }
    }
    return getValueById(id);
  }

  protected synchronized Identifier newIdentifier(final List<Object> values) {
    if (this.createMissingCodes) {
      // TODO prevent duplicates from other threads/processes
      final Record code = this.recordStore.newRecord(this.typePath);
      final RecordDefinition recordDefinition = code.getRecordDefinition();
      Identifier id = this.recordStore.newPrimaryIdentifier(this.typePath);
      if (id == null) {
        final FieldDefinition idField = recordDefinition.getIdField();
        if (idField != null) {
          if (Number.class.isAssignableFrom(idField.getDataType().getJavaClass())) {
            id = Identifier.newIdentifier(getNextId());
          } else {
            id = Identifier.newIdentifier(UUID.randomUUID().toString());
          }
        }
      }
      code.setIdentifier(id);
      for (int i = 0; i < this.valueFieldNames.size(); i++) {
        final String name = this.valueFieldNames.get(i);
        final Object value = values.get(i);
        code.setValue(name, value);
      }

      final Instant now = Instant.now();
      if (this.creationTimestampFieldName != null) {
        code.setValue(this.creationTimestampFieldName, now);
      }
      if (this.modificationTimestampFieldName != null) {
        code.setValue(this.modificationTimestampFieldName, now);
      }

      this.recordStore.insertRecord(code);
      return code.getIdentifier();
    } else {
      return null;
    }
  }

  @Override
  public synchronized void refresh() {
    super.refresh();
    if (isLoadAll()) {
      this.loaded = false;
      loadAll();
    }
  }

  @Override
  public void refreshIfNeeded() {
    if (this.loadAll && !this.loading) {
      super.refreshIfNeeded();
    }
  }

  public MultiValueRecordStoreCodeTable setAllowNullValues(final boolean allowNullValues) {
    this.allowNullValues = allowNullValues;
    return this;
  }

  public MultiValueRecordStoreCodeTable setCreateMissingCodes(final boolean createMissingCodes) {
    this.createMissingCodes = createMissingCodes;
    return this;
  }

  public MultiValueRecordStoreCodeTable setCreationTimestampFieldName(
    final String creationTimestampFieldName) {
    this.creationTimestampFieldName = creationTimestampFieldName;
    return this;
  }

  public MultiValueRecordStoreCodeTable setFieldAliases(final String... fieldNameAliases) {
    setFieldNameAliases(Lists.newArray(fieldNameAliases));
    return this;
  }

  public MultiValueRecordStoreCodeTable setFieldNameAliases(final List<String> fieldNameAliases) {
    this.fieldNameAliases = new ArrayList<>(fieldNameAliases);
    return this;
  }

  public MultiValueRecordStoreCodeTable setIdFieldName(final String idFieldName) {
    this.idFieldName = idFieldName;
    return this;
  }

  public MultiValueRecordStoreCodeTable setLoadAll(final boolean loadAll) {
    this.loadAll = loadAll;
    return this;
  }

  @Override
  public MultiValueRecordStoreCodeTable setLoadMissingCodes(final boolean loadMissingCodes) {
    this.loadMissingCodes = loadMissingCodes;
    return this;
  }

  public MultiValueRecordStoreCodeTable setModificationTimestampFieldName(
    final String modificationTimestampFieldName) {
    this.modificationTimestampFieldName = modificationTimestampFieldName;
    return this;
  }

  public MultiValueRecordStoreCodeTable setOrderBy(final List<String> orderBy) {
    this.orderBy = new ArrayList<>(orderBy);
    return this;
  }

  public MultiValueRecordStoreCodeTable setOrderByFieldName(final String orderBy) {
    this.orderBy = Lists.newArray(orderBy);
    return this;
  }

  public void setRecordDefinition(final RecordDefinition recordDefinition) {
    if (this.recordDefinition != recordDefinition) {
      clearRecordDefinition();
      this.recordDefinition = recordDefinition;
      if (recordDefinition == null) {
        this.recordStore = null;
        this.typePath = null;
      } else {
        this.typePath = recordDefinition.getPathName();
        final String name = this.typePath.getName();
        setName(name);
        this.recordStore = this.recordDefinition.getRecordStore();
        if (!this.valueFieldNames.isEmpty()) {
          final String fieldName = this.valueFieldNames.get(0);
          setValueFieldDefinition(recordDefinition.getField(fieldName));
        }
        setRecordDefinitionDo(recordDefinition);
      }
    }
  }

  protected void setRecordDefinitionDo(final RecordDefinition recordDefinition) {

  }

  public MultiValueRecordStoreCodeTable setValueFieldName(final String valueColumns) {
    setValueFieldNames(valueColumns);
    return this;
  }

  public MultiValueRecordStoreCodeTable setValueFieldNames(final List<String> valueColumns) {
    this.valueFieldNames = new ArrayList<>(valueColumns);
    if (this.orderBy == DEFAULT_FIELD_NAMES) {
      setOrderBy(valueColumns);
    }
    return this;
  }

  public MultiValueRecordStoreCodeTable setValueFieldNames(final String... valueColumns) {
    setValueFieldNames(Arrays.asList(valueColumns));
    return this;
  }

  @Override
  public String toString() {
    return this.typePath + " " + getIdFieldName() + " " + this.valueFieldNames;

  }

  public String toString(final List<String> values) {
    final StringBuilder string = new StringBuilder(values.get(0));
    for (int i = 1; i < values.size(); i++) {
      final String value = values.get(i);
      string.append(",");
      string.append(value);
    }
    return string.toString();
  }

}
