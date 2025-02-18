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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Pivotal Cloud Foundry Connector

    File        :   RestContext.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    RestContext.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.pcf;

import java.net.URI;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import javax.ws.rs.client.WebTarget;

import javax.ws.rs.core.UriBuilder;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.foundation.object.Pair;

import oracle.iam.identity.icf.foundation.logging.AbstractLoggable;

import oracle.iam.identity.icf.rest.ServiceError;
import oracle.iam.identity.icf.rest.ServiceException;

import oracle.iam.identity.icf.rest.domain.ListResult;

import oracle.iam.identity.icf.connector.pcf.rest.domain.Result;
import oracle.iam.identity.icf.connector.pcf.rest.domain.Payload;

import oracle.iam.identity.icf.connector.pcf.rest.schema.Role;
import oracle.iam.identity.icf.connector.pcf.rest.schema.Entity;
import oracle.iam.identity.icf.connector.pcf.rest.schema.Metadata;

import oracle.iam.identity.icf.connector.pcf.rest.request.Lookup;
import oracle.iam.identity.icf.connector.pcf.rest.request.Assign;
import oracle.iam.identity.icf.connector.pcf.rest.request.Search;
import oracle.iam.identity.icf.connector.pcf.rest.request.Create;
import oracle.iam.identity.icf.connector.pcf.rest.request.Delete;
import oracle.iam.identity.icf.connector.pcf.rest.request.Modify;

