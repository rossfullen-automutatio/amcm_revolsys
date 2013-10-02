package com.revolsys.gis.data.query;

import java.util.Arrays;
import java.util.Collection;

import com.revolsys.converter.string.StringConverterRegistry;
import com.revolsys.gis.data.io.DataObjectStore;
import com.revolsys.gis.data.model.Attribute;

public class Conditions {

  public static Condition and(final Collection<Condition> conditions) {
    return new MultipleCondition("AND", conditions);
  }

  public static MultipleCondition and(final Condition... conditions) {
    return new MultipleCondition("AND", conditions);
  }

  public static Condition equal(final Attribute attribute, final Object value) {
    final String name = attribute.getName();
    final Value valueCondition = new Value(attribute, value);
    return equal(name, valueCondition);
  }

  private static BinaryCondition equal(final Condition left,
    final Condition right) {
    return new BinaryCondition(left, "=", right);
  }

  public static Condition equal(final Condition left, final Object value) {
    final Value valueCondition = new Value(value);
    return equal(left, valueCondition);
  }

  public static BinaryCondition equal(final String left, final Condition right) {
    final Column leftCondition = new Column(left);
    return equal(leftCondition, right);
  }

  public static Condition equal(final String name, final Object value) {
    final Value valueCondition = new Value(value);
    return equal(name, valueCondition);
  }

  public static BinaryCondition greaterThan(final String left,
    final Condition right) {
    return new BinaryCondition(new Column(left), ">", right);
  }

  public static Condition greaterThanOrEqual(final Attribute attribute,
    final Object value) {
    final String name = attribute.getName();
    final Value valueCondition = new Value(attribute, value);
    return greaterThanOrEqual(name, valueCondition);
  }

  public static BinaryCondition greaterThanOrEqual(final String left,
    final Condition right) {
    return new BinaryCondition(new Column(left), ">=", right);
  }

  public static BinaryCondition in(final Attribute attribute,
    final Collection<? extends Object> values) {
    return new BinaryCondition(new Column(attribute.getName()), "in",
      new CollectionValue(attribute, values));
  }

  public static BinaryCondition in(final Condition left,
    final CollectionCondition conditions) {
    return new BinaryCondition(left, "in", conditions);
  }

  public static BinaryCondition in(final String name,
    final Collection<? extends Object> values) {
    final Column left = new Column(name);
    return new BinaryCondition(left, "in", new CollectionValue(values));
  }

  public static Condition in(final String name, final Object... values) {
    return in(name, Arrays.asList(values));
  }

  public static RightUnaryCondition isNotNull(final Condition condition) {
    return new RightUnaryCondition(condition, "IS NOT NULL");
  }

  public static RightUnaryCondition isNotNull(final String name) {
    final Column condition = new Column(name);
    return isNotNull(condition);
  }

  public static RightUnaryCondition isNull(final Condition condition) {
    return new RightUnaryCondition(condition, "IS NULL");
  }

  public static RightUnaryCondition isNull(final String name) {
    final Column condition = new Column(name);
    return isNull(condition);
  }

  public static Condition lessThan(final String left, final Condition right) {
    return new BinaryCondition(new Column(left), "<", right);
  }

  public static Condition lessThan(final String left, final Object value) {
    final Value valueCondition = new Value(value);
    return new BinaryCondition(new Column(left), "<", valueCondition);
  }

  public static Condition lessThanOrEqual(final String left,
    final Condition right) {
    return new BinaryCondition(new Column(left), "<=", right);
  }

  public static Condition like(final Condition left, final Condition right) {
    return new BinaryCondition(left, "LIKE", right);
  }

  public static Condition like(final Condition left, final Object value) {
    final Value valueCondition = new Value(value);
    return like(left, valueCondition);
  }

  public static Condition like(final String left, final Condition right) {
    return like(new Column(left), right);
  }

  public static Condition like(final String name, final Object value) {
    final Value valueCondition = new Value(value);
    return like(name, valueCondition);
  }

  public static Condition likeRegEx(final DataObjectStore dataStore,
    final String fieldName, final Object value) {
    Condition left;
    if (dataStore.getClass().getName().contains("Oracle")) {
      left = new SqlCondition("regexp_replace(upper(" + fieldName
        + "), '[^A-Z0-9]','')");
    } else {
      left = new SqlCondition("regexp_replace(upper(" + fieldName
        + "), '[^A-Z0-9]','', 'g')");
    }
    final String right = "%"
      + StringConverterRegistry.toString(value)
        .toUpperCase()
        .replaceAll("[^A-Z0-0]", "") + "%";
    return like(left, right);
  }

  public static Condition likeUpper(final String left, final String right) {
    return like(Function.upper(new Cast(left, "varchar(4000)")),
      ("%" + right + "%").toUpperCase());
  }

  public static LeftUnaryCondition not(final Condition value) {
    return new LeftUnaryCondition("NOT", value);
  }

  public static Condition notEqual(final String name, final Condition right) {
    return new BinaryCondition(new Column(name), "<>", right);
  }

  public static Condition notEqual(final String name, final Object value) {
    return notEqual(name, new Value(value));
  }

  public static MultipleCondition or(final Condition... conditions) {
    return new MultipleCondition("OR", conditions);
  }

  public static void setValue(final int index, final Condition condition,
    final Object value) {
    setValueInternal(-1, index, condition, value);

  }

  public static int setValueInternal(int i, final int index,
    final Condition condition, final Object value) {
    for (final Condition subCondition : condition.getConditions()) {
      if (subCondition instanceof Value) {
        final Value valueCondition = (Value)subCondition;
        i++;
        if (i == index) {
          valueCondition.setValue(value);
          return i;
        }
        i = setValueInternal(i, index, subCondition, value);
        if (i >= index) {
          return i;
        }
      }
    }
    return i;
  }

  public static SqlCondition sql(final String sql) {
    return new SqlCondition(sql);
  }

  public static SqlCondition sql(final String sql, final Object... parameters) {
    return new SqlCondition(sql, parameters);
  }
}
