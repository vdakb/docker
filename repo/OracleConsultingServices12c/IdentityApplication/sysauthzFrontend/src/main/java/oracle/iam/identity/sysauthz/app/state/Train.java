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
    Subsystem   :   System Authorization Management

    File        :   Train.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Train.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2016-03-03  DSteding    First release version
*/

package oracle.iam.identity.sysauthz.app.state;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.LinkedHashMap;

import java.util.List;

import javax.faces.event.ActionEvent;

import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;

import oracle.adf.view.rich.util.ResetUtils;

import oracle.adf.view.rich.component.rich.layout.RichPanelFormLayout;

import oracle.hst.foundation.faces.JSF;
import oracle.hst.foundation.faces.ADF;

import oracle.iam.identity.sysauthz.schema.ApplicationInstanceAdapter;

import oracle.iam.identity.frontend.train.AbstractFlow;

import oracle.iam.identity.sysauthz.app.backing.AccountState;
import oracle.iam.identity.sysauthz.app.backing.AttributeState;
import oracle.iam.identity.sysauthz.app.backing.EntitlementState;
import oracle.iam.identity.sysauthz.app.backing.PublicationState;

import oracle.iam.identity.sysauthz.schema.EntityPublicationAdapter;

////////////////////////////////////////////////////////////////////////////////
// class Train
// ~~~~~ ~~~~~
/**
 ** Declares methods the user interface service provides to navigate across
 ** pages to create or manage <code>Application Instance</code>s.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
 */
