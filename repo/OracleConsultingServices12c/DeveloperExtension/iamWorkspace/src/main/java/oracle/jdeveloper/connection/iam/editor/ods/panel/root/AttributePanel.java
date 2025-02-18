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

package oracle.jdeveloper.connection.iam.editor.ods.panel.root;

import java.awt.Dimension;

import javax.swing.JTable;

import oracle.ide.util.Namespace;

import oracle.jdeveloper.workspace.iam.swing.LayoutFactory;

import oracle.jdeveloper.connection.iam.model.DirectoryValue;

import oracle.jdeveloper.connection.iam.editor.ods.panel.AbstractPanel;

import oracle.jdeveloper.connection.iam.editor.ods.model.DirectoryValueTableModel;
import oracle.jdeveloper.connection.iam.editor.ods.model.DirectoryTableCellRenderer;

////////////////////////////////////////////////////////////////////////////////
// class AttributePanel
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** A flat editor panel suitable to display attributes of a Directory Service
 ** DSE in a table layout.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class AttributePanel extends AbstractPanel {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-5672538361980066248")
  private static final long serialVersionUID = 5154530329008070963L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final DirectoryValueTableModel model;

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
  public static class View extends JTable {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:6924230428859884733")
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
     **                          Allowed object is
     **                          {@link DirectoryValueTableModel}.
     */
    public View(final DirectoryValueTableModel model) {
      // ensure inheritance
      super(model);

      // This may be needed, depends on how fussy people get about the bottom of
      // letters like 'y' getting cut off when the cell is selected.
      setRowHeight(DirectoryTableCellRenderer.DEFAULT_ROW_HEIGHT);
      // Set the renderer for the value
      setDefaultRenderer(String.class, new DirectoryTableCellRenderer());
      // switch of reordering of the table columns at all
      getTableHeader().setReorderingAllowed(false);
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
   ** Constructs an empty <code>AttributePanel</code> responsible to visualize
   ** the table UI of an attribute view that populates its data from the
   ** specified {@link List}.
   **
   ** @param  header             the string to display as the column name.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  path               the name path whose associated value is to be
   **                            handled.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  data               the {@link Namespace} providing access to the
   **                            context.
   **                            <br>
   **                            Allowed object is {@link Namespace}.
   */
  private AttributePanel(final String header, final String path, final Namespace data) {
    // ensure inheritance
    super(path, data);

    // the data that the AttributePanel use to populate specific components
    // comes from the Namespace passed to this constructor and accessed by the
    // specified path
    this.model = DirectoryValueTableModel.build(header, (DirectoryValue)this.data.get(this.path));
    // create the view responsible to visualize the general information about
    // the directory entry
    final JTable t = new View(this.model);
    // this time, we'll put the table in a scroll pane if the row size exceeds
    // the threshold of 20 entries.
    if (this.model.getRowCount() > DirectoryTableCellRenderer.DEFAULT_ROW_THRESHOLD) {
      // this time, we'll put the table in a scroll pane if the row size exceeds
      // the threshold of 20 entries.
      add(LayoutFactory.scrollableTable(t, new Dimension(-1, DirectoryTableCellRenderer.DEFAULT_ROW_THRESHOLD * DirectoryTableCellRenderer.DEFAULT_ROW_HEIGHT)));
    }
    else {
      add(LayoutFactory.standaloneTable(t));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateView (AbstractPanel)
  /**
   ** The data that the <code>TransparentPanel</code> should use to populate
   ** specific components comes from the {@link Namespace} passed to this page.
   */
  @Override
  public void updateView() {
    this.model.update((DirectoryValue)this.data.get(this.path));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>AttributePanel</code> that
   ** populates its data from the specified {@link DirectoryValue}.
   ** <p>
   ** The string representation of the DN can be in RFC 2253 format.
   **
   ** @param  header             the string to display as the column name.
   **                            <br>
   **                            Allowed object is {@link String}.
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
  public static AttributePanel build(final String header, final String path, final Namespace data) {
    return new AttributePanel(header, path, data);
  }
}