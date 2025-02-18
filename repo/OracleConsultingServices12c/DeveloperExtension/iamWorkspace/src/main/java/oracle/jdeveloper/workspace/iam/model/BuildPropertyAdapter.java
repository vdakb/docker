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

    File        :   BuildPropertyAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    BuildPropertyAdapter.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.15  2012-02-06  DSteding    Change setter/getter from name
                                               to file.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.model;

import java.net.URL;

import oracle.javatools.data.HashStructure;

import oracle.jdeveloper.workspace.iam.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// abstract class BuildPropertyAdapter
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>BuildPropertyAdapter</code> a generic to store ANT build properties.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public abstract class BuildPropertyAdapter extends DataStructureAdapter {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final    String DEFAULT_TARGET  = "all";

  protected static final String FILE            = "file";
  protected static final String PROJECT         = "project";
  protected static final String TARGET          = "target";
  protected static final String DESCRIPTION     = "description";
  protected static final String BASEDIR         = "basedir";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>BuildPropertyAdapter</code>.
   ** <p>
   ** This constructor is protected to prevent other classes to use
   ** "new BuildPropertyAdapter()" and enforces use of the public factory of the
   ** implementing sub classes.
   ** <p>
   ** Constructor version is existing to satisfy the wizard state engine
   ** requirements
   */
  protected BuildPropertyAdapter() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>BuildPropertyAdapter</code>.
   ** <p>
   ** This constructor is protected to prevent other classes to use
   ** "new BuildPropertyAdapter()" and enforces use of the public factory of the
   ** implementing sub classes.
   **
   ** @param  hash               the {@link HashStructure} this adapter wrappes.
   */
  protected BuildPropertyAdapter(final HashStructure hash) {
    // ensure inheritance
    super(hash);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   template
  /**
   ** Returns the template of this descriptor the generation is based on.
   **
   ** @return                    the template of this descriptor the generation
   **                            is based on.
   */
  public abstract String template();

  ////////////////////////////////////////////////////////////////////////////
  // Method:   file
  /**
   ** Sets the file name tag used by the component configuration to store the
   ** bulld file in the local filesystem.
   **
   ** @param  file               the file name tag used by the component
   **                            configuration to store the bulld file in the
   **                            local filesystem.
   */
  public void file(final String file) {
    this._hash.putString(FILE, file);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   file
  /**
   ** Returns the file name tag used by the component configuration to store the
   ** bulld file in the local filesystem.
   **
   ** @return                    the file name tag used by the component
   **                            configuration to store the bulld file in the
   **                            local filesystem.
   */
  public String file() {
    // the second parameter to string() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return string(FILE, StringUtility.EMPTY);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   project
  /**
   ** Sets the project name tag used by the component configuration.
   **
   ** @param  project            the project name tag used by the component
   **                            configuration.
   */
  public void project(final String project) {
    this._hash.putString(PROJECT, project);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   project
  /**
   ** Returns the project name tag used by the component configuration.
   **
   ** @return                    the project name tag used by the component
   **                            configuration.
   */
  public String project() {
    // the second parameter to string() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return string(PROJECT, StringUtility.EMPTY);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   description
  /**
   ** Sets the description tag used by the component configuration.
   **
   ** @param  description        the description tag used by the component
   **                            configuration.
   */
  public void description(final String description) {
    this._hash.putString(DESCRIPTION, description);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   description
  /**
   ** Returns the description tag for the project.
   **
   ** @return                    the description tag for the project.
   */
  public String description() {
    // the second parameter to string() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return string(DESCRIPTION, "");
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   target
  /**
   ** Sets the target name tag used by the component configuration.
   **
   ** @param  target             the target name tag used by the component
   **                            configuration.
   */
  public void target(final String target) {
    this._hash.putString(TARGET, target);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   target
  /**
   ** Returns the target name tag used by the component configuration.
   **
   ** @return                    the target name tag used by the component
   **                            configuration.
   */
  public String target() {
    // the second parameter to string() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return string(TARGET, DEFAULT_TARGET);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   basedir
  /**
   ** Sets the basedir name tag used by the component configuration.
   **
   ** @param  basedir            the basedir name tag used by the component
   **                            configuration.
   */
  public void basedir(final URL basedir) {
    this._hash.putURL(BASEDIR, basedir);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   basedir
  /**
   ** Returns the basedir name tag used by the component configuration.
   **
   ** @return                    the basedir name tag used by the component
   **                            configuration.
   */
  public URL basedir() {
    // the second parameter to string() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return this.url(BASEDIR);
  }
}