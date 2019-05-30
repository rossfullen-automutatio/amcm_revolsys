package com.revolsys.geometry.test.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.measure.Unit;

import org.jeometry.common.number.Doubles;
import org.junit.Assert;
import org.junit.Test;

import com.revolsys.collection.list.Lists;
import com.revolsys.geometry.cs.CoordinateSystem;
import com.revolsys.geometry.model.BoundingBox;
import com.revolsys.geometry.model.GeometryFactory;
import com.revolsys.geometry.model.Point;
import com.revolsys.geometry.model.impl.BoundingBoxDoubleGf;
import com.revolsys.geometry.model.impl.PointDouble;
import com.revolsys.geometry.test.TestConstants;
import com.revolsys.geometry.util.BoundingBoxUtil;
import com.revolsys.util.QuantityType;

import tec.uom.se.quantity.Quantities;
import tec.uom.se.unit.Units;

// TODO
//clipToCoordinateSystem()
//clone()
//covers(Geometry)
//covers(Point)
//convert(GeometryFactory)
//covers(BoundingBox)
//covers(Point)
//covers(double, double)
//distance(BoundingBox)
//distance(Geometry)
//equals(Object)
//expand(Point)
//expand(double)
//expand(double, double)
//expandPercent(double)
//expandPercent(double, double)
//expandToInclude(BoundingBox)
//expandToInclude(Record)
//expandToInclude(Geometry)
//expandToInclude(Point)
//getBottomLeftPoint()
//getBottomRightPoint()
//getCentre()
//getCentreX()
//getCentreY()
//getCornerPoint(int)
//getCornerPoints()
//getTopLeftPoint()
//getTopRightPoint()
//hashCode()
//intersection(BoundingBox)
//intersects(BoundingBox)
//intersects(Point)
//intersects(double, double)
//intersects(Geometry)
//move(double, double)
//toGeometry()
//toPolygon()
//toPolygon(GeometryFactory)
//toPolygon(GeometryFactory, int)
//toPolygon(GeometryFactory, int, int)
//toPolygon(int)
//toPolygon(int, int)

public class BoundingBoxTest implements TestConstants {
  private static final double[] NULL_BOUNDS = null;

