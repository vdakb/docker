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

package oracle.iam.identity.sysadmin.job.backing;

import java.util.Map;
import java.util.List;
import java.util.HashMap;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;

import oracle.adf.view.rich.event.DialogEvent;

import oracle.adf.view.rich.util.ResetUtils;

import oracle.hst.foundation.faces.JSF;
import oracle.hst.foundation.faces.ADF;

import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.identity.frontend.train.AbstractSearch;

import oracle.iam.identity.sysadmin.schema.JobAdapter;
import oracle.iam.identity.sysadmin.schema.JobParameterAdapter;

import oracle.iam.identity.sysadmin.job.state.Train;

////////////////////////////////////////////////////////////////////////////////
// class ParameterState
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** Declares methods the user interface service provides to manage the execution
 ** parameter of <code>Scheduler Job</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
 */
public class ParameterState extends AbstractSearch {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String  DETAIL_ITERATOR  = "JobIterator";

  private static final String BUNDLE           = "oracle.iam.identity.bundle.Administration";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-5338103136068505875")
  private static final long   serialVersionUID = 1936671951075155717L;

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
  // Methode:   isRevokeDisabled
  /**
   ** Whether the delete button of a Scheduler Job relationship is disabled.
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
      case GENERIC_UNDO   : return ADF.resourceBundleValue(BUNDLE, "JOB_PARAMETER_UNDO_CONFIRM");
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
        case GENERIC_UNDO   : undo(selection);
                              break;
      }
    }
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
  // Method:   revert (overridden)
  /**
   ** Reverts all changes belonging to the parameter region of the
   ** <code>Scheduler Job</code>.
   */
  @Override
  public void revert() {
    train().clearParameter();
    refreshRegion();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refreshListener (overridden)
  /**
   ** Refresh the history belonging to a certain <code>Scheduled Job</code>.
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
      final Train                            train   = train();
      boolean                                touched = false;
      final Map<String, JobParameterAdapter> assign  = train.parameterAssign();
      final Map<String, JobParameterAdapter> modify  = train.parameterModify();
      final Map<String, JobParameterAdapter> remove  = train.parameterRemove();

      // access the name of the iterator "ParameterIterator" the table is
      // bound to.
      final RowSetIterator rsi = rowSetIterator("ParameterIterator");
      // latch the current position of the iterator to restore it later
      final int            old = rsi.getCurrentRowIndex();
      for (Row cursor : selection) {
        rsi.setCurrentRow(cursor);
        final JobParameterAdapter value  = JobParameterAdapter.from(cursor);
        final String              action = (String)cursor.getAttribute(JobParameterAdapter.ACTION);
        if (StringUtility.isEmpty(action)) {
          value.setPendingAction(JobParameterAdapter.MOD);
          modify.put(value.getName(), value);
          touched = true;
        }
        // prevent adding the same organization twice to avoid potential
        // conflicts in checking the pending action
        else if (!(assign.containsKey(value.getName()))) {
          assign.put(value.getName(), value);
          touched = true;
        }
      }
      // restore the current position of the row iterator
      rsi.setCurrentRowAtRangeIndex(old);
      if (touched) {
        train.markDirty();
        // do not use resfreshRegion because it will reset the table selection
        // also
        ADF.partialRender(getSearchTable());
        partialRenderAction();
        partialRenderSubmitRevert();
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   undo
  /**
   ** Removes the given targets from the collection of values to be triggering
   ** the <code>Scheduler Job</code>
   **
   ** @param  selection          the collection of values to be removed.
   */
  public void undo(final List<Row> selection) {
    if (!CollectionUtility.empty(selection)) {
      final Train                            train   = train();
      boolean                                touched = false;
      final Map<String, JobParameterAdapter> modify  = train.parameterModify();

      // access the name of the iterator "ParameterIterator" the table is
      // bound to.
      final RowSetIterator rsi = rowSetIterator("ParameterIterator");
      // latch the current position of the iterator to restore it later
      final int            old = rsi.getCurrentRowIndex();
      for (Row cursor : selection) {
        // no need to cast
        final Object parameter = cursor.getAttribute(JobAdapter.PK);
        if (modify.containsKey(parameter)) {
          modify.remove(parameter);
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
   ** Refreshs the parameter region of the <code>Scheduler Job</code>.
   */
  private void refresh() {
    final Train                                         train   = train();
    final Map<String, Map<String, JobParameterAdapter>> pending = new HashMap<String, Map<String, JobParameterAdapter>>();

    pending.put(JobParameterAdapter.ADD, train.parameterAssign());
    pending.put(JobParameterAdapter.MOD, train.parameterModify());
    pending.put(JobParameterAdapter.DEL, train.parameterRemove());

    final Map<String, Object> parameter = new HashMap<String, Object>();
    parameter.put("jobName", ADF.pageFlowScopeStringValue(JobAdapter.PK));
    parameter.put("jobTask", ADF.pageFlowScopeStringValue(JobAdapter.TASK));
    parameter.put("pending", pending);
    ADF.executeOperation("refreshParameter", parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   train
  /**
   ** Returns the {@link Train} bound to the task flow in the PageFlow scope.
   */
  private Train train() {
    return JSF.valueFromExpression("#{pageFlowScope.jobTrain}", Train.class);
  }
}