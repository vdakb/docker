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

    File        :   Delete.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Delete.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.archiva.request;

import javax.ws.rs.ProcessingException;

import javax.ws.rs.core.Response;

import javax.ws.rs.client.WebTarget;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.rest.ServiceException;

import oracle.iam.identity.icf.rest.request.Request;

////////////////////////////////////////////////////////////////////////////////
// class Delete
// ~~~~~ ~~~~~~
/**
 ** A factory for APIGEE delete requests.
 **
 ** @param  <T>                  the type of the returned resources.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Delete extends Request<Delete> {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new REST delete request.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new Lookup()" and enforces use of the public method below.
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
  private Delete(final WebTarget target, final String content, final String... accept) {
    // ensure inheritance
    super(target, content, accept);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a generic delete resource request.
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
   **                            Possible object is <code>Lookup</code>.
   */
  public static Delete build(final WebTarget target, final String content, final String... accept) {
    return new Delete(target, content, accept);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   ** Invoke the REST delete request using DELETE.
   **
   ** @return                    <code>true</code> if the REST resource
   **                            successfully deleted.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @throws SystemException    if an error occurred.
   */
  public boolean invoke()
    throws SystemException {

    Response response = null;
    try {
      // Attention:
      // Archiva expose almost GET methods hence we not calling delete
      response = buildRequest().get();
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
    catch (ProcessingException e) {
      throw ServiceException.from(e, target().getUri());
    }
    finally {
      if (response != null)
        response.close();
    }
  }
}