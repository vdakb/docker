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

    Copyright 2019 All Rights reserved

    -----------------------------------------------------------------------

    System      :   BKA Identity Manager
    Subsystem   :   Common Shared Plugin

    File        :   OrchestrationError.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    OrchestrationError.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.02.2019  DSteding    First release version
*/

package bka.iam.identity.event;

////////////////////////////////////////////////////////////////////////////////
// interface OrchestrationError
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface OrchestrationError {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the default error prefix. */
  static final String PREFIX                         = "ORC-";

  // 00001 - 00010 configuration related errors
  static final String PROPERTY_NOTFOUND              = PREFIX + "00001";
  static final String PROPERTY_INVALID               = PREFIX + "00002";

  // 00011 - 00020 notification related errors
  static final String NOTIFICATION_FAILED            = PREFIX + "00011";
  static final String NOTIFICATION_EXCEPTION         = PREFIX + "00012";
  static final String NOTIFICATION_STATIC_DATA       = PREFIX + "00013";
  static final String NOTIFICATION_UNRESOLVED_DATA   = PREFIX + "00014";
  static final String NOTIFICATION_TEMPLATE_NOTFOUND = PREFIX + "00015";
  static final String NOTIFICATION_TEMPLATE_AMBIGOUS = PREFIX + "00016";
  static final String NOTIFICATION_RESOLVER_NOTFOUND = PREFIX + "00017";
  static final String NOTIFICATION_IDENTITY_NOTFOUND = PREFIX + "00018";

  // 00021 - 00030 account request related errors
  static final String ACCOUNT_EXISTS_ANY             = PREFIX + "00021";

  // 00031 - 00040 Unique Identifier related messages
  static final String UID_TENANT_NOT_MATCH           = PREFIX + "00031";
  static final String UID_ALREADY_USED               = PREFIX + "00032";
  static final String UID_MODIFY_NOT_ALLOWED         = PREFIX + "00033";

  // 00041 - 00050 Organization related errors
  static final String ATTRIBUTE_MUST_BE_UNIQUE      = PREFIX + "00041";
  static final String ATTRIBUTE_MUST_NOT_BE_BLANK   = PREFIX + "00042";
  static final String ORGANIZATION_SEARCH_ERROR     = PREFIX + "00043";
  static final String ORGANIZATION_MEMBERS          = PREFIX + "00044";

  // 00051 - 00060 Application related errors
  static final String APPLICATION_SEARCH_BY_OBJ     = PREFIX + "00051";
  static final String ACCOUNT_SEARCH                = PREFIX + "00052";
  static final String ACCOUNT_UPDATE                = PREFIX + "00053";

  // 00061 - 00070 Process task related errors
  static final String PROCESS_TASK_BY_NAME          = PREFIX + "00061";
  static final String PROCESS_TASK_NOT_FOUND        = PREFIX + "00062";

  // 00071 - 00080 Authentication related errors
  static final String LOGIN_FAILED                  = PREFIX + "00071";

}