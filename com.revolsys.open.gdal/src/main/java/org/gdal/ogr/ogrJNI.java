/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 1.3.39
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package org.gdal.ogr;

import org.gdal.osr.CoordinateTransformation;
import org.gdal.osr.SpatialReference;

class ogrJNI {
  private static boolean available = false;
  static {
    try {
      com.revolsys.jar.ClasspathNativeLibraryUtil.loadLibrary("ogrjni");
      available = true;

      if (org.gdal.gdal.gdal.HasThreadSupport() == 0) {
        System.err.println("WARNING : GDAL should be compiled with thread support for safe execution in Java.");
      }

    } catch (final UnsatisfiedLinkError e) {
      available = false;
      System.err.println("Native library load failed.");
      System.err.println(e);
    }
  }

  public final static native long ApproximateArcAngles(double jarg1,
    double jarg2, double jarg3, double jarg4, double jarg5, double jarg6,
    double jarg7, double jarg8, double jarg9);

  public final static native long BuildPolygonFromEdges__SWIG_0(long jarg1,
    Geometry jarg1_, int jarg2, int jarg3, double jarg4);

  public final static native long BuildPolygonFromEdges__SWIG_1(long jarg1,
    Geometry jarg1_, int jarg2, int jarg3);

  public final static native long BuildPolygonFromEdges__SWIG_2(long jarg1,
    Geometry jarg1_, int jarg2);

  public final static native long BuildPolygonFromEdges__SWIG_3(long jarg1,
    Geometry jarg1_);

  public final static native long CreateGeometryFromGML(String jarg1);

  public final static native long CreateGeometryFromJson(String jarg1);

  public final static native long CreateGeometryFromWkb__SWIG_0(byte[] jarg1,
    long jarg3, SpatialReference jarg3_);

  public final static native long CreateGeometryFromWkb__SWIG_1(byte[] jarg1);

  public final static native long CreateGeometryFromWkt__SWIG_0(String jarg1,
    long jarg2, SpatialReference jarg2_);

  public final static native long CreateGeometryFromWkt__SWIG_1(String jarg1);

  public final static native long DataSource_CopyLayer__SWIG_0(long jarg1,
    DataSource jarg1_, long jarg2, Layer jarg2_, String jarg3,
    java.util.Vector jarg4);

  public final static native long DataSource_CopyLayer__SWIG_1(long jarg1,
    DataSource jarg1_, long jarg2, Layer jarg2_, String jarg3);

  public final static native long DataSource_CreateLayer__SWIG_0(long jarg1,
    DataSource jarg1_, String jarg2, long jarg3, SpatialReference jarg3_,
    int jarg4, java.util.Vector jarg5);

  public final static native long DataSource_CreateLayer__SWIG_1(long jarg1,
    DataSource jarg1_, String jarg2, long jarg3, SpatialReference jarg3_,
    int jarg4);

  public final static native long DataSource_CreateLayer__SWIG_2(long jarg1,
    DataSource jarg1_, String jarg2, long jarg3, SpatialReference jarg3_);

  public final static native long DataSource_CreateLayer__SWIG_3(long jarg1,
    DataSource jarg1_, String jarg2);

  public final static native int DataSource_DeleteLayer(long jarg1,
    DataSource jarg1_, int jarg2);

  public final static native long DataSource_ExecuteSQL__SWIG_0(long jarg1,
    DataSource jarg1_, String jarg2, long jarg3, Geometry jarg3_, String jarg4);

  public final static native long DataSource_ExecuteSQL__SWIG_1(long jarg1,
    DataSource jarg1_, String jarg2, long jarg3, Geometry jarg3_);

  public final static native long DataSource_ExecuteSQL__SWIG_2(long jarg1,
    DataSource jarg1_, String jarg2);

  public final static native long DataSource_GetDriver(long jarg1,
    DataSource jarg1_);

  public final static native long DataSource_GetLayerByIndex(long jarg1,
    DataSource jarg1_, int jarg2);

  public final static native long DataSource_GetLayerByName(long jarg1,
    DataSource jarg1_, String jarg2);

  public final static native int DataSource_GetLayerCount(long jarg1,
    DataSource jarg1_);

  public final static native String DataSource_GetName(long jarg1,
    DataSource jarg1_);

  public final static native int DataSource_GetRefCount(long jarg1,
    DataSource jarg1_);

  public final static native long DataSource_GetStyleTable(long jarg1,
    DataSource jarg1_);

  public final static native int DataSource_GetSummaryRefCount(long jarg1,
    DataSource jarg1_);

  public final static native String DataSource_name_get(long jarg1,
    DataSource jarg1_);

  public final static native void DataSource_ReleaseResultSet(long jarg1,
    DataSource jarg1_, long jarg2, Layer jarg2_);

  public final static native void DataSource_SetStyleTable(long jarg1,
    DataSource jarg1_, long jarg2, StyleTable jarg2_);

  public final static native int DataSource_SyncToDisk(long jarg1,
    DataSource jarg1_);

  public final static native boolean DataSource_TestCapability(long jarg1,
    DataSource jarg1_, String jarg2);

  public final static native void delete_DataSource(long jarg1);

  public final static native void delete_Feature(long jarg1);

