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

    Copyright 2011. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   SuggestionComponent.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    SuggestionComponent.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing.widget;

import java.util.List;

import java.awt.Point;

import java.util.Collection;

import javax.swing.JComponent;

import javax.swing.text.JTextComponent;

import oracle.jdeveloper.workspace.iam.swing.Searchable;

////////////////////////////////////////////////////////////////////////////////
// interface SuggestionComponent
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** This is a hookup interface for the decorator.
 ** <br>
 ** An implementation works on a specific component. This interface also allows
 ** the component on how it wants to display suggestions e.g. word by word or on
 ** the entire text etc.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public interface SuggestionComponent<T extends JComponent> {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Literal
  // ~~~~~ ~~~~~~~
  /**
   ** Implementation is for any {@link JTextComponent}.
   ** <br>
   ** It shows the suggestions on entire text.
   */
  static class Literal implements SuggestionComponent<JTextComponent> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private Searchable<String, String> provider;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs searchable <code>Literac</code> inventory.
     **
     ** @param  searchable       the function interface of the collection
     **                          provider.
     **                          <br>
     **                          Allowed object is {@link Searchable} where the
     **                          the result is of type {@link String} and the
     **                          type of the collection elements is also
     **                          {@link String}.
     */
    private Literal(final Searchable<String, String> provider) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.provider = provider;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   location
    /**
     ** Returns the screen coordinates to display the popup.
     **
     ** @param  component        the swing component the popup to display
     **                          belongs to.
     **                          <br>
     **                          Allowed object is {@link JTextComponent}.
     **
     ** @return                  the screen coordinates to display the popup.
     **                          <br>
     **                          Possible object is {@link Point}.
     */
    @Override
    public final Point location(final JTextComponent component) {
      return new Point(0, component.getPreferredSize().height - 1);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: select
    /**
     ** Set the selected value of the component.
     **
     ** @param  component        the swing component the selected value needs to
     **                          be set.
     **                          <br>
     **                          Allowed object is {@link JTextComponent}.
     ** @param  value            the value to set.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    @Override
    public void select(final JTextComponent component, final String value) {
      component.setText(value);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: populate
    /**
     ** Returns the colection of strings to display in the popup.
     ** <p>
     ** The collection is implementing {@link List} to be able applying sort
     ** algorithms on the returnd collection.
     **
     ** @param  component        the swing component for which the collected
     **                          elements are populated
     **                          <br>
     **                          Allowed object is {@link JTextComponent}.
     **
     ** @return                  the colection of strings to display in the
     **                          popup.
     **                          <br>
     **                          Possible object is {@link List} where each
     **                          element is of type {@link String}.
     */
    @Override
    public final Collection<String> populate(final JTextComponent component) {
      return this.provider.filter(component.getText().trim());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   location
  /**
   ** Returns the screen coordinates to display the popup.
   **
   ** @param  component          the swing component the popup to display
   **                            belongs to.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    the screen coordinates to display the popup.
   **                            <br>
   **                            Possible object is {@link Point}.
   */
  Point location(final T component);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   select
  /**
   ** Set the selected value of the component.
   **
   ** @param  component          the swing component the selected value needs to
   **                            be set.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   ** @param  value              the value to set.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  void select(final T component, final String value);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populate
  /**
   ** Returns the colection of strings to display in the popup.
   ** <p>
   ** The collection is implementing {@link List} to be able applying sort
   ** algorithms on the returnd collection.
   **
   ** @param  component          the swing component for which the collected
   **                            elements are populated
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    the colection of strings to display in the
   **                            popup.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link String}.
   */
  Collection<String> populate(final T component);

  //////////////////////////////////////////////////////////////////////////////
  // Methods groupped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   literal
  /**
   ** Factory method to create a suggesting <code>Literal</code> inventory.
   **
   ** @param  searchable         the function interface of the collection
   **                            provider.
   **                            <br>
   **                            Allowed object is {@link Searchable} where the
   **                            the result is of type {@link String} and the
   **                            type of the collection elements is also
   **                            {@link String}.
   **
   ** @return                    the created suggesting {@link Literal}
   **                            provider.
   **                            <br>
   **                            Possible object is {@link SuggestionComponent}
   **                            where the component is of type
   **                            {@link JTextComponent}.
   */
  static SuggestionComponent<JTextComponent> literal(final Searchable<String, String> searchable) {
    return new Literal(searchable);
  }
}
