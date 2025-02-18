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

    Copyright © 2016. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Frontend Extension
    Subsystem   :   System Authorization Management

    File        :   AttributeState.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AttributeState.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2016-03-03  DSteding    First release version
*/

package oracle.iam.identity.sysauthz.cat.backing;

import java.util.Map;
import java.util.HashMap;

import javax.faces.context.FacesContext;

import javax.faces.event.ActionEvent;

import javax.faces.component.UIComponent;

import javax.faces.event.ValueChangeEvent;

import org.apache.myfaces.trinidad.util.ComponentReference;

import oracle.adf.view.rich.component.rich.input.RichInputText;

import oracle.hst.foundation.faces.JSF;
import oracle.hst.foundation.faces.ADF;

import oracle.iam.identity.frontend.train.AbstractStep;

import oracle.iam.identity.sysauthz.cat.state.Train;

import oracle.iam.identity.sysauthz.schema.CatalogItemAdapter;
////////////////////////////////////////////////////////////////////////////////
// class AttributeState
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** Declares methods the user interface service provides to navigate across
 ** modules.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
 */
public class AttributeState extends AbstractStep {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String           PICK_ROLE_SCOPE   = "catalog_role";
  public static final String           PICK_USER_SCOPE   = "catalog_user";

  public static final String           DETAIL_ITERATOR   = "CatalogItemIterator";

  private static final String          PICK_ROLE_FLOW    = "/WEB-INF/oracle/iam/ui/common/tfs/role-picker-tf.xml#role-picker-tf";
  private static final String          PICK_USER_FLOW    = "/WEB-INF/oracle/iam/ui/common/tfs/user-picker-tf.xml#user-picker-tf";