  public final static native void delete_FeatureDefn(long jarg1);

  public final static native void delete_FieldDefn(long jarg1);

  public final static native void delete_Geometry(long jarg1);

  public final static native void delete_GeomFieldDefn(long jarg1);

  public final static native void delete_ProgressCallback(long jarg1);

  public final static native void delete_StyleTable(long jarg1);

  public final static native void delete_TermProgressCallback(long jarg1);

  public final static native void DontUseExceptions();

  public final static native long Driver_CopyDataSource__SWIG_0(long jarg1,
    Driver jarg1_, long jarg2, DataSource jarg2_, String jarg3,
    java.util.Vector jarg4);

  public final static native long Driver_CopyDataSource__SWIG_1(long jarg1,
    Driver jarg1_, long jarg2, DataSource jarg2_, String jarg3);

  public final static native long Driver_CreateDataSource__SWIG_0(long jarg1,
    Driver jarg1_, String jarg2, java.util.Vector jarg3);

  public final static native long Driver_CreateDataSource__SWIG_1(long jarg1,
    Driver jarg1_, String jarg2);

  public final static native int Driver_DeleteDataSource(long jarg1,
    Driver jarg1_, String jarg2);

  public final static native void Driver_Deregister(long jarg1, Driver jarg1_);

  public final static native String Driver_GetName(long jarg1, Driver jarg1_);

  public final static native String Driver_name_get(long jarg1, Driver jarg1_);

  public final static native long Driver_Open__SWIG_0(long jarg1,
    Driver jarg1_, String jarg2, int jarg3);

  public final static native long Driver_Open__SWIG_1(long jarg1,
    Driver jarg1_, String jarg2);

  public final static native void Driver_Register(long jarg1, Driver jarg1_);

  public final static native boolean Driver_TestCapability(long jarg1,
    Driver jarg1_, String jarg2);

  public final static native long Feature_Clone(long jarg1, Feature jarg1_);

  public final static native void Feature_DumpReadable(long jarg1,
    Feature jarg1_);

  public final static native boolean Feature_Equal(long jarg1, Feature jarg1_,
    long jarg2, Feature jarg2_);

  public final static native long Feature_GetDefnRef(long jarg1, Feature jarg1_);

  public final static native int Feature_GetFID(long jarg1, Feature jarg1_);

  public final static native void Feature_GetFieldAsDateTime(long jarg1,
    Feature jarg1_, int jarg2, int[] jarg3, int[] jarg4, int[] jarg5,
    int[] jarg6, int[] jarg7, int[] jarg8, int[] jarg9);

  public final static native double Feature_GetFieldAsDouble__SWIG_0(
    long jarg1, Feature jarg1_, int jarg2);

  public final static native double Feature_GetFieldAsDouble__SWIG_1(
    long jarg1, Feature jarg1_, String jarg2);

  public final static native double[] Feature_GetFieldAsDoubleList(long jarg1,
    Feature jarg1_, int jarg2);

  public final static native int Feature_GetFieldAsInteger__SWIG_0(long jarg1,
    Feature jarg1_, int jarg2);

  public final static native int Feature_GetFieldAsInteger__SWIG_1(long jarg1,
    Feature jarg1_, String jarg2);

  public final static native int[] Feature_GetFieldAsIntegerList(long jarg1,
    Feature jarg1_, int jarg2);

  public final static native String Feature_GetFieldAsString__SWIG_0(
    long jarg1, Feature jarg1_, int jarg2);

  public final static native String Feature_GetFieldAsString__SWIG_1(
    long jarg1, Feature jarg1_, String jarg2);

  public final static native String[] Feature_GetFieldAsStringList(long jarg1,
    Feature jarg1_, int jarg2);

  public final static native int Feature_GetFieldCount(long jarg1,
    Feature jarg1_);

  public final static native long Feature_GetFieldDefnRef__SWIG_0(long jarg1,
    Feature jarg1_, int jarg2);

  public final static native long Feature_GetFieldDefnRef__SWIG_1(long jarg1,
    Feature jarg1_, String jarg2);

  public final static native int Feature_GetFieldIndex(long jarg1,
    Feature jarg1_, String jarg2);

  public final static native int Feature_GetFieldType__SWIG_0(long jarg1,
    Feature jarg1_, int jarg2);

  public final static native int Feature_GetFieldType__SWIG_1(long jarg1,
    Feature jarg1_, String jarg2);

  public final static native long Feature_GetGeometryRef(long jarg1,
    Feature jarg1_);

  public final static native int Feature_GetGeomFieldCount(long jarg1,
    Feature jarg1_);

  public final static native long Feature_GetGeomFieldDefnRef__SWIG_0(
    long jarg1, Feature jarg1_, int jarg2);

  public final static native long Feature_GetGeomFieldDefnRef__SWIG_1(
    long jarg1, Feature jarg1_, String jarg2);

  public final static native int Feature_GetGeomFieldIndex(long jarg1,
    Feature jarg1_, String jarg2);

  public final static native long Feature_GetGeomFieldRef__SWIG_0(long jarg1,
    Feature jarg1_, int jarg2);

  public final static native long Feature_GetGeomFieldRef__SWIG_1(long jarg1,
    Feature jarg1_, String jarg2);

