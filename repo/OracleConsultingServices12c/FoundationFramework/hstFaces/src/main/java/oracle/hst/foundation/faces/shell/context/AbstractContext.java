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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Shared Library
    Subsystem   :   Java Server Faces Foundation

    File        :   AbstractContext.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractContext.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-28-12  DSteding    First release version
*/

package oracle.hst.foundation.faces.shell.context;

import java.util.Map;

import java.io.IOException;

import javax.faces.event.ActionEvent;

import javax.faces.context.FacesContext;

import oracle.adf.view.rich.component.rich.RichPopup;

import oracle.hst.foundation.faces.ADF;
import oracle.hst.foundation.faces.JSF;

import oracle.hst.foundation.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// class AbstractContext
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Internal use only. Backing bean for library template page flows.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class AbstractContext implements java.io.Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String       BUNDLE           = "oracle.hst.foundation.faces.shell..bundle.foundation";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:6727155821796889653")
  private static final long serialVersionUID = -148169435186448220L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String            url2go;
  private String            action2do;
  private String            feedbackMessage  = "";

  private boolean           navigationRefresh;
  private boolean           dialogRefresh;
  private boolean           feedbackRefresh;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractContext</code> event handler that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected AbstractContext() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setUrl2go
  /**
   ** Sets the URL which is currently being called.
   **
   ** @param  value              the URL which is currently being called.
   */
  public void setUrl2go(final String value) {
    this.url2go = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUrl2go
  /**
   ** Retruns the URL which is currently being called.
   **
   ** @return                    the URL which is currently being called.
   */
  public String getUrl2go() {
    return this.url2go;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAction2do
  /**
   ** Sets the action which has to be performed.
   **
   ** @param  value              the action which has to be performed.
   */
  public void setAction2do(final String value) {
    this.action2do = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAction2do
  /**
   ** Retruns the action which has to be performed.
   **
   ** @return                    the action which has to be performed.
   */
  public String getAction2do() {
    return this.action2do;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setNavigationRefresh
  /**
   ** Sets the state for refresh condition of the navigation area.
   **
   ** @param  state              <code>true</code> if the navigation area needs
   **                            to be refreshed; otherwise
   **                            <code>false</code>.
   */
  public void setNavigationRefresh(final boolean state) {
    this.navigationRefresh = state;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getNavigationRefresh
  /**
   ** Returns <code>true</code> if the taskflow running in the navigation area
   ** needs to be refreshed.
   ** <p>
   ** After this method is invoked the refresh condition is reset always to
   ** <code>false</code>.
   **
   ** @return                    <code>true</code> if the taskflow running the
   **                            navigation area needs to be refreshed; otherwise
   **                            <code>false</code>.
   */
  public boolean getNavigationRefresh() {
    boolean rf = this.navigationRefresh;
    this.navigationRefresh = false;
    return rf;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDialogRefresh
  /**
   ** Sets the state for refresh condition of the dialog popup.
   **
   ** @param  state              <code>true</code> if the dialog popup needs
   **                            to be refreshed; otherwise
   **                            <code>false</code>.
   */
  public void setDialogRefresh(final boolean state) {
    this.dialogRefresh = state;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDialogRefresh
  /**
   ** Returns <code>true</code> if the taskflow running in a dialog needs to be
   ** refreshed.
   ** <p>
   ** After this method is invoked the refresh condition is reset always to
   ** <code>false</code>.
   **
   ** @return                    <code>true</code> if the taskflow running in a
   **                            dialog needs to be refreshed; otherwise
   **                            <code>false</code>.
   */
  public boolean getDialogRefresh() {
    boolean rf = this.dialogRefresh;
    this.dialogRefresh = false;
    return rf;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setFeedbackRefresh
  /**
   ** Sets the state for refresh condition of the feedback popup.
   **
   ** @param  state              <code>true</code> if the feedback popup needs
   **                            to be refreshed; otherwise
   **                            <code>false</code>.
   */
  public void setFeedbackRefresh(final boolean state) {
    this.feedbackRefresh = state;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getFeedbackRefresh
  /**
   ** Returns <code>true</code> if the feedback popup needs to be refreshed.
   ** <p>
   ** After this method is invoked the refresh condition is reset always to
   ** <code>false</code>.
   **
   ** @return                    <code>true</code> if the feedback popup needs
   **                            to be refreshed; otherwise
   **                            <code>false</code>.
   */
  public boolean getFeedbackRefresh() {
    boolean rf = this.feedbackRefresh;
    this.feedbackRefresh = false;
    return rf;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setFeedbackMessage
  /**
   ** Sets the message text to display in a feedback popup.
   **
   ** @param  value              the message text to display in a feedback
   **                            popup.
   */
  public void setFeedbackMessage(final String value) {
    this.feedbackMessage = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getFeedbackMessage
  /**
   ** Retruns the message text to display in a feedback popup.
   **
   ** @return                    the message text to display in a feedback
   **                            popup.
   */
  public String getFeedbackMessage() {
    return this.feedbackMessage;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setTaskFlowDialogPopup
  /**
   ** Sets the {@link RichPopup} in the current request scope.
   **
   ** @param  dialog             the {@link RichPopup} to set in the current
   **                            request scope.
   */
  public void setTaskFlowDialogPopup(final RichPopup dialog) {
    final Map<String, Object> mapping = JSF.externalContext().getRequestMap();
    mapping.put("SHELL_TASKFLOW_DIALOG", dialog);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getTaskFlowDialogPopup
  /**
   ** Returns the {@link RichPopup} configured for the current request scope.
   **
   ** @return                    the {@link RichPopup} configured for the
   **                            current request scope.
   */
  public RichPopup getTaskFlowDialogPopup() {
    final Map<String, Object> mapping = JSF.externalContext().getRequestMap();
    return (RichPopup)mapping.get("SHELL_TASKFLOW_DIALOG");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   signOut
  /**
   ** Handle the events occured in any UIXForm component managing the logout
   ** from a user session.
   **
   ** @param  event              the {@link ActionEvent} delivered from the
   **                            action occured in a UIXForm component.
   */
  public void signOut(final ActionEvent event) {
    final FacesContext context = JSF.context();
    final String       page    = (String)event.getComponent().getAttributes().get("signoutURL");
    final String       path    = String.format("%s/adfAuthentication?logout=true&end_url=/faces/%s", context.getExternalContext().getRequestContextPath(), StringUtility.isEmpty(page) ? context.getViewRoot().getViewId() : page);
    try {
      JSF.externalContext().redirect(path);
    }
    catch (IOException e) {
      ;
    }
    context.responseComplete();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localizedMessage
  /**
   ** Pulls a String resource from the specified resource bundle.
   **
   ** @param  key                the key for the desired string pattern.
   ** @param  arguments          the objects to be formatted and substituted.
   **
   ** @return                    resource choice or placeholder error String
   */
  public String localizedMessage(final String key, final Object... arguments) {
    return ADF.resourceBundle(BUNDLE).stringFormatted(key, arguments);
  }
}