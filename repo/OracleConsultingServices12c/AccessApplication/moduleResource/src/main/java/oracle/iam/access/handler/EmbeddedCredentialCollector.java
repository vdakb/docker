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

    File        :   EmbeddedCredentialCollector.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    EmbeddedCredentialCollector.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2018-02-23  DSteding    First release version
*/

package oracle.iam.access.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.RequestDispatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

////////////////////////////////////////////////////////////////////////////////
// class EmbeddedCredentialCollector
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>EmbeddedCredentialCollector</code> defines sepcific methods to receive
 ** an HTTP request and enrich this request with a cookie to store the login
 ** name of a user to authenticate.
 ** <br>
 ** The request in then forwarded to the Oracle Access Manager Credential
 ** Collector hence this servlet needs to be deployed in the same managed server
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class EmbeddedCredentialCollector extends AbstractCredentialCollector {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String USERNAME = "ocs.ecc.username";
  private static final String PASSWORD = "ocs.ecc.passowrd";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-8556219331537806010")
  private static final long serialVersionUID = -6092208278487731617L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>EmbeddedCredentialCollector</code> that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public EmbeddedCredentialCollector() {
    // ensure inheritance
    super(USERNAME, PASSWORD);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   processRequest (overridden)
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
  @Override
  protected void processRequest(final HttpServletRequest request, final HttpServletResponse response)
    throws IOException {

    // ensure inheritance
    super.processRequest(request, response);

    // we forward the altered response object to "/oam/server/auth_cred_submit"
    final RequestDispatcher view = request.getRequestDispatcher("/oam/server/auth_cred_submit");
    try {
      view.forward(request, response);
    }
    catch (ServletException e) {

    }
  }
}