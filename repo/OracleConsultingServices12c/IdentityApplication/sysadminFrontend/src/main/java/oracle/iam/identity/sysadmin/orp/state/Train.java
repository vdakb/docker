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

package oracle.iam.identity.sysadmin.orp.state;

import javax.faces.event.ActionEvent;

import oracle.adf.view.rich.util.ResetUtils;

import oracle.adf.view.rich.component.rich.layout.RichPanelFormLayout;

import oracle.hst.foundation.faces.JSF;
import oracle.hst.foundation.faces.ADF;

import oracle.iam.identity.frontend.train.AbstractFlow;

import oracle.iam.identity.sysadmin.orp.backing.EventState;
import oracle.iam.identity.sysadmin.orp.backing.AttributeState;

////////////////////////////////////////////////////////////////////////////////
// class Train
// ~~~~~ ~~~~~
/**
 ** Declares methods the user interface service provides to navigate across
 ** pages to create or manage <code>Orchestration Process</code>s.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
 */
public class Train extends AbstractFlow {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String BUNDLE           = "oracle.iam.identity.bundle.Administration";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-1196349201977088556")
  private static final long   serialVersionUID = -5195720853096903315L;

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
  // Methode:   attributeState
  /**
   ** Returns the backing bean of the attribute region.
   **
   ** @return                    the backing bean of the attribute region.
   */
  private AttributeState attributeState() {
    try {
      return JSF.valueFromExpression("#{backingBeanScope.orchestrationAttribute}", AttributeState.class);
    }
    // for debugging purpose only catch everything to spool to error log
    catch (Throwable t) {
      t.printStackTrace(System.err);
      throw t;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   eventState
  /**
   ** Returns the backing bean of the event region.
   **
   ** @return                    the backing bean of the event region.
   */
  private EventState eventState() {
    try {
      return JSF.valueFromExpression("#{backingBeanScope.orchestrationEvent}", EventState.class);
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
  // Method:   fetch
  /**
   ** Initialize the train flow to manage an existing
   ** <code>Orchestration Process</code>.
   ** <br>
   ** Leverage the operational binding <code>processFetch</code> to do so.
   */
  public void fetch() {
    ADF.executeOperation("processFetch");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revert
  /**
   ** Revert all changes made on train flow belonging to an
   ** <code>Orchestration Process</code>.
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
    eventState().revert();

    markClean();

    raiseTaskFlowFeedbackEvent(String.format(ADF.resourceBundleValue(BUNDLE, "ORC_TRAIN_REVERT"), "name"));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revertAttribute
  /**
   ** Reverts all changes belonging to the attribute region of an
   ** <code>Orchestration Process</code>.
   */
  public void revertAttribute() {
    ADF.executeOperation("refreshAttribute");
    // need to explicitly reset orchestration and catalog attributes forms this
    // is because Revert button on that page is immediate hence component
    // submittedValues need to be reset explicitly
    final RichPanelFormLayout form = getFormLayout();
    if (form != null) {
      ResetUtils.reset(form);
      ADF.partialRender(form);
      attributeState().partialRenderSubmitRevert();
    }
  }
}