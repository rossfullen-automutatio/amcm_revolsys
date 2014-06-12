/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 1.3.39
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package org.gdal.osr;

public class SpatialReference implements Cloneable {
  public static long getCPtr(final SpatialReference obj) {
    return obj == null ? 0 : obj.swigCPtr;
  }

  private long swigCPtr;

  protected boolean swigCMemOwn;

  public SpatialReference() {
    this(osrJNI.new_SpatialReference__SWIG_1(), true);
  }

  public SpatialReference(final long cPtr, final boolean cMemoryOwn) {
    this.swigCMemOwn = cMemoryOwn;
    this.swigCPtr = cPtr;
  }

  public SpatialReference(final String wkt) {
    this(osrJNI.new_SpatialReference__SWIG_0(wkt), true);
  }

  public String __str__() {
    return osrJNI.SpatialReference___str__(this.swigCPtr, this);
  }

  public int AutoIdentifyEPSG() {
    return osrJNI.SpatialReference_AutoIdentifyEPSG(this.swigCPtr, this);
  }

  @Override
  public Object clone() {
    return Clone();
  }

  public SpatialReference Clone() {
    final long cPtr = osrJNI.SpatialReference_Clone(this.swigCPtr, this);
    return cPtr == 0 ? null : new SpatialReference(cPtr, true);
  }

  public SpatialReference CloneGeogCS() {
    final long cPtr = osrJNI.SpatialReference_CloneGeogCS(this.swigCPtr, this);
    return cPtr == 0 ? null : new SpatialReference(cPtr, true);
  }

  public int CopyGeogCSFrom(final SpatialReference rhs) {
    return osrJNI.SpatialReference_CopyGeogCSFrom(this.swigCPtr, this,
      SpatialReference.getCPtr(rhs), rhs);
  }

  public synchronized void delete() {
    if (this.swigCPtr != 0 && this.swigCMemOwn) {
      this.swigCMemOwn = false;
      osrJNI.delete_SpatialReference(this.swigCPtr);
    }
    this.swigCPtr = 0;
  }

  public int EPSGTreatsAsLatLong() {
    return osrJNI.SpatialReference_EPSGTreatsAsLatLong(this.swigCPtr, this);
  }

  public int EPSGTreatsAsNorthingEasting() {
    return osrJNI.SpatialReference_EPSGTreatsAsNorthingEasting(this.swigCPtr,
      this);
  }

  @Override
  public boolean equals(final Object obj) {
    boolean equal = false;
    if (obj instanceof SpatialReference) {
      equal = ((SpatialReference)obj).swigCPtr == this.swigCPtr;
    }
    return equal;
  }

  public String ExportToMICoordSys() {
    final String array[] = new String[] {
      null
    };
    ExportToMICoordSys(array);
    return array[0];
  }

  public int ExportToMICoordSys(final String[] argout) {
    return osrJNI.SpatialReference_ExportToMICoordSys(this.swigCPtr, this,
      argout);
  }

  public int ExportToPCI(final String[] proj, final String[] units,
    final double[] parms) {
    return osrJNI.SpatialReference_ExportToPCI(this.swigCPtr, this, proj,
      units, parms);
  }

  public String ExportToPrettyWkt() {
    final String array[] = new String[] {
      null
    };
    ExportToPrettyWkt(array);
    return array[0];
  }

  public String ExportToPrettyWkt(final int simplify) {
    final String array[] = new String[] {
      null
    };
    ExportToPrettyWkt(array, simplify);
    return array[0];
  }

  public int ExportToPrettyWkt(final String[] argout) {
    return osrJNI.SpatialReference_ExportToPrettyWkt__SWIG_1(this.swigCPtr,
      this, argout);
  }

  public int ExportToPrettyWkt(final String[] argout, final int simplify) {
    return osrJNI.SpatialReference_ExportToPrettyWkt__SWIG_0(this.swigCPtr,
      this, argout, simplify);
  }

  public String ExportToProj4() {
    final String array[] = new String[] {
      null
    };
    ExportToProj4(array);
    return array[0];
  }

  public int ExportToProj4(final String[] argout) {
    return osrJNI.SpatialReference_ExportToProj4(this.swigCPtr, this, argout);
  }

  public int ExportToUSGS(final int[] code, final int[] zone,
    final double[] parms, final int[] datum) {
    return osrJNI.SpatialReference_ExportToUSGS(this.swigCPtr, this, code,
      zone, parms, datum);
  }

