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

    System      :   Foundation Service Extension
    Subsystem   :   Common Shared Utility

    File        :   SecurityContext.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SecurityContext.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.core.network;

import java.util.Properties;

import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ByteArrayInputStream;

import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.AccessController;
import java.security.KeyStoreException;
import java.security.KeyManagementException;
import java.security.NoSuchProviderException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import java.security.cert.X509Certificate;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.KeyManager;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;

import oracle.hst.platform.core.SystemError;
import oracle.hst.platform.core.SystemException;

import oracle.hst.platform.core.utility.PropertyUtility;

////////////////////////////////////////////////////////////////////////////////
// final class SecurityContext
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Utility class, which helps to configure {@link SSLContext} instances.
 ** <p>
 ** For example:
 ** <pre>
 **   SecurityContext context = SecurityContext.build()
 **     .trustedStoreFile("trusted.jks")
 **     .trustedStorePassword("asdfgh")
 **     .trustedStoreType("JKS")
 **     .trustedManagerFactoryAlgorithm("PKIX")
 **
 **     .identityStoreFile("identity.jks")
 **     .identityPassword("asdfgh")
 **     .identityStoreType("JKS")
 **     .identityManagerFactoryAlgorithm("SunX509")
 **     .identityStoreProvider("SunJSSE")
 **
 **     .securityProtocol("SSL");
 **
 **   SSLContext sslContext = context.create();
 ** </pre>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class SecurityContext {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The <em>default</em> protocol to secure connections.
   ** <p>
   ** No default value is set.
   ** <p>
   ** The name of the configuration property is
   ** <code>jdk.tls.client.protocols</code>.
   */
  public static final String            PROTOCOL_DEFAULT                   = "TLSv1.2";
  /**
   ** <em>Trusted</em> store provider name.
   ** <p>
   ** The value <b>must</b> be a <code>String</code> representing the name of a
   ** <em>trusted</em> store provider.
   ** <p>
   ** No default value is set.
   ** <p>
   ** The name of the configuration property is
   ** <code>javax.net.ssl.trustStoreProvider</code>.
   */
  public static final String            TRUSTED_STORE_PROVIDER             = "javax.net.ssl.trustStoreProvider";
  /**
   ** <em>Identity</em> store provider name.
   ** <p>
   ** The value <b>must</b> be a <code>String</code> representing the name of a
   ** <em>identity</em> store provider.
   ** <p>
   ** No default value is set.
   ** <p>
   ** The name of the configuration property is
   ** <code>javax.net.ssl.keyStoreProvider</code>.
   */
  public static final String            IDENTITY_STORE_PROVIDER            = "javax.net.ssl.keyStoreProvider";
  /**
   ** <em>Trusted</em> store file name.
   ** <p>
   ** The value <b>must</b> be a <code>String</code> representing the name of a
   ** <em>trusted</em> store file.
   ** <p>
   ** No default value is set.
   ** <p>
   ** The name of the configuration property is
   ** <code>javax.net.ssl.trustStore</code>.
   */
  public static final String            TRUSTED_STORE_FILE                 = "javax.net.ssl.trustStore";
  /**
   ** <em>Identity</em> store file name.
   ** <p>
   ** The value <b>must</b> be a <code>String</code> representing the name of a
   ** <em>identity</em> store file.
   ** <p>
   ** No default value is set.
   ** <p>
   ** The name of the configuration property is
   ** <code>javax.net.ssl.keyStore</code>.
   */
  public static final String            IDENTITY_STORE_FILE                = "javax.net.ssl.keyStore";
  /**
   ** <em>Trust</em> store file password - the password used to unlock the
   ** <em>trusted</em> store file.
   ** <p>
   ** The value <b>must</b> be a <code>String</code> representing the name of a
   ** <em>trusted</em> store file password.
   ** <p>
   ** No default value is set.
   ** <p>
   ** The name of the configuration property is
   ** <code>javax.net.ssl.trustStorePassword</code>.
   */
  public static final String            TRUSTED_STORE_PASSWORD             = "javax.net.ssl.trustStorePassword";
  /**
   ** <em>Identity</em> store file password - the password used to unlock the
   ** <em>identity</em> store file.
   ** <p>
   ** The value <b>must</b> be a <code>String</code> representing the name of a
   ** <em>identity</em> store file password.
   ** <p>
   ** No default value is set.
   ** <p>
   ** The name of the configuration property is
   ** <code>javax.net.ssl.keyStorePassword</code>.
   */
  public static final String            IDENTITY_STORE_PASSWORD            = "javax.net.ssl.keyStorePassword";
  /**
   ** <em>Trusted</em> store type (see {@link java.security.KeyStore#getType()}
   ** for more info).
   ** <p>
   ** The value <b>must</b> be a <code>String</code> representing the name of a
   ** <em>trusted</em> store type name.
   ** <p>
   ** No default value is set.
   ** <p>
   ** The name of the configuration property is
   ** <code>javax.net.ssl.trustStoreType</code>.
   */
  public static final String            TRUSTED_STORE_TYPE                 = "javax.net.ssl.trustStoreType";
  /**
   ** <em>Identity</em> store type (see {@link java.security.KeyStore#getType()}
   ** for more info).
   ** <p>
   ** The value <b>must</b> be a <code>String</code> representing the name of a
   ** <em>identity</em> store type name.
   ** <p>
   ** No default value is set.
   ** <p>
   ** The name of the configuration property is
   ** <code>javax.net.ssl.keyStoreType</code>.
   */
  public static final String            IDENTITY_STORE_TYPE                = "javax.net.ssl.keyStoreType";
  /**
   ** <em>Trusted</em> manager factory algorithm name.
   ** <p>
   ** The value <b>must</b> be a <code>String</code> representing the name of a
   ** <em>trusted</em> manager factory algorithm name.
   ** <p>
   ** No default value is set.
   ** <p>
   ** The name of the configuration property is
   ** <code>ssl.trustManagerFactory.algorithm</code>.
   */
  public static final String            TRUSTED_MANAGER_FACTORY_ALGORITHM  = "ssl.trustManagerFactory.algorithm";
  /**
   ** <em>Identity</em> manager factory algorithm name.
   ** <p>
   ** The value <b>must</b> be a <code>String</code> representing the name of a
   ** <em>identity</em> manager factory algorithm name.
   ** <p>
   ** No default value is set.
   ** <p>
   ** The name of the configuration property is
   ** <code>ssl.keyManagerFactory.algorithm</code>.
   */
  public static final String            IDENTITY_MANAGER_FACTORY_ALGORITHM = "ssl.keyManagerFactory.algorithm";
  /**
   ** <em>Trusted</em> manager factory provider name.
   ** <p>
   ** The value <b>must</b> be a <code>String</code> representing the name of a
   ** <em>trusted</em> manager factory provider name.
   ** <p>
   ** No default value is set.
   ** <p>
   ** The name of the configuration property is
   ** <code>ssl.trustManagerFactory.provider</code>.
   */
  public static final String           TRUSTED_MANAGER_FACTORY_PROVIDER    = "ssl.trustManagerFactory.provider";
  /**
   ** <em>Identity</em> manager factory provider name.
   ** <p>
   ** The value <b>must</b> be a <code>String</code> representing the name of a
   ** <em>identity</em> manager factory provider name.
   ** <p>
   ** No default value is set.
   ** <p>
   ** The name of the configuration property is
   ** <code>ssl.keyManagerFactory.provider</code>.
   */
  public static final String           IDENTITY_MANAGER_FACTORY_PROVIDER   = "ssl.keyManagerFactory.provider";
  /**
   ** A {@link X509TrustManager} that thinks that the world is trustworthy.
   */
  public static final TrustManager     INSECURE_TRUSTMANAGER               = new InsecureTrustManager();
  /**
   ** A {@link HostnameVerifier} that thinks the best of all hosts.
   */
  public static final HostnameVerifier INSECURE_HOSTNAME_VERIFIER          = new InsecureHostnameVerifier();
  /**
   ** Default SSL configuration that is used to create default SSL context
   ** instances that do not take into account system properties.
   */
  private static final SecurityContext DEFAULT                             = new SecurityContext(false);

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private Store  identity;
  private Store  trusted;
  private char[] passphrase;
  private String protocol    = PROTOCOL_DEFAULT;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class InsecureHostnameVerifier
  // ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~
  /**
   ** A {@link HostnameVerifier} that thinks the best of all hosts.
   */
  private static class InsecureHostnameVerifier implements HostnameVerifier {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>InsecureHostnameVerifier</code> that allows
     ** use as
     ** a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public InsecureHostnameVerifier() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////
    
    ////////////////////////////////////////////////////////////////////////////
    // Method: verify (HostnameVerifier)
    /**
     ** Verify that the host name is an acceptable match with the server's
     ** authentication scheme.
     **
     ** @param  hostname         the host name.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  session          the {@link SSLSession} used on the connection
     **                          to host.
     **                          <br>
     **                          Allowed object is {@link SSLSession}.
     **
     ** @return                  <code>true</code> if the host name is
     **                          acceptable; means always in this case.
     */
    @Override
    public boolean verify(final String hostname, final SSLSession session) {
      return true;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class InsecureTrustManager
  // ~~~~~ ~~~~~~~~~~~~~~~~~~~~
  /**
   ** A {@link X509TrustManager} that thinks that the world is trustworthy.
   */
  private static class InsecureTrustManager implements X509TrustManager {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>InsecureTrustManager</code> that allows use as
     ** a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public InsecureTrustManager() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////
    
    ////////////////////////////////////////////////////////////////////////////
    // Method: checkServerTrusted (X509TrustManager)
    /**
     ** Given the partial or complete certificate chain provided by the peer,
     ** build a certificate path to a trusted root and return if it can be
     ** validated and is trusted for server SSL authentication based on the
     ** authentication type.
     ** <p>
     ** The authentication type is the key exchange algorithm portion of the
     ** cipher suites represented as a String, such as "RSA", "DHE_DSS".
     ** <br>
     ** <b>Note</b>:
     ** <br>
     ** For some exportable cipher suites, the key exchange algorithm is
     ** determined at run time during the handshake. For instance, for
     ** <code>TLS_RSA_EXPORT_WITH_RC4_40_MD5</code>, the type should be
     ** <code>RSA_EXPORT</code> when an ephemeral RSA key is used for the key
     ** exchange, and RSA when the key from the server certificate is used.
     ** Checking is case-sensitive.
     **
     **
     ** @param  chain            the peer certificate chain.
     **                          <br>
     **                          Allowed object is array of
     **                          {@link X509Certificate}.
     ** @param  type             the key exchange algorithm used.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    @Override
    public void checkServerTrusted(final X509Certificate[] chain, final String type) {
      // intentionally left blank
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: checkClientTrusted (X509TrustManager)
    /**
     ** Given the partial or complete certificate chain provided by the peer,
     ** build a certificate path to a trusted root and return if it can be
     ** validated and is trusted for client SSL authentication based on the
     ** authentication type.
     ** <p>
     ** The authentication type is determined by the actual certificate used.
     ** For instance, if <code>RSAPublicKey</code> is used, the authType should
     ** be "RSA". Checking is case-sensitive.
     **
     ** @param  chain            the peer certificate chain.
     **                          <br>
     **                          Allowed object is array of
     **                          {@link X509Certificate}.
     ** @param  type             the authentication type based on the client
     **                          certificate.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    @Override
    public void checkClientTrusted(final X509Certificate[] chain, final String type) {
      // intentionally left blank
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getAcceptedIssuers (X509TrustManager)
    @Override
    public X509Certificate[] getAcceptedIssuers() {
      return new X509Certificate[0];
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Store
  // ~~~~~ ~~~~~
  public class Store {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private String   type;
    private String   provider;
    private char[]   password;
    private byte[]   payload;
    private String   file;
    private String   factoryAlgorithm;
    private String   factoryProvider;
    private KeyStore delegate;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>Store</code> that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    Store() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Store</code> which populates its properties from the
     ** specified <code>other</code> <code>Store</code>.
     ** <br>
     ** Copy Constructor
     **
     ** @param  other            the template <code>Store</code> to populate
     **                          the instance attributes from.
     **                          <br>
     **                          Allowed object is {@link Store}.
     */
    Store(final Store other) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.type             = other.type;
      this.provider         = other.provider;
      this.password         = other.password;
      this.delegate         = other.delegate;
      this.file             = other.file;
      this.payload          = other.payload;
      this.factoryProvider  = other.factoryProvider;
      this.factoryAlgorithm = other.factoryAlgorithm;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Store</code> which populates its properties from the
     ** specified <code>other</code> <code>Store</code>.
     ** <br>
     ** Copy Constructor
     **
     ** @param  type             the type of <em>KeyStore</em> store to set.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  provider         <em>KeyStore</em> store provider to set.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  file             the {@link java.io.File file} name of the
     **                          <em>trusted</em> store.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  password         the password of <em>key</em> store to set.
     **                          <br>
     **                          Allowed object is array of <code>char</code>.
     ** @param  factoryProvider  the <em>trust</em> manager factory provider.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  factoryAlgorithm the <em>trust</em> manager factory algorithm.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    Store(final String type, final String provider, final String file, final char[] password, final String factoryProvider, final String factoryAlgorithm) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.type             = type;
      this.provider         = provider;
      this.password         = password;
      this.file             = file;
      this.factoryProvider  = factoryProvider;
      this.factoryAlgorithm = factoryAlgorithm;

      this.delegate         = null;
      this.payload          = null;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new SSL configurator instance.
   **
   ** @param  system             if <code>true</code>,
   **                            {@link #retrieve(java.util.Properties) retrieves}
   **                            the initial configuration from
   **                            {@link System#getProperties()}, otherwise the
   **                            instantiated configurator will be empty.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   */
  private SecurityContext(final boolean system) {
    // ensure inheritance
    super();

    // initialize instance
    if (system)
      retrieve(AccessController.doPrivileged(PropertyUtility.systemProperties()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new SSL configurator instance which populates its properties
   ** from the specified <code>other</code> <code>SecurityContext</code>.
   ** <br>
   ** Copy Constructor
   **
   ** @param  other              the template <code>SecurityContext</code> to
   **                            populate the instance attributes from.
   **                            <br>
   **                            Allowed object is {@link SecurityContext}.
   */
  private SecurityContext(final SecurityContext that) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.identity   = new Store(that.identity);
    this.trusted    = new Store(that.trusted);
    this.protocol   = that.protocol;
    this.passphrase = that.passphrase;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   trustedStoreProvider
  /**
   ** Set the <em>trusted</em> store provider name.
   **
   ** @param  value              <em>trusted</em> store provider to set.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>SecurityContext</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>SecurityContext</code>.
   */
  public final SecurityContext trustedStoreProvider(final String value) {
    // lazy initialize the trusted store
    if (this.trusted == null)
      this.trusted = new Store();

    this.trusted.provider = value;
    return this;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   identityStoreProvider
  /**
   ** Set the <em>identity</em> store provider name.
   **
   ** @param  value              <em>identity</em> store provider to set.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>SecurityContext</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>SecurityContext</code>.
   */
  public final SecurityContext identityStoreProvider(final String value) {
    // lazy initialize the identity store
    if (this.identity == null)
      this.identity = new Store();

    this.identity.provider = value;
    return this;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   trustedStoreType
  /**
   ** Set the type of <em>trusted</em> store.
   **
   ** @param  value              the type of <em>trusted</em> store to set.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>SecurityContext</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>SecurityContext</code>.
   */
  public final SecurityContext trustedStoreType(final String value) {
    // lazy initialize the trusted store
    if (this.trusted == null)
      this.trusted = new Store();

    this.trusted.type = value;
    return this;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   identityStoreType
  /**
   ** Set the type of <em>identity</em> store.
   **
   ** @param  value              the type of <em>identity</em> store to set.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>SecurityContext</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>SecurityContext</code>.
   */
  public final SecurityContext identityStoreType(final String value) {
    // lazy initialize the identity store
    if (this.identity == null)
      this.identity = new Store();

    this.identity.type = value;
    return this;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   trustedStorePassword
  /**
   ** Set the password of <em>trusted</em> store.
   **
   ** @param  value              the password of <em>trusted</em> store to set.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>SecurityContext</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>SecurityContext</code>.
   */
  public final SecurityContext trustedStorePassword(final String value) {
    // lazy initialize the trusted store
    if (this.trusted == null)
      this.trusted = new Store();

    this.trusted.password = value.toCharArray();
    return this;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   trustedStorePassword
  /**
   ** Set the password of <em>trusted</em> store.
   **
   ** @param  value              the password of <em>trusted</em> store to set.
   **                            <br>
   **                            Allowed object is array of <code>char</code>.
   **
   ** @return                    the <code>SecurityContext</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>SecurityContext</code>.
   */
  public final SecurityContext trustedStorePassword(final char[] value) {
    // lazy initialize the trusted store
    if (this.trusted == null)
      this.trusted = new Store();

    this.trusted.password = value.clone();
    return this;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   identityStorePassword
  /**
   ** Set the password of <em>identity</em> store.
   **
   ** @param  value              the password of <em>identity</em> store to set.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>SecurityContext</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>SecurityContext</code>.
   */
  public final SecurityContext identityStorePassword(final String value) {
    // lazy initialize the identity store
    if (this.identity == null)
      this.identity = new Store();

    this.identity.password = value.toCharArray();
    return this;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   identityStorePassword
  /**
   ** Set the password of <em>identity</em> store.
   **
   ** @param  value              the password of <em>identity</em> store to set.
   **                            <br>
   **                            Allowed object is array of <code>char</code>.
   **
   ** @return                    the <code>SecurityContext</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>SecurityContext</code>.
   */
  public final SecurityContext identityStorePassword(final char[] value) {
    // lazy initialize the identity store
    if (this.identity == null)
      this.identity = new Store();

    this.identity.password = value.clone();
    return this;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   identityPassword
  /**
   ** Set the password of the key in the <em>identity</em> store.
   **
   ** @param  value              the password of <em>identity</em> to set.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>SecurityContext</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>SecurityContext</code>.
   */
  public final SecurityContext identityPassword(final String value) {
    this.passphrase = value.toCharArray();
    return this;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   identityPassword
  /**
   ** Set the password of the key in the <em>identity</em> store.
   **
   ** @param  value              the password of <em>identity</em> to set.
   **                            <br>
   **                            Allowed object is array of <code>char</code>.
   **
   ** @return                    the <code>SecurityContext</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>SecurityContext</code>.
   */
  public final SecurityContext identityPassword(final char[] value) {
    this.passphrase = value.clone();
    return this;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   trustedStoreFile
  /**
   ** Set the <em>trusted</em> store file name.
   ** <p>
   ** Setting a trusted store instance resets any
   ** {@link #trustedStore(java.security.KeyStore) trusted store instance} or
   ** {@link #trustedStoreBytes(byte[]) trusted store payload} value previously
   ** set.
   **
   ** @param  value              {@link java.io.File file} name of the
   **                            <em>trusted</em> store.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>SecurityContext</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>SecurityContext</code>.
   */
  public final SecurityContext trustedStoreFile(final String value) {
    // lazy initialize the trusted store
    if (this.trusted == null)
      this.trusted = new Store();

    this.trusted.file     = value;
    this.trusted.payload  = null;
    this.trusted.delegate = null;
    return this;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   trustedStoreBytes
  /**
   ** Set the <em>trusted</em> store payload as byte array.
   ** <p>
   ** Setting a trusted store instance resets any
   ** {@link #trustedStoreFile(String) trusted store file} or
   ** {@link #trustedStore(java.security.KeyStore) trusted store instance} value
   ** previously set.
   **
   ** @param  value              the <em>trusted</em> store payload.
   **                            <br>
   **                            Allowed object is array of <code>byte</code>.
   **
   ** @return                    the <code>SecurityContext</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>SecurityContext</code>.
   */
  public final SecurityContext trustedStoreBytes(final byte[] value) {
    // lazy initialize the trusted store
    if (this.trusted == null)
      this.trusted = new Store();

    this.trusted.file     = null;
    this.trusted.payload  = value.clone();
    this.trusted.delegate = null;
    return this;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   identityStoreFile
  /**
   ** Set the <em>identity</em> store file name.
   ** <p>
   ** Setting a key store instance resets any
   ** {@link #identityStore(java.security.KeyStore) key store instance} or
   **
   ** @param  value              the {@link java.io.File file} name of the
   **                            <em>identity</em> store.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>SecurityContext</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>SecurityContext</code>.
   */
  public final SecurityContext identityStoreFile(String value) {
    // lazy initialize the identity store
    if (this.identity == null)
      this.identity = new Store();

    this.identity.file     = value;
    this.identity.payload  = null;
    this.identity.delegate = null;
    return this;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   identityStoreBytes
  /**
   ** Set the <em>identity</em> store payload as byte array.
   ** <p>
   ** Setting a key store instance resets any
   ** {@link #identityStoreFile(String) key store file} or
   ** {@link #identityStore(java.security.KeyStore) key store instance} value
   ** previously set.
   **
   ** @param  value              the <em>identity</em> store payload.
   **                            <br>
   **                            Allowed object is array of <code>byte</code>.
   **
   ** @return                    the <code>SecurityContext</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>SecurityContext</code>.
   */
  public final SecurityContext identityStoreBytes(final byte[] value) {
    // lazy initialize the identity store
    if (this.identity == null)
      this.identity = new Store();

    this.identity.file     = null;
    this.identity.payload  = value.clone();
    this.identity.delegate = null;
    return this;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   trustedStore
  /**
   ** Set the <em>trusted</em> store instance.
   ** <p>
   ** Setting a trusted store instance resets any
   ** {@link #trustedStoreFile(String) trusted store file} or
   ** {@link #trustedStoreBytes(byte[]) trusted store payload} value previously
   ** set.
   **
   ** @param  value              the <em>trusted</em> store instance.
   **                            <br>
   **                            Allowed object is {@link KeyStore}.
   **
   ** @return                    the <code>SecurityContext</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>SecurityContext</code>.
   */
  public final SecurityContext trustedStore(final KeyStore value) {
    // lazy initialize the identity store
    if (this.trusted == null)
      this.trusted = new Store();

    this.trusted.file     = null;
    this.trusted.payload  = null;
    this.trusted.delegate = value;
    return this;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   trustedStore
  /**
   ** Returns the <em>trusted</em> store instance.
   **
   ** @return                    the <em>trusted</em> store instance or
   **                            <code>null</code> if not explicitly set.
   **                            <br>
   **                            Possible object is {@link KeyStore}.
   */
  public final KeyStore trustedStore() {
    return this.trusted == null ? null : this.trusted.delegate;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   identityStore
  /**
   ** Set the <em>identity</em> store instance.
   ** <p>
   ** Setting a key store instance resets any
   ** {@link #identityStoreFile(String) key store file} or
   ** {@link #identityStoreBytes(byte[]) key store payload} value previously
   ** set.
   **
   ** @param  value              the <em>identity</em> store instance.
   **                            <br>
   **                            Allowed object is {@link KeyStore}.
   **
   ** @return                    the <code>SecurityContext</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>SecurityContext</code>.
   */
  public final SecurityContext identityStore(final KeyStore value) {
    // lazy initialize the identity store
    if (this.identity == null)
      this.identity = new Store();

    this.identity.file     = null;
    this.identity.payload  = null;
    this.identity.delegate = value;
    return this;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   identityStore
  /**
   ** Returns the <em>identity</em> store instance.
   **
   ** @return                    the <em>identity</em> store instance or
   **                            <code>null</code> if not explicitly set.
   **                            <br>
   **                            Possible object is {@link KeyStore}.
   */
  public final KeyStore identityStore() {
    return this.identity == null ? null : this.identity.delegate;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   trustedFactoryAlgorithm
  /**
   ** Set the <em>trusted</em> manager factory algorithm.
   **
   ** @param  value              the <em>trusted</em> manager factory algorithm.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>SecurityContext</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>SecurityContext</code>.
   */
  public final SecurityContext trustedFactoryAlgorithm(final String value) {
    // lazy initialize the trusted store
    if (this.trusted == null)
      this.trusted = new Store();

    this.trusted.factoryAlgorithm = value;
    return this;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   identityFactoryAlgorithm
  /**
   ** Set the <em>identity</em> manager factory algorithm.
   **
   ** @param  value              the <em>identity</em> manager factory
   **                            algorithm.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>SecurityContext</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>SecurityContext</code>.
   */
  public final SecurityContext identityFactoryAlgorithm(final String value) {
    // lazy initialize the identity store
    if (this.identity == null)
      this.identity = new Store();

    this.identity.factoryAlgorithm = value;
    return this;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   trustedFactoryProvider
  /**
   ** Set the <em>trusted</em> manager factory provider.
   **
   ** @param  value              the <em>trusted</em> manager factory provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>SecurityContext</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>SecurityContext</code>.
   */
  public final SecurityContext trustedFactoryProvider(final String value) {
    // lazy initialize the trusted store
    if (this.trusted == null)
      this.trusted = new Store();

    this.trusted.factoryProvider = value;
    return this;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   identityFactoryProvider
  /**
   ** Set the <em>identity</em> manager factory provider.
   **
   ** @param  value              the <em>identity</em> manager factory provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>SecurityContext</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>SecurityContext</code>.
   */
  public final SecurityContext identityFactoryProvider(final String value) {
    // lazy initialize the identity store
    if (this.identity == null)
      this.identity = new Store();

    this.identity.factoryProvider = value;
    return this;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   securityProtocol
  /**
   ** Set the SSLContext protocol.
   ** <br>
   ** The default value is {@link #PROTOCOL_DEFAULT} if this is
   ** <code>null</code>.
   **
   ** @param  value              the protocol for
   **                            {@link javax.net.ssl.SSLContext#getProtocol()}.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>SecurityContext</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>SecurityContext</code>.
   */
  public final SecurityContext securityProtocol(final String value) {
    this.protocol = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   defaultContext
  /**
   ** Factory method to create  new instance of a {@link SSLContext} configured
   ** using default configuration settings.
   ** <p>
   ** The default SSL configuration is initialized from system properties. This
   ** method is a shortcut for
   ** {@link #defaultContext(boolean) defaultContext(true)}.
   **
   ** @return                    new instance of a default SSL context
   **                            initialized from system properties.
   **                            <br>
   **                            Possible object is {@link SSLContext}.
   **
   ** @throws SystemException    if the either the <em>trusted</em> or
   **                            <em>identity</em> store could not be
   **                            initialized.
   */
  public static SSLContext defaultContext()
    throws SystemException {

    return defaultContext(true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   defaultContext
  /**
   ** Factory method to create new instance of a {@link SSLContext} configured
   ** using default configuration settings.
   ** <p>
   ** If <code>system</code> parameter is set to <code>true</code>, the default
   ** SSL configuration is initialized from system properties.
   **
   ** @param  system             if <code>true</code>, the default SSL context
   **                            will be initialized using system properties.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    new instance of a default SSL context
   **                            initialized from system properties.
   **                            <br>
   **                            Possible object is {@link SSLContext}.
   **
   ** @throws SystemException    if the either the <em>trusted</em> or
   **                            <em>identity</em> store could not be
   **                            initialized.
   */
  public static SSLContext defaultContext(final boolean system)
    throws SystemException {

    return system ? new SecurityContext(true).create() : DEFAULT.create();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a new &amp; initialized SSL configurator
   ** instance.
   ** <p>
   ** The instance {@link #retrieve(java.util.Properties) retrieves} the initial
   ** configuration from {@link System#getProperties() system properties}.
   **
   ** @return                    a new &amp; initialized SSL configurator
   **                            instance.
   **                            <br>
   **                            Possible object is
   **                            <code>SecurityContext</code>.
   */
  public static SecurityContext build() {
    return new SecurityContext(false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a new SSL configurator instance.
   **
   ** @param  system             if <code>true</code>,
   **                            {@link #retrieve(java.util.Properties) retrieves}
   **                            the initial configuration from
   **                            {@link System#getProperties()}, otherwise the
   **                            instantiated configurator will be empty.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    a new &amp; initialized SSL configurator
   **                            instance.
   **                            <br>
   **                            Possible object is <code>SecurityContext</code>.
   */
  public static SecurityContext build(final boolean system) {
    return new SecurityContext(system);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   copy
  /**
   ** Create a copy of the current SSL configurator instance.
   **
   ** @return                    a copy of the current SSL configurator
   **                            instance.
   **                            <br>
   **                            Possible object is <code>SecurityContext</code>.
   */
  public SecurityContext copy() {
    return new SecurityContext(this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   retrieve
  /**
   ** Retrieve the SSL context configuration from the supplied properties.
   **
   ** @param  properties         the properties containing the SSL context
   **                            configuration.
   **                            <br>
   **                            Allowed object is {@link Properties}.
   **
   ** @return                    the <code>SecurityContext</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>SecurityContext</code>.
   */
  public SecurityContext retrieve(final Properties properties) {
    this.protocol                  = PROTOCOL_DEFAULT;

    this.trusted                   = new Store();
    this.trusted.type              = properties.getProperty(TRUSTED_STORE_TYPE);
    this.trusted.file              = properties.getProperty(TRUSTED_STORE_FILE);
    this.trusted.provider          = properties.getProperty(TRUSTED_STORE_PROVIDER);
    this.trusted.factoryProvider   = properties.getProperty(TRUSTED_MANAGER_FACTORY_PROVIDER);
    this.trusted.factoryAlgorithm  = properties.getProperty(TRUSTED_MANAGER_FACTORY_ALGORITHM);
    this.trusted.payload           = null;
    this.trusted.delegate          = null;

    this.identity                  = new Store();
    this.identity.type             = properties.getProperty(IDENTITY_STORE_TYPE);
    this.identity.file             = properties.getProperty(IDENTITY_STORE_FILE);
    this.identity.provider         = properties.getProperty(IDENTITY_STORE_PROVIDER);
    this.identity.factoryProvider  = properties.getProperty(IDENTITY_MANAGER_FACTORY_PROVIDER);
    this.identity.factoryAlgorithm = properties.getProperty(IDENTITY_MANAGER_FACTORY_ALGORITHM);
    this.identity.payload          = null;
    this.identity.delegate         = null;

    if (properties.getProperty(TRUSTED_STORE_PASSWORD) != null) {
      this.trusted.password = properties.getProperty(TRUSTED_STORE_PASSWORD).toCharArray();
    }
    else {
      this.trusted.password = null;
    }

    if (properties.getProperty(IDENTITY_STORE_PASSWORD) != null) {
      this.identity.password = properties.getProperty(IDENTITY_STORE_PASSWORD).toCharArray();
    }
    else {
      this.identity.password = null;
    }
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Create new SSL context instance using the current SSL context
   ** configuration.
   **
   ** @return                    newly configured SSL context instance.
   **                            <br>
   **                            Possible object is {@link SSLContext}.
   **
   ** @throws SystemException    if the either the <em>trusted</em> or
   **                            <em>identity</em> store could not be
   **                            initialized.
   */
  public SSLContext create()
    throws SystemException {

    final KeyManagerFactory   identity = identity();
    final TrustManagerFactory trusted  = trusted();
    return create(identity != null ? identity.getKeyManagers() : null, trusted != null ? trusted.getTrustManagers() : null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Create new SSL context instance using the current SSL context
   ** configuration.
   ** <br>
   ** Either of the first two parameters may be <code>null</code> in which case
   ** the installed security providers will be searched for the highest priority
   ** implementation of the appropriate factory. Likewise, the secure random
   ** parameter may be <code>null</code> in which case the default
   ** implementation will be used. 
   **
   ** @param  identity           the sources of authentication identities or
   **                            <code>null</code>.
   **                            <br>
   **                            Allowed object is array of {@link KeyManager}.
   ** @param  trusted            the source of peer authentication trust
   **                            decisions or <code>null</code>.
   **                            <br>
   **                            Allowed object is array of
   **                            {@link TrustManager}.
   **
   ** @return                    newly configured SSL context instance.
   **                            <br>
   **                            Possible object is {@link SSLContext}.
   **
   ** @throws SystemException    if the either the <em>trusted</em> or
   **                            <em>identity</em> store could not be
   **                            initialized.
   */
  public SSLContext create(final KeyManager[] identity, final TrustManager[] trusted)
    throws SystemException {

    return create(identity, trusted, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Create new SSL context instance using the current SSL context
   ** configuration.
   ** <br>
   ** Either of the first two parameters may be <code>null</code> in which case
   ** the installed security providers will be searched for the highest priority
   ** implementation of the appropriate factory. Likewise, the secure random
   ** parameter may be <code>null</code> in which case the default
   ** implementation will be used. 
   **
   ** @param  identity           the sources of authentication identities or
   **                            <code>null</code>.
   **                            <br>
   **                            Allowed object is array of {@link KeyManager}.
   ** @param  trusted            the source of peer authentication trust
   **                            decisions or <code>null</code>.
   **                            <br>
   **                            Allowed object is array of
   **                            {@link TrustManager}.
   ** @param  random             the source of randomness for this generator or
   **                            <code>null</code>.
   **                            <br>
   **                            Allowed object is array of
   **                            {@link SecureRandom}.
   **
   ** @return                    newly configured SSL context instance.
   **                            <br>
   **                            Possible object is {@link SSLContext}.
   **
   ** @throws SystemException    if the either the <em>trusted</em> or
   **                            <em>identity</em> store could not be
   **                            initialized.
   */
  public SSLContext create(final KeyManager[] identity, final TrustManager[] trusted, final SecureRandom random)
    throws SystemException {

    try {
      String protocol = "TLSv1";
      if (this.protocol != null) {
        protocol = this.protocol;
      }
      final SSLContext context = SSLContext.getInstance(protocol);
      context.init(identity, trusted, random);
      return context;
    }
    catch (KeyManagementException e) {
      throw SystemException.generic(SystemError.SECURITY_INITIALIZE, e);
    }
    catch (NoSuchAlgorithmException e) {
      throw SystemException.generic(SystemError.SECURITY_ALGORITHM, e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   identity
  /**
   ** Initialize the identity key store.
   **
   ** @return                    the {@link KeyManagerFactory} created.
   **                            <br>
   **                            Possible object is {@link KeyManagerFactory}.
   **
   ** @throws SystemException    if the <em>identity</em> key store could not be
   **                            initialized.
   */
  protected KeyManagerFactory identity()
    throws SystemException {

    KeyManagerFactory factory  = null;
    if (this.identity == null)
      return factory;

    KeyStore identity = this.identity.delegate;
    if (identity == null && (this.identity.payload != null || this.identity.file != null)) {
      try {
        if (this.identity.provider != null) {
          identity = KeyStore.getInstance(this.identity.type != null ? this.identity.type : KeyStore.getDefaultType(), this.identity.provider);
        }
        else {
          identity = KeyStore.getInstance(this.identity.type != null ? this.identity.type : KeyStore.getDefaultType());
        }
        InputStream stream = null;
        try {
          if (this.identity.payload != null) {
            stream = new ByteArrayInputStream(this.identity.payload);
          }
          else if (!this.identity.file.equals("NONE")) {
            stream = new FileInputStream(this.identity.file);
          }
          identity.load(stream, this.identity.password);
        }
        finally {
          try {
            if (stream != null) {
              stream.close();
            }
          }
          catch (IOException ignored) {
            // intentionally left blank
            ;
          }
        }
      }
      catch (KeyStoreException e) {
        throw SystemException.generic(SystemError.IDENTITY_IMPLEMENTATION, this.identity.type);
      }
      catch (CertificateException e) {
        throw SystemException.generic(SystemError.IDENTITY_CERT_NOTLOADED);
      }
      catch (FileNotFoundException e) {
        throw SystemException.generic(SystemError.IDENTITY_FILE_NOTFOUND, this.identity.file);
      }
      catch (IOException e) {
        throw SystemException.generic(SystemError.IDENTITY_FILE_NOTLOADED, this.identity.file);
      }
      catch (NoSuchProviderException e) {
        throw SystemException.generic(SystemError.IDENTITY_PROVIDER, this.identity.provider);
      }
      catch (NoSuchAlgorithmException e) {
        throw new SystemException(SystemError.IDENTITY_ALGORITHM);
      }
    }
    if (identity != null) {
      if (this.identity.factoryAlgorithm == null) {
        this.identity.factoryAlgorithm = AccessController.doPrivileged(PropertyUtility.systemProperty(IDENTITY_MANAGER_FACTORY_ALGORITHM, KeyManagerFactory.getDefaultAlgorithm()));
      }
      try {
        if (this.identity.factoryProvider != null) {
          factory = KeyManagerFactory.getInstance(this.identity.factoryAlgorithm, this.identity.factoryProvider);
        }
        else {
          factory = KeyManagerFactory.getInstance(this.identity.factoryAlgorithm);
        }
        final char[] password = this.passphrase != null ? this.passphrase : this.identity.password;
        if (password != null) {
          factory.init(identity, password);
        }
        else {
//          String ksName = this.identity.provider != null ? LocalizationMessages.SSL_KMF_NO_PASSWORD_FOR_PROVIDER_BASED_KS() : this.identity.bytes != null ? LocalizationMessages.SSL_KMF_NO_PASSWORD_FOR_BYTE_BASED_KS() : this.identity.file;
//          LOGGER.config(LocalizationMessages.SSL_KMF_NO_PASSWORD_SET(ksName));
          factory = null;
        }
      }
      catch (KeyStoreException e) {
        throw SystemException.generic(SystemError.SECURITY_INITIALIZE, e);
      }
      catch (UnrecoverableKeyException e) {
        throw SystemException.generic(SystemError.SECURITY_UNRECOVERABLE, e);
      }
      catch (NoSuchAlgorithmException e) {
        throw SystemException.generic(SystemError.SECURITY_ALGORITHM, e);
      }
      catch (NoSuchProviderException e) {
        throw SystemException.generic(SystemError.SECURITY_PROVIDER, e);
      }
    }
    return factory;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trusted
  /**
   ** Initialize the trusted key store.
   **
   ** @return                    the {@link TrustManagerFactory} created.
   **                            <br>
   **                            Possible object is {@link TrustManagerFactory}.
   **
   ** @throws SystemException    if the <em>trusted</em> key store could not be
   **                            initialized.
   */
  protected TrustManagerFactory trusted()
    throws SystemException {

    TrustManagerFactory factory = null;
    if (this.trusted == null)
      return factory;

    KeyStore trusted = this.trusted.delegate;
    if (trusted == null && (this.trusted.payload != null || this.trusted.file != null)) {
      try {
        if (this.trusted.provider != null) {
          trusted = KeyStore.getInstance(this.trusted.type != null ? this.trusted.type : KeyStore.getDefaultType(), this.trusted.provider);
        }
        else {
          trusted = KeyStore.getInstance(this.trusted.type != null ? this.trusted.type : KeyStore.getDefaultType());
        }
        InputStream stream = null;
        try {
          if (this.trusted.payload != null) {
            stream = new ByteArrayInputStream(this.trusted.payload);
          }
          else if (!this.trusted.file.equals("NONE")) {
            stream = new FileInputStream(this.trusted.file);
          }
          trusted.load(stream, this.trusted.password);
        }
        finally {
          try {
            if (stream != null) {
              stream.close();
            }
          }
          catch (IOException ignored) {
            // intentionally left blank
            ;
          }
        }
      }
      catch (KeyStoreException e) {
        throw SystemException.generic(SystemError.TRUSTED_IMPLEMENTATION, this.trusted.type);
      }
      catch (CertificateException e) {
        throw new SystemException(SystemError.TRUSTED_CERT_NOTLOADED);
      }
      catch (FileNotFoundException e) {
        throw SystemException.generic(SystemError.TRUSTED_FILE_NOTFOUND, this.trusted.file);
      }
      catch (IOException e) {
        throw SystemException.generic(SystemError.TRUSTED_FILE_NOTLOADED, this.trusted.file);
      }
      catch (NoSuchProviderException e) {
        throw SystemException.generic(SystemError.TRUSTED_PROVIDER, this.trusted.provider);
      }
      catch (NoSuchAlgorithmException e) {
        throw new SystemException(SystemError.TRUSTED_ALGORITHM);
      }
    }
    if (trusted != null) {
      if (this.trusted.factoryAlgorithm == null)
        this.trusted.factoryAlgorithm = AccessController.doPrivileged(PropertyUtility.systemProperty(TRUSTED_MANAGER_FACTORY_ALGORITHM, TrustManagerFactory.getDefaultAlgorithm()));

      try {
        if (this.trusted.factoryProvider != null) {
          factory = TrustManagerFactory.getInstance(this.trusted.factoryAlgorithm, this.trusted.factoryProvider);
        }
        else {
          factory = TrustManagerFactory.getInstance(this.trusted.factoryAlgorithm);
        }
        factory.init(trusted);
      }
      catch (KeyStoreException e) {
        throw SystemException.generic(SystemError.SECURITY_INITIALIZE, e);
      }
      catch (NoSuchAlgorithmException e) {
        throw SystemException.generic(SystemError.SECURITY_ALGORITHM, e);
      }
      catch (NoSuchProviderException e) {
        throw SystemException.generic(SystemError.SECURITY_PROVIDER, e);
      }
    }
    return factory;
  }
}