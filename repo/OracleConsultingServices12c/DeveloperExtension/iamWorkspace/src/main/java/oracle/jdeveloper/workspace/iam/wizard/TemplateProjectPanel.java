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

    File        :   TemplateProjectPanel.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    TemplateProjectPanel.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.69  2017-31-01  DSteding    Changed compatibility annotation
                                               accordingliy to the build.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.wizard;

import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

import java.net.URL;

import java.awt.Component;
import java.awt.BorderLayout;

import java.beans.PropertyChangeListener;

import oracle.javatools.data.HashStructure;
import oracle.javatools.data.PropertyStorage;

import oracle.ide.help.HelpSystem;

import oracle.ide.model.Project;
import oracle.ide.model.Workspace;
import oracle.ide.model.TechnologyScope;
import oracle.ide.model.TechnologyScopeConfiguration;

import oracle.ide.net.URLFactory;
import oracle.ide.net.URLFileSystem;
import oracle.ide.net.DefaultNameGenerator;

import oracle.ide.panels.TraversalException;
import oracle.ide.panels.TraversableContext;
import oracle.ide.panels.DefaultTraversablePanel;

import oracle.ide.component.NewFilePanel;

import oracle.jdeveloper.model.TechnologiesPanel;

import oracle.jdeveloper.template.ProjectTemplate;

import oracle.jdeveloper.template.wizard.TemplateWizardArb;
import oracle.jdeveloper.template.wizard.TemplateWizardUtil;
import oracle.jdeveloper.template.wizard.WizardTraversable;

