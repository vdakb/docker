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
    Subsystem   :   Pivotal Cloud Foundry Connector

    File        :   Search.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Search.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.pcf.rest.request;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.ProcessingException;

import javax.ws.rs.core.Response;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.ResponseProcessingException;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonParser;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.rest.ServiceException;

import oracle.iam.identity.icf.rest.request.Request;
import oracle.iam.identity.icf.rest.request.ResultHandler;

import oracle.iam.identity.icf.rest.domain.ListResult;
import oracle.iam.identity.icf.rest.domain.ListResultHandler;

import oracle.iam.identity.icf.rest.utility.MapperFactory;

import oracle.iam.identity.icf.connector.pcf.rest.ExceptionParser;

import oracle.iam.identity.icf.connector.pcf.rest.schema.Embed;

////////////////////////////////////////////////////////////////////////////////
// class Search
// ~~~~~ ~~~~~~
/**
 ** A factory for REST search requests.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Search<R extends Embed> extends Request<Search> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The property name of the 1-based index of the first query result.
   */
  public static final String START     = "page";
  /**
   ** The property name of the integer indicating the desired maximum number of
   ** query results per page.
   */
  public static final String COUNT     = "results-per-page";
  /**
   ** The property name of the filter string used to request a subset of
   ** resources.
   */
  public static final String FILTER    = "q";
  /**
   ** The property name of the string indicating the order in which the sortBy
   ** parameter is applied.
   */
  public static final String ORDER     = "order-direction";
  /**
   ** The property name of the string indicating the attribute whose value shall
   ** be used to order the returned responses.
   */
  public static final String SORT     = "order-by";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The 1-based index of the first result in the current set of list results
   */
  @JsonProperty(START)
  private Integer            start;

  /** The number of resources returned in a list response page */
  @JsonProperty(COUNT)
  private Integer            count;

  /** The filter string used to request a subset of resources */
  @JsonProperty(FILTER)
  private String             filter;

  /** A string indicating the order in which the sortBy parameter is applied */
  @JsonProperty(ORDER)
  private Order              order;

  /**
   ** A string indicating the attribute whose value shall be used to order the
   ** returned responses
   */
  @JsonProperty(SORT)
  private  String            sort;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Order
  // ~~~~ ~~~~~
  /**
   ** The order in which the sortBy parameter is applied.
   */
  public enum Order {
      /** the ascending sort order */
      ASCENDING("asc")
      /** the descending sort order */
    , DESCENDING("desc")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private String value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Order</code> with a constraint value.
     **
     ** @param  value            the constraint name (used in SCIM schemas) of
     **                          the object.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    Order(final String value) {
      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Returns the value of the type constraint.
     **
     ** @return                  the value of the type constraint.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    @JsonValue
    public String value() {
      return this.value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Factory method to create a proper <code>Order</code> constraint from the
     ** given string value.
     **
     ** @param  value            the string value the order constraint should be
     **                          returned for.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Order</code> constraint.
     **                          <br>
     **                          Possible object is <code>Order</code>.
     */
    public static Order from(final String value) {
      for (Order cursor : Order.values()) {
        if (cursor.value.equals(value))
          return cursor;
      }
      throw new IllegalArgumentException(value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a a new REST search request.
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   */
  private Search(final WebTarget target) {
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
   ** @return                    the <code>Search</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Search</code>.
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
   ** @return                    the <code>Search</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Search</code>.
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
   ** @return                    the <code>Search</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Search</code>.
   */
  public Search sort(final String sort, final Order order) {
    this.sort  = sort;
    this.order = order;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a generic search resource request.
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   **
   ** @return                    a JAX-RS request.
   **                            <br>
   **                            Possible object is <code>Search</code>.
   */
  public static Search build(final WebTarget target) {
    return new Search(target);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build (overidden)
  /**
   ** Factory method to create a {@link WebTarget} for the request.
   **
   ** @return                    the {@link WebTarget} for the request.
   */
  @Override
  protected final WebTarget build() {
    WebTarget target = super.build();
    if (this.start != null && this.count != null) {
      // from the docs:
      // queryParam returns: a new target instance.
      target = target.queryParam(START, this.start).queryParam(COUNT, this.count);
    }
    if (this.filter != null) {
      // from the docs:
      // queryParam returns: a new target instance.
      target = target.queryParam(FILTER, this.filter);
    }
    if (this.sort != null && this.order != null) {
      // from the docs:
      // queryParam returns: a new target instance.
      target = target.queryParam(SORT, this.sort).queryParam(ORDER, this.order.value());
    }
    return target;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   ** Invoke the REST retrieve request using GET.
   **
   ** @param  <R>                the type of objects to return.
   **                            <br>
   **                            Allowed object is <code>&lt;R&gt;</code>.
   ** @param  clazz              the Java class type used to determine the type
   **                            to return.
   **                            <br>
   **                            Allowed object is {@link Class} of type
   **                            <code>R</code>.
   **
   ** @return                    the {@link ListResult} containing the search
   **                            results.
   **                            <br>
   **                            Possible object is {@link ListResult} where
   **                            the payload is of type <code>T</code>.
   **
   ** @throws SystemException   if an error occurred.
   */
  public <R> ListResult<R> invoke(final Class<R> clazz)
    throws SystemException {

    final ListResultHandler<R> handler = new ListResultHandler<R>();
    invoke(false, handler, clazz);
    return handler.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   ** Invoke the SCIM retrieve request using GET.
   **
   ** @param  <T>                the type of objects to return.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  handler            the search result handler that should be used
   **                            to process the resources.
   **                            <br>
   **                            Allowed object is {@link ResultHandler} of
   **                            type <code>T</code>.
   ** @param  clazz              the Java class type used to determine the type
   **                            to return.
   **                            <br>
   **                            Allowed object is {@link Class} of type
   **                            <code>T</code>.
   **
   ** @throws SystemException   if an error occurred.
   */
  public <T> void invoke(final ResultHandler<T> handler, final Class<T> clazz)
    throws SystemException {

    invoke(false, handler, clazz);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   ** Invoke the REST retrieve request.
   **
   ** @param  post               <code>true</code> to send the request using
   **                            POST or <code>false</code> to send the request
   **                            using GET.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param  <R>                the type of objects to return.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  handler            the search result handler that should be used
   **                            to process the resources.
   **                            <br>
   **                            Allowed object is {@link ResultHandler} of
   **                            type <code>T</code>.
   ** @param  clazz              the Java class type used to determine the type
   **                            to return.
   **                            <br>
   **                            Allowed object is {@link Class} of type
   **                            <code>T</code>.
   **
   ** @throws SystemException    if the SCIM service provider responded with an
   **                            error.
   */
  private <R> void invoke(final boolean post, final ResultHandler<R> handler, final Class<R> clazz)
    throws SystemException {

    Response response;
    try {
      if (post) {
        response = buildRequest().post(Entity.entity(this, contentType()));
      }
      else {
        response = buildRequest().get();
      }
    }
    catch (ProcessingException e) {
      throw ServiceException.from(e, target().getUri());
    }

    try {
      if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
        InputStream inputStream = response.readEntity(InputStream.class);
        try {
          final JsonParser parser = MapperFactory.instance.reader().getFactory().createParser(inputStream);
          try {
            parser.nextToken();
            boolean stop = false;
            while (!stop && parser.nextToken() != JsonToken.END_OBJECT) {
              final String field = parser.getCurrentName();
              parser.nextToken();
              switch (field) {
                case ListResult.TOTAL    : handler.total(parser.getIntValue());
                                           break;
                case ListResult.START    : handler.start(parser.getIntValue());
                                           break;
                case ListResult.PAGES    : handler.items(parser.getIntValue());
                                           break;
                // Service Providers are not following strictly the RFC
                // regarding naming conventions; somtimes resources is
                // returned instead of Resources how it's required by the RFC.
                case ListResult.RESOURCE : while (parser.nextToken() != JsonToken.END_ARRAY) {
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
        throw ExceptionParser.from(response);
      }
    }
    finally {
      response.close();
    }
  }
}