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

    Copyright 2022 All Rights reserved

    -----------------------------------------------------------------------

    System      :   BKA Identity Manager
    Subsystem   :   Identity Services Integration

    File        :   Delete.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Delete.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      12.07.2022  Dsteding    First release version
*/

package bka.iam.identity.pid.request;

import java.io.InputStream;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import javax.ws.rs.ProcessingException;

import javax.ws.rs.core.Response;

import javax.ws.rs.client.WebTarget;

import oracle.iam.identity.foundation.TaskException;

import oracle.hst.platform.rest.request.Request;

public class Lookup extends Request<Lookup>  {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new REST lookup request.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new Lookup()" and enforces use of the public method below.
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   */
  private Lookup(final WebTarget target) {
    // ensure inheritance
    super(target);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a generic lookup resource request.
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   **
   ** @return                    a JAX-RS request.
   **                            <br>
   **                            Possible object is <code>Lookup</code>.
   */
  public static Lookup  build(final WebTarget target) {
    return new Lookup(target);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   ** Invoke the REST lookup request.
   **
   ** @return                    the successfully lookedup REST resource.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws TaskException      if the REST service provider responded with an
   **                            error.
   */
  public String invoke()
    throws TaskException {

    final Response response = buildRequest().get();
    try {
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
        final InputStream  stream = response.readEntity(InputStream.class);
        final JsonReader   reader = Json.createReader(stream);
        final JsonObject   entity = reader.readObject();
        return entity.getString("id");
      }
      else {
        throw ExceptionParser.from(response);
      }
    }
    catch (ProcessingException e) {
      throw TaskException.unhandled(e);
    }
    finally {
      response.close();
    }
  }
}