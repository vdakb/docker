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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Manager Library
    Subsystem   :   Credential Collector 12c

    File        :   AbstractCredentialCollector.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractCredentialCollector.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2018-02-23  DSteding    First release version
*/

package oracle.iam.access.handler;

import java.io.PrintWriter;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oracle.iam.access.entity.Account;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractCredentialCollector
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>AbstractCredentialCollector</code> defines sepcific methods to receive
 ** an HTTP request and enrich this request with a cookie sto store the login
 ** name of a user to authenticate.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
abstract class AbstractCredentialCollector extends HttpServlet {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:4999065543214353959")
  private static final long serialVersionUID = 7162889188610914356L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final String username;
  private final String password;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractCredentialCollector</code> that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected AbstractCredentialCollector(final String username, final String password) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.username = username;
    this.password = password;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   username
  /**
   ** Returns the value of the <code>username</code> property.
   **
   ** @return                    the value of the <code>username</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String username() {
    return this.username;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   password
  /**
   ** Returns the value of the <code>password</code> property.
   **
   ** @return                    the value of the <code>password</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String password() {
    return this.password;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   init (HttpServlet)
  /**
   ** Initialize the servlet.
   ** <br>
   ** Called by the servlet container to indicate to a servlet that the servlet
   ** is being placed into service.
   ** <br>
   ** This implementation stores the ServletConfig object it receives from the
   ** servlet container for alter use. When overriding this form of the method,
   ** call {@link super.init(config)}.
   **
   ** @param   servletconfig      The <code>ServletConfig</code> object that
   **                             contains configutation information for this
   **                             servlet.
   **
   ** @throws  ServletException   If an exception occurs that interrupts the
   **                             servlet's normal operation.
   */
  @Override
  public void init(ServletConfig config)
    throws ServletException {

    // ensure inheritance
    super.init(config);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode: destroy (HttpServlet)
  /**
   ** This method is called when the cartridge terminates.
   ** <br>
   ** Do some cleanup operations here. Closes the database sessions and all
   ** other connections.
   */
  @Override
  public void destroy() {
    // ensure inheritance
    super.destroy();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   doGet (overridden)
  /**
   ** Method which has to be implemented for a servlet (JServlet-Cartridge).
   ** Creates the ClientRequest/ClientRequest objects and invokes the
   ** processGet-Method.
   **
   ** @param  request            the {@link HttpServletRequest} object that
   **                            represents the request the client makes of the
   **                            servlet.
   ** @param  response           the {@link HttpServletResponse} object that
   **                            represents the response the servlet returns to
   **                            the client.
   */
  @Override
  public final void doGet(final HttpServletRequest request, final HttpServletResponse response)
    throws IOException {
    // process the request
    processRequest(request, response);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   doPost (overridden)
  /**
   ** Invokes the processRequest method
   **
   ** @param  request            the {@link HttpServletRequest} object that
   **                            represents the request the client makes of the
   **                            servlet.
   ** @param  response           the {@link HttpServletResponse} object that
   **                            represents the response the servlet returns to
   **                            the client.
   */
  @Override
  public final void doPost(final HttpServletRequest request, final HttpServletResponse response)
    throws IOException {
    // process the request
    processRequest(request, response);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   processRequest
  /**
   **
   **
   ** @param  request            the {@link HttpServletRequest} object that
   **                            represents the request the client makes of the
   **                            servlet.
   ** @param  response           the {@link HttpServletResponse} object that
   **                            represents the response the servlet returns to
   **                            the client.
   ** @throws IOException
   ** @throws ServletException
   */
  protected void processRequest(final HttpServletRequest request, final HttpServletResponse response)
    throws IOException {

    response.setContentType("text/html;charset=UTF-8");
    PrintWriter out = response.getWriter();
    final String username = request.getParameter("username");
    final String remember = request.getParameter("remember");
    // we check, if "remember" parameter is not null, then set above "username" value accordingly
    if (remember != null) {
      applyCookie(response, username, 5);
    }
    else {
      applyCookie(response, "anonymous", 0);
    }
    out.close();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   findAccount
  /**
   ** If the request contains no cookie, create a new session and return the
   ** id of the session as the value of the cookie.
   **
   ** @param  request            the {@link HttpServletRequest} object that
   **                            represents the request the client makes of the
   **                            servlet.
   **
   ** @return                    the {@link Account}.
   */
  protected final Account findAccount(HttpServletRequest request) {
    final String username = findCookie(request, this.username);
    final String password = findCookie(request, this.password);

    Account account = null;
    if (!username.isEmpty() && !password.isEmpty())
      account = new Account(username, password);
    return account;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   findCookie
  /**
   ** If the request contains no cookie, create a new session and return the
   ** id of the session as the value of the cookie.
   **
   ** @param  request            the {@link HttpServletRequest} object that
   **                            represents the request the client makes of the
   **                            servlet.
   ** @param  cookieName         the name of the {@link Cookie} to get the value
   **                            for.
   **
   ** @return                    the cookie value.
   */
  protected final String findCookie(HttpServletRequest request, final String cookieName) {
    String result = null;
    if (request != null) {
      // HTTPServlet mode: search in the provided cookies for occurence of the
      // cookie where we are insterested on
      final Cookie[] cookie = request.getCookies();
      if (cookie != null)
        for (int i = 0; i < cookie.length; i++)
          if ((cookie[i].getName() != null) && (cookie[i].getName().equals(cookieName))) {
            result = cookie[i].getValue();
            break;
          }
	  }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   applyCookie
  /**
   ** @param  response           the {@link HttpServletResponse} object that
   **                            represents the response the servlet returns to
   **                            the client.
   ** @param  value              the value to be set on the {@link Cookie}.
   ** @param  maxAge             the lifetime of the {@link Cookie}.
   */
  protected final void applyCookie(HttpServletResponse response, String value, final int maxAge) {
    // save the username and password in a cookie
    final Cookie cookie = new Cookie(this.username, value);
//    cookie.setPath("/");
    response.addCookie(cookie);
    // request to remove cookie immediate
    cookie.setMaxAge(maxAge);
    response.addCookie(cookie);
  }
}