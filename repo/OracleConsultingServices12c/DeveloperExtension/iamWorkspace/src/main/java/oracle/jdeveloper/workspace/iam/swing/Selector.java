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

    File        :   Selector.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the interface
                    Selector.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing;

import java.text.Format;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

import java.util.List;

import javax.swing.ListModel;

////////////////////////////////////////////////////////////////////////////////
// interface Selector
// ~~~~~~~~~ ~~~~~~~~
/**
 ** An interface for selecting an item in a {@link ListModel} based on a
 ** criteria.
 **
 ** @param  <T>                  the type of items to be searched.
 **                              <br>
 **                              Allowed object is <code>&lt;T&gt;</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public interface Selector<T> {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Default
  // ~~~~~ ~~~~~~~
  /**
   ** The default implementation selecting an item in a {@link ListModel} based
   ** on a criteria.
   **
   ** @param  <T>                  the type of items to be searched.
   **                              <br>
   **                              Allowed object is <code>&lt;T&gt;</code>.
   */
  static class Default<T> implements Selector<T> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final Format        format;
    private final Comparator<T> comparator;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:  Ctor
    /**
     ** Constructor for <code>Default</code> selector.
     **
     ** @param  format           the {@link Format} to produce a string for any
     **                          item.
     **                          <br>
     **                          Allowed object is {@link Format}.
     ** @param  comparator       the {@link Comparator} to apply.
     **                          <br>
     **                          Allowed object is {@link Comparator} for type
     **                          <code>T</code>.
     */
    private Default(final Format format, final Comparator<T> comparator) {
      this.format     = format;
      this.comparator = comparator;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: match
    /**
	   ** Searches in the specified <code>model</code> for an item that match the
     ** secified <code>criteria</code>.
     **
     ** @param  inventory        the backing collection.
     **                          <br>
     **                          Allowed object is {@link ListModel} where each
     **                          element is of type <code>T</code>.
     ** @param  criteria         the criteria to match.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the item found or <code>null</code> if no item
     **                          match.
     **                          <br>
     **                          Possible object is <code>T</code>.
  	 */
    public final T match(final ListModel<T> inventory, final String criteria) {
      final Collection<T> matches = filter(inventory, criteria.toLowerCase());
      return (matches.isEmpty()) ? null : matches.stream().min(this.comparator).get();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: filter
    /**
	   ** Searches an <code>inventory</code> for items matching the specified
     ** <code>criteria</code>.
     **
     ** @param  inventory        the backing collection.
     **                          <br>
     **                          Allowed object is {@link ListModel} where each
     **                          element is of type <code>T</code>.
     ** @param  criteria         the criteria to match.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  a collection of items found.
     **                          <br>
     **                          Possible object is {@link Collection} where
     **                          each element is of type <code>T</code>.
	   */
    private Collection<T> filter(final ListModel<T> inventory, final String criteria) {
      final List<T> matches = new ArrayList<>();
      for (int i = 0; i < inventory.getSize(); i++) {
        final T item = inventory.getElementAt(i);
        if (item != null && format.format(item).toLowerCase().startsWith(criteria)) {
          matches.add(item);
        }
      }
      return matches;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   element
  /**
   ** Factory method to create a <code>Item</code> selector.
   **
   ** @param  <T>                the type of items to be selected.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  format             the {@link Format} to produce a string for any
   **                            item.
   **                            <br>
   **                            Allowed object is {@link Format}.
   **
   ** @return                    the selector for an item in a {@link ListModel}
   **                            based on a criteria.
   **                            <br>
   **                            Possible object is {@link Selector} for type
   **                            <code>T</code>.
   */
  static <T> Selector<T> element(final Format format) {
    return new Default<T>(format, comparator(format));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   element
  /**
   ** Factory method to create a <code>Item</code> selector.
   **
   ** @param  <T>                the type of items to be selected.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  format             the {@link Format} to produce a string for any
   **                            item.
   **                            <br>
   **                            Allowed object is {@link Format}.
   ** @param  comparator         the {@link Comparator} to apply.
   **                            <br>
   **                            Allowed object is {@link Comparator} for type
   **                            <code>T</code>.
   **
   ** @return                    the selector for an item in a {@link ListModel}
   **                            based on a criteria.
   **                            <br>
   **                            Possible object is {@link Selector} for type
   **                            <code>T</code>.
   */
  static <T> Selector<T> element(final Format format, final Comparator<T> comparator) {
    return new Default<T>(format, comparator.thenComparing(comparator(format)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   comparator
  /**
   ** Factory method to create a {@link Comparator} that compares two elements
   ** based on their string expression produced by <code>format</code>.
   **
   ** @param  <T>                the type of items to be compare.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  format             the {@link Format} to produce a string.
   **                            <br>
   **                            Allowed object is {@link Format}.
   **
   ** @return                    a {@link Comparator} that compares two elements
   **                            based on their string expression.
   **                            <br>
   **                            Allowed object is {@link Comparator} for type
   **                            <code>T</code>.
   */
  static <T> Comparator<T> comparator(final Format format) {
    return (o1, o2) -> format.format(o1).compareToIgnoreCase(format.format(o2));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   match
  /**
	 ** Searches in the specified <code>model</code> for an item that match the
   ** secified <code>criteria</code>.
   **
   ** @param  inventory          the backing collection.
   **                            <br>
   **                            Allowed object is {@link ListModel} where each
   **                            element is of type <code>T</code>.
   ** @param  criteria           the criteria to match.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the item found or <code>null</code> if no item
   **                            match.
   **                            <br>
   **                            Possible object is <code>T</code>.
	 */
  T match(final ListModel<T> inventory, final String criteria);
}