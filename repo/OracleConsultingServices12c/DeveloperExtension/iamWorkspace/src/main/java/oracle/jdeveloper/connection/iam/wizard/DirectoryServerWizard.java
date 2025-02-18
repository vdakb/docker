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

    File        :   DirectoryServerWizard.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    DirectoryServerWizard.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.wizard;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;

import javax.naming.NamingException;

import oracle.adf.share.jndi.AdfJndiContext;

import oracle.bali.ewt.dialog.DialogHeader;

import oracle.bali.ewt.dialog.JEWTDialog;

import oracle.ide.Context;

import oracle.ide.Ide;
import oracle.ide.util.Namespace;

import oracle.javatools.dialogs.MessageDialog;

import oracle.jdeveloper.workspace.iam.Manifest;

import oracle.jdeveloper.connection.iam.Bundle;

import oracle.jdeveloper.connection.iam.model.DirectoryServer;

import oracle.jdeveloper.connection.iam.adapter.DirectoryNodeFactory;

////////////////////////////////////////////////////////////////////////////////
// class DirectoryServerWizard
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** Implementation of the "JDeveloper Gallery" item.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class DirectoryServerWizard extends EndpointWizard {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String BASE_NAME = "ODSConnection";

  //////////////////////////////////////////////////////////////////////////////
  // class Commit
  // ~~~~~ ~~~~~~
  /**
   ** Implementation of the commit to persists connection data.
   */
  private static class Commit extends EndpointCommit {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Commit</code> that observes the specified
     ** {@link Namespace} at commit time.
     **
     ** @param  data             the {@link Namespace} to observe and commit.
     **                          <br>
     **                          Allowed object is {@link Namespace}.
     */
    public Commit(final Namespace data) {
      // ensure inheritance
      super(data);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: vetoableChange (VetoableChangeListener)
    /**
     ** This method gets called when a constrained property is changed.
     **
     ** @param  event            a <code>PropertyChangeEvent</code> object
     **                          describing the event source and the property
     **                          that has changed.
     **                          <br>
     **                          Allowed object is {@link PropertyChangeEvent}.
     **
     ** @throws PropertyVetoException if the recipient wishes the property change
     **                               to be rolled back.
     */
    @Override
    public void vetoableChange(final PropertyChangeEvent event)
      throws PropertyVetoException {

      if (JEWTDialog.isDialogClosingEvent(event)) {
        final String          name       = (String)this.data.get(DirectoryServerPanel.CONNECTION_NAME);
        final boolean         isNew      = ((Boolean)this.data.get(DirectoryServerPanel.CONNECTION_STATE)).booleanValue();
        final DirectoryServer descriptor = (DirectoryServer)this.data.get(DirectoryServerPanel.CONNECTION_MODEL);
        try {
          doCommit(name, isNew, descriptor);
        }
       catch (NamingException e) {
          MessageDialog.error(Ide.getMainWindow(), Bundle.format(Bundle.CONNECTION_NAME_EXISTS, name), Bundle.string(isNew ? Bundle.DIRECTORY_CREATE_TITLE : Bundle.DIRECTORY_MODIFY_TITLE), null);
          throw new PropertyVetoException(e.getMessage(), event);
        }
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DirectoryServerWizard</code> that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public DirectoryServerWizard() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getShortLabel (Wizard)
  /**
   ** Provides the label that represents the Dialog in the Object Gallery.
   ** <p>
   ** The label should be a noun or noun phrase naming the item that is created
   ** by this Dialog and should omit the word "new".
   ** <br>
   ** Examples: "Java Class", "Java Interface", "XML Document",
   ** "Database Connection".
   **
   ** @return                    the human readable label of the Wizard.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String getShortLabel() {
    return Manifest.string(Manifest.DIRECTORY_SERVER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invokeCreate (EndpointWizard)
  /**
   ** Launches the Create Directory Service Connection property dialog.
   **
   ** @return                    <code>true</code> if the property dialog
   **                            succeeded; <code>false</code> if the property
   **                            dialog was canceled.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  protected boolean invokeCreate() {
    return create();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invokeModify (EndpointWizard)
  /**
   ** Launches the Modify Directory Service Connection property dialog.
   **
   ** @param  context            the context of the property dialog.
   **                            All parameters required by the dialog must be
   **                            set on the context. The dialog may also return
   **                            values to the caller by setting them into the
   **                            {@link Context} for the caller to retrieve.
   **                            <br>
   **                            Allowed object is {@link Context}.
   **
   ** @return                    <code>true</code> if the property dialog
   **                            succeeded; <code>false</code> if the property
   **                            dialog was canceled.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  protected boolean invokeModify(final Context context) {
    final String         id  = (String)context.getProperty(CONNECTION_ID);
    final AdfJndiContext ctx = DirectoryNodeFactory.connectionContext();
    try {
      return modify((DirectoryServer)ctx.lookup(id));
    }
    catch (NamingException e) {
      e.printStackTrace();
      return false;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Launches the Create Directory Service Connection property dialog.
   **
   ** @return                    <code>true</code> if the property dialog
   **                            succeeded; <code>false</code> if the property
   **                            dialog was canceled.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public static boolean create() {
    final Namespace data = new Namespace();
    data.put(DirectoryServerPanel.CONNECTION_NAME,  uniqueName(BASE_NAME));
    data.put(DirectoryServerPanel.CONNECTION_MODEL, new DirectoryServer());
    data.put(DirectoryServerPanel.CONNECTION_STATE, Boolean.TRUE);

    final DialogHeader header = new DialogHeader(Bundle.string(Bundle.DIRECTORY_CREATE_HEADER), Bundle.image(Bundle.DIRECTORY_HEADER_IMAGE).getImage());
    return launch(Bundle.string(Bundle.DIRECTORY_CREATE_TITLE), header, new DirectoryServerPanel(), new Commit(data));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Launches the Modify Directory Service Connection property dialog.
   **
   ** @param  provider           the {@link DirectoryServer} to modify.
   **                            <br>
   **                            Allowed object is {@link DirectoryServer}.
   **
   ** @return                    <code>true</code> if the property dialog
   **                            succeeded; <code>false</code> if the property
   **                            dialog was canceled.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public static boolean modify(final DirectoryServer provider) {
    final Namespace data = new Namespace();
    data.put(DirectoryServerPanel.CONNECTION_NAME,  provider.name());
    data.put(DirectoryServerPanel.CONNECTION_MODEL, provider);
    data.put(DirectoryServerPanel.CONNECTION_STATE, Boolean.FALSE);
    final DialogHeader header = new DialogHeader(Bundle.string(Bundle.DIRECTORY_MODIFY_HEADER), Bundle.image(Bundle.DIRECTORY_HEADER_IMAGE).getImage());
    return launch(Bundle.string(Bundle.DIRECTORY_MODIFY_TITLE), header, new DirectoryServerPanel(), new Commit(data));
  }
}