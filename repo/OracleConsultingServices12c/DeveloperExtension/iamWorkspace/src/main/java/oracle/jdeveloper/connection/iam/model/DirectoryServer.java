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

    File        :   DirectoryServer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    DirectoryServer.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.model;

////////////////////////////////////////////////////////////////////////////////
// class DirectoryServer
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** The model to support the Connection dialog for creating or modifiying the
 ** connection properties stored in the <code>DirectoryServer</code> model.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class DirectoryServer extends Endpoint {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the schema option each directory server is using */
  public static final String SCHEMA_DISCOVERY                = "schema-discovery";

  /**
   ** the authentication type each directory server is using.
   ** <p>
   ** The value of this property is a string that specifies the authentication
   ** mechanism(s) for the provider to use. The following values are defined for
   ** this property:
   ** <ul>
   **   <li>none - use no authentication (anonymous bind).
   **   <li>simple - use simple authentication (a cleartext password).
   **   <li>
   ** </ul>
   ** If this property is not set then its default value is <code>none</code>,
   ** unless the java.naming.security.credentials property is set, in which case
   ** the default value is <code>simple</code>. If this property is set to a
   ** value that the provider does not recognize or support, it should throw
   ** AuthenticationNotSupportedException.
   */
  public static final String AUTHENTICATION                  = "authentication";

  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify the timeout period for establishment of the LDAP connection.
   ** <p>
   ** This property affects the TCP timeout when opening a connection to an LDAP
   ** server. When connection pooling has been requested, this property also
   ** specifies the maximum wait time or a connection when all connections in
   ** pool are in use and the maximum pool size has been reached.
   ** <p>
   ** If this property has not been specified, the LDAP provider will wait
   ** indefinitely for a pooled connection to become available, and to wait for
   ** the default TCP timeout to take effect when creating a new connection.
   */
  public static final String CONNECTION_TIMEOUT              = "connection-timeout";

  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify the timeout period the LDAP provider doesn't get an LDAP
   ** response.
   ** <p>
   ** If this property has not been specified, the default is to wait for the
   ** response until it is received.
   */
  public static final String RESPONSE_TIMEOUT                = "response-timeout";

  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify the URL encoding.
   */
  public static final String URL_ENCODING                    = "url-encoding";

  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify the Initial Context factory.
   */
  public static final String INITIAL_CONTEXT_FACTORY         = "context-factory";

  /** the schema discovery mode of the Directory Service */
  public static final String SCHEMA_DISCOVERY_STATIC         = "static";

  /** the schema discovery mode of the  Directory Service */
  public static final String SCHEMA_DISCOVERY_SERVER         = "server";

  /** the authentication mode of the Directory Service */
  public static final String AUTHENTICATION_SIMPLE           = "simple";

  /** the authentication mode of the Directory Service */
  public static final String AUTHENTICATION_GSSAPI           = "gssapi";

  /** the protocol each directory server is using */
  public static final String DEFAULT_PROTOCOL                = "ldap";

  /** the protocol each directory server is using over SSL/TLS */
  public static final String DEFAULT_PROTOCOL_SECURE         = "ldaps";

  /** the standard LDAP port  */
  public static final int    DEFAULT_PORT                    = 389;

  /** the default port number for secure LDAP connections. */
  public static final int    DEFAULT_PORT_SECURE             = 636;

  /**
   ** Default value of the timeout period for establishment of the LDAP
   ** connection.
   */
  public static final int    DEFAULT_CONNECTION_TIMEOUT      = 3000;

  /**
   ** Default value of the timeout period the LDAP provider doesn't get an LDAP
   ** response.
   */
  public static final int    DEFAULT_RESPONSE_TIMEOUT        = 10000;

  /** the default value of the URL encoding. */
  public static final String DEFAULT_URL_ENCODING            = "UTF-8";

  /** the default value of the initial context factory. */
  public static final String DEFAULT_INITIAL_CONTEXT_FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DirectoryServer</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public DirectoryServer() {
    // ensure inheritance
    super(DirectoryServer.class, DirectoryServerFactory.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverPort (overridden)
  /**
   ** Returns the listener port of the server the target system is deployed on
   ** and this <code>DirectoryServer</code> is configured for.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #SERVER_PORT}.
   ** <p>
   ** Not all <code>DirectoryServer</code>s that are managed by this code
   ** following the agreed naming conventions hence it may be necessary to
   ** overload this method so we cannot make it final.
   **
   ** @return                    the listener port of the server the target
   **                            system is deployed on and this
   **                            <code>DirectoryServer</code> is configured for.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  @Override
  public int serverPort() {
    // the second parameter to string() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return integerValue(SERVER_PORT, DEFAULT_PORT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectionTimeout
  /**
   ** Returns the timeout period for establishment of the Directory Server
   ** connection.
   ** <p>
   ** This property affects the TCP timeout when opening a connection to an
   ** Directory Serverr. When connection pooling has been requested, this
   ** property also specifies the maximum wait time or a connection when all
   ** connections in pool are in use and the maximum pool size has been reached.
   ** <p>
   ** If this property has not been specified, the Directory provider will wait
   ** indefinitely for a pooled connection to become available, and to wait for
   ** the default TCP timeout to take effect when creating a new connection.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #CONNECTION_TIMEOUT}.
   ** <p>
   ** If {@link #CONNECTION_TIMEOUT} is not mapped in the underlying
   ** <code>Metadata Descriptor</code> this method returns
   ** {@link #DEFAULT_CONNECTION_TIMEOUT}
   **
   ** @return                    the timeout period for establishment of the
   **                            LDAP connection.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public final int connectionTimeout() {
    return integerValue(CONNECTION_TIMEOUT, DEFAULT_CONNECTION_TIMEOUT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   responseTimeout
  /**
   ** Returns the timeout period the Directory provider doesn't get an LDAP
   ** response.
   ** <p>
   ** When an LDAP request is made by a client to a server and the server does
   ** not respond for some reason, the client waits forever for the server to
   ** respond until the TCP timeouts. On the client-side what the user
   ** experiences is esentially a process hang. In order to control the LDAP
   ** request in a timely manner, a read timeout can be configured for the
   ** JNDI/LDAP Service Provider since Java SE 6.
   ** <p>
   ** If this property is not specified, the default is to wait for the
   ** response until it is received.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #RESPONSE_TIMEOUT}.
   ** <p>
   ** If {@link #RESPONSE_TIMEOUT} is not mapped in the underlying
   ** <code>Metadata Descriptor</code> this method returns
   ** {@link #DEFAULT_RESPONSE_TIMEOUT}
   **
   ** @return                    the timeout period for establishment of the
   **                            LDAP connection.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public final int responseTimeout() {
    return integerValue(RESPONSE_TIMEOUT, DEFAULT_RESPONSE_TIMEOUT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   schemaDiscovery
  /**
   ** Sets the type of schema discovery mode this <code>DirectoryServer</code>
   ** is configured for.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #SCHEMA_DISCOVERY}.
   **
   ** @param  type               the type of schema discovery mode this
   **                            <code>DirectoryServer</code> is configured for.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void schemaDiscovery(final String type) {
    property(SCHEMA_DISCOVERY, type);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   schemaDiscovery
  /**
   ** Returns the type of schema discovery mode this
   ** <code>DirectoryServer</code> is configured for.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #SCHEMA_DISCOVERY}.
   ** <p>
   ** If {@link #SCHEMA_DISCOVERY} is not mapped in the underlying
   ** <code>DirectoryServer</code> this method returns
   ** {@link #SCHEMA_DISCOVERY_STATIC}.
   **
   ** @return                    the type of schema discovery mode this
   **                            <code>DirectoryServer</code> is configured for.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String schemaDiscovery() {
    return stringValue(SCHEMA_DISCOVERY, SCHEMA_DISCOVERY_STATIC);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authentication
  /**
   ** Sets the type of authentication this <code>DirectoryServer</code> is
   ** configured for.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #AUTHENTICATION}.
   **
   ** @param  authencication     the type of authentication this
   **                            <code>DirectoryServer</code> is configured for.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void authentication(final String authencication) {
    property(AUTHENTICATION, authencication);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authentication
  /**
   ** Returns the type of authentication this <code>DirectoryServer</code> is
   ** configured for.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #AUTHENTICATION}.
   ** <p>
   ** If {@link #AUTHENTICATION} is not mapped in the underlying
   ** <code>Metadata Descriptor</code> this method returns
   ** {@link #AUTHENTICATION_SIMPLE}.
   **
   ** @return                    the type of authentication this
   **                            <code>DirectoryServer</code> is configured for.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String authentication() {
    return stringValue(AUTHENTICATION, AUTHENTICATION_SIMPLE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   urlEncoding
  /**
   ** Returns the URL encoding this <code>DirectoryServer</code> is configured
   ** for.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #URL_ENCODING}.
   ** <p>
   ** If {@link #URL_ENCODING} is not mapped in the underlying
   ** <code>DirectoryServer</code> this method returns
   ** {@link #DEFAULT_URL_ENCODING}.
   **
   ** @return                    the URL encoding.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public final String urlEncoding() {
    return stringValue(URL_ENCODING, DEFAULT_URL_ENCODING);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialContextFactory
  /**
   ** Returns the class name of the initial context factory this
   ** <code>DirectoryServer</code> is configured for.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #INITIAL_CONTEXT_FACTORY}.
   ** <p>
   ** If {@link #INITIAL_CONTEXT_FACTORY} is not mapped in the underlying
   ** <code>DirectoryServer</code> this method returns
   ** {@link #DEFAULT_INITIAL_CONTEXT_FACTORY}.
   **
   ** @return                    the class name of the initial context factory.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String initialContextFactory() {
    return stringValue(INITIAL_CONTEXT_FACTORY, DEFAULT_INITIAL_CONTEXT_FACTORY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serviceURL
  /**
   ** Returns the Directory Server URL this <code>DirectoryServer</code> is
   ** configured for.
   **
   ** @return                    the Directory Server URL this
   **                            <code>DirectoryServer</code> is configured for.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String serviceURL() {
    return String.format("%s://%s:%d", (serverSocketSSL() || serverSocketTLS()) ? DEFAULT_PROTOCOL_SECURE : DEFAULT_PROTOCOL, serverName(), serverPort());
  }
}