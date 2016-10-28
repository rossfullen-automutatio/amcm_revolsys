package com.revolsys.record.io.format.saif.geometry;

import com.revolsys.geometry.model.Point;
import com.revolsys.geometry.model.impl.PointDoubleGf;
import com.revolsys.record.io.format.saif.SaifConstants;

public class TextLinePoint extends PointDoubleGf {
  private int characterHeight;

  private String fontName;

  private double orientation;

  private String other;

  private String text;

  public TextLinePoint(final Point point) {
    super(point.getGeometryFactory(), point.getCoordinates());
  }

  public int getCharacterHeight() {
    return this.characterHeight;
  }

  public String getFontName() {
    return this.fontName;
  }

  public double getOrientation() {
    return this.orientation;
  }

  public String getOsnGeometryType() {
    return SaifConstants.TEXT_LINE;
  }

  public String getOther() {
    return this.other;
  }

  public String getText() {
    return this.text;
  }

  public void setCharacterHeight(final int characterHeight) {
    this.characterHeight = characterHeight;
  }

  public void setFontName(final String fontName) {
    this.fontName = fontName;
  }

  public void setOrientation(final double orientation) {
    this.orientation = orientation;
  }

  public void setOther(final String other) {
    this.other = other;
  }

  public void setText(final String text) {
    this.text = text;
  }
}