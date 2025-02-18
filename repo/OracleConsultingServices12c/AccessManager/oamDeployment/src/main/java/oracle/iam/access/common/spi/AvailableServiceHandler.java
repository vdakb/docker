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

    System      :   Oracle Access Manager Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   AvailableServiceHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AvailableServiceHandler.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.common.spi;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;

import java.util.EnumSet;

import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.MBeanServerConnection;

import javax.management.openmbean.CompositeDataSupport;

import org.apache.tools.ant.BuildException;

import oracle.security.am.admin.config.mgmt.model.BooleanSettings;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceMessage;
import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceOperation;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.hst.foundation.logging.TableFormatter;

import oracle.iam.access.common.FeatureError;
import oracle.iam.access.common.FeatureMessage;
import oracle.iam.access.common.FeatureException;
import oracle.iam.access.common.FeatureResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class AvailableServiceHandler
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>AvailableServiceHandler</code> enables or disables serivices in an
 ** Oracle Access Manager infrastructure.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class AvailableServiceHandler extends DomainRuntimeHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** operation executed to maintain <code>Access Service</code> status */
  static final String OPERATION_APPLY    = "applyBooleanProperty";

  /** operation executed to obtain <code>Access Service</code> status */
  static final String OPERATION_STATUS   = "retrieveBooleanProperty";

  /**
   ** Java method signature passed to the MBean Server to find the appropriate
   ** MBean interface to enable or disable an <code>Access Service</code>.
   */
  static final String[] SIGNATURE_APPLY  = {
    String.class.getName()          //  0: path
  , BooleanSettings.class.getName() //  1: value
  };

  /**
   ** Java method signature passed to the MBean Server to find the appropriate
   ** MBean interface to print an <code>Access Service</code> status.
   */
  static final String[] SIGNATURE_STATUS = {
    String.class.getName()         //  0: path
  };

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final List<AvailableServiceType>     type     = new ArrayList<AvailableServiceType>();
  private final List<AvailableServiceInstance> multiple = new ArrayList<AvailableServiceInstance>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>AvailableServiceHandler</code> to initialize the
   ** instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   ** @param  operation          the {@link ServiceOperation} to execute either
   **                            <ul>
   **                              <li>{@link ServiceOperation#print}
   **                              <li>{@link ServiceOperation#enable}
   **                              <li>{@link ServiceOperation#disable}
   **                            </ul>
   */
  public AvailableServiceHandler(final ServiceFrontend frontend, final ServiceOperation operation) {
    // ensure inheritance
    super(frontend, operation);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Call by the ANT deployment to inject the argument for adding an instance.
   **
   ** @param  instance           the instance to add.
   **
   ** @throws BuildException     if this instance is already referencing an
   **                            {@link AvailableServiceInstance} with the same
   **                            name.
   */
  public void add(final AvailableServiceInstance instance)
    throws BuildException {

    // prevent bogus input
    if (instance == null)
      throw new BuildException(FeatureResourceBundle.format(FeatureError.INSTANCE_MANDATORY, FeatureResourceBundle.string(FeatureMessage.ACCESS_SERVICE)));

    // prevent bogus state
    if (this.flatten.contains(instance.name()))
      throw new BuildException(FeatureResourceBundle.format(FeatureError.INSTANCE_ONLYONCE, FeatureResourceBundle.string(FeatureMessage.ACCESS_SERVICE), instance.name()));

    // register the instance name for later validations
    this.flatten.add(instance.name());
    // add the instance to the collection to handle
    this.multiple.add(instance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (overridden)
  /**
   ** The entry point to validate the task to perform.
   **
   ** @throws BuildException     in case a validation error does occur.
   */
  @Override
  public void validate()
    throws BuildException {

    // validate each of the assigned instances
    for (AvailableServiceInstance cursor : this.multiple)
      cursor.validate();

    // ensure inheritance
    super.validate();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enable
  /**
   ** Called by the project to let the task do its work to enable an
   ** <code>Access Service</code>.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @param  connection         the {@link MBeanServerConnection} this task
   **                            will use to do the work.
   **
   ** @throws ServiceException   if something goes wrong with the build
   */
  public void enable(final MBeanServerConnection connection)
    throws ServiceException {

    // prevent bogus input
    if (connection == null)
      throw new NullPointerException("connection");

    // latch the connection to use for further processing in methods
    // called afterwards by this method
    this.connection = connection;
    for (AvailableServiceInstance cursor : this.multiple) {
      enable(cursor);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disable
  /**
   ** Called by the project to let the task do its work to disable an
   ** <code>Access Service</code>.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @param  connection         the {@link MBeanServerConnection} this task
   **                            will use to do the work.
   **
   ** @throws ServiceException   if something goes wrong with the build
   */
  public void disable(final MBeanServerConnection connection)
    throws ServiceException {

    // prevent bogus input
    if (connection == null)
      throw new NullPointerException("connection");

    // latch the connection to use for further processing in methods
    // called afterwards by this method
    this.connection = connection;
    for (AvailableServiceInstance cursor : this.multiple) {
      disable(cursor);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   status
  /**
   ** Called by the project to let the task do its work to print the status of
   ** <code>Access Service</code>s.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @param  connection         the {@link MBeanServerConnection} this task
   **                            will use to do the work.
   **
   ** @throws ServiceException   if something goes wrong with the build
   */
  public void status(final MBeanServerConnection connection)
    throws ServiceException {

    // prevent bogus input
    if (connection == null)
      throw new NullPointerException("connection");

    // latch the connection to use for further processing in methods
    // called afterwards by this method
    this.connection = connection;
    // if there aren't specific service types requuested put all declared types
    // in the request
    if (this.type.size() == 0) {
      for (AvailableServiceType cursor : EnumSet.allOf(AvailableServiceType.class)) {
        this.type.add(cursor);
      }
    }
    // prepare the formatter
    final TableFormatter formatter = new TableFormatter();
    formatter.header(FeatureResourceBundle.string(FeatureMessage.ACCESS_SERVICE)).header(FeatureResourceBundle.string(FeatureMessage.ACCESS_SERVICE_STATUS));
    for (AvailableServiceType cursor : this.type) {
      formatter.row().column(cursor.id).column(status(cursor.path()));
    }
    formatter.print();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enable
  /**
   ** Enables an <code>Access Service</code> in Oracle Access Manager for the
   ** specified {@link AvailableServiceInstance}.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @param  instance           the <code>Access Service</code>
   **                            {@link AvailableServiceInstance} to enable.
   **
   ** @throws BuildException     if something goes wrong with the operation.
   */
  protected void enable(final AvailableServiceInstance instance) {
    final String[] arguments = { FeatureResourceBundle.string(FeatureMessage.ACCESS_SERVICE), instance.name() };
    warning(ServiceResourceBundle.format(ServiceMessage.OPERATION_ENABLE_BEGIN, arguments));
    try {
      final Object[] parameter = {instance.path(), new BooleanSettings("notused", Boolean.TRUE)};
      invoke(OPERATION_APPLY, parameter, SIGNATURE_APPLY);
      info(ServiceResourceBundle.format(ServiceMessage.OPERATION_ENABLE_SUCCESS, arguments));
    }
    catch (ServiceException e) {
      // the service exception my have a wrapped exception in it which is the
      // interesting part to report
      Throwable cause = e.getCause();
      // verify if an ReflectionException is the cause to handle it properly
      if (cause instanceof ReflectionException) {
       cause = ((ReflectionException)cause).getTargetException();
      }
      final String message = ServiceResourceBundle.format(ServiceError.OPERATION_ENABLE_FAILED, FeatureResourceBundle.string(FeatureMessage.ACCESS_SERVICE), instance.name(), cause.getLocalizedMessage());
      if (failonerror())
        throw new BuildException(message);
      else
        error(message);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disable
  /**
   ** Disables an <code>Access Service</code> in Oracle Access Manager for the
   ** specified {@link AvailableServiceInstance}.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @param  instance           the <code>Access Service</code>
   **                            {@link AvailableServiceInstance} to disable.
   **
   ** @throws BuildException     if something goes wrong with the operation.
   */
  protected void disable(final AvailableServiceInstance instance) {
    final String[] arguments = { FeatureResourceBundle.string(FeatureMessage.ACCESS_SERVICE), instance.name() };
    warning(ServiceResourceBundle.format(ServiceMessage.OPERATION_DISABLE_BEGIN, arguments));
    try {
      final Object[] parameter = {instance.path(), new BooleanSettings("notused", Boolean.FALSE)};
      invoke(OPERATION_APPLY, parameter, SIGNATURE_APPLY);
      info(ServiceResourceBundle.format(ServiceMessage.OPERATION_DISABLE_SUCCESS, arguments));
    }
    catch (ServiceException e) {
      // the service exception my have a wrapped exception in it which is the
      // interesting part to report
      Throwable cause = e.getCause();
      // verify if an ReflectionException is the cause to handle it properly
      if (cause instanceof ReflectionException) {
       cause = ((ReflectionException)cause).getTargetException();
      }
      final String message = ServiceResourceBundle.format(ServiceError.OPERATION_DISABLE_FAILED, FeatureResourceBundle.string(FeatureMessage.ACCESS_SERVICE), instance.name(), cause.getLocalizedMessage());
      if (failonerror())
        throw new BuildException(message);
      else
        error(message);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   status
  /**
   ** Returns the status of an <code>Access Service</code> in Oracle Access
   ** Manager for the specified configuration path.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @param  path               the configuration path of the
   **                            <code>Access Service</code> to query.
   **
   ** @return                    the localized status information.
   **
   ** @throws BuildException     if something goes wrong with the operation.
   */
  protected String status(final String path) {
    final String[] parameter = {path};
    try {
      final Object entity = invoke(OPERATION_STATUS, parameter, SIGNATURE_STATUS);
      if (entity != null) {
        final CompositeDataSupport status = (CompositeDataSupport)entity;
        return FeatureResourceBundle.string((Boolean)status.get("value") ? FeatureMessage.ACCESS_SERVICE_ENABLED : FeatureMessage.ACCESS_SERVICE_DISABLED);
      }
      else {
        return FeatureResourceBundle.string(FeatureMessage.ACCESS_SERVICE_UNKNOWN);
      }
    }
    catch (ServiceException e) {
      // the service exception my have a wrapped exception in it which is the
      // interesting part to report
      Throwable cause = e.getCause();
      // verify if an ReflectionException is the cause to handle it properly
      if (cause instanceof ReflectionException) {
       cause = ((ReflectionException)cause).getTargetException();
      }
      final String message = ServiceResourceBundle.format(ServiceError.OPERATION_REPORT_FAILED, FeatureResourceBundle.string(FeatureMessage.ACCESS_SERVICE), path, cause.getLocalizedMessage());
      if (failonerror())
        throw new BuildException(message);
      else
        return message;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configBean
  /**
   ** Returns the domain configuration runtime bean in a Access Manager WebLogic
   ** Domain.
   **
   ** @param  expression         the query expression to find the configuration
   **                            MBean.
   **
   ** @return                    the domain configuration runtime bean in a
   **                            Access Manager WebLogic Domain.
   **
   ** @throws FeatureException   if the request fails overall with detailed
   **                            information about the reason.
   */
  protected final ObjectName configBean(final String expression)
    throws FeatureException {

    ObjectName bean = null;
    try {
      final ObjectName      service    = new ObjectName(expression);
      final Set<ObjectName> result     = this.connection.queryNames(service, null);
      for (ObjectName cursor : result) {
        final String name = cursor.toString();
        if (name.equals(expression)) {
          bean = new ObjectName(name);
          break;
        }
      }
    }
    catch (Exception e) {
      throw new FeatureException(FeatureError.DOMAIN_QUERY_FAILED, e.getLocalizedMessage());
    }
    return bean;
  }
}