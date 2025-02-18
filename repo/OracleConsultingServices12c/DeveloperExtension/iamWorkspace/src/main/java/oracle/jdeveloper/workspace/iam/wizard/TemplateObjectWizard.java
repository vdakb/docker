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

    File        :   TemplateObjectWizard.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    TemplateObjectWizard.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.wizard;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

import java.io.IOException;

import java.net.URL;

import java.awt.Color;
import java.awt.Image;

import oracle.javatools.util.ModelUtil;

import oracle.javatools.data.ChangeBuffer;

import oracle.javatools.dialogs.ExceptionDialog;

import oracle.bali.ewt.wizard.Wizard;
import oracle.bali.ewt.wizard.WizardPage;
import oracle.bali.ewt.wizard.WizardEvent;
import oracle.bali.ewt.wizard.WizardDialog;
import oracle.bali.ewt.wizard.WizardListener2;

import oracle.bali.ewt.wizard.dWizard.DWizard;
import oracle.bali.ewt.wizard.dWizard.WizardSequence2;
import oracle.bali.ewt.wizard.dWizard.WizardSequenceEvent;
import oracle.bali.ewt.wizard.dWizard.WizardSequenceListener;

import oracle.ide.Ide;
import oracle.ide.Context;

import oracle.ide.model.Project;
import oracle.ide.model.Observer;
import oracle.ide.model.Workspace;

import oracle.ide.cmd.CloseNodeCommand;

import oracle.ide.panels.TraversableContext;

import oracle.ide.docking.DockableWindow;

import oracle.ide.explorer.TNode;
import oracle.ide.explorer.TreeExplorer;

import oracle.ide.navigator.NavigatorManager;
import oracle.ide.navigator.NavigatorWindow;

import oracle.jdeveloper.cmd.NewEmptyProjectCommand;

import oracle.jdeveloper.model.ApplicationContent;

import oracle.jdeveloper.template.ProjectTemplate;
import oracle.jdeveloper.template.AbstractTemplate;
import oracle.jdeveloper.template.ApplicationTemplate;

import oracle.jdeveloper.template.wizard.TemplateWizardArb;
import oracle.jdeveloper.template.wizard.TemplateWizardUtil;

