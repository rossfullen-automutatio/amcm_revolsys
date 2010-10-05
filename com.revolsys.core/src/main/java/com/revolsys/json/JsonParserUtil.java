package com.revolsys.json;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.revolsys.json.JsonParser.EventType;
import com.revolsys.util.MathUtil;

public final class JsonParserUtil {
  public static List<Object> getArray(
    final JsonParser parser) {
    if (parser.getEvent() == EventType.startArray || parser.hasNext()
      && parser.next() == EventType.startArray) {
      EventType event = parser.getEvent();
      final List<Object> list = new ArrayList<Object>();
      do {
        final Object value = getValue(parser);
        if (value instanceof EventType) {
          event = (EventType)value;

        } else {
          list.add(value);
          event = parser.next();
        }
      } while (event == EventType.comma);
      if (event != EventType.endArray) {
        throw new IllegalStateException("Exepecting end array, not:" + event);
      }
      return list;
    } else {
      throw new IllegalStateException("Exepecting start array, not:"
        + parser.getEvent());
    }

  }

  public static Map<String, Object> getMap(
    final JsonParser parser) {
    if (parser.getEvent() == EventType.startObject || parser.hasNext()
      && parser.next() == EventType.startObject) {
      EventType event = parser.getEvent();
      final Map<String, Object> map = new LinkedHashMap<String, Object>();
      do {
        if (parser.hasNext() && parser.next() == EventType.string) {
          final String key = getString(parser);
          if (parser.hasNext()) {
            if (parser.next() == EventType.colon) {
              if (parser.hasNext()) {
                final Object value = getValue(parser);
                map.put(key, value);
              }
            }
          }
          event = parser.next();
        } else {
          event = parser.getEvent();
        }
      } while (event == EventType.comma);
      if (event != EventType.endObject) {
        throw new IllegalStateException("Exepecting end object, not:" + event);
      }
      return map;
    } else {
      throw new IllegalStateException("Exepecting end object, not:"
        + parser.getEvent());
    }

  }

  public static String getString(
    final JsonParser parser) {
    if (parser.getEvent() == EventType.string || parser.hasNext()
      && parser.next() == EventType.string) {
      return parser.getValue();
    } else {
      throw new IllegalStateException("Expecting a string");
    }
  }

  /**
   * Skip through the document until the specified object attribute name is
   * found.
   * 
   * @param parser The parser.
   * @param attributeName The name of the attribute to skip through.
   */
  public static void skipToAttribute(
    final JsonParser parser,
    final String attributeName) {
    while (parser.hasNext()) {
      EventType eventType = parser.next();
      if (eventType == EventType.string) {
        String key = getString(parser);
        if (key.equals(attributeName)) {
          if (parser.hasNext() && parser.next() == EventType.colon) {
            return;
          }
        }
      }
    }
  }

  public static String skipToNextAttribute(
    final JsonParser parser) {
    int objectCount = 0;
    while (parser.hasNext()) {
      EventType eventType = parser.next();
      if (objectCount == 0 && eventType == EventType.string) {
        String key = getString(parser);
        if (parser.hasNext() && parser.next() == EventType.colon) {
          return key;
        }
      } else if (eventType == EventType.startObject) {
        objectCount++;
      } else if (eventType == EventType.endObject) {
        if (objectCount == 0) {
          return null;
        } else {
          objectCount--;
        }
      }
    }
    return null;
  }

  public static double[] getDoubleArray(
    final JsonParser parser) {
    if (parser.getEvent() == EventType.startArray || parser.hasNext()
      && parser.next() == EventType.startArray) {
      EventType event = parser.getEvent();
      final List<Number> list = new ArrayList<Number>();
      do {
        final Object value = getValue(parser);
        if (value instanceof EventType) {
          event = (EventType)value;
        } else if (value instanceof Number) {
          list.add((Number)value);
          event = parser.next();
        } else {
          throw new IllegalArgumentException("Expecting number, not: " + value);
        }
      } while (event == EventType.comma);
      if (event != EventType.endArray) {
        throw new IllegalStateException("Exepecting end array, not: " + event);
      }

      return MathUtil.toDoubleArray(list);
    } else {
      throw new IllegalStateException("Exepecting start array, not: "
        + parser.getEvent());
    }
  }


  public static Object getValue(
    final JsonParser parser) {
    // TODO empty array
    if (parser.hasNext()) {
      final EventType event = parser.next();
      if (event == EventType.startArray) {
        return getArray(parser);
      } else if (event == EventType.startObject) {
        return getMap(parser);
      } else if (event == EventType.booleanValue) {
        return parser.getValue();
      } else if (event == EventType.nullValue) {
        return parser.getValue();
      } else if (event == EventType.string) {
        return parser.getValue();
      } else if (event == EventType.number) {
        return parser.getValue();
      } else {
        return event;
      }
    } else {
      throw new IllegalStateException("Expecting a value not EOF");
    }
  }

  private JsonParserUtil() {
  }
}
