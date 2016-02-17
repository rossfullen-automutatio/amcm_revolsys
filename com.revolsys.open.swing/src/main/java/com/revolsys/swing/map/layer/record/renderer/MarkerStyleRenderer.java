package com.revolsys.swing.map.layer.record.renderer;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.beans.PropertyChangeEvent;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.Icon;

import com.revolsys.geometry.model.BoundingBox;
import com.revolsys.geometry.model.Geometry;
import com.revolsys.geometry.model.GeometryFactory;
import com.revolsys.geometry.model.LineString;
import com.revolsys.geometry.model.LinearRing;
import com.revolsys.geometry.model.Point;
import com.revolsys.geometry.model.Polygon;
import com.revolsys.geometry.model.coordinates.LineSegmentUtil;
import com.revolsys.geometry.model.coordinates.PointWithOrientation;
import com.revolsys.geometry.model.vertex.Vertex;
import com.revolsys.io.BaseCloseable;
import com.revolsys.swing.Icons;
import com.revolsys.swing.component.Form;
import com.revolsys.swing.map.Viewport2D;
import com.revolsys.swing.map.layer.AbstractLayer;
import com.revolsys.swing.map.layer.LayerRenderer;
import com.revolsys.swing.map.layer.record.AbstractRecordLayer;
import com.revolsys.swing.map.layer.record.LayerRecord;
import com.revolsys.swing.map.layer.record.style.MarkerStyle;
import com.revolsys.swing.map.layer.record.style.marker.Marker;
import com.revolsys.swing.map.layer.record.style.panel.MarkerStylePanel;
import com.revolsys.util.Property;

public class MarkerStyleRenderer extends AbstractRecordLayerRenderer {
  private static final Icon ICON = Icons.getIcon("style_marker");

  public static <G extends Geometry> G getGeometry(final Viewport2D viewport, final G geometry) {
    final BoundingBox viewExtent = viewport.getBoundingBox();
    if (geometry != null) {
      if (!viewExtent.isEmpty()) {
        final BoundingBox geometryExtent = geometry.getBoundingBox();
        if (geometryExtent.intersects(viewExtent)) {
          final GeometryFactory geometryFactory = viewport.getGeometryFactory();
          return geometry.convertGeometry(geometryFactory);
        }
      }
    }
    return null;
  }

  public static PointWithOrientation getMarkerLocation(final Viewport2D viewport,
    final Geometry geometry, final MarkerStyle style) {
    final GeometryFactory viewportGeometryFactory = viewport.getGeometryFactory();
    if (viewportGeometryFactory != null && geometry != null && !geometry.isEmpty()) {
      Point point = null;
      double orientation = 0;
      final String placementType = style.getMarkerPlacementType();
      final int vertexCount = geometry.getVertexCount();
      if (vertexCount > 1) {
        final Matcher matcher = Pattern.compile("point\\((.*)\\)").matcher(placementType);
        final boolean matches = matcher.matches();
        if (matches) {
          final String argument = matcher.group(1);
          Vertex vertex;
          if (argument.matches("n(?:\\s*-\\s*(\\d+)\\s*)?")) {
            final String indexString = argument.replaceAll("[^0-9\\-]+", "");
            int index;
            if (indexString.length() > 0) {
              index = Integer.parseInt(indexString);
            } else {
              index = -1;
            }
            if (vertexCount - index < 0) {
              index = 0;
            }
            vertex = geometry.getVertex(index);
          } else {
            int index = Integer.parseInt(argument);
            if (index < 0) {
              index = 0;
            } else if (index >= vertexCount) {
              index = vertexCount - 1;
            }
            vertex = geometry.getVertex(index);
          }
          point = vertex.convertGeometry(viewportGeometryFactory, 2);
          orientation = vertex.getOrientaton();
        } else {
          if (geometry instanceof LineString) {
            final Geometry projectedGeometry = geometry.convertGeometry(viewportGeometryFactory);
            final double totalLength = projectedGeometry.getLength();
            final double centreLength = totalLength / 2;
            double currentLength = 0;
            for (int i = 1; i < vertexCount && currentLength < centreLength; i++) {
              final Vertex p1 = projectedGeometry.getVertex(i - 1);
              final Vertex p2 = projectedGeometry.getVertex(i);
              final double segmentLength = p1.distance(p2);
              if (segmentLength + currentLength >= centreLength) {
                point = LineSegmentUtil.project(2, p1, p2,
                  (centreLength - currentLength) / segmentLength);
                // TODO parameter to use orientation or not
                orientation = p1.getOrientaton();
              }
              currentLength += segmentLength;
            }
          } else {
            point = geometry.getPointWithin();
          }
        }
      } else {
        point = geometry.getPoint();
      }
      if (point != null) {
        point = point.convertGeometry(viewportGeometryFactory, 2);
        if (viewport.getBoundingBox().covers(point)) {
          return new PointWithOrientation(point, orientation);
        }
      }
    }
    return null;
  }

