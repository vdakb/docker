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
    Subsystem   :   Generic Database Connector

    File        :   DatabaseError.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    DatabaseError.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.dbms;

////////////////////////////////////////////////////////////////////////////////
// interface DatabaseError
// ~~~~~~~~~ ~~~~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface DatabaseError {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the default message prefix. */
  static final String PREFIX                      = "DBS-";

  // 00051 - 00060 access operational errors
  static final String SCHEMA_DESCRIPTOR_ERROR     = PREFIX + "00051";
  static final String SCHEMA_DESCRIPTOR_EMPTY     = PREFIX + "00052";
  static final String SCHEMA_DESCRIPTOR_NOTFOUND  = PREFIX + "00053";
  static final String SCHEMA_DESCRIPTOR_INVALID   = PREFIX + "00054";
  static final String SCHEMA_DESCRIPTOR_PARSE     = PREFIX + "00055";
  static final String SCHEMA_DESCRIPTOR_PRIMARY   = PREFIX + "00056";
  static final String SCHEMA_DESCRIPTOR_SECONDARY = PREFIX + "00057";

  // 00061 - 00070 connectivity and statment errors
  static final String CONNECTION_NOT_CONNECTED    = PREFIX + "00061";
  static final String CONNECTION_CLOSED           = PREFIX + "00062";
  static final String STATEMENT_CLOSED            = PREFIX + "00063";
  static final String STATEMENT_TIMEOUT           = PREFIX + "00064";

  // 00071 - 00080 access operational errors
  static final String SYNTAX_INVALID              = PREFIX + "00071";
  static final String OPERATION_FAILED            = PREFIX + "00072";
  static final String OPERATION_NOT_SUPPORTED     = PREFIX + "00073";
  static final String INSUFFICIENT_PRIVILEGE      = PREFIX + "00074";
  static final String INSUFFICIENT_INFORMATION    = PREFIX + "00075";
  static final String SEARCH_CONDITION_FAILED     = PREFIX + "00076";

  // 00081 - 00090 regular expresssion errors
  static final String EXPRESSION_BITVALUES        = PREFIX + "00081";
  static final String EXPRESSION_INVALID          = PREFIX + "00082";

  // 00091 - 00100 path expresssion errors
  static final String PATH_UNEXPECTED_EOS         = PREFIX + "00091";
  static final String PATH_UNEXPECTED_CHARACTER   = PREFIX + "00092";
  static final String PATH_EXPECT_ATTRIBUTE_PATH  = PREFIX + "00093";
  static final String PATH_EXPECT_ATTRIBUTE_NAME  = PREFIX + "00094";

  // 00101 - 00120 object operational errors
  static final String OBJECT_NOT_CREATED          = PREFIX + "00101";
  static final String OBJECT_NOT_DELETED          = PREFIX + "00102";
  static final String OBJECT_NOT_MODIFIED         = PREFIX + "00103";
  static final String OBJECT_ALREADY_EXISTS       = PREFIX + "00104";
  static final String OBJECT_NOT_EXISTS           = PREFIX + "00105";
  static final String OBJECT_AMBIGUOUS            = PREFIX + "00106";
  static final String PARENT_NOT_EXISTS           = PREFIX + "00107";
  static final String PARENT_AMBIGUOUS            = PREFIX + "00108";
  static final String PERMISSION_NOT_ASSIGNED     = PREFIX + "00109";
  static final String PERMISSION_ALREADY_ASSIGNED = PREFIX + "00110";
  static final String PERMISSION_NOT_REMOVED      = PREFIX + "00111";
  static final String PERMISSION_ALREADY_REMOVED  = PREFIX + "00112";
}