  public String ExportToWkt() {
    final String array[] = new String[] {
      null
    };
    ExportToWkt(array);
    return array[0];
  }

  public int ExportToWkt(final String[] argout) {
    return osrJNI.SpatialReference_ExportToWkt(this.swigCPtr, this, argout);
  }

  public String ExportToXML() {
    final String array[] = new String[] {
      null
    };
    ExportToXML(array);
    return array[0];
  }

  public String ExportToXML(final String dialect) {
    final String array[] = new String[] {
      null
    };
    ExportToXML(array, dialect);
    return array[0];
  }

  public int ExportToXML(final String[] argout) {
    return osrJNI.SpatialReference_ExportToXML__SWIG_1(this.swigCPtr, this,
      argout);
  }

  public int ExportToXML(final String[] argout, final String dialect) {
    return osrJNI.SpatialReference_ExportToXML__SWIG_0(this.swigCPtr, this,
      argout, dialect);
  }

  @Override
  protected void finalize() {
    delete();
  }

  public int Fixup() {
    return osrJNI.SpatialReference_Fixup(this.swigCPtr, this);
  }

  public int FixupOrdering() {
    return osrJNI.SpatialReference_FixupOrdering(this.swigCPtr, this);
  }

  public double GetAngularUnits() {
    return osrJNI.SpatialReference_GetAngularUnits(this.swigCPtr, this);
  }

  public String GetAttrValue(final String name) {
    return osrJNI.SpatialReference_GetAttrValue__SWIG_1(this.swigCPtr, this,
      name);
  }

  public String GetAttrValue(final String name, final int child) {
    return osrJNI.SpatialReference_GetAttrValue__SWIG_0(this.swigCPtr, this,
      name, child);
  }

  public String GetAuthorityCode(final String target_key) {
    return osrJNI.SpatialReference_GetAuthorityCode(this.swigCPtr, this,
      target_key);
  }

  public String GetAuthorityName(final String target_key) {
    return osrJNI.SpatialReference_GetAuthorityName(this.swigCPtr, this,
      target_key);
  }

  public double GetInvFlattening() {
    return osrJNI.SpatialReference_GetInvFlattening(this.swigCPtr, this);
  }

  public double GetLinearUnits() {
    return osrJNI.SpatialReference_GetLinearUnits(this.swigCPtr, this);
  }

  public String GetLinearUnitsName() {
    return osrJNI.SpatialReference_GetLinearUnitsName(this.swigCPtr, this);
  }

  public double GetNormProjParm(final String name) {
    return osrJNI.SpatialReference_GetNormProjParm__SWIG_1(this.swigCPtr, this,
      name);
  }

  public double GetNormProjParm(final String name, final double default_val) {
    return osrJNI.SpatialReference_GetNormProjParm__SWIG_0(this.swigCPtr, this,
      name, default_val);
  }

  public double GetProjParm(final String name) {
    return osrJNI.SpatialReference_GetProjParm__SWIG_1(this.swigCPtr, this,
      name);
  }

  public double GetProjParm(final String name, final double default_val) {
    return osrJNI.SpatialReference_GetProjParm__SWIG_0(this.swigCPtr, this,
      name, default_val);
  }

  public double GetSemiMajor() {
    return osrJNI.SpatialReference_GetSemiMajor(this.swigCPtr, this);
  }

  public double GetSemiMinor() {
    return osrJNI.SpatialReference_GetSemiMinor(this.swigCPtr, this);
  }

  public double[] GetTOWGS84() {
    final double array[] = new double[7];
    GetTOWGS84(array);
    return array;
  }

  public int GetTOWGS84(final double[] argout) {
    return osrJNI.SpatialReference_GetTOWGS84(this.swigCPtr, this, argout);
  }

  public int GetUTMZone() {
    return osrJNI.SpatialReference_GetUTMZone(this.swigCPtr, this);
  }

  @Override
  public int hashCode() {
    return (int)this.swigCPtr;
  }

  public int ImportFromEPSG(final int arg) {
    return osrJNI.SpatialReference_ImportFromEPSG(this.swigCPtr, this, arg);
  }

  public int ImportFromEPSGA(final int arg) {
    return osrJNI.SpatialReference_ImportFromEPSGA(this.swigCPtr, this, arg);
  }

