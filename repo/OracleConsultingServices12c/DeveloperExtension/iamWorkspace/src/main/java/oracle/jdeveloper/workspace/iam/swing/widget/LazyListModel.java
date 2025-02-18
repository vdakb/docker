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

    File        :   LazyListModel.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the interface
                    LazyListModel.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing.widget;

import java.util.Collection;

import javax.swing.ListModel;

////////////////////////////////////////////////////////////////////////////////
// interface LazyListModel
// ~~~~~~~~~ ~~~~~~~~~~~~~
/**
 ** A list model that supports loading the list items lazily.
 **
 ** @param  <T>                  the expected class type of elements.
 **                              <br>
 **                              Allowed object is <code>&lt;T&gt;</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public interface LazyListModel<T> extends ListModel<T>, Iterable<T> {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////////
  // interface Selector
  // ~~~~~~~~~ ~~~~~~~~
  /**
   ** An interface for selecting an item in a {@link ListModel} based on the
   ** prefix.
   **
   ** @param  <T>                  the expected class type of elements.
   **                              <br>
   **                              Allowed object is <code>&lt;T&gt;</code>.
   **
   */
  interface Selector<T> {

    ////////////////////////////////////////////////////////////////////////////
    // Methods groupped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: match
     T match(final ListModel<T> model, final String prefix);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   indexOf
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
  int indexOf(final Object item);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contains
  /**
   ** Check if an item is in the list.
   **
   ** @param  item               the item to check for existance in the
   **                            collection.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    <code>true</code> if the item is in the list;
   **                            otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  boolean contains(final T item);

  //////////////////////////////////////////////////////////////////////////////
  // Methods groupped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addElement
  /**
   ** Append an item to the list.
   **
   ** @param  item               the new item.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   */
  void addElement(final T item);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Append items to the end of the list..
   **
   ** @param  items              the new items.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type <code>T</code>.
   */
  void add(final Collection<? extends T> items);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   merge
  /**
   ** Add items to the list, skipping items that are already in the list.
   **
   ** @param  items              the new items.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type <code>T</code>.
   */
  void merge(final Collection<? extends T> items);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace
  /**
   ** Replace the items in the list.
   **
   ** @param  items              the new items.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type <code>T</code>.
   */
  void replace(final Collection<? extends T> items);
}