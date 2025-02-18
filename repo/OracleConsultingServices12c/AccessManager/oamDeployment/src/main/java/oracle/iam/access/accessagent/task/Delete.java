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

    System      :   Oracle Access Manager Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   Delete.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Delete.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.accessagent.task;

import oracle.hst.deployment.ServiceOperation;

////////////////////////////////////////////////////////////////////////////////
// class Delete
// ~~~~~ ~~~~~~
/**
 ** Invokes the web-server registration servlet to delete
 ** <code>Access Agent</code>s in <i>Oracle Access Manager</i> (<b>OAM</b>).
 ** <p>
 ** A <code>Access Agent</code> is a web-server plug-in for <i>Oracle Access
 ** Manager</i> (<b>OAM</b>) that intercepts HTTP requests and forwards them to
 ** the <code>Access Server</code> for authentication and authorization.
 ** <p>
 ** <i>Oracle Access Manager</i> (<b>OAM</b>) authenticates each user with a
 ** customer-specified authentication method to determine the identity and
 ** leverages information stored in the user identity store. <i>Oracle Access
 ** Manager</i> (<b>OAM</b>) authentication supports several authentication
 ** methods and different authentication levels. Resources with varying degrees
 ** of sensitivity can be protected by requiring higher levels of authentication
 ** that correspond to more stringent authentication methods.
 ** <p>
 ** When a user tries to access a protected application, the request is received
 ** by <b>OAM</b> which checks for the existence of the <b>SSO</b> cookie.
 ** <p>
 ** After authenticating the user and setting up the user context and token,
 ** <b>OAM</b> sets the <b>SSO</b> cookie and encrypts the cookie with the
 ** SSO Server key (which can be decrypted only by the SSO Engine).
 ** <p>
 ** Depending on the actions (responses in OAM 11g) specified for authentication
 ** success and authentication failure, the user may be redirected to a specific
 ** URL, or user information might be passed on to other applications through a
 ** header variable or a cookie value.
 ** <p>
 ** Based on the authorization policy and results of the check, the user is
 ** allowed or denied access to the requested content. If the user is denied
 ** access, she is redirected to another URL (specified by the administrator in
 ** <code>Access Agent</code> registration).
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Delete extends Servlet {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Delete</code> Ant task that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Delete() {
    // ensure inheritance
    super(ServiceOperation.delete);
  }
}