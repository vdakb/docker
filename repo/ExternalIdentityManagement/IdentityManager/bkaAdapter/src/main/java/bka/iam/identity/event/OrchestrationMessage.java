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

    File        :   OrchestrationMessage.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    OrchestrationMessage.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.02.2019  DSteding    First release version
*/

package bka.iam.identity.event;

////////////////////////////////////////////////////////////////////////////////
// interface OrchestrationMessage
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface OrchestrationMessage {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the default error prefix. */
  static final String PREFIX                           = "ORC-";

  // 01001 - 01010 UI related messages
  static final String PRINCIPAL_NAME_LABEL             = PREFIX + "01001";
  static final String PRINCIPAL_NAME_UNCHNAGED         = PREFIX + "01002";
  static final String PARTICIPANT_LABEL                = PREFIX + "01003";
  static final String PARTICIPANT_UNCHNAGED            = PREFIX + "01004";

  // 01011 - 01020 account request related messages
  static final String ACCOUNT_RULE_VIOLATED            = PREFIX + "01011";
  static final String ACCOUNT_RULE_SATIISFIED          = PREFIX + "01012";
  static final String ACCOUNT_VALIDATION_SINGLE        = PREFIX + "01013";
  static final String ACCOUNT_VALIDATION_MULTIPLE      = PREFIX + "01014";

  // 01021 - 01030 notification related messages
  static final String NOTIFICATION_RESOLVE_INCOME      = PREFIX + "01021";
  static final String NOTIFICATION_RESOLVE_OUTCOME     = PREFIX + "01022";
  static final String NOTIFICATION_ROLE_GRANTED        = PREFIX + "01023";
  static final String NOTIFICATION_ROLE_REVOKED        = PREFIX + "01024";
  static final String NOTIFICATION_ACCOUNT_GRANTED     = PREFIX + "01025";
  static final String NOTIFICATION_ACCOUNT_REVOKED     = PREFIX + "01026";
  static final String NOTIFICATION_ENTITLEMENT_GRANTED = PREFIX + "01027";
  static final String NOTIFICATION_ENTITLEMENT_REVOKED = PREFIX + "01028";
}