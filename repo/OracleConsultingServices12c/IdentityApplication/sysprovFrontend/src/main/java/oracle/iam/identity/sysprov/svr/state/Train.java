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

    File        :   Train.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Train.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2016-03-03  DSteding    First release version
*/

package oracle.iam.identity.sysprov.svr.state;

import java.util.Map;
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

import oracle.iam.identity.frontend.train.AbstractFlow;

import oracle.iam.identity.sysprov.schema.EndpointAdapter;
import oracle.iam.identity.sysprov.schema.EndpointParameterAdapter;
import oracle.iam.identity.sysprov.schema.EndpointAdministratorAdapter;

import oracle.iam.identity.sysprov.svr.backing.AttributeState;
import oracle.iam.identity.sysprov.svr.backing.ParameterState;
import oracle.iam.identity.sysprov.svr.backing.AdministratorState;

////////////////////////////////////////////////////////////////////////////////
// class Train
// ~~~~~ ~~~~~
/**
 ** Declares methods the user interface service provides to navigate across
 ** pages to create or manage <code>IT Resource</code>s.
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

  private static final String BUNDLE              = "oracle.iam.identity.bundle.Provisioning";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:9030965689043414216")
  private static final long   serialVersionUID    = 7590380657871190743L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private Map<Long, EndpointParameterAdapter>     parameterModify     = new LinkedHashMap<Long, EndpointParameterAdapter>();
  private Map<Long, EndpointAdministratorAdapter> administratorAssign = new LinkedHashMap<Long, EndpointAdministratorAdapter>();
  private Map<Long, EndpointAdministratorAdapter> administratorModify = new LinkedHashMap<Long, EndpointAdministratorAdapter>();
  private Map<Long, EndpointAdministratorAdapter> administratorRemove = new LinkedHashMap<Long, EndpointAdministratorAdapter>();

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
  // Methode:   parameterModify
  /**
   ** Returns the collection of parameters to be modified.
   **
   ** @return                    the collection of parameters to be modified.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link EndpointParameterAdapter} as the
   **                            value.
   */
  public Map<Long, EndpointParameterAdapter> parameterModify() {
    return this.parameterModify;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   administratorAssign
  /**
   ** Returns the collection of administrators to be assigned.
   **
   ** @return                    the collection of administrators to be assigned.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link EndpointAdministratorAdapter} as the
   **                            value.
   */
  public Map<Long, EndpointAdministratorAdapter> administratorAssign() {
    return this.administratorAssign;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   administratorRemove
  /**
   ** Returns the collection of administrators to be removed.
   **
   ** @return                    the collection of administrators to be removed.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link EndpointAdministratorAdapter} as the
   **                            value.
   */
  public Map<Long, EndpointAdministratorAdapter> administratorRemove() {
    return this.administratorRemove;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   administratorModify
  /**
   ** Returns the collection of administrators to be modified.
   **
   ** @return                    the collection of administrators to be modified.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link EndpointAdministratorAdapter} as the
   **                            value.
   */
  public Map<Long, EndpointAdministratorAdapter> administratorModify() {
    return this.administratorModify;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   attributeState
  /**
   ** Returns the backing bean of the attribute region.
   **
   ** @return                    the backing bean of the attribute region.
   */
  private AttributeState attributeState() {
    try {
      return JSF.valueFromExpression("#{backingBeanScope.endpointAttribute}", AttributeState.class);
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
      return JSF.valueFromExpression("#{backingBeanScope.endpointParameter}", ParameterState.class);
    }
    // for debugging purpose only catch everything to spool to error log
    catch (Throwable t) {
      t.printStackTrace(System.err);
      throw t;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   administratorState
  /**
   ** Returns the backing bean of the administrator region.
   **
   ** @return                    the backing bean of the administrator region.
   **                            Possible object is {@link AdministratorState}.
   */
  private AdministratorState administratorState() {
    try {
      return JSF.valueFromExpression("#{backingBeanScope.endpointAdministrator}", AdministratorState.class);
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
   ** Initialize the train flow to create a new <code>IT Resource</code>.
   ** <br>
   ** Leverage the operational binding <code>endpointCreate</code> to do so.
   */
  public void create() {
    ADF.executeOperation("endpointCreate");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetch
  /**
   ** Initialize the train flow to manage an existing <code>IT Resource</code>.
   ** <br>
   ** Leverage the operational binding <code>endpointFetch</code> to do so.
   */
  public void fetch() {
    ADF.executeOperation("endpointFetch");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   submit
  /**
   ** Create or updates the <code>IT Resource</code>, open a newly created
   ** <code>IT Resource</code> in the edit mode and close the current task
   ** flow.
   ** <br>
   ** Leverage the operational binding <code>commit</code> to do so.
   **
   ** @return                    the outcome of the operation to support
   **                            control rule navigation in the task flow.
   */
  public String submit() {
    // commit the changes to the persistence layer which will now do what's
    // needed to create or update the IT Resource metadata
    // keep in mind this will not attach any other related values like job
    // values etc.
    final Object result = ADF.executeAction(SUBMIT, this.parameterModify);
    if (ADF.executeActionSucceeded(result)) {
      final RowSetIterator rowSet = rowSetIterator("EndpointIterator");
      final Row    row   = rowSet.getCurrentRow();
      final String name  = (String)row.getAttribute(EndpointAdapter.NAME);
      final Map    scope = ADF.current().getPageFlowScope();
      final String flow  = (String)scope.get(PARAMETER_TASKFLOW);
      if (MODE_CREATE.equals(scope.get(PARAMETER_MODE))) {
        raiseTaskFlowFeedbackEvent(String.format(ADF.resourceBundleValue(BUNDLE, "SVR_TRAIN_CREATE"), name));
        raiseTaskFlowRemoveEvent(flow);
      }
      else {
        raiseTaskFlowFeedbackEvent(String.format(ADF.resourceBundleValue(BUNDLE, "SVR_TRAIN_MODIFY"), name));
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
   ** <code>IT Resource</code>.
   ** <br>
   ** Leverage the operational binding <code>rollback</code> to do so.
   **
   ** @param  event              the {@link ActionEvent} endpoint that
   **                            characterizes the action to perform.
   */
  public void revert(final @SuppressWarnings("unused") ActionEvent event) {
    // rollback any transaction
    ADF.executeAction(REVERT);
    revertAttribute();
    parameterState().revert();
    administratorState().revert();

    markClean();

    // if policy name changed, update tab name and title
    final String name  = ADF.iteratorBindingValue(AttributeState.DETAIL_ITERATOR, EndpointAdapter.NAME, String.class);
    raiseTaskFlowFeedbackEvent(String.format(ADF.resourceBundleValue(BUNDLE, "SVR_TRAIN_REVERT"), name));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revertAttribute
  /**
   ** Reverts all changes belonging to the attribute region of an
   ** <code>IT Resource</code>.
   ** <br>
   ** Leverage the operational binding <code>refreshAttribute</code> to do so.
   */
  public void revertAttribute() {
    ADF.executeOperation("refreshAttribute");
    // need to explicitly reset application and catalog attributes forms this
    // is because Revert button on that page is immediate hence component
    // submittedValues need to be reset explicitly
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
   ** <code>IT Resource</code>.
   */
  public void clearParameter() {
    this.parameterModify.clear();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clearAdministrator
  /**
   ** Reverts all changes belonging to the administrator region of the
   ** <code>IT Resource Type</code>.
   */
  public void clearAdministrator() {
    this.administratorAssign.clear();
    this.administratorRemove.clear();
    this.administratorModify.clear();
  }
}