  public final static native String Feature_GetStyleString(long jarg1,
    Feature jarg1_);

  public final static native boolean Feature_IsFieldSet__SWIG_0(long jarg1,
    Feature jarg1_, int jarg2);

  public final static native boolean Feature_IsFieldSet__SWIG_1(long jarg1,
    Feature jarg1_, String jarg2);

  public final static native int Feature_SetFID(long jarg1, Feature jarg1_,
    int jarg2);

  public final static native void Feature_SetField__SWIG_0(long jarg1,
    Feature jarg1_, int jarg2, String jarg3);

  public final static native void Feature_SetField__SWIG_1(long jarg1,
    Feature jarg1_, String jarg2, String jarg3);

  public final static native void Feature_SetField__SWIG_2(long jarg1,
    Feature jarg1_, int jarg2, int jarg3);

  public final static native void Feature_SetField__SWIG_3(long jarg1,
    Feature jarg1_, String jarg2, int jarg3);

  public final static native void Feature_SetField__SWIG_4(long jarg1,
    Feature jarg1_, int jarg2, double jarg3);

  public final static native void Feature_SetField__SWIG_5(long jarg1,
    Feature jarg1_, String jarg2, double jarg3);

  public final static native void Feature_SetField__SWIG_6(long jarg1,
    Feature jarg1_, int jarg2, int jarg3, int jarg4, int jarg5, int jarg6,
    int jarg7, int jarg8, int jarg9);

  public final static native void Feature_SetField__SWIG_7(long jarg1,
    Feature jarg1_, String jarg2, int jarg3, int jarg4, int jarg5, int jarg6,
    int jarg7, int jarg8, int jarg9);

  public final static native void Feature_SetFieldBinaryFromHexString__SWIG_0(
    long jarg1, Feature jarg1_, int jarg2, String jarg3);

  public final static native void Feature_SetFieldBinaryFromHexString__SWIG_1(
    long jarg1, Feature jarg1_, String jarg2, String jarg3);

  public final static native void Feature_SetFieldDoubleList(long jarg1,
    Feature jarg1_, int jarg2, double[] jarg3);

  public final static native void Feature_SetFieldIntegerList(long jarg1,
    Feature jarg1_, int jarg2, int[] jarg3);

  public final static native void Feature_SetFieldStringList(long jarg1,
    Feature jarg1_, int jarg2, java.util.Vector jarg3);

  public final static native int Feature_SetFrom__SWIG_0(long jarg1,
    Feature jarg1_, long jarg2, Feature jarg2_, int jarg3);

  public final static native int Feature_SetFrom__SWIG_1(long jarg1,
    Feature jarg1_, long jarg2, Feature jarg2_);

  public final static native int Feature_SetFromWithMap(long jarg1,
    Feature jarg1_, long jarg2, Feature jarg2_, int jarg3, int[] jarg4);

  public final static native int Feature_SetGeometry(long jarg1,
    Feature jarg1_, long jarg2, Geometry jarg2_);

  public final static native int Feature_SetGeometryDirectly(long jarg1,
    Feature jarg1_, long jarg2, Geometry jarg2_);

  public final static native int Feature_SetGeomField__SWIG_0(long jarg1,
    Feature jarg1_, int jarg2, long jarg3, Geometry jarg3_);

  public final static native int Feature_SetGeomField__SWIG_1(long jarg1,
    Feature jarg1_, String jarg2, long jarg3, Geometry jarg3_);

  public final static native int Feature_SetGeomFieldDirectly__SWIG_0(
    long jarg1, Feature jarg1_, int jarg2, long jarg3, Geometry jarg3_);

  public final static native int Feature_SetGeomFieldDirectly__SWIG_1(
    long jarg1, Feature jarg1_, String jarg2, long jarg3, Geometry jarg3_);

  public final static native void Feature_SetStyleString(long jarg1,
    Feature jarg1_, String jarg2);

  public final static native void Feature_UnsetField__SWIG_0(long jarg1,
    Feature jarg1_, int jarg2);

  public final static native void Feature_UnsetField__SWIG_1(long jarg1,
    Feature jarg1_, String jarg2);

  public final static native void FeatureDefn_AddFieldDefn(long jarg1,
    FeatureDefn jarg1_, long jarg2, FieldDefn jarg2_);

  public final static native void FeatureDefn_AddGeomFieldDefn(long jarg1,
    FeatureDefn jarg1_, long jarg2, GeomFieldDefn jarg2_);

  public final static native int FeatureDefn_DeleteGeomFieldDefn(long jarg1,
    FeatureDefn jarg1_, int jarg2);

  public final static native int FeatureDefn_GetFieldCount(long jarg1,
    FeatureDefn jarg1_);

  public final static native long FeatureDefn_GetFieldDefn(long jarg1,
    FeatureDefn jarg1_, int jarg2);

  public final static native int FeatureDefn_GetFieldIndex(long jarg1,
    FeatureDefn jarg1_, String jarg2);

  public final static native int FeatureDefn_GetGeomFieldCount(long jarg1,
    FeatureDefn jarg1_);

  public final static native long FeatureDefn_GetGeomFieldDefn(long jarg1,
    FeatureDefn jarg1_, int jarg2);