  public int ImportFromERM(final String proj, final String datum,
    final String units) {
    return osrJNI.SpatialReference_ImportFromERM(this.swigCPtr, this, proj,
      datum, units);
  }

  public int ImportFromESRI(final java.util.Vector ppszInput) {
    return osrJNI.SpatialReference_ImportFromESRI(this.swigCPtr, this,
      ppszInput);
  }

  public int ImportFromMICoordSys(final String pszCoordSys) {
    return osrJNI.SpatialReference_ImportFromMICoordSys(this.swigCPtr, this,
      pszCoordSys);
  }

  public int ImportFromOzi(final String datum, final String proj,
    final String projParms) {
    return osrJNI.SpatialReference_ImportFromOzi(this.swigCPtr, this, datum,
      proj, projParms);
  }

  public int ImportFromPCI(final String proj) {
    return osrJNI.SpatialReference_ImportFromPCI__SWIG_2(this.swigCPtr, this,
      proj);
  }

  public int ImportFromPCI(final String proj, final String units) {
    return osrJNI.SpatialReference_ImportFromPCI__SWIG_1(this.swigCPtr, this,
      proj, units);
  }

  public int ImportFromPCI(final String proj, final String units,
    final double[] argin) {
    return osrJNI.SpatialReference_ImportFromPCI__SWIG_0(this.swigCPtr, this,
      proj, units, argin);
  }

  public int ImportFromProj4(final String ppszInput) {
    return osrJNI.SpatialReference_ImportFromProj4(this.swigCPtr, this,
      ppszInput);
  }

  public int ImportFromUrl(final String url) {
    return osrJNI.SpatialReference_ImportFromUrl(this.swigCPtr, this, url);
  }

  public int ImportFromUSGS(final int proj_code) {
    return osrJNI.SpatialReference_ImportFromUSGS__SWIG_3(this.swigCPtr, this,
      proj_code);
  }

  public int ImportFromUSGS(final int proj_code, final int zone) {
    return osrJNI.SpatialReference_ImportFromUSGS__SWIG_2(this.swigCPtr, this,
      proj_code, zone);
  }

  public int ImportFromUSGS(final int proj_code, final int zone,
    final double[] argin) {
    return osrJNI.SpatialReference_ImportFromUSGS__SWIG_1(this.swigCPtr, this,
      proj_code, zone, argin);
  }

  public int ImportFromUSGS(final int proj_code, final int zone,
    final double[] argin, final int datum_code) {
    return osrJNI.SpatialReference_ImportFromUSGS__SWIG_0(this.swigCPtr, this,
      proj_code, zone, argin, datum_code);
  }

  public int ImportFromWkt(final String ppszInput) {
    return osrJNI.SpatialReference_ImportFromWkt(this.swigCPtr, this, ppszInput);
  }

  public int ImportFromXML(final String xmlString) {
    return osrJNI.SpatialReference_ImportFromXML(this.swigCPtr, this, xmlString);
  }

  public int IsCompound() {
    return osrJNI.SpatialReference_IsCompound(this.swigCPtr, this);
  }

  public int IsGeocentric() {
    return osrJNI.SpatialReference_IsGeocentric(this.swigCPtr, this);
  }

  public int IsGeographic() {
    return osrJNI.SpatialReference_IsGeographic(this.swigCPtr, this);
  }

  public int IsLocal() {
    return osrJNI.SpatialReference_IsLocal(this.swigCPtr, this);
  }

  public int IsProjected() {
    return osrJNI.SpatialReference_IsProjected(this.swigCPtr, this);
  }

  public int IsSame(final SpatialReference rhs) {
    return osrJNI.SpatialReference_IsSame(this.swigCPtr, this,
      SpatialReference.getCPtr(rhs), rhs);
  }

  public int IsSameGeogCS(final SpatialReference rhs) {
    return osrJNI.SpatialReference_IsSameGeogCS(this.swigCPtr, this,
      SpatialReference.getCPtr(rhs), rhs);
  }

  public int IsSameVertCS(final SpatialReference rhs) {
    return osrJNI.SpatialReference_IsSameVertCS(this.swigCPtr, this,
      SpatialReference.getCPtr(rhs), rhs);
  }

  public int IsVertical() {
    return osrJNI.SpatialReference_IsVertical(this.swigCPtr, this);
  }

  public int MorphFromESRI() {
    return osrJNI.SpatialReference_MorphFromESRI(this.swigCPtr, this);
  }

