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

    Copyright 2020 All Rights reserved

    -----------------------------------------------------------------------

    System      :   BKA Identity Manager
    Subsystem   :   Zero Provisioning

    File        :   ZeroMessage.java

    Compiler    :   JDK 1.8

    Author      :   sylvert.bernet@oracle.com

    Purpose     :   This file implements the interface
                    ZeroMessage.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      16.08.2023  SBernet     First release version
*/
package bka.iam.identity.zero;
////////////////////////////////////////////////////////////////////////////////
// interface ZeroMessage
// ~~~~~~~~~ ~~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  sylvert.bernet@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface ZeroMessage {
  
  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the default error prefix. */
  static final String PREFIX                           = "ZRO-";

  // 01001 - 01010 account operation related message
  static final String REQUEST_NEW_ACCOUNT              = PREFIX + "01001";
  static final String REQUEST_MODIFY_ACCOUNT           = PREFIX + "01002";
  static final String REQUEST_REVOKE_ACCOUNT           = PREFIX + "01003";
  static final String NO_ACCOUNT_TO_REQUEST            = PREFIX + "01004";
  
  // 01021 - 01030 ldap operation
  static final String LDAP_SEARCH_QUERY                = PREFIX + "01021";
  static final String LDAP_SEARCH_RESULT               = PREFIX + "01022";
  static final String LDAP_ATTR_NOT_FOUND              = PREFIX + "00023";
  
  // 01031 - 01040 notification related message
  static final String NOTIFICATION_RESOLVE_INCOME      = PREFIX + "01031";
  static final String NOTIFICATION_RESOLVE_OUTCOME     = PREFIX + "01032";
  static final String REPORTER_EMPTY                   = PREFIX + "01033";
  static final String REPORTER_NOT_FOUND               = PREFIX + "01034";
}
