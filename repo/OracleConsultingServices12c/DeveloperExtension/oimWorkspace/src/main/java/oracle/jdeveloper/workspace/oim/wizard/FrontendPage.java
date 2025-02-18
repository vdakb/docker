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

    File        :   FrontendPage.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    FrontendPage.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.60.29  2012-11-08  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.oim.wizard;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.CellConstraints;

import com.jgoodies.forms.builder.DefaultFormBuilder;

import java.net.URL;

import oracle.javatools.ui.plaf.IconicButtonUI;

import oracle.ide.net.URLFactory;
import oracle.ide.net.URLTextField;
import oracle.ide.net.URLFileSystem;

import oracle.ide.panels.TraversableContext;
import oracle.ide.panels.TraversalException;

import oracle.jdeveloper.workspace.iam.model.DataStructureAdapter;

import oracle.jdeveloper.workspace.iam.parser.ANTFileHandler;
import oracle.jdeveloper.workspace.iam.parser.XMLFileHandlerException;
import oracle.jdeveloper.workspace.iam.resource.ComponentBundle;
import oracle.jdeveloper.workspace.iam.wizard.TemplateFeaturePage;

import oracle.jdeveloper.workspace.oim.Bundle;

import oracle.jdeveloper.workspace.oim.model.CustomizationFrontend;
import oracle.jdeveloper.workspace.oim.parser.CustomizationHandler;

////////////////////////////////////////////////////////////////////////////////
// class FrontendPage
// ~~~~~ ~~~~~~~~~~~~
/**
 ** The UI panel that provides support in the configuration dialog to create a
 ** Identity Manager View Project.
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
 ** @since   11.1.1.3.37.60.29
 */
public class FrontendPage extends TemplateFeaturePage {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-3593716758806538684")
  private static final long               serialVersionUID = -6415832325420866789L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // the properties to configure the project properties
  private transient JTextField            name;
  private transient JTextField            project;
  private transient JTextField            description;

  // the properties to configure the frontend library properties
  private transient JTextField            frontendArchive;
  private transient JTextField            frontendPackage;

  // the properties to configure the custom library properties that needs to be
  // included in the assembly
  private transient URLTextField          backendScript;
  private transient JButton               backendScriptBrowse;

  private transient CustomizationFrontend provider         = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>FrontendPage</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public FrontendPage() {
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
    this.name                = new JTextField();
    this.project             = new JTextField();
    this.description         = new JTextField();
    this.backendScript       = new URLTextField();
    this.backendScriptBrowse = new JButton();

    this.frontendArchive     = new JTextField();
    this.frontendPackage     = new JTextField();

    // allow to choose directories only for the backend library include
    this.backendScript.setDirectoryOnly(false);
    this.backendScriptBrowse.addActionListener(this.backendScript);
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
    , "4dlu, pref, 4dlu, pref, 2dlu, pref, 2dlu, pref, 4dlu, pref, 4dlu, pref, 2dlu, pref, 4dlu, pref, 4dlu, pref"
    //   |     1     |     2     |     3     |     4     |     5     |     6     |     7     |     8     |     9
    );
    final DefaultFormBuilder builder    = new DefaultFormBuilder(layout, this);
    final CellConstraints    constraint = new CellConstraints();

    // the 1st logical row of the layout
    int row = 2;
    builder.addSeparator(Bundle.string(Bundle.PROPERTY_HEADER), constraint.xyw(2, row, 5));

    // the 2nd logical row of the layout
    row += 2;
    JLabel label = builder.addLabel(Bundle.string(Bundle.FILE_LABEL), constraint.xy(2, row));
    label.setLabelFor(this.name);
    builder.add(this.name, constraint.xyw(4, row, 3));

    // the 3rd logical row of the layout
    row += 2;
    label = builder.addLabel(Bundle.string(Bundle.PROJECT_LABEL), constraint.xy(2, row));
    label.setLabelFor(this.project);
    builder.add(this.project, constraint.xyw(4, row, 3));

    // the 4th logical row of the layout
    row += 2;
    label = builder.addLabel(Bundle.string(Bundle.DESCRIPTION_LABEL), constraint.xy(2, row));
    label.setLabelFor(this.description);
    builder.add(this.description, constraint.xyw(4, row, 3));

    // the 5th logical row of the layout
    row += 2;
    builder.addSeparator(Bundle.string(Bundle.FRONTEND_HEADER), constraint.xyw(2, row, 5));

