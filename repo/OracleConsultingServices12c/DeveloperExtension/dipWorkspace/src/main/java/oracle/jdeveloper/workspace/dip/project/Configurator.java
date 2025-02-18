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
    Subsystem   :   Directory Integration Platform Facility

    File        :   Configurator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    Configurator.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
    12.2.1.3.42.60.94  2021-03-09  DSteding    Support for Maven Project Object
                                               Model
*/

package oracle.jdeveloper.workspace.dip.project;

import oracle.ide.Ide;

import oracle.ide.util.Namespace;

import oracle.ide.model.Project;
import oracle.ide.model.Workspace;
import oracle.ide.model.TechnologyScope;
import oracle.ide.model.TechnologyScopeConfiguration;

import oracle.ide.panels.TraversableContext;

import oracle.jdeveloper.workspace.iam.utility.StringUtility;

import oracle.jdeveloper.workspace.iam.resource.ComponentBundle;

import oracle.jdeveloper.workspace.iam.wizard.TemplateProjectConfigurator;
import oracle.jdeveloper.workspace.iam.wizard.TemplateApplicationConfigurator;

import oracle.jdeveloper.workspace.iam.project.template.TemplateFolder;
import oracle.jdeveloper.workspace.iam.project.template.TemplatePreview;

import oracle.jdeveloper.workspace.dip.Feature;
import oracle.jdeveloper.workspace.dip.Manifest;

////////////////////////////////////////////////////////////////////////////////
// class Configurator
// ~~~~~ ~~~~~~~~~~~~
/**
 ** The factory implementation that creates the project configurator based on
 ** the technology scope of a Oracle JDeveloper Project.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.94
 ** @since   11.1.1.3.37.56.13
 */
public class Configurator extends TemplatePreview {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Command ID to maintain the Oracle JDeveloper libraries */
  static final String COMMAND = "dip.jpr.configurator";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Configurator</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Configurator() {
    // ensure inheritance
    super(action());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createPreview (TemplatePreview)
  /**
   ** Creates the starting root node of the buildfile hierarchy with all
   ** the options of each particular node. This gives the end user the ability
   ** to adopt those option especially the substitution parameter.
   **
   ** @param  project            the Oracle JDeveloper node for which the
   **                            buildfile hierarchy has to be generated for.
   **
   ** @return                    the created {@link TemplateFolder} ready for
   **                            preview.
   */
  @Override
  protected final TemplateFolder createPreview(final Project project) {
    TraversableContext          context      = new TraversableContext(new Namespace(), TraversableContext.FORWARD_TRAVERSAL);
    TemplateProjectConfigurator configurator = create(project, context);
    return (configurator == null) ? null : configurator.templateRootFolder();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   action (action)
  /**
   ** Returns the id of the action this command is associated with.
   **
   ** @return                    the id of the action this command is associated
   **                            with.
   **
   ** @throws IllegalStateException if the action this command is associated
   **                               with is not registered.
   */
  public static int action() {
    final Integer id = Ide.findCmdID(COMMAND);
    if (id == null)
      throw new IllegalStateException(ComponentBundle.format(ComponentBundle.COMMAND_NOTFOUND, COMMAND));

    return id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateTechnology
  /**
   ** Notification callback if the technology of a {@link Project} is changed
   ** message.
   ** <p>
   ** Subjects call this method when they notify their observers that the
   ** subjects state has changed.
   **
   ** @param  workspace          the {@link Workspace} whose state has changed.
   ** @param  context            the {@link TraversableContext} providing the
   **                            design time settings.
   */
  public static void updateTechnology(final Workspace workspace, final TraversableContext context) {
    TemplateApplicationConfigurator configurator = create(workspace, context);
    if (configurator != null)
      configurator.configure();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateTechnology
  /**
   ** Notification callback if the technology of a {@link Project} is changed
   ** message.
   ** <p>
   ** Subjects call this method when they notify their observers that the
   ** subjects state has changed.
   **
   ** @param  project            the {@link Project} whose state has changed.
   ** @param  context            the {@link TraversableContext} providing the
   **                            design time settings.
   */
  public static void updateTechnology(final Project project, final TraversableContext context) {
    TemplateProjectConfigurator configurator = create(project, context);
    if (configurator != null)
      configurator.configure();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateFeature
  /**
   ** Notification callback if a {@link Project} for an specific feature type
   ** ist created.
   ** <p>
   ** Subjects call this method when they notify their observers that the
   ** subjects state is ready for creation.
   **
   ** @param  project            the {@link Project} whose state has changed.
   ** @param  context            the {@link TraversableContext} providing the
   **                            design time settings.
   */
  public static void updateFeature(final Project project, final TraversableContext context) {
    TemplateProjectConfigurator configurator = create(project, context);
    if (configurator != null)
      configurator.configureFeature(context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Creates and returns the appropriate
   ** {@link TemplateApplicationConfigurator} for the specified
   ** {@link Workspace}.
   ** <p>
   ** The concrete implementation of the returned
   ** {@link TemplateApplicationConfigurator} relies on the technology scope of
   ** the workspace.
   **
   ** @param  workspace          the {@link Workspace} the appropriate
   **                            {@link TemplateApplicationConfigurator} has to
   **                            be returned for.
   ** @param  context            the {@link TraversableContext} providing the
   **                            design time settings.
   **
   ** @return                    the {@link TemplateApplicationConfigurator} for the
   **                            specified {@link Workspace}.
   **                            May be <code>null</code> if the technology
   **                            scope of the specified {@link Workspace} does
   **                            not fit to any technology scope the extension
   **                            plug-in supports.
   */
  private static TemplateApplicationConfigurator create(final Workspace workspace, final TraversableContext context) {
    final String templateId = workspace.getProperty(Workspace.CREATED_BY_TEMPLATE_ID_PROPERTY);
    if (StringUtility.empty(templateId))
      return null;

    return Manifest.APPLICATION.equals(templateId) ? WorkspaceConfigurator.instance(workspace, context) : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Creates and returns the appropriate {@link TemplateProjectConfigurator}
   ** for the specified {@link Project}.
   ** <p>
   ** The concrete implementation of the returned
   ** {@link TemplateProjectConfigurator} relies on the technology scope of the
   ** project.
   **
   ** @param  project            the {@link Project} the appropriate
   **                            {@link TemplateProjectConfigurator} has to be
   **                            returned for.
   ** @param  context            the {@link TraversableContext} providing the
   **                            design time settings.
   **
   ** @return                    the {@link TemplateProjectConfigurator} for the
   **                            specified {@link Project}.
   **                            May be <code>null</code> if the technology
   **                            scope of the specified {@link Project} does not
   **                            fit to any technology scope the extension
   **                            plug-in supports.
   */
  private static TemplateProjectConfigurator create(final Project project, final TraversableContext context) {
    TechnologyScope techScope = TechnologyScopeConfiguration.getInstance(project).getTechnologyScope();
    if (techScope.isEmpty()) {
      return null;
    }
    else if (techScope.contains(Feature.pluginTechnology())) {
      return PluginConfigurator.instance(project, context);
    }
    else
      return null;
  }
}