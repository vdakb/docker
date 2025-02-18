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

    File        :   AssertionConfiguration.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AssertionConfiguration.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2015-03-11  DSteding    First release version
*/

package oracle.iam.platform.access.jmx;

import java.lang.management.ManagementFactory;

import java.util.Properties;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.annotation.PreDestroy;
import javax.annotation.PostConstruct;

import javax.inject.Singleton;

import javax.ejb.Startup;

import javax.management.ObjectName;
import javax.management.MBeanServer;

@Startup
@Singleton
@SuppressWarnings({ "oracle.jdeveloper.cdi.not-proxyable-bean", "oracle.jdeveloper.cdi.uncofig-project" })
public class AssertionConfiguration implements AssertionMXBean {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private MBeanServer       mbeanServer;
  private ObjectName        objectName  = null;

  private final Properties  storage     = new Properties();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AssertionConfiguration</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AssertionConfiguration() {
    // ensure inheritance
    super();

    // initialize instance
    refresh();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setRealm (AssertionMXBean)
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
  @Override
  public void setRealm(final String value) {
    this.storage.put(REALM, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getRealm (AssertionMXBean)
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
  @Override
  public String getRealm() {
    return (String)this.storage.get(REALM);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setIssuer (AssertionMXBean)
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
  @Override
  public void setIssuer(final String value) {
    this.storage.put(ISSUER, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getIssuer (AssertionMXBean)
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
    return (String)this.storage.get(ISSUER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setTenant (AssertionMXBean)
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
  @Override
  public void setTenant(final String value) {
    this.storage.put(TENANT, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getTenant (AssertionMXBean)
  /**
   ** Returns the tenant identifier issued when the application was registered.
   **
   ** @return                    the issuer identifier of the authorization
   **                            server which created the authorization
   **                            response.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String getTenant() {
    return (String)this.storage.get(TENANT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setClient (AssertionMXBean)
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
  @Override
  public void setClient(final String value) {
    this.storage.put(CLIENT, value);
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
    return (String)this.storage.get(CLIENT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSecret (AssertionMXBean)
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
  @Override
  public void setSecret(final String value) {
    this.storage.put(SECRET, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSecret (AssertionMXBean)
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
    return (String)this.storage.get(SECRET);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setMaterial (AssertionMXBean)
  /**
   ** Sets  the PublicKey Material of the signing authority.
   ** </p>
   ** To set this using Microprofile Config use
   ** <code>igs.signing.material</code>.
   ** <p>
   ** Optional property.
   **
   ** @param  value              the PublicKey Material of the signing
   **                            authority.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public void setMaterial(final String value) {
    this.storage.put(SIGNING_MATERIAL, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getMaterial (AssertionMXBean)
  /**
   ** Returns the PublicKey Material of the signing authority.
   **
   ** @return                    the PublicKey Material of the signing
   **                            authority.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String getMaterial() {
    return (String)this.storage.get(SIGNING_MATERIAL);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setLocation (AssertionMXBean)
  /**
   ** Sets the PublicKey local filesystem location of the signing authority.
   ** </p>
   ** To set this using Microprofile Config use
   ** <code>igs.signing.location</code>.
   ** <p>
   ** Optional property.
   **
   ** @param  value              the PublicKey Material of the signing
   **                            authority.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public void setLocation(final String value) {
    this.storage.put(SIGNING_LOCATION, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLocation (AssertionMXBean)
  /**
   ** Returns the PublicKey local filesystem location of the signing authority.
   **
   ** @return                    the PublicKey Material of the signing
   **                            authority.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String getLocation() {
    return (String)this.storage.get(SIGNING_LOCATION);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setInfoEndpoint (AssertionMXBean)
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
  @Override
  public void setInfoEndpoint(final String value) {
    this.storage.put(ENDPOINT_INFO, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getInfoEndpoint (AssertionMXBean)
  /**
   ** Returns the URL for the OAuth2 Authentication Provider to provide
   ** authentication.
   **
   ** @return                    the URL for the OAuth2 Provider to provide
   **                            authentication.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String getInfoEndpoint() {
    return (String)this.storage.get(ENDPOINT_INFO);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDataSource (AssertionMXBean)
  /**
   ** Sets the JNDI name of the JDBC DataSource for the Authentication
   ** Provider.
   ** </p>
   ** To set this using Microprofile Config use
   ** <code>igs.dataSource</code>.
   ** <p>
   ** Required property.
   **
   ** @param  value              the JNDI name of the JDBC DataSource for the
   **                            Authentication Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public void setDataSource(final String value) {
    this.storage.put(JNDI_DATASOURCE, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDataSource (AssertionMXBean)
  /**
   ** Returns the JNDI name of the JDBC DataSource for the Authentication
   ** Provider.
   **
   ** @return                    the JNDI name of the JDBC DataSource for the
   **                            Authentication Provider.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String getDataSource() {
    return (String)this.storage.get(JNDI_DATASOURCE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPrincipalQuery (AssertionMXBean)
  /**
   ** Sets the authentication query used to authenticate users based on
   ** specific key types.
   **
   ** @param  value              the authentication query used to authenticate
   **                            users based on specific key types.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public void setPrincipalQuery(final String value) {
    this.storage.put(QUERY_PRINCIPAL, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPrincipalQuery (AssertionMXBean)
  /**
   ** Returns the authentication query used to authenticate users based on
   ** specific key types.
   **
   ** @return                    the authentication query used to authenticate
   **                            users based on specific key types.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String getPrincipalQuery() {
    return (String)this.storage.get(QUERY_PRINCIPAL);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPermissionQuery (AssertionMXBean)
  /**
   ** Sets the authorization query used to authorize users based on specific key
   ** types.
   **
   ** @param  value              the authorization query used to authorize users
   **                            based on specific key types.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public void setPermissionQuery(final String value) {
    this.storage.put(QUERY_PERMISSION, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPermissionQuery (AssertionMXBean)
  /**
   ** Returns the authorization query used to authorize users based on specific
   ** key types.
   **
   ** @return                    the authorization query used to authorize users
   **                            based on specific key types.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String getPermissionQuery() {
    return (String)this.storage.get(QUERY_PERMISSION);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refresh (AssertionMXBean)
  /**
   ** Refresh the configuration from the fileystem.
   */
  @Override
  public final void refresh() {
    if (PATH.exists()) {
      try {
        // refresh any settings
        this.storage.loadFromXML(new FileInputStream(PATH));
        validate();
      }
      catch (Exception e) {
        e.printStackTrace(System.err);
      }
    }
    else {
      initialize();
      // never trust your self
      save();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   save (AssertionMXBean)
  /**
   ** Save the configuration to the file system.
   */
  @Override
  public final void save() {
    try {
      validate();
      this.storage.storeToXML(new FileOutputStream(PATH), "comment");
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (AssertionMXBean)
  /**
   ** Refresh the configuration from the fileystem.
   */
  @Override
  public final void validate() {
    validateStorage();
    validatePublicKey();
    validateJsonWebKey();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   register
  @PostConstruct
  public void register() {
    try {
      this.objectName  = new ObjectName("igs.service:type=" + this.getClass().getSimpleName());
      this.mbeanServer = ManagementFactory.getPlatformMBeanServer();
      this.mbeanServer.registerMBean(this, this.objectName);
    }
    catch (Exception e) {
      throw new IllegalStateException("Problem during registration of Monitoring into JMX:" + e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unregister
  @PreDestroy
  public void unregister() {
    try {
      this.mbeanServer.unregisterMBean(this.objectName);
    }
    catch (Exception e) {
      throw new IllegalStateException("Problem during unregistration of Monitoring into JMX:" + e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateStorage
  /**
   ** Validates the database configuration for consistency to ensure that a
   ** JDBC DateSource is set and both queries to authenticate/authorize a user
   ** are set.
   **
   ** @throws IllegalStateException if the rule is violated.
   */
  private void validateStorage() {
    if (getRealm() == null || getRealm().isEmpty())
      throw new IllegalStateException("Property igs.verify.realm must be defined");

    if (getDataSource() == null || getDataSource().isEmpty())
      throw new IllegalStateException("Property igs.dataSource must be defined");

    if (getPrincipalQuery() == null || getPrincipalQuery().isEmpty())
      throw new IllegalStateException("Property igs.query.principal must be defined");

    if (getPermissionQuery() == null || getPermissionQuery().isEmpty())
      throw new IllegalStateException("Property igs.query.principal must be defined");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validatePublicKey
  /**
   ** Validates the configuration for consistency to ensure that only a
   ** PublicKey location or the PublicKey material (Base64 encoded) is provided
   ** at the same time.
   **
   ** @throws IllegalStateException if the rule is violated.
   */
  private void validatePublicKey() {
    final String location = getLocation();
    final String material = getMaterial();
    if (location != null && !location.isEmpty() && material != null && !material.isEmpty())
      throw new IllegalStateException("Both properties igs.signing.material and igs.signing.location must not be defined");
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
    final String tenant   = getTenant();
    final String location = getLocation();
    if (location.startsWith("http") && (tenant == null || tenant.isEmpty()))
      throw new IllegalStateException("The tenant cannot be empty if igs.signing.location belongs to an URL");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize
  private void initialize() {
    this.storage.put(REALM,            "igs");
    this.storage.put(ISSUER,           "https://sso.cinnamonstar.oam:443/oauth2");
    this.storage.put(TENANT,           "SecureDomain2");
    this.storage.put(CLIENT,           "uid-service");
    this.storage.put(SECRET,           "Welcome1");
    this.storage.put(SIGNING_LOCATION, "https://sso.cinnamonstar.oam/oauth2/rest/security");
    this.storage.put(ENDPOINT_INFO,    "Welcome1");
    this.storage.put(JNDI_DATASOURCE,  "jdbc/igsDS");
    this.storage.put(QUERY_PRINCIPAL,  "SELECT usr.usr_id FROM igt_user usr WHERE usr.username = ?");
    this.storage.put(QUERY_PERMISSION, "SELECT url.rol_id FROM igt_user usr,igt_userroles url WHERE url.usr_id = usr.usr_id AND usr.username = ?");
  }
}