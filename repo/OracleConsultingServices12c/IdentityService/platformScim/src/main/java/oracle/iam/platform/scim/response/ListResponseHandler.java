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

    File        :   ListResponseHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ListResponseHandler.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.platform.scim.response;

import java.util.List;
import java.util.LinkedList;

import com.fasterxml.jackson.databind.node.ObjectNode;

////////////////////////////////////////////////////////////////////////////////
// class ListResponseHandler
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** An interface for handling the search result response.
 ** <p>
 ** Methods will be called in the order they are received.
 **
 ** @param  <T>                  the type of the returned resources.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations of the request payload
 **                              extending this class (requests can return their
 **                              own specific type instead of type defined by
 **                              this class only).
 **                              <br>
 **                              Allowed object is <code>&lt;T&gt;</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ListResponseHandler<T> implements ResponseHandler<T> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private Integer total;
  private Integer start;
  private Integer items;
  private List<T> resource = new LinkedList<T>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>ListResponseHandler</code> SCIM response handler
   ** that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private ListResponseHandler() {
	  // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   start (ResponseHandler)
  /**
   ** Handle the startIndex in the search response.
   **
   ** @param  value              the 1-based index of the first query result.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the <code>ListResponseHandler</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>ResponseHandler</code>.
   */
  @Override
  public final ResponseHandler start(final int value) {
    this.start = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   items (ResponseHandler)
  /**
   ** Handle the desired maximum number of query results per page.
   **
   ** @param  value              the desired maximum number of query results per
   **                            page.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the <code>ListResponseHandler</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>ResponseHandler</code>.
   */
  @Override
  public final ResponseHandler items(final int value) {
    this.items = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   total (ResponseHandler)
  /**
   ** Handle the total number of results returned by the list or query
   ** operation.
   **
   ** @param  value              the total number of results returned by the
   **                            list or query operation.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the <code>ListResponseHandler</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>ResponseHandler</code>.
   */
  @Override
  public ResponseHandler total(final int value) {
    this.total = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   extension (ResponseHandler)
  /**
   ** Handle an schema extension in the search response.
   **
   ** @param  namespace          the namespace URN of the extension schema.
   **
   ** @param  extension          the {@link ObjectNode} representing the
   **                            extension schema.
   **
   ** @return                    the <code>ListResponseHandler</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>ResponseHandler</code>.
   */
  @Override
  public final ResponseHandler extension(final String namespace, final ObjectNode extension) {
    // intentionally left blank
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resource (ResponseHandler)
  /**
   ** Handle a search result resource.
   **
   ** @param  resource           a search result resource.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    <code>true</code> to continue processing the
   **                            search result response or <code>false</code> to
   **                            immediate stop further processing of the
   **                            response.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  public final boolean resource(final T resource) {
    this.resource.add(resource);
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a {@link ListResponse} for the request.
   ** <p>
   ** This method includes a hack for returning the amount of items in a
   ** response to ensure that query to retrieve resources from the
   ** <code>Service Provider</code> will not be excetued infinit.
   ** (<code>this.resource.size() == 0 ? 0 : this.items</code>).
   ** This ensures that the parsed item size has a finite value.
   ** <br>
   ** Especially Pivotal Cloud Foundry (PCF) replies on a search request with
   ** the values mirrored from the request itself if the query parameter exceeds
   ** the existing resource metrics, e.g.
   ** <code>startIndex=99999</code> and <code>count=2</code> but there are only
   ** <code>100</code> resources PCF responds with
   ** <code>{"resources": [],"startIndex": 99999,"itemsPerPage": 2,"totalResults": 1,"schemas": ["urn:scim:schemas:core:1.0"]}</code>
   ** <br>
   ** This behavior requires the <code>Service Consumer</code> to consult the
   ** resource collection instead of the primitives only to detect valid result
   ** set.
   **
   ** @return                    the {@link ListResponse} for the request.
   **                            <br>
   **                            Possible object is {@link ListResponse}.
   */
  public ListResponse<T> build() {
    // The hack this.resource.size() == 0 ? 0 : this.items ensures that the
    // parsed item size has a finite value
    // Especially Pivotal Cloud Foundry (PCF) replies on a search request with
    // values mirrored from the request itself if the query parameter exceeds
    // the existing resource metrics, e.g.
    // startIndex=99999 and count=2 but there are only 100 resources PCF
    // responds with
    // {"resources": [],"startIndex": 99999,"itemsPerPage": 2,"totalResults": 1,"schemas": ["urn:scim:schemas:core:1.0"]}
    // This behavior requires the <code>Service Consumer</code> to consult the
    // resource collection instead of the primitives only to detect valid result
    // set
    return new ListResponse<T>(this.total == null ? this.resource.size() : this.total, this.start, this.resource.size() == 0 ? 0 : this.items, this.resource);
  }
}