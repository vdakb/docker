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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Oracle Identity Manager Connector

    File        :   SystemPermissionHistory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    SystemPermissionHistory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-06-21  DSteding    First release version
*/

package oracle.iam.identity.gis.persistence.local;

////////////////////////////////////////////////////////////////////////////////
// interface SystemPermissionHistory
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>SystemPermissionHistory</code> declares the usefull constants to
 ** deal with table <code>USG_REVOKE_HISTORY</code>s in Oracle Identity Manager.
 **
 ** @author  Dieter.Steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface SystemPermissionHistory {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the name of the entity to use. */
  static final String ENTITY       = "usg_revoke_history";

  /** the name of the system identifier of an entry in the entity. */
  static final String PRIMARY      = "id";

  /** the name of the system identifier of a user in the entity. */
  static final String USER         = "usr_key";

  /** the name of the system identifier of a role in the entity. */
  static final String ROLE         = "ugp_key";

  /** the name of the revoke timestamp of a membership in the entity. */
  static final String REVOKE       = "usg_revoke_date";
}