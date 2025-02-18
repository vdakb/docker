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

    System      :   JDeveloper Identity and Access Extensions
    Subsystem   :   Identity and Access Management Facilities

    File        :   TemplateFeaturePage.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    TemplateFeaturePage.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.32  2012-10-20  DSteding    Cloning feature implemented.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
    12.2.1.3.42.60.93  2021-02-14  DSteding    Dropped unsupported Application
                                               Servers OC4J, JBoss and Glasfish
*/

package oracle.jdeveloper.workspace.iam.wizard;

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;

import java.net.URL;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import oracle.javatools.data.HashStructure;

import oracle.ide.net.URLChooser;
import oracle.ide.net.URLFileSystem;
import oracle.ide.net.DefaultURLFilter;

import oracle.ide.model.Project;
import oracle.ide.model.Workspace;

import oracle.ide.panels.TraversalException;
import oracle.ide.panels.TraversableContext;
import oracle.ide.panels.DefaultTraversablePanel;

import oracle.ide.dialogs.DialogUtil;

import oracle.jdeveloper.model.ApplicationContent;

import oracle.jdeveloper.template.wizard.WizardTraversable;
import oracle.jdeveloper.template.wizard.TemplateWizardUtil;

import oracle.jdeveloper.workspace.iam.utility.StringUtility;

import oracle.jdeveloper.workspace.iam.resource.ComponentBundle;

import oracle.jdeveloper.workspace.iam.model.DataStructureAdapter;

////////////////////////////////////////////////////////////////////////////////
// abstract class TemplateFeaturePage
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** This is a <b><i>New Object</i></b> wizard page, used as the object creation
 ** wizard for Identity and Access Management Applications and Projects.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.93
 ** @since   11.1.1.3.37.56.13
 */
