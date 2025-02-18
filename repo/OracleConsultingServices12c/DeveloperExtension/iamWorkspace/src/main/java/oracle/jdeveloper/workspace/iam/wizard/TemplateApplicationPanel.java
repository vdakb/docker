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

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facilities

    File        :   TemplateApplicationPanel.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    TemplateApplicationPanel.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.69  2017-31-01  DSteding    Changed compatibility annotation
                                               accordingliy to the build.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.wizard;

import java.net.URL;

import java.awt.Insets;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import oracle.javatools.util.ModelUtil;

import oracle.ide.Ide;

import oracle.ide.util.IdeUtil;
import oracle.ide.util.ResourceUtils;

import oracle.ide.help.HelpSystem;

import oracle.ide.model.Project;
import oracle.ide.model.Workspace;

import oracle.ide.net.URLFactory;
import oracle.ide.net.URLFileSystem;
import oracle.ide.net.NameGenerator;
import oracle.ide.net.DefaultNameGenerator;

import oracle.ide.config.Preferences;

import oracle.ide.panels.NewWorkspacePanel;
import oracle.ide.panels.TraversableContext;
import oracle.ide.panels.TraversalException;

import oracle.jdeveloper.template.wizard.WizardTraversable;
import oracle.jdeveloper.template.wizard.TemplateWizardArb;
import oracle.jdeveloper.template.wizard.TemplateWizardUtil;

import oracle.jdeveloper.workspace.iam.preference.Provider;

