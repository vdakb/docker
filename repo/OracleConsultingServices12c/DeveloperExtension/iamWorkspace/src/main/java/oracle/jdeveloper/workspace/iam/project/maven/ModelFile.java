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

    Copyright © 2021. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   ModelFile.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    ModelFile.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.94  2021-03-09  DSteding    First release version
*/

package oracle.jdeveloper.workspace.iam.project.maven;

import javax.swing.Icon;

////////////////////////////////////////////////////////////////////////////////
// class TemplateFile
// ~~~~~ ~~~~~~~~~~~~
/**
 ** Utility used to maintain product specific descriptor files in the build file
 ** hierarchy.
 ** <p>
 ** The template handled by this instance is the descriptor file itself if its
 ** exists.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.94
 ** @since   12.2.1.3.42.60.94
 */
public class ModelFile extends ModelData {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ModelFile</code> with the specified project name
   ** and base dir.
   **
   ** @param  folder             the abstract pathname the folder where the
   **                            build file should be exist and/or will be
   **                            created.
   ** @param  name               the name of the file
   */
  public ModelFile(final ModelFolder folder, final String name) {
    // ensure inheritance
    super(folder, name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   icon (TemplateData)
  /**
   ** Returns the icon that represents a XML descriptor file.
   **
   ** @return                    the {@link Icon} identifiying the displayed
   **                            node as a XML descriptor file.
   */
  @Override
  public final Icon icon() {
    return ModelBundle.icon(ModelBundle.NODE_DESCRIPTOR_ICON);
  }
}