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

    File        :   FileSystemLocator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    FileSystemLocator.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.connector.integration;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import java.io.File;

import java.net.URL;
import java.net.MalformedURLException;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskException;

import org.identityconnectors.framework.api.ConnectorInfo;
import org.identityconnectors.framework.api.ConnectorFacade;
import org.identityconnectors.framework.api.APIConfiguration;
import org.identityconnectors.framework.api.ConnectorInfoManager;
import org.identityconnectors.framework.api.ConfigurationProperty;
import org.identityconnectors.framework.api.ConnectorFacadeFactory;
import org.identityconnectors.framework.api.ConfigurationProperties;
import org.identityconnectors.framework.api.ConnectorInfoManagerFactory;

import org.identityconnectors.common.pooling.ObjectPoolConfiguration;

////////////////////////////////////////////////////////////////////////////////
// class FileSystemLocator
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** Factory for creating {@link ConnectorFacade} instance which uses local
 ** file system.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class FileSystemLocator {

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  private static File LOCATION;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>FileSystemLocator</code>.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new BundleLocator()" and enforces use of the public method below.
   */
  private FileSystemLocator() {
    // should never be instantiated
    throw new UnsupportedOperationException();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   location
  /**
   ** Sets bundle directory. For testing purposes only.
   **
   ** @param  directory          the path to bundle directory.
   **                            <br>
   **                            Allowed object is {@link File}.
   */
  public static void location(final File directory) {
    LOCATION = directory;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Returns the {@link ConnectorFacade} based on configuration map passed.
   **
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
  public static ConnectorFacade create(final TargetResource resource, final TargetFeature feature)
    throws TaskException {

    // prevent bogus input
    if (resource == null)
      throw TaskException.argumentIsNull("resource");

    // prevent bogus input
    if (feature == null)
      throw TaskException.argumentIsNull("fetaure");

    ConnectorInfoManager manager   = ConnectorInfoManagerFactory.getInstance().getLocalManager(inventory());
    ConnectorInfo        connector = manager.findConnectorInfo(feature.token);
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
      final String lookup = registry.get(name);
      if (StringUtility.isEmpty(lookup))
        throw FrameworkException.optionMapping(name);

      final ConfigurationProperty property = properties.getProperty(name);
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
  // Method:   inventory
  /**
   ** Returns the file {@link URL}s for all registered connector bundles.
   **
   ** @return                    the array containing the file {@link URL}s for
   **                            all registered connector bundles.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link URL}.
   */
  private static URL[] inventory()
    throws TaskException {

    final List<URL> collector = new ArrayList<URL>();
    try {
      for (final File file : LOCATION.listFiles()) {
        if (file.isFile()) {
		  	  // convert file to URI --> URI to URL
			    collector.add(file.toURI().toURL());
        }
      }
    }
    catch (MalformedURLException  e) {
      throw TaskException.general(e);
    }
    return collector.toArray(new URL[0]);
  }
}
