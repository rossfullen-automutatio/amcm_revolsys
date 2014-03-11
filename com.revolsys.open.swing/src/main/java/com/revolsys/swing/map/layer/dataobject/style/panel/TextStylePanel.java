package com.revolsys.swing.map.layer.dataobject.style.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.jdesktop.swingx.VerticalLayout;

import com.revolsys.awt.WebColors;
import com.revolsys.gis.data.model.Attribute;
import com.revolsys.gis.data.model.DataObjectMetaData;
import com.revolsys.swing.SwingUtil;
import com.revolsys.swing.field.Field;
import com.revolsys.swing.map.layer.dataobject.AbstractDataObjectLayer;
import com.revolsys.swing.map.layer.dataobject.renderer.TextStyleRenderer;
import com.revolsys.swing.map.layer.dataobject.style.TextStyle;
import com.revolsys.util.JavaBeanUtil;
import com.revolsys.util.Property;

public class TextStylePanel extends BaseStylePanel implements
  PropertyChangeListener {
  private static final long serialVersionUID = 1L;

  private final TextStyleRenderer textStyleRenderer;

  private final TextStyle textStyle;

  private JPanel previews;

  public TextStylePanel(final TextStyleRenderer textStyleRenderer) {
    super(textStyleRenderer);

    this.textStyleRenderer = textStyleRenderer;
    this.textStyle = textStyleRenderer.getStyle().clone();
    final AbstractDataObjectLayer layer = textStyleRenderer.getLayer();
    final DataObjectMetaData metaData = layer.getMetaData();
    final Attribute geometryAttribute = metaData.getGeometryAttribute();

    if (geometryAttribute != null) {

      final JPanel panel = new JPanel(new BorderLayout());
      panel.setBackground(WebColors.White);
      add(panel, 1);
      final JPanel stylePanels = new JPanel(new VerticalLayout(5));
      stylePanels.setBackground(WebColors.White);
      panel.add(stylePanels, BorderLayout.CENTER);

      this.previews = new JPanel(new VerticalLayout(5));
      SwingUtil.setTitledBorder(previews, "Preview");

      final JPanel previewContainer = new JPanel(new VerticalLayout());
      previewContainer.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
      previewContainer.setBackground(WebColors.White);
      previewContainer.add(previews);
      panel.add(previewContainer, BorderLayout.EAST);

      addPanel(stylePanels, "Text Label", textStyle, "textName", "textSize",
        "textFaceName");
      addPanel(stylePanels, "Text Color", textStyle, "textFill",
        "textBoxColor", "textHaloFill", "textHaloRadius");
      addPanel(stylePanels, "Text Position", textStyle,
        "textHorizontalAlignment", "textVerticalAlignment", "textDx", "textDy",
        "textOrientationType", "textOrientation", "textPlacementType");

      this.previews.add(new TextStylePreview(this.textStyle));
    }
  }

  @Override
  protected Field createField(final String fieldName,
    final Class<?> fieldClass, final Object value) {
    if (fieldName.equals("textName")) {
      final AbstractDataObjectLayer layer = getLayer();
      final TextNameField textNameField = new TextNameField(layer, fieldName,
        value);
      Property.addListener(textNameField, fieldName, this);
      return textNameField;
    } else {
      return super.createField(fieldName, fieldClass, value);
    }
  }

  @Override
  public void doPropertyChange(final PropertyChangeEvent event) {
    final Object source = event.getSource();
    if (source instanceof Field) {
      final Field field = (Field)source;
      final String fieldName = field.getFieldName();
      final Object fieldValue = field.getFieldValue();
      JavaBeanUtil.setProperty(this.textStyle, fieldName, fieldValue);
    }
    for (final Component preview : this.previews.getComponents()) {
      preview.repaint();
    }
  }

  @Override
  public void save() {
    super.save();
    this.textStyleRenderer.setStyle(this.textStyle);
  }
}