///////////////////////////////////////////////////////////////////////////////
// class RestContext
// ~~~~~ ~~~~~~~~~~~
/**
 ** This is connector server specific context class that extends Jersey's
 ** client interface.
 ** <p>
 ** The specific of the context is that it takes care about the REST API the
 ** Pivotal Cloud Foundry Cloud Controller expose regarding tenants (aka
 ** organizations) and spaces.
 ** <p>
 ** The Cloud Controller in Pivotal Cloud Foundry (PCF) provides REST API
 ** endpoints for clients to access the system. The Cloud Controller maintains a
 ** database with tables for organizations, spaces, services, user roles, and
 ** more.
 ** <p>
 ** <b>Note</b>:
 ** <br>
 ** This class is decleared as public only to access it in JUNit tests.
 ** <br>
 ** This may be changed in the future.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class RestContext extends AbstractLoggable<RestContext> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** An HTTP GET to this endpoint is used to retrieve information about
   ** the Cloud Controller resource type <code>Tenant</code> provided by the
   ** Service Provider.
   */
  public static final String ENDPOINT_TENANT = "v2/organizations";
  /**
   ** An HTTP GET to this endpoint is used to retrieve information about
   ** the Cloud Controller resource type <code>Space</code> provided by the
   ** Service Provider.
   */
  public static final String ENDPOINT_SPACES = "v2/spaces";
  /**
   ** An HTTP GET to this endpoint is used to retrieve information about
   ** the Cloud Controller resource type <code>User</code> provided by the
   ** Service Provider.
   */
  public static final String ENDPOINT_USERS = "v2/users";

  /**
   ** The maximum ammount of resource information that can be retrieved in a
   ** batch.
   */
  static final int           SIZE_LIMIT     = 100;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final Service      client;

  /** the endpoint to access the Cloud Controller API **/
  private final WebTarget    target;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>RestContext</code> context which is associated with
   ** the specified {@link Endpoint} for configuration purpose.
   **
   ** @param  client             the {@link Service} <code>IT Resource</code>
   **                            instance where this connector context is
   **                            associated with.
   **                            <br>
   **                            Allowed object is {@link Service}.
   */
  private RestContext(final Service client) {
    // ensure inheritance
    super(client);

    // initialize instance attributes
    this.client = client;
    this.target = client.target(client.endpoint().controllerURL());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a default context.
   **
   ** @param  client             the {@link Service} <code>IT Resource</code>
   **                            instance where this connector context is
   **                            associated with.
   **                            <br>
   **                            Allowed object is {@link Service}.
   **
   ** @return                    a default context.
   **                            <br>
   **                            Possible object is <code>RestContext</code>.
   */
  public static RestContext build(final Service client) {
    return new RestContext(client);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchTenant
  /**
   ** Build a request to query and retrieve the Cloud Controller tenant
   ** resources from the Service Provider.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Search}.
   */
  public Search searchTenant() {
    return Search.build(this.target.path(ENDPOINT_TENANT));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupPermitted
  /**
   ** Build and executes the request to retrieve the Cloud Controller tenant and
   ** space permission mapped at the Service Provider to the account identifier
   ** <code>beneficiary</code>.
   ** <p>
   ** The returned {@link Map} belongs to tenants and space as the key elements.
   **
   ** @param  beneficiary        the account identifier the permissions regested
   **                            for.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the successfully fetched Cloud Controller
   **                            resource permissions.
   **                            <br>
   **                            Possible object is {@link Map}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public Map<String, List<Pair<String, List<String>>>> lookupPermitted(final String beneficiary)
    throws SystemException {

    return lookupPermitted(UriBuilder.fromUri(RestContext.ENDPOINT_USERS).path(beneficiary).path("summary").build());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupPermitted
  /**
   ** Build and executes the request to retrieve the Cloud Controller tenant and
   ** space permission mapped at the Service Provider to the account identifier
   ** <code>beneficiary</code>.
   ** <p>
   ** The returned {@link Map} belongs to tenants and space as the key elements.
   **
   ** @param  uri                the account endpoint the permissions regested
   **                            for.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the successfully fetched Cloud Controller
   **                            resource permissions.
   **                            <br>
   **                            Possible object is {@link Map}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public Map<String, List<Pair<String, List<String>>>> lookupPermitted(final URI uri)
    throws SystemException {

    final Map<String, List<Pair<String, List<String>>>> permitted = new HashMap<String, List<Pair<String, List<String>>>>();
    // the invocation of the service endpoint will throw an exception if the
    // user is not mapped at the Service Provider by the specified beneficiary
    // this means the user does not have any permission at the Cloud Controller
    // for our usage we need to catch this exception to prevent break of the
    // service
    try {
      final Map<String, Object> user = Lookup.build(target(uri)).invoke();
      if (user != null) {
        // obtain the permission mapping form the user
        final Map<String, Object> payload = (Map<String, Object>)user.get(Entity.PREFIX);
        permitted.put(RestMarshaller.TENANT_NAME, permittedScope(payload, Role.TENANT));
        permitted.put(RestMarshaller.SPACE_NAME,  permittedScope(payload, Role.SPACE));
      }
      return permitted;
    }
    catch (ServiceException e) {
      // consume a resource not found exception
      if (ServiceError.PROCESS_EXISTS_NOT.equals(e.code()))
        return permitted;
      else
        throw e;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupTenant
  /**
   ** Build and executes a request to retrieve a known Cloud Controller tenant
   ** resource from the Service Provider.
   **
   ** @param  uri                the {@link URI} of the resource to retrieve.
   **                            <br>
   **                            Allowed object is {@link URI}.
   **
   ** @return                    the successfully retrieved Cloud Controller
   **                            tenant resource.
   **                            <br>
   **                            Possible object is {@link Result.Tenant}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public Result.Tenant lookupTenant(final URI uri)
    throws SystemException {

    return Lookup.build(target(uri)).invoke(Result.Tenant.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveTenant
  /**
   ** Build and executes a request to retrieve the unique identifier of a known
   ** Cloud Controller tenant resource from the Service Provider.
   **
   ** @param  name               the resource name (for example the value of the
   **                            "<code>entity.name</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the unique identifier of the successfully
   **                            retrieved Cloud Controller tenant resource or
   **                            <code>null</code> if the specified resource
   **                            name is not mapped at the Service Provider.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public String resolveTenant(final String name)
    throws SystemException {

    // set the start indes of the search to one but the count of results to a
    // higher number as one so that if a tenant name is ambigiously defined the
    // search will fail
    @SuppressWarnings("unchecked")
    final ListResult<Result.Tenant> result = searchTenant().page(1, 2).filter(RestTranslator.filter("name", name, RestTranslator.EQ, false)).invoke(Result.Tenant.class);
    if (result.total() > 1)
      throw ServiceException.tooMany(name);

    return result.total() == 0 ? null : result.iterator().next().metadata().guid();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   tenantCreate
  /**
   ** Build and executes the request to create the provided new Cloud Controller
   ** tenant resource at the Service Provider.
   **
   ** @param  resource           the new the Cloud Controller resource to
   **                            create.
   **                            <br>
   **                            Allowed object is {@link Payload.Tenant}.
   **
   ** @return                    the successfully create Cloud Controller
   **                            tenant resource.
   **                            <br>
   **                            Possible object is {@link Result.Tenant}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public Result.Tenant createTenant(final Payload.Tenant resource)
    throws SystemException {

    @SuppressWarnings("unchecked")
    final Create<Payload.Tenant> request = Create.build(this.target.path(ENDPOINT_TENANT), resource);
    return request.invoke(Result.Tenant.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifyTenant
  /**
   ** Build and executes the request to modify the Cloud Controller tenant
   ** resource at the Service Provider with the given resource.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** As the operation's intent is to replace all attributes, regardless of each
   ** attribute's mutability.
   ** <br>
   ** The server will apply attribute-by-attribute replacements according to the
   ** mutability rules defined by the schema.
   **
   ** @param  identifier         the identifier of the resource to modify.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  resource           the Cloud Controller tenant resource to modify.
   **                            <br>
   **                            Allowed object is {@link Payload.Tenant}.
   **
   ** @return                    the successfully modified Cloud Controller
   **                            tenant resource.
   **                            <br>
   **                            Possible object is {@link Result.Tenant}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public Result.Tenant modifyTenant(final String identifier, final Payload.Tenant resource)
    throws SystemException {

    @SuppressWarnings("unchecked")
    final Modify<Payload.Tenant> request = Modify.<Payload.Tenant>build(this.target.path(ENDPOINT_TENANT).path(identifier), resource);
    return request.invoke(Result.Tenant.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteTenant
  /**
   ** Delete the Cloud Controller tenant resource in the Service Provider that
   ** belongs to the given identifier.
   **
   ** @param  identifier         the identifier of the Cloud Controller tenant
   **                            resource to delete.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public void deleteTenant(final String identifier)
    throws SystemException {

    Delete.build(this.target.path(ENDPOINT_TENANT).path(identifier)).invoke();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchSpace
  /**
   ** Build a request to query and retrieve Cloud Controller space resources
   ** from the Service Provider.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Search}.
   */
  public Search searchSpace() {
    return Search.build(this.target.path(ENDPOINT_SPACES));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupSpace
  /**
   ** Build and executes a request to retrieve a known Cloud Controller space
   ** resource from the Service Provider.
   **
   ** @param  uri                the {@link URI} of the resource to retrieve.
   **                            <br>
   **                            Allowed object is {@link URI}.
   **
   ** @return                    the successfully retrieved Cloud Controller
   **                            tenant resource.
   **                            <br>
   **                            Possible object is {@link Result.Space}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public Result.Space lookupSpace(final URI uri)
    throws SystemException {

    return Lookup.build(target(uri)).invoke(Result.Space.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveSpace
  /**
   ** Build and executes a request to retrieve the unique identifier of a known
   ** Cloud Controller space resource from the Service Provider.
   **
   ** @param  name               the resource name (for example the value of the
   **                            "<code>entity.name</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the unique identifier of the successfully
   **                            retrieved Cloud Controller space resource or
   **                            <code>null</code> if the specified resource
   **                            name is not mapped at the Service Provider.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public String resolveSpace(final String name)
    throws SystemException {

    // set the start indes of the search to one but the count of results to a
    // higher number as one so that if a tenant name is ambigiously defined the
    // search will fail
    // TODO: how to get rid of "unchecked" annotation
    @SuppressWarnings("unchecked")
    final ListResult<Result.Space> result = searchSpace().page(1, 2).filter(RestTranslator.filter("name", name, RestTranslator.EQ, false)).invoke(Result.Space.class);
    if (result.total() > 1)
      throw ServiceException.tooMany(name);

    return result.total() == 0 ? null : result.iterator().next().metadata().guid();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createSpace
  /**
   ** Build and executes the request to create the provided new Cloud Controller
   ** space resource at the Service Provider.
   **
   ** @param  resource           the new Cloud Controller space resource to
   **                            create.
   **                            <br>
   **                            Allowed object is {@link Payload.Space}.
   **
   ** @return                    the successfully create Cloud Controller space
   **                            resource.
   **                            <br>
   **                            Possible object is {@link Result.Space}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public Result.Space createSpace(final Payload.Space resource)
    throws SystemException {

    // TODO: how to get rid of "unchecked" annotation
    @SuppressWarnings("unchecked")
    final Create<Payload.Space> request = Create.<Payload.Space>build(this.target.path(ENDPOINT_SPACES), resource);
    return request.invoke(Result.Space.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifySpace
  /**
   ** Build and executes the request to modify the Cloud Controller space
   ** resource at the Service Provider with the given resource.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** As the operation's intent is to replace all attributes, regardless of each
   ** attribute's mutability.
   ** <br>
   ** The server will apply attribute-by-attribute replacements according to the
   ** mutability rules defined by the schema.
   **
   ** @param  identifier         the identifier of the resource to modify.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  resource           the Cloud Controller space resource to modify.
   **                            <br>
   **                            Allowed object is {@link Payload.Space}.
   **
   ** @return                    the successfully modified Cloud Controller
   **                            tenant resource.
   **                            <br>
   **                            Possible object is {@link Result.Space}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public Result.Space modifySpace(final String identifier, final Payload.Space resource)
    throws SystemException {

    // TODO: how to get rid of "unchecked" annotation
    @SuppressWarnings("unchecked")
    final Modify<Payload.Space> request = Modify.<Payload.Space>build(this.target.path(ENDPOINT_TENANT).path(identifier), resource);
    return request.invoke(Result.Space.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteSpace
  /**
   ** Delete the Cloud Controller space resource in the
   ** Service Provider that belongs to the given identifier.
   **
   ** @param  identifier         the identifier of the Cloud Controller space
   **                            resource to delete.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public void deleteSpace(final String identifier)
    throws SystemException {

    Delete.build(this.target.path(ENDPOINT_SPACES).path(identifier)).invoke();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assignSpace
  /**
   ** Build and executes the request to assign the Cloud Controller space
   ** resource <code>identifier</code> at the Service Provider with the given
   ** role <code>role</code> where the specified account identifier
   ** <code>beneficiary</code> becomes a member.
   **
   ** @param  identifier         the identifier of the Cloud Controller space
   **                            resource to modify.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  beneficiary        the identifier of the account resource to
   **                            assign.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  role               the role grant to assign.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the successfully modified Cloud Controller
   **                            space resource.
   **                            <br>
   **                            Possible object is {@link Result.Space}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public Result.User assign(final String identifier, final String beneficiary, final String role)
    throws SystemException {

    // TODO: how to get rid of "unchecked" annotation
    @SuppressWarnings("unchecked")
    final Assign<Result.User> request = Assign.<Result.User>build(this.target.path(ENDPOINT_USERS).path(beneficiary).path(role).path(identifier));
    return request.invoke(Result.User.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revoke
  /**
   ** Build and executes the request to revoke the Cloud Controller space
   ** resource <code>identifier</code> at the Service Provider with the given
   ** role <code>role</code> from the specified account identifier
   ** <code>beneficiary</code>.
   **
   ** @param  identifier         the identifier of the Cloud Controller space
   **                            resource to modify.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  beneficiary        the identifier of the account resource to
   **                            revoke.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  role               the role grant to revoke.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws SystemException    if an error occurs.
   */
  public void revoke(final String identifier, final String beneficiary, final String role)
    throws SystemException {

    Delete.build(this.target.path(ENDPOINT_USERS).path(beneficiary).path(role).path(identifier)).invoke();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   permittedScope
  private List<Pair<String, List<String>>> permittedScope(final Map<String, Object> entity, final Set<String> scope) {
    final List<Pair<String, List<String>>> permitted = new ArrayList<Pair<String, List<String>>>();
    for (String role : scope) {
      permitted.add(Pair.of(role, permittedGUID(entity, role)));
    }
    return permitted;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   permittedGUID
  private List<String> permittedGUID(final Map<String, Object> scope, final String role) {
    final List<String>              permitted = new ArrayList<String>();
    final List<Map<String, Object>> result    = (List<Map<String, Object>>)scope.get(role);
    for (Map<String, Object> cursor : result) {
      final Map<String, Object> metadata = (Map<String, Object>)cursor.get(Metadata.PREFIX);
      permitted.add((String)metadata.get(Metadata.ID));
    }
    return permitted;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   target
  /**
   ** Resolve a {@link URI} (relative or absolute) to a web target.
   **
   ** @param  uri                the {@link URI} to resolve.
   **                            <br>
   **                            Allowed object is {@link URI}.
   **
   ** @return                    the {@link WebTarget}.
   **                            <br>
   **                            Possible object is {@link WebTarget}.
   */
  private WebTarget target(final URI uri) {
    URI relativePath;
    if (uri.isAbsolute()) {
      relativePath = this.target.getUri().relativize(uri);
      // the given resource's location is from another service provider
      if (relativePath.equals(uri))
        throw new IllegalArgumentException("Given resource's location " + uri + " is not under this service's base path " + this.target.getUri());
    }
    else {
      relativePath = uri;
    }
    return this.target.path(relativePath.getRawPath());
  }
}