package com.revolsys.gis.model.geometry;

import java.util.List;

import com.revolsys.collection.Visitor;
import com.revolsys.gis.cs.BoundingBox;
import com.revolsys.gis.cs.GeometryFactory;
import com.revolsys.gis.model.coordinates.Coordinates;
import com.revolsys.gis.model.coordinates.CoordinatesPrecisionModel;
import com.revolsys.gis.model.coordinates.CoordinatesUtil;
import com.revolsys.gis.model.coordinates.LineSegmentUtil;
import com.revolsys.gis.model.coordinates.list.AbstractCoordinatesList;
import com.revolsys.gis.model.coordinates.list.CoordinatesList;
import com.revolsys.gis.model.coordinates.list.CoordinatesListUtil;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Polygon;

public class LineSegment extends AbstractCoordinatesList {
  /**
   * 
   */
  private static final long serialVersionUID = 3905321662159212931L;

  private static final GeometryFactory FACTORY = GeometryFactory.getFactory();

  public static void visit(
    final LineString line,
    final Visitor<LineSegment> visitor) {
    final CoordinatesList coords = CoordinatesListUtil.get(line);
    Coordinates previousCoordinate = coords.get(0);
    for (int i = 1; i < coords.size(); i++) {
      final Coordinates coordinate = coords.get(i);
      final LineSegment segment = new LineSegment(
        GeometryFactory.getFactory(line), previousCoordinate, coordinate);
      if (segment.getLength() > 0) {
        if (!visitor.visit(segment)) {
          return;
        }
      }
      previousCoordinate = coordinate;
    }
  }

  private Coordinates coordinates1;

  private Coordinates coordinates2;

  private GeometryFactory geometryFactory;

  private LineString line;

  public LineSegment() {
  }

  public LineSegment(final Coordinates coordinates1,
    final Coordinates coordinates2) {
    this(FACTORY, coordinates1, coordinates2);
  }

  public LineSegment(final GeometryFactory geometryFactory,
    final Coordinates coordinates1, final Coordinates coordinates2) {
    this.geometryFactory = geometryFactory;
    this.coordinates1 = coordinates1;
    this.coordinates2 = coordinates2;
  }

  public LineSegment(final LineString line) {
    this(CoordinatesListUtil.get(line, 0), CoordinatesListUtil.get(line,
      line.getNumPoints() - 1));
  }

  public double angle() {
    return Math.atan2(coordinates2.getY() - coordinates1.getY(),
      coordinates2.getX() - coordinates1.getX());
  }

  @Override
  public LineSegment clone() {
    return new LineSegment(geometryFactory, coordinates1, coordinates2);
  }

  public boolean contains(final Coordinates coordinate) {
    if (get(0).equals(coordinate)) {
      return true;
    } else if (get(1).equals(coordinate)) {
      return true;
    } else {
      return false;
    }
  }

  public double distance(final Coordinates p) {
    return LineSegmentUtil.distance(coordinates1, coordinates2, p);
  }

  public LineSegment extend(final double startDistance, final double endDistance) {
    final double angle = angle();
    final Coordinates c1 = CoordinatesUtil.offset(coordinates1, angle,
      -startDistance);
    final Coordinates c2 = CoordinatesUtil.offset(coordinates2, angle,
      endDistance);
    return new LineSegment(c1, c2);

  }

  private Coordinates getCrossing(
    final Coordinates coordinates1,
    final Coordinates coordinates2,
    final BoundingBox boundingBox) {
    Coordinates intersection = null;
    final Polygon polygon = boundingBox.toPolygon(1);
    final LineString ring = polygon.getExteriorRing();
    final CoordinatesList points = CoordinatesListUtil.get(ring);
    for (int i = 0; i < 4; i++) {
      final Coordinates ringC1 = points.get(i);
      final Coordinates ringC2 = points.get(i);
      final Coordinates currentIntersection = LineSegmentUtil.intersection(
        coordinates1, coordinates2, ringC1, ringC2);
      if (!Double.isNaN(currentIntersection.getX())) {
        if (intersection == null) {
          intersection = currentIntersection;
        } else if (coordinates1.distance(currentIntersection) < coordinates1.distance(intersection)) {
          intersection = currentIntersection;
        }
      }
    }
    return intersection;
  }

