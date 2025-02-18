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

    File        :   DeploymentPage.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    DeploymentPage.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.15  2011-05-14  DSteding    Changes in layout to shrink the
                                               hight the page occupies
    11.1.1.3.37.60.34  2023-05-25  DSteding    Separated the deployment
                                               descriptor of a R1 deployment
                                               from the R2 artifacts like
                                               sandboxes.
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

import oracle.ide.net.URLTextField;
import oracle.ide.net.URLFileSystem;

import oracle.ide.panels.TraversableContext;
import oracle.ide.panels.TraversalException;

import oracle.jdeveloper.workspace.iam.model.DataStructureAdapter;

import oracle.jdeveloper.workspace.iam.resource.ComponentBundle;

import oracle.jdeveloper.workspace.iam.wizard.TemplateFeaturePage;

import oracle.jdeveloper.workspace.oim.Bundle;

import oracle.jdeveloper.workspace.oim.model.Export;
import oracle.jdeveloper.workspace.oim.model.Import;
import oracle.jdeveloper.workspace.oim.model.Request;
import oracle.jdeveloper.workspace.oim.model.OIMContext;

////////////////////////////////////////////////////////////////////////////////
// class DeploymentPage
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The UI panel that provides support in the configuration dialog to create a
 ** deployment artifacts.
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
public class DeploymentPage extends TemplateFeaturePage {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-8043373464019299313")
  private static final long      serialVersionUID = -3990122876744861306L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // the properties to configure the deployment target folder
  private transient URLTextField basedirEdit;
  private transient JButton      basedirBrowse;

  // the properties to configure the export file properties
  private transient JTextField   exportFile;
  private transient JTextField   exportProject;
  private transient JTextField   exportTarget;
  private transient JTextField   exportDescription;

  // the properties to configure the import file properties
  private transient JTextField   importFile;
  private transient JTextField   importProject;
  private transient JTextField   importTarget;
  private transient JTextField   importDescription;

  // the properties to configure the import file properties
  private transient JTextField   requestFile;
  private transient JTextField   requestProject;
  private transient JTextField   requestTarget;
  private transient JTextField   requestDescription;

  // the properties to configure the context properties
  private transient JTextField   contextFile;
  private transient JTextField   contextProject;

