package com.revolsys.io.html;

import java.io.Writer;
import java.net.URI;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.revolsys.gis.data.model.DataObject;
import com.revolsys.gis.data.model.DataObjectMetaData;
import com.revolsys.io.AbstractWriter;
import com.revolsys.io.FileUtil;
import com.revolsys.io.IoConstants;
import com.revolsys.io.xml.XmlWriter;
import com.revolsys.util.CaseConverter;
import com.revolsys.util.HtmlUtil;

public class XhtmlDataObjectWriter extends AbstractWriter<DataObject> {
  private static final NumberFormat NUMBER_FORMAT = new DecimalFormat(
    "#.#########################");

  private String cssClass;

  private final DataObjectMetaData metaData;

  private boolean opened = false;

  /** The writer */
  private XmlWriter out;

  private boolean singleObject;

  private String title;

  private boolean wrap = true;

  public XhtmlDataObjectWriter(final DataObjectMetaData metaData,
    final Writer out) {
    this.metaData = metaData;
    this.out = new XmlWriter(out);
  }

  /**
   * Closes the underlying writer.
   */
  @Override
  public void close() {
    if (out != null) {
      try {
        writeFooter();
        out.flush();
      } finally {
        if (wrap) {
          FileUtil.closeSilent(out);
        }
        out = null;
      }
    }
  }

  @Override
  public void flush() {
    out.flush();
  }

  @Override
  public void setProperty(final String name, final Object value) {
    super.setProperty(name, value);
    if (value != null) {
      if (name.equals(IoConstants.WRAP_PROPERTY)) {
        wrap = Boolean.valueOf(value.toString());
      } else if (name.equals(IoConstants.TITLE_PROPERTY)) {
        title = value.toString();
      } else if (name.equals(IoConstants.CSS_CLASS_PROPERTY)) {
        cssClass = value.toString();
      }
    }
  }

  public void write(final DataObject object) {
    if (!opened) {
      writeHeader();
    }
    if (singleObject) {
      for (final String key : metaData.getAttributeNames()) {
        final Object value = object.getValue(key);
        out.startTag(HtmlUtil.TR);
        out.element(HtmlUtil.TH,
          CaseConverter.toCapitalizedWords(key.toString()));
        out.startTag(HtmlUtil.TD);
        if (value instanceof URI) {
          HtmlUtil.serializeA(out, null, value, value);
        } else if (value instanceof Number) {
          out.text(NUMBER_FORMAT.format(value));
        } else {
          out.text(value);
        }
        out.endTag(HtmlUtil.TD);
        out.endTag(HtmlUtil.TR);
      }
    } else {
      out.startTag(HtmlUtil.TR);
      for (final String key : metaData.getAttributeNames()) {
        final Object value = object.getValue(key);
        out.startTag(HtmlUtil.TD);
        if (value instanceof URI) {
          HtmlUtil.serializeA(out, null, value, value);
        } else if (value instanceof Number) {
          out.text(NUMBER_FORMAT.format(value));
        } else {
          out.text(value);
        }
        out.endTag(HtmlUtil.TD);
      }
      out.endTag(HtmlUtil.TR);

    }
  }

  private void writeFooter() {
    if (opened) {
      out.endTag(HtmlUtil.TBODY);
      out.endTag(HtmlUtil.TABLE);
      out.endTag(HtmlUtil.DIV);
      out.endTag(HtmlUtil.DIV);
      if (wrap) {
        out.endTag(HtmlUtil.BODY);
        out.endTag(HtmlUtil.HTML);
      }
    }
  }

  private void writeHeader() {
    if (wrap) {
      out.startDocument();
      out.startTag(HtmlUtil.HTML);

      out.startTag(HtmlUtil.HEAD);
      out.element(HtmlUtil.TITLE, title);

      out.endTag(HtmlUtil.HEAD);

      out.startTag(HtmlUtil.BODY);
    }
    out.startTag(HtmlUtil.DIV);
    out.attribute(HtmlUtil.ATTR_CLASS, cssClass);
    if (title != null) {
      out.element(HtmlUtil.H1, title);
    }
    singleObject = Boolean.TRUE.equals(getProperty(IoConstants.SINGLE_OBJECT_PROPERTY));
    if (singleObject) {
      out.startTag(HtmlUtil.DIV);
      out.attribute(HtmlUtil.ATTR_CLASS, "objectView");
      out.startTag(HtmlUtil.TABLE);
      out.attribute(HtmlUtil.ATTR_CLASS, "data");
      out.startTag(HtmlUtil.TBODY);
    } else {
      out.startTag(HtmlUtil.DIV);
      out.attribute(HtmlUtil.ATTR_CLASS, "objectList");
      out.startTag(HtmlUtil.TABLE);
      out.attribute(HtmlUtil.ATTR_CLASS, "data");

      out.startTag(HtmlUtil.THEAD);
      out.startTag(HtmlUtil.TR);
      for (final String name : metaData.getAttributeNames()) {
        out.element(HtmlUtil.TD, name);
      }
      out.endTag(HtmlUtil.TR);
      out.endTag(HtmlUtil.THEAD);

      out.startTag(HtmlUtil.TBODY);
    }
    opened = true;
  }
}
