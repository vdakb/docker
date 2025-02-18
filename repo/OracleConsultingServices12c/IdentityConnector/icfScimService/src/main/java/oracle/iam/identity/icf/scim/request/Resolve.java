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

    File        :   Resolve.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Resolve.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.scim.request;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.ProcessingException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.HttpHeaders;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.Invocation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import com.fasterxml.jackson.databind.node.ObjectNode;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.rest.ServiceException;

import oracle.iam.identity.icf.scim.schema.Support;
import oracle.iam.identity.icf.scim.schema.Resource;

import oracle.iam.identity.icf.scim.domain.SearchRequest;

import oracle.iam.identity.icf.scim.response.ListResponse;
import oracle.iam.identity.icf.scim.response.ResponseHandler;
import oracle.iam.identity.icf.scim.response.ListResponseHandler;

////////////////////////////////////////////////////////////////////////////////
// class Resolve
// ~~~~~ ~~~~~~~
/**
 ** A factory for SCIM resolve requests.
 **
 ** @param  <T>                  the type of the implementation.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations of the request extending
 **                              this class (requests can return their own
 **                              specific type instead of type defined by this
 **                              class only).
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Resolve<T extends Resource> extends Return<Resolve>  {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The version to match */
  private String version;

  /** The filter string used to request a subset of resources */
  private String filter;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a a new SCIM find request.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new Replace()" and enforces use of the public method below.
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   ** @param  content            a string describing the media type of content
   **                            sent to the Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  accept             a string (or strings) describing the media type
   **                            that will be accepted from the Service
   **                            Provider.
   **                            <br>
   **                            This parameter must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  private Resolve(final WebTarget target, final String content, final String... accept) {
    // ensure inheritance
    super(target, content, accept);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   match
  /**
   ** Lookup the resource only if the resource has been modified since the
   ** provided version. If the resource has not been modified,
   ** NotModifiedException will be thrown when calling invoke.
   **
   ** @param  version          the version of the resource to compare.
   **                          <br>
   **                          Allowed object is {@link String}.
   **
   **
   ** @return                  the <code>Resolve</code> to allow method
   **                          chaining.
   **                          <br>
   **                          Possible object is <code>Resolve</code>.
   */
  public Resolve match(final String version) {
    this.version = version;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filter
  /**
   ** Sets the filtering request of resources.
   **
   ** @param  filter             the filter string used to request a subset of
   **                            resources.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Resolve</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Resolve</code>.
   */
  public Resolve filter(final String filter) {
    this.filter = filter;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a new SCIM search request that will GET the
   ** given resource at the given web target by applying the filter conditions.
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   ** @param  content            a string describing the media type of content
   **                            sent to the Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  accept             a string (or strings) describing the media type
   **                            that will be accepted from the Service
   **                            Provider.
   **                            <br>
   **                            This parameter must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a JAX-RS request.
   **                            <br>
   **                            Possible object is <code>Delete</code>.
   */
  public static Resolve build(final WebTarget target, final String content, final String... accept) {
    return new Resolve(target, content, accept);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build (overidden)
  /**
   ** Factory method to create a {@link WebTarget} for the request.
   **
   ** @return                    the {@link WebTarget} for the request.
   **                            <br>
   **                            Possible object is {@link WebTarget}.
   */
  @Override
  WebTarget build() {
    WebTarget target = super.build();
    if (this.filter != null) {
      // from the docs:
      // queryParam returns: a new target instance.
      target = target.queryParam(SearchRequest.FILTER, this.filter);
    }
    return target;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   ** Invoke the SCIM retrieve request using GET.
   **
   ** @param  <T>                the type of objects to return.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  clazz              the Java class type used to determine the type
   **                            to return.
   **                            <br>
   **                            Allowed object is {@link Class} of type
   **                            <code>T</code>.
   **
   ** @return                    the {@link ListResponse} containing the search
   **                            results.
   **                            <br>
   **                            Possible object is {@link ListResponse} where
   **                            the payload is of type <code>T</code>.
   **
   ** @throws SystemException    if an error occurred.
   */
  public <T> ListResponse<T> invoke(final Class<T> clazz)
    throws SystemException {

    final ListResponseHandler<T> handler = new ListResponseHandler<T>();
    invoke(handler, clazz);
    return handler.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   ** Invoke the SCIM lookup request.
   **
   ** @param  <T>                the type of resource to return.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   ** @param  handler            the search result handler that should be used
   **                            to process the resources.
   **                            <br>
   **                            Allowed object is {@link ResponseHandler} of
   **                            type <code>T</code>.
   ** @param  clazz              the Java {@link Class} type used to determine
   **                            the type to return.
   **                            <br>
   **                            Allowed object is {@link Class} of type
   **                            <code>T</code>.
   **
   ** @throws SystemException    if the SCIM service provider responded with an
   **                            error.
   */
  public <T> void invoke(final ResponseHandler<T> handler, final Class<T> clazz)
    throws SystemException {

    Response response;
    try {
      response = buildRequest().get();
    }
    catch (ProcessingException e) {
      throw ServiceException.from(e, target().getUri());
    }

    try {
      if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
        InputStream inputStream = response.readEntity(InputStream.class);
        try {
          final JsonParser parser = Support.objectReader().getFactory().createParser(inputStream);
          try {
            parser.nextToken();
            boolean stop = false;
            while (!stop && parser.nextToken() != JsonToken.END_OBJECT) {
              final String field = parser.getCurrentName();
              parser.nextToken();
              if (ListResponse.SCHEMA.equals(field))
                parser.skipChildren();
              else if (ListResponse.TOTAL.equals(field))
                handler.total(parser.getIntValue());
              else if (ListResponse.START.equals(field))
                handler.start(parser.getIntValue());
              else if (ListResponse.ITEMS.equals(field))
                handler.items(parser.getIntValue());
              // Service Providers are not following strictly the RFC
              // regarding naming conventions; somtimes resources is
              // returned instead of Resources how it's required by the RFC.
              else if (ListResponse.RESOURCE.equals(field) || "resources".equals(field))
                while (parser.nextToken() != JsonToken.END_ARRAY) {
                  if (!handler.resource(parser.readValueAs(clazz))) {
                    stop = true;
                    break;
                  }
                }
              else if (Support.namespace(field))
                handler.extension(field, parser.<ObjectNode>readValueAsTree());
              else
                // just skip this field
                parser.nextToken();
            }
          }
          finally {
            if (inputStream != null) {
              inputStream.close();
            }
            parser.close();
          }
        }
        catch (IOException e) {
          throw ServiceException.unhandled(e);
        }
      }
      else {
        throw ServiceException.from(response);
      }
    }
    finally {
      response.close();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildRequest (overridden)
  /**
   ** Factory method to create a {@link Invocation.Builder} for the request.
   **
   ** @return                    the {@link Invocation.Builder} for the request.
   **                            <br>
   **                            Possible object is {@link Invocation.Builder}.
   */
  @Override
  protected final Invocation.Builder buildRequest() {
    Invocation.Builder request = super.buildRequest();
    if (this.version != null) {
      request.header(HttpHeaders.IF_NONE_MATCH, this.version);
    }
    return request;
  }
}