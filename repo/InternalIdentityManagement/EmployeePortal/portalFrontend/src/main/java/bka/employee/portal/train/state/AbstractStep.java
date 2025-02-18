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

    File        :   AbstractStep.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractStep.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2020-03-03  DSteding    First release version
*/

package bka.employee.portal.train.state;

import javax.faces.event.ValueChangeEvent;

import org.apache.myfaces.trinidad.util.ComponentReference;

import oracle.adf.view.rich.component.rich.nav.RichButton;

import oracle.hst.foundation.faces.ADF;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractStep
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~
/**
 ** Declares methods a user interface train step without a noun table provides.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
 */
public abstract class AbstractStep extends bka.employee.portal.view.state.AbstractBean {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:386055210814627374")
  private static final long             serialVersionUID = 4544139068486175789L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private transient AbstractBean        train;

  private transient ComponentReference  submit;
  private transient ComponentReference  revert;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractStep</code> backing bean that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected AbstractStep() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setTrain
  /**
   ** Sets the value of the train property.
   ** <p>
   ** Method needs to be declared as <b><code>public</code></b> because it used
   ** in the UI.
   **
   ** @param  value              the value of the train property.
   **                            Allowed object is {@link AbstractBean}.
   */
  public void setTrain(final AbstractBean value) {
    this.train = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getTrain
  /**
   ** Returns the value of the train property.
   ** <p>
   ** Method needs to be declared as <b><code>public</code></b> because it used
   ** in the UI.
   **
   ** @return                    the value of the train property.
   **                            Possible object is {@link AbstractBean}.
   */
  public AbstractBean getTrain() {
    return this.train;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setSubmit
  /**
   ** Sets the UI component which renders the submit button in a train step
   ** which modifies a certain entry.
   **
   ** @param  component          the UI component which renders the submit
   **                            button in a train step which modifies a
   **                            certain entry.
   **                            Allowed object is {@link RichButton}.
   */
  public void setSubmit(final RichButton component) {
    this.submit = ComponentReference.newUIComponentReference(component);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getSubmit
  /**
   ** Returns the UI component which renders the submit button in a train step
   ** which modifies a certain entry.
   **
   ** @return                    the UI component which renders the submit
   **                            button in a train step which modifies a
   **                            certain entry.
   **                            Possible object is {@link RichButton}.
   */
  public RichButton getSubmit() {
    return (this.submit != null) ? (RichButton)this.submit.getComponent() : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setRevert
  /**
   ** Sets the UI component which renders the revert button in a train step
   ** which modifies a certain entry.
   **
   ** @param  component          the UI component which renders the revert
   **                            button in a train step which modifies a
   **                            certain entry.
   **                            Allowed object is {@link RichButton}.
   */
  public void setRevert(final RichButton component) {
    this.revert = ComponentReference.newUIComponentReference(component);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getRevert
  /**
   ** Returns the UI component which renders the submit button in a train step
   ** which modifies a certain entry.
   **
   ** @return                    the UI component which renders the revert
   **                            button in a train step which modifies a
   **                            certain entry.
   **                            Possible object is {@link RichButton}.
   */
  public RichButton getRevert() {
    return (this.revert != null) ? (RichButton)this.revert.getComponent() : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   changed
  /**
   ** Perfoms all actions belonging to the UI to reflect value change events on
   ** particular components in the current page.
   **
   ** @param  event              the {@link ValueChangeEvent} object that
   **                            characterizes the action to perform.
   */
  public void changed(final @SuppressWarnings("unused") ValueChangeEvent event) {
    getTrain().markDirty();
    partialRenderSubmitRevert();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   partialRenderSubmitRevert
  /**
   ** Add the global action components (Save/Apply/Revert) as a partial
   ** rendering target.
   ** <p>
   ** In response to a partial event, only components registered as partial
   ** targets are re-rendered. For a component to be successfully re-rendered
   ** when it is manually added with this API, it should have the
   ** "clientComponent" attribute set to <code>true</code>. If not, partial
   ** re-rendering may or may not work depending on the component.
   */
  public void partialRenderSubmitRevert() {
    ADF.partialRender(getSubmit());
    ADF.partialRender(getRevert());
  }
}