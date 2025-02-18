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

    Copyright © 2017. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Authentication Plug-In Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   Encoder.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Encoder.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.access.foundation.jwt;

import java.util.List;
import java.util.Objects;
import java.util.ArrayList;
import java.util.Collection;

import java.util.stream.Collectors;

import java.util.function.Consumer;

import javax.xml.bind.DatatypeConverter;

////////////////////////////////////////////////////////////////////////////////
// class Encoder
// ~~~~~ ~~~~~~~
/**
 ** ...
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Encoder {

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  private static Encoder instance;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Encoder</code> task that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Encoder() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   base64Encode
  /**
   ** Converts an array of bytes into a transferrable bes64 encoded string.
   **
   ** @param  data               an array of bytes.
   **
   ** @return                    a string containing a lexical representation of
   **                            xsd:base64Binary.
   **
   ** @throws IllegalArgumentException if <code>data</code> is <code>null</code>.
     */
  public static String base64Encode(final byte[] data) {
    return DatatypeConverter.printBase64Binary(data).split("=")[0].replace('+', '-').replace('/', '_');
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Return a singleton instance of the JSON Web Token Encoder.
   **
   ** @return                    the one and only instance of the JSON Web Token
   **                            encoder.
   */
  public static Encoder instance() {
    if (instance == null) {
      instance = new Encoder();
    }

    return instance;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encode
  /**
   ** Encode the JSON Web Token to produce a dot separated encoded string that
   ** can be sent in an HTTP request header.
   **
   ** @param  token              the JSON Web Token.
   ** @param  signer             the signer used to add a signature to the JSON
   **                            Web Token.
   **
   ** @return                    the encoded JSON Web Token string.
   */
  public String encode(final Token token, final Signer signer) {
    return encode(token, signer, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encode
  /**
   ** Encode the JSON Web Token to produce a dot separated encoded string that
   ** can be sent in an HTTP request header.
   **
   ** @param  token              the JSON Web Token.
   ** @param  signer             the signer used to add a signature to the JSON
   **                            Web Token.
   ** @param  consumer           a header consumer to optionally add header
   **                            values to the encoded JSON Web Token.
   **                            <br>
   **                            May be <code>null</code>.
   **
   ** @return                    the encoded JSON Web Token string.
   */
  public String encode(final Token token, final Signer signer, final Consumer<Header> consumer) {
    Objects.requireNonNull(token);
    Objects.requireNonNull(signer);

    final List<String> parts = new ArrayList<>(3);
    final Header header = new Header();
    if (consumer != null) {
      consumer.accept(header);
    }
    // Set this after we pass the header to the consumer to ensure it isn't tampered with, only the signer can set the algorithm.
    header.algorithm = signer.algorithm();
    parts.add(base64Encode(Mapper.serialize(header)));
    parts.add(base64Encode(Mapper.serialize(token)));

    byte[] signature = signer.sign(join(parts));
    parts.add(base64Encode(signature));

    return join(parts);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   base64Encode
  private String join(final Collection<String> collection) {
    return collection.stream().collect(Collectors.joining("."));
  }
}
