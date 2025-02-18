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

    System      :   Identity Governance Extension
    Subsystem   :   Identity Governance Service

    File        :   BearerAsserterConfigurationMBean.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    BearerAsserterConfigurationMBean.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.jmx;

import java.util.List;

import org.glassfish.gmbal.Description;
import org.glassfish.gmbal.ManagedObject;
import org.glassfish.gmbal.ManagedAttribute;

////////////////////////////////////////////////////////////////////////////////
// interface BearerAsserterConfigurationMBean
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** MXBean declaration for managing OAuth2 Token Asserter configuration.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@ManagedObject
public interface BearerAsserterConfigurationMBean extends TokenAsserterConfigurationMBean {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The Microprofile Config key for the issuer identifier to enable clients to
   ** validate the iss parameter effectively is <code>{@value}</code>.
   */
  static final String ISSUER           = "oauthIssuer";

  /**
   ** The Microprofile Config key for the audience identifier to enable clients
   ** to validate the aud parameter effectively is <code>{@value}</code>.
   */
  static final String AUDIENCE         = "oauthAudience";

  /**
   ** The Microprofile Config key for the identity domain identifier issued when
   ** the application was registered is <code>{@value}</code>.
   */
  static final String DOMAIN           = "oauthDomain";

  /**
   ** The Microprofile Config key for the public identifier for apppplication is
   ** <code>{@value}</code>.
   */
  static final String CLIENT           = "oauthClient";

  /**
   ** The Microprofile Config key for the secret known only to the application
   ** and the authorization server is <code>{@value}</code>
   */
  static final String SECRET           = "oauthSecret";

  /**
   ** The Microprofile Config key for the info endpoint is
   ** <code>{@value}</code>.
   */
  static final String ENDPOINT_INFO    = "oauthInfoEndpoint";

  /**
   ** The Microprofile Config key for the info endpoint is
   ** <code>{@value}</code>.
   */
  static final String ENDPOINT_PROFILE = "oauthProfileEndpoint";

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

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
   ** Optional property.
   ** <p>
   ** To set this property use {@link #ISSUER oauthIssuer}.
   **
   ** @param  value              the issuer identifier of the authorization
   **                            server which created the authorization
   **                            response.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ManagedAttribute(id="issuer")
  @Description("The issuer identifier of the authorization server which created the authorization response.")
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
  // Method:   setAudience
  /**
   ** Sets the audience identifier of the authorization server which created the
   ** authorization response, as defined in
   ** [<a href="https://www.ietf.org/archive/id/draft-meyerzuselhausen-oauth-iss-auth-resp-00.html#RFC8414">RFC 8414</a>].
   ** <p>
   ** Optional property.
   ** <p>
   ** To set this property use {@link #AUDIENCE oauthAudience}.
   **
   ** @param  value              the issuer identifier of the authorization
   **                            server which created the authorization
   **                            response.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link String}.
   */
  @ManagedAttribute(id="audience")
  @Description("The audience identifier of the authorization server which created the authorization response.")
  void setAudience(final List<String> value);

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getAudience
  /**
   ** Returns the audience identifier of the authorization server which created the
   ** authorization response, as defined in
   ** [<a href="https://www.ietf.org/archive/id/draft-meyerzuselhausen-oauth-iss-auth-resp-00.html#RFC8414">RFC 8414</a>].
   **
   ** @return                    the audience identifier of the authorization
   **                            server which created the authorization
   **                            response.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link String}.
   */
  List<String> getAudience();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDomain
  /**
   ** Sets the identity domain identifier issued when the application was
   ** registered.
   ** <p>
   ** Optional property.
   ** <p>
   ** To set this property use {@link #DOMAIN oauthDomain}.
   **
   ** @param  value              the identity domain identifier of the
   **                            authorization server which created the
   **                            authorization response.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ManagedAttribute(id="tenant")
  @Description("The identity domain identifier issued when the application was registered.")
  void setDomain(final String value);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDomain
  /**
   ** Returns the identity domain identifier issued when the application was
   ** registered.
   **
   ** @return                    the identity domain identifier of the
   **                            authorization server which created the
   **                            authorization response.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  String getDomain();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setClient
  /**
   ** Sets the client identifier issued when the application was registered.
   ** <p>
   ** Optional property.
   ** <p>
   ** To set this property use {@link #CLIENT oauthClient}.
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
   ** Optional property.
   ** <p>
   ** To set this property use {@link #SECRET oauthSecret}.
   **
   ** @param  value              the client secret issued when the
   **                            application was registered.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ManagedAttribute(id="secret")
  @Description("The client secret issued when the application was registered.")
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
  // Method:   setInfoEndpoint
  /**
   ** Sets the URL for the OAuth2 Authentication Provider to identity token
   ** information.
   ** <p>
   ** This <b>must</b> be a https endpoint.
   ** <p>
   ** Optional property.
   ** <p>
   ** To set this property use {@link #ENDPOINT_INFO oauthInfoEndpoint}.
   **
   ** @param  value              the URL for the OAuth2 Provider to provide
   **                            identity token information.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ManagedAttribute(id="infoEndpoint")
  void setInfoEndpoint(final String value);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getInfoEndpoint
  /**
   ** Returns the URL for the OAuth2 Authentication Provider to identity
   ** token information.
   **
   ** @return                    the URL for the OAuth2 Provider to provide
   **                            identity token information.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  String getInfoEndpoint();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setInfoEndpoint
  /**
   ** Sets the URL for the OAuth2 Authentication Provider to provide identity
   ** profile information.
   ** <p>
   ** This <b>must</b> be a https endpoint.
   ** <p>
   ** Optional property.
   ** <p>
   ** To set this property use {@link #ENDPOINT_INFO oauthProfileEndpoint}.
   **
   ** @param  value              the URL for the OAuth2 Provider to provide
   **                            identity profile information..
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ManagedAttribute(id="profileEndpoint")
  void setProfileEndpoint(final String value);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getProfileEndpoint
  /**
   ** Returns the URL for the OAuth2 Authentication Provider to provide identity
   ** profile information.
   **
   ** @return                    the URL for the OAuth2 Provider to provide
   **                            identity profile information..
   **                            <br>
   **                            Possible object is {@link String}.
   */
  String getProfileEndpoint();
}