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
package org.apache.olingo.commons.api.edm.provider.annotation;

import java.util.ArrayList;
import java.util.List;

import org.apache.olingo.commons.api.edm.provider.CsdlAnnotatable;
import org.apache.olingo.commons.api.edm.provider.CsdlAnnotation;

/**
 * The edm:UrlRef expression enables a value to be obtained by sending a GET request to the value of
 * the UrlRef expression.
 */
public class CsdlUrlRef extends CsdlDynamicExpression implements CsdlAnnotatable {

  private CsdlExpression value;

  private List<CsdlAnnotation> annotations = new ArrayList<>();

  private boolean checkAnnotations(final List<CsdlAnnotation> csdlUrlRefAnnot) {
    if (csdlUrlRefAnnot == null) {
      return false;
    }
    if (this.getAnnotations().size() == csdlUrlRefAnnot.size()) {
      for (int i = 0; i < this.getAnnotations().size(); i++) {
        if (!this.getAnnotations().get(i).equals(csdlUrlRefAnnot.get(i))) {
          return false;
        }
      }
    } else {
      return false;
    }
    return true;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null || !(obj instanceof CsdlUrlRef)) {
      return false;
    }
    final CsdlUrlRef csdlUrlRef = (CsdlUrlRef)obj;
    return (this.getValue() == null ? csdlUrlRef.getValue() == null
      : this.getValue().equals(csdlUrlRef.getValue()))
      && (this.getAnnotations() == null ? csdlUrlRef.getAnnotations() == null
        : checkAnnotations(csdlUrlRef.getAnnotations()));
  }

  @Override
  public List<CsdlAnnotation> getAnnotations() {
    return this.annotations;
  }

  /**
   * Returns a expression of type Edm.String
   * @return expression of type Edm.String
   */
  public CsdlExpression getValue() {
    return this.value;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (this.value == null ? 0 : this.value.hashCode());
    result = prime * result + (this.annotations == null ? 0 : this.annotations.hashCode());
    return result;
  }

  public CsdlUrlRef setAnnotations(final List<CsdlAnnotation> annotations) {
    this.annotations = annotations;
    return this;
  }

  public CsdlUrlRef setValue(final CsdlExpression value) {
    this.value = value;
    return this;
  }
}
