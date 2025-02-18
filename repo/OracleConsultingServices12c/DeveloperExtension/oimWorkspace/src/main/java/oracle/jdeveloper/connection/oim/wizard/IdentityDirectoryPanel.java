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

    File        :   IdentityDirectoryPanel.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    IdentityDirectoryPanel.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.104 2023-08-13  DSteding    First release version
*/

package oracle.jdeveloper.connection.oim.wizard;

import java.net.URL;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.CellConstraints;

import com.jgoodies.forms.builder.DefaultFormBuilder;

import oracle.javatools.ui.plaf.IconicButtonUI;

import oracle.ide.net.URLFactory;
import oracle.ide.net.URLTextField;

import oracle.ide.panels.TraversableContext;
import oracle.ide.panels.TraversalException;

import oracle.jdeveloper.workspace.iam.resource.ComponentBundle;

import oracle.jdeveloper.workspace.iam.preference.PreferencePanel;

import oracle.jdeveloper.connection.iam.model.FileSystem;

import oracle.jdeveloper.connection.oim.Bundle;

////////////////////////////////////////////////////////////////////////////////
// class IdentityDirectoryPanel
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The UI panel that provides support in the configuration dialog to create and
 ** modify a Identity Governanace deployment file system connection.
 ** <p>
 ** This panel implements the operations that must be supported by a GUI
 ** component added to the project-from-template and application-from-template
 ** wizards.
 ** <p>
 ** The GUI component is associated with a particular technology scope and is
 ** registered declaratively using the <code>technology-hook</code> hook in the
 ** extension manifest.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.104
 ** @since   12.2.1.3.42.60.104
 */
public class IdentityDirectoryPanel extends PreferencePanel {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String           CONNECTION_NAME  = "ConnectionNameKey";
  public static final String           CONNECTION_MODEL = "ConnectionDescriptor";
  public static final String           CONNECTION_STATE = "ConnectionStateKey";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // the properties to configure the general properties
  private transient JTextField         connectionName;
  private transient URLTextField       deployment;
  private transient JButton            deploymentBrowse;

