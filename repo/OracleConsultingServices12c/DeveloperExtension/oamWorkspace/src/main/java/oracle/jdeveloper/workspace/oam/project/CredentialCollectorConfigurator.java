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
    Subsystem   :   Access Manager Facility

    File        :   CredentialCollectorConfigurator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    CredentialCollectorConfigurator.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.69  2017-31-01  DSteding    Changed compatibility annotation
                                               accordingliy to the build.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.oam.project;

import oracle.ide.Ide;

import oracle.ide.net.URLFactory;

import oracle.ide.model.Project;

import oracle.ide.panels.TraversableContext;

import oracle.jdeveloper.model.JProjectLibraries;

import oracle.jdeveloper.workspace.iam.utility.ClassUtility;

import oracle.jdeveloper.workspace.iam.parser.XMLFileHandlerException;

import oracle.jdeveloper.workspace.iam.wizard.TemplateProjectConfigurator;

import oracle.jdeveloper.workspace.iam.project.template.TemplateBundle;
import oracle.jdeveloper.workspace.iam.project.template.TemplateStream;
import oracle.jdeveloper.workspace.iam.project.template.TemplateFolder;

import oracle.jdeveloper.workspace.oam.Library;
import oracle.jdeveloper.workspace.oam.Manifest;

import oracle.jdeveloper.workspace.oam.model.Collector;

import oracle.jdeveloper.workspace.oam.parser.CollectorHandler;

////////////////////////////////////////////////////////////////////////////////
// class CredentialCollectorConfigurator
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The configurator to setup Oracle Access Manager Credential Collector
 ** projects.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
class CredentialCollectorConfigurator extends ProjectConfigurator {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>CredentialCollectorConfigurator</code> for the
   ** specified {@link Project}.
   **
   ** @param  project            the {@link Project} this
   **                            {@link TemplateProjectConfigurator} belongs to.
   ** @param  context            the {@link TraversableContext} providing the
   **                            design time settings.
   */
  private CredentialCollectorConfigurator(final Project project, final TraversableContext context) {
    // ensure inhertitance
    super(project, context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   templateBuildPreview (ProjectConfigurator)
  /**
   ** Creates the ANT project build file for Oracle Access Manager Authenticator
   ** project.
   **
   ** @param  folder             the {@link TemplateFolder} where the file will
   **                            be created within.
   ** @param  target             the ANT build file template to include in the
   **                            {@link TemplateStream} to create.
   */
  @Override
  protected final void templateBuildPreview(final TemplateFolder folder, final TemplateStream target) {
    final TemplateStream collector = folder.add(buildPreview(folder));
    // we assume the template specifies the pattern #{oam.target} to
    // import the build target file
    collector.include(Manifest.OAM_TARGET, target);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   templateProjectDeployment (ProjectConfigurator)
  /**
   ** Creates the ANT project deployment file for Oracle Access Manager projects.
   **
   ** @param  project            the {@link TemplateFolder} where the file will
   **                            be created within.
   ** @param  context            the ANT build file template to include in the
   **                            {@link TemplateStream} to create.
   */
  @Override
  protected void templateProjectDeployment(final TemplateFolder project, final TemplateStream context) {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildfile (TemplateProjectConfigurator)
  /**
   ** Returns the name of the build file for a development project.
   **
   ** @return                    the name of the build file for a development
   **                            project.
   */
  @Override
  protected final String buildfile() {
    final Collector provider = Collector.instance(this.context);
    return provider.file();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mavenfile (TemplateProjectConfigurator)
  /**
   ** Returns the name of the build file for a development project.
   **
   ** @return                    the name of the build file for a development
   **                            project.
   */
  @Override
  protected final String mavenfile() {
    final Collector provider = Collector.instance(this.context);
    return provider.file().replace("ant", "pom");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns the active project node configurator wrapped in a
   ** {@link TemplateProjectConfigurator} representation.
   ** representation.
   **
   ** @param  context            the {@link TraversableContext} providing the
   **                            design time settings.
   **
   ** @return                    the active project node as a
   **                            {@link TemplateProjectConfigurator}
   **                            representation.
   */
  protected static TemplateProjectConfigurator instance(final TraversableContext context) {
    return instance(Ide.getActiveProject(), context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns the specified project node configurator wrapped in a
   ** {@link TemplateProjectConfigurator}.
   **
   ** @param  project            the {@link Project} to configure.
   ** @param  context            the {@link TraversableContext} providing the
   **                            design time settings.
   **
   ** @return                    {@link TemplateProjectConfigurator} specific to
   **                            configure Oracle Access Manager Credential
   **                            Collector {@link Project}s.
   */
  protected static TemplateProjectConfigurator instance(final Project project, final TraversableContext context) {
    return new CredentialCollectorConfigurator(project, context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure (overridden)
  /**
   ** Configures a Oracle JDeveloper {@link Project} completely.
   */
  @Override
  public final void configure() {
    // ensure inhertitance
    super.configure();

    // configure additional directories special for this feature
    configureContentDirectory();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configureLibraries (overridden)
  /**
   ** Configures the libraries the project needs.
   */
  @Override
  protected void configureLibraries() {
    JProjectLibraries settings = JProjectLibraries.getInstance(this.project());
    settings.addLibrary(Library.JSP_RUNTIME_ID,  false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildPreview
  /**
   ** Creates the ANT project build file for Oracle Access Manager Credential
   ** Collector projects.
   **
   ** @param  folder             the {@link TemplateFolder} where the file will
   **                            be created within.
   **
   ** @return                    the created {@link TemplateStream} ready for
   **                            preview.
   */
  private TemplateStream buildPreview(final TemplateFolder folder) {
    final Collector      context = Collector.instance(this.context);
    final TemplateStream item    = new TemplateStream(folder, context.file());
    item.template(ClassUtility.classNameToFile(context.template(), ClassUtility.XML));
    item.add(CollectorHandler.ANT_PROJECT, TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON), context.project());
    item.add(CollectorHandler.ANT_DEFAULT, TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON), context.target());
    item.add(CollectorHandler.ANT_BASEDIR, TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON), item.relativeFile(context.basedir()));
    item.add(CollectorHandler.DESCRIPTION, TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), context.description());
    item.add(CollectorHandler.DESTINATION, TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), item.relativeFile(context.destination()));
    item.add(CollectorHandler.APPLICATION, TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), context.application());
    item.add(CollectorHandler.PACKAGEPATH, TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), context.packagePath());
    // check if the item already exists to override the default values created
    // above with the values found in the file
    item.hotspotSelected(!item.exists());
    if (item.exists()) {
      CollectorHandler handler = new CollectorHandler(URLFactory.newFileURL(item.file()));
      try {
        handler.load();
        item.property(CollectorHandler.ANT_PROJECT, handler.name());
        item.property(CollectorHandler.ANT_DEFAULT, handler.target());
        item.property(CollectorHandler.ANT_BASEDIR, handler.basedir());
        item.property(CollectorHandler.DESCRIPTION, handler.propertyValue(CollectorHandler.DESCRIPTION));
        item.property(CollectorHandler.DESTINATION, item.relativeFile(handler.propertyValue(CollectorHandler.DESTINATION)));
        item.property(CollectorHandler.APPLICATION, handler.propertyValue(CollectorHandler.APPLICATION));
        item.property(CollectorHandler.PACKAGEPATH, handler.propertyValue(CollectorHandler.PACKAGEPATH));
      }
      catch (XMLFileHandlerException e) {
        e.printStackTrace();
      }
    }
    return item;
  }
}