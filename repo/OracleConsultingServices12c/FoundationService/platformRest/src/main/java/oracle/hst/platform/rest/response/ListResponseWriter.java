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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Service Extension
    Subsystem   :   Generic REST Library

    File        :   ListResponseWriter.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ListResponseWriter.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-06  DSteding    First release version
*/

package oracle.hst.platform.rest.response;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import java.io.IOException;
import java.io.OutputStream;

import com.fasterxml.jackson.core.JsonGenerator;

import com.fasterxml.jackson.databind.node.ObjectNode;

import oracle.hst.platform.rest.schema.Support;
import oracle.hst.platform.rest.schema.Resource;

////////////////////////////////////////////////////////////////////////////////
// class ListResponseWriter
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** An interface for writing list/query results using the REST ListResponse
 ** container to an OutputStream.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
class ListResponseWriter<T> {

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
  // Method:   start
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
  ListResponseWriter start()
    throws IOException {

    this.generator.writeStartObject();
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   end
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
  ListResponseWriter end()
    throws IOException {

    if (!this.total.get() && !this.deferred.has(ListResponse.TOTAL)) {
      // the total results was never set. set it to the calculated one.
      total(this.result.get());
    }
    if (this.started.get()) {
      // close the resources array if currently writing it.
      this.generator.writeEndArray();
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
   ** Write the 1-based index of the first search result to the output stream
   ** immediately if no resources have been streamed, otherwise it will be
   ** written after the resources array.
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
   ** Write the desired maximum number of search results to the output stream
   ** immediately if no resources have been streamed, otherwise it will be
   ** written after the resources array.
   **
   ** @param  items              the desired maximum number of search results
   **                            per page or <code>null</code> to not enforce a
   **                            limit.
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
      this.generator.writeArrayFieldStart(ListResponse.RESULT);

    this.generator.writeObject(resource);
    this.result.incrementAndGet();

    return this;    
  }
}
