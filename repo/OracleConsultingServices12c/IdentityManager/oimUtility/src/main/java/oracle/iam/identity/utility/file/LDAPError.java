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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Foundation Shared Library
    Subsystem   :   Common Shared LDAP Facilities

    File        :   LDAPError.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    LDAPError.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

////////////////////////////////////////////////////////////////////////////////
// interface LDAPError
// ~~~~~~~~~ ~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public interface LDAPError {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the default error prefix. */
  static final String PREFIX               = "LDAP-";

  // 00061 - 00080 file system naming and lookup related errors
  static final String FILENAME_MISSING     = PREFIX + "00061";
  static final String FILEEXTENSION_IS_BAD = PREFIX + "00062";
  static final String NOTEXISTS            = PREFIX + "00063";
  static final String NOTAFOLDER           = PREFIX + "00064";
  static final String NOTAFILE             = PREFIX + "00065";
  static final String NOTREADABLE          = PREFIX + "00066";
  static final String NOTWRITABLE          = PREFIX + "00067";
  static final String NOTCREATABLE         = PREFIX + "00068";
  static final String NOTCLOSEDINPUT       = PREFIX + "00069";
  static final String NOTCLOSEDOUTPUT      = PREFIX + "00070";

  // 00081 - 00090 parsing related errors
  static final String SEPARATOR            = PREFIX + "00081";
  static final String OID_REQUIRED         = PREFIX + "00082";
  static final String CRITICALITY_REQUIRED = PREFIX + "00083";
  static final String DELETE_REQUIRED      = PREFIX + "00084";
  static final String ATTRIBUTE_REQUIRED   = PREFIX + "00085";
}