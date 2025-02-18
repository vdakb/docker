/*
    Oracle Deutschland BV & Co. KG

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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Service Extension
    Subsystem   :   GlassFish Server Security Realm

    File        :   RealmError.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    RealmError.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package oracle.iam.access.glassfish.realm;

////////////////////////////////////////////////////////////////////////////////
// interface RealmError
// ~~~~~~~~~ ~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface RealmError {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the default message prefix. */
  static final String PREFIX                 = "IAD-";

  // 00011 - 00020 method argument related errors
  static final String ARGUMENT_IS_NULL       = PREFIX + "00011";
  static final String ARGUMENT_BAD_TYPE      = PREFIX + "00012";
  static final String ARGUMENT_BAD_VALUE     = PREFIX + "00013";

  // 00021 - 00030 configuration property related errors
  static final String PROPERTY_IS_NULL       = PREFIX + "00021";
  static final String PROPERTY_BAD_TYPE      = PREFIX + "00022";
  static final String PROPERTY_BAD_VALUE     = PREFIX + "00023";

  // 00061 - 00070 naming and lookup related errors
  static final String LOCATOR_INITIALIZE     = PREFIX + "00061";
  static final String CONTEXT_CONNECTION     = PREFIX + "00062";
  static final String CONTEXT_INITIALIZE     = PREFIX + "00063";
  static final String CONTEXT_CLOSE          = PREFIX + "00064";
  static final String CONTEXT_ENVIRONMENT    = PREFIX + "00065";
  static final String OBJECT_LOOKUP          = PREFIX + "00066";
  static final String OBJECT_CREATION        = PREFIX + "00067";
  static final String OBJECT_ACCESS          = PREFIX + "00068";

  // 00071 - 00080 realm related errors
  static final String REALM_BADTYPE          = PREFIX + "00071";

  // 00081 - 00090 user lookup related errors
  static final String USER_REQUIRED          = PREFIX + "00071";
  static final String USER_NOTFOUND          = PREFIX + "00072";
  static final String USER_CREDENTIAL        = PREFIX + "00073";

  // 00091 - 00100 token related errors
  static final String TOKEN_EXPIRED          = PREFIX + "00091";
  static final String TOKEN_NOTBEFORE        = PREFIX + "00092";
  static final String TOKEN_SIGNATURE        = PREFIX + "00093";
}