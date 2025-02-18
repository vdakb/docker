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

    File        :   ObjectClassPanel.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    ObjectClassPanel.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.editor.ods.panel.schema;

import java.util.Map;

import java.awt.Insets;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.JList;
import javax.swing.JTree;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JComponent;
import javax.swing.ListSelectionModel;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import oracle.ide.util.Namespace;

import oracle.javatools.ui.ComponentWithTitlebar;

import oracle.jdeveloper.workspace.iam.swing.LayoutFactory;

import oracle.jdeveloper.workspace.iam.swing.widget.ItemizedListModel;

import oracle.jdeveloper.connection.iam.model.DirectorySchema;

import oracle.jdeveloper.connection.iam.editor.ods.resource.Bundle;

import oracle.jdeveloper.connection.iam.editor.ods.panel.AbstractPanel;

import oracle.jdeveloper.connection.iam.editor.ods.model.DirectoryTableCellRenderer;

import oracle.jdeveloper.connection.iam.editor.ods.model.schema.ObjectClassTreeModel;
import oracle.jdeveloper.connection.iam.editor.ods.model.schema.ObjectClassDetailModel;
import oracle.jdeveloper.connection.iam.editor.ods.model.schema.SchemaListCellRenderer;
import oracle.jdeveloper.connection.iam.editor.ods.model.schema.ObjectClassTreeCellRenderer;

