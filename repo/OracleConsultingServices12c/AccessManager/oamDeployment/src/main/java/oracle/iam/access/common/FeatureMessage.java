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

package oracle.iam.access.common;

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
  static final String SERVICE_REQUEST_PAYLOAD   = PREFIX + "01011";
  static final String SERVICE_RESPONSE_PAYLOAD  = PREFIX + "01012";

  // 01021 - 01030 common messages
  static final String OPERATION_PRINT_BEGIN     = PREFIX + "01021";
  static final String OPERATION_PRINT_SUCCESS   = PREFIX + "01022";
  static final String OPERATION_PRINT_SKIPPED   = PREFIX + "01023";
  static final String OPERATION_PRINT_NOTFOUND  = PREFIX + "01024";

  // 01031 - 01040 available service status messages
  static final String ACCESS_SERVICE            = PREFIX + "01031";
  static final String ACCESS_SERVICE_STATUS     = PREFIX + "01032";
  static final String ACCESS_SERVICE_ENABLED    = PREFIX + "01033";
  static final String ACCESS_SERVICE_DISABLED   = PREFIX + "01034";
  static final String ACCESS_SERVICE_UNKNOWN    = PREFIX + "01035";

  // 01041 - 01050 identity store status messages
  static final String IDENTITY_STORE            = PREFIX + "01061";
  static final String IDENTITY_STORE_PROPERTY   = PREFIX + "01062";
  static final String IDENTITY_STORE_VALUE      = PREFIX + "01063";

  // 01051 - 01060 available service status messages
  static final String FEDERATION_PARTNER        = PREFIX + "01051";
  static final String FEDERATION_PARTNER_TYPE   = PREFIX + "01052";

  // 01061 - 01070 available service status messages
  static final String AGENT_PROPERTY            = PREFIX + "01061";
  static final String AGENT_VALUE               = PREFIX + "01062";
}