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

import java.util.Map;
import java.util.List;
import java.util.HashMap;

import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;

import javax.faces.event.ActionEvent;

import javax.faces.event.ValueChangeEvent;

import oracle.adf.view.rich.event.DialogEvent;

import oracle.adf.view.rich.util.ResetUtils;

import oracle.hst.foundation.faces.JSF;
import oracle.hst.foundation.faces.ADF;

import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.identity.frontend.train.AbstractSearch;

import oracle.iam.identity.sysconfig.lku.state.Train;

import oracle.iam.identity.sysconfig.schema.LookupAdapter;
import oracle.iam.identity.sysconfig.schema.LookupValueAdapter;

////////////////////////////////////////////////////////////////////////////////
// class AttributeState
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** Declares methods the user interface service provides to manage
 ** <code>Lookup Definition</code> values.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
 */
public class ValueState extends AbstractSearch {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String BUNDLE           = "oracle.iam.identity.bundle.Configuration";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:6429452658984497483")
  private static final long   serialVersionUID = 7202450626353603114L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private boolean              displayInfo;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ValueState</code> backing bean that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ValueState() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDisplayInfo
  /**
   ** Sets the value of the displayInfo property.
   ** <p>
   ** Method needs to be declared as <b><code>public</code></b> because it used
   ** in the UI.
   **
   ** @param  value              the value of the displayInfo property.
   **                            Allowed object is <code>boolean</code>.
   */
  public void setDisplayInfo(final boolean value) {
    this.displayInfo = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isDisplayInfo
  /**
   ** Returns the value of the displayInfo property.
   ** <p>
   ** Method needs to be declared as <b><code>public</code></b> because it used
   ** in the UI.
   **
   ** @return                    the value of the displayInfo.
   **                            Possible object is <code>boolean</code>.
   */
  public boolean isDisplayInfo() {
    /**
    final String mode  = ADF.pageFlowScopeStringValue(PARAMETER_MODE);
    final Train  train = (Train)getTrain();
    if (MODE_VIEW.equals(mode) || MODE_EDIT.equals(mode) || (!CollectionUtility.empty(train.valueAssign())))
      this.displayInfo = false;
    else
      this.displayInfo = true;
    return this.displayInfo;
    */
    final String mode = ADF.pageFlowScopeStringValue(PARAMETER_MODE);
    if (MODE_VIEW.equals(mode) || MODE_EDIT.equals(mode) || (MODE_CREATE.equals(mode) && iteratorHasRows("LookupValueIterator")))
      this.displayInfo = false;
    else
      this.displayInfo = true;
    return this.displayInfo;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   isRevokeDisabled
  /**
   ** Whether the revoke button of a Lookup Definition/Role relationship is
   ** disabled.
   **
   ** @return                    <code>true</code> revoke button of a
   **                            relationship is disabled; otherwise
   **                            <code>false</code>.
   */
  public boolean isRevokeDisabled() {
    return !(getSelectedRowCount() > 0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getLocalizedMessage
  /**
   ** Returns the localized action message.
   **
   ** @return                    the localized action message.
   */
  public String getLocalizedMessage() {
    String localized = train().getActionName();
    if (StringUtility.isEmpty(localized))
      return "???-emptyornull-???";

    switch (localized) {
      case GENERIC_DELETE : return ADF.resourceBundleValue(BUNDLE, "LKV_DELETE_CONFIRM");
      case GENERIC_UNDO   : return ADF.resourceBundleValue(BUNDLE, "LKV_UNDO_CONFIRM");
      default             : return String.format("???-%s-???", localized);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dialogListener
  /**
   ** Perfoms all actions belonging to the UI to reflect action events belonging
   ** to entries in the value table after confirmation.
   **
   ** @param  event              the {@link DialogEvent} object that
   **                            characterizes the action to perform.
   */
  public void dialogListener(final DialogEvent event) {
    if (DialogEvent.Outcome.yes.equals(event.getOutcome())) {
      final String    actionName = train().getActionName();
      final List<Row> selection  = selectedRow(getSearchTable());
      switch(actionName) {
        case GENERIC_DELETE : remove(selection);
                                break;
        case GENERIC_UNDO   : undo(selection);
                                break;
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   undoListener
  /**
   ** Perfoms all actions belonging to the UI to reflect action events to
   ** undo entries from the value table.
   **
   ** @param  event              the {@link ActionEvent} object that
   **                            characterizes the action to perform.
   */
  public void undoListener(final @SuppressWarnings("unused") ActionEvent event) {
    final List<Row> selection = selectedRow(getSearchTable());
    undo(selection);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeListener
  /**
   ** Perfoms all actions belonging to the UI to reflect action events to
   ** remove entries from the value table.
   **
   ** @param  event              the {@link ActionEvent} object that
   **                            characterizes the action to perform.
   */
  public void removeListener(final @SuppressWarnings("unused") ActionEvent event) {
    final List<Row> selection = selectedRow(getSearchTable());
    remove(selection);
  }

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
    final List<Row> selection = selectedRow(getSearchTable());
    modify(selection);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revert (overridden)
  /**
   ** Reverts all changes belonging to the value region of the
   ** <code>Lookup Definition</code>.
   */
  @Override
  public void revert() {
    // final Train train = (Train)getTrain();
    // train.clearValue();
    // need to explicitly reset lookup value table input components
    // this is because Revert button on that page is immediate hence component
    // submittedValues need to be reset explicitly
    if (getSearchTable() != null)
      ResetUtils.reset(getSearchTable());
    // ensure inheritance
    super.revert();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createListener
  /**
   ** Create a value belonging to a certain <code>Lookup Definition</code>.
   **
   ** @param  event              the {@link ActionEvent} object that
   **                            characterizes the action to perform.
   */
  public void createListener(final ActionEvent event) {
    final Train               train = train();
    final LookupValueAdapter  mab   = new LookupValueAdapter(ADF.pageFlowScopeStringValue(LookupAdapter.PK), String.valueOf(train.valueSequence.next()));

    // keep the row created above in the cache so that the row survives the next
    // refresh request
    final Map<String, Object> assign = train.valueAssign();
    assign.put(mab.getValueKey(), mab);
    refreshListener(event);

    train.markDirty();
    ADF.partialRender(getSearchTable());
    partialRenderAction();
    partialRenderSubmitRevert();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refreshListener (overridden)
  /**
   ** Refresh the value relationships belonging to a certain
   ** <code>Lookup Definition</code>.
   **
   ** @param  event              the {@link ActionEvent} object that
   **                            characterizes the action to perform.
   */
  @Override
  public void refreshListener(final @SuppressWarnings("unused") ActionEvent event) {
    refresh();
    // ensure inheritance
    super.refreshListener(event);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   modify
  /**
   ** Add the given targets to the collection of modified values.
   **
   ** @param  selection          the collection of values to be modified.
   */
  public void modify(final List<Row> selection) {
    if (selection != null && !selection.isEmpty()) {
      final Train               train   = train();
      boolean                   touched = false;
      final Map<String, Object> modify  = train.valueModify();

      // access the name of the iterator "LookupValueIterator" the table is
      // bound to.
      final RowSetIterator rsi = rowSetIterator("LookupValueIterator");
      // latch the current position of the iterator to restore it later
      final int            old = rsi.getCurrentRowIndex();
      for (Row cursor : selection) {
        rsi.setCurrentRow(cursor);
        final String action = (String)cursor.getAttribute(LookupValueAdapter.ACTION);
        if (StringUtility.isEmpty(action)) {
          cursor.setAttribute(LookupValueAdapter.ACTION, LookupValueAdapter.MOD);
          final LookupValueAdapter value = new LookupValueAdapter(cursor);
          modify.put(value.getValueKey(), value);
          touched = true;
        }
      }
      // restore the current position of the row iterator
      rsi.setCurrentRowAtRangeIndex(old);
      if (touched) {
        // do not use resfreshRegion because it will reset the table selection
        // also
        // refreshRegion();
        train.markDirty();
        ADF.partialRender(getSearchTable());
        partialRenderAction();
        partialRenderSubmitRevert();
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   remove
  /**
   ** Removes the given targets from the collection of values which triggers
   ** this <code>Lookup Definition</code>.
   **
   ** @param  selection          the collection of values to be removed.
   */
  public void remove(final List<Row> selection) {
    if (selection != null && !selection.isEmpty()) {
      final Train               train   = train();
      boolean                   touched = false;
      final Map<String, Object> assign = train.valueAssign();
      final Map<String, Object> remove = train.valueRemove();
      for (Row cursor : selection) {
        final String action = (String)cursor.getAttribute(LookupValueAdapter.ACTION);
        if (StringUtility.isEmpty(action) || LookupValueAdapter.MOD.equals(action)) {
          cursor.setAttribute(LookupValueAdapter.ACTION, LookupValueAdapter.DEL);
          final LookupValueAdapter value = new LookupValueAdapter(cursor);
          remove.put(value.getValueKey(), value);
          touched = true;
        }
        else if (LookupValueAdapter.ADD.equals(action)) {
          cursor.remove();
          assign.remove(cursor.getAttribute(LookupValueAdapter.VALUE_KEY));
          touched = true;
        }
      }
      // restore the current position of the row iterator
      if (touched) {
        // do not use resfreshRegion because it will reset the table selection
        // also
        // refreshRegion();
        train.markDirty();
        ADF.partialRender(getSearchTable());
        partialRenderAction();
        partialRenderSubmitRevert();
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   undo
  /**
   ** Removes the given targets from the collection of values to be triggering
   ** the <code>Lookup Definition</code>
   **
   ** @param  selection          the collection of values to be removed.
   */
  public void undo(final List<Row> selection) {
    if (!CollectionUtility.empty(selection)) {
      final Train               train  = train();
      final Map<String, Object> assign = train.valueAssign();
      final Map<String, Object> modify = train.valueModify();
      final Map<String, Object> remove = train.valueRemove();
      for (Row cursor : selection) {
        // no need to cast
        final Object value = cursor.getAttribute(LookupValueAdapter.VALUE_KEY);
        if (assign.containsKey(value)) {
          assign.remove(value);
        }
        if (modify.containsKey(value)) {
          modify.remove(value);
        }
        if (remove.containsKey(value)) {
          remove.remove(value);
        }
      }
      ResetUtils.reset(getSearchTable());
      refreshRegion();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refresh
  /**
   ** Refreshs the value region of the <code>Lookup Definition</code>.
   */
  private void refresh() {
    final Train                            train   = train();
    final Map<String, Map<String, Object>> pending = new HashMap<String, Map<String, Object>>();

    pending.put(LookupValueAdapter.ADD, train.valueAssign());
    pending.put(LookupValueAdapter.MOD, train.valueModify());
    pending.put(LookupValueAdapter.DEL, train.valueRemove());


    final Map<String, Object> parameter = new HashMap<String, Object>();
    parameter.put("identifier", ADF.pageFlowScopeStringValue(LookupAdapter.PK));
    parameter.put("pending",    pending);
    ADF.executeOperation("requeryValue", parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   train
  /**
   ** Returns the {@link Train} bound to the task flow in the PageFlow scope.
   */
  private Train train() {
    return JSF.valueFromExpression("#{pageFlowScope.lookupTrain}", Train.class);
  }
}