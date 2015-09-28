/*
 * $URL$
 * $Author$
 * $Date$
 * $Revision$

 * Copyright 2004-2007 Revolution Systems Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.revolsys.record;

import com.revolsys.record.schema.RecordDefinition;

/**
 * A Record factory
 *
 * @author paustin
 */
public interface RecordFactory {
  /**
   * Construct a new Record using the record definition
   *
   * @param recordDefinition The record definition used to construct the instance.
   * @return The Record instance.
   */
  Record newRecord(RecordDefinition recordDefinition);
}
