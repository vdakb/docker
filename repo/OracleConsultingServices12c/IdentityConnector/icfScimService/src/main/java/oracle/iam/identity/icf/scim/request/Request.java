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

    File        :   Request.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Request.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.scim.request;

import java.util.Map;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.MultivaluedHashMap;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.Invocation;

import oracle.iam.identity.icf.foundation.utility.StringUtility;
import oracle.iam.identity.icf.foundation.utility.CollectionUtility;

import oracle.iam.identity.icf.scim.schema.Resource;

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
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The SCIM media type string */
  public static final String                     MEDIA_TYPE = "application/scim+json";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the web target to send the request */
  private  WebTarget                             target;

  /** the default content type to embed in the request */
  private final String                           content;

  /** the default accepted types to embed in the request */
  private final List<String>                     accept;

  /** arbitrary request headers */
  protected final MultivaluedMap<String, Object> headers    = new MultivaluedHashMap<String, Object>();

  /** arbitrary query parameters */
  protected final MultivaluedMap<String, Object> parameter  = new MultivaluedHashMap<String, Object>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a a new SCIM request.
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
    this.content = content == null ? MEDIA_TYPE  : content;
    this.accept  = (accept == null || accept.length == 0) ? CollectionUtility.list(MEDIA_TYPE) : CollectionUtility.list(accept);
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
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the header value(s).
   **                            <br>
   **                            Allowed object is array of {@link Object}.
   **
   ** @return                    the <code>Request</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Request</code> for
   **                            type <code>T</code>.
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
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the query parameter value(s).
   **                            <br>
   **                            Allowed object is array of {@link Object}.
   **
   ** @return                    the <code>Request</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Request</code> for
   **                            type <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public T parameter(final String name, final Object... value) {
    this.parameter.addAll(name, value);
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   typeContent
  /**
   ** Returns the media type for any content sent to the server.
   **
   ** @return                    a string describing the media type of content
   **                            sent to the Service Provider.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  protected String typeContent() {
    return this.content;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   typeAccept
  /**
   ** Returns the media type(s) that are acceptable as a return from the server.
   **
   ** @return                    the media type(s) that are acceptable as a
   **                            return from the server.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link String}.
   */
  protected List<String> typeAccept() {
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
  // Method:   resourceVersion
  /**
   ** Returns the meta.version attribute of the resource.
   **
   ** @param  resource           the {@link Resource} whose version to retrieve.
   **                            <br>
   **                            Allowed object is {@link Resource}.
   **
   ** @return                    the resource version.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws IllegalArgumentException if the {@link Resource} does not contain
   **                                  a the meta.version attribute.
   */
  protected static String resourceVersion(final Resource resource)
    throws IllegalArgumentException {

    if (resource == null || resource.metadata() == null || resource.metadata().version() == null)
      throw new IllegalArgumentException("Resource version must be specified by meta.version");

    return resource.metadata().version();
  }

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
   */
  WebTarget build() {
    for (Map.Entry<String, List<Object>> queryParam : this.parameter.entrySet()) {
      this.target = this.target.queryParam(queryParam.getKey(), queryParam.getValue().toArray());
    }
    return this.target;
  }
}