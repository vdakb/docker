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

    Copyright © 2024. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Nextcloud Connector

    File        :   TestBaseIntegration.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@icloud.com

    Purpose     :   This file implements the class
                    TestBaseIntegration.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2024-03-22  DSteding    First release version
*/

package oracle.iam.junit.nextcloud.integration;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;

import java.util.stream.Stream;

import java.io.File;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.NoSuchFileException;

import Thor.API.tcResultSet;

import org.identityconnectors.framework.api.ConnectorFacade;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

import oracle.hst.foundation.SystemConsole;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.connector.service.Descriptor;

import oracle.iam.identity.connector.integration.BundleLocator;
import oracle.iam.identity.connector.integration.ServerResource;

import oracle.iam.identity.gws.integration.nextcloud.ServiceFeature;
import oracle.iam.identity.gws.integration.nextcloud.ServiceResource;

import oracle.iam.junit.nextcloud.TestBase;

////////////////////////////////////////////////////////////////////////////////
// class TestBaseIntegration
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>TestBaseIntegration</code> implements the basic functionality of a
 ** Integration Test Cases.
 ** <p>
 ** Implemented by an extra class to keep it outside of the test case classes
 ** itself.
 **
 ** @author  dieter.steding@icloud.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestBaseIntegration extends TestBase {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The console used for logging purpose */
  public static final SystemConsole CONSOLE   = new SystemConsole("gws");
  /** 
   ** The location of the configuration file the Test Cases data are populated
   ** from.
   */
  public static final File          RESOURCES = new File("src/test/resources");
  /**
   ** The location of the configuration file the standard
   ** <code>IT Resource</code> is populated from if the Test Cases are executed
   ** inside of the Intranet.
   */
  public static final File          ENDPOINT  = new File(RESOURCES, "gws/service.conf");
  /**
   ** The location of the connector server configuration in a distributed
   ** network.
   */
  public static final File          SERVER    = new File(RESOURCES, "gws/server.conf");
  /**
   ** The location of the credential file to use
   */
  public static final File          FEATURE   = new File(RESOURCES, "mds/gfn-feature.xml");
  /**
   ** The option a connector service consumer will put in the operation options
   ** of a search operation to configure the size of a batch of resources
   ** returned from a <code>Service Provider</code>.
   */
  public static final String        BATCH_SIZE          = "batchSize";
  /**
   ** The option a connector service consumer will put in the operation options
   ** of a search operation to configure the start index of a batch of resources
   ** returned from a <code>Service Provider</code>.
   */
  public static final String        BATCH_START         = "batchStart";
  /**
   ** The name a connector service consumer will put in the operation options
   ** of a reconciliation process to configure the search base(s) of resources
   ** returned from a <code>Service Provider</code>.
   */
  public static final String        SEARCH_BASE         = "searchBase";
  /**
   ** The name a connector service consumer will put in the operation options
   ** of a reconciliation process to configure the search filter of resources
   ** returned from a <code>Service Provider</code>.
   */
  public static final String        SEARCH_FILTER       = "searchFilter";
  /**
   ** The name a connector service consumer will put in the operation options
   ** of a reconciliation process to configure the search order of resources
   ** returned from a <code>Service Provider</code>.
   */
  public static final String        SEARCH_ORDER        = "searchOrder";
  /**
   ** The value a connector service consumer will put in the operation options
   ** to obtain entries from the Service Provider incrementally means based
   ** on a synchronization token like <code>changeNumber</code> of timestamps.
   */
  public static final String        INCREMENTAL         = "incremental";
  /**
   ** The name a connector service consumer will put in the operation options
   ** of a reconciliation process to configure the synchronization strategy of
   ** resources returned from a <code>Service Provider</code>.
   */
  public static final String        SYNCHRONIZE         = "synchronize";
  /**
   ** The value a connector service consumer will put in the operation options
   ** to specifiy the value of a synchronization token.
   */
  public static final String        SYNCHRONIZE_TOKEN   = "synchronizationToken";

  /**
   ** The default start inedx of a search result obtained from a
   ** <code>Service Provier</code>.
   ** <br>
   ** This value is aligned with the default limits of Nextcloud Server.
   */
  public static final int           BATCH_START_DEFAULT = 0;
  /**
   ** The default size limit of a search result obtained from a
   ** <code>Service Provier</code>.
   ** <br>
   ** This value is aligned with the default limits of Nextcloud Server.
   */
  public static final int           BATCH_SIZE_DEFAULT  = 500;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestBaseIntegration</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestBaseIntegration() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   facade
  /**
   ** Factory method to create a new {@link ConnectorFacade}.
   **
   ** @return                    the {@link ConnectorFacade} to interact with
   **                            the connector bundles.
   **
   ** @throws TaskException      if the connector bundle is not availbale.
   */
  public static ConnectorFacade facade(final ServiceResource resource)
    throws TaskException {

    return BundleLocator.create(server(), resource, ServiceFeature.build(CONSOLE, FEATURE));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   server
  /**
   ** Factory method to create a new {@link ServerResource}.
   ** <br>
   ** The {@link ServerResource} is always running on the same host system.
   **
   ** @return                    the {@link ServerResource} configured for
   **                            intranet.
   **
   ** @throws TaskException      if  one or more attributes are missing on the
   **                            <code>IT Resource</code> instance.
   */
  public static ServerResource server()
    throws TaskException {

    return ServerResource.build(CONSOLE, config(SERVER));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endpoint
  /**
   ** Factory method to create a new {@link ServiceResource}.
   **
   ** @return                    the configured  {@link ServiceResource}.
   **                            <br>
   **                            Possible object is {@link ServiceResource}.
   **
   ** @throws TaskException      if one or more attributes are missing on the
   **                            <code>IT Resource</code> instance.
   */
  public static ServiceResource endpoint()
    throws TaskException {

    return ServiceResource.build(CONSOLE, config(ENDPOINT));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   config
  /**
   ** Load a text file’s content and convert it into a {@link Map}.
   **
   ** @param  path               the absolute abstract path to the file to load
   **                            from the resources.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link Map} providing the configuration
   **                            key-value pairs.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link String} as the value.
   **
   ** @throws TaskException      if an I/O error occurs.
   */
  public static Map<String, String> config(final File path)
    throws TaskException {

    final Map<String, String> map = new HashMap<>();
    try (Stream<String> lines = Files.lines(path.toPath())) {
      lines.filter(
        line -> line.contains(":")).forEach(line -> {
          final String[] pair = line.split(":", 2);
          map.put(pair[0].trim(), pair[1].trim());
        }
      );
      return map;
    }
    catch (NoSuchFileException e) {
      throw TaskException.abort(TaskBundle.format(TaskError.ITRESOURCE_NOT_FOUND, path));
    }
    catch (IOException e) {
      throw TaskException.unhandled(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   failed
  /**
   ** The exception captured provides a special format in the message containing
   ** a error code and a detailed text this message needs to be plitted by a
   ** "::" character sequence
   **
   ** @param  cause              the {@link ConnectorException} thrown from the
   **                            <code>Connector</code>.
   **                            <br>
   **                            Allowed object is {@link ConnectorException}.
   */
  public static void failed(final ConnectorException cause) {
    // the exception thrown provides a special format in the message
    // containing a error code and a detailed text
    failed(cause.getClass().getSimpleName().concat(cause.getLocalizedMessage()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   failed
  /**
   ** The exception captured provides a error code and a detailed message.
   **
   ** @param  cause              the {@link TaskException} thrown from the
   **                            <code>Integration</code>.
   **                            <br>
   **                            Allowed object is {@link TaskException}.
   */
  public static void failed(final TaskException cause) {
    // the exception thrown provides a special format in the message
    // containing a error code and a detailed text
    // this message needs to be split by a "::" character sequence
    failed(cause.getClass().getSimpleName().concat("::").concat(cause.code()).concat("::").concat(cause.getLocalizedMessage()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transfer
  /**
   ** Returns the Identity Manager attributes and their corresponding values
   ** from a process form instance.
   ** <br>
   ** The returning result contains the column names of the process form mapped
   ** to the corresponding values. Only values will be returned that are mapped
   ** by the source attribute name of any {@link Descriptor.Attribute}.
   ** <br>
   ** If a {@link Descriptor.Attribute} is flagged as <code>entitlement</code>
   ** or <code>lookup</code> a potentialy prefixed <code>IT Resource</code> is
   ** removed from the value obtained from the process form.
   **
   ** @param  formData           the {@link tcResultSet} providing acces to a
   **                            certain record populated from a proecess from.
   **                            <br>
   **                            Allowed object is {@link tcResultSet}.
   ** @param  mapping            the {@link Set} of attributes specifying the
   **                            UD_ column names of the process form that are
   **                            part of a provisioning task.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of {@link Descriptor.Attribute}.
   **
   ** @return                    a {@link Map} containing the process data
   **                            mapped to the Identity Manager attribute names.
   **
   ** @throws TaskException      if the operation fails.
   */
  public static Map<String, Object> transfer(final tcResultSet formData, final Set<Descriptor.Attribute> mapping)
    throws TaskException {

    final Map<String, Object> targetData = new HashMap<String, Object>();
    try {
      // gets the attributes that the reference descriptor is trying to
      // provision to the Target System by using the source of the attribute
      // name to obtain the value and put it in the mapping with the target
      // attribute id
      for (final Descriptor.Attribute cursor : mapping) {
        final String value = cursor.entitlement() ? Descriptor.unescapePrefix(formData.getStringValue(cursor.source)) : formData.getStringValue(cursor.source);
        switch(cursor.type) {
          case LONG     : targetData.put(cursor.source, Long.valueOf(value));
                          break;
          case DATE     : 
          case CALENDAR : targetData.put(cursor.source, Long.valueOf(formData.getDate(cursor.source).getTime()));
                          break;
          case BOOLEAN  : targetData.put(cursor.source, Boolean.valueOf(value));
                          break;
          default       : targetData.put(cursor.source, value);
        }
      }
    }
    catch (Exception e) {
      throw TaskException.abort(e);
    }
    return targetData;
  }
}