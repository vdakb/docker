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

    System      :   Oracle Access Manager OAuth Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   AuthorizationMessage.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter Steding

    Purpose     :   This file implements the interface
                    AuthorizationMessage.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.oauth;

////////////////////////////////////////////////////////////////////////////////
// interface AuthorizationMessage
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface AuthorizationMessage {

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
}