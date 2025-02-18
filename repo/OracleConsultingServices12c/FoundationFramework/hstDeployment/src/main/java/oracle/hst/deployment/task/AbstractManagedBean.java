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

    File        :   AbstractManagedBean.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractManagedBean.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment.task;

import oracle.hst.deployment.type.JMXManagedService;

import oracle.hst.deployment.spi.AbstractInvocationHandler;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractManagedBean
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** Invokes the Runtime JMX Bean in Oracle WebLogic Server.
 ** <p>
 ** <b>Note</b>:
 ** Class needs to be declared <code>public</code> to allow ANT introspection.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class AbstractManagedBean extends AbstractServiceTask {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The default MBean server returned to the connection context is
   ** {@link #DOMAIN_SERVER}.
   ** <p>
   ** If it's required to switch to a different MBean server
   ** {@link #setService(Service)} can be used to set the MBean server to use.
   */
  private String runtimeServer = AbstractInvocationHandler.DOMAIN_SERVER;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractManagedBean</code> Ant task that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected AbstractManagedBean() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setService
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>service</code>.
   **
   ** @param  service            the category of the service in Oracle WebLogic
   **                            Server to handle.
   */
  public void setService(final JMXManagedService service) {
    if (JMXManagedService.DOMAIN.equals(service.value()))
      this.runtimeServer = AbstractInvocationHandler.DOMAIN_SERVER;
    else if (JMXManagedService.RUNTIME.equals(service.value()))
      this.runtimeServer = AbstractInvocationHandler.RUNTIME_SERVER;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   runtimeService (AbstactServiceTask)
  /**
   ** Returns the name of the runtime service to connect to.
   **
   ** @return                    the name of the runtime service to connect to.
   */
  @Override
  protected final String runtimeService() {
    return this.runtimeServer;
  }
}