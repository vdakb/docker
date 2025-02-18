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

    File        :   ResourceDelete.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    ResourceDelete.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.editor.mds.command;

import javax.naming.NamingException;

import oracle.adf.share.jndi.AdfJndiContext;

import oracle.ide.Ide;
import oracle.ide.IdeConstants;

import oracle.ide.controller.Command;

import oracle.javatools.dialogs.MessageDialog;
import oracle.javatools.dialogs.ExceptionDialog;

import oracle.jdeveloper.connection.iam.adapter.MetadataPackage;
import oracle.jdeveloper.connection.iam.adapter.MetadataDocument;
import oracle.jdeveloper.connection.iam.adapter.EndpointContextFactory;

import oracle.jdeveloper.connection.iam.editor.mds.resource.Bundle;

////////////////////////////////////////////////////////////////////////////////
// class ResourceDelete
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** Command used by the Oracle Identity and Access Management features to
 ** delete a Metadata Resource in Oracle JDeveloper.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class ResourceDelete {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>ResourceDelete</code> that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   ** <p>
   ** This constructor is private to prevent other classes to use
   ** "new ResourceDelete()".
   */
  private ResourceDelete() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   action
  /**
   ** Returns the id of the action that is responsible for deleting a resource
   ** in a Metadata Service.
   **
   ** @return                    the id of the action that is responsible for
   **                            deleting a resource in a Metadata Service.
   */
  public static int action() {
    return IdeConstants.DELETE_CMD_ID;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Removes the specified connection from the catalog.
   ** <br>
   ** Before the delete will be done the user isk ask for confirmation to do so.
   **
   ** @param  connection         the identifier of the connection to remove from
   **                            the catalog.
   **
   ** @return                    {@link Command#OK OK} id the command
   **                            execution completes successfully; otherwise
   **                            {@link Command#CANCEL CANCEL} or any
   **                            other non-zero value.
   */
  public static int execute(final String connection) {
    if (!MessageDialog.confirm(Ide.getMainWindow(), Bundle.format(Bundle.RESOURCE_REMOVE_MESSAGE, connection), Bundle.string(Bundle.RESOURCE_REMOVE_TITLE),  null, true))
      return Command.CANCEL;

    final AdfJndiContext context = EndpointContextFactory.connectionContext();
    try {
      context.unbind(connection);
      return Command.OK;
    }
    catch (NamingException e) {
      ExceptionDialog.showExceptionDialog(Ide.getMainWindow(), e, Bundle.format(Bundle.RESOURCE_REMOVE_FAILED, e.getMessage()));
      return Command.CANCEL;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Deletes the specified metadata directory in the backend system.
   ** <br>
   ** Before the delete will be done the user isk ask for confirmation to do so.
   **
   ** @param  context            the metadata directory to delete in the backend
   **                            system.
   **
   ** @return                    {@link Command#OK OK} id the command
   **                            execution completes successfully; otherwise
   **                            {@link Command#CANCEL CANCEL} or any
   **                            other non-zero value.
   */
  public static int execute(final MetadataPackage context) {
    if (!MessageDialog.confirm(Ide.getMainWindow(), Bundle.format(Bundle.RESOURCE_DELETE_MESSAGE, context.name().getAbsoluteName()), Bundle.string(Bundle.RESOURCE_DELETE_TITLE),  null, true))
      return Command.CANCEL;
    try {
      context.provider().resourceDelete(context.name());
      return Command.OK;
    }
    catch (Exception e) {
      ExceptionDialog.showExceptionDialog(Ide.getMainWindow(), e, Bundle.format(Bundle.RESOURCE_DELETE_FAILED, e.getMessage()));
      return Command.CANCEL;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Deletes the specified metadata document in the backend system.
   ** <br>
   ** Before the delete will be done the user isk ask for confirmation to do so.
   **
   ** @param  context            the metadata document to delete in the backend
   **                            system.
   **
   ** @return                    {@link Command#OK OK} id the command
   **                            execution completes successfully; otherwise
   **                            {@link Command#CANCEL CANCEL} or any
   **                            other non-zero value.
   */
  public static int execute(final MetadataDocument context) {
    if (!MessageDialog.confirm(Ide.getMainWindow(), Bundle.format(Bundle.RESOURCE_DELETE_MESSAGE, context.getLongLabel()), Bundle.string(Bundle.RESOURCE_DELETE_TITLE),  null, true))
      return Command.CANCEL;

    try {
      context.provider().resourceDelete(context.name());
      return Command.OK;
    }
    catch (Exception e) {
      ExceptionDialog.showExceptionDialog(Ide.getMainWindow(), e, Bundle.format(Bundle.RESOURCE_DELETE_FAILED, e.getMessage()));
      return Command.CANCEL;
    }
  }
}
