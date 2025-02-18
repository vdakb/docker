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

    File        :   Searchable.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the interface
                    Searchable.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing;

import java.util.Collection;

import java.util.stream.Collectors;

import java.io.Serializable;

////////////////////////////////////////////////////////////////////////////////
// interface Searchable
// ~~~~~~~~~ ~~~~~~~~~~
/**
 ** Interface to search an underlying inventory of items and return a collection
 ** of found items.
 ** <br>
 ** The interface provides an abstract way to decouple the auto-complete
 ** component from the searchable inventory, allowing this inventory to be just
 ** about anything - from hardcoded values to a file to even a database.
 **
 ** @param  <R>                  the type of items to be found.
 **                              <br>
 **                              Allowed object is <code>&lt;E&gt;</code>.
 ** @param  <V>                  the type of items to be searched.
 **                              <br>
 **                              Allowed object is <code>&lt;V&gt;</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public interface Searchable<R, V> extends Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  static class Literal implements Searchable<String, String> {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:3673585847523678240")
    private static final long        serialVersionUID = -5017470418918191604L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** The mutable suggestion model. */
    private final Collection<String> inventory;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs searchable <code>Literac</code> inventory.
     **
     ** @param  inventory        the inventory.
     **                          <br>
     **                          Allowed object is {@link Collection} where each
     **                          element is of type {@link String}.
     */
    private Literal(final Collection<String> inventory) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.inventory = inventory;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////
    // Method:   filter
    /**
     ** Searches an underlying inventory of items consisting of type
     ** {@link String}.
     **
     ** @param  value            a searchable value.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  a collection of items found.
     **                          <br>
     **                          Possible object is {@link Collection} where
     **                          each element is of type {@link String}.
     */
    @Override
    public Collection<String> filter(final String value) {
      return this.inventory.stream().filter(s -> (s.indexOf(value) == 0)).limit(20).collect(Collectors.toList());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods groupped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   literal
  /**
   ** Factory method to create a searchable <code>Literal</code> inventory.
   **
   ** @param  inventory          the backing collection of strings.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the created searchable <code>Literal</code>
   **                            inventory.
   **                            <br>
   **                            Possible object is {@link Searchable} where the
   **                            type of the result is {@link String} and the
   **                            type of the search value is also
   **                            {@link String}.
   */
  static Searchable<String, String> literal(final Collection<String> inventory) {
    return new Literal(inventory);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filter
  /**
	 ** Searches an underlying inventory of items consisting of type
   ** <code>E</code>.
   **
   ** @param  value              a searchable value.
   **                            <br>
   **                            Allowed object is <code>V</code>.
   **
   ** @return                    a collection of items found.
   **                            <br>
   **                            Possible object is {@link Collection} where each
   **                            element is of type <code>R</code>.
	 */
  Collection<R> filter(final V value);
}