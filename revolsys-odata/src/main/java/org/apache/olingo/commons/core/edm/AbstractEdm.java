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
package org.apache.olingo.commons.core.edm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.olingo.commons.api.edm.EdmAction;
import org.apache.olingo.commons.api.edm.EdmAnnotations;
import org.apache.olingo.commons.api.edm.EdmComplexType;
import org.apache.olingo.commons.api.edm.EdmEntityContainer;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.edm.EdmEnumType;
import org.apache.olingo.commons.api.edm.EdmFunction;
import org.apache.olingo.commons.api.edm.EdmParameter;
import org.apache.olingo.commons.api.edm.EdmSchema;
import org.apache.olingo.commons.api.edm.EdmTerm;
import org.apache.olingo.commons.api.edm.EdmTypeDefinition;
import org.apache.olingo.commons.api.edm.provider.CsdlAnnotation;

import com.revolsys.io.PathName;

public abstract class AbstractEdm {

  protected Map<PathName, EdmSchema> schemas;

  protected List<EdmSchema> schemaList;

  private boolean isEntityDerivedFromES;

  private boolean isComplexDerivedFromES;

  private boolean isPreviousES;

  private final Map<PathName, EdmEntityContainer> entityContainers = Collections
    .synchronizedMap(new HashMap<>());

  private final Map<PathName, EdmEnumType> enumTypes = Collections.synchronizedMap(new HashMap<>());

  private final Map<PathName, EdmTypeDefinition> typeDefinitions = Collections
    .synchronizedMap(new HashMap<>());

  private final Map<PathName, EdmEntityType> entityTypes = Collections
    .synchronizedMap(new HashMap<>());

  private final Map<PathName, EdmComplexType> complexTypes = Collections
    .synchronizedMap(new HashMap<>());

  private final Map<PathName, EdmAction> unboundActions = Collections
    .synchronizedMap(new HashMap<>());

  private final Map<PathName, List<EdmFunction>> unboundFunctionsByName = Collections
    .synchronizedMap(new HashMap<>());

  private final Map<FunctionMapKey, EdmFunction> unboundFunctionsByKey = Collections
    .synchronizedMap(new HashMap<>());

  private final Map<ActionMapKey, EdmAction> boundActions = Collections
    .synchronizedMap(new HashMap<>());

  private final Map<FunctionMapKey, EdmFunction> boundFunctions = Collections
    .synchronizedMap(new HashMap<>());

  private final Map<PathName, EdmTerm> terms = Collections.synchronizedMap(new HashMap<>());

  private final Map<TargetQualifierMapKey, EdmAnnotations> annotationGroups = Collections
    .synchronizedMap(new HashMap<>());

  private Map<PathName, PathName> aliasToNamespaceInfo = null;

  private final Map<PathName, EdmEntityType> entityTypesWithAnnotations = Collections
    .synchronizedMap(new HashMap<>());

  private final Map<PathName, EdmEntityType> entityTypesDerivedFromES = Collections
    .synchronizedMap(new HashMap<>());

  private final Map<PathName, EdmComplexType> complexTypesWithAnnotations = Collections
    .synchronizedMap(new HashMap<>());

  private final Map<PathName, EdmComplexType> complexTypesDerivedFromES = Collections
    .synchronizedMap(new HashMap<>());

  private final Map<String, List<CsdlAnnotation>> annotationMap = new HashMap<>();

  public void cacheAction(final PathName actionName, final EdmAction action) {
    if (action.isBound()) {
      final ActionMapKey key = new ActionMapKey(actionName,
        action.getBindingParameterTypePathName(), action.isBindingParameterTypeCollection());
      this.boundActions.put(key, action);
    } else {
      this.unboundActions.put(actionName, action);
    }
  }

  public void cacheAliasNamespaceInfo(final PathName alias, final PathName namespace) {
    this.aliasToNamespaceInfo.put(alias, namespace);
  }

