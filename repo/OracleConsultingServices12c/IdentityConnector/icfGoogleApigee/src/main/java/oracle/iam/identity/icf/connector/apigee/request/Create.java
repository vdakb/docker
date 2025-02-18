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
    Subsystem   :   Google Apigee Edge Connector

    File        :   Create.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Create.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-21-05  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.apigee.request;

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

import oracle.iam.identity.icf.connector.apigee.schema.User;
import oracle.iam.identity.icf.connector.apigee.schema.Tenant;
import oracle.iam.identity.icf.connector.apigee.schema.Developer;

////////////////////////////////////////////////////////////////////////////////
// class Create
// ~~~~~ ~~~~~~
/**
 ** A factory for REST create requests.
 **
 ** @author  dieter.steding@oracle.com
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
    super(target, content, accept);
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
   ** Invoke the REST create request using POST.
   **
   ** @param  resource           the Apigee {@link Tenant} resource to create by
   **                            a POST request.
   **                            <br>
   **                            Allowed object is {@link Tenant}.
   **
   ** @return                    <code>true</code> if the resource could be
   **                            created; otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   **
   ** @throws SystemException    if an error occurred.
   */
  public Tenant invoke(final Tenant resource)
    throws SystemException {

    Response response = null;
    try {
      final Entity payload = Entity.entity(MapperFactory.instance.writeValueAsString(resource), contentType());
      response = buildRequest().post(payload);
      if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
        // this is a hack to solve the unmarshalling of an entity if the
        // connector runs in the embedded Conncetor Server deployed in Identity
        // Manager
        // The native call of readEntity fails with an empty entity without any
        // exception; Aaaargh
        // the reason is that the org.eclipse.persistent JsonStructureReader
        // kicks in that isnt't able to resolve to JSON-POJO relation properly.
        // The solution is to explicitly create a Jackson parser for this
        // purpose to bypass the standard implementation
        final ObjectReader reader = MapperFactory.instance.reader();
        final JsonParser   parser = reader.getFactory().createParser(response.readEntity(InputStream.class));
        return reader.readValue(parser, Tenant.class);
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
   ** Invoke the REST create request using POST.
   **
   ** @param  resource           the Apigee {@link User} resource to create by a
   **                            POST request.
   **                            <br>
   **                            Allowed object is {@link User}.
   **
   ** @return                    <code>true</code> if the resource could be
   **                            created; otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   **
   ** @throws SystemException    if an error occurred.
   */
  public boolean invoke(final User resource)
    throws SystemException {


    Response response = null;
    try {
      final Entity payload = Entity.entity(MapperFactory.instance.writeValueAsString(resource), contentType());
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
   ** Invoke the REST create request using POST.
   **
   ** @param  resource           the Apigee {@link Developer} resource to create
   **                            by a POST request.
   **                            <br>
   **                            Allowed object is {@link Developer}.
   **
   ** @return                    <code>true</code> if the resource could be
   **                            created; otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   **
   ** @throws SystemException    if an error occurred.
   */
  public Developer invoke(final Developer resource)
    throws SystemException {

    Response response = null;
    try {
      final Entity payload = Entity.entity(MapperFactory.instance.writeValueAsString(resource), contentType());
      response = buildRequest().post(payload);
      if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
        // this is a hack to solve the unmarshalling of an entity if the
        // connector runs in the embedded Conncetor Server deployed in Identity
        // Manager
        // The native call of readEntity fails with an empty entity without any
        // exception; Aaaargh
        // the reason is that the org.eclipse.persistent JsonStructureReader
        // kicks in that isnt't able to resolve to JSON-POJO relation properly.
        // The solution is to explicitly create a Jackson parser for this
        // purpose to bypass the standard implementation
        final ObjectReader reader = MapperFactory.instance.reader();
        final JsonParser   parser = reader.getFactory().createParser(response.readEntity(InputStream.class));
        return reader.readValue(parser, Developer.class);
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