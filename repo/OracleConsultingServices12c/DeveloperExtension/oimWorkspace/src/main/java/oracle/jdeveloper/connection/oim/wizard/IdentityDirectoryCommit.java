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

    File        :   IdentityDirectoryCommit.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    IdentityDirectoryCommit.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.104 2023-08-13  DSteding    First release version
*/

package oracle.jdeveloper.connection.oim.wizard;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;

import javax.naming.NamingException;

import oracle.bali.ewt.dialog.JEWTDialog;

import oracle.javatools.dialogs.MessageDialog;

import oracle.ide.Ide;

import oracle.ide.panels.TDialog;
import oracle.ide.util.Namespace;

import oracle.jdeveloper.connection.iam.model.FileSystem;

import oracle.jdeveloper.connection.iam.wizard.EndpointCommit;

import oracle.jdeveloper.connection.oim.Bundle;

////////////////////////////////////////////////////////////////////////////////
// class IdentityDirectoryCommit
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Implementation of the commit to persists connections.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.104
 ** @since   12.2.1.3.42.60.104
 */
public class IdentityDirectoryCommit extends EndpointCommit {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>IdentityDirectoryCommit</code> that observes the
   ** specified {@link Namespace} at commit time.
   **
   ** @param  data               the {@link Namespace} to observe and commit.
   */
  public IdentityDirectoryCommit(final Namespace data) {
    // ensure inheritance
    super(data);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   vetoableChange (VetoableChangeListener)
  /**
   ** This method gets called when a constrained property is changed.
   **
   ** @param  event              a <code>PropertyChangeEvent</code> object
   **                            describing the event source and the property
   **                            that has changed.
   **
   ** @throws PropertyVetoException if the recipient wishes the property change
   **                               to be rolled back.
   */
  @Override
  public void vetoableChange(final PropertyChangeEvent event)
    throws PropertyVetoException {

    if (JEWTDialog.isDialogClosingEvent(event) && !((TDialog)event.getSource()).isCancelled()) {
      String     name     = (String)this.data.get(IdentityDirectoryPanel.CONNECTION_NAME);
      boolean    isNew    = ((Boolean)this.data.get(IdentityDirectoryPanel.CONNECTION_STATE)).booleanValue();
      FileSystem provider = (FileSystem)this.data.get(IdentityDirectoryPanel.CONNECTION_MODEL);
      try {
        doCommit(name, isNew, provider);
      }
      catch (NamingException e) {
        MessageDialog.error(Ide.getMainWindow(), Bundle.format(Bundle.CONNECTION_NAME_EXISTS, name), Bundle.string(isNew ?  Bundle.IDENTITY_CREATE_TITLE : Bundle.IDENTITY_MODIFY_TITLE), null);
        throw new PropertyVetoException(e.getMessage(), event);
      }
    }
  }
}