package com.revolsys.swing.field;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFormattedTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DateFormatter;

import org.springframework.util.StringUtils;

import com.revolsys.gis.model.data.equals.EqualsRegistry;

public class DateTextField extends JFormattedTextField implements
  ValidatingField, DocumentListener, FocusListener {
  private static final long serialVersionUID = 1L;

  private static final SimpleDateFormat FORMAT = new SimpleDateFormat(
    "yyyy/MM/dd");

  private Date fieldValue;

  private boolean fieldValid = true;

  private String fieldValidationMessage;

  public DateTextField() {
    super(new DateFormatter(FORMAT));
    setColumns(10);
    getDocument().addDocumentListener(this);
    setFocusLostBehavior(PERSIST);
    addFocusListener(this);
  }

  public void setFieldValid(boolean fieldValid) {
    this.fieldValid = fieldValid;
  }

  public Date getFieldValue() {
    return fieldValue;
  }

  public boolean isFieldValid() {
    return fieldValid;
  }

  public void setFieldValue(Date fieldValue) {
    this.fieldValue = fieldValue;

    String text;
    if (fieldValue == null) {
      text = "";
    } else {
      text = FORMAT.format(fieldValue);
    }
    setText(text);
    validateField();
  }

  private void validateField() {
    Date oldValue = fieldValue;
    Date value = null;
    boolean oldValid = fieldValid;
    boolean valid = true;
    final String text = getText();
    if (StringUtils.hasText(text)) {
      if (text.matches("\\d{4}/\\d{1,2}/\\d{1,2}")) {
        try {
          value = FORMAT.parse(text);
        } catch (final Throwable t) {
          fieldValidationMessage = t.getMessage();
          valid = false;
        }
      } else {
        fieldValidationMessage = "Date must be in the format YYYY/MM/DD";
        valid = false;
      }
    }

    if (valid != oldValid) {
      fieldValid = valid;
      firePropertyChange("fieldValid", oldValid, fieldValid);
    }
    if (valid) {
      if (!EqualsRegistry.equal(oldValue, value)) {
        fieldValue = value;
        firePropertyChange("fieldValid", oldValue, fieldValue);
      }
    }
  }

  public String getFieldValidationMessage() {
    return fieldValidationMessage;
  }

  @Override
  public void insertUpdate(DocumentEvent e) {
    validateField();
  }

  @Override
  public void removeUpdate(DocumentEvent e) {
    validateField();
  }

  @Override
  public void changedUpdate(DocumentEvent e) {
    validateField();
  }

  @Override
  public void focusGained(FocusEvent e) {
  }

  @Override
  public void focusLost(FocusEvent e) {
    updateText();
  }

  private void updateText() {
    String oldText = getText();
    String text;
    if (fieldValue == null) {
      text = "";
    } else {
      text = FORMAT.format(fieldValue);
    }
    if (!oldText.equals(text)) {
      setText(text);    
    }
  }
}
