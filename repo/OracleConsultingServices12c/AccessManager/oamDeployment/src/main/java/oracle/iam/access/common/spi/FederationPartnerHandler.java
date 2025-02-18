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

    File        :   FederationPartnerHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    FederationPartnerHandler.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.common.spi;

import java.util.List;
import java.util.ArrayList;

import javax.management.ReflectionException;
import javax.management.MBeanServerConnection;

import org.apache.tools.ant.BuildException;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceMessage;
import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceOperation;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.hst.foundation.logging.TableFormatter;

import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.access.common.FeatureError;
import oracle.iam.access.common.FeatureMessage;
import oracle.iam.access.common.FeatureResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class FederationPartnerHandler
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>FederationPartnerHandler</code> creates, deletes and configures
 ** Federation Partners, either Identity Provider (idP) or Service Provider (SP)
 ** in an Oracle Access Manager infrastructure.
 ** <p>
 ** An <code>Identity Provider</code> (abbreviated IdP or IDP) is a system
 ** entity that creates, maintains, and manages identity information for
 ** principals while providing authentication services to relying applications
 ** within a federation or distributed network.
 ** <br>
 ** An <code>Identity Provider</code> is "a trusted provider that lets you use
 ** single sign-on (SSO) to access other websites." SSO enhances usability by
 ** reducing password fatigue. It also provides better security by decreasing
 ** the potential attack surface.
 ** <br>
 ** Identity providers can facilitate connections between cloud computing
 ** resources and users, thus decreasing the need for users to re-authenticate
 ** when using mobile and roaming applications.
 ** <p>
 ** A <code>Service Provider</code> (abbreviated SP) is a system entity that
 ** receives and accepts authentication assertions in conjunction with a single
 ** sign-on (SSO) profile.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class FederationPartnerHandler extends DomainRuntimeHandler {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final List<FederationPartnerType>     type     = new ArrayList<FederationPartnerType>();
  private final List<FederationPartnerInstance> multiple = new ArrayList<FederationPartnerInstance>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>FederationPartnerHandler</code> to initialize the
   ** instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   ** @param  operation          the {@link ServiceOperation} to execute either
   **                            <ul>
   **                              <li>{@link ServiceOperation#print}
   **                              <li>{@link ServiceOperation#create}
   **                              <li>{@link ServiceOperation#delete}
   **                              <li>{@link ServiceOperation#modify}
   **                            </ul>
   */
  public FederationPartnerHandler(final ServiceFrontend frontend, final ServiceOperation operation) {
    // ensure inheritance
    super(frontend, operation);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Call by the ANT deployment to inject the argument for adding a type.
   **
   ** @param  type               the type to add.
   **
   ** @throws BuildException     if this instance is already referencing an
   **                            {@link FederationPartnerType} with the same
   **                            name.
   */
  public void add(final FederationPartnerType type)
    throws BuildException {

    // prevent bogus input
    if (type == null)
      throw new BuildException(FeatureResourceBundle.format(FeatureError.PARTNER_TYPE_MANDATORY, FeatureResourceBundle.string(FeatureMessage.FEDERATION_PARTNER)));

    // prevent bogus state
    if (this.type.contains(type))
      throw new BuildException(FeatureResourceBundle.format(FeatureError.PARTNER_TYPE_ONLYONCE, FeatureResourceBundle.string(FeatureMessage.FEDERATION_PARTNER), type.id()));

    // add the type to the collection to handle
    this.type.add(type);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Call by the ANT deployment to inject the argument for adding an instance.
   **
   ** @param  instance           the instance to add.
   **
   ** @throws BuildException     if this instance is already referencing an
   **                            {@link FederationPartnerInstance} with the same
   **                            name.
   */
  public void add(final FederationPartnerInstance instance)
    throws BuildException {

    // prevent bogus input
    if (instance == null)
      throw new BuildException(FeatureResourceBundle.format(FeatureError.INSTANCE_MANDATORY, FederationPartnerProperty.ENTITY));

    // prevent bogus state
    if (this.flatten.contains(instance.name()))
      throw new BuildException(FeatureResourceBundle.format(FeatureError.INSTANCE_ONLYONCE, FederationPartnerProperty.ENTITY, instance.name()));

    // register the instance name for later validations
    this.flatten.add(instance.name());
    // add the instance to the collection to handle
    this.multiple.add(instance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   status
  /**
   ** Called by the project to let the task do its work to print the status of
   ** <code>Federation Partner</code>s that belongs to a specific type.
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
    // prepare the formatter
    final TableFormatter formatter = new TableFormatter();
    formatter.header(FeatureResourceBundle.string(FeatureMessage.FEDERATION_PARTNER)).header(FeatureResourceBundle.string(FeatureMessage.FEDERATION_PARTNER_TYPE));
    // iterate over all requested types of fedaration partners
    for (FederationPartnerType cursor : this.type) {
      final List<String> location = status(cursor.location());
      for (String status : location) {
        formatter.row().column(status).column(cursor.id());
      }
    }
    formatter.print();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Called by the project to let the task do its work to create an
   ** <code>Federation Partner</code>.
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

    // latch the connection to use for further processing in methods
    // called afterwards by this method
    this.connection = connection;
    for (FederationPartnerInstance cursor : this.multiple) {
      create(cursor);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Called by the project to let the task do its work to modify
   ** <code>Federation Partner</code>s.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @param  connection         the {@link MBeanServerConnection} this task
   **                            will use to do the workload.
   **
   ** @throws ServiceException   if something goes wrong with the build
   */
  public void modify(final MBeanServerConnection connection)
    throws ServiceException {

    // prevent bogus input
    if (connection == null)
      throw new NullPointerException("connection");

    // latch the connection to use for further processing in methods
    // called afterwards by this method
    this.connection = connection;
    for (FederationPartnerInstance cursor : this.multiple) {
      modify(cursor);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Called by the project to let the task do its work to delete an
   ** <code>Federation Partner</code>.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @param  connection         the {@link MBeanServerConnection} this task
   **                            will use to do the workload.
   **
   ** @throws ServiceException   if something goes wrong with the build
   */
  public void delete(final MBeanServerConnection connection)
    throws ServiceException {

    // prevent bogus input
    if (connection == null)
      throw new NullPointerException("connection");

    // latch the connection to use for further processing in methods
    // called afterwards by this method
    this.connection = connection;
    for (FederationPartnerInstance cursor : this.multiple) {
      delete(cursor);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   report
  /**
   ** Called by the project to let the task do its work to report an
   ** <code>Federation Partner</code>.
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
  public void report(final MBeanServerConnection connection)
    throws ServiceException {

    // prevent bogus input
    if (connection == null)
      throw new NullPointerException("connection");

    // latch the connection to use for further processing in methods
    // called afterwards by this method
    this.connection = connection;
    final String[] arguments = {FeatureResourceBundle.string(FeatureMessage.FEDERATION_PARTNER), FeatureResourceBundle.string(FeatureMessage.FEDERATION_PARTNER_TYPE)};
    warning(FeatureResourceBundle.format(FeatureMessage.OPERATION_PRINT_BEGIN, arguments));
    for (FederationPartnerInstance cursor : this.multiple)
      print(cursor);
    info(FeatureResourceBundle.format(FeatureMessage.OPERATION_PRINT_SUCCESS, arguments));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   status
  /**
   ** Returns the status of <code>Federation Partner</code>s in Oracle Access
   ** Manager for the specified <code>location</code> type.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @param  location           the location type of the
   **                            <code>Federation Partner</code>.
   **                            Possible values are:
   **                            <ol>
   **                              <li>Agents:
   **                                <ul>
   **                                  <li>{@link FederationPartnerType#OAM}
   **                                  <li>{@link FederationPartnerType#OSSO}
   **                                  <li>{@link FederationPartnerType#OPENSSO}
   **                                 </ul>
   **                              <li>Federation:
   **                                <ul>
   **                                  <li>{@link FederationPartnerType#FEDSP}
   **                                  <li>{@link FederationPartnerType#FEDIDP}
   **                                 </ul>
   **                              <li>Secure Token Service:
   **                                <ul>
   **                                  <li>{@link FederationPartnerType#STSISSUER}
   **                                  <li>{@link FederationPartnerType#STSRELYING}
   **                                  <li>{@link FederationPartnerType#STSREQUESTER}
   **                                 </ul>
   **                            </ol>
   **
   ** @return                    the collection of status mappings.
   **
   ** @throws BuildException     if something goes wrong with the operation.
   */
  protected List<String> status(final String location) {
    final String   operation = FederationPartnerType.STATUS;
    final Object[] parameter = {location};
    final String[] signature = FederationPartnerType.SIGNATURE_STATUS;
    try {
      return CollectionUtility.list((String[])invoke(operation, parameter, signature));
    }
    catch (ServiceException e) {
      // the service exception my have a wrapped exception in it which is the
      // interesting part to report
      Throwable cause = e.getCause();
      // verify if an ReflectionException is the cause to handle it properly
      if (cause instanceof ReflectionException) {
       cause = ((ReflectionException)cause).getTargetException();
      }
      final String message = ServiceResourceBundle.format(ServiceError.OPERATION_REPORT_FAILED, FeatureResourceBundle.string(FeatureMessage.FEDERATION_PARTNER), type, cause.getLocalizedMessage());
      throw new BuildException(message);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   print
  /**
   ** Prints a <code>Federation Partner</code> in Oracle Access Manager for the
   ** specified {@link FederationPartnerInstance}.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @param  instance           the <code>Federation Partner</code>
   **                            {@link FederationPartnerInstance} to report.
   **
   ** @throws BuildException     if something goes wrong with the operation.
   */
  protected void print(final FederationPartnerInstance instance) {
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exists
  /**
   ** Returns an existing <code>Federation Partner</code> from Oracle Access
   ** Manager for the specified {@link FederationPartnerInstance}.
   **
   ** @param  instance           the {@link FederationPartnerInstance} to check.
   **
   ** @return                    <code>true</code> if the
   **                            <code>Federation Partner</code> specified by
   **                            {@link FederationPartnerInstance} exists;
   **                            otherwise <code>false</code>.
   **
   ** @throws BuildException     in case an error does occur.
   */
  protected boolean exists(final FederationPartnerInstance instance) {
    final Object[] parameter = {instance.name()};
    try {
      final String result = (String)silent(FederationPartnerProperty.REPORT, parameter, FederationPartnerProperty.SIGNATURE_REPORT);
      return (!StringUtility.isEmpty(result));
    }
    catch (Exception e) {
      throw new BuildException(ServiceResourceBundle.format(ServiceError.UNHANDLED, e));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Creates an <code>Federation Partner</code> in Oracle Access Manager for
   ** the specified {@link FederationPartnerInstance}.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @param  instance           the <code>Federation Partner</code>
   **                            {@link FederationPartnerInstance} to create.
   **
   ** @throws BuildException     if something goes wrong with the operation.
   */
  protected void create(final FederationPartnerInstance instance) {
    final String[] arguments  = { FederationPartnerProperty.ENTITY, instance.name() };
    warning(ServiceResourceBundle.format(ServiceMessage.OPERATION_CREATE_BEGIN, arguments));
    if (exists(instance)) {
      warning(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_EXISTS, arguments));
      modify(instance);
    }
    else {
      final String   operation = FederationPartnerProperty.CREATE;
      final Object[] parameter = instance.createParameter();
      final String[] signature = instance.createSignature();
      try {
        invoke(operation, parameter, signature);
        info(ServiceResourceBundle.format(ServiceMessage.OPERATION_CREATE_SUCCESS, arguments));
      }
      catch (ServiceException e) {
        // the service exception my have a wrapped exception in it which is the
        // interesting part to report
        final String message = ServiceResourceBundle.format(ServiceError.OPERATION_CREATE_FAILED, instance.name(), e.getCause().getLocalizedMessage());
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
   ** Modifies an <code>Federation Partner</code> in Oracle Access Manager for
   ** the specified {@link FederationPartnerInstance}.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @param  instance           the <code>Federation Partner</code>
   **                            {@link FederationPartnerInstance} to modify.
   **
   ** @throws BuildException     if something goes wrong with the operation.
   */
  protected void modify(final FederationPartnerInstance instance) {
    final String[] arguments  = { FederationPartnerProperty.ENTITY, instance.name() };
    warning(ServiceResourceBundle.format(ServiceMessage.OPERATION_MODIFY_BEGIN, arguments));
    if (exists(instance)) {
      try {
        final String   operation = FederationPartnerProperty.MODIFY;
        final Object[] parameter = instance.modifyParameter();
        final String[] signature = instance.modifySignature();
        invoke(operation, parameter, signature);
        info(ServiceResourceBundle.format(ServiceMessage.OPERATION_MODIFY_SUCCESS, arguments));
      }
      catch (ServiceException e) {
        final String message = ServiceResourceBundle.format(ServiceError.OPERATION_MODIFY_FAILED, instance.name(), e.getCause().getLocalizedMessage());
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
   ** Deletes an <code>Federation Partner</code> in Oracle Access Manager for
   ** the specified {@link FederationPartnerInstance}.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @param  instance           the <code>Federation Partner</code>
   **                            {@link FederationPartnerInstance} to delete.
   **
   ** @throws BuildException     if something goes wrong with the operation.
   */
  protected void delete(final FederationPartnerInstance instance) {
    final String[] arguments  = { FederationPartnerProperty.ENTITY, instance.name() };
    warning(ServiceResourceBundle.format(ServiceMessage.OPERATION_DELETE_BEGIN, arguments));
    if (exists(instance)) {
      try {
        final String   operation = FederationPartnerProperty.DELETE;
        final Object[] parameter = {instance.name()};
        final String[] signature = FederationPartnerProperty.SIGNATURE_REPORT;
        invoke(operation, parameter, signature);
        info(ServiceResourceBundle.format(ServiceMessage.OPERATION_DELETE_SUCCESS, arguments));
      }
      catch (ServiceException e) {
        final String message = ServiceResourceBundle.format(ServiceError.OPERATION_DELETE_FAILED, instance.name(), e.getCause().getLocalizedMessage());
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
  // Method:   invoke
  /**
   ** Invokes an operation on the MBean returned by (@link #objectName()}.
   ** <p>
   ** Because of the need for a signature to differentiate possibly-overloaded
   ** operations, it is much simpler to invoke operations through an MBean proxy
   ** where possible.
   **
   ** @param  operation          the name of the operation to invoke.
   ** @param  parameter          an array containing the parameters to be set
   **                            when the operation is invoked.
   ** @param  signature          an array containing the signature of the
   **                            operation. The class objects will be loaded
   **                            using the same class loader as the one used for
   **                            loading the MBean on which the operation was
   **                            invoked.
   **
   ** @return                    the object returned by the operation, which
   **                            represents the result of invoking the operation
   **                            on the MBean specified.
   **
   ** @throws ServiceException   a communication problem occurred when talking
   **                            to the MBean server.
   */
  protected Object invoke(final String operation, final Object[] parameter, final String[] signature)
    throws ServiceException {

    return invoke(objectName(), operation, parameter, signature);
  }
}