  private transient FileSystem         model     = null;
  private transient TraversableContext context   = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>IdentityDirectoryPanel</code> that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public IdentityDirectoryPanel() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeComponent (AbstractPanel)
  /**
   ** Initialize the component constraints.
   */
  @Override
  protected void initializeComponent() {
    this.connectionName   = new JTextField();
    this.deployment       = new URLTextField();
    this.deploymentBrowse = new JButton();
    // allow to choose directories only for the deployment destination
    this.deployment.setDirectoryOnly(true);
    this.deploymentBrowse.addActionListener(this.deployment);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeLayout (AbstractPanel)
  /**
   ** Layout the panel.
   */
  @Override
  protected void initializeLayout() {
    final FormLayout         layout     = new FormLayout(
    //   |     1     |       2        |        3        |
      "4dlu, pref, 3dlu, pref:grow, 1dlu, right:15dlu, 4dlu"
    , "4dlu, pref, 4dlu, pref, 4dlu, pref, 2dlu, pref, 4dlu, pref, 4dlu, pref, 4dlu, pref, 4dlu, pref, 2dlu, pref"
    //   |     1     |     2     |
    );
    final DefaultFormBuilder builder    = new DefaultFormBuilder(layout, this);
    final CellConstraints    constraint = new CellConstraints();

    // the 1st logical row of the layout
    int row = 2;
    builder.addSeparator(Bundle.string(Bundle.DIRECTORY_NAME_HEADER), constraint.xyw(2, row, 5));

    // the 2nd logical row of the layout
    row += 2;
    JLabel label = builder.addLabel(Bundle.string(Bundle.CONNECTION_NAME_LABEL), constraint.xy(2, row));
    label.setLabelFor(this.connectionName);
    builder.add(this.connectionName, constraint.xy(4, row));

    // the 3rd logical row of the layout
    row += 2;
    builder.addSeparator(Bundle.string(Bundle.DIRECTORY_PATH_HEADER), constraint.xyw(2, row, 5));

    // the 4th logical row of the layout
    row += 2;
    label = builder.addLabel(Bundle.string(Bundle.DIRECTORY_PATH_LABEL), constraint.xy(2, row));
    label.setLabelFor(this.deployment);
    builder.add(this.deployment,       constraint.xy(4, row));
    builder.add(this.deploymentBrowse, constraint.xy(6, row));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localizeComponent (AbstractPanel)
  /**
   ** Localizes human readable text for all controls.
   */
  @Override
  protected final void localizeComponent() {
    this.deploymentBrowse.setIcon(ComponentBundle.icon(ComponentBundle.DIRECTORY_ICON));
    this.deploymentBrowse.setToolTipText("DESTINATION_BASE_HINT");

    IconicButtonUI.install(this.deploymentBrowse);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commit (PreferencePanel)
  /**
   ** Called to have this {@link PreferencePanel} perform the commit action.
   **
   ** @param  destructive        if the operation the confirmation is for is
   **                            destructive (e.g. file deletion), the <i>No</i>
   **                            option button will be the default.
   **
   ** @return                    <code>true</code> if all changes are applied
   **                            succesfully; <code>false</code> otherwise.
   */
  @Override
  protected boolean commit(final boolean destructive) {
    try {
      commitName();
      commitDirectory();
      return true;
    }
    catch (TraversalException e) {
      return false;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods gouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onEntry (overridden)
  /**
   ** This method is called when the <code>Traversable</code> is being entered.
   ** <p>
   ** The data that the <code>Traversable</code> should use to populate its UI
   ** components comes from the specified {@link TraversableContext}.
   ** <p>
   ** When the same <code>Traversable</code> is entered more than once in the
   ** course of interacting with the user, the <code>Traversable</code> needs to
   ** reload the data directly from the {@link TraversableContext} rather than
   ** caching data objects. Some reasons for this include:
   ** <ul>
   **   <li>Other <code>Traversable</code> may edit the data objects or even
   **       replace them.
   **   <li>The same <code>Traversable</code> instance may be used for editing
   **       multiple different instances of the same object type.
   ** </ul>
   ** Loading data directly from the {@link TraversableContext} is the best way
   ** to ensure that the <code>Traversable</code> will not be editing the wrong
   ** data.
   ** <p>
   ** The <code>Traversable</code> should not even cache references to data
   ** objects between invocations of onEntry and
   ** {@link #onExit(TraversableContext)} because the UI container is not
   ** required to guarantee that the references will be identical.
   **
   ** @param  context            the data wrapper where the
   **                            <code>Traversable</code> locates the data that
   **                            it needs to populate the UI.
   **                            <br>
   **                            Allowed object is {@link TraversableContext}.
   */
  @Override
  public void onEntry(final TraversableContext context) {
    this.context = context;
    this.model   = ((FileSystem)context.get(CONNECTION_MODEL));
    // on entering the panel, we need to populate all fields with properties
    // from the model object.
    this.connectionName.setText(((String)context.get(CONNECTION_NAME)));
    this.deployment.setURL(URLFactory.newFileURL(this.model.path()));
    if (!(Boolean)context.get(CONNECTION_STATE)) {
      this.connectionName.setEditable(false);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onExit (overridden)
  /**
   ** This method is called when the <code>Traversable</code> is being exited.
   ** <p>
   ** At this point, the <code>Traversable</code> should copy the data from its
   ** associated UI back into the data structures in the
   ** {@link TraversableContext}.
   ** <p>
   ** If the <code>Traversable</code> should not be exited because the user has
   ** entered either incomplete, invalid, or inconsistent data, then this method
   ** can throw a {@link TraversalException} to indicate to the property dialog
   ** or wizard that validation failed and that the user should not be allowed
   ** to exit the current <code>Traversable</code>. Refer to the
   ** {@link TraversalException} javadoc for details on passing the error
   ** message that should be shown to the user.
   **
   ** @param  context            the data object where changes made in the UI
   **                            should be copied so that the changes can be
   **                            accessed by other <code>Traversable</code>s.
   **                            <br>
   **                            Allowed object is {@link TraversableContext}.
   **
   ** @throws TraversalException if the user has entered either incomplete,
   **                            invalid, or inconsistent data.
   **                            <p>
   **                            This exception prevents the property dialog or
   **                            wizard from continuing and forces the user to
   **                            stay on the current <code>Traversable</code>
   **                            until the data entered is valid or the user
   **                            cancels. The exception class itself is capable
   **                            of carrying an error message that will be shown
   **                            to the user. Refer to its javadoc for details.
   */
  @Override
  public void onExit(final TraversableContext context)
    throws TraversalException {

    // this will raise a commit of the data to the traversable context
    // this will not do anything with the artifacts that this page configures
    // if any error occurs during this phase the wizard will stay on this page
    commit(false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commitName
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>name</code> must be evaluate to a valid
   ** string.
   **
   ** @throws TraversalException if the commit should be aborted.
   */
  protected void commitName()
    throws TraversalException {

    // obtained the value from the UI to store.
    final String value = this.connectionName.getText();

    // check the rules and validate
    validateString(CONNECTION_NAME, value, Bundle.string(Bundle.CONNECTION_NAME_ERROR));
    // store the provided value in the context
    this.context.put(CONNECTION_NAME, value.trim());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commitDirectory
  /**
   ** Stores if the path the Oracle Identity Governance Deployment.
   ** <p>
   ** Validation takes place before the preference provider will be updated.
   ** <p>
   ** In other terms the entry <code>directory</code> must be evaluate to a
   ** valid directory.
   **
   ** @throws TraversalException if the commit should be aborted.
   */
  private void commitDirectory()
    throws TraversalException {

    final URL deployment = this.deployment.getURL();
    if (!validateFolder(deployment)) {
      if (!confirmViolation(FileSystem.PATH, Bundle.string(Bundle.DIRECTORY_PATH_ERROR), false))
        throw new TraversalException(FileSystem.PATH);
    }
    // store the provided value in the context
    this.model.path(deployment.getPath());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateString
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>value</code> must be evaluate to a
   ** valid string.
   **
   ** @param  option             the option that is subject of the violation.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the string to validate.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  message            the human readable string of the exception
   **                            if a violation occurs.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws TraversalException if <code>value</code> violates the conditions.
   */
  protected final void validateString(final String option, final String value, final String message)
    throws TraversalException {

    if (validateString(value))
      throw new TraversalException(message, ComponentBundle.format(ComponentBundle.VALIDATION_ERROR_TITLE, option));
  }
}