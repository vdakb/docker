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
    Subsystem   :   System Configuration Management

    File        :   LookupPicker.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    LookupPicker.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2016-03-03  DSteding    First release version
*/

package oracle.iam.identity.sysconfig.lkv.state;

import java.util.Map;
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
import oracle.hst.foundation.faces.ADF;

import oracle.iam.identity.frontend.AbstractPickerState;

import oracle.iam.identity.sysconfig.schema.LookupValueAdapter;

////////////////////////////////////////////////////////////////////////////////
// class LookupPicker
// ~~~~~ ~~~~~~~~~~~~
/**
 ** Declares methods the user interface service provides to search and select
 ** <code>Lookup Definition</code> values.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
 */
public class LookupPicker extends AbstractPickerState {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String       PARAMETER_SCHEMA    = "schema";
  public static final String       PARAMETER_LOOKUP    = "lookupName";
  public static final String       PARAMETER_ATTRIBUTE = "attribute";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:2751649907954423560")
  private static final long        serialVersionUID    = -3108129076498142400L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private boolean                  single;
  private List<LookupValueAdapter> selectedItems       = new ArrayList<LookupValueAdapter>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>LookupPicker</code> backing bean that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public LookupPicker() {
    // ensure inheritance
    super();

    // initialize instancce attributes
    this.single = SELECTIONTYPE_SINGLE.equalsIgnoreCase(ADF.pageFlowScopeStringValue(EVENT_SELECTIONTYPE));
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
  public List<LookupValueAdapter> getSelectedItems() {
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
  public void selectionListener(final @SuppressWarnings("unused") SelectionEvent event) {
    if (this.single) {
      // if a single entry has to be returned cleanup any previously selected
      // item ...
      this.selectedItems.clear();
      // ... and add the current selection to the cache
      addSelection(getSelectedRow());
    }
    // request rendering of partial triggers
    refreshSourceTargets();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   addSelectedRows (AbstractPickerState)
  /**
   ** Return the appropriate ount-coume either <code>error</code> or
   ** <code>success</code>.
   **
   ** @return                   the appropriate ount-coume either
   **                           <code>error</code> or <code>success</code>.
   */
  @Override
  public String addSelectedRows() {
    final Map     scope = ADF.current().getSessionScope();
    final Boolean failed = (Boolean)scope.get(JSF.EVENT_HANDLER_FAILED);
    return (failed != null && failed) ? ERROR  : SUCCESS;
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
  public void addAllActionListener(final @SuppressWarnings("unused") ActionEvent event) {
    final List<Row> selection = iteratorAllRow("LookupPickerVOIterator");
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
  public void removeSelectedActionListener(final @SuppressWarnings("unused") ActionEvent event) {
    final RichTable rtt  = getSelectionTable();
    final RowKeySet keys = rtt.getSelectedRowKeys();
    final RowKeySet olds = keys.clone();
    final Object    old  = rtt.getRowKey();

    List<LookupValueAdapter> removeRows = new ArrayList<LookupValueAdapter>();
    for (Iterator<Object> i = keys.iterator(); i.hasNext(); ) {
      Object key = i.next();
      rtt.setRowKey(key);
      LookupValueAdapter rowData = (LookupValueAdapter)rtt.getRowData();
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
  public void removeAllActionListener(final @SuppressWarnings("unused") ActionEvent event) {
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
      final LookupValueAdapter obj = new LookupValueAdapter(row);
      if (!getSelectedItems().contains(obj))
        getSelectedItems().add(obj);
    }
    refreshSelectedTargets();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   addSelectedRows
  /**
   ** Adds the selected entries to the result which needs to be returned to the
   ** invoking taskflow handler for further processing.
   **
   ** @param  event              the {@link ActionEvent} delivered from the
   **                            action occured in a UIXTable component.
   */
  public void addSelectedRows(final @SuppressWarnings("unused") ActionEvent event) {
    final Map    scope  = ADF.current().getSessionScope();
    scope.put(JSF.EVENT_HANDLER_FAILED, Boolean.FALSE);

    final List selection = getSelectedItems();
    if (selection == null || selection.size() == 0) {
      scope.put(JSF.EVENT_HANDLER_FAILED, Boolean.TRUE);
      return;
    }

    // send back the value for which reason this picker was used
    final HashMap<String, Object> parameter = new HashMap<String, Object>();
    parameter.put(PARAMETER_SCHEMA,    ADF.pageFlowScopeStringValue(PARAMETER_SCHEMA));
    parameter.put(PARAMETER_ATTRIBUTE, ADF.pageFlowScopeStringValue(PARAMETER_ATTRIBUTE));
    parameter.put(PARAMETER_LOOKUP,    ADF.pageFlowScopeStringValue(PARAMETER_LOOKUP));
    TaskFlowUtils.raiseEvent("return", new TargetSelectionEvent(ADF.pageFlowScopeStringValue(EVENT_DISTINGUISHER), selection, parameter));
  }
}