  public void cacheAnnotationGroup(final PathName targetName,
    final EdmAnnotations annotationsGroup) {
    final TargetQualifierMapKey key = new TargetQualifierMapKey(targetName,
      annotationsGroup.getQualifier());
    this.annotationGroups.put(key, annotationsGroup);
  }

  public void cacheComplexType(final PathName compelxTypeName, final EdmComplexType complexType) {
    this.complexTypes.put(compelxTypeName, complexType);
  }

  public void cacheEntityContainer(final PathName containerFQN,
    final EdmEntityContainer container) {
    this.entityContainers.put(containerFQN, container);
  }

  public void cacheEntityType(final PathName entityTypeName, final EdmEntityType entityType) {
    this.entityTypes.put(entityTypeName, entityType);
  }

  public void cacheEnumType(final PathName enumName, final EdmEnumType enumType) {
    this.enumTypes.put(enumName, enumType);
  }

  public void cacheFunction(final PathName functionName, final EdmFunction function) {
    final FunctionMapKey key = new FunctionMapKey(functionName,
      function.getBindingParameterTypePathName(), function.isBindingParameterTypeCollection(),
      function.getParameterNames());

    if (function.isBound()) {
      this.boundFunctions.put(key, function);
    } else {
      if (!this.unboundFunctionsByName.containsKey(functionName)) {
        this.unboundFunctionsByName.put(functionName, new ArrayList<>());
      }
      this.unboundFunctionsByName.get(functionName)
        .add(function);

      this.unboundFunctionsByKey.put(key, function);
    }
  }

  public void cacheTerm(final PathName termName, final EdmTerm term) {
    this.terms.put(termName, term);
  }

  public void cacheTypeDefinition(final PathName typeDefName, final EdmTypeDefinition typeDef) {
    this.typeDefinitions.put(typeDefName, typeDef);
  }

  protected abstract Map<PathName, PathName> createAliasToNamespaceInfo();

  protected abstract EdmAnnotations createAnnotationGroup(PathName targetName, String qualifier);

  protected abstract EdmAction createBoundAction(PathName actionName,
    PathName bindingParameterTypeName, Boolean isBindingParameterCollection);

  protected abstract EdmFunction createBoundFunction(PathName functionName,
    PathName bindingParameterTypeName, Boolean isBindingParameterCollection,
    List<String> parameterNames);

  protected abstract EdmComplexType createComplexType(PathName complexTypeName);

  protected abstract EdmEntityContainer createEntityContainer(PathName containerName);

  protected abstract EdmEntityType createEntityType(PathName entityTypeName);

  protected abstract EdmEnumType createEnumType(PathName enumName);

  protected abstract Map<PathName, EdmSchema> createSchemas();

  protected abstract EdmTerm createTerm(PathName termName);

  protected abstract EdmTypeDefinition createTypeDefinition(PathName typeDefinitionName);

  protected abstract EdmAction createUnboundAction(PathName actionName);

  protected abstract EdmFunction createUnboundFunction(PathName functionName,
    List<String> parameterNames);

  protected abstract List<EdmFunction> createUnboundFunctions(PathName functionName);

  public EdmAnnotations getAnnotationGroup(final PathName targetName, final String qualifier) {
    final var fqn = resolvePossibleAlias(targetName);
    final TargetQualifierMapKey key = new TargetQualifierMapKey(fqn, qualifier);
    EdmAnnotations _annotations = this.annotationGroups.get(key);
    if (_annotations == null) {
      _annotations = createAnnotationGroup(fqn, qualifier);
      if (_annotations != null) {
        this.annotationGroups.put(key, _annotations);
      }
    }
    return _annotations;
  }

  protected Map<String, List<CsdlAnnotation>> getAnnotationsMap() {
    return this.annotationMap;
  }

