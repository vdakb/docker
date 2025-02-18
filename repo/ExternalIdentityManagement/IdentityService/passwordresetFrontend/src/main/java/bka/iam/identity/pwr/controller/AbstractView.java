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

    File        :   AbstractView.java

    Compiler    :   Oracle JDeveloper 12c

    Purpose     :   This file implements the class
                    AbstractView.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
*/

package bka.iam.identity.pwr.controller;

import bka.iam.identity.pwr.model.BaseModel;
import bka.iam.identity.pwr.state.Session;
import bka.iam.platform.identity.pwr.type.Configuration;
import oracle.hst.platform.jsf.Message;
import oracle.hst.platform.jsf.state.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * A super class for the controllers to provide the common functionalities,
 * such as the Jmx extension support and messaging.
 *
 * @param <T> the type of the actual implementation, required by super class {@link ManagedBean}
 * @param <MODEL> a type that extends class {@link BaseModel}, to handle Jmx attributes
 */
public abstract class AbstractView<T extends ManagedBean<T>, MODEL extends BaseModel> extends ManagedBean<T> {

  /** The official serial version ID which says cryptically which version we're compatible with */
  private static final long    serialVersionUID = -8912586500822829319L;

  /** A {@link ServletContext} instance */
  @Inject
  private ServletContext       context;

  /**
   * Init, a {@link PostConstruct} life cycle listener
   * to initialize the correct image on the UIs based on the client's ip address.
   */
  @PostConstruct
  public void init() {
    final String method = "init";
    trace(method, METHOD_ENTRY);

    try {
      final Configuration      storage   = (Configuration) context.getAttribute("storage");
      final HttpServletRequest request   = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
      final String             requestIp = storage.requestIP(request);
      final String             symbol    = storage.symbol(requestIp);

      getModel().setRequestIp(requestIp);
      getModel().setSymbol(symbol);
    } finally {
      trace(method, METHOD_EXIT);
    }
  }

  /**
   * Returns the model instance of the page.
   * @return the model instance of the page.
   */
  public abstract MODEL getModel();

  /**
   * Shows an information level message on the UI.
   * @param detail the summary of the message.
   */
  protected void message(final String detail) {
    Message.show(Message.information(Message.resourceValue(Session.BUNDLE, "summary.info"), Message.resourceValue(Session.BUNDLE, detail)));
  }

  /**
   * Shows a warning level message on the UI.
   * @param detail the summary of the message.
   */
  protected void warn(final String detail) {
    Message.show(Message.warning(Message.resourceValue(Session.BUNDLE, "summary.warn"), Message.resourceValue(Session.BUNDLE, detail)));
  }

  /**
   * Shows an error level message on the UI.
   * @param detail the summary of the message.
   */
  protected void fault(final String detail) {
    Message.show(Message.error(Message.resourceValue(Session.BUNDLE, "summary.error"), Message.resourceValue(Session.BUNDLE, detail)));
  }

  /**
   * Shows an error level message on the UI.
   * @param detail the summary of the message.
   * @param params the parameters of the message.
   */
  protected void fault(final String detail, Object... params) {
    Message.show(Message.error(Message.resourceValue(Session.BUNDLE, "summary.error"), Message.resourceValue(Session.BUNDLE, detail), params));
  }

  /**
   * Shows an error level validation message on the UI.
   * @param detail the summary of the message.
   */
  protected void validation(final String detail) {
    Message.show(Message.error(Message.resourceValue(Session.BUNDLE, "summary.error"), detail));
  }
}
