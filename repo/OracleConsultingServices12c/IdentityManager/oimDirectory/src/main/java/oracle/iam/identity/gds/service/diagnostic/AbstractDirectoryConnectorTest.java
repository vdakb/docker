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

    Copyright © 2009. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Directory Service Connector

    File        :   AbstractDirectoryConnectorTest.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractDirectoryConnectorTest.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2009-02-01  DSteding    First release version
*/

package oracle.iam.identity.gds.service.diagnostic;

import java.net.URLEncoder;

import java.util.Map;
import java.util.HashMap;
import java.util.Properties;

import javax.naming.Context;

import javax.naming.ldap.LdapContext;
import javax.naming.ldap.InitialLdapContext;

import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import  oracle.hst.foundation.SystemConstant;

import oracle.iam.identity.foundation.ldap.DirectoryResource;
import oracle.iam.identity.foundation.ldap.DirectoryConstant;

////////////////////////////////////////////////////////////////////////////////
// class AbstractDirectoryConnectorTest
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>AbstractDirectoryConnectorTest</code> implements the base
 ** functionality of the connectivity test performed by Oracle Identity Manager
 ** Diagnostic DashBoard for a Generic Directory Service.
 ** <br>
 ** A connector can be any kind of outbound object as a provisioning target or a
 ** reconciliation source. It can also be an inbound object which is a task.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   0.0.0.2
 */
public class AbstractDirectoryConnectorTest {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String     CONTEXT_FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  /** factory used to create the secure socket layer */
  private static SSLSocketFactory socketFactory   = null;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String                  serviceURL      = null;

  private LdapContext             context         = null;

