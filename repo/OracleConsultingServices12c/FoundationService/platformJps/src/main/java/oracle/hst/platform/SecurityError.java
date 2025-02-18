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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Security Foundation Library
    Subsystem   :   Common shared runtime facilities

    File        :   SecurityError.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    SecurityError.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.hst.platform;

////////////////////////////////////////////////////////////////////////////////
// interface SecurityError
// ~~~~~~~~~ ~~~~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface SecurityError {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the default message prefix. */
  static final String PREFIX            = "JCA-";

  // 00011 - 00020 method argument related errors
  static final String ARGUMENT_IS_NULL  = PREFIX + "00011";

  // 00021 - 00030 encryption related errors
  static final String ENCRYPTION_FATAL  = PREFIX + "00021";
  static final String PASSWORD_FATAL    = PREFIX + "00022";
  static final String PASSWORD_HANDLER  = PREFIX + "00023";

  // 00031 - 00040 key generation related errors
  static final String KEYTYPE_FATAL     = PREFIX + "00031";
  static final String PUBLICKEY_FATAL   = PREFIX + "00032";
  static final String PRIVATEKEY_FATAL  = PREFIX + "00033";
  static final String CERTIFICATE_FATAL = PREFIX + "00034";
}