  private transient OIMContext   contextProvider  = null;
  private transient Export       exportProvider   = null;
  private transient Import       importProvider   = null;
  private transient Request      requestProvider  = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DeploymentPage</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public DeploymentPage() {
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
    return this.contextProvider;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeComponent (TemplateFeaturePage)
  /**
   ** Initialize the component constraints.
   */
  @Override
  protected void initializeComponent() {
    // the properties to configure the deployment target folder
    this.basedirEdit        = new URLTextField();
    this.basedirBrowse      = new JButton();

    // the properties to configure the export file properties
    this.exportFile         = new JTextField();
    this.exportProject      = new JTextField();
    this.exportTarget       = new JTextField();
    this.exportDescription  = new JTextField();

    // the properties to configure the import file properties
    this.importFile         = new JTextField();
    this.importProject      = new JTextField();
    this.importTarget       = new JTextField();
    this.importDescription  = new JTextField();

    // the properties to configure the import file properties
    this.requestFile        = new JTextField();
    this.requestProject     = new JTextField();
    this.requestTarget      = new JTextField();
    this.requestDescription = new JTextField();

    // the properties to configure the context properties
    this.contextFile        = new JTextField();
    this.contextProject     = new JTextField();

    this.basedirBrowse.addActionListener(this.basedirEdit);
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
    , "4dlu, pref, 4dlu, pref, 4dlu, pref, 4dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, 4dlu, pref, 4dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, 4dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, 4dlu, pref, 4dlu, pref, 2dlu, pref, 2dlu, pref"
    //   |     1     |     2     |     3     |     4     |     5     |     6     |     7     |     8     |     9     |    10     |    11     |    12     |    13     |    14     |    15     |    16     |    17     |    18     |    19     |    20
    );
    final DefaultFormBuilder builder    = new DefaultFormBuilder(layout, this);
    final CellConstraints    constraint = new CellConstraints();

    // the 1st logical row of the layout
    int row = 2;
    builder.addSeparator(Bundle.string(Bundle.DEPLOYMENT_HEADER), constraint.xywh(2, row, 5, 1));

    // the 2nd logical row of the layout
    row += 2;
    JLabel label = builder.addLabel(Bundle.string(Bundle.DEPLOYMENT_FOLDER_LABEL), constraint.xy(2, row));
    label.setLabelFor(this.basedirEdit);
    builder.add(this.basedirEdit,   constraint.xyw(4, row, 1));
    builder.add(this.basedirBrowse, constraint.xyw(6, row, 1));

    // the 3rd logical row of the layout
    row += 2;
    builder.addSeparator(Bundle.string(Bundle.EXPORT_HEADER), constraint.xywh(2, row, 5, 1));

    // the 4th logical row of the layout
    row += 2;
    label = builder.addLabel(Bundle.string(Bundle.EXPORT_FILE_LABEL), constraint.xy(2, row));
    label.setLabelFor(this.exportFile);
    builder.add(this.exportFile, constraint.xyw(4, row, 3));

    // the 5th logical row of the layout
    row += 2;
    label = builder.addLabel(Bundle.string(Bundle.EXPORT_PROJECT_LABEL), constraint.xy(2, row));
    label.setLabelFor(this.exportProject);
    builder.add(this.exportProject, constraint.xyw(4, row, 3));

    // the 6th logical row of the layout
    row += 2;
    label = builder.addLabel(Bundle.string(Bundle.EXPORT_TARGET_LABEL), constraint.xy(2, row));
    label.setLabelFor(this.exportTarget);
    builder.add(this.exportTarget, constraint.xyw(4, row, 3));

    // the 7th logical row of the layout
    row += 2;
    label = builder.addLabel(Bundle.string(Bundle.EXPORT_DESCRIPTION_LABEL), constraint.xy(2, row));
    label.setLabelFor(this.exportDescription);
    builder.add(this.exportDescription, constraint.xyw(4, row, 3));

    // the 8th logical row of the layout
    row += 2;
    builder.addSeparator(Bundle.string(Bundle.IMPORT_HEADER), constraint.xywh(2, row, 5, 1));

    // the 9th logical row of the layout
    row += 2;
    label = builder.addLabel(Bundle.string(Bundle.IMPORT_FILE_LABEL), constraint.xy(2, row));
    label.setLabelFor(this.importFile);
    builder.add(this.importFile, constraint.xyw(4, row, 3));

    // the 10th logical row of the layout
    row += 2;
    label = builder.addLabel(Bundle.string(Bundle.IMPORT_PROJECT_LABEL), constraint.xy(2, row));
    label.setLabelFor(this.importProject);
    builder.add(this.importProject, constraint.xyw(4, row, 3));

    // the 11th logical row of the layout
    row += 2;
    label = builder.addLabel(Bundle.string(Bundle.IMPORT_TARGET_LABEL), constraint.xy(2, row));
    label.setLabelFor(this.importTarget);
    builder.add(this.importTarget, constraint.xyw(4, row, 3));

    // the 12th logical row of the layout
    row += 2;
    label = builder.addLabel(Bundle.string(Bundle.IMPORT_DESCRIPTION_LABEL), constraint.xy(2, row));
    label.setLabelFor(this.importDescription);
    builder.add(this.importDescription, constraint.xyw(4, row, 3));

    // the 13th logical row of the layout
    row += 2;
    builder.addSeparator(Bundle.string(Bundle.REQUEST_HEADER), constraint.xywh(2, row, 5, 1));

    // the 14th logical row of the layout
    row += 2;
    label = builder.addLabel(Bundle.string(Bundle.REQUEST_FILE_LABEL), constraint.xy(2, row));
    label.setLabelFor(this.requestFile);
    builder.add(this.requestFile, constraint.xyw(4, row, 3));

    // the 15th logical row of the layout
    row += 2;
    label = builder.addLabel(Bundle.string(Bundle.REQUEST_PROJECT_LABEL), constraint.xy(2, row));
    label.setLabelFor(this.requestProject);
    builder.add(this.requestProject, constraint.xyw(4, row, 3));

    // the 16th logical row of the layout
    row += 2;
    label = builder.addLabel(Bundle.string(Bundle.REQUEST_TARGET_LABEL), constraint.xy(2, row));
    label.setLabelFor(this.requestTarget);
    builder.add(this.requestTarget, constraint.xyw(4, row, 3));

    // the 17th logical row of the layout
    row += 2;
    label = builder.addLabel(Bundle.string(Bundle.REQUEST_DESCRIPTION_LABEL), constraint.xy(2, row));
    label.setLabelFor(this.requestDescription);
    builder.add(this.requestDescription, constraint.xyw(4, row, 3));

    // the 18th logical row of the layout
    row += 2;
    builder.addSeparator(Bundle.string(Bundle.CONTEXT_HEADER), constraint.xywh(2, row, 5, 1));

    // the 19th logical row of the layout
    row += 2;
    label = builder.addLabel(Bundle.string(Bundle.CONTEXT_FILE_LABEL), constraint.xy(2, row));
    label.setLabelFor(this.contextFile);
    builder.add(this.contextFile, constraint.xyw(4, row, 3));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localizeComponent (TemplateFeaturePage)
  /**
   ** Localizes human readable text for all controls.
   */
  @Override
  protected final void localizeComponent() {
    this.basedirBrowse.setIcon(ComponentBundle.icon(ComponentBundle.DIRECTORY_ICON));
    this.basedirBrowse.setToolTipText(Bundle.string(Bundle.DEPLOYMENT_FOLDER_HINT));

    IconicButtonUI.install(this.basedirBrowse);
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
    this.basedirEdit.setURL(this.exportProvider.basedir());
    this.exportFile.setText(this.exportProvider.file());
    this.exportProject.setText(this.exportProvider.project());
    this.exportTarget.setText(this.exportProvider.target());
    this.exportDescription.setText(this.exportProvider.description());
    this.importFile.setText(this.importProvider.file());
    this.importProject.setText(this.importProvider.project());
    this.importTarget.setText(this.importProvider.target());
    this.importDescription.setText(this.importProvider.description());
    this.requestFile.setText(this.requestProvider.file());
    this.requestProject.setText(this.requestProvider.project());
    this.requestTarget.setText(this.requestProvider.target());
    this.requestDescription.setText(this.requestProvider.description());
    this.contextFile.setText(this.contextProvider.file());
    this.contextProject.setText(this.contextProvider.project());
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

    commitTargetFolder(validate);
    commitContextName(validate);
    commitContextProject(validate);
    commitExportName(validate);
    commitExportProject(validate);
    commitExportTarget(validate);
    commitExportDescription(validate);
    commitImportName(validate);
    commitImportProject(validate);
    commitImportTarget(validate);
    commitImportDescription(validate);
    commitRequestName(validate);
    commitRequestProject(validate);
    commitRequestTarget(validate);
    commitRequestDescription(validate);
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
    this.contextProvider = OIMContext.instance(context);
    this.exportProvider  = Export.instance(context);
    this.importProvider  = Import.instance(context);
    this.requestProvider = Request.instance(context);

    initializePage();

    // ensure inheritance
    super.onEntry(context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commitContextFile
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>deploymentFolder</code> must be evaluate
   ** to a valid directory.
   **
   ** @param  validate           <code>true</code> if validation of the page is
   **                            required.
   **
   ** @throws TraversalException if the commit should be aborted.
   */
  private final void commitTargetFolder(final boolean validate)
    throws TraversalException {

    // obtained the value from the UI to store.
    final URL value = this.basedirEdit.getURL();
    if (validate)
       validateFolder(value, Bundle.string(Bundle.DEPLOYMENT_FOLDER_ERROR));

    // store the provided value in the context
    if (!URLFileSystem.equals(this.contextProvider.basedir(), value))
      this.contextProvider.basedir(value);

    if (!URLFileSystem.equals(this.exportProvider.basedir(), value))
      this.exportProvider.basedir(value);

    if (!URLFileSystem.equals(this.importProvider.basedir(), value))
      this.importProvider.basedir(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commitContextName
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
  private final void commitContextName(final boolean validate)
    throws TraversalException {

    // obtained the value from the UI to store.
    final String value = this.contextFile.getText();

    // check the rules and validate
    if (validate)
       validateString(value, Bundle.string(Bundle.CONTEXT_FILE_ERROR));

    // store the provided value in the context
    if (!this.contextProvider.file().equals(value))
      this.contextProvider.file(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commitContextProject
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>contextProject</code> must be evaluate to
   ** a valid string.
   **
   ** @param  validate           <code>true</code> if validation of the page is
   **                            required.
   **
   ** @throws TraversalException if the commit should be aborted.
   */
  private final void commitContextProject(final boolean validate)
    throws TraversalException {

    // obtained the value from the UI to store.
    final String value = this.contextProject.getText();

    // check the rules and validate
    if (validate)
       validateString(value, Bundle.string(Bundle.CONTEXT_PROJECT_ERROR));

    // store the provided value in the context
    if (!this.contextProvider.project().equals(value))
      this.contextProvider.project(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commitExportName
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>exportFile</code> must be evaluate to a
   ** valid string.
   **
   ** @param  validate           <code>true</code> if validation of the page is
   **                            required.
   **
   ** @throws TraversalException if the commit should be aborted.
   */
  private final void commitExportName(final boolean validate)
    throws TraversalException {

    // obtained the value from the UI to store.
    final String value = this.exportFile.getText();

    // check the rules and validate
    if (validate)
       validateString(value, Bundle.string(Bundle.EXPORT_FILE_ERROR));

    // store the provided value in the context
    if (!this.exportProvider.file().equals(value))
      this.exportProvider.file(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commitExportProject
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>exportProject</code> must be evaluate to
   ** a valid string.
   **
   ** @param  validate           <code>true</code> if validation of the page is
   **                            required.
   **
   ** @throws TraversalException if the commit should be aborted.
   */
  private final void commitExportProject(final boolean validate)
    throws TraversalException {

    // obtained the value from the UI to store.
    final String value = this.exportProject.getText();

    // check the rules and validate
    if (validate)
       validateString(value, Bundle.string(Bundle.EXPORT_PROJECT_ERROR));

    // store the provided value in the context
    if (!this.exportProvider.project().equals(value))
      this.exportProvider.project(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commitExportTarget
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>exportTarget</code> must be evaluate to a
   ** valid string.
   **
   ** @param  validate           <code>true</code> if validation of the page is
   **                            required.
   **
   ** @throws TraversalException if the commit should be aborted.
   */
  private final void commitExportTarget(final boolean validate)
    throws TraversalException {

    // obtained the value from the UI to store.
    final String value = this.exportTarget.getText();

    // check the rules and validate
    if (validate)
       validateString(value, Bundle.string(Bundle.EXPORT_TARGET_ERROR));

    // store the provided value in the context
    if (!this.exportProvider.target().equals(value))
      this.exportProvider.target(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commitExportDescription
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>exportDescription</code> must be evaluate
   ** to a valid string.
   **
   ** @param  validate           <code>true</code> if validation of the page is
   **                            required.
   **
   ** @throws TraversalException if the commit should be aborted.
   */
  private final void commitExportDescription(final boolean validate)
    throws TraversalException {

    // obtained the value from the UI to store.
    final String value = this.exportDescription.getText();

    // check the rules and validate
    if (validate)
       validateString(value, Bundle.string(Bundle.EXPORT_DESCRIPTION_ERROR));

    // store the provided value in the context
    if (!this.exportProvider.description().equals(value))
      this.exportProvider.description(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commitImportName
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>importFile</code> must be evaluate to a
   ** valid string.
   **
   ** @param  validate           <code>true</code> if validation of the page is
   **                            required.
   **
   ** @throws TraversalException if the commit should be aborted.
   */
  private final void commitImportName(final boolean validate)
    throws TraversalException {

    // obtained the value from the UI to store.
    final String value = this.importFile.getText();

    // check the rules and validate
    if (validate)
       validateString(value, Bundle.string(Bundle.IMPORT_FILE_ERROR));

    // store the provided value in the context
    if (!this.importProvider.file().equals(value))
      this.importProvider.file(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commitImportProject
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>importProject</code> must be evaluate to
   ** a valid string.
   **
   ** @param  validate           <code>true</code> if validation of the page is
   **                            required.
   **
   ** @throws TraversalException if the commit should be aborted.
   */
  private final void commitImportProject(final boolean validate)
    throws TraversalException {

    // obtained the value from the UI to store.
    final String value = this.importProject.getText();

    // check the rules and validate
    if (validate)
       validateString(value, Bundle.string(Bundle.IMPORT_PROJECT_ERROR));

    // store the provided value in the context
    if (!this.importProvider.project().equals(value))
      this.importProvider.project(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commitImportTarget
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>importTarget</code> must be evaluate to a
   ** valid string.
   **
   ** @param  validate           <code>true</code> if validation of the page is
   **                            required.
   **
   ** @throws TraversalException if the commit should be aborted.
   */
  private final void commitImportTarget(final boolean validate)
    throws TraversalException {

    // obtained the value from the UI to store.
    final String value = this.importTarget.getText();

    // check the rules and validate
    if (validate)
       validateString(value, Bundle.string(Bundle.IMPORT_TARGET_ERROR));

    // store the provided value in the context
    if (!this.importProvider.target().equals(value))
      this.importProvider.target(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commitImportDescription
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>importDescription</code> must be evaluate
   ** to a valid string.
   **
   ** @param  validate           <code>true</code> if validation of the page is
   **                            required.
   **
   ** @throws TraversalException if the commit should be aborted.
   */
  private final void commitImportDescription(final boolean validate)
    throws TraversalException {

    // obtained the value from the UI to store.
    final String value = this.importDescription.getText();

    // check the rules and validate
    if (validate)
       validateString(value, Bundle.string(Bundle.IMPORT_DESCRIPTION_ERROR));

    // store the provided value in the context
    if (!this.importProvider.description().equals(value))
      this.importProvider.description(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commitRequestName
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>requestFile</code> must be evaluate to a
   ** valid string.
   **
   ** @param  validate           <code>true</code> if validation of the page is
   **                            required.
   **
   ** @throws TraversalException if the commit should be aborted.
   */
  private final void commitRequestName(final boolean validate)
    throws TraversalException {

    // obtained the value from the UI to store.
    final String value = this.requestFile.getText();

    // check the rules and validate
    if (validate)
       validateString(value, Bundle.string(Bundle.REQUEST_FILE_ERROR));

    // store the provided value in the context
    if (!this.requestProvider.file().equals(value))
      this.requestProvider.file(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commitRequestProject
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>requestProject</code> must be evaluate to
   ** a valid string.
   **
   ** @param  validate           <code>true</code> if validation of the page is
   **                            required.
   **
   ** @throws TraversalException if the commit should be aborted.
   */
  private final void commitRequestProject(final boolean validate)
    throws TraversalException {

    // obtained the value from the UI to store.
    final String value = this.requestProject.getText();

    // check the rules and validate
    if (validate)
       validateString(value, Bundle.string(Bundle.REQUEST_PROJECT_ERROR));

    // store the provided value in the context
    if (!this.requestProvider.project().equals(value))
      this.requestProvider.project(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commitRequestTarget
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>requestTarget</code> must be evaluate to a
   ** valid string.
   **
   ** @param  validate           <code>true</code> if validation of the page is
   **                            required.
   **
   ** @throws TraversalException if the commit should be aborted.
   */
  private final void commitRequestTarget(final boolean validate)
    throws TraversalException {

    // obtained the value from the UI to store.
    final String value = this.requestTarget.getText();

    // check the rules and validate
    if (validate)
       validateString(value, Bundle.string(Bundle.REQUEST_TARGET_ERROR));

    // store the provided value in the context
    if (!this.requestProvider.target().equals(value))
      this.requestProvider.target(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commitRequestDescription
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>requestDescription</code> must be evaluate
   ** to a valid string.
   **
   ** @param  validate           <code>true</code> if validation of the page is
   **                            required.
   **
   ** @throws TraversalException if the commit should be aborted.
   */
  private final void commitRequestDescription(final boolean validate)
    throws TraversalException {

    // obtained the value from the UI to store.
    final String value = this.requestDescription.getText();

    // check the rules and validate
    if (validate)
       validateString(value, Bundle.string(Bundle.REQUEST_DESCRIPTION_ERROR));

    // store the provided value in the context
    if (!this.requestProvider.description().equals(value))
      this.requestProvider.description(value);
  }
}