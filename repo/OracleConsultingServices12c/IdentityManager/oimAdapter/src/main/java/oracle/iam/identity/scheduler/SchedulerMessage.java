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

    System      :   Oracle Identity Manager Scheduler Shared Library
    Subsystem   :   Common Scheduler Operations

    File        :   SchedulerMessage.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    SchedulerMessage.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.scheduler;


////////////////////////////////////////////////////////////////////////////////
// interface SchedulerMessage
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public interface SchedulerMessage {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String PREFIX                 = "JOB-";

  static final String JOB_BEGIN              = PREFIX + "01051";
  static final String JOB_ABORT              = PREFIX + "01052";
  static final String JOB_COMPLETE           = PREFIX + "01053";

  static final String USER_NOTFOUND          = PREFIX + "01073";
  static final String NOTPROVISIONED         = PREFIX + "01074";

  static final String USER_AFFECTED          = PREFIX + "01081";
  static final String USER_DELETED           = PREFIX + "01082";
  static final String SET_DEPROVISONED_DATE  = PREFIX + "01083";
  static final String SET_EXPIRING_INDICATOR = PREFIX + "01083";
  static final String DATE_COMPARISON        = PREFIX + "01084";
  
  //OpenTask
  static final String SQL_EXEPTION           = PREFIX + "01091";
  static final String USER_LOOKUP_EXEPTION   = PREFIX + "01092";
  static final String OPERATION_MISMATCH     = PREFIX + "01093";
  static final String STATUS_MISMATCH        = PREFIX + "01094";
  static final String APPTYPE_MISMATCH       = PREFIX + "01095";
  static final String DATE_MISMATCH          = PREFIX + "01096";
  
}