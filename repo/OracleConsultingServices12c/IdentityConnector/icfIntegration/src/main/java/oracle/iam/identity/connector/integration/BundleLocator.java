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
    Subsystem   :   Connector Bundle Integration

    File        :   BundleLocator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    BundleLocator.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.connector.integration;

import java.util.Set;
import java.util.Map;
import java.util.List;

import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;

import java.io.EOFException;

import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.net.MalformedURLException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;

import org.identityconnectors.common.security.GuardedString;

import org.identityconnectors.common.pooling.ObjectPoolConfiguration;

import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.exceptions.InvalidCredentialException;

import org.identityconnectors.framework.api.ConnectorInfo;
import org.identityconnectors.framework.api.ConnectorFacade;
import org.identityconnectors.framework.api.APIConfiguration;
import org.identityconnectors.framework.api.ConnectorInfoManager;
import org.identityconnectors.framework.api.ConfigurationProperty;
import org.identityconnectors.framework.api.ConnectorFacadeFactory;
import org.identityconnectors.framework.api.ConfigurationProperties;
import org.identityconnectors.framework.api.ConnectorInfoManagerFactory;
import org.identityconnectors.framework.api.RemoteFrameworkConnectionInfo;

import oracle.iam.platform.Platform;

import oracle.iam.connectormgmt.vo.ConnectorBundle;

import oracle.iam.connectormgmt.internal.api.ConnectorBundleStore;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskException;

