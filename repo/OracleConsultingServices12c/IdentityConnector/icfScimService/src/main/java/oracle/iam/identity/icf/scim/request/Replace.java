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

    File        :   Replace.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Replace.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.scim.request;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.ObjectReader;

import javax.ws.rs.ProcessingException;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.ResponseProcessingException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.HttpHeaders;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.rest.ServiceException;

import oracle.iam.identity.icf.rest.utility.MapperFactory;

import oracle.iam.identity.icf.scim.schema.Resource;

////////////////////////////////////////////////////////////////////////////////
// class Replace
// ~~~~~ ~~~~~~~
/**
 ** A factory for SCIM replace requests.
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
public class Replace<T extends Resource> extends Return<Replace<T>>{

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final T resource;
  private String  version;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a a new SCIM create request that will POST the given resource
   ** to the given web target.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new Replace()" and enforces use of the public method below.
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   ** @param  resource           the SCIM resource to POST.
   **                            <br>
   **                            Allowed object is <code>T</code>.
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
  private Replace(final WebTarget target, final T resource, final String content, final String... accept) {
    // ensure inheritance
    super(target, content, accept);

    // initialize instance attributes
    this.resource = resource;
    // fail safe behavior if version metadata not provided
    try {
      this.version = resourceVersion(resource);
    }
    catch (IllegalArgumentException e) {
      this.version = "*";
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   match
  /**
   ** Delete the resource only if the resource has not been modified since the
   ** provided version.
   **
   ** @param  version            the version of the resource to compare.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Replace</code> to allow method#
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Replace</code>.
   */
  public Replace match(final String version) {
    this.version = version;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a new SCIM modify request that will PUT the
   ** given resource at the given web target.
   **
   ** @param  <T>                the type of objects to modify.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   ** @param  resource           the SCIM resource to POST.
   **                            <br>
   **                            Allowed object is <code>T</code>.
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
   **                            Possible object is <code>Replace</code> for type
   **                            <code>T</code>.
   */
  public static <T extends Resource> Replace build(final WebTarget target, final T resource, final String content, final String... accept) {
    return new Replace<T>(target, resource, content, accept);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildRequest
  /**
   ** Factory method to create a {@link Invocation.Builder} for the request.
   **
   ** @return                    the {@link Invocation.Builder} for the
   **                            request.
   **                            <br>
   **                            Possible object is {@link Invocation.Builder}.
   */
  @Override
  protected final Invocation.Builder buildRequest() {
    Invocation.Builder request = super.buildRequest();
    if (this.version != null) {
      request.header(HttpHeaders.IF_MATCH, this.version);
    }
    return request;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   ** Invoke the SCIM replace request using POST.
   **
   ** @return                    the successfully replaced SCIM resource.
   **                            <br>
   **                            Possible object is <code>T</code>.
   **
   ** @throws SystemException    if an error occurred.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public T invoke()
    throws SystemException {

    return (T)invoke(this.resource.getClass());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   ** Invoke the SCIM replace request using POST.
   **
   ** @param  <R>                the type of objects to return.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   ** @param  clazz              the Java class type used to determine the type
   **                            to return.
   **                            <br>
   **                            Allowed object is {@link Class} of type
   **                            <code>T</code>.
   **
   ** @return                    the successfully replaced SCIM resource.
   **                            <br>
   **                            Possible object is <code>R</code>.
   **
   ** @throws SystemException    if an error occurred.
   */
  public <R> R invoke(final Class<R> clazz)
    throws SystemException {

    Response response = null;
    try {
      response = buildRequest().put(Entity.entity(MapperFactory.instance.writeValueAsString(this.resource), typeContent()));
      if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
        // this is a hack to solve the unmarshalling of an entity if the
        // connector runs in the embedded Conncetor Server deployed in Identity
        // Manager
        // The native call of readEntity fails with an empty entity without any
        // exception; Aaaargh
        // the reason is that the org.eclipse.persistent JsonStructureReader
        // kicks in that isnt't able to resolve to JSON-POJO relation properly.
        // The solution is to explicitly create a Jackson parser for this
        // purpose to bypass the standard implementation
        final ObjectReader reader = MapperFactory.instance.reader();
        final JsonParser   parser = reader.getFactory().createParser(response.readEntity(InputStream.class));
        final R            entity = reader.readValue(parser, clazz);
        return entity;
      }
      else {
        throw ServiceException.from(response);
      }
    }
    catch (JsonProcessingException e) {
      throw ServiceException.abort(e);
    }
    catch (IOException e) {
      throw new ResponseProcessingException(response, e);
    }
    catch (ProcessingException e) {
      throw ServiceException.from(e, target().getUri());
    }
    finally {
      if (response != null)
        response.close();
    }
  }
}