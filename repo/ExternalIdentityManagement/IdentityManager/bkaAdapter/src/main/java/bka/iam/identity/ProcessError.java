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
    Subsystem   :   Service Foundation

    File        :   ProcessError.java

    Compiler    :   JDK 1.8

    Author      :   sylvert.bernet@oracle.com

    Purpose     :   This file implements the interface
                    ProcessError.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.02.2020  SBernet    First release version
*/

package bka.iam.identity;
////////////////////////////////////////////////////////////////////////////////
// interface ProcessError
// ~~~~~~~~~ ~~~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  sylvert.bernet@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface ProcessError {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the default error prefix. */
  static final String PREFIX                           = "PRC-";

  // 00001 - 00010 configuration related errors
  static final String PROPERTY_NOTFOUND                = PREFIX + "00001";
  static final String PROPERTY_INVALID                 = PREFIX + "00002";

  // 00011 - 00020 notification related errors
  static final String NOTIFICATION_FAILED              = PREFIX + "00011";
  static final String NOTIFICATION_EXCEPTION           = PREFIX + "00012";
  static final String NOTIFICATION_UNRESOLVED_DATA     = PREFIX + "00013";
  static final String NOTIFICATION_TEMPLATE_NOTFOUND   = PREFIX + "00014";
  static final String NOTIFICATION_TEMPLATE_AMBIGOUS   = PREFIX + "00015";
  static final String NOTIFICATION_RESOLVER_NOTFOUND   = PREFIX + "00016";
  static final String NOTIFICATION_IDENTITY_NOTFOUND   = PREFIX + "00017";
  static final String NOTIFICATION_RECIPIENT_EMPTY     = PREFIX + "00018";

  // 00021 - 00030 housekeeping related errors
  static final String HOUSEKEEPING_CONNECTOR_NOTFOUND  = PREFIX + "00021";
  static final String HOUSEKEEPING_DESCRIPTOR_NOTFOUND = PREFIX + "00022";
  
  // 00031 - 00040 Directory Synchronization related errors
  static final String SEARCH_ENTRY_FAILED              = PREFIX + "00031";
  static final String UPDATE_ENTRY_FAILED              = PREFIX + "00032";

  // 00041 - 00050 Request cleanup related errors
  static final String REQUEST_STAGES_FAILED            = PREFIX + "00041";
  static final String REQUEST_SEARCH_FAILED            = PREFIX + "00042";


}