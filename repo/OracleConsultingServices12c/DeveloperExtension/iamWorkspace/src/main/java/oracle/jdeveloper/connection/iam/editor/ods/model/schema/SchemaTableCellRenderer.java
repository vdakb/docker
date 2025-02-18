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

    File        :   SchemaTableCellRenderer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    SchemaTableCellRenderer.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.editor.ods.model.schema;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTable;

import oracle.jdeveloper.connection.iam.editor.ods.resource.Bundle;

import oracle.jdeveloper.connection.iam.editor.ods.model.DirectoryTableCellRenderer;

////////////////////////////////////////////////////////////////////////////////
// abstract class SchemaTableCellRenderer
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** A minimal extension of {@link DirectoryTableCellRenderer} allowing us to
 ** decorate some rows with an {@link Icon} and set to bold font and
 ** highlighting colors.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public abstract class SchemaTableCellRenderer extends DirectoryTableCellRenderer {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final Icon[] SYMBOL = {
    Bundle.icon(Bundle.ICON_SYNTAX)
  , Bundle.icon(Bundle.ICON_MATCHING_RULE)
  };

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-2298547057129271547")
  private static final long serialVersionUID = -7453638837003362349L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////
  
  private final transient Icon symbol;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Syntax
  // ~~~~~ ~~~~~~
  /**
   ** A minimal extension of {@link SchemaTableCellRenderer} allowing us to
   ** decorate some rows with an {@link Icon} and set to bold font and
   ** highlighting colors.
   */
  public static class Syntax extends SchemaTableCellRenderer {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:-449146995851445956")
    private static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Syntax</code> table cell renderer responsible to
     ** visualize the syntax declarations of a Directory Service schema.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Syntax() {
      // ensure inheritance
      super(SYMBOL[0]);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class MatchingRule
  // ~~~~~ ~~~~~~~~~~~~
  /**
   ** A minimal extension of {@link SchemaTableCellRenderer} allowing us to
   ** decorate some rows with an {@link Icon} and set to bold font and
   ** highlighting colors.
   */
  public static class MatchingRule extends SchemaTableCellRenderer {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:1865786585750915197")
    private static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>MatchingRule</code> table cell renderer responsible to
     ** visualize the syntax declarations of a Directory Service schema.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public MatchingRule() {
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
   ** Constructs an empty <code>SchemaTableCellRenderer</code> that renders the
   ** specified {@link Icon}.
   **
   ** @param  symbol             the {@link Icon} to display in a table row.
   */
  private SchemaTableCellRenderer(final Icon symbol) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.symbol = symbol;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getTableCellRendererComponent (overridden)
  /**
   ** Returns the component used for drawing the cell.
   ** <br>
   ** This method is used to configure the renderer appropriately before
   ** drawing.
   ** <p>
   ** The <code>TableCellRenderer</code> is also responsible for rendering the
   ** the cell representing the table's current DnD drop location if it has one.
   ** If this renderer cares about rendering the DnD drop location, it should
   ** query the table directly to see if the given row and column represent the
   ** drop location:
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
   ** @param  table              the <code>JTable</code> that is asking the
   **                            renderer to draw; can be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link JTable}.
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
   ** @param  focus              if <code>true</code>, render cell
   **                            appropriately. For example, put a special
   **                            border on the cell, if the cell can be edited,
   **                            render in the color used to indicate editing.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param  row                the row index of the cell being drawn. When
   **                            drawing the header, the value of
   **                            <code>row</code> is -1.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  col                the column index of the cell being drawn.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the table cell renderer to apply
   **                            <br>
   **                            Allowed object is {@link Component}.
   **
   ** @see javax.swing.JComponent#isPaintingForPrint()
   */
  @Override
  public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean selected, final boolean focus, final int row, final int col) {
    // apply row banding style on table rows
    super.getTableCellRendererComponent(table, value, selected, focus, row, col);
    if (col == 0) {
      setForeground(COLOR_FOREGROUND_REQUIRED);
      setIcon(this.symbol);
    }
    else {
      setIcon(null);
    }
    return this;
  }
}