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

    Copyright © 2021. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Atlassian Jira Connector

    File        :   Search.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Search.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-22-05  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.jira.request;

import java.util.List;
import java.util.ArrayList;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.ProcessingException;

import javax.ws.rs.core.Response;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.ResponseProcessingException;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonParser;

import com.fasterxml.jackson.databind.JavaType;

import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.rest.ServiceException;

import oracle.iam.identity.icf.rest.request.Request;
import oracle.iam.identity.icf.rest.request.ResultHandler;

import oracle.iam.identity.icf.rest.domain.ListResult;
import oracle.iam.identity.icf.rest.domain.ListResultHandler;

import oracle.iam.identity.icf.rest.utility.MapperFactory;

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
public class Search extends Request<Search> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The property name of the 1-based index of the first query result.
   */
  public static final String START     = "startAt";
  /**
   ** The property name of the integer indicating the desired maximum number of
   ** query results per page.
   */
  public static final String COUNT     = "maxResults";
  /**
   ** The property name of the number of pages returned in a list result
   ** page.
   */
  public static final String TOTAL     = "total";
  /**
   ** The property name of the multi-valued list of groups containing
   ** the requested groups.
   */
  public static final String GROUPS    = "groups";
  /**
   ** The property name that indicate the number of items and the numbers
   */
  public static final String HEADER    = "header";
  /**
   ** The property name that indicate the value of requested items
   */
  public static final String VALUES    = "values";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The 1-based index of the first result in the current set of list results.
   */
  @JsonProperty(START)
  private Integer            start;

  /** The number of resources returned in a list response page. */
  @JsonProperty(COUNT)
  private Integer            count;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a a new REST search request.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new Search()" and enforces use of the public method below.
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
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  count              the desired maximum number of query results per
   **                            page.
   **                            <br>
   **                            Allowed object is <code>int</code>.
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
    return target;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   list
  /**
   ** Invoke the REST search request using GET to retrieve an unamed array.
   **
   ** @param  <R>                the type of object collection to return.
   **                            <br>
   **                            Allowed object is <code>&lt;R&gt;</code>.
   ** @param  clazz              the Java class type used to determine the type
   **                            of elements to return.
   **                            <br>
   **                            Allowed object is {@link Class} of type
   **                            <code>R</code>.
   **
   ** @return                    the {@link List} containing the search results.
   **                            <br>
   **                            Possible object is {@link List} where the
   **                            payload is of type <code>R</code>.
   **
   ** @throws SystemException   if an error occurred.
   */
  public <R> List<R> list(final Class<R> clazz)
    throws SystemException {

    List<R>  result   = null;
    Response response = null;
    try {
      response  = buildRequest().get();
      if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
        final JavaType    type   = MapperFactory.instance.reader().getTypeFactory().constructCollectionType(ArrayList.class, clazz);
        final InputStream stream = response.readEntity(InputStream.class);
        try {
          result = MapperFactory.instance.readValue(stream, type);
        }
        catch (IOException e) {
          throw new ResponseProcessingException(response, e);
        }
        finally {
          if (stream != null) {
            stream.close();
          }
        }
      }
      else {
        throw ExceptionParser.from(response);
      }
    }
    catch (ProcessingException e) {
      throw ServiceException.from(e, target().getUri());
    }
    catch (IOException e) {
      throw new ResponseProcessingException(response, e);
    }
    finally {
      if (response != null)
        response.close();
    }
    return result;
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
   ** Invoke the REST retrieve request using GET.
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
   ** @throws SystemException    if the REST service provider responded with an
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
            if (parser.currentToken() == JsonToken.START_ARRAY) {
              // It looks like an array directly (not as an attribute value) was received. Process it right away.
              parser.nextToken();
              while (parser.nextToken() != JsonToken.END_ARRAY) {
                if (!handler.resource(parser.readValueAs(clazz))) {
                  break;
                }
              }
            } else {
              boolean stop = false;
              // Otherwise assume an object was received, the object contains usual header fields.
              while (!stop && parser.nextToken() != JsonToken.END_OBJECT) {
                final String field = parser.getCurrentName();
                parser.nextToken();
                switch (field) {
                  case ListResult.TOTAL    :
                  case Search.TOTAL        : handler.total(parser.getIntValue());
                                             break;
                  case ListResult.START    :
                  case Search.START        : handler.start(parser.getIntValue());
                                             break;
                  case ListResult.PAGES    :
                  case Search.COUNT        : handler.items(parser.getIntValue());
                                             break;
                  // Ugly stuff but Jira does not include startAt and Item inside
                  // JSON result... The property "hearder" displays this
                  // information inside a string
                  case Search.HEADER       : final String[] info = parser.readValueAs(String.class).replaceAll("[^-?0-9]+", " ").trim().split(" ");
                                             handler.start(Integer.parseInt(info[0]));
                                             handler.items(Integer.parseInt(info[1]));
                                             break;
                  // Service Providers are not following strictly the RFC
                  // regarding naming conventions; somtimes resources is
                  // returned instead of Resources how it's required by the RFC.
                  case ListResult.RESOURCE  :
                  case Search.GROUPS        :
                  case Search.VALUES        : while (parser.nextToken() != JsonToken.END_ARRAY) {
                                                if (!handler.resource(parser.readValueAs(clazz))) {
                                                  stop = true;
                                                  break;
                                                }
                                              }
                                              break;
              
                }
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