  public final static native int FeatureDefn_GetGeomFieldIndex(long jarg1,
    FeatureDefn jarg1_, String jarg2);

  public final static native int FeatureDefn_GetGeomType(long jarg1,
    FeatureDefn jarg1_);

  public final static native String FeatureDefn_GetName(long jarg1,
    FeatureDefn jarg1_);

  public final static native int FeatureDefn_GetReferenceCount(long jarg1,
    FeatureDefn jarg1_);

  public final static native int FeatureDefn_IsGeometryIgnored(long jarg1,
    FeatureDefn jarg1_);

  public final static native int FeatureDefn_IsSame(long jarg1,
    FeatureDefn jarg1_, long jarg2, FeatureDefn jarg2_);

  public final static native int FeatureDefn_IsStyleIgnored(long jarg1,
    FeatureDefn jarg1_);

  public final static native void FeatureDefn_SetGeometryIgnored(long jarg1,
    FeatureDefn jarg1_, int jarg2);

  public final static native void FeatureDefn_SetGeomType(long jarg1,
    FeatureDefn jarg1_, int jarg2);

  public final static native void FeatureDefn_SetStyleIgnored(long jarg1,
    FeatureDefn jarg1_, int jarg2);

  public final static native int FieldDefn_GetFieldType(long jarg1,
    FieldDefn jarg1_);

  public final static native String FieldDefn_GetFieldTypeName(long jarg1,
    FieldDefn jarg1_, int jarg2);

  public final static native int FieldDefn_GetJustify(long jarg1,
    FieldDefn jarg1_);

  public final static native String FieldDefn_GetName(long jarg1,
    FieldDefn jarg1_);

  public final static native String FieldDefn_GetNameRef(long jarg1,
    FieldDefn jarg1_);

  public final static native int FieldDefn_GetPrecision(long jarg1,
    FieldDefn jarg1_);

  public final static native String FieldDefn_GetTypeName(long jarg1,
    FieldDefn jarg1_);

  public final static native int FieldDefn_GetWidth(long jarg1, FieldDefn jarg1_);

  public final static native int FieldDefn_IsIgnored(long jarg1,
    FieldDefn jarg1_);

  public final static native void FieldDefn_SetIgnored(long jarg1,
    FieldDefn jarg1_, int jarg2);

  public final static native void FieldDefn_SetJustify(long jarg1,
    FieldDefn jarg1_, int jarg2);

  public final static native void FieldDefn_SetName(long jarg1,
    FieldDefn jarg1_, String jarg2);

  public final static native void FieldDefn_SetPrecision(long jarg1,
    FieldDefn jarg1_, int jarg2);

  public final static native void FieldDefn_SetType(long jarg1,
    FieldDefn jarg1_, int jarg2);

  public final static native void FieldDefn_SetWidth(long jarg1,
    FieldDefn jarg1_, int jarg2);

  public final static native long ForceToLineString(long jarg1, Geometry jarg1_);

  public final static native long ForceToMultiLineString(long jarg1,
    Geometry jarg1_);

  public final static native long ForceToMultiPoint(long jarg1, Geometry jarg1_);

  public final static native long ForceToMultiPolygon(long jarg1,
    Geometry jarg1_);

  public final static native long ForceToPolygon(long jarg1, Geometry jarg1_);

  public final static native java.util.Vector GeneralCmdLineProcessor__SWIG_0(
    java.util.Vector jarg1, int jarg2);

  public final static native java.util.Vector GeneralCmdLineProcessor__SWIG_1(
    java.util.Vector jarg1);

  public final static native int Geometry_AddGeometry(long jarg1,
    Geometry jarg1_, long jarg2, Geometry jarg2_);

  public final static native int Geometry_AddGeometryDirectly(long jarg1,
    Geometry jarg1_, long jarg2, Geometry jarg2_);

  public final static native void Geometry_AddPoint__SWIG_0(long jarg1,
    Geometry jarg1_, double jarg2, double jarg3, double jarg4);

  public final static native void Geometry_AddPoint__SWIG_1(long jarg1,
    Geometry jarg1_, double jarg2, double jarg3);

  public final static native void Geometry_AddPoint_2D(long jarg1,
    Geometry jarg1_, double jarg2, double jarg3);

  public final static native double Geometry_Area(long jarg1, Geometry jarg1_);

  public final static native void Geometry_AssignSpatialReference(long jarg1,
    Geometry jarg1_, long jarg2, SpatialReference jarg2_);

  public final static native long Geometry_Boundary(long jarg1, Geometry jarg1_);

  public final static native long Geometry_Buffer__SWIG_0(long jarg1,
    Geometry jarg1_, double jarg2, int jarg3);

  public final static native long Geometry_Buffer__SWIG_1(long jarg1,
    Geometry jarg1_, double jarg2);

  public final static native long Geometry_Centroid(long jarg1, Geometry jarg1_);

  public final static native long Geometry_Clone(long jarg1, Geometry jarg1_);

  public final static native void Geometry_CloseRings(long jarg1,
    Geometry jarg1_);

  public final static native boolean Geometry_Contains(long jarg1,
    Geometry jarg1_, long jarg2, Geometry jarg2_);

  public final static native long Geometry_ConvexHull(long jarg1,
    Geometry jarg1_);

