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

    File        :   ResourceRename.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    ResourceRename.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.editor.mds.command;

import oracle.mds.naming.DocumentName;

import oracle.javatools.dialogs.ExceptionDialog;

import oracle.ide.Ide;

import oracle.ide.controller.Command;

import oracle.javatools.dialogs.MessageDialog;

import oracle.jdeveloper.workspace.iam.resource.ComponentBundle;

import oracle.jdeveloper.connection.iam.adapter.MetadataDocument;

import oracle.jdeveloper.connection.iam.editor.mds.resource.Bundle;

////////////////////////////////////////////////////////////////////////////////
// class ResourceRename
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** Command used by the Oracle Identity and Access Management features to
 ** rename a selected Metadata Document.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class ResourceRename {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The id of the command to rename a metadata document in a Metadata service.
   ** This matches the id defined for this action in extension.xml.
   */
  static final String COMMAND = "mds.action.rename";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>ResourceRename</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   ** <p>
   ** This constructor is protected to prevent other classes to use
   ** "new ResourceRename()".
   */
  private ResourceRename() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   action
  /**
   ** Returns the id of the action that is responsible for renaming a document
   ** in a Metadata Service.
   **
   ** @return                    the id of the action that is responsible for
   **                            renaming a document in a Metadata Service.
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
   ** <br>
   ** Renames a {@link MetadataDocument} to a new name request from user.
   ** <p>
   ** When a command executes successfully, implementations should return
   ** {@link Command#OK OK}, otherwise, return
   ** {@link Command#CANCEL CANCEL} or any other non-zero value.
   ** <p>
   ** The default implementation always returns {@link Command#OK OK}.
   ** Subclasses should override this method to implement their own execution
   ** and return the appropriate value.
   **
   ** @param  context            the {@link MetadataDocument} to rename.
   **
   ** @return                    {@link Command#OK OK} id the command
   **                            execution completes successfully; otherwise
   **                            {@link Command#CANCEL CANCEL} or any
   **                            other non-zero value.
   */
  public static int execute(final MetadataDocument context) {
    final DocumentName         source = context.name();
    final ResourceRenameDialog dialog = new ResourceRenameDialog(source.getLocalName());
    if (!dialog.runDialog())
      return Command.CANCEL;

    // prevent unessecary actions if the name of the document isn't changed
    if (source.getLocalName().equals(dialog.value()))
      return Command.CANCEL;

    try {
      final DocumentName target = DocumentName.create(source.getPackage(), dialog.value());
      // ensure the we have a valid resource name derived from the specified
      // parameters of this method
      context.provider().documentRename(source, target);
      MessageDialog.information(Ide.getMainWindow(), Bundle.format(Bundle.RESOURCE_RENAME_SUCCESS, source.getAbsoluteName(), target.getAbsoluteName()), Bundle.string(Bundle.RESOURCE_RENAME_TITLE),  null);
      return Command.OK;
    }
    catch (Exception e) {
      ExceptionDialog.showExceptionDialog(Ide.getMainWindow(), e, Bundle.format(Bundle.RESOURCE_RENAME_FAILED, e.getMessage()));
      return Command.CANCEL;
    }
  }
}