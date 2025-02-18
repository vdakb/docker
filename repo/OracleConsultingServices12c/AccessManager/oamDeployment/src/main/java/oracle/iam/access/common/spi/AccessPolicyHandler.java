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

    File        :   AccessPolicyHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AccessPolicyHandler.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.common.spi;

import java.util.List;
import java.util.ArrayList;

import java.io.StringReader;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import oracle.hst.deployment.ServiceError;

import org.apache.tools.ant.BuildException;

import oracle.hst.deployment.ServiceMessage;
import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceOperation;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.hst.deployment.task.AbstractServletTask;

import oracle.iam.access.common.FeatureError;
import oracle.iam.access.common.FeatureMessage;
import oracle.iam.access.common.FeatureException;
import oracle.iam.access.common.FeatureResourceBundle;

import oracle.iam.access.common.spi.schema.PolicyResponse;

////////////////////////////////////////////////////////////////////////////////
// class AccessPolicyHandler
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** <code>AccessPolicyHandler</code> creates, deletes and configures
 ** Authentication/Authorization Policies in an Oracle Access Manager
 ** infrastructure.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class AccessPolicyHandler extends RegistrationHandler {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final List<AccessPolicyInstance> multiple = new ArrayList<AccessPolicyInstance>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>AccessPolicyHandler</code> to initialize the instance.
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
  public AccessPolicyHandler(final ServiceFrontend frontend, final ServiceOperation operation) {
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
   **                            {@link AccessPolicyInstance} with the same
   **                            name.
   */
  public void add(final AccessPolicyInstance instance)
    throws BuildException {

    // prevent bogus input
    if (instance == null)
      throw new BuildException(FeatureResourceBundle.format(FeatureError.INSTANCE_MANDATORY, AccessPolicyProperty.ENTITY));

    // prevent bogus state
    if (this.flatten.contains(instance.name()))
      throw new BuildException(FeatureResourceBundle.format(FeatureError.INSTANCE_ONLYONCE, AccessPolicyProperty.ENTITY, instance.name()));

    // register the instance name for later validations
    this.flatten.add(instance.name());
    // add the instance to the collection to handle
    this.multiple.add(instance);

    // prepare the core implementation of an instance with all the required
    // infomation needed
    switch (this.operation()) {
      case create   : instance.mode(AccessPolicyProperty.Mode.CREATE.value());
                      break;
      case modify   : instance.mode(AccessPolicyProperty.Mode.MODIFY.value());
                      break;
      case delete   : instance.mode(AccessPolicyProperty.Mode.DELETE.value());
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
      throw new BuildException(FeatureResourceBundle.format(FeatureError.INSTANCE_MANDATORY, AccessPolicyProperty.ENTITY));

    try {
      for (AccessPolicyInstance cursor : this.multiple)
        cursor.validate();
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
      case create   : create();
                      break;
      case modify   : modify();
                      break;
      case delete   : delete();
                      break;
      default       : final String[] arguments = {operation.id(), AccessPolicyProperty.ENTITY};
                      throw new ServiceException(ServiceError.OPERATION_UNSUPPORTED, arguments);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Called by the project to let the task do its work to create one or more
   ** <code>Authentication/Authorization Policy</code>.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @throws ServiceException   if {@link #failonerror()} advice to throw an
   **                            exeption and one of the
   **                            <code>Authentication/Authorization Policy</code>s
   **                            already exists or something else goes wrong
   **                            with the operation.
   */
  protected void create()
    throws ServiceException {

    for (AccessPolicyInstance cursor : this.multiple) {
      create(cursor);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Called by the project to let the task do its work to modify one or more
   ** <code>Authentication/Authorization Policy</code>.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @throws ServiceException   if {@link #failonerror()} advice to throw an
   **                            exeption and one of the
   **                            <code>Authentication/Authorization Policy</code>s
   **                            does not exists or something else goes wrong
   **                            with the operation.
   */
  protected void modify()
    throws ServiceException {

    for (AccessPolicyInstance cursor : this.multiple) {
      modify(cursor);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Called by the project to let the task do its work to delete one or more
   ** <code>Authentication/Authorization Policy</code>.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @throws ServiceException   if {@link #failonerror()} advice to throw an
   **                            exeption and one of the
   **                            <code>Authentication/Authorization Policy</code>s
   **                            does not exists or something else goes wrong
   **                            with the operation.
   */
  protected void delete()
    throws ServiceException {

    for (AccessPolicyInstance cursor : this.multiple) {
      delete(cursor);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Creates an <code>Authentication/Authorization Policy</code> in Oracle
   ** Access Manager for the specified {@link AccessPolicyInstance}.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @param  instance           the <code>Authentication/Authorization Policy</code>
   **                            {@link AccessPolicyInstance} to create.
   **
   ** @throws ServiceException   if the <code>Access Policy</code> already
   **                            exists or something else goes wrong with the
   **                            operation.
   */
  private void create(final AccessPolicyInstance instance)
    throws ServiceException {

    warning(ServiceResourceBundle.format(ServiceMessage.OPERATION_CREATE_BEGIN, AccessPolicyProperty.ENTITY, instance.name()));
    final String         payload  = parseToString(instance.createRequest());
    trace(FeatureResourceBundle.format(FeatureMessage.SERVICE_REQUEST_PAYLOAD, payload));
    /*
    final String         answer   =  RegistrationProtocol.create(instance.serviceURL(), payload).transmit();
    trace(FeatureResourceBundle.format(FeatureMessage.SERVICE_RESPONSE_PAYLOAD, answer));
    final PolicyResponse response = fromString(answer);
    // checking if any error occurred on the server side and throwing
    // exception out to the client
    if (response.getErrorMsgs() == null) {
      info(ServiceResourceBundle.format(ServiceMessage.OPERATION_MODIFY_SUCCESS, AccessPolicyProperty.ENTITY, instance.name()));
    }
    else {
      final String[] arguments = {AccessPolicyProperty.ENTITY, instance.name(), response.getErrorMsgs().getMessage().get(0) };
      if (failonerror())
        throw new ServiceException(ServiceError.OPERATION_MODIFY_FAILED, arguments);
      else
        error(ServiceResourceBundle.format(ServiceError.OPERATION_MODIFY_FAILED, arguments));
    }
*/
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Modifies an <code>Authentication/Authorization Policy</code> in Oracle
   ** Access Manager for the specified {@link AccessPolicyInstance}.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @param  instance           the <code>Authentication/Authorization Policy</code>
   **                            {@link AccessPolicyInstance} to modify.
   **
   ** @throws ServiceException   if the <code>Access Policy</code> does not
   **                            exists or something else goes wrong with the
   **                            operation.
   */
  private void modify(final AccessPolicyInstance instance)
    throws ServiceException {

    warning(ServiceResourceBundle.format(ServiceMessage.OPERATION_MODIFY_BEGIN, AccessPolicyProperty.ENTITY, instance.name()));
    final String payload = parseToString(instance.modifyRequest());
    trace(FeatureResourceBundle.format(FeatureMessage.SERVICE_REQUEST_PAYLOAD, payload));

    final RegistrationProtocol.Response answer = RegistrationProtocol.create(instance.serviceURL(), payload).transmit();
    trace(FeatureResourceBundle.format(FeatureMessage.SERVICE_RESPONSE_PAYLOAD, answer));
    final PolicyResponse response = fromString(answer.content());
    // checking if any error occurred on the server side and throwing
    // exception out to the client
    if (response.getErrorMsgs() == null) {
      info(ServiceResourceBundle.format(ServiceMessage.OPERATION_MODIFY_SUCCESS, AccessPolicyProperty.ENTITY, instance.name()));
    }
    else {
      final String[] arguments = {AccessPolicyProperty.ENTITY, instance.name(), response.getErrorMsgs().getMessage().get(0) };
      if (failonerror())
        throw new ServiceException(ServiceError.OPERATION_MODIFY_FAILED, arguments);
      else
        error(ServiceResourceBundle.format(ServiceError.OPERATION_MODIFY_FAILED, arguments));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Deletes an <code>Authentication/Authorization Policy</code> in Oracle
   ** Access Manager for the specified {@link AccessPolicyInstance}.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @param  instance           the <code>Authentication/Authorization Policy</code>
   **                            {@link AccessPolicyInstance} to delete.
   **
   ** @throws ServiceException   if the <code>Access Policy</code> does not
   **                            exists or something else goes wrong with the
   **                            operation.
   */
  private void delete(final AccessPolicyInstance instance)
    throws ServiceException {

    warning(ServiceResourceBundle.format(ServiceMessage.OPERATION_DELETE_BEGIN, AccessPolicyProperty.ENTITY, instance.name()));
    final String payload = parseToString(instance.deleteRequest());
    trace(FeatureResourceBundle.format(FeatureMessage.SERVICE_REQUEST_PAYLOAD, payload));

    final RegistrationProtocol.Response answer = RegistrationProtocol.create(instance.serviceURL(), payload).transmit();
    trace(FeatureResourceBundle.format(FeatureMessage.SERVICE_RESPONSE_PAYLOAD, answer));
    final PolicyResponse response = fromString(answer.content());
    // checking if any error occurred on the server side and throwing
    // exception out to the client
    if (response.getErrorMsgs() == null) {
      info(ServiceResourceBundle.format(ServiceMessage.OPERATION_DELETE_SUCCESS, AccessPolicyProperty.ENTITY, instance.name()));
    }
    else {
      final String[] arguments = {AccessPolicyProperty.ENTITY, instance.name(), response.getErrorMsgs().getMessage().get(0) };
      if (failonerror())
        throw new ServiceException(ServiceError.OPERATION_DELETE_FAILED, arguments);
      else
        error(ServiceResourceBundle.format(ServiceError.OPERATION_DELETE_FAILED, arguments));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fromString
  /**
   ** This method unmarshalles an XML string to give a parsed object
   **
   ** @param  xml                the XML fragment to parse.
   **
   ** @return                    the root of content tree being unmarshalled.
   **
   ** @throws FeatureException
   */
  private static PolicyResponse fromString(final String xml)
    throws FeatureException {

    final Unmarshaller unmarshaller = createUnmarshaller();
    PolicyResponse response = null;
    try {
      response = (PolicyResponse)unmarshaller.unmarshal(new StreamSource(new StringReader(xml)));
    }
    catch (JAXBException e) {
      e.printStackTrace();
    }
    return response;
  }
}