  public final static native boolean Geometry_Crosses(long jarg1,
    Geometry jarg1_, long jarg2, Geometry jarg2_);

  public final static native long Geometry_Difference(long jarg1,
    Geometry jarg1_, long jarg2, Geometry jarg2_);

  public final static native boolean Geometry_Disjoint(long jarg1,
    Geometry jarg1_, long jarg2, Geometry jarg2_);

  public final static native double Geometry_Distance(long jarg1,
    Geometry jarg1_, long jarg2, Geometry jarg2_);

  public final static native void Geometry_Empty(long jarg1, Geometry jarg1_);

  public final static native boolean Geometry_Equal(long jarg1,
    Geometry jarg1_, long jarg2, Geometry jarg2_);

  public final static native boolean Geometry_Equals(long jarg1,
    Geometry jarg1_, long jarg2, Geometry jarg2_);

  public final static native String Geometry_ExportToGML__SWIG_0(long jarg1,
    Geometry jarg1_, java.util.Vector jarg2);

  public final static native String Geometry_ExportToGML__SWIG_1(long jarg1,
    Geometry jarg1_);

  public final static native String Geometry_ExportToJson__SWIG_0(long jarg1,
    Geometry jarg1_, java.util.Vector jarg2);

  public final static native String Geometry_ExportToJson__SWIG_1(long jarg1,
    Geometry jarg1_);

  public final static native String Geometry_ExportToKML__SWIG_0(long jarg1,
    Geometry jarg1_, String jarg2);

  public final static native String Geometry_ExportToKML__SWIG_1(long jarg1,
    Geometry jarg1_);

  public final static native byte[] Geometry_ExportToWkb__SWIG_0(long jarg1,
    Geometry jarg1_, int jarg4);

  public final static native byte[] Geometry_ExportToWkb__SWIG_1(long jarg1,
    Geometry jarg1_);

  public final static native int Geometry_ExportToWkt__SWIG_0(long jarg1,
    Geometry jarg1_, String[] jarg2);

  public final static native String Geometry_ExportToWkt__SWIG_1(long jarg1,
    Geometry jarg1_);

  public final static native void Geometry_FlattenTo2D(long jarg1,
    Geometry jarg1_);

  public final static native double Geometry_GetArea(long jarg1, Geometry jarg1_);

  public final static native long Geometry_GetBoundary(long jarg1,
    Geometry jarg1_);

  public final static native int Geometry_GetCoordinateDimension(long jarg1,
    Geometry jarg1_);

  public final static native int Geometry_GetDimension(long jarg1,
    Geometry jarg1_);

  public final static native void Geometry_GetEnvelope(long jarg1,
    Geometry jarg1_, double[] jarg2);

  public final static native void Geometry_GetEnvelope3D(long jarg1,
    Geometry jarg1_, double[] jarg2);

  public final static native int Geometry_GetGeometryCount(long jarg1,
    Geometry jarg1_);

  public final static native String Geometry_GetGeometryName(long jarg1,
    Geometry jarg1_);

  public final static native long Geometry_GetGeometryRef(long jarg1,
    Geometry jarg1_, int jarg2);

  public final static native int Geometry_GetGeometryType(long jarg1,
    Geometry jarg1_);

  public final static native void Geometry_GetPoint(long jarg1,
    Geometry jarg1_, int jarg2, double[] jarg3);

  public final static native void Geometry_GetPoint_2D(long jarg1,
    Geometry jarg1_, int jarg2, double[] jarg3);

  public final static native int Geometry_GetPointCount(long jarg1,
    Geometry jarg1_);

  public final static native double[][] Geometry_GetPoints__SWIG_0(long jarg1,
    Geometry jarg1_, int jarg5);

  public final static native double[][] Geometry_GetPoints__SWIG_1(long jarg1,
    Geometry jarg1_);

  public final static native long Geometry_GetSpatialReference(long jarg1,
    Geometry jarg1_);

  public final static native double Geometry_GetX__SWIG_0(long jarg1,
    Geometry jarg1_, int jarg2);

  public final static native double Geometry_GetX__SWIG_1(long jarg1,
    Geometry jarg1_);

  public final static native double Geometry_GetY__SWIG_0(long jarg1,
    Geometry jarg1_, int jarg2);

  public final static native double Geometry_GetY__SWIG_1(long jarg1,
    Geometry jarg1_);

  public final static native double Geometry_GetZ__SWIG_0(long jarg1,
    Geometry jarg1_, int jarg2);

  public final static native double Geometry_GetZ__SWIG_1(long jarg1,
    Geometry jarg1_);

  public final static native boolean Geometry_Intersect(long jarg1,
    Geometry jarg1_, long jarg2, Geometry jarg2_);

  public final static native long Geometry_Intersection(long jarg1,
    Geometry jarg1_, long jarg2, Geometry jarg2_);

  public final static native boolean Geometry_Intersects(long jarg1,
    Geometry jarg1_, long jarg2, Geometry jarg2_);

  public final static native boolean Geometry_IsEmpty(long jarg1,
    Geometry jarg1_);

  public final static native boolean Geometry_IsRing(long jarg1, Geometry jarg1_);

