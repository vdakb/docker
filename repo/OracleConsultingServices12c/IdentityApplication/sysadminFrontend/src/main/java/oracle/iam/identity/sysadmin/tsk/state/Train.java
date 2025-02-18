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

    File        :   Train.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Train.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2016-03-03  DSteding    First release version
*/

package oracle.iam.identity.sysadmin.tsk.state;

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.faces.event.ActionEvent;

import javax.faces.application.NavigationHandler;

import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;

import oracle.adf.view.rich.util.ResetUtils;

import oracle.adf.view.rich.component.rich.layout.RichPanelFormLayout;

import oracle.iam.ui.view.handler.CreateTrain1HandlerInterface;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.foundation.faces.JSF;
import oracle.hst.foundation.faces.ADF;

import oracle.iam.identity.sysadmin.schema.TaskAdapter;

import oracle.iam.identity.frontend.train.AbstractFlow;

import oracle.iam.identity.sysadmin.schema.TaskParameterAdapter;

import oracle.iam.identity.sysadmin.tsk.backing.JobState;
import oracle.iam.identity.sysadmin.tsk.backing.ParameterState;
import oracle.iam.identity.sysadmin.tsk.backing.AttributeState;

////////////////////////////////////////////////////////////////////////////////
// class Train
// ~~~~~ ~~~~~
/**
 ** Declares methods the user interface service provides to navigate across
 ** pages to create or manage <code>Scheduler Task</code>s.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
 */
