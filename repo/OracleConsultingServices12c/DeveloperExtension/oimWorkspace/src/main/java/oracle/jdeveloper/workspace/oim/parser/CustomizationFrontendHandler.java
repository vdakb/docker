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

    File        :   CustomizationFrontendHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the interface
                    CustomizationFrontendHandler.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
    11.1.1.3.37.60.39  2013-07-29  DSteding    First release version
*/

package oracle.jdeveloper.workspace.oim.parser;

import java.net.URL;

import oracle.jdeveloper.workspace.iam.parser.XMLFileHandlerException;

////////////////////////////////////////////////////////////////////////////////
// class CustomizationFrontendHandler
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Oracle Identity Manager build file descriptor.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.60.39
 */
public class CustomizationFrontendHandler extends CustomizationHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String CONTENT_FOLDER   = "adf.content.folder";

  public static final String BACKEND_SCRIPT   = "adf.backend.script";
  public static final String BACKEND_LIBRARY  = "adf.backend.library";

  public static final String BACKEND_PATHID   = "oracle.adf.iam.oim.backend";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:4180672752243466646")
  private static final long  serialVersionUID = -8040619024805086968L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a new <code>CustomizationFrontendHandler</code> instance by
   ** converting the given pathname string into an abstract pathname.
   ** <p>
   ** If the given string is the empty string, then the result is the empty
   ** abstract pathname.
   **
   ** @param  file               the {@link URL} pathname name to the
   **                            {@link ProjectHandler}.
   **
   ** @throws NullPointerException if the <code>name</code> argument is
   **                              <code>null</code>.
   */
  public CustomizationFrontendHandler(final URL file) {
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
    registerProperty(BACKEND_SCRIPT, "backend");
    registerPathID(BACKEND_LIBRARY,  BACKEND_PATHID);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   locationValue
  /**
   ** Fetchs the xpath specified by <code>name</code> from the document.
   **
   ** @param  name               the mapped attribute name of the xpath to read.
   **
   ** @return                    the location value obtained from the ANT file.
   **
   ** @throws XMLFileHandlerException if the anything goes wrong
   */
  public String locationValue(final String name)
    throws XMLFileHandlerException {

    return elementAttributeValue(name, "location");
  }
}