package com.revolsys.gis.util;

import javax.xml.namespace.QName;

import com.revolsys.gis.data.model.DataObject;
import com.revolsys.gis.data.model.DataObjectState;
import com.revolsys.gis.graph.Edge;
import com.revolsys.gis.model.coordinates.Coordinates;
import com.revolsys.gis.model.coordinates.DoubleCoordinates;
import com.revolsys.gis.model.coordinates.list.CoordinatesList;
import com.revolsys.gis.model.coordinates.list.CoordinatesListUtil;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;

public class NoOp {
  public static void equals(
    final Coordinates coordinates1End,
    final double... coordinates) {
    if (coordinates1End.equals(coordinates)) {
      noOp();
    }
  }

  public static void equals(
    final DataObject object,
    final double x,
    final double y) {
    equals(object.getGeometryValue(), x, y);
  }

  public static void equals(
    final DataObject object,
    final Double x,
    final Double y) {
    equals(object.getGeometryValue(), x, y);
  }

  public static void equals(
    final Geometry geometry,
    final double x,
    final double y) {
    CoordinatesList points = CoordinatesListUtil.get(geometry);
    DoubleCoordinates point = new DoubleCoordinates(x, y);
    if (points.equal(0, point, 2)) {
      NoOp.noOp();
    }
  }

  public static boolean equals(
    final LineString line,
    final double x1,
    final double y1,
    final double x2,
    final double y2) {
    final CoordinatesList points = CoordinatesListUtil.get(line);
    if (points.get(0).equals(x1, y1)
      && points.get(points.size() - 1).equals(x2, y2)) {
      noOp();
      return true;
    } else {
      return false;
    }
  }

  public static void equals(final Object object1, final Object object2) {
    if (object1.equals(object2)) {
      noOp();
    }
  }

  public static void idNull(final DataObject object) {
    if (object.getIdValue() == null) {
      noOp();
    }
  }

  public static void infinite(final double value) {
    if (Double.isInfinite(value)) {
      noOp();
    }
  }

  public static void invalidGeometry(final Geometry geometry) {
    if (!geometry.isValid()) {
      noOp();
    }
  }

  public static void isNull(final Object value) {
    if (value == null) {
      noOp();
    }
  }

  public static void modified(final DataObject object) {
    if (object.getState() == DataObjectState.Modified) {
      noOp();
    }
  }

  public static void nan(final double value) {
    if (Double.isNaN(value)) {
      noOp();
    }
  }

  public static void noOp() {
  }

  public static void typeName(final DataObject object, final QName typeName) {
    final QName typeName2 = object.getMetaData().getName();
    equals(typeName2, typeName);
  }

  public static void typeName(final Edge<?> edge, final QName typeName) {
    final QName typeName2 = edge.getTypeName();
    equals(typeName2, typeName);
  }

  public static void zeroLegthLine(final LineString line) {
    if (line.getLength() == 0) {
      noOp();
    }
  }

}