    // the 6th logical row of the layout
    row += 2;
    label = builder.addLabel(Bundle.string(Bundle.FRONTEND_LIBRARY_LABEL), constraint.xy(2, row));
    label.setLabelFor(this.frontendArchive);
    builder.add(this.frontendArchive, constraint.xyw(4, row, 3));

    // the 7th logical row of the layout
    row += 2;
    label = builder.addLabel(Bundle.string(Bundle.FRONTEND_PACKAGE_LABEL), constraint.xy(2, row));
    label.setLabelFor(this.frontendPackage);
    builder.add(this.frontendPackage, constraint.xyw(4, row, 3));

    // the 8th logical row of the layout
    row += 2;
    builder.addSeparator(Bundle.string(Bundle.ASSEMBLY_INCLUDE_HEADER), constraint.xyw(2, row, 6));

    // the 9th logical row of the layout
    row += 2;
    label = builder.addLabel(Bundle.string(Bundle.ASSEMBLY_BACKEND_LABEL), constraint.xy(2, row));
    label.setLabelFor(this.backendScript);
    builder.add(this.backendScript,       constraint.xy(4, row));
    builder.add(this.backendScriptBrowse, constraint.xy(6, row));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localizeComponent (TemplateFeaturePage)
  /**
   ** Localizes human readable text for all controls.
   */
  @Override
  protected final void localizeComponent() {
    this.backendScriptBrowse.setIcon(ComponentBundle.icon(ComponentBundle.FILE_ICON));
    this.backendScriptBrowse.setToolTipText(Bundle.string(Bundle.ASSEMBLY_BACKEND_HINT));

    IconicButtonUI.install(this.backendScriptBrowse);
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
    this.name.setText(this.provider.file());
    this.project.setText(this.provider.project());
    this.description.setText(this.provider.description());

    this.frontendArchive.setText(this.provider.library());
    this.frontendPackage.setText(this.provider.packagePath());
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

    commitName(validate);
    commitProject(validate);
    commitDescription(validate);
    commitFrontend(validate);
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
    this.provider = CustomizationFrontend.instance(context);
    initializePage();

    // ensure inheritance
    super.onEntry(context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commitName
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>name</code> must be evaluate to a valid
   ** string.
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
   ** In other terms the entry <code>project</code> must be evaluate to a valid
   ** string.
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

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commitFrontend
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>frontend</code> <code>package</code>
   ** must be evaluate to a valid string.
   **
   ** @param  validate           <code>true</code> if validation of the page is
   **                            required.
   **
   ** @throws TraversalException if the commit should be aborted.
   */
  private final void commitFrontend(final boolean validate)
    throws TraversalException {

    // obtained the value from the UI to store.
    String value = this.frontendArchive.getText();
    // check the rules and validate
    if (validate)
      validateString(value, Bundle.string(Bundle.FRONTEND_LIBRARY_ERROR));

    // store the provided value in the context
    if (!this.provider.library().equals(value))
      this.provider.library(value);

    // obtained the value from the UI to store.
    value = this.frontendPackage.getText();
    // check the rules and validate
    if (validate)
      validateString(value, Bundle.string(Bundle.FRONTEND_PACKAGE_ERROR));

    // store the provided value in the context
    if (!this.provider.packagePath().equals(value))
      this.provider.packagePath(value);

    // obtained the value from the UI to store.
    final URL file = this.backendScript.getURL();

    // check the rules and validate
    if (validate)
       validateFile(file, Bundle.string(Bundle.ASSEMBLY_BACKEND_ERROR));

    // store the provided value in the context
    if (!URLFileSystem.equals(this.provider.backend(), file))
      this.provider.backend(file);

    // evaluate the target the backend project will build
    ANTFileHandler handler = new CustomizationHandler(file);
    try {
      handler.load();
      this.provider.backendProject(URLFileSystem.getParent(file));

      final String library = handler.propertyValue(CustomizationHandler.LIBRARY) + ".jar";
      final URL    deploy  = URLFactory.newDirURL(this.provider.backendProject(), "lib");
      // <pathelement location="../oimBackend/lib/ocs.iam.identity.ui.backend.jar"/>
      this.provider.backendArchive(URLFactory.newURL(deploy, library));
    }
    catch (XMLFileHandlerException e) {
      // notify the user about an unanticipated condition that prevents the
      // wizard from completing successfully
      throw new TraversalException(e.getLocalizedMessage());
    }
  }
}