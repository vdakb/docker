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
    Subsystem   :   Access Manager Facility

    File        :   PluginRegistryHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the interface
                    PluginRegistryHandler.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.32  2012-10-20  DSteding    Access to files changed from
                                               java.io.File to java.net.URL.
    11.1.1.3.37.60.69  2017-31-01  DSteding    Changed compatibility annotation
                                               accordingliy to the build.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.oam.parser;

import java.io.File;

import oracle.jdeveloper.workspace.iam.parser.XMLFileHandler;
import oracle.jdeveloper.workspace.iam.parser.XMLFileHandlerException;

////////////////////////////////////////////////////////////////////////////////
// class PluginRegistryHandler
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** Oracle Access Manager build file descriptor.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class PluginRegistryHandler extends XMLFileHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String ELEMENT_ROOT     = "Plugin";

  public static final String ROOT_PATH        = "//" + ELEMENT_ROOT;

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-152580311657588492")
  private static final long serialVersionUID = -3193028840722299526L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a new <code>PluginRegistryHandler</code> instance by converting
   ** the given pathname string into an abstract pathname.
   ** <p>
   ** If the given string is the empty string, then the result is the empty
   ** abstract pathname.
   **
   ** @param  file               the {@link File} pathname name to the
   **                            {@link ProjectHandler}.
   **
   ** @throws NullPointerException if the <code>file</code> argument is
   **                              <code>null</code>.
   */
  public PluginRegistryHandler(final File file) {
    // ensure inheritance
    super(file.getAbsolutePath());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   load
  /**
   ** Load the importable element definitions from the file.
   **
   ** @throws XMLFileHandlerException if the anything goes wrong
   */
  public void load()
    throws XMLFileHandlerException {

    // obtain the attributes from the root element to make it possible to handle
    // it proper in any GUI
    this.load(ROOT_PATH);
  }
}