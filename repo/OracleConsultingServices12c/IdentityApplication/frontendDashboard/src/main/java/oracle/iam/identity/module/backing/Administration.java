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

    File        :   Administration.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Administration.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2016-03-03  DSteding    First release version
*/

package oracle.iam.identity.module.backing;

import javax.faces.event.ActionEvent;

import oracle.iam.ui.platform.model.config.ConstantsDefinition;

import oracle.hst.foundation.faces.ADF;

import oracle.iam.identity.frontend.AbstractManagedBean;

////////////////////////////////////////////////////////////////////////////////
// class Administration
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** Declares methods the user interface service provides to navigate across
 ** task flows of the <code>Administration</code> module.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
 */
public class Administration extends AbstractManagedBean {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the Resource Bundle provided by the module specific implementation */
  private static final String BUNDLE           = "oracle.iam.identity.bundle.Launchpad";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-3433362439156534591")
  private static final long   serialVersionUID = 2616224195413735600L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Administration</code> backing bean that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Administration() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   navigateSchedulerTask
  /**
   ** Launch the taskflow belonging to managing <code>Scheduler Task</code>s
   ** which are visible for the connected user.
   ** <p>
   ** Method needs to be declared as <b><code>public</code></b> because it used
   ** in the UI.
   **
   ** @param  event              an {@link ActionEvent} object describing the
   **                            event source and the property that has changed.
   */
  public void navigateSchedulerTask(final @SuppressWarnings("unused") ActionEvent event) {
    launchTaskFlow("tsk-search", "/WEB-INF/oracle/iam/identity/sysadmin/tsk/flow/search-tf.xml#search-tf", ADF.resourceBundleValue(BUNDLE, "TASK_TITLE"), null, null, ConstantsDefinition.HELP_TOPIC_ID_ADM_SYSMAN_SCHED);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   navigateSchedulerJob
  /**
   ** Launch the taskflow belonging to managing <code>Scheduler</code> jobs
   ** which are visible for the connected user.
   ** <p>
   ** Method needs to be declared as <b><code>public</code></b> because it used
   ** in the UI.
   **
   ** @param  event              an {@link ActionEvent} object describing the
   **                            event source and the property that has changed.
   */
  public void navigateSchedulerJob(final @SuppressWarnings("unused") ActionEvent event) {
    launchTaskFlow("job-search", "/WEB-INF/oracle/iam/identity/sysadmin/job/flow/search-tf.xml#search-tf", ADF.resourceBundleValue(BUNDLE, "JOB_TITLE"), null, null, ConstantsDefinition.HELP_TOPIC_ID_ADM_SYSMAN_SCHED);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   navigateOrchestration
  /**
   ** Launch the taskflow belonging to managing <code>Orchestration</code>
   ** events which are visible for the connected user.
   ** <p>
   ** Method needs to be declared as <b><code>public</code></b> because it used
   ** in the UI.
   **
   ** @param  event              an {@link ActionEvent} object describing the
   **                            event source and the property that has changed.
   */
  public void navigateOrchestration(final @SuppressWarnings("unused") ActionEvent event) {
    launchTaskFlow("orp-search", "/WEB-INF/oracle/iam/identity/sysadmin/orp/flow/search-tf.xml#search-tf", ADF.resourceBundleValue(BUNDLE, "ORCHESTRATION_TITLE"), null, null, "orp-search");
  }
}