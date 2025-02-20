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

    Copyright © 2020 All Rights reserved

    -----------------------------------------------------------------------

    System      :   BKA Identity Frontend Extension
    Subsystem   :   Web Service Integration

    File        :   ServiceError.java

    Compiler    :   JDK 1.8

    Author      :   sylver.bernet@silverid.fr

    Purpose     :   This file implements the interface
                    ServiceError.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2022-11-02  SBernet    First release version
*/
package bka.iam.identity.service;

////////////////////////////////////////////////////////////////////////////////
// interface RequestError
// ~~~~~~~~~ ~~~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  sylver.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface ServiceError {
  
  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // 00001 - 00010 system related errors
  static final String GENERAL                        = ServiceConstant.PREFIX + "00001";
  static final String UNHANDLED                      = ServiceConstant.PREFIX + "00002";
  static final String ABORT                          = ServiceConstant.PREFIX + "00003";
  static final String NOTIMPLEMENTED                 = ServiceConstant.PREFIX + "00004";
  
  // 00011 - 00020 service configuration related errors
  static final String ITRESOURCENOTFOUND             = ServiceConstant.PREFIX + "00011";
  static final String ITRESOURCE_MISCONFIGURED       = ServiceConstant.PREFIX + "00012";
  
}
