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

    File        :   DefaultPasswordDigester.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    DefaultPasswordDigester.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    1.0.1.0            2011-05-14  DSteding    First release version
*/

package oracle.hst.foundation.security;

////////////////////////////////////////////////////////////////////////////////
// class DefaultPasswordDigester
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Utility class for easily performing digesting and checking.
 ** <p>
 ** This class internally holds a {@link DefaultStringDigester}
 ** configured this way:
 ** <ul>
 **   <li>Algorithm: <code>MD5</code> (default of DefaultByteDigester).
 **   <li>Salt size: <code>8 bytes</code>  (default of DefaultByteDigester).
 **   <li>Iterations: <code>1000</code>  (default of DefaultByteDigester).
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
public class DefaultPasswordDigester implements PasswordDigester {

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
   ** Constructs an empty <code>DefaultPasswordDigester</code> that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public DefaultPasswordDigester() {
    // ensure inheritance
    super();

    // initialize instance
    this.digester = new DefaultStringDigester();
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