  public EdmAction getBoundAction(final PathName actionName,
    final PathName bindingParameterTypeName, final Boolean isBindingParameterCollection) {

    final var actionFqn = resolvePossibleAlias(actionName);
    final var bindingParameterTypeFqn = resolvePossibleAlias(bindingParameterTypeName);
    final ActionMapKey key = new ActionMapKey(actionFqn, bindingParameterTypeFqn,
      isBindingParameterCollection);
    EdmAction action = this.boundActions.get(key);
    if (action == null) {
      action = createBoundAction(actionFqn, bindingParameterTypeFqn, isBindingParameterCollection);
      if (action != null) {
        this.boundActions.put(key, action);
      }
    }

    return action;
  }

  public EdmAction getBoundActionWithBindingType(final PathName bindingParameterTypeName,
    final Boolean isBindingParameterCollection) {
    for (final EdmSchema schema : getSchemas()) {
      for (final EdmAction action : schema.getActions()) {
        if (action.isBound()) {
          final EdmParameter bindingParameter = action.getParameter(action.getParameterNames()
            .get(0));
          if (bindingParameter.getType()
            .getPathName()
            .equals(bindingParameterTypeName)
            && bindingParameter.isCollection() == isBindingParameterCollection) {
            return action;
          }
        }
      }
    }
    return null;
  }

  public EdmFunction getBoundFunction(final PathName functionName,
    final PathName bindingParameterTypeName, final Boolean isBindingParameterCollection,
    final List<String> parameterNames) {

    final var functionFqn = resolvePossibleAlias(functionName);
    final var bindingParameterTypeFqn = resolvePossibleAlias(bindingParameterTypeName);
    final FunctionMapKey key = new FunctionMapKey(functionFqn, bindingParameterTypeFqn,
      isBindingParameterCollection, parameterNames);
    EdmFunction function = this.boundFunctions.get(key);
    if (function == null) {
      function = createBoundFunction(functionFqn, bindingParameterTypeFqn,
        isBindingParameterCollection, parameterNames);
      if (function != null) {
        this.boundFunctions.put(key, function);
      }
    }

    return function;
  }

  public List<EdmFunction> getBoundFunctionsWithBindingType(final PathName bindingParameterTypeName,
    final Boolean isBindingParameterCollection) {
    final List<EdmFunction> functions = new ArrayList<>();
    for (final EdmSchema schema : getSchemas()) {
      for (final EdmFunction function : schema.getFunctions()) {
        if (function.isBound()) {
          final EdmParameter bindingParameter = function.getParameter(function.getParameterNames()
            .get(0));
          if (bindingParameter.getType()
            .getPathName()
            .equals(bindingParameterTypeName)
            && bindingParameter.isCollection() == isBindingParameterCollection) {
            functions.add(function);
          }
        }
      }
    }
    return functions;
  }

  public EdmComplexType getComplexType(final PathName namespaceOrAliasFQN) {
    final var fqn = resolvePossibleAlias(namespaceOrAliasFQN);
    EdmComplexType complexType = this.complexTypes.get(fqn);
    if (complexType == null) {
      complexType = createComplexType(fqn);
      if (complexType != null) {
        this.complexTypes.put(fqn, complexType);
      }
    }
    return complexType;
  }

  public EdmComplexType getComplexTypeWithAnnotations(final PathName namespaceOrAliasFQN) {
    final var fqn = resolvePossibleAlias(namespaceOrAliasFQN);
    EdmComplexType complexType = this.complexTypesWithAnnotations.get(fqn);
    if (complexType == null) {
      complexType = createComplexType(fqn);
      if (complexType != null) {
        this.complexTypesWithAnnotations.put(fqn, complexType);
      }
    }
    setIsPreviousES(false);
    return complexType;
  }

