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

    System      :   JDeveloper Identity and Access Extensions
    Subsystem   :   Identity and Access Management Facilities

    File        :   DirectoryService.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    DirectoryService.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.service;

import java.util.Set;
import java.util.List;
import java.util.EnumSet;
import java.util.ArrayList;
import java.util.Properties;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.net.URLEncoder;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.NamingEnumeration;
import javax.naming.InvalidNameException;
import javax.naming.NameNotFoundException;
import javax.naming.CommunicationException;
import javax.naming.AuthenticationException;
import javax.naming.NameAlreadyBoundException;
import javax.naming.ServiceUnavailableException;
import javax.naming.OperationNotSupportedException;

import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchResult;
import javax.naming.directory.SearchControls;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SchemaViolationException;

import javax.naming.ldap.Control;
import javax.naming.ldap.LdapContext;
import javax.naming.ldap.StartTlsRequest;
import javax.naming.ldap.StartTlsResponse;
import javax.naming.ldap.InitialLdapContext;

import javax.net.ssl.SSLHandshakeException;

import oracle.jdeveloper.workspace.iam.utility.StringUtility;
import oracle.jdeveloper.workspace.iam.utility.CollectionUtility;

import oracle.jdeveloper.connection.iam.Bundle;

import oracle.jdeveloper.connection.iam.model.DirectoryName;
import oracle.jdeveloper.connection.iam.model.DirectoryEntry;
import oracle.jdeveloper.connection.iam.model.DirectoryValue;
import oracle.jdeveloper.connection.iam.model.DirectoryServer;
import oracle.jdeveloper.connection.iam.model.DirectorySchema;
import oracle.jdeveloper.connection.iam.model.DirectoryAttribute;

import oracle.jdeveloper.connection.iam.support.LDAPFile;
import oracle.jdeveloper.connection.iam.support.LDAPReader;
import oracle.jdeveloper.connection.iam.support.LDAPWriter;
import oracle.jdeveloper.connection.iam.support.LDAPRecord;
import oracle.jdeveloper.connection.iam.support.LDAPSearch;
import oracle.jdeveloper.connection.iam.support.LDAPPageSearch;
import oracle.jdeveloper.connection.iam.support.LDAPAddContent;
import oracle.jdeveloper.connection.iam.support.LDAPModDNContent;
import oracle.jdeveloper.connection.iam.support.LDAPModifyContent;
import oracle.jdeveloper.connection.iam.support.LDAPDeleteContent;

import oracle.jdeveloper.connection.iam.network.TransportSecurity;
import oracle.jdeveloper.connection.iam.network.TransportSocketFactory;
import oracle.jdeveloper.connection.iam.network.TransportSecurityException;

