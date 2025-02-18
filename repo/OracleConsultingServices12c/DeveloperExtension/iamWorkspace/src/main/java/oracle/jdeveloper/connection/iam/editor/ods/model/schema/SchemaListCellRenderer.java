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

    Copyright © 2019. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   SchemaListCellRenderer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    SchemaListCellRenderer.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.editor.ods.model.schema;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JList;

import oracle.jdeveloper.connection.iam.editor.ods.resource.Bundle;

import oracle.jdeveloper.connection.iam.editor.ods.model.DirectoryListCellRenderer;

////////////////////////////////////////////////////////////////////////////////
// abstract class SchemaListCellRenderer
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** A minimal extension of {@link DirectoryListCellRenderer} allowing us to
 ** decorate some rows with an {@link Icon} and set to bold font and
 ** highlighting colors.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public abstract class SchemaListCellRenderer<R> extends DirectoryListCellRenderer<R> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final Icon[] SYMBOL = {
    Bundle.icon(Bundle.ICON_OBJECT_CLASS)
  , Bundle.icon(Bundle.ICON_ATTRIBUTE_TYPE)
  };

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-2015183808776629587")
  private static final long serialVersionUID = 7863360568860309354L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////
  
  private final transient Icon symbol;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class ObjectClass
  // ~~~~~ ~~~~~~~~~~~
  /**
   ** A minimal extension of {@link SchemaListCellRenderer} allowing us to
   ** decorate some rows with an {@link Icon} and set to bold font and
   ** highlighting colors.
   */
  private static class ObjectClass extends SchemaListCellRenderer<String> {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:1452594155193566785")
    private static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>ObjectClass</code> table cell renderer responsible to
     ** visualize the object class declarations of a Directory Service schema.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    private ObjectClass() {
      // ensure inheritance
      super(SYMBOL[0]);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class AttributeType
  // ~~~~~ ~~~~~~~~~~~~~
  /**
   ** A minimal extension of {@link SchemaListCellRenderer} allowing us to
   ** decorate some rows with an {@link Icon} and set to bold font and
   ** highlighting colors.
   */
  private static class AttributeType extends SchemaListCellRenderer<String> {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:-7753081717300524051")
    private static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>AttributeType</code> table cell renderer responsible
     ** to visualize the attribute type declarations of a Directory Service
     ** schema.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public AttributeType() {
      // ensure inheritance
      super(SYMBOL[1]);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>SchemaListCellRenderer</code> that renders the
   ** specified {@link Icon}.
   **
   ** @param  symbol             the {@link Icon} to display in a table row.
   **                            <br>
   **                            Allowed object is {@link Icon}.
   */
  private SchemaListCellRenderer(final Icon symbol) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.symbol = symbol;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectClass
  /**
   ** Factory method to create an <code>SchemaListCellRenderer</code> that
   ** allows use as a JavaBean which renders names for object class
   ** declarations of a Directory Service schema in a {@link JList}.
   **
   ** @return                    the validated
   **                            <code>SchemaListCellRenderer</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SchemaListCellRenderer</code>.
   */
  public static SchemaListCellRenderer<String> objectClass() {
    return new ObjectClass();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributeType
  /**
   ** Factory method to create an <code>SchemaListCellRenderer</code> that
   ** allows use as a JavaBean which renders names for attribute type
   ** declarations of a Directory Service schema in a {@link JList}.
   **
   ** @return                    the validated
   **                            <code>SchemaListCellRenderer</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SchemaListCellRenderer</code>.
   */
  public static SchemaListCellRenderer<String> attributeType() {
    return new AttributeType();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getListCellRendererComponent (overridden)
  /**
   ** Return a component that has been configured to display the specified
   ** value. That component's <code>paint</code> method is then called to
   ** "render" the cell. If it is necessary to compute the dimensions of a list
   ** because the list cells do not have a fixed size, this method is called to
   ** generate a component on which <code>getPreferredSize</code> can be
   ** invoked.
   **
   ** @param  list               the <code>JList</code> that is asking the
   **                            renderer to draw; can be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link JList}.
   ** @param  value              the value of the cell to be rendered. It is up
   **                            to the specific renderer to interpret and draw
   **                            the value. For example, if <code>value</code>
   **                            is the string "true", it could be rendered as a
   **                            string or it could be rendered as a check box
   **                            that is checked.  <code>null</code> is a valid
   **                            value.
   **                            <br>
   **                            Allowed object is {@link Object}.
   ** @param  index              the index of the cell being drawn.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  selected           <code>true</code> if the cell is to be rendered
   **                            with the selection highlighted; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param  focus              if <code>true</code>, render cell
   **                            appropriately. For example, put a special
   **                            border on the cell, if the cell can be edited,
   **                            render in the color used to indicate editing.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   */
  @Override
  public Component getListCellRendererComponent(final JList list, final Object value, final int index, final boolean selected, final boolean focus) {
    super.getListCellRendererComponent(list, value, index, selected, focus);
    setIcon(this.symbol);
    return this;
  }
}