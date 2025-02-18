/*
    Oracle Deutschland BV & Co. KG

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

    Copyright © 2024. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Grafana Connector

    File        :   ListResult.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@icloud.com

    Purpose     :   This file implements the class
                    ListResult.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2024-03-22  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.grafana.response;

import java.util.List;
import java.util.Iterator;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.iam.identity.icf.foundation.utility.CollectionUtility;

import oracle.iam.identity.icf.connector.grafana.schema.Team;

////////////////////////////////////////////////////////////////////////////////
// final class ListResult
// ~~~~~ ~~~~~ ~~~~~~~~~~
/**
 ** An interface for handling the REST search result response.
 **
 ** @author  dieter.steding@icloud.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class ListResult implements Iterable<Team> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The property name of the number of results returned by the list or query
   ** operation.
   */
  public static final String TOTAL    = "totalCount";
  /**
   ** The property name of the 1-based index of the page result in the current
   ** set of list results.
   */
  public static final String PAGE     = "page";
  /**
   ** The property name of the 1-based index of the first result in the current
   ** set of list results.
   */
  public static final String COUNT    = "perPage";
  /**
   ** The property name of the number of resources returned in a list result per
   ** page.
   */
  public static final String RESOURCE = "teams";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The total number of results returned by the list or query operation.
   */
  private final long       total;
  /**
   ** The 1-based index of the first result in the current set of list results.
   */
  private final int        page;
  /**
   ** The number of resources returned in a list result per page.
   */
  private final int        count;
  /**
   ** A multi-valued list of complex objects containing the requested resources.
   */
  private final List<Team> resource;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ListResult</code> REST representation that allows use
   ** as a JavaBean.
   **
   ** @param  total              the total number of results returned by the
   **                            list or query operation.
   **                            <br>
   **                            Allowed object is {@link Long}.
   ** @param  page               the 1-based index of the first result in the
   **                            current set of list results or
   **                            <code>null</code> if pagination is not used and
   **                            the full results are returned.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   ** @param  count              the number of elements returned in a list
   **                            result page.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   ** @param  resource           the list of resources returned by the list or
   **                            query operation.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Team}.
   */
  @JsonCreator
  public ListResult(final @JsonProperty(TOTAL) Long total, final @JsonProperty(PAGE) Integer page, final @JsonProperty(COUNT) Integer count, final @JsonProperty(RESOURCE) List<Team> resource) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.total    = total.longValue();
    this.page     = page.intValue();
    this.count    = count.intValue();
    this.resource = resource;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   total
  /**
   ** Returns the total number of results returned by the list or query
   ** operation.
   **
   ** @return                    the total number of results returned by the
   **                            list or query operation.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public final long total() {
    return this.total;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   page
  /**
   ** Returns the 1-based index of hte first result in the current set of list
   ** results.
   **
   ** @return                    the 1-based index of the first result in the
   **                            current set of list results or
   **                            <code>null</code> if pagination is not used and
   **                            the full results are returned.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public final int page() {
    return this.page;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   count
  /**
   ** Returns the number of elements returned in a list result page.
   **
   ** @return                    the number of elements returned in a list
   **                            result page.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public final int count() {
    return this.count;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resource
  /**
   ** Returns the list of resources returned by the list or query operation.
   **
   ** @return                    the list of resources returned by the list or
   **                            query operation.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Team}.
   */
  public final List<Team> resource() {
    return CollectionUtility.unmodifiableList(this.resource);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   iterator (Iterable)
  /**
   ** Returns an {@link Iterator} over elements the resource elements.
   **
   ** @return                    an {@link Iterator} over elements the resource
   **                            elements.
   **                            <br>
   **                            Possible object is {@link Iterator} over type
   **                            {@link Team}.
   */
  @Override
  public final Iterator<Team> iterator() {
    return this.resource.iterator();
  }
}