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

    File        :   ConnectionError.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    ConnectionError.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2015-06-19  DSteding    First release version
*/

package oracle.iam.identity.sap.control;

////////////////////////////////////////////////////////////////////////////////
// interface ConnectionError
// ~~~~~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   3.1.0.0
 */
public interface ConnectionError {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the default message prefix. */
  static final String PREFIX                        = "SAP-";

  // 00001 - 00010 operations related errors
  static final String UNHANDLED                     = PREFIX + "00001";
  static final String GENERAL                       = PREFIX + "00002";
  static final String ABORT                         = PREFIX + "00003";

  // 00011 - 00015 method argument related errors
  static final String ARGUMENT_IS_NULL               = PREFIX + "00011";
  static final String ARGUMENT_BAD_TYPE              = PREFIX + "00012";
  static final String ARGUMENT_BAD_VALUE             = PREFIX + "00013";
  static final String ARGUMENT_SIZE_MISMATCH         = PREFIX + "00014";

  // 00021 - 00030 instance state related errors
  static final String INSTANCE_ATTRIBUTE_IS_NULL    = PREFIX + "00021";
  static final String INSTANCE_ILLEGAL_STATE        = PREFIX + "00022";

  // 00061 - 00070 destination related errors
  static final String SERVER_NOT_FOUND               = PREFIX + "00061";
  static final String DESTINATION_NOT_FOUND          = PREFIX + "00062";
  static final String ENVIRONMENT_REGISTERATION      = PREFIX + "00063";

  // 00071 - 00080 connectivity related errors
  static final String CONNECTION_UNKNOWN_HOST        = PREFIX + "00071";
  static final String CONNECTION_CREATE_SOCKET       = PREFIX + "00072";
  static final String CONNECTION_ERROR               = PREFIX + "00073";
  static final String CONNECTION_TIMEOUT             = PREFIX + "00074";
  static final String CONNECTION_NOT_AVAILABLE       = PREFIX + "00075";
  static final String CONNECTION_SSL_HANDSHAKE       = PREFIX + "00076";
  static final String CONNECTION_SSL_ERROR           = PREFIX + "00077";
  static final String CONNECTION_SSL_DESELECTED      = PREFIX + "00078";
  static final String CONNECTION_AUTHENTICATION      = PREFIX + "00079";
  static final String CONNECTION_PERMISSION          = PREFIX + "00080";

  // 00081 - 00090 function module related errors
  static final String FUNCTION_CREATE                = PREFIX + "00081";
  static final String FUNCTION_NOT_FOUND             = PREFIX + "00082";
  static final String FUNCTION_NOT_INSTALLED         = PREFIX + "00083";
  static final String FUNCTION_NOT_REMOTABLE         = PREFIX + "00084";
  static final String FUNCTION_EXECUTE_RETRY         = PREFIX + "00085";
  static final String FUNCTION_EXECUTE_ABORT         = PREFIX + "00086";
  static final String FUNCTION_EXECUTE_OTHER         = PREFIX + "00087";
  static final String FUNCTION_FILETRACE_WRITE       = PREFIX + "00088";
  static final String FUNCTION_FILETRACE_CLOSE       = PREFIX + "00089";

  // 00091 - 00100 import parameter related errors
  static final String PARAMETER_TABLE_FORMAT_INVALID = PREFIX + "00090";
  static final String PARAMETER_TABLE_NAME_INVALID   = PREFIX + "00091";
  static final String PARAMETER_VALUE_LIST_EXPECTED  = PREFIX + "00092";
}