  @SuppressWarnings({
    "rawtypes", "unchecked"
  })
  private void assertEnvelope(final BoundingBox boundingBox, final GeometryFactory geometryFactory,
    final boolean empty, final int axisCount, final double... bounds) {
    Assert.assertEquals("Geometry Factory", geometryFactory, boundingBox.getGeometryFactory());
    Assert.assertEquals("Empty", empty, boundingBox.isEmpty());
    Assert.assertEquals("Axis Count", axisCount, boundingBox.getAxisCount());
    Assert.assertEquals("Bounds", Lists.newArray(bounds), Lists.newArray(boundingBox.getBounds()));

    Unit unit = Units.METRE;
    Unit lengthUnit = Units.METRE;
    final StringBuilder wkt = new StringBuilder();
    final int srid = boundingBox.getCoordinateSystemId();
    if (geometryFactory == null) {
      Assert.assertEquals("coordinateSystem", null, boundingBox.getCoordinateSystem());
      Assert.assertEquals("srid", 0, srid);
    } else {
      if (srid > 0) {
        wkt.append("SRID=");
        wkt.append(srid);
        wkt.append(";");
      }
      Assert.assertEquals("srid", geometryFactory.getCoordinateSystemId(), srid);
      final CoordinateSystem coordinateSystem = geometryFactory.getCoordinateSystem();
      Assert.assertEquals("coordinateSystem", coordinateSystem, boundingBox.getCoordinateSystem());
      if (coordinateSystem != null) {
        unit = coordinateSystem.getUnit();
        lengthUnit = coordinateSystem.getLengthUnit();
      }
    }
    wkt.append("BBOX");
    assertMinMax(boundingBox, -1, Double.NaN, Double.NaN);
    assertMinMax(boundingBox, axisCount + 1, Double.NaN, Double.NaN);
    double width = 0;
    double height = 0;

    double minX = Double.NaN;
    double maxX = Double.NaN;
    double minY = Double.NaN;
    double maxY = Double.NaN;
    double area = 0;
    if (bounds == null) {
      wkt.append(" EMPTY");

    } else {
      minX = bounds[0];
      maxX = bounds[axisCount];
      if (axisCount > 1) {
        minY = bounds[1];
        maxY = bounds[axisCount + 1];
        width = Math.abs(maxX - minX);
        height = Math.abs(maxY - minY);
        area = width * height;
      } else {
        area = 0;
      }
      if (axisCount == 3) {
        wkt.append(" Z");
      } else if (axisCount == 4) {
        wkt.append(" ZM");
      } else if (axisCount != 2) {
        wkt.append(" ");
        wkt.append(axisCount);
      }
      wkt.append("(");
      for (int axisIndex = 0; axisIndex < axisCount; axisIndex++) {
        if (axisIndex > 0) {
          wkt.append(',');
        }
        wkt.append(Doubles.toString(bounds[axisIndex]));
      }
      wkt.append(' ');
      for (int axisIndex = 0; axisIndex < axisCount; axisIndex++) {
        if (axisIndex > 0) {
          wkt.append(',');
        }
        wkt.append(Doubles.toString(bounds[axisCount + axisIndex]));
      }
      wkt.append(')');
      for (int i = 0; i < axisCount; i++) {
        assertMinMax(boundingBox, i, bounds[i], bounds[axisCount + i]);

        Assert.assertEquals("Minimum " + i, Quantities.getQuantity(bounds[i], unit),
          boundingBox.getMinimum(i));
        Assert.assertEquals("Maximum" + i, Quantities.getQuantity(bounds[axisCount + i], unit),
          boundingBox.getMaximum(i));

        Assert.assertEquals("Minimum " + i, bounds[i], boundingBox.getMinimum(i, unit), 0);
        Assert.assertEquals("Maximum " + i, bounds[axisCount + i], boundingBox.getMaximum(i, unit),
          0);
      }
    }
    Assert.assertEquals("MinX", minX, boundingBox.getMinX(), 0);
    Assert.assertEquals("MaxX", maxX, boundingBox.getMaxX(), 0);

    Assert.assertEquals("MinimumX", Quantities.getQuantity(minX, unit), boundingBox.getMinimum(0));
    Assert.assertEquals("MaximumX", Quantities.getQuantity(maxX, unit), boundingBox.getMaximum(0));

    Assert.assertEquals("MinimumX", minX, boundingBox.getMinimum(0, unit), 0);
    Assert.assertEquals("MaximumX", maxX, boundingBox.getMaximum(0, unit), 0);

    Assert.assertEquals("MinY", minY, boundingBox.getMinY(), 0);
    Assert.assertEquals("MaxY", maxY, boundingBox.getMaxY(), 0);

    Assert.assertEquals("MinimumY", Quantities.getQuantity(minY, unit), boundingBox.getMinimum(1));
    Assert.assertEquals("MaximumY", Quantities.getQuantity(maxY, unit), boundingBox.getMaximum(1));

    Assert.assertEquals("MinimumY", minY, boundingBox.getMinimum(1, unit), 0);
    Assert.assertEquals("MaximumY", maxY, boundingBox.getMaximum(1, unit), 0);

    Assert.assertEquals("WKT", wkt.toString(), boundingBox.toString());
    Assert.assertEquals("Area", area, boundingBox.getArea(), 0);
    Assert.assertEquals("Width", width, boundingBox.getWidth(), 0);
    Assert.assertEquals("Width", width,
      QuantityType.doubleValue(boundingBox.getWidthLength(), lengthUnit), 0);
    Assert.assertEquals("Width", Quantities.getQuantity(width, lengthUnit),
      boundingBox.getWidthLength());
    Assert.assertEquals("Height", height, boundingBox.getHeight(), 0);
    Assert.assertEquals("Height", height,
      QuantityType.doubleValue(boundingBox.getHeightLength(), lengthUnit), 0);
    Assert.assertEquals("Height", Quantities.getQuantity(height, lengthUnit),
      boundingBox.getHeightLength());

    Assert.assertEquals("Aspect Ratio", width / height, boundingBox.getAspectRatio(), 0);

  }

