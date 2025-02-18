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

package oracle.jdeveloper.connection.iam.editor.ods.panel.entry;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JComponent;

import javax.swing.event.TableModelListener;

import oracle.javatools.ui.ComponentWithTitlebar;

import oracle.ide.util.Namespace;

import oracle.jdeveloper.workspace.iam.swing.LayoutFactory;

import oracle.jdeveloper.connection.iam.model.DirectoryValue;
import oracle.jdeveloper.connection.iam.model.DirectoryAttribute;

import oracle.jdeveloper.connection.iam.editor.ods.resource.Bundle;

import oracle.jdeveloper.connection.iam.editor.ods.panel.AbstractPanel;

import oracle.jdeveloper.connection.iam.editor.ods.model.entry.AttributeModel;
import oracle.jdeveloper.connection.iam.editor.ods.model.entry.AttributeEditor;
import oracle.jdeveloper.connection.iam.editor.ods.model.entry.AttributeCellRenderer;

////////////////////////////////////////////////////////////////////////////////
// class AttributePanel
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** A flat editor panel suitable to display and modify attributes of a Directory
 ** Service entry in a table layout.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class AttributePanel extends    AbstractPanel
                            implements PropertyChangeListener {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:1329635231374265955")
  private static final long serialVersionUID = -8530227860399364746L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  transient View            view;
  transient AttributeModel  model;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Table
  // ~~~~~ ~~~~~
  /**
   ** A minimal extension of {@link JTable} allowing table editing under some
   ** conditions build by the underlying model.
   */
  public static class View extends JTable {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:8701332970055663125")
    private static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>View</code> that is initialized with
     ** <code>model</code> as the data model, a default column model, and a
     ** default selection model.
     **
     ** @param  model            the data model for the table.
     **                          <br>
     **                          Allowed object is {@link AttributeModel}.
     */
    public View(final AttributeModel model) {
      // ensure inheritance
      super(model);

      // this may be needed, depends on how fussy people get about the bottom of
      // letters like 'y' getting cut off when the cell is selected
      setRowHeight(AttributeCellRenderer.DEFAULT_ROW_HEIGHT);
      // set the renderer for the attribute type
      setDefaultRenderer(DirectoryAttribute.class,  AttributeCellRenderer.type());
      // set the renderer for the attribute value
      setDefaultRenderer(Object.class, AttributeCellRenderer.item());
      // switch of reordering of the table columns at all
      getTableHeader().setReorderingAllowed(false);
      // adjust the geometry of some columns
      // first column is smaller
      getColumnModel().getColumn(0).setMaxWidth(200);
      getColumnModel().getColumn(0).setPreferredWidth(200);
      getColumnModel().getColumn(0).setCellEditor(AttributeEditor.name(model.prefix()));
      getColumnModel().getColumn(1).setCellEditor(AttributeEditor.value());
      // attach the sort capabilities on this tabe
      model.sortTrigger(this);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: isCellEditable (overridden)
    /**
     ** Returns <code>true</code> if the cell coordinates pointing to the value
     ** cell of the model.
     **
     ** @param  row              the row being queried.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param  column           the column being queried.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     **
     ** @return                  <code>true</code> if the second column
     **                          (index = 1) being queried; otherwise
     **                          <code>false</code>
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    @Override
    public boolean isCellEditable(final int row, final int column) {
      return getModel().isCellEditable(row, column);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>AttributePanel</code> responsible to visualize
   ** the table UI of an attribute view that populates its data from the
   ** specified {@link Namespace}.
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
  private AttributePanel(final String path, final Namespace data) {
    // ensure inheritance
    super(path, data);

    // the MODEL that the AttributePanel use to populate specific components
    // comes from the Namespace passed to the constructor and accessed by the
    // specific path
    this.model = AttributeModel.build((Map<DirectoryAttribute, DirectoryValue>)this.data.get(this.path), true);

    final JTable view = new View(this.model);
    // register this component as a listener on the attribute grid data changes
    view.addPropertyChangeListener(this);

    // this time, we'll put the table in a scroll pane if the row size exceeds
    // the threshold of 25 entries
    add(new ComponentWithTitlebar<JComponent>(this.model.getRowCount() > AttributeCellRenderer.DEFAULT_ROW_THRESHOLD ? LayoutFactory.scrollableTable(view) : LayoutFactory.standaloneTable(view), new JLabel(Bundle.string(Bundle.ENTRY_ATTRIBUTE_PANEL_HEADER)), null));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   propertyChange (PropertyChangeListener)
  /**
   ** This method gets called when a bound property is changed.
   **
   ** @param  event              a {@link PropertyChangeEvent} object describing
   **                            the event source  and the property that has
   **                            changed.
   **                            <br>
   **                            Allowed object is {@link PropertyChangeEvent}.
   */
  @Override
  @SuppressWarnings("unchecked")
  public void propertyChange(final PropertyChangeEvent event) {
    // a cell has started/stopped editing
    if ("tableCellEditor".equals(event.getPropertyName())) {
      final View view = (View)event.getSource();
      if (!view.isEditing() && event.getOldValue() != null) {
        // code for editing stopped
        // TODO: this needs refactoring due to it doesn't use the schema hence
        //       it iterates over
        final AttributeEditor                         type   = (AttributeEditor)event.getOldValue();
        final String                                  name   = (String)type.getCellEditorValue();
        final Map<DirectoryAttribute, DirectoryValue> data   = (Map<DirectoryAttribute, DirectoryValue>)this.data.get(this.path);
        for (Map.Entry<DirectoryAttribute, DirectoryValue> cursor : data.entrySet()) {
          if (cursor.getKey().name.equals(name)) {
            final DirectoryValue value = data.get(cursor.getKey());
            // add a null item to the attribute values so that it can detected
            // as created 
            value.add(DirectoryValue.item(null, value.type.flag));
            break;
          }
        }
        updateView();
      }
    }
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
    this.model.update((Map<DirectoryAttribute, DirectoryValue>)this.data.get(this.path));
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
  // Method:   addListener
  /**
   ** Adds a listener to the table model that's notified each time a change to
   ** the data model occurs.
   **
   ** @param  listener           the {@link TableModelListener} to add.
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
   ** @param  listener           the {@link TableModelListener} to remove.
   **                            <br>
   **                            Allowed object is {@link TableModelListener}.
   */
  public void removeListener(final TableModelListener listener) {
    this.model.removeTableModelListener(listener);
  }
}