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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Consulting Services Foundation Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   ConfigurationHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ConfigurationHandler.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-12-09  DSteding    First release version
*/

package oracle.hst.deployment.spi;

import javax.management.ObjectName;
import javax.management.MalformedObjectNameException;

import oracle.hst.deployment.ServiceFrontend;

import oracle.security.jps.mas.mgmt.jmx.util.JpsJmxConstants;

////////////////////////////////////////////////////////////////////////////////
// class ConfigurationHandler
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** Invokes the Runtime JMX Bean to manage configuration stores in Oracle
 ** WebLogic Server domains.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ConfigurationHandler extends AbstractInvocationHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Manages domain configuration data, that is in the file jps-config.xml.
   ** This MBean provides the only way to modify configuration data.
   ** <p>
   ** Update or write operations require server restart to effect changes.
   */
  private static ObjectName   configurationStore;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ConfigurationHandler</code> to initialize the instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   */
  protected ConfigurationHandler(final ServiceFrontend frontend) {
    // ensure inheritance
    super(frontend);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectName (AbstractInvocationHandler)
  /**
   ** Returns the JMX {@link ObjectName} to access the credential store.
   **
   ** @return                    the JMX {@link ObjectName} to access the
   **                            credential store.
   */
  @Override
  protected final ObjectName objectName() {
    return configuration();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configuration
  /**
   ** Returns the JMX {@link ObjectName} to access the configuration store.
   **
   ** @return                    the JMX {@link ObjectName} to access the
   **                            configuration store.
   */
  private static ObjectName configuration() {
    if (configurationStore == null) {
      try {
        // Manages domain configuration data, that is in the file
        // jps-config.xml.
        // This MBean provides the only way to modify configuration data.
        // Update or write operations require server restart to effect changes.
        configurationStore = new ObjectName(JpsJmxConstants.MBEAN_JPS_CONFIG_FUNCTIONAL);
      }
      catch (MalformedObjectNameException e) {
        throw new AssertionError(e.getMessage());
      }
    }
    return configurationStore;
  }
}