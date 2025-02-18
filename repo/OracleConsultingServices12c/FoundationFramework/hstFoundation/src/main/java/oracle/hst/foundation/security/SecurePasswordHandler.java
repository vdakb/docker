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

    Copyright © 2011. All Rights reserved

    --------------------------------------------------------------------------

    System      :   Foundation Shared Library
    Subsystem   :   Common shared security functions

    File        :   SecurePasswordHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the interface
                    SecurePasswordHandler.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    1.0.1.0            2011-05-14  DSteding    First release version
*/

package oracle.hst.foundation.security;

////////////////////////////////////////////////////////////////////////////////
// interface SecurePasswordHandler
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** Common interface for all entities which can be set a password in char[]
 ** shape, which can be cleaned once the encryptor is initialized so that no
 ** immutable Strings containing the password are left in memory.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.1.0
 ** @since   1.0.1.0
 */
public interface SecurePasswordHandler extends PasswordHandler {

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   password
  /**
   ** Sets a password to be used by the encryptor, as a (cleanable) char[].
   **
   ** @param  password           the password to be used.
   */
  void password(final char[] password);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clean
  /**
   ** Zero-out the password char array.
   **
   ** @param  password           the char array to cleanup.
   */
  void clean(final char[] password);
}