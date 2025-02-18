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

    Copyright © 2020. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Employee Self Service Portal
    Subsystem   :   Common Shared Components

    File        :   AbstractBean.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractBean.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2020-03-03  DSteding    First release version
*/

package bka.employee.portal.train.state;

import java.util.Map;

import org.apache.myfaces.trinidad.util.ComponentReference;

import oracle.adf.view.rich.component.rich.layout.RichPanelFormLayout;

import oracle.hst.foundation.faces.ADF;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractBean
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~
/**
 ** Declares methods a user interface train step without a noun table provides.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class AbstractBean extends bka.employee.portal.view.state.AbstractBean {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:3749311744052265111")
  private static final long            serialVersionUID = -8295303104081656454L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private boolean                      flowDirty        = false;

  /**
   ** component below is related to the attribute form placed in the detail
   ** region of an <code>Application Instance</code>.
   ** The component needs to be reachable at any time if the one of the revert
   ** buttons is pressed in any region hence it needs to be serialized in the
   ** page flow scope.
   */
  private transient ComponentReference formLayout;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractBean</code> backing bean that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected AbstractBean() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setFlowDirty
  /**
   ** Sets the value of the flowDirty property.
   ** <p>
   ** Method needs to be declared as <b><code>public</code></b> because it used
   ** in the UI.
   **
   ** @param  value              the value of the flowDirty property.
   **                            Allowed object is <code>boolean</code>.
   */
  public void setFlowDirty(final boolean value) {
    this.flowDirty = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isFlowDirty
  /**
   ** Returns the value of the flowDirty property.
   ** <p>
   ** Method needs to be declared as <b><code>public</code></b> because it used
   ** in the UI.
   **
   ** @return                    the value of the flowDirty property.
   **                            Possible object is <code>boolean</code>.
   */
  public boolean isFlowDirty() {
    return this.flowDirty;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setFormLayout
  /**
   ** Sets the UI component which renders the form layout of the attribute
   ** region.
   **
   ** @param  component          the UI component which renders the form layout
   **                            of the attribute region.
   **                            Allowed object is
   **                            {@link RichPanelFormLayout}.
   */
  public void setFormLayout(final RichPanelFormLayout component) {
    this.formLayout = ComponentReference.newUIComponentReference(component);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getFormLayout
  /**
   ** Returns the UI component which renders the form layout of the attribute
   ** region.
   **
   ** @return                    the UI component which renders the form layout
   **                            of the attribute region.
   **                            Possible object is
   **                            {@link RichPanelFormLayout}.
   */
  public RichPanelFormLayout getFormLayout() {
    return (this.formLayout != null) ? (RichPanelFormLayout)this.formLayout.getComponent() : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   markDirty
  /**
   ** Raise a <code>Contextaul Event</code> programmatically to mark the train
   ** taskflow as dirty.
   */
  public void markDirty() {
    this.flowDirty = true;
    raiseTaskFlowMarkDirtyEvent(ADF.pageFlowScopeStringValue(PARAMETER_TASKFLOW));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   markClean
  /**
   ** Raise a <code>Contextaul Event</code> programmatically to mark the train
   ** taskflow as clean.
   ** <p>
   ** The short version assumes that there is no change in the taskflow
   ** metadata.
   */
  public void markClean() {
    markClean(null, null, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   markClean
  /**
   ** Raise a <code>Contextaul Event</code> programmatically to mark the train
   ** taskflow as clean.
   **
   ** @param  name               the identifiying name of the taskflow a UI
   **                            region mostly the tab
   **                            Allowed object {@link String}.
   ** @param  description        the discription of the taskflow
   **                            Allowed object {@link String}.
   ** @param  parameter          the parameter mapping to pass to the taskflow.
   **                            Allowed object {@link Map}.
   */
  public void markClean(final String name, final String description, final Map<String, Object> parameter) {
    setFlowDirty(false);
    raiseTaskFlowMarkCleanEvent(ADF.pageFlowScopeStringValue(PARAMETER_TASKFLOW), name, description, parameter);
  }
}