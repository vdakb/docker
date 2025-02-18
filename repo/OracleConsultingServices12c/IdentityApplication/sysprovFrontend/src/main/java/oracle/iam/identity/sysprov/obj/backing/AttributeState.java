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
    Subsystem   :   System Provisioning Management

    File        :   AttributeState.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AttributeState.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2016-03-03  DSteding    First release version
*/

package oracle.iam.identity.sysprov.obj.backing;

import java.util.EnumSet;

import javax.faces.event.ValueChangeEvent;

import javax.faces.component.UIComponent;

import oracle.adf.view.rich.component.rich.output.RichOutputText;

import oracle.adf.view.rich.component.rich.input.RichSelectOneRadio;

import oracle.hst.foundation.faces.JSF;
import oracle.hst.foundation.faces.ADF;

import oracle.iam.identity.sysprov.schema.ResourceObjectAdapter;

import oracle.iam.identity.frontend.train.AbstractStep;

import oracle.iam.identity.sysprov.obj.state.Train;

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

  public static final String            DETAIL_ITERATOR  = "ResourceObjectIterator";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-5264025221550595037")
  private static final long             serialVersionUID = 7402232037270851355L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private transient RichSelectOneRadio orderForSwitcher;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  public enum OrderFor {
      USER("U")
    , ORGANIZATION("O")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version
    // we're compatible with
    @SuppressWarnings("compatibility:-3083889115511685009")
    private static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final String       value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a empty <code>OrderFor</code> that allows use as a JavaBean.
     **
     ** @param  value            the value.
     */
    OrderFor(final String value) {
      // initialize instance attributes
      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Returns the value of the value property.
     **
     ** @return                  the value of the value property.
     **                          Possible object is {@link String}.
     */
    public String value() {
      return this.value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: fromValue
    /**
     ** Factory method to create a proper OwnerType from the given string value.
     **
     ** @param  value            the string value the severity should be
     **                          returned for.
     **
     ** @return                  the owner type.
     */
    public static OrderFor fromValue(final String value) {
      for (OrderFor cursor : OrderFor.values()) {
        if (cursor.value.equals(value))
          return cursor;
      }
      throw new IllegalArgumentException(value);
    }
  };

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AttributeState</code> backing bean that
   ** allows use as a JavaBean.
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
  // Methode:   setOrderForSwitcher
  /**
   ** Sets the UI component which renders the orderable for selection component.
   **
   ** @param  component          the UI component which renders the orderable
   **                            for selection component.
   */
  public void setOrderForSwitcher(final RichSelectOneRadio component) {
    this.orderForSwitcher = component;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getOrderForSwitcher
  /**
   ** Returns the UI component which renders the orderable for selection
   ** component.
   **
   ** @return                    the UI component which renders the orderable
   **                            for selection component.
   */
  public RichSelectOneRadio getOrderForSwitcher() {
    return this.orderForSwitcher;
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
    JSF.valueFromExpression("#{pageFlowScope.resourceTrain}", Train.class).markDirty();
    partialRenderSubmitRevert();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orderForValueChanged
  /**
   ** Perfoms all actions belonging to the UI to reflect value change events on
   ** the orderable type components in the attribute region.
   **
   ** @param  event              the {@link ValueChangeEvent} object that
   **                            characterizes the action to perform.
   */
  public void orderForValueChanged(final ValueChangeEvent event) {
    // first delegate any action further
    changed(event);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refresh
  /**
   ** Refreshs the attribute region of the <code>ResourceObject</code>.
   */
  private void refresh() {
    // if enticy name changed, update tab name and title
    final String name  = ADF.iteratorBindingValue(DETAIL_ITERATOR, ResourceObjectAdapter.PK, String.class);
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