  public final static native boolean Geometry_IsSimple(long jarg1,
    Geometry jarg1_);

  public final static native boolean Geometry_IsValid(long jarg1,
    Geometry jarg1_);

  public final static native double Geometry_Length(long jarg1, Geometry jarg1_);

  public final static native boolean Geometry_Overlaps(long jarg1,
    Geometry jarg1_, long jarg2, Geometry jarg2_);

  public final static native long Geometry_PointOnSurface(long jarg1,
    Geometry jarg1_);

  public final static native void Geometry_Segmentize(long jarg1,
    Geometry jarg1_, double jarg2);

  public final static native void Geometry_SetCoordinateDimension(long jarg1,
    Geometry jarg1_, int jarg2);

  public final static native void Geometry_SetPoint__SWIG_0(long jarg1,
    Geometry jarg1_, int jarg2, double jarg3, double jarg4, double jarg5);

  public final static native void Geometry_SetPoint__SWIG_1(long jarg1,
    Geometry jarg1_, int jarg2, double jarg3, double jarg4);

  public final static native void Geometry_SetPoint_2D(long jarg1,
    Geometry jarg1_, int jarg2, double jarg3, double jarg4);

  public final static native long Geometry_Simplify(long jarg1,
    Geometry jarg1_, double jarg2);

  public final static native long Geometry_SimplifyPreserveTopology(long jarg1,
    Geometry jarg1_, double jarg2);

  public final static native long Geometry_SymDifference(long jarg1,
    Geometry jarg1_, long jarg2, Geometry jarg2_);

  public final static native long Geometry_SymmetricDifference(long jarg1,
    Geometry jarg1_, long jarg2, Geometry jarg2_);

  public final static native boolean Geometry_Touches(long jarg1,
    Geometry jarg1_, long jarg2, Geometry jarg2_);

  public final static native int Geometry_Transform(long jarg1,
    Geometry jarg1_, long jarg2, CoordinateTransformation jarg2_);

  public final static native int Geometry_TransformTo(long jarg1,
    Geometry jarg1_, long jarg2, SpatialReference jarg2_);

  public final static native long Geometry_Union(long jarg1, Geometry jarg1_,
    long jarg2, Geometry jarg2_);

  public final static native long Geometry_UnionCascaded(long jarg1,
    Geometry jarg1_);

  public final static native boolean Geometry_Within(long jarg1,
    Geometry jarg1_, long jarg2, Geometry jarg2_);

  public final static native int Geometry_WkbSize(long jarg1, Geometry jarg1_);

  public final static native String GeometryTypeToName(int jarg1);

  public final static native int GeomFieldDefn_GetFieldType(long jarg1,
    GeomFieldDefn jarg1_);

  public final static native String GeomFieldDefn_GetName(long jarg1,
    GeomFieldDefn jarg1_);

  public final static native String GeomFieldDefn_GetNameRef(long jarg1,
    GeomFieldDefn jarg1_);

  public final static native long GeomFieldDefn_GetSpatialRef(long jarg1,
    GeomFieldDefn jarg1_);

  public final static native int GeomFieldDefn_IsIgnored(long jarg1,
    GeomFieldDefn jarg1_);

  public final static native void GeomFieldDefn_SetIgnored(long jarg1,
    GeomFieldDefn jarg1_, int jarg2);

  public final static native void GeomFieldDefn_SetName(long jarg1,
    GeomFieldDefn jarg1_, String jarg2);

  public final static native void GeomFieldDefn_SetSpatialRef(long jarg1,
    GeomFieldDefn jarg1_, long jarg2, SpatialReference jarg2_);

  public final static native void GeomFieldDefn_SetType(long jarg1,
    GeomFieldDefn jarg1_, int jarg2);

  public final static native long GetDriver(int jarg1);

  public final static native long GetDriverByName(String jarg1);

  public final static native int GetDriverCount();

  public final static native String GetFieldTypeName(int jarg1);

  public final static native long GetOpenDS(int jarg1);

  public final static native int GetOpenDSCount();

  public static boolean isAvailable() {
    return available;
  }

  public final static native int Layer_AlterFieldDefn(long jarg1, Layer jarg1_,
    int jarg2, long jarg3, FieldDefn jarg3_, int jarg4);

  public final static native int Layer_Clip__SWIG_0(long jarg1, Layer jarg1_,
    long jarg2, Layer jarg2_, long jarg3, Layer jarg3_, java.util.Vector jarg4,
    ProgressCallback jarg5);

  public final static native int Layer_Clip__SWIG_2(long jarg1, Layer jarg1_,
    long jarg2, Layer jarg2_, long jarg3, Layer jarg3_, java.util.Vector jarg4);

  public final static native int Layer_Clip__SWIG_3(long jarg1, Layer jarg1_,
    long jarg2, Layer jarg2_, long jarg3, Layer jarg3_);

  public final static native int Layer_CommitTransaction(long jarg1,
    Layer jarg1_);

  public final static native int Layer_CreateFeature(long jarg1, Layer jarg1_,
    long jarg2, Feature jarg2_);

  public final static native int Layer_CreateField__SWIG_0(long jarg1,
    Layer jarg1_, long jarg2, FieldDefn jarg2_, int jarg3);

