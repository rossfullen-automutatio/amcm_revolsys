/*
 * The JTS Topology Suite is a collection of Java classes that
 * implement the fundamental operations required to validate a given
 * geo-spatial data set to a known topological specification.
 *
 * Copyright (C) 2001 Vivid Solutions
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * For more information, contact:
 *
 *     Vivid Solutions
 *     Suite #1A
 *     2328 Government Street
 *     Victoria BC  V8T 5G5
 *     Canada
 *
 *     (250)385-6040
 *     www.vividsolutions.com
 */
package com.revolsys.geometry.index;

import java.util.List;

import com.revolsys.geometry.model.BoundingBox;

/**
 * The basic operations supported by classes
 * implementing spatial index algorithms.
 * <p>
 * A spatial index typically provides a primary filter for range rectangle queries.
 * A secondary filter is required to test for exact intersection.
 * The secondary filter may consist of other kinds of tests,
 * such as testing other spatial relationships.
 *
 * @version 1.7
 */
public interface SpatialIndex<V> {
  /**
   * Adds a spatial item with an extent specified by the given {@link BoundingBox} to the index
   */
  void insert(BoundingBox boundingBox, V item);

  /**
   * Queries the index for all items whose extents intersect the given search {@link BoundingBox}
   * Note that some kinds of indexes may also return objects which do not in fact
   * intersect the query envelope.
   *
   * @param searchEnv the envelope to query for
   * @return a list of the items found by the query
   */
  List<V> query(BoundingBox searchEnv);

  // /**
  // * Queries the index for all items whose extents intersect the given search
  // {@link BoundingBox},
  // * and applies an {@link ItemVisitor} to them.
  // * Note that some kinds of indexes may also return objects which do not in
  // fact
  // * intersect the query envelope.
  // *
  // * @param searchEnv the envelope to query for
  // * @param visitor a visitor object to apply to the items found
  // */
  // void query(BoundingBox searchEnv, ItemVisitor visitor);

  /**
   * Removes a single item from the tree.
   *
   * @param boundingBox the BoundingBox of the item to remove
   * @param item the item to remove
   * @return <code>true</code> if the item was found
   */
  boolean removeItem(BoundingBox b, V item);

}
