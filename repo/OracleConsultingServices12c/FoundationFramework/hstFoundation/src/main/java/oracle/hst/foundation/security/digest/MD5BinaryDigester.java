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
    PURPOSE, OR NON-INFRINGEMENT. ORACLE MD5LL NOT BE LIABLE FOR ANY DAMAGES
    SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
    THIS SOFTWARE OR ITS DERIVATIVES.

    Copyright © 2011. All Rights reserved

    --------------------------------------------------------------------------

    System      :   Foundation Shared Library
    Subsystem   :   Common shared security functions

    File        :   MD5BinaryDigester.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    MD5BinaryDigester.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    1.0.1.0            2011-05-14  DSteding    First release version
*/

package oracle.hst.foundation.security.digest;

import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.Base64Transcoder;
import oracle.hst.foundation.utility.CharacterUtility;

import oracle.hst.foundation.security.DigesterException;
import oracle.hst.foundation.security.DefaultBinaryDigester;

////////////////////////////////////////////////////////////////////////////////
// class MD5BinaryDigester
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** Stronger implementation of the {@link DefaultBinaryDigester}.
 ** <p>
 ** This class lets the user specify the algorithm (and provider) to be used for
 ** creating digests, the size of the salt to be applied, the number of times
 ** the hash function will be applied (iterations) and the salt generator to be
 ** used.
 ** <p>
 ** This class avoids byte-conversion problems related to the fact of different
 ** platforms having different default charsets, and returns digests in the form
 ** of BASE64-encoded ASCII Strings.
 ** <p>
 ** This class is <i>thread-safe</i>.
 ** <p>
 ** <b><u>Configuration</u></b>
 ** <p>
 ** The algorithm, provider, salt size, iterations and salt generator can take
 ** values  in any of these ways:
 ** <ul>
 **   <li>Using its default values.
 **   <li>Calling the corresponding <code>setter</code> methods.
 ** </ul>
 ** And the actual values to be used for initialization will be established by
 ** applying the following priorities:
 ** <ol>
 **   <li>First, the default values are considered.
 **   <li>Finally, if the corresponding <code>setter</code> method has been
 **       called on the digester itself for any of the configuration parameters,
 **       the  values set by these calls override all of the above.
 ** </ol>
 ** <p>
 ** <b><u>Initialization</u></b>
 ** <p>
 ** Before it is ready to create digests, an object of this class has to be
 ** <i>initialized</i>. Initialization happens:
 ** <ul>
 **   <li>When <code>initialize</code> is called.
 **   <li>When <code>digest</code> or <code>matches</code> are called for the
 **       first time, if <code>initialize</code> has not been called before.
 ** </ul>
 ** Once a digester has been initialized, trying to change its configuration
 ** will result in a NOP.
 ** <p>
 ** <b><u>Usage</u></b>
 ** <p>
 ** A digester may be used in two different ways:
 ** <ul>
 **   <li>For <i>creating digests</i>, by calling the <code>digest</code>
 **       method.
 **   <li>For <i>matching digests</i>, this is, checking whether a digest
 **       corresponds adequately to a digest (as in password checking) or not,
 **       by calling the <code>matches</code> method.</li>
 ** </ul>
 ** The steps taken for creating digests are:
 ** <ol>
 **   <li>The String message is converted to a byte array.
 **   <li>A salt of the specified size is generated (see {@link oracle.hst.foundation.security.Salt})
 **   <li>The salt bytes are added to the message.
 **   <li>The hash function is applied to the salt and message altogether, and
 **       then to the results of the function itself, as many times as specified
 **       (iterations).
 **   <li>If specified by the salt generator (see (see {@link oracle.hst.foundation.security.Salt}),
 **       the <i>undigested</i> salt and the final result of the hash function
 **       are concatenated and returned as a result.
 **   <li>The result of the concatenation is encoded in BASE64 or HEXADECIMAL
 **       and returned as an ASCII String.
 ** </ol>
 ** Put schematically in bytes:
 ** <ul>
 **   <li>DIGEST = <code>|<b>S</b>|..(ssb)..|<b>S</b>|<b>X</b>|<b>X</b>|<b>X</b>|...|<b>X</b>|</code>
 **     <ul>
 **       <li><code><b>S</b></code>: salt bytes (plain, not digested). <i>(OPTIONAL)</i>.
 **       <li><code>ssb</code>: salt size in bytes.
 **       <li><code><b>X</b></code>: bytes resulting from hashing (see below).
 **     </ul>
 **   </li>
 **   <li><code>|<b>X</b>|<b>X</b>|<b>X</b>|...|<b>X</b>|</code> = <code><i>H</i>(<i>H</i>(<i>H</i>(..(it)..<i>H</i>(<b>Z</b>|<b>Z</b>|<b>Z</b>|...|<b>Z</b>|))))</code>
 **     <ul>
 **       <li><code><i>H</i></code>: Hash function (algorithm).
 **       <li><code>it</code>: Number of iterations.
 **       <li><code><b>Z</b></code>: Input for hashing (see below).
 **     </ul>
 **   </li>
 **   <li>
 **     <code>|<b>Z</b>|<b>Z</b>|<b>Z</b>|...|<b>Z</b>|</code> = <code>|<b>S</b>|..(ssb)..|<b>S</b>|<b>M</b>|<b>M</b>|<b>M</b>...|<b>M</b>|</code>
 **     <ul>
 **       <li><code><b>S</b></code>: salt bytes (plain, not digested).
 **       <li><code>ssb</code>: salt size in bytes.
 **       <li><code><b>M</b></code>: message bytes.
 **     </ul>
 **   </li>
 ** </ul>
 ** <b>If a random salt generator is used, two digests created for the same
 ** message will always be different (except in the case of random salt
 ** coincidence).</b>
 ** <br>
 ** Because of this, in this case the result of the <code>digest</code> method
 ** will contain both the <i>undigested</i> salt and the digest of the
 ** (salt + message), so that another digest operation can be performed with the
 ** same salt on a different message to check if both messages match (all of
 ** which will be managed automatically by the  <code>matches</code> method).
 ** <p>
 ** To learn more about the mechanisms involved in digest creation, read
 ** <a href="http://www.rsasecurity.com/rsalabs/node.asp?id=2127" target="_blank">PKCS &#035;5: Password-Based Cryptography Standard</a>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.1.0
 ** @since   1.0.1.0
 */
public class MD5BinaryDigester extends DefaultBinaryDigester {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>MD5BinaryDigester</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public MD5BinaryDigester() {
    // ensure inheritance
    super();

    // initialize instance
    saltSize(0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compute
  /**
   ** Create a digest of the input message.
   **
   ** @param  message            the message to be digested.
   **
   ** @return                    the digest
   */
  public String compute(final char[] message) {
    // convert the char[] to bytes.
    // We know there are utility methods for doing this, but we don't know what
    // sort of buffering they use. Because it is possibly sensitive data, we do
    // this in line so that we can clear out our bytes after we are done.
    byte [] bytes = null;
    try {
      bytes = CharacterUtility.charactersToBytes(message);
      return compute(bytes);
    }
    finally {
      // clear the possibly sensitive bytes out
      CharacterUtility.clear(bytes);
      // no need to clear "data" since it is now just a hash
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compute
  /**
   ** Create a digest of the input message.
   **
   ** @param  message            the message to be digested.
   **
   ** @return                    the digest
   */
  public String compute(final byte[] message) {
    // we encode the result always BASE64 so that we obtain the safest result
    // String possible.
    return StringUtility.bytesToString(Base64Transcoder.encode(digest(message)), StringUtility.ASCII);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compare
  /**
   ** Checks a message against a given digest.
   ** <p>
   ** This method tells whether a message corresponds to a specific digest or
   ** not by getting the salt with which the digest was created and applying it
   ** to a digest operation performed on the message. If new and existing digest
   ** match, the message is said to match the digest.
   ** <p>
   ** This method will be used, for instance, for password checking in
   ** authentication processes.
   ** <p>
   ** A <code>null</code> message will only match a <code>null</code> digest.
   **
   ** @param  message            the message to be compared to the digest.
   ** @param  digest             the digest to be compared to the digest.
   **
   ** @return                    <code>true</code> if the specified message
   **                            matches the digest, <code>false</code>
   **                            otherwise.
   **
   ** @throws DigesterException if the digest operation fails, or if
   **                           initialization could not be correctly done (for
   **                           example, if the digest algorithm chosen cannot
   **                           be used) ommitting any further information about
   **                           the cause for security reasons.
   */
  public boolean compare(final char[] message, final String digest) {
    // convert the char[] to bytes.
    // We know there are utility methods for doing this, but we don't know what
    // sort of buffering they use. Because it is possibly sensitive data, we do
    // this in line so that we can clear out our bytes after we are done.
    return compare(CharacterUtility.charactersToBytes(message), digest);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compare
  /**
   ** Checks a message against a given digest.
   ** <p>
   ** This method tells whether a message corresponds to a specific digest or
   ** not by getting the salt with which the digest was created and applying it
   ** to a digest operation performed on the message. If new and existing digest
   ** match, the message is said to match the digest.
   ** <p>
   ** This method will be used, for instance, for password checking in
   ** authentication processes.
   ** <p>
   ** A <code>null</code> message will only match a <code>null</code> digest.
   **
   ** @param  message            the message to be compared to the digest.
   ** @param  digest             the digest to be compared to the digest.
   **
   ** @return                    <code>true</code> if the specified message
   **                            matches the digest, <code>false</code>
   **                            otherwise.
   **
   ** @throws DigesterException if the digest operation fails, or if
   **                           initialization could not be correctly done (for
   **                           example, if the digest algorithm chosen cannot
   **                           be used) ommitting any further information about
   **                           the cause for security reasons.
   */
  public boolean compare(final byte[] message, final String digest) {
    // prevent bogus inpout
    if (message == null)
      return (StringUtility.isEmpty(digest));
    else if (StringUtility.isEmpty(digest))
      return false;

    try {
      // the BASE64 or HEXADECIMAL encoding is reversed and the digest is
      // converted into a byte array.
      // the digest must be a US-ASCII String BASE64-encoded
      byte[] digestBytes = digest.getBytes(StringUtility.ASCII.name());
      return match(message, Base64Transcoder.decode(digestBytes));
    }
    catch (Exception e) {
      // If digest fails, it is more secure not to return any information
      // about the cause in nested exceptions. Simply fail.
      throw new DigesterException();
    }
    finally {
      // clear the possibly sensitive bytes out
      CharacterUtility.clear(message);
      // no need to clear "data" since it is now just a hash
    }
  }
}