  public final static native int Layer_CreateField__SWIG_1(long jarg1,
    Layer jarg1_, long jarg2, FieldDefn jarg2_);

  public final static native int Layer_CreateGeomField__SWIG_0(long jarg1,
    Layer jarg1_, long jarg2, GeomFieldDefn jarg2_, int jarg3);

  public final static native int Layer_CreateGeomField__SWIG_1(long jarg1,
    Layer jarg1_, long jarg2, GeomFieldDefn jarg2_);

  public final static native int Layer_DeleteFeature(long jarg1, Layer jarg1_,
    int jarg2);

  public final static native int Layer_DeleteField(long jarg1, Layer jarg1_,
    int jarg2);

  public final static native int Layer_Erase__SWIG_0(long jarg1, Layer jarg1_,
    long jarg2, Layer jarg2_, long jarg3, Layer jarg3_, java.util.Vector jarg4,
    ProgressCallback jarg5);

  public final static native int Layer_Erase__SWIG_2(long jarg1, Layer jarg1_,
    long jarg2, Layer jarg2_, long jarg3, Layer jarg3_, java.util.Vector jarg4);

  public final static native int Layer_Erase__SWIG_3(long jarg1, Layer jarg1_,
    long jarg2, Layer jarg2_, long jarg3, Layer jarg3_);

  public final static native int Layer_FindFieldIndex(long jarg1, Layer jarg1_,
    String jarg2, int jarg3);

  public final static native int Layer_GetExtent(long jarg1, Layer jarg1_,
    double[] jarg2, int jarg3);

  public final static native long Layer_GetFeature(long jarg1, Layer jarg1_,
    int jarg2);

  public final static native int Layer_GetFeatureCount__SWIG_0(long jarg1,
    Layer jarg1_, int jarg2);

  public final static native int Layer_GetFeatureCount__SWIG_1(long jarg1,
    Layer jarg1_);

  public final static native long Layer_GetFeaturesRead(long jarg1, Layer jarg1_);

  public final static native String Layer_GetFIDColumn(long jarg1, Layer jarg1_);

  public final static native String Layer_GetGeometryColumn(long jarg1,
    Layer jarg1_);

  public final static native int Layer_GetGeomType(long jarg1, Layer jarg1_);

  public final static native long Layer_GetLayerDefn(long jarg1, Layer jarg1_);

  public final static native String Layer_GetName(long jarg1, Layer jarg1_);

  public final static native long Layer_GetNextFeature(long jarg1, Layer jarg1_);

  public final static native int Layer_GetRefCount(long jarg1, Layer jarg1_);

  public final static native long Layer_GetSpatialFilter(long jarg1,
    Layer jarg1_);

  public final static native long Layer_GetSpatialRef(long jarg1, Layer jarg1_);

  public final static native long Layer_GetStyleTable(long jarg1, Layer jarg1_);

  public final static native int Layer_Identity__SWIG_0(long jarg1,
    Layer jarg1_, long jarg2, Layer jarg2_, long jarg3, Layer jarg3_,
    java.util.Vector jarg4, ProgressCallback jarg5);

  public final static native int Layer_Identity__SWIG_2(long jarg1,
    Layer jarg1_, long jarg2, Layer jarg2_, long jarg3, Layer jarg3_,
    java.util.Vector jarg4);

  public final static native int Layer_Identity__SWIG_3(long jarg1,
    Layer jarg1_, long jarg2, Layer jarg2_, long jarg3, Layer jarg3_);

  public final static native int Layer_Intersection__SWIG_0(long jarg1,
    Layer jarg1_, long jarg2, Layer jarg2_, long jarg3, Layer jarg3_,
    java.util.Vector jarg4, ProgressCallback jarg5);

  public final static native int Layer_Intersection__SWIG_2(long jarg1,
    Layer jarg1_, long jarg2, Layer jarg2_, long jarg3, Layer jarg3_,
    java.util.Vector jarg4);

  public final static native int Layer_Intersection__SWIG_3(long jarg1,
    Layer jarg1_, long jarg2, Layer jarg2_, long jarg3, Layer jarg3_);

  public final static native int Layer_ReorderField(long jarg1, Layer jarg1_,
    int jarg2, int jarg3);

  public final static native int Layer_ReorderFields(long jarg1, Layer jarg1_,
    int[] jarg2);

  public final static native void Layer_ResetReading(long jarg1, Layer jarg1_);

  public final static native int Layer_RollbackTransaction(long jarg1,
    Layer jarg1_);

  public final static native int Layer_SetAttributeFilter(long jarg1,
    Layer jarg1_, String jarg2);

  public final static native int Layer_SetFeature(long jarg1, Layer jarg1_,
    long jarg2, Feature jarg2_);

  public final static native int Layer_SetIgnoredFields(long jarg1,
    Layer jarg1_, java.util.Vector jarg2);

  public final static native int Layer_SetNextByIndex(long jarg1, Layer jarg1_,
    int jarg2);

  public final static native void Layer_SetSpatialFilter__SWIG_0(long jarg1,
    Layer jarg1_, long jarg2, Geometry jarg2_);

  public final static native void Layer_SetSpatialFilter__SWIG_1(long jarg1,
    Layer jarg1_, int jarg2, long jarg3, Geometry jarg3_);

