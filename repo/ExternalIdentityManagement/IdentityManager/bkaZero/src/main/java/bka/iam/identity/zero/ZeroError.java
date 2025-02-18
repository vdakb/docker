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

    File        :   ZeroError.java

    Compiler    :   JDK 1.8

    Author      :   sylvert.bernet@oracle.com

    Purpose     :   This file implements the interface
                    ZeroError.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      14.08.2023  SBernet     First release version
*/
package bka.iam.identity.zero;
////////////////////////////////////////////////////////////////////////////////
// interface ZeroError
// ~~~~~~~~~ ~~~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  sylvert.bernet@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface ZeroError {
  
  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the default error prefix. */
  static final String PREFIX                           = "ZRO-";

  // 00001 - 00010 configuration related errors
  static final String APPLICATION_NOTFOUND             = PREFIX + "00001";
  static final String ORGANIZATION_NOTFOUND            = PREFIX + "00002";
  static final String PROPERTY_NOTFOUND                = PREFIX + "00003";
  static final String PROPERTY_INVALID                 = PREFIX + "00004";
  
  // 00011 - 00020 ldap related errors
  static final String LDAP_ERROR                       = PREFIX + "00011";
  static final String NAMING_DN_ERROR                  = PREFIX + "00012";
  static final String INDEX_RDN_OUT_RANGE              = PREFIX + "00013";
  
  // 00021 - 00030 notification related errors
  static final String NOTIFICATION_FAILED              = PREFIX + "00021";
  static final String NOTIFICATION_EXCEPTION           = PREFIX + "00022";
  static final String NOTIFICATION_UNRESOLVED_DATA     = PREFIX + "00023";
  static final String NOTIFICATION_TEMPLATE_NOTFOUND   = PREFIX + "00024";
  static final String NOTIFICATION_TEMPLATE_AMBIGOUS   = PREFIX + "00025";
  static final String NOTIFICATION_RESOLVER_NOTFOUND   = PREFIX + "00026";
  static final String NOTIFICATION_IDENTITY_NOTFOUND   = PREFIX + "00027";
  static final String NOTIFICATION_RECIPIENT_EMPTY     = PREFIX + "00028";

}
