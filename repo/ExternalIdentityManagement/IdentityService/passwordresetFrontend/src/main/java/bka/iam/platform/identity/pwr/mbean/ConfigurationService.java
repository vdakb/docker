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

    -----------------------------------------------------------------------

    System      :   Identity Governance Extension
    Subsystem   :   Password Reset Administration

    File        :   ConfigurationService.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ConfigurationService.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2019-03-03  DSteding    First release version
*/

package bka.iam.platform.identity.pwr.mbean;

import bka.iam.platform.identity.pwr.type.Configuration;
import bka.iam.platform.identity.pwr.type.Serializer;
import bka.iam.platform.identity.pwr.type.Subnet;
import bka.iam.platform.identity.resources.ServiceBundle;
import oracle.as.jmx.framework.util.RuntimeMBeanSupport;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

////////////////////////////////////////////////////////////////////////////////
// class ConfigurationService
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** MXBean implementation for managing Access Manager Credential Collector
 ** configuration.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ConfigurationService extends    RuntimeMBeanSupport
                                  implements ConfigurationServiceMXBean {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String FILE    = "pwr-config.xml";
  private static final String CONFIG  = System.getProperty("oracle.domain.config.dir");
  private static final File   PATH    = new File(String.format("%s%c%s", CONFIG, File.separatorChar, FILE));

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final Configuration storage;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ConfigurationService</code> backing bean that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ConfigurationService() {
    // ensure inheritance
    super(BUNDLE);

    // initialize instance attributes
    this.storage = Configuration.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   storage
  /**
   ** Returns the configuration instance this MBean manage.
   **
   ** @return                    the configuration instance this MBean manage.
   **                            Possible object is {@link Configuration}.
   */
  public Configuration storage() {
    return this.storage;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   save (ConfigurationServiceMXBean)
  /**
   ** Save the configuration to the file system.
   */
  @Override
  public final void save() {
    try {
      Serializer.marshal(this.storage, PATH);
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refresh (ConfigurationServiceMXBean)
  /**
   ** Refresh the configuration from the fileystem.
   */
  @Override
  public final void refresh() {
    // remove any settings
    this.storage.reset();
    try {
      Serializer.unmarshal(this.storage, PATH.toURI().toURL());
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   locationList (ConfigurationServiceMXBean)
  /**
   ** List the locations actually stored the configuration.
   **
   ** @return                    the locations actually stored the
   **                            configuration.
   */
  @Override
  public final List<Map<String, String>> locationList() {
    final List<Map<String, String>> data = new ArrayList<Map<String, String>>();
    for (Map.Entry<Subnet, String> entry : this.storage.network().entrySet()) {
      final Map<String, String> row = new LinkedHashMap<String, String>();
      row.put("cidr",   entry.getKey().cidr());
      row.put("symbol", entry.getValue());
      data.add(row);
    }
    return data;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   locationRegister
  /**
   ** Regsiters a location in the configuration.
   **
   ** @param  cidr               a CIDR-notation string, e.g. "192.168.0.1/16".
   **                            <br>
   **                            Allowed object {@link String}.
   ** @param  symbol             name of a symbol to identify the location by
   **                            end-users.
   **                            <br>
   **                            Allowed object {@link String}.
   */
  public final void locationRegister(final String cidr, final String symbol) {
    final Subnet network = Subnet.build(cidr);
    // verify if the location is not registered in the configuration
    if (this.storage.network().containsKey(network))
      throw new RuntimeException(ServiceBundle.string("pwr.error.register"));

    // set the new location at last in the configuration storage
    this.storage.network().put(Subnet.build(cidr), symbol);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   locationRemove (ConfigurationServiceMXBean)
  /**
   ** List the locations actually stored the configuration.
   **
   ** @return                    the locations actually stored the
   **                            configuration.
   */
  @Override
  public final void locationRemove(final String cidr) {
    final Subnet subject = Subnet.build(cidr);
    if (this.storage.network().containsKey(subject)) {
      this.storage.network().remove(subject);
    }
    else {
      throw new RuntimeException(ServiceBundle.string("pwr.error.remove"));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   locationModify (ConfigurationServiceMXBean)
  /**
   ** Adds a location to the configuration.
   **
   ** @param  cidr               a CIDR-notation string, e.g. "192.168.0.1/16".
   ** @param  symbol             name of a symbol to identify the location by
   **                            end-users.
   */
  @Override
  public final void locationModify(final String cidr, final String symbol) {
    final Subnet subject = Subnet.build(cidr);
    if (this.storage.network().containsKey(subject)) {
      this.storage.network().put(subject, symbol);
    }
    else {
      throw new RuntimeException(ServiceBundle.string("pwr.error.remove"));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   postRegister (overridden)
  /**
   ** Allows this MBean to perform any operations needed after having been
   ** registered in the MBean server.
   ** <p>
   ** Invoked by the MBean server.
   **
   ** @param  registrationDone   indicates whether or not the MBean has been
   **                            successfully registered in the MBean server.
   **                            The value <code>false</code> means that the
   **                            registration phase has failed.
   */
  @Override
  public void postRegister(final Boolean registrationDone) {
    if (registrationDone) {
      refresh();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   postDeregister (overridden)
  /**
   ** Allows this MBean to perform any operations needed after having been
   ** unregistered in the MBean server.
   ** <p>
   ** Invoked by the MBean server.
   */
  @Override
  public void postDeregister() {
    save();
  }
}