public abstract class TemplateFeaturePage extends    DefaultTraversablePanel
                                          implements WizardTraversable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  protected static final String               WEBLOGIC          = "weblogic";
  protected static final String               WEBSPHERE         = "websphere";

  protected static final Map<String, String>  PLATFORM          = new LinkedHashMap<String, String>();

  protected static final Map<String, Integer> SYMBOL            = new HashMap<String, Integer>();

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-6620687059497560664")
  private static final long                    serialVersionUID = -2029069026961861505L;

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    PLATFORM.put(WEBLOGIC,  "Oracle WebLogic Server");
    PLATFORM.put(WEBSPHERE, "WebSphere Application Server");

    SYMBOL.put(WEBLOGIC,  ComponentBundle.VENDOR_ORACLE);
    SYMBOL.put(WEBSPHERE, ComponentBundle.VENDOR_IBM);
  };

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class FeatureClone
  // ~~~~~ ~~~~~~~~~~~~
  /**
   ** An {@link ActionListener} to clone an existing feature configuration
   */
  protected static class FeatureClone implements ActionListener {

    ////////////////////////////////////////////////////////////////////////////
    // static attributes
    ////////////////////////////////////////////////////////////////////////////

    static URL                 last;

    ////////////////////////////////////////////////////////////////////////////
    // instances attributes
    ////////////////////////////////////////////////////////////////////////////

    final  String              contextKey;
    final  TemplateFeaturePage page;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an <code>FeatureClone</code> that refers to the specified
     ** {@link TemplateFeaturePage} and maintains <code>contextKey</code> in
     ** the {@link TraversableContext} of the wizard.
     **
     ** @param  page             the {@link TemplateFeaturePage} that needs to
     **                          be refreshed after this {@link ActionListener}
     **                          is performed.
     ** @param  contextKey       the identifier of a {@link HashStructure} that
     **                          is part of the {@link TraversableContext} and
     **                          belongs to <code>page</code>.
     */
    public FeatureClone(final TemplateFeaturePage page, final String contextKey) {
      // ensure inheritance
      super();

      // initialize instance
      this.page       = page;
      this.contextKey = contextKey;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   actionPerformed (ActionListener)
    /**
     ** Invoked when an action occurs.
     **
     ** @param  event            a {@link ActionEvent} object describing the
     **                          event source and the property that has changed.
     */
    @Override
    public void actionPerformed(final ActionEvent event) {
      if (chooseSource() && FeatureClone.last != null && this.page.propertyStorage() != null) {
        // clone the data
        this.page.propertyStorage().clone(FeatureClone.last);
        // mark the storage to be overridden
        this.page.propertyStorage().override(true);
        // reinitialize the page
        this.page.initializePage();
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   chooseSource
    /**
     ** Select a file that will be the source to clone.
     **
     ** @return                  the {@link URL} to the file that will be the
     **                          source of the cloning process.
     */
    protected boolean chooseSource() {
      URL base = null;
      if (FeatureClone.last != null && URLFileSystem.exists(FeatureClone.last))
        base = URLFileSystem.getParent(FeatureClone.last);

      final DefaultURLFilter filter  = new DefaultURLFilter(ComponentBundle.string(ComponentBundle.CONFIG_CLONEFROM_LABEL), ".xml");
      final URLChooser       chooser = DialogUtil.newURLChooser(base);
      chooser.setShowJarsAsDirs(false);
      chooser.setSelectionMode(URLChooser.SINGLE_SELECTION);
      chooser.setSelectionScope(URLChooser.FILES_ONLY);
      chooser.setURLFilter(filter);

      final int response = chooser.showOpenDialog(this.page, ComponentBundle.string(ComponentBundle.FILE_TITLE));
      if (response == URLChooser.APPROVE_OPTION)
        FeatureClone.last = chooser.getSelectedURL();
      return (response == URLChooser.APPROVE_OPTION);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>TemplateFeaturePage</code> that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TemplateFeaturePage() {
    // ensure inheritance
    super();

    // initialize UI components
    initializeComponent();
    localizeComponent();
    initializeLayout();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   propertyStorage
  /**
   ** Returns the designtime object of this wizard page
   **
   ** @return                    the designtime object of this wizard page.
   */
  public abstract DataStructureAdapter propertyStorage();

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

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
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commitWizardChanges (WizardTraversable)
  /**
   ** Called when the user clicks <code>Finish</code> on the wizard to commit
   ** the changes in the data context. This is were the custom settings should
   ** be applied to the project.
   ** <p>
   ** The wizard framework handles setting the technology scope in the generated
   ** project, so classes that implement {@link TemplateFeaturePage} should not
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
   **                            <b><i>New Project</i></b> wizard, this value is
   **                            the not a new workspace but the workspace that
   **                            was active when the wizard was invoked.
   ** @param  context            the data object where changes made in the UI
   **                            are stored for use during the lifetime of the
   **                            wizard.
   */
  @Override
  public void commitWizardChanges(final Project project, final Workspace workspace, final TraversableContext context) {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

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
    commit(context.getDirection() != TraversableContext.BACKWARD_TRAVERSAL);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeComponent
  /**
   ** Initialize the component constraints.
   */
  protected abstract void initializeComponent();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localizeComponent
  /**
   ** Localizes human readable text for all controls.
   */
  protected abstract void localizeComponent();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeLayout
  /**
   ** Layout the panel.
   */
  protected abstract void initializeLayout();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializePage
  /**
   ** The data that the <code>Traversable</code> should use to populate its UI
   ** components comes from the {@link TraversableContext} passed to this page
   ** on method {@link #onEntry(TraversableContext)}.
   */
  protected abstract void initializePage();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commit
  /**
   ** Called to have this {@link WizardTraversable} perform the commit action.
   **
   ** @param  validate           <code>true</code> if validation of the page is
   **                            required.
   **
   ** @throws TraversalException if the commit should be aborted.
   */
  protected abstract void commit(final boolean validate)
    throws TraversalException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateFolder
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>folderName</code> must be evaluate to a
   ** valid directory that is readable.
   **
   ** @param  folder             the {@link URL} to the folder to validate.
   ** @param  message            the human readable string of the exception
   **                            if a violation occurs.
   **
   ** @throws TraversalException if <code>value</code> violates the conditions.
   */
  protected final void validateFolder(final URL folder, final String message)
    throws TraversalException {

    if (folder == null || StringUtility.empty(folder.toExternalForm()))
      // notify the user about an unanticipated condition that prevents the
      // wizard from completing successfully
      throw new TraversalException(message);

    if (!(URLFileSystem.exists(folder) && URLFileSystem.isDirectory(folder) && URLFileSystem.canRead(folder)))
      // notify the user about an unanticipated condition that prevents the
      // wizard from completing successfully
      throw new TraversalException(message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateFile
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>folderName</code> must be evaluate to a
   ** valid directory that is readable.
   **
   ** @param  file               the {@link URL} to the file to validate.
   ** @param  message            the human readable string of the exception
   **                            if a violation occurs.
   **
   ** @throws TraversalException if <code>value</code> violates the conditions.
   */
  protected final void validateFile(final URL file, final String message)
    throws TraversalException {

    if (file == null || StringUtility.empty(file.toExternalForm()))
      // notify the user about an unanticipated condition that prevents the
      // wizard from completing successfully
      throw new TraversalException(message);

    if (!(URLFileSystem.exists(file) && URLFileSystem.isRegularFile(file) && URLFileSystem.canRead(file)))
      // notify the user about an unanticipated condition that prevents the
      // wizard from completing successfully
      throw new TraversalException(message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateString
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>value</code> must be evaluate to a
   ** non-empty string.
   **
   ** @param  value              the value to validate.
   ** @param  message            the human readable string of the exception
   **                            if a violation occurs.
   **
   ** @throws TraversalException if <code>value</code> violates the conditions.
   */
  protected final void validateString(final String value, final String message)
    throws TraversalException {

    if (StringUtility.empty(value))
      // notify the user about an unanticipated condition that prevents the
      // wizard from completing successfully
      throw new TraversalException(message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateRange
  /**
   ** Checks if the instance satisfies all requirements.
   ** <p>
   ** In other terms the entry <code>value</code> must be evaluate to a
   ** non-empty string.
   **
   ** @param  value              the value to validate.
   ** @param  lowerBound         the lower bound value.
   ** @param  higherBound        the higher bound value.
   ** @param  message            the human readable string of the exception
   **                            if a range violation occurs.
   **
   ** @throws TraversalException if <code>value</code> violates the conditions.
   */
  protected final void validateRange(final int value, final int lowerBound, final int higherBound, final String message)
    throws TraversalException {

    if ((value < lowerBound) || (value > higherBound))
      // notify the user about an unanticipated condition that prevents the
      // wizard from completing successfully
      throw new TraversalException(message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   integer
  /**
   ** Converts the given String value to an native <code>int</code> value.
   **
   ** @param  value              the value to validate.
   ** @param  message            the human readable string of the exception
   **                            if a format violation occurs.
   **
   ** @return                    the converted value
   **
   ** @throws TraversalException if <code>value</code> violates the conditions.
   */
  protected final int integer(final String value, final String message)
    throws TraversalException {

    // checking valid integer using parseInt() method
    try {
      return Integer.parseInt(value);
    }
    catch (NumberFormatException e) {
      // notify the user about an unanticipated condition that prevents the
      // wizard from completing successfully
      throw new TraversalException(message);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   packagePrefix
  /**
   ** Returns the package prefix assigned to the application.
   **
   ** @param  context            the {@link TraversableContext} of the current
   **                            workspace.
   **
   ** @return                    the package prefix assigned to the application.
   */
  protected final String packagePrefix(final TraversableContext context) {
    // we need to ask too for specific properties from the workspace to
    // initialize the model
    final Workspace workspace = TemplateWizardUtil.getApplication(context);
    return workspace.getProperty(ApplicationContent.APPLICATION_PACKAGE_PREFIX_KEY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   packagePrefix
  /**
   ** Returns the package prefix assigned to the application.
   **
   ** @param  packagePrefix      the package prefix.
   ** @param  path               the package path.
   **
   ** @return                    the package prefix assigned to the application.
   */
  protected final String packagePath(final String packagePrefix, final String path) {
    return packagePrefix.replace('.', '/')   + "/" + path + "/**/*.class";
  }
}