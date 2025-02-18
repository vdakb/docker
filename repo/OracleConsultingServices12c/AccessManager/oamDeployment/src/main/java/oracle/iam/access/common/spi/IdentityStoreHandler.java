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

    File        :   IdentityStoreHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    IdentityStoreHandler.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.common.spi;

import java.util.Map;
import java.util.List;
import java.util.TreeMap;
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

import oracle.hst.foundation.logging.TableFormatter;

import oracle.iam.access.common.FeatureError;
import oracle.iam.access.common.FeatureMessage;
import oracle.iam.access.common.FeatureResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class IdentityStoreHandler
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>IdentityStoreHandler</code> creates, deletes and configures Identity
 ** Stores in an Oracle Access Manager infrastructure.
 ** <p>
 ** A <code>Identity Store</code> is a centralized LDAP repository in which
 ** an aggregation of Administrator and user-oriented data is stored and
 ** maintained in an organized way.
 ** <p>
 ** Oracle Access Management supports multiple LDAP vendors, and multiple LDAP
 ** stores can be registered for use by Oracle Access Management and its
 ** services.
 ** <br>
 ** Oracle Access Management addresses each user population and LDAP directory
 ** store as an identity domain. Each identity domain maps to a configured LDAP
 ** <code>Identity Store</code> that must be registered with Oracle Access
 ** Management.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class IdentityStoreHandler extends ApplicationRuntimeHandler {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final List<IdentityStoreInstance> multiple = new ArrayList<IdentityStoreInstance>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>IdentityStoreHandler</code> to initialize the instance.
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
  public IdentityStoreHandler(final ServiceFrontend frontend, final ServiceOperation operation) {
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
   **                            {@link IdentityStoreInstance} with the same
   **                            name.
   */
  public void add(final IdentityStoreInstance instance)
    throws BuildException {

    // prevent bogus input
    if (instance == null)
      throw new BuildException(FeatureResourceBundle.format(FeatureError.INSTANCE_MANDATORY, FeatureResourceBundle.string(FeatureMessage.IDENTITY_STORE)));

    // prevent bogus state
    if (this.flatten.contains(instance.name()))
      throw new BuildException(FeatureResourceBundle.format(FeatureError.INSTANCE_ONLYONCE, FeatureResourceBundle.string(FeatureMessage.IDENTITY_STORE), instance.name()));

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
      throw new BuildException(FeatureResourceBundle.format(FeatureError.INSTANCE_MANDATORY, FeatureResourceBundle.string(FeatureMessage.IDENTITY_STORE)));

    try {
      final ServiceOperation operation = this.operation();
      for (IdentityStoreInstance cursor : this.multiple)
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
   ** <code>Identity Store</code>.
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
    for (IdentityStoreInstance cursor : this.multiple) {
      create(cursor);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Called by the project to let the task do its work to modify
   ** <code>Identity Store</code>s.
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

    this.connection = connection;
    for (IdentityStoreInstance cursor : this.multiple) {
      modify(cursor);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Called by the project to let the task do its work to delete
   ** <code>Identity Store</code>s.
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

    this.connection = connection;
    for (IdentityStoreInstance cursor : this.multiple) {
      delete(cursor);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   print
  /**
   ** Called by the project to let the task do its work to print an
   ** <code>Identity Store</code>.
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
    for (IdentityStoreInstance cursor : this.multiple) {
      print(cursor);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exists
  /**
   ** Returns an existing <code>Identity Store</code> from Oracle Access Manager
   ** for the specified {@link IdentityStoreInstance}.
   **
   ** @param  instance           the {@link IdentityStoreInstance} to check.
   **
   ** @return                    <code>true</code> if the
   **                            <code>Identity Store</code> specified by
   **                            {@link IdentityStoreInstance} exists; otherwise
   **                            <code>false</code>.
   **
   ** @throws BuildException     in case an error does occur.
   */
  protected boolean exists(final IdentityStoreInstance instance) {
    final Object[] parameter = {instance.name()};
    try {
      final String result = (String)silent(IdentityStoreProperty.REPORT, parameter, IdentityStoreProperty.SIGNATURE_REPORT);
      return (!StringUtility.isEmpty(result));
    }
    catch (Exception e) {
      throw new BuildException(ServiceResourceBundle.format(ServiceError.UNHANDLED, e));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Creates an <code>Identity Store</code> in Oracle Access Manager for the
   ** specified {@link IdentityStoreInstance}.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @param  instance           the <code>Identity Store</code>
   **                            {@link IdentityStoreInstance} to create.
   **
   ** @throws BuildException     if something goes wrong with the operation.
   */
  protected void create(final IdentityStoreInstance instance) {
    final String[] arguments  = { FeatureResourceBundle.string(FeatureMessage.IDENTITY_STORE), instance.name() };
    warning(ServiceResourceBundle.format(ServiceMessage.OPERATION_CREATE_BEGIN, arguments));
    if (exists(instance)) {
      warning(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_EXISTS, arguments));
      modify(instance);
    }
    else {
      final String   operation = IdentityStoreProperty.CREATE;
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
   ** Modifies an <code>Identity Store</code> in Oracle Access Manager for the
   ** specified {@link IdentityStoreInstance}.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @param  instance           the <code>Identity Store</code>
   **                            {@link IdentityStoreInstance} to modify.
   **
   ** @throws BuildException     if something goes wrong with the operation.
   */
  protected void modify(final IdentityStoreInstance instance) {
    final String[] arguments  = { FeatureResourceBundle.string(FeatureMessage.IDENTITY_STORE), instance.name() };
    warning(ServiceResourceBundle.format(ServiceMessage.OPERATION_MODIFY_BEGIN, arguments));
    if (exists(instance)) {
      try {
        final String   operation = IdentityStoreProperty.MODIFY;
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
   ** Deletes an <code>Identity Store</code> in Oracle Access Manager for the
   ** specified {@link IdentityStoreInstance}.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @param  instance           the <code>Identity Store</code>
   **                            {@link IdentityStoreInstance} to delete.
   **
   ** @throws BuildException     if something goes wrong with the operation.
   */
  protected void delete(final IdentityStoreInstance instance) {
    final String[] arguments  = { FeatureResourceBundle.string(FeatureMessage.IDENTITY_STORE), instance.name() };
    warning(ServiceResourceBundle.format(ServiceMessage.OPERATION_DELETE_BEGIN, arguments));
    if (exists(instance)) {
      try {
        final String   operation = IdentityStoreProperty.DELETE;
        final Object[] parameter = {instance.name()};
        final String[] signature = IdentityStoreProperty.SIGNATURE_REPORT;
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
  // Method:   print
  /**
   ** Prints the configuration of an <code>Identity Store</code> in Oracle
   ** Access Manager for the specified {@link IdentityStoreInstance}.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @param  instance           the <code>Identity Store</code>
   **                            {@link IdentityStoreInstance} to report.
   **
   ** @throws BuildException     if something goes wrong with the operation.
   */
  protected void print(final IdentityStoreInstance instance) {
    final String[] arguments = { FeatureResourceBundle.string(FeatureMessage.IDENTITY_STORE), instance.name() };
    if (exists(instance)) {
      final String   operation = IdentityStoreProperty.REPORT;
      final Object[] parameter = { instance.name() };
      final String[] signature = IdentityStoreProperty.SIGNATURE_REPORT;
      try {
        final Map<String, String> value = parse((String)invoke(operation, parameter, signature));
        final TableFormatter formatter = new TableFormatter();
        formatter.header(FeatureResourceBundle.string(FeatureMessage.IDENTITY_STORE_PROPERTY)).header(FeatureResourceBundle.string(FeatureMessage.IDENTITY_STORE_VALUE));
        for (Map.Entry<String, String> property : value.entrySet()) {
          formatter.row().column(property.getKey()).column(property.getValue());
        }
        formatter.print();
      }
      catch (ServiceException e) {
        // the service exception my have a wrapped exception in it which is the
        // interesting part to report
        Throwable cause = e.getCause();
        // verify if an ReflectionException is the cause to handle it properly
        if (cause instanceof ReflectionException) {
         cause = ((ReflectionException)cause).getTargetException();
        }
        final String message = ServiceResourceBundle.format(ServiceError.OPERATION_REPORT_FAILED, FeatureResourceBundle.string(FeatureMessage.IDENTITY_STORE), cause.getLocalizedMessage());
        throw new BuildException(message);
      }
    }
    else {
      error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments));
    }
  }
  
  /**
   ** Parseing the configuration string returned.
   ** <p>
   ** Is this shit ugly
   * @param raw
   * @return
   */
  private final Map<String, String> parse(final String raw) {
    final Map<String, String> value = new TreeMap<String, String>();
    int    com = 0;
    // find occurens of the last collon
    int    col = raw.lastIndexOf(':');
    String crs = raw;
    while (col != -1) {
      String v = crs.substring(col + 1).trim();
      crs = crs.substring(0, col);
      com = crs.lastIndexOf(',');
      String k = crs.substring(com + 1).trim();
      col = k.indexOf(':');
      if (col != -1) {
        v = k + ":" + v;
        k = k.substring(0, col - 1).trim();
        col = v.indexOf(':');
        v = v.substring(col + 1).trim();
      }
      value.put(k, v);
      if (com == -1)
        break;
      crs = crs.substring(0, com);
      col = crs.lastIndexOf(':');
    }
    return value;
  }
}