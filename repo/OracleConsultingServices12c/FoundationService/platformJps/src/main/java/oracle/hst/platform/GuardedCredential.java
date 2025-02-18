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

    System      :   Oracle Security Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   GuardedCredential.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter Steding

    Purpose     :   This file implements the interface
                    GuardedCredential.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.hst.platform;

////////////////////////////////////////////////////////////////////////////////
// interface GuardedCredential
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** Common interface for all entities which can be set a password.
 ** <p>
 ** Secure string implementation that solves the problems associated with
 ** keeping passwords as {@link String}. That is, anything represented as a
 ** <code>string</code> is kept in memory as a clear text password and stays in
 ** memory <b>at least</b> until it is garbage collected.
 ** <p>
 ** The {@link GuardedCredential.Guard} interface alleviates this problem by
 ** storing the characters in memory in an encrypted form. The encryption key
 ** will be a randomly-generated key.
 ** <p>
 ** In their serialized form, {@link GuardedCredential.Guard} will be encrypted
 ** using a known default key. This is to provide a minimum level of protection
 ** regardless of the transport. For communications over the wire it is
 ** recommended that deployments enable SSL for true encryption.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface GuardedCredential {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String NORMALIZER = "java.text.Normalizer";

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // interface Accessor
  // ~~~~~~~~~ ~~~~~~~~
  /**
   ** Callback interface for those times that it is necessary to access the
   ** clear text of the secure string.
   */
  interface Accessor {

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: access
    /**
     ** This method will be called with the clear text of the string.
     ** <br>
     ** After the call the character array will be automatically zeroed out,
     ** thus keeping the window of potential exposure to a bare-minimum.
     **
     ** @param  plain            the clear text of the of the password.
     **                          <br>
     **                          Allowed object is array of <code>char</code>.
     */
    public void access(final char[] plain);
  }

  //////////////////////////////////////////////////////////////////////////////
  // interface Guard
  // ~~~~~~~~~ ~~~~~
  interface Guard extends GuardedCredential {

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: password
    /**
     ** Sets a password to be used by a consumer, as a (cleanable) char[].
     **
     ** @param  password         the password to be used.
     **                          <br>
     **                          Allowed object is array of <code>char</code>.
     */
    void password(final char[] password);

    ////////////////////////////////////////////////////////////////////////////
    // Method: clean
    /**
     ** Zero-out the password char array.
     **
     ** @param  password         the char array to cleanup.
     **                          <br>
     **                          Allowed object is array of <code>char</code>.
     */
    void clean(final char[] password);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   normalize
  /**
   ** Normalize Unicode-input message to NFC.
   ** <p>
   ** This algorithm will normalize the input's UNICODE using
   ** <code>java.text.Normalizer</code>. If this is not present either (this
   ** class appeared in JavaSE 6), it will raise an exception.
   **
   ** @param  message            the message to be normalized.
   **
   ** @return                    the result of the normalization operation
   */
  public static char[] normalize(final char[] message) {
    try {
      GuardedCredential.class.getClassLoader().loadClass(NORMALIZER);
    }
    catch (ClassNotFoundException e) {
      throw new RuntimeException("Cannot find a valid UNICODE normalizer: " + NORMALIZER + " have not been found at the classpath. If you are using a version of the JDK older than JavaSE 6, you should include the icu4j library in your classpath.");
    }
    // using java JDK's Normalizer, we cannot avoid creating Strings
    // (it is the only possible interface to the Normalizer class).
    final String result = java.text.Normalizer.normalize(new String(message), java.text.Normalizer.Form.NFC);
    return result.toCharArray();
  }
}