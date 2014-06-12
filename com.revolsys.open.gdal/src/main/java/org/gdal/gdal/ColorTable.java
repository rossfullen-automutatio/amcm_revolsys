/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 1.3.39
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package org.gdal.gdal;

/* imports for getIndexColorModel */
import java.awt.Color;
import java.awt.image.IndexColorModel;

public class ColorTable implements Cloneable {
  protected static long getCPtr(final ColorTable obj) {
    return obj == null ? 0 : obj.swigCPtr;
  }

  private long swigCPtr;

  protected boolean swigCMemOwn;

  public ColorTable() {
    this(gdalJNI.new_ColorTable__SWIG_1(), true);
  }

  public ColorTable(final int palette) {
    this(gdalJNI.new_ColorTable__SWIG_0(palette), true);
  }

  protected ColorTable(final long cPtr, final boolean cMemoryOwn) {
    if (cPtr == 0) {
      throw new RuntimeException();
    }
    this.swigCMemOwn = cMemoryOwn;
    this.swigCPtr = cPtr;
  }

  /* Ensure that the GC doesn't collect any parent instance set from Java */
  protected void addReference(final Object reference) {
  }

  @Override
  public Object clone() {
    return Clone();
  }

  public ColorTable Clone() {
    final long cPtr = gdalJNI.ColorTable_Clone(this.swigCPtr, this);
    return cPtr == 0 ? null : new ColorTable(cPtr, true);
  }

  public void CreateColorRamp(final int nStartIndex,
    final java.awt.Color startcolor, final int nEndIndex,
    final java.awt.Color endcolor) {
    gdalJNI.ColorTable_CreateColorRamp(this.swigCPtr, this, nStartIndex,
      startcolor, nEndIndex, endcolor);
  }

  public synchronized void delete() {
    if (this.swigCPtr != 0 && this.swigCMemOwn) {
      this.swigCMemOwn = false;
      gdalJNI.delete_ColorTable(this.swigCPtr);
    }
    this.swigCPtr = 0;
  }

  @Override
  protected void finalize() {
    delete();
  }

  public java.awt.Color GetColorEntry(final int entry) {
    return gdalJNI.ColorTable_GetColorEntry(this.swigCPtr, this, entry);
  }

  public int GetCount() {
    return gdalJNI.ColorTable_GetCount(this.swigCPtr, this);
  }

  /* convienance method */
  public IndexColorModel getIndexColorModel(final int bits) {
    final int size = GetCount();
    final byte[] reds = new byte[size];
    final byte[] greens = new byte[size];
    final byte[] blues = new byte[size];
    final byte[] alphas = new byte[size];
    int noAlphas = 0;
    int zeroAlphas = 0;
    int lastAlphaIndex = -1;

    Color entry = null;
    for (int i = 0; i < size; i++) {
      entry = GetColorEntry(i);
      reds[i] = (byte)(entry.getRed() & 0xff);
      greens[i] = (byte)(entry.getGreen() & 0xff);
      blues[i] = (byte)(entry.getBlue() & 0xff);
      final byte alpha = (byte)(entry.getAlpha() & 0xff);

      // The byte type is -128 to 127 so a normal 255 will be -1.
      if (alpha == -1) {
        noAlphas++;
      } else {
        if (alpha == 0) {
          zeroAlphas++;
          lastAlphaIndex = i;
        }
      }
      alphas[i] = alpha;
    }
    if (noAlphas == size) {
      return new IndexColorModel(bits, size, reds, greens, blues);
    } else if (noAlphas == size - 1 && zeroAlphas == 1) {
      return new IndexColorModel(bits, size, reds, greens, blues,
        lastAlphaIndex);
    } else {
      return new IndexColorModel(bits, size, reds, greens, blues, alphas);
    }
  }

  public int GetPaletteInterpretation() {
    return gdalJNI.ColorTable_GetPaletteInterpretation(this.swigCPtr, this);
  }

  public void SetColorEntry(final int entry, final java.awt.Color centry) {
    gdalJNI.ColorTable_SetColorEntry(this.swigCPtr, this, entry, centry);
  }

}