  public static void renderMarker(final Viewport2D viewport, final Geometry geometry,
    final MarkerStyle style) {
    try (
      BaseCloseable transformClosable = viewport.setUseModelCoordinates(false)) {
      final Graphics2D graphics = viewport.getGraphics();
      if (graphics != null && geometry != null) {
        if ("vertices".equals(style.getMarkerPlacementType())) {
          renderMarkerVertices(viewport, graphics, geometry, style);
        } else {
          for (int i = 0; i < geometry.getGeometryCount(); i++) {
            final Geometry part = geometry.getGeometry(i);
            if (part instanceof Point) {
              final Point point = (Point)part;
              renderMarker(viewport, graphics, point, style);
            } else if (part instanceof LineString) {
              final LineString line = (LineString)part;
              renderMarker(viewport, graphics, line, style);
            } else if (part instanceof Polygon) {
              final Polygon polygon = (Polygon)part;
              renderMarker(viewport, graphics, polygon, style);
            }
          }
        }
      }
    }
  }

  private static void renderMarker(final Viewport2D viewport, final Graphics2D graphics,
    final LineString line, final MarkerStyle style) {
    final PointWithOrientation point = getMarkerLocation(viewport, line, style);
    if (point != null) {
      final double orientation = point.getOrientation();
      renderMarker(viewport, graphics, point, style, orientation);
    }
  }

  public static final void renderMarker(final Viewport2D viewport, final Graphics2D graphics,
    Point point, final MarkerStyle style) {
    point = getGeometry(viewport, point);
    if (Property.hasValue(point)) {
      try (
        BaseCloseable transformClosable = viewport.setUseModelCoordinates(graphics, false)) {
        renderMarker(viewport, graphics, point, style, 0);
      }
    }
  }

  /**
   * Point must be in the same geometry factory as the view.
   *
   * @param viewport
   * @param graphics
   * @param point
   * @param style
   */
  public static void renderMarker(final Viewport2D viewport, final Graphics2D graphics,
    final Point point, final MarkerStyle style, final double orientation) {
    if (viewport.getBoundingBox().covers(point)) {
      final Paint paint = graphics.getPaint();
      try {
        final Marker marker = style.getMarker();
        final double x = point.getX();
        final double y = point.getY();
        marker.render(viewport, graphics, style, x, y, orientation);
      } finally {
        graphics.setPaint(paint);
      }
    }
  }

  private static void renderMarker(final Viewport2D viewport, final Graphics2D graphics,
    final Polygon polygon, final MarkerStyle style) {
    final Point point = polygon.getPointWithin();
    renderMarker(viewport, graphics, point, style);
  }

  /**
   * Point must be in the same geometry factory as the view.
   *
   * @param viewport
   * @param graphics
   * @param style
   * @param point
   */
  private static void renderMarkers(final Viewport2D viewport, final Graphics2D graphics,
    LineString line, final MarkerStyle style) {
    line = viewport.convertGeometry(line, 2);
    if (Property.hasValue(line)) {
      final Marker marker = style.getMarker();
      final String orientationType = style.getMarkerOrientationType();
      final boolean hasOrientationType = !"none".equals(orientationType);
      final Paint paint = graphics.getPaint();
      try (
        BaseCloseable transformClosable = viewport.setUseModelCoordinates(graphics, false)) {
        for (final Vertex vertex : line.vertices()) {
          final double x = vertex.getX();
          final double y = vertex.getY();
          double orientation = 0;
          if (hasOrientationType) {
            orientation = vertex.getOrientaton();
          }
          marker.render(viewport, graphics, style, x, y, orientation);
        }
      } finally {
        graphics.setPaint(paint);
      }
    }
  }