  public int MorphToESRI() {
    return osrJNI.SpatialReference_MorphToESRI(this.swigCPtr, this);
  }

  public int SetACEA(final double stdp1, final double stdp2, final double clat,
    final double clong, final double fe, final double fn) {
    return osrJNI.SpatialReference_SetACEA(this.swigCPtr, this, stdp1, stdp2,
      clat, clong, fe, fn);
  }

  public int SetAE(final double clat, final double clong, final double fe,
    final double fn) {
    return osrJNI.SpatialReference_SetAE(this.swigCPtr, this, clat, clong, fe,
      fn);
  }

  public int SetAngularUnits(final String name, final double to_radians) {
    return osrJNI.SpatialReference_SetAngularUnits(this.swigCPtr, this, name,
      to_radians);
  }

  public int SetAttrValue(final String name, final String value) {
    return osrJNI.SpatialReference_SetAttrValue(this.swigCPtr, this, name,
      value);
  }

  public int SetAuthority(final String pszTargetKey, final String pszAuthority,
    final int nCode) {
    return osrJNI.SpatialReference_SetAuthority(this.swigCPtr, this,
      pszTargetKey, pszAuthority, nCode);
  }

  public int SetBonne(final double stdp, final double cm, final double fe,
    final double fn) {
    return osrJNI.SpatialReference_SetBonne(this.swigCPtr, this, stdp, cm, fe,
      fn);
  }

  public int SetCEA(final double stdp1, final double cm, final double fe,
    final double fn) {
    return osrJNI.SpatialReference_SetCEA(this.swigCPtr, this, stdp1, cm, fe,
      fn);
  }

  public int SetCompoundCS(final String name, final SpatialReference horizcs,
    final SpatialReference vertcs) {
    return osrJNI.SpatialReference_SetCompoundCS(this.swigCPtr, this, name,
      SpatialReference.getCPtr(horizcs), horizcs,
      SpatialReference.getCPtr(vertcs), vertcs);
  }

  public int SetCS(final double clat, final double clong, final double fe,
    final double fn) {
    return osrJNI.SpatialReference_SetCS(this.swigCPtr, this, clat, clong, fe,
      fn);
  }

  public int SetEC(final double stdp1, final double stdp2, final double clat,
    final double clong, final double fe, final double fn) {
    return osrJNI.SpatialReference_SetEC(this.swigCPtr, this, stdp1, stdp2,
      clat, clong, fe, fn);
  }

  public int SetEckertIV(final double cm, final double fe, final double fn) {
    return osrJNI.SpatialReference_SetEckertIV(this.swigCPtr, this, cm, fe, fn);
  }

  public int SetEckertVI(final double cm, final double fe, final double fn) {
    return osrJNI.SpatialReference_SetEckertVI(this.swigCPtr, this, cm, fe, fn);
  }

  public int SetEquirectangular(final double clat, final double clong,
    final double fe, final double fn) {
    return osrJNI.SpatialReference_SetEquirectangular(this.swigCPtr, this,
      clat, clong, fe, fn);
  }

  public int SetEquirectangular2(final double clat, final double clong,
    final double pseudostdparallellat, final double fe, final double fn) {
    return osrJNI.SpatialReference_SetEquirectangular2(this.swigCPtr, this,
      clat, clong, pseudostdparallellat, fe, fn);
  }

  public int SetFromUserInput(final String name) {
    return osrJNI.SpatialReference_SetFromUserInput(this.swigCPtr, this, name);
  }

  public int SetGaussSchreiberTMercator(final double clat, final double clong,
    final double sc, final double fe, final double fn) {
    return osrJNI.SpatialReference_SetGaussSchreiberTMercator(this.swigCPtr,
      this, clat, clong, sc, fe, fn);
  }

  public int SetGeocCS() {
    return osrJNI.SpatialReference_SetGeocCS__SWIG_1(this.swigCPtr, this);
  }

  public int SetGeocCS(final String name) {
    return osrJNI.SpatialReference_SetGeocCS__SWIG_0(this.swigCPtr, this, name);
  }

  public int SetGeogCS(final String pszGeogName, final String pszDatumName,
    final String pszEllipsoidName, final double dfSemiMajor,
    final double dfInvFlattening) {
    return osrJNI.SpatialReference_SetGeogCS__SWIG_4(this.swigCPtr, this,
      pszGeogName, pszDatumName, pszEllipsoidName, dfSemiMajor, dfInvFlattening);
  }

