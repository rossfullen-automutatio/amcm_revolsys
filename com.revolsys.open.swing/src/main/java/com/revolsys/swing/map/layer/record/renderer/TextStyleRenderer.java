package com.revolsys.swing.map.layer.record.renderer;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.revolsys.collection.map.MapEx;
import com.revolsys.geometry.model.BoundingBox;
import com.revolsys.geometry.model.Geometry;
import com.revolsys.swing.Icons;
import com.revolsys.swing.component.Form;
import com.revolsys.swing.map.layer.LayerRenderer;
import com.revolsys.swing.map.layer.record.AbstractRecordLayer;
import com.revolsys.swing.map.layer.record.LayerRecord;
import com.revolsys.swing.map.layer.record.style.TextStyle;
import com.revolsys.swing.map.layer.record.style.panel.TextStylePanel;
import com.revolsys.swing.map.view.ViewRenderer;

public class TextStyleRenderer extends AbstractRecordLayerRenderer {

  private static final Icon ICON = Icons.getIcon("style_text");

  private TextStyle style = new TextStyle();

  public TextStyleRenderer(final AbstractRecordLayer layer, final LayerRenderer<?> parent) {
    super("textStyle", "Text Style", layer, parent);
    setIcon(newIcon());
  }

  public TextStyleRenderer(final AbstractRecordLayer layer, final TextStyle textStyle) {
    super("textStyle", "Text Style");
    setStyle(textStyle);
  }

  public TextStyleRenderer(final Map<String, ? extends Object> properties) {
    super("textStyle", "Text Style");
    setIcon(ICON);
    setProperties(properties);
  }

  @Override
  public TextStyleRenderer clone() {
    final TextStyleRenderer clone = (TextStyleRenderer)super.clone();
    clone.setStyle(this.style.clone());
    return clone;
  }

  @Override
  public Icon getIcon() {
    Icon icon = super.getIcon();
    if (icon == ICON) {
      icon = newIcon();
      setIcon(icon);
    }
    return icon;
  }

  public TextStyle getStyle() {
    return this.style;
  }

  @Override
  public Icon newIcon() {
    final BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
    final Graphics2D graphics = image.createGraphics();
    this.style.drawTextIcon(graphics, 12);
    graphics.dispose();
    final Icon icon = new ImageIcon(image);
    return icon;

  }

  @Override
  public Form newStylePanel() {
    return new TextStylePanel(this);
  }

  @Override
  public void propertyChange(final PropertyChangeEvent event) {
    final Object source = event.getSource();
    if (source == this.style) {
      refreshIcon();
    }
    super.propertyChange(event);
  }

  @Override
  public void renderRecord(final ViewRenderer view, final BoundingBox visibleArea,
    final AbstractRecordLayer layer, final LayerRecord record) {
    final Geometry geometry = record.getGeometry();
    view.drawText(record, geometry, this.style);
  }

  @Override
  public void setProperties(final Map<String, ? extends Object> properties) {
    super.setProperties(properties);
    if (this.style != null) {
      this.style.setProperties(properties);
    }
  }

  public void setStyle(final TextStyle style) {
    if (this.style != null) {
      this.style.removePropertyChangeListener(this);
    }
    this.style = style;
    if (this.style != null) {
      this.style.addPropertyChangeListener(this);
    }
    refreshIcon();
  }

  @Override
  public MapEx toMap() {
    final MapEx map = super.toMap();
    if (this.style != null) {
      final Map<String, Object> styleMap = this.style.toMap();
      map.putAll(styleMap);
    }
    return map;
  }
}
