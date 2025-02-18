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

    File        :   TargetHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the interface
                    TargetHandler.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.21  2012-06-16  DSteding    Change to a superclass for
                                               OIMTargetHandler and
                                               SOATargetHandler.
    11.1.1.3.37.60.32  2012-10-20  DSteding    Access to files changed from
                                               java.io.File to java.net.URL.
    11.1.1.3.37.60.38  2013-07-10  DSteding    Change to a superclass for
                                               OIMTargetHandler,
                                               SOATargetHandler and
                                               ADFTargetHandler.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.oim.parser;

import java.net.URL;

import oracle.jdeveloper.workspace.iam.parser.ANTFileHandler;
import oracle.jdeveloper.workspace.iam.parser.XMLFileHandlerException;

////////////////////////////////////////////////////////////////////////////////
// abstract class TargetHandler
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~
/**
 ** Oracle Identity Manager build file descriptor.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public abstract class TargetHandler extends ANTFileHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String WKS_PACKAGE                = "wks.package";
  public static final String WKS_PACKAGE_PATH           = "//target[@name='make']/javadoc";

  public static final String SPECIFICATION_VENDOR       = "wks.specification.vendor";
  public static final String IMPLEMENTATION_VENDOR      = "wks.implementation.vendor";

  public static final String SPECIFICATION_VENDOR_PATH  = "//target[@name='manifest']/manifest/attribute[@name='Specification-Vendor']";
  public static final String IMPLEMENTATION_VENDOR_PATH = "//target[@name='manifest']/manifest/attribute[@name='Implementation-Vendor']";

  public static final String ATTRIBUTE_PACKAGE          = "packagenames";
  public static final String ATTRIBUTE_VALUE            = "value";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:7602795302943048611")
  private static final long  serialVersionUID           = 2696023409943496923L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a new <code>TargetHandler</code> instance by converting the
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
  protected TargetHandler(final URL file) {
    // ensure inheritance
    super(file);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   packageName
  /**
   ** Fetchs the xpath specified by <code>name</code> from the document.
   **
   ** @return                    the property value obtained from the ANT file.
   **
   ** @throws XMLFileHandlerException if the anything goes wrong
   */
  public String packageName()
    throws XMLFileHandlerException {

    return elementAttributeValue(WKS_PACKAGE, ATTRIBUTE_PACKAGE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   specificationVendor
  /**
   ** Fetchs the xpath specified by <code>name</code> from the document.
   **
   ** @return                    the property value obtained from the ANT file.
   **
   ** @throws XMLFileHandlerException if the anything goes wrong
   */
  public String specificationVendor()
    throws XMLFileHandlerException {

    return elementAttributeValue(SPECIFICATION_VENDOR, ATTRIBUTE_VALUE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   implementationVendor
  /**
   ** Fetchs the xpath specified by <code>name</code> from the document.
   **
   ** @return                    the property value obtained from the ANT file.
   **
   ** @throws XMLFileHandlerException if the anything goes wrong
   */
  public String implementationVendor()
    throws XMLFileHandlerException {

    return elementAttributeValue(IMPLEMENTATION_VENDOR, ATTRIBUTE_VALUE);
  }

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
    register(WKS_PACKAGE,           WKS_PACKAGE_PATH);
    register(SPECIFICATION_VENDOR,  SPECIFICATION_VENDOR_PATH);
    register(IMPLEMENTATION_VENDOR, IMPLEMENTATION_VENDOR_PATH);
  }
}