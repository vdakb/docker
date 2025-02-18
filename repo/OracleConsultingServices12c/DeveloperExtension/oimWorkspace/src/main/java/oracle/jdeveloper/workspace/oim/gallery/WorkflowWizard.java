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

    File        :   WorkflowWizard.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    WorkflowWizard.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.oim.gallery;

import java.io.IOException;

import oracle.ide.Context;

import oracle.ide.model.Project;
import oracle.ide.model.TechnologyScopeConfiguration;

import oracle.ide.panels.TraversableContext;

import oracle.jdeveloper.model.JProjectLibraries;

import oracle.jdeveloper.template.ProjectTemplate;

import oracle.jdeveloper.template.wizard.TemplateWizardUtil;

import oracle.jdeveloper.deploy.DeploymentProfiles;

import oracle.jdeveloper.deploy.common.SelectionRules;
import oracle.jdeveloper.deploy.common.PackagingFileGroup;

import oracle.jdeveloper.workspace.oim.Feature;

import oracle.tip.tools.ide.fabric.deploy.sar.SarProfile;

import oracle.tip.tools.ide.fabric.addin.SCAProjectConfigurator;

import oracle.jdeveloper.workspace.oim.Library;
import oracle.jdeveloper.workspace.oim.Manifest;

import oracle.jdeveloper.workspace.oim.model.Workflow;

////////////////////////////////////////////////////////////////////////////////
// final class WorkflowWizard
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Implementation of the "Oracle Identity Manager Workflow Project" gallery
 ** item.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public final class WorkflowWizard extends ProjectWizard {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String RESOURCES_CONTENT_SET = "oracle.ide.model.ResourcePaths/resourcesContentSet";

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Flow
  // ~~~~~ ~~~~
  /**
   ** Extends the general "Identity Manager Project" dialog page flow to
   ** injected the additional technology scopes required by a project that
   ** belongs to SOA.
   */
  protected class Flow extends ProjectWizard.Flow {

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functinality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   postProcessing (overridden)
    /**
     ** Called when the wizard is finished and all other processing has
     ** happened after the workspace and/or projects have been created, and the
     ** wizard pages allowed to make modifications to the generated project.
     ** <p>
     ** We cannot add the technology keys that belongs to the SOA Composite in
     ** the same way as we do for the technologies created by the extension
     ** itself. The wizard sequence generator will create the property pages for
     ** each technology that has a configuration page to setup the technology.
     ** Such posibility we don't want to expose to the end user to prevent
     ** mistakes in the project setup overall hence the technologies will be
     ** injected after all other configuration steps are processed.
     */
    @Override
    protected void postProcessing() {
      // ensure inheritance
      super.postProcessing();

      // find the project by creating it or take it from the wizard context
      final TraversableContext[]         contexts   = TemplateWizardUtil.getProjectTraversableContexts(this.wizardContext());
      final Project                      project    = TemplateWizardUtil.getProject(contexts[0]);
      final TechnologyScopeConfiguration technology = TechnologyScopeConfiguration.getInstance(project);
      final String[]                     scope      = {Manifest.WORKFLOW_JAVA, Manifest.WORKFLOW_SOA, Manifest.WORKFLOW_WEB_SERVICE, Manifest.WORKFLOW_XML};
      technology.updateTechnologyScope(scope);

      // the configuration of the deployment must be the last step due to it
      // needs the SOA artifacts configured above
      final Context context = Context.newIdeContext();
      context.setProject(project);
      context.setWorkspace(TemplateWizardUtil.getContext(this.wizardContext()).getWorkspace());
      SCAProjectConfigurator.configSCAProject(context);
      // the configuration created above looks a little bit different as it is
      // in the project the application generator creates which is shipped with
      // the product hence we have to adjust something
      final Workflow           workflow = Workflow.instance(this.wizardContext());
      final DeploymentProfiles profiles = DeploymentProfiles.getInstance(project);
      final SarProfile         profile  = (SarProfile)profiles.getProfileByName(workflow.name());
      final PackagingFileGroup archive  = profile.getFileGroups().findPackagingFileGroup("sar-files");
      final SelectionRules     filter   = archive.getFilters();
      filter.addExclude(Workflow.instance(this.wizardContext()).file(), true);
      // configures the Composite content set properties of the project to
      // create.
      SCAProjectConfigurator.checkSOAContentSet(project);
      SCAProjectConfigurator.cleanContentSet(project, RESOURCES_CONTENT_SET);

      // configure the development class path
      JProjectLibraries settings = JProjectLibraries.getInstance(project);
      // remove the libraries that are attachd to the project
      settings.removeLibrary(Library.SOA_DESIGNTIME_ID);
      settings.removeLibrary(Library.SOA_RUNTIME_ID);
      settings.removeLibrary(Library.BPEL_RUNTIME_ID);
      settings.removeLibrary(Library.MEDIATOR_RUNTIME_ID);
      settings.removeLibrary(Library.MDS_RUNTIME_ID);
      // reattach all again in a proper sequence
      settings.addLibrary(Library.SOA_DESIGNTIME_ID,   false);
      settings.addLibrary(Library.SOA_RUNTIME_ID,      false);
      settings.addLibrary(Library.BPEL_RUNTIME_ID,     false);
      settings.addLibrary(Library.MEDIATOR_RUNTIME_ID, false);
      settings.addLibrary(Library.MDS_RUNTIME_ID,      false);
      settings.addLibrary(Library.OIM_PLATFORM_ID,     false);
      settings.addLibrary(Library.OIM_ADAPTER_ID,      false);
      settings.addLibrary(Library.OIM_SCHEDULER_ID,    false);
      settings.addLibrary(Library.HST_FOUNDATION_ID,   false);
      settings.addLibrary(Library.OIM_FOUNDATION_ID,   false);
      settings.addLibrary(Library.OIM_UTILITY_ID,      false);

      // now we are late in changing the project configuration hence we have to
      // save the project again
      try {
        project.save();
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>WorkflowWizard</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public WorkflowWizard() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getShortLabel (Wizard)
  /**
   ** Provides the label that represents the Wizard in the Object Gallery.
   ** <p>
   ** The label should be a noun or noun phrase naming the item that is created
   ** by this Wizard and should omit the word "new".
   ** <br>
   ** Examples: "Java Class", "Java Interface", "XML Document",
   ** "Database Connection".
   **
   ** @return                    the human readable label of the Wizard.
   */
  @Override
  public String getShortLabel() {
    return Manifest.string(Manifest.WORKFLOW);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   projectTemplate (ProjectWizard)
  /**
   ** Returns the project template id for this this
   ** <code>TemplateProjectWizard</code> expects to get operational.
   **
   ** @return                    the application template id of the owning
   **                            workspace this
   **                            <code>TemplateProjectWizard</code> expects to
   **                            get operational.
   */
  @Override
  public ProjectTemplate projectTemplate() {
    return Feature.lookupProjectTemplate(Manifest.WORKFLOW);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functinality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke (overridden)
  /**
   ** Invokes the wizard.
   **
   ** @param  context            the {@link Context} of the wizard.
   **                            All parameters required by the wizard must be
   **                            set on the context. The wizard may also return
   **                            values to the caller by setting them into the
   **                            {@link Context} for the caller to retrieve.
   **
   ** @return                    <code>true</code> if the invocation was
   **                            successful, <code>false</code> if it failed or
   **                            was canceled.
   */
  @Override
  public boolean invoke(final Context context) {
    // basic test if we are able to work
    if (!isAvailable(context))
      return true;

    final Flow dialog = new Flow();
    return dialog.runWizard(context, projectTemplate());
  }
}