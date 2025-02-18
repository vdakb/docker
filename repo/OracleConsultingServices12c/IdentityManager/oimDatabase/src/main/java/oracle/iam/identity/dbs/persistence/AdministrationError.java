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

    File        :   AdministrationError.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    AdministrationError.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.dbs.persistence;

import oracle.iam.identity.dbs.Constant;

////////////////////////////////////////////////////////////////////////////////
// interface AdministrationError
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public interface AdministrationError {

  // 00081 - 00090 regular expresssion errors
  static final String EXPRESSION_BITVALUES        = Constant.PREFIX + "00081";
  static final String EXPRESSION_INVALID          = Constant.PREFIX + "00082";

  // 00091 - 00130 operational errors
  static final String OBJECT_CONNECTED            = Constant.PREFIX + "00097";
  static final String OBJECT_NOT_ENABLED          = Constant.PREFIX + "00098";
  static final String OBJECT_NOT_DISABLED         = Constant.PREFIX + "00099";
  static final String OBJECT_NOT_LOCKED           = Constant.PREFIX + "00100";
  static final String OBJECT_NOT_UNLOCKED         = Constant.PREFIX + "00101";
}