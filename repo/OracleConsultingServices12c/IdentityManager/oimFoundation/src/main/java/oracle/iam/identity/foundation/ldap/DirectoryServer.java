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

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   DirectoryServer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DirectoryServer.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.ldap;

////////////////////////////////////////////////////////////////////////////////
// class DirectoryServer
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** The <code>DirectoryServer</code> is a value holder for the failover
 ** configuration of a LDAP.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   3.0.0.0
 */
public class DirectoryServer {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final String serverName;
  protected final int    serverPort;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DirectoryServer</code> task adapter.
   **
   ** @param  serverName         Host name or IP address of the target system on
   **                            which the LDAP Server is installed
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  serverPort         the port the LDAP server is listening on
   **                            <br>
   **                            Default value for non-SSL: 389
   **                            Default value for SSL: 636
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  private DirectoryServer(final String serverName, final int serverPort) {
    // ensure inheritance
    super();

    // initailize instance
    this.serverName = serverName;
    this.serverPort = serverPort;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an empty <code>DirectoryServer</code> task
   ** adapter.
   **
   ** @param  serverName         Host name or IP address of the target system on
   **                            which the LDAP Server is installed
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  serverPort         the port the LDAP server is listening on
   **                            <br>
   **                            Default value for non-SSL: 389
   **                            Default value for SSL: 636
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    an empty <code>IT Resource</code> instance
   **                            wrapped as
   **                            <code>IdentityServerResource</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>IdentityServerResource</code>.
   */
  public static DirectoryServer build(final String serverName, final int serverPort) {
    return new DirectoryServer(serverName, serverPort);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns a string representation of this instance.
   **
   ** @return                    the string representation of this instance.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String toString() {
	return String.format("%s:%d", this.serverName, this.serverPort);
  }
}