  public int SetGeogCS(final String pszGeogName, final String pszDatumName,
    final String pszEllipsoidName, final double dfSemiMajor,
    final double dfInvFlattening, final String pszPMName) {
    return osrJNI.SpatialReference_SetGeogCS__SWIG_3(this.swigCPtr, this,
      pszGeogName, pszDatumName, pszEllipsoidName, dfSemiMajor,
      dfInvFlattening, pszPMName);
  }

  public int SetGeogCS(final String pszGeogName, final String pszDatumName,
    final String pszEllipsoidName, final double dfSemiMajor,
    final double dfInvFlattening, final String pszPMName,
    final double dfPMOffset) {
    return osrJNI.SpatialReference_SetGeogCS__SWIG_2(this.swigCPtr, this,
      pszGeogName, pszDatumName, pszEllipsoidName, dfSemiMajor,
      dfInvFlattening, pszPMName, dfPMOffset);
  }

  public int SetGeogCS(final String pszGeogName, final String pszDatumName,
    final String pszEllipsoidName, final double dfSemiMajor,
    final double dfInvFlattening, final String pszPMName,
    final double dfPMOffset, final String pszUnits) {
    return osrJNI.SpatialReference_SetGeogCS__SWIG_1(this.swigCPtr, this,
      pszGeogName, pszDatumName, pszEllipsoidName, dfSemiMajor,
      dfInvFlattening, pszPMName, dfPMOffset, pszUnits);
  }

  public int SetGeogCS(final String pszGeogName, final String pszDatumName,
    final String pszEllipsoidName, final double dfSemiMajor,
    final double dfInvFlattening, final String pszPMName,
    final double dfPMOffset, final String pszUnits,
    final double dfConvertToRadians) {
    return osrJNI.SpatialReference_SetGeogCS__SWIG_0(this.swigCPtr, this,
      pszGeogName, pszDatumName, pszEllipsoidName, dfSemiMajor,
      dfInvFlattening, pszPMName, dfPMOffset, pszUnits, dfConvertToRadians);
  }

  public int SetGEOS(final double cm, final double satelliteheight,
    final double fe, final double fn) {
    return osrJNI.SpatialReference_SetGEOS(this.swigCPtr, this, cm,
      satelliteheight, fe, fn);
  }

  public int SetGH(final double cm, final double fe, final double fn) {
    return osrJNI.SpatialReference_SetGH(this.swigCPtr, this, cm, fe, fn);
  }

  public int SetGnomonic(final double clat, final double clong,
    final double fe, final double fn) {
    return osrJNI.SpatialReference_SetGnomonic(this.swigCPtr, this, clat,
      clong, fe, fn);
  }

  public int SetGS(final double cm, final double fe, final double fn) {
    return osrJNI.SpatialReference_SetGS(this.swigCPtr, this, cm, fe, fn);
  }

  public int SetHOM(final double clat, final double clong,
    final double azimuth, final double recttoskew, final double scale,
    final double fe, final double fn) {
    return osrJNI.SpatialReference_SetHOM(this.swigCPtr, this, clat, clong,
      azimuth, recttoskew, scale, fe, fn);
  }

  public int SetHOM2PNO(final double clat, final double dfLat1,
    final double dfLong1, final double dfLat2, final double dfLong2,
    final double scale, final double fe, final double fn) {
    return osrJNI.SpatialReference_SetHOM2PNO(this.swigCPtr, this, clat,
      dfLat1, dfLong1, dfLat2, dfLong2, scale, fe, fn);
  }

  public int SetIGH() {
    return osrJNI.SpatialReference_SetIGH(this.swigCPtr, this);
  }

  public int SetKrovak(final double clat, final double clong,
    final double azimuth, final double pseudostdparallellat,
    final double scale, final double fe, final double fn) {
    return osrJNI.SpatialReference_SetKrovak(this.swigCPtr, this, clat, clong,
      azimuth, pseudostdparallellat, scale, fe, fn);
  }

  public int SetLAEA(final double clat, final double clong, final double fe,
    final double fn) {
    return osrJNI.SpatialReference_SetLAEA(this.swigCPtr, this, clat, clong,
      fe, fn);
  }

  public int SetLCC(final double stdp1, final double stdp2, final double clat,
    final double clong, final double fe, final double fn) {
    return osrJNI.SpatialReference_SetLCC(this.swigCPtr, this, stdp1, stdp2,
      clat, clong, fe, fn);
  }

