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

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Generic SCIM Interface

    File        :   ListResponseWriter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ListResponseWriter.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.scim.response;

import java.util.Map;
import java.util.Iterator;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import java.io.IOException;
import java.io.OutputStream;

import com.fasterxml.jackson.core.JsonGenerator;

import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.databind.node.ObjectNode;

import oracle.iam.system.simulation.scim.schema.Support;
import oracle.iam.system.simulation.scim.schema.Resource;

////////////////////////////////////////////////////////////////////////////////
// class ListResponseWriter
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** An interface for writing list/query results using the SCIM ListResponse
 ** container to an OutputStream.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ListResponseWriter<T extends Resource> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final AtomicBoolean startedResourcesArray = new AtomicBoolean();
  private final AtomicBoolean sentTotalResults      = new AtomicBoolean();
  private final AtomicInteger resultsSent           = new AtomicInteger();

  private final JsonGenerator generator;

  private ObjectNode          deferred;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new ListResponseWriter that will write to the provided output stream.
   **
   ** @param  stream             the output stream to write to.
   **                            <br>
   **                            Allowed object is {@link OutputStream}.
   **
   ** @throws IOException        if an exception occurs while writing to the
   **                            output stream.
   */
  public ListResponseWriter(final OutputStream stream)
    throws IOException {

    // ensure inheritance
    super();

    // initialize instance attributes
    this.generator = Support.objectReader().getFactory().createGenerator(stream);
    this.deferred  = Support.jsonNodeFactory().objectNode();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   startResponse
  /**
   ** Start the response.
   **
   ** @throws IOException        if an exception occurs while writing to the
   **                            output stream.
   */
  // FIXME: public only for debugging purpose
  public void startResponse()
    throws IOException {

    this.generator.writeStartObject();
    this.generator.writeArrayFieldStart("schemas");
    this.generator.writeString("urn:ietf:params:scim:api:messages:2.0:ListResponse");
    this.generator.writeEndArray();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endResponse
  /**
   ** End the response.
   **
   ** @throws IOException        if an exception occurs while writing to the
   **                            output stream.
   */
  // FIXME: public only for debugging purpose
  public void endResponse()
    throws IOException {

    if (!this.sentTotalResults.get() && !this.deferred.has("totalResults")) {
      // The total results was never set. Set it to the calculated one.
      totalResults(this.resultsSent.get());
    }
    if (this.startedResourcesArray.get()) {
      // Close the resources array if currently writing it.
      this.generator.writeEndArray();
    }

    Iterator<Map.Entry<String, JsonNode>> i = this.deferred.fields();
    while (i.hasNext()) {
      Map.Entry<String, JsonNode> field = i.next();
      this.generator.writeObjectField(field.getKey(), field.getValue());
    }
    this.generator.writeEndObject();
    this.generator.flush();
    this.generator.close();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   startIndex
  /**
   ** Write the startIndex to the output stream immediately if no resources have
   ** been streamed, otherwise it will be written after the resources array.
   **
   ** @param  startIndex         the startIndex to write.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @throws IOException        if an exception occurs while writing to the
   **                            output stream.
   */
  public void startIndex(final int startIndex)
    throws IOException {

    if (this.startedResourcesArray.get()) {
      this.deferred.put("startIndex", startIndex);
    }
    else {
      this.generator.writeNumberField("startIndex", startIndex);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   itemsPerPage
  /**
   ** Write the itemsPerPage to the output stream immediately if no resources
   ** have been streamed, otherwise it will be written after the resources
   ** array.
   **
   ** @param  items              the items per page to write.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @throws IOException        if an exception occurs while writing to the
   **                            output stream.
   */
  public void itemsPerPage(final int items)
    throws IOException {

    if (this.startedResourcesArray.get()) {
      this.deferred.put("itemsPerPage", items);
    }
    else {
      this.generator.writeNumberField("itemsPerPage", items);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   totalResults
  /**
   ** Write the totalResults to the output stream immediately if no resources
   ** have been streamed, otherwise it will be written after the resources
   ** array.
   **
   ** @param  total              the total results to write.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @throws IOException        if an exception occurs while writing to the
   **                            output stream.
   */
  public void totalResults(final int total)
    throws IOException {

    if (this.startedResourcesArray.get()) {
      this.deferred.put("totalResults", total);
    }
    else {
      this.generator.writeNumberField("totalResults", total);
      this.sentTotalResults.set(true);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resource
  /**
   ** Write the result resource to the output stream immediately.
   **
   ** @param  resource           the resource to write.
   **
   ** @throws IOException        if an exception occurs while writing to the
   **                            output stream.
   */
  public void resource(final T resource)
    throws IOException {

    if (this.startedResourcesArray.compareAndSet(false, true)) {
      this.generator.writeArrayFieldStart("Resources");
    }
    try {
      this.generator.writeObject(resource);
    }
    catch (Throwable t) {
      t.printStackTrace();
    }
    this.resultsSent.incrementAndGet();
  }
}