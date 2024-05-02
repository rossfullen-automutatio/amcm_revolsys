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

import java.util.List;

import com.revolsys.io.PathName;

/**
 * The type Csdl entity set.
 */
public class CsdlEntitySet extends CsdlBindingTarget {

  // Default for EntitySets is true
  private boolean includeInServiceDocument = true;

  /**
   * Is include in service document.
   *
   * @return the boolean
   */
  public boolean isIncludeInServiceDocument() {
    return this.includeInServiceDocument;
  }

  @Override
  public CsdlEntitySet setAnnotations(final List<CsdlAnnotation> annotations) {
    super.setAnnotations(annotations);
    return this;
  }

  /**
   * Sets include in service document.
   *
   * @param includeInServiceDocument the include in service document
   * @return the include in service document
   */
  public CsdlEntitySet setIncludeInServiceDocument(final boolean includeInServiceDocument) {
    this.includeInServiceDocument = includeInServiceDocument;
    return this;
  }

  @Override
  public CsdlEntitySet setMapping(final CsdlMapping mapping) {
    this.mapping = mapping;
    return this;
  }

  @Override
  public CsdlEntitySet setName(final String name) {
    this.name = name;
    return this;
  }

  @Override
  public CsdlEntitySet setNavigationPropertyBindings(
    final List<CsdlNavigationPropertyBinding> navigationPropertyBindings) {
    this.navigationPropertyBindings = navigationPropertyBindings;
    return this;
  }

  @Override
  public CsdlEntitySet setTitle(final String title) {
    super.setTitle(title);
    return this;
  }

  @Override
  public CsdlEntitySet setType(final PathName type) {
    this.type = type;
    return this;
  }

  @Override
  public CsdlEntitySet setType(final String type) {
    this.type = PathName.fromDotSeparated(type);
    return this;
  }
}
