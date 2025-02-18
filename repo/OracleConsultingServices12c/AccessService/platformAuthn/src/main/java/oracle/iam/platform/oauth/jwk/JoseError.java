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

    System      :   Oracle Access Manager OAuth Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   JoseError.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter Steding

    Purpose     :   This file implements the interface
                    JoseError.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.oauth.jwk;

////////////////////////////////////////////////////////////////////////////////
// interface JoseError
// ~~~~~~~~~ ~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface JoseError {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the default message prefix. */
  static final String PREFIX                             = "JWK-";

  static final String UNHANDLED                          = PREFIX + "00001";
  static final String GENERAL                            = PREFIX + "00002";
  static final String ABORT                              = PREFIX + "00003";
  static final String NOTIMPLEMENTED                     = PREFIX + "00004";

  // 00011 - 00020 method argument related errors
  static final String ARGUMENT_IS_NULL                   = PREFIX + "00011";
  static final String ARGUMENT_BAD_TYPE                  = PREFIX + "00012";
  static final String ARGUMENT_BAD_VALUE                 = PREFIX + "00013";
  static final String ARGUMENT_SIZE_MISMATCH             = PREFIX + "00014";

  // 00021 - 00030 JSON parsing related errors
  static final String JSON_OBJECT_NOTNULL                = PREFIX + "00021";
  static final String JSON_TYPE_UNEXPECTED               = PREFIX + "00022";

  // 00031 - 00040 cryptographic or signature related errors
  static final String JOSE_ALGORITHM_UNEXPECTED          = PREFIX + "00031";
}