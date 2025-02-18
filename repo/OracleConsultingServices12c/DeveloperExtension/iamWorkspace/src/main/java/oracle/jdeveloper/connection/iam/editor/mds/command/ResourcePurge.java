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

    File        :   ResourcePurge.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    ResourcePurge.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.editor.mds.command;

import oracle.ide.Ide;

import oracle.ide.controller.Command;

import oracle.javatools.dialogs.ExceptionDialog;

import oracle.jdeveloper.workspace.iam.resource.ComponentBundle;

import oracle.jdeveloper.connection.iam.adapter.MetadataPackage;

import oracle.jdeveloper.connection.iam.editor.mds.resource.Bundle;

////////////////////////////////////////////////////////////////////////////////
// class ResourcePurge
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** Command used by the Oracle Identity and Access Management features to
 ** removes old versions from version history of unlabeled documents on the
 ** Application's repository partition.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class ResourcePurge {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The id of the command to purge old metadata document versions in a
   ** Metadata Service.
   */
  static final String COMMAND = "mds.action.purge";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>ResourcePurge</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   ** <p>
   ** This constructor is private to prevent other classes to use
   ** "new ResourcePurge()".
   */
  private ResourcePurge() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   action
  /**
   ** Returns the id of the action that is responsible for purging old versions
   ** from a Metadata Service.
   **
   ** @return                    the id of the action that is responsible for
   **                            purging old versions from a Metadata Service.
   **
   ** @throws IllegalStateException if the action this command is associated
   **                               with is not registered.
   */
  public static int action() {
    final Integer id = Ide.findCmdID(COMMAND);
    if (id == null)
      throw new IllegalStateException(ComponentBundle.format(ComponentBundle.COMMAND_NOTFOUND, COMMAND));

    return id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Executes the actions associated with a specific command.
   ** <p>
   ** When a command executes successfully, implementations should return
   ** {@link Command#OK OK}, otherwise, return
   ** {@link Command#CANCEL CANCEL} or any other non-zero value.
   ** <p>
   ** The default implementation always returns {@link Command#OK OK}.
   ** Subclasses should override this method to implement their own execution
   ** and return the appropriate value.
   **
   ** @param  context            the {@link MetadataPackage} acting as an anchor
   **                            to purge a respository.
   **
   ** @return                    {@link Command#OK OK} id the command
   **                            execution completes successfully; otherwise
   **                            {@link Command#CANCEL CANCEL} or any
   **                            other non-zero value.
   */
  public static int execute(final MetadataPackage context) {
    final ResourcePurgeDialog dialog = new ResourcePurgeDialog();
    if (!dialog.runDialog())
      return Command.CANCEL;

    try {
      context.provider().resourcePurge(dialog.value());
      return Command.OK;
    }
    catch (Exception e) {
      ExceptionDialog.showExceptionDialog(Ide.getMainWindow(), e, Bundle.format(Bundle.RESOURCE_PURGE_FAILED, e.getMessage()));
      return Command.CANCEL;
    }
  }
}