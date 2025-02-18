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

    File        :   ReconciliationManager.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    ReconciliationManager.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.naming;

////////////////////////////////////////////////////////////////////////////////
// interface ReconciliationManager
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>ReconciliationManager</code> declares the usefull constants to
 ** deal with <code>Reconciliatio Events</code>s.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public interface ReconciliationManager extends Entity {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Standard prefix name for Resource Objects. */
  static final String PREFIX                    = "Reconciliation Manager.";

  /**
   ** the logical name of the column to access the status of a Reconciliation
   ** Event.
   */
  static final String FIELD_STATUS              = "Status";

  /**
   ** the logical name of the column to access the last action on a
   ** Reconciliation Event.
   */
  static final String FIELD_LAST_ACTION         = "Last Action";

  /**
   ** the logical name of the column to access the action history action on a
   ** Reconciliation Event.
   */
  static final String FIELD_EVENTHISTORY_ACTION = "Event Action History.Action";

  /**
   ** the logical name of the column to access the action history note on a
   ** Reconciliation Event.
   */
  static final String FIELD_EVENTHISTORY_NOTE   = "Event Action History.Note";

  /**
   ** the logical name of the column to access the event data field value on a
   ** Reconciliation Event.
   */
  static final String FIELD_EVENTDATA_VALUE     = "Event Data.Value";

  /**
   ** the logical name of the column to access the event data field note on a
   ** Reconciliation Event.
   */
  static final String FIELD_EVENTDATA_NOTE      = "Event Data.Note";

  /**
   ** the mapping key contained in a collection to specify that the
   ** Reconciliation Event system key should be resolved
   */
  static final String KEY                       = PREFIX + FIELD_KEY;

  /**
   ** the mapping key contained in a collection to specify that the status of
   ** Reconciliation Event should be resolved
   */
  static final String STATUS                    = PREFIX + FIELD_STATUS;

  /**
   ** the mapping key contained in a collection to specify that the last action
   ** on a Reconciliation Event should be resolved
   */
  static final String LAST_ACTION               = PREFIX + FIELD_LAST_ACTION;

  /**
   ** the mapping key contained in a collection to specify that the last action
   ** on a Reconciliation Event should be resolved
   */
  static final String NOTE                      = PREFIX + FIELD_NOTE;

  /**
   ** the mapping key contained in a collection to specify that the action
   ** history action on a Reconciliation Event should be resolved
   */
  static final String EVENTHISTORY_ACTION       = PREFIX + FIELD_EVENTHISTORY_ACTION;

  /**
   ** the mapping key contained in a collection to specify that the action
   ** history note on a Reconciliation Event should be resolved
   */
  static final String EVENTHISTORY_NOTE         = PREFIX + FIELD_EVENTHISTORY_NOTE;

  /**
   ** the mapping key contained in a collection to specify that the data field
   ** value on a Reconciliation Event should be resolved
   */
  static final String EVENTDATA_VALUE           = PREFIX + FIELD_EVENTDATA_VALUE;

  /**
   ** the mapping key contained in a collection to specify that the data field
   ** value on a Reconciliation Event should be resolved
   */
  static final String EVENTDATA_NOTE            = PREFIX + FIELD_EVENTDATA_NOTE;
}