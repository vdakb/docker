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

    File        :   TemplatePreview.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the interface
                    TemplatePreview.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.project.template;

import oracle.ide.model.Project;

import oracle.ide.controller.Command;

import oracle.jdeveloper.workspace.iam.project.template.panel.Configurator;

////////////////////////////////////////////////////////////////////////////////
// abstract class TemplatePreview
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Utility used to maintain our custom ANT build file hierarchy.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public abstract class TemplatePreview extends Command {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TemplatePreview</code> that will be invoked on the
   ** specified action id.
   **
   ** @param  action             the action command to bind.
   */
  public TemplatePreview(final int action) {
    // ensure inheritance
    super(action);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   doit (Command)
  /**
   ** Executes the actions associated with a specific command.
   ** <p>
   ** When a command executes successfully, implementations should return
   ** {@link Command#OK OK}, otherwise, return
   ** {@link Command#CANCEL CANCEL} or any other non-zero value.
   ** <p>
   ** The default implementation always returns {@link Command#OK OK}.
   ** Subclasses should override this method to implement their own execution
   ** and return the appropriate value.
   **
   ** @return                    {@link Command#OK OK} id the command
   **                            execution completes successfully; otherwise
   **                            {@link Command#CANCEL CANCEL} or any
   **                            other non-zero value.
   **
   ** @throws Exception          if the execution of the command fails in
   **                            general
   */
  @Override
  public int doit()
    throws Exception {

    // build the workspace root folder the file to generate will be located
    // within (remember this does not need to be the folder of the Oracle
    // JDeveloper Workspace the given project is associated with)
    final TemplateFolder iamRoot = createPreview(getContext().getProject());

    // launches a dialog whose to preview the generation option
    if (Configurator.runDialog(iamRoot)) {
      TemplateGenerator generator = new TemplateGenerator(iamRoot);
      generator.startProgress();
      return OK;
    }
    else
      return CANCEL;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createPreview
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
  protected abstract TemplateFolder createPreview(final Project project);
}