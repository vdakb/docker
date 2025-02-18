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

    File        :   AccessServerHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AccessServerHandler.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.common.spi;

import java.util.List;
import java.util.ArrayList;

import javax.management.MBeanServerConnection;

import javax.management.ReflectionException;

import org.apache.tools.ant.BuildException;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceMessage;
import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceOperation;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.iam.access.common.FeatureError;
import oracle.iam.access.common.FeatureResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class AccessServerHandler
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** <code>AccessServerHandler</code> creates, deletes and configures Access
 ** Servers in an Oracle Access Manager infrastructure.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class AccessServerHandler extends ApplicationRuntimeHandler {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final List<AccessServerInstance> multiple = new ArrayList<AccessServerInstance>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>AccessServerHandler</code> to initialize the instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   ** @param  operation          the {@link ServiceOperation} to execute either
   **                            <ul>
   **                              <li>{@link ServiceOperation#create}
   **                              <li>{@link ServiceOperation#delete}
   **                              <li>{@link ServiceOperation#modify}
   **                            </ul>
   */
  public AccessServerHandler(final ServiceFrontend frontend, final ServiceOperation operation) {
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
   **                            {@link AccessServerInstance} with the same
   **                            name.
   */
  public void add(final AccessServerInstance instance)
    throws BuildException {

    // prevent bogus input
    if (instance == null)
      throw new BuildException(FeatureResourceBundle.format(FeatureError.INSTANCE_MANDATORY, AccessServerProperty.ENTITY));

    // prevent bogus state
    if (this.flatten.contains(instance.name()))
      throw new BuildException(FeatureResourceBundle.format(FeatureError.INSTANCE_ONLYONCE, AccessServerProperty.ENTITY, instance.name()));

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

    if (this.multiple.size() == 0)
      throw new BuildException(FeatureResourceBundle.format(FeatureError.INSTANCE_MANDATORY, AccessServerProperty.ENTITY));

    try {
      final ServiceOperation operation = this.operation();
      for (AccessServerInstance cursor : this.multiple)
        cursor.validate(operation);
    }
    catch (Exception e) {
      throw new BuildException(e.getLocalizedMessage());
    }

    // ensure inheritance
    super.validate();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Called by the project to let the task do its work to create an
   ** <code>Access Server</code>.
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
  public void create(final MBeanServerConnection connection)
    throws ServiceException {

    // prevent bogus input
    if (connection == null)
      throw new NullPointerException("connection");

    this.connection = connection;
    for (AccessServerInstance cursor : this.multiple) {
      create(cursor);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Called by the project to let the task do its work to modify an
   ** <code>Access Server</code>.
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
  public void modify(final MBeanServerConnection connection)
    throws ServiceException {

    // prevent bogus input
    if (connection == null)
      throw new NullPointerException("connection");

    this.connection = connection;
    for (AccessServerInstance cursor : this.multiple) {
      modify(cursor);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Called by the project to let the task do its work to delete an
   ** <code>Access Server</code>.
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
  public void delete(final MBeanServerConnection connection)
    throws ServiceException {

    // prevent bogus input
    if (connection == null)

    this.connection = connection;
    for (AccessServerInstance cursor : this.multiple) {
      delete(cursor);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   print
  /**
   ** Called by the project to let the task do its work to report an
   ** <code>Access Server</code>.
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
  public void print(final MBeanServerConnection connection)
    throws ServiceException {

    // prevent bogus input
    if (connection == null)
      throw new NullPointerException("connection");

    this.connection = connection;
    for (AccessServerInstance cursor : this.multiple) {
      print(cursor);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exists
  /**
   ** Returns an existing <code>Access Server</code> from Oracle Access Manager
   ** for the specified {@link AccessServerInstance}.
   **
   ** @param  instance           the {@link AccessServerInstance} to check.
   **
   ** @return                    <code>true</code> if the
   **                            <code>Access Server</code> specified by
   **                            {@link AccessServerInstance} exists; otherwise
   **                            <code>false</code>.
   **
   ** @throws BuildException     in case an error does occur.
   */
  protected boolean exists(final AccessServerInstance instance) {
    final Object[] parameter = {instance.name()};
    try {
      final String result = (String)silent(AccessServerProperty.REPORT, parameter, AccessServerProperty.SIGNATURE_REPORT);
      return (!StringUtility.isEmpty(result));
    }
    catch (Exception e) {
      throw new BuildException(ServiceResourceBundle.format(ServiceError.UNHANDLED, e));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Creates an <code>Access Server</code> in Oracle Access Manager for the
   ** specified {@link AccessServerInstance}.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @param  instance           the <code>Access Server</code>
   **                            {@link AccessServerInstance} to create.
   **
   ** @throws BuildException     if something goes wrong with the operation.
   */
  protected void create(final AccessServerInstance instance) {
    final String[] arguments  = { AccessServerProperty.ENTITY, instance.name() };
    warning(ServiceResourceBundle.format(ServiceMessage.OPERATION_CREATE_BEGIN, arguments));
    if (exists(instance)) {
      warning(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_EXISTS, arguments));
      modify(instance);
    }
    else {
      final String   operation = AccessServerProperty.CREATE;
      final Object[] parameter = instance.createParameter();
      final String[] signature = instance.createSignature();
      try {
        invoke(operation, parameter, signature);
        info(ServiceResourceBundle.format(ServiceMessage.OPERATION_CREATE_SUCCESS, arguments));
      }
      catch (ServiceException e) {
        // the service exception my have a wrapped exception in it which is the
        // interesting part to report
        Throwable cause = e.getCause();
        // verify if an ReflectionException is the cause to handle it properly
        if (cause instanceof ReflectionException) {
         cause = ((ReflectionException)cause).getTargetException();
        }
        final String message = ServiceResourceBundle.format(ServiceError.OPERATION_CREATE_FAILED, AccessServerProperty.ENTITY, instance.name(), cause.getLocalizedMessage());
        if (failonerror())
          throw new BuildException(message);
        else
          error(message);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Modifies an <code>Access Server</code> in Oracle Access Manager for the
   ** specified {@link AccessServerInstance}.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @param  instance           the <code>Access Server</code>
   **                            {@link AccessServerInstance} to modify.
   **
   ** @throws BuildException     if something goes wrong with the operation.
   */
  protected void modify(final AccessServerInstance instance) {
    final String[] arguments  = { AccessServerProperty.ENTITY, instance.name() };
    warning(ServiceResourceBundle.format(ServiceMessage.OPERATION_MODIFY_BEGIN, arguments));
    if (exists(instance)) {
      try {
        final String   operation = AccessServerProperty.MODIFY;
        final Object[] parameter = instance.modifyParameter();
        final String[] signature = instance.modifySignature();
        invoke(operation, parameter, signature);
        info(ServiceResourceBundle.format(ServiceMessage.OPERATION_MODIFY_SUCCESS, arguments));
      }
      catch (ServiceException e) {
        // the service exception my have a wrapped exception in it which is the
        // interesting part to report
        Throwable cause = e.getCause();
        // verify if an ReflectionException is the cause to handle it properly
        if (cause instanceof ReflectionException) {
         cause = ((ReflectionException)cause).getTargetException();
        }
        final String message = ServiceResourceBundle.format(ServiceError.OPERATION_MODIFY_FAILED, AccessServerProperty.ENTITY, instance.name(), cause.getLocalizedMessage());
        if (failonerror()) {
          throw new BuildException(message);
        }
        else
          error(message);
      }
    }
    else {
      error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Deletes an <code>Access Server</code> in Oracle Access Manager for the
   ** specified {@link AccessServerInstance}.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @param  instance           the <code>Access Server</code>
   **                            {@link AccessServerInstance} to delete.
   **
   ** @throws BuildException     if something goes wrong with the operation.
   */
  protected void delete(final AccessServerInstance instance) {
    final String[] arguments  = { AccessServerProperty.ENTITY, instance.name() };
    warning(ServiceResourceBundle.format(ServiceMessage.OPERATION_DELETE_BEGIN, arguments));
    if (exists(instance)) {
      try {
        final String   operation = AccessServerProperty.DELETE;
        final Object[] parameter = {instance.name()};
        final String[] signature = AccessServerProperty.SIGNATURE_REPORT;
        invoke(operation, parameter, signature);
        info(ServiceResourceBundle.format(ServiceMessage.OPERATION_DELETE_SUCCESS, arguments));
      }
      catch (ServiceException e) {
        // the service exception my have a wrapped exception in it which is the
        // interesting part to report
        Throwable cause = e.getCause();
        // verify if an ReflectionException is the cause to handle it properly
        if (cause instanceof ReflectionException) {
         cause = ((ReflectionException)cause).getTargetException();
        }
        final String message = ServiceResourceBundle.format(ServiceError.OPERATION_DELETE_FAILED, AccessServerProperty.ENTITY, instance.name(), cause.getLocalizedMessage());
        if (failonerror())
          throw new BuildException(message);
        else
          error(message);
      }
    }
    else {
      error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   print
  /**
   ** Prints the configuration of an <code>Access Server</code> in Oracle
   ** Access Manager for the specified {@link AccessServerInstance}.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @param  instance           the <code>Access Server</code>
   **                            {@link AccessServerInstance} to report.
   **
   ** @throws BuildException     if something goes wrong with the operation.
   */
  protected void print(final AccessServerInstance instance) {
    final String[] arguments  = { AccessServerProperty.ENTITY, instance.name() };
    if (exists(instance)) {
      final String   operation = AccessServerProperty.REPORT;
      final Object[] parameter = { instance.name() };
      final String[] signature = AccessServerProperty.SIGNATURE_REPORT;
      try {
        final Object entity = invoke(operation, parameter, signature);
        info(entity.toString());
      }
      catch (ServiceException e) {
        e.printStackTrace();
      }
    }
    else {
      error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments));
    }
  }
}