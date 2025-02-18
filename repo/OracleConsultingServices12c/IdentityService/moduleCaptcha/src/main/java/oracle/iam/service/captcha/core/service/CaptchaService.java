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

    Copyright © 2020. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Service Extension
    Subsystem   :   Captcha Service Feature

    File        :   CaptchaService.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    CaptchaService.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.service.captcha.core.service;

import oracle.iam.service.captcha.core.Captcha;

////////////////////////////////////////////////////////////////////////////////
// interface CaptchaService
// ~~~~~~~~~ ~~~~~~~~~~~~~~
/**
 ** <code>CaptchaService</code> generates a {@link Captcha} conatining a random
 ** character sequence from an alphabet.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface CaptchaService {

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   captcha
  /**
   ** Returns a {@link Captcha} generated.
   **
   ** @return                    a {@link Captcha} containing a random character
   **                            sequence from an alphabet.
   **                            <br>
   **                            Possible object is {@link Captcha}.
   */
  Captcha captcha();
}