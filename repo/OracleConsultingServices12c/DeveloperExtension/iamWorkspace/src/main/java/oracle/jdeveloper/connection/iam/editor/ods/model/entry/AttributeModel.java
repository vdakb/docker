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

    File        :   AttributeModel.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    AttributeModel.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.editor.ods.model.entry;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;

import java.util.stream.Collectors;

import javax.naming.directory.Attribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.ModificationItem;

import oracle.jdeveloper.connection.iam.model.DirectoryValue;
import oracle.jdeveloper.connection.iam.model.DirectoryAttribute;

import oracle.jdeveloper.connection.iam.editor.ods.resource.Bundle;

import oracle.jdeveloper.connection.iam.editor.ods.model.DirectoryTableModel;

////////////////////////////////////////////////////////////////////////////////
// class AttributeModel
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** A table model suitable for the list of attributes of a Directory Service
 ** entry in a form.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class AttributeModel extends DirectoryTableModel {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the string to display in the table for extensibility
  private static final String      NEW       = Bundle.string(Bundle.ENTRY_EXTENSIBLE_LABEL);

  private static final Class<?>[] CLASS      = {
    DirectoryAttribute.class
  , DirectoryValue.Item.class
  };

  private static final String[]   HEADER     = {
    Bundle.string(Bundle.ENTRY_ATTRIBUTE_NAME_HEADER)
  , Bundle.string(Bundle.ENTRY_ATTRIBUTE_VALUE_HEADER)
  };

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:857734378385358949")
  private static final long serialVersionUID = 5496806121785099567L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final Set<String>                             prefix = new HashSet<String>();

  // the collection of Directory Service attributes that belongs to a specific
  // page
  final boolean                                 extensible;
  final Map<DirectoryAttribute, DirectoryValue> segment;

  DirectoryAttribute[]                          value;
  DirectoryValue.Item[]                         item;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>AttributeModel</code> responsible to
   ** populate the table UI of entry attribute view.
   **
   ** @param  segment            the entry attributes populated from a Directory
   **                            Service.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link DirectoryAttribute}
   **                            for the key and {@link DirectoryValue} as the
   **                            value.
   ** @param  extensible         <code>true</code> if table rows can be added on
   **                            demand.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
  */
  private AttributeModel(final Map<DirectoryAttribute, DirectoryValue> segment, final boolean extensible) {
    // ensure inheritance
    super(HEADER, CLASS);

    // initialize instance attributes
    this.segment    = segment;
    this.extensible = extensible;
    refresh(this.segment);
 }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isCellEditable (overridden)
  /**
   ** Returns <code>true</code> if the cell coordinates pointing to the value
   ** cell of the model.
   **
   ** @param  row                the row being queried.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  column             the column being queried.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    <code>true</code> if the second column
   **                            (index = 1) being queried; otherwise
   **                            <code>false</code>
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  public boolean isCellEditable(final int row, final int column) {
    return (this.extensible && (column == 0 && this.value[row].is("<<new>>")) || (column == 1 && (!(this.value[row].readonly() || this.value[row].objectClass()))));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setValueAt (overridden)
  /**
   ** Allows a cell value to be set (should only ever be the second column,
   ** column=1).
   **
   ** @param  value              the value to assign to cell.
   **                            <br>
   **                            Allowed object is {@link Object}.
   ** @param  row                the row of cell.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  column             the column of cell.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  @Override
  public void setValueAt(final Object value, final int row, final int column) {
    // which it jolly well should...
    if (column == 1) {
      if (this.item[row].changed(value)) {
        this.item[row].update(value);
        fireTableCellUpdated(row, column);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prefix
  /**
   ** Returns the available attribute prefixes.
   **
   ** @return                    the available attribute prefixes.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link String}.
   */
  public final Set<String> prefix() {
    return this.prefix;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   changes
  /**
   ** Returns the modified rows as a collection of {@link ModificationItem}.
   **
   ** @return                    the modified rows as a collection of
   **                            {@link ModificationItem}.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link ModificationItem}.
   */
  public final List<ModificationItem> changes() {
    final List<ModificationItem> mod = new ArrayList<ModificationItem>();
    for (int row = 0; row < this.item.length; row++) {
      if (this.item[row].created()) {
        final Attribute a = new BasicAttribute(this.value[row].name);
        a.add(this.item[row].value());
        final ModificationItem item = new ModificationItem(DirContext.ADD_ATTRIBUTE, a);
        mod.add(item);
      }
      else if (this.item[row].changed()) {
        final Attribute a = new BasicAttribute(this.value[row].name);
        a.add(this.item[row].value());
        final ModificationItem item = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, a);
        mod.add(item);
      }
    }
    return mod;
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
   ** @param  row                the cell row.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  column             the cell column.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the text for the given cell of the table.
   **                            <br>
   **                            Possible object is {@link Object}.
   **
   ** @see    DirectoryTableModel#getValueAt
   */
  @Override
  public Object getValueAt(final int row, final int column) {
    switch(column) {
      case 0  : return this.value[row];
      case 1  : return this.item[row];
      default : throw new IllegalArgumentException("Column not found");
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>AttributeModel</code> object from the
   ** specified properties.
   ** <p>
   ** The string representation of the DN can be in RFC 2253 format.
   **
   ** @param  segment            the entry attributes to refresh.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link DirectoryAttribute}
   **                            for the key and {@link DirectoryValue} as the
   **                            value.
   ** @param  extensible         <code>true</code> if table rows can be added on
   **                            demand.
   **                            <br>
   **                            Allowed object is <code>booleac</code>.
   **
   ** @return                    the validated <code>AttributeModel</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>AttributeModel</code>.
   */
  public static AttributeModel build(final Map<DirectoryAttribute, DirectoryValue> segment, final boolean extensible) {
    return new AttributeModel(segment, extensible);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   update
  /**
   ** Updates an <code>AttributeModel</code> object from the specified
   ** properties.
   ** <p>
   ** The string representation of the DN can be in RFC 2253 format.
   **
   ** @param  segment            the entry attributes to refresh.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link DirectoryAttribute}
   **                            for the key and {@link DirectoryValue} as the
   **                            value.
   */
  public void update(final Map<DirectoryAttribute, DirectoryValue> segment) {
    refresh(segment);
    fireTableDataChanged();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revert
  /**
   ** Resets the <code>AttributeModel</code> to its original state by reverting
   ** all changes mad so far.
   */
  public void revert() {
    this.segment.values().forEach(DirectoryValue::revert);
    fireTableDataChanged();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refresh
  /**
   ** Populates the <code>AttributeModel</code> responsible to display the
   ** populate with labels and values of attribute types.
   **
   ** @param  entry              the entry attributes to refresh.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link DirectoryAttribute}
   **                            for the key and {@link DirectoryValue} as the
   **                            value.
   */
  private void refresh(final Map<DirectoryAttribute, DirectoryValue> entry) {
    // cleanup any previously populated values
    this.prefix.clear();
    this.value = new DirectoryAttribute[entry.size()];
    this.item  = new DirectoryValue.Item[entry.size()];

    if (entry.size() > 0) {
      this.prefix.addAll(entry.keySet().stream().filter(DirectoryAttribute::multiValued).map(e -> e.name).collect(Collectors.toSet()));

      // if the entry is new no value might be avalaible at the time the model
      // is refreshed therefor at least one row have to be created for any
      // attribute name
      final int size = Math.max(entry.size(), entry.values().stream().mapToInt(e -> e.size()).sum());
      this.value = new DirectoryAttribute [size + (this.extensible ? 1 : 0)];
      this.item  = new DirectoryValue.Item[size + (this.extensible ? 1 : 0)];

      // build up the model from scratch
      int k = 0;
      for (DirectoryValue cursor : entry.values()) {
        // if the value doesn't have an item assign one
        if (cursor.size() == 0) {
          cursor.add(DirectoryValue.item(null, cursor.type.flag));
        }
        for (int i = 0; i < cursor.size(); i++) {
          this.value[k] = cursor.type;
          this.item[k]  = cursor.get(i);
          k++;
        }
      }
      if (this.extensible) {
        // add a dummy row to allow extensibility of the attributes
        this.value[k] = DirectoryAttribute.build(NEW);
        this.item[k]  = DirectoryValue.build(this.value[k], "").get(0);
      }
    }
  }
}