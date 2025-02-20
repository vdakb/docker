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

    Copyright © 2018 All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Service Simulation
    Subsystem   :   eFBS SCIM Interface

    File        :   UserHandler.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    UserHandler.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.efbs.service;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.HashSet;
import java.util.Collections;

import java.io.IOException;
import java.io.InputStream;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectReader;

import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.system.simulation.ServiceError;
import oracle.iam.system.simulation.NotFoundException;
import oracle.iam.system.simulation.ForbiddenException;
import oracle.iam.system.simulation.BadRequestException;
import oracle.iam.system.simulation.ProcessingException;
import oracle.iam.system.simulation.ServerErrorException;
import oracle.iam.system.simulation.NotImplementedException;
import oracle.iam.system.simulation.ResourceConflictException;

import oracle.iam.system.simulation.resource.ServiceBundle;

import oracle.iam.system.simulation.scim.Path;

import oracle.iam.system.simulation.scim.annotation.Schema;

import oracle.iam.system.simulation.scim.domain.PatchRequest;
import oracle.iam.system.simulation.scim.domain.SearchRequest;

import oracle.iam.system.simulation.scim.object.Filter;

import oracle.iam.system.simulation.scim.utility.Parser;

import oracle.iam.system.simulation.scim.schema.Support;

import oracle.iam.system.simulation.scim.v2.schema.UserResource;
import oracle.iam.system.simulation.scim.v2.schema.EnterpriseUserExtension;

import oracle.iam.system.simulation.efbs.v2.schema.Account;
import oracle.iam.system.simulation.efbs.v2.schema.UserExtension;