  private static final String          AUTHORIZATION     = "oracle.iam.identity.sysauthz.bundle.Backend";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-4888406423041560338")
  private static final long            serialVersionUID  = 2715629866529397055L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private transient ComponentReference approverRoleName;
  private transient ComponentReference approverRoleDisplayName;
  private transient ComponentReference approverUserName;
  private transient ComponentReference approverUserDisplayName;
  private transient ComponentReference certifierRoleName;
  private transient ComponentReference certifierRoleDisplayName;
  private transient ComponentReference certifierUserName;
  private transient ComponentReference certifierUserDisplayName;
  private transient ComponentReference fulfillmentRoleName;
  private transient ComponentReference fulfillmentRoleDisplayName;
  private transient ComponentReference fulfillmentUserName;
  private transient ComponentReference fulfillmentUserDisplayName;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AttributeState</code> backing bean that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AttributeState() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setApproverRoleName
  /**
   ** Sets the UI component which renders the approver role input component in
   ** the attribute region.
   ** <br>
   ** When you store a reference to a UI Component in a bean, the complete
   ** component tree (children and parent) are kept in memory.
   ** This causes the ADF application to consume a unnecessary big amount of
   ** memory.
   ** <b>Note</b>:
   ** <br>
   ** Unfortunately when you use the auto generation feature for component
   ** binding in JDeveloper, it will generates the references as depicted below:
   ** <pre>
   **   public void setApproverRoleName(final RichInputText component) {
   **     this.approverRoleName = component;
   **   }
   ** </pre>
   ** This should be changed using the much more memory efficient
   ** {@link ComponentReference} class:
   ** <pre>
   **   private ComponentReference approverRoleName;
   **   ...
   **   public void setApproverRoleName(final RichInputText component) {
   **     this.approverRoleName = ComponentReference.newUIComponentReference(component);
   **   }
   ** </pre>
   **
   ** @param  component          the UI component which renders the approver
   **                            role input component in the attribute region.
   */
  public void setApproverRoleName(final RichInputText component) {
    this.approverRoleName = ComponentReference.newUIComponentReference(component);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getApproverRoleName
  /**
   ** Returns the UI component which renders the approver role input component
   ** in the attribute region.
   **
   ** @return                    the UI component which renders the approver
   **                            role input component in the attribute region.
   */
  public RichInputText getApproverRoleName() {
    return (this.approverRoleName != null) ? (RichInputText)this.approverRoleName.getComponent() : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setApproverRoleDisplayName
  /**
   ** Sets the UI component which renders the approver role input component in
   ** the attribute region.
   ** <br>
   ** When you store a reference to a UI Component in a bean, the complete
   ** component tree (children and parent) are kept in memory.
   ** This causes the ADF application to consume a unnecessary big amount of
   ** memory.
   ** <b>Note</b>:
   ** <br>
   ** Unfortunately when you use the auto generation feature for component
   ** binding in JDeveloper, it will generates the references as depicted below:
   ** <pre>
   **   public void setApproverRoleDisplayName(final RichInputText component) {
   **     this.approverRoleDisplayName = component;
   **   }
   ** </pre>
   ** This should be changed using the much more memory efficient
   ** {@link ComponentReference} class:
   ** <pre>
   **   private ComponentReference approverRoleDisplayName;
   **   ...
   **   public void setApproverRoleDisplayName(final RichInputText component) {
   **     this.approverRoleDisplayName = ComponentReference.newUIComponentReference(component);
   **   }
   ** </pre>
   **
   ** @param  component          the UI component which renders the approver
   **                            role input component in the attribute region.
   */
  public void setApproverRoleDisplayName(final RichInputText component) {
    this.approverRoleDisplayName = ComponentReference.newUIComponentReference(component);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getApproverRoleDisplayName
  /**
   ** Returns the UI component which renders the approver role input component
   ** in the attribute region.
   **
   ** @return                    the UI component which renders the approver
   **                            role input component in the attribute region.
   */
  public RichInputText getApproverRoleDisplayName() {
    return (this.approverRoleDisplayName != null) ? (RichInputText)this.approverRoleDisplayName.getComponent() : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setApproverUserName
  /**
   ** Sets the UI component which renders the approver user input component in
   ** the attribute region.
   ** <br>
   ** When you store a reference to a UI Component in a bean, the complete
   ** component tree (children and parent) are kept in memory.
   ** This causes the ADF application to consume a unnecessary big amount of
   ** memory.
   ** <b>Note</b>:
   ** <br>
   ** Unfortunately when you use the auto generation feature for component
   ** binding in JDeveloper, it will generates the references as depicted below:
   ** <pre>
   **   public void setApproverUserName(final RichInputText component) {
   **     this.approverUserName = component;
   **   }
   ** </pre>
   ** This should be changed using the much more memory efficient
   ** {@link ComponentReference} class:
   ** <pre>
   **   private ComponentReference ApproverRole;
   **   ...
   **   public void setApproverUserName(final RichInputText component) {
   **     this.approverUserName = ComponentReference.newUIComponentReference(component);
   **   }
   ** </pre>
   **
   ** @param  component          the UI component which renders the endpoint
   **                            name input component in the attribute region.
   */
  public void setApproverUserName(final RichInputText component) {
    this.approverUserName = ComponentReference.newUIComponentReference(component);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getApproverUserName
  /**
   ** Returns the UI component which renders the approver user input component
   ** in the attribute region.
   **
   ** @return                    the UI component which renders the endpoint
   **                            name input component in the attribute region.
   */
  public RichInputText getApproverUserName() {
    return (this.approverUserName != null) ? (RichInputText)this.approverUserName.getComponent() : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setApproverUserDisplayName
  /**
   ** Sets the UI component which renders the approver user input component in
   ** the attribute region.
   ** <br>
   ** When you store a reference to a UI Component in a bean, the complete
   ** component tree (children and parent) are kept in memory.
   ** This causes the ADF application to consume a unnecessary big amount of
   ** memory.
   ** <b>Note</b>:
   ** <br>
   ** Unfortunately when you use the auto generation feature for component
   ** binding in JDeveloper, it will generates the references as depicted below:
   ** <pre>
   **   public void setApproverUserDisplayName(final RichInputText component) {
   **     this.approverUserDisplayName = component;
   **   }
   ** </pre>
   ** This should be changed using the much more memory efficient
   ** {@link ComponentReference} class:
   ** <pre>
   **   private ComponentReference ApproverRole;
   **   ...
   **   public void setApproverUserDisplayName(final RichInputText component) {
   **     this.approverUserDisplayName = ComponentReference.newUIComponentReference(component);
   **   }
   ** </pre>
   **
   ** @param  component          the UI component which renders the endpoint
   **                            name input component in the attribute region.
   */
  public void setApproverUserDisplayName(final RichInputText component) {
    this.approverUserDisplayName = ComponentReference.newUIComponentReference(component);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getApproverUserDisplayName
  /**
   ** Returns the UI component which renders the approver user input component
   ** in the attribute region.
   **
   ** @return                    the UI component which renders the endpoint
   **                            name input component in the attribute region.
   */
  public RichInputText getApproverUserDisplayName() {
    return (this.approverUserDisplayName != null) ? (RichInputText)this.approverUserDisplayName.getComponent() : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setCertifierRoleName
  /**
   ** Sets the UI component which renders the certifier role input component in
   ** the attribute region.
   ** <br>
   ** When you store a reference to a UI Component in a bean, the complete
   ** component tree (children and parent) are kept in memory.
   ** This causes the ADF application to consume a unnecessary big amount of
   ** memory.
   ** <b>Note</b>:
   ** <br>
   ** Unfortunately when you use the auto generation feature for component
   ** binding in JDeveloper, it will generates the references as depicted below:
   ** <pre>
   **   public void setCertifierRoleName(final RichInputText component) {
   **     this.certifierRoleName = component;
   **   }
   ** </pre>
   ** This should be changed using the much more memory efficient
   ** {@link ComponentReference} class:
   ** <pre>
   **   private ComponentReference certifierRoleName;
   **   ...
   **   public void setCertifierRoleName(final RichInputText component) {
   **     this.certifierRoleName = ComponentReference.newUIComponentReference(component);
   **   }
   ** </pre>
   **
   ** @param  component          the UI component which renders the certifier
   **                            role input component in the attribute region.
   */
  public void setCertifierRoleName(final RichInputText component) {
    this.certifierRoleName = ComponentReference.newUIComponentReference(component);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getCertifierRoleName
  /**
   ** Returns the UI component which renders the certifier role input component
   ** in the attribute region.
   **
   ** @return                    the UI component which renders the certifier
   **                            role input component in the attribute region.
   */
  public RichInputText getCertifierRoleName() {
    return (this.certifierRoleName != null) ? (RichInputText)this.certifierRoleName.getComponent() : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setCertifierRoleDisplayName
  /**
   ** Sets the UI component which renders the certifier role input component in
   ** the attribute region.
   ** <br>
   ** When you store a reference to a UI Component in a bean, the complete
   ** component tree (children and parent) are kept in memory.
   ** This causes the ADF application to consume a unnecessary big amount of
   ** memory.
   ** <b>Note</b>:
   ** <br>
   ** Unfortunately when you use the auto generation feature for component
   ** binding in JDeveloper, it will generates the references as depicted below:
   ** <pre>
   **   public void setCertifierRoleDisplayName(final RichInputText component) {
   **     this.certifierRoleDisplayName = component;
   **   }
   ** </pre>
   ** This should be changed using the much more memory efficient
   ** {@link ComponentReference} class:
   ** <pre>
   **   private ComponentReference certifierRoleDisplayName;
   **   ...
   **   public void setCertifierRoleDisplayName(final RichInputText component) {
   **     this.certifierRoleDisplayName = ComponentReference.newUIComponentReference(component);
   **   }
   ** </pre>
   **
   ** @param  component          the UI component which renders the certifier
   **                            role input component in the attribute region.
   */
  public void setCertifierRoleDisplayName(final RichInputText component) {
    this.certifierRoleDisplayName = ComponentReference.newUIComponentReference(component);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getCertifierRoleDisplayName
  /**
   ** Returns the UI component which renders the certifier role input component
   ** in the attribute region.
   **
   ** @return                    the UI component which renders the certifier
   **                            role input component in the attribute region.
   */
  public RichInputText getCertifierRoleDisplayName() {
    return (this.certifierRoleDisplayName != null) ? (RichInputText)this.certifierRoleDisplayName.getComponent() : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setCertifierUserName
  /**
   ** Sets the UI component which renders the certifier user input component in
   ** the attribute region.
   ** <br>
   ** When you store a reference to a UI Component in a bean, the complete
   ** component tree (children and parent) are kept in memory.
   ** This causes the ADF application to consume a unnecessary big amount of
   ** memory.
   ** <b>Note</b>:
   ** <br>
   ** Unfortunately when you use the auto generation feature for component
   ** binding in JDeveloper, it will generates the references as depicted below:
   ** <pre>
   **   public void setCertifierUserName(final RichInputText component) {
   **     this.certifierUserName = component;
   **   }
   ** </pre>
   ** This should be changed using the much more memory efficient
   ** {@link ComponentReference} class:
   ** <pre>
   **   private ComponentReference certifierUserName;
   **   ...
   **   public void setCertifierUserName(final RichInputText component) {
   **     this.certifierUserName = ComponentReference.newUIComponentReference(component);
   **   }
   ** </pre>
   **
   ** @param  component          the UI component which renders the endpoint
   **                            name input component in the attribute region.
   */
  public void setCertifierUserName(final RichInputText component) {
    this.certifierUserName = ComponentReference.newUIComponentReference(component);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getCertifierUserName
  /**
   ** Returns the UI component which renders the certifier user input component
   ** in the attribute region.
   **
   ** @return                    the UI component which renders the endpoint
   **                            name input component in the attribute region.
   */
  public RichInputText getCertifierUserName() {
    return (this.certifierUserName != null) ? (RichInputText)this.certifierUserName.getComponent() : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setCertifierUserDisplayName
  /**
   ** Sets the UI component which renders the certifier user input component in
   ** the attribute region.
   ** <br>
   ** When you store a reference to a UI Component in a bean, the complete
   ** component tree (children and parent) are kept in memory.
   ** This causes the ADF application to consume a unnecessary big amount of
   ** memory.
   ** <b>Note</b>:
   ** <br>
   ** Unfortunately when you use the auto generation feature for component
   ** binding in JDeveloper, it will generates the references as depicted below:
   ** <pre>
   **   public void setCertifierUserDisplayName(final RichInputText component) {
   **     this.certifierUserDisplayName = component;
   **   }
   ** </pre>
   ** This should be changed using the much more memory efficient
   ** {@link ComponentReference} class:
   ** <pre>
   **   private ComponentReference certifierUserDisplayName;
   **   ...
   **   public void setCertifierUserDisplayName(final RichInputText component) {
   **     this.certifierUserDisplayName = ComponentReference.newUIComponentReference(component);
   **   }
   ** </pre>
   **
   ** @param  component          the UI component which renders the endpoint
   **                            name input component in the attribute region.
   */
  public void setCertifierUserDisplayName(final RichInputText component) {
    this.certifierUserDisplayName = ComponentReference.newUIComponentReference(component);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getCertifierUserDisplayName
  /**
   ** Returns the UI component which renders the certifier user input component
   ** in the attribute region.
   **
   ** @return                    the UI component which renders the endpoint
   **                            name input component in the attribute region.
   */
  public RichInputText getCertifierUserDisplayName() {
    return (this.certifierUserDisplayName != null) ? (RichInputText)this.certifierUserDisplayName.getComponent() : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setFulfillmentRoleName
  /**
   ** Sets the UI component which renders the fulfillment role input component in
   ** the attribute region.
   ** <br>
   ** When you store a reference to a UI Component in a bean, the complete
   ** component tree (children and parent) are kept in memory.
   ** This causes the ADF application to consume a unnecessary big amount of
   ** memory.
   ** <b>Note</b>:
   ** <br>
   ** Unfortunately when you use the auto generation feature for component
   ** binding in JDeveloper, it will generates the references as depicted below:
   ** <pre>
   **   public void setFulfillmentRoleName(final RichInputText component) {
   **     this.fulfillmentRoleName = component;
   **   }
   ** </pre>
   ** This should be changed using the much more memory efficient
   ** {@link ComponentReference} class:
   ** <pre>
   **   private ComponentReference fulfillmentRoleName;
   **   ...
   **   public void setFulfillmentRoleName(final RichInputText component) {
   **     this.fulfillmentRoleName = ComponentReference.newUIComponentReference(component);
   **   }
   ** </pre>
   **
   ** @param  component          the UI component which renders the fulfillment
   **                            role input component in the attribute region.
   */
  public void setFulfillmentRoleName(final RichInputText component) {
    this.fulfillmentRoleName = ComponentReference.newUIComponentReference(component);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getFulfillmentRoleName
  /**
   ** Returns the UI component which renders the fulfillment role input component
   ** in the attribute region.
   **
   ** @return                    the UI component which renders the fulfillment
   **                            role input component in the attribute region.
   */
  public RichInputText getFulfillmentRoleName() {
    return (this.fulfillmentRoleName != null) ? (RichInputText)this.fulfillmentRoleName.getComponent() : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setFulfillmentRoleDisplayName
  /**
   ** Sets the UI component which renders the fulfillment role input component in
   ** the attribute region.
   ** <br>
   ** When you store a reference to a UI Component in a bean, the complete
   ** component tree (children and parent) are kept in memory.
   ** This causes the ADF application to consume a unnecessary big amount of
   ** memory.
   ** <b>Note</b>:
   ** <br>
   ** Unfortunately when you use the auto generation feature for component
   ** binding in JDeveloper, it will generates the references as depicted below:
   ** <pre>
   **   public void setFulfillmentRoleDisplayName(final RichInputText component) {
   **     this.fulfillmentRoleDisplayName = component;
   **   }
   ** </pre>
   ** This should be changed using the much more memory efficient
   ** {@link ComponentReference} class:
   ** <pre>
   **   private ComponentReference fulfillmentRoleDisplayName;
   **   ...
   **   public void setFulfillmentRoleDisplayName(final RichInputText component) {
   **     this.fulfillmentRoleDisplayName = ComponentReference.newUIComponentReference(component);
   **   }
   ** </pre>
   **
   ** @param  component          the UI component which renders the fulfillment
   **                            role input component in the attribute region.
   */
  public void setFulfillmentRoleDisplayName(final RichInputText component) {
    this.fulfillmentRoleDisplayName = ComponentReference.newUIComponentReference(component);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getFulfillmentRoleDisplayName
  /**
   ** Returns the UI component which renders the fulfillment role input component
   ** in the attribute region.
   **
   ** @return                    the UI component which renders the fulfillment
   **                            role input component in the attribute region.
   */
  public RichInputText getFulfillmentRoleDisplayName() {
    return (this.fulfillmentRoleDisplayName != null) ? (RichInputText)this.fulfillmentRoleDisplayName.getComponent() : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setFulfillmentUserName
  /**
   ** Sets the UI component which renders the fulfillment user input component in
   ** the attribute region.
   ** <br>
   ** When you store a reference to a UI Component in a bean, the complete
   ** component tree (children and parent) are kept in memory.
   ** This causes the ADF application to consume a unnecessary big amount of
   ** memory.
   ** <b>Note</b>:
   ** <br>
   ** Unfortunately when you use the auto generation feature for component
   ** binding in JDeveloper, it will generates the references as depicted below:
   ** <pre>
   **   public void setFulfillmentUserName(final RichInputText component) {
   **     this.fulfillmentUserName = component;
   **   }
   ** </pre>
   ** This should be changed using the much more memory efficient
   ** {@link ComponentReference} class:
   ** <pre>
   **   private ComponentReference fulfillmentUserName;
   **   ...
   **   public void setFulfillmentUserName(final RichInputText component) {
   **     this.fulfillmentUserName = ComponentReference.newUIComponentReference(component);
   **   }
   ** </pre>
   **
   ** @param  component          the UI component which renders the endpoint
   **                            name input component in the attribute region.
   */
  public void setFulfillmentUserName(final RichInputText component) {
    this.fulfillmentUserName = ComponentReference.newUIComponentReference(component);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getFulfillmentUserName
  /**
   ** Returns the UI component which renders the fulfillment user input component
   ** in the attribute region.
   **
   ** @return                    the UI component which renders the endpoint
   **                            name input component in the attribute region.
   */
  public RichInputText getFulfillmentUserName() {
    return (this.fulfillmentUserName != null) ? (RichInputText)this.fulfillmentUserName.getComponent() : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setFulfillmentUserDisplayName
  /**
   ** Sets the UI component which renders the fulfillment user input component in
   ** the attribute region.
   ** <br>
   ** When you store a reference to a UI Component in a bean, the complete
   ** component tree (children and parent) are kept in memory.
   ** This causes the ADF application to consume a unnecessary big amount of
   ** memory.
   ** <b>Note</b>:
   ** <br>
   ** Unfortunately when you use the auto generation feature for component
   ** binding in JDeveloper, it will generates the references as depicted below:
   ** <pre>
   **   public void setFulfillmentUserDisplayName(final RichInputText component) {
   **     this.fulfillmentUserDisplayName = component;
   **   }
   ** </pre>
   ** This should be changed using the much more memory efficient
   ** {@link ComponentReference} class:
   ** <pre>
   **   private ComponentReference fulfillmentUserDisplayName;
   **   ...
   **   public void setFulfillmentUserDisplayName(final RichInputText component) {
   **     this.fulfillmentUserDisplayName = ComponentReference.newUIComponentReference(component);
   **   }
   ** </pre>
   **
   ** @param  component          the UI component which renders the endpoint
   **                            name input component in the attribute region.
   */
  public void setFulfillmentUserDisplayName(final RichInputText component) {
    this.fulfillmentUserDisplayName = ComponentReference.newUIComponentReference(component);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getFulfillmentUserDisplayName
  /**
   ** Returns the UI component which renders the fulfillment user input component
   ** in the attribute region.
   **
   ** @return                    the UI component which renders the endpoint
   **                            name input component in the attribute region.
   */
  public RichInputText getFulfillmentUserDisplayName() {
    return (this.fulfillmentUserDisplayName != null) ? (RichInputText)this.fulfillmentUserDisplayName.getComponent() : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   changed (AbstractStep)
  /**
   ** Perfoms all actions belonging to the UI to reflect value change events on
   ** particular components in the current page.
   **
   ** @param  event              the {@link ValueChangeEvent} object that
   **                            characterizes the action to perform.
   */
  @Override
  public final void changed(final ValueChangeEvent event) {
    JSF.valueFromExpression("#{pageFlowScope.catalogitemTrain}", Train.class).markDirty();
    partialRenderSubmitRevert();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pickApproverRole
  /**
   ** Raise a <code>Contextual Event</code> programmatically to launch the
   ** taskflow to select a <code>Role</code> to approve a request on the
   ** selected <code>Catalog Item</code>.
   **
   ** @param  event              the {@link ActionEvent} object that
   **                            characterizes the action to perform.
   */
  public void pickApproverRole(@SuppressWarnings("unused") final ActionEvent event) {
    pickRole(CatalogItemAdapter.APPROVER_ROLE, "cat.approverrole.label");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pickApproverUser
  /**
   ** Raise a <code>Contextual Event</code> programmatically to launch the
   ** taskflow to select an <code>Identity</code> to approve a request on the
   ** selected <code>Catalog Item</code>.
   **
   ** @param  event              the {@link ActionEvent} object that
   **                            characterizes the action to perform.
   */
  public void pickApproverUser(@SuppressWarnings("unused") final ActionEvent event) {
    pickIdentity(CatalogItemAdapter.APPROVER_USER, "cat.approveruser.label");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateApproverUser
  /**
   ** Validate manual entered value for entity item <code>approverUser</code>
   **
   ** @param  context            the {@link FacesContext} object that
   **                            characterizes the action to perform.
   ** @param  component          the {@link UIComponent} related to the
   **                            validation to perform.
   ** @param  value              the value end user entered in the UI
   */
  public void validateApproverUser(final FacesContext context, final UIComponent component, final Object value) {
    if ((value != null && value.toString().length() != 0) && !sameValuePresent(value, CatalogItemAdapter.APPROVER_USER_NAME)) {
      partialRenderSubmitRevert();//enableApplyRevertAndNotifyConsumers();
//      checkValueValidity(CatalogItemAdapter.APPROVER_USER_NAME, (String)value, "cat.approverusername.label");
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pickCertifierRole
  /**
   ** Raise a <code>Contextual Event</code> programmatically to launch the
   ** taskflow to select a <code>Role</code> to certify the selected
   ** <code>Catalog Item</code>.
   **
   ** @param  event              the {@link ActionEvent} object that
   **                            characterizes the action to perform.
   */
  public void pickCertifierRole(@SuppressWarnings("unused") final ActionEvent event) {
    pickRole(CatalogItemAdapter.CERTIFIER_ROLE, "cat.certifierrole.label");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pickCertifierUser
  /**
   ** Raise a <code>Contextual Event</code> programmatically to launch the
   ** taskflow to select an <code>Identity</code> to certify the selected
   ** <code>Catalog Item</code>.
   **
   ** @param  event              the {@link ActionEvent} object that
   **                            characterizes the action to perform.
   */
  public void pickCertifierUser(@SuppressWarnings("unused") final ActionEvent event) {
    pickIdentity(CatalogItemAdapter.CERTIFIER_USER, "cat.certifieruser.label");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pickFulfillmentRole
  /**
   ** Raise a <code>Contextual Event</code> programmatically to launch the
   ** taskflow to select a <code>Role</code> to fulfill a request on the
   ** selected <code>Catalog Item</code> if its appropriate.
   **
   ** @param  event              the {@link ActionEvent} object that
   **                            characterizes the action to perform.
   */
  public void pickFulfillmentRole(@SuppressWarnings("unused") final ActionEvent event) {
    pickRole(CatalogItemAdapter.FULFILLMENT_ROLE, "cat.fulfillmentrole.label");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pickFulfillmentUser
  /**
   ** Raise a <code>Contextual Event</code> programmatically to launch the
   ** taskflow to select an <code>Identity</code> to fulfill a request on the
   ** selected <code>Catalog Item</code> if its appropriate.
   **
   ** @param  event              the {@link ActionEvent} object that
   **                            characterizes the action to perform.
   */
  public void pickFulfillmentUser(@SuppressWarnings("unused") final ActionEvent event) {
    pickIdentity(CatalogItemAdapter.FULFILLMENT_USER, "cat.fulfillmentuser.label");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refreshRole
  /**
   ** Refreshing the state of the UI component which displays a role assignment.
   **
   ** @param  attributeName      the name of the attribute the refresh operation
   **                            belongs to.
   */
  public void refreshRole(final String attributeName) {
    if (attributeName != null) {
      if (CatalogItemAdapter.APPROVER_ROLE.equals(attributeName)) {
        ADF.partialRender(getApproverRoleName());
        ADF.partialRender(getApproverRoleDisplayName());
      }
      else if (CatalogItemAdapter.CERTIFIER_ROLE.equals(attributeName)) {
        ADF.partialRender(getCertifierRoleName());
        ADF.partialRender(getCertifierRoleDisplayName());
      }
      else if (CatalogItemAdapter.FULFILLMENT_ROLE.equals(attributeName)) {
        ADF.partialRender(getFulfillmentRoleName());
        ADF.partialRender(getFulfillmentRoleDisplayName());
      }
      changed(null);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refreshUser
  /**
   ** Refreshing the state of the UI component which displays a user assignment.
   **
   ** @param  attributeName      the name of the attribute the refresh operation
   **                            belongs to.
   */
  public void refreshUser(final String attributeName) {
    if (attributeName != null) {
      if (CatalogItemAdapter.APPROVER_USER.equals(attributeName)) {
        ADF.partialRender(getApproverUserName());
        ADF.partialRender(getApproverUserDisplayName());
      }
      else if (CatalogItemAdapter.CERTIFIER_USER.equals(attributeName)) {
        ADF.partialRender(getCertifierUserName());
        ADF.partialRender(getCertifierUserDisplayName());
      }
      else if (CatalogItemAdapter.FULFILLMENT_USER.equals(attributeName)) {
        ADF.partialRender(getFulfillmentUserName());
        ADF.partialRender(getFulfillmentUserDisplayName());
      }
      changed(null);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pickRole
  /**
   ** Raise a <code>Contextual Event</code> programmatically to launch the
   ** taskflow to select a <code>Role</code> to be selected for a certain
   ** property like <code>Approver Role</code> or <code>Certifier Role</code>
   ** for a <code>Catalog Item</code>.
   **
   ** @param  attributeName      the name of the attribute that will receive the
   **                            selected <code>Role</code> value.
   ** @param  title              the resource bundle key to display to extend
   **                            the title information in the called picker task
   **                            flow with the attribute context.
   */
  private void pickRole(String attributeName, String title) {
    // The approach chosen here is a little bit tricky and belongs to the
    // behavior of the state bean bound to the role picker task flow.
    // The state beans evaluates on return which kind of data have to pushed
    // back by verifiying the value of the event distinguisher passed to the
    // role picker task flow.
    // For the selection type single the expected event distinguishers are:
    //  o catalog_role                  {Role Display Name, Role Key}
    //  o select_application_owner_role {Role Display Name, Role Key}
    //  o select_policy_role_edit       {Role Display Name, Role Key}
    //  o select_policy_role_create     {Role Display Name, Role Key}
    // If nothing fits an empty target selection event is returned.
    // Unfortunately we want to display also the unique name of the selected
    // role also in the UI due to the fact that the cardinality of a display
    // name isn't sufficient to provide proper information which role is really
    // assigned in a certain scope.
    // Having this in mind extra effort is required in the event handler to
    // populate the data of a role.
    final String              taskId    = String.format("cat#%s", ADF.pageFlowScopeStringValue(CatalogItemAdapter.PK));
    final Map<String, Object> parameter = new HashMap<String, Object>();
    // it's required to distinct the taskflow of the picker to give the event
    // handler the ability to bypass taskflows which didn't raise the event due
    // to that the event handler is registered for every region and there can be
    // more than one taskflow on the same page
    parameter.put(EVENT_DISTINGUISHER, PICK_ROLE_SCOPE);
    parameter.put("attributeName",     attributeName);
    parameter.put(EVENT_SELECTIONTYPE, SELECTIONTYPE_SINGLE);
    this.raiseTaskFlowLaunchEvent(taskId, PICK_ROLE_FLOW, ADF.resourceBundleValue(BUNDLE, "SEARCH_SELECT", ADF.resourceBundleValue(AUTHORIZATION, title)), "/images/add_ena.png", null, null, true, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pickIdentity
  /**
   ** Raise a <code>Contextual Event</code> programmatically to launch the
   ** taskflow to select an <code>Identity</code> to be selected for a certain
   ** property like <code>Approver User</code> or <code>Certifier User</code>
   ** for a <code>Catalog Item</code>.
   **
   ** @param  attributeName      the name of the attribute that will receive the
   **                            selected <code>Identity</code> value.
   ** @param  title              the resource bundle key to display to extend
   **                            the title information in the called picker task
   **                            flow with the attribute context.
   */
  private void pickIdentity(final String attributeName, final String title) {
    // The approach chosen here is a little bit tricky and belongs to the
    // behavior of the state bean bound to the user picker task flow.
    // The state beans evaluates on return which kind of data have to pushed
    // back by verifiying the value of the event distinguisher passed to the
    // user picker task flow.
    // For the selection type single the expected event distinguishers and the
    // positioned return values in the list are:
    //  o pick_certifier_user     {UserAdapterBean}
    //  o select_proxy_user       {User Login, Display Name}
    //  o select_user_manager     {usr_key, , Display Name}
    //  o select_admin_role_owner {UserAdapterBean}
    //  o catalog_user            {attributeName, Display Name,usr_key}
    //  o generic_pick_user_event {UserAdapterBean}
    // If nothing of this fits an empty target selection event is returned.
    // Unfortunately we want to display also the login name of the selected
    // user in the UI due to the fact that the cardinality of a display name
    // isn't sufficient to provide proper information which role is really
    // assigned in a certain scope.
    // Having this in mind extra effort is required in the event handler to
    // populate the data of a role.
    final String              taskId    = String.format("cat#%s", ADF.pageFlowScopeStringValue(CatalogItemAdapter.PK));
    final Map<String, Object> parameter = new HashMap<String, Object>();
    // it's required to distinct the taskflow of the picker to give the event
    // handler the ability to bypass taskflows which didn't raise the event due
    // to that the event handler is registered for every region and there can be
    // more than one taskflow on the same page
    parameter.put(EVENT_DISTINGUISHER, PICK_USER_SCOPE);
    parameter.put("attributeName",     attributeName);
    parameter.put(EVENT_SELECTIONTYPE, SELECTIONTYPE_SINGLE);
    this.raiseTaskFlowLaunchEvent(taskId, PICK_USER_FLOW, ADF.resourceBundleValue(BUNDLE, "SEARCH_SELECT", ADF.resourceBundleValue(AUTHORIZATION, title)), "/images/add_ena.png", null, null, true, parameter);
  }

  private boolean sameValuePresent(final Object newValue, final String attributeName) {
    final Object oldValue = ADF.attributeBindingValue(attributeName);
    return (newValue == oldValue || newValue != null && newValue.equals(oldValue));
  }
}
