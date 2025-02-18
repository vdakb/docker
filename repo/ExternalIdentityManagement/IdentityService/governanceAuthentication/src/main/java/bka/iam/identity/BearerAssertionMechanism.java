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
    Subsystem   :   Identity Governance Service

    File        :   BearerAssertionMechanism.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    BearerAssertionMechanism.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity;

import javax.servlet.http.HttpServletRequest;

import javax.enterprise.inject.Typed;

import javax.security.enterprise.authentication.mechanism.http.AutoApplySession;

////////////////////////////////////////////////////////////////////////////////
// class BearerAssertionMechanism
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>BearerAssertionMechanism</code> implements {@link AssertionMechanism}
 ** for obtaining a caller's credentials from an Authentication Provider, using
 ** the HTTP protocol where necessary.
 ** <p>
 ** This is used to help in securing Servlet endpoints, including endpoints that
 ** may be build on top of Servlet like JAX-RS endpoints and JSF views.
 ** <br>
 ** It specifically is not used for endpoints such as remote EJB beans or (JMS)
 ** message driven beans.
 ** <p>
 ** A {@link AssertionMechanism} is essentially a Servlet specific and CDI
 ** enabled version of the ServerAuthModule that adheres to the Servlet
 ** Container Profile. See the JASPIC spec for further details on this.
 */
@AutoApplySession
@Typed(BearerAssertionMechanism.class)
public class BearerAssertionMechanism extends AssertionMechanism {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String  HEADER    = "Authorization";
  public static final String  BEARER    = "Bearer";
  public static final String  PARAMETER = "access_token";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a {@link BearerAssertionMechanism} that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   ** <p>
   ** This is followed by a call to the init() method.
   */
  public BearerAssertionMechanism() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   token (AssertionMechanism)
  /**
   ** Factory method to create a bearer token authentication
   ** <code>Credential</code>.
   **
   ** @param  request            the context of the incomming request.
   **                            <br>
   **                            Allowed object is
   **                            {@link HttpServletRequest}.
   **
   ** @return                    the bearer token Authentication
   **                            <code>Credential</code> populated with the
   **                            given <code>token</code>.
   **                            <br>
   **                            Possible object is
   **                            {@link AssertionCredential}.
   */
  @Override
  protected AssertionCredential token(final HttpServletRequest request) {
    String token = extract(request, HEADER, BEARER);
    // if authorization header is not used, check query parameter where token
    // can be passed as well
    if (token == null) {
      token = request.getParameter(PARAMETER);
    }
    // check if the Authorization header is valid
    // it must not be null and must be prefixed with "Bearer" plus a whitespace
    // the authentication scheme comparison must be case-insensitive
    return (token == null) ? null : new AssertionCredential(token.trim());
  }
}