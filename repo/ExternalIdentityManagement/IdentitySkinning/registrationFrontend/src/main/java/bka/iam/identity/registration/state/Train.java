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

    Copyright © 2021 All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Frontend Extension
    Subsystem   :   Self Service Registration

    File        :   Train.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Train.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2021-13-09  DSteding    First release version
*/

package bka.iam.identity.registration.state;

import javax.faces.event.ActionEvent;

import javax.faces.application.NavigationHandler;

import oracle.iam.ui.view.handler.CreateTrain1HandlerInterface;

import oracle.hst.foundation.faces.JSF;

import oracle.iam.identity.frontend.train.AbstractFlow;

////////////////////////////////////////////////////////////////////////////////
// class Train
// ~~~~~ ~~~~~
/**
 ** Declares methods the user interface service provides to navigate across
 ** pages to create or manage <code>Self Registration Request</code>s.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Train extends    AbstractFlow
                   implements CreateTrain1HandlerInterface {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String BUNDLE           = "bka.iam.identity.bundle.Registration";

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
  // Method:   submit
  /**
   ** Create or updates the <code>Scheduler Job</code>, open a newly created
   ** <code>Scheduler Job</code> in the edit mode and close the current task
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
  public String submit(){
    return ERROR;
  }
}