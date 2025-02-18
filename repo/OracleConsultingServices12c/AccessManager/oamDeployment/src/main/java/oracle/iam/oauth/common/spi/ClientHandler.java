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

    File        :   ClientHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ClientHandler.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.oauth.common.spi;

import java.io.StringReader;

import java.util.List;
import java.util.ArrayList;

import java.net.HttpURLConnection;

import java.util.Collection;

import org.apache.tools.ant.BuildException;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceMessage;
import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceOperation;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.hst.deployment.type.APIServerContext;

import oracle.hst.foundation.json.simple.JsonContext;
import oracle.hst.foundation.json.simple.JsonObject;
import oracle.hst.foundation.json.simple.JsonParser;
import oracle.hst.foundation.logging.TableFormatter;

import oracle.iam.oauth.common.FeatureError;
import oracle.iam.oauth.common.FeatureMessage;
import oracle.iam.oauth.common.FeatureResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class ClientHandler
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** Invokes the Runtime REST API to maintain <code>Client</code>s in Oracle
 ** Access Manager infrastructure.
 ** <p>
 ** A <code>Client</code> initiates the OAuth protocol by invoking the OAuth
 ** Services.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ClientHandler extends FeatureHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String CONTEXT = "/oam/services/rest/ssa/api/v1/oauthpolicyadmin/client";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final List<DomainInstance> multiple = new ArrayList<DomainInstance>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>DomainHandler</code> to initialize the instance.
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
  public ClientHandler(final ServiceFrontend frontend, final ServiceOperation operation) {
    // ensure inheritance
    super(frontend, CONTEXT, operation);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Call by the ANT deployment to inject the argument for adding an domain.
   **
   ** @param  instance           the instance to add.
   **
   ** @throws BuildException     if this instance is already referencing an
   **                            {@link DomainInstance} with the same name.
   */
  public void add(final DomainInstance instance)
    throws BuildException {

    // prevent bogus input
    if (instance == null)
      throw new BuildException(FeatureResourceBundle.format(FeatureError.INSTANCE_MANDATORY, ClientProperty.ENTITY));

    // prevent bogus state
    if (this.flatten.contains(instance.name()))
      throw new BuildException(FeatureResourceBundle.format(FeatureError.INSTANCE_ONLYONCE, ClientProperty.ENTITY, instance.name()));

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
      throw new BuildException(FeatureResourceBundle.format(FeatureError.INSTANCE_MANDATORY, ClientProperty.ENTITY));

    final ServiceOperation operation = this.operation();
    try {
      for (DomainInstance cursor : this.multiple) {
        // perform basic validation on the container instance to meet the
        // minimum requirements of a domain
        cursor.validate(ServiceOperation.none);
        // check the resource clients are assigned to the domain and complain if
        // not
        final Collection<ClientInstance> client = cursor.clients();
        if (client.size() == 0)
          throw new BuildException(ServiceResourceBundle.string(ServiceError.OBJECT_ELEMENT_MANDATORY));

        // validate each of the resource clients assigned to the domain
        for (ClientInstance subject : client) {
          subject.validate(operation);
        }
      }
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
   ** <code>Resource Client</code>.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @param  context            the {@link APIServerContext} this task
   **                            will use to do the work.
   **
   ** @throws ServiceException   if something goes wrong with the build
   */
  public void create(final APIServerContext context)
    throws ServiceException {

    for (DomainInstance domain : this.multiple)
      for (ClientInstance client : domain.clients())
        create(context, client);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Called by the project to let the task do its work to modify an
   ** <code>Resource Client</code>.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @param  context            the {@link APIServerContext} this task
   **                            will use to do the work.
   **
   ** @throws ServiceException   if something goes wrong with the build
   */
  public void modify(final APIServerContext context)
    throws ServiceException {

    for (DomainInstance domain : this.multiple)
      for (ClientInstance client : domain.clients())
        modify(context, client);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Called by the project to let the task do its work to delete an
   ** <code>Resource Client</code>.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @param  context            the {@link APIServerContext} this task
   **                            will use to do the work.
   **
   ** @throws ServiceException   if something goes wrong with the build
   */
  public void delete(final APIServerContext context)
    throws ServiceException {

    for (DomainInstance domain : this.multiple)
      for (ClientInstance client : domain.clients())
        delete(context, client);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   report
  /**
   ** Called by the project to let the task do its work to report an
   ** <code>Resource Client</code>.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @param  context            the {@link APIServerContext} this task
   **                            will use to do the work.
   **
   ** @throws ServiceException   if something goes wrong with the build
   */
  public void report(final APIServerContext context)
    throws ServiceException {

    for (DomainInstance domain : this.multiple)
      for (ClientInstance client : domain.clients())
        lookup(context, client);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Creates an <code>Resource Client</code> in Oracle Access Manager for the
   ** specified {@link ClientInstance}.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @param  context            the {@link APIServerContext} this task
   **                            will use to do the work.
   ** @param  instance           the <code>Resource Client</code>
   **                            {@link ClientInstance} to create.
   **
   ** @throws BuildException     if something goes wrong with the operation.
   */
  protected void create(final APIServerContext context, final ClientInstance instance)
    throws ServiceException {

    warning(ServiceResourceBundle.format(ServiceMessage.OPERATION_CREATE_BEGIN, ClientProperty.ENTITY, instance.name()));
    final String payload = instance.marshal();
    trace(FeatureResourceBundle.format(FeatureMessage.SERVICE_REQUEST_PAYLOAD, payload));

    if (exists(context, instance)) {
      warning(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_EXISTS, ClientProperty.ENTITY, instance.name()));
      modify(context, instance);
    }
    else {
      try {
        final Response response = invoke(connection(context, CREATE, payload));
        trace(FeatureResourceBundle.format(FeatureMessage.SERVICE_RESPONSE_PAYLOAD, response));
        if (response.code() == HttpURLConnection.HTTP_OK)
          info(ServiceResourceBundle.format(ServiceMessage.OPERATION_CREATE_SUCCESS, ClientProperty.ENTITY, instance.name()));
        // if this error occurs we need to verify our validation outine on the
        // instance due to it looks like that some required parameter are missed
        else if (response.code() == HttpURLConnection.HTTP_BAD_REQUEST) {
          error(ServiceResourceBundle.format(ServiceError.OPERATION_CREATE_FAILED, ClientProperty.ENTITY, instance.name(), response.content() != null ? response.content(): response.message()));
        }
        // should not happens but be to be safe check it again due to exists
        // might not worked correctly
        else if (response.code() == 422) {
          error(ServiceResourceBundle.format(ServiceError.OPERATION_CREATE_FAILED, ClientProperty.ENTITY, instance.name(), response.message()));
        }
        else
          error(ServiceResourceBundle.format(ServiceError.OPERATION_CREATE_FAILED, ClientProperty.ENTITY, instance.name(), response.message()));
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
   ** Modifies an <code>Resource Client</code> in Oracle Access Manager for the
   ** specified {@link ClientInstance}.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @param  context            the {@link APIServerContext} this task
   **                            will use to do the work.
   ** @param  instance           the <code>Resource Client</code>
   **                            {@link ClientInstance} to modify.
   **
   ** @throws ServiceException   if something goes wrong with the operation.
   */
  protected void modify(final APIServerContext context, final ClientInstance instance)
    throws ServiceException {

    warning(ServiceResourceBundle.format(ServiceMessage.OPERATION_MODIFY_BEGIN, ClientProperty.ENTITY, instance.name()));
    final String payload = instance.marshal();
    trace(FeatureResourceBundle.format(FeatureMessage.SERVICE_REQUEST_PAYLOAD, payload));

    if (exists(context, instance)) {
      // the API is highly inconsistent
      // for read operations the query parameter has to be identityDomainName
      // for creating or updating operations it has to be idDomain
      // this unbelivable behavior requires extra code to make the stuff
      // functional
      final String[][] parameter = {{"idDomain", instance.domain()},{ClientProperty.NAME.id(), instance.name()}};
      try {
        final Response response = invoke(connection(context, MODIFY, parameter, payload));
        trace(FeatureResourceBundle.format(FeatureMessage.SERVICE_RESPONSE_PAYLOAD, response));
        if (response.code() == HttpURLConnection.HTTP_OK)
          info(ServiceResourceBundle.format(ServiceMessage.OPERATION_MODIFY_SUCCESS, ClientProperty.ENTITY, instance.name()));
        // if this error occurs we need to verify our validation outine on the
        // instance due to it looks like that some required parameter are missed
        else if (response.code() == HttpURLConnection.HTTP_BAD_REQUEST) {
          error(ServiceResourceBundle.format(ServiceError.OPERATION_MODIFY_FAILED, ClientProperty.ENTITY, instance.name(), response.content() != null ? response.content(): response.message()));
        }
        // should not happens but be to be safe check it again due to exists
        // might not worked correctly
        else if (response.code() == 422) {
          error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, ClientProperty.ENTITY, instance.name()));
        }
        else
          error(ServiceResourceBundle.format(ServiceError.OPERATION_MODIFY_FAILED, ClientProperty.ENTITY, instance.name(), response.message()));
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
      error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, ClientProperty.ENTITY, instance.name()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Deletes an <code>Resource Client</code> in Oracle Access Manager for the
   ** specified {@link ClientInstance}.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @param  context            the {@link APIServerContext} this task
   **                            will use to do the work.
   ** @param  instance           the <code>Resource Client</code>
   **                            {@link ClientInstance} to delete.
   **
   ** @throws ServiceException   if something goes wrong with the operation.
   */
  protected void delete(final APIServerContext context, final ClientInstance instance)
    throws ServiceException {

    warning(ServiceResourceBundle.format(ServiceMessage.OPERATION_DELETE_BEGIN, ClientProperty.ENTITY, instance.name()));
    final String payload = instance.marshal();
    trace(FeatureResourceBundle.format(FeatureMessage.SERVICE_REQUEST_PAYLOAD, payload));

    if (exists(context, instance)) {
      try {
        final Response response = invoke(request(context, DELETE, instance));
        trace(FeatureResourceBundle.format(FeatureMessage.SERVICE_RESPONSE_PAYLOAD, response));
        if (response.code() == HttpURLConnection.HTTP_OK)
          info(ServiceResourceBundle.format(ServiceMessage.OPERATION_DELETE_SUCCESS, ClientProperty.ENTITY, instance.name()));
        // should not happens but be to be safe check it again due to exists
        // might not worked correctly
        else if (response.code() == 422) {
          error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, ClientProperty.ENTITY, instance.name()));
        }
        else
          error(ServiceResourceBundle.format(ServiceError.OPERATION_DELETE_FAILED, ClientProperty.ENTITY, instance.name(), response.message()));
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
      error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, ClientProperty.ENTITY, instance.name()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** Reports the configuration of an <code>Resource Client</code> in Oracle
   ** Access Manager for the specified {@link ClientInstance}.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @param  context            the {@link APIServerContext} this task
   **                            will use to do the work.
   ** @param  instance           the <code>Resource Client</code>
   **                            {@link ClientInstance} to report.
   **
   ** @throws BuildException     if something goes wrong with the operation.
   */
  protected void lookup(final APIServerContext context, final ClientInstance instance)
    throws ServiceException {

    warning(FeatureResourceBundle.format(FeatureMessage.OPERATION_REPORT_BEGIN, ClientProperty.ENTITY, instance.name()));
    final Response response = invoke(request(context, LOOKUP, instance));
    trace(FeatureResourceBundle.format(FeatureMessage.SERVICE_RESPONSE_PAYLOAD, response));
    if (response.code() == HttpURLConnection.HTTP_OK) {
      parseResponse(response.content());
      info(FeatureResourceBundle.format(FeatureMessage.OPERATION_REPORT_SUCCESS, ClientProperty.ENTITY, instance.name()));
    }
    else if (response.code() == 422) {
      error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, ClientProperty.ENTITY, instance.name()));
    }
    else {
      error(FeatureResourceBundle.format(FeatureError.OPERATION_PRINT_FAILED, ClientProperty.ENTITY, instance.name(), response.message()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exists
  /**
   ** Returns an existing <code>Resource Client</code> from Oracle Access
   ** Manager for the specified {@link ClientInstance}.
   **
   ** @param  context            the {@link APIServerContext} this task
   **                            will use to do the work.
   ** @param  instance           the <code>Resource Client</code>
   **                            {@link ClientInstance} to verify.
   **
   ** @throws ServiceException  if the {@link HttpURLConnection} cannot be
   **                           created.
   */
  protected boolean exists(final APIServerContext context, final ClientInstance instance)
    throws ServiceException {

    final Response response = invoke(request(context, LOOKUP, instance));
    return (response.code() == HttpURLConnection.HTTP_OK);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   request
  /**
   ** Factory method to create a {@link HttpURLConnection} for the purpose of
   ** form-urlencoded requests.
   **
   ** @param  context            the {@link APIServerContext} providing
   **                            information how to locate the requested
   **                            resources and the credentials required to
   **                            authenticate.
   ** @param  requestMethod      the HTTP method.
   **                            <ul>
   **                              <li>GET
   **                              <li>DELETE
   **                            </ul>
   **                            are legal, subject to protocol restrictions.
   **
   ** @return                    a common {@link HttpURLConnection}.
   **
   ** @throws ServiceException  if the {@link HttpURLConnection} cannot be
   **                           created.
   */
  private HttpURLConnection request(final APIServerContext context, final String requestMethod, final ClientInstance instance)
    throws ServiceException {

    if (StringUtility.isEmpty(instance.id())) {
      // the API is highly inconsistent
      // for read operations the query parameter has to be identityDomainName
      // for creating or updating operations it has to be idDomain
      // this unbelivable behavior requires extra code to make the stuff
      // functional
      final String[][] parameter = {{"identityDomainName", instance.domain()},{ResourceProperty.NAME.id(), instance.name()}};
      return connection(context, requestMethod, parameter);
    }
    else {
      final String[][] parameter = {{"identityDomainId", instance.domain()},{ResourceProperty.ID.id(), instance.id()}};
      return connection(context, requestMethod, parameter);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parseResponse
  /**
   ** What the hell we have to do by parsing the bullshit response we got from
   ** the <code>GET</code> and <code>DELETE</code> REST API calls as a kind of
   ** something unstructured and JSON-Format.
   ** <br>
   ** The entire response looks like, e.g.:
   ** <pre>
   **   Sucessfully retrieved entity - OAuthClient, detail - OAuth Client - uid = 7133ee7d-9e56-4b4c-874b-4e89aab44f40, name = playground-hcm, id = fcd26a8839ff4b91a57ae95d839e34b9, identityDomain = PlayGroundIdentityDomain, description = The client making resource requests on behalf of the resource owner with the resource owners authorization., secret = ******, , clientType = CONFIDENTIAL_CLIENT, grantTypes = [CLIENT_CREDENTIALS], attributes = [{"attrName":"userName","attrValue":"$user.attr.cn","attrType":DYNAMIC}, {"attrName":"lastName","attrValue":"$user.attr.sn","attrType":DYNAMIC}, {"attrName":"firstName","attrValue":"$user.attr.givenName","attrType":DYNAMIC}, {"attrName":"email","attrValue":"$user.attr.mail","attrType":DYNAMIC}, {"attrName":"memberOf","attrValue":"$user.attr.orclMemberOf","attrType":DYNAMIC}, {"attrName":"sessionId","attrValue":"$session.id","attrType":DYNAMIC}, {"attrName":"sessionId","attrValue":"$session.id","attrType":DYNAMIC}], scopes = [PlayGroundResourceServer.all], defaultScope = PlayGroundResourceServer.all, redirectURIs = []
   ** </pre>
   ** We need to split the response by ' :: ' to strip of the useless
   ** informational part. The interesting part will be found at index 1 of the
   ** string array that is were we will take care on
   ** <pre>
   **   String message = result.split("\\s::\\s")[1];
   ** </pre>
   ** Afterwards we has to walk through the message response to figure out the
   ** value pairs that are a combination of name and value separated by a hyphen
   ** (<code>'-'</code>) character.
   ** <pre>
   **   int pos   = message.indexOf("-");
   ** </pre>
   ** But wait the value of token settings looks like a JSON-String this we will
   ** transform using a JsonParser.
   */
  private void parseResponse(final String message) {
    final TableFormatter report = new TableFormatter();
    report.header(FeatureResourceBundle.string(FeatureMessage.RESOURCE_CLIENT_PROPERTY)).header(FeatureResourceBundle.string(FeatureMessage.RESOURCE_CLIENT_VALUE));

    final JsonParser     parser = JsonContext.deserializer(new StringReader(message));
    while (parser.hasNext()) {
      final JsonObject    server = parser.next().asObject();
      for (ClientProperty property : ClientProperty.values()) {
        switch (property) {
          case ID               :
          case UID              :
          case NAME             :
          case SECRET           :
          case DOMAIN           :
          case DESCRIPTION      :
          case DEFAULTSCOPE     :
          case REDIRECTURL      :
          case REDIRECTSECURE   : report.row().column(property.id).column(server.get(property.id));
                                  break;
          default               : report.row().column(property.id).column("???");
        }
      }
    }
    parser.close();
    report.print();
  }
}