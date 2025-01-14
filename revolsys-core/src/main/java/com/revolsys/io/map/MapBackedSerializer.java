package com.revolsys.io.map;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import com.revolsys.collection.json.JsonObject;
import com.revolsys.collection.json.JsonObjectHash;
import com.revolsys.collection.map.DelegatingMap;

public class MapBackedSerializer extends DelegatingMap<String, Object> implements MapSerializer {
  public static MapBackedSerializer hash() {
    return new MapBackedSerializer(new HashMap<>());
  }

  public static MapBackedSerializer linked() {
    return new MapBackedSerializer(new LinkedHashMap<>());
  }

  public static MapBackedSerializer tree() {
    return new MapBackedSerializer(new TreeMap<>());
  }

  public MapBackedSerializer(final Map<String, Object> map) {
    super(map);
  }

  @Override
  public JsonObject toMap() {
    return new JsonObjectHash(getMap());
  }
}
