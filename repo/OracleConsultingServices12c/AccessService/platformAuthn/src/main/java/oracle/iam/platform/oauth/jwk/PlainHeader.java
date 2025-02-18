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

    File        :   PlainHeader.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    PlainHeader.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.oauth.jwk;

import java.util.Set;
import java.util.Map;
import java.util.HashSet;
import java.util.Objects;

import javax.json.JsonObject;

import oracle.hst.platform.core.marshal.JsonMarshaller;

////////////////////////////////////////////////////////////////////////////////
// final class PlainHeader
// ~~~~~ ~~~~~ ~~~~~~~~~~~
/**
 ** Plaintext header.
 ** <p>
 ** Supports all {@link #RESERVED reserved header parameters} of the plain
 ** specification:
 ** <ul>
 **   <li>alg (set to {@link Algorithm#NONE "none"}).
 **   <li>typ
 **   <li>cty
 ** </ul>
 ** <p>
 ** The header may also carry {@link #parameter() custom parameters}; these will
 ** be serialized and parsed along the reserved ones.
 ** <p>
 ** Example:
 ** <pre>
 **   {"alg" : "none"}
 ** </pre>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class PlainHeader extends Header<PlainHeader> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-7323669742349679344")
  private static final long serialVersionUID = 5257372450756381426L;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // final class Builder
  // ~~~~~ ~~~~~ ~~~~~~~
  /**
   ** Builder for constructing unsecured (plain) headers..
   ** <p>
   ** Example usage:
   ** <pre>
   **   final PlainHeader header = PlainHeader.Builder.of()
   **     .contentType("text/plain")
   **     .parameter("exp", new Date().getTime())
   **     .build()
   **   ;
   ** </pre>
   */
  public static final class Builder extends Header.Builder<Builder, PlainHeader> {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:  Ctor
    /**
     ** Constructs a new {@link PlainHeader} <code>Builder</code> for the
     ** {@link Algorithm#NONE}.
     **
     ** @param  parsed           the optional parsed {@link EncodedURL}.
     **                          <br>
     **                          May be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     */
    private Builder() {
      // ensure inheritance
      super(Algorithm.NONE);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: build
    /**
     ** Factory method to create a {@link PlainHeader} with the properties
     ** configured.
     **
     ** @return                  the created {@link PlainHeader} populated with
     **                          the properties configured.
     **                          <br>
     **                          Possible object is {@link PlainHeader}.
		 **
		 ** @throws IllegalStateException if the JSON Web Key Header properties were
		 **                               inconsistently specified.
		 */
    @Override
    public final PlainHeader build() {
      return new PlainHeader(this.typ, this.cty, this.prm, this.b64);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: of
    /**
     ** Factory method to create a {@link PlainHeader} <code>Builder</code> for
     ** the {@link Algorithm#NONE}.
     **
     ** @return                  the created @link PlainHeader}
     **                          <code>Builder</code>.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public static Builder of() {
      return new Builder();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructs a new <code>PlainHeader</code> with the {@link Algorithm#NONE}.
   **
   ** @param  type               the optional {@link Type type}
   **                            (<code>typ</code>) parameter.
   **                            <br>
   **                            May be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Type}.
   ** @param  contentType        the optional content type (<code>cty</code>)
   **                            parameter.
   **                            <br>
   **                            May be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  parameter          the optional custom parameter parameter.
   **                            <br>
   **                            May be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} as the key
   **                            and {@link String} for the value.
   ** @param  parsed             the optional parsed {@link EncodedURL}.
   **                            <br>
   **                            May be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   */
  private PlainHeader(final Type type, final String contentType, final Map<String, String> parameter, final EncodedURL parsed) {
    // ensure inheritance
    super(Algorithm.NONE, type, contentType, parameter, parsed);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   included (Header)
  /**
   ** Returns the names of all included parameters (reserved and custom) in the
   ** header.
   **
   ** @return                    the included parameters.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link String}.
   */
  @Override
  public final Set<String> included() {
    final Set<String> parameter = new HashSet<String>(parameter().keySet());
    parameter.add(ALG);

    if (this.typ != null)
      parameter.add(TYP);
    if (this.cty != null)
      parameter.add(CTY);

    return parameter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   from
  /**
   ** Parses a JSON Web Encryption (JWE) header from the specified
   ** {@link EncodedURL}.
   **
   ** @param  value              the {@link EncodedURL} value to parse.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   **
   ** @return                    the <code>PlainHeader</code> matching standard
   **                            constant.
   **                            <br>
   **                            Possible object is <code>PlainHeader</code>.
   **
   ** @throws NullPointerException     if <code>value</code> is
   **                                  <code>null</code>.
   ** @throws IllegalArgumentException if the specified <code>value</code>
   **                                  doesn't represent a valid header.
   */
  public static PlainHeader from(final EncodedURL value)
    throws IllegalArgumentException {

    // prevent bogus input
    return from(Objects.requireNonNull(value, "The encoded value must not be null").decodeString(), value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   from
  /**
   ** Parses a plain header from the specified string.
   **
   ** @param  value              the string to parse.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>PlainHeader</code> matching standard
   **                            constant.
   **                            <br>
   **                            Possible object is <code>PlainHeader</code>.
   **
   ** @throws NullPointerException     if <code>value</code> is
   **                                   <code>null</code>.
   ** @throws IllegalArgumentException if the specified JSON object string
   **                                  doesn't represent a valid header.
   */
  public static PlainHeader from(final String value)
    throws IllegalArgumentException {

    // prevent bogus input
    return from(value, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   from
  /**
   ** Parses a plain header from the specified string.
   **
   ** @param  value              the string to parse.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  origin             the original parsed {@link EncodedURL} of the
   **                            <code>PlainHeader</code> to set.
   **                            <br>
   **                            May be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   **
   ** @return                    the <code>PlainHeader</code> matching standard
   **                            constant.
   **                            <br>
   **                            Possible object is <code>PlainHeader</code>.
   **
   ** @throws NullPointerException     if <code>value</code> is
   **                                   <code>null</code>.
   ** @throws IllegalArgumentException if the specified JSON object string
   **                                  doesn't represent a valid header.
   */
  public static PlainHeader from(final String value, final EncodedURL origin)
    throws IllegalArgumentException {

    // prevent bogus input
    return from(JsonMarshaller.readObject(Objects.requireNonNull(value,"The value must not be null")), origin);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:    from
  /**
   ** Parses a plain header from the specified JSON object.
   **
   ** @param  object             the JSON object representation of the plain
   **                            header to parse.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   ** @param  origin             the original parsed {@link EncodedURL} of the
   **                            <code>PlainHeader</code> to set.
   **                            <br>
   **                            May be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   **
   ** @return                    the <code>PlainHeader</code> matching standard
   **                            constant.
   **                            <br>
   **                            Possible object is <code>PlainHeader</code>.
   **
   **
   ** @throws NullPointerException     if <code>value</code> is
   **                                   <code>null</code>.
   ** @throws IllegalArgumentException if the specified JSON object doesn't
   **                                  represent a valid header.
   */
  public static PlainHeader from(final JsonObject object, final EncodedURL origin)
    throws IllegalArgumentException {

    // obtain the "alg" parameter
    final Algorithm alg = Header.algorithm(Objects.requireNonNull(object, "The Json Object must not be null"));
    if (alg != Algorithm.NONE)
      throw new IllegalArgumentException("The algorithm \"alg\" header parameter must be \\\"none\\\"");

    final Builder builder = Builder.of().origin(origin);
    for (String name : object.keySet()) {
      // skip
      if (name.equals(ALG))
        continue;
      else if (name.equals(TYP))
        builder.type(Type.from(object));
      else if (name.equals(CTY))
        builder.contentType(object.getString(name));
      else
        builder.parameter(name, object.getString(name));
    }
    return builder.build();
  }
}