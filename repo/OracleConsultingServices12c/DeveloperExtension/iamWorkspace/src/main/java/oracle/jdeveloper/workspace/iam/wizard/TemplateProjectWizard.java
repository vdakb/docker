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

    File        :   TemplateProjectWizard.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    TemplateProjectWizard.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.wizard;

import java.util.List;
import java.util.ArrayList;

import java.awt.Image;

import oracle.bali.ewt.wizard.dWizard.WizardSequence2;

import oracle.ide.model.Workspace;

import oracle.ide.panels.TraversableContext;

import oracle.jdeveloper.model.ApplicationContent;

import oracle.jdeveloper.template.AbstractTemplate;
import oracle.jdeveloper.template.ProjectTemplate;

import oracle.jdeveloper.template.wizard.TemplateWizardArb;
import oracle.jdeveloper.template.wizard.TemplateWizardUtil;

import oracle.jdeveloper.workspace.iam.resource.ComponentBundle;

////////////////////////////////////////////////////////////////////////////////
// abstract class TemplateProjectWizard
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** Implementation of the "Oracle Identity and Access Management Project" dialog
 ** page flow.
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
public abstract class TemplateProjectWizard extends TemplateObjectWizard {

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
    if (!(template instanceof ProjectTemplate))
      throw new IllegalArgumentException(ComponentBundle.format(ComponentBundle.TEMPALTE_MISMATCHED_TYPE, ProjectTemplate.class.getName()));

    Workspace workspace = TemplateWizardUtil.getContext(context).getWorkspace();
    TemplateWizardUtil.setApplication(workspace, context);
    TemplateWizardUtil.setApplicationPackage(ApplicationContent.getInstance(workspace).getAppPackagePrefix(), context);
    TemplateWizardUtil.setApplicationURL(workspace.getURL(), context);
    TemplateWizardUtil.setProjectTemplate((ProjectTemplate)template, context);
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
    ProjectTemplate template = TemplateWizardUtil.getProjectTemplate(this.wizardContext());
    return TemplateWizardArb.format(TemplateWizardArb.WIZARD_FROM_TEMPLATE_TITLE_CREATE_PREFIX, template.getName());
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
    return TemplateWizardArb.getImage(TemplateWizardArb.PROJECT_FROM_TEMPLATE_WIZARD_IMAGE);
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
    List<ProjectTemplate> list = new ArrayList<ProjectTemplate>(1);
    list.add(TemplateWizardUtil.getProjectTemplate(this.wizardContext()));
    return new TemplateProjectSequence(list, this.wizardContext());
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
    // intentionally left blank
  }
}