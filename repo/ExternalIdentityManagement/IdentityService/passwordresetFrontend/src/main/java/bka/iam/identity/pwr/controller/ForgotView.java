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

    File        :   ForgotView.java

    Compiler    :   Oracle JDeveloper 12c

    Purpose     :   This file implements the class
                    ForgotView.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
*/

package bka.iam.identity.pwr.controller;

import bka.iam.identity.pwr.api.PasswordForgotFacade;
import bka.iam.identity.pwr.error.ServiceException;
import bka.iam.identity.pwr.model.ForgotModel;
import nl.captcha.Captcha;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 * The ForgotView managed bean to handle events and bindings of page <em>forgot.jsp</em>.
 */
@ViewScoped
@Named("forgot")
public class ForgotView extends AbstractView<ForgotView, ForgotModel> {

  /** The official serial version ID which says cryptically which version we're compatible with */
  private static final long    serialVersionUID = -5920836106204350583L;

  /** Name of the header which defines where the request comes from */
  private static String        HOST_HEADER       = "PWRHOST";

  /** A {@link ForgotModel} instance to handle all the UI bindings on the <em>forgot.jsp</em> page. */
  private ForgotModel          model            = new ForgotModel();

  /** A {@link PasswordForgotFacade} instance which performs the e-mail sending operation. */
  @Inject
  private PasswordForgotFacade facade;

  /**
   * A listener method for submit event of <em>forgot-form</em> on page <em>forgot.jsp</em>.
   * The handler:
   * <ul>
   *  <li>validates the captcha is not expired,</li>
   *  <li>validates the entered captcha value is correct,</li>
   *  <li>performs the message sending operation,</li>
   *  <li>evaluates the result of the process,</li>
   *  <li>handles any errors during the execution.</li>
   * </ul>
   */
  @SuppressWarnings("unused") //frontend
  public void send() {
    String method = "send";
    trace(method, METHOD_ENTRY);

    try {
      final HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
      final Captcha            captcha = (Captcha) request.getSession().getAttribute(Captcha.NAME);
      final String             header  = request.getHeader(HOST_HEADER);
      debug(method, HOST_HEADER + " header: " + header);
      //if the session expires, captcha will be null
      if (captcha == null) {
        debug(method, "Captcha was expired, reload the page.");
        warn("forgot.captcha.expired");
        return;
      }

      if (captcha.isCorrect(model.getCaptcha())) {
        debug(method, "The entered captcha value is correct. Sending password reset e-mail to: " + model.getEmail());
        boolean messageSent = facade.requestPasswordReset(model.getEmail(), header);

        if (messageSent) {
          info("Password reset link was successfully sent to: " + model.getEmail());
          message("forgot.feedback.success");
        } else {
          debug(method, "The process has been finished without error, but the e-mail was not sent.");
          fault("forgot.notification.failed");
        }
      } else {
        debug(method, "The entered captcha value is incorrect. Entered value: " + model.getCaptcha() + ", correct answer: " + captcha.getAnswer());
        fault("forgot.captcha.failed");
      }
    } catch (ServiceException e) {
      fatal(method, e);
      fault(e.getCode(), e.getParams());
    } finally {
      trace(method, METHOD_EXIT);
    }
  }

  public void audio() {
    this.model.setAudio(!this.model.isAudio());
  }


  /**
   * Returns the model instance of the page.
   * @return the model instance of the page.
   */
  public ForgotModel getModel() {
    return model;
  }

  /**
   * Sets the model instance of the page.
   * @param model a {@link ForgotModel} instance
   */
  public void setModel(ForgotModel model) {
    this.model = model;
  }
}
