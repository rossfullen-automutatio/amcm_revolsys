package com.revolsys.elevation.cloud.las;

import com.revolsys.collection.map.LinkedHashMapEx;
import com.revolsys.collection.map.MapEx;
import com.revolsys.geometry.model.Point;
import com.revolsys.io.channels.ChannelReader;
import com.revolsys.io.endian.EndianOutput;
import com.revolsys.io.map.MapSerializer;

public interface LasPoint extends Point, MapSerializer {

  @Override
  LasPoint clone();

  byte getClassification();

  byte getClassificationByte();

  default double getGpsTime() {
    return 315964800;
  }

  int getIntensity();

  byte getNumberOfReturns();

  LasPointFormat getPointFormat();

  default int getPointFormatId() {
    return getPointFormat().getId();
  }

  int getPointSourceID();

  byte getReturnByte();

  byte getReturnNumber();

  short getScanAngleRank();

  byte getScannerChannel();

  short getUserData();

  boolean isEdgeOfFlightLine();

  boolean isKeyPoint();

  boolean isScanDirectionFlag();

  boolean isSynthetic();

  boolean isWithheld();

  void read(LasPointCloud pointCloud, ChannelReader reader);

  void setClassification(byte classification);

  void setClassificationByte(byte classificationByte);

  void setEdgeOfFlightLine(boolean edgeOfFlightLine);

  default void setGpsTime(final double gpsTime) {
    throw new UnsupportedOperationException();
  }

  void setIntensity(int intensity);

  void setKeyPoint(boolean keyPoint);

  void setNumberOfReturns(byte numberOfReturns);

  void setPointSourceID(int pointSourceID);

  void setReturnByte(byte returnByte);

  void setReturnNumber(byte returnNumber);

  void setScanAngleRank(short scanAngleRank);

  void setScanDirectionFlag(boolean scanDirectionFlag);

  void setScannerChannel(byte scannerChannel);

  void setSynthetic(boolean synthetic);

  void setUserData(short userData);

  void setWithheld(boolean withheld);

  void setX(double x);

  void setY(double y);

  void setZ(double z);

  @Override
  default MapEx toMap() {
    final MapEx map = new LinkedHashMapEx();
    return map;
  }

  void write(final LasPointCloud pointCloud, final EndianOutput out);
}
