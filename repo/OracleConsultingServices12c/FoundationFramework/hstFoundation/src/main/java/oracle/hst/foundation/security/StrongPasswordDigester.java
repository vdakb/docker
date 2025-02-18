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

    File        :   StrongPasswordDigester.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    StrongPasswordDigester.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    1.0.1.0            2011-05-14  DSteding    First release version
*/

package oracle.hst.foundation.security;

////////////////////////////////////////////////////////////////////////////////
// final class StrongPasswordDigester
// ~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Utility class for easily performing high-strength password digesting and
 ** checking.
 ** <p>
 ** This class internally holds a {@link DefaultStringDigester}
 ** configured this way:
 ** <ul>
 **   <li>Algorithm: <code>SHA-256</code>.
 **   <li>Salt size: <code>16 bytes</code>.
 **   <li>Iterations: <code>10000</code>.
 ** </ul>
 ** The required steps to use it are:
 ** <ol>
 **   <li>Create an instance (using <code>new</code>).
 **   <li>Perform the desired <code>{@link #digest(String)}</code> or
 **       <code>{@link #match(String, String)}</code> operations.
 ** </ol>
 ** <p>
 ** This class is <i>thread-safe</i>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.1.0
 ** @since   1.0.1.0
 */
public final class StrongPasswordDigester implements PasswordDigester {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // the StringDigester that will be internally used.
  private final DefaultStringDigester digester;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>StrongPasswordDigester</code> that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public StrongPasswordDigester() {
    // ensure inheritance
    super();

    // initialize instance
    this.digester = new DefaultStringDigester();
    this.digester.algorithm("SHA-256");
    this.digester.iteration(10000);
    this.digester.saltSize(16);
    this.digester.initialize();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   digest (PasswordDigester)
  /**
   ** Encrypts (digests) a password.
   **
   ** @param  password           the password to be encrypted.
   **
   ** @return                    the resulting digest.
   **
   ** @see DefaultStringDigester#digest(String)
   */
  @Override
  public String digest(final String password) {
    return this.digester.digest(password);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   match (PasswordDigester)
  /**
   ** Checks an unencrypted (plain) password against an encrypted one (a digest)
   ** to see if they match.
   **
   ** @param  password           the plain password to be compared to the
   **                            digest.
   ** @param  digest             the digest to be compared to the plain text
   **                            password.
   **
   ** @return                    <code>true</code> if passwords match,
   **                            <code>false</code> otherwise.
   **
   ** @see DefaultStringDigester#match(String, String)
   */
  @Override
  public boolean match(final String password, final String digest) {
    return this.digester.match(password, digest);
  }
}