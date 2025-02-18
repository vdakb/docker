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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Foundation Shared Library

    File        :   CredentialAccessor.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    CredentialAccessor.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.foundation.utility;

import java.util.Arrays;

import org.identityconnectors.common.security.GuardedString;

////////////////////////////////////////////////////////////////////////////////
// class CredentialAccessor
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** Callback implementation for those times that it is necessary to access the
 ** clear text of the secure string.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class CredentialAccessor implements GuardedString.Accessor {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private char[] secret;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>CredentialAccessor</code> to obtain the secured string
   ** from the specified {@link GuardedString}.
   **
   ** @param  guarded            the {@link GuardedString} whos secured string
   **                            is needed.
   **                            Allowed object is {@link GuardedString}.
   */
  private CredentialAccessor(final GuardedString guarded) {
    // ensure inheritance
    super();

    // initialize instance
    guarded.access(this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   secret
  /**
   ** Returns the plain text secret.
   **
   ** @return                  the plain text secret.
   **                          Possible object <code>char[]</code>.
   */
  public char[] secret() {
    return this.secret;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   access (GuardedString.Accessor)
  /**
   ** This method will be called with the clear text of the string.
   ** <br>
   ** After the call the clearChars array will be automatically zeroed out, thus
   ** keeping the window of potential exposure to a bare-minimum.
   **
   ** @param  secret             the clear text credential.
   */
  @Override
  public void access(final char[] secret) {
    this.secret = Arrays.copyOf(secret, secret.length);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sequence
  /**
   ** Factory method to return the character sequence of a {@link GuardedString}.
   **
   ** @param  guarded            the {@link GuardedString} whos character
   **                            sequence is needed.
   **                            Allowed object is {@link GuardedString}.
   **
   ** @return                    the character sequence of the
   **                            {@link GuardedString}.
   **                            Possible object <code>char[]</code>.
   */
  public static char[] sequence(final GuardedString guarded) {
    return new CredentialAccessor(guarded).secret;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   string
  /**
   ** Factory method to return the string of a {@link GuardedString}.
   **
   ** @param  guarded            the {@link GuardedString} whos secured string
   **                            is needed.
   **                            Allowed object is {@link GuardedString}.
   **
   ** @return                    the string value of the {@link GuardedString}.
   **                            Possible object {@link String}.
   */
  public static String string(final GuardedString guarded) {
    return new String(sequence(guarded));
  }
}