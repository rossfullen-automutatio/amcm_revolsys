package com.revolsys.swing.map.layer.record;

import com.revolsys.identifier.Identifier;
import com.revolsys.record.Record;
import com.revolsys.record.RecordState;

public class LoadingRecord extends AbstractLayerRecord {
  public LoadingRecord(final AbstractRecordLayer layer) {
    super(layer);
  }

  @Override
  public Identifier getIdentifier() {
    return null;
  }

  @Override
  public RecordState getState() {
    return RecordState.Initalizing;
  }

  @Override
  public <T> T getValue(final int index) {
    return null;
  }

  @Override
  public int hashCode() {
    return 1;
  }

  @Override
  public boolean isSame(final Record record) {
    return record instanceof LoadingRecord;
  }

  @Override
  public void setState(final RecordState state) {
  }

  @Override
  public boolean setValue(final int index, final Object value) {
    return false;
  }
}