////////////////////////////////////////////////////////////////////////////////
// class DirectoryService
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** This class provides a facility for interacting with an LDAPv3 Directory
 ** Server. It provides a means of establishing a connection to the server,
 ** sending requests, and reading responses.
 ** See <a href="http://www.ietf.org/rfc/rfc4511.txt">RFC 4511</a> for the
 ** LDAPv3 protocol specification and more information about the types of
 ** operations defined in LDAP.
 ** <h2>Creating, Establishing, and Authenticating Connections</h2>
 ** An LDAP connection can be established either at the time that the object is
 ** created or as a separate step. Similarly, authentication can be performed on
 ** the connection at the time it is created, at the time it is established, or
 ** as a separate process. For example:
 ** <pre>
 **   // Create a new connection that is established at creation time, and then
 **   // authenticate separately using simple authentication.
 **   DirectoryService service = new DirectoryService(resource);
 **   service.connect(contextURL);
 **   BindResult bindResult = service.bind(principalUsername, principalPassword);
 ** </pre>
 ** When authentication is performed at the time that the connection is
 ** established, it is only possible to perform a simple bind and it is not
 ** possible to include controls in the bind request, nor is it possible to
 ** receive response controls if the bind was successful. Therefore, it is
 ** recommended that authentication be performed as a separate step if the
 ** server may return response controls even in the event of a successful
 ** authentication (e.g., a control that may indicate that the user's password
 ** will soon expire).
 ** <p>
 ** By default, connections will use standard unencrypted network sockets.
 ** However, it may be desirable to create connections that use SSL/TLS to
 ** encrypt communication. This can be done by specifying a
 ** <code>SocketFactory</code> that should be used to create the socket to use
 ** to communicate with the directory server. The
 ** <code>SSLSocketFactory.getDefault</code> method or the
 ** <code>SSLContext.getSocketFactory</code> method may be used to obtain a
 ** socket factory for performing secure communication. See the
 ** <a href="http://java.sun.com/j2se/1.5.0/docs/guide/security/jsse/JSSERefGuide.html">
 ** JSSE Reference Guide</a> for more information on using these classes.
 ** <p>
 ** Whenever the connection is no longer needed, it may be terminated using the
 ** {@link DirectoryService#disconnect} method.
 ** <h2>Processing LDAP Operations</h2>
 ** This class provides a number of methods for processing the different types
 ** of operations. The types of operations that can be processed include:
 ** <ul>
 **   <li>Abandon   -- This may be used to request that the server stop
 **                    processing on an operation that has been invoked
 **                    asynchronously.
 **   <li>Bind      -- This may be used to authenticate to the directory server.
 **   <li>Search    -- This may be used to retrieve a set of entries in the
 **                    server that match a given set of criteria.
 **   <li>Add       -- This may be used to add a new entry to the directory
 **                    server.
 **   <li>Delete    -- This may be used to remove an entry from the directory
 **                    server.
 **   <li>Modify    -- This may be used to alter an entry in the directory
 **                    server.
 **                    information about processing modify operations.
 **   <li>Modify DN -- This may be used to rename an entry or subtree and/or
 **                    move that entry or subtree below a new parent in the
 **                    directory server.
 **   <li>Compare   -- This may be used to determine whether a specified entry
 **                    has a given attribute value.
 **   <li>Extended  -- This may be used to process an operation which is not
 **                    part of the core LDAP protocol but is a custom extension
 **                    supported by the directory server.
 ** </ul>
 ** Most of the methods in this class used to process operations operate in a
 ** synchronous manner. In these cases, the SDK will send a request to the
 ** server and wait for a response to arrive before returning to the caller. In
 ** these cases, the value returned will include the contents of that response,
 ** including the result code, diagnostic message, matched DN, referral URLs,
 ** and any controls that may have been included. However, it also possible to
 ** process operations asynchronously, in which case the SDK will return control
 ** back to the caller after the request has been sent to the server but before
 ** the response has been received.
 ** <p>
 ** This class is mostly threadsafe. It is possible to process multiple
 ** concurrent operations over the same connection as long as the methods being
 ** invoked will not change the state of the connection in a way that might
 ** impact other operations in progress in unexpected ways. In particular, the
 ** following should not be attempted while any other operations may be in
 ** progress on this connection:
 ** <ul>
 **   <li>Using one of the {@code connect} methods to re-establish the
 **       connection.
 **   <li>Using one of the {@code close} methods to terminate the connection.
 **   <li>Using one of the {@code bind} methods to attempt to authenticate the
 **       connection (unless you are certain that the bind will not impact the
 **       identity of the associated connection, for example by including the
 **       retain identity request control in the bind request if using the
 **       LDAP SDK in conjunction with a Ping Identity, UnboundID, or
 **       Nokia/Alcatel-Lucent 8661 Directory Server).
 **   <li>Attempting to make a change to the way that the underlying
 **       communication is processed (e.g., by using the StartTLS extended
 **       operation to convert an insecure connection into a secure one).
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class DirectoryService extends EndpointService {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Default value of the distinguished name attibute of an entry in a V3
   ** compliant LDAP server.
   */
  public static final String  DN                       = "dn";
  /**
   ** Default value of the name of the generic object class of an entry in a V3
   ** compliant LDAP server.
   */
  public static final String  OBJECTCLASS              = "objectClass";
  /**
   ** Default value to access the type of the entry in the changelog.
   */
  public static final String  CHANGE_TYPE              = "changeType";
  /**
   ** The name to specify that the type of a change in the directory is an add
   */
  public static final String  CHANGE_TYPE_ADD          = "add";
  /**
   ** The name to specify that the type of a change in the directory is a
   ** modification
   */
  public static final String  CHANGE_TYPE_MODIFY       = "modify";
  /**
   ** The name to specify that the type of a change in the directory is a
   ** delete
   */
  public static final String  CHANGE_TYPE_DELETE       = "delete";

  public static final String  CHANGE_TYPE_RENAME_DN    = "moddn";
  public static final String  CHANGE_TYPE_RENAME_RDN   = "modrdn";
  public static final String  CHANGE_OPERATION_ADD     = "add";
  public static final String  CHANGE_OPERATION_REPLACE = "replace";
  public static final String  CHANGE_OPERATION_REMOVE  = "remove";

  public static final String  RDNNEW                   = "newrdn";
  public static final String  RDNOLD_DELETE            = "deleteoldrdn";
  public static final String  PARENTNEW                = "newparent";
  public static final String  SUPERIORNEW              = "newsuperior";

  public static final String  CONTROL                  = "control";
  public static final String  VERSION                  = "version";

  static final String         OBJECT_SCOPE_FILTER      = "(objectClass=*)";
  static final SearchControls OBJECT_SCOPE_ALL         = new SearchControls(SearchControls.OBJECT_SCOPE, 0, 0, new String[]{"*", "+"}, false, false);
  static final SearchControls OBJECT_SCOPE_NULL        = null;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private LdapContext         context                  = null;
  private StartTlsResponse    startTls                 = null;
  private DirectorySchema     schema                   = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DirectoryService</code> which is associated with the
   ** specified task.
   **
   ** @param  endpoint           the {@link DirectoryServer} definition where
   **                            this connector is associated with.
   **                            <br>
   **                            Allowed object is {@link DirectoryServer}.
   */
  private DirectoryService(final DirectoryServer endpoint) {
    // ensure inheritance
    super(endpoint);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessors methods
  //////////////////////////////////////////////////////////////////////////////

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
   ** {@link DirectoryServer#CONNECTION_TIMEOUT}.
   ** <p>
   ** If {@link DirectoryServer#CONNECTION_TIMEOUT} is not mapped in the
   ** underlying <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryServer#DEFAULT_CONNECTION_TIMEOUT}
   **
   ** @return                    the timeout period for establishment of the
   **                            LDAP connection.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public final int connectionTimeout() {
    return ((DirectoryServer)this.resource).connectionTimeout();
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
   ** {@link DirectoryServer#RESPONSE_TIMEOUT}.
   ** <p>
   ** If {@link DirectoryServer#RESPONSE_TIMEOUT} is not mapped in the
   ** underlying <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryServer#DEFAULT_RESPONSE_TIMEOUT}
   **
   ** @return                    the timeout period for establishment of the
   **                            LDAP connection.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public final int responseTimeout() {
    return ((DirectoryServer)this.resource).responseTimeout();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   schemaDiscovery
  /**
   ** Returns the type of schema discovery mode this <code>IT Resource</code> is
   ** configured for.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryServer#SCHEMA_DISCOVERY}.
   ** <p>
   ** If {@link DirectoryServer#SCHEMA_DISCOVERY} is not mapped in the underlying
   ** <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryServer#SCHEMA_DISCOVERY_STATIC}.
   **
   ** @return                    the type of schema discovery mode this
   **                            <code>IT Resource</code> is configured for.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String schemaDiscovery() {
    return DirectoryServer.SCHEMA_DISCOVERY_SERVER;
//    return ((DirectoryServer)this.resource).schemaDiscovery();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authentication
  /**
   ** Returns the type of authentication this <code>IT Resource</code> is
   ** configured for.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryServer#AUTHENTICATION}.
   ** <p>
   ** If {@link DirectoryServer#AUTHENTICATION} is not mapped in the underlying
   ** <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryServer#AUTHENTICATION_SIMPLE}.
   **
   ** @return                    the type of authentication this
   **                            <code>IT Resource</code> is configured for.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String authentication() {
    return ((DirectoryServer)this.resource).authentication();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   urlEncoding
  /**
   ** Returns the URL encoding this <code>IT Resource</code> is configured for.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryServer#URL_ENCODING}.
   ** <p>
   ** If {@link DirectoryServer#URL_ENCODING} is not mapped in the underlying
   ** <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryServer#DEFAULT_URL_ENCODING}.
   **
   ** @return                    the URL encoding.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String urlEncoding() {
    return ((DirectoryServer)this.resource).urlEncoding();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialContextFactory
  /**
   ** Returns the class name of the initial context factory this
   ** <code>IT Resource</code> is configured for.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryServer#INITIAL_CONTEXT_FACTORY}.
   ** <p>
   ** If {@link DirectoryServer#INITIAL_CONTEXT_FACTORY} is not mapped in the
   ** underlying <code>Metadata Descriptor</code> this method returns
   ** {@link DirectoryServer#DEFAULT_INITIAL_CONTEXT_FACTORY}.
   **
   ** @return                    the class name of the initial context factory.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String initialContextFactory() {
    return ((DirectoryServer)this.resource).initialContextFactory();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trustStorePath
  /**
   ** Returns the path to the key store used to verify trusted communication
   ** with an endpoint.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DirectoryServer#SERVER_TRUST_KEYSTORE}.
   **
   ** @return                    the path to the key store used to verify
   **                            trusted communication with an endpoint.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String trustStorePath() {
    return this.resource.trustStorePath();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trustStorePassword
  /**
   ** Returns the password required to unlock the trusted key store.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link DirectoryServer#SERVER_TRUST_PASSWORD}.
   **
   ** @return                    the password required to unlock the trusted key
   **                            store.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String trustStorePassword() {
    return this.resource.trustStorePassword();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Convenience method to create a <code>DirectoryService</code> which is
   ** associated with the specified <code>DirectoryServer</code> endpoint.
   **
   ** @param  endpoint           the {@link DirectoryServer} definition where
   **                            this connector is associated with.
   **                            <br>
   **                            Allowed object is {@link DirectoryServer}.
   **
   ** @return                    the <code>DirectoryService</code>.
   **                            <br>
   **                            Possible object is {@link DirectoryService}.
   */
  public static DirectoryService build(final DirectoryServer endpoint) {
    return new DirectoryService(endpoint);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contextURL
  /**
   ** Return the fullqualified URL to the Directory Information Tree.
   ** <p>
   ** The URL consists of the server part of the ldap url, ldap://host:port and
   ** the absolute path to the entry. The entry is post fixed with the context
   ** root of the connection.
   **
   ** @param  distinguishedName  a component of a Directory Information Tree
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the full qualified LDAP URL.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws DirectoryException if the given distinguished name cannot
   **                            converted to a
   **                            <code>application/x-www-form-urlencoded</code>
   **                            MIME format.
   */
  public final String contextURL(final String distinguishedName)
    throws DirectoryException {

    // encode the distinguished name that we want to access with the restriction
    // that the URLEncoder implements the HTML Specifications for how to encode
    // URLs in HTML forms. This fails for LDAP URL's so we have to replace all
    // plus signs back to a space manually after the encoder return the result
    String encodedPath = null;
    try {
      encodedPath = URLEncoder.encode(distinguishedName, urlEncoding());
      encodedPath = encodedPath.replace("+", "%20");
    }
    catch (UnsupportedEncodingException e) {
      throw new DirectoryException(Bundle.format(Bundle.CONTEXT_ERROR_ENCODING, urlEncoding()));
    }

    final StringBuilder url = new StringBuilder(((DirectoryServer)this.resource).serviceURL());
    url.append('/').append(encodedPath);
    // return the resulting url by escaping all space characters that may
    // contained by the appropriate encoded character
    return url.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   schema
  /**
   ** Return the schema upon request.
   **
   ** @return                    the schema.
   **                            <br>
   **                            Possible object is {@link DirectorySchema}.
   **
   ** @throws DirectoryException if the discovery of the schema fails.
   */
  public DirectorySchema schema()
    throws DirectoryException {

    if (this.schema == null)
      this.schema = DirectoryServer.SCHEMA_DISCOVERY_SERVER.equals(schemaDiscovery()) ? new DirectorySchema.Server(this) : new DirectorySchema.Static(this);

    return this.schema;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connect
  /**
   ** Constructs an initial {@link LdapContext} by creating the appropriate
   ** environment from the attributes of the associated IT Resource.
   ** <p>
   ** In LDAP 3.0, rootDSE is defined as the root of the directory data tree on
   ** a directory server. The rootDSE is not part of any namespace. The purpose
   ** of the rootDSE is to provide data about the directory server.
   **
   ** @throws DirectoryException if the {@link InitialLdapContext} could not be
   **                            created at the first time this method is
   **                            invoked.
   */
  @Override
  public final void connect()
    throws DirectoryException {

    // To search for root DSE,
    // 1. Set LDAP version to LDAP_V3 before binding
    // 2. Set the search base to an empty string
    // 3. Set the search filter to (objectclass=*)
    // 4. Set the search scope to LDAP_SCOPE_BASE
    this.context = connect(environment(((DirectoryServer)this.resource).serviceURL()));
    established(this.context != null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connect
  /**
   ** Constructs an initial {@link LdapContext} by creating the appropriate
   ** environment from the attributes of the associated IT Resource.
   **
   ** @param  contextPath        the context to bind to.
   **                            Usually for user operations, user container
   **                            context is passed and for group operations,
   **                            group container context is passed.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the context this connector use to communicate
   **                            with the LDAP server.
   **                            <br>
   **                            Possible object is {@link LdapContext}.
   **
   ** @throws DirectoryException if the {@link InitialLdapContext} could not be
   **                            created at the first time this method is
   **                            invoked.
   */
  public LdapContext connect(final String contextPath)
    throws DirectoryException {

    if (this.context == null) {
      // Constructs an LDAP context object using environment properties and
      // connection request controls.
      // See javax.naming.InitialContext for a discussion of environment
      // properties.
      this.context = connect(environment(contextURL(contextPath)));
    }
    return this.context;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   environment
  /**
   ** Creates the {@link Properties} from the attributes of the associated
   ** IT Resource that afterwards can be passed to a {@link LdapContext} to
   ** establish a connection to the target system.
   **
   ** @param  contextPath        the context to bind to.
   **                            Usually for user operations, user container
   **                            context is passed and for group operations,
   **                            group container context is passed.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the environment context this connector use to
   **                            communicate with the LDAP server.
   **                            <br>
   **                            Possible object is {@link Properties}.
   */
  public final Properties environment(final String contextPath) {
    final Properties environment = new Properties();
    // Set up general environment for creating initial context
    environment.put(Context.INITIAL_CONTEXT_FACTORY,      initialContextFactory());
    environment.put(Context.PROVIDER_URL,                 contextPath);
    environment.put("java.naming.ldap.version",           "3");
    environment.put("com.sun.jndi.ldap.connect.timeout",  String.valueOf(connectionTimeout()));
    environment.put("com.sun.jndi.ldap.read.timeout",     String.valueOf(responseTimeout()));
    environment.put("java.naming.ldap.derefAliases",      "finding");
    environment.put("java.naming.referral",               "ignore");
    
    if (serverSocketSSL() || serverSocketTLS()) {
      environment.put("java.naming.security.protocol",   "ssl");
      environment.put("java.naming.ldap.factory.socket", TransportSocketFactory.class.getName());
    }

    return environment;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connect
  /**
   ** Constructs an initial {@link LdapContext} by creating the appropriate
   ** environment from the instance attributes.
   **
   ** @param  environment        environment used to create the initial
   **                            {@link InitialLdapContext}.
   **                            <code>null</code> indicates an empty
   **                            environment.
   **                            <br>
   **                            Allowed object is {@link Properties}.
   **
   ** @return                    the {@link LdapContext} this task use to
   **                            communicate with the LDAP server.
   **                            <br>
   **                            Possible object is {@link LdapContext}.
   **
   ** @throws DirectoryException if the {@link InitialLdapContext} could not be
   **                            created at the first time this method is
   **                            invoked.
   */
  public final LdapContext connect(final Properties environment)
    throws DirectoryException {

    if (serverSocketSSL() || serverSocketTLS()) {
      try {
        TransportSecurity.validateCertificate(serverName(), serverPort(), connectionTimeout());
      }
      catch (TransportSecurityException e) {
        throw new DirectoryException(Bundle.format(Bundle.CONTEXT_ERROR_ABORT, e.getLocalizedMessage()), e);
      }
    }

    LdapContext context = null;
    try {
      // Constructs an LDAP context object using environment properties but
      // none connection request controls.
      // See javax.naming.ldap.InitialLdapContext for a discussion of environment
      // properties.
      try {
//        context = (LdapContext)LocalInitialContextFactoryBuilder.createInitialContext(environment, this.getClass().getClassLoader());
        context = new InitialLdapContext(environment, null);
        established(true);
      }
      // when the host, port or something else is wrong in the server properties
      catch (CommunicationException e) {
        throw new DirectoryException(e);
      }
      // if anything else went wrong
      catch (NamingException e) {
        // delegate to outer try/catch block
        throw new DirectoryException(e);
      }
      // Set up TLS environment for creating initial context
      if (serverStartTLS()) {
        this.startTls = (StartTlsResponse)context.extendedOperation(new StartTlsRequest());
        try {
          this.startTls.negotiate(TransportSocketFactory.build());
        }
        catch (SSLHandshakeException e) {
          // likely got an unknown certificate, just report it and return
          // success
          System.out.println ("ERROR on LDAP authentication: " + e.toString ());
        }
        catch (IOException e) {
          e.printStackTrace();
        }
/*        
        finally {
          try {
            tls.close();
          }
          catch (IOException e) {
            e.printStackTrace();
          }
        }
*/
      }

      // Set up authentication  environment for creating initial context
      context.addToEnvironment(Context.SECURITY_AUTHENTICATION, authentication());
      context.addToEnvironment(Context.SECURITY_PRINCIPAL,      principalName());
      context.addToEnvironment(Context.SECURITY_CREDENTIALS,    principalPassword());
      // enforce authentication
      context.reconnect(null);
    }
    // when the host, port or something else is wrong in the server properties
    catch (CommunicationException e) {
      throw new DirectoryException(e);
    }
    // when the principal or password is wrong in the server properties
    catch (AuthenticationException e) {
      throw new DirectoryException(e, principalName());
    }
    // when a problem may be with physical connectivity or Target System is not alive
    catch (ServiceUnavailableException e) {
      throw new DirectoryException(e);
    }
    // when the context path to connecto to is invalid
    catch (InvalidNameException e) {
      throw new DirectoryException(Bundle.format(Bundle.CONTEXT_ERROR_GENERAL, e.getLocalizedMessage()), e);
    }
    // when the operation fails in general
    catch (NamingException e) {
      throw new DirectoryException(e);
    }
    return context;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disconnect
  /**
   ** Closes the managed directory context.
   ** <br>
   ** This method releases the context's resources immediately, instead of
   ** waiting for them to be released automatically by the garbage collector.
   ** <p>
   ** This method is idempotent:  invoking it on a context that has already been
   ** closed has no effect. Invoking any other method on a closed context is not
   ** allowed, and results in undefined behaviour.
   **
   ** @throws DirectoryException if the connection could not be established at
   **                            the first time this method is invoked.
   */
  public void disconnect()
    throws DirectoryException {

    this.disconnect(this.context);
    try {
      if (this.startTls != null)
        this.startTls.close();
    }
    catch (IOException e) {
      throw new DirectoryException(Bundle.string(Bundle.CONTEXT_ERROR_UNHANDLED), e);
    }
    this.context = null;
    established(false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disconnect
  /**
   ** Closes an unmanaged directory context.
   ** <br>
   ** This method releases the context's resources immediately, instead of
   ** waiting for them to be released automatically by the garbage collector.
   ** <p>
   ** This method is idempotent:  invoking it on a context that has already been
   ** closed has no effect. Invoking any other method on a closed context is not
   ** allowed, and results in undefined behaviour.
   **
   ** @param  context            the {@link LdapContext} to close.
   **                            <br>
   **                            Allowed object is {@link LdapContext}.
   **
   ** @throws DirectoryException if the connection could not be closed.
   */
  public void disconnect(final LdapContext context)
    throws DirectoryException {

    try {
      if (context != null) {
        context.close();
      }
    }
    catch (NamingException e) {
      throw new DirectoryException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   search
  /**
   ** Searches in the named context or object for entries in the sub-tree.
   ** <br>
   ** The {@link NamingEnumeration} that results from the search against the
   ** directory context contains elements of objects from the subtree (including
   ** the named context) that satisfy the search filter specified in search().
   ** <br>
   ** The names of elements in the NamingEnumeration are either relative to the
   ** named context or is a URL string.
   ** <br>
   ** If the named context satisfies the search filter, it is included in the
   ** enumeration with the empty string as
   **
   ** @param  context            the naming context or object to search.
   **                            <br>
   **                            Allowed object is {@link DirectoryName}.
   **
   ** @return                    the {@link List} containing the names of the
   **                            LDAP objects find for the specified filter.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link SearchResult}.
   **
   ** @throws DirectoryException in case the search operation cannot be
   **                            performed.
   */
  public List<SearchResult> search(final DirectoryName context)
    throws DirectoryException {
    
    // lazy initialize context
    if (this.context == null)
      connect();

    final List<SearchResult> subcontext = new ArrayList<SearchResult>();
    try {
      // The NamingEnumeration that results from context.search() using
      // OBJECT_SCOPE contains elements of objects from the object (including
      // the named context) that satisfy the search filter specified in
      // context.search().
      // The names of elements in the NamingEnumeration are either relative to
      // the named context or is a URL string.
      // If the named context satisfies the search filter, it is included in the
      // enumeration with the empty string as its name.
      final NamingEnumeration<SearchResult> result = this.context.search(context, OBJECT_SCOPE_FILTER, OBJECT_SCOPE_NULL);
      while (result.hasMore()) {
        subcontext.add(result.next());
      }
    }
    catch (NamingException e) {
       throw new DirectoryException(e);
    }
    return subcontext;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributes
  /**
   ** Returns the attributes for the current object scope.
   **
   ** @return                    the collection of {@link DirectoryAttribute}s
   **                            of an directory entry and their correspondening
   **                            values.
   **                            <br>
   **                            Possible object is {@link DirectoryEntry}.
   **
   ** @throws DirectoryException in case the search operation cannot be
   **                            performed.
   */
  public DirectoryEntry attributes()
    throws DirectoryException {

    DirectoryEntry subject = null;
    try {
      final DirectorySchema                 schema      = schema();
      final Set<String>                     required    = CollectionUtility.caseInsensitiveSet();
      // The NamingEnumeration that results from context.search() using
      // OBJECT_SCOPE contains elements of objects from the object (including
      // the named context) that satisfy the search filter specified in
      // context.search().
      // The names of elements in the NamingEnumeration are either relative to
      // the named context or is a URL string.
      // If the named context satisfies the search filter, it is included in the
      // enumeration with the empty string as its name.
      final NamingEnumeration<SearchResult> result      = this.context.search(StringUtility.EMPTY, OBJECT_SCOPE_FILTER, OBJECT_SCOPE_ALL);
      final SearchResult                    entry       = result.next();
      subject = DirectoryEntry.build(entry.getNameInNamespace());

      final Attribute      clazz  = entry.getAttributes().remove(OBJECTCLASS);
      DirectoryAttribute   type   = DirectoryAttribute.OBJECTCLASS;
      DirectoryValue       value  = DirectoryValue.build(type);
      NamingEnumeration<?> cursor = clazz.getAll();
      while (cursor.hasMore()) {
        final String clazzName = (String)cursor.nextElement();
        value.add(DirectoryValue.item(clazzName, DirectoryAttribute.OBJECTCLASS.flag));
        // obtain the object class definition from the schema
        final DirectorySchema.ObjectClass temp = schema.objectClass(clazzName);
        // may be if its a custom schema the object class might not be in the
        // schema 
        if (temp != null) {
          required.addAll(temp.required);
        }
      }
      subject.add(value);

      final NamingEnumeration<? extends Attribute> entryValue = entry.getAttributes().getAll();
      while (entryValue.hasMore()) {
        final Attribute attribute = entryValue.nextElement();
        final String    name      = attribute.getID();
        if (DirectorySchema.omit(name))
          continue;

        type   = DirectoryAttribute.build(schema.attributeType().get(name), required.contains(name));
        value  = DirectoryValue.build(type);
        cursor = attribute.getAll();
        while (cursor.hasMore()) {
          value.add(DirectoryValue.item(cursor.nextElement(), type.flag));
        }
        subject.add(value);
      }
    }
    catch (NamingException e) {
      throw new DirectoryException(e);
    }
    return subject;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryCreate
  /**
   ** Creates object and binds to the passed distinguished name  and the
   ** attributes.
   ** <br>
   ** Calls the context.createSubcontext() to create object and bind it with the
   ** passed distinguished name and the attributes.
   **
   ** @param  entry              the populated entry to create
   **                            <br>
   **                            Allowed object is {@link DirectoryEntry}.
   **
   ** @throws DirectoryException if the operation fails
   */
  public void entryCreate(final DirectoryEntry entry)
    throws DirectoryException {

    final Attributes x = new BasicAttributes();
    for (DirectoryValue v : entry.value().values()) {
      final Attribute a = new BasicAttribute(v.type.name);
      for (DirectoryValue.Item i : v) {
        a.add(i.value());
      }
      x.put(a);
    }
    entryCreate(entry.name(), x);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryCreate
  /**
   ** Creates object and binds to the passed distinguished name  and the
   ** attributes.
   ** <br>
   ** Calls the context.createSubcontext() to create object and bind it with the
   ** passed distinguished name and the attributes.
   **
   ** @param  objectRDN          the relative distinguished name of the object
   **                            to create, e.g. <code>cn=george</code>.
   **                            <br>
   **                            Allowed object is {@link DirectoryName}.
   ** @param  attributes         the attributes of the object.
   **                            <br>
   **                            Allowed object is {@link Attributes}.
   **
   ** @throws DirectoryException if the operation fails
   */
  public void entryCreate(final DirectoryName objectRDN, final Attributes attributes)
    throws DirectoryException {

    // lazy initialize context
    if (this.context == null)
      connect();

    try {
      // create object
      this.context.createSubcontext(objectRDN, attributes);
    }
    catch (OperationNotSupportedException e) {
      throw new DirectoryException(Bundle.format(Bundle.CONTEXT_OPERATION_NOT_SUPPORTED, objectRDN), e);
    }
    catch (NamingException e) {
      throw new DirectoryException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryRename
  /**
   ** Renames object of the passed distinguished name.
   ** <br>
   ** Calls the context.rename() to rename object
   **
   ** @param  origin             the distinguished name of the object, e.g.
   **                            <code>'cn=george,ou=people,dc=company,dc=com'</code>
   **                            <br>
   **                            Allowed object is {@link DirectoryName}.
   ** @param  target             the distinguished name of the target object,
   **                            e.g. <code>'cn=lucas,ou=people,dc=company,dc=com'</code>
   **                            <br>
   **                            Allowed object is {@link DirectoryName}.
   **
   ** @throws DirectoryException if the operation fails
   */
  public void entryRename(final DirectoryName origin, final DirectoryName target)
    throws DirectoryException {
    
    // lazy initialize context
    if (this.context == null)
      connect();

    try {
      this.context.rename(origin, target);
    }
    catch (NameNotFoundException e) {
      throw new DirectoryException(Bundle.format(Bundle.CONTEXT_OBJECT_NOT_EXISTS, origin), e);
    }
    catch (NameAlreadyBoundException e) {
      throw new DirectoryException(Bundle.format(Bundle.CONTEXT_OBJECT_EXISTS, target), e);
    }
    catch (NamingException e) {
      throw new DirectoryException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryModify
  /**
   ** Modifies object and binds to the passed distinguished name and the
   ** attributes.
   ** <br>
   ** Calls the context.createSubcontext() to create object and bind it with the
   ** passed distinguished name and the attributes.
   **
   ** @param  dn                 the distinguished name of the object to modify,
   **                            e.g. <code>cn=george,dc=example,dc=com</code>.
   **                            <br>
   **                            Allowed object is {@link DirectoryName}.
   ** @param  attributes         the attributes of the object to modify.
   **                            <br>
   **                            Allowed object is {@link Attributes}.
   **
   ** @throws DirectoryException if the operation fails
   */
  public void entryModify(final DirectoryName dn, final List<ModificationItem> attributes)
    throws DirectoryException {

    LdapContext context = null;
    try {
      context = connect(environment(contextURL(dn.suffix().toString())));
      // modify object
      context.modifyAttributes(dn.prefix().toString(), attributes.toArray(new ModificationItem[0]));
    }
    catch (Exception e) {
      throw new DirectoryException(Bundle.format(Bundle.CONTEXT_ERROR_ABORT, e.getLocalizedMessage()), e);
    }
    finally {
      if (context != null)
        try {
          context.close();
        }
        catch (NamingException e) {
          ;
        }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryDelete
  /**
   ** Deletes object of the passed distinguished name.
   ** <br>
   ** Calls the context.destroySubcontext() to delete object
   **
   ** @param  objectRDN          the distinguished name of the object, e.g.
   **                            <code>'cn=george,ou=people,dc=company,dc=com'</code>
   **                            <br>
   **                            Allowed object is {@link DirectoryName}.
   ** @param  deleteControl      the possibly <code>null</code> controls to
   **                            use. If <code>null</code>, no controls are
   **                            used.
   **                            <br>
   **                            Allowed object is array of {@link Control}.
   **
   ** @throws DirectoryException if the operation fails.
   */
  public void entryDelete(final DirectoryName objectRDN, final Control[] deleteControl)
    throws DirectoryException {
    
    // lazy initialize context
    if (this.context == null)
      connect();

    try {
      if (deleteControl != null)
        this.context.setRequestControls(deleteControl);

      // delete object
      this.context.destroySubcontext(objectRDN);
    }
    catch (NameNotFoundException e) {
      throw new DirectoryException(Bundle.format(Bundle.CONTEXT_OBJECT_NOT_EXISTS, objectRDN), e);
    }
    catch (OperationNotSupportedException e) {
      throw new DirectoryException(Bundle.format(Bundle.CONTEXT_OPERATION_NOT_SUPPORTED, objectRDN), e);
    }
    catch (NamingException e) {
      throw new DirectoryException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exportContent
  /**
   ** Exports the object definition to the specified file.
   **
   ** @param  context            the distinguished name of the entry to export.
   **                            <br>
   **                            Allowed object is {@link DirectoryName}.
   ** @param  content            the absolut {@link File} path of the
   **                            content destination.
   **                            <br>
   **                            Allowed object is {@link File}.
   ** @param  format             the format descriptor one of
   **                            <ul>
   **                              <li>{@link LDAPFile.Format#DSML2}
   **                              <li>{@link LDAPFile.Format#DSML1}
   **                              <li>{@link LDAPFile.Format#LDIF}
   **                              <li>{@link LDAPFile.Format#JSON}
   **                             </ul>
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  subtreeEntry       <code>true</code> if the subordinated entries
   **                            will be exported; otherwise <code>false</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param  omitReadonly       <code>true</code> if attribute that are
   **                            declared readonly by the schema should be
   **                            omitted; otherwise <code>false</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param  attributesOnly     <code>true</code> if only the attribute names
   **                            will be exported; otherwise <code>false</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the ammount of records exported.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @throws DirectoryException in case an error does occured.
   */
  public final int exportContent(final DirectoryName context, final File content, final String format, final boolean subtreeEntry, final boolean omitReadonly, final boolean attributesOnly)
    throws DirectoryException {

    int        count  = 0;
    LDAPWriter writer = LDAPWriter.build(schema(), format, content);
    writer.omitReadonly(omitReadonly);
    writer.attributesOnly(attributesOnly);
    try {
      writer.printPrologue();
      // initialize PagedResultControl method to set the request
      // controls here we requesting a paginated result set
      final LDAPSearch process = new LDAPPageSearch(
        connect(context.toString())
      , ""
      , "(objectClass=*)"
      , null
      , LDAPSearch.control(subtreeEntry ? LDAPSearch.SCOPE_SUBTREE : LDAPSearch.SCOPE_OBJECT)
      , 1000
      );
      // this while loop is used to read the LDAP entries in blocks
      // this should decrease memory usage and help with server load
      do {
        NamingEnumeration<SearchResult> results = process.next();
        // loop through the results and
        while (results != null && results.hasMoreElements()) {
          final SearchResult result = results.nextElement();
          // the Attributes instance will have all the values from the source
          // system that requested by setting returning attributes in
          // SearchControls instance passed to the server or all if null was
          // passed by the caller to this method
          final LDAPRecord record = new LDAPRecord(result.getNameInNamespace(), result.getAttributes());
          record.toStream(writer);
          count++;
        }
      } while(process.hasMore());
    }
    finally {
      if (writer != null) {
        writer.printEpilogue();
        try {
          writer.flush();
        }
        catch (IOException e) {
          ;
        }
        writer.close();
      }
    }
    return count;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   importContent
  /**
   ** Imports the object definition from the specified file.
   **
   ** @param  content            the absolut {@link File} path of the
   **                            content destination.
   **                            <br>
   **                            Allowed object is {@link File}.
   ** @param  error              the absolut {@link File} path of the
   **                            error destination.
   **                            <br>
   **                            Allowed object is {@link File}.
   ** @param  format             the format descriptor one of
   **                            <ul>
   **                              <li>{@link LDAPFile.Format#DSML2}
   **                              <li>{@link LDAPFile.Format#DSML1}
   **                              <li>{@link LDAPFile.Format#LDIF}
   **                              <li>{@link LDAPFile.Format#JSON}
   **                             </ul>
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  stopOnError        <code>true</code> if the entire operation is to
   **                            be aborted in the event of an error; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the ammount of records imported.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @throws DirectoryException in case an error does occured.
   */
  public final int importContent(final File content, final File error, final String format, final boolean stopOnError)
    throws DirectoryException {

    int              count  = 0;
    final LDAPWriter writer = LDAPWriter.build(schema(), format, error);
    final LDAPReader reader = LDAPReader.build(format, content);
    try {
      writer.printPrologue();
      // this while loop is used to read the LDAP entries in one by one.
      // this should decrease memory usage and help with server load.
      do {
        final LDAPRecord binding = reader.nextRecord();
        if (binding == null)
          break;

        if (binding instanceof LDAPModifyContent) {
          count += importContent((LDAPModifyContent)binding, writer, stopOnError);
        }
        else if (binding instanceof LDAPModDNContent) {
          count += importContent((LDAPModDNContent)binding, writer, stopOnError);
        }
        else if (binding instanceof LDAPDeleteContent) {
          count += importContent((LDAPDeleteContent)binding, writer, stopOnError);
        }
        // fall through treat everything as an create
        else {
          count += importContent((LDAPRecord)binding, writer, stopOnError);
        }
      } while(true);
    }
    finally {
      if (writer != null) {
        writer.printEpilogue();
        try {
          writer.flush();
        }
        catch (IOException e) {
          ;
        }
        writer.close();
      }
      reader.close();
    }
    return count;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   importContent
  /**
   ** Imports the object definition for the specfied {@link LDAPAddContent}.
   **
   ** @param  content            the {@link LDAPRecord} to import.
   **                            <br>
   **                            Allowed object is {@link LDAPRecord}.
   ** @param  writer             the {@link LDAPWriter} to record errors.
   **                            <br>
   **                            Allowed object is {@link LDAPWriter}.
   ** @param  stopOnError        <code>true</code> if the entire operation is to
   **                            be aborted in the event of an error; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the ammount of records imported.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @throws DirectoryException in case an error does occured.
   */
  private final int importContent(final LDAPRecord content, final LDAPWriter writer, final boolean stopOnError)
    throws DirectoryException {

    int         success = 0;
    LdapContext context = null;
    try {
      // create object
      final Attributes attributes = content.attributes();
      if (attributes.size() > 1) {
        final DirectoryName dn = DirectoryName.build(content.namespace());
        context = connect(environment(contextURL(dn.suffix().toString())));
        context.createSubcontext(dn.prefix().toString(), attributes);
      }
      // it's also success if the entry to import is empty
      success++;
    }
    catch (SchemaViolationException e) {
      errorContent(writer, e.getLocalizedMessage(), content);
      if (stopOnError)
        throw new DirectoryException(Bundle.format(Bundle.CONTEXT_ERROR_ABORT, e.getLocalizedMessage()), e);
    }
    catch (NameAlreadyBoundException e) {
      errorContent(writer, e.getLocalizedMessage(), content);
      if (stopOnError)
        throw new DirectoryException(Bundle.format(Bundle.CONTEXT_ERROR_ABORT, e.getLocalizedMessage()), e);
    }
    catch (NamingException e) {
      errorContent(writer, e.getLocalizedMessage(), content);
      if (stopOnError)
        throw new DirectoryException(Bundle.format(Bundle.CONTEXT_ERROR_ABORT, e.getLocalizedMessage()), e);
    }
    finally {
      if (context != null)
        try {
          context.close();
        }
        catch (NamingException e) {
          ;
        }
    }
    return success;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   importContent
  /**
   ** Imports the object definition for the specfied {@link LDAPModDNContent}.
   **
   ** @param  content            the {@link LDAPModDNContent} to import.
   **                            <br>
   **                            Allowed object is {@link LDAPModDNContent}.
   ** @param  writer             the {@link LDAPWriter} to record errors.
   **                            <br>
   **                            Allowed object is {@link LDAPWriter}.
   ** @param  stopOnError        <code>true</code> if the entire operation is to
   **                            be aborted in the event of an error; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the <code>1</code> to increment the success
   **                            counter at the enclosing method; otherwise
   **                            <code>0</code> if the operation fails.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @throws DirectoryException in case an error does occured.
   */
 private final int importContent(final LDAPModDNContent content, final LDAPWriter writer, final boolean stopOnError)
    throws DirectoryException {

    int            success   = 0;
    LdapContext    context   = null;
    final String[] parameter = new String[2];
    try {
      final DirectoryName dn = DirectoryName.build(content.namespace());
      parameter[0] = dn.prefix().toString();
      parameter[1] = dn.suffix().toString();
      context = connect(environment(parameter[0]));

      // rename or move object
      success++;
      context.close();
    }
    catch (NamingException e) {
      errorContent(writer, e.getLocalizedMessage(), content);
      if (stopOnError)
        throw new DirectoryException(Bundle.format(Bundle.CONTEXT_ERROR_ABORT, e.getLocalizedMessage()), e);
    }
    return success;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   importContent
  /**
   ** Imports the object definition for the specfied {@link LDAPModifyContent}.
   **
   ** @param  content            the {@link LDAPModifyContent} to import.
   **                            <br>
   **                            Allowed object is {@link LDAPModifyContent}.
   ** @param  writer             the {@link LDAPWriter} to record errors.
   **                            <br>
   **                            Allowed object is {@link LDAPWriter}.
   ** @param  stopOnError        <code>true</code> if the entire operation is to
   **                            be aborted in the event of an error; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the <code>1</code> to increment the success
   **                            counter at the enclosing method; otherwise
   **                            <code>0</code> if the operation fails.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @throws DirectoryException in case an error does occured.
   */
  private final int importContent(final LDAPModifyContent content, final LDAPWriter writer, final boolean stopOnError)
    throws DirectoryException {

    int            success   = 0;
    LdapContext    context   = null;
    final String[] parameter = new String[2];
    try {
      final DirectoryName dn = DirectoryName.build(content.namespace());
      parameter[0] = dn.prefix().toString();
      parameter[1] = dn.suffix().toString();
      context = connect(environment(parameter[0]));
      // modify object
      context.modifyAttributes(parameter[1], content.modifications());
      success++;
    }
    catch (SchemaViolationException e) {
      errorContent(writer, e.getLocalizedMessage(), content);
      if (stopOnError)
        throw new DirectoryException(Bundle.format(Bundle.CONTEXT_ERROR_ABORT, e.getLocalizedMessage()), e);
    }
    catch (NamingException e) {
      errorContent(writer, e.getLocalizedMessage(), content);
      if (stopOnError)
        throw new DirectoryException(Bundle.format(Bundle.CONTEXT_ERROR_ABORT, e.getLocalizedMessage()), e);
    }
    finally {
      if (context != null)
        try {
          context.close();
        }
        catch (NamingException e) {
          ;
        }
    }
    return success;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   importContent
  /**
   ** Imports the object definition for the specfied {@link LDAPDeleteContent}.
   **
   ** @param  content            the {@link LDAPDeleteContent} to import.
   **                            <br>
   **                            Allowed object is {@link LDAPDeleteContent}.
   ** @param  writer             the {@link LDAPWriter} to record errors.
   **                            <br>
   **                            Allowed object is {@link LDAPWriter}.
   ** @param  stopOnError        <code>true</code> if the entire operation is to
   **                            be aborted in the event of an error; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the <code>1</code> to increment the success
   **                            counter at the enclosing method; otherwise
   **                            <code>0</code> if the operation fails.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @throws DirectoryException in case an error does occured.
   */
  private int importContent(final LDAPDeleteContent content, final LDAPWriter writer, final boolean stopOnError)
    throws DirectoryException {

    int            increment = 0;
    LdapContext    context   = null;
    final String[] parameter = new String[2];
    try {
      final DirectoryName dn = DirectoryName.build(content.namespace());
      parameter[0] = dn.prefix().toString();
      parameter[1] = dn.suffix().toString();
      // delete object
      context = connect(environment(parameter[0]));
      context.destroySubcontext(parameter[1]);
      context.close();
      increment++;
    }
    catch (NamingException e) {
      errorContent(writer, e.getLocalizedMessage(), content);
      if (stopOnError)
        throw new DirectoryException(Bundle.format(Bundle.CONTEXT_ERROR_ABORT, e.getLocalizedMessage()), e);
    }
    return increment;
  }
  
  private int errorContent(final LDAPWriter writer, final String message, LDAPRecord record) {
    writer.printComment(message);
    record.toStream(writer);
    return 0;
  }
}