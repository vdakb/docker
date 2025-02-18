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

    System      :   Oracle Access Service Extension
    Subsystem   :   Common shared runtime facilities

    File        :   PlainObject.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    PlainObject.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.oauth.jwk;

import oracle.hst.platform.core.annotation.ThreadSafety;

////////////////////////////////////////////////////////////////////////////////
// class PlainObject
// ~~~~~ ~~~~~~~~~~~
/**
 ** Plaintext (unsecured) JOSE object.
 ** <p>
 ** This class is thread-safe.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@ThreadSafety(level=ThreadSafety.Level.COMPLETELY)
public class PlainObject extends JoseObject {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-976437604594394869")
  private static final long serialVersionUID = -2003425070394783002L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The header. */
  private final PlainHeader header;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructs a new <code>PlainObject</code> with the specified payload.
   **
   ** @param  payload            the {@link Payload} or <code>null</code> if not
   **                            available (e.g for an encrypted JWE object).
   **                            <br>
   **                            Allowed object is {@link Payload}.
   */
  protected PlainObject(final Payload payload) {
    // ensure inheritance
    this(PlainHeader.Builder.of().build(), payload);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructs a new <code>PlainObject</code> with the specified header and
   ** payload.
   **
   ** @param  header             the {@link PlainHeader}
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link PlainHeader}.
   ** @param  payload            the {@link Payload} or <code>null</code> if not
   **                            available (e.g for an encrypted JWE object).
   **                            <br>
   **                            Allowed object is {@link Payload}.
   */
  protected PlainObject(final PlainHeader header, final Payload payload) {
    // ensure inheritance
    super(payload);

    // initalize instance attributes
    this.header = header;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructs a new <code>PlainObject</code> with the specified
   ** {@link EncodedURL} subjects.
   **
   **
   ** @param  header              the first part, corresponding to the plaintext
   **                             header.
   **                             <br>
   **                             Must not be <code>null</code>.
   **                             <br>
   **                             Allowed object is {@link EncodedURL}.
   ** @param  payload             the second part, corresponding to the payload.
   **                             <br>
   **                             Must not be <code>null</code>.
   **                             <br>
   **                             Allowed object is {@link EncodedURL}.
   */
  public PlainObject(final EncodedURL header, final EncodedURL payload) {
    // ensure inheritance
    super(Payload.of(payload));

    // initialize instance attributes
    this.header = PlainHeader.from(header);
    segment(header, payload, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   header (JoseObject)
  /**
   ** Returns the header of this JOSE object.
   **
   ** @return                    the header of this JOSE object.
   **                            <br>
   **                            Possible object is {@link Header}.
   */
  @Override
  public final Header header() {
    return this.header;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serialize (JoseObject)
  /**
   ** Serialises this JOSE object to its compact format consisting of
   ** {@link EncodedURL} subjects delimited by period ('.') characters.
   **
   ** @return                    the serialized JOSE object.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws IllegalStateException if the JOSE object is not in a state that
   **                               permits serialisation.
   */
  @Override
  public final String serialize() {
    final StringBuilder builder = new StringBuilder(this.header.encoded().toString());
    builder.append('.').append(this.payload.encoded().toString()).append('.');
    return builder.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   from
  /**
   ** Factory method to create a <code>PlainObject</code> from the specified
   ** string in compact format.
   **
   ** @param  value              the string to parse.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the parsed <code>PlainObject</code>.
   **                            <br>
   **                            Possible object is <code>PlainObject</code>.
   **
   ** @throws IllegalArgumentException if the string couldn't be parsed to a
   **                                  valid  plaintext JOSE object.
   */
  public static PlainObject from(final String value)
    throws IllegalArgumentException {

    final EncodedURL[] subject = split(value);
    if (!subject[2].toString().isEmpty())
      throw new IllegalArgumentException("Unexpected third encoded part");

    return new PlainObject(subject[0], subject[1]);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create a <code>PlainObject</code> with the specified
   ** payload.
   **
   ** @param  payload            the {@link Payload} or <code>null</code> if not
   **                            available (e.g for an encrypted JWE object).
   **                            <br>
   **                            Allowed object is {@link Payload}.
   **
   ** @return                    the <code>Payload</code> matching standard
   **                            constant.
   **                            <br>
   **                            Possible object is <code>PlainHeader</code>.
   */
  public static PlainObject of(final Payload payload) {
    return new PlainObject(payload);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create a <code>PlainObject</code> with the specified
   ** header and payload.
   **
   ** @param  header             the {@link PlainHeader}
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link PlainHeader}.
   ** @param  payload            the {@link Payload} or <code>null</code> if not
   **                            available (e.g for an encrypted JWE object).
   **                            <br>
   **                            Allowed object is {@link Payload}.
   **
   ** @return                    the <code>Payload</code> matching standard
   **                            constant.
   **                            <br>
   **                            Possible object is <code>PlainHeader</code>.
   */
  public static PlainObject of(final PlainHeader header, final Payload payload) {
    // prevent bogus input
    if (header == null)
      throw new IllegalArgumentException("The plain header must not be null");

    return new PlainObject(payload);
  }
}