////////////////////////////////////////////////////////////////////////////////
// class TemplateApplicationPanel
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The UI panel that provides support to create a {@link Workspace}.
 ** <p>
 ** This class has to be reimplementad due to the implementation of class
 ** oracle.jdeveloper.template.wizard.NewApplicationPanel is package proteced.
 ** Hence we have to create our own class ApplicationPanel that do the same Job.
 ** <p>
 ** No chance to reuse the existing class!!!
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class TemplateApplicationPanel extends    JPanel
                                      implements WizardTraversable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-1731318240248343153")
  private static final long       serialVersionUID = 2916847955075608325L;

  private static final String     HELP_ID          = "f1_idednewapplication_html";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private transient String        applicationName;

  /**
   ** the class implements the UI to create a new workspace.
   ** <p>
   ** The panel is initialized with
   ** <ol>
   **   <li>showAddProject        - <code>false</code> to ...
   **   <li>showOpenNavigator     - <code>false</code> to ...
   **   <li>defaultWorkspaceFiles - <code>false</code> to ...
   ** </ol>
   */
  private final NewWorkspacePanel workspacePanel   = new NewWorkspacePanel(false, false, false);

  private final JLabel            packageRootLabel = new JLabel();
  private final JTextField        packageRootEdit  = new JTextField();
  private final Box               spacer           = Box.createVerticalBox();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>TemplateApplicationPanel</code> that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TemplateApplicationPanel() {
    // ensure inheritance
    super();

    HelpSystem.getHelpSystem().registerTopic(this, HELP_ID);

    this.localizeComponent();
    this.initializeLayout();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  ////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   defaultFocusComponent
  /**
   ** Returns the component that should be focued this panel is vesible for the
   ** first time.
   **
   ** @return                    the component that should be focued this panel
   **                            is vesible for the first time.
   */
  public Component defaultFocusComponent() {
    return this.workspacePanel.getNewFilePanel().getDefaultFocusComponent();
 }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   workspacePanel
  /**
   ** Returns the component to maintain the information about the new
   ** application to create.
   **
   ** @return                    the component to maintain the information about
   **                            the new application to create.
   */
  public NewWorkspacePanel workspacePanel() {
    return this.workspacePanel;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   applicationName
  /**
   ** Set the name of the default application template used by this page.
   **
   ** @param  applicationName    the name of the default application template
   **                            to use by this page.
   */
  public void applicationName(final String applicationName) {
    this.applicationName = applicationName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   packageRoot
  /**
   ** Set the package name the application to create will use as the default for
   ** all Oracle JDeveloper projects created within the application.
   **
   ** @param  packageRoot        package name the application to create will use
   **                            for all Oracle JDeveloper projects created
   **                            within the application.
   */
  public void packageRoot(final String packageRoot) {
    this.packageRootEdit.setText(packageRoot);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   packageRoot
  /**
   ** Returns the package name the application to create will use as the default
   ** for all Oracle JDeveloper projects created within the application.
   **
   ** @return                    package name the application to create will use
   **                            for all Oracle JDeveloper projects created
   **                            within the application.
   */
  public String packageRoot() {
    return this.packageRootEdit.getText().trim();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getComponent (WizardTraversable)
  /**
   ** Returns normally, the {@link WizardTraversable} class itself to be the
   ** UI Component. Therefore, getComponent() typically just returns this.
   ** <p>
   ** In this situation the getComponent() method then is simply a means of
   ** avoiding a type cast.
   **
   ** @return                    the UI {@link Component} that the user
   **                            interacts with for creating or editing an
   **                            object.
   */
  @Override
  public Component getComponent() {
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getHelpID (WizardTraversable)
  /**
   ** Returns the context-sensitive help topic ID to use for this
   ** {@link WizardTraversable}.
   ** <p>
   ** A <code>null</code> return value means that the {@link WizardTraversable}
   ** implementation doesn't specify a help topic ID. However, there are other
   ** ways that a help topic ID could get associated with a
   ** {@link WizardTraversable}.
   ** <p>
   ** This method is called only when the user requests help, so the actual help
   ** ID may be determined dynamically (e.g. return a different ID depending on
   ** the state of the UI).
   **
   ** @return                    the context-sensitive help topic ID to use for
   **                            this {@link WizardTraversable}.
   */
  @Override
  public String getHelpID() {
    return HELP_ID;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getExitTransition (WizardTraversable)
  /**
   ** Returns the exit transition for the {@link WizardTraversable} that is used
   ** by dynamic interview-style wizards to determine the next course of action.
   ** <p>
   ** The wizard can use the exit transition to direct the user through an
   ** alternate or streamlined set of panels based on their current input.
   ** <p>
   ** A {@link WizardTraversable} class that does not support multiple possible
   ** transitions should just return <code>null</code>.
   **
   ** @return                    <code>null</code> by default.
   */
  @Override
  public Object getExitTransition() {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onEntry (WizardTraversable)
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
   */
  @Override
  public void onEntry(final TraversableContext context) {
    URL    applcationURL   = TemplateWizardUtil.getApplicationURL(context);
    String applicationName = null;
    if (applcationURL == null) {
      URL workingURL = null;
      Provider preferences = Provider.instance(Preferences.getPreferences());
      if (preferences.workspace() != null)
        workingURL = preferences.workspace();
      else
        workingURL = Ide.getWorkspaces().getActiveWorkspaceURL();
      if (workingURL == null) {
        workingURL = Ide.getWorkspaces().getWorkDirectory();
        if (workingURL == null)
          workingURL = URLFactory.newDirURL(Ide.getWorkDirectory());
      }
      applcationURL   = URLFactory.newUniqueURL(workingURL, nameGenerator());
      applicationName = URLFileSystem.getFileName(applcationURL);
    }
    else {
      applicationName = URLFileSystem.getFileName(URLFileSystem.getParent(applcationURL));
    }

    this.workspacePanel.setDirectoryURL(applcationURL);
    this.workspacePanel.setFileName(applicationName + Workspace.EXT);
    // setting the file name of the application will also change the application
    // directory hence set it again this stupid behavior cannot be changed due
    // to setting the file name expects that a directory URL was set at first
    this.workspacePanel.setDirectoryURL(applcationURL);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onExit (WizardTraversable)
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

    try {
      TemplateWizardUtil.setApplicationURL(this.workspacePanel.getNewFilePanel().getFileURL(), context);
    }
    catch (TraversalException e) {
      this.workspacePanel.getNewFilePanel().getFileField().requestFocus();
      throw e;
    }

    final String packageRoot = this.packageRoot();
    if (!IdeUtil.isPackageIdentifier(packageRoot)) {
      this.packageRootEdit.requestFocus();
      throw new TraversalException(TemplateWizardArb.format(TemplateWizardArb.NEW_APPLICATION_PACKAGE_ERROR_MESSAGE, packageRoot));
    }
    TemplateWizardUtil.setApplicationPackage(packageRoot, context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mustCommitWizardChanges (WizardTraversable)
  /**
   ** Indicates whether or not
   ** {@link #commitWizardChanges(Project, Workspace, TraversableContext)}
   ** should be called even if the wizard page has not been visited, and thus
   ** the UI not changed.
   **
   ** @param  context            the data object where changes made in the UI
   **                            are stored for use during the lifetime of the
   **                            wizard.
   **
   ** @return                    <code>true</code> specifies that
   **                            {@link #commitWizardChanges(Project, Workspace, TraversableContext)}
   **                            should be called even if the page was not
   **                            visted before the wizard was finished,
   **                            <code>false</code> indicates that it's okay to
   **                            finish the wizard without calling
   **                            {@link #commitWizardChanges(Project, Workspace, TraversableContext)}
   **                            on this panel.
   */
  @Override
  public boolean mustCommitWizardChanges(final TraversableContext context) {
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commitWizardChanges (WizardTraversable)
  /**
   ** Called when the user clicks Finish on the wizard to commit the changes in
   ** the <code>context</code>. This is were the custom settings should be
   ** applied to the project.
   ** <p>
   ** The wizard framework handles setting the technology scope in the generated
   ** project, so classes that implement {@link WizardTraversable} should not
   ** set the technology scope on their own. Any registered listeners are
   ** notified as a result of technologies being added to the project, so only
   ** operations which are not handled as a result of technology changes should
   ** be handled here.
   **
   ** @param  project            the project that was created as a result of
   **                            finishing the wizard. Listeners should make
   **                            whatever changes are applicable directly on
   **                            this project.
   ** @param  workspace          the workspace that was created as a result of
   **                            finishing the wizard. In the case of the
   **                            <b>New Project</b> wizard, this value is the
   **                            not a new workspace but the workspace that was
   **                            active when the wizard was invoked.
   ** @param  context            the data object where changes made in the UI
   **                            are stored for use during the lifetime of the
   **                            wizard.
   */
  @Override
  public void commitWizardChanges(final Project project, final Workspace workspace, final TraversableContext context) {
    // intentionally left blank
  }

  ////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  ////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nameGenerator
  /**
   ** Returns the default implementation of the {@link NameGenerator} interface
   ** used when generating new unique URLs from URLFactory.
   ** <p>
   ** This implementation supports producing a name by incrementing a counter
   ** that is appended to the name before its suffix.
   **
   ** @return                    the default implementation of the
   **                            {@link NameGenerator} interface.
   */
  public final NameGenerator nameGenerator() {
    String name = TemplateWizardArb.getString(TemplateWizardArb.NEW_APPLICATION_ROOT_NAME);
    return ModelUtil.hasLength(this.applicationName) && !ModelUtil.areEqual(name, this.applicationName) ? new DefaultNameGenerator(this.applicationName, null, -1) : new DefaultNameGenerator(name, null, 1);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeLayout
  /**
   ** Layout the panel.
   */
  private void initializeLayout() {
    this.setLayout(new GridBagLayout());
    this.add(this.workspacePanel,   new GridBagConstraints(0, 0, 2, 1, 1.0D, 0.0D, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(this.packageRootLabel, new GridBagConstraints(0, 2, 2, 1, 1.0D, 0.0D, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 2, 0), 0, 0));
    this.add(this.packageRootEdit,  new GridBagConstraints(0, 3, 2, 1, 1.0D, 0.0D, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(this.spacer,           new GridBagConstraints(0, 5, 2, 1, 0.0D, 1.0D, GridBagConstraints.LINE_START, GridBagConstraints.BOTH,       new Insets(0, 0, 0, 0), 0, 0));
    this.spacer.setVisible(true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localizeComponent
  /**
   ** Localizes human readable text for all controls.
   */
  private void localizeComponent() {
    ResourceUtils.resLabel(this.packageRootLabel, this.packageRootEdit, TemplateWizardArb.getString(TemplateWizardArb.NEW_APPLICATION_ROOT_PACKAGE_LABEL));
    this.workspacePanel.setFilePrompt(TemplateWizardArb.getString(TemplateWizardArb.NEW_APPLICATION_NAME_LABEL));
    this.workspacePanel.getNewFilePanel().setCanOverwriteOpenNodes(true);
  }
}