package com.revolsys.elevation.cloud.las;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.revolsys.collection.map.LinkedHashMapEx;
import com.revolsys.collection.map.MapEx;
import com.revolsys.elevation.cloud.PointCloud;
import com.revolsys.elevation.cloud.las.pointformat.LasPoint;
import com.revolsys.elevation.cloud.las.pointformat.LasPoint0Core;
import com.revolsys.elevation.cloud.las.pointformat.LasPointFormat;
import com.revolsys.elevation.cloud.las.zip.LazChunkedIterator;
import com.revolsys.elevation.cloud.las.zip.LazPointwiseIterator;
import com.revolsys.elevation.tin.TriangulatedIrregularNetwork;
import com.revolsys.elevation.tin.quadedge.QuadEdgeDelaunayTinBuilder;
import com.revolsys.geometry.model.BoundingBox;
import com.revolsys.geometry.model.GeometryFactory;
import com.revolsys.geometry.model.Point;
import com.revolsys.io.BaseCloseable;
import com.revolsys.io.StringWriter;
import com.revolsys.io.ZipUtil;
import com.revolsys.io.channels.ChannelReader;
import com.revolsys.io.endian.EndianOutputStream;
import com.revolsys.io.map.MapSerializer;
import com.revolsys.properties.BaseObjectWithProperties;
import com.revolsys.record.io.format.html.HtmlWriter;
import com.revolsys.spring.resource.Resource;
import com.revolsys.util.HtmlElem;
import com.revolsys.util.Pair;

