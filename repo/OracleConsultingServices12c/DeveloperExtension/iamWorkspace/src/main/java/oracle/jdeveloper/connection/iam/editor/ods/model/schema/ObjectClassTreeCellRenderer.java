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

    Copyright © 2023. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   ObjectClassTreeCellRenderer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    ObjectClassTreeCellRenderer.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.102 2023-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.editor.ods.model.schema;

import java.awt.Component;

import javax.swing.Icon;

import javax.swing.JTree;

import javax.swing.tree.DefaultTreeCellRenderer;

import oracle.jdeveloper.connection.iam.editor.ods.resource.Bundle;

////////////////////////////////////////////////////////////////////////////////
// class DefaultContentTreeCellRenderer
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Displays a panel (Panel) in a tree.
 ** <p>
 ** <code>ObjectClassTreeCellRenderer</code> is not opaque and unless you
 ** subclass paint you should not change this.
 ** <p>
 ** See <a href="http://java.sun.com/docs/books/tutorial/uiswing/components/tree.html">How to Use Trees</a>
 ** in <em>The Java Tutorial</em> for examples of customizing node display using
 ** such classes.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.102
 ** @since   12.2.1.3.42.60.102
 */
public class ObjectClassTreeCellRenderer<R> extends DefaultTreeCellRenderer  {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the default hight of an entry in a list view */
  public static final int      DEFAULT_ROW_HEIGHT         = 18;
  public static final int      DEFAULT_ROW_THRESHOLD      = 20;

  private static final Icon[] SYMBOL = {
    Bundle.icon(Bundle.ICON_OBJECT_CLASS)
  , Bundle.icon(Bundle.ICON_ATTRIBUTE_TYPE)
  };

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:5087473261611512616")
  private static final long serialVersionUID = -1497439413776284236L;

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
   ** A minimal extension of {@link ObjectClassTreeCellRenderer} allowing us to
   ** decorate some rows with an {@link Icon} and set to bold font and
   ** highlighting colors.
   */
  private static class ObjectClass extends ObjectClassTreeCellRenderer<String> {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:-7097862981546240397")
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
   ** A minimal extension of {@link ObjectClassTreeCellRenderer} allowing us to
   ** decorate some rows with an {@link Icon} and set to bold font and
   ** highlighting colors.
   */
  private static class AttributeType extends ObjectClassTreeCellRenderer<String> {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:5478156841212964161")
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
   ** Constructs an empty <code>ObjectClassTreeCellRenderer</code> that renders
   ** the specified {@link Icon}.
   **
   ** @param  symbol             the {@link Icon} to display in a table row.
   **                            <br>
   **                            Allowed object is {@link Icon}.
   */
  private ObjectClassTreeCellRenderer(final Icon symbol) {
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
   ** Factory method to create an <code>ObjectClassTreeCellRenderer</code> that
   ** allows use as a JavaBean which renders names for object class
   ** declarations of a Directory Service schema in a {@link JTree}
   **
   ** @return                    the validated
   **                            <code>ObjectClassTreeCellRenderer</code>.
   **                            Possible object is
   **                            <code>ObjectClassTreeCellRenderer</code>.
   */
  public static ObjectClassTreeCellRenderer<String> objectClass() {
    return new ObjectClass();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributeType
  /**
   ** Factory method to create an <code>ObjectClassTreeCellRenderer</code> that
   ** allows use as a JavaBean which renders names for attribute type
   ** declarations of a Directory Service schema in a {@link JTree}
   **
   ** @return                    the validated
   **                            <code>ObjectClassTreeCellRenderer</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>ObjectClassTreeCellRenderer</code>.
   */
  public static ObjectClassTreeCellRenderer<String> attributeType() {
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
   ** @param  tree               the {@link JTree} that is asking the renderer
   **                            to draw; can be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link JTree}.
   ** @param  value              the value of the cell to be rendered. It is up
   **                            to the specific renderer to interpret and draw
   **                            the value. For example, if <code>value</code>
   **                            is the string "true", it could be rendered as a
   **                            string or it could be rendered as a check box
   **                            that is checked.  <code>null</code> is a valid
   **                            value.
   **                            <br>
   **                            Allowed object is {@link Object}.
   ** @param  selected           <code>true</code> if the cell is to be rendered
   **                            with the selection highlighted; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param  expanded           <code>true</code> if the cell is to be rendered
   **                            expanded; otherwise <code>false</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param  row                the index of the cell being drawn.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  focused            if <code>true</code>, render cell
   **                            appropriately. For example, put a special
   **                            border on the cell, if the cell can be edited,
   **                            render in the color used to indicate editing.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.c   */
  @Override
  public Component getTreeCellRendererComponent(final JTree tree, final Object value, final boolean selected, final boolean expanded, final boolean leaf, final int row, final boolean focused) {
    // ensure inheritance
    super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, focused);
    setIcon(this.symbol);
    return this;
  }
}