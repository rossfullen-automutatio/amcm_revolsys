package com.revolsys.format.gml;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import com.revolsys.spring.resource.Resource;

import com.revolsys.data.record.io.RecordWriter;
import com.revolsys.data.record.io.RecordWriterFactory;
import com.revolsys.data.record.schema.RecordDefinition;
import com.revolsys.geometry.io.GeometryReader;
import com.revolsys.geometry.io.GeometryReaderFactory;
import com.revolsys.io.AbstractIoFactoryWithCoordinateSystem;
import com.revolsys.io.FileUtil;

public class GmlIoFactory extends AbstractIoFactoryWithCoordinateSystem
  implements RecordWriterFactory, GeometryReaderFactory {
  public GmlIoFactory() {
    super(GmlConstants.FORMAT_DESCRIPTION);
    addMediaTypeAndFileExtension(GmlConstants.MEDIA_TYPE, GmlConstants.FILE_EXTENSION);
  }

  @Override
  public GeometryReader createGeometryReader(final Resource resource) {
    final GmlGeometryIterator iterator = new GmlGeometryIterator(resource);
    return new GeometryReader(iterator);
  }

  @Override
  public RecordWriter createRecordWriter(final String baseName,
    final RecordDefinition recordDefinition, final OutputStream outputStream,
    final Charset charset) {
    final OutputStreamWriter writer = FileUtil.createUtf8Writer(outputStream);
    return new GmlRecordWriter(recordDefinition, writer);
  }
}
