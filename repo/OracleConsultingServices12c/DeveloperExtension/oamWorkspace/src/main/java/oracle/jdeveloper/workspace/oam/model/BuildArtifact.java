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

    File        :   BuildArtifact.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    BuildArtifact.


    Revisions       Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.oam.model;

import java.net.URL;

import oracle.javatools.data.HashStructure;

import oracle.jdeveloper.workspace.iam.model.BuildPropertyAdapter;

////////////////////////////////////////////////////////////////////////////////
// abstract class BuildArtifact
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~
/**
 ** The model to support the configuration wizard in creating a Oracle Access
 ** Manager Project.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public abstract class BuildArtifact extends BuildPropertyAdapter {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String APPLICATION     = "application";
  public static final String DESTINATION     = "destination";
  public static final String PACKAGEPATH     = "packagepath";

  public static final String DEFAULT_PACKAGE = "oracle.iam.access";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>BuildArtifact</code>.
   ** <br>
   ** This constructor is protected to prevent other classes to use
   ** "new BuildArtifact()" and enforces use of the public factory method below.
   **
   ** @param  structure          the data provider to initialize the instance.
   */
  protected BuildArtifact(final HashStructure structure) {
    // ensure inheritance
    super(structure);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   application
  /**
   ** Sets the application tag used by the component configuration.
   **
   ** @param  application        the application tag used by the component
   **                            configuration.
   */
  public void application(final String application) {
    this._hash.putString(APPLICATION, application);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   application
  /**
   ** Returns the application tag for the project.
   **
   ** @return                    the application tag for the project.
   */
  public abstract String application();

  ////////////////////////////////////////////////////////////////////////////
  // Method:   destination
  /**
   ** Sets the destination tag used by the component configuration.
   **
   ** @param  destination        the destination tag used by the component
   **                            configuration.
   */
  public void destination(final URL destination) {
    this._hash.putURL(DESTINATION, destination);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   destination
  /**
   ** Returns the destination tag for the project.
   **
   ** @return                    the destination tag for the project.
   */
  public URL destination() {
    return this.url(DESTINATION);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   packagePath
  /**
   ** Sets the package path used by the component configuration.
   **
   ** @param  packagePath        the package path used by the component
   **                            configuration.
   */
  public void packagePath(final String packagePath) {
    this._hash.putString(PACKAGEPATH, packagePath);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   packagePath
  /**
   ** Returns the package path for the project.
   **
   ** @return                    the package path for the project.
   */
  public String packagePath() {
    // the second parameter to string() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return string(PACKAGEPATH, DEFAULT_PACKAGE);
  }
}