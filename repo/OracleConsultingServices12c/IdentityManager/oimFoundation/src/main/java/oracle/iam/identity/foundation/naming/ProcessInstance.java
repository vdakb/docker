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

    File        :   ProcessInstance.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    ProcessInstance.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.naming;

////////////////////////////////////////////////////////////////////////////////
// interface ProcessInstance
// ~~~~~~~~~ ~~~~~~~~~~~~~~~
/**
 ** The <code>ProcessInstance</code> declares the usefull constants to deal
 ** with <code>Provisioning Workflow</code> instances.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public interface ProcessInstance extends Entity {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////
  /**
   ** the key contained in a collection to specify that the reason should be
   ** mapped
   */
  static final String FIELD_REASON = "Reason";

  /**
   ** the key contained in a collection to specify that the error should be
   ** mapped
   */
  static final String FIELD_ERROR   = "SCH_REASON";

  /** Standard prefix name for Process Instance. */
  static final String PREFIX        = "Process Instance.";

  /** Standard prefix name for Process Definitions. */
  static final String PREFIX_TASK   = "Task Details.";

  /** Standard prefix name for Process Definitions. */
  static final String PREFIX_TASKS  = PREFIX + PREFIX_TASK;

  /**
   ** the mapping key contained in a collection to specify that the process
   ** system key should be resolved
   */
  static final String KEY           = PREFIX + FIELD_KEY;

  /**
   ** the mapping key contained in a collection to specify that the process name
   ** should be resolved
   */
  static final String NAME          = PREFIX + FIELD_NAME;

  /**
   ** the mapping key contained in a collection to specify that the note
   ** should be resolved
   */
  static final String NOTE          = PREFIX_TASKS + FIELD_NOTE;

  /**
   ** the mapping key contained in a collection to specify that the reason
   ** should be resolved
   */
  static final String REASON        = PREFIX_TASKS + FIELD_REASON;

  /**
   ** the mapping key contained in a collection to specify that the version
   ** should be resolved
   */
  static final String VERSION       = PREFIX_TASKS + FIELD_VERSION;

  /**
   ** the mapping key contained in a collection to specify that the error
   ** should be resolved
   */
  static final String ERROR         = FIELD_ERROR;
}
