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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Foundation Shared Library

    File        :   AbstractEndpoint.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractEndpoint.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.foundation;

import java.util.List;
import java.util.ArrayList;

import org.identityconnectors.common.security.GuardedString;

import oracle.iam.identity.icf.foundation.logging.Loggable;
import oracle.iam.identity.icf.foundation.logging.AbstractLoggable;

////////////////////////////////////////////////////////////////////////////////
// class AbstractEndpoint
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** The <code>AbstractEndpoint</code> implements the base functionality of a
 ** Connector Framwork <code>IT Resource</code>.
 **
 ** @param  <T>                  the type of the implementation.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations of the endpoints
 **                              extending this class (endpoints can return
 **                              their own specific type instead of type defined
 **                              by this class only).
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class AbstractEndpoint<T extends AbstractEndpoint> extends AbstractLoggable<T> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to hold
   ** the name of the target system where this <code>IT Resource</code> will be
   ** working on.
   */
  public static final String SERVER_HOST                         = "server-host";

  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to hold
   ** the port of the target system where this <code>IT Resource</code> will be
   ** working on.
   */
  public static final String  SERVER_PORT                        = "server-port";

  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to hold
   ** the name of the root context where this <code>IT Resource</code> will be
   ** working on.
   */
  public static final String  ROOT_CONTEXT                       = "root-context";

  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to
   ** specify if you plan to configure SSL to secure communication between
   ** Identity Manager and the target system.
   */
  public static final String  SECURE_SOCKET                      = "secure-socket";

  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to
   ** specify the user name of the target system account to be used to establish
   ** a connection.
   */
  public static final String  PRINCIPAL_NAME                     = "principal-name";

  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to
   ** specify the password of the target system account specified by the
   ** #PRINCIPAL_NAME parameter.
   */
  public static final String  PRINCIPAL_PASSWORD                 = "principal-password";

  /**
   ** Attribute tag which must be defined on an IT Resource to hold the name of
   ** language the target system is using.
   */
  public static final String  LOCALE_LANGUAGE                    = "locale-language";

  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to hold
   ** the name of language region the target system is using.
   */
  public static final String  LOCALE_COUNTRY                     = "locale-country";

  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to hold
   ** the name of time zone the target system is using.
   */
  public static final String  LOCALE_TIMEZONE                    = "locale-timezone";

  /**
   ** Attribute tag which might be defined on an <code>IT Resource</code> to
   ** hold the timeout period for establishment of the connection.
   */
  public static final String  CONNECTION_TIMEOUT                = "connection-timeout";

  /**
   ** Attribute tag which might be defined on an <code>IT Resource</code> to
   ** hold the number of consecutive attempts to be made at establishing a
   ** connection with the <code>Service Provider</code> endpoint.
   */
  public static final String  CONNECTION_RETRY_COUNT            = "connection-retry-count";

  /**
   ** Attribute tag which might be defined on an <code>IT Resource</code> to
   ** hold the interval (in milliseconds) between consecutive attempts at
   ** establishing a connection with the <code>Service Provider</code> endpoint.
   */
  public static final String  CONNECTION_RETRY_INTERVAL         = "connection-retry-interval";

  /**
   ** Attribute tag which might be defined on an <code>IT Resource</code> to
   ** hold the timeout period for reading data on an already established
   ** connection.
   */
  public static final String  RESPONSE_TIMEOUT                  = "response-timeout";

  /**
   ** The default timeout period for establishment of a connection.
   */
  public static final int     CONNECTION_TIMEOUT_DEFAULT        = -1;
  /**
   ** The default number of consecutive attempts to be made at establishing a
   ** connection with the <code>Service Provider</code> endpoint.
   */
  public static final int     CONNECTION_RETRY_COUNT_DEFAULT    = 3;

  /**
   ** The default the interval (in milliseconds) between consecutive attempts at
   ** establishing a connection with the <code>Service Provider</code> endpoint.
   */
  public static final int     CONNECTION_RETRY_INTERVAL_DEFAULT = -1;

  /**
   ** The default timeout period for reading data on an already established
   ** connection.
   */
  public static final int     RESPONSE_TIMEOUT_DEFAULT          = 10000;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to
   ** specify the host name and port for the primary
   ** <code>Service Provider</code> endpoint.
   */
  protected Server        primary                 = new Server();
  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify the servers that should be used if it will not be possible to
   ** establish a connection to the primary server defined in the
   ** <code>IT Resource</code>.
   ** <p>
   ** To configure failover systems, you need to specify the complete URL in the
   ** following format:
   ** <em>protocol</em>://SERVERADDRESS:PORT/ <em>protocol</em>://SERVERADDRESS1:PORT1/
   */
  protected List<Server>  failover                 = null;
  /**
   ** Attribute tag which may be defined on an <code>IT Resource</code> to
   ** specify the fully qualified domain name of the parent or root
   ** URI.
   */
  protected String        rootContext              = null;
  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to
   ** specify if you plan to configure SSL to secure communication between
   ** Identity Manager and the <code>Service Provider</code> endpoint.
   */
  protected boolean       secureSocket              = false;
  /**
   ** Attribute tag which might be defined on an <code>IT Resource</code> to
   ** specify if the keystore type if you plan to configure SSL to secure
   ** communication between Identity Manager and the
   ** <code>Service Provider</code> endpoint.
   */
  protected String        trustedStoreType          = "JKS";
  /**
   ** Attribute tag which might be defined on an <code>IT Resource</code> to
   ** specify the keystore file if you plan to configure SSL to secure
   ** communication between Identity Manager and the
   ** <code>Service Provider</code> endpoint.
   */
  protected String        trustedStoreFile;
  /**
   ** Attribute tag which might be defined on an <code>IT Resource</code> to
   ** specify the password for the keystore if you plan to configure SSL to
   ** secure communication between Identity Manager and the
   ** <code>Service Provider</code> endpoint.
   */
  protected String        trustedStorePassword;
  /**
   ** Attribute tag which might be defined on an <code>IT Resource</code> to
   ** specify if the keystore type if you plan to configure SSL to secure
   ** communication between Identity Manager and the
   ** <code>Service Provider</code> endpoint.
   */
  protected String        identityStoreType         = "JKS";
  /**
   ** Attribute tag which might be defined on an <code>IT Resource</code> to
   ** specify the keystore file if you plan to configure SSL to secure
   ** communication between Identity Manager and the
   ** <code>Service Provider</code> endpoint.
   */
  protected String        identityStoreFile;
  /**
   ** Attribute tag which might be defined on an <code>IT Resource</code> to
   ** specify the password for the keystore if you plan to configure SSL to
   ** secure communication between Identity Manager and the
   ** <code>Service Provider</code> endpoint.
   */
  protected String        identityStorePassword;
  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to
   ** specify the user name and password credentials of the
   ** <code>Service Provider</code> endpoint account used to authenticate a 
   ** onnection.
   */
  protected Principal     principal                 = null;
  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to hold
   ** the name of language the <code>Service Provider</code> endpoint is using.
   */
  protected String        language                  = null;
  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to hold
   ** the name of language region the <code>Service Provider</code> endpoint is
   ** using.
   */
  protected String        country                   = null;
  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to hold
   ** the name of language region the <code>Service Provider</code> endpoint is
   ** using.
   */
  protected String        timeZone                  = null;
  /**
   ** The timeout period for reading data on an already established connection.
   ** <p>
   ** When an service request is made by a client to a server and the server
   ** does not respond for some reason, the client waits forever for the server
   ** to respond until the TCP timeouts. On the client-side what the user
   ** experiences is esentially a process hang. In order to control the service
   ** request in a timely manner, a read timeout can be configured for the
   ** <code>Service Provider</code> endpoint.
   ** <p>
   ** If this property is not specified, the default is to wait for the response
   ** until it is received.
   */
  protected int          timeOutResponse            = RESPONSE_TIMEOUT_DEFAULT;
  /**
   ** The timeout period for establishment of a connection.
   ** <p>
   ** This property affects the TCP timeout when opening a connection to a
   ** <code>Service Provider</code> endpoint. When connection pooling has been
   ** requested, this property also specifies the maximum wait time or a
   ** connection when all connections in pool are in use and the maximum pool
   ** size has been reached.
   ** <p>
   ** If this property has not been specified, the Service Client will wait
   ** indefinitely for a pooled connection to become available, and to wait
   ** for the default TCP timeout to take effect when creating a new
   ** connection.
   */
  protected int           timeOutConnect            = CONNECTION_TIMEOUT_DEFAULT;
  /**
   ** The number of consecutive attempts to be made at establishing a connection
   ** with the <code>Service Provider</code> endpoint.
   */
  protected int           retryCount                = CONNECTION_RETRY_COUNT_DEFAULT;
  /**
   ** The the interval (in milliseconds) between consecutive attempts at
   ** establishing a connection with the <code>Service Provider</code> endpoint.
   */
  protected int           retryInterval             = CONNECTION_RETRY_INTERVAL_DEFAULT;
  /**
   ** Attribute tag which may be defined on a <code>Metadata Descriptor</code>
   ** to specify the format of a timestamp value in the
   ** <code>Service Provider</code> endpoint.
   */
  protected String        timestampFormat           = "yyyyMMddHHmmss.SSS'Z'";
  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the identifier attribute of an entry in a
   ** <code>Service Provider</code> endpoint.
   */
  protected String        identifier               = null;
  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the unqiue name attribute of an entry in a
   ** <code>Service Provider</code> endpoint.
   */
  protected String        uniqueName               = null;
  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the password attribute of an entry in a
   ** <code>Service Provider</code> endpoint.
   */
  protected String        password                 = null;
  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the status attribute of an entry in a
   ** <code>Service Provider</code> endpoint.
   */
  protected String        status                   = null;
  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the creator name attribute of an entry in a
   ** <code>Service Provider</code> endpoint.
   */
  protected String        creator                  = null;
  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the create timestamp attribute of an entry in a
   ** <code>Service Provider</code> endpoint.
   */
  protected String        created                  = null;
  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the modifier name attribute of an entry in a
   ** <code>Service Provider</code> endpoint.
   */
  protected String        modifier                 = null;
  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify the name of the modified timestamp attribute of an entry in a
   ** <code>Service Provider</code> endpoint.
   */
  protected String        modified                 = null;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Server
  /**
   ** <code>Server</code> is a value holder for the failover configuration of a
   ** <code>Service Provider</code> that isn't frontended by a load balancer.
   */
  public static class Server<S extends Server> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    protected String host;
    protected int    port;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>Server</code> that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Server() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Server</code> with host name and port.
     **
     ** @param  host             the host name of the server of the
     **                          <code>Service Provider</code> (the iron).
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  port             the port number the
     **                          <code>Service Provider</code> endpoint is
     **                          listening on.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     */
    public Server(final String host, final int port) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.host = host;
      this.port = port;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////
    // Method: host
    /**
     ** Sets the <code>host</code> attribute for the
     ** <code>Service Provider</code> endpoint.
     **
     ** @param  value            the host name of the server of the
     **                          <code>Service Provider</code> (the iron).
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the {@link Server} to allow method chaining.
     **                          <br>
     **                          Possible object is {@link Server}.
     */
    @SuppressWarnings({"cast", "unchecked"})
    public final S host(final String value) {
      this.host = value;
      return (S)this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: host
    /**
     ** Returns the <code>host</code> attribute for the
     ** <code>Service Provider</code> endpoint.
     **
     ** @return                  the host name of the server of the
     **                          <code>Service Provider</code> (the iron).
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String host() {
      return this.host;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method: port
    /**
     ** Sets the <code>port</code> attribute for the
     ** <code>Service Provider</code> endpoint.
     **
     ** @param  value            the port number the
     **                          <code>Service Provider</code> endpoint is
     **                          listening on.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     **
     ** @return                  the {@link Server} to allow method chaining.
     **                          <br>
     **                          Possible object is {@link Server}.
     */
    @SuppressWarnings({"cast", "unchecked"})
    public final S port(final int value) {
      this.port = value;
      return (S)this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: port
    /**
     ** Returns the <code>port</code> attribute for the
     ** <code>Service Provider</code> endpoint.
     **
     ** @return                  the port number the
     **                          <code>Service Provider</code> endpoint is
     **                          listening on.
     **                          <br>
     **                          Possible object is <code>int</code>.
     */
    public final int port() {
      return this.port;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: of
    /**
     ** Factory method to create a proper <code>Server</code> with the specified
     ** host name and port.
     **
     ** @param  host             the host name of the server of the
     **                          <code>Service Provider</code> (the iron).
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  port             the port number the
     **                          <code>Service Provider</code> endpoint is
     **                          listening on.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     **
     ** @return                  the <code>Server</code> constraint.
     **                          <br>
     **                          Possible object is <code>Server</code>.
     */
    public static Server of(final String host, final int port) {
      return new Server(host, port);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: serviceURL
    /**
     ** Return the server part of an url, <em>protocol</em>://host:port.
     **
     ** @param  protocol         the protocol of the url to build.
     **
     ** @return                  the server part of the an url,
     **                          <em>protocol</em>://host:port
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public String serviceURL(final String protocol) {
      final StringBuilder service = new StringBuilder(protocol);
      service.append("://");
      if (this.host != null)
        service.append(this.host);
      if (this.port != -1)
        service.append(SystemConstant.COLON).append(this.port);

      return service.toString();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Principal
  /**
   ** <code>Principal</code> is a value holder for the security principal used
   ** to authenticate a connection request.
   */
  public static class Principal<P extends Principal> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /**
     ** Attribute tag which must be defined on an <code>IT Resource</code> to
     ** specify the user name of the <code>Service Provider</code> endpoint
     ** account used to establish a connection.
     */
    protected String        username = null;
    /**
     ** Attribute tag which must be defined on an <code>IT Resource</code> to
     ** specify the credential of the <code>Service Provider</code> endpoint
     ** account used to establish a connection.
     */
    protected GuardedString password = null;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>Principal</code> that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Principal() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Principal</code> with host name and port.
     **
     ** @param  username         the <code>username</code> attribute for the
     **                          <code>Service Provider</code> endpoint.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  password         the <code>password</code> attribute for the
     **                          <code>Service Provider</code> endpoint.
     **                          <br>
     **                          Allowed object is {@link GuardedString}.
     */
    public Principal(final String username, final GuardedString password) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.username = username;
      this.password = password;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////
    // Method: username
    /**
     ** Sets the <code>username</code> attribute for the
     ** <code>Service Provider</code> endpoint.
     **
     ** @param  value            the <code>username</code> attribute for the
     **                          <code>Service Provider</code> endpoint.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the {@link Principal} to allow method chaining.
     **                          <br>
     **                          Possible object is {@link Principal}.
     */
    @SuppressWarnings({"cast", "unchecked"})
    public final P username(final String value) {
      this.username = value;
      return (P)this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: username
    /**
     ** Returns the <code>username</code> attribute for the
     ** <code>Service Provider</code> endpoint.
     **
     ** @return                  the <code>username</code> attribute for the
     **                          <code>Service Provider</code> endpoint.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String username() {
      return this.username;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: password
    /**
     ** Sets the <code>password</code> attribute for the
     ** <code>Service Provider</code> endpoint.
     **
     ** @param  value            the <code>password</code> attribute for the
     **                          <code>Service Provider</code> endpoint.
     **                          <br>
     **                          Allowed object is {@link GuardedString}.
     **
     ** @return                  the {@link Principal} to allow method chaining.
     **                          <br>
     **                          Possible object is {@link Principal}.
     */
    @SuppressWarnings({"cast", "unchecked"})
    public final P password(final GuardedString value) {
      this.password= value;
      return (P)this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: password
    /**
     ** Returns the <code>password</code> attribute for the
     ** <code>Service Provider</code> endpoint.
     **
     ** @return                  the <code>password</code> attribute for the
     **                          <code>Service Provider</code> endpoint.
     **                          <br>
     **                          Possible object is {@link GuardedString}.
     */
    public final GuardedString password() {
      return password;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: of
    /**
     ** Factory method to create a proper <code>Principal</code> with the
     ** specified username and password.
     **
     ** @param  username         the <code>username</code> attribute for the
     **                          <code>Service Provider</code> endpoint.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  password         the <code>password</code> attribute for the
     **                          <code>Service Provider</code> endpoint.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Principal</code> constraint.
     **                          <br>
     **                          Possible object is <code>Principal</code>.
     */
    public static Principal of(final String username, final String password) {
      return of(username, password.toCharArray());
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: of
    /**
     ** Factory method to create a proper <code>Principal</code> with the
     ** specified username and password.
     **
     ** @param  username         the <code>username</code> attribute for the
     **                          <code>Service Provider</code> endpoint.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  password         the <code>password</code> attribute for the
     **                          <code>Service Provider</code> endpoint.
     **                          <br>
     **                          Allowed object is array of <code>char</code>.
     **
     ** @return                  the <code>Principal</code> constraint.
     **                          <br>
     **                          Possible object is <code>Principal</code>.
     */
    public static Principal of(final String username, final char[] password) {
      return of(username, new GuardedString(password));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: of
    /**
     ** Factory method to create a proper <code>Principal</code> with the
     ** specified username and password.
     **
     ** @param  username         the <code>username</code> attribute for the
     **                          <code>Service Provider</code> endpoint.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  password         the <code>password</code> attribute for the
     **                          <code>Service Provider</code> endpoint.
     **                          <br>
     **                          Allowed object is {@link GuardedString}.
     **
     ** @return                  the <code>Principal</code> constraint.
     **                          <br>
     **                          Possible object is <code>Principal</code>.
     */
    public static Principal of(final String username, final GuardedString password) {
      return new Principal(username, password);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AbstractEndpoint</code> which is associated with the
   ** specified {@link Loggable}.
   **
   ** @param  loggable           the {@link Loggable} which has
   **                            instantiated this {@link AbstractLoggable}.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   */
  protected AbstractEndpoint(final Loggable loggable) {
    // ensure inheritance
    super(loggable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractEndpoint</code> which is associated with the
   ** specified {@link Loggable}.
   ** <br>
   ** The <code>IT Resource</code> will be populated from the specified
   ** parameters.
   **
   ** @param  loggable           the {@link Loggable} that instantiate the
   **                            <code>AbstractEndpoint</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  serverHost         the host name or IP address of the target
   **                            system on which the
   **                            <code>Service Provider</code> is deployed.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  serverPort         the port the <code>Service Provider</code> is
   **                            listening on
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  rootContext        the fully qualified domain name of the parent
   **                            or root organization.
   **                            <br>
   **                            For example, the root suffix.
   **                            <br>
   **                            Format: <code>ou=<i>ORGANIZATION_NAME</i>,dc=<i>DOMAIN</i></code>
   **                            <br>
   **                            Sample value: <code>ou=Adapters, dc=adomain</code>
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  principalName      the fully qualified domain name corresponding
   **                            to the acccount of the
   **                            <code>Service Provider</code> with
   **                            administrator privikeges.
   **                            <br>
   **                            Format: <code>cn=<i>ADMIN_LOGIN</i>,cn=Users,dc=<i>DOMAIN</i></code>
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  principalPassword  the password of the administrator account that
   **                            is used
   **                            <br>
   **                            Allowed object is {@link GuardedString}.
   ** @param  secureSocket       specifies whether or not to use SSL to secure
   **                            communication between Identity Manager and the
   **                            target system.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param  language           the language code of the
   **                            <code>Service Provider</code>.
   **                            <br>
   **                            Default value: en
   **                            <br>
   **                            Note: You must specify the value in lowercase.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  country            Country code of the ervice Provider
   **                            <br>
   **                            Default value: US
   **                            <br>
   **                            Note: You must specify the value in uppercase.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  timeZone           use this parameter to specify the time zone of
   **                            the target system. For example: GMT-08:00 and
   **                            GMT+05:30.
   **                            <br>
   **                            During a provisioning operation, the connector
   **                            uses this time zone information to convert
   **                            date-time values entered on the process form to
   **                            date-time values relative to the time zone of
   **                            the target system.
   **                            <br>
   **                            Default value: GMT
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  protected AbstractEndpoint(final Loggable loggable, final String serverHost, final int serverPort, final String rootContext, final String principalName, final GuardedString principalPassword, final boolean secureSocket, final String language, final String country, final String timeZone) {
    // ensure inheritance
    this(loggable, new Server(serverHost, serverPort), rootContext, new Principal(principalName, principalPassword), secureSocket, language, country, timeZone);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractEndpoint</code> which is associated with the
   ** specified {@link Loggable}.
   ** <br>
   ** The <code>IT Resource</code> will be populated from the specified
   ** parameters.
   **
   ** @param  loggable           the {@link Loggable} that instantiate the
   **                            <code>AbstractEndpoint</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  primary            the primary server properties on which the
   **                            <code>Service Provider</code> is deployed and
   **                            listening.
   **                            <br>
   **                            Allowed object is {@link Server}.
   ** @param  rootContext        the fully qualified domain name of the parent
   **                            or root organization.
   **                            <br>
   **                            For example, the root suffix.
   **                            <br>
   **                            Format: <code>ou=<i>ORGANIZATION_NAME</i>,dc=<i>DOMAIN</i></code>
   **                            <br>
   **                            Sample value: <code>ou=Adapters, dc=adomain</code>
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  principal          the security principal properties user to
   **                            authenticate a connection with the Service
   **                            Provider.
   **                            <br>
   **                            Allowed object is {@link Principal}.
   ** @param  secureSocket       specifies whether or not to use SSL to secure
   **                            communication between Identity Manager and the
   **                            <code>Service Provider</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param  language           the language code of the
   **                            <code>Service Provider</code>
   **                            <br>
   **                            Default value: en
   **                            <br>
   **                            Note: You must specify the value in lowercase.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  country            Country code of the ervice Provider
   **                            <br>
   **                            Default value: US
   **                            <br>
   **                            Note: You must specify the value in uppercase.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  timeZone           use this parameter to specify the time zone of
   **                            the target system. For example: GMT-08:00 and
   **                            GMT+05:30.
   **                            <br>
   **                            During a provisioning operation, the connector
   **                            uses this time zone information to convert
   **                            date-time values entered on the process form to
   **                            date-time values relative to the time zone of
   **                            the target system.
   **                            <br>
   **                            Default value: GMT
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  protected AbstractEndpoint(final Loggable loggable, final Server primary, final String rootContext, final Principal principal, final boolean secureSocket, final String language, final String country, final String timeZone) {
    // ensure inheritance
    super(loggable);

    this.primary      = primary;
    this.principal    = principal;
    this.secureSocket = secureSocket;
    this.rootContext  = rootContext;
    this.language     = language;
    this.country      = country;
    this.timeZone     = timeZone;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   primary
  /**
   ** Sets the <code>primary</code> attribute for the
   ** <code>Service Provider</code> endpoint.
   **
   **
   ** @param  host               the host name or IP address of the
   **                            <code>primary</code> server for the
   **                            <code>Service Provider</code> endpoint.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  port               the port number of the <code>primary</code>
   **                            server for the
   **                            <code>Service Provider</code> endpoint.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the <code>AbstractEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  public final T primary(final String host, final int port) {
    return primary(new Server(host, port));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   primary
  /**
   ** Sets the <code>primary</code> attribute for the
   ** <code>Service Provider</code> endpoint.
   **
   ** @param  value              the <code>primary</code> attribute for the
   **                            <code>Service Provider</code> endpoint.
   **                            <br>
   **                            Allowed object is {@link Server}.
   **
   ** @return                    the <code>AbstractEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T primary(final Server value) {
    this.primary = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   primary
  /**
   ** Returns the <code>primary</code> attribute for the
   ** <code>Service Provider</code> endpoint.
   **
   ** @return                    the <code>primary</code> attribute for the
   **                            <code>Service Provider</code> endpoint.
   **                            <br>
   **                            Possible object is {@link Server}.
   */
  public final Server primary() {
    return this.primary;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   primaryHost
  /**
   ** Sets the <code>primaryHost</code> attribute for the
   ** <code>Service Provider</code> endpoint.
   ** <p>
   ** Convenience method to shortens the access to the configuration property
   ** {@link Server#host} of the primary server.
   **
   ** @param  value              the host name or IP address of the
   **                            <code>primary</code> server for the
   **                            <code>Service Provider</code> endpoint.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>AbstractEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T primaryHost(final String value) {
    this.primary.host = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   primaryHost
  /**
   ** Returns the <code>primaryHost</code> attribute for the
   ** <code>Service Provider</code> endpoint.
   ** <p>
   ** Convenience method to shortens the access to the configuration property
   ** {@link Server#host} of the primary server.
   **
   ** @return                    the host name or IP address of the
   **                            <code>primary</code> server for the
   **                            <code>Service Provider</code> endpoint.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String primaryHost() {
    return this.primary.host;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   primaryPort
  /**
   ** Sets the <code>primaryPort</code> attribute for the
   ** <code>Service Provider</code> endpoint.
   ** <p>
   ** Convenience method to shortens the access to the configuration property
   ** {@link Server#host} of the primary server.
   **
   ** @param  value              the port number of the <code>primary</code>
   **                            server for the
   **                            <code>Service Provider</code> endpoint.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the <code>AbstractEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T primaryPort(final int value) {
    this.primary.port = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   primaryPort
  /**
   ** Returns the <code>primaryPort</code> attribute for the
   ** <code>Service Provider</code> endpoint.
   ** <p>
   ** Convenience method to shortens the access to the configuration property
   ** {@link Server#host} of the primary server.
   **
   ** @return                    the port number of the <code>primary</code>
   **                            server for the
   **                            <code>Service Provider</code> endpoint.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public final int primaryPort() {
    return this.primary.port;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rootContext
  /**
   ** Sets the <code>rootContext</code> attribute for the
   ** <code>Service Provider</code> endpoint.
   ** <p>
   ** Method isn't defined as final to allow subclasses to override method to
   ** initialize default values.
   **
   ** @param  value              the <code>rootContext</code> attribute for the
   **                            <code>Service Provider</code> endpoint.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>AbstractEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public T rootContext(final String value) {
    this.rootContext = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rootContext
  /**
   ** Returns the <code>rootContext</code> attribute for the
   ** <code>Service Provider</code> endpoint.
   ** <p>
   ** Method isn't defined as final to allow subclasses to override method to
   ** return default values.
   **
   ** @return                    the <code>rootContext</code> attribute for the
   **                            <code>Service Provider</code> endpoint.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String rootContext() {
    return this.rootContext;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   secureSocket
  /**
   ** Sets the <code>secureSocket</code> attribute for the
   ** <code>Service Provider</code> endpoint.
   **
   ** @param  value              the <code>secureSocket</code> attribute for the
   **                            <code>Service Provider</code> endpoint.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the <code>AbstractEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T secureSocket(final boolean value) {
    this.secureSocket = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   secureSocket
  /**
   ** Returns the <code>secureSocket</code> attribute for the
   ** <code>Service Provider</code> endpoint.
   **
   ** @return                    the <code>secureSocket</code> attribute for the
   **                            <code>Service Provider</code> endpoint.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean secureSocket() {
    return this.secureSocket;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trustedStoreType
  /**
   ** Sets the <code>trustedStoreType</code> attribute for the
   ** <code>Service Provider</code> endpoint if you plan to secure communication
   ** between Identity Manager and the <code>Service Provider</code> endpoint.
   **
   ** @param  value              the <code>trustedStoreType</code> attribute for
   **                            the <code>Service Provider</code> endpoint.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>AbstractEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T trustedStoreType(final String value) {
    this.trustedStoreType = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trustedStoreType
  /**
   ** Returns the <code>trustedStoreType</code> attribute for the
   ** <code>Service Provider</code> endpoint if you plan to secure communication
   ** between Identity Manager and the <code>Service Provider</code> endpoint.
   **
   ** @return                    the <code>trustedStoreType</code> attribute for
   **                            the <code>Service Provider</code> endpoint.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String trustedStoreType() {
    return this.trustedStoreType;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trustedStoreFile
  /**
   ** Sets the <code>trustedStoreFile</code> attribute for the
   ** <code>Service Provider</code> endpoint if you plan to secure communication
   ** between Identity Manager and the <code>Service Provider</code> endpoint.
   **
   ** @param  value              the <code>trustedStoreFile</code> attribute for
   **                            the <code>Service Provider</code> endpoint.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>AbstractEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T trustedStoreFile(final String value) {
    this.trustedStoreFile = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trustedStoreFile
  /**
   ** Returns the <code>trustedStoreFile</code> attribute for the
   ** <code>Service Provider</code> endpoint if you plan to secure communication
   ** between Identity Manager and the <code>Service Provider</code> endpoint.
   **
   ** @return                    the <code>trustedStoreFile</code> attribute for
   **                            the <code>Service Provider</code> endpoint.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String trustedStoreFile() {
    return this.trustedStoreFile;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trustedStorePassword
  /**
   ** Sets the <code>trustedStorePassword</code> attribute for the
   ** <code>Service Provider</code> endpoint if you plan to secure communication
   ** between Identity Manager and the <code>Service Provider</code> endpoint.
   **
   ** @param  value              the <code>trustedStorePassword</code> attribute
   **                            for the <code>Service Provider</code> endpoint.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>AbstractEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T trustedStorePassword(final String value) {
    this.trustedStorePassword = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trustedStorePassword
  /**
   ** Returns the <code>trustedStorePassword</code> attribute for the
   ** <code>Service Provider</code> endpoint if you plan to secure communication
   ** between Identity Manager and the <code>Service Provider</code> endpoint.
   **
   ** @return                    the <code>trustedStorePassword</code> attribute
   **                            for the <code>Service Provider</code> endpoint.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String trustedStorePassword() {
    return this.trustedStorePassword;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   identityStoreType
  /**
   ** Sets the <code>identityStoreType</code> attribute for the
   ** <code>Service Provider</code> endpoint if you plan to secure communication
   ** between Identity Manager and the <code>Service Provider</code> endpoint.
   **
   ** @param  value              the <code>identityStoreType</code> attribute for
   **                            the <code>Service Provider</code> endpoint.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>AbstractEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T identityStoreType(final String value) {
    this.identityStoreType = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   identityStoreType
  /**
   ** Returns the <code>identityStoreType</code> attribute for the
   ** <code>Service Provider</code> endpoint if you plan to secure communication
   ** between Identity Manager and the <code>Service Provider</code> endpoint.
   **
   ** @return                    the <code>identityStoreType</code> attribute for
   **                            the <code>Service Provider</code> endpoint.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String identityStoreType() {
    return this.identityStoreType;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   identityStoreFile
  /**
   ** Sets the <code>identityStoreFile</code> attribute for the
   ** <code>Service Provider</code> endpoint if you plan to secure communication
   ** between Identity Manager and the <code>Service Provider</code> endpoint.
   **
   ** @param  value              the <code>identityStoreFile</code> attribute for
   **                            the <code>Service Provider</code> endpoint.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>AbstractEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T identityStoreFile(final String value) {
    this.identityStoreFile = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   identityStoreFile
  /**
   ** Returns the <code>identityStoreFile</code> attribute for the
   ** <code>Service Provider</code> endpoint if you plan to secure communication
   ** between Identity Manager and the <code>Service Provider</code> endpoint.
   **
   ** @return                    the <code>identityStoreFile</code> attribute for
   **                            the <code>Service Provider</code> endpoint.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String identityStoreFile() {
    return this.identityStoreFile;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   identityStorePassword
  /**
   ** Sets the <code>identityStorePassword</code> attribute for the
   ** <code>Service Provider</code> endpoint if you plan to secure communication
   ** between Identity Manager and the <code>Service Provider</code> endpoint.
   **
   ** @param  value              the <code>identityStorePassword</code> attribute
   **                            for the <code>Service Provider</code> endpoint.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>AbstractEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T identityStorePassword(final String value) {
    this.identityStorePassword = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   identityStorePassword
  /**
   ** Returns the <code>identityStorePassword</code> attribute for the
   ** <code>Service Provider</code> endpoint if you plan to secure communication
   ** between Identity Manager and the <code>Service Provider</code> endpoint.
   **
   ** @return                    the <code>identityStorePassword</code> attribute
   **                            for the <code>Service Provider</code> endpoint.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String identityStorePassword() {
    return this.identityStorePassword;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   principal
  /**
   ** Sets the <code>principal</code> attribute for the
   ** <code>Service Provider</code> endpoint.
   **
   ** @param  value              the <code>principal</code> attribute for the
   **                            <code>Service Provider</code> endpoint.
   **                            <br>
   **                            Allowed object is {@link Principal}.
   **
   ** @return                    the <code>AbstractEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T principal(final Principal value) {
    this.principal = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   principal
  /**
   ** Returns the <code>principal</code> attribute for the
   ** <code>Service Provider</code> endpoint.
   **
   ** @return                    the <code>principal</code> attribute for the
   **                            <code>Service Provider</code> endpoint.
   **                            <br>
   **                            Possible object is {@link Principal}.
   */
  public final Principal principal() {
    return this.principal;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   principalUsername
  /**
   ** Sets the <code>username</code> attribute for the
   ** <code>Service Provider</code> endpoint.
   ** <p>
   ** Convenience method to shortens the access to the configuration property
   ** {@link Principal#username}.
   **
   ** @param  value              the <code>username</code> attribute for the
   **                            <code>Service Provider</code> endpoint.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>AbstractEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T principalUsername(final String value) {
    // prevent bogus state
    if (this.principal == null)
      this.principal = new Principal();

    this.principal.username = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   principalUsername
  /**
   ** Returns the <code>username</code> attribute for the
   ** <code>Service Provider</code> endpoint.
   ** <p>
   ** Convenience method to shortens the access to the configuration property
   ** {@link Principal#username}.
   **
   ** @return                    the <code>username</code> attribute for the
   **                            <code>Service Provider</code> endpoint.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String principalUsername() {
    // prevent bogus state
    return this.principal == null ? null : this.principal.username;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:    principalPassword
  /**
   ** Sets the <code>password</code> attribute for the
   ** <code>Service Provider</code> endpoint.
   ** <p>
   ** Convenience method to shortens the access to the configuration property
   ** {@link Principal#password}.
   **
   ** @param  value              the <code>password</code> attribute for the
   **                            <code>Service Provider</code> endpoint.
   **                            <br>
   **                            Allowed object is {@link GuardedString}.
   **
   ** @return                    the <code>AbstractEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T principalPassword(final GuardedString value) {
    // prevent bogus state
    if (this.principal == null)
      this.principal = new Principal();

    this.principal.password = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   principalPassword
  /**
   ** Returns the <code>password</code> attribute for the
   ** <code>Service Provider</code> endpoint.
   ** <p>
   ** Convenience method to shortens the access to the configuration property
   ** {@link Principal#password}.
   **
   ** @return                    the <code>password</code> attribute for the
   **                            <code>Service Provider</code> endpoint.
   **                            <br>
   **                            Possible object is {@link GuardedString}.
   */
  public final GuardedString principalPassword() {
    // prevent bogus state
    return this.principal == null ? null : this.principal.password;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localeLanguage
  /**
   ** Sets the <code>language</code> attribute for the
   ** <code>Service Provider</code> endpoint.
   **
   ** @param  value              the <code>language</code> attribute for the
   **                            <code>Service Provider</code> endpoint.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>AbstractEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T localeLanguage(final String value) {
    this.language = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localeLanguage
  /**
   ** Returns the <code>language</code> attribute for the
   ** <code>Service Provider</code> endpoint.
   **
   ** @return                    the <code>language</code> attribute for the
   **                            <code>Service Provider</code> endpoint.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String localeLanguage() {
    return this.language;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localeCountry
  /**
   ** Sets the <code>country</code> attribute for the
   ** <code>Service Provider</code> endpoint.
   **
   ** @param  value              the <code>country</code> attribute for the
   **                            <code>Service Provider</code> endpoint.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>AbstractEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T localeCountry(final String value) {
    this.country = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localeCountry
  /**
   ** Returns the <code>country</code> attribute for the
   ** <code>Service Provider</code> endpoint.
   **
   ** @return                    the <code>country</code> attribute for the
   **                            <code>Service Provider</code> endpoint.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String localeCountry() {
    return this.country;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localeTimeZone
  /**
   ** Sets the <code>timeZone</code> attribute for the
   ** <code>Service Provider</code> endpoint.
   **
   ** @param  value              the <code>timeZone</code> attribute for the
   **                            <code>Service Provider</code> endpoint.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>AbstractEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T localeTimeZone(final String value) {
    this.timeZone = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localeTimeZone
  /**
   ** Returns the <code>timeZone</code> attribute for the
   ** <code>Service Provider</code> endpoint.
   **
   ** @return                    the <code>timeZone</code> attribute for the
   **                            <code>Service Provider</code> endpoint.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String localeTimeZone() {
    return this.timeZone;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   timeOutConnect
  /**
   ** Sets the timeout period for establishment of the connection.
   ** <p>
   ** This property affects the TCP timeout when opening a connection to a
   ** <code>Service Provider</code> endpoint. When connection pooling has been
   ** requested, this property also specifies the maximum wait time or a
   ** connection when all connections in pool are in use and the maximum pool
   ** size has been reached.
   ** <p>
   ** If this property has not been specified, the Service Client will wait
   ** indefinitely for a pooled connection to become available, and to wait
   ** for the default TCP timeout to take effect when creating a new
   ** connection.
   **
   ** @param  value              the timeout to establish a connection.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the <code>AbstractEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T timeOutConnect(final int value) {
    this.timeOutConnect = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   timeOutConnect
  /**
   ** Returns the timeout period for establishment of the connection.
   ** <p>
   ** This property affects the TCP timeout when opening a connection to a
   ** <code>Service Provider</code> endpoint. When connection pooling has been
   ** requested, this property also specifies the maximum wait time or a
   ** connection when all connections in pool are in use and the maximum pool
   ** size has been reached.
   ** <p>
   ** If this property has not been specified, the content provider will wait
   ** indefinitely for a pooled connection to become available, and to wait for
   ** the default TCP timeout to take effect when creating a new connection.
   **
   ** @return                    the timeout to establish a connection.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public final int timeOutConnect() {
    return this.timeOutConnect;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   retryCount
  /**
   ** Sets the number of consecutive attempts to be made at establishing a
   ** connection with the <code>Service Provider</code> endpoint.
   **
   ** @param  value              the number of consecutive attempts to be made
   **                            at establishing a connection with the Service
   **                            Provider endpoint.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the <code>AbstractEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T retryCount(final int value) {
    this.retryCount = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   retryCount
  /**
   ** Returns the number of consecutive attempts to be made at establishing a
   ** connection with the <code>Service Provider</code> endpoint.
   **
   ** @return                    the number of consecutive attempts to be made
   **                            at establishing a connection with the Service
   **                            Provider endpoint.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public final int retryCount() {
    return this.retryCount;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   retryInterval
  /**
   ** Sets the interval (in milliseconds) between consecutive attempts at
   ** establishing a connection with the <code>Service Provider</code> endpoint.
   **
   ** @param  value              the interval (in milliseconds) between
   **                            consecutive attempts at establishing a
   **                            connection with the
   **                            <code>Service Provider</code> endpoint.
   **                            <br>
   **                            Possible object is <code>int</code>.
   **
   ** @return                    the <code>AbstractEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T retryInterval(final int value) {
    this.retryInterval = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   retryInterval
  /**
   ** Returns the interval (in milliseconds) between consecutive attempts at
   ** establishing a connection with the <code>Service Provider</code> endpoint.
   **
   ** @return                    the interval (in milliseconds) between
   **                            consecutive attempts at establishing a
   **                            connection with the
   **                            <code>Service Provider</code> endpoint.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public final int retryInterval() {
    return this.retryInterval;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   timeOutResponse
  /**
   ** Sets the timeout period the Service Client doesn't get a response.
   ** <p>
   ** When an service request is made by a client to a server and the server
   ** does not respond for some reason, the client waits forever for the
   ** server to respond until the TCP timeouts. On the client-side what the
   ** user experiences is esentially a process hang. In order to control the
   ** service request in a timely manner, a read timeout can be configured for
   ** the service provider.
   ** <p>
   ** If this property is not specified, the default is to wait for the
   ** response until it is received.
   **
   ** @param  value              timeout period the Service Client will wait
   **                            for a response.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the <code>AbstractEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T timeOutResponse(final int value) {
    this.timeOutResponse = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   timeOutResponse
  /**
   ** Returns the timeout period the Service Client doesn't get a response.
   ** <p>
   ** When an service request is made by a client to a server and the server
   ** does not respond for some reason, the client waits forever for the
   ** server to respond until the TCP timeouts. On the client-side what the
   ** user experiences is esentially a process hang. In order to control the
   ** service request in a timely manner, a read timeout can be configured for
   ** the service provider.
   ** <p>
   ** If this property is not specified, the default is to wait for the
   ** response until it is received.
   **
   ** @return                    timeout period the Service Client will wait
   **                            for a response.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public final int timeOutResponse() {
    return this.timeOutResponse;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   timestampFormat
  /**
   ** Sets the format of a timestamp value in target
   ** <code>Service Provider</code>.
   **
   ** @param  value              the format of a timestamp value in target
   **                            <code>Service Provider</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>AbstractEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T timestampFormat(final String value) {
    this.timestampFormat = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   timestampFormat
  /**
   ** Returns the format of a timestamp value in target
   ** <code>Service Provider</code>.
   ** <p>
   ** Method isn't defined as final to allow subclasses to override method to
   ** return default values.
   **
   ** @return                    the format of a timestamp value in target
   **                            <code>Service Provider</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String timestampFormat() {
    return this.timestampFormat;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryIdentifierAttribute
  /**
   ** Set the name of the identifier attribute of an
   ** <code>Service Provider</code> entry.
   ** <br>
   ** <b>Note</b>:
   ** <br>
   ** Usually the attribute configure is transferred via the <code>Uid</code>
   ** instance of a connector object.
   **
   ** @param  value              the name of the identifier attribute of an
   **                            <code>Service Provider</code> entry.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>AbstractEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T entryIdentifierAttribute(final String value) {
    this.identifier = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryIdentifierAttribute
  /**
   ** Returns the name of the identifier attribute of an
   ** <code>Service Provider</code> entry.
   ** <br>
   ** <b>Note</b>:
   ** <br>
   ** Usually the attribute configure is transferred via the <code>Uid</code>
   ** instance of a connector object.
   ** <p>
   ** Method isn't defined as final to allow subclasses to override method to
   ** return default values.
   **
   ** @return                    the name of the identifier attribute of an
   **                            <code>Service Provider</code> entry.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String entryIdentifierAttribute() {
    return this.identifier;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryUniqueNameAttribute
  /**
   ** Set the name of the identifier attribute of an
   ** <code>Service Provider</code> entry.
   ** <br>
   ** <b>Note</b>:
   ** <br>
   ** Usually the attribute configure is transferred via the <code>Uid</code>
   ** instance of a connector object.
   **
   ** @param  value              the name of the identifier attribute of an
   **                            <code>Service Provider</code> entry.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>AbstractEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T entryUniqueNameAttribute(final String value) {
    this.uniqueName = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryUniqueNameAttribute
  /**
   ** Returns the name of the unique name attribute of an
   ** <code>Service Provider</code> entry.
   ** <br>
   ** <b>Note</b>:
   ** <br>
   ** Usually the attribute configure is transferred via the <code>Name</code>
   ** instance of a connector object.
   ** <p>
   ** Method isn't defined as final to allow subclasses to override method to
   ** return default values.
   **
   ** @return                    the name of the unique name attribute of an
   **                            <code>Service Provider</code> entry.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String entryUniqueNameAttribute() {
    return this.uniqueName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryPasswordAttribute
  /**
   ** Set the name of the password attribute of an
   ** <code>Service Provider</code> entry.
   **
   ** @param  value              the name of the password attribute of an
   **                            <code>Service Provider</code> entry.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>AbstractEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T entryPasswordAttribute(final String value) {
    this.password = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryPasswordAttribute
  /**
   ** Returns the name of the password attribute of an
   ** <code>Service Provider</code> entry.
   ** <p>
   ** Method isn't defined as final to allow subclasses to override method to
   ** return default values.
   **
   ** @return                    the name of the password attribute of an
   **                            <code>Service Provider</code> entry.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String entryPasswordAttribute() {
    return this.password;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryStatusAttribute
  /**
   ** Set the name of the status attribute of an
   ** <code>Service Provider</code> entry.
   **
   ** @param  value              the name of the status attribute of an
   **                            <code>Service Provider</code> entry.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>AbstractEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T entryStatusAttribute(final String value) {
    this.status = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryStatusAttribute
  /**
   ** Returns the name of the status attribute of an
   ** <code>Service Provider</code> entry.
   ** <p>
   ** Method isn't defined as final to allow subclasses to override method to
   ** return default values.
   **
   ** @return                    the name of the status attribute of an
   **                            <code>Service Provider</code> entry.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String entryStatusAttribute() {
    return this.status;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryCreatorAttribute
  /**
   ** Set the name of the attribute to detect the creator name of an
   ** <code>Service Provider</code> entry.
   **
   ** @param  value              the name of the attribute to detect the
   **                            creator name of an
   **                            <code>Service Provider</code> entry.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>AbstractEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T entryCreatorAttribute(final String value) {
    this.creator = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryCreatorAttribute
  /**
   ** Returns the name of the attribute to detect the creator name of an
   ** <code>Service Provider</code> entry.
   ** <p>
   ** Method isn't defined as final to allow subclasses to override method to
   ** return default values.
   **
   ** @return                    the name of the attribute to detect the
   **                            creator name of an
   **                            <code>Service Provider</code> entry.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String entryCreatorAttribute() {
    return this.creator;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryCreatedAttribute
  /**
   ** Set the name of the attribute to detect the created timestamp of an
   ** <code>Service Provider</code> entry.
   **
   ** @param  value              the name of the attribute to detect the
   **                            created timestamp of an
   **                            <code>Service Provider</code> entry.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>AbstractEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T entryCreatedAttribute(final String value) {
    this.created = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryCreatedAttribute
  /**
   ** Returns the name of the attribute to detect the created timestamp of an
   ** <code>Service Provider</code> entry.
   ** <p>
   ** Method isn't defined as final to allow subclasses to override method to
   ** return default values.
   **
   ** @return                    the name of the attribute to detect the
   **                            created timestamp of an
   **                            <code>Service Provider</code> entry.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String entryCreatedAttribute() {
    return this.created;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryModifierAttribute
  /**
   ** Set the name of the attribute to detect the modifier name of an
   ** <code>Service Provider</code> entry.
   **
   ** @param  value              the name of the attribute to detect the
   **                            modifier name of an
   **                            <code>Service Provider</code> entry.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>AbstractEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T entryModifierAttribute(final String value) {
    this.modifier = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryModifierAttribute
  /**
   ** Returns the name of the attribute to detect the modifier name of an
   ** <code>Service Provider</code> entry.
   ** <p>
   ** Method isn't defined as final to allow subclasses to override method to
   ** return default values.
   **
   ** @return                    the name of the attribute to detect the
   **                            modifier name of an
   **                            <code>Service Provider</code> entry.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String entryModifierAttribute() {
    return this.modifier;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryModifiedAttribute
  /**
   ** Set the name of the attribute to detect the modified timestamp of an
   ** <code>Service Provider</code> entry.
   **
   ** @param  value              the name of the attribute to detect the
   **                            modified timestamp an
   **                            <code>Service Provider</code> entry.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>AbstractEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T entryModifiedAttribute(final String value) {
    this.modified = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryModifiedAttribute
  /**
   ** Returns the name of the attribute to detect the modified timestamp of an
   ** <code>Service Provider</code> entry.
   ** <p>
   ** Method isn't defined as final to allow subclasses to override method to
   ** return default values.
   **
   ** @return                    the name of the attribute to detect the
   **                            modified timestamp an
   **                            <code>Service Provider</code> entry.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String entryModifiedAttribute() {
    return this.modified;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serviceURL
  /**
   ** Return the server part of an url, <em>protocol</em>://host:port.
   **
   ** @param  protocol           the protocol of the url to build.
   **                            Allowed object is {@link String}.
   **
   ** @return                    the server part of the an url,
   **                            <em>protocol</em>://host:port
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String serviceURL(final String protocol) {
    return this.primary.serviceURL(protocol);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addFailover
  /**
   ** Adds the specified {@link Server} definition to the
   **
   ** @param  serverHost         the host name or IP address of the target
   **                            system on which the
   **                            <code>Service Provider</code> is deployed.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  serverPort         the port the <code>Service Provider</code> is
   **                            listening on.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  public final void addFailover(final String serverHost, final int serverPort) {
    addFailover(new Server(serverHost, serverPort));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addFailover
  /**
   ** Adds the specified {@link Server} properties to the collection of
   ** failover instances for the primary server.
   **
   ** @param  server             the server properties to add to the collection
   **                            of failover instances for the primary server..
   **                            <br>
   **                            Allowed object is {@link Server}.
   */
  public final void addFailover(final Server server) {
    // prevent bogus input
    if (server == null)
      return;
    
    // prevent bogus state
    if (this.failover == null)
      this.failover = new ArrayList<Server>();
    
    this.failover.add(server);
  }
}