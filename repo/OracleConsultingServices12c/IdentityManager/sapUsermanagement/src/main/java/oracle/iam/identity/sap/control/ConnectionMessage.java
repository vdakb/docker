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

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   SAP/R3 Usermanagement Connector

    File        :   ConnectionMessage.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    ConnectionMessage.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2015-06-19  DSteding    First release version
*/

package oracle.iam.identity.sap.control;

////////////////////////////////////////////////////////////////////////////////
// interface ConnectionMessage
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   3.1.0.0
 */
public interface ConnectionMessage {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the default message prefix. */
  static final String PREFIX                   = "SAP-";

  // 01001 - 01010 connectivity related messages
  static final String CONNECTION_OPEN          = PREFIX + "01001";
  static final String CONNECTION_ISOPEN        = PREFIX + "01002";
  static final String CONNECTION_OPENED        = PREFIX + "01003";
  static final String CONNECTION_CLOSE         = PREFIX + "01004";
  static final String CONNECTION_CLOSED        = PREFIX + "01005";
  static final String CONNECTION_VALID         = PREFIX + "01006";
  static final String CONNECTION_INVALID       = PREFIX + "01007";

  // 01011 - 01020 function module related messages
  static final String FUNCTION_EXECUTE_GENERIC  = PREFIX + "01011";
  static final String FUNCTION_EXECUTE_ACCOUNT  = PREFIX + "01012";
  static final String FUNCTION_EXECUTE_RECREATE = PREFIX + "01013";

  // 01021 - 01030 import parameter related messages
  static final String FUNCTION_IMPORT_PARAMTER  = PREFIX + "01021";
  static final String FUNCTION_IMPORT_ATTRIBUTE = PREFIX + "01022";
}