  private Map<String, String>     attribute       = new HashMap<String, String>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>AbstractDirectoryConnectorTest</code> that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AbstractDirectoryConnectorTest() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverName
  /**
   ** Returns the name of the server where the LDAP Directory is running and
   ** this IT Resource is configured for.
   ** <br>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link DirectoryResource#SERVER_NAME}.
   **
   ** @return                    the name of the server where LDAP Directory.
   */
  public final String serverName() {
    return stringValue(this.attribute, DirectoryResource.SERVER_NAME);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverPort
  /**
   ** Returns the port of the server where LDAP Directory is running and this
   ** IT Resource is using.
   ** <br>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link DirectoryResource#SERVER_PORT}.
   **
   ** @return                    the name of the server where LDAP Directory.
   */
  public final int serverPort() {
    return integerValue(this.attribute, DirectoryResource.SERVER_PORT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   secureSocket
  /**
   ** Returns the <code>true</code> if the server is using the secure ldap
   ** protocol.
   ** <br>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link DirectoryResource#SECURE_SOCKET}.
   **
   ** @return                    <code>true</code> if the server is using
   **                            the secure ldap protocol; otherwise
   **                            <code>false</code>.
   */
  public final boolean secureSocket() {
    return booleanValue(this.attribute, DirectoryResource.SECURE_SOCKET);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   principalName
  /**
   ** Returns the distinguished name of the principal of a Directory Server to
   ** establish a connection.
   ** <br>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link DirectoryResource#PRINCIPAL_NAME}.
   **
   ** @return                    the distinguished name of the principal to
   **                            establish a connection.
   */
  public final String principalName() {
    return stringValue(this.attribute, DirectoryResource.PRINCIPAL_NAME);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   principalPassword
  /**
   ** Returns the password of the principal of a Directory Server to establish
   ** a connection.
   ** <br>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link DirectoryResource#PRINCIPAL_PASSWORD}.
   **
   ** @return                    the password of the principal to establish a
   **                            connection.
   */
  public final String principalPassword() {
    return stringValue(this.attribute, DirectoryResource.PRINCIPAL_PASSWORD);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rootContext
  /**
   ** Returns the name of the root context in an LDAP Directory where this IT
   ** Resource will be working on
   ** <br>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link DirectoryResource#ROOT_CONTEXT}.
   **
   ** @return                    the name of the root context in an LDAP
   **                            Directory.
   */
  public final String rootContext() {
    return stringValue(this.attribute, DirectoryResource.ROOT_CONTEXT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   distinguishedNameRelative
  /**
   ** Returns whether all pathes are treated as relative to the naming context
   ** of the connected LDAP Server.
   ** Context.
   ** <br>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link DirectoryResource#DISTINGUISHED_NAME_RELATIVE}.
   **
   ** @return                    <code>true</code> if all pathes are treated as
   **                            relative in the connectec LDAP Server.
   */
  public final boolean distinguishedNameRelative() {
    return booleanValue(this.attribute, DirectoryResource.DISTINGUISHED_NAME_RELATIVE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testBasicConnectivity
  /**
   ** Called by Diagnostic DashBoard
   **
   ** @param  parameter          the parameter passed to the unit test by
   **                            Diagnostic DashBoard.
   **
   ** @return                    the result of the unit test
   **
   ** @throws Throwable          if the {@link InitialLdapContext} could
   **                            not be created at the first time this
   **                            method is invoked.
   */
  public String testBasicConnectivity(HashMap<String, String> parameter)
    throws Throwable {

    attributes(parameter);
    connect();
    disconnect();
    return Boolean.TRUE.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testProvisioning
  /**
   ** Called by Diagnostic DashBoard
   **
   ** @param  parameter          the parameter passed to the unit test by
   **                            Diagnostic DashBoard.
   **
   ** @return                    the result of the unit test
   **
   ** @throws Throwable          if the {@link InitialLdapContext} could
   **                            not be created at the first time this
   **                            method is invoked.
   */
  public String testProvisioning(HashMap<String, String> parameter)
    throws Throwable {

    attributes(parameter);
    connect();
    disconnect();
    throw new Exception("Not yet implemented");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testReconciliation
  /**
   ** Called by Diagnostic DashBoard
   **
   ** @param  parameter          the parameter passed to the unit test by
   **                            Diagnostic DashBoard.
   **
   ** @return                    the result of the unit test
   **
   ** @throws Throwable          if the {@link InitialLdapContext} could
   **                            not be created at the first time this
   **                            method is invoked.
   */
  public String testReconciliation(HashMap<String, String> parameter)
    throws Throwable {

    attributes(parameter);
    connect();
    disconnect();
    throw new Exception("Not yet implemented");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connect
  /**
   ** Constructs an initial {@link LdapContext} by creating the appropriate
   ** environment from the attributes of the associated IT Resource.
   **
   ** @return                    the context this connector use to communicate
   **                            with the LDAP server.
   **
   ** @throws Throwable          if the {@link InitialLdapContext} could
   **                            not be created at the first time this
   **                            method is invoked.
   */
  public LdapContext connect()
    throws Throwable {

    // call connect without any container instead will bind to the principal dn
    return connect("");
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
   **
   ** @return                    the context this connector use to communicate
   **                            with the LDAP server.
   **
   ** @throws Throwable          if the {@link InitialLdapContext} could
   **                            not be created at the first time this
   **                            method is invoked.
   */
  public LdapContext connect(final String contextPath)
    throws Throwable {

    if (this.context == null) {
      // Constructs an LDAP context object using environment properties and
      // connection request controls.
      // See javax.naming.InitialContext for a discussion of environment
      // properties.
      context = connect(environment(this.contextURL(contextPath), true));
    }
    return context;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connect
  /**
   ** Constructs an initial {@link LdapContext} by creating the appropriate
   ** environment from the attributes of the associated IT Resource.
   **
   ** @param  environment        environment used to create the initial
   **                            {@link InitialLdapContext}.
   **                            <code>null</code> indicates an empty
   **                            environment.
   **
   ** @return                    the context this connector use to communicate
   **                            with the LDAP server.
   **
   ** @throws Throwable          if the {@link InitialLdapContext} could
   **                            not be created at the first time this
   **                            method is invoked.
   */
  public final LdapContext connect(final Properties environment)
    throws Throwable {

    // Constructs an LDAP context object using environment properties and
    // connection request controls.
    // See javax.naming.InitialContext for a discussion of environment
    // properties.
    return new InitialLdapContext(environment, null);
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
   ** @throws Throwable          if the {@link InitialLdapContext} could
   **                            not be closed at the time this method is
   **                            invoked.
   */
  public void disconnect()
    throws Throwable {

    this.disconnect(this.context);
    context = null;
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
   **
   ** @throws Throwable          if the {@link InitialLdapContext} could
   **                            not be closed at the time this method is
   **                            invoked.
   */
  public void disconnect(final LdapContext context)
    throws Throwable {

    if (context != null)
      context.close();
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
   ** @param  pooling            <code>true</code> if the underlying
   **                            implementation supports connection pooling;
   **                            otherwise <code>false</code>.
   **
   ** @return                    the context this connector use to communicate
   **                            with the LDAP server.
   **
   ** @throws Throwable          if the affected hist is unknown or the socket
   **                            cannot be opened at the time this method is
   **                            invoked.
   */
  public final Properties environment(final String contextPath, final boolean pooling)
    throws Throwable {

    Properties environment = new Properties();
    // Set up environment for creating initial context
    environment.put(Context.INITIAL_CONTEXT_FACTORY,      CONTEXT_FACTORY);
    environment.put(Context.PROVIDER_URL,                 contextPath);
    environment.put("java.naming.ldap.version",           "3");
    environment.put(Context.SECURITY_PRINCIPAL,           this.attribute.get(DirectoryResource.PRINCIPAL_NAME));
    environment.put(Context.SECURITY_CREDENTIALS,         this.attribute.get(DirectoryResource.PRINCIPAL_PASSWORD));
    // Enable or disable connection pooling
    environment.put("com.sun.jndi.ldap.connect.pool",     pooling ? SystemConstant.TRUE : SystemConstant.FALSE);

    if (Boolean.getBoolean(this.attribute.get(DirectoryResource.SECURE_SOCKET))) {
      environment.put(Context.SECURITY_PROTOCOL, DirectoryConstant.SECURITY_PROTOCOL);
      invalidateSSLSession();
    }
    return environment;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invalidateSSLSession
  /**
   ** Invalidates the SSL session.
   ** <br>
   ** Future connections will not be able to resume or join this session.
   ** However, any existing connection using this session can continue to use
   ** the session until the connection is closed.
   **
   ** @throws Throwable          if the affected hist is unknown or the socket
   **                            cannot be opened at the time this method is
   **                            invoked.
   */
  private void invalidateSSLSession()
    throws Throwable {

    // if not already done obtain the default SSL socket factory
    if (socketFactory == null)
      socketFactory = (SSLSocketFactory)SSLSocketFactory.getDefault();

    final String     host    = serverName();
    final int        port    = serverPort();

    // creates a SSL socket and connect it to the remote host at the remote
    // port.
    final SSLSocket  socket  = (SSLSocket)socketFactory.createSocket(host, port);
    final SSLSession session = socket.getSession();
    session.invalidate();
    socket.close();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   booleanValue
  /**
   ** Returns a <code>boolean</code> from the attribute mapping of this wrapper.
   **
   ** @param  parameter          the attribute mapping providing the key/value
   **                            pairs.
   ** @param  key                the key for the desired attribute string.
   **
   ** @return                    the boolean for the given key.
   **
   ** @throws IllegalStateException if the provided key does not have a value in
   **                               the passed {@link Map}.
   ** @throws ClassCastException    if the object returned by the get operation
   **                               on the mappping is not castable to a
   **                               <code>String</code>.
   */
  public static boolean booleanValue(final Map<String, String> parameter, final String key) {
    String result = stringValue(parameter, key);
    // convert the yes/no semantic to the correct meaning for class Boolean
    if (SystemConstant.YES.equalsIgnoreCase(result))
      result = SystemConstant.TRUE;

    return Boolean.valueOf(result).booleanValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   integerValue
  /**
   ** Returns a <code>int</code> from the attribute mapping of this wrapper.
   **
   ** @param  parameter          the attribute mapping providing the key/value
   **                            pairs.
   ** @param  key                the key for the desired attribute string.
   **
   ** @return                    the int for the given key.
   **
   ** @throws IllegalStateException if the provided key does not have a value in
   **                               the passed {@link Map}.
   ** @throws ClassCastException    if the object returned by the get operation
   **                               on the mappping is not castable to a
   **                               <code>String</code>.
   */
  public static int integerValue(final Map<String, String> parameter, final String key) {
    return Integer.parseInt(stringValue(parameter, key));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringValue
  /**
   ** Returns a <code>String</code> from the attribute mapping of this wrapper.
   **
   ** @param  parameter          the attribute mapping providing the key/value
   **                            pairs.
   ** @param  key                the key for the desired attribute string.
   **
   ** @return                    the <code>String</code> for the given key.
   **
   ** @throws IllegalStateException if the provided key does not have a value in
   **                               the passed {@link Map}.
   ** @throws ClassCastException    if the object returned by the get operation
   **                               on the mappping is not castable to a
   **                               <code>String</code>.
   */
  private static String stringValue(final Map<String, String> parameter, final String key) {
    final String result = parameter.get(key);
    if (result == null || result.length() == 0)
      throw new IllegalStateException(key + " is missing");
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   normalizeContext
  /**
   ** Removes the any component from the given context that are not specifying
   ** a naming context.
   **
   ** @param  context            the relative distinguished name of the
   **                            Root Context passed.
   **
   ** @return                    the distinguished name of the Root Context
   */
  public static String normalizeContext(final String context) {
    String rootContext = context.toLowerCase();
    int firstComponent = rootContext.indexOf("dc");
    return (firstComponent == -1L) ? rootContext : rootContext.substring(firstComponent);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   normalizePath
  /**
   ** Forms the basis of building the hierarchical tree to the LDAP object.
   ** Used by connect to build the correct connection.
   **
   ** @param  context            Contains the elements in the tree, deepest one
   **                            first. The String must be of format
   **                            "Class Type=Object CN,Class Type=Object CN"
   **                            where:
   **                            <ul>
   **                              <li>Class Type is the objects class type ("CN", "OU", ...)
   **                              <li>Object CN is the LDAP objects common name ("dsteding", "finance group", ... )
   **                            </ul>
   **                            Basically whatever is assigned to the
   **                            mandatory property "cn" or "ou". e.g.
   **                            <code>CN=Dumbo,OU=Leaders,OU=Elephants</code>
   **
   ** @return                    String of the canonical path (including the
   **                            root context), e.g.
   **                            OU=Users,OU=abc,OU=Companies,DC=thordev,DC=com
   */
  public String normalizePath(String context) {

    String domainContext = normalizeContext(this.rootContext());

    StringBuilder path = new StringBuilder();
    // if context is empty or null
    if (context == null || context.length() == 0)
      path.append(domainContext);
    else {
      if (this.distinguishedNameRelative()) {
        // if context value passed is Relative Distinguished Name
        path.append(context);
        path.append(',');
        path.append(domainContext);
      }
      else
        // if context value is treated as a Distinguished Name of the context
        path.append(context);
    }
    return path.toString();
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
   **
   ** @return                    the full qualified LDAP URL.
   **
   ** @throws Throwable          if the named encoding is not supported.
   */
  public final String contextURL(String distinguishedName)
    throws Throwable {

    // create the service url (server name and port) prafixed with the protocol
    StringBuilder url = new StringBuilder(serviceURL());
    // check if the service url end already with a slash '/'
    if (url.charAt(url.length() - 1) != SystemConstant.SLASH)
      url.append(SystemConstant.SLASH);

    url.append(URLEncoder.encode(normalizePath(distinguishedName), "UTF-8"));
    // return the resulting url by escaping all space characters that may
    // contained by the appropriate encoded character
    return url.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributes
  /**
   ** Copies the necessary attribute from the passed {@link Map} to the internal
   ** managed atribute mapping.
   **
   ** @param  parameter          the parameter passed to the unit test by
   **                            Diagnostic DashBoard.
   **
   ** @return                    the result of the unit test
   **
   ** @throws Throwable          if the one attribute is missing.
   */
  private final void attributes(Map<String, String> parameter) {
    this.attribute.put(DirectoryResource.SERVER_NAME,        stringValue(parameter, DirectoryResource.SERVER_NAME));
    this.attribute.put(DirectoryResource.SERVER_PORT,        stringValue(parameter, DirectoryResource.SERVER_PORT));
    this.attribute.put(DirectoryResource.ROOT_CONTEXT,       stringValue(parameter, DirectoryResource.ROOT_CONTEXT));
    this.attribute.put(DirectoryResource.PRINCIPAL_NAME,     stringValue(parameter, DirectoryResource.PRINCIPAL_NAME));
    this.attribute.put(DirectoryResource.PRINCIPAL_PASSWORD, stringValue(parameter, DirectoryResource.PRINCIPAL_PASSWORD));
    this.attribute.put(DirectoryResource.SECURE_SOCKET,      stringValue(parameter, DirectoryResource.SECURE_SOCKET));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serviceURL
  /**
   ** Return the server part of the ldap url, ldap://host:port
   **
   ** @return                    the server part of the ldap url,
   **                            ldap://host:port
   */
  private final String serviceURL() {
    if (this.serviceURL == null) {
      StringBuilder url = new StringBuilder(DirectoryConstant.PROTOCOL_DEFAULT);
      url.append("://");
      if (serverName() != null)
        url.append(serverName()).append(':').append(serverPort());

      this.serviceURL = url.toString();
    }
    return this.serviceURL;
  }
}