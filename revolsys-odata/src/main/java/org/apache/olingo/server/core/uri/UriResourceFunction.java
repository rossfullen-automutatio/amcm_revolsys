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
package org.apache.olingo.server.core.uri;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.olingo.commons.api.edm.EdmFunction;
import org.apache.olingo.commons.api.edm.EdmFunctionImport;
import org.apache.olingo.commons.api.edm.EdmType;
import org.apache.olingo.server.api.uri.UriParameter;
import org.apache.olingo.server.api.uri.UriResourceKind;

/**
 * Covers Function imports and BoundFunction in URI
 */
public class UriResourceFunction extends UriResourceWithKeysImpl {

  private final EdmFunctionImport functionImport;

  private final EdmFunction function;

  private final List<UriParameter> parameters;

  private final Map<String, UriParameter> parameterByName = new HashMap<>();

  public UriResourceFunction(final EdmFunctionImport edmFunctionImport, final EdmFunction function,
    final List<UriParameter> parameters) {
    super(UriResourceKind.function);
    this.functionImport = edmFunctionImport;
    this.function = function;
    if (parameters == null) {
      this.parameters = Collections.emptyList();
    } else {
      this.parameters = parameters;
      for (final UriParameter parameter : parameters) {
        final var name = parameter.getName();
        this.parameterByName.put(name, parameter);
      }
    }
  }

  public EdmFunction getFunction() {
    return this.function;
  }

  public EdmFunctionImport getFunctionImport() {
    return this.functionImport;
  }

  public List<UriParameter> getParameters() {
    return this.parameters;
  }

  public String getParameterValue(final String name) {
    final var parameter = this.parameterByName.get(name);
    if (parameter == null) {
      return null;
    }
    final var text = parameter.getText();
    return text.substring(1, text.length() - 1);
  }

  @Override
  public String getSegmentValue() {
    return this.functionImport == null ? this.function == null ? "" : this.function.getName()
      : this.functionImport.getName();
  }

  @Override
  public EdmType getType() {
    return this.function.getReturnType()
      .getType();
  }

  @Override
  public boolean isCollection() {
    return this.keyPredicates == null && this.function.getReturnType()
      .isCollection();
  }
}
