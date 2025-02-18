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

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   Property.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the interface
                    Property.


    Revisions        Date       Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.model;

import javax.swing.Icon;

import oracle.bali.inspector.PropertyEditorFactory2;

import oracle.bali.inspector.editor.PropertyValueApplier;

////////////////////////////////////////////////////////////////////////////////
// interface Property
// ~~~~~~~~~ ~~~~~~~~
/**
 ** Component based on the java.beans.PropertyDescriptor for easy wrapping of
 ** beans.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public interface Property extends Identifier
                          ,       Cloneable
                          ,       PropertyValueApplier
                          ,       PropertyEditorFactory2 {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:5525659624946525102")
  static final long serialVersionUID = -2199020883766560788L;

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   icon
  /**
   ** Returns the {@link Icon} that represents the <code>Property</code> in the
   ** UI.
   **
   ** @return                    the {@link Icon} of the <code>Property</code>.
   */
  Icon icon();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   label
  /**
   ** Returns the label that represents the <code>Property</code> in the UI.
   ** <p>
   ** The label should be a noun or noun phrase naming the item that is
   ** maintained.
   ** <br>
   ** Examples: "Java Class", "Java Interface", "XML Document",
   ** "Database Connection".
   **
   ** @return                    the human readable label of the
   **                            <code>Property</code>.
   */
  String label();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hint
  /**
   ** Returns a description of the <code>Property</code> that appears in the
   ** hint text area of the UI.
   **
   ** @return                    the human readable description of the
   **                            <code>Property</code>.
   */
  String hint();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Returns the type this property represetnts.
   **
   ** @return                    the type this property represetnts.
   */
  Class<?> type();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   category
  /**
   ** Returns the name of the category of this property instance.
   **
   ** @return                    the name of the category of this property
   **                            instance.
   */
  String category();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writable
  /**
   ** Returns whether or not this property is writable or not
   **
   ** @return                    <code>true</code> if this property instance is
   **                            writable; otherwise <code>false</code>.
   */
  boolean writable();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Set (or change) the object value.
   **
   ** @param  value              the new object value to be set.
   */
  void value(final Object value);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the property value.
   **
   ** @return                    the value of the property.
   */
  Object value();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parent
  /**
   ** Returns the parent of this property instance.
   **
   ** @return                    the parent <code>Property</code> of this instance.
   */
  Property parent();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   children
  /**
   ** Returns the children of this property instance.
   **
   ** @return                    the children of this property instance.
   */
  Property[] children();

   //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionalities
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clone
  /**
   ** Creates and returns a copy of this instance.
   **
   ** @return                    a clone of this instance.
   */
  Object clone();

 //////////////////////////////////////////////////////////////////////////////
  // Method:   fetch
  /**
   ** Reads the value of this <code>Property</code> from the given object.
   ** <p>
   ** It uses reflection and looks for a method starting with "is" or "get"
   ** followed by the capitalized {@link Property} name or a getter method with
   ** the same name as the property id.
   **
   ** @param  object             the instance the specified <code>value</code>
   **                            value read from.
   */
//  void fetch(final Object object);
}