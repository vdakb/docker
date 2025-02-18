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
    Subsystem   :   Generic REST Library

    File        :   ServiceMessage.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    ServiceMessage.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.rest;

////////////////////////////////////////////////////////////////////////////////
// interface ServiceMessage
// ~~~~~~~~~ ~~~~~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface ServiceMessage {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the default message prefix. */
  static final String PREFIX                 = "GWS-";

  // 01001 - 01010 logging related messages
  static final String LOGGER_ELIPSE_MORE     = PREFIX + "01001";
  static final String LOGGER_THREAD_NAME     = PREFIX + "01002";
  static final String LOGGER_CLIENT_REQUEST  = PREFIX + "01003";
  static final String LOGGER_CLIENT_RESPONSE = PREFIX + "01004";
  static final String LOGGER_SERVER_REQUEST  = PREFIX + "01005";
  static final String LOGGER_SERVER_RESPONSE = PREFIX + "01006";

  // 01011 - 01020 connection messages
  static final String CONNECTING_BEGIN       = PREFIX + "01011";
  static final String CONNECTING_SUCCESS     = PREFIX + "01012";
  static final String OPERATION_BEGIN        = PREFIX + "01013";
  static final String OPERATION_SUCCESS      = PREFIX + "01014";
  static final String AUTHENTICATION_BEGIN   = PREFIX + "01015";
  static final String AUTHENTICATION_SUCCESS = PREFIX + "01016";

  // 01021 - 01030 reconciliation process related messages
  static final String NOTHING_TO_CHANGE      = PREFIX + "01021";

  // 01031 - 01040 provisioning process related messages
  static final String UNSPECIFIED_ERROR      = PREFIX + "01031";
  static final String STATUS_NOT_PROVIDED    = PREFIX + "01032";
}