////////////////////////////////////////////////////////////////////////////////
// class UserHandler
// ~~~~~ ~~~~~~~~~~~
/**
 ** A handler which is invoked to process SCIM user resource exchanges.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
class UserHandler extends AbstractHandler<UserResource> {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>UserHandler</code> context handler.
   **
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
  public Response<UserResource> get(final HttpExchange t)
    throws ProcessingException {

    final String[]            segments  = t.getRequestURI().getPath().split("/");
    final String              last      = segments[segments.length - 1];
    final Map<String, String> parameter = splitQuery(t.getRequestURI().getQuery());
    // verify if a known resource is requested means the last part of the pass
    // is not the request context mapped to this handler
    if (Server.CONTEXT_USER.endsWith(last)) {
      // if not given startIndex, default to 1
      int start = 1;
      // if not given count, default to 1000
      int count = 1000;
      if (parameter.containsKey("startIndex"))
        start = Integer.parseInt(parameter.get("startIndex"));
      if (parameter.containsKey("count"))
        count = Integer.parseInt(parameter.get("count"));

      final Filter             filter = filter(parameter.get("filter"));
      final int                total  = Provider.instance.accountCount(filter);
      final List<UserResource> result = Provider.instance.accountSearch(start, count, filter, attribute(parameter.get("attributes")));
      return createResponse(200, total, start, result);
    }
    else {
      final UserResource result = Provider.instance.accountLookup(last, true, attribute(parameter.get("attributes")));
      return createResponse(200, result);
    }
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
   **                            Possible object is {@link Response}.
   **
   ** @throws ProcessingException if the request could not be handled anyway.
   */
  @Override
  public Response<UserResource> post(final HttpExchange t)
    throws ProcessingException {

    final String[] segments = t.getRequestURI().getPath().split("/");
    final String   last     = segments[segments.length - 1];
    // verify if a search request ist posted
    if (Server.CONTEXT_USER.endsWith(last)) {
      final UserResource request = unmarshal(t.getRequestBody(), UserResource.class);
      validate(request);
      if (Provider.instance.accountExists(request.userName(), false))
        throw ResourceConflictException.uniqueness(ServiceBundle.string(ServiceError.REQUEST_METHOD_ENTITY_EXISTS, request.userName(), Account.SCHEMA.resource));

      Provider.instance.accountCreate(request);
      return createResponse(201, request);
    }
    else if (StringUtility.isEqual(".search", last)) {
      final SearchRequest request = unmarshal(t.getRequestBody(), SearchRequest.class);
      // if not given startIndex, default to 1
      final int    start      = request.start() == null ? 1 : request.start().intValue();
      // if not given count, default to 1000
      final int    count      = request.count() == null ? 1 : request.count().intValue();
      // obtain the filter criteria from the request if specified
      final Filter expression = filter(request.filter());

      final int                total  = Provider.instance.accountCount(expression);
      final List<UserResource> result = Provider.instance.accountSearch(start, count, expression, attribute(request.omitted()));
      return createResponse(200, total, start, result);
    }
    else
      throw new NotImplementedException();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:    put (AbstractHandler)
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
  public Response<UserResource> put(final HttpExchange t)
    throws ProcessingException {

    final String[] segments = t.getRequestURI().getPath().split("/");
    final String   last     = segments[segments.length - 1];
    // verify if a known resource is requested means the last part of the path
    // is not the request context mapped to this handler
    if (Server.CONTEXT_USER.endsWith(last))
      throw new ForbiddenException(ServiceBundle.string(ServiceError.REQUEST_METHOD_ENTITY_ID_REQUIRED));

    final UserResource resource = unmarshal(t.getRequestBody(), UserResource.class);
    validate(resource);
    if (!Provider.instance.accountExists(last, true))
      throw new NotFoundException(ServiceBundle.string(ServiceError.REQUEST_METHOD_ENTITY_NOTFOUND, last, Account.SCHEMA.resource));

    // update the account in the backend
    Provider.instance.accountModify(last, resource);
    return createResponse(200, resource);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:    patch (AbstractHandler)
  /**
   ** Handle the given <code>PATCH</code>request and generate an appropriate
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
  public Response<UserResource> patch(final HttpExchange t)
    throws ProcessingException {

    final String[]            segments  = t.getRequestURI().getPath().split("/");
    final String              last      = segments[segments.length - 1];
    final Map<String, String> parameter = splitQuery(t.getRequestURI().getQuery());
    // verify if a known resource is requested means the last part of the path
    // is not the request context mapped to this handler
    if (Server.CONTEXT_USER.endsWith(last))
      throw new ForbiddenException(ServiceBundle.string(ServiceError.REQUEST_METHOD_ENTITY_ID_REQUIRED));

    if (!Provider.instance.accountExists(last, true))
      throw new NotFoundException(ServiceBundle.string(ServiceError.REQUEST_METHOD_ENTITY_NOTFOUND, last, Account.SCHEMA.resource));

    final PatchRequest request = unmarshal(t.getRequestBody(), PatchRequest.class);
    // update the account in the backend
    Provider.instance.accountModify(last, request.operation());

    // https://tools.ietf.org/html/rfc7644#page-32 says
    // the server either MUST return a 200 OK response code and the entire
    // resource within the response body, subject to the "attributes" query
    // parameter (see Section 3.9), or MAY return HTTP status code 204 (No
    // Content) and the appropriate response headers for a successful PATCH
    // request
    // https://tools.ietf.org/html/rfc7644#page-32 says
    // the server MUST return a 200 OK if the "attributes" parameter is
    // specified in the request.
    if (parameter.containsKey("attributes")) {
      return createResponse(200, Provider.instance.accountLookup(last, true, attribute(parameter.get("attributes"))));
    }
    else {
      return createResponse(204, null);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:    delete (AbstractHandler)
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
  public Response<UserResource> delete(final HttpExchange t)
    throws ProcessingException {

    final String[] segments = t.getRequestURI().getPath().split("/");
    final String   last     = segments[segments.length - 1];
    // verify if a known resource is requested means the last part of the path
    // is not the request context mapped to this handler
    if (Server.CONTEXT_USER.endsWith(last))
      throw new ForbiddenException(ServiceBundle.string(ServiceError.REQUEST_METHOD_ENTITY_ID_REQUIRED));

    if (!Provider.instance.accountExists(last, true))
      throw new NotFoundException(ServiceBundle.string(ServiceError.REQUEST_METHOD_ENTITY_NOTFOUND, last, Account.SCHEMA.resource));

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

  ////////////////////////////////////////////////////////////////////////////
  // Method:   unmarshal
  /**
   ** Factory method to create a SCIM resource parsing the data provided in the
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
    return (T)resource;
  }

  private void validate(final UserResource resource)
    throws ProcessingException {

    if (!StringUtility.isEmpty(resource.id()))
      throw BadRequestException.invalidValue(ServiceBundle.string(ServiceError.REQUEST_METHOD_ENTITY_ID_INVALID, resource.id(), Account.SCHEMA.resource));

    try {
      JsonNode       node   = resource.extensionObjectNode().path(Support.namespace(EnterpriseUserExtension.class));
      final Schema[] schema = EnterpriseUserExtension.class.getAnnotationsByType(Schema.class);
      // prevent bogus input
      if (node.isMissingNode())
        throw BadRequestException.entityEmpty(schema[0].id());

      final EnterpriseUserExtension enterprise = Support.nodeToValue(node, EnterpriseUserExtension.class);
      // prevent bogus input state
      if (StringUtility.isEmpty(enterprise.organization())) {
        throw BadRequestException.entityEmpty(ServiceBundle.string(ServiceError.REQUEST_METHOD_ENTITY_ATTRIBUTE, schema[0].id(), "organization"));
      }
      if (StringUtility.isEmpty(enterprise.division())) {
        throw BadRequestException.entityEmpty(ServiceBundle.string(ServiceError.REQUEST_METHOD_ENTITY_ATTRIBUTE, schema[0].id(), "division"));
      }
      if (StringUtility.isEmpty(enterprise.department())) {
        throw BadRequestException.entityEmpty(ServiceBundle.string(ServiceError.REQUEST_METHOD_ENTITY_ATTRIBUTE, schema[0].id(), "department"));
      }
      node   = resource.extensionObjectNode().path(Support.namespace(UserExtension.class));
      if (node.isMissingNode())
        resource.extension(new UserExtension());
    }
    catch (JsonProcessingException e) {
      throw BadRequestException.invalidPath(e.toString());
    }
  }

  private Set<String> attribute(final String parameter)
    throws ProcessingException {

    if (StringUtility.isEmpty(parameter)) {
      return attribute(Collections.<String>emptySet());
    }
    else {
      return attribute(CollectionUtility.set(StringUtility.splitCSV(parameter)));
    }
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Parse the attributes to return fro the given set of attribute names.
   ** <p>
   ** The attribute name may be prefixed with the schema URN.
   ** <br>
   ** Find the last ":" before any open brackets. Everything to the left is the
   ** schema URN, everything on the right is the attribute name plus a potential
   ** value filter.
   ** <p>
   ** The value filter will be ignored for the time being
   **
   ** @param  parameter          the {@link Set} of full-qualified attribute
   **                            names requested.
   **
   ** @return                    the trimmed attributes mapped to the permitted
   **                            attributes.
   **
   ** @throws ProcessingException if an attribute requested is not allowed by
   **                             the restricted schema or a certain attribute
   **                             in the set isn't a valid path.
   */
  private Set<String> attribute(final Set<String> parameter)
    throws ProcessingException {

    // initilaize the returning attributes with the minimal set of the
    // operationals
    final Set<String> returning = new HashSet<String>(Account.SCHEMA.minimal.keySet());
    // extends the attributes to be returned with the attributes requested
    for (String cursor : parameter) {
      // transform the requeste string to a path
      returning.add(cursor);
    }
    return returning;
  }

  private Filter filter(final String parameter)
    throws ProcessingException {

    return StringUtility.isEmpty(parameter) ? null : Parser.filter(parameter);
  }
}