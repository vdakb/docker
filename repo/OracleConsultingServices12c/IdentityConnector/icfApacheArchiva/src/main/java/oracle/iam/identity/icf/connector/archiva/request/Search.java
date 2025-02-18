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
    Subsystem   :   Apache Archiva Connector

    File        :   Search.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Search.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.archiva.request;

import java.util.List;

import java.io.InputStream;
import java.io.IOException;

import javax.ws.rs.ProcessingException;

import javax.ws.rs.core.Response;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.ResponseProcessingException;

import com.fasterxml.jackson.databind.type.CollectionType;

import com.fasterxml.jackson.databind.type.TypeFactory;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.schema.Resource;

import oracle.iam.identity.icf.rest.ServiceException;

import oracle.iam.identity.icf.rest.request.Request;

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
public class Search<R extends Resource> extends Request<Search> {

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
    return super.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   user
  /**
   ** Invoke the REST search request using GET to retrive a user array.
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
   ** @return                    the collection containing the search results.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type <code>R</code>.
   **
   ** @throws SystemException   if an error occurred.
   */
  public List<R> invoke(final Class<R> clazz)
    throws SystemException {

    List<R>  result   = null;
    Response response = null;
    try {
      response  = buildRequest().get();
      if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
        final InputStream    stream = response.readEntity(InputStream.class);
        final CollectionType type   = TypeFactory.defaultInstance().constructCollectionType(List.class, clazz);
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
}