  protected EdmComplexType getComplexTypeWithAnnotations(final PathName namespaceOrAliasFQN,
    final boolean isComplexDerivedFromES) {
    this.isComplexDerivedFromES = isComplexDerivedFromES;
    final var fqn = resolvePossibleAlias(namespaceOrAliasFQN);
    if (!isPreviousES() && getEntityContainer() != null) {
      getEntityContainer().getEntitySetsWithAnnotations();
    }
    EdmComplexType complexType = this.complexTypesDerivedFromES.get(fqn);
    if (complexType == null) {
      complexType = createComplexType(fqn);
      if (complexType != null) {
        this.complexTypesDerivedFromES.put(fqn, complexType);
      }
    }
    this.isComplexDerivedFromES = false;
    return complexType;
  }

  public EdmEntityContainer getEntityContainer() {
    return getEntityContainer(null);
  }

  public EdmEntityContainer getEntityContainer(final PathName pathName) {
    final var aliasName = resolvePossibleAlias(pathName);
    EdmEntityContainer container = this.entityContainers.get(aliasName);
    if (container == null) {
      container = createEntityContainer(aliasName);
      if (container != null) {
        this.entityContainers.put(aliasName, container);
        if (aliasName == null) {
          this.entityContainers.put(container.getNamespace()
            .newChild(container.getName()), container);
        }
      }
    }
    return container;
  }

  public EdmEntityType getEntityType(final PathName namespaceOrAliasFQN) {
    final var fqn = resolvePossibleAlias(namespaceOrAliasFQN);
    EdmEntityType entityType = this.entityTypes.get(fqn);
    if (entityType == null) {
      entityType = createEntityType(fqn);
      if (entityType != null) {
        this.entityTypes.put(fqn, entityType);
      }
    }
    return entityType;
  }

  public EdmEntityType getEntityTypeWithAnnotations(final PathName namespaceOrAliasFQN) {
    final var fqn = resolvePossibleAlias(namespaceOrAliasFQN);
    EdmEntityType entityType = this.entityTypesWithAnnotations.get(fqn);
    if (entityType == null) {
      entityType = createEntityType(fqn);
      if (entityType != null) {
        this.entityTypesWithAnnotations.put(fqn, entityType);
      }
    }
    setIsPreviousES(false);
    return entityType;
  }

  protected EdmEntityType getEntityTypeWithAnnotations(final PathName namespaceOrAliasFQN,
    final boolean isEntityDerivedFromES) {
    this.isEntityDerivedFromES = isEntityDerivedFromES;
    final var fqn = resolvePossibleAlias(namespaceOrAliasFQN);
    if (!isPreviousES() && getEntityContainer() != null) {
      getEntityContainer().getEntitySetsWithAnnotations();
    }
    EdmEntityType entityType = this.entityTypesDerivedFromES.get(fqn);
    if (entityType == null) {
      entityType = createEntityType(fqn);
      if (entityType != null) {
        this.entityTypesDerivedFromES.put(fqn, entityType);
      }
    }
    this.isEntityDerivedFromES = false;
    return entityType;
  }

  public EdmEnumType getEnumType(final PathName namespaceOrAliasFQN) {
    final var fqn = resolvePossibleAlias(namespaceOrAliasFQN);
    EdmEnumType enumType = this.enumTypes.get(fqn);
    if (enumType == null) {
      enumType = createEnumType(fqn);
      if (enumType != null) {
        this.enumTypes.put(fqn, enumType);
      }
    }
    return enumType;
  }

  public EdmSchema getSchema(final PathName namespace) {
    if (this.schemas == null) {
      initSchemas();
    }

    EdmSchema schema = this.schemas.get(namespace);
    if (schema == null) {
      schema = this.schemas.get(this.aliasToNamespaceInfo.get(namespace));
    }
    return schema;
  }

  public List<EdmSchema> getSchemas() {
    if (this.schemaList == null) {
      initSchemas();
    }
    return this.schemaList;
  }

  public EdmTerm getTerm(final PathName termName) {
    final var fqn = resolvePossibleAlias(termName);
    EdmTerm term = this.terms.get(fqn);
    if (term == null) {
      term = createTerm(fqn);
      if (term != null) {
        this.terms.put(fqn, term);
      }
    }
    return term;
  }

