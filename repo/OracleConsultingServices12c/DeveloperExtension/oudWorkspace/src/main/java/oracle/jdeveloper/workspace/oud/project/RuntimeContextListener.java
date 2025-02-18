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
    Subsystem   :   Unified Directory Facility

    File        :   RuntimeContextListener.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    RuntimeContextListener.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.60.42  2013-10-23  DSteding    First release version
    11.1.1.3.37.60.44  2013-10-24  DSteding    Fixed display of menu item if
                                               the project does not belong to
                                               the technology this feature
                                               belongs to.
    11.1.1.3.37.60.47  2013-10-24  DSteding    Fix for Defect DE-000109
                                               RuntimeContextListener configures
                                               Configurator instead of Runtime.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.oud.project;

import oracle.ide.model.Project;

import oracle.ide.controller.IdeAction;
import oracle.ide.controller.ContextMenu;

import oracle.ide.model.TechnologyScope;
import oracle.ide.model.TechnologyScopeConfiguration;

import oracle.jdeveloper.workspace.iam.project.template.TemplateContextListener;

import oracle.jdeveloper.workspace.oud.Feature;

////////////////////////////////////////////////////////////////////////////////
// class RuntimeContextListener
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The callback interface implementation that allows extensions to add menu
 ** items and submenus to the context menu.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.60.42
 */
public class RuntimeContextListener extends TemplateContextListener {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>RuntimeContextListener</code> that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public RuntimeContextListener() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   menuWillShow (ContextMenuListener)
  /**
   ** This method is called just before a context menu is shown.
   ** <p>
   ** Implementations should add their items to the context menu here.
   **
   ** @param  contextMenu         the context menu being shown.
   */
  @Override
  public void menuWillShow(final ContextMenu contextMenu) {
    // add a menu to the context menu only when user clicked on Project Node
    // class.
    if (contextMenu.getContext().getNode() instanceof Project) {
      final TechnologyScope scope = TechnologyScopeConfiguration.getInstance((Project)contextMenu.getContext().getNode()).getTechnologyScope();
      // add a menu to the context menu only when the project belongs to the
      // required technology scope.
      if (scope.contains(Feature.featureTechnology())) {
        final IdeAction action = IdeAction.find(Runtime.COMMAND);
        contextMenu.add(contextMenu.createMenuItem(action));
      }
    }
  }
}