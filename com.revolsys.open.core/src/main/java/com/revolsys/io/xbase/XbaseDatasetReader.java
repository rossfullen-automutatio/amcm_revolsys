/*
 * $URL:https://secure.revolsys.com/svn/open.revolsys.com/rs-gis-dbase/trunk/src/main/java/com/revolsys/gis/format/xbase/io/XbaseDatasetReader.java $
 * $Author:paul.austin@revolsys.com $
 * $Date:2007-07-03 19:26:54 -0700 (Tue, 03 Jul 2007) $
 * $Revision:410 $

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
package com.revolsys.io.xbase;

import java.io.File;

import com.revolsys.gis.data.io.AbstractDirectoryReader;
import com.revolsys.gis.data.io.DataObjectDirectoryReader;
import com.revolsys.gis.data.io.DataObjectReader;

/**
 * <p>
 * The XbaseDatasetReader is a {@link DataObjectReader} that can read .dbf data
 * files contained in a single directory. The reader will iterate through the
 * .dbf files in alphabetical order returning all features.
 * </p>
 * <p>
 * See the {@link AbstractDirectoryReader} class for examples on how to use
 * directory readers.
 * </p>
 * 
 * @author Paul Austin
 * @see AbstractDirectoryReader
 */
public class XbaseDatasetReader extends DataObjectDirectoryReader {
  /**
   * Construct a new XbaseDatasetReader to read .dbf files from the specified
   * directory.
   * 
   * @param directory The directory containing the .dbf files.
   */
  public XbaseDatasetReader(final File directory) {
    setFileExtensions("dbf");
    setDirectory(directory);
  }
}
