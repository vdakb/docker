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

    System      :   Employee Self Service Portal
    Subsystem   :   Vehicle Administration

    File        :   Picker.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@oracle.com

    Purpose     :   This file implements the class
                    Picker.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     020-05-28   SBernet     First release version
*/
package bka.employee.portal.vehicle.vmb.state;

import bka.employee.portal.vehicle.model.view.BrandVORowImpl;
import bka.employee.portal.view.state.AbstractPicker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.event.ActionEvent;

import oracle.hst.foundation.faces.JSF;

import oracle.iam.ui.platform.utils.TaskFlowUtils;
import oracle.iam.ui.platform.view.event.TargetSelectionEvent;

import oracle.jbo.Row;

import org.apache.myfaces.trinidad.event.SelectionEvent;

////////////////////////////////////////////////////////////////////////////////
// class Picker
// ~~~~~ ~~~~~~
/**
 ** Declares methods the user interface service provides to search and select
 ** <code>Vehicule Brand</code>.
 **
 ** @author  sylvert.bernet@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Picker extends AbstractPicker {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////
  @SuppressWarnings("compatibility:8680586035245677066")
  private static final long serialVersionUID = -2483434916786250681L;
  
  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private List<BrandVORowImpl> selectedItems    = new ArrayList<BrandVORowImpl>();

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
  public List<BrandVORowImpl> getSelectedItems() {
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
  public void selectionListener(SelectionEvent event) {
    System.out.println("**************Selection detected");
    addSelection(selectedRow(getSearchTable()));
    System.out.println("**************Leaving Selection");
    
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
  // Method:   addSelectedActionListener (AbstractPickerState)
  /**
   ** Add the selected rows in the search table to the selection.
   **
   ** @param  event              the {@link ActionEvent} delivered from the
   **                            action occured in a UIXForm component.
   */
  @Override
  public void addSelectedActionListener(final @SuppressWarnings("unused") ActionEvent event) {
    addSelection(selectedRow(getSearchTable()));
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
  protected void addSelection(List<Row> selection) {
    getSelectedItems().clear();
    for (Row row : selection) {
      final BrandVORowImpl obj = (BrandVORowImpl)row;
      if (!getSelectedItems().contains(obj))
        getSelectedItems().add(obj);
      refreshSelectedTargets();
    }
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
  public void addAllActionListener(ActionEvent event) {
    // Unused
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
  public void removeSelectedActionListener(ActionEvent event) {
    // Unused
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
  public void removeAllActionListener(ActionEvent event) {
    // Unused
  }
}
