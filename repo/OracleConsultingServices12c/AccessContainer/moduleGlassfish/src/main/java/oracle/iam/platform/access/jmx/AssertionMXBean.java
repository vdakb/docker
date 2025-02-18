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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Governance Extension
    Subsystem   :   Identity Governance Authentication

    File        :   AssertionMXBean.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AssertionMXBean.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2015-03-11  DSteding    First release version
*/

package oracle.iam.platform.access.jmx;

import java.io.File;

import org.glassfish.gmbal.Description;
import org.glassfish.gmbal.ManagedObject;
import org.glassfish.gmbal.ManagedAttribute;

////////////////////////////////////////////////////////////////////////////////
// interface AssertionMXBean
// ~~~~~~~~~ ~~~~~~~~~~~~~~~
/**
 ** MXBean declaration for managing Asserter configuration.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@ManagedObject
public interface AssertionMXBean {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final File   PATH              = new File(String.format("%s/config/%s", System.getProperty("com.sun.aas.instanceRoot"), "igs-config.xml"));

  /**
   ** The Microprofile Config key for the name of realm that will be sent via
   ** the <code>WWW-Authenticate</code> header is <code>{@value}</code>.
   */
  static final String REALM             = "igs.verify.realm";

  /**
   ** The Microprofile Config key for the issuer identifier to enable clients to
   ** validate the iss parameter effectively is <code>{@value}</code>.
   */
  static final String ISSUER            = "igs.verify.issuer";

  /**
   ** The Microprofile Config key for the tenant identifier issued when the
   ** application was registered is <code>{@value}</code>.
   */
  static final String TENANT            = "igs.oauth.tenant";

  /**
   ** The Microprofile Config key for the public identifier for apppplication is
   ** <code>{@value}</code>.
   */
  static final String CLIENT            = "igs.oauth.client";

  /**
   ** The Microprofile Config key for the secret known only to the application
   ** and the authorization server is <code>{@value}</code>
   */
  static final String SECRET            = "igs.oauth.secret";

  /**
   ** The Microprofile Config key for the info endpoint is
   ** <code>{@value}</code>.
   */
  static final String ENDPOINT_INFO     = "igs.oauth.infoEndpoint";

  /**
   ** The Microprofile Config key for the PublicKey Material of the signing
   ** authority is <code>{@value}</code>.
   */
  static final String SIGNING_MATERIAL  = "igs.signing.key";
  
  /**
   ** The Microprofile Config key for the PublicKey location of the signing
   ** authority is <code>{@value}</code>.
   */
  static final String SIGNING_LOCATION  = "igs.signing.url";

  /**
   ** The Microprofile Config key for the JNDI name of the JDBC DataSource used
   ** for authentication and authotization purpose is <code>{@value}</code>.
   */
  static final String JNDI_DATASOURCE   = "igs.jndi.datasource";

  /**
   ** The Microprofile Config key for the authentication query used to
   ** authenticate users based on specific key types is <code>{@value}</code>.
   */
  static final String QUERY_PRINCIPAL   = "igs.query.principal";

  /**
   ** The Microprofile Config key for the authentication query used to
   ** authorize users based on specific key types is <code>{@value}</code>.
   */
  static final String QUERY_PERMISSION  = "igs.query.permission";

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setRealm
  /**
   ** Sets the name of realm that will be sent via the
   ** <code>WWW-Authenticate</code> header.
   ** <p>
   ** <b>Note</b>
   ** <br>
   ** This realm name <b>does not</b> couple a named identity store
   ** configuration to the authentication mechanism.
   ** <p>
   ** To set this using Microprofile Config use
   ** <code>igs.verify.realm</code>.
   ** <p>
   ** Required property.
   ** 
   ** @param  value              the name realm that will be sent via the
   **                            <code>WWW-Authenticate</code> header.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  void setRealm(final String value);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getRealm
  /**
   ** Returns the name of realm that will be sent via the
   ** <code>WWW-Authenticate</code> header.
   ** <p>
   ** <b>Note</b>
   ** <br>
   ** This realm name <b>does not</b> couple a named identity store
   ** configuration to the authentication mechanism.
   ** 
   ** @return                    the name realm that will be sent via the
   **                            <code>WWW-Authenticate</code> header.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  String getRealm();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setIssuer
  /**
   ** Sets the issuer identifier of the authorization server which created the
   ** authorization response, as defined in
   ** [<a href="https://www.ietf.org/archive/id/draft-meyerzuselhausen-oauth-iss-auth-resp-00.html#RFC8414">RFC 8414</a>].
   ** <p>
   ** Its value <b>must</b> be a URL that uses the "https" scheme without any
   ** query or fragment components. If the authorization server provides
   ** metadata as defined in
   ** [<a href="https://www.ietf.org/archive/id/draft-meyerzuselhausen-oauth-iss-auth-resp-00.html#RFC8414">RFC 8414</a>],
   ** the value of the parameter iss <b>must</b> be identical to the
   ** authorization server metadata value issuer.
   ** <p>
   ** To set this using Microprofile Config use
   ** <code>igs.oauth.tenant</code>.
   ** <p>
   ** Optional property.
   **
   ** @param  value              the issuer identifier of the authorization
   **                            server which created the authorization
   **                            response.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  void setIssuer(final String value);

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getIssuer
  /**
   ** Returns the issuer identifier of the authorization server which created the
   ** authorization response, as defined in
   ** [<a href="https://www.ietf.org/archive/id/draft-meyerzuselhausen-oauth-iss-auth-resp-00.html#RFC8414">RFC 8414</a>].
   ** <p>
   ** Its value <b>must</b> be a URL that uses the "https" scheme without any
   ** query or fragment components. If the authorization server provides
   ** metadata as defined in
   ** [<a href="https://www.ietf.org/archive/id/draft-meyerzuselhausen-oauth-iss-auth-resp-00.html#RFC8414">RFC 8414</a>],
   ** the value of the parameter iss <b>must</b> be identical to the
   ** authorization server metadata value issuer.
   **
   ** @return                    the issuer identifier of the authorization
   **                            server which created the authorization
   **                            response.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  String getIssuer();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setTenant
  /**
   ** Sets the tenant identifier issued when the application was registered.
   ** <p>
   ** To set this using Microprofile Config use
   ** <code>igs.oauth.tenant</code>.
   ** <p>
   ** Optional property.
   **
   ** @param  value              the issuer identifier of the authorization
   **                            server which created the authorization
   **                            response.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ManagedAttribute(id="tenant")
  @Description("The tenant identifier issued when the application was registered.")
  void setTenant(final String value);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getTenant
  /**
   ** Returns the tenant identifier issued when the application was registered.
   **
   ** @return                    the issuer identifier of the authorization
   **                            server which created the authorization
   **                            response.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  String getTenant();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setClient
  /**
   ** Sets the client identifier issued when the application was registered.
   ** <p>
   ** To set this using Microprofile Config use
   ** <code>igs.oauth.client</code>.
   ** <p>
   ** Optional property.
   **
   ** @param  value              the client identifier issued when the
   **                            application was registered.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ManagedAttribute(id="client")
  @Description("The client identifier issued when the application was registered.")
  void setClient(final String value);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getClient
  /**
   ** Returns the client identifier issued when the application was registered.
   **
   ** @return                    the client identifier issued when the
   **                            application was registered.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  String getClient();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSecret
  /**
   ** Sets the client secret issued when the application was registered.
   ** <p>
   ** To set this using Microprofile Config use
   ** <code>igs.oauth2..clientSecret</code>
   ** <p>
   ** Optional property.
   **
   ** @param  value              the client identifier issued when the
   **                            application was registered.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ManagedAttribute(id="secret")
  void setSecret(final String value);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSecret
  /**
   ** Returns the client secret issued when the application was registered.
   **
   ** @return                    the client secret issued when the application
   **                            was registered.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  String getSecret();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setMaterial
  /**
   ** Sets the PEM encoded certificate as the source of the signing
   ** authority.
   ** </p>
   ** To set this using Microprofile Config use
   ** <code>igs.signing.key</code>.
   ** <p>
   ** Optional property.
   **
   ** @param  value              the PEM encoded certificate as the source of
   **                            the signing authority.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ManagedAttribute(id="material")
  void setMaterial(final String value);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getMaterial
  /**
   ** Returns the PEM encoded certificate as the source of the signing
   ** authority.
   **
   ** @return                    the PEM encoded certificate as the source of
   **                            the signing authority.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  String getMaterial();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setLocation
  /**
   ** Sets the location of the <code>PublicKey</code> of the signing authority.
   ** <p>
   ** The value <b>must</b> represent a URL using either <code>https</code> in
   ** the case using a Remote JSON Web Key (JWK) as the source, or
   ** <code>file</code> when using a PEM encoded certificate as the source to
   ** retrieve the public key material.
   ** <p>
   ** To set this using Microprofile Config use
   ** <code>igs.signing.url</code>.
   ** <p>
   ** Optional property.
   **
   ** @param  value              the PublicKey Material of the signing
   **                            authority.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ManagedAttribute(id="location")
  void setLocation(final String value);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLocation
  /**
   ** Returns the PublicKey local filesystem location of the signing authority.
   **
   ** @return                    the PublicKey Material of the signing
   **                            authority.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  String getLocation();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setInfoEndpoint
  /**
   ** Sets the URL for the OAuth2 Authentication Provider to provide
   ** authentication.
   ** <p>
   ** This must be a https endpoint.
   ** </p>
   ** To set this using Microprofile Config use
   ** <code>igs.oauth2..infoEndpoint</code>.
   ** <p>
   ** Optional property.
   **
   ** @param  value              the URL for the OAuth2 Provider to provide
   **                            authentication.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ManagedAttribute(id="infoEndpoint")
  void setInfoEndpoint(final String value);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getInfoEndpoint
  /**
   ** Returns the URL for the OAuth2 Authentication Provider to provide
   ** authentication.
   **
   ** @return                    the URL for the OAuth2 Provider to provide
   **                            authentication.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  String getInfoEndpoint();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDataSource
  /**
   ** Sets the JNDI name of the JDBC DataSource used for authentication and
   ** authotization purpose.
   ** </p>
   ** To set this using Microprofile Config use
   ** <code>igs.dataSource</code>.
   ** <p>
   ** Required property.
   **
   ** @param  value              the JNDI name of the JDBC DataSource used for
   **                            authentication and authotization purpose.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ManagedAttribute(id="dataSource")
  void setDataSource(final String value);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDataSource
  /**
   ** Returns the JNDI name of the JDBC DataSource used for authentication and
   ** authotization purpose.
   **
   ** @return                    the JNDI name of the JDBC DataSource used for
   **                            authentication and authotization purpose.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  String getDataSource();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPrincipalQuery
  /**
   ** Sets the query used to authenticate users based on specific key types.
   ** <p>
   ** To set this using Microprofile Config use
   ** <code>igs.query.principal</code>.
   ** <p>
   ** Required property.
   **
   ** @param  value              the query used to authenticate users based on
   **                            specific key types.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ManagedAttribute(id="principalQuery")
  void setPrincipalQuery(final String value);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPrincipalQuery
  /**
   ** Returns the authentication query used to authenticate users based on
   ** specific key types.
   **
   ** @return                    the query used to authenticate users based on
   **                            specific key types.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  String getPrincipalQuery();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPermissionQuery
  /**
   ** Sets the query used to authorize users based on specific key types.
   ** </p>
   ** To set this using Microprofile Config use
   ** <code>igs.query.permission</code>.
   ** <p>
   ** Required property.
   **
   ** @param  value              the query used to authorize users based on
   **                            specific key types.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ManagedAttribute(id="permissionQuery")
  void setPermissionQuery(final String value);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPermissionQuery
  /**
   ** Returns the query used to authorize users based on specific key types.
   **
   ** @return                    the query used to authorize users based on
   **                            specific key types.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  String getPermissionQuery();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refresh
  /**
   ** Refresh the configuration from the fileystem.
   */
  void refresh();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   save
  /**
   ** Save the configuration to the file system.
   */
  void save();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** Validate the consistency of the configuration.
   */
  void validate();
}