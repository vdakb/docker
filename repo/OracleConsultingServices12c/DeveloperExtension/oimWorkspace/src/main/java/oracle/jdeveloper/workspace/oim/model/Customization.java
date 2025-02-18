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
    Subsystem   :   Identity Management Facility

    File        :   Customization.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    Customization.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.60.37  2013-06-24  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.oim.model;

import oracle.javatools.data.HashStructure;

import oracle.jdeveloper.workspace.oim.Manifest;

////////////////////////////////////////////////////////////////////////////////
// abstract class Customization
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~
/**
 ** The Backend to support the configuration wizard in creating an Oracle
 ** Identity Manager Customization Project.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.60.37
 */
public abstract class Customization extends BuildArtifact {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  protected static final String LIBRARY           = "library";
  protected static final String DEFAULT_PACKAGE   = "ocs/iam/identity/ui/**/*.class";

  private static final String   FILE              = "adf-library-ant";
  private static final String   DEFAULT_FILE      = FILE + "." + Manifest.CONFIG_FILE_TYPE;

  private static final String DEFAULT_APPLICATION = "ocs.iam.identity.ui.library";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Customization</code>.
   ** <br>
   ** This constructor is protected to prevent other classes to use
   ** "new Customization()" and enforces use of the public factory method below.
   **
   ** @param  structure          the data provider to initialize the instance.
   */
  protected Customization(final HashStructure structure) {
    // ensure inheritance
    super(structure);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   file (overridden)
  /**
   ** Returns the file name tag used by the component configuration to store the
   ** bulld file in the local filesystem.
   **
   ** @return                    the file name tag used by the component
   **                            configuration to store the bulld file in the
   **                            local filesystem.
   */
  @Override
  public String file() {
    // The second parameter to getString() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return string(FILE, DEFAULT_FILE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   packagePath (overridden)
  /**
   ** Returns the package path for the project.
   **
   ** @return                    the package path for the project.
   */
  @Override
  public String packagePath() {
    // the second parameter to string() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return string(PACKAGEPATH, DEFAULT_PACKAGE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   library
  /**
   ** Sets the library tag used by the component configuration.
   **
   ** @param  library            the library tag used by the component
   **                            configuration.
   */
  public void library(final String library) {
    this._hash.putString(LIBRARY, library);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   library
  /**
   ** Returns the library tag for the library.
   **
   ** @return                    the library tag for the library.
   */
  public abstract String library();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   application (BuildArtifact)
  /**
   ** Returns the application tag for the library.
   **
   ** @return                    the application tag for the library.
   */
  @Override
  public String application() {
    return string(APPLICATION, DEFAULT_APPLICATION);
  }
}