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
    Subsystem   :   Connector Bundle Integration

    File        :   FrameworkError.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    FrameworkError.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.connector.integration;

////////////////////////////////////////////////////////////////////////////////
// interface FrameworkError
// ~~~~~~~~~ ~~~~~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  Dieter Steding
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface FrameworkError {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the default message prefix. */
  static final String PREFIX                          = "ICF-";

  // 00031 - 00040 connectivity related errors
  static final String CONNECTION_UNKNOWN_HOST         = PREFIX + "00031";
  static final String CONNECTION_CREATE_SOCKET        = PREFIX + "00032";
  static final String CONNECTION_SECURE_SOCKET        = PREFIX + "00033";
  static final String CONNECTION_ERROR                = PREFIX + "00034";
  static final String CONNECTION_TIMEOUT              = PREFIX + "00035";
  static final String CONNECTION_NOT_AVAILABLE        = PREFIX + "00036";
  static final String CONNECTION_AUTHENTICATION       = PREFIX + "00037";

  // 00041 - 00050 connector locator related errors
  static final String CONNECTOR_BUNDLE_NOTFOUND       = PREFIX + "00041";
  static final String CONNECTOR_OPTION_MAPPING        = PREFIX + "00042";
  static final String CONNECTOR_OPTION_REQUIRED       = PREFIX + "00043";
  static final String CONNECTOR_OPTION_NOTFOUND       = PREFIX + "00044";

  // 00051 - 00060 reconciliation thread pool related errors
  static final String RECONCILIATION_POOL_SIZE        = PREFIX + "00051";
  static final String RECONCILIATION_POOL_CLOSED      = PREFIX + "00052";
  static final String RECONCILIATION_POOL_FINISHED    = PREFIX + "00053";

  // 00061 - 00070 reconciliation thread executor related errors
  static final String RECONCILIATION_EVENT_LOGIN      = PREFIX + "00061";
}