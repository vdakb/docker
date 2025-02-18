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

    File        :   TemplateApplicationWizard.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    TemplateApplicationWizard.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.wizard;

import java.util.List;
import java.util.ArrayList;

import java.net.URL;

import java.awt.Image;

import oracle.bali.ewt.wizard.dWizard.ArraySequence;
import oracle.bali.ewt.wizard.dWizard.SequenceSeries2;
import oracle.bali.ewt.wizard.dWizard.WizardSequence2;

import oracle.javatools.dialogs.MessageDialog;

import oracle.ide.Ide;
import oracle.ide.Context;

import oracle.ide.net.URLFactory;
import oracle.ide.net.URLFileSystem;

import oracle.ide.config.DTCache;

import oracle.ide.controller.IdeAction;

import oracle.ide.cmd.NewWorkspaceCommand;

import oracle.ide.model.Element;
import oracle.ide.model.Project;
import oracle.ide.model.Workspace;

import oracle.ide.panels.TraversableContext;

import oracle.ideimpl.appoverview.AppOverviewAddin;

import oracle.jdeveloper.model.ApplicationContent;

import oracle.jdeveloper.template.ProjectTemplate;
import oracle.jdeveloper.template.AbstractTemplate;
import oracle.jdeveloper.template.ApplicationTemplate;

import oracle.jdeveloper.template.wizard.TemplateWizardArb;
import oracle.jdeveloper.template.wizard.TemplateWizardUtil;
import oracle.jdeveloper.template.wizard.NewApplicationFromTemplateWizard;

import oracle.ide.config.Preferences;

import oracle.jdeveloper.workspace.iam.utility.StringUtility;

import oracle.jdeveloper.workspace.iam.preference.Provider;

import oracle.jdeveloper.workspace.iam.resource.ComponentBundle;

