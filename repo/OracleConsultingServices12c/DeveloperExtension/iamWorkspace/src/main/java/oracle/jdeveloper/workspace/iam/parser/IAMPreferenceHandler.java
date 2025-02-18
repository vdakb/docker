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
    Subsystem   :   Identity and Access Management Facility

    File        :   IAMPreferenceHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    IAMPreferenceHandler.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.32  2012-10-20  DSteding    Access changed from java.io.file
                                               to java.net.URL.
    11.1.1.3.37.60.37  2013-06-24  DSteding    Fixed missing declaration of
                                               jdk.compiler in global workspace
                                               property file iam-prefeneces.xml
    11.1.1.3.37.60.61  2014-10-29  DSteding    Included file extensions cpx and
                                               dcx in the pattern of files to
                                               copy to output directory if ANT
                                               compiles the project.
                                               Removed javax.servlet_1.0.0.0_2-5.jar
                                               and javax.jsp_1.2.0.0_2-1.jar
                                               from oracle.fmw.wsri hence
                                               oracle.fmw.j2ee and oracle.fmw.wsri
                                               needs to be specified in the
                                               build classpath.
                                               New path oracle.fmw.jsp to
                                               include glassfish.el_1.0.0.0_2-1.jar
                                               in buld classpath if required.
    11.1.1.3.37.60.69  2017-31-01  DSteding    Changed compatibility annotation
                                               accordingliy to the build.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.parser;

import java.net.URL;

////////////////////////////////////////////////////////////////////////////////
// class IAMPreferenceHandler
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** IAM preference source file Descriptor.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class IAMPreferenceHandler extends ANTFileHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String  JDK_HOME         = "jdk.home";
  public static final String  JDK_VERSION      = "jdk.version";

  public static final String  JCE_PASSPHRASE   = "jce.passphrase";

  public static final String  FMW_BASE         = "fmw.base";
  public static final String  OCS_BASE         = "ocs.base";
  public static final String  WKS_BASE         = "wks.base";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:332327234078648218")
  private static final long   serialVersionUID = -4219626145050924899L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a new <code>IAMPreferenceHandler</code> instance by converting the
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
  public IAMPreferenceHandler(final URL file) {
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
  protected void register() {
    registerProperty(JDK_HOME);
    registerProperty(JDK_VERSION);
    registerProperty(JCE_PASSPHRASE);
    registerProperty(FMW_BASE);
    registerProperty(OCS_BASE);
    registerProperty(WKS_BASE);
  }
}