import oracle.jdeveloper.workspace.iam.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// abstract class TemplateObjectWizard
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** Implementation of the "Oracle Identity and Access Management Application"
 ** dialog page flow.
 ** <p>
 ** This class has to be exists due to the implementation of class
 ** oracle.jdeveloper.template.wizard.NewApplicationPanel and
 ** oracle.jdeveloper.template.wizard.NewProjectFromTemplatePanel are package
 ** proteced. Hence we have to create our own class ApplicationPanel that do the
 ** same Job.
 ** <br>
 ** Also we have to find the plug-in-point to intercept the creatino of a
 ** project in a common matter. This is only possible if the method
 ** {@link #commitProjectChanges(Wizard, List, Workspace, Context)} is
 ** overridden. The reason is that the class <code>TemplateWizardPage</code>
 ** has to be replaced by our own implementation. the mentioned class is final.
 ** <p>
 ** No chance to reuse the existing class!!!
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public abstract class TemplateObjectWizard implements Observer {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final Color[] WIZARD_HEADER_GRADIENT = {
    new Color(254, 254, 254)
  , new Color(206, 223, 230)
  };

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private DWizard              wizardView;
  private WizardDialog         wizardDialog;
  private TraversableContext   wizardContext;
  private WizardAdapter        wizardAdapter;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class WizardAdapter
  // ~~~~~ ~~~~~~~~~~~~~
  /**
   ** Implementation of ....
   */
  protected class WizardAdapter implements WizardListener2
                                ,          WizardSequenceListener {

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   wizardSelectionChanged (WizardListener)
    @Override
    public void wizardSelectionChanged(final WizardEvent event) {
      WizardPage page = event.getPage();
      if (page != null) {
        // due to this we have to reinvent the wheel
        ((TemplateObjectPage)page).enterPage();
        TemplateObjectWizard.this.updateWizardTitle();
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   wizardApplyState (WizardListener)
    @Override
    public void wizardApplyState(final WizardEvent event) {
      // intentionally left blank
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   wizardCanceled (WizardListener)
    @Override
    public void wizardCanceled(final WizardEvent event) {
      // intentionally left blank
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   wizardFinished (WizardListener)
    @Override
    public void wizardFinished(final WizardEvent event) {
      final Workspace     workspace = TemplateObjectWizard.this.workspaceContext();
      // add the projects to the workspace
      final List<Project> project   = TemplateObjectWizard.this.createProjects();
      if (project.size() > 0)
        workspace.add(project, true);

      // perform post action on the artifacts created by the page flow of the wizard
      final Context context = TemplateWizardUtil.getContext(TemplateObjectWizard.this.wizardContext);
      TemplateObjectWizard.this.commitProjectChanges((Wizard)event.getSource(), project, workspace, context);
      // commit the workspace changes
      // a Workspace will allays be saved regardless a project is assigned or
      // not
      try {
        workspace.save();
      }
      catch (IOException e) {
        ExceptionDialog.showExceptionDialog(Ide.getMainWindow(), e);
      }
      // start post processing
      TemplateObjectWizard.this.postProcessing();
      // set the workspace as the curent that subsequently invoked operations
      // can refere to it
      Ide.setActiveWorkspace(workspace);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   wizardSaveState (WizardListener2)
    @Override
    public void wizardSaveState(final WizardEvent event) {
      // intentionally left blank
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   wizardClosed (WizardListener2)
    @Override
    public void wizardClosed(final WizardEvent event) {
      // intentionally left blank
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   pagesAdded (WizardSequenceListener)
    @Override
    public void pagesAdded(final WizardSequenceEvent event) {
      TemplateObjectWizard.this.updateWizardTitle();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   pagesRemoved (WizardSequenceListener)
    @Override
    public void pagesRemoved(final WizardSequenceEvent event) {
      TemplateObjectWizard.this.updateWizardTitle();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   wizardContext
  /**
   ** Returns the {@link TraversableContext} of this {@link Wizard}.
   **
   ** @return                    the {@link TraversableContext} of this
   **                            {@link Wizard}.
   */
  protected final TraversableContext wizardContext() {
    return this.wizardContext;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   expandProject
  /**
   ** Expands the specified {@link Project} in the application navigator.
   **
   ** @param  project            the specified {@link Project} in the
   **                            application navigator.
   */
  public void expandProject(final Project project) {
    NavigatorManager navigator = NavigatorManager.getApplicationNavigatorManager();
    if (navigator == null)
      navigator = NavigatorManager.getWorkspaceNavigatorManager();

    NavigatorWindow window = null;
    if (navigator != null)
      window = navigator.getNavigatorWindow();
    if (window != null) {
      TreeExplorer explorer = window.getTreeExplorer();
      if (explorer == null) {
        List<DockableWindow> tnode = window.getHostedDockables();
        if (tnode != null) {
          Iterator<DockableWindow> i = tnode.iterator();
          while (i.hasNext()) {
            DockableWindow dockable = i.next();
            if (dockable instanceof NavigatorWindow) {
              NavigatorWindow nestedWindow = (NavigatorWindow)dockable;
              TreeExplorer nested = nestedWindow.getTreeExplorer();
              if (nested != null) {
                explorer = nested;
                break;
              }
            }
          }
        }
      }

      if (explorer != null) {
        TNode tnode1 = explorer.searchTNode(project, explorer.getRoot());
        if (tnode1 != null)
          explorer.expand(tnode1, true);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   runWizard
  /**
   ** All what's called inside is declared private (--&gt; reinvent the wheel)
   **
   ** @param  context            the global context of the Oracle JDeveloper
   **                            IDE.
   **                            <br>
   **                            Allowed object is {@link Context}.
   ** @param  template           either a {@link ApplicationTemplate} or a
   **                            {@link ProjectTemplate} to create the steps of
   **                            this {@link Wizard}
   **                            <br>
   **                            Allowed object is {@link AbstractTemplate}.
   **
   ** @return                    the user action chosen to exit the
   **                            {@link Wizard}.
   */
  public boolean runWizard(final Context context, final AbstractTemplate template) {
    // initalize the context of the wizard
    this.wizardContext = TemplateWizardUtil.newWizardContext(context);
    populateTraversableContext(this.wizardContext, template);

    this.wizardView   = createWizard(wizardSequence(), true);
    this.wizardDialog = new WizardDialog(this.wizardView, Ide.getMainWindow());
    updateWizardTitle();

    boolean result = this.wizardDialog.runDialog();
    this.wizardDialog.dispose();
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateTraversableContext
  /**
   ** Prepares the context.
   ** <p>
   ** Subclasses should implement this method and populate the
   ** {@link TraversableContext} with the data needed by the wizard.
   ** <p>
   ** This method is called before the wizard, and any wizard pages are created.
   ** <p>
   ** The active context is stored in the {@link TraversableContext} and can be
   ** retrieved by calling
   ** TemplateWizardUtil.getContext(oracle.ide.panels.TraversableContext)
   **
   ** @param  context                    the ...
   ** @param  template                   the active ApplicationTemplate or
   **                                    ProjectTemplate, must not be
   **                                    <code>null</code>.
   */
  protected abstract void populateTraversableContext(final TraversableContext context, final AbstractTemplate template);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createWizard
  /**
   ** Creates the page flow of the wizard from the specified
   ** {@link WizardSequence2}.
   **
   ** @param  sequence           the {@link WizardSequence2} to crete the page
   **                            flow from.
   **                            <br>
   **                            Allowed object is {@link WizardSequence2}.
   ** @param  mustFinish         whether the wizard can finish at any time, or
   **                            must reach the last page before finishing.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the {@link DWizard} created for the specified
   **                            {@link WizardSequence2}.
   **                            <br>
   **                            Possible object is {@link DWizard}.
   */
  protected DWizard createWizard(final WizardSequence2 sequence, final boolean mustFinish) {
    TemplateObjectFlow wizard = new TemplateObjectFlow();
    wizard.setRoadmapVisible(true);
    wizard.setVisitedLinkEnabled(true);
    wizard.setHeaderGradientBackground(WIZARD_HEADER_GRADIENT);
    wizard.setLogoImage(wizardHeaderImage());
    wizard.setMustFinish(mustFinish);
    wizard.addWizardListener(wizardEventAdapter());
    wizard.setSequence(sequence);
    sequence.addWizardSequenceListener(wizardEventAdapter());
    return wizard;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   wizardTitle
  /**
   ** Returns the string to display as the window title.
   **
   ** @return                    the string to display as the window title.
   */
  protected abstract String wizardTitle();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   wizardHeaderImage
  /**
   ** Returns the {@link Image} to display in the header area of the dialog box.
   **
   ** @return                    the {@link Image} to display in the header area
   **                            of the dialog box.
   */
  protected abstract Image wizardHeaderImage();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   wizardSequence
  /**
   ** Returns a sequence of wizard pages.
   ** <p>
   ** Subclasses should implement this method and return their sequence of
   ** wizard pages.
   **
   ** @return                    a sequence of wizard pages.
   */
  protected abstract WizardSequence2 wizardSequence();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   postProcessing
  /**
   ** Called when the wizard is finished and all other processing has happened:
   ** after the workspace and/or projects have been created, and the wizard
   ** pages allowed to make modifications to the generated application or
   ** projects.
   */
  protected abstract void postProcessing();

  //////////////////////////////////////////////////////////////////////////////
  // Method: createProjects
  /**
   ** Creates the {@link List} of {@link Project}s for the wizard context
   ** <p>
   ** The properties for this {@link Project}s to create are provided by the
   ** internal {@link TraversableContext},
   **
   ** @return                  the {@link List} of created {@link Project} from
   **                          the internal {@link TraversableContext}.
   */
  protected List<Project> createProjects() {
    final Workspace             workspace        = workspaceContext();
    final URL                   workspaceURL     = workspace.getURL();
    final String                workspacePackage = ApplicationContent.getInstance(workspace).getAppPackagePrefix();

    final TraversableContext[]  contexts         = TemplateWizardUtil.getProjectTraversableContexts(this.wizardContext);
    final List<ProjectTemplate> templates        = projectTemplate(contexts);
    final List<Project>         result           = new ArrayList<Project>(contexts.length);

    for (int i = 0; i < contexts.length; ++i) {
      final ProjectTemplate    template = templates.get(i);
      try {
        URL url = TemplateWizardUtil.getProjectURL(contexts[i]);
        if (url == null)
          url = TemplateProjectPanel.uniqueProjectURL(template.getContainerName(), workspaceURL, contexts[i]);

        final Project project = NewEmptyProjectCommand.createProjectInWorkspace(url, workspace, projectPackage(template.getPackageName(), workspacePackage));
        final String  profile = template.getDeploymentProfile();
        if (!StringUtility.empty(profile))
          createDeploymentProfile(profile, workspace, project);

        TemplateWizardUtil.setProject(project, contexts[i]);
        result.add(project);
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   workspaceContext
  /**
   ** Retrives the Oracle JDeveloper Workspace from the wizard context.
   **
   ** @return                    the {@link Workspace} context.
   */
  protected Workspace workspaceContext() {
    return TemplateWizardUtil.getContext(this.wizardContext).getWorkspace();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createDeploymentProfile
  protected void createDeploymentProfile(final String deploymentProfileClass, final Workspace workspace, final Project project) {
    /*
    try {
      final Class              profileClass  = Class.forName(deploymentProfileClass);
      final DeployProfileDt    profileDt     = Deployment.getProfileDesignTime(profileClass);
      final String             workspaceName = workspace.getShortLabel().substring(0, workspace.getShortLabel().indexOf(".")) + "_";
      final String             projectName   = project == null ? StringUtility.EMPTY : project.getShortLabel().substring(0, project.getShortLabel().indexOf(".")) + "_";
      final String             profileName   = workspaceName + projectName + profileDt.getUniqueName(new TreeSet());
      final DataContainer      container     = project != null ? project : workspace;
      final Profile            profile       = Deployment.createDeploymentProfile(container, profileName, profileClass);
      final DeploymentProfiles profiles      = DeploymentProfiles.getInstance(container);
      profiles.addProfile(profile);
      if (project == null)
        profiles.setDefaultDeploymentProfile(profile);

      if (profileDt.addToApplicationAssemblyOnProfileAutogen()) {
        Profile defaultApplicationProfile = DeploymentProfiles.getInstance(workspace).getDefaultDeploymentProfile();
        if (defaultApplicationProfile != null)
          defaultApplicationProfile.addDependency(profile);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    */
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commitProjectChanges
  protected void commitProjectChanges(final Wizard wizard, List<Project> projectList, final Workspace workspace, final Context context) {
    final Project[]      projects = projectList.toArray(new Project[projectList.size()]);
    final ChangeBuffer[] changes  = new ChangeBuffer[projects.length];

    for (int i = 0; i < projects.length; ++i) {
      changes[i] = new ChangeBuffer();
      projects[i].getProperties().addStructureChangeListener(changes[i]);
      // all what you find in this package we did only to do the line below
      // in general, we was only interested in being notified of state changes
      // in projects
      // we didn't number the line of code that we had implemented to do this
      // simple stuff
      projects[i].attach(this);
    }

    // may be here is our hook to intercept the creation of a project and to
    // plug in the project configurator factory
    for (int i = 0; i < wizard.getPageCount(); ++i) {
      TemplateObjectPage page = (TemplateObjectPage)wizard.getPageAt(i);
      if (page.mustFinish() || page.isVisited())
        page.wizardFinished();
    }

    for (int i = 0; i < projects.length; ++i) {
      final Project      project = projects[i];
      final ChangeBuffer buffer  = changes[i];
      project.detach(this);
      project.getProperties().removeStructureChangeListener(buffer);
      if (buffer.getChanges().length > 0) {
        project.applyBatchChanges(new Runnable() {
          public void run() {
            project.ensureOpen();
            project.getProperties().applyChanges(buffer.getChanges());
          }
        });
      }

      workspace.setActiveProject(project);
      expandProject(project);

      CloseNodeCommand closeCommand = new CloseNodeCommand();
      closeCommand.setContext(context);
      closeCommand.setNeedConfirm(false);
      closeCommand.close(project);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   projectTemplate
  /**
   ** Returns the list of project templates.
   **
   ** @param  contexts           ...
   **
   ** @return                    the list of project templates.
   */
  private List<ProjectTemplate> projectTemplate(final TraversableContext[] contexts) {
    List<ProjectTemplate> templates = new ArrayList<ProjectTemplate>(contexts.length);
    for (int i = 0; i < contexts.length; ++i)
      templates.add(TemplateWizardUtil.getProjectTemplate(contexts[i]));

    if (contexts != null && contexts.length == templates.size())
      return templates;

    throw new IllegalStateException("Mismatched number of project templates from project wizard context objects.");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   projectPackage
  private String projectPackage(final String applicationPackage, String projectPackage) {
//    final String defaultPackage = JDevExtensionManifest.get(JDevExtensionManifest.TEMPLATE_AUTO_GENERATE_DEFAULT_PACKAGE);
    final String defaultPackage = "$$$$$$";
    if (ModelUtil.areDifferent(defaultPackage, projectPackage) && ModelUtil.hasLength(projectPackage)) {
      if (ModelUtil.hasLength(applicationPackage))
        projectPackage = applicationPackage + "." + projectPackage;
    }
    else
      projectPackage = applicationPackage;

    return projectPackage;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   wizardEventAdapter
  /**
   ** Creates the adapter that's listining on the events raised by the
   ** navigation model of the wizard and follows the actions of the user.
   **
   ** @return                    the adapter that's listining on the wizard
   **                            navigation events.
   **                            <br>
   **                            Possible object is {@link WizardAdapter}.
   */
  private WizardAdapter wizardEventAdapter() {
   if (this.wizardAdapter == null)
     this.wizardAdapter = new WizardAdapter();

    return this.wizardAdapter;
 }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateWizardTitle
  /**
   ** Updates the title of the wizard accordingly to the currently selected
   ** page.
   */
  private void updateWizardTitle() {
    if(this.wizardDialog != null && this.wizardView != null) {
      final String step = TemplateWizardArb.format(TemplateWizardArb.WIZARD_FROM_TEMPLATE_TITLE_STEP_OF_STEPS_FORMAT, Integer.valueOf(this.wizardView.getCurrentPageIndex() + 1), Integer.valueOf(this.wizardView.getPageCount()));
      this.wizardDialog.setTitle(String.format("%s - %s", new Object[]{ this.wizardTitle(), step }));
    }
  }
}