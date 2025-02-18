/*
    Oracle Deutschland BV & Co. KG

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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Governance Extension
    Subsystem   :   Password Reset Administration

    File        :   ResetView.java

    Compiler    :   Oracle JDeveloper 12c

    Purpose     :   This file implements the class
                    ResetView.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
*/

package bka.iam.identity.pwr.controller;

import bka.iam.identity.pwr.api.PasswordResetFacade;
import bka.iam.identity.pwr.error.ServiceException;
import bka.iam.identity.pwr.error.ValidationException;
import bka.iam.identity.pwr.model.ForgotModel;
import bka.iam.identity.pwr.model.PasswordReset;
import bka.iam.identity.pwr.model.ResetModel;
import bka.iam.identity.pwr.state.Session;
import oracle.hst.platform.core.utility.StringUtility;
import oracle.hst.platform.jsf.Faces;
import oracle.hst.platform.jsf.Message;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * The ResetView managed bean to handle events and bindings of page <em>reset.jsp</em>.
 */
@ViewScoped
@Named("reset")
public class ResetView extends AbstractView<ResetView, ResetModel> {

  /**
   * The official serial version ID which says cryptically which version we're compatible with
   */
  private static final long   serialVersionUID = 4273124646093806869L;

  /** A {@link ResetModel} instance to handle all the UI bindings on the <em>reset.jsp</em> page. */
  private ResetModel          model            = new ResetModel();

  /** The value of the url parameter <em>pwrid</em>. */
  private String              pwrId;

  /** A {@link PasswordResetFacade} instance to perform the password reset operation. */
  @Inject
  private PasswordResetFacade facade;

  /**
   * A page load event handler to validate the url contains a <em>pwrid</em> parameter
   * which must not be expired. Also sets validness of the form and initializes the e-mail value.
   */
  @SuppressWarnings("unused") //frontend
  public void initialize() {
    final String method = "initialize";
    trace(method, METHOD_EXIT);

    try {
      final LocalDateTime       timestamp     = LocalDateTime.now();
      final Map<String, String> parameters    = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
      this.pwrId                              = parameters.get("pwrid");
      if (this.pwrId != null) {
        final PasswordReset     passwordReset = facade.lookup(this.pwrId);

        //verify the link is valid
        if (timestamp.isAfter(passwordReset.created().plusMinutes(10))) {
          debug(method, "The link " + passwordReset.requestId() + " is expired.");
          fault("reset.link.expired");
          return;
        }

        this.model.setEmail(passwordReset.email());
        this.model.setValid(true);

        String policyRules = facade.policyRules(passwordReset.email(), Faces.externalContext().getRequestLocale());
        if (policyRules == null) {
          policyRules = Message.resourceValue(Session.BUNDLE, "reset.password.hint");
        }

        this.model.setPasswordHint(policyRules);
      } else {
        debug(method, "The link doesn't contain 'pwrid' parameter.");
        fault("reset.parameter.error");
      }
    } catch (ServiceException e) {
      fatal(method, e);
      fault(e.getCode(), e.getParams());
    } finally {
      trace(method, METHOD_EXIT);
    }
  }

  /**
   * A listener method for submit event of <em>reset-form</em> on page <em>reset.jsp</em>.
   * Validates the url (request) is still valid, and checks the password mismatch as well.
   * Then performs the password reset operation, and cleans up the used password reset request.
   */
  @SuppressWarnings("unused") //frontend
  public void submit() {
    final String method = "submit";
    trace(method, "entering");

    try {
      final LocalDateTime timestamp      = LocalDateTime.now();
      //verify the password has not been changed meanwhile
      final PasswordReset passwordReset  = facade.lookup(this.pwrId);

      //verify the link is still valid
      if (timestamp.isAfter(passwordReset.created().plusMinutes(10))) {
        debug(method, "The link " + passwordReset.requestId() + " is already expired.");
        fault("reset.link.expired");
        return;
      }

      if (StringUtility.unequal(model.getPassword(), model.getConfirmation())) {
        debug(method, "Password and Confirmation don't match.");
        fault("reset.password.mismatch");
        return;
      }

      facade.reset(model.getLogin(), model.getEmail(), model.getPassword());
      message("reset.feedback.success");
      facade.delete(passwordReset.id());
    } catch (ValidationException e) {
      fatal(method, e);
      validation(e.getMessage());
    } catch (ServiceException e) {
      fatal(method, e);
      fault(e.getCode(), e.getParams());
    } finally {
      trace(method, "exiting");
    }
  }

  /**
   * Returns the model instance of the page.
   * @return the model instance of the page.
   */
  public ResetModel getModel() {
    return model;
  }

  /**
   * Sets the model instance of the page.
   * @param model a {@link ForgotModel} instance
   */
  public void setModel(ResetModel model) {
    this.model = model;
  }
}