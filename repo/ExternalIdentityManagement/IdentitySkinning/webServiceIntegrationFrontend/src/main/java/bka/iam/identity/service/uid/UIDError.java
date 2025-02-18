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
    Subsystem   :   UID Service

    File        :   UIDError.java

    Compiler    :   JDK 1.8

    Author      :   sylver.bernet@silverid.fr

    Purpose     :   This file implements the interface
                    UIDError.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2022-11-02  SBernet    First release version
*/
package bka.iam.identity.service.uid;

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
public interface UIDError {
  
  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////
  
  // 00001 - 00010 system related errors
  static final String GENERAL                        = UIDConstant.PREFIX + "00001";
  static final String UNHANDLED                      = UIDConstant.PREFIX + "00002";
  static final String ABORT                          = UIDConstant.PREFIX + "00003";
  static final String NOTIMPLEMENTED                 = UIDConstant.PREFIX + "00004";
  
  // 00011 - 00020 UID service configuration error
  static final String TENANT_MISSING                 = UIDConstant.PREFIX + "00011";
  static final String TENANT_FIELD_MISSING           = UIDConstant.PREFIX + "00012";
  static final String UID_MISSING                    = UIDConstant.PREFIX + "00013";
  static final String UID_FIELD_MISSING              = UIDConstant.PREFIX + "00014";
  
}
