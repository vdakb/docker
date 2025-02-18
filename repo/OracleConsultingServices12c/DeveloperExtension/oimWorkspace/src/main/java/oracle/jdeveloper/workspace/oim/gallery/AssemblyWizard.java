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

    File        :   AssemblyWizard.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    AssemblyWizard.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.60.39  2013-07-29  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.oim.gallery;

import java.io.File;

import oracle.ide.Context;

import oracle.ide.net.URLPath;

import oracle.ide.model.Project;

import oracle.ide.panels.TraversableContext;

import oracle.jdeveloper.model.J2eeSettings;
import oracle.jdeveloper.model.PathsConfiguration;

import oracle.jdeveloper.template.ProjectTemplate;

import oracle.jdeveloper.template.wizard.TemplateWizardUtil;

import oracle.jdeveloper.workspace.iam.utility.ClassUtility;

import oracle.jdeveloper.workspace.iam.project.template.TemplateBundle;
import oracle.jdeveloper.workspace.iam.project.template.TemplateFolder;
import oracle.jdeveloper.workspace.iam.project.template.TemplateStream;
import oracle.jdeveloper.workspace.iam.project.template.TemplateGenerator;

import oracle.jdeveloper.workspace.oim.Feature;
import oracle.jdeveloper.workspace.oim.Manifest;

import oracle.jdeveloper.workspace.oim.model.CustomizationAssembly;
import oracle.jdeveloper.workspace.oim.model.CustomizationManifest;

import oracle.jdeveloper.workspace.oim.parser.CustomizationManifestHandler;

////////////////////////////////////////////////////////////////////////////////
// final class AssemblyWizard
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~
/**
 ** Implementation of the "Oracle Identity Manager Assembly Project" gallery
 ** item.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.60.39
 */
public class AssemblyWizard extends ProjectWizard {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Flow
  // ~~~~~ ~~~~
  /**
   ** Extends the general "Identity Manager Project" dialog page flow to
   ** injected the additional technology scopes required by a project that
   ** belongs to ADF.
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
      final TraversableContext[] contexts = TemplateWizardUtil.getProjectTraversableContexts(this.wizardContext());
      final Project              project  = TemplateWizardUtil.getProject(contexts[0]);

      // the generation of the source files must be the last step
      final Context context = Context.newIdeContext();
      context.setProject(project);
      context.setWorkspace(TemplateWizardUtil.getContext(this.wizardContext()).getWorkspace());

      final URLPath               source   = PathsConfiguration.getInstance(project).getProjectSourcePath();
      final TemplateFolder        metainf  = new TemplateFolder(new File(source.getFirstEntry().getFile(), "META-INF"));
      final CustomizationAssembly assembly = CustomizationAssembly.instance(this.wizardContext());
      final CustomizationManifest manifest = CustomizationManifest.instance(this.wizardContext());
      TemplateStream              item     = metainf.add(new TemplateStream(metainf, manifest.file()));
      // configure the template to use
      item.template(ClassUtility.classNameToFile(manifest.template(), "mf"));
      // configure the substitution parameter
      item.add(CustomizationManifestHandler.SPECIFICATION_TITLE,    TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_JAVA_ICON), assembly.description());
      item.add(CustomizationManifestHandler.SPECIFICATION_VENDOR,   TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_JAVA_ICON), manifest.specificationVendor());
      item.add(CustomizationManifestHandler.SPECIFICATION_VERSION,  TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_JAVA_ICON), manifest.specificationVersion());
      item.add(CustomizationManifestHandler.IMPLEMENTATION_TITLE,   TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_JAVA_ICON), manifest.description());
      item.add(CustomizationManifestHandler.IMPLEMENTATION_VENDOR,  TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_JAVA_ICON), manifest.implementationVendor());
      item.add(CustomizationManifestHandler.IMPLEMENTATION_VERSION, TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_JAVA_ICON), manifest.implementationVersion());
      // check if the item already exists to avoid overriding the values created
      item.hotspotSelected(!item.exists());

      // generate the manifest files
      TemplateGenerator builder = new TemplateGenerator(metainf);
      builder.startProgress();

      // generate the web application configuration files
      final J2eeSettings   settings = J2eeSettings.getInstance(project);
      final TemplateFolder webinf   = new TemplateFolder(new File(settings.getHtmlRootDirectory().getFile(), "WEB-INF"));
      item   = webinf.add(new TemplateStream(webinf, "web.xml"));
      item.template(ClassUtility.classNameToFile(Manifest.BUILDFILE_CONFIG + ".web",             ClassUtility.XML));
      item.hotspotSelected(true);
      item   = webinf.add(new TemplateStream(webinf, "weblogic.xml"));
      item.hotspotSelected(true);
      item.template(ClassUtility.classNameToFile(Manifest.BUILDFILE_CONFIG + ".weblogic",        ClassUtility.XML));
      item.hotspotSelected(true);
      item   = webinf.add(new TemplateStream(webinf, "faces-config.xml"));
      item.template(ClassUtility.classNameToFile(Manifest.BUILDFILE_CONFIG + ".faces-config",    ClassUtility.XML));
      item.hotspotSelected(true);
      item   = webinf.add(new TemplateStream(webinf, "trinidad-config.xml"));
      item.template(ClassUtility.classNameToFile(Manifest.BUILDFILE_CONFIG + ".trinidad-config", ClassUtility.XML));
      item.hotspotSelected(true);

      // generate the web application configuration files
      builder = new TemplateGenerator(webinf);
      builder.startProgress();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>AssemblyWizard</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AssemblyWizard() {
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
    return Manifest.string(Manifest.OIM_ASSEMBLY);
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
    return Feature.lookupProjectTemplate(Manifest.OIM_ASSEMBLY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
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