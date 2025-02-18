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

    File        :   ListResponseWriter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ListResponseWriter.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.platform.scim.response;

import java.util.Map;
import java.util.Iterator;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import java.io.IOException;
import java.io.OutputStream;

import com.fasterxml.jackson.core.JsonGenerator;

import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.databind.node.ObjectNode;

import oracle.iam.platform.scim.schema.Support;
import oracle.iam.platform.scim.schema.Resource;

////////////////////////////////////////////////////////////////////////////////
// class ListResponseWriter
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** An interface for writing list/query results using the SCIM 2
 ** {@link ListResponse} container to an OutputStream.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
class ListResponseWriter<T extends Resource> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final ObjectNode    deferred;
  private final JsonGenerator generator;

  private final AtomicBoolean started    = new AtomicBoolean();
  private final AtomicBoolean total      = new AtomicBoolean();
  private final AtomicInteger result     = new AtomicInteger();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ListResponseWriter</code> that will write to the
   ** provided output stream.
  **
   ** @param  stream             the output stream to write to.
   **                            <br>
   **                            Allowed object is {@link OutputStream}.
   */
  ListResponseWriter(final OutputStream stream)
    throws IOException {

    // ensure inheritance
    super();

    // initialize instance attributes
    this.deferred  = Support.nodeFactory().objectNode();
    this.generator = Support.objectReader().getFactory().createGenerator(stream);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   startResponse
  /**
   ** Start the response.
   **
   ** @return                    this instance of
   **                            <code>ListResponseWriter</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>ListResponseWriter</code>.
   **
   ** @throws IOException        if an exception occurs while writing to the
   **                            output stream.
   */
  ListResponseWriter startResponse()
    throws IOException {

    this.generator.writeStartObject();
    this.generator.writeArrayFieldStart(Resource.SCHEMA);
    this.generator.writeString(ListResponse.ID);
    this.generator.writeEndArray();

    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endResponse
  /**
   ** End the response.
   **
   ** @return                    this instance of
   **                            <code>ListResponseWriter</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>ListResponseWriter</code>.
   **
   ** @throws IOException        if an exception occurs while writing to the
   **                            output stream.
   */
  ListResponseWriter endResponse()
    throws IOException {

    if (!this.total.get() && !this.deferred.has(ListResponse.TOTAL)) {
      // the total results was never set. set it to the calculated one.
      total(this.result.get());
    }
    if (this.started.get()) {
      // close the resources array if currently writing it.
      this.generator.writeEndArray();
    }

    final Iterator<Map.Entry<String, JsonNode>> i = this.deferred.fields();
    while (i.hasNext()) {
      Map.Entry<String, JsonNode> field = i.next();
      this.generator.writeObjectField(field.getKey(), field.getValue());
    }
    this.generator.writeEndObject();
    this.generator.flush();
    this.generator.close();

    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   total
  /**
   ** Write the totalResults to the output stream immediately if no resources
   ** have been streamed, otherwise it will be written after the resources
   ** array.
   **
   ** @param  value              the total results to write.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   **
   ** @return                    this instance of
   **                            <code>ListResponseWriter</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>ListResponseWriter</code>.
   **
   ** @throws IOException        if an exception occurs while writing to the
   **                            output stream.
   */
  public ListResponseWriter total(final long value)
    throws IOException {

    if (this.started.get()) {
      this.deferred.put(ListResponse.TOTAL, value);
    }
    else {
      this.generator.writeNumberField(ListResponse.TOTAL, value);
      this.total.set(true);
    }
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   start
  /**
   ** Write the startIndex to the output stream immediately if no resources have
   ** been streamed, otherwise it will be written after the resources array.
   **
   ** @param  value              the 1-based index of the first search result or
   **                            <code>null</code> if pagination is not
   **                            required.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    this instance of
   **                            <code>ListResponseWriter</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>ListResponseWriter</code>.
   **
   ** @throws IOException        if an exception occurs while writing to the
   **                            output stream.
   */
  public ListResponseWriter start(final int value)
    throws IOException {

    if (this.started.get()) {
      this.deferred.put(ListResponse.START, value);
    }
    else {
      this.generator.writeNumberField(ListResponse.START, value);
    }
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   items
  /**
   ** Write the itemsPerPage to the output stream immediately if no resources
   ** have been streamed, otherwise it will be written after the resources
   ** array.
   **
   ** @param  value              the items per page to write.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    this instance of
   **                            <code>ListResponseWriter</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>ListResponseWriter</code>.
   **
   ** @throws IOException        if an exception occurs while writing to the
   **                            output stream.
   */
  public ListResponseWriter items(final int value)
    throws IOException {

    if (this.started.get()) {
      this.deferred.put(ListResponse.ITEMS, value);
    }
    else {
      this.generator.writeNumberField(ListResponse.ITEMS, value);
    }
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resource
  /**
   ** Write the result resource to the output stream immediately.
   **
   ** @param  resource           the resource to write.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    this instance of
   **                            <code>ListResponseWriter</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>ListResponseWriter</code>.
   **
   ** @throws IOException        if an exception occurs while writing to the
   **                            output stream.
   */
  public ListResponseWriter resource(final T resource)
    throws IOException {

    if (this.started.compareAndSet(false, true))
      this.generator.writeArrayFieldStart(ListResponse.RESOURCE);

    this.generator.writeObject(resource);
    this.result.incrementAndGet();

    return this;
  }
}