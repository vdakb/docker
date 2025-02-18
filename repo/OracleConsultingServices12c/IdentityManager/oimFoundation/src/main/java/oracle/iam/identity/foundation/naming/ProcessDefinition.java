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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   ProcessDefinition.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    ProcessDefinition.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.naming;

////////////////////////////////////////////////////////////////////////////////
// interface ProcessDefinition
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** The <code>ProcessDefinition</code> declares the usefull constants to deal
 ** with <code>Provisioning Workflow</code> definitions.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public interface ProcessDefinition extends Entity {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Standard prefix name for Process Definitions. */
  static final String PREFIX           = "Process.";

  /** Standard prefix name for Process Definitions. */
  static final String PREFIX_DEFINTION = "Process Definition.";

  /** Standard prefix name for Process Instance. */
  static final String PREFIX_INSTANCE  = "Process Instance.";

  /** Standard prefix name for Process Definitions. */
  static final String PREFIX_TASK      = "Tasks.";

  /** Standard prefix name for Process Definitions. */
  static final String PREFIX_TASKS     = PREFIX_DEFINTION + PREFIX_TASK;

  /** Standard process type provisioning. */
  static final String PROVISIONING     = "Provisioning";

  /** Standard process type approval. */
  static final String APPROVAL         = "Approval";

  /**
   ** the mapping key contained in a collection to specify that the process
   ** system key should be resolved
   */
  static final String KEY              = PREFIX_DEFINTION + FIELD_KEY;

  /**
   ** the mapping key contained in a collection to specify that the process name
   ** should be resolved
   */
  static final String NAME             = PREFIX_DEFINTION + FIELD_NAME;

  /**
   ** the mapping key contained in a collection to specify that the process
   ** flag default process should be resolved
   */
  static final String DEFAULT          = PREFIX_DEFINTION + "Default Process";

  /**
   ** the mapping key contained in a collection to specify that the process type
   ** should be resolved
   */
  static final String TYPE             = PREFIX_DEFINTION + "Type";

  /**
   ** the mapping key contained in a collection to specify that the process type
   ** is deleted should be resolved
   */
  static final String DELETED          = PREFIX_DEFINTION + FIELD_DELETED;

  /**
   ** the mapping key contained in a collection to specify that the process type
   ** should be resolved
   */
  static final String DESCRIPTION      = PREFIX_DEFINTION + "Process Description";

  /**
   ** the mapping key contained in a collection to specify that the process task
   ** key should be resolved
   */
  static final String TASK_KEY         = PREFIX_TASKS + FIELD_KEY;

  /**
   ** the mapping key contained in a collection to specify that the process task
   ** name should be resolved
   */
  static final String TASK_NAME        = PREFIX_TASKS + "Task Name";

  /**
   ** the mapping key contained in a collection to specify that the process task
   ** name should be resolved
   */
  static final String TASK_EFFECT      = PREFIX_TASKS + "Task Effect";

  /**
   ** the mapping key contained in a collection to specify that the process flag
   ** deleted should be resolved
   */
  static final String TASK_DELETED     = PREFIX_TASKS + FIELD_DELETED;

  /**
   ** the mapping key contained in a collection to specify that the process flag
   ** auto save should be resolved
   */
  static final String PREPOPULATE      = PREFIX_TASK + "Auto Prepopulate";

  /**
   ** the mapping key contained in a collection to specify that the process flag
   ** auto save should be resolved
   */
  static final String AUTOSAVE         = PREFIX_DEFINTION + "Auto Save";

  /**
   ** the mapping key contained in a collection to specify that the system
   ** key of a process instamce should be resolved
   */
  static final String INSTANCE_KEY     = PREFIX_INSTANCE + FIELD_KEY;

  /**
   ** the mapping key contained in a collection to specify that the name of a
   ** process instamce should be resolved
   */
  static final String INSTANCE_NAME    = PREFIX_INSTANCE + FIELD_NAME;
}