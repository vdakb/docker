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

    File        :   ServerConfig.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    ServerConfig.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.model;

import oracle.javatools.data.HashStructure;

import oracle.jdeveloper.workspace.iam.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// abstract class ServerConfig
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~
/**
 ** <code>ServerPreference</code> a generic server and runtime implementation
 ** that can adjust its behaviour by a server type definition.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public abstract class ServerConfig extends PlatformConfig {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String SERVER = "server";
  public static final String HOME   = "home";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ServerConfig</code>.
   ** <br>
   ** This constructor is protected to prevent other classes to use
   ** "new ServerConfig()" and enforces use of the public factory method
   ** below.
   **
   ** @param  structure          the data provider to initialize the instance.
   */
  protected ServerConfig(final HashStructure structure) {
    // ensure inheritance
    super(structure);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   server
  /**
   ** Sets the name of the managed server in a Oracle WebLogic environment.
   **
   ** @param  server             the name of the managed server in a Oracle
   **                            WebLogic environment.
   */
  public void server(final String server) {
    this._hash.putString(SERVER, server);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   server
  /**
   ** Returns the name of the managed server in a Oracle WebLogic environment.
   **
   ** @return                    the name of the managed server in a Oracle
   **                            WebLogic environment.
   */
  public String server() {
    return string(SERVER, defaultServer());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   defaultServer
  /**
   ** Returns the default name of the managed server in a Oracle WebLogic
   ** environment.
   **
   ** @return                    the default name of the managed server in a
   **                            Oracle WebLogic environment.
   */
  public abstract String defaultServer();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   home
  /**
   ** Called to inject the argument for instance attribute <code>home</code>.
   **
   ** @param  home               the home folder the Application Server is
   **                            installed.
   */
  public void home(final String home) {
    this._hash.putString(HOME, home);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   home
  /**
   ** Returns the home the Application Server is listening on.
   **
   ** @return                    the home folder the Application Server is
   **                            installed.
   */
  public String home() {
    // the second parameter to string() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return string(HOME, StringUtility.EMPTY);
  }
}