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

    Copyright © 2023. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity Manager Facility

    File        :   IdentityDirectoryWizard.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    IdentityDirectoryWizard.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.104 2023-08-13  DSteding    First release version
*/

package oracle.jdeveloper.connection.oim.wizard;

import javax.naming.NamingException;

import oracle.adf.share.jndi.AdfJndiContext;

import oracle.bali.ewt.dialog.DialogHeader;

import oracle.ide.Ide;
import oracle.ide.Context;

import oracle.ide.util.Namespace;

import oracle.ide.model.Workspace;

import oracle.jdeveloper.connection.iam.model.FileSystem;

import oracle.jdeveloper.connection.iam.adapter.FileSystemNodeFactory;

import oracle.jdeveloper.connection.iam.wizard.EndpointWizard;

import oracle.jdeveloper.connection.oim.Bundle;

import oracle.jdeveloper.workspace.oim.Manifest;

////////////////////////////////////////////////////////////////////////////////
// class IdentityDirectoryWizard
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Implementation of the "JDeveloper Gallery" item.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.104
 ** @since   12.2.1.3.42.60.104
 */
public class IdentityDirectoryWizard extends EndpointWizard {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String BASE_NAME = "OIGDeployment";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>IdentityDirectoryWizard</code> that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public IdentityDirectoryWizard() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstrcat base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getShortLabel (Wizard)
  /**
   ** Provides the label that represents the Wizard in the Object Gallery.
   ** <p>
   ** The label should be a noun or noun phrase naming the item that is created
   ** by this Wizard and should omit the word "new".
   ** <br>
   ** Examples: "Java Class", "Java Interface", "XML Document",
   ** "Database Connection".
   **
   ** @return                    the human readable label of the Wizard.
   */
  @Override
  public String getShortLabel() {
    return Manifest.string(Manifest.IDENTITY_DEPLOY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invokeCreate (EndpointWizard)
  /**
   ** Launches the Create Identity Offline Connection wizard.
   **
   ** @return                    <code>true</code> if the wizard succeeded;
   **                            <code>false</code> if the wizard was canceled.
   */
  @Override
  public boolean invokeCreate() {
    return create();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invokeModify (EndpointWizard)
  /**
   ** Launches the Modify Identity Service Connection wizard.
   **
   ** @return                    <code>true</code> if the wizard succeeded;
   **                            <code>false</code> if the wizard was canceled.
   */
  @Override
  public boolean invokeModify(final Context context) {
    final String         id  = (String)context.getProperty("oracle.jdeveloper.rescat2.connectionId");
    final AdfJndiContext ctx = FileSystemNodeFactory.connectionContext();
    try {
      return modify(id, (FileSystem)ctx.lookup(id));
    }
    catch (NamingException e) {
      e.printStackTrace();
      return false;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by finctionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Launches the Create Identity Service Connection wizard.
   **
   ** @return                    <code>true</code> if the wizard succeeded;
   **                            <code>false</code> if the wizard was canceled.
   */
  public static boolean create() {
    final Namespace data = new Namespace();
    data.put(IdentityDirectoryPanel.CONNECTION_NAME,  uniqueName(BASE_NAME));
    data.put(IdentityDirectoryPanel.CONNECTION_MODEL, new FileSystem());
    data.put(IdentityDirectoryPanel.CONNECTION_STATE, Boolean.TRUE);
    
    // find out if a workspace exists
    final Workspace workspace = Ide.getActiveWorkspace();
    final String    name      = (workspace == null) ? Bundle.string(Bundle.CONNECTION_NO_WORKSPACE) : workspace.getShortLabel();
    final DialogHeader header = new DialogHeader(Bundle.format(Bundle.DIRECTORY_CREATE_HEADER, name), Bundle.image(Bundle.DIRECTORY_HEADER_IMAGE).getImage());
    return launch(Bundle.string(Bundle.DIRECTORY_CREATE_TITLE), header, new IdentityDirectoryPanel(), new IdentityDirectoryCommit(data));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Launches the Modify Identity Service Connection wizard.
   **
   ** @param  id                 the name of the {@link FileSystem} provider
   **                            to modify.
   ** @param  provider           the {@link FileSystem} to modify.
   **
   ** @return                    <code>true</code> if the wizard succeeded;
   **                            <code>false</code> if the wizard was canceled.
   */
  public static boolean modify(final String id, final FileSystem provider) {
    final Namespace data = new Namespace();
    data.put(IdentityDirectoryPanel.CONNECTION_NAME,  id);
    data.put(IdentityDirectoryPanel.CONNECTION_MODEL, provider);
    data.put(IdentityDirectoryPanel.CONNECTION_STATE, Boolean.FALSE);
    // find out if a workspace exists
    final Workspace workspace = Ide.getActiveWorkspace();
    final String    name      = (workspace == null) ? Bundle.string(Bundle.CONNECTION_NO_WORKSPACE) : workspace.getShortLabel();
    final DialogHeader header = new DialogHeader(Bundle.format(Bundle.IDENTITY_MODIFY_HEADER, name), Bundle.image(Bundle.IDENTITY_HEADER_IMAGE).getImage());
    return launch(Bundle.string(Bundle.IDENTITY_MODIFY_TITLE), header, new IdentityDirectoryPanel(), new IdentityDirectoryCommit(data));
  }
}