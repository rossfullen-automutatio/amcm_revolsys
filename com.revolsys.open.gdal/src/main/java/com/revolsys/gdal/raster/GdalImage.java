package com.revolsys.gdal.raster;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import org.gdal.gdal.Band;
import org.gdal.gdal.Dataset;

import com.revolsys.gdal.Gdal;
import com.revolsys.geometry.model.BoundingBox;
import com.revolsys.geometry.model.GeometryFactory;
import com.revolsys.raster.AbstractGeoreferencedImage;
import com.revolsys.raster.BufferedGeoreferencedImage;
import com.revolsys.raster.GeoreferencedImage;
import com.revolsys.spring.resource.Resource;

public class GdalImage extends AbstractGeoreferencedImage {

  private Dataset dataset;

  public GdalImage(final GdalImageFactory readerSpi, final Resource imageResource) {
    setImageResource(imageResource);
    final Dataset dataset = getDataset();
    final Band band = dataset.GetRasterBand(1);
    for (int i = 0; i < band.GetOverviewCount(); i++) {
      final Band overview = band.GetOverview(i);
      final int overviewWidth = overview.getXSize();
      final int overviewHeight = overview.getYSize();
      addOverviewSize(overviewWidth, overviewHeight);
    }

    final String projection = dataset.GetProjection();
    final double[] geoTransform = dataset.GetGeoTransform();
    if (projection != null) {
      final GeometryFactory geometryFactory = GeometryFactory.floating2d(projection);
      setGeometryFactory(geometryFactory);
    }
    setBoundingBox(geoTransform[0], geoTransform[3], geoTransform[1], geoTransform[5]);
    postConstruct();
  }

  @Override
  public void drawImage(final Graphics2D graphics, final BoundingBox viewBoundingBox,
    final int viewWidth, final int viewHeight, final boolean useTransform) {
    try {
      final Dataset dataset = getDataset();

      final BoundingBox imageBoundingBox = getBoundingBox();
      final BoundingBox viewImageClipBbox = viewBoundingBox.bboxIntersection(imageBoundingBox);
      final BoundingBox imageViewClipBbox = imageBoundingBox.bboxIntersection(viewBoundingBox);
      final double scaleFactor = viewWidth / viewBoundingBox.getWidth();

      final double clipModelWidth = viewImageClipBbox.getWidth();
      final int targetWidth = (int)Math.ceil(clipModelWidth * scaleFactor);

      final double clipModelHeight = viewImageClipBbox.getHeight();
      final int targetHeight = (int)Math.ceil(clipModelHeight * scaleFactor);

      int bestOverviewIdx = -1;

      int srcWidth = getImageWidth();
      int srcHeight = getImageHeight();
      final double clipResolution = Math.abs(imageViewClipBbox.getHeight() / targetHeight);
      final List<Dimension> overviewSizes = getOverviewSizes();
      for (int i = 0; i < overviewSizes.size(); i++) {
        final Dimension overviewSize = overviewSizes.get(i);
        final int width = overviewSize.width;
        final int height = overviewSize.height;

        if (0 != height && 0 != width) {
          final double overviewResolution = Math.abs(imageBoundingBox.getHeight() / height);
          if (overviewResolution <= clipResolution) {
            bestOverviewIdx = i;
            srcWidth = width;
            srcHeight = height;
          }
        }
      }

      final double scaleX = srcWidth / imageBoundingBox.getWidth();
      final double scaleY = srcHeight / imageBoundingBox.getHeight();
      final double minX = imageBoundingBox.getMinX();
      final double maxY = imageBoundingBox.getMaxY();

      final int clipXoff = (int)Math.max(0,
        Math.floor((imageViewClipBbox.getMinX() - imageBoundingBox.getMinX()) * scaleX));
      final int clipYoff = (int)Math.max(0,
        Math.floor((maxY - imageViewClipBbox.getMaxY()) * scaleY));
      final int clipWidth = (int)Math.min(srcWidth, Math.ceil(clipModelWidth * scaleX));
      final int clipHeight = (int)Math.min(srcHeight, Math.ceil(clipModelHeight * scaleY));
      final BufferedImage bufferedImage = Gdal.getBufferedImage(dataset, bestOverviewIdx, clipXoff,
        clipYoff, clipWidth, clipHeight, targetWidth, targetHeight);

      if (isSameCoordinateSystem(viewBoundingBox)) {
        super.drawRenderedImage(bufferedImage, imageViewClipBbox, graphics, viewBoundingBox,
          viewWidth, useTransform);
      } else {
        final double newMinX = minX + clipXoff / scaleX;
        final double newMaxY = maxY - clipYoff / scaleY;
        final double newMaxX = newMinX + clipWidth / scaleX;
        final double newMinY = newMaxY - clipHeight / scaleY;
        final BoundingBox newBoundingBox = getGeometryFactory().newBoundingBox(newMinX, newMinY,
          newMaxX, newMaxY);
        final BufferedImage image = new BufferedImage(targetWidth, targetHeight,
          BufferedImage.TYPE_INT_ARGB);
        image.getGraphics().drawImage(bufferedImage, 0, 0, targetWidth, targetHeight, null);

        final GeoreferencedImage newImage = new BufferedGeoreferencedImage(newBoundingBox, image);
        newImage.drawImage(graphics, viewBoundingBox, viewWidth, viewHeight, useTransform);
      }

    } catch (final Throwable e) {
      e.printStackTrace();
    }
  }

  @Override
  protected void finalize() throws Throwable {
    super.finalize();
    this.dataset = Gdal.closeDataSet(this.dataset);
  }

  public synchronized Dataset getDataset() {
    if (this.dataset == null) {
      final File file = getFile();
      this.dataset = Gdal.getDataset(file);
    }
    return this.dataset;
  }

  @Override
  public int getImageHeight() {
    return getDataset().getRasterYSize();
  }

  @Override
  public int getImageWidth() {
    return getDataset().getRasterXSize();
  }
}
