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

    Copyright © 2019. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   TransportSocketFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    TransportSocketFactory.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.network;

import java.util.Set;
import java.util.HashSet;
import java.util.Enumeration;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.GeneralSecurityException;

import java.security.cert.X509Certificate;
import java.security.cert.CertificateException;

import javax.net.SocketFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import javax.naming.ldap.Rdn;
import javax.naming.ldap.LdapName;
import javax.naming.InvalidNameException;

import oracle.jdeveloper.workspace.iam.utility.StringUtility;

import oracle.jdeveloper.credential.model.CredentialInfo;
import oracle.jdeveloper.credential.model.CredentialConfiguration;
import oracle.jdeveloper.credential.model.CredentialConfigurationImpl;

////////////////////////////////////////////////////////////////////////////////
// class TransportSocketFactory
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** An {@link SSLSocketFactory} subclass that takes care of using
 ** {@link TransportSocketFactory.TrustCollector} as {@link X509TrustManager}
 ** subclass to collect server certificates and allows creating unconnected
 ** sockets, as required by application protocol handlers. This class is made
 ** public in order to allow being configured as the factory to be used by
 ** application properties mechanism, etc.
 ** <p>
 ** It may be subclassed by other factories, which create particular subclasses
 ** of sockets and thus provide a general framework for the addition of public
 ** socket-level functionality.
 ** <P>
 ** Socket factories are a simple way to capture a variety of policies related
 ** to the sockets being constructed, producing such sockets in a way which does
 ** not require special configuration of the code which asks for the sockets:
 ** <ul>
 **   <li>Due to polymorphism of both factories and sockets, different kinds of
 **       sockets can be used by the same application code just by passing it
 **       different kinds of factories.
 **   <li>Factories can themselves be customized with parameters used in socket
 **       construction. So for example, factories could be customized to return
 **       sockets with different networking timeouts or security parameters
 **       already configured.
 **   <li>The sockets returned to the application can be subclasses of
 **       java.net.Socket, so that they can directly expose new APIs for
 **       features such as compression, security, record marking, statistics
 **       collection, or firewall tunneling.
 ** </ul>
 ** Factory classes are specified by environment-specific configuration
 ** mechanisms.
 ** <br>
 ** For example, the <em>getDefault</em> method could return a factory that was
 ** appropriate for a particular user or applet, and a framework could use a
 ** factory customized to its own purposes.
 */
public class TransportSocketFactory extends SSLSocketFactory {

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final Set<X509Certificate> harvested   = new HashSet<X509Certificate>();
  private static final Set<X509Certificate> accumulated = new HashSet<X509Certificate>();

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  private static String          trustStorePath     = defaultTrustStorePath();
  private static String          trustStorePassword = defaultTrustStorePassword();

