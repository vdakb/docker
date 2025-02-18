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

    File        :   AttributeEditor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    AttributeEditor.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.editor.ods.model.entry;

import java.awt.Component;

import java.util.Set;

import javax.swing.JTextField;
import javax.swing.DefaultCellEditor;

import javax.swing.JTable;

import oracle.jdeveloper.workspace.iam.swing.Searchable;

import oracle.jdeveloper.workspace.iam.swing.widget.SuggestionComponent;
import oracle.jdeveloper.workspace.iam.swing.widget.SuggestionDecorator;

////////////////////////////////////////////////////////////////////////////////
// class AttributeEditor
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** This base implementation defines the method any object that would like to be
 ** an editor of values for components such as <code>JList</code>,
 ** <code>JComboBox</code>, <code>JTree</code>, or <code>JTable</code> needs to
 ** implement.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class AttributeEditor extends DefaultCellEditor { //AbstractCellEditor

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-6884232522783447391")
  private static final long serialVersionUID = 6470377811475059116L;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Value
  // ~~~~~ ~~~~
  /**
   ** The table cell editor applicable on the name column.
   */
  private static class Name extends AttributeEditor {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:-6972517787454028354")
    private static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a name <code>AttributeEditor</code> that allows use as a
     ** JavaBean.
     **
     ** @param  component        the editor component.
     **                          <br>
     **                          Allowed object is {@link JTextField}.
     ** @param  inventory        the collection of possible object class names.
     **                          <br>
     **                          Allowed object is {@link Set} where each
     **                          element is of type {@link String}.
     */
    private Name(final JTextField component, final Set<String> inventory) {
      super(component);

      // decorate the editor with the model to filter and select elements
      SuggestionDecorator.decorate(component, SuggestionComponent.<JTextField>literal(Searchable.literal(inventory)));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getTableCellEditorComponent (overridden)
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
     ** @param  table            the {@link JTable} that is asking the editor to
     **                          edit; can be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link JTable}.
     ** @param  value            the value of the cell to be edited; it is up to
     **                          the specific editor to interpret and draw the
     **                          value.
     **                          <br>
     **                          Allowed object is {@link Object}.
     ** @param  selected         <code>true</code> if the cell is to be rendered
     **                          with highlighting.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     ** @param  row              the row of the cell being edited.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param  col              the column of the cell being edited.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     **
     ** @return                  the component for editing.
     **                          <br>
     **                          Possible object is {@link Component}.
     */
    @Override
    public Component getTableCellEditorComponent(final JTable table, final Object value, final boolean selected, final int row, final int col) {
      // ensure inheritance
      return super.getTableCellEditorComponent(table, value, false, row, col);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Value
  // ~~~~~ ~~~~
  /**
   ** The table cell editor applicable on the value column.
   */
  private static class Value extends AttributeEditor {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:2806582869859720413")
    private static final long serialVersionUID = 1L;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a value <code>AttributeEditor</code> that allows use as a
     ** JavaBean.
     **
     ** @param                   the editor component.
     **                          <br>
     **                          Allowed object is {@link JTextField}.
     */
    private Value(final JTextField component) {
      super(component);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>AttributeEditor</code> that allows use as a
   ** JavaBean.
   **
   ** @param component           the editor component.
   **                            <br>
   **                            Allowed object is {@link JTextField}.
   */
  protected AttributeEditor(final JTextField component) {
    // ensure inheritance
    super(component);

    // customize the editor to look pretty
    component.setBorder(null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Factory method to create a <code>AttributeEditor</code> for the attribute
   ** name cell.
   **
   ** @param  inventory          the collection of possible prefixes of
   **                            attributes.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the validated <code>AttributeEditor</code>.
   **                            Possible object
   **                            <code>AttributeEditor</code>.
   */
  public static AttributeEditor name(final Set<String> inventory) {
    final JTextField editor = new JTextField();
    return new Name(editor, inventory);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Factory method to create a <code>AttributeEditor</code> for the attribute
   ** value cell.
   **
   ** @return                    the validated <code>AttributeEditor</code>.
   **                            Possible object
   **                            <code>AttributeEditor</code>.
   */
  public static AttributeEditor value() {
    final JTextField editor = new JTextField();
    return new Value(editor);
  }
}