public class Train extends AbstractFlow {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String                   BUNDLE           = "oracle.iam.identity.bundle.Authorization";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-7385335616695416991")
  private static final long                     serialVersionUID = 5568391939477533385L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private Map<String, EntityPublicationAdapter> scopeAssign      = new LinkedHashMap<String, EntityPublicationAdapter>();
  private Map<String, EntityPublicationAdapter> scopeRemove      = new LinkedHashMap<String, EntityPublicationAdapter>();
  private Map<String, EntityPublicationAdapter> scopeModify      = new LinkedHashMap<String, EntityPublicationAdapter>();

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
  // Methode:   scopeAssign
  /**
   ** Returns the collection of publication scopes to be assigned.
   **
   ** @return                    the collection of publication scopes to be
   **                            assigned.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link EntityPublicationAdapter} as the
   **                            value.
   */
  public Map<String, EntityPublicationAdapter> scopeAssign() {
    return this.scopeAssign;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   scopeRemove
  /**
   ** Returns the collection of publication scopes to be removed.
   **
   ** @return                    the collection of publication scopes to be
   **                            removed.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link EntityPublicationAdapter} as the
   **                            value.
   */
  public Map<String, EntityPublicationAdapter> scopeRemove() {
    return this.scopeRemove;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   scopeModify
  /**
   ** Returns the collection of publication scopes to be removed.
   **
   ** @return                    the collection of publication scopes to be
   **                            modified.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link EntityPublicationAdapter} as the
   **                            value.
   */
  public Map<String, EntityPublicationAdapter> scopeModify() {
    return this.scopeModify;
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
      return JSF.valueFromExpression("#{backingBeanScope.applicationAttribute}", AttributeState.class);
    }
    // for debugging purpose only catch everything to spool to error log
    catch (Throwable t) {
      t.printStackTrace(System.err);
      throw t;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   publicationState
  /**
   ** Returns the backing bean of the publication region.
   **
   ** @return                    the backing bean of the publication region.
   */
  private PublicationState publicationState() {
    try {
      return JSF.valueFromExpression("#{backingBeanScope.applicationPublication}", PublicationState.class);
    }
    // for debugging purpose only catch everything to spool to error log
    catch (Throwable t) {
      t.printStackTrace(System.err);
      throw t;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   accountState
  /**
   ** Returns the backing bean of the account region.
   **
   ** @return                    the backing bean of the account region.
   */
  private AccountState accountState() {
    try {
      return JSF.valueFromExpression("#{backingBeanScope.applicationAccount}", AccountState.class);
    }
    // for debugging purpose only catch everything to spool to error log
    catch (Throwable t) {
      t.printStackTrace(System.err);
      throw t;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   entitlementState
  /**
   ** Returns the backing bean of the entitlement region.
   **
   ** @return                    the backing bean of the entitlement region.
   */
  private EntitlementState entitlementState() {
    try {
      return JSF.valueFromExpression("#{backingBeanScope.applicationEntitlement}", EntitlementState.class);
    }
    // for debugging purpose only catch everything to spool to error log
    catch (Throwable t) {
      t.printStackTrace(System.err);
      throw t;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Initialize the train flow to create a new
   ** <code>Application Instance</code>.
   ** <br>
   ** Leverage the operational binding <code>instanceCreate</code> to do so.
   */
  public void create() {
    ADF.executeOperation("instanceCreate");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetch
  /**
   ** Initialize the train flow to manage an existing
   ** <code>Application Instance</code>.
   ** <br>
   ** Leverage the operational binding <code>instanceFetch</code> to do so.
   */
  public void fetch() {
    ADF.executeOperation("instanceFetch");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   submit
  /**
   ** Create or updates the <code>Application Instance</code>, open a newly
   ** created <code>Application Instance</code> in the edit mode and close the
   ** current task flow.
   ** <br>
   ** Leverage the operational binding <code>commit</code> to do so.
   **
   ** @return                    the outcome of the operation to support
   **                            control rule navigation in the task flow.
   */
  public String submit() {
    // before the instance itself will be touched take care about the
    // publications of the application instance that are pending
    final List<Map<String, EntityPublicationAdapter>> pending = new ArrayList<Map<String, EntityPublicationAdapter>>();
    pending.add(this.scopeAssign);
    pending.add(this.scopeRemove);
    pending.add(this.scopeModify);
    final Map<String, Object> parameter = new HashMap<String, Object>();
    parameter.put("identifier", ADF.pageFlowScopeStringValue(ApplicationInstanceAdapter.PK));
    parameter.put("pending",    pending);
    ADF.executeOperation("applyPublication", parameter);

    // commit the changes to the persistence layer which will now do what's
    // needed to create or update the Application Instance metadata
    // keep in mind this will not attach any other related values like job
    // values etc.
    final Object result = ADF.executeAction(SUBMIT);
    if (ADF.executeActionSucceeded(result)) {
      final RowSetIterator rowSet = rowSetIterator("ApplicationInstanceIterator");
      final Row    row   = rowSet.getCurrentRow();
      final String name  = (String)row.getAttribute(ApplicationInstanceAdapter.DISPLAY_NAME);
      final Map    scope = ADF.current().getPageFlowScope();
      final String flow  = (String)scope.get(PARAMETER_TASKFLOW);
      if (MODE_CREATE.equals(scope.get(PARAMETER_MODE))) {
        raiseTaskFlowFeedbackEvent(String.format(ADF.resourceBundleValue(BUNDLE, "APP_TRAIN_CREATE"), name));
        raiseTaskFlowRemoveEvent(flow);
      }
      else {
        raiseTaskFlowFeedbackEvent(String.format(ADF.resourceBundleValue(BUNDLE, "APP_TRAIN_MODIFY"), name));
        refreshTaskFlow(flow, name, null, null);
        // operation succesful hence clean up the state of the taskflow
        markClean();
        clearScope();
        attributeState().partialRenderSubmitRevert();
        publicationState().partialRenderSubmitRevert();
        accountState().partialRenderSubmitRevert();
        entitlementState().partialRenderSubmitRevert();
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
   ** <code>Application Instance</code>.
   ** <br>
   ** Leverage the operational binding <code>rollback</code> to do so.
   ** so.
   **
   ** @param  event              the {@link ActionEvent} object that
   **                            characterizes the action to perform.
   */
  public void revert(final @SuppressWarnings("unused") ActionEvent event) {
    // rollback any transaction
    ADF.executeAction(REVERT);
    revertAttribute();
    publicationState().revert();
    accountState().revert();
    entitlementState().revert();

    markClean();

    // if application instance name changed, update tab name and title
    final String name  = ADF.iteratorBindingValue(AttributeState.DETAIL_ITERATOR, ApplicationInstanceAdapter.NAME, String.class);
    raiseTaskFlowFeedbackEvent(String.format(ADF.resourceBundleValue(BUNDLE, "APP_TRAIN_REVERT"), name));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revertAttribute
  /**
   ** Reverts all changes belonging to the attribute region of an
   ** <code>Application Instance</code>.
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
  // Method:   clearScope
  /**
   ** Reverts all changes belonging to the publication region of an
   ** <code>Application Instance</code>.
   */
  public void clearScope() {
    this.scopeAssign.clear();
    this.scopeRemove.clear();
    this.scopeModify.clear();
  }
}