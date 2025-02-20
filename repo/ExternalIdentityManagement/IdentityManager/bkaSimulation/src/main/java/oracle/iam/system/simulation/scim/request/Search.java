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

    System      :   Identity Manager Service Simulation
    Subsystem   :   Generic SCIM Interface

    File        :   Search.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Search.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.scim.request;

import java.util.Set;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.core.Response;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.ResponseProcessingException;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonParser;

import javax.ws.rs.ProcessingException;

import oracle.iam.system.simulation.ServiceException;

import oracle.iam.system.simulation.scim.schema.Support;

import oracle.iam.system.simulation.scim.domain.ListResponse;
import oracle.iam.system.simulation.scim.domain.SearchRequest;
import oracle.iam.system.simulation.scim.domain.ResponseHandler;
import oracle.iam.system.simulation.scim.domain.ListResponseHandler;

////////////////////////////////////////////////////////////////////////////////
// class Search
// ~~~~~ ~~~~~~
/**
 ** A factory for SCIM search requests.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Search extends Return<Search> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The 1-based index of the first result in the current set of list results
   */
  private Integer             start;

  /** The number of resources returned in a list response page */
  private Integer             count;

  /** The filter string used to request a subset of resources */
  private String              filter;

   /** A string indicating the order in which the sortBy parameter is applied */
  private SearchRequest.Order order;

  /**
   ** A string indicating the attribute whose value shall be used to order the
   ** returned responses
   */
  private String               sort;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a a new SCIM search request.
   **
   ** @param  target             the {@link WebTarget} to send the request.
   */
  public Search(final WebTarget target) {
    // ensure inheritance
    super(target);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   page
  /**
   ** Sets the pagination request of resources.
   **
   ** @param  start              the 1-based index of the first query result.
   ** @param  count              the desired maximum number of query results per
   **                            page.
   **
   ** @return                    the {@link Search} to allow method chaining.
   **                            <br>
   **                            Possible object is {@link Search}.
   */
  public Search page(final int start, final int count) {
    this.start = start;
    this.count = count;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filter
  /**
   ** Sets the filtering request of resources.
   **
   ** @param  filter             the filter string used to request a subset of
   **                            resources.
   **
   ** @return                    the {@link Search} to allow method chaining.
   **                            <br>
   **                            Possible object is {@link Search}.
   */
  public Search filter(final String filter) {
    this.filter = filter;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sort
  /**
   ** Sets the sorting request of resources.
   **
   ** @param  sort               the string indicating the attribute whose value
   **                            shall be used to order the returned responses.
   ** @param  order              the order in which the sortBy parameter is
   **                            applied.
   **
   ** @return                    the {@link Search} to allow method chaining.
   **                            <br>
   **                            Possible object is {@link SearchRequest.Order}.
   */
  public Search sort(final String sort, final SearchRequest.Order order) {
    this.sort  = sort;
    this.order = order;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build (overidden)
  /**
   ** Factory method to create a {@link WebTarget} for the request.
   **
   ** @return                    the {@link WebTarget} for the request.
   */
  @Override
  WebTarget build() {
    WebTarget target = super.build();
    if (this.start != null && this.count != null) {
      // from the docs:
      // queryParam returns: a new target instance.
      target = target.queryParam(SearchRequest.START, this.start).queryParam(SearchRequest.COUNT, this.count);
    }
    if (this.filter != null) {
      // from the docs:
      // queryParam returns: a new target instance.
      target = target.queryParam(SearchRequest.FILTER, this.filter);
    }
    if (this.sort != null && this.order != null) {
      // from the docs:
      // queryParam returns: a new target instance.
      target = target.queryParam(SearchRequest.SORT, this.sort).queryParam(SearchRequest.ORDER, this.order.value());
    }
    return target;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   ** Invoke the SCIM retrieve request using GET.
   **
   ** @param  <T>                the type of objects to return.
   ** @param  clazz              the Java class type used to determine the type
   **                            to return.
   **
   ** @return                    the {@link ListResponse} containing the search
   **                            results.
   **
   ** @throws ServiceException   if an error occurred.
   */
  public <T> ListResponse<T> invoke(final Class<T> clazz)
    throws ServiceException {

    final ListResponseHandler<T> handler = new ListResponseHandler<T>();
    invoke(false, handler, clazz);
    return handler.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   ** Invoke the SCIM retrieve request using GET.
   **
   ** @param  <T>                the type of objects to return.
   ** @param  handler            the search result handler that should be used
   **                            to process the resources.
   ** @param  clazz              the Java class type used to determine the type
   **                            to return.
   **
   ** @throws ServiceException   if an error occurred.
   */
  public <T> void invoke(final ResponseHandler<T> handler, final Class<T> clazz)
    throws ServiceException {

    invoke(false, handler, clazz);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   ** Invoke the SCIM retrieve request.
   **
   ** @param  post               <code>true</code> to send the request using
   **                            POST or <code>false</code> to send the request
   **                            using GET.
   ** @param  <T>                the type of objects to return.
   ** @param  handler            the search result handler that should be used
   **                            to process the resources.
   ** @param  clazz              the Java class type used to determine the type
   **                            to return.
   **
   ** @throws ServiceException    if the SCIM service provider responded with an
   **                             error.
   ** @throws ProcessingException if a JAX-RS runtime exception occurred.
   */
  private <T> void invoke(final boolean post, final ResponseHandler<T> handler, final Class<T> clazz)
    throws ServiceException {

    Response response;
    try {
      if (post) {
        Set<String> emitted = null;
        Set<String> omitted = null;
        if (this.attributes != null && this.attributes.size() > 0) {
          if (!this.omitted) {
            emitted = this.attributes;
          }
          else {
            omitted = this.attributes;
          }
        }
        final SearchRequest searchRequest = new SearchRequest(this.start, this.count, this.filter, this.sort, this.order, emitted, omitted);
        response = buildRequest().post(Entity.entity(searchRequest, contentType()));
      }
      else {
        response = buildRequest().get();
      }
    }
    catch (ProcessingException e) {
      throw ServiceException.build(e, target().getUri());
    }
    catch (Throwable t) {
      Throwable tx = t.getCause();
      throw t;
    }

    try {
      if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
        InputStream inputStream = response.readEntity(InputStream.class);
        try {
          final JsonParser parser = Support.objectReader().getFactory().createParser(inputStream);
          try {
            parser.nextToken();
            boolean stop = false;
            while (!stop && parser.nextToken() != JsonToken.END_OBJECT) {
              final String field = parser.getCurrentName();
              parser.nextToken();
              switch (field) {
                case ListResponse.SCHEMA   : parser.skipChildren();
                                             break;
                case ListResponse.TOTAL    : handler.total(parser.getIntValue());
                                             break;
                case ListResponse.START    : handler.start(parser.getIntValue());
                                             break;
                case ListResponse.ITEMS    : handler.items(parser.getIntValue());
                                             break;
                // Service Providers are not following strictly the RFC
                // regarding naming conventions; somtimes resources is
                // returned instead of Resources how it's required by the RFC.
                case "resources"           : // intentionally fall through
                case ListResponse.RESOURCE : while (parser.nextToken() != JsonToken.END_ARRAY) {
                                               if (!handler.resource(parser.readValueAs(clazz))) {
                                                 stop = true;
                                                 break;
                                               }
                                             }
                                             break;
              }
            }
          }
          finally {
            if (inputStream != null) {
              inputStream.close();
            }
            parser.close();
          }
        }
        catch (IOException e) {
          throw new ResponseProcessingException(response, e);
        }
      }
      else {
        throw toException(response);
      }
    }
    finally {
      response.close();
    }
  }
}