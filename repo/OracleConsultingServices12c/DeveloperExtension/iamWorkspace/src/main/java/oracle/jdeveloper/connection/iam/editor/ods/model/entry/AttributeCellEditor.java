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

    File        :   AttributeCellEditor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    AttributeCellEditor.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.102 2023-04-29  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.editor.ods.model.entry;

import java.util.Set;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.DefaultCellEditor;

import oracle.jdeveloper.workspace.iam.swing.Searchable;
import oracle.jdeveloper.workspace.iam.swing.widget.SuggestionComponent;
import oracle.jdeveloper.workspace.iam.swing.widget.SuggestionDecorator;

////////////////////////////////////////////////////////////////////////////////
// class AttributeCellEditor
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** A minimal extension of {@link DefaultCellEditor} allowing us to select an
 ** attribute prefix.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.102
 ** @since   12.2.1.3.42.60.102
 */
public class AttributeCellEditor extends DefaultCellEditor {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:9072627867225977790")
  private static final long serialVersionUID = -8455736695267856516L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AttributeCellEditor</code> that displays a
   ** {@link JTextField} as the cell editor.
   **
   ** @param  editor             the editor component to apply.
   **                            <br>
   **                            Allowed object is {@link JTextField}.
   ** @param  inventory          the collection of possible prefixes of
   **                            attributes.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   */
  private AttributeCellEditor(final JTextField editor, final Set<String> inventory) {
    // ensure inheritance
    super(editor);

    // decorate the editor with the model to filter and select elements
    SuggestionDecorator.decorate(editor, SuggestionComponent.<JTextField>literal(Searchable.literal(inventory)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>AttributeCellEditor</code> table cell
   ** editor responsible to visualize the possible names of a Directory Service
   ** schema attribute.
   **
   ** @param  inventory          a collection of string values.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the table cell editor to apply.
   **                            <br>
   **                            Possible object is {@link DefaultCellEditor}.
   */
  public static AttributeCellEditor build(final Set<String> inventory) {
    return new AttributeCellEditor(new JTextField(), inventory);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getTableCellEditorComponent (overridden)
  /**
   ** Sets an initial <code>value</code> for the editor.
   ** <br>
   ** This will cause the editor to <code>stopEditing</code> and lose any
   ** partially edited value if the editor is editing when this method is
   ** called.
   ** <p>
   ** Returns the component that should be added to the client's
   ** <code>Component</code> hierarchy. Once installed in the client's hierarchy
   ** this component will then be able to draw and receive user input.
   **
   ** @param  table              the {@link JTable} that is asking the editor to
   **                            edit; can be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link JTable}.
   ** @param  value              the value of the cell to be edited; it is up to
   **                            the specific editor to interpret and draw the
   **                            value.
   **                            <br>
   **                            Allowed object is {@link Object}.
   ** @param  selected           <code>true</code> if the cell is to be rendered
   **                            with highlighting.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param  row                the row of the cell being edited.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  col                the column of the cell being edited.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the component for editing.
   **                            <br>
   **                            Possible object is {@link Component}.
   */
  @Override
  public Component getTableCellEditorComponent(final JTable table, final Object value, final boolean selected, final int row, final int col) {
    // ensure inheritance
    return super.getTableCellEditorComponent(table, value, false, row, col);
  }
}