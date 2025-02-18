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
    Subsystem   :   SAP/R3 Usermanagement Connector

    File        :   LockStatus.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    LockStatus.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2015-06-19  DSteding    First release version
*/

package oracle.iam.identity.sap.persistence;

import com.sap.conn.jco.JCoStructure;

import oracle.iam.identity.sap.control.Function;

public class LockStatus {

  /** Isolates the Lock Status of an SAP User. */
  // 0 (docs say this can also be 00) enabled
  // 32 disabled by CUA
  // 64 disabled by Administrator
  // 128 disabled by too many failed log in attempts
  // 192 disabled by too many failed log in attempts and by administrator
  // it is possible, though I haven't see supporting docs, for
  // 32 + 128, 32 + 64, 32 + 64 + 128 values.

  // Lock status constants
  final static int UNLOCKED    = 0;
  final static int LOCAL       = 32;
  final static int GLOBAL      = 64;
  final static int WRONG_LOGON = 128;
  final static int NO_PASSWORD = 256;

  // used to store the lock status of the user
  private int mask = UNLOCKED;

  public LockStatus(final Function function) {
    final JCoStructure lockingData = function.exportParameter().getStructure("ISLOCKED");
    final String local      = lockingData.getString("LOCAL_LOCK");
    final String global     = lockingData.getString("GLOB_LOCK");
    final String noUserPwd  = lockingData.getString("NO_USER_PW");
    final String wrongLogon = lockingData.getString("WRNG_LOGON");
    this.mask = ("L".equals(local) ? LockStatus.LOCAL : UNLOCKED) | ("L".equals(global) ? LockStatus.GLOBAL : UNLOCKED) | ("L".equals(wrongLogon) ? LockStatus.WRONG_LOGON : UNLOCKED) | ("L".equals(noUserPwd) ? LockStatus.NO_PASSWORD : UNLOCKED);
  }

  protected LockStatus(int mask) {
    super();

    this.mask = mask;
  }

  public boolean unlocked() {
    return this.mask == UNLOCKED;
  }

  public boolean local() {
    return (this.mask & LOCAL) == LOCAL;
  }

  public boolean global() {
    return (this.mask & GLOBAL) == GLOBAL;
  }

  public boolean wrongLogon() {
    return (this.mask & WRONG_LOGON) == WRONG_LOGON;
  }

  public boolean noPassword() {
    return (this.mask & NO_PASSWORD) == NO_PASSWORD;
  }
}