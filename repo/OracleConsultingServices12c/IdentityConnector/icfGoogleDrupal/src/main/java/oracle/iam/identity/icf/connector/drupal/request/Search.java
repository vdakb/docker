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
    Subsystem   :   Google Drupal Connector

    File        :   Search.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   adrien.farkas@oracle.com based on work of dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Search.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-21-05  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.drupal.request;

import java.util.List;
import java.util.Map;

import java.io.InputStream;
import java.io.IOException;

import javax.ws.rs.ProcessingException;

import javax.ws.rs.core.Response;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.ResponseProcessingException;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonParser;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

import java.util.Arrays;

import javax.ws.rs.client.Invocation;

import oracle.iam.identity.icf.connector.drupal.schema.User;
import oracle.iam.identity.icf.connector.drupal.schema.UserJson;
import oracle.iam.identity.icf.connector.drupal.schema.UserListJson;
import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.rest.ServiceException;

import oracle.iam.identity.icf.rest.domain.ListResult;
import oracle.iam.identity.icf.rest.domain.ListResultHandler;

import oracle.iam.identity.icf.rest.request.Request;
import oracle.iam.identity.icf.rest.request.ResultHandler;

import oracle.iam.identity.icf.rest.utility.MapperFactory;
import oracle.iam.identity.icf.rest.utility.MarshalFactory;


////////////////////////////////////////////////////////////////////////////////
// class Search
// ~~~~~ ~~~~~~
/**
 ** A factory for REST search requests.
 **
 ** @author  adrien.farkas@oracle.com based on work of dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Search extends Request<Search> {

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
    super(target.
                 queryParam("_format", "json")
      );
    final String method = "Search#Search(WebTarget)";
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
    final String method = "Search#build(WebTarget)";
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
    final String method = "Search#build()";
    return super.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   user
  /**
   * Invoke the REST search request using GET to retrive a user array.
   *
   * @return the collection containing the search results.
   * <br>
   * Possible object is list of {@link oracle.iam.identity.icf.connector.drupal.schema.User} objects.
   *
   * @throws SystemException if an error occurred.
   */
  public List<User> user()
    throws SystemException {

    final String method = "Search#user()";

    List<User> result   = new ArrayList<>();
    Response   response = null;
    try {
      response  = buildRequest().get();
      if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
        final InputStream stream = response.readEntity(InputStream.class);
        final JsonParser  parser = MarshalFactory.objectReader().getFactory().createParser(stream);
        // The thing here is that we do not really know if we're listing all users or a particular one,
        // as these return different outputs (UserJson vs. UserListJson[])
        try {
          UserJson rawResponse = parser.readValueAs(UserJson.class);
//          System.out.println("Search#user(): rawResponse: " + rawResponse);
          result.add(new User(rawResponse));
//          System.out.println("Search#user(): new User(rawResponse): " + new User(rawResponse));
        }
        catch (IOException e) {
//          System.out.println(method + ": it was the list, let's parse that one");
          try {
            UserListJson[] rawResponse = parser.readValueAs(UserListJson[].class);
            for (UserListJson userJsonListEntry : rawResponse) {
              result.add(new User(userJsonListEntry));
            }
          }
          catch (IOException ex) {
            throw new ResponseProcessingException(response, ex);
          }
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
  // Method:   list
  /**
   ** Invoke the REST search request using GET to retrive a string array.
   **
   ** @return                    the {@link List} containing the search results.
   **                            <br>
   **                            Possible object is {@link List} where the
   **                            payload is of type {@link String}.
   **
   ** @throws SystemException   if an error occurred.
   */
  public List<Map<String, String>> list()
    throws SystemException {

    final String method = "Search#list()";

    List<Map<String, String>> result   = null;
    Response     response = null;
    try {
      response = buildRequest().get();
      if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
        final InputStream  stream = response.readEntity(InputStream.class);
        final ObjectMapper mapper = MapperFactory.objectMapper();
        result = mapper.readValue(stream, List.class);
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

    final String method = "Search#invoke(Class)";

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

    final String method = "Search#invoke(ResultHandler, Class)";

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

    final String method = "Search#invoke(post, handler, clazz)";
    
    Response response;
    Invocation.Builder request = buildRequest();
    try {
      if (post) {
        response = request.post(Entity.entity(this, contentType()));
      }
      else {
        response = request.get();
      }
    }
    catch (ProcessingException e) {
      throw ServiceException.from(e, target().getUri());
    }

    try {
      if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
        InputStream inputStream = response.readEntity(InputStream.class);
        try {
          final JsonParser parser = MarshalFactory.objectReader().getFactory().createParser(inputStream);
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