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

    Copyright © 2021 All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Service Simulation
    Subsystem   :   Google API Gateway

    File        :   TenantHandler.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    TenantResultHandler.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2021-28-01  DSteding    First release version
*/

package oracle.iam.system.simulation.apigee.service;

import java.io.IOException;
import java.io.InputStream;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

import com.fasterxml.jackson.core.JsonParser;

import com.fasterxml.jackson.databind.ObjectReader;

import java.io.BufferedReader;

import java.io.InputStreamReader;

import java.io.UnsupportedEncodingException;

import java.net.URLDecoder;

import java.util.stream.Collectors;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.system.simulation.ServiceError;
import oracle.iam.system.simulation.BadRequestException;
import oracle.iam.system.simulation.ProcessingException;
import oracle.iam.system.simulation.ServerErrorException;
import oracle.iam.system.simulation.NotImplementedException;

import oracle.iam.system.simulation.resource.ServiceBundle;

import oracle.iam.system.simulation.rest.schema.Resource;

import oracle.iam.system.simulation.scim.schema.Support;

import oracle.iam.system.simulation.apigee.schema.User;
import oracle.iam.system.simulation.apigee.schema.Tenant;
import oracle.iam.system.simulation.apigee.schema.UserRole;

import oracle.iam.system.simulation.apigee.persistence.Provider;
import oracle.iam.system.simulation.apigee.persistence.Account;
import oracle.iam.system.simulation.apigee.persistence.Organization;
import oracle.iam.system.simulation.apigee.schema.Developer;

