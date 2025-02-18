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

    File        :   ListResultHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ListResultHandler.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.rest.domain;

import java.util.List;
import java.util.LinkedList;

import oracle.iam.identity.icf.rest.request.ResultHandler;

////////////////////////////////////////////////////////////////////////////////
// class ListResultHandler
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** An interface for handling the REST search result response.
 ** <p>
 ** Methods will be called in the order they are received.
 **
 ** @param  <T>                  the type of the returned resources.
 **                              <br>
 **                              Allowed object is <code>&lt;T&gt;</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ListResultHandler<T> implements ResultHandler<T> {

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
   ** Constructs an empty <code>ListResultHandler</code> REST response handler
   ** that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ListResultHandler() {
	  // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   start (ResultHandler)
  /**
   ** Handle the start index in the search response.
   **
   ** @param  value              the 1-based index of the first query result.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  @Override
  public final void start(final int value) {
    this.start = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   start (ResultHandler)
  /**
   ** Return the start index in the search response.
   **
   ** @return value              the 1-based index of the first query result.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  @Override
  public final int start() {
    return this.start;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   items (ResultHandler)
  /**
   ** Handle the desired maximum number of query results per page.
   **
   ** @param  value              the desired maximum number of query results per
   **                            page.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  @Override
  public final void items(final int value) {
    this.items = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   items (ResultHandler)
  /**
   ** Return the desired maximum number of query results per page.
   **
   ** @return value              the desired maximum number of query results per
   **                            page.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  @Override
  public final int items() {
    return this.items;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   total (ResultHandler)
  /**
   ** Handle the total number of results returned by the list or query
   ** operation.
   **
   ** @param  value              the total number of results returned by the
   **                            list or query operation.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  @Override
  public void total(final int value) {
    this.total = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   total (ResultHandler)
  /**
   ** Return the total number of results returned by the list or query
   ** operation.
   **
   ** @return value              the total number of results returned by the
   **                            list or query operation.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  @Override
  public int total() {
    return this.total;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resource (ResultHandler)
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
   ** Factory method to create a {@link ListResult} for the request.
   ** <p>
   ** This method includes a hack for returning the amount of items in a
   ** response to ensure that query to retrieve resources from the Service
   ** Provider will not be excetued infinit.
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
   ** @return                    the {@link ListResult} for the request.
   **                            <br>
   **                            Possible object is {@link ListResult} with
   **                            embed type <code>T</code>.
   */
  public ListResult<T> build() {
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
    final int size = this.resource.size();
    return new ListResult<T>(this.total == null ? size : this.total, this.resource, this.start == null ? 1 : this.start, size == 0 ? 0 : this.items == null ? size : this.items);
  }
}