public class Train extends    AbstractFlow
                   implements CreateTrain1HandlerInterface {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String BUNDLE           = "oracle.iam.identity.bundle.Administration";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:1349375307528685086")
  private static final long   serialVersionUID = 7001175036357486191L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private Map<String, Object> parameterAssign  = new LinkedHashMap<String, Object>();
  private Map<String, Object> parameterModify  = new LinkedHashMap<String, Object>();
  private Map<String, Object> parameterRemove  = new LinkedHashMap<String, Object>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Train</code> backing bean that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Train() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   isSaveEnabled
  /**
   ** Returns <code>true</code> if the train flow allows to save changes.
   **
   ** @return                    <code>true</code> if the train flow allows to
   **                            save changes; otherwise <code>false</code>.
   **                            Possible object is <code>boolean</code>.
   */
  public boolean isSaveEnabled() {
    final String mode = ADF.pageFlowScopeStringValue(PARAMETER_MODE);
    if (StringUtility.isBlank(mode))
      return false;
    else {
      return (mode.equals(MODE_EDIT) || mode.equals(MODE_CREATE));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   parameterAssign
  /**
   ** Returns the collection of parameters to be assigned.
   **
   ** @return                    the collection of parameters to be assigned.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link Object} as the value.
   */
  public Map<String, Object> parameterAssign() {
    return this.parameterAssign;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   parameterRemove
  /**
   ** Returns the collection of parameters to be removed.
   **
   ** @return                    the collection of parameters to be removed.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link Object} as the value.
   */
  public Map<String, Object> parameterRemove() {
    return this.parameterRemove;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   parameterModify
  /**
   ** Returns the collection of parameters to be modified.
   **
   ** @return                    the collection of parameters to be modified.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link Object} as the value.
   */
  public Map<String, Object> parameterModify() {
    return this.parameterModify;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   attributeState
  /**
   ** Returns the backing bean of the attribute region.
   **
   ** @return                    the backing bean of the attribute region.
   **                            Possible object is {@link AttributeState}.
   */
  private AttributeState attributeState() {
    try {
      return JSF.valueFromExpression("#{backingBeanScope.taskAttribute}", AttributeState.class);
    }
    // for debugging purpose only catch everything to spool to error log
    catch (Throwable t) {
      t.printStackTrace(System.err);
      throw t;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   parameterState
  /**
   ** Returns the backing bean of the parameter region.
   **
   ** @return                    the backing bean of the parameter region.
   **                            Possible object is {@link ParameterState}.
   */
  private ParameterState parameterState() {
    try {
      return JSF.valueFromExpression("#{backingBeanScope.taskParameter}", ParameterState.class);
    }
    // for debugging purpose only catch everything to spool to error log
    catch (Throwable t) {
      t.printStackTrace(System.err);
      throw t;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   jobState
  /**
   ** Returns the backing bean of the job region.
   **
   ** @return                    the backing bean of the job region.
   **                            Possible object is {@link JobState}.
   */
  private JobState jobState() {
    try {
      return JSF.valueFromExpression("#{backingBeanScope.taskJob}", JobState.class);
    }
    // for debugging purpose only catch everything to spool to error log
    catch (Throwable t) {
      t.printStackTrace(System.err);
      throw t;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   finishActionHandler (CreateTrain1HandlerInterface)
  /**
   ** The action listener to be called then the finish button is clicked.
   **
   ** @param  event              the {@link ActionEvent} object that
   **                            characterizes the action to perform.
   **                            <br>
   **                            Allowed object is {@link ActionEvent}.
   */
  @Override
  public void finishActionHandler(final ActionEvent event) {
    final NavigationHandler nh = JSF.application().getNavigationHandler();
    nh.handleNavigation(JSF.context(), "", submit());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Initialize the train flow to create a new <code>Scheduler Task</code>.
   ** <br>
   ** Leverage the operational binding <code>taskCreate</code> to do so.
   */
  public void create() {
    ADF.executeOperation("taskCreate");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetch
  /**
   ** Initialize the train flow to manage an existing <code>Scheduler Task</code>.
   ** <br>
   ** Leverage the operational binding <code>taskFetch</code> to do so.
   */
  public void fetch() {
    ADF.executeOperation("taskFetch");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   submit
  /**
   ** Create or updates the <code>Scheduler Task</code>, open a newly created
   ** <code>Scheduler Task</code> in the edit mode and close the current task
   ** flow.
   ** <br>
   ** Leverage the operational binding <code>commit</code> to do so.
   ** <p>
   ** <b>Note</b>:
   ** The access to the binding layer assumes that the actions are trigger by
   ** a UI component of the embedded pages which comprise the train task flow.
   **
   ** @return                    the outcome of the operation to support
   **                            control rule navigation in the task flow.
   */
  public String submit() {
    final Map<String, Object> parameter = new HashMap<String, Object>() {{
      put(TaskParameterAdapter.ADD, parameterAssign);
      put(TaskParameterAdapter.MOD, parameterModify);
      put(TaskParameterAdapter.DEL, parameterRemove);
    }};
    // commit the changes to the persistence layer which will now do what's
    // needed to create or update the task metadata
    // keep in mind this will not attach any other related values like task
    // values etc.
    final Object result = ADF.executeAction(SUBMIT, parameter);
    if (ADF.executeActionSucceeded(result)) {
      final RowSetIterator rowSet = rowSetIterator("TaskIterator");
      final Row    row   = rowSet.getCurrentRow();
      final String name  = (String)row.getAttribute(TaskAdapter.PK);
      final Map    scope = ADF.current().getPageFlowScope();
      final String flow  = (String)scope.get(PARAMETER_TASKFLOW);
      if (MODE_CREATE.equals(scope.get(PARAMETER_MODE))) {
        raiseTaskFlowFeedbackEvent(String.format(ADF.resourceBundleValue(BUNDLE, "TSK_TRAIN_CREATE"), name));
        raiseTaskFlowRemoveEvent(flow);
      }
      else {
        raiseTaskFlowFeedbackEvent(String.format(ADF.resourceBundleValue(BUNDLE, "TSK_TRAIN_MODIFY"), name));
        refreshTaskFlow(flow, name, null, null);
      }
      return SUCCESS;
    }
    else
      return ERROR;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revert
  /**
   ** Revert all changes made on train flow belonging to an
   ** <code>Scheduler Task</code>.
   ** <br>
   ** Leverage the operational binding <code>rollback</code> to do so.
   **
   ** @param  event              the {@link ActionEvent} object that
   **                            characterizes the action to perform.
   */
  public void revert(final @SuppressWarnings("unused") ActionEvent event) {
    // rollback any transaction
    ADF.executeAction(REVERT);
    revertAttribute();
    parameterState().revert();
    jobState().revert();

    markClean();

    // if task name changed, update tab name and title
    final String name  = ADF.iteratorBindingValue(AttributeState.DETAIL_ITERATOR, TaskAdapter.PK, String.class);
    raiseTaskFlowFeedbackEvent(String.format(ADF.resourceBundleValue(BUNDLE, "TSK_TRAIN_REVERT"), name));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revertAttribute
  /**
   ** Reverts all changes belonging to the attribute region of an
   ** <code>Scheduler Task</code>.
   ** <br>
   ** Leverage the operational binding <code>refreshAttribute</code> to do so.
   */
  public void revertAttribute() {
    ADF.executeOperation("refreshAttribute");
    // need to explicitly reset task attributes forms this is because Revert
    // button on that page is immediate hence component submitted values need to
    // be reset explicitly
    final RichPanelFormLayout form = getFormLayout();
    if (form != null) {
      ResetUtils.reset(form);
      ADF.partialRender(form);
      attributeState().partialRenderSubmitRevert();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clearParameter
  /**
   ** Reverts all changes belonging to the parameter region of the
   ** <code>Scheduler Task</code>.
   */
  public void clearParameter() {
    this.parameterAssign.clear();
    this.parameterRemove.clear();
    this.parameterModify.clear();
  }
}