////////////////////////////////////////////////////////////////////////////////
// class TenantHandler
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** A handler which is invoked to process REST organization resource exchanges.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
class TenantHandler extends AbstractHandler<Resource> {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TenantHandler</code> context handler.
   **
   ** @param  server             the HTTP server that handles this context
   **                            handler.
   **                            <br>
   **                            Allowed object is {@link HttpServer}.
   ** @param  contextURI         the context mapped to this handler by the HTTP
   **                            server.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  private TenantHandler(final HttpServer server, final String context) {
    // ensure inheritance
    super(server, context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:    get (AbstractHandler)
  /**
   ** Handle the given <code>GET</code>request and generate an appropriate
   ** response.
   ** <br>
   ** See {@link HttpExchange} for a description of the steps involved in
   ** handling an exchange.
   **
   ** @param  t                  the exchange containing the request from the
   **                            client and used to send the response.
   **                            <br>
   **                            Allowed object is {@link HttpExchange}.
   **
   ** @return                    the process result of the operation to be
   **                            returned to the client.
   **                            <br>
   **                            Possible object is {@link Response}.
   **
   ** @throws ProcessingException if the request could not be handled anyway.
   */
  @Override
  public Response<Resource> get(final HttpExchange t)
    throws ProcessingException {

    final String[] path = t.getRequestURI().getPath().split("/");
    final int      type = path.length;
    Resource result = null;
    // verify if a known resource is requested means the last part of the path
    // is not the request context mapped to this handler
    switch(path[type - 1]) {
      case Server.CONTEXT_TENANT      : result = Provider.instance.organizationSearch();
                                        break;
      case Server.CONTEXT_ROLE        : result = Provider.instance.userroleSearch(path[type - 2]);
                                        break;
      case Server.CONTEXT_USER        : result = Provider.instance.userroleMember(path[type - 4], path[type - 2]);
                                        break;
      case Server.CONTEXT_COMPANY     : result = Provider.instance.companySearch(path[type - 2]);
                                        break;
      case Server.CONTEXT_PRODUCT     : result = Provider.instance.productSearch(path[type - 2]);
                                        break;
      case Server.CONTEXT_DEVELOPER   : result = Provider.instance.developerSearch(path[type - 2]);
                                        break;
      case Server.CONTEXT_APPLICATION : result = Provider.instance.applicationSearch(path[type - 2]);
                                        break;
      // second try the get method comae with a parameter
      default                    : switch(path[type - 2]) {
                                     case Server.CONTEXT_TENANT      : result = Provider.instance.organizationLookup(path[type - 1]);
                                                                       break;
                                     case Server.CONTEXT_ROLE        : result = Provider.instance.userroleMember(path[type - 3], path[type - 1]);
                                                                       break;
                                     case Server.CONTEXT_USER        : result = Provider.instance.userroleMember(path[type - 5], path[type - 3], path[type - 1]);
                                                                       break;
                                     case Server.CONTEXT_COMPANY     : result = Provider.instance.companyLookup(path[type - 3], path[type - 1]);
                                                                       break;
                                     case Server.CONTEXT_PRODUCT     : result = Provider.instance.productLookup(path[type - 3], path[type - 1]);
                                                                       break;
                                     case Server.CONTEXT_DEVELOPER   : result = Provider.instance.developerLookup(path[type - 3], path[type - 1]);
                                                                       break;
                                     case Server.CONTEXT_APPLICATION : result = Provider.instance.applicationLookup(path[type - 3], path[type - 1]);
                                                                       break;
                                     default                         : throw new NotImplementedException();
                                   }
    }
    if (result == null)
      throw new BadRequestException(path[type - 2], "Blaa");

    return createResponse(200, result);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:    post (AbstractHandler)
  /**
   ** Handle the given <code>POST</code>request and generate an appropriate
   ** response.
   ** <br>
   ** See {@link HttpExchange} for a description of the steps involved in
   ** handling an exchange.
   **
   ** @param  t                  the exchange containing the request from the
   **                            client and used to send the response.
   **                            <br>
   **                            Allowed object is {@link HttpExchange}.
   **
   ** @return                    the process result of the operation to be
   **                            returned to the client.
   **                            <br>
   **                            Possible object is {@link Response} for type
   **                            {@link User}.
   **
   ** @throws ProcessingException if the request could not be handled anyway.
   */
  @Override
  public Response<Resource> post(final HttpExchange t)
    throws ProcessingException {

    final String[] path = t.getRequestURI().getPath().split("/");
    final int      type = path.length;
    switch (path[type - 1]) {
      case Server.CONTEXT_TENANT    : return createResponse(201, organizationCreate(t.getRequestBody()));
      case Server.CONTEXT_ROLE      : return createResponse(200, userroleCreate(path[type - 2], t.getRequestBody()));
      case Server.CONTEXT_USER      : return createResponse(200, userroleAssign(path[type - 4], path[type - 2], t.getRequestBody()));
      case Server.CONTEXT_DEVELOPER : return createResponse(201, developerCreate(path[type - 2], t.getRequestBody()));
      default                       : throw new NotImplementedException();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:    put (AbstractTransactionHandler)
  /**
   ** Handle the given <code>PUT</code>request and generate an appropriate
   ** response.
   ** <br>
   ** See {@link HttpExchange} for a description of the steps involved in
   ** handling an exchange.
   **
   ** @param  t                  the exchange containing the request from the
   **                            client and used to send the response.
   **                            <br>
   **                            Allowed object is {@link HttpExchange}.
   **
   ** @return                    the process result of the operation to be
   **                            returned to the client.
   **                            <br>
   **                            Possible object is {@link Response}.
   **
   ** @throws ProcessingException if the request could not be handled anyway.
   */
  @Override
  public Response<Resource> put(final HttpExchange t)
    throws ProcessingException {

    final String[] path = t.getRequestURI().getPath().split("/");
    final int      type = path.length;
    switch (path[type - 2]) {
      case Server.CONTEXT_TENANT    : return createResponse(200, organizationModify(path[type - 1], t.getRequestBody()));
      case Server.CONTEXT_ROLE      : return createResponse(200, userroleModify(path[type - 3], path[type - 1], t.getRequestBody()));
      case Server.CONTEXT_DEVELOPER : return createResponse(200, developerModify(path[type - 3], path[type - 1], t.getRequestBody()));
      default                       : throw new NotImplementedException();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:    delete (AbstractTransactionHandler)
  /**
   ** Handle the given <code>DELETE</code>request and generate an appropriate
   ** response.
   ** <br>
   ** See {@link HttpExchange} for a description of the steps involved in
   ** handling an exchange.
   **
   ** @param  t                  the exchange containing the request from the
   **                            client and used to send the response.
   **                            <br>
   **                            Allowed object is {@link HttpExchange}.
   **
   ** @throws ProcessingException if the request could not be handled anyway.
   */
  @Override
  public Response<Resource> delete(final HttpExchange t)
    throws ProcessingException {

    final String[] path = t.getRequestURI().getPath().split("/");
    final int      type = path.length;
    // verify if a known resource is requested means the last part of the path
    // is not the request context mapped to this handler
    switch(path[type - 2]) {
      case Server.CONTEXT_TENANT    : Provider.instance.organizationDelete(path[type - 1]);
                                      break;
      case Server.CONTEXT_ROLE      : Provider.instance.userroleDelete(path[type - 3], path[type - 1]);
                                      break;
      case Server.CONTEXT_USER      : Provider.instance.userroleRevoke(path[type - 5], path[type - 3], path[type - 1]);
                                      break;
      case Server.CONTEXT_DEVELOPER : Provider.instance.developerDelete(path[type - 3], path[type - 1]);
                                      break;
      default                       : throw new NotImplementedException();
    }
    return createResponse(204, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attach
  /**
   ** Factory method to create a <code>UserHandler</code> context handler and
   ** maps the handler created to the context URI {@link Provider#CONTEXT_USER}.
   ** <br>
   ** A HttpContext represents a mapping from a URI path to the exchange handler
   ** on a HttpServer.
   ** <br>
   ** Once created, all requests received by the server for the path will be
   ** handled by calling the given handler object. The context is identified by
   ** the path, and can later be removed from the server using this with the
   ** removeContext(String) method.
   ** <p>
   ** The path specifies the root URI path for this context. The first character
   ** of path <b>must</b> be '/'.
   ** <p>
   ** The class overview describes how incoming request URIs are mapped to
   ** {@link HttpContext} instances.
   **
   ** @param  server             the HTTP server that handles the
   **                            <code>UserHandler</code> context handler.
   **                            <br>
   **                            Allowed object is {@link HttpServer}.
   ** @param  contextURI         the context mapped to this handler by the HTTP
   **                            server.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  static void attach(final HttpServer server, final String context) {
    new TenantHandler(server, context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationCreate
  /**
   ** Create an organization in the database.
   ** <p>
   ** After this method completed sucessfully the metadata of the given resource
   ** updated accordingly.
   **
   ** @param  resource           the {@link InputStream} to unmarshall from the
   **                            request.
   **                            <br>
   **                            Allowed object is {@link InputStream}.
   **
   ** @throws ProcessingException if the account cannot be created.
   */
  private Resource organizationCreate(final InputStream resource)
    throws ProcessingException {

    Tenant request = unmarshal(resource, Tenant.class);
    request.version("00000000").createdBy(this.principal.username).modifiedBy(this.principal.username);
    validate(request);
    return Provider.instance.organizationCreate(request);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationModify
  /**
   ** Modify an organization in the database.
   ** <p>
   ** After this method completed sucessfully the metadata of the given resource
   ** updated accordingly.
   **
   ** @param  tenant             the system identifier of the tenant resource
   **                            to update.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  resource           the {@link InputStream} to unmarshall from the
   **                            request.
   **                            <br>
   **                            Allowed object is {@link InputStream}.
   **
   ** @throws ProcessingException if the account cannot be created.
   */
  private Resource organizationModify(final String tenant, final InputStream resource)
    throws ProcessingException {

    Tenant request = unmarshal(resource, Tenant.class);
    request.version("00000000").createdBy(this.principal.username).modifiedBy(this.principal.username);
    validate(request);
    return Provider.instance.organizationModify(tenant, request);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userroleCreate
  /**
   ** Create organization userroless.
   **
   ** @param  tenant             the unique organization identifier to lookup
   **                            the resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  resource           the {@link InputStream} to unmarshall from the
   **                            request.
   **                            <br>
   **                            Allowed object is {@link InputStream}.
   **
   ** @throws ProcessingException if the database operation fails.
   */
  private Resource userroleCreate(final String tenant, final InputStream resource)
    throws ProcessingException {

    final UserRole request = unmarshal(resource, UserRole.class);
    for (UserRole.Entry cursor : request.list()) {
      cursor.tenant(tenant).version("00000000");
      cursor.createdBy(this.principal.username);
      cursor.modifiedBy(this.principal.username);
      validate(cursor);
      Provider.instance.userroleCreate(cursor);
    }
    return request;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userroleModify
  /**
   ** Modify an userrole in the database.
   ** <p>
   ** After this method completed sucessfully the metadata of the given resource
   ** updated accordingly.
   **
   ** @param  tenant             the system identifier of the role resource
   **                            to update.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  resource           the {@link InputStream} to unmarshall from the
   **                            request.
   **                            <br>
   **                            Allowed object is {@link InputStream}.
   **
   ** @throws ProcessingException if the account cannot be created.
   */
  private Resource userroleModify(final String tenant, final String role, final InputStream resource)
    throws ProcessingException {

    UserRole.Entry request = unmarshal(resource, UserRole.Entry.class);
    request.version("00000000");
    request.createdBy(this.principal.username);
    request.modifiedBy(this.principal.username);
    validate(request);
    return Provider.instance.userroleModify(tenant, role, request);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userroleAssign
  /**
   ** Assigns an organization userroles to a grantee.
   **
   ** @param  tenant             the identifier of the organization to associate
   **                            the resource to create with.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  role               the name of the userrole to populate the
   **                            members for.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  resource           the {@link InputStream} to unmarshall from the
   **                            request.
   **                            <br>
   **                            Allowed object is {@link InputStream}.
   **
   ** @throws ProcessingException if the database operation fails.
   */
  private Resource userroleAssign(final String tenant, final String role, final InputStream resource)
    throws ProcessingException {

    // the id of the user to assign is in the POST body stream
    String   result = new BufferedReader(new InputStreamReader(resource)).lines().collect(Collectors.joining("\n"));
    try {
      result = URLDecoder.decode(result, "UTF-8");
    }
    catch (UnsupportedEncodingException e) {
      throw BadRequestException.invalidValue(ServiceBundle.string(ServiceError.REQUEST_METHOD_ENTITY_ID_INVALID, "member", Account.RESOURCE));
    }

    // check wellformness
    final String[] member = result.split("=");
    if (member.length != 2)
      throw BadRequestException.invalidValue(ServiceBundle.string(ServiceError.REQUEST_METHOD_ENTITY_ID_INVALID, "member", Account.RESOURCE));

    return Provider.instance.userroleAssign(tenant, role, member[1]);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   developerCreate
  /**
   ** Registers a developer in a organization.
   **
   ** @param  tenant             the identifier of the organization to associate
   **                            the resource to create with.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  resource           the {@link InputStream} to unmarshall from the
   **                            request.
   **                            <br>
   **                            Allowed object is {@link InputStream}.
   **
   ** @throws ProcessingException if the database operation fails.
   */
  private Resource developerCreate(final String tenant, final InputStream resource)
    throws ProcessingException {

    final Developer request = unmarshal(resource, Developer.class);
    validate(request);
    request.tenant(tenant).version("00000000");
    request.createdBy(this.principal.username).modifiedBy(this.principal.username);
    Provider.instance.developerCreate(request);
    return request;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   developerModify
  /**
   ** Modify an developer in the database.
   ** <p>
   ** After this method completed sucessfully the metadata of the given resource
   ** updated accordingly.
   **
   ** @param  tenant             the system identifier of the tenant resource
   **                            to update.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  identifier         the unique identifier of the developer resource
   **                            to modify.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  resource           the {@link InputStream} to unmarshall from the
   **                            request.
   **                            <br>
   **                            Allowed object is {@link InputStream}.
   **
   ** @throws ProcessingException if the account cannot be created.
   */
  private Resource developerModify(final String tenant, final String identifier, final InputStream resource)
    throws ProcessingException {

    Developer request = unmarshal(resource, Developer.class);
    request.version("00000000").tenant(tenant).createdBy(this.principal.username).modifiedBy(this.principal.username);
    validate(request);
    return Provider.instance.developerModify(tenant, identifier, request);
  }


  ////////////////////////////////////////////////////////////////////////////
  // Method:   unmarshal
  /**
   ** Factory method to create a rest resource parsing the data provided in the
   ** HTTP request.
   **
   ** @param  <T>                the generic type parameter of the Java class
   **                            used to determine the type to return.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  stream             the {@link InputStream} providing the data
   **                            submitted by the cleint
   **                            <br>
   **                            Allowed object is {@link InputStream}.
   ** @param  clazz              the Java class object used to determine the
   **                            type to return.
   **                            <br>
   **                            Allowed object is {@link Class}.
   **
   ** @return                    the submitted value transformed in a Java
   **                            content tree.
   **                            <br>
   **                            Possible object is <code>&lt;T&gt;</code>.
   **
   ** @throws JsonProcessingException  if the value can not be parsed to the
   **                                  type specified by the Java class
   **                                  object.
   ** @throws ServiceException         if the path is invalid.
   ** @throws IllegalArgumentException if the operation contains more than one
   **                                  value, in which case, the values method
   **                                  should be used to retrieve all values.
   */
  private <T> T unmarshal(final InputStream stream, final Class<T> type)
    throws ProcessingException {

    T resource = null;
    try {
      // separat try/catch block to handle JSON parser errors
      JsonParser parser = null;
      try {
        final ObjectReader reader = Support.objectReader().forType(type);
        parser = reader.getFactory().createParser(stream);
        if (parser.nextToken() == null)
          throw BadRequestException.entityEmpty(ServiceBundle.string(ServiceError.REQUEST_METHOD_ENTITY_EMPTY));

        resource = reader.readValue(parser);
      }
      finally {
        stream.close();
        parser.close();
      }
    }
    catch (IOException e) {
      throw new ServerErrorException("JSON", e.getLocalizedMessage(), e);
    }
    return resource;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** Verifies if the REST Resouce {@link Tenant} is well prepared for the
   ** transaction.
   **
   ** @param  resource           the REST Resouce {@link Tenant} to validate.
   **                            <br>
   **                            Allowed object is {@link Tenant}.
   **
   ** @throws ProcessingException if the provided REST Resouce doesn't meet the
   **                             requiremnts.
   */
  private void validate(final Tenant resource)
    throws ProcessingException {

    if (StringUtility.isEmpty(resource.name()))
      throw BadRequestException.invalidValue(ServiceBundle.string(ServiceError.REQUEST_METHOD_ENTITY_ATTRIBUTE, Organization.RESOURCE, "name"));

    // prevent bogus input state
    if (StringUtility.isEmpty(resource.type()))
      throw BadRequestException.invalidValue(ServiceBundle.string(ServiceError.REQUEST_METHOD_ENTITY_ATTRIBUTE, Organization.RESOURCE, "type"));

    if (StringUtility.isEmpty(resource.displayName()))
      throw BadRequestException.entityEmpty(ServiceBundle.string(ServiceError.REQUEST_METHOD_ENTITY_ATTRIBUTE, Organization.RESOURCE, "displayName"));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** Verifies if the REST Resouce {@link UserRole.Entry} is well prepared for
   ** the transaction.
   **
   ** @param  resource           the REST Resouce {@link UserRole.Entry} to
   **                            validate.
   **                            <br>
   **                            Allowed object is {@link UserRole.Entry}.
   **
   ** @throws ProcessingException if the provided REST Resouce doesn't meet the
   **                             requiremnts.
   */
  private void validate(final UserRole.Entry resource)
    throws ProcessingException {

    if (StringUtility.isEmpty(resource.role()))
      throw BadRequestException.invalidValue(ServiceBundle.string(ServiceError.REQUEST_METHOD_ENTITY_ATTRIBUTE, Organization.USERROLE, "name"));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** Verifies if the REST Resouce {@link UserRole.Entry} is well prepared for
   ** the transaction.
   **
   ** @param  resource           the REST Resouce {@link UserRole.Entry} to
   **                            validate.
   **                            <br>
   **                            Allowed object is {@link UserRole.Entry}.
   **
   ** @throws ProcessingException if the provided REST Resouce doesn't meet the
   **                             requiremnts.
   */
  private void validate(final UserRole.Member resource)
    throws ProcessingException {

    if (StringUtility.isEmpty(resource.user()))
      throw BadRequestException.invalidValue(ServiceBundle.string(ServiceError.REQUEST_METHOD_ENTITY_ID_INVALID, resource.user(), Account.RESOURCE));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** Verifies if the REST Resouce {@link Tenant} is well prepared for the
   ** transaction.
   **
   ** @param  resource           the REST Resouce {@link Tenant} to validate.
   **                            <br>
   **                            Allowed object is {@link Tenant}.
   **
   ** @throws ProcessingException if the provided REST Resouce doesn't meet the
   **                             requiremnts.
   */
  private void validate(final Developer resource)
    throws ProcessingException {

    // prevent bogus input state
    if (StringUtility.isEmpty(resource.email()))
      throw BadRequestException.invalidValue(ServiceBundle.string(ServiceError.REQUEST_METHOD_ENTITY_ATTRIBUTE, Organization.DEVELOPER, "email"));
  }
}