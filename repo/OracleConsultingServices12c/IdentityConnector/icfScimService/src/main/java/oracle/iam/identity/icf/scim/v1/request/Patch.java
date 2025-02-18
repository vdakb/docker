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

    File        :   Patch.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Patch.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.scim.v1.request;

import java.util.List;
import java.util.Iterator;

import java.io.InputStream;
import java.io.IOException;

import javax.ws.rs.ProcessingException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.HttpHeaders;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.ResponseProcessingException;

import com.fasterxml.jackson.core.JsonParser;

import com.fasterxml.jackson.databind.ObjectReader;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.glassfish.jersey.client.HttpUrlConnectorProvider;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.rest.ServiceException;

import oracle.iam.identity.icf.scim.request.Return;

import oracle.iam.identity.icf.scim.schema.Support;
import oracle.iam.identity.icf.scim.schema.Resource;

import oracle.iam.identity.icf.scim.annotation.Schema;

////////////////////////////////////////////////////////////////////////////////
// class Patch
// ~~~~~ ~~~~~
/**
 ** SCIM provides a resource type for <code>Patch</code> resource that belongs
 ** to SCIM 1 interface.
 ** <br>
 ** The core schema for <code>Patch</code> is identified using the URI:
 * <code>urn:scim:schemas:core:1.0</code>
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
public class Patch<T extends Resource> extends Return<Patch> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The attribute name, whose value is an array of one or more PATCH
   ** operations.
   */
  public static final String OPERATIONS = "Operations";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The version to match. */
  private String             version;

  /** The patch request. */
  private final Request      request;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Request
  // ~~~~~ ~~~~~
  /**
   ** Class representing a SCIM 1 patch request.
   */
  @Schema(id="urn:scim:schemas:core:1.0", name="Patch Operation", description="SCIM 1.1 Patch Operation Request")
  public static class Request extends    oracle.iam.identity.icf.scim.schema.Entity
                              implements Iterable<Operation> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** The list of patch operations to include in the request. */
    private final List<Operation> operation;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a new SCIM 2 patch request that will apply the given
     ** <code>operation</code>.
     **
     ** @param  operation        the patch operations to apply by the update.
     **                          <br>
     **                          Allowed object is {@link List} where each
     **                          element is of type {@link Operation}.
     */
    public Request(final List<Operation> operation) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.operation = operation;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: iterator (Iterable)
    /**
     ** Returns an {@link Iterator} over the elements.
     **
     ** @return                  an {@link Iterator} over the elements.
     **                          <br>
     **                          Possible object is {@link Iterator}.
     */
    @Override
    public Iterator<Operation> iterator() {
      return this.operation.iterator();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: apply
    /**
     ** Apply this patch operation to an {@link ObjectNode}.
     **
     ** @param  node               the {@link ObjectNode} to apply this patch
     **                            operation to.
     **                            <br>
     **                            Allowed object is {@link ObjectNode}.
     **
     ** @throws ServiceException   if the patch operation is invalid.
     */
    public void apply(final ObjectNode node)
      throws ServiceException {

      for (Operation cursor : this) {
        cursor.apply(node);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new SCIM 1 patch request that will apply the given
   ** <code>operation</code> to the given web <code>target</code> leveraging
   ** <code>PATCH</code> method.
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   ** @param  operation          the patch operations to apply by the update.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Operation}.
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
  public Patch(final WebTarget target, final List<Operation> operation, final String content, final String... accept) {
    // ensure inheritance
    // apply the hack to pass through custom HTTP method like PATCH on the fly
    super(target.property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true), content, accept);

    // initialize instance attributes
    this.request = new Request(operation);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   match
  /**
   ** Modify the resource only if the resource has not been modified since the
   ** provided version.
   ** <br>
   ** If the resource has not been modified, NotModifiedException will be
   ** thrown when calling invoke.
   **
   ** @param  version            the version of the resource to compare.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Patch</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Patch</code> for type
   **                            <code>T</code>.
   */
  public Patch<T> match(final String version) {
    this.version = version;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (overridden)
  /**
   ** Returns a hash code value for the object.
   ** <br>
   ** This method is supported for the benefit of hash tables such as those
   ** provided by {@link java.util.HashMap}.
   ** <p>
   ** The general contract of <code>hashCode</code> is:
   ** <ul>
   **   <li>Whenever it is invoked on the same object more than once during an
   **       execution of a Java application, the <code>hashCode</code> method
   **       must consistently return the same integer, provided no information
   **       used in <code>equals</code> comparisons on the object is modified.
   **       This integer need not remain consistent from one execution of an
   **       application to another execution of the same application.
   **   <li>If two objects are equal according to the
   **       <code>equals(Object)</code> method, then calling the
   **       <code>hashCode</code> method on each of the two objects must produce
   **       the same integer result.
   **   <li>It is <em>not</em> required that if two objects are unequal
   **       according to the {@link java.lang.Object#equals(java.lang.Object)}
   **       method, then calling the <code>hashCode</code> method on each of the
   **       two objects must produce distinct integer results.  However, the
   **       programmer should be aware that producing distinct integer results
   **       for unequal objects may improve the performance of hash tables.
   ** </ul>
   **
   ** @return                    a hash code value for this object.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  @Override
  public int hashCode() {
    return 31 * super.hashCode() + this.request.hashCode();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Patch</code>s are considered equal if and only if they represent
   ** the same properties. As a consequence, two given <code>Patch</code>s may
   ** be different even though they contain the same set of names with the same
   ** values, but in a different order.
   **
   ** @param  other              the reference object with which to compare.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    <code>true</code> if this object is the same as
   **                            the object argument; <code>false</code>
   **                            otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  public boolean equals(final Object other) {
    if (this == other) {
      return true;
    }
    if (other == null || getClass() != other.getClass()) {
      return false;
    }
    if (!super.equals(other)) {
      return false;
    }

    final Patch that = (Patch)other;
    return this.request.equals(that.request);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   ** Invoke the SCIM 1 modify request.
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
   ** @return                    the successfully modified SCIM resource.
   **                            <br>
   **                            Possible object is <code>R</code>.
   **
   ** @throws SystemException    if the SCIM service provider responded with an
   **                            error.
   */
  public <R> R invoke(final Class<R> clazz)
    throws SystemException {

    Response response = null;
    try {
      final ObjectNode node   = Support.nodeFactory().objectNode();
      final ArrayNode  schema = Support.objectMapper().valueToTree(this.request.namespace());
      this.request.apply(node);
      node.putArray("schemas").addAll(schema);
      response = buildRequest().method("PATCH", Entity.entity(node, typeContent()));
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
        final InputStream  stream = response.readEntity(InputStream.class);
        final ObjectReader reader = Support.objectReader();
        final JsonParser   parser = reader.getFactory().createParser(stream);
        final R            entity = reader.readValue(parser, clazz);
        return entity;
      }
      else {
        throw ServiceException.from(response);
      }
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

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildRequest
  /**
   ** Factory method to create a {@link Invocation.Builder} for the request.
   **
   ** @return                     the {@link Invocation.Builder} for the request.
   */
  @Override
  protected Invocation.Builder buildRequest() {
    Invocation.Builder request = super.buildRequest();
    if (this.version != null) {
      request.header(HttpHeaders.IF_MATCH, this.version);
    }
    return request;
  }
}