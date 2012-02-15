/*
 * $URL:https://secure.revolsys.com/svn/open.revolsys.com/GIS/trunk/src/main/java/com/revolsys/gis/format/core/io/DataObjectReader.java $
 * $Author:paul.austin@revolsys.com $
 * $Date:2007-06-09 09:28:28 -0700 (Sat, 09 Jun 2007) $
 * $Revision:265 $

 * Copyright 2004-2005 Revolution Systems Inc.
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
package com.revolsys.gis.io;

import java.util.Iterator;

import com.revolsys.gis.data.model.DataObject;

/**
 * <p>
 * The DataObjectReader defines an interface for reading data objects from a
 * data store (flat file, database etc.). The reader interface extends from
 * {@link Iterator} to allow iteration through the features that are read.
 * </p>
 * <p>
 * The interface defines methods to get the next data object ({@link #next()}),
 * to get the schema definition ({@link #getSchema()}) and to {@link #close()}
 * the connection to the underlying data store.
 * </p>
 * <p>
 * The following code fragment shows how a reader would typically be used.
 * </p>
 * 
 * <pre>
 *         DataObjectReader reader = new ...;
 *         Writer<DataObject> writer = new ...;
 *         writer.setSchema(reader.getSchema());
 *         while (reader.hasNext()) {
 *           DataObject object = reader.next();
 *           ... // Process the object
 *           writer.write(object;
 *         }
 *         writer.close();
 *         read.close()
 * </pre>
 * 
 * @author Paul Austin
 * @see Writer<DataObject>
 */
public interface DataObjectReader extends Iterator<DataObject> {

  /**
   * Close the connection to the data store.
   */
  void close();

  DataObject next();

  void open();

  String toString();
}
