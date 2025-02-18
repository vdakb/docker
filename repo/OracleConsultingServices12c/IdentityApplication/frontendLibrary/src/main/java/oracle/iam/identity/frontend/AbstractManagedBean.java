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

    File        :   AbstractManagedBean.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractManagedBean.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2016-03-03  DSteding    First release version
*/

package oracle.iam.identity.frontend;

import java.util.Map;

import java.io.Serializable;

import oracle.iam.ui.platform.utils.TaskFlowUtils;

import oracle.iam.ui.platform.view.backing.UserMap;
import oracle.iam.ui.platform.view.backing.OIMContext;

import oracle.iam.ui.platform.model.config.ConstantsDefinition;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.foundation.faces.ManagedBean;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractManagedBean
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** Declares methods a user interface service provides.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
 */
public abstract class AbstractManagedBean extends    ManagedBean
                                          implements Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  protected static final String EMPTY_TF         = "/WEB-INF/oracle/iam/ui/common/tfs/empty-tf.xml#empty-tf";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-5965401201987094257")
  private static final long     serialVersionUID = -8709187174084846617L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractManagedBean</code> managed bean that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected AbstractManagedBean() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectedUser
  /**
   ** Returns information about the authenticated user.
   **
   ** @return                    the information about the authenticated user.
   **                            Possible object {@link UserMap}.
   */
  protected UserMap connectedUser() {
    final OIMContext context = new OIMContext();
    return context.getCurrentUser();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   taskFlowOpen
  /**
   ** Determines if a taskflow is open in the UI to prevent that it's opened
   ** more than one.
   **
   ** @param  id                 the internal identifier of the taskflow.
   **                            Allowed object {@link String}.
   **
   ** @return                    <code>true</code> if the task flow is open in
   **                            the current shell module; otherwise
   **                            <code>false</code>.
   */
  protected boolean taskFlowOpen(final String id) {
    return TaskFlowUtils.isTaskFlowOpen(id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   launchTaskFlow
  /**
   ** Raise a <code>Contextaul Event</code> programmatically to launch a
   ** taskflow.
   **
   ** @param  id                 the internal identifier of the taskflow.
   **                            Allowed object {@link String}.
   ** @param  instance           the instance (aka the absolute path) to the
   **                            taskflow.
   **                            Allowed object {@link String}.
   ** @param  name
   **                            Allowed object {@link String}.
   ** @param  icon               the icon to display in the region the task flow
   **                            will occupy.
   **                            Allowed object {@link String}.
   */
  protected void launchTaskFlow(final String id, final String instance, final String name, final String icon) {
    launchTaskFlow(id, instance, name, icon, null, null, false, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   launchTaskFlow
  /**
   ** Raise a <code>Contextaul Event</code> programmatically to launch a
   ** taskflow.
   **
   ** @param  id                 the internal identifier of the taskflow.
   **                            Allowed object {@link String}.
   ** @param  instance           the instance (aka the absolute path) to the
   **                            taskflow.
   **                            Allowed object {@link String}.
   ** @param  name
   **                            Allowed object {@link String}.
   ** @param  icon               the icon to display in the region the task flow
   **                            will occupy.
   **                            Allowed object {@link String}.
   ** @param  description        the discription of the taskflow
   **                            Allowed object {@link String}.
   ** @param  topic              the optional help topic
   **                            Allowed object {@link String}.
   */
  protected void launchTaskFlow(final String id, final String instance, final String name, final String icon, final String description, final String topic) {
    launchTaskFlow(id, instance, name, icon, description, topic, false, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   launchTaskFlow
  /**
   ** Raise a <code>Contextaul Event</code> programmatically to launch a
   ** taskflow.
   **
   ** @param  id                 the internal identifier of the taskflow.
   **                            Allowed object {@link String}.
   ** @param  instance           the instance (aka the absolute path) to the
   **                            taskflow.
   **                            Allowed object {@link String}.
   ** @param  name
   **                            Allowed object {@link String}.
   ** @param  icon               the icon to display in the region the task flow
   **                            will occupy.
   **                            Allowed object {@link String}.
   ** @param  description        the discription of the taskflow
   **                            Allowed object {@link String}.
   ** @param  topic              the optional help topic
   **                            Allowed object {@link String}.
   ** @param  parameter          the parameter mapping to pass to the taskflow.
   **                            Allowed object {@link Map}.
   */
  protected void launchTaskFlow(final String id, final String instance, final String name, final String icon, final String description, final String topic, final Map<String, Object> parameter) {
    launchTaskFlow(id, instance, name, icon, description, StringUtility.isEmpty(topic) ? "oracle_home" : topic, false, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   launchTaskFlow
  /**
   ** Raise a <code>Contextaul Event</code> programmatically to launch a
   ** taskflow.
   **
   ** @param  id                 the internal identifier of the taskflow.
   **                            Allowed object {@link String}.
   ** @param  instance           the instance (aka the absolute path) to the
   **                            taskflow.
   **                            Allowed object {@link String}.
   ** @param  name
   **                            Allowed object {@link String}.
   ** @param  icon               the icon to display in the region the task flow
   **                            will occupy.
   **                            Allowed object {@link String}.
   ** @param  description        the discription of the taskflow
   **                            Allowed object {@link String}.
   ** @param  topic              the optional help topic
   **                            Allowed object {@link String}.
   ** @param  dialog             <code>true</code> if the taskflow behaves as a
   **                            modal dialog.
   **                            Allowed object <code>boolean</code>.
   ** @param  parameter          the parameter mapping to pass to the taskflow.
   **                            Allowed object {@link Map}.
   */
  protected void launchTaskFlow(final String id, final String instance, final String name, final String icon, final String description, final String topic, final boolean dialog, final Map<String, Object> parameter) {
    final String payload = TaskFlowUtils.createContextualEventPayLoad(id, instance, name, icon, description, StringUtility.isEmpty(topic) ? "dwpbank_home" : topic, dialog, parameter);
    TaskFlowUtils.raiseContextualEvent(TaskFlowUtils.RAISE_TASK_FLOW_LAUNCH_EVENT, payload);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refreshTaskFlow
  /**
   ** Raise a <code>Contextaul Event</code> programmatically to refresh a
   ** taskflow.
   **
   ** @param  id                 the internal identifier of the taskflow.
   **                            Allowed object {@link String}.
   ** @param  name
   **                            Allowed object {@link String}.
   ** @param  icon               the icon to display in the region the task flow
   **                            will occupy.
   **                            Allowed object {@link String}.
   ** @param  description        the discription of the taskflow
   **                            Allowed object {@link String}.
   */
  protected void refreshTaskFlow(final String id, final String name, final String icon, final String description) {
    TaskFlowUtils.refreshTaskFlow(id, name, icon, description);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   raiseTaskFlowLaunchEvent
  /**
   ** Raise a <code>Contextaul Event</code> programmatically to raise a
   ** taskflow.
   **
   ** @param  id                 the internal identifier of the taskflow.
   **                            Allowed object {@link String}.
   ** @param  instance           the instance (aka the absolute path) to the
   **                            taskflow.
   **                            Allowed object {@link String}.
   ** @param  name
   **                            Allowed object {@link String}.
   ** @param  icon               the icon to display in the region the task flow
   **                            will occupy.
   **                            Allowed object {@link String}.
   ** @param  description        the discription of the taskflow
   **                            Allowed object {@link String}.
   ** @param  topic              the optional help topic
   **                            Allowed object {@link String}.
   ** @param  dialog             <code>true</code> if the taskflow behaves as a
   **                            modal dialog.
   **                            Allowed object <code>boolean</code>.
   ** @param  parameter          the parameter mapping to pass to the taskflow.
   **                            Allowed object {@link Map}.
   */
  protected void raiseTaskFlowLaunchEvent(final String id, final String instance, final String name, final String icon, final String description, final String topic, final boolean dialog, final Map<String, Object> parameter) {
    final String payload = TaskFlowUtils.createContextualEventPayLoad(id, instance, name, icon, description, StringUtility.isEmpty(topic) ? ConstantsDefinition.HELP_TOPIC_ID_USELF_TOC : topic, dialog, parameter);
    TaskFlowUtils.raiseEvent(TaskFlowUtils.RAISE_TASK_FLOW_LAUNCH_EVENT, payload);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   raiseTaskFlowRemoveEvent
  /**
   ** Raise a <code>Contextaul Event</code> programmatically to remove a
   ** taskflow.
   **
   ** @param  id                 the internal identifier of the taskflow.
   **                            Allowed object {@link String}.
   */
  protected void raiseTaskFlowRemoveEvent(final String id) {
    final String payload = TaskFlowUtils.createContextualEventPayLoad(id, null, null, null, null, null, null, false, null);
    TaskFlowUtils.raiseEvent(TaskFlowUtils.RAISE_TASK_FLOW_REMOVE_EVENT, payload);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   raiseTaskFlowMarkCleanEvent
  /**
   ** Raise a <code>Contextaul Event</code> programmatically to clean up a
   ** taskflow.
   **
   ** @param  id                 the internal identifier of the taskflow.
   **                            Allowed object {@link String}.
   ** @param  name               the identifiying name of the taskflow a UI
   **                            region mostly the tab
   **                            Allowed object {@link String}.
   ** @param  description        the discription of the taskflow
   **                            Allowed object {@link String}.
   ** @param  parameter          the parameter mapping to pass to the taskflow.
   **                            Allowed object {@link Map}.
   */
  protected void raiseTaskFlowMarkCleanEvent(final String id, final String name, final String description, final Map<String, Object> parameter) {
    final String payload = TaskFlowUtils.createContextualEventPayLoad(id, null, null, name, null, description, null, false, parameter);
    TaskFlowUtils.raiseEvent(TaskFlowUtils.RAISE_TASK_FLOW_MARK_CLEAN_EVENT, payload);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   raiseTaskFlowMarkDirtyEvent
  /**
   ** Raise a <code>Contextaul Event</code> programmatically to dirty a
   ** taskflow.
   **
   ** @param  id                 the internal identifier of the taskflow.
   **                            Allowed object {@link String}.
   */
  protected void raiseTaskFlowMarkDirtyEvent(final String id) {
    final String payload = TaskFlowUtils.createContextualEventPayLoad(id, null, null, null, null, null, null, false, null);
    TaskFlowUtils.raiseEvent(TaskFlowUtils.RAISE_TASK_FLOW_MARK_DIRTY_EVENT, payload);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   raiseTaskFlowFeedbackEvent
  /**
   ** Raise a <code>Contextaul Event</code> programmatically to display a
   ** feedback message.
   **
   ** @param message             the message to show as feedback in the UI.
   **                            Allowed object {@link String}.
   */
  protected void raiseTaskFlowFeedbackEvent(final String message) {
    TaskFlowUtils.raiseEvent(TaskFlowUtils.RAISE_TASK_FLOW_FEEDBACK_EVENT, message);
  }
}