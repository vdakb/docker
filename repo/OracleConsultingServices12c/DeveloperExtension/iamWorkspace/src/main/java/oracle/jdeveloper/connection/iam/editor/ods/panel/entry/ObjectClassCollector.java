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

    File        :   ObjectClassCollector.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    ObjectClassCollector.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.102 2023-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.editor.ods.panel.entry;

import java.util.Set;
import java.util.Map;

import javax.swing.JTable;

import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;

import oracle.jdeveloper.connection.iam.model.DirectorySchema;

import oracle.jdeveloper.connection.iam.editor.ods.model.entry.ObjectClassModel;
import oracle.jdeveloper.connection.iam.editor.ods.model.entry.ObjectClassCellEditor;
import oracle.jdeveloper.connection.iam.editor.ods.model.entry.ObjectClassCellRenderer;

////////////////////////////////////////////////////////////////////////////////
// class ObjectClassCollector
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** An editor component suitable to collect object classes for an entry in a
 ** Directory Service in a table layout to be able to create or modify such
 ** attributes.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.102
 ** @since   12.2.1.3.42.60.102
 */
public class ObjectClassCollector extends JTable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:7997775748850337543")
  private static final long serialVersionUID = 9176051867702802003L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ObjectClassCollector</code> that populates its
   ** available data to visualize from the specified {@link ObjectClassModel} as
   ** part of the {@link DirectorySchema}.
   **
   ** @param  selection          the names of selected object classes from an
   **                            entry of a Directory Service.
   **                            <br>
   **                            Allowed object is {@link ObjectClassModel}.
   ** @param  editor             the collection of object classes populated from
   **                            a Directory Service backing this view.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   */
  private ObjectClassCollector(final ObjectClassModel selection, ObjectClassCellEditor editor) {
    // ensure inheritance
    super(selection);

    // this may be needed, depends on how fussy people get about the bottom of
    // letters like 'y' getting cut off when the cell is selected.
    setRowHeight(ObjectClassCellRenderer.DEFAULT_ROW_HEIGHT);
    // set the renderer for the object class
    setDefaultRenderer(String.class, ObjectClassCellRenderer.build());
    // set the editor for the object class
    setDefaultEditor(String.class, editor);
    // switch of reordering of the table columns at all
    getTableHeader().setReorderingAllowed(false);
    // adjust the geometry of some columns; the one and only column is stretched
    setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

    setColumnSelectionAllowed(false);
    setFillsViewportHeight(true);
    setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    // set the column header to null to avoid rendering of the header in a
    // scroll pane
    setTableHeader(null);

    // mke the table editable on every click
    getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "startEditing");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods gouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>ObjectClassCollector</code> that
   ** populates its data to visualize from the specified {@link Map} as part of
   ** the {@link DirectorySchema}.
   **
   ** @param  selection          the names of selected object classes from an
   **                            entry of a Directory Service.
   **                            <br>
   **                            Allowed object is {@link ObjectClassModel}.
   ** @param  editor             the collection of object classes populated from
   **                            a Directory Service backing this view.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the <code>ObjectClassCollector</code>.
   **                            <br>
   **                            Possible object
   **                            <code>ObjectClassCollector</code>.
   */
  public static ObjectClassCollector build(final ObjectClassModel selection, final ObjectClassCellEditor editor) {
    return new ObjectClassCollector(selection, editor);
  }
}