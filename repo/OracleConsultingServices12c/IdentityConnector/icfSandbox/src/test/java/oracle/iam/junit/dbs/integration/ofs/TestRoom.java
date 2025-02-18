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

    Copyright © 2021. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Openfire Database Connector

    File        :   TestRoom.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    TestRoom.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-13-06  DSteding    First release version
*/

package oracle.iam.junit.dbs.integration.ofs;

import java.util.Map;

import org.identityconnectors.framework.common.objects.Uid;

import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.junit.TestCaseIntegration;

////////////////////////////////////////////////////////////////////////////////
// class TestRoom
// ~~~~~ ~~~~~~~~
/**
 ** The general test case to manage entries in the target system leveraging the
 ** <code>Connector Server</code> facade.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
abstract class TestRoom extends TestCaseIntegration {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static interface orclShowRoom {
    static final Uid                 SID    = new Uid("1");
    static final Map<String, Object> CREATE = CollectionUtility.map(
        "UD_OFS_MUC_SID",         SID.getUidValue()
      , "UD_OFS_MUC_RID",         "orclShowRoom"
      , "UD_OFS_MUC_DESCRIPTION", "Oracle Show Room"
    );
    static final Map<String, Object> MODIFY = CollectionUtility.map(
        "UD_OFS_MUC_SID",         SID.getUidValue()
      , "UD_OFS_MUC_RID",         "orclShowRoom"
      , "UD_OFS_MUC_DESCRIPTION", "Oracle Show Room(Modified)"
    );
  }
  static interface orclPrivateRoom {
    static final Uid                 SID    = new Uid("2");
    static final Map<String, Object> CREATE = CollectionUtility.map(
        "UD_OFS_MUC_SID",         SID.getUidValue()
      , "UD_OFS_MUC_RID",         "orclPrivateRoom"
      , "UD_OFS_MUC_DESCRIPTION", "Oracle Private Room"
    );
    static final Map<String, Object> MODIFY = CollectionUtility.map(
        "UD_OFS_MUC_SID",         SID.getUidValue()
      , "UD_OFS_MUC_RID",         "orclPrivateRoom"
      , "UD_OFS_MUC_DESCRIPTION", "Oracle Private Room(Modified)"
    );
  }
}