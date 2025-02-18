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

    File        :   TemplateObjectPage.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    TemplateObjectPage.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.wizard;

import java.awt.Component;

import oracle.bali.ewt.event.Cancelable;

import oracle.bali.ewt.wizard.WizardPage;
import oracle.bali.ewt.wizard.WizardEvent;
import oracle.bali.ewt.wizard.WizardValidateListener;

import oracle.ide.model.Project;
import oracle.ide.model.Workspace;

import oracle.ide.panels.TraversableContext;
import oracle.ide.panels.TraversalException;

import oracle.jdeveloper.template.wizard.WizardTraversable;
import oracle.jdeveloper.template.wizard.TemplateWizardUtil;
import oracle.jdeveloper.template.wizard.WizardPageFocusSetter;

////////////////////////////////////////////////////////////////////////////////
// abstract class TemplateObjectPage
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** TemplateObjectPage hold the content of the wizard.
 ** <p>
 ** This class implements the operations that must be supported by a GUI
 ** component added to the project-from-template and application-from-template
 ** wizards.
 ** <br>
 ** The GUI component is associated with a particular technology scope and is
 ** registered declaratively using the technology-hook hook.
 ** <p>
 ** This class has to be reimplementad due to the implementation of class
 ** oracle.jdeveloper.template.wizard.TemplateWizardPage is final.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class TemplateObjectPage extends    WizardPage
                                implements WizardPageFocusSetter
                                ,          WizardValidateListener {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // the properties to stored during the traversal of a wizard
  private final TraversableContext traversableContext;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a wizard page.
   **
   ** @param  content            the component used for this page.
   ** @param  label              the label on this page.
   **                            The label is only user-visible for
   **                            ReentrantWizards.
   ** @param  title              the label on this page.
   ** @param  context            the {@link TraversableContext} of the current
   **                            workspace.
   */
  public TemplateObjectPage(final Component content, final String label, final String title, final TraversableContext context) {
    // ensure inheritance
    super(content, label, title);

    // initialize instance attributes
    this.traversableContext = context;
    this.addWizardValidateListener(this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDefaultFocusComponent (WizardValidateListener)
  /**
   ** Set the component that should be given focus when a wizard page is
   ** visited.
   **
   ** @param  component          the initial-focus component
   */
  @Override
  public void setDefaultFocusComponent(final Component component) {
    this.setInitialFocus(component);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   wizardValidatePage (WizardPageFocusSetter)
  /**
   ** Invoked when the user has asked to finish the wizard, apply the current
   ** state, or switch to another page.
   ** <p>
   ** Clients can refuse the user's request by casting the event to a
   ** {@link Cancelable} and calling the cancel() method on the event.
   **
   ** @param  event              a {@link WizardEvent} object describing the
     **                          event source and the property that has changed.
   */
  public void wizardValidatePage(final WizardEvent event) {
    if (event != null && event.getPage() == this) {
      Component gui = event.getPage().getContent();
      if (gui instanceof WizardTraversable) {
        try {
          ((WizardTraversable)gui).onExit(this.traversableContext);
        }
        catch (TraversalException e) {
          e.showMessageDialog(gui);
          ((Cancelable)event).cancel();
        }
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enterPage
  /**
   ** Called when the page becomes visible in the page flow
   */
  public void enterPage() {
    if (this.getContent() instanceof WizardTraversable) {
      this.traversableContext.put(FOCUS_SETTER_KEY, this);
      ((WizardTraversable)this.getContent()).onEntry(this.traversableContext);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mustFinish
  /**
   ** Indicates whether or not {@link #wizardFinished()} should be called even
   ** if the wizard page has not been visited, and thus the UI not changed.
   **
   ** @return                    <code>true</code> specifies that
   **                            {@link #wizardFinished()} should be called even
   **                            if the page was not visted before the wizard
   **                            was finished <code>false</code> indicates that
   **                            it's okay to finish the wizard without calling
   **                            {@link #wizardFinished()} on this panel.
   */
  public boolean mustFinish() {
    return this.getContent() instanceof WizardTraversable ? ((WizardTraversable)this.getContent()).mustCommitWizardChanges(this.traversableContext) : false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   wizardFinished
  /**
   ** Called when the user clicks Finish on the wizard to commit the changes in
   ** the wizard context.
   ** <p>
   ** The wizard framework handles setting the technology scope in the generated
   ** project, so classes that implement {@link WizardTraversable} should not
   ** set the technology scope on their own. Any registered listeners are
   ** notified as a result of technologies being added to the project, so only
   ** operations which are not handled as a result of technology changes should
   ** be handled here.
   */
  public void wizardFinished() {
    if (this.getContent() instanceof WizardTraversable) {
      Project   project   = TemplateWizardUtil.getProject(this.traversableContext);
      Workspace workspace = TemplateWizardUtil.getApplication(this.traversableContext);
      ((WizardTraversable)this.getContent()).commitWizardChanges(project, workspace, this.traversableContext);
    }
  }
}