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

    Copyright © 2020 All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Frontend Extension
    Subsystem   :   Special Account Request

    File        :   AccountView.java

    Compiler    :   JDK 1.8

    Author      :   sylvert.bernet@oracle.com

    Purpose     :   This file implements the class
                    AccountView.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2020-03-03  SBernet    First release version
*/

package bka.iam.identity.ui.request.backing;

import java.util.List;
import java.util.ArrayList;

import javax.faces.model.SelectItem;

import org.apache.myfaces.trinidad.util.ComponentReference;

import oracle.adf.view.rich.component.rich.nav.RichButton;

import oracle.hst.foundation.faces.JSF;
import oracle.hst.foundation.faces.ADF;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.frontend.AbstractManagedBean;

import bka.iam.identity.ui.RequestMessage;
import bka.iam.identity.ui.RequestException;

import bka.iam.identity.ui.request.Adapter;

import bka.iam.identity.ui.resource.Bundle;

import bka.iam.identity.ui.request.model.Template;
import bka.iam.identity.ui.request.model.Environment;

////////////////////////////////////////////////////////////////////////////////
// class AccountView
// ~~~~~ ~~~~~~~~~~~
/**
 ** Declares methods to submit EFBS account creation from a bulk of Request
 ** Template.
 **
 ** @author  sylvert.bernet@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class AccountView extends AbstractManagedBean {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String          ACTION_ERROR     = "error";
  private static final String          ACTION_SUBMIT    = "submit";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:6321083366147214176")
  private static final long            serialVersionUID = 7942872985678875533L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private transient Adapter            model            = null;
  private transient ComponentReference submit           = null; // RichCommandButton

  //////////////////////////////////////////////////////////////////////////////
  // Constructor
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AccountView</code> state bean that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AccountView() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   setModel
  /**
   ** Sets the value of the model property.
   ** <p>
   ** Method needs to be declared as <b><code>public</code></b> because it used
   ** in the UI.
   **
   ** @param  value              the value of the train property.
   **                            Allowed object is {@link Adapter}.
   */
  public void setModel(final Adapter value) {
    this.model = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getModel
  /**
   ** Returns the value of the model property.
   ** <p>
   ** Method needs to be declared as <b><code>public</code></b> because it used
   ** in the UI.
   **
   ** @return                    the value of the train property.
   **                            Possible object is {@link Adapter}.
   */
  public Adapter getModel() {
    return this.model;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isPrepared
  /**
   ** Callback method to prepares the request model for the selected
   ** beneficiary.
   ** <p>
   ** Method is called from the header component of the dialog component.
   **
   ** @return                    always <code>true</code>.
   */
  public boolean isPrepared() {
    try {
      this.model.prepare();
    }
    catch (RequestException e) {
      raiseException(e);
    }
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isSubmitDisbaled
  /**
   ** Return <code>true</code> if the model is not in the state to submit.
   ** <p>
   ** In other words the template selected in the UI can only be submitted if
   ** the evaluation of the actauls has at least one elemenet to request.
   **
   ** @return                    <code>true</code> if the model is not in the
   **                            state to submit; otherwise <code>false</code>.
   */
  public boolean isSubmitDisbaled() {
    final Template actual = this.model.getActual();
    return actual == null || ((actual.getEntitlement() == null ? 0 : actual.getEntitlement().size()) + (actual.getApplication() == null ? 0 : actual.getApplication().size()) == 0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setAction
  /**
   ** Sets the UI component which renders the submit action component in the
   ** popup region.
   ** <br>
   ** When you store a reference to a UI Component in a bean, the complete
   ** component tree (children and parent) are kept in memory.
   ** This causes the ADF application to consume a unnecessary big amount of
   ** memory.
   ** <b>Note</b>:
   ** <br>
   ** Unfortunately when you use the auto generation feature for component
   ** binding in JDeveloper, it will generates the references as depicted below:
   ** <pre>
   **   public void setAction(final RichButton component) {
   **     this.submit = component;
   **   }
   ** </pre>
   ** This should be changed using the much more memory efficient
   ** {@link ComponentReference} class:
   ** <pre>
   **   private ComponentReference environment;
   **   ...
   **   public void setAction(final RichButton component) {
   **     this.submit = ComponentReference.newUIComponentReference(component);
   **   }
   ** </pre>
   **
   ** @param  component          the UI component which renders the submit
   **                            action component in the popup region.
   **                            <br>
   **                            Allowed object is {@link RichButton}.
   */
  public void setAction(final RichButton component) {
    this.submit = ComponentReference.newUIComponentReference(component);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getAction
  /**
   ** Returns the UI component which renders the submit action component in the
   ** popup region.
   **
   ** @return                    the UI component which renders the submit
   **                            action component in the popup region.
   **                            <br>
   **                            Possible object is {@link RichButton}.
   */
  public RichButton getAction() {
    return (this.submit != null) ? (RichButton)this.submit.getComponent() : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setEnvironmentValue
  /**
   ** Sets the current selected environement in the model.
   ** <p>
   ** Any exception occured is reported in the UI.
   ** <p>
   ** There is no need to raise a feedback event if he value is set due to the
   ** partial rendering request issued by the fraemwork due to the dependency
   ** of the template component.
   **
   ** @param  value              the environement to select in the model.
   **                            Allowed object {@link String}.
   */
  public void setEnvironmentValue(final String value) {
    try {
      this.model.setEnvironment(value);
    }
    catch (RequestException e) {
      // raise error into UI
      raiseException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getEnvironmentValue
  /**
   ** Returns the current selected environement from the model.
   **
   ** @return                    the current selected environement from the
   **                            model.
   **                            Possible object {@link String}.
   */
  public String getEnvironmentValue() {
    return this.model.getEnvironment();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getEnvironmentList
  /**
   ** Returns the collection of configured environements.
   **
   ** @return                    a {@link List} binding with the availbale
   **                            environements.
   **                            <br>
   **                            Possible object {@link List} where each element
   **                            is of type {@link SelectItem}.
   */
  public List<SelectItem> getEnvironmentList() {
    final List<SelectItem> model = new ArrayList<SelectItem>();
    for (Environment cursor : this.model.getEnvironments()) {
      model.add(new SelectItem(cursor.getId(), JSF.valueFromExpression(cursor.getLabel(), String.class)));
    }
    return model;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setTemplateValue
  /**
   ** Sets the current selected template in the model.
   ** <p>
   ** Any exception occured is reported in the UI.
   **
   ** @param  value              the template to select in the model.
   **                            Allowed object {@link String}.
   */
  public void setTemplateValue(final String value) {
    try {
      this.model.setTemplate(value);
      ADF.partialRender("gc42");
    }
    catch (RequestException e) {
      // raise error into UI
      raiseException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getTemplateValue
  /**
   ** Returns the current selected template from the model.
   **
   ** @return                    the current selected template from the
   **                            model.
   **                            Possible object {@link String}.
   */
  public String getTemplateValue() {
    return this.model.getTemplate();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getTemplateList
  /**
   ** Returns the collection of configured templates.
   **
   ** @return                    a {@link List} binding with the availbale
   **                            templates.
   **                            <br>
   **                            Possible object {@link List} where each element
   **                            is of type {@link SelectItem}.
   */
  public List<SelectItem> getTemplateList() {
    final List<SelectItem> model = new ArrayList<SelectItem>();
    for (Template cursor : this.model.getTemplates()) {
      model.add(new SelectItem(cursor.getId(), JSF.valueFromExpression(cursor.getLabel(), String.class)));
    }
    return model;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getRequest
  /**
   ** Returns the template ready for request.
   **
   ** @return                    the template ready for request.
   **                            <br>
   **                            Possible object is {@link Template}.
   */
  public Template getRequest() {
    return this.model.getActual();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLabel
  /**
   ** Evaluates the given expression to a string value.
   **
   ** @param  expression         the expression to evaluate.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the evaluated value.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getLabel(final String expression) {
    return JSF.valueFromExpression(expression, String.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   submit
  /**
   ** Submit the account and entitlements depending of the selected environement
   ** and account type and leave taskflow.
   ** <br>
   ** If submit process failed, a message is raised on UI.
   **
   ** @return                    the navigation outcome action.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String submit() {
    // request account and dependencies
    try {
      final String id = this.model.submit();
      if (id != null)
        raiseTaskFlowFeedbackEvent(StringUtility.isEmpty(id) ? Bundle.string(RequestMessage.REQUEST_COMPLETE_SUCCESS) : Bundle.format(RequestMessage.REQUEST_SUBMIT_SUCCESS, id));
      return ACTION_SUBMIT;
    }
    catch (RequestException e) {
      // raise error into UI
      raiseException(e);
      return ACTION_ERROR;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   raiseException
  /**
   ** In case of exception, this method raise a popUp message which indicate the
   ** raising error message.
   **
   ** @param  cause              the causing exeption that should be displayed
   **                            on UI.
   */
  public void raiseException(final RequestException cause) {
    fatal(cause);
    ADF.rootPort().clearException();
    raiseError(cause.getLocalizedMessage());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   raiseError
  /**
   ** In case of exception, this method raise a popup message which indicate the
   ** raising error message.
   **
   ** @param  message            the error message that should be displayed
   **                            on UI.
   */
  public static void raiseError(final String message) {
    JSF.showErrorMessage(message);
  }
}