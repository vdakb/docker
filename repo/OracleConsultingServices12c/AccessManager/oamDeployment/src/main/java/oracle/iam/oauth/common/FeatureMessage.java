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

    System      :   Oracle Access Manager Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   FeatureMessage.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    FeatureMessage.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.oauth.common;

////////////////////////////////////////////////////////////////////////////////
// interface FeatureMessage
// ~~~~~~~~~ ~~~~~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface FeatureMessage extends FeatureConstant {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // 01011 - 01020 common messages
  static final String SERVICE_REQUEST_PAYLOAD  = PREFIX + "01011";
  static final String SERVICE_RESPONSE_PAYLOAD = PREFIX + "01012";

  // 01021 - 01030 operation messages
  static final String OPERATION_REPORT_BEGIN   = PREFIX + "01021";
  static final String OPERATION_REPORT_SUCCESS = PREFIX + "01022";
  static final String OPERATION_REPORT_SKIPPED = PREFIX + "01023";

  // 01031 - 01040 identity domina property messages
  static final String IDENTITY_DOMAIN          = PREFIX + "01041";
  static final String IDENTITY_DOMAIN_PROPERTY = PREFIX + "01042";
  static final String IDENTITY_DOMAIN_VALUE    = PREFIX + "01043";
  static final String RESOURCE_SERVER          = PREFIX + "01044";
  static final String RESOURCE_SERVER_PROPERTY = PREFIX + "01045";
  static final String RESOURCE_SERVER_VALUE    = PREFIX + "01046";
  static final String RESOURCE_CLIENT          = PREFIX + "01047";
  static final String RESOURCE_CLIENT_PROPERTY = PREFIX + "01048";
  static final String RESOURCE_CLIENT_VALUE    = PREFIX + "01049";
}