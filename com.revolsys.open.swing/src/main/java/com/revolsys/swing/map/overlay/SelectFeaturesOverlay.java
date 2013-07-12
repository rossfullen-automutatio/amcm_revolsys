package com.revolsys.swing.map.overlay;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.beans.PropertyChangeEvent;
import java.util.Collection;

import javax.swing.SwingUtilities;

import org.jdesktop.swingx.color.ColorUtil;

import com.revolsys.awt.WebColors;
import com.revolsys.gis.cs.BoundingBox;
import com.revolsys.gis.cs.GeometryFactory;
import com.revolsys.gis.jts.IsSimpleOp;
import com.revolsys.gis.jts.IsValidOp;
import com.revolsys.gis.model.coordinates.Coordinates;
import com.revolsys.swing.SwingWorkerManager;
import com.revolsys.swing.map.MapPanel;
import com.revolsys.swing.map.Viewport2D;
import com.revolsys.swing.map.layer.Layer;
import com.revolsys.swing.map.layer.LayerGroup;
import com.revolsys.swing.map.layer.Project;
import com.revolsys.swing.map.layer.dataobject.DataObjectLayer;
import com.revolsys.swing.map.layer.dataobject.LayerDataObject;
import com.revolsys.swing.map.layer.dataobject.renderer.GeometryStyleRenderer;
import com.revolsys.swing.map.layer.dataobject.renderer.MarkerStyleRenderer;
import com.revolsys.swing.map.layer.dataobject.style.GeometryStyle;
import com.revolsys.swing.map.layer.dataobject.style.MarkerStyle;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.operation.valid.TopologyValidationError;

@SuppressWarnings("serial")
public class SelectFeaturesOverlay extends AbstractOverlay {

  private static final Color COLOR = WebColors.Lime;

  private static final Color TRANSPARENT_COLOR = ColorUtil.setAlpha(COLOR, 127);

  private static final Color BOX_OUTLINE_COLOR = new Color(COLOR.getRed() / 2,
    COLOR.getGreen() / 2, COLOR.getBlue() / 2);

  private static final Color BOX_FILL_COLOR = ColorUtil.setAlpha(
    BOX_OUTLINE_COLOR, 127);

  private static final BasicStroke BOX_STROKE = new BasicStroke(2,
    BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 2, new float[] {
      6, 6
    }, 0f);

  private static final GeometryStyle HIGHLIGHT_STYLE = GeometryStyle.polygon(
    COLOR, 3, TRANSPARENT_COLOR);

  private static final GeometryStyle OUTLINE_STYLE = GeometryStyle.line(WebColors.Black);

  private static final MarkerStyle VERTEX_STYLE = MarkerStyle.marker("ellipse",
    6, new Color(0, 0, 0, 127), 1, TRANSPARENT_COLOR);;

  private static final MarkerStyle ERROR_STYLE = MarkerStyle.marker("ellipse",
    7, WebColors.Yellow, 1, WebColors.Red);

  private Double selectBox;

  private java.awt.Point selectBoxFirstPoint;

  public SelectFeaturesOverlay(final MapPanel map) {
    super(map);
  }

  protected Collection<LayerDataObject> getSelectedObjects(
    final DataObjectLayer layer) {
    return layer.getSelectedRecords();
  }

  protected boolean isSelectable(final DataObjectLayer dataObjectLayer) {
    return dataObjectLayer.isSelectable();
  }

  public boolean isSelectEvent(final MouseEvent event) {
    if (SwingUtilities.isLeftMouseButton(event)) {
      final boolean keyPress = event.isControlDown() || event.isMetaDown();
      return keyPress;
    }
    return false;
  }

