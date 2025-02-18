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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Connector Bundle Integration

    File        :   FrameworkMessage.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    FrameworkMessage.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.connector.integration;

////////////////////////////////////////////////////////////////////////////////
// interface FrameworkMessage
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  Dieter Steding
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface FrameworkMessage {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the default message prefix. */
  static final String PREFIX                       = "ICF-";

  // 01001 - 01010 script action related message
  static final String SCRIPT_ACTION_EMPTY          = PREFIX + "01001";
  static final String SCRIPT_ACTION_START          = PREFIX + "01002";
  static final String SCRIPT_ACTION_SUCCESS        = PREFIX + "01003";
  static final String SCRIPT_ACTION_FAILED         = PREFIX + "01004";

  // 01011 - 01020 reconciliation pool related message
  static final String RECONCILIATION_POOL_ADD      = PREFIX + "01011";
  static final String RECONCILIATION_POOL_ADDED    = PREFIX + "01012";
  static final String RECONCILIATION_POOL_LIMITS   = PREFIX + "01015";
  static final String RECONCILIATION_POOL_COMPLETE = PREFIX + "01016";

  // 01021 - 01030 reconciliation event related message
  static final String RECONCILIATION_EVENT_REGULAR = PREFIX + "01021";
  static final String RECONCILIATION_EVENT_CREATED = PREFIX + "01022";
  static final String RECONCILIATION_EVENT_FAILED  = PREFIX + "01023";
  static final String RECONCILIATION_EVENT_DELETE  = PREFIX + "01024";
  static final String RECONCILIATION_EVENT_NOTHING = PREFIX + "01025";
}