////////////////////////////////////////////////////////////////////////////////
// class ObjectClassPanel
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** A flat editor panel suitable to display object classes of a Directory
 ** Service schema in a table layout.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class ObjectClassPanel extends    AbstractPanel
                              implements ListSelectionListener {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-8082204723561515598")
  private static final long serialVersionUID = -1238728415144801286L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  transient JList<String>                            list;
  transient JList<String>                            sup;
  transient JTree                                    must;
  transient JTree                                    may;
  transient ObjectClassTreeModel                     required;
  transient ObjectClassTreeModel                     optional;
  transient ObjectClassDetailModel                   detail;
  transient Map<String, DirectorySchema.ObjectClass> collection;

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
    @SuppressWarnings("compatibility:575353933948407520")
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
     **                          Allowed object is
     **                          {@link ObjectClassDetailModel}.
     */
    public Table(final ObjectClassDetailModel model) {
      // ensure inheritance
      super(model);
      // This may be needed, depends on how fussy people get about the bottom of
      // letters like 'y' getting cut off when the cell is selected.
      setRowHeight(SchemaListCellRenderer.DEFAULT_ROW_HEIGHT);
      // Set the renderer for the label
      setDefaultRenderer(model.getColumnClass(0), new DirectoryTableCellRenderer());
      // Set the renderer for the value
      setDefaultRenderer(model.getColumnClass(1), new DirectoryTableCellRenderer());
      // switch of reordering of the table columns at all
      getTableHeader().setReorderingAllowed(false);
      // adjust the geometry of some columns
      // first column is smaller
      getColumnModel().getColumn(0).setMaxWidth(100);
      getColumnModel().getColumn(0).setPreferredWidth(100);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>ObjectClassPanel</code> responsible to visualize
   ** the table UI of an schema object class view that populates its data from
   ** the specified {@link Namespace}.
   **
   ** @param  path               the name path whose associated value is to be
   **                            handled.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  data               the {@link Namespace} providing access to the
   **                            data.
   **                            <br>
   **                            Allowed object is {@link Namespace}.
   */
  @SuppressWarnings("unchecked") 
  private ObjectClassPanel(final String path, final Namespace data) {
    // ensure inheritance
    super(path, data);

    // the data that the ObjectClassPanel use to populate specific components
    // comes from the Namespace passed to this constructor and accessed by the
    // specified path
    this.collection = (Map<String, DirectorySchema.ObjectClass>)data.get(this.path);
    this.detail     = ObjectClassDetailModel.build();
    this.required   = ObjectClassTreeModel.build();
    this.optional   = ObjectClassTreeModel.build();
    this.list       = new JList<String>(ItemizedListModel.<String>build(this.collection.keySet()));
    this.list.setCellRenderer(SchemaListCellRenderer.objectClass());
    this.list.setFixedCellHeight(SchemaListCellRenderer.DEFAULT_ROW_HEIGHT);
    this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    this.sup        = new JList<String>();
    this.sup.setCellRenderer(SchemaListCellRenderer.objectClass());
    this.sup.setFixedCellHeight(SchemaListCellRenderer.DEFAULT_ROW_HEIGHT);
    this.sup.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    this.must       = new JTree(this.required);
    this.must.setCellRenderer(ObjectClassTreeCellRenderer.attributeType());
    this.must.setRootVisible(false);
    this.must.setShowsRootHandles(true);
    this.must.setRowHeight(ObjectClassTreeCellRenderer.DEFAULT_ROW_HEIGHT);

    this.may        = new JTree(this.optional);
    this.may.setCellRenderer(ObjectClassTreeCellRenderer.attributeType());
    this.may.setRootVisible(false);
    this.may.setShowsRootHandles(true);
    this.may.setRowHeight(ObjectClassTreeCellRenderer.DEFAULT_ROW_HEIGHT);

    // the object class browser is placed in the leftmost column of the grid
    // the control is stretched vertically
    add(
      // this time, we'll put the list in a scroll pane
      new ComponentWithTitlebar<JComponent>(
        LayoutFactory.scrollableList(this.list, new Dimension(640, SchemaListCellRenderer.DEFAULT_ROW_THRESHOLD * SchemaListCellRenderer.DEFAULT_ROW_HEIGHT))
      , new JLabel(Bundle.string(Bundle.SCHEMA_OBJECTCLASS_BROWSE_HEADER))
      , null
      )
    , new GridBagConstraints(0, 0, 1, 4, 0.0D, 1.0D, GridBagConstraints.NORTHWEST, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0)
    );

    // initialize layout constraints
    GridBagConstraints gbc = new GridBagConstraints(1, 0, 1, 1, 1.0D, 0.0D, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 0), 0, 0);
    // the object class details are placed in the rightmost column of the grid
    // the control is stretched horizontal
    add(
      new ComponentWithTitlebar<JComponent>(
        LayoutFactory.standaloneTable(new Table(this.detail), new Dimension(140,  4 * SchemaListCellRenderer.DEFAULT_ROW_HEIGHT), false)
      , new JLabel(Bundle.string(Bundle.SCHEMA_OBJECTCLASS_DETAIL_HEADER))
      , null
      )
    , gbc
    );

    gbc.gridy  = 1;
    gbc.insets = new Insets(5, 10, 0, 0);
    // the object class inheritance are placed below the property taböe of the
    // grid the control is stretched horizontal
    add(
      new ComponentWithTitlebar<JComponent>(
        LayoutFactory.scrollableList(this.sup, new Dimension(140, 4 * SchemaListCellRenderer.DEFAULT_ROW_HEIGHT))
      , new JLabel(Bundle.string(Bundle.SCHEMA_OBJECTCLASS_SUPERIROR_LABEL))
      , null
      )
    , gbc
    );

    gbc.gridy = 2;
    // create the panel responsible to visualize the required attributes about
    // the object class entry
    add(
      new ComponentWithTitlebar<JComponent>(
        LayoutFactory.scrollableTree(this.must, new Dimension(140, 8 * ObjectClassTreeCellRenderer.DEFAULT_ROW_HEIGHT))
      , new JLabel(Bundle.string(Bundle.SCHEMA_OBJECTCLASS_MUST_LABEL))
      , null
      )
    , gbc
    );

    gbc.gridy = 3;
    // create the panel responsible to visualize the optional attributes about
    // the object class entry
    add(
      new ComponentWithTitlebar<JComponent>(
        LayoutFactory.scrollableTree(this.may, new Dimension(140, 8 * ObjectClassTreeCellRenderer.DEFAULT_ROW_HEIGHT))
      , new JLabel(Bundle.string(Bundle.SCHEMA_OBJECTCLASS_MAY_LABEL))
      , null
      )
    , gbc
    );

    this.list.addListSelectionListener(this);
    this.list.setSelectedIndex(0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   valueChanged (ListSelectionListener)
  /**
   ** Called whenever the value of the selection changes.
   **
   ** @param  event              the event that characterizes the change.
   **                            <br>
   **                            Allowed object is {@link ListSelectionEvent}.
   */
  @Override
  @SuppressWarnings("unchecked") 
  public void valueChanged(final ListSelectionEvent event) {
    // wait until the event signals that the selection is adjusted means the
    // new value is selected and focused in the list
    if (event.getValueIsAdjusting())
      return;

    // obtain the view, ususally the list of attribute type names
    final JList<String> list = (JList<String>)event.getSource();
    // prevent bogus state
    final String value = list.getSelectedValue();
    if (value == null || value.length() == 0)
      return;

    // refresh the table view with the selected common properties
    final DirectorySchema.ObjectClass structure = this.collection.get(value);
    this.detail.refresh(structure);

    // refresh table view with the selected inheritance properties
    this.sup.setModel(ItemizedListModel.<String>build(structure.superior));

    // refresh the tree view with the selected required attributes properties
    this.required.refresh(this.must, structure.required);

    // refresh the tree view with the selected optional attributes properties
    this.optional.refresh(this.may, structure.optional);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

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
    this.collection = (Map<String, DirectorySchema.ObjectClass>)this.data.get(this.path);
    this.list.setModel(ItemizedListModel.build(this.collection.keySet()));
    this.list.setSelectedIndex(0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>ObjectClassPanel</code> that populates
   ** its data from the specified {@link Namespace}.
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
   ** @return                    the validated <code>ObjectClassPanel</code>.
   **                            <br>
   **                            Possible object <code>ObjectClassPanel</code>.
   */
  public static ObjectClassPanel build(final String path, final Namespace data) {
    return new ObjectClassPanel(path, data);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeLayout (overriddden)
  /**
   ** Layout the page.
   ** <br>
   ** By default the layout in initialized as a {@link GridBagLayout}.
   ** <p>
   ** Sub classes should override this mathod of anaothe layout is more
   ** appropriate
   */
  @Override
  protected void initializeLayout() {
    // initialize panel layout with 1 row, and 2 columns
    setLayout(new GridBagLayout());
  }
}