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

    File        :   UserHandler.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    UserHandler.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2021-28-01  DSteding    First release version
*/

package oracle.iam.system.simulation.apigee.service;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.core.JsonParser;

import com.fasterxml.jackson.databind.ObjectReader;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.system.simulation.ServiceError;
import oracle.iam.system.simulation.NotFoundException;
import oracle.iam.system.simulation.ForbiddenException;
import oracle.iam.system.simulation.BadRequestException;
import oracle.iam.system.simulation.ProcessingException;
import oracle.iam.system.simulation.ServerErrorException;
import oracle.iam.system.simulation.NotImplementedException;

import oracle.iam.system.simulation.resource.ServiceBundle;

import oracle.iam.system.simulation.rest.schema.Resource;

import oracle.iam.system.simulation.scim.schema.Support;

import oracle.iam.system.simulation.apigee.schema.User;

import oracle.iam.system.simulation.apigee.persistence.Account;
import oracle.iam.system.simulation.apigee.persistence.Provider;

////////////////////////////////////////////////////////////////////////////////
// class UserHandler
// ~~~~~ ~~~~~~~~~~~
/**
 ** A handler which is invoked to process REST user resource exchanges.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
class UserHandler extends AbstractHandler<Resource> {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>UserHandler</code> context handler.
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
  private UserHandler(final HttpServer server, final String context) {
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

    final String[] segments  = t.getRequestURI().getPath().split("/");
    final String   last      = segments[segments.length - 1];
    // verify if a known resource is requested means the last part of the path
    // is not the request context mapped to this handler
    final Resource result = Server.CONTEXT_USER.endsWith(last) ? Provider.instance.accountSearch() : Provider.instance.accountLookup(last);
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

    final String[] segments = t.getRequestURI().getPath().split("/");
    final String   last     = segments[segments.length - 1];
    // verify if a search request ist posted
    if (Server.CONTEXT_USER.endsWith(last)) {
      final User request = unmarshal(t.getRequestBody(), User.class);
      validate(request);

      request.version("0000000").createdBy(this.principal.username).modifiedBy(this.principal.username);
      Provider.instance.accountCreate(request);
      return createResponse(201, request);
    }
    else
      throw new NotImplementedException();
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

    final String[] segments = t.getRequestURI().getPath().split("/");
    final String   last     = segments[segments.length - 1];
    // verify if a known resource is requested means the last part of the path
    // is not the request context mapped to this handler
    if (Server.CONTEXT_USER.endsWith(last))
      throw new ForbiddenException(ServiceBundle.string(ServiceError.REQUEST_METHOD_ENTITY_ID_REQUIRED));

    final User resource = unmarshal(t.getRequestBody(), User.class);
    validate(resource);

    // update the account in the backend
    resource.version("0000000").createdBy(this.principal.username).modifiedBy(this.principal.username);
    Provider.instance.accountModify(last, resource);
    return createResponse(200, resource);
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

    final String[] segments = t.getRequestURI().getPath().split("/");
    final String   last     = segments[segments.length - 1];
    // verify if a known resource is requested means the last part of the path
    // is not the request context mapped to this handler
    if (Server.CONTEXT_USER.endsWith(last))
      throw new ForbiddenException(ServiceBundle.string(ServiceError.REQUEST_METHOD_ENTITY_ID_REQUIRED));

    if (!Provider.instance.accountExists(last))
      throw new NotFoundException(ServiceBundle.string(ServiceError.REQUEST_METHOD_ENTITY_NOTFOUND, last, Account.RESOURCE));

    Provider.instance.accountDelete(last);
    // https://tools.ietf.org/html/rfc7644#page-48 says
    // In response to a successful DELETE, the server SHALL return a successful
    // HTTP status code 204 (No Content).
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
    new UserHandler(server, context);
  }

  //////////////////////////////////////////////////////////////////////////////
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
   ** Verifies if the REST Resouce {@link User} is well prepared for the
   ** transaction.
   **
   ** @param  resource           the REST Resouce {@link User} to validate.
   **                            <br>
   **                            Allowed object is {@link User}.
   **
   ** @throws ProcessingException if the provided REST Resouce doesn't meet the
   **                             requiremnts.
   */
 private void validate(final User resource)
    throws ProcessingException {

    if (StringUtility.isEmpty(resource.emailId()))
      throw BadRequestException.invalidValue(ServiceBundle.string(ServiceError.REQUEST_METHOD_ENTITY_ATTRIBUTE, Account.RESOURCE, Account.Attribute.MAIL.id));

    if (StringUtility.isEmpty(resource.firstName())) {
      throw BadRequestException.entityEmpty(ServiceBundle.string(ServiceError.REQUEST_METHOD_ENTITY_ATTRIBUTE, Account.RESOURCE, Account.Attribute.FIRSTNAME.id));
    }

    if (StringUtility.isEmpty(resource.lastName())) {
      throw BadRequestException.entityEmpty(ServiceBundle.string(ServiceError.REQUEST_METHOD_ENTITY_ATTRIBUTE, Account.RESOURCE, Account.Attribute.LASTNAME.id));
    }
  }
}