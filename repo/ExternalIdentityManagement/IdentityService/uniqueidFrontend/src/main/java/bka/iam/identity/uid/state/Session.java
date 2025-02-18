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
    Subsystem   :   Unique Identifier Administration 

    File        :   Session.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Session.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.uid.state;

import java.util.Locale;

import java.io.IOException;

import java.security.Principal;

import javax.ejb.EJB;

import javax.inject.Inject;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import javax.faces.context.ExternalContext;

import javax.security.enterprise.SecurityContext;

import oracle.hst.platform.jsf.Faces;
import oracle.hst.platform.jsf.Message;

import oracle.hst.platform.jsf.state.ManagedBean;

import oracle.hst.platform.jpa.PersistenceException;

import bka.iam.identity.igs.model.User;

import bka.iam.identity.igs.api.UserFacade;

////////////////////////////////////////////////////////////////////////////////
// class Session
// ~~~~~ ~~~~~~~
/**
 ** The <code>Session</code> bean to handle the UID Generator.
 ** <p>
 **
 ** The goal of this class is to facilitate access to the current user profile.
 ** <br>
 ** This is achieved by injecting the {@link SecurityContext} to make the
 ** Principal instance of the current user available in the class and by
 ** defining some facades to this principal's methods (i.e., to getPrincipal,
 ** getDisplayName, and getPicture).
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
//@SessionScoped
//@Named(Session.NAME)
//@SuppressWarnings("oracle.jdeveloper.cdi.not-proxyable-bean")
public class Session extends ManagedBean<Session> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public  static final String NAME             = "sessionState";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:8514063699756543849")
  private static final long   serialVersionUID = 3795589266253216829L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The locale grabbed from the browser settings. */
  private final Locale        locale;
  private final Preference    preference       = new Preference();

  /** Additional properties of the logged in user. */
  private User                detail           = null;

  /** The context to obtain the proper user identity. */
  @Inject
  @SuppressWarnings({"oracle.jdeveloper.cdi.unsatisfied-dependency", "oracle.jdeveloper.java.field-not-serializable"})
  SecurityContext             securityContext;

  /**
   ** The business facade injected to populate the additinal properties of the
   ** logged in user.
   */
  @EJB
  private UserFacade          facade;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a managed <code>Session</code> bean that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Session() {
    // ensure inheritance
    super();

    // initlaize instance attributes
    this.locale = Faces.externalContext().getRequestLocale();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   isAuthenticated
  /**
   ** Returns <code>true</code> if the user logged in.
   **
   ** @return                    <code>true</code> if user has been
   **                            authenticated; otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public boolean isAuthenticated() {
    return getPrincipal() != null;  
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getLocale
  /**
   ** Returns the <code>Session</code> {@link Locale}.
   **
   ** @return                    the <code>Session</code> {@link Locale}.
   **                            <br>
   **                            Possible object is {@link Locale}.
   */
  public Locale getLocale() {
    return this.locale;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getLanguage
  /**
   ** Returns the language code of the <code>Session</code> obtained from the
   ** <code>User Agent</code> locale.
   **
   ** @return                    the language code of the <code>Session</code>
   **                            obtained from the <code>User Agent</code>
   **                            locale.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getLanguage() {
    return this.locale.getLanguage();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getPrincipal
  /**
   ** Returns the <code>Session</code> {@link User}.
   **
   ** @return                    the <code>Session</code> {@link User}.
   **                            <br>
   **                            Possible object is {@link User}.
   */
  public Principal getPrincipal() {
    return this.securityContext.getCallerPrincipal();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDisplayName
  /**
   ** Returns the surrogate of <code>firstName</code> and <code>lastName</code>
   ** properties of the <code>User</code> principal.
   **
   ** @return                    the surrogate of <code>firstName</code> and
   **                            <code>lastName</code> properties of the
   **                            <code>User</code> principal.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getDisplayName() {
    return getDisplayName("ltr");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDisplayName
  /**
   ** Returns the surrogate of <code>firstName</code> and <code>lastName</code>
   ** properties of the <code>User</code> principal.
   **
   ** @param  ltr                specifies the base direction of directionally
   **                            neutral text for the value.
   **                            <br>
   **                            Possible values:
   **                            <ul>
   **                              <li>ltr - Left-to-right text
   **                              <li>rtl - tight-to-left text
   **                            </ul>
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the surrogate of <code>firstName</code> and
   **                            <code>lastName</code> properties of the
   **                            <code>User</code> principal.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getDisplayName(final String ltr) {
    if (!isAuthenticated()) {
      if (!(signin() && isAuthenticated())) {
        try {
          Faces.externalContext().dispatch("/error/401.jsf");
        }
        catch (IOException e) {
          Message.error("Ooooops", e.getLocalizedMessage());
        }
      }
    }  
    return ltr.equalsIgnoreCase("rtl") ? String.format("%s, %s", "this.detail.lastName()", "this.detail.firstName()") : String.format("%s %s", "this.detail.firstName()", "this.detail.lastName()");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDisplayRole
  /**
   ** Returns the most valued role of the logged in {@link User} principal.
   **
   ** @return                    the most valued role of the logged in
   **                            {@link User} principal.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getDisplayRole() {
    return Faces.request().isUserInRole("Administrator") ? "Administrator" : "Viewer";
  }
 
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDisplayAvatar
  /**
   ** Returns the most valued avatar image of the logged in {@link User}
   ** principal.
   **
   ** @return                    the most valued avatar image of the logged in
   **                            {@link User} principal.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getDisplayAvatar() {
    return "john-doe.png";
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPreference
  /**
   ** Returns the <code>preference</code> property of the <code>Session</code>.
   **
   ** @return                    the <code>preference</code> property of the
   **                            <code>Session</code>.
   **                            <br>
   **                            Possible object is {@link Preference}.
   */
  public Preference getPreference() {
    return this.preference;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   signin
  /**
   ** Fetch the user information form the identity store based on the user
   ** authenticated by the JAAS login module.
   **
   ** @return                    the outcome of this action.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public boolean signin() {
    final Principal principal = getPrincipal();
    // prevent bogus state
    if (principal == null)
      return false;

    try {
      this.detail = this.facade.lookup(principal.getName());
      return true;
    }
    catch (PersistenceException e) {
      e.printStackTrace();
      return false;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   signout
  /**
   ** Logging out from the application using the request and session information
   ** of the user authenticated by the JAAS login module.
   **
   ** @return                    the outcome of this action.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String signout()  {
    final ExternalContext    context = Faces.externalContext();
    final HttpServletRequest request = (HttpServletRequest)context.getRequest();
    final Cookie[]           cookie  = request.getCookies();
    // invalidate the session in context
    context.invalidateSession();
    // remove any cookie we got from the user agent
    if (cookie != null) {
      for (Cookie cursor : cookie) {
        cursor.setMaxAge(0);
        Faces.response().addCookie(cursor);
      }
    }
    return "signout";
  }
}