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

    File        :   ProjectWizard.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    ProjectWizard.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.oim.gallery;

import java.beans.PropertyChangeEvent;

import oracle.ide.Context;

import oracle.ide.model.Project;
import oracle.ide.model.UpdateMessage;
import oracle.ide.model.TechnologyScopeConfiguration;

import oracle.ide.panels.TraversableContext;

import oracle.jdeveloper.template.ProjectTemplate;

import oracle.jdeveloper.template.wizard.TemplateWizardUtil;

import oracle.jdeveloper.workspace.iam.AbstractWizard;

import oracle.jdeveloper.workspace.iam.wizard.TemplateProjectWizard;

import oracle.jdeveloper.workspace.oim.project.Configurator;

////////////////////////////////////////////////////////////////////////////////
// abstract class ProjectWizard
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~
/**
 ** Abstract implementation of any "Oracle Identity Manager Project" gallery
 ** item.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public abstract class ProjectWizard extends AbstractWizard {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Flow
  // ~~~~~ ~~~~
  /**
   ** Implementation of the general "Identity Manager Project" dialog page flow.
   */
  protected class Flow extends TemplateProjectWizard {

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   update (Observer)
    /**
     ** Notification message.
     ** <p>
     ** Subjects call this method when they notify their observers that the
     ** subjects state has changed.
     **
     ** @param observed          the subject whose state has changed.
     ** @param change            what has changed.
     */
    @Override
    public final void update(final Object observed, final UpdateMessage change) {
      // if the update message originates to the technology scope
      PropertyChangeEvent event = (PropertyChangeEvent)change.getProperty(TechnologyScopeConfiguration.TECHNOLOGY_SCOPE_PROPERTY);
      if (event != null)
        // ... configure the project accordingly
        Configurator.updateTechnology((Project)observed, wizardContext());
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functinality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   postProcessing (overridden)
    /**
     ** Called when the wizard is finished and all other processing has
     ** happened after the workspace and/or projects have been created, and the
     ** wizard pages allowed to make modifications to the generated project.
     */
    @Override
    protected void postProcessing() {
      // find the project by creating it or take it from the wizard context
      final TraversableContext[]  contexts = TemplateWizardUtil.getProjectTraversableContexts(this.wizardContext());
      final Project               project  = TemplateWizardUtil.getProject(contexts[0]);
      Configurator.updateFeature(project, wizardContext());

      // ensure inheritance
      super.postProcessing();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>ProjectWizard</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected ProjectWizard() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Access methdos
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   projectTemplate
  /**
   ** Returns the project template id for this this
   ** <code>TemplateProjectWizard</code> expects to get operational.
   **
   ** @return                    the application template id of the owning
   **                            workspace this
   **                            <code>TemplateProjectWizard</code> expects to
   **                            get operational.
   */
  public abstract ProjectTemplate projectTemplate();

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isAvailable (Wizard)
  /**
   ** Called when the sensitivity of the menu item that opens this wizard must
   ** be determined.
   ** <p>
   ** Since OSGi Bundle support introduced in JDeveloper the method has no
   ** importance anymore to conttrol the behavior of the gallery items this
   ** wizard belongs to. Therefor the method has to be implemented due to the
   ** class constraint where its declared abstract, but we retuning always
   ** <code>true</code>.
   **
   ** @param  context            the {@link Context} to use when invoking this
   **                            {@link AbstractWizard}.
   **
   ** @return                    always <code>true</code> due to the
   **                            modifications to control the state of the
   **                            gallery item.
   */
  @Override
  public final boolean isAvailable(final Context context) {
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke (Wizard)
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
    final Flow dialog = new Flow();
    return dialog.runWizard(context, projectTemplate());
  }
}