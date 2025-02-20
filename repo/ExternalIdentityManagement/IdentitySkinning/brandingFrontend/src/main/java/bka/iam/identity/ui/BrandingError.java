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

    File        :   BrandingError.java

    Compiler    :   JDK 1.8

    Author      :   sylvert.bernet@oracle.com

    Purpose     :   This file implements the interface
                    BrandingError.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-19-01  SBernet    First release version
*/

package bka.iam.identity.ui;

////////////////////////////////////////////////////////////////////////////////
// interface BrandingError
// ~~~~~~~~~ ~~~~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  sylvert.bernet@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface BrandingError {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // 00001 - 00010 system related errors
  static final String GENERAL                         = BrandingConstant.PREFIX + "00001";
  static final String UNHANDLED                       = BrandingConstant.PREFIX + "00002";
  static final String ABORT                           = BrandingConstant.PREFIX + "00003";
  static final String NOTIMPLEMENTED                  = BrandingConstant.PREFIX + "00004";

  // 00011 - 00020 method argument related errors
  static final String ARGUMENT_IS_NULL                = BrandingConstant.PREFIX + "00011";
  static final String ARGUMENT_BAD_TYPE               = BrandingConstant.PREFIX + "00012";
  static final String ARGUMENT_BAD_VALUE              = BrandingConstant.PREFIX + "00013";
  static final String ARGUMENT_SIZE_MISMATCH          = BrandingConstant.PREFIX + "00014";

  // 00021 - 00030 instance attribute related errors
  static final String ATTRIBUTE_IS_NULL               = BrandingConstant.PREFIX + "00021";

  // 00032 - 00040 file related errors
  static final String FILE_MISSING                    = BrandingConstant.PREFIX + "00031";
  static final String FILE_IS_NOT_A_FILE              = BrandingConstant.PREFIX + "00032";
  static final String FILE_OPEN                       = BrandingConstant.PREFIX + "00033";
  static final String FILE_CLOSE                      = BrandingConstant.PREFIX + "00034";
  static final String FILE_READ                       = BrandingConstant.PREFIX + "00035";
  static final String FILE_WRITE                      = BrandingConstant.PREFIX + "00036";

  // 00041 - 00050 request related errors
  static final String BRANDING_CONFIGURATION_PROPERTY = BrandingConstant.PREFIX + "00041";
  static final String BRANDING_CONFIGURATION_STREAM   = BrandingConstant.PREFIX + "00042";
  static final String BRANDING_CONFIGURATION_PARSING  = BrandingConstant.PREFIX + "00043";
  static final String BRANDING_NOT_DEFINED            = BrandingConstant.PREFIX + "00044";
}