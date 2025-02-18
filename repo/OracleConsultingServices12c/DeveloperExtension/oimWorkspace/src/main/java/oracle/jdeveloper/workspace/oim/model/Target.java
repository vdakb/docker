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

    File        :   Target.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    Target.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.60.38  2013-07-10  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.oim.model;

import oracle.javatools.data.HashStructure;

import oracle.jdeveloper.workspace.iam.model.BuildPropertyAdapter;

import oracle.jdeveloper.workspace.oim.parser.TargetHandler;

////////////////////////////////////////////////////////////////////////////////
// abstract class Target
// ~~~~~~~~ ~~~~~ ~~~~~~
/**
 ** The model to support the configuration wizard in creating an Oracle Identity
 ** Manager application build targets.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.60.38
 */
abstract class Target extends BuildPropertyAdapter {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String DEFAULT_VENDOR  = "Oracle Consulting Services";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a generic <code>Target</code>.
   ** <br>
   ** This constructor is protected to prevent other classes to use
   ** "new Target()" and enforces use of the public factory method below.
   **
   ** @param  structure          the data provider to initialize the instance.
   */
  protected Target(final HashStructure structure) {
    // ensure inheritance
    super(structure);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   specificationVendor
  /**
   ** Sets the vendor of the specification of the package.
   **
   ** @param  vendor             the vendor of the specification of the package.
   */
  public void specificationVendor(final String vendor) {
    this._hash.putString(TargetHandler.SPECIFICATION_VENDOR, vendor);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   specificationVendor
  /**
   ** Returns the vendor of the specification of the package.
   **
   ** @return                    the vendor of the specification of the package.
   */
  public String specificationVendor() {
    return string(TargetHandler.SPECIFICATION_VENDOR, defaultImplementationVendor());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   implementationVendor
  /**
   ** Sets the vendor of the implementation of the package.
   **
   ** @param  vendor             the vendor of the implementation of the package.
   */
  public void implementationVendor(final String vendor) {
    this._hash.putString(TargetHandler.IMPLEMENTATION_VENDOR, vendor);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   implementationVendor
  /**
   ** Returns the vendor of the implementation of the package.
   **
   ** @return                    the vendor of the implementation of the package.
   */
  public String implementationVendor() {
    return string(TargetHandler.IMPLEMENTATION_VENDOR, defaultImplementationVendor());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   defaultSpecificationVendor
  /**
   ** Returns the vendor of the specification of the package.
   **
   ** @return                    the vendor of the specification of the package.
   */
  public final String defaultSpecificationVendor(){
    return DEFAULT_VENDOR;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   defaultImplementationVendor
  /**
   ** Returns the vendor of the implementation of the package.
   **
   ** @return                    the vendor of the implementation of the
   **                            package.
   */
  public final String defaultImplementationVendor(){
    return DEFAULT_VENDOR;
  }
}