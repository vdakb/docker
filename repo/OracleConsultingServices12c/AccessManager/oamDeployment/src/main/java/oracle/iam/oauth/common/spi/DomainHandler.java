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

    File        :   DomainHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DomainHandler.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.oauth.common.spi;

import java.util.List;
import java.util.ArrayList;

import java.io.StringReader;

import java.net.HttpURLConnection;

import org.apache.tools.ant.BuildException;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceMessage;
import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceOperation;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.hst.deployment.type.APIServerContext;

import oracle.hst.foundation.json.simple.JsonObject;
import oracle.hst.foundation.json.simple.JsonParser;
import oracle.hst.foundation.json.simple.JsonContext;

import oracle.hst.foundation.logging.TableFormatter;

import oracle.iam.oauth.common.FeatureError;
import oracle.iam.oauth.common.FeatureMessage;
import oracle.iam.oauth.common.FeatureResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class DomainHandler
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** <code>DomainHandler</code> creates, deletes and configures Identity
 ** Domains in an Oracle Access Manager infrastructure.
 ** <p>
 ** A <code>Identity Domain</code> is a entity that contain all artifacts
 ** required to provide standard OAuth Services .
 ** <p>
 ** Each <code>Identity Domain</code> is an independent entity.
 ** <br>
 ** One of the primary use cases of the <code>Identity Domain</code> is for
 ** multi tenants deployments. Each <code>Identity Domain</code> will correspond
 ** to a tenant. This can apply to different departments in an organization if
 ** there is a need for independence. This will also be useful for cloud
 ** deployments where each <code>Identity Domain</code> can correspond to a
 ** separate tenant or entity. The following artifacts are just some of the
 ** components configured within an OAuth Services <code>Identity Domain</code>.
 ** <ul>
 **   <li>One or more Clients
 **   <li>One or more Resource Servers
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DomainHandler extends FeatureHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String        CONTEXT  = "/oam/services/rest/ssa/api/v1/oauthpolicyadmin/oauthidentitydomain";

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
  public DomainHandler(final ServiceFrontend frontend, final ServiceOperation operation) {
    // ensure inheritance
    super(frontend, CONTEXT, operation);
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
   **                            {@link DomainInstance} with the same name.
   */
  public void add(final DomainInstance instance)
    throws BuildException {

    // prevent bogus input
    if (instance == null)
      throw new BuildException(FeatureResourceBundle.format(FeatureError.INSTANCE_MANDATORY, DomainProperty.ENTITY));

    // prevent bogus state
    if (this.flatten.contains(instance.name()))
      throw new BuildException(FeatureResourceBundle.format(FeatureError.INSTANCE_ONLYONCE, DomainProperty.ENTITY, instance.name()));

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
      throw new BuildException(FeatureResourceBundle.format(FeatureError.INSTANCE_MANDATORY, DomainProperty.ENTITY));

    try {
      final ServiceOperation operation = this.operation();
      for (DomainInstance cursor : this.multiple)
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
   ** <code>Identity Domain</code>.
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

    for (DomainInstance cursor : this.multiple)
      create(context, cursor);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Called by the project to let the task do its work to modify an
   ** <code>Identity Domain</code>.
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

    for (DomainInstance cursor : this.multiple)
      modify(context, cursor);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Called by the project to let the task do its work to delete an
   ** <code>Identity Domain</code>.
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

    for (DomainInstance cursor : this.multiple)
      delete(context, cursor);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   report
  /**
   ** Called by the project to let the task do its work to report an
   ** <code>Identity Domain</code>.
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

    for (DomainInstance instance : this.multiple) {
      lookup(context, instance);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Creates an <code>Identity Domain</code> in Oracle Access Manager for the
   ** specified {@link DomainInstance}.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @param  context            the {@link APIServerContext} this task
   **                            will use to do the work.
   ** @param  instance           the <code>Identity Domain</code>
   **                            {@link DomainInstance} to create.
   **
   ** @throws BuildException     if something goes wrong with the operation.
   */
  protected void create(final APIServerContext context, final DomainInstance instance)
    throws ServiceException {

    warning(ServiceResourceBundle.format(ServiceMessage.OPERATION_CREATE_BEGIN, DomainProperty.ENTITY, instance.name()));
    final String payload = instance.marshal();
    trace(FeatureResourceBundle.format(FeatureMessage.SERVICE_REQUEST_PAYLOAD, payload));

    if (exists(context, instance)) {
      warning(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_EXISTS, DomainProperty.ENTITY, instance.name()));
      modify(context, instance);
    }
    else {
      try {
        final Response response = invoke(connection(context, CREATE, payload));
        trace(FeatureResourceBundle.format(FeatureMessage.SERVICE_RESPONSE_PAYLOAD, response));
        if (response.code() == HttpURLConnection.HTTP_OK)
          info(ServiceResourceBundle.format(ServiceMessage.OPERATION_CREATE_SUCCESS, DomainProperty.ENTITY, instance.name()));
        else
          error(ServiceResourceBundle.format(ServiceError.OPERATION_CREATE_FAILED, DomainProperty.ENTITY, instance.name(), response.message()));
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
   ** Modifies an <code>Identity Domain</code> in Oracle Access Manager for the
   ** specified {@link DomainInstance}.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @param  context            the {@link APIServerContext} this task
   **                            will use to do the work.
   ** @param  instance           the <code>Identity Domain</code>
   **                            {@link DomainInstance} to modify.
   **
   ** @throws ServiceException   if something goes wrong with the operation.
   */
  protected void modify(final APIServerContext context, final DomainInstance instance)
    throws ServiceException {

    warning(ServiceResourceBundle.format(ServiceMessage.OPERATION_MODIFY_BEGIN, DomainProperty.ENTITY, instance.name()));
    final String payload = instance.marshal();
    trace(FeatureResourceBundle.format(FeatureMessage.SERVICE_REQUEST_PAYLOAD, payload));

    if (exists(context, instance)) {
      try {
        final Response response = invoke(connection(context, MODIFY, payload));
        trace(FeatureResourceBundle.format(FeatureMessage.SERVICE_RESPONSE_PAYLOAD, response));
        if (response.code() == HttpURLConnection.HTTP_OK)
          info(ServiceResourceBundle.format(ServiceMessage.OPERATION_MODIFY_SUCCESS, DomainProperty.ENTITY, instance.name()));
        else
          error(ServiceResourceBundle.format(ServiceError.OPERATION_MODIFY_FAILED, DomainProperty.ENTITY, instance.name(), response.message()));
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
      error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, DomainProperty.ENTITY, instance.name()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Deletes an <code>Identity Domain</code> in Oracle Access Manager for the
   ** specified {@link DomainInstance}.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @param  context            the {@link APIServerContext} this task
   **                            will use to do the work.
   ** @param  instance           the <code>Identity Domain</code>
   **                            {@link DomainInstance} to delete.
   **
   ** @throws ServiceException   if something goes wrong with the operation.
   */
  protected void delete(final APIServerContext context, final DomainInstance instance)
    throws ServiceException {

    warning(ServiceResourceBundle.format(ServiceMessage.OPERATION_DELETE_BEGIN, DomainProperty.ENTITY, instance.name()));
    final String payload = instance.marshal();
    trace(FeatureResourceBundle.format(FeatureMessage.SERVICE_REQUEST_PAYLOAD, payload));

    if (exists(context, instance)) {
      try {
        final Response response = invoke(request(context, DELETE, instance));
        trace(FeatureResourceBundle.format(FeatureMessage.SERVICE_RESPONSE_PAYLOAD, response));
        if (response.code() == HttpURLConnection.HTTP_OK)
          info(ServiceResourceBundle.format(ServiceMessage.OPERATION_DELETE_SUCCESS, DomainProperty.ENTITY, instance.name()));
        else
          error(ServiceResourceBundle.format(ServiceError.OPERATION_DELETE_FAILED, DomainProperty.ENTITY, instance.name(), response.message()));
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
      error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, DomainProperty.ENTITY, instance.name()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** Reports the configuration of an <code>Identity Domain</code> in Oracle
   ** Access Manager for the specified {@link DomainInstance}.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @param  context            the {@link APIServerContext} this task
   **                            will use to do the work.
   ** @param  instance           the <code>Identity Domain</code>
   **                            {@link DomainInstance} to report.
   **
   ** @throws BuildException     if something goes wrong with the operation.
   */
  protected void lookup(final APIServerContext context, final DomainInstance instance)
    throws ServiceException {

    warning(FeatureResourceBundle.format(FeatureMessage.OPERATION_REPORT_BEGIN, DomainProperty.ENTITY, instance.name()));
    final Response response = invoke(request(context, LOOKUP, instance));
    trace(FeatureResourceBundle.format(FeatureMessage.SERVICE_RESPONSE_PAYLOAD, response));
    if (response.code() == HttpURLConnection.HTTP_OK) {
      printResponse(response.content());
      info(FeatureResourceBundle.format(FeatureMessage.OPERATION_REPORT_SUCCESS, DomainProperty.ENTITY, instance.name()));
    }
    else if (response.code() == 422) {
      error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, DomainProperty.ENTITY, instance.name()));
    }
    else {
      error(FeatureResourceBundle.format(FeatureError.OPERATION_PRINT_FAILED, DomainProperty.ENTITY, instance.name(), response.message()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exists
  /**
   ** Returns an existing <code>Identity Domain</code> from Oracle Access
   ** Manager for the specified {@link DomainInstance}.
   **
   ** @param  context            the {@link APIServerContext} this task
   **                            will use to do the work.
   ** @param  instance           the <code>Identity Domain</code>
   **                            {@link DomainInstance} to verify.
   **
   ** @throws ServiceException  if the {@link HttpURLConnection} cannot be
   **                           created.
   */
  protected boolean exists(final APIServerContext context, final DomainInstance instance)
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
  private HttpURLConnection request(final APIServerContext context, final String requestMethod, final DomainInstance instance)
    throws ServiceException {

    HttpURLConnection request = null;
    if (StringUtility.isEmpty(instance.id()))
      request = connection(context, requestMethod, DomainProperty.NAME.id(), instance.name());
    else
      request = connection(context, requestMethod, DomainProperty.NAME.id(), instance.id());
    return request;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printResponse
  /**
   ** What the hell we have to do by parsing the bullshit response we got from
   ** the <code>GET</code> and <code>DELETE</code> REST API calls as a kind of
   ** something unstructured and JSON-Format.
   ** <br>
   ** The entire response looks like, e.g.:
   ** <pre>
   **   Sucessfully retrieved entity - OAuthIdentityDomain, detail - OAuth Identity Domain :: Name - AccessManagerOpenID, Id - c0c8662d5d544835972deb3ea5646bbb, Description - null, TrustStore Identifiers - [AccessManagerOpenID], Identity Provider - UserIdentityStore1, TokenSettings - [{"tokenType":"ACCESS_TOKEN","tokenExpiry":3600,"lifeCycleEnabled":false,"refreshTokenEnabled":false,"refreshTokenExpiry":86400,"refreshTokenLifeCycleEnabled":false}, {"tokenType":"AUTHZ_CODE","tokenExpiry":3600,"lifeCycleEnabled":false,"refreshTokenEnabled":false,"refreshTokenExpiry":86400,"refreshTokenLifeCycleEnabled":false}, {"tokenType":"SSO_LINK_TOKEN","tokenExpiry":3600,"lifeCycleEnabled":false,"refreshTokenEnabled":false,"refreshTokenExpiry":86400,"refreshTokenLifeCycleEnabled":false}], ConsentPageURL - /oam/pages/consent.jsp, ErrorPageURL - /oam/pages/servererror.jsp, CustomAttrs - null
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
  private void printResponse(final String message) {
    final TableFormatter report = new TableFormatter();
    report.header(FeatureResourceBundle.string(FeatureMessage.IDENTITY_DOMAIN_PROPERTY)).header(FeatureResourceBundle.string(FeatureMessage.IDENTITY_DOMAIN_VALUE));

    final JsonParser     parser = JsonContext.deserializer(new StringReader(message));
    while (parser.hasNext()) {
      final JsonObject domain = parser.next().asObject();
      for (DomainProperty property : DomainProperty.values()) {
        switch (property) {
          case ID                    :
          case NAME                  :
          case TOKENTYPE             :
          case DESCRIPTION           :
          case ERRORPAGEURL          :
          case CONSENTPAGEURL        :
          case CUSTOMATTRIBUTES      :
          case IDENTITYPROVIDER      :
          case TRUSTSTOREIDENTIFIER  : report.row().column(property.id).column(domain.getString(property.id));
                                       break;
          case TOKENLIFECYCLE        :
          case REFRESHTOKENENABLED   :
          case REFRESHTOKENLIFECYCLE : report.row().column(property.id).column(domain.getBoolean(property.id, false));
                                       break;
          case TOKENEXPIRY           :
          case REFRESHTOKENEXPIRY    : report.row().column(property.id).column(domain.getInt(property.id));
                                       break;
          case TOKENSETTINGS         : break;
          default                    : report.row().column(property.id).column("???");
        }
      }
    }
    parser.close();
    report.print();
  }
}