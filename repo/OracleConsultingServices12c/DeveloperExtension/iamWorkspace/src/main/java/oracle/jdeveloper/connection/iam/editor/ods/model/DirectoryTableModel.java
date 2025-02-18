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

    File        :   DirectoryTableModel.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    DirectoryTableModel.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.editor.ods.model;

import java.util.List;
import java.util.ArrayList;

import java.awt.Component;

import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;

import javax.swing.table.TableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.AbstractTableModel;

import oracle.jdeveloper.connection.iam.editor.ods.resource.Bundle;

////////////////////////////////////////////////////////////////////////////////
// abstract class DirectoryTableModel
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** A table model suitable for the list of attributes of a Directory Service
 ** entry in a form.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public abstract class DirectoryTableModel extends AbstractTableModel {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final Icon[] SORT = {
    /**
     ** Symbol value indicating the items are unordered.
     ** For example, the set <code>1, 4, 0</code> in
     ** <code>UNSORTED</code> order is <code>1, 4, 0</code>.
     */
    null
    /**
     ** Symbol indicating the items are sorted in decreasing order.
     ** For example, the set <code>1, 4, 0</code> sorted in
     ** <code>DESCENDING</code> order is <code>4, 1, 0</code>.
     */
  , Bundle.icon(Bundle.ICON_SORT_DESCENDING)
    /**
     ** Symbol indicating the items are sorted in increasing order.
     ** For example, the set <code>1, 4, 0</code> sorted in
     ** <code>ASCENDING</code> order is <code>0, 1, 4</code>.
     */
  , Bundle.icon(Bundle.ICON_SORT_ASCENDING)
  };

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-6490946051179171674")
  private static final long serialVersionUID = 440358801007363516L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final transient Class<?>[]                   type;
  final transient String[]                     name;

  private transient int                        clickCount;
  private transient TableRowSorter<TableModel> sorter;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Trigger
  // ~~~~~ ~~~~~~~
  /**
   ** Default sort behaviour, plus every third click removes the sort.
   */
  private final class Trigger extends MouseAdapter {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>Trigger</code> that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    private Trigger() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: mouseClicked (overridden)
    /**
     ** Invoked when the mouse button has been clicked (pressed and released) on
     ** a component.
     ** <p>
     ** See the class description for {@link MouseEvent} for a definition of a
     ** mouse clicked event.
     **
     ** @param  event              the data that characterizes the event.
     **                            <br>
     **                            Allowed object is {@link MouseEvent}.
     */
    @Override
    public void mouseClicked(final MouseEvent event) {
      final JTableHeader header = (JTableHeader)event.getSource();
      final int          column = header.getColumnModel().getColumnIndexAtX(event.getX());
      if (column == 0) {
        // build a list of sort keys for this column, and pass it to the sorter
        // you can build the list to fit your needs here for example, you can
        // sort on multiple columns, not just one
        final List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        // cycle through all orders; sort is removed every 3rd click
        final SortOrder order = SortOrder.values()[clickCount % 3];
        sortKeys.add(new RowSorter.SortKey(column, order));
        sorter.setSortKeys(sortKeys);
        clickCount++;
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Renderer
  // ~~~~~ ~~~~~~~~
  /**
   ** A minimal extension of {@link TableCellRenderer} allowing us to set an
   ** icon to indicate the sort order of the model.
   */
  private final class Renderer implements TableCellRenderer {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////
    
    final TableCellRenderer delegate;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>Renderer</code> that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    private Renderer(final TableCellRenderer renderer) {
      // ensure inheritance
      super();

      // initialize instance
      this.delegate = renderer;
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
     ** @param  value            the value of the cell to be rendered. It is up
     **                          to the specific renderer to interpret and draw
     **                          the value. For example, if <code>value</code>
     **                          is the string "true", it could be rendered as a
     **                          string or it could be rendered as a check box
     **                          that is checked.  <code>null</code> is a valid
     **                          value.
     ** @param  selected         <code>true</code> if the cell is to be rendered
     **                          with the selection highlighted; otherwise
     **                          <code>false</code>.
     ** @param  focus            if <code>true</code>, render cell
     **                          appropriately. For example, put a special
     **                          border on the cell, if the cell can be edited,
     **                          render in the color used to indicate editing.
     ** @param  row              the row index of the cell being drawn. When
     **                          drawing the header, the value of
     **                          <code>row</code> is -1.
     ** @param  column           the column index of the cell being drawn.
     **
     ** @see javax.swing.JComponent#isPaintingForPrint()
     */
    @Override
    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean selected, final boolean focus, final int row, final int column) {
      final Component header = this.delegate.getTableCellRendererComponent(table, value, selected, focus, row, column);
      if (header instanceof JLabel) {
        final JLabel label = (JLabel)header;
        label.setHorizontalTextPosition(JLabel.LEFT);
        label.setIcon((column == 0) ? SORT[clickCount % 3] : null); 
      }
      return header;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DirectoryTableModel</code> responsible to
   ** populate the table UI of entry attribute view.
   **
   ** @param  name               the array of string representing the column
   **                            names.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   ** @param  type               the array of classes for each column.
   **                            <br>
   **                            Allowed object is array of {@link Class}.
   */
  protected DirectoryTableModel(final String[] name, final Class<?>[] type) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.name = name;
    this.type = type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getColumnClass (overridden)
  /**
   ** Returns the class of the objects in the different columns:
   ** DirectoryAttribute.class for column 0, DirectoryValue.class for col 1.
   **
   **
   ** @param  column             the column being queried.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the Object.class.
   **                            <br>
   **                            Possible object is {@link Class}.
   */
  @Override
  public Class getColumnClass(final int column) {
    return this.type[column];
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isCellEditable (overridden)
  /**
   ** Returns <code>true</code> if the cell coordinates pointing to the value
   ** cell of the model.
   **
   ** @param  row                the row being queried.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  column             the column being queried.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    <code>true</code> if the second column
   **                            (index = 1) being queried; otherwise
   **                            <code>false</code>
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  public boolean isCellEditable(final int row, final int column) {
    return column >= 0;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getColumnCount (TableModel)
  /**
   ** Returns the number of column in the table.
   **
   ** @return                    the number of columns in the table.
   **                            <br>
   **                            Possible object is <code>int</code>.
   **
   ** @see    AbstractTableModel#getColumnCount
   */
  @Override
  public int getColumnCount() {
    return this.name.length;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getColumnName (TableModel)
  /**
   ** Returns the title for this column.
   **
   ** @param  column             the column being queried.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the title for this column.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @see    AbstractTableModel#getColumnName
   */
  @Override
  public String getColumnName(final int column) {
    return this.name[column];
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sortTrigger
  /**
   ** Applies a {@link MouseAdapter} on the given {@link JTable} used to trigger
   ** the sort behavior.
   **
   ** @param  table              the {@link JTable} being sorted.
   **                            <br>
   **                            Allowed object is {@link JTable}.
   */
  public void sortTrigger(final JTable table) {
    this.sorter   = new TableRowSorter<>(this);
    this.sorter.setSortable(0, true);
    for (int i = 1; i < name.length; i++)
      this.sorter.setSortable(i, false);
    table.setRowSorter(this.sorter);
    table.getTableHeader().addMouseListener(new Trigger());
    table.getTableHeader().setDefaultRenderer(new Renderer(table.getTableHeader().getDefaultRenderer()));
  }
}