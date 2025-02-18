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

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   SAP/R3 Usermanagement Connector

    File        :   ServerProvider.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ServerProvider.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2015-06-19  DSteding    First release version
*/

package oracle.iam.identity.sap.control;

import java.util.Map;
import java.util.HashMap;
import java.util.Properties;

import com.sap.conn.jco.ext.Environment;
import com.sap.conn.jco.ext.ServerDataProvider;
import com.sap.conn.jco.ext.ServerDataEventListener;

import oracle.iam.identity.sap.service.resource.ConnectionBundle;

////////////////////////////////////////////////////////////////////////////////
// class ServerProvider
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** A Server manages the server configuration.
 ** The JCo runtime will use the registered provider instance to obtain the
 ** server configuration. It is strongly recommended to use a secure persistence
 ** layer to store the server configurations.
 ** <br>
 ** The following properties are supported:
  ** <pre>
 **   jco.server.gwhost                 Gateway service
 **   jco.server.gwhost 	              Gateway host on which the server should be registered
 **   jco.server.gwserv 	              Gateway service, i.e. the port on which a registration can be done
 **   jco.server.progid 	              The program ID with which the registration is done
 **   jco.server.trace 	                Enable/disable RFC trace (0 or 1)
 **   jco.server.saprouter 	            SAP router string to use for a system protected by a firewall
 **   jco.server.max_startup_delay 	    The maximum time (in seconds) between two startup attempts in case of failures
 **   jco.server.repository_destination Client destination from which to obtain the repository
 **   jco.server.connection_count 	    The number of connections that should be registered at the gateway
 **   jco.server.snc_mode 	            Secure network connection (SNC) mode, 0 (off) or 1 (on)
 **   jco.server.snc_qop 	              SNC level of security, 1 to 9
 **   jco.server.snc_myname 	          SNC name of your server. Overrides the default SNC name. Typically something like p:CN=JCoServer, O=ACompany, C=EN
 **   jco.server.snc_lib 	              Path to library which provides SNC service.
 ** </pre>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   3.1.0.0
 */
public class ServerProvider implements ServerDataProvider {

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  private static ServerProvider singleton = instance();

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private ServerDataEventListener listener;
  private Map<String, Properties> properties = new HashMap<String, Properties>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ServerProvider</code>.
   ** <p>
   ** Access modifier private prevents other classes to use
   ** <code>new ServerProvider()</code>.
   */
  private ServerProvider() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDestinationProperties (ServerDataProvider)
  /**
   ** Return a properties object that contains a subset of the supported
   ** properties mentioned above representing the configuration of the given
   ** destination in your destination configuration repository.
   **
   ** @param  server             ...
   **
   ** @return                    the {@link Properties} for the destination or
   **                            <code>null</code> if the destination was not
   **                            found.
   **
   ** @throws RuntimeException   if the server was not found.
   */
  @Override
  public Properties getServerProperties(final String server)
    throws RuntimeException {

    if (this.properties.containsKey(server))
      return this.properties.get(server);
    throw new RuntimeException(ConnectionBundle.format(ConnectionError.SERVER_NOT_FOUND, server));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   supportsEvents (ServerDataProvider)
  /**
   ** Returns <code>true</code> if the implementation can support
   ** <code>DestinationDataEvents</code> that allow a better integration into
   ** the JCo runtime management.
   ** <p>
   ** We cannot support events therefore we returning <code>false</code>.
   **
   ** @return                    whether the implementation supports
   **                            <code>DestinationDataEvents</code> completely.
   */
  @Override
  public boolean supportsEvents() {
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setServerDataEventListener (ServerDataProvider)
  /**
   ** This method sets a {@link ServerDataEventListener} implemented by JCo that
   ** processes the fired events within the JCo runtime.
   **
   ** @param  listener            the {@link ServerDataEventListener} to
   **                             which configuration events need to be fired.
   */
  @Override
  public void setServerDataEventListener(final ServerDataEventListener listener) {
    this.listener = listener;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns the singleton instance of this {@link ServerDataProvider},
   **
   ** @return                    the singleton instance of this
   **                            {@link ServerDataProvider}.
   */
  public static synchronized ServerProvider instance() {
    if (singleton == null) {
      singleton = new ServerProvider();
      Environment.registerServerDataProvider(singleton);
    }

    return singleton;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addServer
  /**
   ** Adds the specified properties object that contains a subset of the
   ** supported properties mentioned above representing the configuration of the
   ** given destination to the destination configuration repository.
   **
   ** @param  server             ...
   ** @param  properties         the {@link Properties} for the server or
   **                            <code>null</code> if the server was not found.
   */
  public void addServer(final String server, final Properties properties) {
    synchronized(this.properties) {
      this.properties.put(server, properties);
    }
  }
}