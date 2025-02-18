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
    Subsystem   :   Identity Manager Facility

    File        :   CompositePage.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    CompositePage.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.oim.wizard;

import java.net.URL;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.CellConstraints;

import com.jgoodies.forms.builder.DefaultFormBuilder;

import oracle.javatools.ui.plaf.IconicButtonUI;

import oracle.ide.net.URLFileSystem;
import oracle.ide.net.URLTextField;

import oracle.ide.panels.TraversableContext;
import oracle.ide.panels.TraversalException;

import oracle.jdeveloper.workspace.iam.utility.StringUtility;

import oracle.jdeveloper.workspace.iam.model.DataStructureAdapter;

import oracle.jdeveloper.workspace.iam.resource.ComponentBundle;

import oracle.jdeveloper.workspace.iam.wizard.TemplateFeaturePage;

import oracle.jdeveloper.workspace.oim.Bundle;

import oracle.jdeveloper.workspace.oim.model.Workflow;

////////////////////////////////////////////////////////////////////////////////
// class CompositePage
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** The UI panel that provides support in the configuration dialog to create a
 ** SOA Composite.
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
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class CompositePage extends TemplateFeaturePage {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:7415528427052534410")
  private static final long      serialVersionUID = 5278191414442971502L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // the properties to configure the project composite properties
  private transient JTextField   serviceName;
  private transient JTextField   workflowName;

  // the properties to configure the composite deployment properties
  private transient JTextField   partition;
  private transient JTextField   revision;

  // the properties to configure the project build properties
  private transient JTextField   name;
  private transient JTextField   project;
  private transient JTextField   description;
  private transient URLTextField destination;
  private transient JButton      destinationBrowse;

  // the properties to configure the composite library properties
  private transient JTextField   application;
  private transient JTextField   packagePath;

  private transient Workflow     provider         = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>CompositePage</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public CompositePage() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   propertyStorage (TemplateFeaturePage)
  /**
   ** Returns the designtime object of this wizard page
   **
   ** @return                    the designtime object of this wizard page.
   */
  @Override
  public final DataStructureAdapter propertyStorage() {
    return this.provider;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeComponent (TemplateFeaturePage)
  /**
   ** Initialize the component constraints.
   */
  @Override
  protected void initializeComponent() {
    this.serviceName        = new JTextField();
    this.workflowName       = new JTextField();

    this.project           = new JTextField();
    this.description       = new JTextField();
    this.application       = new JTextField();
    this.destination       = new URLTextField();
    this.destinationBrowse = new JButton();
    this.packagePath       = new JTextField();

    this.partition         = new JTextField();
    this.revision          = new JTextField();
    this.name              = new JTextField();

    // allow to choose directories only for the deployment destination
    this.destination.setDirectoryOnly(true);
    this.destinationBrowse.addActionListener(this.destination);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeLayout (TemplateFeaturePage)
  /**
   ** Layout the panel.
   */
  @Override
  protected void initializeLayout() {
    final FormLayout         layout     = new FormLayout(
    //   |         1           |       2        |        3        |
      "4dlu, r:max(50dlu;p), 3dlu, pref:grow, 1dlu, right:15dlu, 4dlu"
    , "8dlu, pref, 4dlu, pref, 3dlu, pref, 6dlu, pref, 4dlu, pref, 3dlu, pref, 3dlu, pref, 6dlu, pref, 4dlu, pref, 3dlu, pref, 6dlu, pref, 4dlu, pref, 3dlu, pref"
    //   |     1     |     2     |     3     |     4     |     5     |     6     |     7     |     8     |     9     |    10     |    11     |    12     |    13
    );
    final DefaultFormBuilder builder    = new DefaultFormBuilder(layout, this);
    final CellConstraints    constraint = new CellConstraints();

    // the 1st logical row of the layout
    int row = 2;
    builder.addSeparator(Bundle.string(Bundle.WORKFLOW_HEADER), constraint.xyw(2, row, 5));

    // the 2nd logical row of the layout
    row += 2;
    JLabel label = builder.addLabel(Bundle.string(Bundle.WORKFLOW_SERVICE_LABEL), constraint.xy(2, row));
    label.setLabelFor(this.serviceName);
    builder.add(this.serviceName, constraint.xyw(4, row, 3));

    // the 3rd logical row of the layout
    row += 2;
    label = builder.addLabel(Bundle.string(Bundle.WORKFLOW_NAME_LABEL), constraint.xy(2, row));
    label.setLabelFor(this.workflowName);
    builder.add(this.workflowName, constraint.xyw(4, row, 3));

    // the 4th logical row of the layout
    row += 2;
    builder.addSeparator(Bundle.string(Bundle.PROPERTY_HEADER), constraint.xyw(2, row, 5));

    // the 5th logical row of the layout
    row += 2;
    label = builder.addLabel(Bundle.string(Bundle.FILE_LABEL), constraint.xy(2, row));
    label.setLabelFor(this.name);
    builder.add(this.name, constraint.xyw(4, row, 3));

    // the 6th logical row of the layout
    row += 2;
    label = builder.addLabel(Bundle.string(Bundle.PROJECT_LABEL), constraint.xy(2, row));
    label.setLabelFor(this.project);
    builder.add(this.project, constraint.xyw(4, row, 3));

    // the 7th logical row of the layout
    row += 2;
    label = builder.addLabel(Bundle.string(Bundle.DESCRIPTION_LABEL), constraint.xy(2, row));
    label.setLabelFor(this.description);
    builder.add(this.description, constraint.xyw(4, row, 3));

    // the 8th logical row of the layout
    row += 2;
    builder.addSeparator(Bundle.string(Bundle.COMPOSITE_HEADER), constraint.xyw(2, row, 6));

    // the 9th logical row of the layout
    row += 2;
    label = builder.addLabel(Bundle.string(Bundle.COMPOSITE_BASE_LABEL), constraint.xy(2, row));
    label.setLabelFor(this.destination);
    builder.add(this.destination,       constraint.xy(4, row));
    builder.add(this.destinationBrowse, constraint.xy(6, row));

    // the 10th logical row of the layout
    row += 2;
    label = builder.addLabel(Bundle.string(Bundle.COMPOSITE_PACKAGE_LABEL), constraint.xy(2, row));
    label.setLabelFor(this.packagePath);
    builder.add(this.packagePath, constraint.xyw(4, row, 3));

    // the 11th logical row of the layout
    row += 2;
    builder.addSeparator(Bundle.string(Bundle.DEPLOYMENT_HEADER), constraint.xyw(2, row, 5));

    // the 12th logical row of the layout
    row += 2;
    label = builder.addLabel(Bundle.string(Bundle.COMPOSITE_PARTITION_LABEL), constraint.xy(2, row));
    label.setLabelFor(this.partition);
    builder.add(this.partition, constraint.xyw(4, row, 3));

    // the 13th logical row of the layout
    row += 2;
    label = builder.addLabel(Bundle.string(Bundle.COMPOSITE_REVISION_LABEL), constraint.xy(2, row));
    label.setLabelFor(this.revision);
    builder.add(this.revision, constraint.xyw(4, row, 3));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localizeComponent (TemplateFeaturePage)
  /**
   ** Localizes human readable text for all controls.
   */
  @Override
  protected final void localizeComponent() {
    this.destinationBrowse.setIcon(ComponentBundle.icon(ComponentBundle.DIRECTORY_ICON));
    this.destinationBrowse.setToolTipText(Bundle.string(Bundle.DESTINATION_BASE_HINT));

    IconicButtonUI.install(this.destinationBrowse);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializePage (TemplateFeaturePage)
  /**
   ** The data that the <code>Traversable</code> should use to populate its UI
   ** components comes from the {@link TraversableContext} passed to this page
   ** on method {@link #onEntry(TraversableContext)}.
   */
  @Override
  protected final void initializePage() {
    this.serviceName.setText(this.provider.service());
    this.workflowName.setText(this.provider.name());

    this.name.setText(this.provider.file());
    this.project.setText(this.provider.project());
    this.description.setText(this.provider.description());

    this.packagePath.setText(this.provider.packagePath());

    this.partition.setText(this.provider.partition());
    this.revision.setText(this.provider.revision());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commit (DefaultPreferencePanel)
  /**
   ** Called to have this {@link TemplateFeaturePage} perform the commit action.
   **
   ** @param  validate           <code>true</code> if validation of the page is
   **                            required.
   **
   ** @throws TraversalException if the commit should be aborted.
   */
  @Override
  protected void commit(final boolean validate)
    throws TraversalException {

    commitService(validate);
    commitWorkflow(validate);
    commitName(validate);
    commitProject(validate);
    commitDescription(validate);
    commitDestination(validate);
    commitComposite(validate);
    commitPartition(validate);
    commitRevision(validate);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
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
   */
  @Override
  public void onEntry(final TraversableContext context) {
    // On entering the panel, we need to populate all fields with properties
    // from the model object.
    this.provider = Workflow.instance(context);
    // we need to ask to for specific properties from the workspace to
    // initialize the model
    final String prefix = packagePrefix(context);
    if (!StringUtility.empty(prefix)) {
      // the package path ist a file path not a java package name hence we have
      // to convert the package name to a path
      this.provider.packagePath(packagePath(prefix, "service/workflow"));
    }

    initializePage();

    // ensure inheritance
    super.onEntry(context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commitPartition
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>partition</code> must be evaluate to a
   ** valid string.
   **
   ** @param  validate           <code>true</code> if validation of the page is
   **                            required.
   **
   ** @throws TraversalException if the commit should be aborted.
   */
  private final void commitPartition(final boolean validate)
    throws TraversalException {

    // obtained the value from the UI to store.
    final String value = this.partition.getText();

    // check the rules and validate
    if (validate)
      validateString(value, Bundle.string(Bundle.COMPOSITE_PARTITION_ERROR));

    // store the provided value in the context
    if (!this.provider.partition().equals(value))
      this.provider.partition(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commitWorkflow
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>workflow</code> must be evaluate to a
   ** valid Java Name.
   **
   ** @param  validate           <code>true</code> if validation of the page is
   **                            required.
   **
   ** @throws TraversalException if the commit should be aborted.
   */
  private final void commitWorkflow(final boolean validate)
    throws TraversalException {

    // obtained the value from the UI to store.
    final String value = this.workflowName.getText();

    // check the rules and validate
    if (validate) {
      // the name of the worflow is mandatory
      if (StringUtility.empty(value))
        // notify the user about an unanticipated condition that prevents the
        // wizard from completing successfully
        throw new TraversalException(Bundle.string(Bundle.WORKFLOW_NAME_ERROR));
      // the name is only valid if it contains only alphanumerical characters
      if (value.indexOf(StringUtility.BLANK) > -1)
        // notify the user about an unanticipated condition that prevents the
        // wizard from completing successfully
        throw new TraversalException(Bundle.string(Bundle.WORKFLOW_NAME_VALID));
    }

    // store the provided value in the context
    if (!this.provider.name().equals(value))
      this.provider.name(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commitRevision
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>revision</code> must be evaluate to a
   ** valid string.
   **
   ** @param  validate           <code>true</code> if validation of the page is
   **                            required.
   **
   ** @throws TraversalException if the commit should be aborted.
   */
  private final void commitRevision(final boolean validate)
    throws TraversalException {

    // obtained the value from the UI to store.
    final String value = this.revision.getText();

    // check the rules and validate
    if (validate)
      validateString(value, Bundle.string(Bundle.COMPOSITE_REVISION_ERROR));

    // store the provided value in the context
    if (!this.provider.revision().equals(value))
      this.provider.revision(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commitService
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>service</code> must be evaluate to a
   ** valid string.
   **
   ** @param  validate           <code>true</code> if validation of the page is
   **                            required.
   **
   ** @throws TraversalException if the commit should be aborted.
   */
  private final void commitService(final boolean validate)
    throws TraversalException {

    // obtained the value from the UI to store.
    final String value = this.serviceName.getText();

    // check the rules and validate
    if (validate)
      validateString(value, Bundle.string(Bundle.WORKFLOW_SERVICE_ERROR));

    // store the provided value in the context
    if (!this.provider.service().equals(value))
      this.provider.service(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commitName
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>contextFile</code> must be evaluate to a
   ** valid string.
   **
   ** @param  validate           <code>true</code> if validation of the page is
   **                            required.
   **
   ** @throws TraversalException if the commit should be aborted.
   */
  private final void commitName(final boolean validate)
    throws TraversalException {

    // obtained the value from the UI to store.
    final String value = this.name.getText();

    // check the rules and validate
    if (validate)
       validateString(value, Bundle.string(Bundle.FILE_ERROR));

    // store the provided value in the context
    if (!this.provider.file().equals(value))
      this.provider.file(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commitProject
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>project</code> must be evaluate to
   ** a valid string.
   **
   ** @param  validate           <code>true</code> if validation of the page is
   **                            required.
   **
   ** @throws TraversalException if the commit should be aborted.
   */
  private final void commitProject(final boolean validate)
    throws TraversalException {

    // obtained the value from the UI to store.
    final String value = this.project.getText();

    // check the rules and validate
    if (validate)
       validateString(value, Bundle.string(Bundle.PROJECT_ERROR));

    // store the provided value in the context
    if (!this.provider.project().equals(value))
      this.provider.project(value);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   commitDescription
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>description</code> must be evaluate to a
   ** valid string.
   **
   ** @param  validate           <code>true</code> if validation of the page is
   **                            required.
   **
   ** @throws TraversalException if the commit should be aborted.
   */
  private final void commitDescription(final boolean validate)
    throws TraversalException {

    // obtained the value from the UI to store.
    final String value = this.description.getText();

    // check the rules and validate
    if (validate)
      validateString(value, Bundle.string(Bundle.DESCRIPTION_ERROR));

    // store the provided value in the context
    if (!this.provider.description().equals(value))
      this.provider.description(value);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   commitDestination
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>destination</code> must be evaluate to a
   ** valid directory.
   **
   ** @param  validate           <code>true</code> if validation of the page is
   **                            required.
   **
   ** @throws TraversalException if the commit should be aborted.
   */
  private final void commitDestination(final boolean validate)
    throws TraversalException {

    // obtained the value from the UI to store.
    final URL value = this.destination.getURL();

    // check the rules and validate
    if (validate)
       validateFolder(value, Bundle.string(Bundle.COMPOSITE_BASE_ERROR));

    // store the provided value in the context
    if (!URLFileSystem.equals(this.provider.destination(), value))
      this.provider.destination(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commitComposite
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>composite</code> <code>package</code>
   ** must be evaluate to a valid string.
   **
   ** @param  validate           <code>true</code> if validation of the page is
   **                            required.
   **
   ** @throws TraversalException if the commit should be aborted.
   */
  private final void commitComposite(final boolean validate)
    throws TraversalException {

    // obtained the value from the UI to store.
    String value = this.packagePath.getText();
    // check the rules and validate
    if (validate)
      validateString(value, Bundle.string(Bundle.COMPOSITE_PACKAGE_ERROR));

    // store the provided value in the context
    if (!this.provider.packagePath().equals(value))
      this.provider.packagePath(value);
  }

}