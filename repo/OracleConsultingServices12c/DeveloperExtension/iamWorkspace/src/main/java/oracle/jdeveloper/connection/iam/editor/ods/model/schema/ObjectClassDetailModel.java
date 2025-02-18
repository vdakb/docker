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

    File        :   ObjectClassDetailModel.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    ObjectClassDetailModel.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.editor.ods.model.schema;

import java.util.List;
import java.util.EnumMap;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import oracle.jdeveloper.connection.iam.model.DirectorySchema;

import oracle.jdeveloper.connection.iam.editor.ods.resource.Bundle;

////////////////////////////////////////////////////////////////////////////////
// class ObjectClassDetailModel
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** A table model suitable for a particular object class of a Directory Service
 ** entry in a table.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class ObjectClassDetailModel extends AbstractTableModel {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final Class<?>[] CLASS           = {
    String.class
  , String.class
  };

  private static final String[] HEADER           = {
    Bundle.string(Bundle.SCHEMA_OBJECTCLASS_LABEL_HEADER)
  , Bundle.string(Bundle.SCHEMA_OBJECTCLASS_VALUE_HEADER)
  };

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:5404826828521902732")
  private static final long     serialVersionUID = -6134411160168532196L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // the collection of Directory Service attribute type property names
  final List<String>            label            = new ArrayList<>();
  final List<String>            value            = new ArrayList<>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>ObjectClassDetailModel</code> that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   ** <p>
   ** This constructor is private to prevent other classes to use
   ** "new ObjectClassDetailModel()".
   */
  private ObjectClassDetailModel() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getColumnClass (overridden)
  /**
   ** Returns the class of the objects in the different columns:
   ** DirectoryAttribute.class for column 0, DirectoryValue.class for col 1.
   **
   **
   ** @param  column             the column being queried.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the column java class.
   **                            <br>
   **                            Possible object is {@link Class}.
   */
  @Override
  public Class getColumnClass(final int column) {
    return CLASS[column];
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getRowCount (TableModel)
  /**
   ** Returns the number of rows in the table.
   **
   ** @return                    the number of rows in the table.
   **                            <br>
   **                            Possible object is <code>int</code>.
   **
   ** @see    AbstractTableModel#getRowCount
   */
  @Override
  public int getRowCount() {
    return this.label.size();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getColumnCount (TableModel)
  /**
   ** Returns the number of column in the table
   **
   ** @return                    the number of columns in the table
   **                            <br>
   **                            Possible object is <code>int</code>.
   **
   ** @see    AbstractTableModel#getColumnCount
   */
  @Override
  public int getColumnCount() {
    // 2 columns: "Label" and "Value"
    return HEADER.length;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getColumnName (TableModel)
  /**
   ** Returns the title for this column.
   **
   ** @param  column             the column being queried.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the title for this column.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @see    AbstractTableModel#getColumnName
   */
  @Override
  public String getColumnName(final int column) {
    return HEADER[column];
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getValueAt (TableModel)
  /**
   ** Returns the text for the given cell of the table.
   **
   ** @param  row                the cell row.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  column             the cell column.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the alue object for this column.
   **                            <br>
   **                            Possible object is {@link Object}.
   **
   ** @see    AbstractTableModel#getValueAt
   */
  @Override
  public Object getValueAt(final int row, final int column) {
    switch(column) {
      case 0  : return this.label.get(row);
      case 1  : return this.value.get(row).toString();
      default : throw new IllegalArgumentException("Column not found");
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>ObjectClassDetailModel</code> object
   ** from the specified properties.
   **
   ** @return                    the validated
   **                            <code>ObjectClassDetailModel</code>.
   **                            Possible object is
   **                            <code>ObjectClassDetailModel</code>.
   */
  public static ObjectClassDetailModel build() {
    return new ObjectClassDetailModel();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refresh
  /**
   ** Populates an <code>ObjectClassDetailModel</code> responsible to display
   ** the populate with labels and values of attribute types.
   **
   ** @param  data               the data mapping providing access to the
   **                            context.
   **                            <br>
   **                            Allowed object is {@link EnumMap} where each
   **                            elements is of type
   **                            {@link DirectorySchema.Attribute} and
   **                            {@link Object} as the value.
   */
  public void refresh(final DirectorySchema.ObjectClass data) {
    // cleanup any previously populated values
    this.label.clear();
    this.value.clear();
    singleValue(DirectorySchema.ObjectClass.Name.NAME,       data.name);
    singleValue(DirectorySchema.ObjectClass.Name.DESC,       data.description);
    singleValue(DirectorySchema.ObjectClass.Name.KIND,       data.kind.label);
    singleValue(DirectorySchema.ObjectClass.Name.NUMERICOID, data.oid);
    // notify all listeners that all cell values in the table's rows may have
    // changed
    fireTableDataChanged();
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   singleValue
  /**
   ** Obtains the value collection mapped at <code>property</code> from the
   ** given {@link EnumMap} <code>data</code> and the <code>label</code> and
   ** <code>value</code> instance attributes.
   **
   ** @param  property           the property key to get the value collection
   **                            from <code>data</code>.
   **                            <br>
   **                            Allowed object is 
   **                            {@link DirectorySchema.ObjectClass.Name}.
   ** @param  data               the data mapping providing access to the
   **                            context.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  private void singleValue(final DirectorySchema.ObjectClass.Name property, final String content) {
    this.label.add(property.label);
    this.value.add(content == null ? "<empty>" : content.toString());
  }
}