  private void assertMinMax(final BoundingBox boundingBox, final int axisIndex,
    final double expectedMin, final double expectedMax) {
    final double min = boundingBox.getMin(axisIndex);
    Assert.assertEquals("Min " + axisIndex, expectedMin, min, 0);
    final double max = boundingBox.getMax(axisIndex);
    Assert.assertEquals("Max " + axisIndex, expectedMax, max, 0);
  }

  @Test
  public void testConstructorCoordinatesArray() {
    final BoundingBox emptyNull = new BoundingBoxDoubleGf((Point[])null);
    assertEnvelope(emptyNull, null, true, 0, NULL_BOUNDS);

    final BoundingBox emptyList = new BoundingBoxDoubleGf(new Point[0]);
    assertEnvelope(emptyList, null, true, 0, NULL_BOUNDS);

    final BoundingBox emptyListWithNulls = new BoundingBoxDoubleGf((Point)null);
    assertEnvelope(emptyListWithNulls, null, true, 0, NULL_BOUNDS);

    // Different number of axis and values
    for (int axisCount = 2; axisCount < 6; axisCount++) {
      for (int valueCount = 1; valueCount < 10; valueCount++) {
        Point[] points = new Point[valueCount];
        final double[] bounds = BoundingBoxUtil.newBounds(axisCount);
        for (int i = 0; i < valueCount; i++) {
          final double[] values = new double[axisCount];
          for (int axisIndex = 0; axisIndex < axisCount; axisIndex++) {
            final double value = Math.random() * 360 - 180;
            values[axisIndex] = value;
            final double min = bounds[axisIndex];
            if (Double.isNaN(min) || value < min) {
              bounds[axisIndex] = value;
            }
            final double max = bounds[axisCount + axisIndex];
            if (Double.isNaN(max) || value > max) {
              bounds[axisCount + axisIndex] = value;
            }
          }
          points[i] = new PointDouble(values);
        }
        final BoundingBoxDoubleGf noGeometryFactory = new BoundingBoxDoubleGf(points);
        assertEnvelope(noGeometryFactory, null, false, axisCount, bounds);

        final GeometryFactory gfFloating = GeometryFactory.floating(4326, axisCount);
        assertEnvelope(new BoundingBoxDoubleGf(gfFloating, points), gfFloating, false, axisCount,
          bounds);

        final GeometryFactory gfFixed = GeometryFactory.fixed(4326, axisCount, 10.0, 10.0);

        points = gfFixed.getPrecise(points);
        final double[] boundsPrecise = gfFixed.copyPrecise(bounds);
        assertEnvelope(new BoundingBoxDoubleGf(gfFixed, points), gfFixed, false, axisCount,
          boundsPrecise);
      }
    }
  }

  @Test
  public void testConstructorDoubleArray() {
    // Empty
    assertEnvelope(new BoundingBoxDoubleGf(0), null, true, 0, NULL_BOUNDS);
    assertEnvelope(new BoundingBoxDoubleGf(-1), null, true, 0, NULL_BOUNDS);
    assertEnvelope(new BoundingBoxDoubleGf(2), null, true, 0, NULL_BOUNDS);
    assertEnvelope(new BoundingBoxDoubleGf(2, NULL_BOUNDS), null, true, 0, NULL_BOUNDS);

    // Different number of axis and values
    for (int axisCount = 1; axisCount < 6; axisCount++) {
      for (int valueCount = 1; valueCount < 10; valueCount++) {
        final double[] values = new double[axisCount * valueCount];
        final double[] bounds = BoundingBoxUtil.newBounds(axisCount);
        for (int i = 0; i < valueCount; i++) {
          for (int axisIndex = 0; axisIndex < axisCount; axisIndex++) {
            final double value = Math.random() * 360 - 180;
            values[i * axisCount + axisIndex] = value;
            final double min = bounds[axisIndex];
            if (Double.isNaN(min) || value < min) {
              bounds[axisIndex] = value;
            }
            final double max = bounds[axisCount + axisIndex];
            if (Double.isNaN(max) || value > max) {
              bounds[axisCount + axisIndex] = value;
            }
          }
        }
        final BoundingBoxDoubleGf noGeometryFactory = new BoundingBoxDoubleGf(axisCount, values);
        assertEnvelope(noGeometryFactory, null, false, axisCount, bounds);

        if (axisCount > 1) {
          final GeometryFactory gfFloating = GeometryFactory.floating(4326, axisCount);
          assertEnvelope(new BoundingBoxDoubleGf(gfFloating, axisCount, values), gfFloating, false,
            axisCount, bounds);

          final GeometryFactory gfFixed = GeometryFactory.fixed(4326, axisCount, 10.0, 10.0);
          final double[] valuesPrecise = gfFixed.copyPrecise(values);
          final double[] boundsPrecise = gfFixed.copyPrecise(bounds);
          assertEnvelope(new BoundingBoxDoubleGf(gfFixed, axisCount, valuesPrecise), gfFixed, false,
            axisCount, boundsPrecise);
        }
      }
    }
  }

