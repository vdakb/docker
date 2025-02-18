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
    Subsystem   :   System Administration Management

    File        :   Search.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Search.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2016-03-03  DSteding    First release version
*/

package oracle.iam.identity.sysadmin.tsk.state;

import java.util.Map;
import java.util.List;
import java.util.HashMap;

import oracle.jbo.Row;

import oracle.adf.view.rich.event.DialogEvent;

import oracle.iam.ui.platform.model.config.ConstantsDefinition;

import oracle.hst.foundation.faces.ADF;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.frontend.AbstractSearchState;

import oracle.iam.identity.sysadmin.schema.TaskAdapter;

////////////////////////////////////////////////////////////////////////////////
// class Search
// ~~~~~ ~~~~~~
/**
 ** Declares methods the user interface service provides to search for
 ** <code>Schedule Task</code>s.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
 */
public class Search extends AbstractSearchState {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String BUNDLE           = "oracle.iam.identity.bundle.Administration";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-939610587625893625")
  private static final long  serialVersionUID = 263550141230018592L;

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
    super(TaskAdapter.PK, TaskAdapter.PK);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   isModifyDisabled
  /**
   ** Whether the selected row (aka Scheduler Task) can be opened in a detail
   ** page or not.
   **
   ** @return                    <code>true</code> if the navigation to the
   **                            detail page is possible; otherwise
   **                            <code>false</code>.
   */
  public boolean isModifyDisabled() {
    boolean flag = false;
    if (selectedRowCount(getSearchTable()) == 1) {
      flag = false;
    }
    else {
      flag = true;
    }
    return flag;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   isDeleteDisabled
  /**
   ** Whether the selected row(s) (aka Scheduler Task) can be deleted or not.
   **
   ** @return                    <code>true</code> if the delete operation is
   **                            possible; otherwise <code>false</code>.
   */
  public boolean isDeleteDisabled() {
    return !(selectedRowCount(getSearchTable()) > 0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getLocalizedMessage
  /**
   ** Returns the localized action message.
   **
   ** @return                    the localized action message.
   */
  public String getLocalizedMessage() {
    String localized = getActionName();
    if (StringUtility.isEmpty(localized))
      return "???-emptyornull-???";

    final List<Row> selection  = getSelectedRow();
    switch (localized) {
      case GENERIC_DELETE : return (selection.size() == 1)
                            ? String.format(ADF.resourceBundleValue(BUNDLE, "TSK_DELETE_CONFIRM_SINGLE"), selection.get(0).getAttribute(TaskAdapter.PK))
                            : String.format(ADF.resourceBundleValue(BUNDLE, "TSK_DELETE_CONFIRM_MULTIPLE"), selection.size());
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
   ** to entries in the publication table after confirmation.
   **
   ** @param  event              the {@link DialogEvent} object that
   **                            characterizes the action to perform.
   */
  public void dialogListener(final DialogEvent event) {
    if (DialogEvent.Outcome.yes.equals(event.getOutcome())) {
      final String    actionName = getActionName();
      final List<Row> selection  = getSelectedRow();
      switch(actionName) {
        case GENERIC_DELETE : delete(selection);
                              break;
      }
    }
  }

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
    refresh("TaskIterator");
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Raise a <code>Contextual Event</code> programmatically to launch the
   ** taskflow to render the creation train belonging to
   ** <code>Scheduler Task</code>s.
   **
   ** @return                    the outcome of the action to apply control
   **                            flow rules in the task flow.
   */
  public String create() {
    launch(MODE_CREATE);
    return MODE_CREATE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Raise a <code>Contextual Event</code> programmatically to launch the
   ** taskflow to render the details of the selected
   ** <code>Scheduler Task</code>.
   **
   ** @return                    the outcome of the action to apply control
   **                            flow rules in the task flow.
   */
  public String modify() {
    launch(MODE_EDIT);
    return MODE_EDIT;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   delete
  /**
   ** Deletes the given targets from the collection.
   **
   ** @param  selection          the collection to be deleted.
   */
  public void delete(final List<Row> selection) {
    if (selection != null && !selection.isEmpty()) {
      // latch the name of the first job to delete for feedback purpose
      final Object name = selection.get(0).getAttribute(TaskAdapter.PK);
      // perform the delete row by row
      for (Row cursor : selection) {
        cursor.remove();
      }
      // commit the changes to the persistence layer which will now do what's
      // needed to delete the entity instance(s)
      ADF.executeAction(SUBMIT);
      // send the feed back message before we ar going ahead to refresh the
      // table to still have access to the row data
      if (selection.size() == 1)
        raiseTaskFlowFeedbackEvent(String.format(ADF.resourceBundleValue(BUNDLE, "TSK_DELETE_FEEDBACK_SINGLE"), name));
      else
        raiseTaskFlowFeedbackEvent(String.format(ADF.resourceBundleValue(BUNDLE, "TSK_DELETE_FEEDBACK_MULTIPLE"), selection.size()));
      // now refresh the model and table to visualize the progress
      refresh();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   launch
  /**
   ** Raise a <code>Contextual Event</code> programmatically to launch the
   ** taskflow to render the train to create a new or modify an existing
   ** <code>Scheduler Task</code>.
   **
   ** @param  mode               the mode the train is created with, either
   **                            <code>create</code> or <code>edit</code>.
   **                            Allowed object is {@link String}.
   */
  private void launch(final String mode) {
    String  name = "-1";
    if (MODE_EDIT.equals(mode)) {
      final List<Row> selection = selectedRow(getSearchTable());
      if (selection == null || selection.size() == 0)
        return;
      name = (String)selection.get(0).getAttribute(TaskAdapter.PK);
    }

    final String              taskFlowId  = "tsk" + name;
    final String              regionTitle = MODE_EDIT.equals(mode) ? name : ADF.resourceBundleValue(BUNDLE, "TSK_CREATE_TITLE");

    final Map<String, Object> parameter   = new HashMap<String, Object>();
    parameter.put(TaskAdapter.PK,     name);
    parameter.put(PARAMETER_MODE,     mode);
    parameter.put(PARAMETER_TASKFLOW, taskFlowId);
    raiseTaskFlowLaunchEvent(taskFlowId, "/WEB-INF/oracle/iam/identity/sysadmin/tsk/flow/train-tf.xml#train-tf", regionTitle, null, name, ConstantsDefinition.HELP_TOPIC_ID_ADM_SYSMAN_SCHED, false, parameter);
  }
}