  public final static native void Layer_SetSpatialFilterRect__SWIG_0(
    long jarg1, Layer jarg1_, double jarg2, double jarg3, double jarg4,
    double jarg5);

  public final static native void Layer_SetSpatialFilterRect__SWIG_1(
    long jarg1, Layer jarg1_, int jarg2, double jarg3, double jarg4,
    double jarg5, double jarg6);

  public final static native void Layer_SetStyleTable(long jarg1, Layer jarg1_,
    long jarg2, StyleTable jarg2_);

  public final static native int Layer_StartTransaction(long jarg1, Layer jarg1_);

  public final static native int Layer_SymDifference__SWIG_0(long jarg1,
    Layer jarg1_, long jarg2, Layer jarg2_, long jarg3, Layer jarg3_,
    java.util.Vector jarg4, ProgressCallback jarg5);

  public final static native int Layer_SymDifference__SWIG_2(long jarg1,
    Layer jarg1_, long jarg2, Layer jarg2_, long jarg3, Layer jarg3_,
    java.util.Vector jarg4);

  public final static native int Layer_SymDifference__SWIG_3(long jarg1,
    Layer jarg1_, long jarg2, Layer jarg2_, long jarg3, Layer jarg3_);

  public final static native int Layer_SyncToDisk(long jarg1, Layer jarg1_);

  public final static native boolean Layer_TestCapability(long jarg1,
    Layer jarg1_, String jarg2);

  public final static native int Layer_Union__SWIG_0(long jarg1, Layer jarg1_,
    long jarg2, Layer jarg2_, long jarg3, Layer jarg3_, java.util.Vector jarg4,
    ProgressCallback jarg5);

  public final static native int Layer_Union__SWIG_2(long jarg1, Layer jarg1_,
    long jarg2, Layer jarg2_, long jarg3, Layer jarg3_, java.util.Vector jarg4);

  public final static native int Layer_Union__SWIG_3(long jarg1, Layer jarg1_,
    long jarg2, Layer jarg2_, long jarg3, Layer jarg3_);

  public final static native int Layer_Update__SWIG_0(long jarg1, Layer jarg1_,
    long jarg2, Layer jarg2_, long jarg3, Layer jarg3_, java.util.Vector jarg4,
    ProgressCallback jarg5);

  public final static native int Layer_Update__SWIG_2(long jarg1, Layer jarg1_,
    long jarg2, Layer jarg2_, long jarg3, Layer jarg3_, java.util.Vector jarg4);

  public final static native int Layer_Update__SWIG_3(long jarg1, Layer jarg1_,
    long jarg2, Layer jarg2_, long jarg3, Layer jarg3_);

  public final static native long new_Feature(long jarg1, FeatureDefn jarg1_);

  public final static native long new_FeatureDefn__SWIG_0(String jarg1);

  public final static native long new_FeatureDefn__SWIG_1();

  public final static native long new_FieldDefn__SWIG_0(String jarg1, int jarg2);

  public final static native long new_FieldDefn__SWIG_1(String jarg1);

  public final static native long new_FieldDefn__SWIG_2();

  public final static native long new_Geometry__SWIG_0(int jarg1, String jarg2,
    byte[] jarg3, String jarg5);

  public final static native long new_Geometry__SWIG_1(int jarg1);

  public final static native long new_GeomFieldDefn__SWIG_0(String jarg1,
    int jarg2);

  public final static native long new_GeomFieldDefn__SWIG_1(String jarg1);

  public final static native long new_GeomFieldDefn__SWIG_2();

  public final static native long new_ProgressCallback();

  public final static native long new_StyleTable();

  public final static native long new_TermProgressCallback();

  public final static native long Open__SWIG_0(String jarg1, int jarg2);

  public final static native long Open__SWIG_1(String jarg1);

  public final static native long OpenShared__SWIG_0(String jarg1, int jarg2);

  public final static native long OpenShared__SWIG_1(String jarg1);

  public final static native int ProgressCallback_run(long jarg1,
    ProgressCallback jarg1_, double jarg2, String jarg3);

  public final static native void RegisterAll();

  public final static native int SetGenerate_DB2_V72_BYTE_ORDER(int jarg1);

  public final static native int StyleTable_AddStyle(long jarg1,
    StyleTable jarg1_, String jarg2, String jarg3);

  public final static native String StyleTable_Find(long jarg1,
    StyleTable jarg1_, String jarg2);

  public final static native String StyleTable_GetLastStyleName(long jarg1,
    StyleTable jarg1_);

  public final static native String StyleTable_GetNextStyle(long jarg1,
    StyleTable jarg1_);

  public final static native int StyleTable_LoadStyleTable(long jarg1,
    StyleTable jarg1_, String jarg2);

  public final static native void StyleTable_ResetStyleStringReading(
    long jarg1, StyleTable jarg1_);

  public final static native int StyleTable_SaveStyleTable(long jarg1,
    StyleTable jarg1_, String jarg2);

  public final static native long SWIGTermProgressCallbackUpcast(long jarg1);

  public final static native int TermProgressCallback_run(long jarg1,
    TermProgressCallback jarg1_, double jarg2, String jarg3);

  public final static native void UseExceptions();
}
