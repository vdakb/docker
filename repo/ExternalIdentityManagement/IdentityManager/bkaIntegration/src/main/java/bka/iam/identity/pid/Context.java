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

    Copyright 2022 All Rights reserved

    -----------------------------------------------------------------------

    System      :   BKA Identity Manager
    Subsystem   :   Identity Services Integration

    File        :   Context.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Context.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      12.07.2022  Dsteding    First release version
*/

package bka.iam.identity.pid;

import java.net.URI;

import javax.ws.rs.client.WebTarget;

import oracle.hst.foundation.logging.Loggable;

import oracle.iam.identity.foundation.TaskException;

import bka.iam.identity.rest.ServiceClient;
import bka.iam.identity.rest.ServiceResource;

import bka.iam.identity.pid.request.Lookup;
import bka.iam.identity.pid.request.Create;
import bka.iam.identity.pid.request.Delete;

///////////////////////////////////////////////////////////////////////////////
// class Context
// ~~~~~ ~~~~~~~
/**
 ** This is service consumer specific context class that extends Jersey's client
 ** interface.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Context extends ServiceClient {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** An HTTP URI to the endpoint is used to retrieve information about the
   ** resource type {@link User} provided by the Service Provider.
   */
  static final String     ENDPOINT_PID        = "pid";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the endpoint to access the Anonymous Identifier Generator API **/
  private final WebTarget target;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Context</code> which is associated with the specified
   ** {@link ServiceClient} for configuration purpose.
   **
   ** @param  loggable           the {@link Loggable} which has
   **                            instantiated this {@link ServiceClient}.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  endpoint           the {@link ServiceResource}
   **                            <code>IT Resource</code> instance where this
   **                            connector client is associated with.
   **                            <br>
   **                            Allowed object is {@link ServiceResource}.
   **
   ** @throws TaskException      if the authentication/authorization process
   **                            fails.
   */
  private Context(final Loggable loggable, final ServiceResource endpoint)
    throws TaskException {

    // ensure inheritance
    super(loggable, endpoint);

    // initialize instance attributes
    this.target = this.client.target(endpoint.contextURL());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a default context.
   **
   ** @param  loggable           the {@link Loggable} which has
   **                            instantiated this {@link ServiceClient}.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  endpoint           the {@link ServiceResource}
   **                            <code>IT Resource</code> instance where this
   **                            connector client is associated with.
   **                            <br>
   **                            Allowed object is {@link ServiceResource}.
   **
   ** @return                    a default context.
   **                            <br>
   **                            Possible object is <code>Context</code>.
   **
   ** @throws TaskException      if the authentication/authorization process
   **                            fails.
   */
  public static Context build(final Loggable loggable, final ServiceResource endpoint)
    throws TaskException {

    return new Context(loggable, endpoint);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupIdentifier
  /**
   ** Returns a known REST anonymous identifier from the Service Provider.
   **
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws TaskException      if the REST service provider responded with an
   **                            error.
   */
  @SuppressWarnings("unchecked")
  public String lookupIdentifier(final String id)
    throws TaskException {

    final String method = "lookupIdentifier";
    entering(method);
    try {
      return lookupRequest(ENDPOINT_PID, id).invoke();
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createIdentifier
  /**
   ** Build and executes the request to create the provided new anonymous
   ** identifier at the Service Provider.
   **
   ** @param  prefix             the prefix of the
   **                            <code>Anonymous Identifier</code> to generate.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  usedby             the user resource to create the anonymous
   **                            identifier for.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the successfully created anonymous
   **                            identifier.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws TaskException      if the REST service provider responded with an
   **                            error.
   */
  public String createIdentifier(final String prefix, final String usedby)
    throws TaskException {

    final String method = "createIdentifier";
    entering(method);
    try {
      return createRequest(ENDPOINT_PID).invoke(prefix, usedby);
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteIdentifier
  /**
   ** Deletes a known REST user resource from the Service Provider.
   **
   ** @param  id                 the resource identifier (for example the value
   **                            of the "<code>id</code>" attribute).
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws TaskException      if the REST service provider responded with an
   **                            error.
   */
  public void deleteIdentifier(final String id)
    throws TaskException {

    final String method = "deleteIdentifier";
    entering(method);
    try {
      deleteRequest(ENDPOINT_PID, id).invoke();
    }
    finally {
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupRequest
  /**
   ** Build a request to lookup a known REST resource from the Service Provider.
   **
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>projects</code>"
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
  @SuppressWarnings("unchecked")
  public Lookup lookupRequest(final String context, final String id) {
    return Lookup.build(requestTarget(context, id));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createRequest
  /**
   ** Build a request to create the provided new JIRA resource at the Service
   ** Provider.
   **
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>groups</code>"
   **                              <li>"<code>user</code>"
   **                            </ul>
   **                            as defined by the associated resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the request builder that may be used to specify
   **                            additional request parameters and to invoke the
   **                            request.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings("unchecked")
  protected Create createRequest(final String context) {
    return Create.build(this.target.path(context));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteRequest
  /**
   ** Build a request to delete a known REST resource from the Service Provider.
   **
   ** @param  context            the resource context URI such as
   **                            <ul>
   **                              <li>"<code>users</code>"
   **                              <li>"<code>groups</code>"
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
   **                            Possible object is {@link Delete} for type
   **                            <code>T</code>.
   */
  private Delete deleteRequest(final String context, final String id) {
    return Delete.build(resolve(requestTarget(context).getUri()).path(id));
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
   **                              <li>"<code>user</code>"
   **                              <li>"<code>role</code>"
   **                              <li>"<code>project</code>"
   **                            </ul>
   **                            as defined by the associated resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  identifier         the identifier of the resource resource
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the created JAX-RS web target.
   **                            <br>
   **                            Possible object {@link WebTarget}.
   */
  private WebTarget requestTarget(final String context, final String identifier) {
    return requestTarget(context).path(identifier);
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
   **                              <li>"<code>user</code>"
   **                              <li>"<code>role</code>"
   **                              <li>"<code>project</code>"
   **                            </ul>
   **                            as defined by the associated resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the created JAX-RS web target.
   **                            <br>
   **                            Possible object {@link WebTarget}.
   */
  private WebTarget requestTarget(final String context) {
    return this.target.path(context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolve
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
  private WebTarget resolve(final URI uri) {
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