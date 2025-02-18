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

    File        :   AbstractServiceTask.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractServiceTask.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment.task;

import java.io.IOException;

import javax.management.MBeanServerConnection;

import javax.management.remote.JMXConnector;

import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.Reference;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceMessage;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.hst.deployment.type.ServerContext;
import oracle.hst.deployment.type.JMXServerContext;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractServiceTask
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** This is the abstract base class for Ant JMX mbean tasks.
 ** Implementations of <code>Service</code> inherit its attributes (see below)
 ** for connecting to the JMX MBean server.
 ** <p>
 ** Refer to the user documentation for more information and examples on how to
 ** use this task.
 ** <p>
 ** <b>Note</b>:
 ** Class needs to be declared <code>public</code> to allow ANT introspection.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class AbstractServiceTask extends AbstractTask {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected JMXServerContext      server     = null;
  protected MBeanServerConnection connection = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AbstractServiceTask</code> Ant task that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected AbstractServiceTask() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AbstractServiceTask</code> that use the specified
   ** {@link JMXServerContext} <code>server</code> as the runtime environment.
   **
   ** @param  server             the {@link JMXServerContext} used as the
   **                            runtime environment.
   */
  protected AbstractServiceTask(final JMXServerContext server) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.server = server;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setContextRef
  /**
   ** Call by the ANT kernel to inject the argument for task attribute
   ** <code>server</code> as a {@link Reference} to a declared
   ** {@link JMXServerContext} in the build script hierarchy.
   **
   ** @param  reference          the attribute value converted to a
   **                            {@link JMXServerContext}.
   **
   ** @throws BuildException     if the <code>reference</code> does not meet the
   **                            requirements to be a predefined
   **                            {@link JMXServerContext}.
   */
  public void setContextRef(final Reference reference)
    throws BuildException {

    final Object object = reference.getReferencedObject(this.getProject());
    if (!(object instanceof ServerContext)) {
      final String[] parameter = {reference.getRefId(), JMXServerContext.CONTEXT_TYPE, reference.getRefId(), object.getClass().getName() };
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_REFERENCE_MISMATCH, parameter));
    }
    this.server = (JMXServerContext)object;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   runtimeService
  /**
   ** Returns the name of the runtime service to connect to.
   **
   ** @return                    the name of the runtime service to connect to.
   */
  protected abstract String runtimeService();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connection
  /**
   ** Returns the connection to the {@link MBeanServerConnection}.
   ** <p>
   ** The first time this method is invoked it will aquire the
   ** {@link MBeanServerConnection} from the {@link JMXConnector}.
   ** <p>
   ** Further invocations of this methods will return the same
   ** {@link MBeanServerConnection}.
   **
   ** @return                    the aquired {@link MBeanServerConnection}.
   **
   ** @throws ServiceException   if the {@link MBeanServerConnection} cannot be
   **                            aquired.
   */
  protected final MBeanServerConnection connection()
    throws ServiceException {

    if (this.connection == null)
      try {
        this.connection = this.server.connector().getMBeanServerConnection();
        if (this.connection == null)
          throw new ServiceException(ServiceError.CONTEXT_CONNECTION, this.server.host());
      }
      catch (IOException e) {
        throw new ServiceException(e);
      }

    return this.connection;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (AbstractTask)
  /**
   ** The entry point to validate the task to perform.
   **
   ** @throws BuildException     in case an error does occur.
   */
  @Override
  protected void validate() {
    if (this.server == null)
      handleAttributeError("server");

    this.server.validate();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connected (AbstractTask)
  /**
   ** Returns the state of connection.
   **
   ** @return                    the state of connection.
   */
  @Override
  protected final boolean connected() {
    return this.server.established();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connect (AbstractTask)
  /**
   ** Establish a connection to the JMX server and creates the MBeanServer to
   ** the use during task execution.
   ** <p>
   ** The caller is responsible for invoking this method prior to executing any
   ** other method.
   ** <p>
   ** The environment() method will be invoked prior to this method.
   **
   ** @throws ServiceException   if an error occurs attempting to establish a
   **                            connection.
   */
  @Override
  protected void connect()
    throws ServiceException {

    final String[] parameter = {this.server.host(), this.server.port()};
    info(ServiceResourceBundle.format(ServiceMessage.SERVER_CONNECTING, parameter));
    try {
      this.server.connect(runtimeService());
      info(ServiceResourceBundle.string(ServiceMessage.SERVER_CONNECTED));
    }
    catch (SecurityException e) {
      throw new ServiceException(ServiceError.CONTEXT_ACCESS_DENIED, this.server.principal().name());
    }
    catch (ServiceException e) {
      error(ServiceResourceBundle.format(ServiceError.CONTEXT_CONNECTION, e.getLocalizedMessage()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disconnect (AbstractTask)
  /**
   ** Close a connection to the target system.
   **
   ** @throws ServiceException      in case an error does occur.
   */
  @Override
  protected void disconnect()
    throws ServiceException {

    this.server.disconnect();
  }
}