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

    File        :   SyntaxPanel.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    SyntaxPanel.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.editor.ods.panel.schema;

import java.util.List;

import java.awt.Dimension;

import javax.swing.JTable;

import oracle.ide.util.Namespace;

import oracle.jdeveloper.workspace.iam.swing.LayoutFactory;

import oracle.jdeveloper.connection.iam.model.DirectorySchema;

import oracle.jdeveloper.connection.iam.editor.ods.panel.AbstractPanel;

import oracle.jdeveloper.connection.iam.editor.ods.model.schema.SyntaxModel;
import oracle.jdeveloper.connection.iam.editor.ods.model.schema.SchemaTableCellRenderer;

////////////////////////////////////////////////////////////////////////////////
// class SyntaxPanel
// ~~~~~ ~~~~~~~~~~~
/**
 ** A flat editor panel suitable to display syntaxes of a Directory Service
 ** schema in a table layout.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class SyntaxPanel extends AbstractPanel {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-2092750041740778811")
  private static final long serialVersionUID = -4655398181832671222L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final SyntaxModel model;

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
    @SuppressWarnings("compatibility:5688436314972975998")
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
     **                          Allowed object is {@link SyntaxModel}.
     */
    public Table(final SyntaxModel model) {
      // ensure inheritance
      super(model);
      // This may be needed, depends on how fussy people get about the bottom of
      // letters like 'y' getting cut off when the cell is selected.
      setRowHeight(SchemaTableCellRenderer.DEFAULT_ROW_HEIGHT);
      // set the renderer for the syntax
      setDefaultRenderer(String.class, new SchemaTableCellRenderer.Syntax());
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
   ** Constructs an empty <code>SyntaxPanel</code> responsible to visualize the
   ** table UI of an schema syntax view that populates its data from the
   ** specified {@link Namespace}.
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
  private SyntaxPanel(final String path, final Namespace data) {
    // ensure inheritance
    super(path, data);
    
    // the data that the SyntaxPanel use to populate specific components comes
    // from the Namespace passed to this constructor and accessed by the
    // specified path
    this.model = SyntaxModel.build((List<DirectorySchema.Syntax>)this.data.get(this.path));
    // the syntax table view is placed in the leftmost column of the grid
    // the control is stretched vertically and horizantal
    // this time, we'll put the table in a scroll pane
    add(LayoutFactory.scrollableTable(new Table(this.model), new Dimension(-1, SchemaTableCellRenderer.DEFAULT_ROW_THRESHOLD * SchemaTableCellRenderer.DEFAULT_ROW_HEIGHT)));
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
    this.model.update((List<DirectorySchema.Syntax>)this.data.get(this.path));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>SyntaxPanel</code> that populates its
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
   ** @return                    the validated <code>SyntaxPanel</code>.
   **                            <br>
   **                            Possible object <code>SyntaxPanel</code>.
   */
  public static SyntaxPanel build(final String path, final Namespace data) {
    return new SyntaxPanel(path, data);
  }
}