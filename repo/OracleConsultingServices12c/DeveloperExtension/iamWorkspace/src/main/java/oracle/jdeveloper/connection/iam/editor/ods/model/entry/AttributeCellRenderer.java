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

    File        :   AttributeCellRenderer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    AttributeCellRenderer.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.editor.ods.model.entry;

import java.awt.Component;

import javax.swing.JTable;

import oracle.jdeveloper.connection.iam.model.DirectoryValue;
import oracle.jdeveloper.connection.iam.model.DirectoryAttribute;

import oracle.jdeveloper.connection.iam.editor.ods.resource.Bundle;

import oracle.jdeveloper.connection.iam.editor.ods.model.DirectoryTableCellRenderer;

////////////////////////////////////////////////////////////////////////////////
// class AttributeCellRenderer
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** A minimal extension of {@link DirectoryTableCellRenderer} allowing us to set
 ** some rows to bold font and others to normal font.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class AttributeCellRenderer extends DirectoryTableCellRenderer {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:4211890478518579897")
  private static final long serialVersionUID = 2714479469616271123L;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Type
  // ~~~~~ ~~~~
  /**
   ** A minimal extension of {@link AttributeCellRenderer} allowing us to set
   ** some rows to bold font and others to normal font.
   */
  public static class Type extends AttributeCellRenderer {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:2683435206076676605")
    private static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Type</code> table cell renderer responsible to
     ** visualize the attribute type a Directory Service entry.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    private Type() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: getTableCellRendererComponent (overridden)
    /**
     ** Returns the component used for drawing the cell.
     ** <br>
     ** This method is used to configure the renderer appropriately before
     ** drawing.
     ** <p>
     ** The <code>TableCellRenderer</code> is also responsible for rendering the
     ** the cell representing the table's current DnD drop location if it has
     ** one. If this renderer cares about rendering the DnD drop location, it
     ** should query the table directly to see if the given row and column
     ** represent the drop location:
     ** <pre>
     **  JTable.DropLocation dropLocation = table.getDropLocation();
     **  if (dropLocation != null
     **  &amp;&amp; !dropLocation.isInsertRow()
     **  &amp;&amp; !dropLocation.isInsertColumn()
     **  &amp;&amp; dropLocation.getRow() == row
     **  &amp;&amp; dropLocation.getColumn() == column) {
     **    // this cell represents the current drop location
     **    // so render it specially, perhaps with a different color
     **  }
     ** </pre>
     ** During a printing operation, this method will be called with
     ** <code>selected</code> and <code>hasFocus</code> values of
     ** <code>false</code> to prevent selection and focus from appearing in the
     ** printed output. To do other customization based on whether or not the
     ** table is being printed, check the return value from
     ** {@link javax.swing.JComponent#isPaintingForPrint()}.
     **
     ** @param  table            the <code>JTable</code> that is asking the
     **                          renderer to draw; can be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link JTable}.
     ** @param  value            the value of the cell to be rendered. It is up
     **                          to the specific renderer to interpret and draw
     **                          the value. For example, if <code>value</code>
     **                          is the string "true", it could be rendered as a
     **                          string or it could be rendered as a check box
     **                          that is checked.  <code>null</code> is a valid
     **                          value.
     **                          <br>
     **                          Allowed object is {@link JTable}.
     ** @param  selected         <code>true</code> if the cell is to be rendered
     **                          with the selection highlighted; otherwise
     **                          <code>false</code>.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     ** @param  focus            if <code>true</code>, render cell
     **                          appropriately. For example, put a special
     **                          border on the cell, if the cell can be edited,
     **                          render in the color used to indicate editing.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     ** @param  row              the row index of the cell being drawn. When
     **                          drawing the header, the value of
     **                          <code>row</code> is -1.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param  col              the column index of the cell being drawn.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     **
     ** @return                  the table cell renderer to apply
     **                          <br>
     **                          Allowed object is {@link Component}.
     **
     ** @see javax.swing.JComponent#isPaintingForPrint()
     */
    @Override
    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean selected, final boolean focus, final int row, final int col) {
      if (value instanceof DirectoryAttribute) {
        final DirectoryAttribute type = (DirectoryAttribute)value;
        // ensure inheritance
        super.getTableCellRendererComponent(table, type.name, selected, focus, row, col);
        setFont(type.required()       ? this.required.font  : type.readonly()    ? this.readonly.font  : this.normal.font);
        setForeground(type.readonly() ? this.readonly.color : type.objectClass() ? this.required.color : this.normal.color);
        if (type.is("aci"))
          setIcon(Bundle.icon(Bundle.ICON_ACCESS_CONTROL_INFORMATION));
        else if (type.objectClass())
          setIcon(Bundle.icon(Bundle.ICON_OBJECT_CLASS));
        else if (type.is("pwdPolicySubentry"))
          setIcon(Bundle.icon(Bundle.ICON_PASSWORD_POLICY));
        else
          setIcon(Bundle.icon(Bundle.ICON_ATTRIBUTE_TYPE));
      }
      return this;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Item
  // ~~~~~ ~~~~
  /**
   ** A minimal extension of {@link AttributeCellRenderer} allowing us to set
   ** some rows to bold font and others to normal font.
   */
  public static class Item extends AttributeCellRenderer {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:506069039838894004")
    private static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Item</code> table cell renderer responsible to
     ** visualize the attribute value a Directory Service entry.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    private Item() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: getTableCellRendererComponent (overridden)
    /**
     ** Returns the component used for drawing the cell.
     ** <br>
     ** This method is used to configure the renderer appropriately before
     ** drawing.
     ** <p>
     ** The <code>TableCellRenderer</code> is also responsible for rendering the
     ** the cell representing the table's current DnD drop location if it has
     ** one. If this renderer cares about rendering the DnD drop location, it
     ** should query the table directly to see if the given row and column
     ** represent the drop location:
     ** <pre>
     **  JTable.DropLocation dropLocation = table.getDropLocation();
     **  if (dropLocation != null
     **  &amp;&amp; !dropLocation.isInsertRow()
     **  &amp;&amp; !dropLocation.isInsertColumn()
     **  &amp;&amp; dropLocation.getRow() == row
     **  &amp;&amp; dropLocation.getColumn() == column) {
     **    // this cell represents the current drop location
     **    // so render it specially, perhaps with a different color
     **  }
     ** </pre>
     ** During a printing operation, this method will be called with
     ** <code>selected</code> and <code>hasFocus</code> values of
     ** <code>false</code> to prevent selection and focus from appearing in the
     ** printed output. To do other customization based on whether or not the
     ** table is being printed, check the return value from
     ** {@link javax.swing.JComponent#isPaintingForPrint()}.
     **
     ** @param  table            the <code>JTable</code> that is asking the
     **                          renderer to draw; can be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link JTable}.
     ** @param  value            the value of the cell to be rendered. It is up
     **                          to the specific renderer to interpret and draw
     **                          the value. For example, if <code>value</code>
     **                          is the string "true", it could be rendered as a
     **                          string or it could be rendered as a check box
     **                          that is checked.  <code>null</code> is a valid
     **                          value.
     **                          <br>
     **                          Allowed object is {@link JTable}.
     ** @param  selected         <code>true</code> if the cell is to be rendered
     **                          with the selection highlighted; otherwise
     **                          <code>false</code>.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     ** @param  focus            if <code>true</code>, render cell
     **                          appropriately. For example, put a special
     **                          border on the cell, if the cell can be edited,
     **                          render in the color used to indicate editing.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     ** @param  row              the row index of the cell being drawn. When
     **                          drawing the header, the value of
     **                          <code>row</code> is -1.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param  col              the column index of the cell being drawn.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     **
     ** @return                  the table cell renderer to apply
     **                          <br>
     **                          Allowed object is {@link Component}.
     **
     ** @see javax.swing.JComponent#isPaintingForPrint()
     */
    @Override
    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean selected, final boolean focus, final int row, final int col) {
      if (value instanceof DirectoryValue.Item) {
        final DirectoryValue.Item item = (DirectoryValue.Item)value;
        super.getTableCellRendererComponent(table, item, selected, focus, row, col);
        setFont(item.required()       ? this.required.font  : item.readonly()    ? this.readonly.font  : this.normal.font);
        setForeground(item.readonly() ? this.readonly.color : item.objectClass() ? this.required.color : this.normal.color);
        setToolTipText(item.toString());
      }
      return this;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>AttributeCellRenderer</code> that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private AttributeCellRenderer() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Factory method to create a <code>Type</code> table cell renderer
   ** responsible to visualize the attribute value a Directory Service entry.
   **
   ** @return                    a {@link Type} table cell renderer.
   **                            <br>
   **                            Possible object is
   **                            {@link DirectoryTableCellRenderer}.
   */
  public static DirectoryTableCellRenderer type() {
    return new Type();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   item
  /**
   ** Factory method to create a <code>Value</code> table cell renderer
   ** responsible to visualize the attribute value a Directory Service entry.
   **
   ** @return                    a {@link Item} table cell renderer.
   **                            <br>
   **                            Possible object is
   **                            {@link DirectoryTableCellRenderer}.
   */
  public static DirectoryTableCellRenderer item() {
    return new Item();
  }
}