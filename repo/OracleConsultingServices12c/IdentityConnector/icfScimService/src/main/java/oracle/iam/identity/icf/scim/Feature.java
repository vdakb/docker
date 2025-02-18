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
    Subsystem   :   Generic SCIM Library

    File        :   Feature.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Feature.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.scim;

import java.net.URI;

import javax.ws.rs.client.WebTarget;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.rest.ServiceClient;
import oracle.iam.identity.icf.rest.ServiceContext;
import oracle.iam.identity.icf.rest.ServiceEndpoint;

import oracle.iam.identity.icf.scim.request.Search;
import oracle.iam.identity.icf.scim.request.Lookup;
import oracle.iam.identity.icf.scim.request.Create;
import oracle.iam.identity.icf.scim.request.Delete;
import oracle.iam.identity.icf.scim.request.Replace;

import oracle.iam.identity.icf.scim.schema.Resource;

import oracle.iam.identity.icf.scim.response.ListResponse;

///////////////////////////////////////////////////////////////////////////////
// abstract class Feature
// ~~~~~~~~ ~~~~~ ~~~~~~~
/**
 ** This is connector server specific context class that extends Jersey's
 ** client interface.
 ** <p>
 ** The class covvers common action required to perform activities regrading
 ** SCIM request/reply synchronous invocations to the REST API of a SCIM Service
 ** Provider.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
public abstract class Feature extends ServiceContext {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** An HTTP GET to this endpoint will return a JSON structure that describes
   ** the SCIM specification features available on a Service Provider.
   */
  public static final String ENDPOINT_SERVICE_PROVIDER_CONFIG = "ServiceProviderConfig";
  /**
   ** An HTTP GET to this endpoint is used to discover the types of resources
   ** available on a SCIM Service Provider (for example, Users and Groups).
   */
  public static final String ENDPOINT_RESOURCE_TYPES          = "ResourceTypes";
  /**
   ** An HTTP GET to this endpoint is used to retrieve information about
   ** resource schemas supported by a SCIM Service Provider.
   */
  public static final String ENDPOINT_SCHEMAS                 = "Schemas";
  /**
   ** An HTTP GET to this endpoint is used to retrieve information about
   ** of the resource type <code>User</code>.
   ** <p>
   ** SCIM provides a resource type for <code>User</code>s resources. The core
   ** schema for <code>User</code> is identified using the following schema URI:
   ** <ul>
   **   <li>Version 1: <code>urn:scim:schemas:core:1.0</code>.
   **   <li>Version 2: <code>urn:ietf:params:scim:schemas:core:2.0:User</code>,
   **                  <code>urn:ietf:params:scim:schemas:extension:enterprise:2.0:User</code>.
   ** </ul>
   */
  public static final String ENDPOINT_USERS                   = "Users";
  /**
   ** An HTTP GET to this endpoint is used to retrieve information about
   ** of the resource type <code>Group</code> provided by a SCIM Service
   ** Provider.
   ** <p>
   ** SCIM provides a schema for representing groups. The core schema for
   ** resource type <code>Group</code> is identified using the following schema
   ** URI:
   ** <ul>
   **   <li>Version 1: <code>urn:scim:schemas:core:1.0</code>.
   **   <li>Version 2: <code>urn:ietf:params:scim:schemas:core:2.0:Group</code>,
   **                  urn:ietf:params:scim:schemas:extension:enterprise:2.0:Group.
   ** </ul>
   */
  public static final String ENDPOINT_GROUPS                   = "Groups";
  /**
   ** The "<code>/Me</code>" authenticated subject URI alias for the User or
   ** other resource associated with the currently authenticated subject for any
   ** SCIM operation.
   */
  public static final String ENDPOINT_ME                      = "Me";
  /**
   ** The HTTP query parameter used in a URI to include specific SCIM
   ** attributes.
   */
  public static final String QUERY_PARAMETER_ATTRIBUTES       = "attributes";
  /**
   ** The HTTP query parameter used in a URI to exclude specific SCIM
   ** attributes.
   */
  public static final String QUERY_PARAMETER_EXCLUDED         = "excludedAttributes";
  /**
   ** The HTTP query parameter used in a URI to provide a filter expression.
   */
  public static final String QUERY_PARAMETER_FILTER           = "filter";
  /**
   ** The HTTP query parameter used in a URI to sort by a SCIM attribute.
   */
  public static final String QUERY_PARAMETER_SORT_BY          = "sortBy";
  /**
   ** The HTTP query parameter used in a URI to specify the sort order.
   */
  public static final String QUERY_PARAMETER_SORT_ORDER       = "sortOrder";
  /**
   ** The HTTP query parameter used in a URI to specify the starting index for
   ** page results.
   */
  public static final String QUERY_PARAMETER_PAGE_START_INDEX = "startIndex";
  /**
   ** The HTTP query parameter used in a URI to specify the maximum size of a
   ** page of results.
   */
  public static final String QUERY_PARAMETER_PAGE_SIZE        = "count";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final WebTarget    target;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Feature</code> which is associated with the
   ** specified {@link ServiceEndpoint} for configuration purpose.
   **
   ** @param  client             the {@link ServiceClient}
   **                            <code>IT Resource</code> definition where this
   **                            connector is associated with.
   **                            Allowed object is {@link ServiceClient}.
   */
  protected Feature(final ServiceClient client) {
    // ensure inheritance
    super(client);

    // initialize instance attributes
    this.target = buildTarget();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   search
  /**
   ** Build a request to query and retrieve resources of a single resource type
   ** from the Service Provider.
   **
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>Users</code>"
   **                              <li>"<code>Groups</code>"
   **                            </ul>
   **                            as defined by the associated resource type.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   */
  public final Search search(final String context) {
    return Search.build(this.target.path(context), this.client.typeContent(), this.client.typeAccept());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** Returns a known SCIM resource from the Service Provider.
   **
   ** @param  <T>                the Java type of the resource to return.
   **                            <br>
   **                            Allowed object is <code>%lt;T&gt;</code>.
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>Users</code>"
   **                              <li>"<code>Groups</code>"
   **                            </ul>
   **                            as defined by the associated resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link Long}.
   ** @param  clazz              the Java {@link Class} type used to determine
   **                            the type to return.
   **                            <br>
   **                            Allowed object is {@link Class} of type
   **                            <code>T</code>.
   **
   ** @return                    the successfully retrieved SCIM resource.
   **                            <br>
   **                            Possible object is {@link Resource} of type
   **                            <code>T</code>.
   **
   ** @throws SystemException    if an error occurs.
   */
  public final <T extends Resource> T lookup(final String context, final Long id, final Class<T> clazz)
    throws SystemException {

    return lookupRequest(context, id).invoke(clazz);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** Returns a known SCIM resource from the Service Provider.
   **
   ** @param  <T>                the Java type of the resource to return.
   **                            <br>
   **                            Allowed object is <code>%lt;T&gt;</code>.
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>Users</code>"
   **                              <li>"<code>Groups</code>"
   **                            </ul>
   **                            as defined by the associated resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  clazz              the Java {@link Class} type used to determine
   **                            the type to return.
   **                            <br>
   **                            Allowed object is {@link Class} of type
   **                            <code>T</code>.
   **
   ** @return                    the successfully retrieved SCIM resource.
   **                            <br>
   **                            Possible object is {@link Resource} of type
   **                            <code>T</code>.
   **
   ** @throws SystemException    if an error occurs.
   */
  public final <T extends Resource> T lookup(final String context, final String id, final Class<T> clazz)
    throws SystemException {

    return lookupRequest(context, id).invoke(clazz);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** Returns a known SCIM resource from the Service Provider.
   **
   ** @param  <T>                the Java type of the resource to return.
   **                            <br>
   **                            Allowed object is <code>%lt;T&gt;</code>.
   ** @param  uri                the {@link URI} of the resource to retrieve.
   **                            <br>
   **                            Allowed object is {@link URI}.
   ** @param  clazz              the Java {@link Class} type used to determine
   **                            the type to return.
   **                            <br>
   **                            Allowed object is {@link Class} of type
   **                            <code>T</code>.
   **
   ** @return                    the successfully retrieved SCIM resource.
   **                            <br>
   **                            Possible object is {@link Resource} of type
   **                            <code>T</code>.
   **
   ** @throws SystemException    if an error occurs.
   */
  public final <T extends Resource> T lookup(final URI uri, final Class<T> clazz)
    throws SystemException {

    return lookupRequest(uri).invoke(clazz);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolve
  /**
   ** Returns a known SCIM resource from the Service Providerby the name
   ** specified.
   **
   ** @param  <T>                the Java type of the resource to return.
   **                            <br>
   **                            Allowed object is <code>%lt;T&gt;</code>.
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>Users</code>"
   **                              <li>"<code>Groups</code>"
   **                            </ul>
   **                            as defined by the associated resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  name               the resource attribute (for example the value
   **                            of the "<code>userName</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  clazz              the Java {@link Class} type used to determine
   **                            the type to return.
   **                            <br>
   **                            Allowed object is {@link Class} of type
   **                            <code>T</code>.
   **
   ** @return                    the successfully retrieved SCIM resource.
   **                            <br>
   **                            Possible object is {@link Resource} of type
   **                            <code>T</code>.
   **
   ** @throws SystemException    if an error occurs.
   */
  public final <T extends Resource> String resolve(final String context, final String name, final String id, final Class<T> clazz)
    throws SystemException {

    final ListResponse<T> response = searchRequest(context).page(1, 2).filter(name + " eq \"" + id + "\"").invoke(clazz);
    return (response.items() == 0) ? null : response.resource().iterator().next().id();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Create the provided new SCIM resource at the Service Provider.
   **
   ** @param  <T>                the Java type of the resource to return.
   **                            <br>
   **                            Allowed object is <code>%lt;T&gt;</code>.
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>Users</code>"
   **                              <li>"<code>Groups</code>"
   **                            </ul>
   **                            as  defined by the associated resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  resource           the new resource to create.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    the successfully create SCIM resource.
   **                            <br>
   **                            Possible object is {@link Resource} of type
   **                            <code>T</code>.
   **
   ** @throws SystemException    if an error occurs.
   */
  public final <T extends Resource> T create(final String context, final T resource)
    throws SystemException {

    return createRequest(context, resource).invoke();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Build a request to modify a SCIM resource by replacing the resource's
   ** attributes at the Service Provider.
   ** <br>
   ** If the service provider supports resource versioning, the resource will
   ** only be modified if it has not been modified since it was retrieved.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** As the operation's intent is to replace all attributes all attributes are
   ** send, regardless of each attribute's mutability.
   ** <br>
   ** The server will apply attribute-by-attribute replacements according to the
   ** mutability rules defined by the schema.
   **
   ** @param  <T>                the Java type of the resource to return.
   **                            <br>
   **                            Allowed object is <code>%lt;T&gt;</code>.
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>Users</code>"
   **                              <li>"<code>Groups</code>"
   **                            </ul>
   **                            as defined by the associated resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  resource           the resource to modify.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    the successfully modified SCIM resource.
   **                            <br>
   **                            Possible object is {@link Resource} for type
   **                            <code>T</code>.
   **
   ** @throws SystemException    if an error occurs.
   */
  public final <T extends Resource> T modify(final String context, final T resource)
    throws SystemException {

    return modifyRequest(context, resource).invoke();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Delete the provided new SCIM resource at the Service Provider.
   **
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>Users</code>"
   **                              <li>"<code>Groups</code>"
   **                            </ul>
   **                            as  defined by the associated resource type.
   ** @param  identifier         the identifier of the resource resource to
   **                            delete.
   **
   ** @throws SystemException    if an error occurs.
   */
  public final void delete(final String context, final String identifier)
    throws SystemException {

    deleteRequest(context, identifier).invoke();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchRequest
  /**
   ** Build a request to query and retrieve resources of a single resource type
   ** from the Service Provider.
   **
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>Users</code>"
   **                              <li>"<code>Groups</code>"
   **                            </ul>
   **                            as defined by the associated resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Search}.
   */
  public Search searchRequest(final String context) {
    return Search.build(this.target.path(context), this.client.typeContent(), this.client.typeAccept());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupRequest
  /**
   ** Build a request to lookup a known SCIM resource from the Service Provider.
   **
   ** @param  <T>                the Java type of the resource to return.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  uri                the {@link URI} of the resource to lookup.
   **                            <br>
   **                            Allowed object is {@link URI}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Lookup} for type
   **                            <code>T</code>.
   */
  public <T extends Resource> Lookup<T> lookupRequest(final URI uri) {
    return new Lookup<T>(resolveTarget(uri), this.client.typeContent(), this.client.typeAccept());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupRequest
  /**
   ** Build a request to lookup a known SCIM resource from the Service Provider.
   **
   ** @param  <T>                the Java type of the resource to return.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>Users</code>"
   **                              <li>"<code>Groups</code>"
   **                            </ul>
   **                            as  defined by the associated resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Lookup} for type
   **                            <code>T</code>.
   */
  public <T extends Resource> Lookup<T> lookupRequest(final String context, final Integer id) {
    return new Lookup<T>(requestTarget(context, id), this.client.typeContent(), this.client.typeAccept());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupRequest
  /**
   ** Build a request to lookup a known SCIM resource from the Service Provider.
   **
   ** @param  <T>                the Java type of the resource to return.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>Users</code>"
   **                              <li>"<code>Groups</code>"
   **                            </ul>
   **                            as  defined by the associated resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Lookup} for type
   **                            <code>T</code>.
   */
  public <T extends Resource> Lookup<T> lookupRequest(final String context, final Long id) {
    return new Lookup<T>(requestTarget(context, id), this.client.typeContent(), this.client.typeAccept());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupRequest
  /**
   ** Build a request to lookup a known SCIM resource from the Service Provider.
   **
   ** @param  <T>                the Java type of the resource to return.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>Users</code>"
   **                              <li>"<code>Groups</code>"
   **                            </ul>
   **                            as  defined by the associated resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Lookup} for type
   **                            <code>T</code>.
   */
  public <T extends Resource> Lookup<T> lookupRequest(final String context, final String id) {
    return new Lookup<T>(requestTarget(context, id), this.client.typeContent(), this.client.typeAccept());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createRequest
  /**
   ** Build a request to create the provided new SCIM resource at the Service
   ** Provider.
   **
   ** @param  <T>                the Java type of the resource to return.
   **                            <br>
   **                            Allowed object is <code>%lt;T&gt;</code>.
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>Users</code>"
   **                              <li>"<code>Groups</code>"
   **                            </ul>
   **                            as defined by the associated resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  resource           the new resource to create.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Create} for type
   **                            <code>T</code>.
   */
  @SuppressWarnings("unchecked")
  public <T extends Resource> Create<T> createRequest(final String context, final T resource) {
    return Create.<T>build(this.target.path(context), resource, this.client.typeContent(), this.client.typeAccept());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifyRequest
  /**
   ** Build a request to modify a SCIM resource by replacing the resource's
   ** attributes at the Service Provider.
   **
   ** @param  <T>                the Java type of the resource to return.
   **                            <br>
   **                            Allowed object is <code>%lt;T&gt;</code>.
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>Users</code>"
   **                              <li>"<code>Groups</code>"
   **                            </ul>
   **                            as defined by the associated resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  resource           the resource to modify.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Replace} for type
   **                            <code>T</code>.
   */
  @SuppressWarnings("unchecked")
  public <T extends Resource> Replace<T> modifyRequest(final String context, final T resource) {
    return Replace.<T>build(requestTarget(context, resource.id()), resource, this.client.typeContent(), this.client.typeAccept());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteRequest
  /**
   ** Build a request to create the provided new SCIM resource at the Service
   ** Provider.
   **
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>Users</code>"
   **                              <li>"<code>Groups</code>"
   **                            </ul>
   **                            as defined by the associated resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  identifier         the identifier of the resource resource to
   **                            delete.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Delete}.
   */
  public Delete deleteRequest(final String context, final Integer identifier) {
    return Delete.build(requestTarget(context, identifier), this.client.typeContent(), this.client.typeAccept());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteRequest
  /**
   ** Build a request to create the provided new SCIM resource at the Service
   ** Provider.
   **
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>Users</code>"
   **                              <li>"<code>Groups</code>"
   **                            </ul>
   **                            as defined by the associated resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  identifier         the identifier of the resource resource to
   **                            delete.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Delete}.
   */
  public Delete deleteRequest(final String context, final Long identifier) {
    return Delete.build(requestTarget(context, identifier), this.client.typeContent(), this.client.typeAccept());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteRequest
  /**
   ** Build a request to create the provided new SCIM resource at the Service
   ** Provider.
   **
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>Users</code>"
   **                              <li>"<code>Groups</code>"
   **                            </ul>
   **                            as defined by the associated resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  identifier         the identifier of the resource resource to
   **                            delete.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Delete}.
   */
  public Delete deleteRequest(final String context, final String identifier) {
    return Delete.build(requestTarget(context, identifier), this.client.typeContent(), this.client.typeAccept());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requestTarget
  /**
   ** Factory method to create a {@link WebTarget} for the request.
   ** <p>
   ** Create a JAX-RS web target whose URI refers to the
   ** <code>ServiceEndpoint.contextURL()</code> the JAX-RS / Jersey application
   ** is deployed at.
   ** <p>
   ** This method is an equivalent of calling
   ** <code>client().target(endpoint.contextURL())</code>.
   **
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>Users</code>"
   **                              <li>"<code>Groups</code>"
   **                            </ul>
   **                            as defined by the associated resource type.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   ** @param  identifier         the identifier of the resource resource to
   **                            delete.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the created JAX-RS web target.
   **                            <br>
   **                            Possible object {@link WebTarget}.
   */
  protected final WebTarget requestTarget(final String context, final Integer identifier) {
    return requestTarget(context, String.valueOf(identifier));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requestTarget
  /**
   ** Factory method to create a {@link WebTarget} for the request.
   ** <p>
   ** Create a JAX-RS web target whose URI refers to the
   ** <code>ServiceEndpoint.contextURL()</code> the JAX-RS / Jersey application
   ** is deployed at.
   ** <p>
   ** This method is an equivalent of calling
   ** <code>client().target(endpoint.contextURL())</code>.
   **
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>Users</code>"
   **                              <li>"<code>Groups</code>"
   **                            </ul>
   **                            as defined by the associated resource type.
   **                            <br>
   **                            Allowed object is {@link Long}.
   ** @param  identifier         the identifier of the resource resource to
   **                            delete.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the created JAX-RS web target.
   **                            <br>
   **                            Possible object {@link WebTarget}.
   */
  protected final WebTarget requestTarget(final String context, final Long identifier) {
    return requestTarget(context, String.valueOf(identifier));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requestTarget
  /**
   ** Factory method to create a {@link WebTarget} for the request.
   ** <p>
   ** Create a JAX-RS web target whose URI refers to the
   ** <code>ServiceEndpoint.contextURL()</code> the JAX-RS / Jersey application
   ** is deployed at.
   ** <p>
   ** This method is an equivalent of calling
   ** <code>client().target(endpoint.contextURL())</code>.
   **
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>Users</code>"
   **                              <li>"<code>Groups</code>"
   **                            </ul>
   **                            as defined by the associated resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  identifier         the identifier of the resource resource to
   **                            delete.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the created JAX-RS web target.
   **                            <br>
   **                            Possible object {@link WebTarget}.
   */
  protected final WebTarget requestTarget(final String context, final String identifier) {
    return this.target.path(context).path(identifier);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveTarget
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
  protected final WebTarget resolveTarget(final URI uri) {
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