  @Test
  public void testConstructorEmpty() {
    final BoundingBox empty = BoundingBox.EMPTY;
    assertEnvelope(empty, null, true, 0, NULL_BOUNDS);

    final BoundingBox emptyNullGeometryFactory = new BoundingBoxDoubleGf((GeometryFactory)null);
    assertEnvelope(emptyNullGeometryFactory, null, true, 0, NULL_BOUNDS);

    final BoundingBox emptyWithGeometryFactory = new BoundingBoxDoubleGf(UTM10_GF_2_FLOATING);
    assertEnvelope(emptyWithGeometryFactory, UTM10_GF_2_FLOATING, true, 0, NULL_BOUNDS);
  }

  @Test
  public void testConstructorIterable() {
    final BoundingBox emptyNull = new BoundingBoxDoubleGf((Iterable<Point>)null);
    assertEnvelope(emptyNull, null, true, 0, NULL_BOUNDS);

    final BoundingBox emptyList = new BoundingBoxDoubleGf(new ArrayList<Point>());
    assertEnvelope(emptyList, null, true, 0, NULL_BOUNDS);

    final BoundingBox emptyListWithNulls = new BoundingBoxDoubleGf(
      Collections.<Point> singleton(null));
    assertEnvelope(emptyListWithNulls, null, true, 0, NULL_BOUNDS);

    final BoundingBox emptyNullCoordinatesList = new BoundingBoxDoubleGf((Iterable<Point>)null);
    assertEnvelope(emptyNullCoordinatesList, null, true, 0, NULL_BOUNDS);

    final BoundingBox emptyCoordinatesList = new BoundingBoxDoubleGf(new ArrayList<Point>());
    assertEnvelope(emptyCoordinatesList, null, true, 0, NULL_BOUNDS);

    // Different number of axis and values
    for (int axisCount = 2; axisCount < 6; axisCount++) {
      for (int valueCount = 1; valueCount < 10; valueCount++) {
        final List<Point> points = new ArrayList<>();
        final double[] bounds = BoundingBoxUtil.newBounds(axisCount);
        for (int i = 0; i < valueCount; i++) {
          final double[] values = new double[axisCount];
          for (int axisIndex = 0; axisIndex < axisCount; axisIndex++) {
            final double value = Math.random() * 360 - 180;
            values[axisIndex] = value;
            final double min = bounds[axisIndex];
            if (Double.isNaN(min) || value < min) {
              bounds[axisIndex] = value;
            }
            final double max = bounds[axisCount + axisIndex];
            if (Double.isNaN(max) || value > max) {
              bounds[axisCount + axisIndex] = value;
            }
          }
          points.add(new PointDouble(values));
        }
        final BoundingBoxDoubleGf noGeometryFactory = new BoundingBoxDoubleGf(points);
        assertEnvelope(noGeometryFactory, null, false, axisCount, bounds);

        final GeometryFactory gfFloating = GeometryFactory.floating(4326, axisCount);
        assertEnvelope(new BoundingBoxDoubleGf(gfFloating, points), gfFloating, false, axisCount,
          bounds);

        final GeometryFactory gfFixed = GeometryFactory.fixed(4326, axisCount, 10.0, 10.0);

        final double[] boundsPrecise = gfFixed.copyPrecise(bounds);
        assertEnvelope(new BoundingBoxDoubleGf(gfFixed, points), gfFixed, false, axisCount,
          boundsPrecise);
      }
    }
  }

}
