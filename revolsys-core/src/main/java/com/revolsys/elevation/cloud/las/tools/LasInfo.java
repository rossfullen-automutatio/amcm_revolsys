package com.revolsys.elevation.cloud.las.tools;

import java.io.IOException;
import java.io.OutputStreamWriter;

import org.jeometry.common.collection.map.MapEx;
import org.jeometry.common.json.JsonWriter;
import org.jeometry.common.util.BaseCloseable;

import com.revolsys.elevation.cloud.PointCloud;
import com.revolsys.elevation.cloud.las.LasPointCloud;
import com.revolsys.spring.resource.PathResource;

public class LasInfo implements Runnable, BaseCloseable {

  public static void main(final String... args) {
    final String fileName = args[0];
    try (
      final LasInfo lasInfo = new LasInfo(fileName)) {
      lasInfo.run();
    }
  }

  private LasPointCloud pointCloud;

  public LasInfo(final String fileName) {
    this.pointCloud = PointCloud.newPointCloud(new PathResource(fileName));
  }

  @Override
  public void close() {
    final LasPointCloud pointCloud = this.pointCloud;
    if (pointCloud != null) {
      this.pointCloud = null;
      pointCloud.close();
    }
  }

  @Override
  public void run() {
    final MapEx lasInfo = this.pointCloud.toMap();

    try (
      OutputStreamWriter writer = new OutputStreamWriter(System.out);
      JsonWriter jsonWriter = new JsonWriter(writer, true);) {
      jsonWriter.write(lasInfo);
    } catch (final IOException e) {
    }
  }
}
