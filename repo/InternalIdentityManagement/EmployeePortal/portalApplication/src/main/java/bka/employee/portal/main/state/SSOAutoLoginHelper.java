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

    Copyright © 2020. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Employee Portal
    Subsystem   :   Application Console

    File        :   SSOAutoLoginHelper.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SSOAutoLoginHelper.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2020-02-23  DSteding    First release version
*/

package bka.employee.portal.main.state;

import java.util.Set;

import java.io.IOException;

import javax.security.auth.login.LoginException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oracle.iam.passwordmgmt.vo.AutoLoginResponse;

import oracle.iam.platform.utils.URLUtil;

import oracle.iam.ui.platform.utils.SessionUtils;
import oracle.iam.ui.platform.utils.TaskFlowUtils;
import oracle.iam.ui.platform.utils.ADFContextUtils;

import oracle.iam.platform.context.MultiTenancyUtility;

import oracle.iam.ui.platform.sso.api.AutoLoginProvider;
import oracle.iam.ui.platform.sso.oam.impl.OAMAutoLoginProviderFactory;

import oracle.hst.foundation.faces.JSF;
import oracle.hst.foundation.faces.ADF;

////////////////////////////////////////////////////////////////////////////////
// class SSOAutoLoginHelper
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** Declares methods the user interface service provides to navigate across
 ** modules.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class SSOAutoLoginHelper extends oracle.iam.ui.platform.view.backing.SSOAutoLoginHelper {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String PARAMETER_BACKURL             = "backUrl";

  private final static String DEFAULT_REDIRECTION_URL       = "/epp/faces/home";
  private static final String ADF_LOGOUT_SERVLET_URL_SUFFIX = "/epp/adfAuthentication?logout=true&end_url=";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:2652257938315164082")
  private static final long serialVersionUID = 7887389517306428880L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String              redirectionUrl                = DEFAULT_REDIRECTION_URL;
  private String              useLoginBackUrl               = DEFAULT_REDIRECTION_URL;
  private String              accountLockedPageBackUrl      = DEFAULT_REDIRECTION_URL;
  private String              registrationPageBackUrl       = DEFAULT_REDIRECTION_URL;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SSOAutoLoginHelper</code> backing bean that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public SSOAutoLoginHelper() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getSignoutRedirectionUrl (overridden)
  /**
   ** This method returns the signout redirection URL for change password page.
   ** <br>
   ** In FA/OAM integration scenario, it would be the <code>backUrl</code>
   ** parameter in the request
   ** <br>
   ** In vanilla OIM case, it would be /sysauthz/faces/home
   */
  @Override
  public String getSignoutRedirectionUrl() {
    final Object backUrl = SessionUtils.get(PARAMETER_BACKURL);
    if (backUrl != null && !backUrl.toString().isEmpty()) {
      return ADF_LOGOUT_SERVLET_URL_SUFFIX + (String)backUrl;
    }
    return ADF_LOGOUT_SERVLET_URL_SUFFIX + DEFAULT_REDIRECTION_URL;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getAccountLockedPageBackUrl (overridden)
  /**
   ** This method returns the redirection URL for the account locked task flow.
   ** <br>
   ** In FA/OAM integration scenario, it would be the <code>backUrl</code>
   ** parameter in the request
   ** <br>
   ** In vanilla OIM case, it would be /sysauthz/faces/home
   */
  @Override
  public String getAccountLockedPageBackUrl() {
    return this.accountLockedPageBackUrl;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getAccountLockedPageBackUrl (overridden)
  /**
   ** This method returns the redirection URL for the self-registration request
   ** task flow.
   ** <br>
   ** In FA/OAM integration scenario, it would be the <code>backUrl</code>
   ** parameter in the request
   ** <br>
   ** In vanilla OIM case, it would be /sysauthz/faces/home
   */
  @Override
  public String getRegistrationPageBackUrl() {
    return this.registrationPageBackUrl;
  }

  public void setUseLoginBackUrl(final String useLoginBackUrl) {
    this.useLoginBackUrl = useLoginBackUrl;
  }

  public String getUseLoginBackUrl() {
    return this.useLoginBackUrl;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  // This method is called from user-login taskflow, in order to store backUrl
  public String readBackUrlParamForUsrLoginPage() {
    final String url = JSF.request().getParameter(PARAMETER_BACKURL);
    if (url != null && !url.isEmpty())
      this.useLoginBackUrl = url;

    return "";
  }

  // This method is called from forgot-password taskflow, in order to store backUrl
  public String readBackUrlParam() {
    final String url = JSF.request().getParameter(PARAMETER_BACKURL);
    if (url != null && !url.isEmpty())
      this.redirectionUrl = url;
    else if (TaskFlowUtils.getPageFlowScopeParameter(PARAMETER_BACKURL) != null)
      // since the request doesn't contain a backUrl param, read it from
      // pageFlowScope
      // This case occurs if forgot-password taskflow is triggered from
      // account-locked taskflow
      this.redirectionUrl = ADF.pageFlowScopeStringValue(PARAMETER_BACKURL);

    return "";
  }

  // This method is called from account-locked taskflow, in order to store backUrl
  public String readBackUrlParamForAccountLockedPage() {
    final String url = JSF.request().getParameter(PARAMETER_BACKURL);
    if (url != null && !url.isEmpty())
      this.accountLockedPageBackUrl = url;

    return "";
  }

  //This method is called from self/track registration taskflows, in order to store backUrl
  public String readBackUrlParamForRegistration() {
    final String url = JSF.request().getParameter(PARAMETER_BACKURL);
    if (url != null && !url.isEmpty())
      this.registrationPageBackUrl = url;

    return "";
  }

  public String performAutoLoginForChangePasswordFlow(final String userLogin, final String password) {
    AutoLoginResponse autoLoginResponse = null;
    try {
    final Object url = SessionUtils.get(PARAMETER_BACKURL);
      if (url != null && !url.toString().isEmpty())
        this.redirectionUrl = url.toString();
      autoLoginResponse = doAutologin(JSF.request(), this.redirectionUrl, userLogin, password, Boolean.FALSE);
    }
    catch (LoginException e) {
      e.printStackTrace();
    }
    redirect(autoLoginResponse);
    return "";
  }

  public String performAutoLoginForForgotPasswordFlow() {
    String  userLogin = ADF.pageFlowScopeStringValue("userLogin");
    String  password  = ADF.pageFlowScopeStringValue("reTypePassword");
    Boolean flow      = "true".equals(ADF.current().getRequestScope().get("passwordResetSuccessSSOFlow"));
    if (MultiTenancyUtility.isMultiTenancyEnv()) {
      userLogin = generateName(JSF.valueFromExpression("#{pageFlowScope.tenantName}", String.class), userLogin);
    }

    AutoLoginResponse autoLoginResponse = null;
    try {
      autoLoginResponse = doAutologin(JSF.request(), this.redirectionUrl, userLogin, password, flow);
    }
    catch (LoginException e) {
      e.printStackTrace();
    }

    redirect(autoLoginResponse);

    if (flow)
      ADF.pageFlowScope().put("userLogin", ADF.current().getRequestScope().get("userLoginId"));
    return "";
  }

  private void redirect(final AutoLoginResponse autoLoginResponse) {
    HttpServletResponse servletResponse = JSF.response();

    if (autoLoginResponse != null) {
      for (Cookie authnCookie : autoLoginResponse.getHigherAuthCookies()) {
        servletResponse.addCookie(authnCookie);
      }
      if (autoLoginResponse.getRedirectionUrl() != null)
        this.redirectionUrl = autoLoginResponse.getRedirectionUrl();
    }

    try {
      servletResponse.sendRedirect(servletResponse.encodeURL(this.redirectionUrl));
      JSF.context().responseComplete();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  private AutoLoginResponse doAutologin(HttpServletRequest request, String protectedUrl, String userLoginId, String userPasswd, Boolean passwordResetSuccessSSOFlow)
    throws LoginException {

    String userLogin = userLoginId;
    // incase of password reset success, dont get the configured Login ID as the
    // user will input configured Login Id only
    if (!passwordResetSuccessSSOFlow) {
      // if customLoginIdAttribute is configured then retrive that login
      // information
      userLogin = ADFContextUtils.getConfiguredUserLogin(userLoginId);
    }

    final AutoLoginResponse response = new AutoLoginResponse();
    // Initialize resource host, port and URL
    final String resourceHost = request.getServerName();
    final int resourcePort    = request.getServerPort();
    final String clientIP     = request.getRemoteAddr();

    // if protected url is not complete, form one
    if (protectedUrl != null && !URLUtil.isFullURL(protectedUrl))
      protectedUrl = URLUtil.getAbsoluteURL(resourceHost, resourcePort, protectedUrl);

    Set<Cookie> higherAuthCookies = null;
    try {
      // Check for sso scenario
      if (isIsSSOEnabled()) {
        // Initialize sso autologin handler
        AutoLoginProvider oamAutoLoginProvider = OAMAutoLoginProviderFactory.getAutoLoginProvider();
        // do sso autologin
        oamAutoLoginProvider.login(protectedUrl, userLogin, userPasswd, clientIP);
        // return higher auth cookies
        higherAuthCookies = oamAutoLoginProvider.getAuthenticationCookies(resourceHost, resourcePort);
        response.setHigherAuthCookies(higherAuthCookies);
        response.setRedirectionUrl(oamAutoLoginProvider.getRedirectionURL());
      }
    }
    catch (Exception e) {
      throw new LoginException("Error while autologin " + e);
    }
    return response;
  }
}