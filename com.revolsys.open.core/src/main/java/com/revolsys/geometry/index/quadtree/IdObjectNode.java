package com.revolsys.geometry.index.quadtree;

import java.util.function.Consumer;

public class IdObjectNode<T> extends AbstractQuadTreeNode<T> {
  private static final long serialVersionUID = 1L;

  private Object[] ids = new Object[0];

  public IdObjectNode() {
  }

  public IdObjectNode(final int level, final double... bounds) {
    super(level, bounds);
  }

  @Override
  protected void addDo(final QuadTree<T> tree, final double[] bounds, final T item) {
    synchronized (this.nodes) {
      final Object id = ((IdObjectQuadTree<T>)tree).getId(item);
      final int length = this.ids.length;
      final Object[] newIds = new Object[length + 1];
      System.arraycopy(this.ids, 0, newIds, 0, length);
      newIds[length] = id;
      this.ids = newIds;
    }
  }

  @Override
  protected boolean changeItem(final QuadTree<T> tree, final double[] bounds, final T item) {
    synchronized (this.nodes) {
      final IdObjectQuadTree<T> idTree = (IdObjectQuadTree<T>)tree;
      final Object itemId = idTree.getId(item);
      int index = 0;
      final Object[] ids = this.ids;
      for (final Object existingId : ids) {
        final T existingItem = idTree.getItem(existingId);
        if (idTree.equalsItem(item, existingItem)) {
          ids[index] = itemId;
          return true;
        }
        index++;
      }
      return false;
    }
  }

  @Override
  protected void forEachItem(final QuadTree<T> tree, final Consumer<? super T> action) {
    final IdObjectQuadTree<T> idObjectTree = (IdObjectQuadTree<T>)tree;
    synchronized (this.nodes) {
      final Object[] ids = this.ids;
      final int itemCount = getItemCount();
      for (int i = 0; i < itemCount; i++) {
        final Object id = ids[i];
        final T item = idObjectTree.getItem(id);
        action.accept(item);
      }
    }
  }

  @Override
  protected void forEachItem(final QuadTree<T> tree, final double[] bounds,
    final Consumer<? super T> action) {
    synchronized (this.nodes) {
      final IdObjectQuadTree<T> idObjectTree = (IdObjectQuadTree<T>)tree;
      final Object[] ids = this.ids;
      final int itemCount = getItemCount();
      for (int i = 0; i < itemCount; i++) {
        final Object id = ids[i];
        if (idObjectTree.intersectsBounds(id, bounds[0], bounds[1], bounds[2], bounds[3])) {
          final T item = idObjectTree.getItem(id);
          action.accept(item);
        }
      }
    }
  }

  @Override
  public int getItemCount() {
    return this.ids.length;
  }

  @Override
  protected boolean hasItems() {
    return this.ids.length > 0;
  }

  @Override
  protected final AbstractQuadTreeNode<T> newNode(final int level, final double... newBounds) {
    return new IdObjectNode<>(level, newBounds);
  }

  @Override
  protected boolean removeItem(final QuadTree<T> tree, final T item) {
    synchronized (this.nodes) {
      boolean removed = false;
      final Object[] ids = this.ids;
      for (int index = 0; index < ids.length; index++) {
        final Object id = ids[index];
        final T item2 = ((IdObjectQuadTree<T>)tree).getItem(id);
        if (tree.equalsItem(item, item2)) {
          final int length = ids.length;
          final int newLength = length - 1;
          if (newLength == 0) {
            this.ids = new Object[0];
          } else {
            final Object[] newIds = new Object[newLength];
            if (index > 0) {
              System.arraycopy(ids, 0, newIds, 0, index);
            }
            if (index < newLength) {
              System.arraycopy(ids, index + 1, newIds, index, length - index - 1);
            }
            this.ids = newIds;
          }
          removed = true;
        }
      }
      return removed;
    }
  }

}
