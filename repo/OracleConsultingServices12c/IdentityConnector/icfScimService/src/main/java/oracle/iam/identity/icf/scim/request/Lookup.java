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

    File        :   Lookup.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Lookup.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.scim.request;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.HttpHeaders;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.Invocation;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.rest.ServiceException;

import oracle.iam.identity.icf.scim.schema.Resource;

////////////////////////////////////////////////////////////////////////////////
// class Lookup
// ~~~~~ ~~~~~~
/**
 ** A factory for SCIM lookup requests.
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
public class Lookup<T extends Resource> extends Return<Lookup> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The version to match */
  private String version;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new SCIM lookup request.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new Lookup()" and enforces use of the public method below.
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
  public Lookup(final WebTarget target, final String content, final String... accept) {
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
   ** @return                  the <code>Lookup</code> to allow method chaining.
   **                          <br>
   **                          Possible object is <code>Lookup</code>.
   */
  public Lookup match(final String version) {
    this.version = version;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a new SCIM seach request that will GET the
   ** given resource from the given web target.
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
   **                            Possible object is <code>Search</code>.
   */
  public static Lookup build(final WebTarget target, final String content, final String... accept) {
    return new Lookup(target, content, accept);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   ** Invoke the SCIM lookup request.
   **
   ** @param  <R>                the type of resource to return.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   ** @param  clazz              the Java {@link Class} type used to determine
   **                            the type to return.
   **                            <br>
   **                            Allowed object is {@link Class} of type
   **                            <code>T</code>.
   **
   ** @return                    the successfully lookedup SCIM resource.
   **                            <br>
   **                            Possible object is <code>R</code>.
   **
   ** @throws SystemException    if the SCIM service provider responded with an
   **                            error.
   */
  public <R> R invoke(final Class<R> clazz)
    throws SystemException {

    Response response = buildRequest().get();
    try {
      if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
        return response.readEntity(clazz);
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