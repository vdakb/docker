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

    File        :   AttributePanel.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    AttributePanel.


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
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import oracle.javatools.ui.layout.VerticalFlowLayout;

import oracle.ide.util.Namespace;

import oracle.javatools.ui.ComponentWithTitlebar;

import oracle.jdeveloper.workspace.iam.swing.LayoutFactory;

import oracle.jdeveloper.connection.iam.model.DirectorySchema;

import oracle.jdeveloper.connection.iam.editor.ods.SchemaEditor;

import oracle.jdeveloper.connection.iam.editor.ods.resource.Bundle;

import oracle.jdeveloper.connection.iam.editor.ods.model.DirectoryTableCellRenderer;

import oracle.jdeveloper.connection.iam.editor.ods.panel.AbstractPanel;

import oracle.jdeveloper.connection.iam.editor.ods.model.schema.AttributeNameModel;
import oracle.jdeveloper.connection.iam.editor.ods.model.schema.AttributeDetailModel;
import oracle.jdeveloper.connection.iam.editor.ods.model.schema.SchemaListCellRenderer;

////////////////////////////////////////////////////////////////////////////////
// class AttributePanel
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** A flat editor panel suitable to display attributes of a Directory Service
 ** schema in a table layout.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class AttributePanel extends    AbstractPanel
                            implements ListSelectionListener {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:7105277784559299727")
  private static final long serialVersionUID = 7249474439256223377L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final JList<String>                              list;
  final AttributeDetailModel                       detail;
  transient Map<String, DirectorySchema.Attribute> collection;
  
  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>AttributePanel</code> responsible to visualize
   ** the table UI of an schema attribute types view that populates its data
   ** from the specified {@link Namespace}.
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
  private AttributePanel(final String path, final Namespace data) {
    // ensure inheritance
    super(path, data);

    // the data that the AttributePanel use to populate specific components
    // comes from the Namespace passed to this constructor and accessed by the
    // specified path
    this.collection = (Map<String, DirectorySchema.Attribute>)this.data.get(this.path);
    this.detail     = AttributeDetailModel.build();

    this.list       = new JList<String>(AttributeNameModel.build(this.collection.keySet()));
    this.list.setCellRenderer(SchemaListCellRenderer.attributeType());
    this.list.setFixedCellHeight(SchemaListCellRenderer.DEFAULT_ROW_HEIGHT);
    this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    final JScrollPane pane = LayoutFactory.scrollableList(this.list, new Dimension(640, SchemaListCellRenderer.DEFAULT_ROW_THRESHOLD * SchemaListCellRenderer.DEFAULT_ROW_HEIGHT));

    // create the panel responsible to visualize the general information about
    // the directory entry
    final JTable t = new JTable(this.detail);
    // This may be needed, depends on how fussy people get about the bottom of
    // letters like 'y' getting cut off when the cell is selected.
    t.setRowHeight(SchemaListCellRenderer.DEFAULT_ROW_HEIGHT);
    // Set the renderer for the label
    t.setDefaultRenderer(this.detail.getColumnClass(0), new DirectoryTableCellRenderer());
    // Set the renderer for the value
    t.setDefaultRenderer(this.detail.getColumnClass(1), new DirectoryTableCellRenderer());
      // switch of reordering of the table columns at all
    t.getTableHeader().setReorderingAllowed(false);
    // adjust the geometry of some columns
    // first column is smaller
    t.getColumnModel().getColumn(0).setMaxWidth(100);
    t.getColumnModel().getColumn(0).setPreferredWidth(100);

    // the attribute browser is placed in the leftmost column of the grid
    // the control is stretched vertically
    add(
      // this time, we'll put the list in a scroll pane
      new ComponentWithTitlebar<JComponent>(pane, new JLabel(Bundle.string(Bundle.SCHEMA_ATTRIBUTE_BROWSE_HEADER)), null)
    , new GridBagConstraints(0, 0, 1, 1, 0.0D, 1.0D, GridBagConstraints.NORTHWEST, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0)
    );
    // the attribute details is placed in the rightmost column of the grid
    // the control is stretched horizontal
    add(
      new ComponentWithTitlebar<JComponent>(t, new JLabel(Bundle.string(Bundle.SCHEMA_ATTRIBUTE_DETAIL_HEADER)), null)
    , new GridBagConstraints(1, 0, 1, 1, 1.0D, 0.0D, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 0), 0, 0)
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

    // refresh the table view with the selected properties
    this.detail.refresh(this.collection.get(value));
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
    this.collection = (Map<String, DirectorySchema.Attribute>)this.data.get(SchemaEditor.ATTRIBUTE);
    this.list.setModel(AttributeNameModel.build(this.collection.keySet()));
    this.list.setSelectedIndex(0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>AttributePanel</code> that populates its
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
   ** @return                    the validated <code>AttributePanel</code>.
   **                            <br>
   **                            Possible object <code>AttributePanel</code>.
   */
  public static AttributePanel build(final String path, final Namespace data) {
    return new AttributePanel(path, data);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeLayout (overriddden)
  /**
   ** Layout the page.
   ** <br>
   ** By default the layout in initialized as a {@link VerticalFlowLayout}.
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