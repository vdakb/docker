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

    File        :   AccessAgentHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AccessAgentHandler.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.common.spi;

import java.util.List;
import java.util.ArrayList;

import org.apache.tools.ant.BuildException;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceMessage;
import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceOperation;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.hst.deployment.task.AbstractServletTask;

import oracle.iam.access.common.FeatureError;
import oracle.iam.access.common.FeatureMessage;
import oracle.iam.access.common.FeatureResourceBundle;

import oracle.iam.access.common.spi.schema.BaseResponse;

////////////////////////////////////////////////////////////////////////////////
// class AccessAgentHandler
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** <code>AccessAgentHandler</code> creates, deletes and configures
 ** <code>Access Agent</code>s in an <i>Oracle Access Manager</i> (<b>OAM</b>)
 ** infrastructure.
 ** <p>
 ** A <code>Access Agent</code> is a web-server plug-in for <i>Oracle Access
 ** Manager</i> (<b>OAM</b>) that intercepts HTTP requests and forwards them to
 ** the <code>Access Server</code> for authentication and authorization.
 ** <p>
 ** <i>Oracle Access Manager</i> (<b>OAM</b>) authenticates each user with a
 ** customer-specified authentication method to determine the identity and
 ** leverages information stored in the user identity store. <i>Oracle Access
 ** Manager</i> (<b>OAM</b>) authentication supports several authentication
 ** methods and different authentication levels. Resources with varying degrees
 ** of sensitivity can be protected by requiring higher levels of authentication
 ** that correspond to more stringent authentication methods.
 ** <p>
 ** When a user tries to access a protected application, the request is received
 ** by <b>OAM</b> which checks for the existence of the <b>SSO</b> cookie.
 ** <p>
 ** After authenticating the user and setting up the user context and token,
 ** <b>OAM</b> sets the <b>SSO</b> cookie and encrypts the cookie with the
 ** SSO Server key (which can be decrypted only by the SSO Engine).
 ** <p>
 ** Depending on the actions (responses in OAM 11g) specified for authentication
 ** success and authentication failure, the user may be redirected to a specific
 ** URL, or user information might be passed on to other applications through a
 ** header variable or a cookie value.
 ** <p>
 ** Based on the authorization policy and results of the check, the user is
 ** allowed or denied access to the requested content. If the user is denied
 ** access, she is redirected to another URL (specified by the administrator in
 ** <code>Access Agent</code> registration).
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class AccessAgentHandler extends RegistrationHandler {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final List<AccessAgentInstance> multiple = new ArrayList<AccessAgentInstance>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>AccessAgentHandler</code> to initialize the instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   ** @param  operation          the {@link ServiceOperation} to execute either
   **                            <ul>
   **                              <li>{@link ServiceOperation#create}
   **                              <li>{@link ServiceOperation#modify}
   **                              <li>{@link ServiceOperation#delete}
   **                              <li>{@link ServiceOperation#validate}
   **                            </ul>
   */
  public AccessAgentHandler(final ServiceFrontend frontend, final ServiceOperation operation) {
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
   **                            {@link AccessAgentInstance} with the same name.
   */
  public void add(final AccessAgentInstance instance)
    throws BuildException {

    // prevent bogus input
    if (instance == null)
      throw new BuildException(FeatureResourceBundle.format(FeatureError.INSTANCE_MANDATORY, AccessAgentProperty.ENTITY));

    // prevent bogus state
    if (this.flatten.contains(instance.name()))
      throw new BuildException(FeatureResourceBundle.format(FeatureError.INSTANCE_ONLYONCE, AccessAgentProperty.ENTITY));

    // register the instance name for later validations
    this.flatten.add(instance.name());
    // add the instance to the collection to handle
    this.multiple.add(instance);

    // prepare the core implementation of an instance with all the required
    // infomation needed
    switch (this.operation()) {
      case print    : instance.mode(AccessAgentProperty.Mode.REPORT.value());
                      break;
      case create   : instance.mode(AccessAgentProperty.Mode.CREATE.value());
                      break;
      case modify   : instance.mode(AccessAgentProperty.Mode.MODIFY.value());
                      break;
      case delete   : instance.mode(AccessAgentProperty.Mode.DELETE.value());
                      break;
      case validate : instance.mode(AccessAgentProperty.Mode.VALIDATE.value());
                      break;
    }
    instance.username(((AbstractServletTask)this.frontend).context().username());
    instance.password(((AbstractServletTask)this.frontend).context().password());
    instance.serviceURL(((AbstractServletTask)this.frontend).context().serviceURL());
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
      throw new BuildException(FeatureResourceBundle.format(FeatureError.INSTANCE_MANDATORY, AccessAgentProperty.ENTITY));

    try {
      final ServiceOperation operation = this.operation();
      for (AccessAgentInstance cursor : this.multiple)
        cursor.validate(operation);
    }
    catch (Exception e) {
      throw new BuildException(e.getLocalizedMessage());
    }

    // ensure inheritance
    super.validate();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Called by the project task to let the handler do its work.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @throws ServiceException   if something goes wrong with the build
   */
  public void execute()
    throws ServiceException {

    final ServiceOperation operation = operation();
    switch (operation) {
      case print    : report();
                      break;
      case create   : create();
                      break;
      case modify   : modify();
                      break;
      case delete   : delete();
                      break;
      case validate : verify();
                      break;
      default       : final String[] arguments = {operation.id(), AccessAgentProperty.ENTITY};
                      throw new ServiceException(ServiceError.OPERATION_UNSUPPORTED, arguments);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Called by the project to let the task do its work to create an
   ** <code>Access Agent</code>.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @throws ServiceException   if {@link #failonerror()} advice to throw an
   **                            exeption and one of the
   **                            <code>Access Agent</code>s already exists or
   **                            something else goes wrong with the operation.
   */
  protected void create()
    throws ServiceException {
    for (AccessAgentInstance cursor : this.multiple) {
      create(cursor);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Called by the project to let the task do its work to modify an
   ** <code>Access Agent</code>.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @throws ServiceException   if {@link #failonerror()} advice to throw an
   **                            exeption and one of the
   **                            <code>Access Agent</code>s does not exists or
   **                            something else goes wrong with the operation.
   */
  protected void modify()
    throws ServiceException {
    for (AccessAgentInstance cursor : this.multiple) {
      modify(cursor);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Called by the project to let the task do its work to delete an
   ** <code>Access Agent</code>.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @throws ServiceException   if {@link #failonerror()} advice to throw an
   **                            exeption and one of the
   **                            <code>Access Agent</code>s does not exists or
   **                            something else goes wrong with the operation.
   */
  protected void delete()
    throws ServiceException{

    for (AccessAgentInstance cursor : this.multiple) {
      delete(cursor);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   report
  /**
   ** Called by the project to let the task do its work to report an
   ** <code>Access Agent</code>.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @throws ServiceException   if {@link #failonerror()} advice to throw an
   **                            exeption and one of the
   **                            <code>Access Agent</code>s does not exists or
   **                            something else goes wrong with the operation.
   */
  protected void report()
    throws ServiceException{

    for (AccessAgentInstance cursor : this.multiple) {
      report(cursor);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   verify
  /**
   ** This is the main handler method on the client side for the validate mode
   ** of agents.
   ** <br>
   ** For the agents specified, the agent profile details as well as
   ** application domain and policy details for this agent (if present) are
   ** retrieved from the server side and their summary is displayed.
   ** <p>
   ** Called by the project to let the task do its work to report an
   ** <code>Access Agent</code>.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @throws ServiceException   if {@link #failonerror()} advice to throw an
   **                            exeption and one of the
   **                            <code>Access Agent</code>s does not exists or
   **                            something else goes wrong with the operation.
   */
  protected void verify()
    throws ServiceException {

    for (AccessAgentInstance cursor : this.multiple) {
      verify(cursor);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Creates an <code>Access Agent</code> in Oracle Access Manager for the
   ** specified {@link AccessAgentInstance}.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @param  instance           the <code>Access Server</code>
   **                            {@link AccessAgentInstance} to create.
   **
   ** @throws ServiceException   if the <code>Access Agent</code> already exists
   **                            or something else goes wrong with the
   **                            operation.
   */
  private void create(final AccessAgentInstance instance)
    throws ServiceException {

    warning(ServiceResourceBundle.format(ServiceMessage.OPERATION_CREATE_BEGIN, AccessAgentProperty.ENTITY, instance.name()));
    final String payload = parseToString(instance.createRequest());
    if (verbose())
      trace(FeatureResourceBundle.format(FeatureMessage.SERVICE_REQUEST_PAYLOAD, payload));
    final RegistrationProtocol.Response answer = RegistrationProtocol.create(instance.serviceURL(), payload).transmit();
    if (verbose())
      trace(FeatureResourceBundle.format(FeatureMessage.SERVICE_RESPONSE_PAYLOAD, answer.content()));

    final BaseResponse                response = instance.createResponse(answer.content());
    // checking if any error occurred on the server side and throwing
    // exception out to the client
    if (response.getErrorMsgs() == null) {
      info(ServiceResourceBundle.format(ServiceMessage.OPERATION_CREATE_SUCCESS, AccessAgentProperty.ENTITY, instance.name()));
      if (verbose())
        instance.createReport(System.out, response);
    }
    else {
      final String[] arguments = {AccessAgentProperty.ENTITY, instance.name(), response.getErrorMsgs().getMessage().get(0) };
      if (failonerror())
        throw new ServiceException(ServiceError.OPERATION_CREATE_FAILED, arguments);
      else
        error(ServiceResourceBundle.format(ServiceError.OPERATION_CREATE_FAILED, arguments));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Modifies an <code>Access Agent</code> in Oracle Access Manager for the
   ** specified {@link AccessAgentInstance}.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @param  instance           the <code>Access Server</code>
   **                            {@link AccessAgentInstance} to modify.
   **
   ** @throws ServiceException   if the <code>Access Agent</code> does not
   **                            exists or something else goes wrong with the
   **                            operation.
   */
  private void modify(final AccessAgentInstance instance)
    throws ServiceException {

    warning(ServiceResourceBundle.format(ServiceMessage.OPERATION_MODIFY_BEGIN, AccessAgentProperty.ENTITY, instance.name()));
    final String payload = parseToString(instance.modifyRequest());
    if (verbose())
      trace(FeatureResourceBundle.format(FeatureMessage.SERVICE_REQUEST_PAYLOAD, payload));

    final RegistrationProtocol.Response answer = RegistrationProtocol.create(instance.serviceURL(), payload).transmit();
    if (verbose())
      trace(FeatureResourceBundle.format(FeatureMessage.SERVICE_RESPONSE_PAYLOAD, answer));

    final BaseResponse response = instance.createResponse(answer.content());
    // checking if any error occurred on the server side and throwing
    // exception out to the client
    if (response.getErrorMsgs() == null) {
      info(ServiceResourceBundle.format(ServiceMessage.OPERATION_MODIFY_SUCCESS, AccessAgentProperty.ENTITY, instance.name()));
      if (verbose())
        instance.createReport(System.out, response);
    }
    else {
      final String[] arguments = {AccessAgentProperty.ENTITY, instance.name(), response.getErrorMsgs().getMessage().get(0) };
      if (failonerror())
        throw new ServiceException(ServiceError.OPERATION_MODIFY_FAILED, arguments);
      else
        error(ServiceResourceBundle.format(ServiceError.OPERATION_MODIFY_FAILED, arguments));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Deletes an <code>Access Agent</code> in Oracle Access Manager for the
   ** specified {@link AccessAgentInstance}.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @param  instance           the <code>Access Agent</code>
   **                            {@link AccessAgentInstance} to delete.
   **
   ** @throws ServiceException   if the <code>Access Agent</code> does not
   **                            exists or something else goes wrong with the
   **                            operation.
   */
  private void delete(final AccessAgentInstance instance)
    throws ServiceException {

    warning(ServiceResourceBundle.format(ServiceMessage.OPERATION_DELETE_BEGIN, AccessAgentProperty.ENTITY, instance.name()));
    final String payload = parseToString(instance.baseRequest());
    if (verbose())
      trace(FeatureResourceBundle.format(FeatureMessage.SERVICE_REQUEST_PAYLOAD, payload));

    final RegistrationProtocol.Response answer  = RegistrationProtocol.create(instance.serviceURL(), payload).transmit();
    if (verbose())
      trace(FeatureResourceBundle.format(FeatureMessage.SERVICE_RESPONSE_PAYLOAD, answer));

    final BaseResponse response = instance.createResponse(answer.content());
    instance.createReport(System.out, response);
    // checking if any error occurred on the server side and throwing
    // exception out to the client
    if (response.getErrorMsgs() == null) {
      info(ServiceResourceBundle.format(ServiceMessage.OPERATION_DELETE_SUCCESS, AccessAgentProperty.ENTITY, instance.name()));
    }
    else {
      final String[] arguments = {AccessAgentProperty.ENTITY, instance.name(), response.getErrorMsgs().getMessage().get(0) };
      if (failonerror())
        throw new ServiceException(ServiceError.OPERATION_DELETE_FAILED, arguments);
      else
        error(ServiceResourceBundle.format(ServiceError.OPERATION_DELETE_FAILED, arguments));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   report
  /**
   ** Reports the configuration of an <code>Access Agent</code> in Oracle
   ** Access Manager for the specified {@link AccessAgentInstance}.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @param  instance           the <code>Access Agent</code>
   **                            {@link AccessAgentInstance} to report.
   **
   ** @throws BuildException     if something goes wrong with the operation.
   */
  private void report(final AccessAgentInstance instance)
    throws ServiceException {

    final String payload = parseToString(instance.agent11gReport());
    if (verbose())
      trace(FeatureResourceBundle.format(FeatureMessage.SERVICE_REQUEST_PAYLOAD, payload));
    final RegistrationProtocol.Response answer = RegistrationProtocol.create(instance.serviceURL(), payload).transmit();
    if (verbose())
      trace(FeatureResourceBundle.format(FeatureMessage.SERVICE_RESPONSE_PAYLOAD, answer));
  
    final BaseResponse response = instance.createResponse(answer.content());
    instance.createReport(System.out, response);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   verify
  /**
   ** Validates the configuration of an <code>Access Agent</code> in Oracle
   ** Access Manager for the specified {@link AccessAgentInstance}.
   ** <p>
   ** This is the main handler method on the client side for the validate mode
   ** of an <code>Access Agent</code>.
   ** <br>
   ** For the <code>Access Agent</code> specified, the agent profile details as
   ** well as application domain and policy details for this agent (if present)
   ** are retrieved from the server side and their summary is displayed.
   **
   ** @param  instance           the <code>Access Agent</code>
   **                            {@link AccessAgentInstance} to report.
   **
   ** @throws ServiceException   if the <code>Access Agent</code> does not
   **                            exists or something else goes wrong with the
   **                            operation.
   */
  private void verify(final AccessAgentInstance instance)
    throws ServiceException {

    warning(ServiceResourceBundle.format(ServiceMessage.OPERATION_VALIDATE_BEGIN, AccessAgentProperty.ENTITY, instance.name()));
    final String       payload  = parseToString(instance.baseRequest());
    if (verbose())
      trace(FeatureResourceBundle.format(FeatureMessage.SERVICE_REQUEST_PAYLOAD, payload));

    final RegistrationProtocol.Response answer = RegistrationProtocol.create(instance.serviceURL(), payload).transmit();
    if (verbose())
      trace(FeatureResourceBundle.format(FeatureMessage.SERVICE_RESPONSE_PAYLOAD, answer));

    final BaseResponse response = instance.createResponse(answer.content());
    instance.createReport(System.out, response);
    // checking if any error occurred on the server side and throwing
    // exception out to the client
    if (response.getErrorMsgs() == null) {
      info(ServiceResourceBundle.format(ServiceMessage.OPERATION_VALIDATE_SUCCESS, AccessAgentProperty.ENTITY, instance.name()));
    }
    else {
      final String[] arguments = {AccessAgentProperty.ENTITY, instance.name(), response.getErrorMsgs().getMessage().get(0) };
      if (failonerror())
        throw new ServiceException(ServiceError.OPERATION_VALIDATE_FAILED, arguments);
      else
        error(ServiceResourceBundle.format(ServiceError.OPERATION_VALIDATE_FAILED, arguments));
    }
  }
}