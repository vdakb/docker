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

    File        :   JMXManagedService.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    JMXManagedService.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment.type;

import org.apache.tools.ant.types.EnumeratedAttribute;

////////////////////////////////////////////////////////////////////////////////
// class JMXManagedService
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** <code>JMXManagedService</code> defines the attribute restriction on values
 ** that can be passed as the attribute <code>service</code> to an
 ** <code>AbstractManagedBean</code> to instantiate the proper runtime server
 ** bean in a Oracle WebLogic Server Domain.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class JMXManagedService extends EnumeratedAttribute {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Provides a common access point for navigating to all runtime and
   ** configuration MBeans in the domain as well as to MBeans that provide
   ** domain-wide services (such as controlling and monitoring the life cycles
   ** of servers and message-driven EJBs and coordinating the migration of
   ** migratable services).
   ** <p>
   ** This MBean is available only on the Administration Server.
   ** <p>
   ** The javax.management.ObjectName of this MBean is
   ** <pre>
   **   com.bea:Name=DomainRuntimeService,Type=weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean
   ** </pre>
   ** This is the only object name that a JMX client needs to navigate the
   ** hierarchy available from this MBean.
   */
  public static final String DOMAIN  = "domain";

  /**
   ** Provides an entry point for navigating the hierarchy of WebLogic Server
   ** runtime MBeans and active configuration MBeans for the current server.
   ** <p>
   ** Each server instance in a domain provides its own instance of this MBean.
   ** <p>
   ** The javax.management.ObjectName of this MBean is
   ** <pre>
   **  com.bea:Name=RuntimeService,Type=weblogic.management.mbeanservers.runtime.RuntimeServiceMBean
   ** </pre>
   ** This is the only object name that a JMX client needs to navigate the
   ** hierarchy available from this MBean.
   */
  public static final String RUNTIME = "runtime";

  // the names of the exportable/importable object definitions in alphabetical
  // order
  private static final String[] VALUE = { DOMAIN , RUNTIME };

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>JMXManagedService</code> task that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public JMXManagedService() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>JMXManagedService</code> with the specified value.
   **
   ** @param  value              the value for this service.
   **                            Allowed object is {@link String}.
   */
  public JMXManagedService(final String value) {
    // ensure inheritance
    super();

    this.setValue(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the value of the MBean server category.
   **
   ** @return                    the value of the MBean server category.
   **                            Possible object is {@link String}.
   */
  public final String value() {
    return this.getValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getValues (EnumeratedAttribute)
  /**
   ** The only method a subclass needs to implement.
   **
   ** @return                    an array holding all possible values of the
   **                            enumeration. The order of elements must be
   **                            fixed so that indexOfValue(String) always
   **                            return the same index for the same value.
   */
  @Override
  public String[] getValues() {
    return VALUE;
  }
}