/*
    Oracle Deutschland BV & Co. KG

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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Manager
    Subsystem   :   OpenIdConnect Extension

    File        :   Extension.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   jovan.j.lakic@oracle.com

    Purpose     :   This file implements the class
                    Extension.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-19-07  JLakic     First release version
*/

package bka.iam.access.oidc.extension;

import java.util.Set;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.ClientBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;

import bka.iam.access.oidc.extension.utils.WLSUtil;

////////////////////////////////////////////////////////////////////////////////
// class Extension
// ~~~~~ ~~~~~~~~~
/**
 ** Base class used in OIDC Extensions. It comes with support methods
 **
 ** @author  tomas.t.sebo@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface Extension {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String        DOMAIN_PATH   = "/oauth/IdentityDomain/";
  static final String        TEMPLATE_PATH = "/templates/";
  static final String        KEYSTORE_PATH = "/IDM/SecurityDomains/Default/OAM/keystore/";

  static final ClientConfig  CONFIG        = new ClientConfig()
    .property(ClientProperties.CONNECT_TIMEOUT,  30000)
    .property(ClientProperties.READ_TIMEOUT,     10000)
    .property(ClientProperties.FOLLOW_REDIRECTS, Boolean.TRUE)
  ;
  static final Client        CLIENT        = ClientBuilder.newClient(CONFIG);

  static final ObjectMapper  MAPPER        = new ObjectMapper()
    // don't serialize POJO nulls as JSON nulls
    .setSerializationInclusion(JsonInclude.Include.NON_NULL)
    // only use xsd:dateTime format for dates
    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    // take care about case when de-serializing POJOs
    .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, false)
    // ignore unknown properties in Jackson
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  ;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////////
  // interface Endpoint
  // ~~~~~~~~~ ~~~~~~~~
  /**
   ** Endpoints invoked by the authentication and authoritation flows.
   */
  interface Endpoint {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    /**
     ** The resource service endpoint on Access Manager tc issue a token.
     */
    static final WebTarget TOKEN    = CLIENT.target(WLSUtil.serverURL()).path("oauth2/rest/token");
    /**
     ** The resource service endpoint on Access Manager to obtain detailed
     ** information contained in a token.
     */
    static final WebTarget INFO     = TOKEN.path("info");
    /**
     ** The resource service endpoint on Access Manager to exchange tokens.
     */
    static final WebTarget EXCHANGE = TOKEN.path("exchange");
    /**
     ** OAM Client RESTFull enddpoint URL on AdminServer
     */
    static final String SERVICE  = "/oam/services/rest/ssa/api/v1/oauthpolicyadmin/client";
  }

  ////////////////////////////////////////////////////////////////////////////////
  // interface Header
  // ~~~~~~~~~ ~~~~~~
  /**
   ** HTTP Request Header.
   */
  interface Header {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    static final String AUTHORIZATION  = "authorization";
    static final String IDENTITYDOMAIN = "x-oauth-identity-domain-name";
  }

  ////////////////////////////////////////////////////////////////////////////////
  // interface Parameter
  // ~~~~~~~~~ ~~~~~~~~~
  /**
   ** Name of query and form parameters.
   */
  interface Parameter {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    static final String ACCESS_TOKEN  = "access_token";
  }

  ////////////////////////////////////////////////////////////////////////////////
  // enum GrantType
  // ~~~~ ~~~~~~~~~
  /**
   ** The Types of a token grant.
   */
  enum GrantType {
      /** The grant type for autorization code. */
      AUTHORIZATION("AUTHORIZATION_CODE")
      /** The grant type for refresh token. */
    , REFRESH("REFRESH_TOKEN")
      /** The grant type for bearer token. */
    , BEARER("JWT_BEARER")
      /** The grant type for password credential. */
    , PASSWORD("PASSWORD")
      /** The grant type for client credential. */
    , CREDENTIAL("CLIENT_CREDENTIALS")
      /** The grant type for token exchange. */
    , TOKEN_EXCHANGE("TOKEN_EXCHANGE")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    public final String id;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>GrantType</code> with a constraint value.
     **
     ** @param  value            the constraint name of the object.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    GrantType(final String value) {
      this.id = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Factory method to create a proper <code>GrantType</code> constraint from
     ** the given string value.
     **
     ** @param  value            the string value the order constraint should be
     **                          returned for.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>GrantType</code> constraint.
     **                          <br>
     **                          Possible object is <code>GrantType</code>.
     */
    public static GrantType from(final String value) {
      for (GrantType cursor : GrantType.values()) {
        if (cursor.id.equalsIgnoreCase(value))
          return cursor;
      }
      throw new IllegalArgumentException(value);
    }
  }

  ////////////////////////////////////////////////////////////////////////////////
  // enum Algorithm
  // ~~~~ ~~~~~~~~~
  /**
   ** Available JSON Web Algorithms (JWA) as described in RFC 7518 available for
   ** this JWT implementation.
   */
  public enum Algorithm {
      /**
       ** No digital signature or MAC performed.
       */
      NONE("None")
      /**
       ** ECDSA using P-256 and SHA-256
       ** OID: 1.2.840.10045.3.1.7
       ** - prime256v1 / secp256r1
       */
    , ES256("SHA256withECDSA")
      /**
       ** ECDSA using P-384 and SHA-384
       ** OID: 1.3.132.0.34
       ** - secp384r1 / secp384r1
       */
    , ES384("SHA384withECDSA")
      /**
       ** ECDSA using P-521 and SHA-512
       ** OID: 1.3.132.0.35
       ** - prime521v1 / secp521r1
       */
    , ES512("SHA512withECDSA")
      /**
       ** HMAC using SHA-256
       */
    , HS256("HmacSHA256")
      /**
       ** HMAC using SHA-384
       */
    , HS384("HmacSHA384")
     /**
      ** HMAC using SHA-512
      */
    , HS512("HmacSHA512")
      /**
       ** RSASSA-PSS using SHA-256 and MGF1 with SHA-256
       ** - SHA256withRSAandMGF1
       */
    , PS256("SHA-256")
     /**
      ** RSASSA-PSS using SHA-384 and MGF1 with SHA-384
      ** - SHA384withRSAandMGF1
      */
    , PS384("SHA-384")
      /**
       ** RSASSA-PSS using SHA-512 and MGF1 with SHA-512
       ** - SHA512withRSAandMGF1
       */
    , PS512("SHA-512")
      /**
       ** RSASSA-PKCS1-v1_5 using SHA-256
       */
    , RS256("SHA256withRSA")
      /**
       ** RSASSA-PKCS1-v1_5 using SHA-384
       */
    , RS384("SHA384withRSA")
      /**
       ** RSASSA-PKCS1-v1_5 using SHA-512
       */
    , RS512("SHA512withRSA")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    public final String id;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Algorithm</code> with a constraint value.
     **
     ** @param  value            the constraint name of the object.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    Algorithm(final String value) {
      this.id = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Factory method to create a proper <code>GrantType</code> constraint from
     ** the given string value.
     **
     ** @param  value            the string value the order constraint should be
     **                          returned for.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>GrantType</code> constraint.
     **                          <br>
     **                          Possible object is <code>GrantType</code>.
     */
    public static Algorithm from(final String value) {
      for (Algorithm cursor : Algorithm.values()) {
        if (cursor.id.equals(value)) {
          return cursor;
        }
      }
      throw new IllegalArgumentException(value);
    }
  }
  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   headerValue
  /**
   ** Return HTTP header value from HTTP request where HTTP Header name is not
   ** case sensitive.
   **
   ** @param  headers            all HTTP Headers which are part of HTTP
   **                            request.
   ** @param  name               the name of HTTP header.
   **
   ** @return                    the value of the HTTP Header mapped at
   **                            <code>name</code>.
   **/
  default String headerValue(final MultivaluedMap<String, String> headers, final String name) {
    String value = null;
    Set<String> keys = headers.keySet();
    for (String key : keys) {
      if (key.equalsIgnoreCase(name)) {
        List<String> list = headers.get(key);
        value = list.get(0);
        break;
      }
    }
    return value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmarshal
  /**
   ** Method to deserialize JSON content from given JSON content String.
   **
   ** @param  <T>                the expected node type.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  payload            the string repesentation to transforms.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  type               the {@link Class} type of the value to
   **                            transforms.
   **                            <br>
   **                            Allowed object is {@link Class}.
   **
   ** @return                    the transformed POJO.
   **                            <br>
   **                            Possible object is <code>T</code>.
   **
   ** @throws IllegalArgumentException if an error occurs while binding the JSON
   **                                  node to the value type.
   */
  static <T> T unmarshal(final String payload, final Class<T> type) {
    try {
      return MAPPER.readValue(payload, type);
    }
    catch (JsonProcessingException e) {
      throw new IllegalArgumentException(e.getMessage(), e);
    }
  }
}