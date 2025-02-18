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

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Oracle Database Account Connector

    File        :   Constant.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Constant.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.dbs;

////////////////////////////////////////////////////////////////////////////////
// interface Constant
// ~~~~~~~~~ ~~~~~~~
/**
 ** Declares global visible identifier used for internal referencing by the
 ** adapter.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public interface Constant {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String PREFIX               = "DBA-";

  /** the mapping key to reconcile the name of a role granted to an account.*/
  static final String USER_ROLE_NAME       = "Role Name";

  /**
   ** the mapping key to reconcile the administrator option of a role granted to
   ** an account.
   */
  static final String USER_ROLE_ADMIN      = "Role Administrator";

  /** the mapping key to reconcile granted system privileges of an account */
  static final String USER_PRIVILEGES      = "Privileges";

  /**
   ** the mapping key to reconcile the name of a privilege granted to an
   ** account.
   */
  static final String USER_PRIVILEGE_NAME  = "Privilege Name";

  /**
   ** the mapping key to reconcile the name of a privilege granted to an
   ** account.
   */
  static final String USER_PRIVILEGE_ADMIN = "Privilege Administrator";
  /**
   ** the mapping key to reconcile the child data like roles and privileges
   ** assigned to the account
   */
  public static final String CHILDDATA     = "child-data";

}