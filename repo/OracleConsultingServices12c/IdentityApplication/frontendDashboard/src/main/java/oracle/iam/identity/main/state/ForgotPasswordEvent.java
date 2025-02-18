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

    File        :   ForgotPasswordEvent.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ForgotPasswordEvent.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2016-03-03  DSteding    First release version
*/

package oracle.iam.identity.main.state;

import oracle.adf.controller.ControllerContext;
import oracle.adf.controller.TaskFlowContext;
import oracle.adf.controller.TaskFlowTrainModel;
import oracle.adf.controller.TaskFlowTrainStopModel;
import oracle.adf.controller.ViewPortContext;

import oracle.adf.model.binding.DCBindingContainer;

import oracle.adf.model.binding.DCControlBinding;

import oracle.hst.foundation.faces.JSF;

import oracle.iam.identity.frontend.AbstractStateBean;

import oracle.iam.ui.common.model.forgotpassword.ForgotPasswordDataProvider;

////////////////////////////////////////////////////////////////////////////////
// class ForgotPasswordEvent
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** Declares methods the user interface service provides to navigate across
 ** modules.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
 */
public class ForgotPasswordEvent extends AbstractStateBean {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-7853209959847446794")
  private static final long serialVersionUID = 2873886077982756937L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String            username;
  private String            password;
  private String            message;

  private Integer           challenges       = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ForgotPasswordEvent</code> backing bean that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ForgotPasswordEvent() {
    // ensure inheritance
    super();

    // initialize instance attributes
    initializeChallenges();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setUsername
  /**
   ** Sets the value of the username property.
   **
   ** @param  value              the value of the username property.
   **                            Allowed object is {@link String}.
   */
  public void setUsername(final String value) {
    this.username = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUsername
  /**
   ** Returns the value of the username property.
   **
   ** @return                    the value of the username property.
   **                            Possible object is {@link String}.
   */
  public String getUsername() {
    return this.username;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPassword
  /**
   ** Sets the value of the password property.
   **
   ** @param  value              the value of the password property.
   **                            Allowed object is {@link String}.
   */
  public void setPassword(final String value) {
    this.password = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPassword
  /**
   ** Returns the value of the password property.
   **
   ** @return                    the value of the password property.
   **                            Possible object is {@link String}.
   */
  public String getPassword() {
    return this.password;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setMessage
  /**
   ** Sets the value of the message property.
   **
   ** @param  value              the value of the message property.
   **                            Allowed object is {@link String}.
   */
  public void setMessage(final String value) {
    this.message = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getMessage
  /**
   ** Returns the value of the message property.
   **
   ** @return                    the value of the message property.
   **                            Possible object is {@link String}.
   */
  public String getMessage() {
    return this.message;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceBundle (AbstractStateNean)
  /**
   ** Returns the <code>ResourceBundle</code> name used by a state bean.
   **
   ** @return                    the <code>ResourceBundle</code> name used by a
   **                            state bean.
   */
  protected String resourceBundle() {
    return "oracle.iam.identity.bundle.Main";
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   navigateNext
  /**
   ** Navigates to the next step in the train of the forgott password task flow
   */
  public String navigateNext() {
    // Add event code here...
    final ControllerContext      controller  = ControllerContext.getInstance();
    final ViewPortContext        currentView = controller.getCurrentViewPort();
    final TaskFlowContext        taskFlow    = currentView.getTaskFlowContext();
    final TaskFlowTrainModel     model       = taskFlow.getTaskFlowTrainModel();
    final TaskFlowTrainStopModel current     = model.getCurrentStop();
    final TaskFlowTrainStopModel next        = model.getNextStop(current);
    return next.getOutcome();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   checkPassword
  public String checkPassword() {
    // Add event code here...
    String returnString = "error";
    final DCBindingContainer container = JSF.valueFromExpression("#{bindings}", DCBindingContainer.class);
    final DCControlBinding   binding   = container.findCtrlBinding("password");
    final String             password  = binding.toString();
    if (password.compareTo(this.password) == 0) {
      returnString = "save";
    }
    else {
      setMessage("Password Matching error");
    }
    return returnString;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   initializeChallenges
  /**
   ** Initialize the amount of challenge question sto answer.
   */
  private void initializeChallenges() {
    if (this.challenges == null) {
      final ForgotPasswordDataProvider dataProvider = new ForgotPasswordDataProvider();
      this.challenges = dataProvider.getNoOfAvailableQuestions();
    }
  }
}
