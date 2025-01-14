/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.olingo.commons.api.edm.provider;

import java.util.ArrayList;
import java.util.List;

import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.geo.SRID;

/**
 * The type Csdl type definition.
 */
public class CsdlTypeDefinition implements CsdlAbstractEdmItem, CsdlNamed, CsdlAnnotatable {

  private String name;

  private FullQualifiedName underlyingType;

  // Facets
  private Integer maxLength;

  private Integer precision;

  private Integer scale;

  private boolean unicode = true;

  private SRID srid;

  private List<CsdlAnnotation> annotations = new ArrayList<>();

  @Override
  public List<CsdlAnnotation> getAnnotations() {
    return this.annotations;
  }

  /**
   * Gets max length.
   *
   * @return the max length
   */
  public Integer getMaxLength() {
    return this.maxLength;
  }

  @Override
  public String getName() {
    return this.name;
  }

  /**
   * Gets precision.
   *
   * @return the precision
   */
  public Integer getPrecision() {
    return this.precision;
  }

  /**
   * Gets scale.
   *
   * @return the scale
   */
  public Integer getScale() {
    return this.scale;
  }

  /**
   * Gets srid.
   *
   * @return the srid
   */
  public SRID getSrid() {
    return this.srid;
  }

  /**
   * Gets underlying type.
   *
   * @return the underlying type
   */
  public String getUnderlyingType() {
    if (this.underlyingType != null) {
      return this.underlyingType.getFullQualifiedNameAsString();
    }
    return null;
  }

  /**
   * Is unicode.
   *
   * @return the boolean
   */
  public boolean isUnicode() {
    return this.unicode;
  }

  /**
   * Sets a list of annotations
   * @param annotations list of annotations
   * @return this instance
   */
  public CsdlTypeDefinition setAnnotations(final List<CsdlAnnotation> annotations) {
    this.annotations = annotations;
    return this;
  }

  /**
   * Sets max length.
   *
   * @param maxLength the max length
   * @return the max length
   */
  public CsdlTypeDefinition setMaxLength(final Integer maxLength) {
    this.maxLength = maxLength;
    return this;
  }

  /**
   * Sets name.
   *
   * @param name the name
   * @return the name
   */
  public CsdlTypeDefinition setName(final String name) {
    this.name = name;
    return this;
  }

  /**
   * Sets precision.
   *
   * @param precision the precision
   * @return the precision
   */
  public CsdlTypeDefinition setPrecision(final Integer precision) {
    this.precision = precision;
    return this;
  }

  /**
   * Sets scale.
   *
   * @param scale the scale
   * @return the scale
   */
  public CsdlTypeDefinition setScale(final Integer scale) {
    this.scale = scale;
    return this;
  }

  /**
   * Sets srid.
   *
   * @param srid the srid
   * @return the srid
   */
  public CsdlTypeDefinition setSrid(final SRID srid) {
    this.srid = srid;
    return this;
  }

  /**
   * Sets underlying type.
   *
   * @param underlyingType the underlying type
   * @return the underlying type
   */
  public CsdlTypeDefinition setUnderlyingType(final FullQualifiedName underlyingType) {
    this.underlyingType = underlyingType;
    return this;
  }

  /**
   * Sets underlying type.
   *
   * @param underlyingType the underlying type
   * @return the underlying type
   */
  public CsdlTypeDefinition setUnderlyingType(final String underlyingType) {
    this.underlyingType = new FullQualifiedName(underlyingType);
    return this;
  }

  /**
   * Sets unicode.
   *
   * @param unicode the unicode
   * @return the unicode
   */
  public CsdlTypeDefinition setUnicode(final boolean unicode) {
    this.unicode = unicode;
    return this;
  }
}
