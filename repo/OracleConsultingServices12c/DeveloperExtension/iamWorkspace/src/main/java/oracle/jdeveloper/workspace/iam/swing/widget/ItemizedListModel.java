/*
    Oracle Deutschland GmbH

    This software is the confidential and proprietary information of
    Oracle Corporation. ("Confidential Information").  You shall not
    disclose such Confidential Information and shall use it only in
    accordance with the terms of the license agreement you entered
    into with Oracle.

    ORACLE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
    SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
    IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
    PURPOSE, OR NON-INFRINGEMENT. ORACLE SHALL NOT BE LIABLE FOR ANY DAMAGES
    SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
    THIS SOFTWARE OR ITS DERIVATIVES.

    Copyright © 2011. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extensions
    Subsystem   :   Identity and Access Management Facilities

    File        :   ItemizedListModel.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    ItemizedListModel.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing.widget;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import java.util.stream.Collectors;

import javax.swing.AbstractListModel;

////////////////////////////////////////////////////////////////////////////////
// class ItemizedListModel
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** A list model that contains item instances.
 ** <p>
 ** This class is intended for use with a {@code JList} or {@code JComboBox}.
 **
 ** @param  <T>                  the expected class type of items.
 **                              <br>
 **                              Allowed object is <code>&lt;T&gt;</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class ItemizedListModel<T> extends    AbstractListModel<T>
                                  implements LazyListModel<T> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:8304249986314980468")
  private static final long serialVersionUID = -7503865674206966095L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @SuppressWarnings("oracle.jdeveloper.java.field-not-serializable")
  private final List<T>     item = new ArrayList<>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method: Ctor
  /**
   ** Constructs a <code>ItemizedListModel</code> suitable for rendering Swing
   ** list components.
   **
   ** @param  item               the initial payload of the list.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type <code>T</code>.
   */
  protected ItemizedListModel(final Collection<? extends T> item) {
    // ensure inheritance
    super();

    // intialize instance attributes
    if (item != null && item.size() > 0)
      this.item.addAll(item);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   iterator (Iterable)
  /**
   ** This is returns an {@link Iterator} that is used to loop through the
   ** ouptut nodes from the top down. This allows the node writer to determine
   ** what <code>Mode</code> should be used by an output node.
   ** <p>
   ** This reverses the iteration of the list.
   **
   ** @return                    an iterator to iterate from the top down.
   **                            <br>
   **                            Possible object is {@link Iterator}.
   */
  @Override
  public Iterator<T> iterator() {
    return Collections.unmodifiableList(this.item).iterator();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSize (ListModel)
  /**
   ** Returns the length of the list.
   **
   ** @return                    the length of the list.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
   @Override
   public final int getSize() {
    return this.item.size();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getElementAt (ListModel)
  /**
   ** Returns the value at the specified index.
   **
   ** @param  index              the requested index.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the value at <code>index</code>.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
 @Override
  public final T getElementAt(final int index) {
    return this.item.get(index);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   indexOf (LazyListModel)
  /**
   ** Returns the index of an item in the list.
   **
   ** @param  item               the item of the list the index is requested
   **                            for.
   **                            <br>
   **                            Possible object is {@link Object}.
   **
   ** @return                    the index of the item or <code>-1</code> if it
   **                            is not in the list.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  @Override
  public final int indexOf(final Object item) {
    return this.item.indexOf(item);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contains (LazyListModel)
  /**
   ** Check if an item is in the list.
   **
   ** @return                    <code>true</code> if the item is in the list;
   **                            otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  public final boolean contains(final T item) {
    return this.item.contains(item);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addElement (LazyListModel)
  /**
   ** Append an item to the list.
   **
   ** @param  item               the new item.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   */
  @Override
  public final void addElement(final T item) {
    int index = this.item.size();
    this.item.add(item);
    fireIntervalAdded(this, index, index);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add (LazyListModel)
  /**
   ** Append items to the end of the list..
   **
   ** @param  items              the new items.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type <code>T</code>.
   */
  @Override
  public final void add(final Collection<? extends T> items) {
    if (items.size() > 0) {
      int size = this.item.size();
      this.item.addAll(items);
      fireIntervalAdded(this, size, size + items.size() - 1);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   merge (LazyListModel)
  /**
   ** Add items to the list, skipping items that are already in the list.
   **
   ** @param  item               the new items.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type <code>T</code>.
   */
  @Override
  public final void merge(final Collection<? extends T> item) {
    int index = getSize() > 0 && getElementAt(0) == null ? 1 : 0;
    for (T cursor : this.item) {
      if (indexOf(cursor) < 0) {
        insertElementAt(cursor, index);
      }
      index++;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace (LazyListModel)
  /**
   ** Replace the items in the list.
   **
   ** @param  items              the new items.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type <code>T</code>.
   */
  @Override
  public final void replace(final Collection<? extends T> items) {
    this.item.clear();
    this.item.addAll(items);
    fireContentsChanged(this, 0, Integer.MAX_VALUE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   empty
  /**
   ** Factory method to create an empty <code>ItemizedListModel</code> suitable
   ** for rendering Swing list components.
   ** <p>
   ** The payload of the list in not initialized and needs to be provided by
   ** {@link #replace(Collection)} later on.
   **
   ** @param  <T>                the expected class type of items.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   **
   ** @return                    the <code>ItemizedListModel</code> with the
   **                            initial payload.
   **                            <br>
   **                            Possible object is
   **                            <code>ItemizedListModel</code>.
   */
  public static <T> ItemizedListModel<T> empty() {
    return new ItemizedListModel<T>(null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>ItemizedListModel</code> suitable for
   ** rendering Swing list components with the specified initial payload.
   **
   ** @param  <T>                the expected class type of items.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  item               the initial payload of the list.
   **                            <br>
   **                            Allowed object is array of type <code>T</code>.
   **
   ** @return                    the <code>ItemizedListModel</code> without an
   **                            initial payload.
   **                            <br>
   **                            Possible object is
   **                            <code>ItemizedListModel</code>.
   */
  @SuppressWarnings("unchecked")
  public static <T> ItemizedListModel<T> build(final T... item) {
    return build(Arrays.stream(item).collect(Collectors.toList()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>ItemizedListModel</code> suitable for
   ** rendering Swing list components with the specified initial payload.
   **
   ** @param  <T>                the expected class type of items.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  item               the initial payload of the list.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type <code>T</code>.
   **
   ** @return                    the <code>ItemizedListModel</code> without an
   **                            initial payload.
   **                            <br>
   **                            Possible object is
   **                            <code>ItemizedListModel</code>.
   */
  public static <T> ItemizedListModel<T> build(final Collection<? extends T> item) {
    return new ItemizedListModel<T>(item);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clear
  /**
   ** Clear the list of items.
   */
  public void clear() {
    int size = this.item.size();
    if (size > 0) {
      this.item.clear();
      fireIntervalRemoved(this, 0, size - 1);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   insertElementAt
  /**
   ** Insert an item in the list at a specific index.
   **
   ** @param  item               the item to insert.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   ** @param  index              the index at which to add the item.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  public final void insertElementAt(final T item, int index) {
    this.item.add(index, item);
    fireIntervalAdded(this, index, index);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeElement
  /**
   ** Remove an item from the list.
   **
   ** @param  item               the item to remove.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   */
  public final void removeElement(final Object item) {
    int index = indexOf(item);
    if (index >= 0) {
      removeElementAt(index);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeElementAt
  /**
   ** Remove an item from the list.
   **
   ** @param  index              the index of the item to remove.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  public final void removeElementAt(final int index) {
    this.item.remove(index);
    fireIntervalRemoved(this, index, index);
  }
}