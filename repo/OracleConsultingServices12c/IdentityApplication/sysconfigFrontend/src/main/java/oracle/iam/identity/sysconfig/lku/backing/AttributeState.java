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
    Subsystem   :   System Configuration Management

    File        :   AttributeState.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AttributeState.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2016-03-03  DSteding    First release version
*/

package oracle.iam.identity.sysconfig.lku.backing;

import javax.faces.component.UIComponent;

import javax.faces.event.ValueChangeEvent;

import org.apache.myfaces.trinidad.util.ComponentReference;

import oracle.adf.view.rich.component.rich.output.RichOutputText;

import oracle.adf.view.rich.component.rich.input.RichInputText;

import oracle.hst.foundation.faces.JSF;
import oracle.hst.foundation.faces.ADF;

import oracle.iam.identity.sysconfig.schema.LookupAdapter;

import oracle.iam.identity.frontend.train.AbstractStep;

import oracle.iam.identity.sysconfig.lku.state.Train;

////////////////////////////////////////////////////////////////////////////////
// class AttributeState
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** Declares methods the user interface service provides to manage attribute
 ** values of <code>Lookup Definition</code>s.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
 */
public class AttributeState extends AbstractStep {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String           DETAIL_ITERATOR  = "LookupIterator";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:8980857747691004451")
  private static final long            serialVersionUID = 2679713448855672872L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private transient ComponentReference lookupName;

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
  // Methode:   setLookupName
  /**
   ** Sets the UI component which renders the input component belonging to the
   ** lookup name.
   **
   ** @param  component          the UI component which renders the input
   **                            component belonging to the lookup name.
   **                            Allowed object is {@link RichInputText}.
   */
  public void setLookupName(final RichInputText component) {
    this.lookupName = ComponentReference.newUIComponentReference(component);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getLookupName
  /**
   ** Returns the UI component which renders the input component belonging to
   ** the lookup name.
   **
   ** @return                    the UI component which renders the input
   **                            component belonging to the lookup name.
   **                            Possible object is {@link RichInputText}.
   */
  public RichInputText getLookupName() {
    return (this.lookupName != null) ? (RichInputText)this.lookupName.getComponent() : null;
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
    JSF.valueFromExpression("#{pageFlowScope.lookupTrain}", Train.class).markDirty();
    partialRenderSubmitRevert();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refresh
  /**
   ** Refreshs the attribute region of the <code>lookup</code>.
   */
  private void refresh() {
    // if Lookup name changed, update tab name and title
    final String name  = ADF.iteratorBindingValue(DETAIL_ITERATOR, LookupAdapter.NAME, String.class);
    // tab title outputtext id
    final UIComponent component = JSF.find("dc_ot1");
    if (component != null && name != null && name.length() != 0) {
      final RichOutputText ot1 = (RichOutputText)component;
      ot1.setValue(name);
      ADF.partialRender(ot1);
    }
    refreshTaskFlow(ADF.pageFlowScopeStringValue(PARAMETER_TASKFLOW), name, null, null);
  }
}