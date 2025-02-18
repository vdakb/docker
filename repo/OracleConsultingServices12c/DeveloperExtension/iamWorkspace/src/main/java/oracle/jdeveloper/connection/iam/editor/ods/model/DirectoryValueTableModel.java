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

    File        :   DirectoryValueTableModel.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    DirectoryValueTableModel.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.editor.ods.model;

import oracle.jdeveloper.connection.iam.model.DirectoryEntry;
import oracle.jdeveloper.connection.iam.model.DirectoryValue;

////////////////////////////////////////////////////////////////////////////////
// abstract class DirectoryValueTableModel
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** A table model suitable for the list of attributes of a Directory Service
 ** entry in a form.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class DirectoryValueTableModel extends DirectoryTableModel {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final Class<?>[] CLASS            = {String.class};

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-4982816604379330416")
  private static final long       serialVersionUID = 2986569134084338354L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // the collection of Directory Service attributes
  DirectoryValue                  entry;
  String[]                        value;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DirectoryValueTableModel</code> responsible to
   ** populate the table UI of value view.
   **
   ** @param  header             the label to display as the column header.
   ** @param  data               the data populated from a Directory Service.
   */
  private DirectoryValueTableModel(final String header, final DirectoryValue entry) {
    super(new String[]{header}, CLASS);

    // initialize instance attributes
    this.entry = entry;
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
   ** @see    DirectoryTableModel#getRowCount
   */
  @Override
  public int getRowCount() {
    return this.value.length;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getValueAt (TableModel)
  /**
   ** Returns the text for the given cell of the table.
   **
   ** @param  row              the cell row.
   ** @param  column           the cell column.
   **
   ** @see    DirectoryTableModel#getValueAt
   */
  @Override
  public Object getValueAt(final int row, final int column) {
    switch(column) {
      case 0  : return this.value[row];
      default : throw new IllegalArgumentException("Column not found");
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>DirectoryValueTableModel</code> object
   ** from the specified properties.
   ** <p>
   ** The string representation of the DN can be in RFC 2253 format.
   **
   ** @param  header             the label to display as the column header.
   ** @param  entry              the {@link DirectoryValue} providing access to
   **                            the context.
   **
   ** @return                    the validated
   **                            <code>DirectoryValueTableModel</code>.
   **                            Possible object
   **                            <code>DirectoryValueTableModel</code>.
   */
  public static DirectoryValueTableModel build(final String header, final DirectoryValue entry) {
    return new DirectoryValueTableModel(header, entry);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   update
  /**
   ** Updates an <code>AttributeModel</code> object from the specified properties.
   ** <p>
   ** The string representation of the DN can be in RFC 2253 format.
   **
   ** @param  entry              the {@link DirectoryEntry} providing access to
   **                            the context.
   */
  public void update(final DirectoryValue entry) {
    this.entry = entry;
    refresh();
    fireTableDataChanged();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refresh
  /**
   ** Populates the <code>AttributeModel</code> responsible to dispaly the
   ** populate with labels and values of attribute types.
   */
  private void refresh() {
    int size = this.entry.size();
    // cleanup any previously populated values
    this.value = new String[size];
    for (int i = 0; i < size; i++)
      this.value[i] = this.entry.get(i).toString();
  }
}