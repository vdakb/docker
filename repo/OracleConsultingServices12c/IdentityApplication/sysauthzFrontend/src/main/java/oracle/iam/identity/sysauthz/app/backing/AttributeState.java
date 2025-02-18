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

package oracle.iam.identity.sysauthz.app.backing;

import javax.faces.component.UIComponent;

import javax.faces.event.ValueChangeEvent;

import org.apache.myfaces.trinidad.util.ComponentReference;

import oracle.adf.view.rich.component.rich.output.RichOutputText;

import oracle.adf.view.rich.component.rich.input.RichInputListOfValues;

import oracle.hst.foundation.faces.JSF;
import oracle.hst.foundation.faces.ADF;

import oracle.iam.identity.sysauthz.schema.ApplicationInstanceAdapter;

import oracle.iam.identity.frontend.train.AbstractStep;

import oracle.iam.identity.sysauthz.app.state.Train;

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

  public static final String           DETAIL_ITERATOR  = "ApplicationInstanceIterator";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-1853638197855404969")
  private static final long            serialVersionUID = -8698772828944378646L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private transient ComponentReference objectName;
  private transient ComponentReference endpointName;

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
  // Methode:   setObjectName
  /**
   ** Sets the UI component which renders the object name input component in the
   ** attribute region.
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
   **   public void setObjectName(final RichInputListOfValues component) {
   **     this.objectName = component;
   **   }
   ** </pre>
   ** This should be changed using the much more memory efficient
   ** {@link ComponentReference} class:
   ** <pre>
   **   private ComponentReference objectName;
   **   ...
   **   public void setObjectName(final RichInputListOfValues component) {
   **     this.objectName = ComponentReference.newUIComponentReference(component);
   **   }
   ** </pre>
   **
   ** @param  component          the UI component which renders the object name
   **                            input component in the attribute region.
   */
  public void setObjectName(final RichInputListOfValues component) {
    this.objectName = ComponentReference.newUIComponentReference(component);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getObjectName
  /**
   ** Returns the UI component which renders the object name input component in
   ** the attribute region.
   **
   ** @return                    the UI component which renders the object name
   **                            input component in the attribute region.
   */
  public RichInputListOfValues getObjectName() {
    return (this.objectName != null) ? (RichInputListOfValues)this.objectName.getComponent() : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setEndpointName
  /**
   ** Sets the UI component which renders the endpoint name input component in
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
   **   public void setEndpointName(final RichInputListOfValues component) {
   **     this.endpointName = component;
   **   }
   ** </pre>
   ** This should be changed using the much more memory efficient
   ** {@link ComponentReference} class:
   ** <pre>
   **   private ComponentReference objectName;
   **   ...
   **   public void setEndpointName(final RichInputListOfValues component) {
   **     this.endpointName = ComponentReference.newUIComponentReference(component);
   **   }
   ** </pre>
   **
   ** @param  component          the UI component which renders the endpoint
   **                            name input component in the attribute region.
   */
  public void setEndpointName(final RichInputListOfValues component) {
    this.endpointName = ComponentReference.newUIComponentReference(component);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getEndpointName
  /**
   ** Returns the UI component which renders the endpoint name input component
   ** in the attribute region.
   **
   ** @return                    the UI component which renders the endpoint
   **                            name input component in the attribute region.
   */
  public RichInputListOfValues getEndpointName() {
    return (this.endpointName != null) ? (RichInputListOfValues)this.endpointName.getComponent() : null;
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
    JSF.valueFromExpression("#{pageFlowScope.applicationTrain}", Train.class).markDirty();
    partialRenderSubmitRevert();
  }
  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refresh
  /**
   ** Refreshs the attribute region of the <code>ApplicationInstance</code>.
   */
  private void refresh() {
    // if enticy name changed, update tab name and title
    final String name  = ADF.iteratorBindingValue(DETAIL_ITERATOR, ApplicationInstanceAdapter.PK, String.class);
    // tab title outputtext id
    final UIComponent component = JSF.find("dc_ot1");
    if (component != null && name != null && name.length() != 0) {
      RichOutputText ot1 = (RichOutputText)component;
      ot1.setValue(name);
      ADF.partialRender(ot1);
    }
    refreshTaskFlow(ADF.pageFlowScopeStringValue(PARAMETER_TASKFLOW), name, null, null);
  }
}