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

    File        :   SyntaxModel.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    SyntaxModel.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.editor.ods.model.schema;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import oracle.ide.util.Namespace;

import oracle.jdeveloper.connection.iam.model.DirectorySchema;

import oracle.jdeveloper.connection.iam.editor.ods.resource.Bundle;

import oracle.jdeveloper.connection.iam.editor.ods.model.DirectoryTableModel;

////////////////////////////////////////////////////////////////////////////////
// class SyntaxModel
// ~~~~~ ~~~~~~~~~~~
/**
 ** A list model suitable for the list of schema syntaxes of a directory
 ** service.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class SyntaxModel extends DirectoryTableModel {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final Class<?>[] CLASS            = {
    String.class
  , String.class
  };

  private static final String[]   HEADER           = {
    Bundle.string(Bundle.SCHEMA_SYNTAX_OID_HEADER)
  , Bundle.string(Bundle.SCHEMA_SYNTAX_DESCRIPTION_HEADER)
  };

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:5225680622322281228")
  private static final long              serialVersionUID = -2044648519813679431L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // the collection of Directory Service attributes
  transient List<DirectorySchema.Syntax> syntax;
  String[]                               oid;
  String[]                               description;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SyntaxModel</code> responsible to populate the table UI
   ** of syntax list view.
   **
   ** @param  syntax             the data populated from a Directory Service.
   */
  private SyntaxModel(final List<DirectorySchema.Syntax> syntax) {
    // ensure inheritance
    super(HEADER, CLASS);

    // initialize instance attributes
    this.syntax = syntax;
    refresh();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getRowCount (TableModel)
  /**
   ** Returns the number of rows in the table.
   **
   ** @return                   the number of rows in the table.
   **
   ** @see    AbstractTableModel#getRowCount
   */
  @Override
  public int getRowCount() {
    return this.oid.length;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getValueAt (TableModel)
  /**
   ** Returns the text for the given cell of the table.
   **
   ** @param  row              the cell row.
   ** @param  column           the cell column.
   **
   ** @see    AbstractTableModel#getValueAt
   */
  @Override
  public Object getValueAt(final int row, final int column) {
    switch(column) {
      case 0  : return this.oid[row];
      case 1  : return this.description[row];
      default : throw new IllegalArgumentException("Column not found");
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>SyntaxModel</code> object from the
   ** specified properties.
   ** <p>
   ** The string representation of the DN can be in RFC 2253 format.
   **
   ** @param  syntax             the {@link List} of syntax declarations
   **                            providing access to the context.
   **
   ** @return                    the validated <code>SyntaxModel</code>.
   **                            Possible object <code>SyntaxModel</code>.
   */
  public static SyntaxModel build(final List<DirectorySchema.Syntax> syntax) {
    return new SyntaxModel(syntax);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   update
  /**
   ** Updates an <code>SyntaxModel</code> object from the specified
   ** properties.
   ** <p>
   ** The string representation of the DN can be in RFC 2253 format.
   **
   ** @param  syntax             the collection of syntax declarations providing
   **                            access to the context.
   */
  public void update(final List<DirectorySchema.Syntax> syntax) {
    this.syntax = syntax;
    refresh();
    fireTableDataChanged();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revert
  /**
   ** Resets the <code>AttributeModel</code> to its original state by reverting
   ** all changes mad so far.
   */
  public void revert() {
//    this.syntax.revert();
    fireTableDataChanged();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refresh
  /**
   ** Populates the <code>AttributeModel</code> responsible to display the
   ** populate with labels and values of attribute types.
   */
  private void refresh() {
    final int size = this.syntax.size();
    // cleanup any previously populated values
    this.oid         = new String[size];
    this.description = new String[size];

    // build up the model from scratch
    for (int i = 0; i < size; i++) {
      this.oid[i]         = this.syntax.get(i).oid;
      this.description[i] = this.syntax.get(i).description;
    }
  }
}