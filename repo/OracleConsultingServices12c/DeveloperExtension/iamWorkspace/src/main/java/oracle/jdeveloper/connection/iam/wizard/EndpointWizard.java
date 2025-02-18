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

    File        :   EndpointWizard.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    EndpointWizard.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.wizard;

import java.awt.Component;

import javax.naming.Referenceable;

import oracle.bali.ewt.dialog.JEWTDialog;
import oracle.bali.ewt.dialog.DialogHeader;

import oracle.adf.share.jndi.AdfJndiContext;

import oracle.ide.Ide;
import oracle.ide.Context;

import oracle.ide.wizard.Wizard;
import oracle.ide.wizard.Invokable;

import oracle.ide.panels.TDialogLauncher;

import oracle.jdeveloper.connection.iam.adapter.MetadataContextFactory;
import oracle.jdeveloper.workspace.iam.swing.widget.AbstractPanel;

////////////////////////////////////////////////////////////////////////////////
// abstract class EndpointWizard
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~
/**
 ** Implementation of the "JDeveloper Gallery" item.
 ** <p>
 ** For the gallery it is sufficient to implement {@link Invokable}. But for
 ** the <code>Resource Catalog</code> it must be a subclass of {@link Wizard}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public abstract class EndpointWizard extends Wizard {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////
  
  protected final String CONNECTION_ID = "oracle.jdeveloper.rescat2.connectionId";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>EndpointWizard</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected EndpointWizard() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke (Invokable)
  /**
   ** Invokes the connection wizard
   **
   ** @param  context            the context of the wizard.
   **                            All parameters required by the wizard must be
   **                            set on the context. The wizard may also return
   **                            values to the caller by setting them into the
   **                            {@link Context} for the caller to retrieve.
   **                            <br>
   **                            Allowed object is {@link Context}.
   **
   ** @return                    <code>true</code> if the invocation was
   **                            successful, <code>false</code> if it failed or
   **                            was canceled.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public boolean invoke(final Context context) {
    return editMode(context) ? invokeCreate() : invokeModify(context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isAvailable (Wizard)
  /**
   ** Called when the sensitivity of the menu item that opens this wizard must
   ** be determined.
   ** <p>
   ** Since OSGi Bundle support introduced in JDeveloper the method has no
   ** importance anymore to conttrol the behavior of the gallery items this
   ** wizard belongs to. Therefor the method has to be implemented due to the
   ** class constraint where its declared abstract, but we retuning always
   ** <code>true</code>.
   **
   ** @param  context            the {@link Context} to use when invoking this
   **                            {@link EndpointWizard}.
   **                            <br>
   **                            Allowed object is {@link Context}.
   **
   ** @return                    always <code>true</code> due to the
   **                            modifications to control the state of the
   **                            gallery item.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  public final boolean isAvailable(final Context context) {
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   launch
  /**
   ** Launches the dialog dialog with <code>OK</code> and <code>Cancel</code>
   ** buttons.
   **
   ** @param  title              the dialog title.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  header             the dialog header.
   **                            <br>
   **                            Allowed object is {@link DialogHeader}.
   ** @param  panel              the content panel
   **                            <br>
   **                            Allowed object is {@link AbstractPanel}.
   ** @param  commit             the {@link EndpointCommit} listener to be added.
   **                            <br>
   **                            Allowed object is {@link EndpointCommit}.
   **
   ** @return                    <code>true</code> if <code>OK</code> was
   **                            selected; <code>false</code> if
   **                            <code>Cancel</code> was selected or if the
   **                            Traversable associated with the commit action
   **                            is <code>null</code>. 
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public static boolean launch(final String title, final DialogHeader header, final AbstractPanel panel, final EndpointCommit commit) {
    return launch(Ide.getMainWindow(), title, header, panel, commit);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   launch
  /**
   ** Launches the dialog dialog with <code>OK</code> and <code>Cancel</code>
   ** buttons.
   **
   ** @param  parent             the owning {@link Component} of the dialog.
   **                            <br>
   **                            Allowed object is {@link Context}.
   ** @param  title              the dialog title.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  header             the dialog header.
   **                            <br>
   **                            Allowed object is {@link DialogHeader}.
   ** @param  panel              the content panel
   **                            <br>
   **                            Allowed object is {@link AbstractPanel}.
   ** @param  commit             the {@link EndpointCommit} listener to be added.
   **                            <br>
   **                            Allowed object is {@link EndpointCommit}.
   **
   ** @return                    <code>true</code> if <code>OK</code> was
   **                            selected; <code>false</code> if
   **                            <code>Cancel</code> was selected or if the
   **                            Traversable associated with the commit action
   **                            is <code>null</code>. 
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public static boolean launch(final Component parent, final String title, final DialogHeader header, final AbstractPanel panel, final EndpointCommit commit) {
    final TDialogLauncher  launcher = new TDialogLauncher(parent, title, panel, commit.data());
    launcher.setDialogHeader(header);
    launcher.setPackDialog(true);
    // initializes the JEWTDialog that the TDialogLauncher will use to host the
    // Traversable.
    JEWTDialog dialog = launcher.initDialog();
    dialog.addVetoableChangeListener(commit);
    // Shows dialog with OK, Cancel, and Help buttons.
    // The specified Namespace must not be null, or else an
    // IllegalArgumentException is thrown. 
    return launcher.showDialog();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   editMode
  /**
   ** Determines if the endpoint configuration is about to create or modified.
   **
   ** @param  context            the context of the wizard.
   **                            <br>
   **                            All parameters required by the wizard must be
   **                            set on the context. The wizard may also return
   **                            values to the caller by setting them into the
   **                            {@link Context} for the caller to retrieve.
   **                            <br>
   **                            Allowed object is {@link Context}.
   **
   ** @return                    <code>true</code> if the wizard is about to
   **                            modify; otherwise <code>false</code> if the
   **                            wizard is about to create.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  protected boolean editMode(final Context context) {
    final String id = (String)context.getProperty(CONNECTION_ID);
    return ((id == null) || (id.length() == 0));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invokeCreate
  /**
   ** Launches the Create Connection wizard.
   **
   ** @return                    <code>true</code> if the wizard succeeded;
   **                            <code>false</code> if the wizard was canceled.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  protected abstract boolean invokeCreate();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invokeModify
  /**
   ** Launches the Modify Connection wizard.
   **
   ** @param  context            the context of the wizard.
   **                            <br>
   **                            All parameters required by the wizard must be
   **                            set on the context. The wizard may also return
   **                            values to the caller by setting them into the
   **                            {@link Context} for the caller to retrieve.
   **                            <br>
   **                            Allowed object is {@link Context}.
   **
   ** @return                    <code>true</code> if the wizard succeeded;
   **                            <code>false</code> if the wizard was canceled.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  protected abstract boolean invokeModify(final Context context);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   uniqueName
  /**
   ** Generates a unique name for the connection to create
   **
   ** @param  baseName          the prefix of the connection name to generate.
   ** 
   ** @return                   a unique name for the connection to create.
   */
  protected static final String uniqueName(final String baseName) {
    int    count  = 1;
    String unique = baseName + count;

    final AdfJndiContext context = MetadataContextFactory.connectionContext();
    if (context == null)
      return unique;
    try {
      Referenceable reference = (Referenceable)context.lookup(unique);
      while (reference != null) {
        count++;
        unique = baseName + count;
        reference = (Referenceable)context.lookup(unique);
      }
    }
    catch (Exception e) {
      // intentionally left blank
      ;
    }
    return unique;
  }
}