  // this one is needed here to allow being shared with embedded classes
  private static KeyStore        system;
  private static KeyStore        trusted;
  private static SSLContext      context;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final SSLSocketFactory delegate;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////////
  // class TrustCollector
  // ~~~~~ ~~~~~~~~~~~~~~
  /**
   ** An {@link X509TrustManager} subclass that accumulates unknown certificates
   ** in order to allow saving them afterwards.
   */
  private static class TrustCollector implements X509TrustManager {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final X509TrustManager     delegate;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** The constructor.
     **
     ** @param delegate          an {@link X509TrustManager} instance to do the
     **                          standard part of certificates validation job.
     **
     ** @throws KeyStoreException
     */
    TrustCollector(final X509TrustManager delegate)
      throws KeyStoreException {

      // ensure inhertitance
      super();

      // prevent bogus input
      if (delegate == null)
        throw new IllegalArgumentException("Delegated trust manager cannot be null.");

      if (system != null) {
        final Enumeration<String> aliases = system.aliases();
        while (aliases.hasMoreElements()) {
          String alias = aliases.nextElement();
          accumulated.add((X509Certificate)system.getCertificate(alias));
        }
      }
      if (trusted != null) {
        final Enumeration<String> aliases = trusted.aliases();
        while (aliases.hasMoreElements()) {
          String alias = aliases.nextElement();
          accumulated.add((X509Certificate)trusted.getCertificate(alias));
        }
      }

      // initilaize instance attributes
      this.delegate = delegate;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: getAcceptedIssuers (X509TrustManager)
    /**
     *+ Returns the list of certificate issuer authorities which are trusted for
     *+ authentication of peers.
     *+
     *  @return                  the list of certificate issuer authorities
     *                           which are trusted for authentication of peers.
     */
    public X509Certificate[] getAcceptedIssuers() {
      return this.delegate.getAcceptedIssuers();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: checkClientTrusted (X509TrustManager)
    /**
     ** Checks whether the specified certificate chain (partial or complete) can
     ** be validated and is trusted for client authentication for the specified
     ** authentication type.
     **
     ** @param  chain            the certificate chain to validate.
     ** @param  authType         the authentication type used.
     **
     ** @throws CertificateException     if the certificate chain can't be
     **                                  validated or isn't trusted.
     ** @throws IllegalArgumentException if the specified certificate chain is
     **                                  empty or <code>null</code>, or if the
     **                                  specified authentication type is
     **                                  <code>null</code> or an empty string.
     */
    @Override
    public void checkClientTrusted(final X509Certificate[] chain, final String authType)
      throws CertificateException {

      throw new UnsupportedOperationException();
//      this.delegate.checkClientTrusted(chain, authType);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: checkServerTrusted (X509TrustManager)
    /**
     ** Checks whether the specified certificate chain (partial or complete) can
     ** be validated and is trusted for server authentication for the specified
     ** key exchange algorithm.
     **
     ** @param  chain            the certificate chain to validate.
     ** @param  authType          the key exchange algorithm name.
     **
     ** @throws CertificateException     if the certificate chain can't be
     **                                  validated or isn't trusted.
     ** @throws IllegalArgumentException if the specified certificate chain is
     **                                  empty or <code>null</code>, or if the
     **                                  specified authentication type is
     **                                  <code>null</code> or an empty string.
     */
    @Override
    public void checkServerTrusted(final X509Certificate[] chain, final String authType)
      throws CertificateException {

      // catch the certificates provided for future purpose
      CertificateException rethrow = null;
      // check the certificate chain against the system truststore
      try {
        this.delegate.checkServerTrusted(chain, authType);
      }
      // the certificate chain was found not trusted
      catch (CertificateException e) {
        // check if the first certificate in the chain is not known yet to the
        // local certificate storage
        if (!accumulated.contains(chain[0])) {
          // save the exception to be re-thrown later if not known
          rethrow = e;
          // save the full chain to both local accumulators
          for (X509Certificate cert : chain) {
            accumulated.add(cert);
            harvested.add(cert);
          }
        }
      }
      // check and re-throw the exception if any
      if (rethrow != null) {
        throw rethrow;
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>TransportSocketFactory</code> that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   **
   */
  private TransportSocketFactory() {
    // ensure inheritance
    this.delegate = socketFactory();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessors and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   systemTrustStore
  /**
   ** Returns the Java default path to the key store used to verify
   ** trusted communication with an endpoint.
   **
   ** @return                    the Java default path to the key store
   **                            used to verify trusted communication with an
   **                            endpoint.
   */
  static KeyStore systemTrustStore()
    throws Exception {

    final File securityDirectory = new File(System.getProperty ("java.home"), "lib/security");
    File systemTrustStore  = new File(securityDirectory, "jssecacerts");
    if (!systemTrustStore.exists()) {
      systemTrustStore = new File(securityDirectory, "cacerts");
    }
    System.out.println ("... loading system truststore from '" + systemTrustStore.getCanonicalPath() + "' ...");
    return populateKeyStore(systemTrustStore.getCanonicalPath(), systemTrustStorePassword().toCharArray());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   systemTrustStorePassword
  /**
   ** Returns the Java default password required to unlock the Java default
   ** trusted key store.
   **
   ** @return                    the Java default password required to
   **                            unlock the Java default trusted key store.
   */
  static String systemTrustStorePassword() {
    return "changeit";
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   defaultTrustStorePath
  /**
   ** Returns the JDeveloper default path to the key store used to verify
   ** trusted communication with an endpoint.
   **
   ** @return                    the JDeveloper default path to the key store
   **                            used to verify trusted communication with an
   **                            endpoint.
   */
  public static String defaultTrustStorePath() {
    return credential().getHttpsClientTrustKeystore();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   defaultTrustStorePassword
  /**
   ** Returns the JDeveloper default password required to unlock the JDeveloper
   ** default trusted key store.
   **
   ** @return                    the JDeveloper default password required to
   **                            unlock the JDeveloper default trusted key
   **                            store.
   */
  static String defaultTrustStorePassword() {
    return new String(credential().getHttpsClientTrustPassword());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trustStorePath
  /**
   ** Sets the path to the key store used to verify trusted communication with
   ** an endpoint.
   **
   ** @param  path               the path to the key store used to verify
   **                            trusted communication with an endpoint.
   */
  public static void trustStorePath(final String path) {
    trustStorePath = path;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trustStorePath
  /**
   ** Returns the path to the key store used to verify trusted communication
   ** with an endpoint.
   **
   ** @return                    the path to the key store used to verify
   **                            trusted communication with an endpoint.
   */
  static String trustStorePath() {
    return trustStorePath;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trustStorePassword
  /**
   ** Sets the password required to unlock the trusted key store.
   **
   ** @param  value              the password required to unlock the trusted key
   **                            store.
   */
  public static void trustStorePassword(final String value) {
    trustStorePassword = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trustStorePassword
  /**
   ** Returns the password required to unlock the trusted key store.
   **
   ** @return                    the password required to unlock the trusted key
   **                            store.
   */
  static String trustStorePassword() {
    return trustStorePassword;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accumulated
  /**
   ** Returns the certificate chain provided by any server and catched by
   ** checkServerTrusted.
   **
   ** @return                  the certificate chain provided by any server and
   **                          catched by checkServerTrusted.
   */
  static final Set<X509Certificate> accumulated() {
    return accumulated;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   harvested
  /**
   ** Returns the harvested certificate chains provided by any server and
   ** catched by checkServerTrusted.
   **
   ** @return                  the harvested certificate chain provided by any
   **                          server and catched by checkServerTrusted.
   */
  static final Set<X509Certificate> harvested() {
    return harvested;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   harvested
  /**
   ** Verifies if the specified certificate is currently known by any of the
   ** {@link KeyStore}s.
   **
   ** @param  certificate      the {@link X509Certificate} to verify.
   **
   ** @return                  <code>true</code> if the specified
   **                          certificate is already known.
   **
   ** @throws KeyStoreException if one of the keystore has not been initialized
   **                           (loaded).
   */
  static final boolean harvested(final X509Certificate certificate)
    throws KeyStoreException {
    if (system.getCertificateAlias(certificate) != null) {
      System.out.println("Certificate already known to the system truststore.");
      return true;
    }
    else if (trusted.getCertificateAlias(certificate) != null) {
      System.out.println("Certificate already known to the default truststore.");
      return true;
    }
    else {
      return false;
    }
  }

  static final void save(final Set<X509Certificate> certificate)
    throws IOException
    ,      KeyStoreException
    ,      InvalidNameException
    ,      CertificateException
    ,      NoSuchAlgorithmException {
    // assign aliases using host name and certificate common name
    for (X509Certificate cursor : certificate) {
      String alias = "host" + " - " + commonName(cursor);
      trusted.setCertificateEntry(alias, cursor);
      System.out.println();
      System.out.println(cursor);
      System.out.println();
      System.out.println("Certificate will be added using alias '" + alias + "'.");
    }

    // save them to the extra truststore for certificates known just
    // locally
    System.out.println();
    System.out.println("... adding certificate(s) to the extra truststore ...");
    OutputStream out = new FileOutputStream(defaultTrustStorePath());
    trusted.store(out, defaultTrustStorePassword().toCharArray());
    out.close();
    System.out.println();
    System.out.println("New certificates saved to truststore '" + defaultTrustStorePath() + "' in the current directory.");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commonName
  public static String commonName(final X509Certificate certificate)
    throws InvalidNameException {

    // use LDAP API to parse the certifiate Subject :)
    // see http://stackoverflow.com/a/7634755/972463
    final LdapName dn = new LdapName(certificate.getSubjectX500Principal().getName());
    String   cn = StringUtility.EMPTY;
    for (Rdn rdn : dn.getRdns()) {
      if (rdn.getType().equals("CN")) {
        cn = rdn.getValue().toString();
      }
    }
    return cn;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   socketFactory
  /**
   ** Returns a <code>SocketFactory</code> object for ths context.
   **
   ** @return                    the <code>SocketFactory</code> object.
   **
   ** @throws IllegalStateException if the context implementation requires
   **                               initialization and the <code>init()</code>
   **                               has not been called
   */
  public static synchronized SSLSocketFactory socketFactory() {
    return socketFactory(TransportSecurity.Protocol.TLS);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   socketFactory
  /**
   ** Returns a <code>SocketFactory</code> object for ths context.
   **
   ** @param  protocol           the kind of protocol the socket needs to
   **                            support.
   **
   ** @return                    the <code>SocketFactory</code> object.
   **
   ** @throws IllegalStateException if the context implementation requires
   **                               initialization and the <code>init()</code>
   **                               has not been called
   */
  public static synchronized SSLSocketFactory socketFactory(final TransportSecurity.Protocol protocol) {
    if (context == null) {
      try {
        // initialize the keystores
        system  = systemTrustStore();
        trusted = trustKeyStore(trustStorePath, trustStorePassword);
        // obtain a TrustManagerFactory instance
        final TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        // initialize it with known certificate data from the keystore
        // initialized above
        factory.init(system);
        // obtain default TrustManager instance
        final X509TrustManager trustDefault = (X509TrustManager)factory.getTrustManagers()[0];
        context = new TransportSecurity().protocol(protocol).trustManager(new TrustCollector(trustDefault)).build();
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }
    return context.getSocketFactory();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   credential
  /**
   ** Returns the user name and password that are required to obtain and unlock
   ** the JDeveloper default trusted key store.
   **
   ** @return                    the user name and password credentials.
   */
  private static CredentialInfo credential() {
    CredentialConfiguration config = CredentialConfigurationImpl.getInstance();
    return config.getCredential("HTTPS Credential");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDefaultCipherSuites (SSLSocketFactory)
  /**
   ** Returns the list of cipher suites which are enabled by default.
   ** <br>
   ** Unless a different list is enabled, handshaking on an SSL connection will
   ** use one of these cipher suites. The minimum quality of service for these
   ** defaults requires confidentiality protection and server authentication
   ** (that is, no anonymous cipher suites).
   **
   ** @return                    array of the cipher suites enabled by default.
   **
   ** @see    #getSupportedCipherSuites()
   */
  @Override
  public String[] getDefaultCipherSuites() {
    return this.delegate.getDefaultCipherSuites();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSupportedCipherSuites (SSLSocketFactory)
  /**
   ** Returns the names of the cipher suites which could be enabled for use on
   ** an SSL connection. Normally, only a subset of these will actually be
   ** enabled by default, since this list may include cipher suites which do not
   ** meet quality of service requirements for those defaults. Such cipher
   ** suites are useful in specialized applications.
   **
   ** @return                    an array of cipher suite names
   ** @see    #getDefaultCipherSuites()
   */
  @Override
  public String[] getSupportedCipherSuites() {
    return this.delegate.getSupportedCipherSuites();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createSocket (SocketFactory)
  /**
   ** Creates a socket and connects it to the specified remote host at the
   ** specified remote port.
   ** <br>
   ** This socket is configured using the socket options established for this
   ** factory.
   ** <p>
   ** If there is a security manager, its <code>checkConnect</code> method is
   ** called with the host address and <code>port</code> as its arguments. This
   ** could result in a SecurityException.
   **
   ** @param  host               the server host name with which to connect, or
   **                            <code>null</code> for the loopback address.
   ** @param  port               the server port.
   **
   ** @return                    the <code>Socket</code>.
   **
   ** @throws IOException              if an I/O error occurs when creating the
   **                                  socket.
   ** @throws SecurityException        if a security manager exists and its
   **                                  <code>checkConnect</code> method doesn't
   **                                  allow the operation.
   ** @throws UnknownHostException     if the host is not known.
   ** @throws IllegalArgumentException if the port parameter is outside the
   **                                  specified range of valid port values,
   **                                  which is between 0 and 65535, inclusive.
   **
   ** @see    SecurityManager#checkConnect
   ** @see    java.net.Socket#Socket(String, int)
   */
  @Override
  public Socket createSocket(final String host, final int port)
    throws IOException, UnknownHostException {

    return this.delegate.createSocket(host, port);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createSocket (SocketFactory)
  /**
   ** Creates a socket and connects it to the specified remote host on the
   ** specified remote port.
   ** <br>
   ** The socket will also be bound to the local address and port supplied. This
   ** socket is configured using the socket options established for this
   ** factory.
   ** <p>
   ** If there is a security manager, its <code>checkConnect</code> method is
   ** called with the host address and <code>port</code> as its arguments. This
   ** could result in a SecurityException.
   **
   ** @param  host               the server host name with which to connect, or
   **                            <code>null</code> for the loopback address.
   ** @param  port               the server port.
   ** @param  localHost          the local address the socket is bound to.
   ** @param  localPort          the local port the socket is bound to.
   **
   ** @return                    the <code>Socket</code>
   **
   ** @throws IOException              if an I/O error occurs when creating the
   **                                  socket.
   ** @throws SecurityException        if a security manager exists and its
   **                                  <code>checkConnect</code> method doesn't
   **                                  allow the operation.
   ** @throws UnknownHostException     if the host is not known.
   ** @throws IllegalArgumentException if the port parameter or localPort
   **                                  parameter is outside the specified range
   **                                  of valid port values, which is between 0
   **                                  and 65535, inclusive.
   **
   ** @see    SecurityManager#checkConnect
   ** @see    java.net.Socket#Socket(String, int, java.net.InetAddress, int)
   */
  @Override
  public Socket createSocket(final String host, final int port, final InetAddress localHost, final int localPort)
    throws IOException, UnknownHostException {

    return this.delegate.createSocket(host, port, localHost, localPort);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createSocket (SocketFactory)
  /**
   ** Creates a socket and connects it to the specified port number at the
   ** specified address.
   ** <br>
   ** This socket is configured using the socket options established for this
   ** factory.
   ** <p>
   ** If there is a security manager, its <code>checkConnect</code> method is
   ** called with the host address and <code>port</code> as its arguments. This
   ** could result in a SecurityException.
   **
   ** @param  host               the server host.
   ** @param  port               the server port.
   **
   ** @return                    the <code>Socket</code>
   **
   ** @throws IOException              if an I/O error occurs when creating the
   **                                  socket.
   ** @throws SecurityException        if a security manager exists and its
   **                                  <code>checkConnect</code> method doesn't
   **                                  allow the operation.
   ** @throws IllegalArgumentException if the port parameter is outside the
   **                                  specified range of valid port values,
   **                                  which is between 0 and 65535, inclusive.
   ** @throws NullPointerException     if <code>host</code> is
   **                                 <code>null</code>.
   **
   ** @see    SecurityManager#checkConnect
   ** @see    java.net.Socket#Socket(java.net.InetAddress, int)
   */
  @Override
  public Socket createSocket(final InetAddress host, final int port)
    throws IOException {

    return this.delegate.createSocket(host, port);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createSocket (SocketFactory)
  /**
   ** Creates a socket and connect it to the specified remote address on the
   ** specified remote port.
   ** <br>
   ** The socket will also be bound to the local address and port suplied.
   ** <br>
   ** The socket is configured using the socket options established for this
   ** factory.
   ** <p>
   ** If there is a security manager, its <code>checkConnect</code> method is
   ** called with the host address and <code>port</code> as its arguments. This
   ** could result in a SecurityException.
   **
   ** @param  address            the server network address
   ** @param  port               the server port.
   ** @param  localAddress       the local address the socket is bound to.
   ** @param  localPort          the local port the socket is bound to.
   **
   ** @return                    the <code>Socket</code>
   **
   ** @throws IOException              if an I/O error occurs when creating the
   **                                  socket.
   ** @throws SecurityException        if a security manager exists and its
   **                                  <code>checkConnect</code> method doesn't
   **                                  allow the operation.
   ** @throws IllegalArgumentException if the port parameter or localPort
   **                                  parameter is outside the specified range
   **                                  of valid port values, which is between 0
   **                                  and 65535, inclusive.
   ** @throws NullPointerException     if <code>address</code> is
   **                                  <code>null</code>.
   **
   ** @see    SecurityManager#checkConnect
   ** @see    java.net.Socket#Socket(java.net.InetAddress, int, java.net.InetAddress, int)
   */
  @Override
  public Socket createSocket(final InetAddress address, final int port, final InetAddress localAddress, final int localPort)
    throws IOException {

    return this.delegate.createSocket(address, port, localAddress, localPort);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createSocket (SSLSocketFactory)
  /**
   ** Returns a socket layered over an existing socket connected to the named
   ** host, at the given port.
   ** <br>
   ** This constructor can be used when tunneling SSL through a proxy or when
   ** negotiating the use of SSL over an existing socket. The host and port
   ** refer to the logical peer destination.
   ** <br>
   ** This socket is configured using the socket options established for this
   ** factory.
   **
   ** @param  socket             the existing socket.
   ** @param  host               the server host.
   ** @param  port               the server port.
   ** @param  autoClose          close the underlying socket when this socket is
   **                            closed.
   **
   ** @return                    a socket connected to the specified host and
   **                            port.
   **
   ** @throws IOException          if an I/O error occurs when creating the
   **                              socket.
   ** @throws NullPointerException if the parameter s is <code>null</code>.
   */
  @Override
  public Socket createSocket(final Socket socket, final String host, final int port, final boolean autoClose)
    throws IOException {

    return this.delegate.createSocket(socket, host, port, autoClose);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDefault
  public static SocketFactory getDefault() {
    return build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  public static SSLSocketFactory build() {
    return new TransportSocketFactory();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ask
  static String ask(final String prompt)
    throws IOException {

    System.out.print(prompt);
    final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    return reader.readLine().trim();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trustKeyStore
  /**
   ** Loads a {@link KeyStore} from the given path.
   ** <p>
   ** A password may be given to unlock the keystore (e.g. the {@link KeyStore}
   ** resides on a hardware token device), or to check the integrity of the
   ** {@link KeyStore} data. If a password is not given for integrity checking,
   ** then integrity checking is not performed.
   ** <p>
   ** Note that if the {@link KeyStore} has already been loaded, it is
   ** reinitialized and loaded again from the given path.
   **
   ** @param  path               the abstract path from which the keystore is
   **                            loaded.
   ** @param  password           the password used to check the integrity of the
   **                            {@link KeyStore}, the password used to unlock
   **                            the {@link KeyStore}, or <code>null</code>.
   **
   ** @return                    a {@link KeyStore} either populated from the
   **                            specified path or a initialized with defaults.
   **
   ** @throws Exception          if there is an I/O or format problem with the
   **                            {@link KeyStore} data, if a password is
   **                            required but not given, or if the given
   **                            password was incorrect. If the error is due to
   **                            a wrong password, the
   **                            {@link Throwable#getCause cause} of the
   **                            {@link IOException} should be an
   **                            <code>UnrecoverableKeyException</code>.
   */
  public static KeyStore trustKeyStore(final String path, final String password)
    throws Exception {

    System.out.println ("... loading system truststore from '" + path + "' ...");
    return populateKeyStore(path, password.toCharArray());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createSocket
  /**
   ** Bypass the default <code>javax.net.SocketFactory</code> implementation
   ** that throws <code>java.net.SocketException</code> with nested
   ** <code>java.lang.UnsupportedOperationException</code> with "Unconnected
   ** sockets not implemented" message.
   **
   ** @throws IOException          if an I/O error occurs when creating the
   **                              socket.
   */
  @Override
  public Socket createSocket()
    throws IOException {

    return new Socket();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateKeyStore
  /**
   ** Loads a {@link KeyStore} from the given path.
   ** <p>
   ** A password may be given to unlock the keystore (e.g. the {@link KeyStore}
   ** resides on a hardware token device), or to check the integrity of the
   ** {@link KeyStore} data. If a password is not given for integrity checking,
   ** then integrity checking is not performed.
   ** <p>
   ** Note that if the {@link KeyStore} has already been loaded, it is
   ** reinitialized and loaded again from the given path.
   **
   ** @param  path               the abstract path from which the keystore is
   **                            loaded.
   ** @param  password           the password used to check the integrity of the
   **                            {@link KeyStore}, the password used to unlock
   **                            the {@link KeyStore}, or <code>null</code>.
   **
   ** @return                    a {@link KeyStore} either populated from the
   **                            specified path or a initialized with defaults.
   **
   ** @throws IOException              if there is an I/O or format problem with
   **                                  the {@link KeyStore} data, if a password
   **                                  is required but not given, or if the
   **                                  given password was incorrect. If the
   **                                  error is due to a wrong password, the
   **                                  {@link Throwable#getCause cause} of the
   **                                  {@link IOException} should be an
   **                                  <code>UnrecoverableKeyException</code>.
   ** @throws NoSuchAlgorithmException if the algorithm used to check the
   **                                  integrity of the {@link KeyStore} cannot
   **                                  be found.
   ** @throws CertificateException     if any of the certificates in the
   **                                  {@link KeyStore} could not be loaded.
   */
  private static KeyStore populateKeyStore(final String path, final char[] password)
    throws Exception {

    KeyStore store = null;
    if (!StringUtility.empty(path)) {
      if (new File(path).exists()) {
        FileInputStream stream = null;
        try {
          stream = new FileInputStream(path);
          store  = KeyStore.getInstance(KeyStore.getDefaultType());
          store.load(stream, password != null ? password : null);
          System.out.println ("... truststore loaded from '" + path + "' ...");
        }
        catch (GeneralSecurityException e) {
          throw e;
        }
        catch (IOException e) {
          throw e;
        }
        finally {
          if (stream != null)
            stream.close();
        }
      }
      // the given path is not valid hence load a key store with defaults
      else {
        System.out.println("... creating truststore " + path + " as a new one ...");
        store  = KeyStore.getInstance(KeyStore.getDefaultType());
        store.load(null, password != null ? password : null);
      }
    }
    // a path was not given hence load a key store with defaults
    else {
      System.out.println("... creating truststore as a new one ...");
      store = KeyStore.getInstance(KeyStore.getDefaultType());
      store.load(null, password != null ? password : null);
    }
    // retrieves the number of entries in this keystore.
    // this raise a KeyStoreException if the keystore has not been initialized
    // hence we load it again with defaults
    try {
      store.size();
    }
    catch (KeyStoreException e) {
      System.out.println("... creating truststore as a new one ...");
      store.load(null, password != null ? password : null);
    }
    return store;
  }
}