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

    File        :   PasswordDigester.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the interface
                    PasswordDigester.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    1.0.1.0            2011-05-14  DSteding    First release version
*/

package oracle.hst.foundation.security;

////////////////////////////////////////////////////////////////////////////////
// interface PasswordDigester
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** Common interface for all util classes aimed at password encryption.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.1.0
 ** @since   1.0.1.0
 */
public interface PasswordDigester {

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   digest
  /**
   ** Encrypts (digests) a password.
   **
   ** @param  password           the password to be encrypted.
   **
   ** @return                    the resulting digest.
   */
  String digest(final String password);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   match
  /**
   ** Matchs an unencrypted (plain) password against an encrypted one (a digest)
   ** to see if they match.
   **
   ** @param  password           the plain password to be compared to the
   **                            digest.
   ** @param  digest             the digest to be compared to the plain text
   **                            password.
   **
   ** @return                    <code>true</code> if passwords match,
   **                            <code>false</code> otherwise.
   */
  boolean match(final String password, final String digest);
}