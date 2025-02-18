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

    File        :   AbstractEndpoint.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractEndpoint.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.core.network;

import java.util.List;

import oracle.hst.platform.core.logging.Loggable;
import oracle.hst.platform.core.logging.AbstractLoggable;

////////////////////////////////////////////////////////////////////////////////
// class AbstractEndpoint
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** The <code>AbstractEndpoint</code> implements the base functionality of an
 ** <code>IT Resource</code>.
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
public class AbstractEndpoint<T extends AbstractEndpoint> extends AbstractLoggable<T>{

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to
   ** specify the host name and port for the primary
   ** <code>Service Provider</code> endpoint.
   */
  protected Server        primary               = null;
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
  protected List<Server>  failover              = null;
  /**
   ** Attribute tag which might be defined on an <code>IT Resource</code> to
   ** specify if the keystore type if you plan to configure SSL to secure
   ** communication between <code>Service Consumer</code> and the
   ** <code>Service Provider</code> endpoint.
   */
  protected String        trustedStoreType      = "JKS";
  /**
   ** Attribute tag which might be defined on an <code>IT Resource</code> to
   ** specify the keystore file if you plan to configure SSL to secure
   ** communication between <code>Service Consumer</code> and the
   ** <code>Service Provider</code> endpoint.
   */
  protected String        trustedStoreFile;
  /**
   ** Attribute tag which might be defined on an <code>IT Resource</code> to
   ** specify the password for the keystore if you plan to configure SSL to
   ** secure communication between <code>Service Consumer</code> and the
   ** <code>Service Provider</code> endpoint.
   */
  protected String        trustedStorePassword;
  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to
   ** specify the user name and password credentials of the
   ** <code>Service Provider</code> endpoint account used to authenticate a 
   ** onnection.
   */
  protected Principal     principal             = null;
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
  protected int           timeOutResponse       = -1;
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
  protected int           timeOutConnect        = -1;
  /**
   ** The number of consecutive attempts to be made at establishing a connection
   ** with the <code>Service Provider</code> endpoint.
   */
  protected int           retryCount            = 3;
  /**
   ** The the interval (in milliseconds) between consecutive attempts at
   ** establishing a connection with the <code>Service Provider</code> endpoint.
   */
  protected int           retryInterval         = 3000;

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
   ** @param  serverSecure       specifies whether or not to use SSL to secure
   **                            communication between Identity Manager and the
   **                            target system.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
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
   **                            Allowed object is array of <code>byte</code>.
   */
  protected AbstractEndpoint(final Loggable loggable, final String serverHost, final int serverPort, final boolean serverSecure, final String principalName, final String principalPassword) {
    // ensure inheritance
    this(loggable, Server.of(serverHost, serverPort, serverSecure), Principal.of(principalName, principalPassword));
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
   ** @param  principal          the security principal properties user to
   **                            authenticate a connection with the Service
   **                            Provider.
   **                            <br>
   **                            Allowed object is {@link Principal}.
   */
  protected AbstractEndpoint(final Loggable loggable, final Server primary, final Principal principal) {
    // ensure inheritance
    super(loggable);

    this.primary   = primary;
    this.principal = principal;
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
   ** @param  secure             specifies whether or not to use SSL to secure
   **                            communication with the
   **                            <code>Service Provider</code> endpoint.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the <code>AbstractEndpoint</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  public final T primary(final String host, final int port, final boolean secure) {
    return primary(Server.of(host, port, secure));
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
   ** Returns the <code>host</code> attribute for the primary
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
   ** Returns the <code>port</code> attribute for the primary
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
  // Method:   primaryPort
  /**
   ** Returns the <code>secure</code> attribute for the primary
   ** <code>Service Provider</code> endpoint.
   ** <p>
   ** Convenience method to shortens the access to the configuration property
   ** {@link Server#secure} of the primary server.
   **
   ** @return                    whether or not to use SSL to secure
   **                            communication with the
   **                            <code>Service Provider</code> endpoint.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean primarySecure() {
    return this.primary.secure;
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
}