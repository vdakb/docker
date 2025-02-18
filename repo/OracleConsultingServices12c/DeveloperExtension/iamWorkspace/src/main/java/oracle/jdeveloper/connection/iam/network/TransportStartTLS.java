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

    File        :   TransportStartTLS.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    TransportStartTLS.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.network;

import java.util.Hashtable;
import java.util.Properties;

import java.io.IOException;

import java.security.Security;

import javax.net.ssl.SSLHandshakeException;

import javax.mail.Store;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.AuthenticationFailedException;

import javax.naming.Context;
import javax.naming.NamingException;

import javax.naming.ldap.LdapContext;
import javax.naming.ldap.StartTlsRequest;
import javax.naming.ldap.StartTlsResponse;
import javax.naming.ldap.InitialLdapContext;

import oracle.jdeveloper.workspace.iam.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// class TransportStartTLS
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** A startTLS protocol extension wrapper class that makes an effort to decide
 ** on the application protocol to try and runs an appropriate protocol handler.
 */
class TransportStartTLS {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Protocol
  // ~~~~ ~~~~~~~~
  /**
   ** This is a type-safe enumerated type of known application-level TCP-based
   ** protocols that support startTLS extension, with their standard port
   ** numbers.
   */
  enum Protocol {
      SMTP(25)
    , POP3(110)
    // TODO implement NNTP/startTLS (119) some day...
    , IMAP(143)
    , LDAP(389)
    // TODO implement XMPP/startTLS (5222) some day...
    ;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final int port;

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
    Protocol(final int port) {
      this.port = port;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods groped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: from (generated)
    public static Protocol from(final int port) {
      for (Protocol p : Protocol.values()) {
        if (p.port == port) {
          return p;
        }
      }
      return null;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // interface Handler
  // ~~~~~~~~~ ~~~~~~~~
  /**
   ** An interface to be implemented by application protocol specific startTLS
   ** handlers, to be used by {@link Starttls} class.
   */
  interface Handler {

    ////////////////////////////////////////////////////////////////////////////
    // Member classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // class SMTP
    // ~~~~~ ~~~~
    /**
     ** A {@link Handler} implementation for SMTP protocol.
     */
    static class SMTP implements Handler {

      //////////////////////////////////////////////////////////////////////////
      // Constructors
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: Ctor
      /**
       ** Constructs an empty <code>SMTP</code> startTLS handler that allows use
       ** as a JavaBean.
       ** <br>
       ** Zero argument constructor required by the framework.
       ** <br>
       ** Default Constructor
       */
      SMTP() {
        // ensure inheritance
        super();
      }

      //////////////////////////////////////////////////////////////////////////
      // Method of implemented interfaces
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: run (Handler)
      /**
       ** Do the application protocol specific actions to initiate a protocol
       ** specific startTLS session, starting from a new connection.
       **
       ** @param  host           the host to connect to.
       ** @param  port           the port to connect to.
       **
       ** @return                <code>true</code> if getting a certificate via
       **                        startTLS handler is believed to be successful,
       **                        <code>false</code> otherwise.
       */
      // see http://javamail.kenai.com/nonav/javadocs/com/sun/mail/smtp/package-summary.html
      @Override
      public boolean run(final String host, final int port) {
        System.out.println ("... trying SMTP with startTLS extension ...");
        Properties environment = new Properties();
        environment.put("mail.transport.protocol",         "smtp");
        environment.put("mail.smtp.socketFactory.class",   "javax.net.ssl.SSLSocketFactory");
        environment.put("mail.smtp.socketFactory.fallback", "false");
        environment.put("mail.smtp.starttls.enable",        "true");

        Security.setProperty("ssl.SocketFactory.provider", TransportSocketFactory.class.getName());
        final Session session = Session.getDefaultInstance (environment);
        Transport tr = null;
        try {
          tr = session.getTransport();
        }
        catch (NoSuchProviderException e) {
          e.printStackTrace();
          return false;
        }
        try {
          tr.connect(host, port, null, null);
        }
        catch (MessagingException e) {
          if (e.getNextException() instanceof SSLHandshakeException) {
            // likely got an unknown certificate, just report it and
            // return success
            System.out.println("ERROR on SSL handshake: " + e.toString());
            return true;
          }
          else {
            e.printStackTrace();
            return false;
          }
        }
        finally {
          if (tr.isConnected()) {
            try {
              tr.close();
            }
            catch (MessagingException e) {
              // intentionally left blank
              ;
            }
          }
        }
        return false;
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // class POP3
    // ~~~~~ ~~~~
    /**
     ** A {@link Handler} implementation for POP3 protocol.
     */
    class POP3 implements Handler {

      //////////////////////////////////////////////////////////////////////////
      // Constructors
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: Ctor
      /**
       ** Constructs an empty <code>POP3</code> startTLS handler that allows use
       ** as a JavaBean.
       ** <br>
       ** Zero argument constructor required by the framework.
       ** <br>
       ** Default Constructor
       */
      public POP3() {
        // ensure inheritance
        super();
      }

      //////////////////////////////////////////////////////////////////////////
      // Method of implemented interfaces
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: run (Handler)
      /**
       ** Do the application protocol specific actions to initiate a protocol
       ** specific startTLS session, starting from a new connection.
       **
       ** @param  host           the host to connect to.
       ** @param  port           the port to connect to.
       **
       ** @return                <code>true</code> if getting a certificate via
       **                        startTLS handler is believed to be successful,
       **                        <code>false</code> otherwise.
       */
      // see http://javamail.kenai.com/nonav/javadocs/com/sun/mail/smtp/package-summary.html
      @Override
      public boolean run(final String host, final int port) {
        System.out.println ("... trying POP3 with startTLS extension ...");
        final Properties environment = new Properties();
        environment.put("mail.transport.protocol",          "pop3");
        environment.put("mail.smtp.socketFactory.class",    "javax.net.ssl.SSLSocketFactory");
        environment.put("mail.smtp.socketFactory.fallback", "false");
        environment.put("mail.smtp.starttls.enable",        "true");

        Security.setProperty("ssl.SocketFactory.provider", TransportSocketFactory.class.getName());
        final Session session = Session.getDefaultInstance (environment);
        Store store = null;
        try {
          store = session.getStore ("pop3");
        }
        catch (NoSuchProviderException e) {
          e.printStackTrace();
          return false;
        }
        try {
          store.connect(host, port, StringUtility.EMPTY, StringUtility.EMPTY);
        }
        catch (AuthenticationFailedException e) {
          // likely got an unknown certificate, just report it and return
          // success
          System.out.println("ERROR on POP3 authentication: " + e.toString());
          return true;
        }
        catch (MessagingException e) {
          e.printStackTrace();
          return false;
        }
        finally {
          if (store.isConnected()) {
            try {
              store.close();
            }
            catch (MessagingException e) {
              // intentionally left blank
              ;
            }
          }
        }
        return false;
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // class IMAP
    // ~~~~~ ~~~~
   /**
     ** A {@link Handler} implementation for IMAP protocol.
     */
    class IMAP implements Handler {

      //////////////////////////////////////////////////////////////////////////
      // Constructors
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: Ctor
      /**
       ** Constructs an empty <code>IMAP</code> startTLS handler that allows use
       ** as a JavaBean.
       ** <br>
       ** Zero argument constructor required by the framework.
       ** <br>
       ** Default Constructor
      */
      public IMAP() {
        // ensure inheritance
        super();
      }

      //////////////////////////////////////////////////////////////////////////
      // Method of implemented interfaces
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: run (Handler)
      /**
       ** Do the application protocol specific actions to initiate a protocol
       ** specific startTLS session, starting from a new connection.
       **
       ** @param  host           the host to connect to.
       ** @param  port           the port to connect to.
       **
       ** @return                <code>true</code> if getting a certificate via
       **                        startTLS handler is believed to be successful,
       **                        <code>false</code> otherwise.
       */
      // see http://javamail.kenai.com/nonav/javadocs/com/sun/mail/imap/package-summary.html
      @Override
      public boolean run(final String host, final int port) {
        System.out.println ("... trying IMAP with startTLS extension ...");
        Properties environment = new Properties();
        environment.put("mail.transport.protocol",          "imap");
        environment.put("mail.smtp.socketFactory.class",    "javax.net.ssl.SSLSocketFactory");
        environment.put("mail.smtp.socketFactory.fallback", "false");
        environment.put("mail.smtp.starttls.enable",        "true");

        Security.setProperty("ssl.SocketFactory.provider", TransportSocketFactory.class.getName());
        final Session session = Session.getDefaultInstance (environment);
        Store store = null;
        try {
          store = session.getStore ("imap");
        }
        catch (NoSuchProviderException e) {
          e.printStackTrace();
          return false;
        }
        try {
          store.connect(host, port, StringUtility.EMPTY, StringUtility.EMPTY);
        }
        catch (AuthenticationFailedException e) {
          // likely got an unknown certificate, just report it and return
          // success
          System.out.println("ERROR on IMAP authentication: " + e.toString());
          return true;
        }
        catch (MessagingException e) {
          e.printStackTrace();
          return false;
        }
        finally {
          if (store.isConnected()) {
            try {
              store.close();
            }
            catch (MessagingException e) {
              // intentionally left blank
              ;
            }
          }
        }
        return false;
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // class LDAP
    // ~~~~~ ~~~~
    /**
     ** A {@link Handler} implementation for LDAP protocol.
     */
    class LDAP implements Handler {

      //////////////////////////////////////////////////////////////////////////
      // Constructors
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: Ctor
      /**
       ** Constructs an empty <code>LDAP</code> startTLS handler that allows use
       ** as a JavaBean.
       ** <br>
       ** Zero argument constructor required by the framework.
       ** <br>
       ** Default Constructor
       */
      public LDAP() {
        // ensure inheritance
        super();
      }

      //////////////////////////////////////////////////////////////////////////
      // Method of implemented interfaces
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: run (Handler)
      /**
       ** Do the application protocol specific actions to initiate a protocol
       ** specific startTLS session, starting from a new connection.
       **
       ** @param  host           the host to connect to.
       ** @param  port           the port to connect to.
       **
       ** @return                <code>true</code> if getting a certificate via
       **                        startTLS handler is believed to be successful,
       **                        <code>false</code> otherwise.
       */
      // see http://docs.oracle.com/javase/jndi/tutorial/ldap/ext/starttls.html
      // see http://docs.oracle.com/javase/7/docs/technotes/guides/jndi/jndi-ldap.html
      @Override
      public boolean run(final String host, final int port) {
        System.out.println ("... trying LDAP with startTLS extension ...");
        final Hashtable <String, String> environment = new Hashtable <String, String> ();
        environment.put (Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        environment.put (Context.PROVIDER_URL,            "ldap://" + host + ":" + port + "/");
        // enforce a connection time out in ms
        environment.put ("com.sun.jndi.ldap.connect.timeout", "5000");
        // enforce a response time out in ms
        environment.put ("com.sun.jndi.ldap.read.timeout", "5000");
        LdapContext      ctx = null;
        StartTlsResponse tls = null;
        try {
          try {
            // create initial context
            ctx = new InitialLdapContext(environment, null);
            
            // create the startTLS handler object
            tls = (StartTlsResponse)ctx.extendedOperation(new StartTlsRequest());
          }
          catch (NamingException e) {
            e.printStackTrace();
          }

          // start TLS
          try {
            tls.negotiate(TransportSocketFactory.build());
          }
          catch (SSLHandshakeException e) {
            // likely got an unknown certificate, just report it and return
            // success
            System.out.println("ERROR on LDAP authentication: " + e.toString());
            return true;
          }
          catch (IOException e) {
            e.printStackTrace();
          }
        }
        finally {
          // stop TLS
          try {
            tls.close();
          }
          catch (IOException e) {
            e.printStackTrace();
          }

          // close the context
          try {
            ctx.close();
          }
          catch (NamingException e) {
            e.printStackTrace();
          }
        }
        return false;
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods groped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: run
    /**
     ** Do the application protocol specific actions to initiate a protocol
     ** specific startTLS session, starting from a new connection.
     **
     ** @param  host             the host to connect to.
     ** @param  port             the port to connect to.
     **
     ** @return                  <code>true</code> if getting a certificate via
     **                          startTLS handler is believed to be successful,
     **                          <code>false</code> otherwise.
     */
    boolean run(final String host, final int port);
  }

  public TransportStartTLS() {
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods groped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   consider
  /**
   ** Make an effort to guess the right application protocol for startTLS
   ** extension, either by standard port or by interrogating the user; then
   ** obtain the appropriate protocol handler and run it.
   **
   ** @param  host             the host to connect to.
   ** @param  port             the port to connect to.
   **
   ** @return                  <code>true</code> if getting a certificate via
   **                          startTLS handler is believed to be successful,
   **                          <code>false</code> otherwise.
   **
   ** @throws IOException
   */
  public static boolean consider(final String host, final int port)
    throws IOException {

    Protocol protocol = Protocol.from(port);
    if (protocol != null) {
      return negotiate(protocol.name(), host, port);
    }
    else {
      final StringBuilder prompt = new StringBuilder("Try startTLS with one of these protocols?\n");
      for (Protocol p : Protocol.values()) {
        prompt.append(p.name());
        prompt.append(": ");
        prompt.append(p.ordinal() + 1);
        prompt.append("\n");
      }
      prompt.append("none: <Enter> : ");
      while (true) {
        String response = TransportSocketFactory.ask(prompt.toString());
        if (response.length() > 0) {
          int itemNr;
          try {
            itemNr = Integer.parseInt(response);
          }
          catch (NumberFormatException e) {
            System.out.println("... invalid response.");
            continue;
          }
          protocol = Protocol.values()[itemNr - 1];
          if (protocol != null) {
            return negotiate(protocol.name(), host, port);
          }
          else {
            System.out.println("... invalid response.");
            continue;
          }
        }
        else {
          System.out.println("... no more ideas, program terminated.");
          return false;
        }
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   negotiate
  /**
   ** Load a given application specific protocol startTLS handler and run
   ** it.
   **
   ** @param suffix            the protocol handler name suffix, named after
   **                          protocol itself.
   **
   ** @param  host             the host to connect to.
   ** @param  port             the port to connect to.
   **
   ** @return                  <code>true</code> if getting a certificate via
   **                          startTLS handler is believed to be successful,
   **                          <code>false</code> otherwise.
   */
  static boolean negotiate(final String suffix, final String host, final int port) {
    Class<Handler> handlerClass = null;
    try {
      // avoid static linking to Headstart Security library and other
      // protocol-specific libraries
      @SuppressWarnings("unchecked") Class<Handler> unchecked = (Class<Handler>)Class.forName(Handler.class.getName() + "$" + suffix);
      handlerClass = unchecked;
    }
    catch (ClassNotFoundException e) {
      // not really observed, but we should expect may happen...
      e.printStackTrace();
    }
    catch (NoClassDefFoundError e) {
      if (e.getCause().getClass().equals(ClassNotFoundException.class) && e.getCause().getMessage().equals("oracle.hst.security.NoSuchProviderException")) {
        System.out.println("ERROR loading protocol-specific startTLS handler: " + e.toString());
        System.out.println("Looks like you need to make security library available on your classpath, something like this:\n java -cp " + System.getProperty("java.class.path") + System.getProperty("path.separator") + "..." + System.getProperty("file.separator") + "hst-security.jar oracle.jdeveloper.connection.iam.network.TransportStartTLS");
      }
      else {
        // provide more info for analysis...
        e.printStackTrace();
      }
    }
    Handler handler = null;
    try {
      handler = handlerClass.newInstance();
    }
    catch (InstantiationException e) {
      // should not happen...
      e.printStackTrace();
      return false;
    }
    catch (IllegalAccessException e) {
      // should not happen...
      e.printStackTrace();
      return false;
    }
    return handler.run(host, port);
  }
}