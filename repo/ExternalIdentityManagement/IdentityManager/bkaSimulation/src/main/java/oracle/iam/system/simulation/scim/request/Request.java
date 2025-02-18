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

    Copyright © 2018. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Service Simulation
    Subsystem   :   Generic SCIM Interface

    File        :   Request.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Request.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.scim.request;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import javax.ws.rs.ProcessingException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.MultivaluedHashMap;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.Invocation;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.system.simulation.ServiceException;

import oracle.iam.system.simulation.scim.AbstractContext;

import oracle.iam.system.simulation.scim.schema.Resource;

import oracle.iam.system.simulation.scim.domain.ErrorResponse;

////////////////////////////////////////////////////////////////////////////////
// class Request
// ~~~~~ ~~~~~~~
/**
 ** Abstract SCIM request.
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
public class Request<T extends Request> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the web target to send the request */
  private WebTarget                              target;

  private String                                 content   = AbstractContext.MEDIA_TYPE;

  private List<String>                           accept    = new ArrayList<String>();

  /** arbitrary request headers */
  protected final MultivaluedMap<String, Object> headers   = new MultivaluedHashMap<String, Object>();

  /** arbitrary query parameters */
  protected final MultivaluedMap<String, Object> parameter = new MultivaluedHashMap<String, Object>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a a new SCIM request.
   **
   ** @param  target             the {@link WebTarget} to send the request.
   */
  public Request(final WebTarget target) {
    // ensure inheritance
    super();

	  // initialize instance attributes
    this.target = target;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   header
  /**
   ** Add an arbitrary HTTP header to the request.
   **
   ** @param  name               the header name.
   ** @param  value              the header value(s).
   **
   ** @return                    the {@link Request} to allow method chaining.
   **                            <br>
   **                            Possible object is {@link Request}.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public T header(final String name, final Object... value) {
    this.headers.addAll(name, value);
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parameter
  /**
   ** Add an arbitrary query parameter to the request.
   **
   ** @param  name               the query parameter name.
   ** @param  value              the query parameter value(s).
   **
   ** @return                    the {@link Request} to allow method chaining.
   **                            <br>
   **                            Possible object is {@link Request}.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public T parameter(final String name, final Object... value) {
    this.parameter.addAll(name, value);
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contentType
  /**
   ** Sets the media type for any content sent to the server.
   ** <br>
   ** The default value is
   ** {@link AbstractContext#MEDIA_TYPE "application/scim+json"}.
   **
   ** @param  value              a string describing the media type of content
   **                            sent to the server.
   **                            <br>
   **                            Allowed object is @link String}.
   **
   ** @return                    the {@link Request} to allow method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public T contentType(final String value) {
    this.content = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contentType
  /**
   ** Returns the media type for any content sent to the server.
   **
   ** @return                    the media type for any content sent to the
   **                            server.
   **                            <br>
   **                            Possible object is @link String}.
   */
  protected String contentType() {
    return this.content;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   acceptType
  /**
   ** Sets the media type(s) that are acceptable as a return from the server.
   ** <br>
   ** The default accepted media types are
   ** {@link AbstractContext#MEDIA_TYPE ("application/scim+json")} and
   ** <code>MediaType#APPLICATION_JSON ("application/json")</code>
   **
   ** @param  value              a string (or strings) describing the media type
   **                            that will be accepted from the server.
   **                            <br>
   **                            This parameter must not be <code>null</code>.
   **                            <br>
   **                            Allowed object array of @link String}.
   **
   ** @return                    the {@link Request} to allow method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public T acceptType(final String... value) {
    if ((value == null) || (value.length == 0))
      throw new IllegalArgumentException("Accepted media types must not be null or empty");

    this.accept.clear();
    for (String cursor : value) {
      this.accept.add(cursor);
    }

    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   acceptType
  /**
   ** Returns the media type(s) that are acceptable as a return from the server.
   **
   ** @return                    the media type(s) that are acceptable as a
   **                            return from the server.
   **                            <br>
   **                            Possible object array of @link List} where each
   **                            element is of type {@link String}.
   */
  protected List<String> acceptType() {
    return this.accept;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   target
  /**
   ** Returns the unbuilt {@link WebTarget} for the request.
   ** <br>
   ** In most cases, {@link #build()} should be used instead.
   **
   ** @return                    the {@link WebTarget} for the request.
   **                            <br>
   **                            Possible object is @link WebTarget}.
   */
  protected WebTarget target() {
    return target;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceVersion
  /**
   ** Returns the meta.version attribute of the resource.
   **
   ** @param  resource           the {@link Resource} whose version to retrieve.
   **                            <br>
   **                            Allowed object array of @link Resource}.
   **
   ** @return                    the resource version.
   **                            <br>
   **                            Possible object is @link String}.
   **
   ** @throws IllegalArgumentException if the {@link Resource} does not contain
   **                                  a the meta.version attribute.
   */
  static String resourceVersion(final Resource resource)
    throws IllegalArgumentException {

    if (resource == null || resource.metadata() == null || resource.metadata().version() == null)
      throw new IllegalArgumentException("Resource version must be specified by meta.version");

    return resource.metadata().version();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toException
  /**
   ** Convert a JAX-RS response to a {@link ServiceException}.
   **
   ** @param  response           the JAX-RS response.
   **                            <br>
   **                            Allowed object array of @link Response}.
   **
   ** @return                    the converted {@link ServiceException}.
   **                            <br>
   **                            Possible object is @link ServiceException}.
   */
  static ServiceException toException(final Response response) {
    try {
      final ErrorResponse error = response.readEntity(ErrorResponse.class);
      // if are able to read an error response, use it to build the exception.
      final ServiceException exception = (error == null) ? ServiceException.build(response.getStatus()) : ServiceException.build(error.status());
      response.close();
      return exception;
    }
    catch (ProcessingException e) {
      // if not, use the http status code to determine the exception.
      return ServiceException.build(response.getStatus());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildRequest
  /**
   ** Factory method to create a {@link Invocation.Builder} for the request.
   **
   ** @return                    the {@link Invocation.Builder} for the request.
   **                            <br>
   **                            Possible object is @link Invocation.Builder}.
   */
  Invocation.Builder buildRequest() {
    Invocation.Builder builder = build().request(this.accept.toArray(new String[this.accept.size()]));
    for (Map.Entry<String, List<Object>> header : this.headers.entrySet()) {
      builder = builder.header(header.getKey(), StringUtility.listToString(header.getValue(), ','));
    }
    return builder;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a {@link WebTarget} for the request.
   **
   ** @return                    the {@link WebTarget} for the request.
   **                            <br>
   **                            Possible object is @link WebTarget}.
   */
  WebTarget build() {
    for (Map.Entry<String, List<Object>> queryParam : this.parameter.entrySet()) {
      this.target = this.target.queryParam(queryParam.getKey(), queryParam.getValue().toArray());
    }
    return this.target;
  }
}