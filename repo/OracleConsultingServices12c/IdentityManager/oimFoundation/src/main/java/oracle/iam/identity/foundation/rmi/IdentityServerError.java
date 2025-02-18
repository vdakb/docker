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

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   IdentityServerError.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    IdentityServerError.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.identity.foundation.rmi;

////////////////////////////////////////////////////////////////////////////////
// interface IdentityServerError
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   3.1.0.0
 */
public interface IdentityServerError {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the default message prefix. */
  static final String PREFIX                          = "RMI-";

  // 00001 - 00010 operations related errors
  static final String UNHANDLED                       = PREFIX + "00001";
  static final String GENERAL                         = PREFIX + "00002";
  static final String ABORT                           = PREFIX + "00003";
  static final String NOTIMPLEMENTED                  = PREFIX + "00004";

  // 00011 - 00020 context related errors
  static final String CONTEXT_SERVERTYPE_NOTSUPPORTED = PREFIX + "00011";
  static final String CONTEXT_ENCODING_NOTSUPPORTED   = PREFIX + "00012";
  static final String CONTEXT_CONNECTION_ERROR        = PREFIX + "00013";
  static final String CONTEXT_AUTHENTICATION          = PREFIX + "00014";
  static final String CONTEXT_ACCESS_DENIED           = PREFIX + "00015";

  // 00021 - 00030 identity related errors
  static final String INSUFFICIENT_INFORMATION        = PREFIX + "00021";
  static final String IDENTITY_NOT_EXISTS             = PREFIX + "00022";
  static final String IDENTITY_AMBIGUOUS              = PREFIX + "00023";
  static final String PERMISSION_NOT_EXISTS           = PREFIX + "00024";
  static final String PERMISSION_AMBIGUOUS            = PREFIX + "00025";
  static final String ORGANIZATION_NOT_EXISTS         = PREFIX + "00026";
  static final String ORGANIZATION_AMBIGUOUS          = PREFIX + "00027";
  static final String ROLE_NOT_EXISTS                 = PREFIX + "00028";
  static final String ROLE_AMBIGUOUS                  = PREFIX + "00029";
  static final String ROLE_SEARCH_KEY_AMBIGUOUS       = PREFIX + "00030";

  // 00031 - 00040 permission assignment errors
  static final String PERMISSION_ACCESS_DENEID        = PREFIX + "00031";
  static final String PERMISSION_NOT_GRANTED          = PREFIX + "00032";
  static final String PERMISSION_NOT_REVOKED          = PREFIX + "00033";
  static final String PERMISSION_NOT_MODIFIED         = PREFIX + "00034";
}