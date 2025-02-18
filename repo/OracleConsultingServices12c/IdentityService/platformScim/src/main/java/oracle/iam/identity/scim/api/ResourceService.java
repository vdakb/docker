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

    System      :   Identity Service Library
    Subsystem   :   Generic SCIM Interface

    File        :   ResourceService.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    ResourceService.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.scim.api;

import java.util.Map;
import java.util.LinkedHashMap;

import java.net.URI;

import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.MultivaluedMap;

import javax.ws.rs.core.Response;

import oracle.iam.platform.scim.ProcessingException;
import oracle.iam.platform.scim.ResourceTypeDefinition;

import oracle.iam.platform.scim.schema.Resource;
import oracle.iam.platform.scim.schema.Generic;

import oracle.iam.platform.scim.request.PatchRequest;
import oracle.iam.platform.scim.request.SearchRequest;

import oracle.iam.platform.scim.response.SearchResponse;

////////////////////////////////////////////////////////////////////////////////
// interface ResourceService
// ~~~~~~~~~ ~~~~~~~~~~~~~~~
/**
 ** A JAX-RS resource interface to manipulate User resources.
 ** <p>
 ** <a name="query"></a>
 ** <b>Notes:</b>
 ** <br>
 ** The query parameters <i>attributes</i> and <i>excludedAttributes</i> found
 ** in methods of this interface are aimed regarded in section 3.9 of SCIM
 ** specification protocol document (RFC 7644).
 ** <p>
 ** Every SCIM service operation that returns resources (e.g Users, Groups,
 ** etc.) offers the possibility to specify which attributes can be included for
 ** every resource part of the response. The default behavior is returning those
 ** attributes that according to the resource Schema have returnability =
 ** "always" in addition to those with returnability = "default".
 ** <p>
 ** <a name="emit"></a>
 ** <i><b>attributes</b></i> is used to override the default attribute set, so
 ** when supplying a not-null or not empty string, the attributes included in
 ** the resource(s) of the response will be those with returnability = "always"
 ** as well as those specified by <i>attributes</i>.
 ** <p>
 ** This query parameter consists of  a comma-separated list of attribute names.
 ** An example of a valid value for <i>emit</i> when the resource of interest
 ** is User, could be: <code>userName, active, name.familyName, addresses,
 ** emails.value, emails.type, urn:ietf:params:scim:schemas:extension:hcm:2.0:User:validFrom</code>
 ** <p>
 ** <b>Note</b>:
 ** <br>
 ** Attributes marked with returnability = "never" (such as a User password)
 ** will always be filtered out from the output, so including such attributes in
 ** <i>attributes</i> has no effect.
 ** <p>
 ** <a name="omit"></a>
 ** <i><b>excludedAttributs</b></i> is used to specify the set of attributes
 ** that should be excluded from the default attribute set. In this sense, the
 ** resources found in the response will include the attributes whose
 ** returnability = "always" in addition to those with returnability = "default"
 ** except for those included in <i>excludedAttributs</i>. As with
 ** <i>excludedAttributs</i>, this parameter must be in the form of a
 ** comma-separated list of attribute names.
 ** <p>
 ** <i>attributes</i> and <i>excludedAttributs</i> are mutually exclusive: if
 ** both are provided only <i>attributes</i> will be taken into account to
 ** compute the output attribute
 ** set.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface ResourceService<T extends Resource> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The path name of the unique identifier to retrieve a SCIM resource.
   */
  static final String IDENTIFIER = "{id}";

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   location
  /**
   ** Returns a fullqualified {@link URI} to inject it in the returned SCIM
   ** matadata resources.
   **
   ** @param  type               the resource type definition for resources to
   **                            prepare.
   **                            <br>
   **                            Allowed object is
   **                            {@link ResourceTypeDefinition}.
   ** @param  request            the {@link UriInfo} of the request to build the
   **                            respone for.
   **                            <br>
   **                            Allowed object is {@link UriInfo}.
   **
   ** @return                   a fullqualified {@link URI}.
   **                           <br>
   **                           Possible object is {@link URI}.
   */
  static URI location(final ResourceTypeDefinition type, final UriInfo request) {
    return request.getBaseUriBuilder().path(type.endpoint()).buildFromMap(flattenParameter(request));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   flatten
  /**
   ** Simplified a JAX-RS {@link MultivaluedMap}.
   **
   ** @param  request            the {@link UriInfo} of the request to build the
   **                            respone for.
   **                            <br>
   **                            Allowed object is {@link UriInfo}.
   **
   ** @return                    a {@link Map} containing the path elements of
   **                            an JAX-RS {@link UriInfo}.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type {@link String} as the key
   **                            and {@link String} as the value.
   */
  static Map<String, String> flattenParameter(final UriInfo request) {
    final MultivaluedMap<String, String> multivalued  = request.getPathParameters();
    final Map<String, String>            singlevalued = new LinkedHashMap<String, String>();
    for (String k : multivalued.keySet()) {
      singlevalued.put(k, multivalued.getFirst(k));
    }
    return singlevalued;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   search
  /**
   ** REST service search request for SCIM resources using GET (see section
   ** 3.4.2 of RFC 7644).
   ** <p>
   ** The query parameter of the REST service request may contain:
   ** <ul>
   **   <li><b>startIndex</b> - the 1-based index of the first query result.
   **                           <br>
   **                           If a negative integer or <code>null</code> is
   **                           provided, the search is performed as if
   **                           <code>1</code> was provided as value.
   **   <li><b>count</b>      - specifies the desired maximum number of query
   **                           results per page the response must include.
   **                           <br>
   **                           If <code>null</code> is provided, the maximum
   **                           supported by the server is used. If
   **                           <code>count</code> is zero, this is interpreted
   **                           as no results should be included (only the total
   **                           amount is). If a negative number is supplied,
   **                           the search is performed as if zero was provided
   **                           as value.
   **   <li><b>filter</b>     - a filter expression so that the search will
   **                           return only those resources matching the
   **                           expression.
   **                           <br>
   **                           To learn more about SCIM filter expressions and
   **                           operators, see section 3.4.2.2 of RFC 7644.
   **   <li><b>sortBy</b>     - specifies the attribute whose value will be used
   **                           to order the returned resources. If the results
   **                           will be sorted by <code>id</code> attribute.
   **   <li><b>sortOrder</b>  - the order in which the <code>sortBy</code>
   **                           parameter is applied. Allowed values are
   **                           <code>ascending</code> or
   **                           <code>descending</code>, being
   **                           <code>ascending</code> the default if
   **                           <code>null</code>or an unknown value is passed.
   **   <li><b>attributes</b> - see notes about <a href="#emit">attributes</a>
   **                           query parameter.
   **   <li><b>excludedAttributes</b> - see notes about
   **                           <a href="#omit">excludedAttributes</a> query
   **                           parameter.
   ** </ul>
   ** @param  context            the request context.
   **                            <br>
   **                            Allowed object is {@link UriInfo}.
   **
   ** @return                    an object abstracting the response obtained
   **                            from the server to this request.
   **                            <br>
   **                            A succesful response for this operation
   **                            contains a status code of 200 and a
   **                            {@link oracle.iam.platform.scim.response.ListResponse ListResponse}
   **                            in the entity body (holding a collection of
   **                            SCIM resources).
   **
   ** @throws ProcessingException if the request fails.
   */
  SearchResponse search(final UriInfo context)
    throws ProcessingException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   search
  /**
   ** REST service search request for SCIM resources using POST (see section
   ** 3.4.3 of RFC 7644).
   **
   ** @param  request            An object containing the parameters for the
   **                            query to execute.
   **                            <br>
   **                            These are the same parameters passed in the URL
   **                            for searches, for example in
   **                            {@link #search(UriInfo)}
   **                            <br>
   **                            Allowed object is {@link SearchRequest}.
   ** @param  context            the request context.
   **                            <br>
   **                            Allowed object is {@link UriInfo}.
   **
   ** @return                    an object abstracting the response obtained
   **                            from the server to this request.
   **                            <br>
   **                            A succesful response for this operation
   **                            contains a status code of 200 and a
   **                            {@link oracle.iam.platform.scim.response.ListResponse ListResponse}
   **                            in the entity body (holding a collection of
   **                            SCIM resources).
   **
   ** @throws ProcessingException if the request fails.
   */
  SearchResponse search(final SearchRequest request, final UriInfo context)
    throws ProcessingException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** REST service request to retrieve a certain SCIM {@link Generic} by its
   ** <code>id</code> using GET (as per section 3.4.2 of RFC 7644).
   **
   ** @param  id                 the identifier of the {@link Generic} to
   **                            lookup.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  context            the request context.
   **                            <br>
   **                            Allowed object is {@link UriInfo}.
   **
   ** @return                    the {@link Generic} mapped at <code>id</code>
   **                            at the Service Provider.
   **                            <br>
   **                            Possible object is {@link Generic}.
   **
   ** @throws ProcessingException if the request fails.
   */
  Generic lookup(final String id, final UriInfo context)
    throws ProcessingException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** REST service request to create a certain SCIM resource using POST (as per
   ** section 3.3 of RFC 7644).
   **
   ** @param  resource           an object that represents the SCIM resource to
   **                            create.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   ** @param  context            the request context.
   **                            <br>
   **                            Allowed object is {@link UriInfo}.
   **
   ** @return                    an object abstracting the response obtained
   **                            from the server to this request.
   **                            <br>
   **                            A succesful response for this operation
   **                            contains a status code of 201 (created) and a
   **                            {@link Generic} in the entity body (the
   **                            resource just created).
   **                            <br>
   **                            Possible object is {@link Generic}.
   **
   ** @throws ProcessingException if the request fails.
   */
  Generic create(final T resource, final UriInfo context)
    throws ProcessingException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace
  /**
   ** REST service request to modify a certain SCIM resource using PUT (as per
   ** section 3.5.1 of RFC 7644)
   ** <p>
   ** This operation is not suitable to delete/remove/nullify attributes. For
   ** this purpose you can use the PATCH operation instead. PUT is intended to
   ** do replacements using the (not-null) values supplied in the
   ** <code>resource</code> argument.
   **
   ** @param  id                 the identifier of the resource to modify.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  resource           an object that contains the data to update on a
   **                            destination resource.
   **                            <br>
   **                            There is no need to supply a full resource,
   **                            just provide one with the attributes which are
   **                            intended to be replaced in the destination.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   ** @param  context            the request context.
   **                            <br>
   **                            Allowed object is {@link UriInfo}.
   **
   ** @return                    an object abstracting the response obtained
   **                            from the server to this request.
   **                            <br>
   **                            A succesful response for this operation
   **                            contains a status code of 200 and a
   **                            {@link Generic} in the entity body (the
   **                            resource after the modify took place).
   **                            <br>
   **                            Possible object is {@link Generic}.
   **
   ** @throws ProcessingException if the request fails.
   */
  Generic replace(final String id, final T resource, final UriInfo context)
    throws ProcessingException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** REST service request to modify a certain SCIM resource using PATCH (as per
   ** section 3.5.2 of RFC 7644).
   **
   ** @param  id                 the identifier of the resource to modify.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  request            a {@link PatchRequest} that contains the
   **                            operations to apply upon the resource being
   **                            modified.
   **                            <br>
   **                            Allowed object is {@link PatchRequest}.
   ** @param  context            the request context.
   **                            <br>
   **                            Allowed object is {@link UriInfo}.
   **
   ** @return                    an object abstracting the response obtained
   **                            from the server to this request.
   **                            <br>
   **                            A succesful response for this operation
   **                            contains a status code of 200 and a
   **                            {@link Generic} in the entity body (the
   **                            resource after the modify took place).
   **                            <br>
   **                            Possible object is {@link Generic}.
   **
   ** @throws ProcessingException if the request fails.
   */
  Generic modify(final String id, final PatchRequest request, final UriInfo context)
    throws ProcessingException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** REST service request to remove a certain SCIM resource by its
   ** <code>id</code> using DELETE (as per section 3.6 of RFC 7644).
   **
   ** @param  id                 the identifier of the resource to remove.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  context            the request context.
   **                            <br>
   **                            Allowed object is {@link UriInfo}.
   **
   ** @return                    an object abstracting the response obtained
   **                            from the server to this request.
   **                            <br>
   **                            A succesful response for this operation
   **                            contains a status code of 204 (no content).
   **                            <br>
   **                            Possible object is {@link Response}.
   **
   ** @throws ProcessingException if the request fails.
   */
  Response delete(final String id, final UriInfo context)
    throws ProcessingException;
}