  public int SetLCC1SP(final double clat, final double clong,
    final double scale, final double fe, final double fn) {
    return osrJNI.SpatialReference_SetLCC1SP(this.swigCPtr, this, clat, clong,
      scale, fe, fn);
  }

  public int SetLCCB(final double stdp1, final double stdp2, final double clat,
    final double clong, final double fe, final double fn) {
    return osrJNI.SpatialReference_SetLCCB(this.swigCPtr, this, stdp1, stdp2,
      clat, clong, fe, fn);
  }

  public int SetLinearUnits(final String name, final double to_meters) {
    return osrJNI.SpatialReference_SetLinearUnits(this.swigCPtr, this, name,
      to_meters);
  }

  public int SetLinearUnitsAndUpdateParameters(final String name,
    final double to_meters) {
    return osrJNI.SpatialReference_SetLinearUnitsAndUpdateParameters(
      this.swigCPtr, this, name, to_meters);
  }

  public int SetLocalCS(final String pszName) {
    return osrJNI.SpatialReference_SetLocalCS(this.swigCPtr, this, pszName);
  }

  public int SetMC(final double clat, final double clong, final double fe,
    final double fn) {
    return osrJNI.SpatialReference_SetMC(this.swigCPtr, this, clat, clong, fe,
      fn);
  }

  public int SetMercator(final double clat, final double clong,
    final double scale, final double fe, final double fn) {
    return osrJNI.SpatialReference_SetMercator(this.swigCPtr, this, clat,
      clong, scale, fe, fn);
  }

  public int SetMollweide(final double cm, final double fe, final double fn) {
    return osrJNI.SpatialReference_SetMollweide(this.swigCPtr, this, cm, fe, fn);
  }

  public int SetNormProjParm(final String name, final double val) {
    return osrJNI.SpatialReference_SetNormProjParm(this.swigCPtr, this, name,
      val);
  }

  public int SetNZMG(final double clat, final double clong, final double fe,
    final double fn) {
    return osrJNI.SpatialReference_SetNZMG(this.swigCPtr, this, clat, clong,
      fe, fn);
  }

  public int SetOrthographic(final double clat, final double clong,
    final double fe, final double fn) {
    return osrJNI.SpatialReference_SetOrthographic(this.swigCPtr, this, clat,
      clong, fe, fn);
  }

  public int SetOS(final double dfOriginLat, final double dfCMeridian,
    final double scale, final double fe, final double fn) {
    return osrJNI.SpatialReference_SetOS(this.swigCPtr, this, dfOriginLat,
      dfCMeridian, scale, fe, fn);
  }

  public int SetPolyconic(final double clat, final double clong,
    final double fe, final double fn) {
    return osrJNI.SpatialReference_SetPolyconic(this.swigCPtr, this, clat,
      clong, fe, fn);
  }

  public int SetProjCS() {
    return osrJNI.SpatialReference_SetProjCS__SWIG_1(this.swigCPtr, this);
  }

  public int SetProjCS(final String name) {
    return osrJNI.SpatialReference_SetProjCS__SWIG_0(this.swigCPtr, this, name);
  }

  public int SetProjection(final String arg) {
    return osrJNI.SpatialReference_SetProjection(this.swigCPtr, this, arg);
  }

  public int SetProjParm(final String name, final double val) {
    return osrJNI.SpatialReference_SetProjParm(this.swigCPtr, this, name, val);
  }

  public int SetPS(final double clat, final double clong, final double scale,
    final double fe, final double fn) {
    return osrJNI.SpatialReference_SetPS(this.swigCPtr, this, clat, clong,
      scale, fe, fn);
  }

  public int SetRobinson(final double clong, final double fe, final double fn) {
    return osrJNI.SpatialReference_SetRobinson(this.swigCPtr, this, clong, fe,
      fn);
  }

  public int SetSinusoidal(final double clong, final double fe, final double fn) {
    return osrJNI.SpatialReference_SetSinusoidal(this.swigCPtr, this, clong,
      fe, fn);
  }

  public int SetSOC(final double latitudeoforigin, final double cm,
    final double fe, final double fn) {
    return osrJNI.SpatialReference_SetSOC(this.swigCPtr, this,
      latitudeoforigin, cm, fe, fn);
  }

  public int SetStatePlane(final int zone) {
    return osrJNI.SpatialReference_SetStatePlane__SWIG_3(this.swigCPtr, this,
      zone);
  }