  @Override
  public void keyPressed(final KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
      clearMapCursor();
      selectBox = null;
      selectBoxFirstPoint = null;
      repaint();
    }
  }

  @Override
  public void keyReleased(final KeyEvent e) {
  }

  @Override
  public void keyTyped(final KeyEvent e) {
  }

  @Override
  public void mouseClicked(final MouseEvent event) {
    if (event.getClickCount() == 1 && isSelectEvent(event)) {
      final int x = event.getX();
      final int y = event.getY();
      final double[] location = getViewport().toModelCoordinates(x, y);
      final GeometryFactory geometryFactory = getViewportGeometryFactory();
      BoundingBox boundingBox = new BoundingBox(geometryFactory, location[0],
        location[1]);
      final double modelUnitsPerViewUnit = getViewport().getModelUnitsPerViewUnit();
      boundingBox = boundingBox.expand(modelUnitsPerViewUnit * 5);
      SwingWorkerManager.execute("Select records", this, "selectRecords",
        boundingBox);
      event.consume();
    }
  }

  @Override
  public void mouseDragged(final MouseEvent event) {
    if (selectBoxFirstPoint != null) {
      selectBoxDrag(event);
      event.consume();
    }
  }

  @Override
  public void mousePressed(final MouseEvent event) {
    if (isSelectEvent(event)) {
      selectBoxStart(event);
    }
  }

  @Override
  public void mouseReleased(final MouseEvent event) {
    if (selectBoxFirstPoint != null) {
      selectBoxFinish(event);
    }
  }

  protected void paint(final Graphics2D graphics2d, final LayerGroup layerGroup) {
    for (final Layer layer : layerGroup.getLayers()) {
      if (layer instanceof LayerGroup) {
        final LayerGroup childGroup = (LayerGroup)layer;
        paint(graphics2d, childGroup);
      } else if (layer instanceof DataObjectLayer) {
        final DataObjectLayer dataObjectLayer = (DataObjectLayer)layer;
        for (final LayerDataObject object : getSelectedObjects(dataObjectLayer)) {
          if (object != null && dataObjectLayer.isVisible(object)) {
            final Geometry geometry = object.getGeometryValue();
            paintSelected(graphics2d, geometry, HIGHLIGHT_STYLE, OUTLINE_STYLE,
              VERTEX_STYLE);
          }
        }
      }
    }
  }

  @Override
  public void paintComponent(final Graphics2D graphics) {
    final Project layerGroup = getProject();
    paint(graphics, layerGroup);
    paintSelectBox(graphics);
  }

  protected void paintSelectBox(final Graphics2D graphics2d) {
    if (selectBox != null) {
      graphics2d.setColor(BOX_OUTLINE_COLOR);
      graphics2d.setStroke(BOX_STROKE);
      graphics2d.draw(selectBox);
      graphics2d.setPaint(BOX_FILL_COLOR);
      graphics2d.fill(selectBox);
    }
  }

  protected void paintSelected(final Graphics2D graphics2d, Geometry geometry,
    final GeometryStyle highlightStyle, final GeometryStyle outlineStyle,
    final MarkerStyle vertexStyle) {
    if (geometry != null && !geometry.isEmpty()) {
      final Viewport2D viewport = getViewport();
      final GeometryFactory viewportGeometryFactory = getViewportGeometryFactory();
      geometry = viewport.getGeometry(geometry);
      MarkerStyleRenderer.renderMarkerVertices(viewport, graphics2d, geometry,
        vertexStyle);
      GeometryStyleRenderer.renderGeometry(viewport, graphics2d, geometry,
        highlightStyle);
      GeometryStyleRenderer.renderOutline(viewport, graphics2d, geometry,
        outlineStyle);
      final IsValidOp validOp = new IsValidOp(geometry);
      if (validOp.isValid()) {
        final IsSimpleOp simpleOp = new IsSimpleOp(geometry);
        if (!simpleOp.isSimple()) {
          for (final Coordinates coordinates : simpleOp.getNonSimplePoints()) {
            final Point point = viewportGeometryFactory.createPoint(coordinates);
            MarkerStyleRenderer.renderMarker(viewport, graphics2d, point,
              ERROR_STYLE);
          }
        }
      } else {
        for (final TopologyValidationError error : validOp.getErrors()) {
          final Point point = viewportGeometryFactory.createPoint(error.getCoordinate());
          MarkerStyleRenderer.renderMarker(viewport, graphics2d, point,
            ERROR_STYLE);
        }
      }
    }
  }

  @Override
  public void propertyChange(final PropertyChangeEvent event) {
    final String propertyName = event.getPropertyName();
    if ("layers".equals(propertyName)) {
      repaint();
    } else if ("selectable".equals(propertyName)) {
      repaint();
    } else if ("visible".equals(propertyName)) {
      repaint();
    } else if ("editable".equals(propertyName)) {
      repaint();
    } else if ("selected".equals(propertyName)) {
      clearUndoHistory();
    }
  }

  public void selectBoxDrag(final MouseEvent event) {
    final double width = Math.abs(event.getX() - selectBoxFirstPoint.getX());
    final double height = Math.abs(event.getY() - selectBoxFirstPoint.getY());
    final java.awt.Point topLeft = new java.awt.Point(); // java.awt.Point
    if (selectBoxFirstPoint.getX() < event.getX()) {
      topLeft.setLocation(selectBoxFirstPoint.getX(), 0);
    } else {
      topLeft.setLocation(event.getX(), 0);
    }

    if (selectBoxFirstPoint.getY() < event.getY()) {
      topLeft.setLocation(topLeft.getX(), selectBoxFirstPoint.getY());
    } else {
      topLeft.setLocation(topLeft.getX(), event.getY());
    }
    selectBox.setRect(topLeft.getX(), topLeft.getY(), width, height);
    event.consume();
    repaint();
  }

  public void selectBoxFinish(final MouseEvent event) {
    // Convert first point to envelope top left in map coords.
    final int minX = (int)selectBox.getMinX();
    final int minY = (int)selectBox.getMinY();
    final Point topLeft = getViewport().toModelPoint(minX, minY);

    // Convert second point to envelope bottom right in map coords.
    final int maxX = (int)selectBox.getMaxX();
    final int maxY = (int)selectBox.getMaxY();
    final Point bottomRight = getViewport().toModelPoint(maxX, maxY);

    final GeometryFactory geometryFactory = getMap().getGeometryFactory();
    final BoundingBox boundingBox = new BoundingBox(geometryFactory,
      topLeft.getX(), topLeft.getY(), bottomRight.getX(), bottomRight.getY());

    selectBoxFirstPoint = null;
    selectBox = null;
    clearMapCursor();
    repaint();
    SwingWorkerManager.execute("Select records", this, "selectRecords",
      boundingBox);
    event.consume();
  }

  public void selectBoxStart(final MouseEvent event) {
    setMapCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
    selectBoxFirstPoint = event.getPoint();
    selectBox = new Rectangle2D.Double();
    event.consume();
  }

  public void selectRecords(final BoundingBox boundingBox) {
    final Project project = getProject();
    selectRecords(project, boundingBox);
    final LayerRendererOverlay overlay = getMap().getLayerOverlay();
    overlay.redraw();
  }

  private void selectRecords(final LayerGroup group,
    final BoundingBox boundingBox) {

    final double scale = getViewport().getScale();
    for (final Layer layer : group.getLayers()) {
      if (layer instanceof LayerGroup) {
        final LayerGroup childGroup = (LayerGroup)layer;
        selectRecords(childGroup, boundingBox);
      } else if (layer instanceof DataObjectLayer) {
        final DataObjectLayer dataObjectLayer = (DataObjectLayer)layer;
        if (dataObjectLayer.isSelectable(scale)) {
          dataObjectLayer.setSelectedRecords(boundingBox);
          dataObjectLayer.showRecordsTable();
        } else {
          dataObjectLayer.clearSelectedRecords();
        }
      }
    }
  }
}
