/*
 * $URL$
 * $Author$
 * $Date$
 * $Revision$

 * Copyright 2004-2007 Revolution Systems Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.revolsys.gis.data.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.revolsys.converter.string.StringConverter;
import com.revolsys.converter.string.StringConverterRegistry;
import com.revolsys.gis.data.model.codes.CodeTable;
import com.revolsys.gis.data.model.types.DataType;
import com.revolsys.gis.data.model.types.DataTypes;
import com.revolsys.gis.jts.JtsGeometryUtil;
import com.revolsys.gis.model.data.equals.EqualsRegistry;
import com.revolsys.util.JavaBeanUtil;
import com.vividsolutions.jts.geom.Geometry;

public final class DataObjectUtil {

  public static final DataObjectMetaData GEOMETRY_META_DATA = new DataObjectMetaDataImpl(
    "Feature", new Attribute("geometry", DataTypes.GEOMETRY, true));

  /**
   * Clone the value if it has a clone method.
   * 
   * @param value The value to clone.
   * @return The cloned value.
   */
  public static Object clone(final Object value) {
    if (value instanceof Cloneable) {
      try {
        final Class<? extends Object> valueClass = value.getClass();
        final Method method = valueClass.getMethod("clone", new Class[0]);
        if (method != null) {
          return method.invoke(value, new Object[0]);
        }
      } catch (final IllegalArgumentException e) {
        throw e;
      } catch (final InvocationTargetException e) {

        final Throwable cause = e.getCause();
        if (cause instanceof RuntimeException) {
          final RuntimeException re = (RuntimeException)cause;
          throw re;
        } else if (cause instanceof Error) {
          final Error ee = (Error)cause;
          throw ee;
        } else {
          throw new RuntimeException(cause.getMessage(), cause);
        }
      } catch (final RuntimeException e) {
        throw e;
      } catch (final Exception e) {
        throw new RuntimeException(e.getMessage(), e);
      }

    }
    return value;
  }

  /**
   * Create a copy of the data object replacing the geometry with the new
   * geometry. If the existing geometry on the object has user data it will be
   * cloned to the new geometry.
   * 
   * @param object The object to copy.
   * @param geometry The new geometry.
   * @return The copied object.
   */
  public static <T extends DataObject> T copy(final T object,
    final Geometry geometry) {
    final Geometry oldGeometry = object.getGeometryValue();
    final T newObject = (T)object.clone();
    newObject.setGeometryValue(geometry);
    final Map<String, Object> userData = JtsGeometryUtil.getGeometryProperties(oldGeometry);
    if (userData != null) {
      geometry.setUserData(new HashMap<String, Object>(userData));
    }
    return newObject;
  }

  @SuppressWarnings("unchecked")
  public static <T> T getAttributeByPath(final DataObject object,
    final String path) {
    final DataObjectMetaData metaData = object.getMetaData();

    final String[] propertyPath = path.split("\\.");
    Object propertyValue = object;
    for (int i = 0; i < propertyPath.length && propertyValue != null; i++) {
      final String propertyName = propertyPath[i];
      if (propertyValue instanceof DataObject) {
        final DataObject dataObject = (DataObject)propertyValue;

        if (dataObject.hasAttribute(propertyName)) {
          propertyValue = dataObject.getValue(propertyName);
          if (propertyValue == null) {
            return null;
          } else if (i + 1 < propertyPath.length) {
            final CodeTable codeTable = metaData.getCodeTableByColumn(propertyName);
            if (codeTable != null) {
              propertyValue = codeTable.getMap(propertyValue);
            }
          }
        } else {
          return null;
        }
      } else if (propertyValue instanceof Geometry) {
        final Geometry geometry = (Geometry)propertyValue;
        propertyValue = JtsGeometryUtil.getGeometryProperty(geometry,
          propertyName);
      } else if (propertyValue instanceof Map) {
        final Map<String, Object> map = (Map<String, Object>)propertyValue;
        propertyValue = map.get(propertyName);
        if (propertyValue == null) {
          return null;
        } else if (i + 1 < propertyPath.length) {
          final CodeTable codeTable = metaData.getCodeTableByColumn(propertyName);
          if (codeTable != null) {
            propertyValue = codeTable.getMap(propertyValue);
          }
        }
      } else {
        try {
          propertyValue = JavaBeanUtil.getProperty(propertyValue, propertyName);
        } catch (final IllegalArgumentException e) {
          throw new IllegalArgumentException("Path does not exist " + path, e);
        }
      }
    }
    return (T)propertyValue;
  }

  public static boolean getBoolean(final DataObject object,
    final String attributeName) {
    if (object == null) {
      return false;
    } else {
      final Object value = object.getValue(attributeName);
      if (value == null) {
        return false;
      } else if (value instanceof Boolean) {
        final Boolean booleanValue = (Boolean)value;
        return booleanValue;
      } else if (value instanceof Number) {
        final Number number = (Number)value;
        return number.intValue() == 1;
      } else {
        final String stringValue = value.toString();
        if (stringValue.equals("1") || Boolean.parseBoolean(stringValue)) {
          return true;
        } else {
          return false;
        }
      }
    }
  }

  public static Double getDouble(final DataObject object,
    final int attributeIndex) {
    final Number value = object.getValue(attributeIndex);
    if (value == null) {
      return null;
    } else {
      return value.doubleValue();
    }
  }

  public static Double getDouble(final DataObject object,
    final String attributeName) {
    final Number value = object.getValue(attributeName);
    if (value == null) {
      return null;
    } else {
      return value.doubleValue();
    }
  }

  public static Integer getInteger(final DataObject object,
    final String attributeName) {
    if (object == null) {
      return null;
    } else {
      final Number value = object.getValue(attributeName);
      if (value == null) {
        return null;
      } else {
        return value.intValue();
      }
    }
  }

  public static Integer getInteger(final DataObject object,
    final String attributeName, final Integer defaultValue) {
    if (object == null) {
      return null;
    } else {
      final Number value = object.getValue(attributeName);
      if (value == null) {
        return defaultValue;
      } else {
        return value.intValue();
      }
    }
  }

  public static Long getLong(final DataObject object, final String attributeName) {
    final Number value = object.getValue(attributeName);
    if (value == null) {
      return null;
    } else {
      return value.longValue();
    }
  }

  public static void setValues(final DataObject target,
    final DataObject source, final Collection<String> attributesNames,
    final Collection<String> ignoreAttributeNames) {
    for (final String attributeName : attributesNames) {
      if (!ignoreAttributeNames.contains(attributeName)) {
        final Object oldValue = target.getValue(attributeName);
        Object newValue = source.getValue(attributeName);
        if (!EqualsRegistry.INSTANCE.equals(oldValue, newValue)) {
          newValue = DataObjectUtil.clone(newValue);
          target.setValue(attributeName, newValue);
        }
      }
    }
  }

  private DataObjectUtil() {
  }

  public static List<DataObject> getObjects(DataObjectMetaData metaData,
    Collection<? extends Map<String, Object>> list) {
    List<DataObject> objects = new ArrayList<DataObject>();
    for (Map<String, Object> map : list) {
      DataObject object = getObject(metaData, map);
      objects.add(object);
    }
    return objects;
  }

  public static DataObject getObject(DataObjectMetaData metaData,
    Map<String, Object> values) {
    DataObject object = new ArrayDataObject(metaData);
    for (Entry<String, Object> entry : values.entrySet()) {
      String name = entry.getKey();
      Attribute attribute = metaData.getAttribute(name);
      if (attribute != null) {
        Object value = entry.getValue();
        if (value != null) {
          final DataType dataType = attribute.getType();
          @SuppressWarnings("unchecked")
          final Class<Object> dataTypeClass = (Class<Object>)dataType.getJavaClass();
          if (dataTypeClass.isAssignableFrom(value.getClass())) {
            object.setValue(name, value);
          } else {
            final StringConverter<Object> converter = StringConverterRegistry.getInstance()
              .getConverter(dataTypeClass);
            if (converter == null) {
              object.setValue(name, value);
            } else {
              final Object convertedValue = converter.toObject(value);
              object.setValue(name, convertedValue);
            }
          }
        }
      }
    }
    return object;
  }

}
