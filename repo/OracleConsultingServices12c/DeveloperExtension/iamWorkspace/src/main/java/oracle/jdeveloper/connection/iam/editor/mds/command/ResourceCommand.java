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

    Copyright © 2019. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   ResourceCommand.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    ResourceCommand.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.editor.mds.command;

import java.net.URL;

import oracle.ide.Ide;

import oracle.ide.net.URLChooser;
import oracle.ide.net.URLFileSystem;
import oracle.ide.net.DefaultURLFilter;

import oracle.ide.dialogs.DialogUtil;

import oracle.jdeveloper.connection.iam.editor.mds.resource.Bundle;

////////////////////////////////////////////////////////////////////////////////
// class ResourceCommand
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Command used by the Oracle Identity and Access Management features to
 ** import a selected Metadata Document from the local file system.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
class ResourceCommand {

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  static URL selected;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>ResourceCommand</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   ** <p>
   ** This constructor is protected to prevent other classes to use
   ** "new ResourceCommand()".
   */
  protected ResourceCommand() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   chooseTarget
  /**
   ** Select a file that will be the target of the export process.
   **
   ** @param  title              the title of the dialog box to display.
   ** @param  fileName           the pre-selected filename.
   **
   ** @return                    the response of the user applied on the control
   **                            component of the dialog.
   */
  protected static boolean chooseTarget(final String title, final String fileName) {
    final URLChooser       chooser = chooser();
    chooser.setFileNameURL(fileName);

    final int response = chooser.showSaveDialog(Ide.getMainWindow(), title);
    if (response == URLChooser.APPROVE_OPTION)
      ResourceCommand.selected = chooser.getSelectedURL();
    return (response == URLChooser.APPROVE_OPTION);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   chooseSource
  /**
   ** Select a file that will be the source of the import process.
   **
   ** @param  title              the title of the dialog box to display.
   **
   ** @return                    the response of the user applied on the control
   **                            component of the dialog.
   */
  protected static boolean chooseSource(final String title) {
    final URLChooser chooser  = chooser();
    final int        response = chooser.showOpenDialog(Ide.getMainWindow(), title);
    if (response == URLChooser.APPROVE_OPTION)
      ResourceCommand.selected = chooser.getSelectedURL();
    return (response == URLChooser.APPROVE_OPTION);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   path
  /**
   ** Returns the {@link URL} latched in {@link #selected} as a platform
   ** independed path name.
   **
   ** @return                    the {@link URL} latched in {@link #selected} as
   **                            a platform independed path name.
   */
  protected static String path() {
    
    if (selected == null)
      return "";

    return URLFileSystem.getPlatformPathName(selected);
    /*
    String path = selected.getPath();
    // hack to get around windows using \ instead of /
    if (System.getProperty("os.name").startsWith("Windows")) {
      path = path.substring(1);
    }
    return path;
   */
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   chooser
  /**
   ** Factory method to create a {@link URLChooser}.
   **
   ** @return                    a {@link URLChooser} ready to use.
   */
  private static URLChooser chooser() {
    URL base = null;
    if (ResourceCommand.selected != null && URLFileSystem.exists(ResourceCommand.selected))
      base = URLFileSystem.getParent(ResourceCommand.selected);

    final DefaultURLFilter filter  = new DefaultURLFilter(Bundle.string(Bundle.RESOURCE_DOCUMENT_LABEL), ".xml");
    final URLChooser       chooser = DialogUtil.newURLChooser(base);
    chooser.setShowJarsAsDirs(false);
    chooser.setSelectionMode(URLChooser.SINGLE_SELECTION);
    chooser.setSelectionScope(URLChooser.FILES_ONLY);
    chooser.setURLFilter(filter);
    return chooser;
  }
}