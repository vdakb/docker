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

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   WebServiceError.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    WebServiceError.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.0.0.0      2014-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.soap;

////////////////////////////////////////////////////////////////////////////////
// interface WebServiceError
// ~~~~~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.0.0.0
 ** @since   3.0.0.0
 */
public interface WebServiceError {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the default message prefix. */
  static final String PREFIX                           = "SOA-";

  // 00001 - 00010 system related errors
  static final String UNHANDLED                        = PREFIX + "00001";
  static final String GENERAL                          = PREFIX + "00002";
  static final String ABORT                            = PREFIX + "00003";
  static final String NOTIMPLEMENTED                   = PREFIX + "00004";

  // 00011 - 00012 method argument related errors
  static final String ARGUMENT_IS_NULL                 = PREFIX + "00011";

  // 00021 - 00030 instance state related errors
  static final String INSTANCE_ATTRIBUTE_IS_NULL       = PREFIX + "00021";

  // 00061 - 00080 connectivity errors
  static final String CONNECTION_UNKNOWN_HOST          = PREFIX + "00061";
  static final String CONNECTION_CREATE_SOCKET         = PREFIX + "00062";
  static final String CONNECTION_ERROR                 = PREFIX + "00063";
  static final String CONNECTION_TIMEOUT               = PREFIX + "00064";
  static final String CONNECTION_UNAVAILABLE           = PREFIX + "00065";
  static final String CONNECTION_SSL_HANDSHAKE         = PREFIX + "00066";
  static final String CONNECTION_SSL_ERROR             = PREFIX + "00067";
  static final String CONNECTION_SSL_DESELECTED        = PREFIX + "00068";
  static final String CONNECTION_AUTHENTICATION        = PREFIX + "00069";
  static final String CONNECTION_AUTHORIZATION         = PREFIX + "00070";
  static final String CONNECTION_ENCODING_NOTSUPPORTED = PREFIX + "00071";

  // 00081 - 00090 request errors
  static final String SERVICE_AUTHENTICATION           = PREFIX + "00081";
  static final String SERVICE_AUTHORIZATION            = PREFIX + "00082";
  static final String SERVICE_UNAVAILABLE              = PREFIX + "00083";
}