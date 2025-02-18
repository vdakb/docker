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

    Copyright © 2016. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Frontend Extension
    Subsystem   :   System Provisioning Management

    File        :   Picker.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Picker.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2016-03-03  DSteding    First release version
*/

package oracle.iam.identity.sysprov.svr.state;

import java.util.List;
import java.util.Iterator;
import java.util.HashMap;
import java.util.ArrayList;

import javax.faces.event.ActionEvent;

import oracle.jbo.Row;

import org.apache.myfaces.trinidad.event.SelectionEvent;

import org.apache.myfaces.trinidad.model.RowKeySet;

import oracle.adf.view.rich.component.rich.data.RichTable;

import oracle.iam.ui.platform.utils.TaskFlowUtils;

import oracle.iam.ui.platform.view.event.TargetSelectionEvent;

import oracle.hst.foundation.faces.JSF;

import oracle.iam.identity.frontend.AbstractPickerState;

import oracle.iam.identity.sysprov.schema.EndpointAdapter;

////////////////////////////////////////////////////////////////////////////////
// class Picker
// ~~~~~ ~~~~~~
/**
 ** Declares methods the user interface service provides to search and select
 ** <code>IT Resource</code>s.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
 */
public class Picker extends AbstractPickerState {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-8364524774315643749")
  private static final long     serialVersionUID = -8485737011433709688L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private List<EndpointAdapter> selectedItems    = new ArrayList<EndpointAdapter>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Picker</code> backing bean that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Picker() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getSelectedItems
  /**
   ** Returns the collection of selected items.
   **
   ** @return                    the collection of selected items.
   */
  public List<EndpointAdapter> getSelectedItems() {
    return this.selectedItems;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   selectionEmpty
  /**
   ** Determines if no item is in the select collection.
   **
   ** @return                    <code>true</code> if no item is contained in
   **                            the collection of selected items; otherwise
   **                            <code>false</code>.
   */
  public boolean isSelectionEmpty() {
    return getSelectedItems().size() == 0;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base calsses
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   selectionListener (AbstractPickerState)
  /**
   ** Handles the event if the selection in the table of available entries
   ** becomes to change.
   **
   ** @param  event              the {@link SelectionEvent} delivered from the
   **                            action occured in a UIXTable component.
   */
  @Override
  public void selectionListener(final SelectionEvent event) {
    refreshSourceTargets();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   addSelectedRows (AbstractPickerState)
  /**
   ** Adds the selected entries to the result which needs to be returned to
   ** the invoking taskflow handler for further processing.
   **
   ** @return                   the action name the embedding task flow requires
   **                           to handle.
   */
  @Override
  public String addSelectedRows() {
    final List selection = getSelectedItems();
    if (selection.size() == 0) {
      return "error";
    }
    // send back the value for which reason this picker was used
    final HashMap<String, Object> parameter = new HashMap<String, Object>();
    parameter.put("eventReason", JSF.valueFromExpression("#{pageFlowScope.eventReason}", String.class));
    TaskFlowUtils.raiseEvent("return", new TargetSelectionEvent(JSF.valueFromExpression("#{pageFlowScope.eventDistinguisher}", String.class), selection, parameter));
    return SUCCESS;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addAllActionListener (AbstractPickerState)
  /**
   ** Add the all rows in the search table to the selection.
   **
   ** @param  event              the {@link ActionEvent} delivered from the
   **                            action occured in a UIXForm component.
   */
  @Override
  public void addAllActionListener(final ActionEvent event) {
    final List<Row> selection = iteratorAllRow("EndpointIterator");
    addSelection(selection);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeSelectedActionListener (AbstractPickerState)
  /**
   ** Remove the selected rows from the selection table.
   **
   ** @param  event              the {@link ActionEvent} delivered from the
   **                            action occured in a UIXForm component.
   */
  @Override
  public void removeSelectedActionListener(final ActionEvent event) {
    final RichTable rtt  = getSelectionTable();
    final RowKeySet keys = rtt.getSelectedRowKeys();
    final RowKeySet olds = keys.clone();
    final Object    old  = rtt.getRowKey();

    List<EndpointAdapter> removeRows = new ArrayList<EndpointAdapter>();
    for (Iterator<Object> i = keys.iterator(); i.hasNext(); ) {
      Object key = i.next();
      rtt.setRowKey(key);
      EndpointAdapter rowData = (EndpointAdapter)rtt.getRowData();
      removeRows.add(rowData);
    }
    this.selectedItems.removeAll(removeRows);
    resetSelectedRowKey(rtt);
    rtt.setSelectedRowKeys(olds);
    rtt.setRowKey(old);
    refreshSelectedTargets();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeAllActionListener (AbstractPickerState)
  /**
   ** Remove the all rows from the selection table.
   **
   ** @param  event              the {@link ActionEvent} delivered from the
   **                            action occured in a UIXForm component.
   */
  @Override
  public void removeAllActionListener(final ActionEvent event) {
    this.selectedItems.clear();
    resetSelectedRowKey(getSelectionTable());
    refreshSelectedTargets();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addSelection (AbstractPickerState)
  /**
   ** Add the provided collection of {@link Row}s to the selected items.
   **
   ** @param  selection          the collection of {@link Row}s to add to the
   **                            selected items.
   */
  @Override
  protected void addSelection(final List<Row> selection) {
    for (Row row : selection) {
      final EndpointAdapter svr = new EndpointAdapter(row);
      if (!getSelectedItems().contains(svr))
        getSelectedItems().add(svr);
    }
    refreshSelectedTargets();
  }
}