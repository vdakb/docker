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

    Copyright © 2018. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Service Simulation
    Subsystem   :   Generic SCIM Interface

    File        :   DatabaseError.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    DatabaseError.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.dbs;

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
  static final String VENDOR                      = "ORA-";

  // 00001 - 00010 system related errors
  static final String UNHANDLED                   = PREFIX + "00001";
  static final String GENERAL                     = PREFIX + "00002";
  static final String ABORT                       = PREFIX + "00003";

  // 00011 - 00015 method argument related errors
  static final String ARGUMENT_IS_NULL            = PREFIX + "00011";
  static final String ARGUMENT_BAD_TYPE           = PREFIX + "00012";
  static final String ARGUMENT_BAD_VALUE          = PREFIX + "00013";
  static final String ARGUMENT_SIZE_MISMATCH      = PREFIX + "00014";

  // 00021 - 00030 instance state related errors
  static final String INSTANCE_ATTRIBUTE_IS_NULL  = PREFIX + "00021";
  static final String INSTANCE_ILLEGAL_STATE      = PREFIX + "00022";

  // 00031 - 00040 connectivity errors
  static final String CONNECTION_UNKNOWN_HOST     = PREFIX + "00031";
  static final String CONNECTION_CREATE_SOCKET    = PREFIX + "00032";
  static final String CONNECTION_ERROR            = PREFIX + "00033";
  static final String CONNECTION_TIMEOUT          = PREFIX + "00034";
  static final String CONNECTION_NOT_AVAILABLE    = PREFIX + "00035";
  static final String CONNECTION_SSL_HANDSHAKE    = PREFIX + "00036";
  static final String CONNECTION_SSL_ERROR        = PREFIX + "00037";
  static final String CONNECTION_SSL_DESELECTED   = PREFIX + "00038";
  static final String CONNECTION_AUTHENTICATION   = PREFIX + "00039";
  static final String CONNECTION_PERMISSION       = PREFIX + "00040";

  // 00041 - 00050 access operational errors
  static final String SYNTAX_INVALID              = PREFIX + "00041";
  static final String OPERATION_FAILED            = PREFIX + "00042";
  static final String OPERATION_NOT_SUPPORTED     = PREFIX + "00043";
  static final String INSUFFICIENT_PRIVILEGE      = PREFIX + "00044";
  static final String INSUFFICIENT_INFORMATION    = PREFIX + "00045";
  static final String SEARCH_CONDITION_FAILED     = PREFIX + "00046";

  // 00051 - 00060 object operational errors
  static final String OBJECT_NOT_CREATED          = PREFIX + "00051";
  static final String OBJECT_NOT_DELETED          = PREFIX + "00052";
  static final String OBJECT_NOT_MODIFIED         = PREFIX + "00053";
  static final String OBJECT_ALREADY_EXISTS       = PREFIX + "00054";
  static final String OBJECT_NOT_EXISTS           = PREFIX + "00055";
  static final String OBJECT_AMBIGUOUS            = PREFIX + "00056";
  static final String PARENT_NOT_EXISTS           = PREFIX + "00057";
  static final String PARENT_AMBIGUOUS            = PREFIX + "00058";
}