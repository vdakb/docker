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

    System      :   Employee Self Service Portal
    Subsystem   :   Vehicle Administration

    File        :   Train.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@oracle.com

    Purpose     :   This file implements the class
                    Train.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2020-05-28   SBernet     First release version
*/

package bka.employee.portal.vehicle.vhc.state;

import bka.employee.portal.train.state.AbstractBean;
import bka.employee.portal.vehicle.vhc.backing.AttributeState;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.faces.event.ActionEvent;

import oracle.adf.view.rich.component.rich.layout.RichPanelFormLayout;
import oracle.adf.view.rich.util.ResetUtils;

import oracle.hst.foundation.faces.ADF;
import oracle.hst.foundation.faces.JSF;

import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;

////////////////////////////////////////////////////////////////////////////////
// class Train
// ~~~~~ ~~~~~
/**
 ** Declares methods the user interface service provides to navigate across
 ** pages to manage <code>Vehicule Color</code>.
 **
 ** @author  sylvert.bernet@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Train extends AbstractBean {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String BUNDLE           = "bka.employee.portal.vehicle.Frontend";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-3511428525813595197")
  private static final long serialVersionUID = -9036660308466859804L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////
  private Map<String, Object> scopeModify      = new LinkedHashMap<String, Object>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Train</code> backing bean that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Train() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   scopeModify
  /**
   ** Returns the collection of parameters to be modified.
   **
   ** @return                    the collection of parameters to be modified.
   */
  public Map<String, Object> scopeModify() {
    return this.scopeModify;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   attributeState
  /**
   ** Returns the backing bean of the attribute region.
   **
   ** @return                    the backing bean of the attribute region.
   */
  private AttributeState attributeState() {
    try {
      return JSF.valueFromExpression("#{backingBeanScope.colorAttribute}", AttributeState.class);
    }
    // for debugging purpose only catch everything to spool to error log
    catch (Throwable t) {
      t.printStackTrace(System.err);
      throw t;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetch
  /**
   ** Initialize the train flow to manage an existing
   ** <code>Vehicule Color</code>.
   ** <br>
   ** Leverage the operational binding <code>colorFetch</code> to do so.
   */
  public void fetch() {
    ADF.executeOperation("colorFetch");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   submit
  /**
   ** Create or updates the <code>Vehicule Color</code>, open a newly
   ** created <code>Vehicule Color</code> in the edit mode and close
   ** the current task flow.
   ** <br>
   ** Leverage the operational binding <code>commit</code> to do so.
   ** The access to the binding layer assumes that the actions are trigger by
   ** a UI component of the embedded pages which comprise the train task flow.
   **
   ** @return                    the outcome of the operation to support
   **                            control rule navigation in the task flow.
   */
  public String submit() {
    final Map<String, Object> parameter = new HashMap<String, Object>() {{
      put("add", scopeModify);
    }};
    // commit the changes to the persistence layer which will now do what's
    // needed to create or update the Vehicule Color

    final Object result = ADF.executeAction(SUBMIT, parameter);
    if (ADF.executeActionSucceeded(result)) {
      final RowSetIterator rowSet = rowSetIterator("ColorIterator");
      final Row    row   = rowSet.getCurrentRow();
      final String name  = (String)row.getAttribute("meaning");
      final Map    scope = ADF.current().getPageFlowScope();
      final String flow  = (String)scope.get(PARAMETER_TASKFLOW);
      // Create mode not used, remove if not used
      if (MODE_CREATE.equals(scope.get(PARAMETER_MODE))) {
        raiseTaskFlowFeedbackEvent(String.format(ADF.resourceBundleValue(BUNDLE, "VHC_TRAIN_CREATE"), name));
        raiseTaskFlowRemoveEvent(flow);
      }
      else {
        markClean();
        raiseTaskFlowFeedbackEvent(String.format(ADF.resourceBundleValue(BUNDLE, "VHC_TRAIN_MODIFY"), name));
        refreshTaskFlow(flow, name, null, null);
      }
      return SUCCESS;
    }
    else
      return ERROR;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revert
  /**
   ** Revert all changes made on train flow belonging to an
   ** <code>Vehicule Color</code>.
   ** <br>
   ** Leverage the operational binding <code>rollback</code> to do so.
   **
   ** @param  event              the {@link ActionEvent} object that
   **                            characterizes the action to perform.
   */
  public void revert(final @SuppressWarnings("unused") ActionEvent event) {
    // rollback any transaction
    ADF.executeAction(REVERT);
    revertAttribute();
    markClean();

    // if Color name changed, update tab name and title
    final String name  = ADF.iteratorBindingValue(AttributeState.DETAIL_ITERATOR, "meaning", String.class);
    raiseTaskFlowFeedbackEvent(String.format(ADF.resourceBundleValue(BUNDLE, "VMB_TRAIN_REVERT"), name));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revertAttribute
  /**
   ** Reverts all changes belonging to the attribute region of the
   ** <code>Vehicule Color</code>.
   */
  public void revertAttribute() {
    ADF.executeOperation("refreshColorAttribute");
    // need to explicitly reset color attributes forms this
    // is because Revert button on that page is immediate hence component
    // submittedValues need to be reset explicitly
    final RichPanelFormLayout form = getFormLayout();
    if (form != null) {
      ResetUtils.reset(form);
      ADF.partialRender(form);
      attributeState().partialRenderSubmitRevert();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clearScope
  /**
   ** Reverts all changes belonging to the publication region of the
   ** <code>Vehicule Color</code>.
   */
  public void clearScope() {
    this.scopeModify.clear();
  }
}