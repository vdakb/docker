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

    File        :   DirectoryServerDialog.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    DirectoryServerDialog.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.wizard;

import javax.naming.NamingException;

import oracle.adf.share.jndi.AdfJndiContext;

import oracle.ide.Ide;

import oracle.ide.controller.Command;

import oracle.javatools.dialogs.MessageDialog;
import oracle.javatools.dialogs.ExceptionDialog;

import oracle.jdeveloper.connection.iam.adapter.EndpointContextFactory;

import oracle.jdeveloper.connection.iam.editor.ods.resource.Bundle;

////////////////////////////////////////////////////////////////////////////////
// abstract class DirectoryServerDialog
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** Miscellaneous collection utility methods. Mainly for internal use.
 **
 ** @author  Dieter.Steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class DirectoryServerDialog {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DirectoryServerDialog</code>.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new DirectoryServerDialog()" and enforces use of the public method below.
   */
  private DirectoryServerDialog() {
    // should never be instantiated
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
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
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int delete(final String connection) {
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
}