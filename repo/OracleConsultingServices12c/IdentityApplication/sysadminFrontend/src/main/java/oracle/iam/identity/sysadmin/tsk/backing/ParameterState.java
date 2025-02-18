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
    Subsystem   :   System Administration Management

    File        :   ParameterState.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ParameterState.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2016-03-03  DSteding    First release version
*/

package oracle.iam.identity.sysadmin.tsk.backing;

import java.util.Map;
import java.util.List;
import java.util.HashMap;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;

import oracle.adf.view.rich.event.DialogEvent;

import oracle.adf.view.rich.util.ResetUtils;

import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.CollectionUtility;

import oracle.hst.foundation.faces.JSF;
import oracle.hst.foundation.faces.ADF;

import oracle.iam.identity.frontend.train.AbstractSearch;

import oracle.iam.identity.sysadmin.schema.TaskAdapter;
import oracle.iam.identity.sysadmin.schema.TaskParameterAdapter;

import oracle.iam.identity.sysadmin.tsk.state.Train;

////////////////////////////////////////////////////////////////////////////////
// class ParameterState
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** Declares methods the user interface service provides to manage the execution
 ** parameter of <code>Scheduler Task</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
 */
public class ParameterState extends AbstractSearch {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String BUNDLE           = "oracle.iam.identity.bundle.Administration";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-7404163695253090970")
  private static final long   serialVersionUID = 8788910121336268479L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private boolean             displayInfo;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ParameterState</code> backing bean that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ParameterState() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDisplayInfo
  /**
   ** Sets the value of the displayInfo property.
   ** <p>
   ** Method needs to be declared as <b><code>public</code></b> because it used
   ** in the UI.
   **
   ** @param  value              the value of the displayInfo property.
   **                            Allowed object is <code>boolean</code>.
   */
  public void setDisplayInfo(final boolean value) {
    this.displayInfo = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isDisplayInfo
  /**
   ** Returns the value of the displayInfo property.
   ** <p>
   ** Method needs to be declared as <b><code>public</code></b> because it used
   ** in the UI.
   **
   ** @return                    the value of the displayInfo.
   **                            Possible object is <code>boolean</code>.
   */
  public boolean isDisplayInfo() {
    final String mode = ADF.pageFlowScopeStringValue(PARAMETER_MODE);
    if (MODE_VIEW.equals(mode) || MODE_EDIT.equals(mode) || (MODE_CREATE.equals(mode) && iteratorHasRows("TaskParameterIterator")))
      this.displayInfo = false;
    else
      this.displayInfo = true;
    return this.displayInfo;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   isRevokeDisabled
  /**
   ** Whether the delete button of a Scheduler Task relationship is disabled.
   **
   ** @return                    <code>true</code> revoke button of a
   **                            relationship is disabled; otherwise
   **                            <code>false</code>.
   */
  public boolean isRevokeDisabled() {
    return !(getSelectedRowCount() > 0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getLocalizedMessage
  /**
   ** Returns the localized action message.
   **
   ** @return                    the localized action message.
   */
  public String getLocalizedMessage() {
    String localized = train().getActionName();
    if (StringUtility.isEmpty(localized))
      return "???-emptyornull-???";

    switch (localized) {
      case GENERIC_DELETE : return ADF.resourceBundleValue(BUNDLE, "TSK_PARAMETER_DELETE_CONFIRM");
      case GENERIC_UNDO   : return ADF.resourceBundleValue(BUNDLE, "TSK_PARAMETER_UNDO_CONFIRM");
      default             : return String.format("???-%s-???", localized);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   changed
  /**
   ** Perfoms all actions belonging to the UI to reflect value change events on
   ** particular components in the current page.
   **
   ** @param  event              the {@link ValueChangeEvent} object that
   **                            characterizes the action to perform.
   */
  public void changed(final @SuppressWarnings("unused") ValueChangeEvent event) {
    final List<Row> selection = selectedRow(getSearchTable());
    modify(selection);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dialogListener
  /**
   ** Perfoms all actions belonging to the UI to reflect action events belonging
   ** to entries in the value table after confirmation.
   **
   ** @param  event              the {@link DialogEvent} object that
   **                            characterizes the action to perform.
   */
  public void dialogListener(final DialogEvent event) {
    if (DialogEvent.Outcome.yes.equals(event.getOutcome())) {
      final String    actionName = train().getActionName();
      final List<Row> selection  = selectedRow(getSearchTable());
      switch(actionName) {
        case GENERIC_DELETE : remove(selection);
                              break;
        case GENERIC_UNDO   : undo(selection);
                              break;
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createListener
  /**
   ** Create a value belonging to a certain <code>Scheduler Task</code>.
   **
   ** @param  event              the {@link ActionEvent} object that
   **                            characterizes the action to perform.
   */
  public void createListener(final ActionEvent event) {
    final Train                train = train();
    final TaskParameterAdapter mab   = new TaskParameterAdapter();

    // keep the row created above in the cache so that the row survives the next
    // refresh request
    final Map<String, Object> assign = train.parameterAssign();
    assign.put(mab.getName(), mab);
    refreshListener(event);

    train.markDirty();
    ADF.partialRender(getSearchTable());
    partialRenderAction();
    partialRenderSubmitRevert();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   undoListener
  /**
   ** Perfoms all actions belonging to the UI to reflect action events to
   ** undo entries from the value table.
   **
   ** @param  event              the {@link ActionEvent} object that
   **                            characterizes the action to perform.
   */
  public void undoListener(final @SuppressWarnings("unused") ActionEvent event) {
    final List<Row> selection = selectedRow(getSearchTable());
    undo(selection);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeListener
  /**
   ** Perfoms all actions belonging to the UI to reflect action events to
   ** remove entries from the value table.
   **
   ** @param  event              the {@link ActionEvent} object that
   **                            characterizes the action to perform.
   */
  public void removeListener(final @SuppressWarnings("unused") ActionEvent event) {
    final List<Row> selection = selectedRow(getSearchTable());
    remove(selection);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revert (overridden)
  /**
   ** Reverts all changes belonging to the parameter region of the
   ** <code>Scheduler Task</code>.
   */
  @Override
  public void revert() {
    train().clearParameter();
    refreshRegion();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refreshListener (overridden)
  /**
   ** Refresh the history belonging to a certain <code>Scheduled Task</code>.
   **
   ** @param  event              the {@link ActionEvent} object that
   **                            characterizes the action to perform.
   */
  @Override
  public void refreshListener(final ActionEvent event) {
    refresh();
    // ensure inheritance
    super.refreshListener(event);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   modify
  /**
   ** Add the given targets to the collection of modified values.
   **
   ** @param  selection          the collection of values to be modified.
   */
  public void modify(final List<Row> selection) {
    if (selection != null && !selection.isEmpty()) {
      final Train               train   = train();
      boolean                   touched = false;
      final Map<String, Object> modify  = train.parameterModify();

      // access the name of the iterator "TaskParameterIterator" the table is
      // bound to.
      final RowSetIterator rsi = rowSetIterator("TaskParameterIterator");
      // latch the current position of the iterator to restore it later
      final int            old = rsi.getCurrentRowIndex();
      for (Row cursor : selection) {
        rsi.setCurrentRow(cursor);
        final String action = (String)cursor.getAttribute(TaskParameterAdapter.ACTION);
        if (StringUtility.isEmpty(action)) {
          cursor.setAttribute(TaskParameterAdapter.ACTION, TaskParameterAdapter.MOD);
          final TaskParameterAdapter value = new TaskParameterAdapter(cursor);
          modify.put(value.getName(), value);
          touched = true;
        }
      }
      // restore the current position of the row iterator
      rsi.setCurrentRowAtRangeIndex(old);
      // restore the current position of the row iterator
      rsi.setCurrentRowAtRangeIndex(old);
      if (touched) {
        train.markDirty();
        ADF.partialRender(getSearchTable());
        partialRenderAction();
        partialRenderSubmitRevert();
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   remove
  /**
   ** Removes the given targets from the collection of values which triggers
   ** this <code>Scheduler Task</code>.
   **
   ** @param  selection          the collection of values to be removed.
   */
  public void remove(final List<Row> selection) {
    if (selection != null && !selection.isEmpty()) {
      final Train               train   = train();
      boolean                   touched = false;
      final Map<String, Object> assign = train.parameterAssign();
      final Map<String, Object> remove = train.parameterRemove();
      for (Row cursor : selection) {
        final String action = (String)cursor.getAttribute(TaskParameterAdapter.ACTION);
        if (StringUtility.isEmpty(action) || TaskParameterAdapter.MOD.equals(action)) {
          cursor.setAttribute(TaskParameterAdapter.ACTION, TaskParameterAdapter.DEL);
          final TaskParameterAdapter value = new TaskParameterAdapter(cursor);
          remove.put(value.getName(), value);
          touched = true;
        }
        else if (TaskParameterAdapter.ADD.equals(action)) {
          cursor.remove();
          assign.remove(cursor.getAttribute(TaskParameterAdapter.PK));
          touched = true;
        }
      }
      if (touched) {
        train.markDirty();
        refreshRegion();
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   undo
  /**
   ** Removes the given targets from the collection of values to be triggering
   ** the <code>Scheduler Task</code>
   **
   ** @param  selection          the collection of values to be removed.
   */
  public void undo(final List<Row> selection) {
    if (!CollectionUtility.empty(selection)) {
      final Train               train   = train();
      boolean                   touched = false;
      final Map<String, Object> assign  = train.parameterAssign();
      final Map<String, Object> modify  = train.parameterModify();
      final Map<String, Object> remove  = train.parameterRemove();

      // access the name of the iterator "TaskParameterIterator" the table is
      // bound to.
      final RowSetIterator rsi = rowSetIterator("TaskParameterIterator");
      // latch the current position of the iterator to restore it later
      final int            old = rsi.getCurrentRowIndex();
      for (Row cursor : selection) {
        // no need to cast
        final Object parameter = cursor.getAttribute(TaskParameterAdapter.PK);
        if (assign.containsKey(parameter)) {
          assign.remove(parameter);
          touched = true;
        }
        else if (modify.containsKey(parameter)) {
          modify.remove(parameter);
          touched = true;
        }
        else if (remove.containsKey(parameter)) {
          remove.remove(parameter);
          touched = true;
        }
      }
      // restore the current position of the row iterator
      rsi.setCurrentRowAtRangeIndex(old);
      if (touched) {
        ResetUtils.reset(getSearchTable());
//        ADF.partialRender(getSearchTable());
        partialRenderAction();
        partialRenderSubmitRevert();
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refreshRegion (overridden)
  /**
   ** Add the UI components placed on the value relationship as a partial
   ** rendering target.
   ** <p>
   ** In response to a partial event, only components registered as partial
   ** targets are re-rendered. For a component to be successfully re-rendered
   ** when it is manually added with this API, it should have the
   ** "clientComponent" attribute set to <code>true</code>. If not, partial
   ** re-rendering may or may not work depending on the component.
   */
  @Override
  protected void refreshRegion() {
    refresh();
    // ensure inheritance
    super.refreshRegion();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refresh
  /**
   ** Refreshs the parameter region of the <code>Scheduler Task</code>.
   */
  private void refresh() {
    final Train                            train   = train();
    final Map<String, Map<String, Object>> pending = new HashMap<String, Map<String, Object>>();

    pending.put(TaskParameterAdapter.ADD, train.parameterAssign());
    pending.put(TaskParameterAdapter.DEL, train.parameterRemove());
    pending.put(TaskParameterAdapter.MOD, train.parameterModify());

    final Map<String, Object> parameter = new HashMap<String, Object>();
    parameter.put("identifier", ADF.pageFlowScopeStringValue(TaskAdapter.PK));
    parameter.put("pending",    pending);
    ADF.executeOperation("refreshParameter", parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   train
  /**
   ** Returns the {@link Train} bound to the task flow in the PageFlow scope.
   */
  private Train train() {
    return JSF.valueFromExpression("#{pageFlowScope.taskTrain}", Train.class);
  }
}