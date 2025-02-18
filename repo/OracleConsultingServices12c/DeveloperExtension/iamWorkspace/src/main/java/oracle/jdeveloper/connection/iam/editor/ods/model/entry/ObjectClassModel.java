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

    File        :   ObjectClassModel.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    ObjectClassModel.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.102 2023-04-29  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.editor.ods.model.entry;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashSet;

import java.util.stream.Collectors;

import oracle.jdeveloper.connection.iam.editor.ods.resource.Bundle;

import oracle.jdeveloper.connection.iam.editor.ods.model.DirectoryTableModel;

////////////////////////////////////////////////////////////////////////////////
// class ObjectClassModel
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** A table model suitable for the list of object classes of a Directory Service
 ** entry in a form.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.102
 ** @since   12.2.1.3.42.60.102
 */
public class ObjectClassModel extends DirectoryTableModel {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the string to display in the table for extensibility
  private static final String      NEW            = Bundle.string(Bundle.ENTRY_EXTENSIBLE_LABEL);

  private static final Class<?>[] CLASS           = {
    String.class
  };

  private static final String[]   HEADER          = {
    Bundle.string(Bundle.ENTRY_OBJECT_LABEL)
  };

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-4871387590264278512")
  private static final long      serialVersionUID = 7574293619516630231L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // the collection of selected Directory Service object classes
  private List<String>           selection;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>ObjectClassModel</code> responsible to
   ** populate the table UI of selected object class view.
   ** <br>
   ** Argument <code>selection</code> specifies the the names of object classes
   ** which are selected.
   **
   ** @param  selection          the names of selected object classes from a
   **                            Directory Service.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   */
  private ObjectClassModel(final Set<String> selection) {
    // ensure inheritance
    super(HEADER, CLASS);

    // initialize instance attributes
    this.selection = extend(selection, null);
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
    return true;
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
    if (column == 0) {
      // if an already selected object class is changed set the value
      if (row < this.selection.size() - 1) {
        // first notify about the old value has to be removed
        fireTableRowsDeleted(row, row);
        this.selection.set(row, value.toString());
        // than notify about the value that subtitute the former value
        fireTableRowsUpdated(row, row);
      }
      else {
        this.selection = extend(this.selection, value.toString());
        fireTableRowsInserted(row, row);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   selection
  /**
   ** Returns the collection of selected object classes.
   ** <p>
   ** The symbolic entry that's used to select a new object class to include in
   ** the collection of selected object classes is exclude in the
   ** returned collection.
   **
   ** @return                    the collection of selected object classes.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element of of type {@link String}.
   */
  public List<String> selection() {
    return this.selection.subList(0, this.selection.size() -1);
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
    return this.selection.size();
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
      case 0  : return this.selection.get(row);
      default : throw new IllegalArgumentException("Column not found");
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>ObjectClassModel</code> object from the
   ** specified properties.
   **
   ** @param  selection          the names of selected object classes from a
   **                            Directory Service.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the validated <code>ObjectClassModel</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>ObjectClassModel</code>.
   */
  public static ObjectClassModel build(final Set<String> selection) {
    return new ObjectClassModel(selection);
  }

  private static List<String> extend(final Collection<String> source, final String value) {
    if (value != null)
      source.add(value);

    List<String> result = null;
    if (source == null) {
      result = new ArrayList<String>();
    }
    else {
      final LinkedHashSet<String> ordered = source.stream()
      .filter(e -> !e.equals(NEW))
      // sort while streaming
      .sorted(Comparator.comparing(e -> e))
      // collecting to a set that maintains the order
      .collect(Collectors.toCollection(LinkedHashSet::new))
      ;
      result = ordered.stream().collect(Collectors.toList());
    }
    result.add(NEW);
    return result;
  }
}