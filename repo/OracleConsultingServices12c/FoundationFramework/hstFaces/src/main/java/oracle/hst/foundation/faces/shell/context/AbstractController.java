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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Shared Library
    Subsystem   :   Java Server Faces Foundation

    File        :   AbstractController.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractController.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-28-12  DSteding    First release version
*/

package oracle.hst.foundation.faces.shell.context;

import java.io.Serializable;

import org.apache.myfaces.trinidad.component.UIXPanel;

import org.apache.myfaces.trinidad.util.ComponentReference;

import oracle.adf.view.rich.component.rich.RichMenu;

import oracle.adf.view.rich.component.rich.RichPopup;

import oracle.hst.foundation.faces.ManagedBean;

////////////////////////////////////////////////////////////////////////////////
// class AbstractController
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** Multi region page flow controller
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class AbstractController extends    ManagedBean
                                implements Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-3242569594371304784")
  private static final long     serialVersionUID = 4875207575304161838L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private ComponentReference<?> userMenu;
  private ComponentReference<?> rootPanel;
  private ComponentReference<?> contentPanel;
  private ComponentReference<?> feedbackPopup;
  private ComponentReference<?> unsavedModulePopup;
  private ComponentReference<?> unsavedWarningPopup;
  private ComponentReference<?> feedbackTaskFlowPopup;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractController</code> event handler that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected AbstractController() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setRootPanel
  /**
   ** Sets the root {@link UIXPanel} of the content area.
   **
   ** @param  value              the root {@link UIXPanel} of the content area.
   **                            Allowed object is {@link UIXPanel}.
   */
  public void setRootPanel(final UIXPanel value) {
    this.rootPanel = ComponentReference.newUIComponentReference(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getRootPanel
  /**
   ** Returns the root {@link UIXPanel} of the content area.
   **
   ** @return                    the root {@link UIXPanel} of the content area.
   **                            Possible object is {@link UIXPanel}.
   */
  public UIXPanel getRootPanel() {
    return (this.rootPanel != null) ? (UIXPanel)this.rootPanel.getComponent() : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setContentPanel
  /**
   ** Sets the region {@link UIXPanel} of the content area.
   **
   ** @param  value              the region {@link UIXPanel} of the content
   **                            area.
   **                            Allowed object is {@link UIXPanel}.
   */
  public void setContentPanel(final UIXPanel value) {
    this.contentPanel = ComponentReference.newUIComponentReference(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getContentPanel
  /**
   ** Returns the region {@link UIXPanel} of the content area.
   **
   ** @return                    the region {@link UIXPanel} of the content
   **                            area.
   **                            Possible object is {@link UIXPanel}.
   */
  public UIXPanel getContentPanel() {
    return (this.contentPanel != null) ? (UIXPanel)this.contentPanel.getComponent() : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setUserMenu
  /**
   ** Sets the ui componente which renders the user menu.
   **
   ** @param  value              the ui componente which renders the user menu.
   **                            Allowed object is {@link RichMenu}.
   */
  public void setUserMenu(final RichMenu value) {
    this.userMenu = ComponentReference.newUIComponentReference(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUserMenu
  /**
   ** Returns the ui componente which renders the user menu.
   **
   ** @return                    the ui componente which renders the user menu.
   **                            Possible object is {@link RichMenu}.
   */
  public RichMenu getUserMenu() {
    return (this.userMenu != null) ? (RichMenu)this.userMenu.getComponent() : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setFeedbackPopup
  /**
   ** Sets the {@link RichPopup} component used to provide feedback to an end
   ** user.
   **
   ** @param  value              the {@link RichPopup} component to be used to
   **                            provide feedback to an end user.
   **                            Allowed object is {@link RichPopup}.
   */
  public void setFeedbackPopup(final RichPopup value) {
    this.feedbackPopup = ComponentReference.newUIComponentReference(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getFeedbackPopup
  /**
   ** Returns the {@link RichPopup} component used to provide feedback to an end
   ** user.
   **
   ** @return                    the {@link RichPopup} component used to provide
   **                            feedback to an end user.
   **                            Possible object is {@link RichPopup}.
   */
  public RichPopup getFeedbackPopup() {
    return (this.feedbackPopup == null) ? null : (RichPopup)this.feedbackPopup.getComponent();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setUnsavedModulePopup
  /**
   ** Sets the {@link RichPopup} component used to provide feedback about
   ** unsaved changes in a module task flow.
   **
   ** @param  value              the {@link RichPopup} component used to provide
   **                            feedback about unsaved changes in a module task
   **                            flow.
   **                            Allowed object is {@link RichPopup}.
   */
  public void setUnsavedModulePopup(final RichPopup value) {
    this.unsavedModulePopup = ComponentReference.newUIComponentReference(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUnsavedModulePopup
  /**
   ** Returns the {@link RichPopup} component used to provide feedback about
   ** unsaved changes in a module task flow.
   **
   ** @return                    the {@link RichPopup} component used to provide
   **                            feedback about unsaved changes in a module task
   **                            flow.
   **                            Possible object is {@link RichPopup}.
   */
  public RichPopup getUnsavedModulePopup() {
    return (this.unsavedModulePopup == null) ? null : (RichPopup)this.unsavedModulePopup.getComponent();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setUnsavedWarningPopup
  /**
   ** Sets the {@link RichPopup} component used to provide feedback about
   ** unsaved changes in a detail task flow.
   **
   ** @param  value              the {@link RichPopup} component used to provide
   **                            feedback about unsaved changes in a detail task
   **                            flow.
   **                            Allowed object is {@link RichPopup}.
   */
  public void setUnsavedWarningPopup(final RichPopup value) {
    this.unsavedWarningPopup = ComponentReference.newUIComponentReference(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUnsavedWarningPopup
  /**
   ** Returns the {@link RichPopup} component used to provide feedback about
   ** unsaved changes in a detail task flow.
   **
   ** @return                    the {@link RichPopup} component used to provide
   **                            feedback about unsaved changes in a detail task
   **                            flow.
   **                            Possible object is {@link RichPopup}.
   */
  public RichPopup getUnsavedWarningPopup() {
    return (this.unsavedWarningPopup == null) ? null : (RichPopup)this.unsavedWarningPopup.getComponent();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setFeedbackTaskFlowPopup
  /**
   ** Sets the {@link RichPopup} component used to provide feedback to an end
   ** user about unsaved changes in a task flow.
   **
   ** @param  value              the {@link RichPopup} component used to provide
   **                            feedback to an end user about unsaved changes
   **                            in a task flow.
   **                            Allowed object is {@link RichPopup}.
   */
  public void setFeedbackTaskFlowPopup(final RichPopup value) {
    this.feedbackTaskFlowPopup = ComponentReference.newUIComponentReference(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getFeedbackTaskFlowPopup
  /**
   ** Returns the {@link RichPopup} component used to provide feedback to an end
   ** user about unsaved changes in a task flow.
   **
   ** @return                    the {@link RichPopup} component used to provide
   **                            feedback to an end user about unsaved changes
   **                            in a task flow.
   **                            Possible object is {@link RichPopup}.
   */
  public RichPopup getFeedbackTaskFlowPopup() {
    return (this.feedbackTaskFlowPopup == null) ? null : (RichPopup)this.feedbackTaskFlowPopup.getComponent();
  }
}