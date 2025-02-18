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

    File        :   DetachedCredentialCollector.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DetachedCredentialCollector.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2018-02-23  DSteding    First release version
*/

package oracle.iam.access.handler;

import java.io.IOException;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

////////////////////////////////////////////////////////////////////////////////
// class DetachedCredentialCollector
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>DetachedCredentialCollector</code> defines sepcific methods to receive
 ** an HTTP request and enrich this request with a cookie to store the login
 ** name of a user to authenticate.
 ** <br>
 ** The request in then redirected to the Oracle Access Manager Credential
 ** Collector
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DetachedCredentialCollector extends AbstractCredentialCollector {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////


  private static final String USERNAME = "ocs.dcc.username";
  private static final String PASSWORD = "ocs.dcc.passowrd";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-5224120329502658432")
  private static final long serialVersionUID = 5348349025571301600L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DetachedCredentialCollector</code> that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public DetachedCredentialCollector() {
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
    response.sendRedirect("/oam/server/auth_cred_submit");
  }
}