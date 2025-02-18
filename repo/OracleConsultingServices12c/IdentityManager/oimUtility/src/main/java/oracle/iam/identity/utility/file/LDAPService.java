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

    System      :   Oracle Identity Manager Foundation Shared Library
    Subsystem   :   Common Shared LDAP Facilities

    File        :   LDAPService.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    LDAPService.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

import java.io.File;

import oracle.hst.foundation.SystemException;

import oracle.iam.identity.foundation.ldap.DirectoryConnector;
import oracle.iam.identity.foundation.ldap.DirectoryFeature;
import oracle.iam.identity.foundation.ldap.DirectoryFeatureFactory;

////////////////////////////////////////////////////////////////////////////////
// class LDAPService
// ~~~~~ ~~~~~~~~~~~
abstract class LDAPService {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final DirectoryConnector service;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** @param  serverName         Host name or IP address of the target system on
   **                            which the LDAP Server is installed
   ** @param  serverPort         the port the LDAP server is listening on
   **                            <br>
   **                            Default value for non-SSL: 389
   **                            Default value for SSL: 636
   ** @param  rootContext        the fully qualified domain name of the parent
   **                            or root organization.
   **                            <br>
   **                            For example, the root suffix.
   **                            <br>
   **                            Format: <code>ou=<i>ORGANIZATION_NAME</i>,dc=<i>DOMAIN</i></code>
   **                            <br>
   **                            Sample value: <code>ou=Adapters, dc=adomain</code>
   ** @param  principalName      the fully qualified domain name corresponding
   **                            to the administrator with LDAP administrator
   **                            rights.
   **                            <br>
   **                            Format: <code>cn=<i>ADMIN_LOGIN</i>,cn=Users,dc=<i>DOMAIN</i></code>
   ** @param  principalPassword  the password of the administrator account that
   **                            is used
   ** @param  secureSocket       specifies whether or not to use SSL to secure
   **                            communication between Oracle Identity Manager
   **                            and the LDAP Server.
   ** @param  relativeDN         whether all pathes are treated as relative to
   **                            the naming context of the connected LDAP
   **                            Server.
   ** @param  featurePath        the name of the file providing the target
   **                            system specific feature descriptor like
   **                            objectClasses, attribute id's etc.
   **
   **
   ** @throws SystemException    if the feature configuration cannot be created.
   */
  protected LDAPService(final String serverName, final int serverPort, final String rootContext, final String principalName, final String principalPassword, final boolean secureSocket, final boolean relativeDN, final String featurePath)
    throws SystemException {

    // ensure inheritance
    super();

    // initialize instance
    DirectoryFeature feature = DirectoryFeature.build(null);
    DirectoryFeatureFactory.configure(feature, new File(featurePath));
    this.service = new DirectoryConnector(null, serverName, serverPort, rootContext, principalName, principalPassword, secureSocket, relativeDN, "en", "US", "GMT+01:00", feature);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** @param  serverName         Host name or IP address of the target system on
   **                            which the LDAP Server is installed
   ** @param  serverPort         the port the LDAP server is listening on
   **                            <br>
   **                            Default value for non-SSL: 389
   **                            Default value for SSL: 636
   ** @param  rootContext        the fully qualified domain name of the parent
   **                            or root organization.
   **                            <br>
   **                            For example, the root suffix.
   **                            <br>
   **                            Format: <code>ou=<i>ORGANIZATION_NAME</i>,dc=<i>DOMAIN</i></code>
   **                            <br>
   **                            Sample value: <code>ou=Adapters, dc=adomain</code>
   ** @param  principalName      the fully qualified domain name corresponding
   **                            to the administrator with LDAP administrator
   **                            rights.
   **                            <br>
   **                            Format: <code>cn=<i>ADMIN_LOGIN</i>,cn=Users,dc=<i>DOMAIN</i></code>
   ** @param  principalPassword  the password of the administrator account that
   **                            is used
   ** @param  secureSocket       specifies whether or not to use SSL to secure
   **                            communication between Oracle Identity Manager
   **                            and the LDAP Server.
   ** @param  relativeDN         whether all pathes are treated as relative to
   **                            the naming context of the connected LDAP
   **                            Server.
   ** @param  feature            the target system specific feature descriptor
   **                            like objectClasses, attribute id's etc.
   **
   ** @throws SystemException    if the feature configuration cannot be created.
   */
  protected LDAPService(final String serverName, final int serverPort, final String rootContext, final String principalName, final String principalPassword, final boolean secureSocket, final boolean relativeDN, final DirectoryFeature feature)
    throws SystemException {

    // ensure inheritance
    super();

    // initialize instance
    this.service = new DirectoryConnector(null, serverName, serverPort, rootContext, principalName, principalPassword, secureSocket, relativeDN, "en", "US", "GMT+01:00", feature);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error (Loggable)
  /**
   ** Writes an normal error to the standard error channel.
   **
   ** @param  message            the message to log.
   */
  protected static void error(final String message) {
    System.err.println(message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warning
  /**
   ** Writes an warning error to the standard output channel.
   **
   ** @param  message            the message to log.
   */
  protected static void warning(final String message) {
    System.out.println(message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   info
  /**
   ** Writes an informational message to the standard output channel.
   **
   ** @param  message            the message to log.
   */
  protected static void info(final String message) {
    System.out.println(message);
  }
}