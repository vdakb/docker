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

    File        :   AttributeCollector.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    AttributeCollector.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.102 2023-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.editor.ods.panel.entry;

import javax.swing.JTable;

import oracle.jdeveloper.connection.iam.model.DirectoryValue;
import oracle.jdeveloper.connection.iam.model.DirectoryAttribute;

import oracle.jdeveloper.connection.iam.editor.ods.model.entry.AttributeModel;
import oracle.jdeveloper.connection.iam.editor.ods.model.entry.AttributeEditor;
import oracle.jdeveloper.connection.iam.editor.ods.model.entry.AttributeCellRenderer;

////////////////////////////////////////////////////////////////////////////////
// class AttributeCollector
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** An editor component suitable to collect attributes for an entry in a
 ** Directory Service in a table layout to be able to create or modify such
 ** attributes.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.102
 ** @since   12.2.1.3.42.60.102
 */
public class AttributeCollector extends JTable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-4085280728037065724")
  private static final long serialVersionUID = 1703284700176639804L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AttributeCollector</code> that populates its data to
   ** visualize from the specified {@link AttributeModel}.
   **
   ** @param  model              the collection of attributes collectect so far
   **                            for an entry of a Directory Service.
   **                            <br>
   **                            Changes on this collection have to propagated
   **                            by a <code>TableModelEvent</code>
   **                            <br>
   **                            Allowed object is {@link AttributeModel}.
   */
  private AttributeCollector(final AttributeModel model) {
    // ensure inheritance
    super(model);

    // this may be needed, depends on how fussy people get about the bottom of
    // letters like 'y' getting cut off when the cell is selected.
    setRowHeight(AttributeCellRenderer.DEFAULT_ROW_HEIGHT);
    // Set the renderer for the attribute type
    setDefaultRenderer(DirectoryAttribute.class,  AttributeCellRenderer.type());
    // Set the renderer for the attribute value
    setDefaultRenderer(DirectoryValue.Item.class, AttributeCellRenderer.item());
    // switch of reordering of the table columns at all
    getTableHeader().setReorderingAllowed(false);
    // adjust the geometry of some columns
    // first column is smaller
    getColumnModel().getColumn(0).setMaxWidth(200);
    getColumnModel().getColumn(0).setPreferredWidth(200);
    getColumnModel().getColumn(0).setCellEditor(AttributeEditor.name(model.prefix()));
    getColumnModel().getColumn(1).setCellEditor(AttributeEditor.value());
    // adjust the geometry of some columns; the one and only column is stretched
    setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods gouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>AttributeCollector</code> that populates
   ** its data to visualize from the specified {@link AttributeModel}.
   **
   ** @param  model              the collection of attributes collectect so far
   **                            for an entry of a Directory Service.
   **                            <br>
   **                            Changes on this collection have to propagated
   **                            by a <code>TableModelEvent</code>
   **                            <br>
   **                            Allowed object is {@link AttributeModel}.
   **
   ** @return                    the <code>AttributeCollector</code>.
   **                            <br>
   **                            Possible object
   **                            <code>AttributeCollector</code>.
   */
  public static AttributeCollector build(final AttributeModel model) {
    return new AttributeCollector(model);
  }
}