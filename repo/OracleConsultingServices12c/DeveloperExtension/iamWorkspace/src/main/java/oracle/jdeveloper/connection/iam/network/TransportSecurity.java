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

    File        :   TransportSecurity.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    TransportSecurity.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.network;

import java.util.Set;
import java.util.HashSet;

import java.io.IOException;
import java.io.EOFException;

import java.net.SocketException;
import java.net.UnknownHostException;

import java.security.Provider;
import java.security.SecureRandom;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.GeneralSecurityException;

import java.security.cert.X509Certificate;
import java.security.cert.CertificateException;

import javax.naming.InvalidNameException;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.SSLHandshakeException;

import oracle.javatools.dialogs.MessageDialog;

import oracle.ide.Ide;

import oracle.jdeveloper.connection.iam.Bundle;

////////////////////////////////////////////////////////////////////////////////
// class TransportSecurity
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** An SSL context builder provides an interface for incrementally constructing
 ** {@link SSLContext} instances for use when securing connections with SSL or
 ** the startTLS extended operation. The {@link #build()} should be called in
 ** order to obtain the {@link SSLContext}.
 ** <p>
 ** For example, use the SSL context builder when setting up LDAP options needed
 ** to use startTLS. {@link javax.net.ssl.TrustManager TrustManagers} has
 ** methods you can use to set the trust manager for the SSL context builder.
 ** <pre>
 **  TransportSecurity context = new TransportSecurity().trustManager(...).build();
 **  options.setSSLContext(sslContext);
 **  options.startTLS(true);
 **
 **  String host = ...;
 **  int    port = ...;
 **  LDAPConnectionFactory factory = new LDAPConnectionFactory(host, port, options);
 ** Connection connection = factory.getConnection();
 ** // Connection uses atartTLS...
 ** </pre>
 */
public final class TransportSecurity {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Protocol
  // ~~~~ ~~~~~~~~
  /**
   ** This is a type-safe enumerated type of known security-level TCP-based
   ** protocols
   */
  public enum Protocol {
      /**
       ** SSL protocol: supports some version of SSL; may support other versions.
       */
      SSL("SSL")
      /**
       ** SSL protocol: supports SSL version 2 or higher; may support other
       ** versions.
       */
    , SSLv2("SSLv2")
      /**
       ** SSL protocol: supports SSL version 3 or higher; may support other
       ** versions.
       */
    , SSLv3("SSLv3")
      /**
       ** SSL protocol: supports some version of TLS; may support other versions.
       */
    , TLS("TLS")
      /**
       ** SSL protocol: supports RFC 2246: TLS version 1.0 ; may support other
       ** versions.
       */
    , TLSv1("TLSv1")
      /**
       ** SSL protocol: supports RFC 4346: TLS version 1.1 ; may support other
       ** versions.
       */
    , TLSv1_1("TLSv1.1")
      /**
       ** SSL protocol: supports RFC 5246: TLS version 1.2 ; may support other
       ** versions.
       */
    , TLSv1_2("TLSv1.2")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final String value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** The constructor for a given standard port
     **
     ** @param  port             the standard port for a protocol
     */
    Protocol(final String value) {
      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods groped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: from (generated)
    public static Protocol from(final String value) {
      for (Protocol p : Protocol.values()) {
        if (p.value.equals(value)) {
          return p;
        }
      }
      return null;
    }
  }
  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private Protocol     protocol     = Protocol.TLSv1_2;
  private KeyManager   keyManager   = null;
  private TrustManager trustManager = null;
  private Provider     provider     = null;
  private String       providerName = null;
  private SecureRandom random       = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>TransportSecurity</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TransportSecurity() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   secureRandom
  /**
   ** Sets the secure random number generator which the SSL context should use.
   ** <br>
   ** By default, the default secure random number generator associated with
   ** this JVM will be used.
   **
   ** @param  random             the secure random number generator which the
   **                            SSL context should use, which may be
   **                            <code>null</code> indicating that the default
   **                            secure random number generator associated with
   **                            this JVM will be used.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public TransportSecurity secureRandom(SecureRandom random) {
    this.random = random;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trustManager
  /**
   ** Sets the trust manager which the SSL context should use.
   ** <br>
   ** By default, no trust manager is specified indicating that only
   ** certificates signed by the authorities associated with this JVM will be
   ** accepted.
   **
   ** @param  manager            the trust manager which the SSL context should
   **                            use, which may be <code>null</code> indicating
   **                            that only certificates signed by the
   **                            authorities associated with this JVM will be
   **                            accepted.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public TransportSecurity trustManager(TrustManager manager) {
    this.trustManager = manager;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   protocol
  /**
   ** Sets the protocol which the SSL context should use.
   ** <br>
   ** By default, TLSv1.2 will be used.
   **
   ** @param  protocol           the protocol which the SSL context should use,
   **                            which may be <code>null</code> indicating that
   **                            TLSv1.2 will be used.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public TransportSecurity protocol(final Protocol protocol) {
    this.protocol = protocol;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   provider
  /**
   ** Sets the provider which the SSL context should use.
   ** <br>
   ** By default, the default provider associated with this JVM will be used.
   **
   ** @param  provider           the provider which the SSL context should use,
   **                            which may be <code>null</code> indicating that
   **                            the default provider associated with this JVM
   **                            will be used.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public TransportSecurity provider(final Provider provider) {
    this.provider     = provider;
    this.providerName = null;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   provider
  /**
   ** Sets the provider which the SSL context should use.
   ** <br>
   ** By default, the default provider associated with this JVM will be used.
   **
   ** @param  provider           the name of the provider which the SSL context
   **                            should use, which may be <code>null</code>
   **                            indicating that the default provider associated
   **                            with this JVM will be used.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public TransportSecurity provider(final String provider) {
    this.provider     = null;
    this.providerName = provider;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   keyManager
  /**
   ** Sets the key manager which the SSL context should use.
   ** <br>
   ** By default, no key manager is specified indicating that no certificates
   ** will be used.
   **
   ** @param  manager            the key manager which the SSL context should
   **                            use, which may be <code>null</code> indicating
   **                            that no certificates will be used.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public TransportSecurity keyManager(final KeyManager manager) {
    this.keyManager = manager;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Creates a {@code SSLContext} using the parameters of this SSL context
   ** builder.
   **
   ** @return                    a {@code SSLContext} using the parameters of
   **                            this SSL context builder.
   **
   ** @throws GeneralSecurityException if the SSL context could not be created,
   **                                  perhaps due to missing algorithms.
   */
  public SSLContext build()
    throws GeneralSecurityException {

    SSLContext context;
    if (this.provider != null) {
      context = SSLContext.getInstance(this.protocol.value, this.provider);
    }
    else if (this.providerName != null) {
      context = SSLContext.getInstance(this.protocol.value, this.providerName);
    }
    else {
      context = SSLContext.getInstance(this.protocol.value);
    }
    TrustManager[] tm = null;
    if (this.trustManager != null)
      tm = new TrustManager[] { this.trustManager };

    KeyManager[] km = null;
    if (this.keyManager != null)
      km = new KeyManager[] { this.keyManager };

    context.init(km, tm, random);
    return context;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateCertificate
  /**
   ** Validates if the specified server provides a certificate that can be
   ** trusted to ensure security on the transport layer.
   **
   ** @param  serverName          the server host name with which to connect, or
   **                             <code>null</code> for the loopback address.
   ** @param  serverPort          the server port.
   ** @param  timeOut             the specified timeout, in milliseconds.
   **
   ** @throws TransportSecurityException if the certificate validation fails.
   */
  public static void validateCertificate(final String serverName, final int serverPort, final int timeOut)
    throws TransportSecurityException {

    try {
      final SSLSocketFactory factory = TransportSocketFactory.socketFactory();
      final SSLSocket        socket  = (SSLSocket)factory.createSocket(serverName, serverPort);
      socket.setSoTimeout(timeOut);
      try {
        socket.startHandshake();
        System.out.println("No errors, certificate is trusted signed.");
      }
      // LDAP/startTLS servers seem tending to yield an SSLHandshakeException
      // with nested EOFException or a SocketException with "Connection reset"
      // message.
      // Thus three distinct cases for considering a startTLS extension below
      catch (SSLHandshakeException e) {
        // prevent complains about usage of internal proprietary API 
        final Class<?> intermediate = e.getCause().getClass();
        final Class<?> root         = e.getCause().getCause().getClass();
        if (intermediate.getName().equals("sun.security.validator.ValidatorException") && root.getName().equals("sun.security.provider.certpath.SunCertPathBuilderException")) {
          // this is the standard case: looks like we just got a previously
          // unknown certificate, so report it and go ahead...
          System.out.println(e.toString());
        }
        // "Remote host closed connection during handshake"
        else if (e.getCause().getClass().equals(EOFException.class)) {
          // close the unsuccessful SSL socket
          socket.close();
          // consider trying startTLS extension over ordinary socket
          if (!TransportStartTLS.negotiate(TransportStartTLS.Protocol.LDAP.name(), serverName, serverPort)) {
            // TransportStartTLS.negotiate is expected to have reported everything
            // except the final good-bye...
            throw new TransportSecurityException(e.getLocalizedMessage(), e);
          }
        }
        else {
          throw new TransportSecurityException(e.getLocalizedMessage(), e);
        }
      }
      catch (SSLException e) {
        if (e.getMessage().equals("Unrecognized SSL message, plaintext connection?")) {
          System.out.println("ERROR on SSL handshake: " + e.toString());
          socket.close();
          // consider trying startTLS extension over ordinary socket
          if (!TransportStartTLS.negotiate(TransportStartTLS.Protocol.LDAP.name(), serverName, serverPort)) {
            // TransportStartTLS.negotiate is expected to have reported everything
            // except the final good-bye...
            throw new TransportSecurityException(e.getLocalizedMessage(), e);
          }
        }
        else {
          throw new TransportSecurityException(e.getLocalizedMessage(), e);
        }
      }
      catch (SocketException e) {
        if (e.getMessage().equals("Connection reset")) {
          System.out.println("ERROR on SSL handshake: " + e.toString());
          socket.close();
          // consider trying startTLS extension over ordinary socket
          if (!TransportStartTLS.negotiate(TransportStartTLS.Protocol.LDAP.name(), serverName, serverPort)) {
            // TransportStartTLS.negotiate is expected to have reported everything
            // except the final good-bye...
            throw new TransportSecurityException(e.getLocalizedMessage(), e);
          }
          else {
            throw new TransportSecurityException(e.getLocalizedMessage(), e);
          }
        }
      }
      catch (Exception e) {
        if (TransportSocketFactory.harvested() == null) {
          throw new TransportSecurityException(Bundle.string(Bundle.CERTIFICATE_EMPTY));
        }
      }
      finally {
        socket.close();
      }
    }
    catch (UnknownHostException e) {
      throw new TransportSecurityException(Bundle.format(Bundle.CONTEXT_ERROR_UNKNOWN_HOST, e.getLocalizedMessage()), e);
    }
    catch (IOException e) {
      throw new TransportSecurityException(e.getLocalizedMessage(), e);
    }

    // get the full set of new accumulated certificates as an array
    final Set<X509Certificate> confirmed = new HashSet<X509Certificate>();
    for (X509Certificate cursor : TransportSocketFactory.harvested()) {
      final String message = Bundle.format(Bundle.CERTIFICATE_TEXT_LABEL, serverName);
      if (MessageDialog.confirm(Ide.getMainWindow(), message, Bundle.string(Bundle.CERTIFICATE_DIALOG_TITLE), null, true, Bundle.string(Bundle.CERTIFICATE_ACCEPT_LABEL), Bundle.string(Bundle.CERTIFICATE_NOTACCEPT_LABEL)))
        confirmed.add(cursor);
    }
    if (confirmed.size() > 0) {
      try {
        TransportSocketFactory.save(confirmed);
      }
      catch (IOException e) {
        throw new TransportSecurityException(e.getLocalizedMessage(), e);
      }
      catch (InvalidNameException e) {
        throw new TransportSecurityException(e.getLocalizedMessage(), e);
      }
      catch (CertificateException e) {
        throw new TransportSecurityException(e.getLocalizedMessage(), e);
      }
      catch (NoSuchAlgorithmException e) {
        throw new TransportSecurityException(e.getLocalizedMessage(), e);
      }
      catch (KeyStoreException e) {
        throw new TransportSecurityException(e.getLocalizedMessage(), e);
      }
    }
  }
}