  public double getElevation(final Coordinates point) {
    return CoordinatesUtil.getElevation(point, coordinates1, coordinates2);
  }

  public Envelope getEnvelope() {
    return getLine().getEnvelopeInternal();
  }

  public GeometryFactory getGeometryFactory() {
    return geometryFactory;
  }

  /**
   * Computes the length of the line segment.
   * 
   * @return the length of the line segment
   */
  public double getLength() {
    return coordinates1.distance(coordinates2);
  }

  public LineString getLine() {
    if (line == null) {
      line = geometryFactory.createLineString(this);
    }
    return line;
  }

  public byte getNumAxis() {
    return (byte)Math.max(coordinates1.getNumAxis(), coordinates2.getNumAxis());
  }

  public double getValue(final int index, final int axisIndex) {
    switch (index) {
      case 0:
        return coordinates1.getValue(axisIndex);
      case 1:
        return coordinates2.getValue(axisIndex);

      default:
        return 0;
    }
  }

  public LineSegment intersection(BoundingBox boundingBox) {
    boundingBox = boundingBox.convert(geometryFactory);
    final boolean contains1 = boundingBox.contains(coordinates1);
    final boolean contains2 = boundingBox.contains(coordinates2);
    if (contains1) {
      if (contains2) {
        return this;
      } else {
        final Coordinates c1 = coordinates1;
        final Coordinates c2 = getCrossing(coordinates2, coordinates1,
          boundingBox);
        return new LineSegment(geometryFactory, c1, c2);
      }
    } else {
      if (contains2) {
        final Coordinates c1 = getCrossing(coordinates1, coordinates2,
          boundingBox);
        final Coordinates c2 = coordinates2;
        return new LineSegment(geometryFactory, c1, c2);
      } else {
        final Coordinates c1 = getCrossing(coordinates1, coordinates2,
          boundingBox);
        final Coordinates c2 = getCrossing(coordinates2, coordinates1,
          boundingBox);
        return new LineSegment(geometryFactory, c1, c2);
      }
    }
  }

  public List<Coordinates> intersection(
    final CoordinatesPrecisionModel precisionModel,
    final LineSegment lineSegment2) {
    return LineSegmentUtil.intersection(precisionModel, coordinates1,
      coordinates2, lineSegment2.coordinates1, lineSegment2.coordinates2);
  }

  public Coordinates intersection(final LineSegment lineSegment2) {
    return LineSegmentUtil.intersection(coordinates1, coordinates2,
      lineSegment2.coordinates1, lineSegment2.coordinates2);
  }

  public boolean intersects(final BoundingBox boundingBox) {
    return (boundingBox.contains(coordinates1) || boundingBox.contains(coordinates2));
  }

  public boolean intersects(final Coordinates point, final double maxDistance) {
    return LineSegmentUtil.isPointOnLine(coordinates1, coordinates2, point,
      maxDistance);
  }

  public boolean isEmpty() {
    return coordinates1 == null || coordinates2 == null;
  }

  public Coordinates project(final Coordinates p) {
    return LineSegmentUtil.project(coordinates1, coordinates2,
      projectionFactor(p));
  }

  public double projectionFactor(final Coordinates p) {
    return LineSegmentUtil.projectionFactor(coordinates1, coordinates2, p);
  }

  public void setElevationOnPoint(
    final CoordinatesPrecisionModel precisionModel,
    final Coordinates point) {
    final double z = getElevation(point);
    point.setZ(z);
    precisionModel.makePrecise(point);
  }

  public void setValue(final int index, final int axisIndex, final double value) {
    switch (index) {
      case 0:
        coordinates1.setValue(axisIndex, value);
      break;
      case 1:
        coordinates2.setValue(axisIndex, value);
      break;
      default:
    }
  }

  public int size() {
    return 2;
  }

  @Override
  public String toString() {
    if (isEmpty()) {
      return "LINESTRING EMPTY";
    } else {
      return getLine().toString();
    }
  }

}