import oracle.jdeveloper.workspace.iam.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// class TemplateProjectPanel
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** The UI panel that provides support to create a {@link Project}.
 ** <p>
 ** This class has to be exists due to the implementation of class
 ** oracle.jdeveloper.template.wizard.NewProjectFromTemplatePanel is package
 ** proteced. Hence we have to create our own class ApplicationPanel that do the
 ** same Job.
 ** <p>
 ** No chance to reuse the existing class!!!
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class TemplateProjectPanel extends    DefaultTraversablePanel
                                  implements WizardTraversable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-3081255030036979767")
  private static final long       serialVersionUID  = -3719839141196255992L;

  private static final String     HELP_TOPIC        = "f1_idednewappprojname_html";

  private static final String     CLASS_PREFIX      = "oracle.jdeveloper.template.wizard.NewProjectFromTemplatePanel";
  private static final String     WORKSPACE_URL_KEY = CLASS_PREFIX + "_workspace.url.key";
  private static final String     PANEL_VISITED_KEY = CLASS_PREFIX + "_panel.visited.key";
  private static final String     UNIQUE_ID_KEY     = CLASS_PREFIX + "_unique.id.key";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final NewFilePanel      projectPanel      = new NewFilePanel();
  private final TechnologiesPanel technologiesPanel = new TechnologiesPanel();
  private final UUID              uniqueId          = UUID.randomUUID();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>TemplateProjectPanel</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TemplateProjectPanel() {
    // ensure inheritance
    super();

    HelpSystem.getHelpSystem().registerTopic(this, HELP_TOPIC);

    localizeComponent();
    initializeComponent();
    initializeLayout();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  ////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getHelpID (overridden)
  /**
   ** Returns the context-sensitive help topic ID to use for this
   ** <code>Traversable</code>.
   **
   ** @return                    the context-sensitive help topic ID.
   */
  @Override
  public String getHelpID() {
    return HELP_TOPIC;
  }

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
    return this.projectPanel.getDefaultFocusComponent();
  }

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
    TechnologyScope scope = technologyScope(context);
    // if we got a technology scope for the project apply it to the project
    // this will raise a StructureChangeEvent where the wizard that has
    // intantiated this page is listening on to finalize the configuration of
    // the project
    if (scope != null)
      TechnologyScopeConfiguration.getInstance(project).setTechnologyScope(scope);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  ////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   uniqueProjectURL
  public static URL uniqueProjectURL(final String projectName, final URL workspaceURL, final TraversableContext wizardContext) {
    final URL parentDir   = URLFileSystem.getParent(workspaceURL);
    final URL projectDir  = uniqueProjectDir(projectName, parentDir, gatherProjectFolderURLs(wizardContext));
    final String fileName = URLFileSystem.getFileName(projectDir);
    return URLFactory.newURL(projectDir, fileName + Project.EXT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   technologyScope
  public static TechnologyScope technologyScope(final TraversableContext wizardContext) {
    TechnologyScope scope;
    if (wizardContext.getDesignTimeObject(TechnologyScopeConfiguration.TECHNOLOGY_SCOPE_PROPERTY) != null)
      scope = (TechnologyScope)wizardContext.getDesignTimeObject(TechnologyScopeConfiguration.TECHNOLOGY_SCOPE_PROPERTY);
    else {
      ProjectTemplate projectTemplate = TemplateWizardUtil.getProjectTemplate(wizardContext);
      scope = projectTemplate.getTechnologyScope();
    }
    return scope;
  }

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
    if (!context.contains(UNIQUE_ID_KEY))
      context.put(UNIQUE_ID_KEY, this.uniqueId);

    final URL projectURL    = TemplateWizardUtil.getApplicationURL(context);
    boolean   templateScope = false;
    if(context.contains(WORKSPACE_URL_KEY))
      templateScope = !URLFileSystem.equals(projectURL, (URL)context.get(WORKSPACE_URL_KEY));

    context.put(WORKSPACE_URL_KEY, projectURL);
    URL tsConfig;
    if (context.contains(PANEL_VISITED_KEY) && TemplateWizardUtil.getProjectURL(context) != null) {
      if (templateScope) {
        tsConfig = this.adjustProjectURL(projectURL, context);
        TemplateWizardUtil.setProjectURL(tsConfig, context);
        this.projectNameAndLocation(tsConfig);
      }
    }
    else {
      context.put(PANEL_VISITED_KEY, Boolean.TRUE);
      tsConfig = defaultProjectURL(projectURL, context);
      projectNameAndLocation(tsConfig);
    }

    ProjectTemplate template   = TemplateWizardUtil.getProjectTemplate(context);
    TechnologyScope technology = template.getTechnologyScope();
    this.technologiesPanel.setUnremoveableTechnologies(technology.toArray());
    if (context.getDesignTimeObject(TechnologyScopeConfiguration.TECHNOLOGY_SCOPE_PROPERTY) == null) {
      TechnologyScopeConfiguration technologyConfig = TechnologyScopeConfiguration.getInstance(new PropertyStorage() {
        public HashStructure getProperties() {
          return HashStructure.newInstance();
        }
      });
      TechnologyScope scope = technologyConfig.getTechnologyScope();
      context.putDesignTimeObject(TechnologyScopeConfiguration.TECHNOLOGY_SCOPE_PROPERTY, scope);
      technology.copyTo(scope);
      this.technologiesPanel.onEntry(context);
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

    this.projectPanel.resetURLCache();
    URL projectURL = this.projectPanel.getFileURL();
    if (!this.uniqueURL(projectURL, context))
      throw new TraversalException(TemplateWizardArb.format(TemplateWizardArb.PROJECT_FROM_TEMPLATE_PANEL_PROJECT_NAME_CONFLICT_MSG, URLFileSystem.getPlatformPathName(projectURL)));

    TemplateWizardUtil.setProjectURL(projectURL, context);
    this.technologiesPanel.onExit(context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   uniqueProjectDir
  private static URL uniqueProjectDir(final String projectName, final URL appDir, final List<URL> inUseURLs) {
    DefaultNameGenerator nameGen;
    if (!StringUtility.empty(projectName)) {
      if (projectName.endsWith(Project.EXT)) {
        int name = projectName.length() - Project.EXT.length();
        String url = projectName.substring(0, name);
        nameGen = new DefaultNameGenerator(url, null, -1);
      }
      else
        nameGen = new DefaultNameGenerator(projectName, null, -1);
    }
    else
      nameGen = new DefaultNameGenerator(Project.getDefaultName(), null, 1);

    URL url1;
    do {
      String name1 = nameGen.nextName();
      url1 = URLFactory.newDirURL(appDir, name1);
      if (url1 == null)
        return null;
    } while(inUseURLs.contains(url1) || URLFileSystem.isBound(url1));
    return url1;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gatherProjectFolderURLs
  /**
   ** Returns the {@link List} of {@link URL}s for all {@link Project}s from the
   ** {link TraversableContext}.
   **
   ** @param  context            the {link TraversableContext} the
   **                            {@link Project}s has to be extracted from.
   **
   ** @return                    the {@link List} of {@link URL}s for all
   **                            {@link Project}s from the {link TraversableContext}.
   */
  private static List<URL> gatherProjectFolderURLs(final TraversableContext context) {
    TraversableContext[] contexts = projects(context);
    List<URL> urls = new ArrayList<URL>(contexts.length);
    for(int i = 0; i < contexts.length; ++i) {
      TraversableContext cursor = contexts[i];
      URL url = TemplateWizardUtil.getProjectURL(cursor);
      if(url != null) {
        URL dir = URLFileSystem.getParent(url);
        if(dir != null)
          urls.add(URLFactory.newDirURL(dir, StringUtility.EMPTY));
      }
    }
    return urls;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   projects
  /**
   ** Return all {@link Project}s from the {link TraversableContext}.
   **
   ** @param  context            the {link TraversableContext} the
   **                            {@link Project}s has to be extracted from.
   **
   ** @return                    all {@link Project}s from the specified
   **                            {link TraversableContext}.
   */
  private static TraversableContext[] projects(final TraversableContext context) {
    TraversableContext application = TemplateWizardUtil.getApplicationData(context);
    return TemplateWizardUtil.getProjectTraversableContexts(application);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addTechnologyPropertyChangeListener
  public void addTechnologyPropertyChangeListener(final PropertyChangeListener listener) {
    this.technologiesPanel.addTechnologyPropertyChangeListener(listener);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeTechnologyPropertyChangeListener
  public void removeTechnologyPropertyChangeListener(final PropertyChangeListener listener) {
    this.technologiesPanel.removeTechnologyPropertyChangeListener(listener);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   uniqueURL
  /**
   ** Validate the specified URL for uniqueness.
   */
  private boolean uniqueURL(final URL projectURL, final TraversableContext data) {
    final TraversableContext[] contexts = projects(data);
    for (int i = 0; i < contexts.length; ++i) {
      final TraversableContext context = contexts[i];
      if (!this.uniqueId.equals(context.get(UNIQUE_ID_KEY))) {
        final URL url = TemplateWizardUtil.getProjectURL(context);
        if (URLFileSystem.equals(url, projectURL))
          return false;
      }
    }
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   adjustProjectURL
  /**
   ** Sets project name and file information in the UI accordingly to the
   ** context of the specified {@link URL}.
   **
   ** @param  workspaceURL       the location of the {@link Workspace} the
   **                            {@link Project} to create belongs to.
   **
   **
   ** @return                    the location of the {@link Project} in the
   **                            local file system relative to the specified
   **                            {@link URL} the owning {@link Workspace}.
   */
  private URL adjustProjectURL(final URL workspaceURL, final TraversableContext context) {
    URL url;
    try {
      URL e = this.projectPanel.getFileURL();
      url = uniqueProjectURL(URLFileSystem.getFileName(e), workspaceURL, context);
    }
    catch (Exception var5) {
      url = defaultProjectURL(workspaceURL, context);
    }
    return url;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   defaultProjectURL
  /**
   ** Sets project name and file information in the UI accordingly to the
   ** context of the specified {@link URL}.
   **
   ** @param  workspaceURL       the location of the {@link Workspace} the
   **                            {@link Project} to create belongs to.
   **
   **
   ** @return                    the location of the {@link Project} in the
   **                            local file system relative to the specified
   **                            {@link URL} the owning {@link Workspace}.
   */
  private URL defaultProjectURL(final URL workspaceURL, final TraversableContext context) {
    final ProjectTemplate projectTemplate = TemplateWizardUtil.getProjectTemplate(context);
    return uniqueProjectURL(projectTemplate.getContainerName(), workspaceURL, context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   projectNameAndLocation
  /**
   ** Sets project name and file information in the UI accordingly to the
   ** context of the specified {@link URL}.
   **
   ** @param  projectURL         the location of the {@link Project} in the
   **                            local file system.
   */
  private void projectNameAndLocation(final URL projectURL) {
    this.projectPanel.getFileField().setText(StringUtility.EMPTY);
    this.projectPanel.setDirectoryURL(URLFileSystem.getParent(projectURL));
    this.projectPanel.setFileName(URLFileSystem.getFileName(projectURL));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeLayout
  /**
   ** Layout the panel.
   */
  private void initializeLayout() {
    this.setLayout(new BorderLayout(0, 10));
    this.add(this.projectPanel,                     BorderLayout.NORTH);
    this.add(this.technologiesPanel.getComponent(), BorderLayout.CENTER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localizeComponent
  /**
   ** Localizes human readable text for all controls.
   */
  private void localizeComponent() {
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeComponent
  /**
   ** Initialize the component constraints.
   */
  private void initializeComponent() {
    this.projectPanel.setFileExtension(Project.EXT);
    this.projectPanel.setFileType(TemplateWizardArb.getString(TemplateWizardArb.PROJECT_FROM_TEMPLATE_PANEL_PROJECT_FILE_TYPE));
    this.projectPanel.setFilePrompt(TemplateWizardArb.getString(TemplateWizardArb.PROJECT_FROM_TEMPLATE_PANEL_NAME_LABEL));
    this.projectPanel.setDirectoryPrompt(TemplateWizardArb.getString(TemplateWizardArb.PROJECT_FROM_TEMPLATE_PANEL_DIRECTORY_LABEL));
    this.projectPanel.setBrowseButtonLabel(TemplateWizardArb.getString(TemplateWizardArb.PROJECT_FROM_TEMPLATE_PANEL_BROWSE_BUTTON_LABEL));
    this.projectPanel.setShowExtension(false);
    this.projectPanel.setLayoutOrientation(0);
  }
}