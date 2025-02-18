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
    Subsystem   :   Internet Directory Facility

    File        :   PreferenceHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the interface
                    PreferenceHandler.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.32  2012-10-20  DSteding    Access to files changed from
                                               java.io.File to java.net.URL.
    @version 12.2.1.3.42.60.74  2017-31-01  DSteding    Changed compatibility annotation
                                               accordingliy to the build.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.oid.parser;

import java.net.URL;

import oracle.jdeveloper.workspace.iam.parser.ANTFileHandler;

////////////////////////////////////////////////////////////////////////////////
// class PreferenceHandler
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** OID preference source file Descriptor.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class PreferenceHandler extends ANTFileHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String OID_RELEASE      = "oid.release";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:6846586029583194950")
  private static final long  serialVersionUID = -8401934552546912658L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a new <code>PreferenceHandler</code> instance by converting the
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
  public PreferenceHandler(final URL file) {
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
    registerProperty(OID_RELEASE);
  }
}