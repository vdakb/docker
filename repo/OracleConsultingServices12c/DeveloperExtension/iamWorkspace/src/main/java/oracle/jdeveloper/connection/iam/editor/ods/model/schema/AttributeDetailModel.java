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

    File        :   AttributeDetailModel.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    AttributeDetailModel.


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
// class AttributeDetailModel
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** A table model suitable for a particular attribute type of a directory
 ** service entry in a table.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class AttributeDetailModel extends AbstractTableModel {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final Class<?>[] CLASS           = {
    String.class
  , String.class
  };

  private static final String[] HEADER           = {
    Bundle.string(Bundle.SCHEMA_ATTRIBUTE_LABEL_HEADER)
  , Bundle.string(Bundle.SCHEMA_ATTRIBUTE_VALUE_HEADER)
  };

  private static final String[] BOOLEAN          = {
    Bundle.string(Bundle.SCHEMA_ATTRIBUTE_BOOLEAN_TRUE)
  , Bundle.string(Bundle.SCHEMA_ATTRIBUTE_BOOLEAN_FALSE)
  };

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-7561485779081378153")
  private static final long     serialVersionUID = -2847955518774853363L;

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
   ** Constructs an empty <code>AttributeDetailModel</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   ** <p>
   ** This constructor is private to prevent other classes to use
   ** "new AttributeDetailModel()".
   */
  private AttributeDetailModel() {
    // ensure inheritance
    super();

    this.label.add(DirectorySchema.Attribute.Name.NUMERICOID.label);
    this.label.add(DirectorySchema.Attribute.Name.NAME.label);
    this.label.add(DirectorySchema.Attribute.Name.DESC.label);
    this.label.add(DirectorySchema.Attribute.Name.SYNTAX.label);
    this.label.add(DirectorySchema.Attribute.Name.EQUALITY.label);
    this.label.add(DirectorySchema.Attribute.Name.ORDERING.label);
    this.label.add(DirectorySchema.Attribute.Name.SUBSTRING.label);
    this.label.add(DirectorySchema.Attribute.Name.SUP.label);

    this.label.add(Bundle.string(Bundle.SCHEMA_ATTRIBUTE_MULTIVALUED_LABEL));
    this.label.add(Bundle.string(Bundle.SCHEMA_ATTRIBUTE_COLLECTIVE_LABEL));
    this.label.add(Bundle.string(Bundle.SCHEMA_ATTRIBUTE_OBSOLETE_LABEL));
    this.label.add(Bundle.string(Bundle.SCHEMA_ATTRIBUTE_OPERATIONAL_LABEL));
    this.label.add(Bundle.string(Bundle.SCHEMA_ATTRIBUTE_READONLY_LABEL));
    this.label.add(Bundle.string(Bundle.SCHEMA_ATTRIBUTE_CREATEABLE_LABEL));
    this.label.add(Bundle.string(Bundle.SCHEMA_ATTRIBUTE_MODIFYABLE_LABEL));
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
   ** Factory method to create an empty <code>AttributeDetailModel</code> that
   ** allows use as a JavaBean.
   **
   ** @return                    the validated
   **                            <code>AttributeDetailModel</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>AttributeDetailModel</code>.
   */
  public static AttributeDetailModel build() {
    return new AttributeDetailModel();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refresh
  /**
   ** Populates an <code>AttributeDetailModel</code> responsible to display the
   ** populate with labels and values of attribute types.
   **
   ** @param  data               the data mapping providing access to the
   **                            context.
   **                            <br>
   **                            Allowed object is {@link EnumMap} where each
   **                            elements is of type
   **                            {@link DirectorySchema.Attribute} and
   **                            {@link Object} as the value.
   */
  public void refresh(final DirectorySchema.Attribute data) {
    // cleanup any previously populated values
    this.value.clear();
    // avoid confusing users by ensuring the same sequnce in adding attribute
    // values as the label create in the constructor
    this.value.add(data.oid         == null ? "<empty>" : data.oid);
    this.value.add(data.name        == null ? "<empty>" : data.name);
    this.value.add(data.description == null ? "<empty>" : data.description);
    this.value.add(data.syntax      == null ? "<empty>" : data.syntax);
    this.value.add(data.equality    == null ? "<empty>" : data.equality);
    this.value.add(data.ordering    == null ? "<empty>" : data.ordering);
    this.value.add(data.substring   == null ? "<empty>" : data.substring);
    this.value.add(data.superior    == null ? "<empty>" : data.superior.toString());

    this.value.add(data.multiValued() ? BOOLEAN[0] : BOOLEAN[1]);
    this.value.add(data.collective()  ? BOOLEAN[0] : BOOLEAN[1]);
    this.value.add(data.obsolete()    ? BOOLEAN[0] : BOOLEAN[1]);
    this.value.add(data.operational() ? BOOLEAN[0] : BOOLEAN[1]);
    this.value.add(data.readonly()    ? BOOLEAN[0] : BOOLEAN[1]);
    this.value.add(data.createable()  ? BOOLEAN[0] : BOOLEAN[1]);
    this.value.add(data.updateable()  ? BOOLEAN[0] : BOOLEAN[1]);

    // notify all listeners that all cell values in the table's rows may have
    // changed
    fireTableDataChanged();
  }
}