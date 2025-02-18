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

package oracle.iam.identity.sysprov.rce.state;

import javax.faces.event.ActionEvent;

import oracle.iam.ui.view.handler.CreateTrain1HandlerInterface;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.foundation.faces.JSF;
import oracle.hst.foundation.faces.ADF;

import oracle.iam.identity.frontend.train.AbstractFlow;

import oracle.iam.identity.sysprov.rce.backing.DataState;
import oracle.iam.identity.sysprov.rce.backing.AccountState;
import oracle.iam.identity.sysprov.rce.backing.HistoryState;
import oracle.iam.identity.sysprov.rce.backing.IdentityState;
import oracle.iam.identity.sysprov.rce.backing.AttributeState;
import oracle.iam.identity.sysprov.schema.ReconciliationEventAdapter;
import oracle.iam.identity.sysprov.schema.ReconciliationEventStatus;

////////////////////////////////////////////////////////////////////////////////
// class Train
// ~~~~~ ~~~~~
/**
 ** Declares methods the user interface service provides to navigate across
 ** pages to create or manage <code>IT Resource Type</code>s.
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


  public static final String  DETAIL_ITERATOR  = "EventIterator";

  private static final String BUNDLE           = "oracle.iam.identity.bundle.Provisioning";

  // the official serial version ID which says cryptically which version we're
  // compatible with

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
  // Method:   isOpen
  /**
   ** Returns <code>true</code> if the event isn't succeded.
   ** <p>
   ** Method needs to be declared as <b><code>public</code></b> because it used
   ** in the UI.
   **
   ** @return                    <code>true</code> if the event isn't succeded;
   **                            otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public boolean isOpen() {
    // an open event is anything where the status isn't succeeded
    final ReconciliationEventStatus status = ReconciliationEventStatus.from(ADF.iteratorBindingValue(DETAIL_ITERATOR, ReconciliationEventAdapter.STATUS, String.class));
    return !ReconciliationEventAdapter.SUCCESS.contains(status);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isLink
  /**
   ** Returns <code>true</code> if the event is in no match found state.
   ** <p>
   ** Method needs to be declared as <b><code>public</code></b> because it used
   ** in the UI.
   **
   ** @return                    <code>true</code> if the event is in no match
   **                            found state otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public boolean isLink() {
    // an event that can be link is anything where the status isn't
    // No User Match Found
    final ReconciliationEventStatus status = ReconciliationEventStatus.from(ADF.iteratorBindingValue(DETAIL_ITERATOR, ReconciliationEventAdapter.STATUS, String.class));
    return ReconciliationEventStatus.MATCH_NOT_FOUND == status;
  }

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
  // Methode:   attributeState
  /**
   ** Returns the backing bean of the attribute region.
   **
   ** @return                    the backing bean of the attribute region.
   */
  private AttributeState attributeState() {
    try {
      return JSF.valueFromExpression("#{backingBeanScope.endpointTypeAttribute}", AttributeState.class);
    }
    // for debugging purpose only catch everything to spool to error log
    catch (Throwable t) {
      t.printStackTrace(System.err);
      throw t;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   dataState
  /**
   ** Returns the backing bean of the reconciliation data region.
   **
   ** @return                    the backing bean of the reconciliation data region.
   **                            Possible object is {@link ParameterState}.
   */
  private DataState dataState() {
    try {
      return JSF.valueFromExpression("#{backingBeanScope.eventData}", DataState.class);
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
   ** Returns the backing bean of the matched account region.
   **
   ** @return                    the backing bean of the matched account region.
   **                            Possible object is {@link EndpointState}.
   */
  private AccountState accountState() {
    try {
      return JSF.valueFromExpression("#{backingBeanScope.accountState}", AccountState.class);
    }
    // for debugging purpose only catch everything to spool to error log
    catch (Throwable t) {
      t.printStackTrace(System.err);
      throw t;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   identityState
  /**
   ** Returns the backing bean of the matched identity region.
   **
   ** @return                    the backing bean of the matched identity region.
   **                            Possible object is {@link EndpointState}.
   */
  private IdentityState identityState() {
    try {
      return JSF.valueFromExpression("#{backingBeanScope.identityState}", IdentityState.class);
    }
    // for debugging purpose only catch everything to spool to error log
    catch (Throwable t) {
      t.printStackTrace(System.err);
      throw t;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   historyState
  /**
   ** Returns the backing bean of the event history region.
   **
   ** @return                    the backing bean of the event history region.
   **                            Possible object is {@link EndpointState}.
   */
  private HistoryState historyState() {
    try {
      return JSF.valueFromExpression("#{backingBeanScope.historyState}", HistoryState.class);
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
   */
  @Override
  public void finishActionHandler(final ActionEvent event) {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reevaluateEvent
  /**
   ** The action listener to be called then the reevaluate button is clicked.
   **
   ** @param  event              the {@link ActionEvent} object that
   **                            characterizes the action to perform.
   **                            <br>
   **                            Allowed object is {@link ActionEvent}.
   */
  public void reevaluateEvent(final ActionEvent event) {
    final Long eventKey = Long.valueOf(ADF.pageFlowScopeStringValue(ReconciliationEventAdapter.PK));
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   closeEvent
  /**
   ** The action listener to be called then the close button is clicked.
   **
   ** @param  event              the {@link ActionEvent} object that
   **                            characterizes the action to perform.
   **                            <br>
   **                            Allowed object is {@link ActionEvent}.
   */
  public void closeEvent(final ActionEvent event) {
    final Long eventKey = Long.valueOf(ADF.pageFlowScopeStringValue(ReconciliationEventAdapter.PK));
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   linkEvent
  /**
   ** The action listener to be called then the link button is clicked.
   **
   ** @param  event              the {@link ActionEvent} object that
   **                            characterizes the action to perform.
   **                            <br>
   **                            Allowed object is {@link ActionEvent}.
   */
  public void linkEvent(final ActionEvent event) {
    final Long eventKey = Long.valueOf(ADF.pageFlowScopeStringValue(ReconciliationEventAdapter.PK));
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refreshEvent
  /**
   ** The action listener to be called then the refresh button is clicked.
   **
   ** @param  event              the {@link ActionEvent} object that
   **                            characterizes the action to perform.
   **                            <br>
   **                            Allowed object is {@link ActionEvent}.
   */
  public void refreshEvent(final ActionEvent event) {
    final Long eventKey = Long.valueOf(ADF.pageFlowScopeStringValue(ReconciliationEventAdapter.PK));
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetch
  /**
   ** Initialize the train flow to manage an existing
   ** <code>Reconciliation Event</code>.
   ** <br>
   ** Leverage the operational binding <code>eventFetch</code> to do so.
   */
  public void fetch() {
    ADF.executeOperation("eventFetch");
  }
}