  public int SetStatePlane(final int zone, final int is_nad83) {
    return osrJNI.SpatialReference_SetStatePlane__SWIG_2(this.swigCPtr, this,
      zone, is_nad83);
  }

  public int SetStatePlane(final int zone, final int is_nad83,
    final String unitsname) {
    return osrJNI.SpatialReference_SetStatePlane__SWIG_1(this.swigCPtr, this,
      zone, is_nad83, unitsname);
  }

  public int SetStatePlane(final int zone, final int is_nad83,
    final String unitsname, final double units) {
    return osrJNI.SpatialReference_SetStatePlane__SWIG_0(this.swigCPtr, this,
      zone, is_nad83, unitsname, units);
  }

  public int SetStereographic(final double clat, final double clong,
    final double scale, final double fe, final double fn) {
    return osrJNI.SpatialReference_SetStereographic(this.swigCPtr, this, clat,
      clong, scale, fe, fn);
  }

  public int SetTargetLinearUnits(final String target, final String name,
    final double to_meters) {
    return osrJNI.SpatialReference_SetTargetLinearUnits(this.swigCPtr, this,
      target, name, to_meters);
  }

  public int SetTM(final double clat, final double clong, final double scale,
    final double fe, final double fn) {
    return osrJNI.SpatialReference_SetTM(this.swigCPtr, this, clat, clong,
      scale, fe, fn);
  }

  public int SetTMG(final double clat, final double clong, final double fe,
    final double fn) {
    return osrJNI.SpatialReference_SetTMG(this.swigCPtr, this, clat, clong, fe,
      fn);
  }

  public int SetTMSO(final double clat, final double clong, final double scale,
    final double fe, final double fn) {
    return osrJNI.SpatialReference_SetTMSO(this.swigCPtr, this, clat, clong,
      scale, fe, fn);
  }

  public int SetTMVariant(final String pszVariantName, final double clat,
    final double clong, final double scale, final double fe, final double fn) {
    return osrJNI.SpatialReference_SetTMVariant(this.swigCPtr, this,
      pszVariantName, clat, clong, scale, fe, fn);
  }

  public int SetTOWGS84(final double p1, final double p2, final double p3) {
    return SetTOWGS84(p1, p2, p3, 0, 0, 0, 0);
  }

  public int SetTOWGS84(final double p1, final double p2, final double p3,
    final double p4, final double p5, final double p6, final double p7) {
    return osrJNI.SpatialReference_SetTOWGS84(this.swigCPtr, this, p1, p2, p3,
      p4, p5, p6, p7);
  }

  public int SetUTM(final int zone) {
    return osrJNI.SpatialReference_SetUTM__SWIG_1(this.swigCPtr, this, zone);
  }

  public int SetUTM(final int zone, final int north) {
    return osrJNI.SpatialReference_SetUTM__SWIG_0(this.swigCPtr, this, zone,
      north);
  }

  public int SetVDG(final double clong, final double fe, final double fn) {
    return osrJNI.SpatialReference_SetVDG(this.swigCPtr, this, clong, fe, fn);
  }

  public int SetVertCS() {
    return osrJNI.SpatialReference_SetVertCS__SWIG_3(this.swigCPtr, this);
  }

  public int SetVertCS(final String VertCSName) {
    return osrJNI.SpatialReference_SetVertCS__SWIG_2(this.swigCPtr, this,
      VertCSName);
  }

  public int SetVertCS(final String VertCSName, final String VertDatumName) {
    return osrJNI.SpatialReference_SetVertCS__SWIG_1(this.swigCPtr, this,
      VertCSName, VertDatumName);
  }

  public int SetVertCS(final String VertCSName, final String VertDatumName,
    final int VertDatumType) {
    return osrJNI.SpatialReference_SetVertCS__SWIG_0(this.swigCPtr, this,
      VertCSName, VertDatumName, VertDatumType);
  }

  public int SetWellKnownGeogCS(final String name) {
    return osrJNI.SpatialReference_SetWellKnownGeogCS(this.swigCPtr, this, name);
  }

  public int StripCTParms() {
    return osrJNI.SpatialReference_StripCTParms(this.swigCPtr, this);
  }

  @Override
  public String toString() {
    return __str__();
  }

  public int Validate() {
    return osrJNI.SpatialReference_Validate(this.swigCPtr, this);
  }

}