  public static void renderMarkers(final Viewport2D viewport, final Graphics2D graphics,
    LineString line, final MarkerStyle styleFirst, final MarkerStyle styleLast,
    final MarkerStyle styleVertex) {
    line = viewport.convertGeometry(line, 2);
    if (Property.hasValue(line)) {
      final Paint paint = graphics.getPaint();
      try (
        BaseCloseable transformClosable = viewport.setUseModelCoordinates(graphics, false)) {
        for (final Vertex vertex : line.vertices()) {
          MarkerStyle style;
          if (vertex.isFrom()) {
            style = styleFirst;
          } else if (vertex.isTo()) {
            style = styleLast;
          } else {
            style = styleVertex;
          }
          if (style != null) {
            final double x = vertex.getX();
            final double y = vertex.getY();
            final double orientation = vertex.getOrientaton();
            final Marker marker = style.getMarker();
            marker.render(viewport, graphics, style, x, y, orientation);
          }
        }
      } finally {
        graphics.setPaint(paint);
      }
    }
  }

  public static final void renderMarkerVertices(final Viewport2D viewport,
    final Graphics2D graphics, Geometry geometry, final MarkerStyle style) {
    try (
      BaseCloseable transformClosable = viewport.setUseModelCoordinates(false)) {
      geometry = getGeometry(viewport, geometry);
      if (Property.hasValue(geometry)) {
        for (final Geometry part : geometry.geometries()) {
          if (part instanceof Point) {
            final Point point = (Point)part;
            renderMarker(viewport, graphics, point, style);
          } else if (part instanceof LineString) {
            final LineString line = (LineString)part;
            renderMarkers(viewport, graphics, line, style);
          } else if (part instanceof Polygon) {
            final Polygon polygon = (Polygon)part;
            for (final LinearRing ring : polygon.rings()) {
              renderMarkers(viewport, graphics, ring, style);
            }
          }
        }
      }
    }
  }

  private MarkerStyle style;

  public MarkerStyleRenderer(final AbstractRecordLayer layer, final LayerRenderer<?> parent) {
    this(layer, parent, new MarkerStyle());
  }

  public MarkerStyleRenderer(final AbstractRecordLayer layer, final LayerRenderer<?> parent,
    final Map<String, Object> geometryStyle) {
    super("markerStyle", "Marker Style", layer, parent, geometryStyle);
    setStyle(new MarkerStyle(geometryStyle));
    setIcon(ICON);
  }

  public MarkerStyleRenderer(final AbstractRecordLayer layer, final LayerRenderer<?> parent,
    final MarkerStyle style) {
    super("markerStyle", "Marker Style", layer, parent);
    setStyle(style);
    setIcon(ICON);
  }

  public MarkerStyleRenderer(final AbstractRecordLayer layer, final MarkerStyle style) {
    this(layer, null, style);
  }

  @Override
  public MarkerStyleRenderer clone() {
    final MarkerStyleRenderer clone = (MarkerStyleRenderer)super.clone();
    if (this.style != null) {
      clone.setStyle(this.style.clone());
    }
    return clone;
  }

  @Override
  public Icon getIcon() {
    final Marker marker = this.style.getMarker();
    final Icon icon = marker.getIcon(this.style);
    if (icon == null) {
      return super.getIcon();
    } else {
      return icon;
    }
  }

  public MarkerStyle getStyle() {
    return this.style;
  }

  @Override
  public Form newStylePanel() {
    return new MarkerStylePanel(this);
  }

  @Override
  public void propertyChange(final PropertyChangeEvent event) {
    final Object source = event.getSource();
    if (source == this.style) {
      final Icon icon = getIcon();
      firePropertyChange("icon", null, icon);
    }
    super.propertyChange(event);
  }

  @Override
  public void renderRecord(final Viewport2D viewport, final BoundingBox visibleArea,
    final AbstractLayer layer, final LayerRecord record) {
    if (isVisible(record)) {
      final Geometry geometry = record.getGeometry();
      renderMarker(viewport, geometry, this.style);
    }
  }

  public void setStyle(final MarkerStyle style) {
    if (this.style != null) {
      this.style.removePropertyChangeListener(this);
    }
    this.style = style;
    if (this.style != null) {
      this.style.addPropertyChangeListener(this);
    }
    firePropertyChange("style", null, style);
  }

  @Override
  public Map<String, Object> toMap() {
    final Map<String, Object> map = super.toMap();
    if (this.style != null) {
      final Map<String, Object> styleMap = this.style.toMap();
      map.putAll(styleMap);
    }
    return map;
  }
}
