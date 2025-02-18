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
    Subsystem   :   Identity Manager Facility

    File        :   OIMServerHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the interface
                    OIMServerHandler.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.15  2012-02-06  DSteding    The name of the managed server is
                                               included now in the properties.
                                               Also MBean application and
                                               version are supported.
    11.1.1.3.37.60.28  2012-02-06  DSteding    Made the MDS storage of UI
                                               sandboxing configurable.
    11.1.1.3.37.60.32  2012-10-20  DSteding    Access to files changed from
                                               java.io.File to java.net.URL.
    11.1.1.3.37.60.33  2012-12-15  DSteding    Deployment Mode Feature
                                               introduced.
    11.1.1.3.37.60.34  2013-01-22  DSteding    MBean property Application
                                               removed due to it's always the
                                               same as the name of the WebLogic
                                               Managed Server.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.oim.parser;

import java.net.URL;

import oracle.jdeveloper.workspace.iam.parser.ANTFileHandler;

////////////////////////////////////////////////////////////////////////////////
// class OIMServerHandler
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** Oracle Identity Manager build file descriptor.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class OIMServerHandler extends ANTFileHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String PLATFORM         = "oim.server.type";
  public static final String PROTOCOL         = "oim.server.protocol";
  public static final String HOST             = "oim.server.host";
  public static final String PORT             = "oim.server.port";
  public static final String NAME             = "oim.server.name";
  public static final String HOME             = "oim.server.home";
  public static final String MODE             = "oim.server.production";
  public static final String USERNAME         = "oim.server.username";
  public static final String PASSWORD         = "oim.server.password";
  public static final String AUTHENTICATION   = "oim.server.authentication";

  public static final String PARTITION        = "oim.metadata.partition";
  public static final String SANDBOX          = "oim.sandbox.partition";

  public static final String METADATA         = "oim.mbean.metadata";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:3959600610643418496")
  private static final long  serialVersionUID = 2765220271844367742L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a new <code>OIMServerHandler</code> instance by converting the
   ** given pathname string into an abstract pathname.
   ** <p>
   ** If the given string is the empty string, then the result is the empty
   ** abstract pathname.
   **
   ** @param  file               the {@link URL} pathname name to the
   **                            {@link ANTFileHandler}.
   **
   ** @throws NullPointerException if the <code>name</code> argument is
   **                              <code>null</code>.
   */
  public OIMServerHandler(final URL file) {
    // ensure inheritance
    super(file);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   register
  /**
   ** Registers all path translation this instance can handle.
   */
  @Override
  protected void register() {
    // ensure inheritance
    super.register();

    // register local defined substitution placeholder
    registerProperty(PLATFORM);
    registerProperty(PROTOCOL);
    registerProperty(HOST);
    registerProperty(PORT);
    registerProperty(HOME);
    registerProperty(NAME);
    registerProperty(MODE);
    registerProperty(USERNAME);
    registerProperty(PASSWORD);
    registerProperty(AUTHENTICATION);
    registerProperty(PARTITION);
    registerProperty(SANDBOX);
    registerProperty(METADATA);
  }
}