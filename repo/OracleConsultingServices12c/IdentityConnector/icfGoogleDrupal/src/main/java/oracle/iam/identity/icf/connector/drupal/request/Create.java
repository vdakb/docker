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

    File        :   Create.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   adrien.farkas@oracle.com based on work of dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Create.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-21-05  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.drupal.request;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.ProcessingException;

import javax.ws.rs.core.Response;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.ResponseProcessingException;

import com.fasterxml.jackson.core.JsonParser;

import com.fasterxml.jackson.databind.ObjectReader;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.rest.ServiceException;

import oracle.iam.identity.icf.rest.request.Request;

import oracle.iam.identity.icf.rest.utility.MapperFactory;
import oracle.iam.identity.icf.rest.utility.MarshalFactory;

import oracle.iam.identity.icf.connector.drupal.schema.User;
import oracle.iam.identity.icf.connector.drupal.schema.UserJson;

import org.glassfish.jersey.client.HttpUrlConnectorProvider;

////////////////////////////////////////////////////////////////////////////////
// class Create
// ~~~~~ ~~~~~~
/**
 ** A factory for REST create requests.
 **
 ** @author  adrien.farkas@oracle.com based on work of dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Create extends Request<Create> {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a a new REST create request that will POST the given resource
   ** to the given web target.
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   ** @param  content            a string describing the media type of content
   **                            sent to the Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  accept             a string (or strings) describing the media type
   **                            that will be accepted from the Service
   **                            Provider.
   **                            <br>
   **                            This parameter must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  private Create(final WebTarget target, final String content, final String... accept) {
    // ensure inheritance
//    super(target, content, accept);
    super(target.
                 queryParam("_format", "json"),
          content, accept);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a generic create resource request.
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   ** @param  content            a string describing the media type of content
   **                            sent to the Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  accept             a string (or strings) describing the media type
   **                            that will be accepted from the Service
   **                            Provider.
   **                            <br>
   **                            This parameter must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a JAX-RS request.
   **                            <br>
   **                            Possible object is <code>Create</code>.
   */
  public static Create build(final WebTarget target, final String content, final String... accept) {
    return new Create(target, content, accept);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   * Invoke the REST create request using POST.
   *
   * @param resource the Drupal {@link oracle.iam.identity.icf.connector.drupal.schema.User} resource to create by a
   * POST request.
   * <br>
   * Allowed object is {@link oracle.iam.identity.icf.connector.drupal.schema.User} .
   *
   * @return <code>true</code> if the resource could be
   * created; otherwise <code>false</code>.
   * <br>
   * Possible object is <code>boolean</code>.
   *
   * @throws SystemException if an error occurred.
   */
  public boolean invoke(final User resource)
    throws SystemException {

    final String method = "Create#invoke(User)";
    Response response = null;
    try {
      final Entity payload = Entity.entity(MapperFactory.objectMapper().writeValueAsString(resource), contentType());
      response = buildRequest().post(payload);
      if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
        // the request is answered with a response body that we need at first to
        // extract as a plain String
        final String output = response.readEntity(String.class);
        return Boolean.valueOf(output);
      }
      else {
        throw ExceptionParser.from(response);
      }
    }
    catch (IOException e) {
      throw new ResponseProcessingException(response, e);
    }
    catch (ProcessingException e) {
      throw ServiceException.from(e, target().getUri());
    }
    finally {
      if (response != null)
        response.close();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   * Invoke the REST create request using POST.
   *
   * @param resource the Drupal {@link oracle.iam.identity.icf.connector.drupal.schema.User} resource to create by a
   * POST request.
   * <br>
   * Allowed object is {@link oracle.iam.identity.icf.connector.drupal.schema.User} .
   *
   * @return <code>true</code> if the resource could be
   * created; otherwise <code>false</code>.
   * <br>
   * Possible object is <code>boolean</code>.
   *
   * @throws SystemException if an error occurred.
   */
  public <T> T invoke(final T resource)
    throws SystemException {

    final String method = "Create#invoke(resource)";

    Response response = null;
    try {
      final Entity payload = Entity.entity(MapperFactory.objectMapper().writeValueAsString(resource), contentType());
      response = buildRequest().post(payload);
      if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
        final ObjectReader reader = MarshalFactory.objectReader();
        final JsonParser   parser = reader.getFactory().createParser(response.readEntity(InputStream.class));
        return (T)reader.readValue(parser, (Class<T>)resource.getClass());
      }
      else {
        throw ExceptionParser.from(response);
      }
    }
    catch (IOException e) {
      throw new ResponseProcessingException(response, e);
    }
    catch (ProcessingException e) {
      throw ServiceException.from(e, target().getUri());
    }
    finally {
      if (response != null)
        response.close();
    }
  }
}