////////////////////////////////////////////////////////////////////////////////
// class BundleLocator
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** Factory for creating {@link ConnectorFacade} instance which uses local
 ** bundles or remote connector server - based on the configuration.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class BundleLocator {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String PROTOCOL = "oimjar";

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Cache
  // ~~~~~ ~~~~~
  private static enum Cache {
    INSTANCE;

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:4686732261579159417")
    private static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private URL[] urls;
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Connection
  // ~~~~~ ~~~~~~~~~~
  /**
   ** <code>Connection</code> represent a communications link between the
   ** application and a URL. Instances of this class can be used both to read
   ** from and to write to the resource referenced by the URL.
   ** <p>
   ** Loads the speciefied connector bundle URL from OIM backend.
   */
  private static class Connection extends URLConnection {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final URL url;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a {@link URL} connection to the specified URL.
     ** <p>
     ** A connection to the object referenced by the {@link URL} is not created.
     ** <br>
     ** Access modifier private prevents other classes using "new Connection()".
     **
     ** @param  url              the specified URL.
     **                          <br>
     **                          Allowed object is {@link URL}.
     */
    private Connection(final URL url) {
      // ensure inheritance
      super(url);

      // initialize instance attributes
      this.url = url;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: connect (URLConnection)
    /**
     ** Opens a communications link to the resource referenced by this
     ** {@link URL}, if such a connection has not already been established.
     ** <p>
     ** If the <code>connect</code> method is called when the connection has
     ** already been opened (indicated by the <code>connected</code> field having
     ** the value <code>true</code>), the call is ignored.
     ** <p>
     ** <code>OIMConnection</code> objects go through two phases: first they are
     ** created, then they are connected. After being created, and before being
     ** connected, various options can be specified (e.g., doInput and UseCaches).
     ** <br>
     ** After connecting, it is an error to try to set them. Operations that
     ** depend on being connected, like getContentLength, will implicitly perform
     ** the connection, if necessary.
     **
     ** @throws IOException      if the protocol schemes does not match.
     */
    @Override
    public void connect()
      throws IOException {

      final String protocol = this.url.getProtocol();
      if (!StringUtility.isEqual(PROTOCOL, protocol))
        throw new IOException("Invalid protocol " + this.url.getProtocol());
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: getInputStream (overridden)
    /**
     ** Returns an input stream that reads from this open connection.
     ** <p>
     ** A SocketTimeoutException can be thrown when reading from the returned
     ** input stream if the read timeout expires before data is available for
     ** read.
     **
     ** @return                  an input stream that reads from this open
     **                          connection.
     **                          <br>
     **                          Possible object is {@link InputStream}.
     **
     ** @throws IOException      if an I/O error occurs while creating the input
     **                          stream.
     */
    @Override
    public InputStream getInputStream()
      throws IOException {

      final ConnectorBundleStore store  = Platform.getBean(ConnectorBundleStore.class);
      final ConnectorBundle      bundle = store.getConnectorBundle(this.url.getFile());
      return new ByteArrayInputStream(bundle.getBundle());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Handler
  // ~~~~~ ~~~~~~~
  /**
   ** <code>Handler</code> is the stream protocol handlers to handle the oimjar
   ** protocol, used to load connector bundles from OIM backend.
   ** <p>
   ** A stream protocol handler knows how to make a connection for a particular
   ** protocol type, such as <code>http</code>, <code>ftp</code>, or
   ** <code>gopher</code>.
   ** <p>
   ** In most cases, an instance of a <code>Handler</code> subclass is not
   ** created directly by an application. Rather, the first time a protocol
   ** name is encountered when constructing a <code>URL</code>, the appropriate
   ** stream protocol handler is automatically loaded.
   */
  private static class Handler extends URLStreamHandler {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a {@link URLStreamHandler} connection to the specified URL.
     ** <br>
     ** Access modifier private prevents other classes using "new Handler()".
     */
    private Handler() {
      // ensure inheritance
      super();
    }

    //////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    //////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: connect (URLStreamHandler)
    /**
     ** Opens a connection to the object referenced by the {@link URL} argument.
     **
     ** @param  url              the URL that this connects to.
     **                          <br>
     **                          Allowed object is {@link URL}.
     **
     ** @return                  a {@link URLConnection} object for the
     **                          {@link URL}.
     **                          <br>
     **                          Possible object is {@link URLConnection}.
     **
     ** @throws IOException      if an I/O error occurs while opening the
     **                          connection.
     */
    protected URLConnection openConnection(URL url)
      throws IOException {

      return new Connection(url);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>BundleLocator</code>.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new BundleLocator()" and enforces use of the public method below.
   */
  private BundleLocator() {
    // should never be instantiated
    throw new UnsupportedOperationException();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Returns the {@link ConnectorFacade} based on configuration map passed.
   **
   ** @param  endpoint           the {@link ServerResource} containing the
   **                            properties to establish the connection to a
   **                            remote deployed <code>Connector Server</code>.
   **                            <br>
   **                            Allowed object is {@link ServerResource}.
   ** @param  resource           the {@link TargetResource} containing the
   **                            properties to establish the connection to a
   **                            target system.
   **                            <br>
   **                            Allowed object is {@link TargetResource}.
   ** @param  feature            the {@link TargetFeature} containing the
   **                            system specifc properties required to support
   **                            the connection to a target system.
   **                            <br>
   **                            Allowed object is {@link TargetFeature}.
   **
   ** @return                    the {@link ConnectorFacade} object based on
   **                            Bundle Name, Bundle Version, Connector Name.
   **                            <br>
   **                            Possible object is {@link ConnectorFacade}.
   **
   ** @throws TaskException      if not able to find the {@link ConnectorInfo}
   **                            by the implicitly constructed
   **                            <code>ConnectorKey</code> contained in the
   **                            given {@link TargetFeature}
   **                            <code>feature</code>.
   */
  public static ConnectorFacade create(final ServerResource endpoint, final TargetResource resource, final TargetFeature feature)
    throws TaskException {

    // prevent bogus input
    if (resource == null)
      throw TaskException.argumentIsNull("resource");

    // prevent bogus input
    if (feature == null)
      throw TaskException.argumentIsNull("feature");

    ConnectorInfoManager manager   = (endpoint != null) ? remote(endpoint) : local(false);
    ConnectorInfo        connector = manager.findConnectorInfo(feature.token);
    if (connector == null) {
      manager   = local(true);
      connector = manager.findConnectorInfo(feature.token);
    }
    if (connector == null)
      throw FrameworkException.bundleNotFound(feature.token);

    // configure the configuration
    return ConnectorFacadeFactory.getInstance().newInstance(configure(resource, feature, connector));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Configures the API.
   **
   ** @param  resource           the configuration properties of the
   **                            <code>IT Resource</code> to transfer.
   **                            <br>
   **                            Allowed object is {@link TargetResource}.
   ** @param  feature            the extended configuration properties of the
   **                            <code>IT Resource</code> to transfer.
   **                            <br>
   **                            Allowed object is {@link TargetFeature}.
   ** @param  connector          the {@link ConnectorInfo} to be configured.
   **                            <br>
   **                            Allowed object is {@link ConnectorInfo}.
   **
   ** @return                    the {@link APIConfiguration} object based on
   **                            Bundle Name, Bundle Version, Connector Name.
   **                            <br>
   **                            Possible object is {@link APIConfiguration}.
   **
   ** @throws FrameworkException if issues encountered regarding configuration
   **                            options.
   */
  private static APIConfiguration configure(final TargetResource resource, final TargetFeature feature, final ConnectorInfo connector)
    throws FrameworkException {

    final APIConfiguration configuration = connector.createDefaultAPIConfiguration();
    // configure the pool options
    configure(feature, configuration.getConnectorPoolConfiguration());
    // configure the API properties
    configure(resource, feature, configuration.getConfigurationProperties());
    return configuration;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Configures the pool options.
   **
   ** @param  feature            the extended configuration properties of the
   **                            <code>IT Resource</code> to transfer.
   **                            <br>
   **                            Allowed object is {@link TargetFeature}.
   ** @param  pool               the {@link ObjectPoolConfiguration} to be
   **                            configured.
   **                            <br>
   **                            Allowed object is {@link ObjectPoolConfiguration}.
   */
  private static void configure(final TargetFeature feature, final ObjectPoolConfiguration pool) {
    // configure the pool options
    pool.setMaxIdle(feature.poolMaxIdle());
    pool.setMinIdle(feature.poolMinIdle());
    pool.setMaxObjects(feature.poolMaxSize());
    pool.setMaxWait(feature.poolMaxWait());
    pool.setMinEvictableIdleTimeMillis(feature.poolMinWait());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Configures the pool options.
   **
   ** @param  resource           the configuration properties of the
   **                            <code>IT Resource</code> to transfer.
   **                            <br>
   **                            Allowed object is {@link TargetResource}.
   ** @param  feature            the extended configuration properties of the
   **                            <code>IT Resource</code> to transfer.
   **                            <br>
   **                            Allowed object is {@link TargetFeature}.
   ** @param  properties         the {@link ConfigurationProperties} to be
   **                            configured.
   **                            <br>
   **                            Allowed object is
   **                            {@link ConfigurationProperties}.
   **
   ** @throws FrameworkException if issues encountered regarding configuration
   **                            options.
   */
  private static void configure(final TargetResource resource, final TargetFeature feature, final ConfigurationProperties properties)
    throws FrameworkException {

    final List<String>        names    = properties.getPropertyNames();
    final Map<String, String> registry = resource.registry();
    registry.putAll(feature.registry());
    for (String name : names) {
      final String                lookup   = registry.get(name);
      final ConfigurationProperty property = properties.getProperty(name);
      // check if we a to abort
      if (property.isRequired() && StringUtility.isEmpty(lookup))
        throw FrameworkException.optionRequired(name);

      String value = resource.stringValue(lookup);
      if (StringUtility.isEmpty(value))
        value = feature.stringValue(lookup);

      if (StringUtility.isEmpty(value)) {
        if (property.isRequired())
          throw FrameworkException.optionRequired(name);

         resource.debug("configure", FrameworkBundle.format(FrameworkError.CONNECTOR_OPTION_NOTFOUND, name));
      }
      else {
        property.setValue(PropertyTypeUtility.convert(value, property.getType()));
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   local
  /**
   ** Returns one of the available instance of {@link ConnectorInfoManager} from
   ** @link ConnectorInfoManagerFactory}. It maintains the instances for the
   ** metadata (info) of a Connector.
   **
   ** @param  reloadCache        <code>true</code> if you don't want to use the
   **                            cached {@link ConnectorInfoManager}.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    an available instance of
   **                            {@link ConnectorInfoManager}.
   **                            <br>
   **                            Possible object is
   **                            {@link ConnectorInfoManager}.
   */
  private static ConnectorInfoManager local(final boolean reloadCache) {
    synchronized (Cache.INSTANCE) {
      if ((Cache.INSTANCE.urls == null) || (reloadCache)) {
        Cache.INSTANCE.urls = inventory();
      }
    }
    return ConnectorInfoManagerFactory.getInstance().getLocalManager(Cache.INSTANCE.urls);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   remote
  /**
   ** actory method to create the {@link ConnectorInfoManager} for a remote
   ** framework.
   **
   ** @param  endpoint           the {@link ServerResource} containing the
   **                            properties to establish the connection to a
   **                            remotely deployed
   **                            <code>Connector Server</code>.
   **                            <br>
   **                            Allowed object is {@link ServerResource}.
   **
   ** @return                    an available instance of
   **                            {@link ConnectorInfoManager}.
   **                            <br>
   **                            Possible object is
   **                            {@link ConnectorInfoManager}.
   */
  private static synchronized ConnectorInfoManager remote(final ServerResource endpoint)
    throws TaskException {

    // creates a new instance of RemoteFrameworkConnectionInfo.
    final RemoteFrameworkConnectionInfo descriptor = new RemoteFrameworkConnectionInfo(
      endpoint.stringValue(ServerResource.SERVERNAME)
    , endpoint.integerValue(ServerResource.SERVERPORT)
    , new GuardedString(endpoint.stringValue(ServerResource.SECURETOKEN).toCharArray())
    , endpoint.booleanValue(ServerResource.SECURESOCKET, false)
    , null
    , endpoint.integerValue(ServerResource.CONNECTIONTIMEOUT, 0)
    );
    ConnectorInfoManager remoteManager = null;
    try {
      remoteManager = ConnectorInfoManagerFactory.getInstance().getRemoteManager(descriptor);
    }
    catch (InvalidCredentialException e) {
      throw FrameworkException.authentication(e);
    }
    catch (ConnectorException e) {
      Throwable t = e.getCause();
      if (t != null) {
        if ((t instanceof UnknownHostException)) {
          throw FrameworkException.unknownHost(endpoint.serverName(), t);
        }
        else if (t instanceof NoRouteToHostException) {
          throw FrameworkException.createSocket(endpoint.serverName(), endpoint.serverPort());
        }
        else if ((t instanceof ConnectException)) {
          throw FrameworkException.unavailable(endpoint.serverName(), endpoint.serverPort(), t);
        }
        else if ((t instanceof SocketTimeoutException))
          throw FrameworkException.timeout(endpoint.serverName(), endpoint.serverPort(), t);
        else if ((t instanceof EOFException)) {
          throw FrameworkException.createSocket(endpoint.serverName(), endpoint.serverPort());
        }
        else {
          throw FrameworkException.unhandled(e);
        }
      }
      else {
        throw FrameworkException.abort(e);
      }
    }
    return remoteManager;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   inventory
  /**
   ** Returns the file {@link URL}s for all registered connector bundles.
   **
   ** @return                    the array containing the file {@link URL}s for
   **                            all registered connector bundles.
   **                            <br>
   **                            Possible object is array of {@link URL}s.
   */
  private static URL[] inventory() {
    try {
      final ConnectorBundleStore store   = Platform.getBean(ConnectorBundleStore.class);
      final Set<ConnectorBundle> bundles = store.getConnectorBundles();
      final URL[]                urls    = new URL[bundles.size()];
      int i= 0;
      for (ConnectorBundle bundle : bundles)
        urls[i++] = new URL(PROTOCOL, "local", 0, bundle.getName(), new Handler());
      return urls;
    }
    catch (MalformedURLException e) {
      throw new IllegalStateException(e);
    }
  }
}