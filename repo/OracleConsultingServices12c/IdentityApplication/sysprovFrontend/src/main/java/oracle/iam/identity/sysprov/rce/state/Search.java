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

    File        :   Search.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Search.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2016-03-03  DSteding    First release version
*/

package oracle.iam.identity.sysprov.rce.state;

import java.util.Map;
import java.util.List;
import java.util.HashMap;

import oracle.jbo.Row;

import oracle.iam.ui.platform.model.config.ConstantsDefinition;

import oracle.iam.identity.frontend.AbstractSearchState;

import oracle.iam.identity.sysprov.schema.ReconciliationEventStatus;
import oracle.iam.identity.sysprov.schema.ReconciliationEventAdapter;

////////////////////////////////////////////////////////////////////////////////
// class Search
// ~~~~~ ~~~~~~
/**
 ** Declares methods the user interface service provides to search for
 ** <code>Reconcilaition Event</code>s.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
 */
public class Search extends AbstractSearchState {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:2480677993821619090")
  private static final long serialVersionUID = 1310462966517188625L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Search</code> backing bean that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Search() {
    // ensure inheritance
    super(ReconciliationEventAdapter.PK, ReconciliationEventAdapter.STATUS);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   isCloseDisabled
  /**
   ** Whether the selected row (aka Reconciliation Event) can be closed or not.
   ** <p>
   ** Closing of a <code>Reconciliation Event</code> depends on the status of
   ** a particular Reconciliation Event.
   ** <br>
   ** A <code>Reconciliation Event</code> can only be closed if the status is
   ** something else as success.
   **
   ** @return                    <code>true</code> if the
   **                            <code>Reconciliation Event</code> can not be
   **                            closed..
   */
  public boolean isCloseDisabled() {
    final List<Row> selection = selectedRow(getSearchTable());
    if (selection.size() == 0)
      return false;

    boolean disabled = false;
    for (Row cursor : selection) {
      final ReconciliationEventStatus status = ReconciliationEventStatus.from((String)cursor.getAttribute(ReconciliationEventAdapter.STATUS));
      if (!ReconciliationEventAdapter.CLOSE.contains(status)) {
        disabled = true;
        break;
      }
    }
    return disabled;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   isLinkDisabled
  /**
   ** Whether the selected row(s) (aka Reconciliation Event) can be linked
   ** or not.
   ** <p>
   ** Linking an <code>Reconciliation Event</code> depends on the status of
   ** a particular Reconciliation Event.
   ** <br>
   ** A <code>Reconciliation Event</code> can only be linked if the status
   ** is <code>No Match Found</code>
   **
   ** @return                    <code>true</code> if the reevaluate event
   **                            operation is possible; otherwise
   **                            <code>false</code>.
   */
  public boolean isLinkDisabled() {
    final List<Row> selection = selectedRow(getSearchTable());
    if (selection.size() == 0)
      return false;

    boolean disabled = false;
    for (Row cursor : selection) {
      final ReconciliationEventStatus status = ReconciliationEventStatus.from((String)cursor.getAttribute(ReconciliationEventAdapter.STATUS));
      if (ReconciliationEventStatus.MATCH_NOT_FOUND != status) {
        disabled = true;
        break;
      }
    }
    return disabled;
  }
  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   refresh
  /**
   ** Refreshing the state of the UI component which displays the table of
   ** queried profiles and controls the further actions for those profiles in
   ** this table.
   **
   ** @return                    the outcome of the action to apply control
   **                            flow rules in the task flow.
   */
  public String refresh() {
    refresh("EventIterator");
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   launch
  /**
   ** Raise a <code>Contextual Event</code> programmatically to launch the
   ** taskflow to render the train to create a new or modify an existing
   ** <code>IT Resource</code>.
   **
   ** @return                    the outcome of the action to apply control
   **                            flow rules in the task flow.
   */
  public String launch() {
    final List<Row> selection = selectedRow(getSearchTable());
    if (selection == null || selection.size() == 0)
      return "";

    final Row                 row         = selection.get(0);
    final Long                eventKey    = (Long)row.getAttribute(ReconciliationEventAdapter.PK);
    final String              taskFlowId  = "rce" + eventKey;
    final String              regionTitle = String.valueOf(eventKey);
    final Map<String, Object> parameter   = new HashMap<String, Object>();
    parameter.put(PARAMETER_MODE,                MODE_EDIT);
    parameter.put(PARAMETER_TASKFLOW,            taskFlowId);
    parameter.put(ReconciliationEventAdapter.PK, eventKey);
    raiseTaskFlowLaunchEvent(taskFlowId, "/WEB-INF/oracle/iam/identity/sysprov/rce/flow/train-tf.xml#train-tf", regionTitle, null, regionTitle, ConstantsDefinition.HELP_TOPIC_ID_ADM_CONFIG_APPINST, false, parameter);
    return MODE_EDIT;
  }
}