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
    Subsystem   :   Virtual Directory Facility

    File        :   Feature.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    Feature.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.44  2013-10-24  DSteding    Added support to discover base
                                               technology this feature
                                               implements.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.ovd;

import java.util.List;
import java.util.ArrayList;

import oracle.ide.model.TechId;
import oracle.ide.model.TechnologyScope;
import oracle.ide.model.TechnologyRegistry;

import oracle.jdeveloper.template.TemplateManager;
import oracle.jdeveloper.template.ProjectTemplate;
import oracle.jdeveloper.template.ApplicationTemplate;

import oracle.jdeveloper.workspace.iam.AbstractAddin;

import oracle.jdeveloper.workspace.ovd.gallery.AdapterWizard;
import oracle.jdeveloper.workspace.ovd.gallery.ApplicationWizard;

////////////////////////////////////////////////////////////////////////////////
// class Feature
// ~~~~~ ~~~~~~~
/**
 ** Addin for application and project functionality within Oracle JDeveloper to
 ** handle Virtual Direcory artifacts.
 **
 ** The {@link AbstractAddin} provides the mechanism for this JDeveloper
 ** extensions to carry out programmatic initialization during the startup
 ** sequence of the JDeveloper IDE.
 ** <br>
 ** The initialize() method of this {@link AbstractAddin} is called prior to
 ** the display of the main window.
 ** <br>
 ** The registration is done in the <code>addins</code> section of the extension
 ** manifest. For more information on the extension manifest, see the
 ** documentation in jdev\doc\extension.
 ** <br>
 ** Care should be taken when implementing the initialize() method to minimize
 ** the work carried out and the classes loaded. If it is possible to defer some
 ** initialization until a later point, it will be deferred.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class Feature extends AbstractAddin {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Feature</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Feature() {
    // ensure inheritance
    super(Manifest.string(Manifest.FEATURE));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize (Addin)
  /**
   ** Invoked by the super class after the instance of the {@link AbstractAddin}
   ** is instantiated by the <code>AddinManager</code>.
   ** <br>
   ** The method is called prior to the display of the main window.
   **
   ** @see  AbstractAddin#initialize
   */
  @Override
  public void initialize() {
    createTemplate();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   featureTechnology
  /**
   ** Returns the {@link TechId} registered in Oracle JDeveloper as the
   ** technology related to Oracle Virtual Directory.
   **
   ** @return                    the {@link TechId} registered in Oracle
   **                            JDeveloper as the technology related to Oracle
   **                            Virtual Directory.
   */
  public static TechId featureTechnology() {
    return TechnologyRegistry.getInstance().getTechId(Manifest.TECHNOLOGY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   adapterTechnology
  /**
   ** Returns the {@link TechId} registered in Oracle JDeveloper as the
   ** technology related to Oracle Virtual Directory Adapters.
   **
   ** @return                    the {@link TechId} registered in Oracle
   **                            JDeveloper as the technology related to Oracle
   **                            Virtual Directory Adapters.
   */
  public static TechId adapterTechnology() {
    TechnologyRegistry registry = TechnologyRegistry.getInstance();
    return registry.getTechId(Manifest.ADAPTER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createTemplate
  /**
   ** Creates the project and application templates specific for Oracle Virtual
   ** Directory and registers the gallery items for each of them.
   */
  private void createTemplate() {
    // create the adapter project template and enlist it in the collection
    createAdapterTemplate();
    // create the collection that will contain the project templates associated
    // with the application template
    final List<ProjectTemplate> template = new ArrayList<ProjectTemplate>();
    // register the application template
    createApplicationTemplate(template);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAdapterTemplate
  /**
   ** Creates the project template specific for Oracle Virtual Directory
   ** Adapter development and registers the gallery item for ir.
   **
   ** @return                    the created project template specific for
   **                            Oracle Virtual Directory Adapter development.
   */
  private ProjectTemplate createAdapterTemplate() {
    // create a proper technology scope for the project template to create
    final TechnologyRegistry registry = TechnologyRegistry.getInstance();
    final TechnologyScope    scope    = new TechnologyScope();
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.iam.Manifest.TECHNOLOGY));
    scope.add(registry.getTechId(Manifest.TECHNOLOGY));
    scope.add(registry.getTechId(Manifest.ADAPTER));

    // create the project template
    final ProjectTemplate  template = new ProjectTemplate();
    template.setTemplateId(Manifest.ADAPTER);
    template.setName(Manifest.string(Manifest.ADAPTER));
    template.setIconFile(Manifest.string(Manifest.ADAPTER_ICON));
    template.setDescription(Manifest.string(Manifest.ADAPTER_DESCRIPTION));
    template.setTechnologyScope(scope);
    TemplateManager.getInstance().registerTemplate(template);

    // create a generic gallery elements
    // the elment will be displayed in the folders represented by setPath
    // the folder itself is defined in the extension manifest as a gallery item
    // and the label that is resolved from the extension manifest resource
    // bundle
    // the specific class AdapterWizard will guide the user through the creation
    // of the artifacts.
    registerTemplate(template, new String[] {oracle.jdeveloper.workspace.iam.Manifest.FEATURE, Manifest.FEATURE}, Manifest.GALLERY_RULE, AdapterWizard.class);
    return template;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createApplicationTemplate
  /**
   ** Creates the application template specific for Oracle Virtual Directory
   ** development and registers the gallery item for it.
   **
   ** @param  project            the {@link ProjectTemplate}s associated with
   **                            the aplication template.
   */
  private void createApplicationTemplate(final List<ProjectTemplate> project) {
    // create a proper technology scope for the application template to create
    final TechnologyRegistry registry = TechnologyRegistry.getInstance();
    final TechnologyScope    scope    = new TechnologyScope();
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.iam.Manifest.TECHNOLOGY));
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.ovd.Manifest.TECHNOLOGY));

    // create the application template
    final ApplicationTemplate template = new ApplicationTemplate();
    template.setTemplateId(Manifest.APPLICATION);
    template.setName(Manifest.string(Manifest.APPLICATION));
    template.setIconFile(Manifest.string(Manifest.APPLICATION_ICON));
    template.setDescription(Manifest.string(Manifest.APPLICATION_DESCRIPTION));
    template.setProjectTemplates(project);

    // create a generic gallery elements
    // the elment will be displayed in the folders represented by setPath
    // the folder itself is defined in the extension manifest as a gallery item
    // and the label that is resolved from the extension manifest resource
    // bundle
    // the specific class ApplicationWizard will guide the user through the
    // creation of the artifacts.
    registerTemplate(template, Manifest.FEATURE_RULE, ApplicationWizard.class);
  }
}