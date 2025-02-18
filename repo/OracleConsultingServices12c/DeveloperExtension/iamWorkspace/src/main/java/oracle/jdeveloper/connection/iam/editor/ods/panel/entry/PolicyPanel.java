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

    File        :   PolicyPanel.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    PolicyPanel.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.editor.ods.panel.entry;

import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.JComponent;

import javax.swing.table.TableModel;

import javax.swing.event.TableModelListener;

import oracle.javatools.ui.ComponentWithTitlebar;

import oracle.ide.util.Namespace;

import oracle.jdeveloper.workspace.iam.swing.LayoutFactory;

import oracle.jdeveloper.connection.iam.model.DirectoryValue;
import oracle.jdeveloper.connection.iam.model.DirectoryAttribute;

import oracle.jdeveloper.connection.iam.editor.ods.resource.Bundle;

import oracle.jdeveloper.connection.iam.editor.ods.panel.AbstractPanel;

import oracle.jdeveloper.connection.iam.editor.ods.model.entry.AttributeModel;
import oracle.jdeveloper.connection.iam.editor.ods.model.entry.AttributeCellRenderer;

////////////////////////////////////////////////////////////////////////////////
// class PolicyPanel
// ~~~~~ ~~~~~~~~~~~
/**
 ** A flat editor panel suitable to display Policy Information of a Directory
 ** Service entry in a table layout.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class PolicyPanel extends AbstractPanel {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-5685012512235080331")
  private static final long serialVersionUID = -7045453836073678822L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  transient Table                 view;
  transient AttributeModel        model;
  transient RowSorter<TableModel> sorter;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Table
  // ~~~~~ ~~~~~
  /**
   ** A minimal extension of {@link JTable} allowing us to show tooltips for
   ** some cells.
   */
  public static class Table extends JTable {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:-2238124372328450751")
    private static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Table</code> that is initialized with
     ** <code>model</code> as the data model, a default column model, and a
     ** default selection model.
     **
     ** @param  model            the data model for the table.
     **                          <br>
     **                          Allowed object is {@link AttributeModel}.
     */
    public Table(final AttributeModel model) {
      // ensure inheritance
      super(model);

      // this may be needed, depends on how fussy people get about the bottom of
      // letters like 'y' getting cut off when the cell is selected
      setRowHeight(AttributeCellRenderer.DEFAULT_ROW_HEIGHT);
      // set the renderer for the attribute type
      setDefaultRenderer(DirectoryAttribute.class, AttributeCellRenderer.type());
      // set the renderer for the attribute value
      setDefaultRenderer(Object.class, AttributeCellRenderer.item());
      // switch of reordering of the table columns at all
      getTableHeader().setReorderingAllowed(false);
      // adjust the geometry of some columns
      // first column is smaller
      getColumnModel().getColumn(0).setMaxWidth(200);
      getColumnModel().getColumn(0).setPreferredWidth(200);
      // attach the sort capabilities on this tabe
      model.sortTrigger(this);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>SecurityPanel</code> responsible to visualize
   ** the table UI of an Access Control Information view that populates its data
   ** from the specified {@link Namespace}.
   **
   ** @param  path               the name path whose associated value is to be
   **                            handled.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  data               the {@link Namespace} providing access to the
   **                            context.
   **                            <br>
   **                            Allowed object is {@link Namespace}.
   */
  @SuppressWarnings("unchecked")
  private PolicyPanel(final String path, final Namespace data) {
    // ensure inheritance
    super(path, data);

    // the data that the SecurityPanel use to populate specific components
    // comes from the Namespace passed to this constructor and accessed by the
    // specified path
    this.model = AttributeModel.build((Map<DirectoryAttribute, DirectoryValue>)data.get(this.path), false);
    // create the view responsible to visualize the security information about
    // the directory entry
    final JTable view = new Table(this.model);
    // this time, we'll put the table in a scroll pane if the row size exceeds
    // the threshold of 25 entries.
    add(new ComponentWithTitlebar<JComponent>(this.model.getRowCount() > AttributeCellRenderer.DEFAULT_ROW_THRESHOLD ? LayoutFactory.scrollableTable(view) : LayoutFactory.standaloneTable(view), new JLabel(Bundle.string(Bundle.ENTRY_ATTRIBUTE_PANEL_HEADER)), null));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  // Method:   updateView (AbstractPanel)
  /**
   ** The data that the <code>FlatEditorTransparentPanel</code> should use to
   ** populate specific components comes from the {@link Namespace} passed to
   ** this page.
   */
  @Override
  @SuppressWarnings("unchecked")
  public void updateView() {
    this.model.update((Map<DirectoryAttribute, DirectoryValue>)data.get(this.path));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>PolicyPanel</code> that populates its
   ** data from the specified {@link Namespace}.
   ** <p>
   ** The string representation of the DN can be in RFC 2253 format.
   **
   ** @param  path               the name path whose associated value is to be
   **                            handled.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  data               the {@link Namespace} providing access to the
   **                            context.
   **                            <br>
   **                            Allowed object is {@link Namespace}.
   **
   ** @return                    the validated <code>PolicyPanel</code>.
   **                            <br>
   **                            Possible object <code>PolicyPanel</code>.
   */
  public static PolicyPanel build(final String path, final Namespace data) {
    return new PolicyPanel(path, data);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addListener
  /**
   ** Adds a listener to the table model that's notified each time a change to
   ** the data model occurs.
   **
   ** @param listener            the {@link TableModelListener} to add.
   **                            <br>
   **                            Allowed object is {@link TableModelListener}.
   */
  public void addListener(final TableModelListener listener) {
    this.model.addTableModelListener(listener);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeListener
  /**
   ** Removes a listener to the table model that's notified each time a change
   ** to the data model occurs.
   **
   ** @param listener            the {@link TableModelListener} to remove.
   **                            <br>
   **                            Allowed object is {@link TableModelListener}.
   */
  public void removeListener(final TableModelListener listener) {
    this.model.removeTableModelListener(listener);
  }
}