////////////////////////////////////////////////////////////////////////////////
// abstract class ApplicationWizard
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** Implementation of the "Oracle Identity and Access Management Application"
 ** dialog page flow.
 ** <p>
 ** This class has to be exists due to the implementation of class
 ** oracle.jdeveloper.template.wizard.NewApplicationPanel is package proteced.
 ** Hence we have to create our own class ApplicationPanel that do the same Job.
 ** <p>
 ** No chance to reuse the existing class!!!
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public abstract class TemplateApplicationWizard extends TemplateObjectWizard {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String LAST_ROOT_PACKAGE_KEY         = NewApplicationFromTemplateWizard.class.getName() + ".LAST_ROOT_PACKAGE";
  private static final String LAST_ACTIVE_TEMPLATE_NAME_KEY = NewApplicationFromTemplateWizard.class.getName() + ".LAST_ACTIVE_TEMPLATE_NAME";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final TemplateApplicationPanel panel;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>TemplateApplicationWizard</code> that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TemplateApplicationWizard() {
    // ensure inheritance
    super();

    // create the panel to gather the information to create an Oracle JDeveloper
    // Application.
    this.panel = new TemplateApplicationPanel();
    final String packageRoot = lastPackageRoot();
    if (!StringUtility.empty(packageRoot))
      this.panel.packageRoot(packageRoot);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   workingURL
  /**
   ** Returns the {@link URL} to the full path name of the current work
   ** directory.
   ** <br>
   ** <b>Important</b>:
   ** <br>
   ** The work directory is not a fixed location. It starts off in a standard
   ** place (usually "mywork" under the user directory). However, as new
   ** applications are created, the work directory will change. You should not
   ** rely on this method returning the same location between calls.
   **
   ** @return                   the {@link URL} to the full path name of the
   **                           current work directory.
   */
  public URL workingURL() {
    URL workingURL = Provider.instance(Preferences.getPreferences()).workspace();
    if (workingURL == null) {
      workingURL = Ide.getWorkspaces().getWorkDirectory();
      if (workingURL == null)
        workingURL = URLFactory.newDirURL(Ide.getWorkDirectory());
      else
        workingURL = URLFileSystem.getParent(workingURL);
    }
    return workingURL;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   projectTemplates
  /**
   ** Returns the {@link List} of {@link ProjectTemplate} that are assigned to
   ** the specfified {@link ApplicationTemplate}
   **
   ** @param  template           the {@link ApplicationTemplate} the
   **                            {@link List} of {@link ProjectTemplate} has to
   **                            be returned for.
   **
   ** @return                   the {@link List} of {@link ProjectTemplate}
   **                           taken from the specified
   **                           {@link ApplicationTemplate}.
   **                           May be empty but never <code>null</code>.
   */
  protected final List<ProjectTemplate> projectTemplates(final ApplicationTemplate template) {
    return template != null ? template.getProjectTemplates() : new ArrayList<ProjectTemplate>();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lastUsedPackage
  /**
   ** Persists the specified package name for reuse if the wizard is exceuted
   ** again the next time.
   **
   ** @param  packageName        the name of the desfault package to persist in
   **                            the runtime cache.
   */
  protected static void lastUsedPackage(String packageName) {
    final DTCache dtCache = Ide.getDTCache();
    dtCache.putString(LAST_ROOT_PACKAGE_KEY, packageName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lastPackageRoot
  /**
   ** Restores the last used package name from the runtime cache to reuse it
   ** in the wizard.
   **
   ** @return                    the name of the default package name restored
   **                            from the runtime cache.
   */
  protected static String lastPackageRoot() {
    final DTCache dtCache = Ide.getDTCache();
    return dtCache.getString(LAST_ROOT_PACKAGE_KEY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lastTemplateName
  /**
   ** Persists the specified template name for reuse if the wizard is exceuted
   ** again the next time.
   **
   ** @param  templateName       the name of the {@link ApplicationTemplate} to
   **                            persist in the runtime cache.
   */
  protected static void lastTemplateName(final String templateName) {
    final DTCache dtCache = Ide.getDTCache();
    dtCache.putString(LAST_ACTIVE_TEMPLATE_NAME_KEY, templateName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lastUsedTemplate
  /**
   ** Restores the last used template name from the runtime cache to reuse it
   ** in the wizard is exceuted.
   **
   ** @return                    the name of the {@link ApplicationTemplate}
   **                            restored from the runtime cache.
   */
  protected static String lastUsedTemplate() {
    final DTCache dtCache = Ide.getDTCache();
    return dtCache.getString(LAST_ACTIVE_TEMPLATE_NAME_KEY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateTraversableContext (TemplateObjectWizard)
  /**
   ** Prepares the context.
   ** <p>
   ** Subclasses should implement this method and populate the
   ** {@link TraversableContext} with the data necessary by the wizard.
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
  @Override
  protected void populateTraversableContext(final TraversableContext context, final AbstractTemplate template) {
    // prevent bogus input
    if (template == null)
      throw new IllegalArgumentException(ComponentBundle.string(ComponentBundle.TEMPALTE_MANDATORY));

    // prevent bogus input
    if (!(template instanceof ApplicationTemplate))
      throw new IllegalArgumentException(ComponentBundle.format(ComponentBundle.TEMPALTE_MISMATCHED_TYPE, ApplicationTemplate.class.getName()));

    TemplateWizardUtil.setApplicationURL(applicationURL(), context);
    TemplateWizardUtil.setApplicationTemplate((ApplicationTemplate)template, context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   wizardTitle (TemplateObjectWizard)
  /**
   ** Returns the string to display as the window title.
   **
   ** @return                    the string to display as the window title.
   */
  @Override
  protected String wizardTitle() {
    ApplicationTemplate template = TemplateWizardUtil.getApplicationTemplate(this.wizardContext());
    return template == null ? TemplateWizardArb.getString(TemplateWizardArb.NEW_APPLICATION_TITLE) : TemplateWizardArb.format(TemplateWizardArb.WIZARD_FROM_TEMPLATE_TITLE_CREATE_PREFIX, template.getName());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   wizardHeaderImage (TemplateObjectWizard)
  /**
   ** Returns the {@link Image} to display in the header area of the dialog box.
   **
   ** @return                    the {@link Image} to display in the header area
   **                            of the dialog box.
   */
  @Override
  protected Image wizardHeaderImage() {
    return TemplateWizardArb.getImage(TemplateWizardArb.WIZARD_FROM_TEMPLATE_APP_IMAGE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   wizardSequence (TemplateObjectWizard)
  /**
   ** Returns a sequence of wizard pages.
   ** <p>
   ** Subclasses should implement this method and return their sequence of
   ** wizard pages.
   **
   ** @return                    a sequence of wizard pages.
   */
  @Override
  protected WizardSequence2 wizardSequence() {
    // the check for existence of a template can be safely skipped due to the
    // context initialization did this already
    final ApplicationTemplate    template   = TemplateWizardUtil.getApplicationTemplate(this.wizardContext());
    // ask the implementing class if property configuration is required for the
    // application to create
    // this hack is necessary due to the unavailability of the application
    // template to be extended with additional steps based on the technology
    // scope
    final TemplateObjectSequence technologySequence = createTechnologySequence(this.wizardContext());
    // create the page flow for the project template
    final TemplateObjectSequence projectSequence    = new TemplateProjectSequence(this.projectTemplates(template), this.wizardContext());
    final TemplateObjectPage     page               = new TemplateObjectPage(this.panel, TemplateWizardArb.getString(TemplateWizardArb.WIZARD_FROM_TEMPLATE_APP_NAME_PAGE_LABEL), TemplateWizardArb.getString(TemplateWizardArb.WIZARD_FROM_TEMPLATE_APP_NAME_PAGE_TITLE), this.wizardContext());

    this.panel.applicationName(template.getContainerName());
    page.setInitialFocus(this.panel.defaultFocusComponent());

    // compose all pages as the wizard sequence
    TemplateObjectPage[] pages = new TemplateObjectPage[] { page };
    return new SequenceSeries2(new WizardSequence2[] { new ArraySequence(pages), technologySequence, projectSequence });
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   postProcessing (TemplateObjectWizard)
  /**
   ** Called when the wizard is finished and all other processing has happened:
   ** after the workspace and/or projects have been created, and the wizard
   ** pages allowed to make modifications to the generated application or
   ** projects.
   */
  @Override
  protected void postProcessing() {
    // persists the package name specified in the dialog for reuse if the wizard
    // is exceuted the next time
    final String usedPackage = TemplateWizardUtil.getApplicationPackage(this.wizardContext());
    TemplateApplicationWizard.lastUsedPackage(usedPackage);

    // persists the template name specified in the dialog for reuse if the wizard
    // is exceuted the next time
    final ApplicationTemplate template = TemplateWizardUtil.getApplicationTemplate(this.wizardContext());
    TemplateApplicationWizard.lastTemplateName(template.getName());

    // as the final step display the overview of the created artifacts of the
    // page flow
    showApplicationOverview();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   workspaceContext
  /**
   ** Creates an empty Oracle JDeveloper Workspace with the specified.
   ** <p>
   ** The properties for this workspace to create are provided by the specified
   ** {@link Context},
   **
   ** @return                    the created {@link Workspace} context.
   */
  @Override
  protected Workspace workspaceContext() {
    Workspace workspace = TemplateWizardUtil.getApplication(this.wizardContext());
    // Really strange discussion
    // We tried to wrap the functionality below in a specific method that we
    // want also to have abstract in our own super class to decrease the amount
    // of code that we have to write.
    // the funny thing is if we carve out the code from this method we will get
    // always a NullPointerException thrown from
    // oracle.jdeveloper.template.wizard.TemplateObjectWizard$WizardEventAdapter.wizardFinished
    // only if the code is implemented as below we will not see this exeption
    if (workspace == null) {
      final Context context      = TemplateWizardUtil.getContext(this.wizardContext());
      final URL     workspaceURL = TemplateWizardUtil.getApplicationURL(this.wizardContext());

      // create an empty application by delegating the activities to the
      // appropriate command implementation
      try {
        workspace = NewWorkspaceCommand.createEmptyWorkspace(context, workspaceURL);
      }
      catch (Exception e) {
        showError();
      }

      // persists the application currently created in the context of the
      // running wizard
      TemplateWizardUtil.setApplication(workspace, this.wizardContext());
      // persists the package prefix specified in the dialog in the created
      // workspace
      ApplicationContent.getInstance(workspace).setAppPackagePrefix(TemplateWizardUtil.getApplicationPackage(this.wizardContext()));

      // assign the application template to the created workspace
      // it must be done here due to all other activities related to the
      // products needs this information to know what they have to do. It's also
      // used to as a discriminator to decide if the projects or something like
      // this are related to an IAM workspace in general.
      final ApplicationTemplate template = TemplateWizardUtil.getApplicationTemplate(this.wizardContext());
      final String templateId = template.getTemplateId();
      if (templateId != null)
        workspace.setProperty(Workspace.CREATED_BY_TEMPLATE_ID_PROPERTY, templateId);

      // check out the deployment profile the application template specifies and
      // assign it to the workspace created above.
      final String deploymentProfile = template.getDeploymentProfile();
      if (!StringUtility.empty(deploymentProfile))
        this.createDeploymentProfile(deploymentProfile, workspace, null);

      Ide.setActiveWorkspace(workspace);
    }

    return workspace;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   applicationURL
  /**
   ** Retrieves the {@link URL} of the application folder.
   **
   ** @return                    the {@link URL} of the application folder.
   */
  protected abstract URL applicationURL();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createTechnologySequence
  /**
   ** Create the application technology configuration page flow.
   ** <p>
   ** This hack is necessary due to the unavailability of the application
   ** template to be extended with additional steps based on the technology
   ** scope.
   ** <p>
   ** This provides the opportunity to inject customizations in the page flow
   ** that have to proceed before the project configuration happens.
   **
   ** @param  context            the {@link TraversableContext} the page flow
   **                            will be based on.
   **
   ** @return                    the {@link TemplateObjectSequence} comprising
   **                            the wizard pages to configure the application.
   */
  protected abstract TemplateObjectSequence createTechnologySequence(final TraversableContext context);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   projectTemplates
  /**
   ** Returns the {@link List} of project templates that will be used to create
   ** the Oracle JDeveopler {@link Project}s.
   **
   ** @param  context            the array of {@link TraversableContext}s the
   **                            wizard engine has gathered.
   **
   ** @return                    the {@link List} of {@link ProjectTemplate}s
   */
  protected List<ProjectTemplate> projectTemplates(final TraversableContext[] context) {
    List<ProjectTemplate> templates = new ArrayList<ProjectTemplate>(context.length);
    for (int i = 0; i < context.length; ++i)
      templates.add(TemplateWizardUtil.getProjectTemplate(context[i]));

    if (context != null && context.length == templates.size())
      return templates;
    else
      throw new IllegalStateException(ComponentBundle.string(ComponentBundle.TEMPALTE_MISMATCHED_PROJECT));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   showApplicationOverview
  /**
   ** Display the information to the user about all what the wizard has done.
   */
  protected void showApplicationOverview() {
    final Workspace workspace = this.workspaceContext();
    if (workspace.projects().size() == 0)
      return;

    final IdeAction overview  = IdeAction.get(AppOverviewAddin.SHOW_APPOVERVIEW_CMD_ID);
    if (overview != null) {
      try {
        Context context = new Context();
        context.setWorkspace(workspace);
        context.setProject(workspace.getActiveProject());
        context.setSelection(new Element[] { workspace });
        overview.performAction(context);
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   showError
  /**
   ** Display an error alert about that the workspace could not be created.
   */
  protected void showError() {
    MessageDialog.error(
      Ide.getMainWindow()
    , TemplateWizardArb.getString(TemplateWizardArb.ERROR_FAILED_TO_CREATE_APPLICATION_MSG)
    , TemplateWizardArb.getString(TemplateWizardArb.ERROR_FAILED_TO_CREATE_APPLICATION_TITLE)
    , null
    );
  }
}