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

    File        :   BearerAsserterConfiguration.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    BearerAsserterConfiguration.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.jmx;

import java.util.List;

////////////////////////////////////////////////////////////////////////////////
// class BearerAsserterConfiguration
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The base calass of configuration provider as a JMX management
 ** implementation.
 ** <p>
 ** Subclasses needs to be annotated by <code>@Singleton</code> to get observed
 ** by a CDI implementation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class BearerAsserterConfiguration extends    TokenAsserterConfiguration
                                         implements BearerAsserterConfigurationMBean {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-3338424867803498456")
  private static final long serialVersionUID = -3829228016797772898L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>BearerAsserterConfiguration</code> that use the
   ** specified <code>realm</code> parameter later for realm that will be
   ** sent via the <code>WWW-Authenticate</code> header.
   ** <br>
   ** <b>Note</b>
   ** <br>
   ** This realm name <b>does not</b> couple a named identity store
   ** configuration to the authentication mechanism.
   ** <p>
   ** The parameter <code>realm</code> its also used to maintain the persisted
   ** configuration.
   **
   ** @param  realm              the realm that will be sent via the
   **                            <code>WWW-Authenticate</code> header and used
   **                            as the name of the file placed in the domain
   **                            instance configuration directory to persist
   **                            the configuration.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public BearerAsserterConfiguration(final String realm) {
    // ensure inheritance
    super(realm);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setIssuer (BearerAsserterConfigurationMBean)
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
  @Override
  public void setIssuer(final String value) {
    extend(ISSUER, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getIssuer (BearerAsserterConfigurationMBean)
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
  @Override
  public String getIssuer() {
    return propertyString(ISSUER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAudience (BearerAsserterConfigurationMBean)
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
  @Override
  public void setAudience(final List<String> value) {
    extend(AUDIENCE, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getAudience (BearerAsserterConfigurationMBean)
  /**
   ** Returns the audience identifier of the authorization server which created
   ** the authorization response, as defined in
   ** [<a href="https://www.ietf.org/archive/id/draft-meyerzuselhausen-oauth-iss-auth-resp-00.html#RFC8414">RFC 8414</a>].
   **
   ** @return                    the audience identifier of the authorization
   **                            server which created the authorization
   **                            response.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link String}.
   */
  @Override
  public List<String> getAudience() {
    return propertyList(AUDIENCE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDomain (AssertionMXBean)
  /**
   ** Sets the identity domain identifier issued when the application was
   ** registered.
   ** <p>
   ** Optional property.
   ** <p>
   ** To set this property use {@link #DOMAIN oauthDomain}.
   **
   ** @param  value              the identity domain identifier of the
   **                            authorization
   **                            server which created the authorization
   **                            response.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public void setDomain(final String value) {
    extend(DOMAIN, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDomain (AssertionMXBean)
  /**
   ** Returns the identity domain identifier issued when the application was
   ** registered.
   **
   ** @return                    the issuer identifier of the authorization
   **                            server which created the authorization
   **                            response.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String getDomain() {
    return propertyString(DOMAIN);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setClient (BearerAsserterConfigurationMBean)
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
  @Override
  public void setClient(final String value) {
    extend(CLIENT, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getClient (AssertionMXBean)
  /**
   ** Returns the client identifier issued when the application was registered.
   **
   ** @return                    the client identifier issued when the
   **                            application was registered.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String getClient() {
    return propertyString(CLIENT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSecret (AssertionMXBean)
  /**
   ** Sets the client secret issued when the application was registered.
   ** <p>
   ** Optional property.
   ** <p>
   ** To set this property use {@link #SECRET oauthSecret}.
   **
   ** @param  value              the client identifier issued when the
   **                            application was registered.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public void setSecret(final String value) {
    extend(SECRET, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSecret (BearerAsserterConfigurationMBean)
  /**
   ** Returns the client secret issued when the application was registered.
   **
   ** @return                    the client secret issued when the application
   **                            was registered.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String getSecret() {
    return propertyString(SECRET);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setInfoEndpoint (BearerAsserterConfigurationMBean)
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
  @Override
  public void setInfoEndpoint(final String value) {
    extend(ENDPOINT_INFO, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getInfoEndpoint (BearerAsserterConfigurationMBean)
  /**
   ** Returns the URL for the OAuth2 Authentication Provider to identity token
   ** information.
   **
   ** @return                    the URL for the OAuth2 Provider to provide
   **                            identity token information.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String getInfoEndpoint() {
    return propertyString(ENDPOINT_INFO);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setProfileEndpoint (BearerAsserterConfigurationMBean)
  /**
   ** Sets the URL for the OAuth2 Authentication Provider to identity token
   ** information.
   ** <p>
   ** This <b>must</b> be a https endpoint.
   ** <p>
   ** Optional property.
   ** <p>
   ** To set this property use {@link #ENDPOINT_PROFILE oauthProfileEndpoint}.
   **
   ** @param  value              the URL for the OAuth2 Provider to provide
   **                            identity token information.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public void setProfileEndpoint(final String value) {
    extend(ENDPOINT_PROFILE, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getProfileEndpoint (BearerAsserterConfigurationMBean)
  /**
   ** Returns the URL for the OAuth2 Authentication Provider to provide identity
   ** profile information.
   **
   ** @return                    the URL for the OAuth2 Provider to provide
   **                            identity profile information..
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String getProfileEndpoint() {
    return propertyString(ENDPOINT_PROFILE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (overridden)
  /**
   ** Refresh the configuration from the fileystem.
   */
  @Override
  public final void validate() {
    // ensure inheritance
    super.validate();
    validateJsonWebKey();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize (overridden)
  /**
   ** Initialize the configuration with default values.
   */
  @Override
  protected void initialize() {
    // ensure inheritance
    super.initialize();

    // extend the configuration with the module specific options
    extend(ISSUER,           "<specify>");
    extend(AUDIENCE,         this.realm);
    extend(DOMAIN,           "<specify>");
    extend(CLIENT,           "<specify>");
    extend(SECRET,           "<specify>");
    extend(SIGNING_LOCATION, "<specify>");
    extend(ENDPOINT_INFO,    "<specify>");
    extend(ENDPOINT_PROFILE, "<specify>");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateJsonWebKey
  /**
   ** Validates the configuration for consistency to ensure that if a remote URL
   ** is configured for <code>igs.signing.location</code> (either http or https)
   ** an Identity Domain is also contained in the configuration.
   **
   ** @throws IllegalStateException if the rule is violated.
   */
  private void validateJsonWebKey() {
    final String domain   = getDomain();
    final String location = getLocation();
    if (location.startsWith("http") && (domain == null || domain.isEmpty()))
      throw new IllegalStateException("The oauthDomain cannot be empty if signingLocation belongs to an URL.");
  }
}