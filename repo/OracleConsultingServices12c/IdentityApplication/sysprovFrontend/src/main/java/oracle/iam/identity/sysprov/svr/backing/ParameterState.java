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

    File        :   ParameterState.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ParameterState.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2016-03-03  DSteding    First release version
*/

package oracle.iam.identity.sysprov.svr.backing;

import java.util.Map;
import java.util.List;
import java.util.HashMap;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;

import oracle.adf.view.rich.event.DialogEvent;

import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.CollectionUtility;

import oracle.hst.foundation.faces.JSF;
import oracle.hst.foundation.faces.ADF;

import oracle.iam.identity.frontend.train.AbstractSearch;

import oracle.iam.identity.sysprov.schema.EndpointAdapter;
import oracle.iam.identity.sysprov.schema.EndpointParameterAdapter;

import oracle.iam.identity.sysprov.svr.state.Train;

////////////////////////////////////////////////////////////////////////////////
// class ParameterState
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** Declares methods the user interface service provides to manage the execution
 ** parameter of <code>IT Resource</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
 */
public class ParameterState extends AbstractSearch {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String BUNDLE           = "oracle.iam.identity.bundle.Provisioning";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:7807243016365160403")
  private static final long   serialVersionUID = -4485149503262220317L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ParameterState</code> backing bean that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ParameterState() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   isRevokeDisabled
  /**
   ** Whether the delete button of an IT Resource relationship is disabled.
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
      case GENERIC_DELETE : return ADF.resourceBundleValue(BUNDLE, "SVR_PARAMETER_DELETE_CONFIRM");
      case GENERIC_UNDO   : return ADF.resourceBundleValue(BUNDLE, "SVR_PARAMETER_UNDO_CONFIRM");
      default             : return String.format("???-%s-???", localized);
    }
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
    final List<Row> selection = selectedRow(getSearchTable());
    if (selection != null && !selection.isEmpty()) {
      final Map<Long, EndpointParameterAdapter> modify  = train().parameterModify();
      // access the name of the iterator "ParameterIterator" the table is
      // bound to.
      final RowSetIterator rsi = rowSetIterator("ParameterIterator");
      // latch the current position of the iterator to restore it later
      final int            old = rsi.getCurrentRowIndex();
      for (Row cursor : selection) {
        rsi.setCurrentRow(cursor);
        final EndpointParameterAdapter value = new EndpointParameterAdapter(cursor);
        value.setValue((String)event.getNewValue());
        value.setPendingAction(EndpointParameterAdapter.MOD);
        modify.put(value.getParameterKey(), value);
      }
      // restore the current position of the row iterator
      rsi.setCurrentRowAtRangeIndex(old);
      train().markDirty();
      refresh();
      refreshRegion();
    }
  }

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
      if (GENERIC_UNDO.equals(train().getActionName())) {
        undo(selectedRow(getSearchTable()));
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revert (overridden)
  /**
   ** Reverts all changes belonging to the parameter region of the
   ** <code>IT Resource</code>.
   */
  @Override
  public void revert() {
    train().clearParameter();
    refresh();
    refreshRegion();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refreshListener (overridden)
  /**
   ** Refresh the history belonging to a certain <code>Scheduled Endpoint</code>.
   **
   ** @param  event              the {@link ActionEvent} object that
   **                            characterizes the action to perform.
   */
  @Override
  public void refreshListener(final ActionEvent event) {
    refresh();
    // ensure inheritance
    super.refreshListener(event);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   undo
  /**
   ** Removes the given targets from the collection of values to be triggering
   ** the <code>IT Resource</code>
   **
   ** @param  selection          the collection of values to be removed.
   */
  public void undo(final List<Row> selection) {
    if (!CollectionUtility.empty(selection)) {
      boolean                                   touched = false;
      final Map<Long, EndpointParameterAdapter> modify  = train().parameterModify();
      // access the name of the iterator "ParameterIterator" the table is
      // bound to.
      final RowSetIterator rsi = rowSetIterator("ParameterIterator");
      // latch the current position of the iterator to restore it later
      final int            old = rsi.getCurrentRowIndex();
      for (Row cursor : selection) {
        // no need to cast
        final Long parameter = (Long)cursor.getAttribute(EndpointParameterAdapter.SVD_PK);
        if (modify.containsKey(parameter)) {
          modify.remove(parameter);
          touched = true;
        }
      }
      if (touched) {
        refresh();
        refreshRegion();
        // restore the current position of the row iterator
        rsi.setCurrentRowAtRangeIndex(old);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refresh
  /**
   ** Refreshs the parameter region of the <code>IT Resource</code>.
   */
  private void refresh() {
    final Map<String, Map<Long, EndpointParameterAdapter>> pending = new HashMap<String, Map<Long, EndpointParameterAdapter>>();
    pending.put(EndpointParameterAdapter.MOD, train().parameterModify());

    final Map<String, Object> parameter = new HashMap<String, Object>();
    // looks like regardless which class type is defined on the task flow input
    // parameter definition ADF stores any values always as of type String
    parameter.put("identifier", Long.valueOf(ADF.pageFlowScopeStringValue(EndpointAdapter.PK)));
    parameter.put("pending",    pending);
    ADF.executeOperation("requeryParameter", parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   train
  /**
   ** Returns the {@link Train} bound to the task flow in the PageFlow scope.
   */
  private Train train() {
    return JSF.valueFromExpression("#{pageFlowScope.endpointTrain}", Train.class);
  }
}