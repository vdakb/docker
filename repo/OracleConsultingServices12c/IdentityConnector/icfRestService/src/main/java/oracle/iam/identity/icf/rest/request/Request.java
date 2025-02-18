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
    Subsystem   :   Generic REST Library

    File        :   Request.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Request.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.rest.request;

import java.util.Map;
import java.util.List;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.Invocation;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.MultivaluedHashMap;

import oracle.iam.identity.icf.foundation.utility.StringUtility;
import oracle.iam.identity.icf.foundation.utility.CollectionUtility;

////////////////////////////////////////////////////////////////////////////////
// abstract class Request
// ~~~~~~~~ ~~~~~ ~~~~~~~
/**
 ** The payload for a request that can be used to access data for all REST
 ** resource.
 **
 ** @param  <T>                  the type of entity contained within the
 **                              request.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations of the resources
 **                              implementing this class (resources can return
 **                              their own specific type instead of type defined
 **                              by this class only).
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class Request<T extends Request> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the web target to send the request */
  private WebTarget                              target;

  /** the default content type to embed in the request */
  private String                                 content   = MediaType.APPLICATION_JSON;

  /** the default accepted types to embed in the request */
  private List<String>                           accept    = CollectionUtility.list(MediaType.APPLICATION_JSON);

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
   ** Constructs a a new REST request.
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   */
  protected Request(final WebTarget target) {
    // ensure inheritance
    super();

	  // initialize instance attributes
    this.target = target;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a a new REST request.
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
  protected Request(final WebTarget target, final String content, final String... accept) {
    // ensure inheritance
    super();

	  // initialize instance attributes
    this.target  = target;
    this.content = content == null ? MediaType.APPLICATION_JSON  : content;
    this.accept  = (accept == null || accept.length == 0) ? CollectionUtility.list(MediaType.APPLICATION_JSON) : CollectionUtility.list(accept);
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
   ** The default value is {@link MediaType#APPLICATION_JSON "application/json"}.
   **
   ** @param  value              a string describing the media type of content
   **                            sent to the server.
   **
   ** @return                    the {@link Request} to allow method chaining.
   **                            <br>
   **                            Possible object is {@link Request}.
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
   */
  protected String contentType() {
    return this.content;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   acceptType
  /**
   ** Sets the media type(s) that are acceptable as a return from the server.
   ** <br>
   ** The default accepted media type is
   ** {@link MediaType#APPLICATION_JSON "application/json"}.
   **
   ** @param  value              a string (or strings) describing the media type
   **                            that will be accepted from the server.
   **                            <br>
   **                            This parameter must not be <code>null</code>.
   **
   ** @return                    the {@link Request} to allow method chaining.
   **                            <br>
   **                            Possible object is {@link Request}.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public T acceptType(final String... value) {
    if ((value == null) || (value.length == 0))
      throw new IllegalArgumentException("Accepted media types must not be null or empty");

    this.accept.clear();
    for (String cursor : value)
      this.accept.add(cursor);

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
   **                            Possible object is {@link List} where each
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
   **                            Possible object is {@link WebTarget}.
   */
  protected WebTarget target() {
    return target;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildRequest
  /**
   ** Factory method to create a {@link Invocation.Builder} for the request.
   **
   ** @return                    the {@link Invocation.Builder} for the request.
   **                            <br>
   **                            Possible object is {@link Invocation.Builder}.
   */
  protected Invocation.Builder buildRequest() {
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
   **                            Possible object is {@link WebTarget}.
   */
  protected WebTarget build() {
    for (Map.Entry<String, List<Object>> queryParam : this.parameter.entrySet()) {
      this.target = this.target.queryParam(queryParam.getKey(), queryParam.getValue().toArray());
    }
    return this.target;
  }
}