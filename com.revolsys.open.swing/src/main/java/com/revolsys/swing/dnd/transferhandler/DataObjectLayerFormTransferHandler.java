package com.revolsys.swing.dnd.transferhandler;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

import org.slf4j.LoggerFactory;

import com.revolsys.swing.dnd.transferable.DataObjectTransferable;
import com.revolsys.swing.dnd.transferable.MapTransferable;
import com.revolsys.swing.map.form.DataObjectLayerForm;

public class DataObjectLayerFormTransferHandler extends TransferHandler {
  private static final long serialVersionUID = 1L;

  private final Reference<DataObjectLayerForm> form;

  public DataObjectLayerFormTransferHandler(final DataObjectLayerForm form) {
    this.form = new WeakReference<>(form);
  }

  @Override
  public boolean canImport(final JComponent comp,
    final DataFlavor[] transferFlavors) {
    for (final DataFlavor dataFlavor : transferFlavors) {
      if (MapTransferable.MAP_FLAVOR.equals(dataFlavor)) {
        return true;
      } else if (DataObjectTransferable.DATA_OBJECT_FLAVOR.equals(dataFlavor)) {
        return true;
      }
    }
    return false;
  }

  @Override
  protected Transferable createTransferable(final JComponent component) {
    final Map<String, Object> values = getForm().getValues();
    final Transferable transferable = new MapTransferable(values);
    return transferable;
  }

  public DataObjectLayerForm getForm() {
    return this.form.get();
  }

  @Override
  public int getSourceActions(final JComponent component) {
    return COPY;
  }

  @Override
  public boolean importData(final JComponent comp,
    final Transferable transferable) {
    for (final DataFlavor dataFlavor : Arrays.asList(
      DataObjectTransferable.DATA_OBJECT_FLAVOR, MapTransferable.MAP_FLAVOR)) {
      if (pasteValues(transferable, dataFlavor)) {
        return true;
      }
    }
    return false;
  }

  @SuppressWarnings("unchecked")
  public boolean pasteValues(final Transferable transferable,
    final DataFlavor dataFlavor) {
    if (transferable.isDataFlavorSupported(dataFlavor)) {
      try {
        final Map<String, Object> map = (Map<String, Object>)transferable.getTransferData(dataFlavor);
        getForm().pasteValues(map);
        return true;
      } catch (final Throwable e) {
        LoggerFactory.getLogger(getClass()).error("Unable to paste data",
          transferable);
        return false;
      }
    } else {
      return false;
    }
  }
}
