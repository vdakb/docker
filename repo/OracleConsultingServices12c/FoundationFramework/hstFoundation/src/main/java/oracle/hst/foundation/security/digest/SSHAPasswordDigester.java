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

    File        :   SSHAPasswordDigester.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    SSHAPasswordDigester.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    1.0.1.0            2011-05-14  DSteding    First release version
*/

package oracle.hst.foundation.security.digest;

import oracle.hst.foundation.security.PasswordDigester;
import oracle.hst.foundation.security.DefaultStringDigester;

////////////////////////////////////////////////////////////////////////////////
// final class SSHAPasswordDigester
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** Utility class for easily performing password digesting and checking
 ** according to {SSHA}, a password encryption scheme defined in RFC2307 and
 ** commonly found in LDAP systems.
 ** <p>
 ** This class internally holds a {@link DefaultStringDigester}
 ** configured this way:
 ** <ul>
 **   <li>Algorithm: <code>SHA-1</code>.
 **   <li>Salt size: <code>8 bytes</code> (configurable with {@link #saltSize(int)}).
 **   <li>Iterations: <code>1</code> (no hash iteration).
 **   <li>Prefix: <code>{SSHA}</code>.</li>
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
public final class SSHAPasswordDigester implements PasswordDigester {

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
   ** Constructs an empty <code>SSHAPasswordDigester</code> that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public SSHAPasswordDigester() {
    // ensure inheritance
    super();

    // initialize instance
    this.digester = new DefaultStringDigester();
    this.digester.algorithm("SHA-1");
    this.digester.iteration(1);
    this.digester.saltSize(8);
    this.digester.prefix("{SSHA}");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   saltSize
  /**
   ** Sets the size of the salt to be used to compute the digest.
   ** <p>
   ** This mechanism is explained in <a href="http://www.rsasecurity.com/rsalabs/node.asp?id=2127" target="_blank">PKCS &#035;5: Password-Based Cryptography Standard</a>.
   ** <p>
   ** If salt size is set to zero, then no salt will be used.
   **
   ** @param  size               the size of the salt to be used, in bytes.
   */
  public synchronized void saltSize(final int size) {
    this.digester.saltSize(size);
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