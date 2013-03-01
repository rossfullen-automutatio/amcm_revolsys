package com.revolsys.swing.map.layer.bing;

import java.awt.Image;

import com.revolsys.gis.bing.BingClient;
import com.revolsys.gis.bing.BingClient.ImagerySet;
import com.revolsys.gis.bing.BingClient.MapLayer;
import com.revolsys.swing.map.layer.MapTile;

public class BingMapTile extends MapTile {

  private String quadKey;

  private BingLayer layer;

  public BingMapTile(BingLayer layer, int zoomLevel, int tileX, int tileY) {
    super(BingClient.getBoundingBox(zoomLevel, tileX, tileY));
    this.layer = layer;
    this.quadKey = BingClient.getQuadKey(zoomLevel, tileX, tileY);
  }

  public String getQuadKey() {
    return quadKey;
  }

  @Override
  public Image loadImage() {
    final BingClient client = layer.getClient();
    ImagerySet imagerySet = layer.getImagerySet();
    MapLayer mapLayer = layer.getMapLayer();
    Image image = client.getMapImage(imagerySet, mapLayer, quadKey);
    return image;
  }
}