public class LasPointCloud extends BaseObjectWithProperties
  implements PointCloud<LasPoint>, BaseCloseable, MapSerializer, Iterable<LasPoint> {

  public static void forEachPoint(final Object source, final Consumer<? super LasPoint> action) {
    try (
      final LasPointCloud pointCloud = PointCloud.newPointCloud(source)) {
      pointCloud.forEachPoint(action);
    }
  }

  private GeometryFactory geometryFactory = GeometryFactory.fixed3d(1000.0, 1000.0, 1000.0);

  private LasPointCloudHeader header;

  private List<LasPoint> points = new ArrayList<>();

  private ChannelReader reader;

  private Resource resource;

  private boolean exists;

  private ByteBuffer byteBuffer;

  private Resource lasResource;

  private boolean allLoaded = false;

  public LasPointCloud(final LasPointFormat pointFormat, final GeometryFactory geometryFactory) {
    final LasPointCloudHeader header = new LasPointCloudHeader(pointFormat, geometryFactory);
    this.header = header;
    this.geometryFactory = header.getGeometryFactory();
  }

  public LasPointCloud(final Resource resource, final MapEx properties) {
    setProperties(properties);
    this.resource = resource;
    this.lasResource = resource;
    if (resource.getFileNameExtension().equals("zip")) {
      final Pair<Resource, GeometryFactory> result = ZipUtil
        .getZipResourceAndGeometryFactory(resource, ".las", this.geometryFactory);
      this.lasResource = result.getValue1();
      if (this.geometryFactory == null) {
        this.geometryFactory = result.getValue2();
      }
    } else {
      if (this.geometryFactory == null || !this.geometryFactory.isHasHorizontalCoordinateSystem()) {
        final GeometryFactory geometryFactoryFromPrj = GeometryFactory.floating3d(resource);
        if (geometryFactoryFromPrj != null) {
          this.geometryFactory = geometryFactoryFromPrj;
        }
      }
    }

    this.reader = open();
  }

  @SuppressWarnings("unchecked")
  public <P extends LasPoint0Core> P addPoint(final double x, final double y, final double z) {
    final LasPoint lasPoint = this.header.newLasPoint(this, x, y, z);
    this.points.add(lasPoint);
    return (P)lasPoint;
  }

  public void clear() {
    closeReader();
    this.header.clear();
    this.points = new ArrayList<>();
  }

  @Override
  public void close() {
    closeReader();
  }

  protected void closeReader() {
    final ChannelReader reader = this.reader;
    this.reader = null;
    if (reader != null) {
      reader.close();
    }
  }

  @Override
  public void forEachPoint(final Consumer<? super LasPoint> action) {
    final Iterable<LasPoint> iterable = iterable();
    try {
      iterable.forEach(action);
    } catch (RuntimeException | Error e) {
      if (iterable instanceof BaseCloseable) {
        ((BaseCloseable)iterable).close();
      }
      throw e;
    }
  }

  @Override
  public BoundingBox getBoundingBox() {
    return this.header.getBoundingBox();
  }

  @Override
  public Predicate<Point> getDefaultFilter() {
    return point -> LasClassification.GROUND == ((LasPoint)point).getClassification();
  }

  @Override
  public GeometryFactory getGeometryFactory() {
    return this.geometryFactory;
  }

  public LasPointCloudHeader getHeader() {
    return this.header;
  }

  public LasZipHeader getLasZipHeader() {
    return this.header.getLasZipHeader();
  }

  public long getPointCount() {
    return this.header.getPointCount();
  }

  public LasPointFormat getPointFormat() {
    return this.header.getPointFormat();
  }

  public List<LasPoint> getPoints() {
    loadAllPoints();
    return this.points;
  }

  public Resource getResource() {
    return this.resource;
  }

  public boolean isExists() {
    return this.exists;
  }

  public Iterable<LasPoint> iterable() {
    final long pointCount = getPointCount();
    if (this.allLoaded) {
      return this.points;
    } else if (pointCount == 0) {
      return Collections.emptyList();
    } else {
      ChannelReader reader = this.reader;
      this.reader = null;
      if (reader == null) {
        reader = open();
      }
      if (reader == null) {
        return Collections.emptyList();
      } else {
        try {
          if (this.header.isLaszip()) {
            final LasZipHeader lasZipHeader = getLasZipHeader();
            if (lasZipHeader.isCompressor(LasZipHeader.LASZIP_COMPRESSOR_POINTWISE)) {
              return new LazPointwiseIterator(this, reader);
            } else {
              return new LazChunkedIterator(this, reader);
            }

          } else {
            return new LasPointCloudIterator(this, reader);
          }
        } catch (RuntimeException | Error e) {
          reader.close();
          throw e;
        }
      }
    }
  }

  @Override
  public Iterator<LasPoint> iterator() {
    return iterable().iterator();
  }

  private synchronized void loadAllPoints() {
    if (!this.allLoaded && this.lasResource != null) {
      this.allLoaded = true;
      final List<LasPoint> points = new ArrayList<>((int)getPointCount());
      forEachPoint(points::add);
      this.points = points;
    }
  }

  @Override
  public TriangulatedIrregularNetwork newTriangulatedIrregularNetwork() {
    final GeometryFactory geometryFactory = getGeometryFactory();
    final QuadEdgeDelaunayTinBuilder tinBuilder = new QuadEdgeDelaunayTinBuilder(geometryFactory);
    forEachPoint((lasPoint) -> {
      tinBuilder.insertVertex(lasPoint);
    });
    final TriangulatedIrregularNetwork tin = tinBuilder.newTriangulatedIrregularNetwork();
    return tin;
  }

  @Override
  public TriangulatedIrregularNetwork newTriangulatedIrregularNetwork(
    final Predicate<? super Point> filter) {
    final GeometryFactory geometryFactory = getGeometryFactory();
    final QuadEdgeDelaunayTinBuilder tinBuilder = new QuadEdgeDelaunayTinBuilder(geometryFactory);
    forEachPoint((lasPoint) -> {
      if (filter.test(lasPoint)) {
        tinBuilder.insertVertex(lasPoint);
      }
    });
    final TriangulatedIrregularNetwork tin = tinBuilder.newTriangulatedIrregularNetwork();
    return tin;
  }

  private ChannelReader open() {
    final ChannelReader reader = this.lasResource.newChannelReader(this.byteBuffer);
    if (reader == null) {
      this.exists = false;
    } else {
      reader.setByteOrder(ByteOrder.LITTLE_ENDIAN);
      this.exists = true;
      this.header = new LasPointCloudHeader(reader, this.geometryFactory);
      this.geometryFactory = this.header.getGeometryFactory();
      if (this.header.getPointCount() == 0) {
        reader.close();
        return null;
      }
    }
    return reader;
  }

  public void setByteBuffer(final ByteBuffer byteBuffer) {
    this.byteBuffer = byteBuffer;
  }

  public void setGeometryFactory(final GeometryFactory geometryFactory) {
    this.geometryFactory = geometryFactory;
  }

  @Override
  public double toDoubleX(final int x) {
    return this.geometryFactory.toDoubleX(x);
  }

  @Override
  public double toDoubleY(final int y) {
    return this.geometryFactory.toDoubleY(y);
  }

  @Override
  public double toDoubleZ(final int z) {
    return this.geometryFactory.toDoubleZ(z);
  }

  @Override
  public String toHtml() {
    try (
      final StringWriter out = new StringWriter();
      HtmlWriter writer = new HtmlWriter(out, false)) {
      writer.element(HtmlElem.HTML, this::writeHtml);
      return out.toString();
    }
  }

  @Override
  public int toIntX(final double x) {
    return this.geometryFactory.toIntX(x);
  }

  @Override
  public int toIntY(final double y) {
    return this.geometryFactory.toIntY(y);
  }

  @Override
  public int toIntZ(final double z) {
    return this.geometryFactory.toIntZ(z);
  }

  @Override
  public MapEx toMap() {
    final MapEx map = new LinkedHashMapEx();
    addToMap(map, "url", this.resource.getUri());
    addToMap(map, "header", this.header);
    return map;
  }

  public void writeHtml(final HtmlWriter writer) {
    writer.elementClass(HtmlElem.DIV, "las", this.header::writeHtml);
  }

  public void writePointCloud(final Object target) {
    final Resource resource = Resource.getResource(target);
    try (
      EndianOutputStream out = resource.newBufferedOutputStream(EndianOutputStream::new)) {
      this.header.writeHeader(out);
      for (final LasPoint point : this.points) {
        point.write(out);
      }
    }
  }
}
