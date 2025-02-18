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

    Copyright © 2011. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facilities

    File        :   PlatformConfig.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    PlatformConfig.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.model;

//import oracle.ide.util.Namespace;

import oracle.javatools.data.HashStructure;

import oracle.jdeveloper.workspace.iam.utility.StringUtility;

import oracle.jdeveloper.workspace.iam.parser.SCPServerHandler;

////////////////////////////////////////////////////////////////////////////////
// abstract class PlatformConfig
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~
/**
 ** <code>PlatformConfig</code> a generic server and runtime implementation
 ** that can adjust its behaviour by a server platform definition.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public abstract class PlatformConfig extends BuildPropertyAdapter {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String HOST = "host";
  public static final String PORT = "port";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>PlatformConfig</code>.
   ** <br>
   ** This constructor is protected to prevent other classes to use
   ** <code>new PlatformConfig()</code> and enforces use of the public factory
   ** method below.
   **
   ** @param  structure          the data provider to initialize the instance.
   */
  protected PlatformConfig(final HashStructure structure) {
    // ensure inheritance
    super(structure);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   platform
  /**
   ** Sets the <code>platform</code> attribute for the RMI/JNDI provider.
   **
   ** @param  platform           the value for the attribute
   **                            <code>platform</code> used as the RMI/JNDI
   **                            provider.
   */
  public abstract void platform(final String platform);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   platform
  /**
   ** Returns the <code>platform</code> attribute for the RMI/JNDI provider.
   **
   ** @return                    the <code>platform</code> attribute for the
   **                            RMI/JNDI provider.
   */
  public abstract String platform();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   host
  /**
   ** Sets the host name where the service provider is deployed
   **
   ** @param  host               the host name where the service provider is
   **                            deployed.
   */
  public void host(final String host) {
    this._hash.putString(HOST, host);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   host
  /**
   ** Returns the host name where the service provider is deployed
   **
   ** @return                    the host name where the service provider is
   **                            deployed.
   */
  public String host() {
    // the second parameter to string() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return string(HOST, StringUtility.EMPTY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   port
  /**
   ** Called to inject the argument for instance attribute <code>port</code>.
   **
   ** @param  port               the port theservice provider is listening on
   **                            to set.
   */
  public void port(final String port) {
    this._hash.putString(PORT, port);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   port
  /**
   ** Returns the port the service provider is listening on.
   **
   ** @return                    the port the service provider is listening
   **                            on.
   */
  public String port() {
    // The second parameter to getString() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return string(PORT, StringUtility.EMPTY);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   username
  /**
   ** Called to inject the argument for parameter <code>username</code>.
   **
   ** @param  username           the name of the administrative user.
   **
   */
  public abstract void username(final String username);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   username
  /**
   ** Returns the name of the security principal of the server used to connect
   ** to.
   **
   ** @return                    the name of the security principal server used
   **                            to connect to.
   */
  public abstract String username();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   password
  /**
   ** Called to inject the argument for parameter <code>password</code>.
   **
   ** @param  password           the password of the administrative user.
   */
  public abstract void password(final String password);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   password
  /**
   ** Returns the password of the security principal of the server used to
   ** connect to.
   **
   ** @return                    the password of the security principal server
   **                            used to connect to.
   */
  public abstract String password();
}