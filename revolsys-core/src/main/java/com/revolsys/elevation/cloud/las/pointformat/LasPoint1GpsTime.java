package com.revolsys.elevation.cloud.las.pointformat;

import com.revolsys.collection.json.JsonObject;
import com.revolsys.elevation.cloud.las.LasPointCloud;
import com.revolsys.io.channels.ChannelWriter;
import com.revolsys.io.channels.DataReader;

public class LasPoint1GpsTime extends LasPoint0Core implements LasPointGpsTime {
  private static final long serialVersionUID = 1L;

  private double gpsTime;

  public LasPoint1GpsTime(final LasPointCloud pointCloud) {
    super(pointCloud);
    this.gpsTime = pointCloud.getFileGpsTime();
  }

  @Override
  public double getGpsTime() {
    return this.gpsTime;
  }

  @Override
  public LasPointFormat getPointFormat() {
    return LasPointFormat.GpsTime;
  }

  @Override
  public void read(final LasPointCloud pointCloud, final DataReader reader) {
    super.read(pointCloud, reader);
    this.gpsTime = reader.getDouble();
  }

  @Override
  public LasPoint1GpsTime setGpsTime(final double gpsTime) {
    this.gpsTime = gpsTime;
    return this;
  }

  @Override
  public JsonObject toMap() {
    final JsonObject map = super.toMap();
    addToMap(map, "gpsTime", this.gpsTime);
    return map;
  }

  @Override
  public void writeLasPoint(final ChannelWriter out) {
    super.writeLasPoint(out);
    out.putDouble(this.gpsTime);
  }
}
