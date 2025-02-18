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

    File        :   TaskScheduler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TaskScheduler.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.naming;

////////////////////////////////////////////////////////////////////////////////
// interface TaskScheduler
// ~~~~~~~~~ ~~~~~~~~~~~~~
/**
 ** The <code>TaskScheduler</code> declares the usefull constants to deal with
 ** <code>Task Scheduler</code>s.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public interface TaskScheduler extends Entity {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Standard prefix name for users. */
  static final String PREFIX          = "Task Scheduler.";

  /**
   ** the mapping key contained in a collection to specify that the group system
   ** key should be resolved
   */
  static final String KEY             = PREFIX + FIELD_KEY;

  /**
   ** the mapping key contained in a collection to specify that the group name
   ** should be resolved
   */
  static final String NAME            = PREFIX + FIELD_NAME;

  /**
   ** the mapping key contained in a collection to specify that the attribute
   ** key should be resolved
   */
  static final String ATTRIBUTE_KEY   = PREFIX + "Task Attributes.Key";

  /**
   ** the mapping key contained in a collection to specify that the attribute
   ** value should be resolved
   */
  static final String ATTRIBUTE_NAME  = PREFIX + "Task Attributes.Name";
  /**
   ** the mapping key contained in a collection to specify that the attribute
   ** value should be resolved
   */
  static final String ATTRIBUTE_VALUE = PREFIX + "Task Attributes.Value";
}