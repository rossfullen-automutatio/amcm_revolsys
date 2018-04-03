package com.revolsys.geometry.cs;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.measure.Unit;
import javax.measure.quantity.Length;

import com.revolsys.geometry.cs.datum.VerticalDatum;
import com.revolsys.geometry.cs.epsg.EpsgAuthority;
import com.revolsys.geometry.cs.projection.CoordinatesOperation;
import com.revolsys.geometry.cs.unit.LinearUnit;
import com.revolsys.geometry.model.BoundingBox;
import com.revolsys.geometry.model.GeometryFactory;

public class VerticalCoordinateSystem implements CoordinateSystem {

  private final LinearUnit linearUnit;

  private final Area area;

  private final Authority authority;

  private final List<Axis> axis;

  private final VerticalDatum verticalDatum;

  private final boolean deprecated;

  private final int id;

  private final String name;

  private final Map<ParameterName, ParameterValue> parameterValues;

  public VerticalCoordinateSystem(final Authority authority, final String name,
    final VerticalDatum verticalDatum, final Map<ParameterName, ParameterValue> parameterValues,
    final LinearUnit linearUnit, final List<Axis> axis) {
    this(authority, name, verticalDatum, parameterValues, linearUnit, axis, null, false);
  }

  private VerticalCoordinateSystem(final Authority authority, final String name,
    final VerticalDatum verticalDatum, final Map<ParameterName, ParameterValue> parameterValues,
    final LinearUnit linearUnit, final List<Axis> axis, final Area area, final boolean deprecated) {
    this.authority = authority;
    if (authority == null) {
      this.id = 0;
    } else {
      this.id = authority.getId();
    }
    this.name = name;
    this.verticalDatum = verticalDatum;
    this.parameterValues = parameterValues;
    this.axis = axis;
    if (axis.size() > 0) {
      this.linearUnit = (LinearUnit)axis.get(0).getUnit();
    } else {
      this.linearUnit = linearUnit;
    }
    this.area = area;
    this.deprecated = deprecated;
  }

  public VerticalCoordinateSystem(final int id, final String name,
    final VerticalDatum verticalDatum, final List<Axis> axis, final Area area,
    final boolean deprecated) {
    this(new EpsgAuthority(id), name, verticalDatum, null, null, axis, area, deprecated);
  }

  @Override
  public GeographicCoordinateSystem clone() {
    try {
      return (GeographicCoordinateSystem)super.clone();
    } catch (final Exception e) {
      return null;
    }
  }

  @Override
  public boolean equals(final Object object) {
    if (object == null) {
      return false;
    } else if (object == this) {
      return true;
    } else if (object instanceof VerticalCoordinateSystem) {
      final VerticalCoordinateSystem cs = (VerticalCoordinateSystem)object;
      if (!equals(this.verticalDatum, cs.verticalDatum)) {
        return false;
      } else if (!equals(this.linearUnit, cs.linearUnit)) {
        return false;
      } else {
        return true;
      }
    } else {
      return false;
    }
  }

  private boolean equals(final Object object1, final Object object2) {
    if (object1 == object2) {
      return true;
    } else if (object1 == null || object2 == null) {
      return false;
    } else {
      return object1.equals(object2);
    }
  }

  @Override
  public boolean equalsExact(final CoordinateSystem coordinateSystem) {
    if (coordinateSystem instanceof VerticalCoordinateSystem) {
      final VerticalCoordinateSystem verticalCoordinateSystem = (VerticalCoordinateSystem)coordinateSystem;
      return equalsExact(verticalCoordinateSystem);
    }
    return false;
  }

  public boolean equalsExact(final VerticalCoordinateSystem cs) {
    if (cs == null) {
      return false;
    } else if (cs == this) {
      return true;
    } else {
      if (!equals(this.linearUnit, cs.linearUnit)) {
        return false;
      } else if (!equals(this.area, cs.area)) {
        return false;
      } else if (!equals(this.authority, cs.authority)) {
        return false;
      } else if (!equals(this.axis, cs.axis)) {
        return false;
      } else if (!equals(this.verticalDatum, cs.verticalDatum)) {
        return false;
      } else if (this.deprecated != cs.deprecated) {
        return false;
      } else if (this.id != cs.id) {
        return false;
      } else if (!equals(this.name, cs.name)) {
        return false;
      } else {
        return true;
      }
    }
  }

  @Override
  public Area getArea() {
    return this.area;
  }

  @Override
  public BoundingBox getAreaBoundingBox() {
    final GeometryFactory geometryFactory = getGeometryFactory();
    if (this.area != null) {
      return this.area.getLatLonBounds().convert(geometryFactory);
    } else {
      return geometryFactory.newBoundingBox(-180, -90, 180, 90);
    }
  }

  @Override
  public Authority getAuthority() {
    return this.authority;
  }

  @Override
  public List<Axis> getAxis() {
    return Collections.unmodifiableList(this.axis);
  }

  @Override
  public CoordinatesOperation getCoordinatesOperation(final CoordinateSystem coordinateSystem) {
    return null;
  }

  @Override
  public int getCoordinateSystemId() {
    return this.id;
  }

  @Override
  public String getCoordinateSystemName() {
    return this.name;
  }

  @Override
  public String getCoordinateSystemType() {
    return "Vertical";
  }

  public VerticalDatum getDatum() {
    return this.verticalDatum;
  }

  @Override
  public Unit<Length> getLengthUnit() {
    return this.linearUnit.getUnit();
  }

  public LinearUnit getLinearUnit() {
    return this.linearUnit;
  }

  public Map<ParameterName, ParameterValue> getParameterValues() {
    return this.parameterValues;
  }

  @Override
  @SuppressWarnings("unchecked")
  public Unit<Length> getUnit() {
    return this.linearUnit.getUnit();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    if (this.verticalDatum != null) {
      result = prime * result + this.verticalDatum.hashCode();
    }
    return result;
  }

  @Override
  public boolean isDeprecated() {
    return this.deprecated;
  }

  @Override
  public String toString() {
    return this.name;
  }
}
