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

    Copyright © 2011. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   SAP R3 System

    File        :   Provider.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Cengiz.Tuztas@oracle.com

    Purpose     :   This file implements the class
                    Provider.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2011-06-10  CTuztas    First release version
*/

package oracle.iam.identity.sap.diagnostic;

import java.util.Map;
import java.util.HashMap;
import java.util.Properties;

import com.sap.conn.jco.ext.DestinationDataProvider;
import com.sap.conn.jco.ext.DestinationDataEventListener;

////////////////////////////////////////////////////////////////////////////////
// class Provider
// ~~~~~ ~~~~~~~~
/**
 ** The class implements the interface {@link DestinationDataProvider} to
 ** provide the properties for a client connection to a remote SAP system.
 ** <p>
 ** Depending on the type of middleware layer loaded, the properties that need
 ** to be provided by the implementation of the {@link DestinationDataProvider}
 ** may vary.
 ** <p>
 ** For MiddlewareJavaRfc, i.e. the default RFC layer, the supported properties
 ** are:
 ** <pre>
 **   jco.client.client                       SAP client
 **   jco.client.user                         Logon user
 **   jco.client.alias_user                   Logon user alias
 **   jco.client.passwd                       Logon password
 **   jco.client.lang                         Logon language
 **   jco.client.saprouter                    SAP router string to use for a system protected by a firewall
 **   jco.client.sysnr                        SAP system number
 **   jco.client.ashost                       SAP application server
 **   jco.client.mshost                       SAP message server
 **   jco.client.msserv                       Optional: SAP message server port to use instead of the default sapms#sysid#
 **   jco.client.gwhost                       Gateway host
 **   jco.client.gwserv                       Gateway service
 **   jco.client.r3name                       System ID of the SAP system
 **   jco.client.group                        Group of SAP application servers
 **   jco.client.tpname                       Program ID of external server program
 **   jco.client.tphost                       Host of external server program
 **   jco.client.type                         Type of remote host 2 = R/2, 3 = R/3, E = External
 **   jco.client.trace                        Enable/disable RFC trace (0 or 1)
 **   jco.client.cpic_trace                   Enable/disable CPIC trace (-1 [take over environment value], 0 no trace, 1,2,3 different amount of trace)
 **   jco.client.use_sapgui                   Start a SAP GUI and associate with the connection. (0 - do not start [default], 1 start GUI, 2 start GUI and hide if not used)
 **   jco.client.codepage                     Initial codepage in SAP notation
 **   jco.client.getsso2                      Get/Don't get a SSO ticket after logon (1 or 0)
 **   jco.client.mysapsso2                    Use the specified SAP Cookie Version 2 as logon ticket
 **   jco.client.x509cert                     Use the specified X509 certificate as logon ticket
 **   jco.client.lcheck                       Enable/Disable logon check at open time, 1 (enable) or 0 (disable)
 **   jco.client.snc_mode                     Secure network connection (SNC) mode, 0 (off) or 1 (on)
 **   jco.client.snc_partnername              SNC partner, e.g. p:CN=R3, O=XYZ-INC, C=EN
 **   jco.client.snc_qop                      SNC level of security, 1 to 9
 **   jco.client.snc_myname                   SNC name. Overrides default SNC partner
 **   jco.client.snc_lib                      Path to library which provides SNC service
 **   jco.client.dsr                          Enable/Disable dsr support (0 or 1)
 **   jco.destination.peak_limit              Maximum number of active connections that can be created for a destination simultaneously
 **   jco.destination.pool_capacity           Maximum number of idle connections kept open by the destination. A value of 0 has the effect that there is no connection pooling.
 **   jco.destination.expiration_time         Time in ms after that the connections hold by the destination can be closed
 **   jco.destination.expiration_check_period Period in ms after that the destination checks the released connections for expiration
 **   jco.destination.max_get_client_time     Max time in ms to wait for a connection, if the max allowed number of connections is allocated by the application
 **   jco.destination.repository_destination  Specifies which destination should be used as repository, i.e. use this destination's repository
 **   jco.destination.repository.user         Optional: If repository destination is not set, and this property is set, it will be used as user for repository calls. This allows using a different user for repository lookups
 **   jco.destination.repository.passwd       The password for a repository user. Mandatory, if a repository user should be used.
 **   jco.destination.repository.snc_mode     Optional: If SNC is used for this destination, it is possible to turn it off for repository connections, if this property is set to 0. Defaults to the value of jco.client.snc_mode
 ** </pre>
 ** The properties build 4 properties groups:
 ** <ul>
 **   <li>user logon properties
 **   <li>configuration for physical connection
 **   <li>SNC configuration
 **   <li>destination configurat
 ** </ul>
 ** There is only one existing instance of the class in a JVM; it is implemented
 ** as singleton.
 */
public class Provider implements DestinationDataProvider {

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  public  static boolean                 register  = false;

  private static Provider                singleton = null;

  private static Map<String, Properties> registry  = new HashMap<String, Properties>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Provider</code>.
   ** <p>
   ** Access modifier private prevents other classes to use
   ** <code>new Provider()</code>.
   */
  private Provider() {
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDestinationProperties (DestinationDataProvider)
  /**
   ** Return a properties object that contains a subset of the supported
   ** properties mentioned above representing the configuration of the given
   ** destination in your destination configuration repository.
   **
   ** @param  destination        ...
   **
   ** @return                    the {@link Properties} for the destination or
   **                            <code>null</code> if the destination was not
   **                            found.
   */
  @Override
  public Properties getDestinationProperties(final String destination) {
    if (registry.containsKey(destination))
      return registry.get(destination);
    throw new RuntimeException("JCo destination not found: " + destination);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   supportsEvents (DestinationDataProvider)
  /**
   ** Returns <code>true</code> if the implementation can support
   ** <code>DestinationDataEvent</code>s that allow a better integration into
   ** the JCo runtime management.
   ** <p>
   ** We cannot support events therefore we returning <code>false</code>.
   **
   ** @return                    whether the implementation supports
   **                            DestinationDataEvents completely.
   */
  @Override
  public boolean supportsEvents() {
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   supportsEvents (DestinationDataProvider)
  /**
   ** This method sets a {@link DestinationDataEventListener} implemented by
   ** JCo that processes the fired events within the JCo runtime.
   **
   ** @param  listener            the {@link DestinationDataEventListener} to
   **                             which configuration events need to be fired.
   */
  @Override
  public void setDestinationDataEventListener(final DestinationDataEventListener listener) {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns the singleton instance of this {@link DestinationDataProvider},
   **
   ** @return                    the singleton instance of this
   **                            {@link DestinationDataProvider}.
   */
  public static synchronized Provider instance() {
    if (singleton == null)
      singleton = new Provider();

    return singleton;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addDestination
  /**
   ** Adds the spefified properties object that contains a subset of the
   ** supported properties mentioned above representing the configuration of the
   ** given destination to the destination configuration repository.
   **
   ** @param  destination        ...
   ** @param  properties         the {@link Properties} for the destination or
   **                            <code>null</code> if the destination was not
   **                            found.
   */
  public void addDestination(final String destination, final Properties properties) {
    synchronized(registry) {
      registry.put(destination, properties);
    }
  }
}