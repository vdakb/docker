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

    File        :   JsonWebKeySet.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    JsonWebKeySet.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.oauth.jwk;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.Objects;
import java.util.Collection;
import java.util.LinkedList;

import java.util.stream.Collectors;

import java.util.function.Function;

import javax.json.Json;
import javax.json.JsonValue;
import javax.json.JsonArray;
import javax.json.JsonString;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

import oracle.hst.platform.core.marshal.JsonMarshaller;

////////////////////////////////////////////////////////////////////////////////
// class JsonWebKeySet
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** Represented by a JSON object that contains a collection of
 ** {@link JsonWebKey JSON Web Keys} as the value of its <code>keys</code>
 ** member.
 ** <p>
 ** Additional (custom) members of the JSON Web Key Set JSON object are also
 ** supported.
 ** <br>
 ** It serializes to a JSON object.
 ** <p>
 ** Example Json Web Key set:
 ** <pre>
 **   { "keys": [
 **       { "kty" : "EC"
 **       , "crv" : "P-256"
 **       , "x"   : "MKBCTNIcKUSDii11ySs3526iDZ8AiTo7Tu6KPAqv7D4"
 **       , "y"   : "4Etl6SRW2YiLUrN5vfvVHuhp7x8PxltmWWlbbM4IFyM"
 **       , "use" : "enc"
 **       , "kid" : "1"
 **       }
 **     , { "kty" : "RSA"
 **       , "n"   : "0vx7agoebGcQSuuPiLJXZptN9nndrQmbXEps2aiAFbWhM78LhWx
 **                  4cbbfAAtVT86zwu1RK7aPFFxuhDR1L6tSoc_BJECPebWKRXjBZCiFV4n3oknjhMs
 **                  tn64tZ_2W-5JsGY4Hc5n9yBXArwl93lqt7_RN5w6Cf0h4QyQ5v-65YGjQR0_FDW2
 **                  QvzqY368QQMicAtaSqzs8KJZgnYb9c7d0zgdAZHzu6qMQvRL5hajrn1n91CbOpbI
 **                  SD08qNLyrdkt-bFTWhAI4vMQFh6WeZu0fM4lFd2NcRwr3XPksINHaQ-G_xBniIqb
 **                  w0Ls1jF44-csFCur-kEgU8awapJzKnqDKgw"
 **       , "e"   : "AQAB"
 **       , "kid" : "2011-04-29"
 **       }
 **     ]
 **   }
 ** </pre>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class JsonWebKeySet extends HashMap<String, JsonWebKey> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** JWK parameter for keys.
   */
  public static final String        KEY              = "keys";  

  /**
   ** The MIME type of {@link JsonWebKey JSON Web Keys} set objects: 
   ** <br>
   ** <code>application/jwk-set+json; charset=UTF-8</code>
   */
  public static final String        MIME             = "application/jwk-set+json; charset=UTF-8";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The additional custom members. */
  private final Map<String, Object> member           = new HashMap<String,Object>();

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-7920595607418637163")
  private static final long         serialVersionUID = 5111404126758182776L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>JsonWebKeySet</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private JsonWebKeySet() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new <code>JsonWebKeySet</code> set with a single
   ** {@link JsonWebKeySet}.
   **
   ** @param  key                the initial <code>JsonWebKeySet</code>.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link JsonWebKey}.
   */
  private JsonWebKeySet(final JsonWebKey key) {
    // ensure inheritance
    super();

    // initialize instance attributes
    super.put(key.kid, key);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new <code>JsonWebKeySet</code> set with the specified
   ** collection of {@link JsonWebKey}s.
   **
   ** @param  keys               the collection of {@link JsonWebKey}.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link JsonWebKey}.
   */
  private JsonWebKeySet(final List<JsonWebKey> keys) {
    // ensure inheritance
    super();

    // initialize instance attributes
    super.putAll(keys.stream().collect(Collectors.toMap(k -> k.kid, Function.identity())));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   key
  /**
   ** Returns the keys (ordered) of this <code>JsonWebKeySet</code>.
   **
   ** @return                    the keys (ordered) of this
   **                            <code>JsonWebKeySet</code>.
   **                            <br>
   **                            Possible object is {@link Collection} where
   **                            each element is of type {@link JsonWebKey}.
   */
  public final Collection<JsonWebKey> key() {
    return super.values();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   member
  /**
   ** Returns the additional custom members of this <code>JsonWebKeySet</code>.
   **
   ** @return                    the additional custom members of this
   **                            <code>JsonWebKeySet</code>.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link String} as the value.
   */
  public final Map<String, Object> member() {
    return this.member;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   from
  /**
   ** Parses a <code>JsonWebKeySet</code> from the specified string
   ** representation.
   **
   ** @param  value              the JSON object string to parse.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the parsed <code>JsonWebKeySet</code>.
   **                            <br>
   **                            Possible object is <code>JsonWebKeySet</code>.
   **
   **
   ** @throws NullPointerException if <code>value</code> is <code>null</code>.
   */
  public static JsonWebKeySet from(final String value) {
    // prevent bogus input
    return from(JsonMarshaller.readObject(Objects.requireNonNull(value, "The Json Object string must not be null")));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   from
  /**
   ** Factory method to create a <code>JsonWebKeySet</code> from the specified
   ** {@link JsonObject} representation.
   **
   ** @param  object             the JSON object to parse.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   **
   ** @return                    the parsed <code>JsonWebKeySet</code>.
   **                            <br>
   **                            Possible object is <code>JsonWebKeySet</code>.
   **
   **
   ** @throws IllegalArgumentException if <code>object</code> is
   **                                  <code>null</code> or empty.
   */
  public static JsonWebKeySet from(final JsonObject object)
    throws IllegalArgumentException {

    // prevent bogus input
    final JsonArray        a = Objects.requireNonNull(object, "The Json Object must not be null").getJsonArray(KEY);
    final List<JsonWebKey> c = new LinkedList<JsonWebKey>();
    a.forEach(
      i -> {
        if (!(i instanceof JsonObject))
          throw new IllegalArgumentException("The \"keys\" JSON array must contain JSON objects only");

        try {
          c.add(JsonWebKey.from((JsonObject)i));
        }
        catch (IllegalArgumentException e) {
          throw new IllegalArgumentException("Invalid JsonWebKey: " + e.getMessage());
        }
      }
    );
    final JsonWebKeySet set = of(c);
    // parse additional custom members
    for (Map.Entry<String, JsonValue> entry : object.entrySet()) {
      if (entry.getKey() == null || entry.getKey().equals(KEY))
        continue;
      JsonValue value = entry.getValue();
      switch (value.getValueType()) {
        case STRING : set.member.put(entry.getKey(),((JsonString)value).getString());
                      break;
        case NUMBER : set.member.put(entry.getKey(),((JsonNumber)value).bigDecimalValue());
                      break;
        case TRUE   : set.member.put(entry.getKey(), true);
                      break;
        case FALSE  : set.member.put(entry.getKey(), false);
                      break;
        case NULL   : set.member.put(entry.getKey(), null);
                      break;
        default     : set.member.put(entry.getKey(), value.toString());
                      break;
      }
    }
    return set;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create an empty <code>JsonWebKeySet</code>.
   **
   ** @return                    the created empty <code>JsonWebKeySet</code>.
   **                            <br>
   **                            Possible object is <code>JsonWebKeySet</code>.
   **
   ** @throws IllegalArgumentException if <code>name</code> is <code>null</code>
   **                                  or empty.
   */
  public static JsonWebKeySet build() {
    return new JsonWebKeySet();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create an empty <code>JsonWebKeySet</code> with a single
   ** key.
   **
   ** @param  key                the initial <code>JsonWebKeySet</code>.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link JsonWebKey}.
   **
   ** @return                    the created empty <code>JsonWebKeySet</code>.
   **                            <br>
   **                            Possible object is <code>JsonWebKeySet</code>.
   **
   ** @throws IllegalArgumentException if <code>name</code> is <code>null</code>
   **                                  or empty.
   */
  public static JsonWebKeySet of(final JsonWebKey key) {
    // prevent bogus input
    if (key == null)
      throw new IllegalArgumentException("The JsonWebKey must not be null");
    
    return new JsonWebKeySet(key);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create an empty <code>JsonWebKeySet</code> with the
   ** specified collection of {@link JsonWebKey}.
   **
   ** @param  keys               the collection of {@link JsonWebKey}.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link JsonWebKey}.
   **
   ** @return                    the created empty <code>JsonWebKeySet</code>.
   **                            <br>
   **                            Possible object is <code>JsonWebKeySet</code>.
   **
   ** @throws IllegalArgumentException if <code>name</code> is <code>null</code>
   **                                  or empty.
   */
  public static JsonWebKeySet of(final List<JsonWebKey> keys) {
    // prevent bogus input
    if (keys == null)
      throw new IllegalArgumentException("The JsonWebKey collection must not be null");
    
    return new JsonWebKeySet(keys);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns the string representation for the <code>JsonWebKey</code> value in
   ** its minimal form, without any additional whitespace.
   **
   ** @return                    a string representation that represents this
   **                            literal.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String toString() {
    return jsonify().build().toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   jsonify
  /**
   ** Returns a JSON object representation of this <code>JsonWebKeySet</code>.
   ** <p>
   ** This method is intended to be called from extending classes.
   ** <br>
   ** The result is guaranteed to be a valid input for the method
   ** {@code JsonReader#readObject(Reader)} and to create a value that is
   ** <em>equal</em> to this object.
   **
   ** @return                    the JSON object representation.
   **                            <br>
   **                            Possible object is {@link JsonObjectBuilder}.
   */
  public JsonObjectBuilder jsonify() {
//    final JsonArrayBuilder  a = Json.createArrayBuilder(this.key.stream().map(e -> e.jsonify()).collect(Collectors.toList()));
    final JsonArrayBuilder  a =  Json.createArrayBuilder();
    for (JsonWebKey cursor : values()) {
      a.add(cursor.jsonify());
    }
//    final JsonObjectBuilder m = Json.createObjectBuilder(this.member);
//    return Json.createObjectBuilder().add(KEY, a).addAll(m);    final JsonObjectBuilder m = Json.createObjectBuilder();
    final JsonObjectBuilder m = Json.createObjectBuilder();
    m.add(KEY, a);
    for (Map.Entry<String, Object> entry : this.member.entrySet()) {
      m.add(entry.getKey(), entry.getValue().toString());
    }
    return m;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Adds a single {@link JsonWebKey} to the embedded collection of
   ** {@link JsonWebKey}s.
   **
   ** @param  key                the {@link JsonWebKey} to add.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link JsonWebKey}.
   **
   ** @return                    the <code>JsonWebKeySet</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>JsonWebKeySet</code>.
   **
   ** @throws IllegalArgumentException if <code>use</code> is <code>null</code>
   **                                  or empty or does not match.
   */
  public final JsonWebKeySet add(final JsonWebKey key) {
    // prevent bogus input
    if (key == null)
      throw new IllegalArgumentException("The JsonWebKey must not be null");

    super.put(key.kid, key);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Adds a additional custom members of this <code>JsonWebKeySet</code>.
   **
   ** @param  key                the key of the key-value pair to add.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @param  value              the value of the key-value pair to add.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>JsonWebKeySet</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>JsonWebKeySet</code>.
   **
   ** @throws IllegalArgumentException if <code>use</code> is <code>null</code>
   **                                  or empty or does not match.
   */
  public final JsonWebKeySet add(final String key, final String value) {
    // prevent bogus input
    if (key == null)
      throw new IllegalArgumentException("The key of the custom member must not be null");

    this.member.put(key, value);
    return this;
  }
}