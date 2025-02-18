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

    System      :   JDeveloper Identity and Access Extensions
    Subsystem   :   Identity and Access Management Facilities

    File        :   PreferencePanel.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    PreferencePanel.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.preference;

import java.net.URL;

import java.awt.EventQueue;

import oracle.javatools.dialogs.MessageDialog;

import oracle.ide.net.URLFileSystem;

import oracle.ide.controls.WaitCursor;

import oracle.ide.panels.DefaultTraversablePanel;

import oracle.jdeveloper.workspace.iam.utility.StringUtility;

import oracle.jdeveloper.workspace.iam.resource.ComponentBundle;

import oracle.jdeveloper.workspace.iam.builder.LibraryBuilder;

import oracle.jdeveloper.workspace.iam.swing.widget.AbstractPanel;

////////////////////////////////////////////////////////////////////////////////
// abstract class PreferencePanel
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** The UI panel that provides support in the Preference dialog for editing the
 ** preferences stored in the {@link Provider} model.
 ** <p>
 ** In general, preferences panels are not supposed to be published APIs, so we
 ** enforce that. Even though the panel is constructed by the IDE framework
 ** using reflection, the IDE framework does not require that it is public (only
 ** that it has a no-argument constructor).
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public abstract class PreferencePanel extends AbstractPanel {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-296428832529662317")
  private static final long serialVersionUID = -6215148358578910592L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>PreferencePanel</code> that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected PreferencePanel() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commit
  /**
   ** Called to have this {@link DefaultTraversablePanel} perform the commit
   ** action.
   **
   ** @param  destructive        if the operation the confirmation is for is
   **                            destructive (e.g. file deletion), the <i>No</i>
   **                            option button will be the default.
   **
   ** @return                    <code>true</code> if all changes are applied
   **                            succesfully; <code>false</code> otherwise.
   */
  protected abstract boolean commit(final boolean destructive);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateFolder
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>folder</code> must be evaluate to a valid
   ** directory that is readable.
   **
   ** @param  folder             the {@link URL} of the folder to validate.
   **
   ** @return                    <code>true</code> if the specified {@link URL}
   **                            meets all requirements of an accessible folder.
   */
  protected final boolean validateFolder(final URL folder) {
    return (URLFileSystem.isValid(folder) && URLFileSystem.exists(folder) && URLFileSystem.isDirectory(folder) && URLFileSystem.canRead(folder));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateString
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>value</code> must be evaluate to a
   ** valid string.
   **
   ** @param  value              the string to validate.
   **
   ** @return                    <code>true</code> if the given value meets the
   **                            requirements for further processing.
   */
  protected final boolean validateString(final String value) {
    return StringUtility.empty(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   confirmViolation
  /**
   ** Notifies the user the a validation violation has to be confirmed.
   ** <p>
   ** The confirmation alert is used to warn the user about the consequences of
   ** the action, giving the user a chance to accept the consequences, avoid
   ** them, or resolve them in some other fashion.
   **
   ** @param  option             the option that is subject of the violation
   ** @param  message            a detaied maesage that explain the violation deeper
   ** @param  destructive        if the operation the confirmation is for is
   **                            destructive (e.g. file deletion), the <i>No</i>
   **                            option button will be the default.
   **
   ** @return                    <code>true</code> if the user clicked
   **                            <i>Yes</i> option, <code>false</code> otherwise.
   */
  protected final boolean confirmViolation(final String option, final String message, final boolean destructive)  {
    // notify the user about an unanticipated condition that prevents the task
    // from completing successfully
    if (destructive) {
      MessageDialog.critical(this, message, ComponentBundle.format(ComponentBundle.VALIDATION_ERROR_TITLE, option), null);
      return false;
    }
    else
      return MessageDialog.confirm(this, message, ComponentBundle.format(ComponentBundle.VALIDATION_ERROR_TITLE, option), null, destructive, ComponentBundle.string(ComponentBundle.IGNORE), ComponentBundle.string(ComponentBundle.CANCEL));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generateLibraries
  /**
   ** Generates the libraries of a product.
   **
   ** @param  builder            the {@link LibraryBuilder} to invoke for
   **                            generating the libraries.
   */
  protected final void generateLibraries(final LibraryBuilder builder) {
    // attach a WaitCursor to the RootPaneContainer of this panel
    final WaitCursor waitCursor = new WaitCursor(this);

    // schedules the wait cursor to be shown after 400 milliseconds has
    // elapsed
    waitCursor.show(400);
    try {
      // schedule the build of the libraries
      EventQueue.invokeLater(builder);
    }
    finally {
      // hides the wait cursor if visible.
      waitCursor.hide();
    }
  }
}