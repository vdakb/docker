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

    Copyright © 2015. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facilities

    File        :   Panel.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the interface
                    Panel.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.60.63  2015-02-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.project.content.build;

import oracle.jdeveloper.workspace.iam.Bundle;

import oracle.jdeveloper.workspace.iam.project.content.FeaturePanel;

import oracle.jdeveloper.workspace.iam.wizard.TemplateProjectConfigurator;

////////////////////////////////////////////////////////////////////////////////
// class Panel
// ~~~~~ ~~~~~
/**
 ** Panel for the Project Settings dialog that allows the user to alter the
 ** build source path etc for the project.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.60.63
 */
public class Panel extends FeaturePanel {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-3227598771232428729")
  private static final long serialVersionUID = -5641234005347926653L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Panel</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Panel() {
    // ensure inheritance
    super(Settings.DATA);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   defaultDirectory (FeaturePanel)
  /**
   ** Returns the name of the default content set directory.
   ** <p>
   ** The name isn't an absolute path; it's specifies the relative location
   ** regarding the project base directory.
   **
   ** @return                    the name of the default content set directory.
   */
  @Override
  protected final String defaultDirectory() {
    return TemplateProjectConfigurator.BUILD_DIRECTORY;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localizeComponent (FeaturePanel)
  /**
   ** Localizes human readable text for all controls.
   */
  @Override
  protected void localizeComponent() {
    this.noteComponent().setText(Bundle.string(Bundle.CONTENT_ANTBUILD_PATH_HINT));
    this.contentComponent().setContentSetLabel(Bundle.string(Bundle.CONTENT_ANTBUILD_PATH_LABEL));
  }
}