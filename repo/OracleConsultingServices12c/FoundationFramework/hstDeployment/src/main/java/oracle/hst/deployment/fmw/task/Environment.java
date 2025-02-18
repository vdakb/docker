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

    File        :   Environment.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Environment.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment.fmw.task;

import javax.management.MBeanServerConnection;

import org.apache.tools.ant.BuildException;

import oracle.hst.deployment.ServiceException;

import oracle.hst.deployment.task.AbstractManagedBean;

import oracle.hst.deployment.spi.EnvironmentHandler;

import oracle.hst.deployment.fmw.type.Bean;
import oracle.hst.deployment.fmw.type.Domain;

////////////////////////////////////////////////////////////////////////////////
// abstract class Environment
// ~~~~~~~~  ~~~~~ ~~~~~~~~~~
/**
 ** Invokes the Runtime JMX Bean to configure Oracle WebLogic Domain metadata.
 ** <p>
 ** <b>Note</b>:
 ** Class needs to be declared <code>public</code> to allow ANT introspection.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class Environment extends AbstractManagedBean {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final EnvironmentHandler handler;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Actual
  // ~~~~~ ~~~~~~
  /**
   ** Invokes the Runtime JMX Bean to reports the configured actual values.
   ** <p>
   ** Writes the <code>JMX ObjectName</code> report containing the actual values
   ** for all beans fetched through the {@link MBeanServerConnection} to the
   ** associated log.
   */
  public static class Actual extends Environment {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Actual</code> task that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Actual() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: onExecution (AbstractTask)
    /**
     ** Called by the project to let the task do its work.
     ** <p>
     ** This method may be called more than once, if the task is invoked more
     ** than once. For example, if target1 and target2 both depend on target3,
     ** then running "ant target1 target2" will run all tasks in target3 twice.
     **
     ** @throws ServiceException   if something goes wrong with the build
     */
    @Override
    public void onExecution()
      throws ServiceException {

      this.handler.actual(this.connection());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Compare
  // ~~~~~ ~~~~~~~
  /**
   ** Invokes the Runtime JMX Bean to reports the configured actual and target
   ** values.
   ** <p>
   ** Writes the <code>JMX ObjectName</code> report containing the actual values
   ** for all beans fetched through the {@link MBeanServerConnection} to the
   ** associated log.
   */
  public static class Compare extends Environment {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Compare</code> task that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Compare() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: onExecution (AbstractTask)
    /**
     ** Called by the project to let the task do its work.
     ** <p>
     ** This method may be called more than once, if the task is invoked more
     ** than once. For example, if target1 and target2 both depend on target3,
     ** then running "ant target1 target2" will run all tasks in target3 twice.
     **
     ** @throws ServiceException   if something goes wrong with the build
     */
    @Override
    public void onExecution()
      throws ServiceException {

      this.handler.compare(this.connection());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Modify
  // ~~~~~ ~~~~~~
  /**
   ** Modifies the <code>JMX ObjectName</code> of all beans through the
   ** {@link MBeanServerConnection}.
   */
  public static class Modify extends Environment {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Modify</code> task that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Modify() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: onExecution (AbstractTask)
    /**
     ** Called by the project to let the task do its work.
     ** <p>
     ** This method may be called more than once, if the task is invoked more
     ** than once. For example, if target1 and target2 both depend on target3,
     ** then running "ant target1 target2" will run all tasks in target3 twice.
     **
     ** @throws ServiceException   if something goes wrong with the build
     */
    @Override
    public void onExecution()
      throws ServiceException {

      this.handler.modify(this.connection());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Invoke
  // ~~~~~ ~~~~~~
  /**
   ** Invokes the <code>JMX ObjectName</code> operations for all beans
   ** through the {@link MBeanServerConnection}.
   */
  public static class Invoke extends Environment {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Invoke</code> task that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Invoke() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: onExecution (AbstractTask)
    /**
     ** Called by the project to let the task do its work.
     ** <p>
     ** This method may be called more than once, if the task is invoked more
     ** than once. For example, if target1 and target2 both depend on target3,
     ** then running "ant target1 target2" will run all tasks in target3 twice.
     **
     ** @throws ServiceException   if something goes wrong with the build
     */
    @Override
    public void onExecution()
      throws ServiceException {

      this.handler.invoke(this.connection());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Environment</code> Ant task that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected Environment() {
    // ensure inheritance
    super();

    // initialize instance
    this.handler = new EnvironmentHandler(this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setLocation
  /**
   ** Sets the name of the location the runtime server has to connect to.
   **
   ** @param  location           the name of the location the runtime server
   **                            has to connect to.
   */
  public final void setLocation(final String location) {
    this.handler.location(location);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDriverType
  /**
   ** Sets the name of the driverType the runtime server has to connect to.
   **
   ** @param  driverType         the name of the driverType the runtime server
   **                            has to connect to.
   */
  public final void setDriverType(final String driverType) {
    this.handler.driverType(driverType);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setApplication
  /**
   ** Sets the name of the application the runtime server has to connect to.
   **
   ** @param  application        the name of the application the runtime server
   **                            has to connect to.
   */
  public final void setApplication(final String application) {
    this.handler.application(application);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setVersion
  /**
   ** Sets the version of the application the runtime server has to connect to.
   **
   ** @param  version            the version of the application the runtime
   **                            server has to connect to.
   */
  public final void setVersion(final String version) {
    this.handler.version(version);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setServer
  /**
   ** Sets the name of the server the runtime server has to connect to.
   **
   ** @param  server             the name of the server the runtime server
   **                            has to connect to.
   */
  public final void setServer(final String server) {
    this.handler.server(server);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDomain
  /**
   ** Sets the name of the MBean domain the runtime server has to connect to.
   **
   ** @param  domain             the name the MBean domain the runtime server
   **                            has to connect to.
   */
  public final void setDomain(final String domain) {
    this.handler.domain(domain);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setType
  /**
   ** Sets the name of the MBean type the runtime server has to connect to.
   **
   ** @param  type               the name the MBean type the runtime server
   **                            has to connect to.
   */
  public final void setType(final String type) {
    this.handler.type(type);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setBean
  /**
   ** Sets the name of the MBean the runtime server has to connect to.
   **
   ** @param  bean               the name the MBean the runtime server has to
   **                            connect to.
   */
  public final void setBean(final String bean) {
    this.handler.bean(bean);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setCamelCase
  /**
   ** Set to <code>true</code> if the name building has to care about how the
   ** Name and Type of the object name to build for this bean has to be
   ** spelled.
   ** <ol>
   **   <li><code>false</code> (the default) advice that the name and type
   **       prefix has to be spelled in lower case letters.
   **   <li><code>true</code> advice that the name and type prefix has to be
   **       spelled in camel case letters.
   ** </ol>
   **
   ** @param  onoff              <code>true</code> advice that the name and
   **                            type prefix has to be spelled in camel case
   **                            letters; otherwise <code>false</code>.
   */
  public final void setCamelCase(final boolean onoff) {
    this.handler.camelCase(onoff);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredDomain
  /**
   ** Call by the ANT deployment to inject the nested "domain" element.
   **
   ** @param  root               the {@link Domain}s to add,
   **
   ** @throws BuildException     if one of the resulting {@link Domain}s are
   **                            already part of this operation.
   */
  public void addConfiguredDomain(final Domain root)
    throws BuildException {

    this.handler.addDomain(root.instance());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredAttribute
  /**
   ** Call by the ANT deployment to inject the argument for nested parameter
   ** <code>attribute</code>.
   **
   ** @param  attribute          the named value pair to add.
   **
   ** @throws BuildException     if the specified name is already part of the
   **                            parameter mapping.
   */
  public void addConfiguredAttribute(final Bean.Attribute attribute)
    throws BuildException {

    this.handler.addAttribute(attribute.name(), attribute.value(), attribute.type());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredProperty
  /**
   ** Call by the ANT deployment to inject the argument for nested parameter
   ** <code>property</code>.
   **
   ** @param  parameter          the named value pair to add.
   **
   ** @throws BuildException     if the specified name is already part of the
   **                            parameter mapping.
   */
  public void addConfiguredProperty(final Bean.Property parameter)
    throws BuildException {

    this.handler.addProperty(parameter.name(), parameter.value(), parameter.type());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredOperation

  /**
   ** Call by the ANT deployment to inject the argument for nested parameter
   ** <code>operation</code>.
   **
   ** @param  operation          the operation to add.
   **
   ** @throws BuildException     if the specified name is already part of the
   **                            parameter mapping.
   */
  public void addConfiguredOperation(final Bean.Operation operation)
    throws BuildException {

    this.handler.addOperation(operation.instance());
  }
}