  public EdmTypeDefinition getTypeDefinition(final PathName namespaceOrAliasFQN) {
    final var fqn = resolvePossibleAlias(namespaceOrAliasFQN);
    EdmTypeDefinition typeDefinition = this.typeDefinitions.get(fqn);
    if (typeDefinition == null) {
      typeDefinition = createTypeDefinition(fqn);
      if (typeDefinition != null) {
        this.typeDefinitions.put(fqn, typeDefinition);
      }
    }
    return typeDefinition;
  }

  public EdmAction getUnboundAction(final PathName actionName) {
    final var fqn = resolvePossibleAlias(actionName);
    EdmAction action = this.unboundActions.get(fqn);
    if (action == null) {
      action = createUnboundAction(fqn);
      if (action != null) {
        this.unboundActions.put(actionName, action);
      }
    }

    return action;
  }

  public EdmFunction getUnboundFunction(final PathName functionName,
    final List<String> parameterNames) {
    final var functionFqn = resolvePossibleAlias(functionName);

    final FunctionMapKey key = new FunctionMapKey(functionFqn, null, null, parameterNames);
    EdmFunction function = this.unboundFunctionsByKey.get(key);
    if (function == null) {
      function = createUnboundFunction(functionFqn, parameterNames);
      if (function != null) {
        this.unboundFunctionsByKey.put(key, function);
      }
    }

    return function;
  }

  public List<EdmFunction> getUnboundFunctions(final PathName functionName) {
    final var functionFqn = resolvePossibleAlias(functionName);

    List<EdmFunction> functions = this.unboundFunctionsByName.get(functionFqn);
    if (functions == null) {
      functions = createUnboundFunctions(functionFqn);
      if (functions != null) {
        this.unboundFunctionsByName.put(functionFqn, functions);

        for (final EdmFunction unbound : functions) {
          final FunctionMapKey key = new FunctionMapKey(unbound.getNamespace()
            .newChild(unbound.getName()), unbound.getBindingParameterTypePathName(),
            unbound.isBindingParameterTypeCollection(), unbound.getParameterNames());
          this.unboundFunctionsByKey.put(key, unbound);
        }
      }
    }

    return functions;
  }

  private void initSchemas() {
    loadAliasToNamespaceInfo();
    final Map<PathName, EdmSchema> localSchemas = createSchemas();
    this.schemas = Collections.synchronizedMap(localSchemas);

    this.schemaList = Collections.unmodifiableList(new ArrayList<>(this.schemas.values()));
  }

  protected boolean isComplexDerivedFromES() {
    return this.isComplexDerivedFromES;
  }

  protected boolean isEntityDerivedFromES() {
    return this.isEntityDerivedFromES;
  }

  protected boolean isPreviousES() {
    return this.isPreviousES;
  }

  private void loadAliasToNamespaceInfo() {
    final Map<PathName, PathName> localAliasToNamespaceInfo = createAliasToNamespaceInfo();
    this.aliasToNamespaceInfo = Collections.synchronizedMap(localAliasToNamespaceInfo);
  }

  private PathName resolvePossibleAlias(final PathName namespaceOrAliasFQN) {
    if (this.aliasToNamespaceInfo == null) {
      loadAliasToNamespaceInfo();
    }
    PathName finalFQN = null;
    if (namespaceOrAliasFQN != null) {
      final var namespace = this.aliasToNamespaceInfo.get(namespaceOrAliasFQN.getParent());
      // If not contained in info it must be a namespace
      if (namespace == null) {
        finalFQN = namespaceOrAliasFQN;
      } else {
        finalFQN = namespace.newChild(namespaceOrAliasFQN.getName());
      }
    }
    return finalFQN;
  }

  protected void setIsPreviousES(final boolean isPreviousES) {
    this.isPreviousES = isPreviousES;
  }
}
