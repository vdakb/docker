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

    File        :   SystemPermission.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    SystemPermission.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-06-21  DSteding    First release version
*/

package oracle.iam.identity.gis.persistence.local;

////////////////////////////////////////////////////////////////////////////////
// interface SystemPermission
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** The <code>SystemPermission</code> declares the usefull constants to
 ** deal with table <code>UGP</code>s in Oracle Identity Manager.
 **
 ** @author  Dieter.Steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface SystemPermission {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the name of the entity to use. */
  static final String ENTITY       = "ugp";

  /** the name of the system identifier of an entry in the entity. */
  static final String PRIMARY      = "ugp_key";

  /** the system identifier name of the category of an entry in the entity. */
  static final String CATEGORY     = "ugp_role_category_key";
}