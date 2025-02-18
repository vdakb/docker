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

    File        :   DatabaseError.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    DatabaseError.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.persistence;

////////////////////////////////////////////////////////////////////////////////
// interface DatabaseError
// ~~~~~~~~~ ~~~~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   0.0.0.2
 */
public interface DatabaseError {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the default message prefix. */
  static final String PREFIX                     = "DBS-";
  static final String VENDOR                     = "SQL-";

  // 00001 - 00010 operations related errors
  static final String UNHANDLED                  = PREFIX + "00001";
  static final String GENERAL                    = PREFIX + "00002";
  static final String ABORT                      = PREFIX + "00003";

  // 00011 - 00015 method argument related errors
  static final String ARGUMENT_IS_NULL            = PREFIX + "00011";
  static final String ARGUMENT_BAD_TYPE           = PREFIX + "00012";
  static final String ARGUMENT_BAD_VALUE          = PREFIX + "00013";
  static final String ARGUMENT_SIZE_MISMATCH      = PREFIX + "00014";

  // 00021 - 00030 instance state related errors
  static final String INSTANCE_ATTRIBUTE_IS_NULL  = PREFIX + "00021";
  static final String INSTANCE_ILLEGAL_STATE      = PREFIX + "00022";

  // 00061 - 00070 connectivity errors
  static final String CONNECTION_UNKNOWN_HOST     = PREFIX + "00061";
  static final String CONNECTION_CREATE_SOCKET    = PREFIX + "00062";
  static final String CONNECTION_ERROR            = PREFIX + "00063";
  static final String CONNECTION_TIMEOUT          = PREFIX + "00064";
  static final String CONNECTION_NOT_AVAILABLE    = PREFIX + "00065";
  static final String CONNECTION_SSL_HANDSHAKE    = PREFIX + "00066";
  static final String CONNECTION_SSL_ERROR        = PREFIX + "00067";
  static final String CONNECTION_SSL_DESELECTED   = PREFIX + "00068";
  static final String CONNECTION_AUTHENTICATION   = PREFIX + "00069";
  static final String CONNECTION_PERMISSION       = PREFIX + "00070";

  // 00071 - 00080 access operational errors
  static final String SYNTAX_INVALID              = PREFIX + "00071";
  static final String OPERATION_FAILED            = PREFIX + "00072";
  static final String OPERATION_NOT_SUPPORTED     = PREFIX + "00073";
  static final String INSUFFICIENT_PRIVILEGE      = PREFIX + "00074";
  static final String INSUFFICIENT_INFORMATION    = PREFIX + "00075";
  static final String SEARCH_CONDITION_FAILED     = PREFIX + "00076";

  // 00091 - 00130 object operational errors
  static final String OBJECT_NOT_CREATED          = PREFIX + "00091";
  static final String OBJECT_NOT_DELETED          = PREFIX + "00092";
  static final String OBJECT_NOT_MODIFIED         = PREFIX + "00093";
  static final String OBJECT_ALREADY_EXISTS       = PREFIX + "00094";
  static final String OBJECT_NOT_EXISTS           = PREFIX + "00095";
  static final String OBJECT_AMBIGUOUS            = PREFIX + "00096";
  static final String PARENT_NOT_EXISTS           = PREFIX + "00097";
  static final String PARENT_AMBIGUOUS            = PREFIX + "00098";
  static final String PERMISSION_NOT_ASSIGNED     = PREFIX + "00099";
  static final String PERMISSION_ALREADY_ASSIGNED = PREFIX + "00100";
  static final String PERMISSION_NOT_REMOVED      = PREFIX + "00101";
  static final String PERMISSION_ALREADY_REMOVED  = PREFIX + "00102";
}