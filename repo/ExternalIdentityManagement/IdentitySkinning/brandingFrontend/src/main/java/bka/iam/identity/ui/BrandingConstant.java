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

    System      :   Oracle Identity Frontend Extension
    Subsystem   :   Branding Customization

    File        :   BrandingConstant.java

    Compiler    :   JDK 1.8

    Author      :   sylvert.bernet@oracle.com

    Purpose     :   This file implements the interface
                    BrandingConstant.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-01-20  SBernet    First release version
*/

package bka.iam.identity.ui;

////////////////////////////////////////////////////////////////////////////////
// interface BrandingConstant
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** Declares global visible identifier used for user information purpose.
 **
 ** @author  sylvert.bernet@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface BrandingConstant {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the default message prefix. */
  static final String PREFIX                 = "BB-";
}