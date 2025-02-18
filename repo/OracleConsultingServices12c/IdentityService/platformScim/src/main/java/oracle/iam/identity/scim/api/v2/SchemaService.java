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

    System      :   Identity Service Library
    Subsystem   :   Generic SCIM Interface

    File        :   SchemaService.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    SchemaService.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.scim.api.v2;

import javax.ws.rs.core.UriInfo;

import oracle.hst.platform.rest.ProcessingException;

import oracle.iam.platform.scim.schema.Generic;

import oracle.iam.platform.scim.response.SearchResponse;

////////////////////////////////////////////////////////////////////////////////
// interface SchemaService
// ~~~~~~~~~ ~~~~~~~~~~~~~
/**
 ** An abstract JAX-RS resource class for servicing the Schemas endpoint.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface SchemaService {

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   search
  /**
   ** REST service search request for SCIM resources using GET (see section
   ** 3.4.2 of RFC 7644).
   ** <p>
   ** An HTTP GET to this service is used to retrieve information about resource
   ** schemas supported by the SCIM Service Provider.
   ** <br>
   ** An HTTP GET to the endpoint "/Schemas" returns all supported schemas in
   ** {@link SearchResponse} format.
   **
   ** @param  context            the request context.
   **                            <br>
   **                            Allowed object is {@link UriInfo}.
   **
   ** @return                    an object abstracting the response obtained
   **                            from the server to this request.
   **                            <br>
   **                            A succesful response for this operation contain
   **                            a status code of 200 and a
   **                            {@link oracle.iam.platform.scim.response.SearchResponse SearchResponse}
   **                            in the entity body (holding a collection of
   **                            SCIM schema resources).
   **
   ** @throws ProcessingException if the request fails.
   */
  SearchResponse search(final UriInfo context)
    throws ProcessingException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** REST service request to retrieve a certain SCIM {@link Generic} by its
   ** <code>id</code> using GET (as per section 3.4.1 of RFC 7644).
   **
   ** @param  id                 the identifier of the {@link Generic} to
   **                            lookup.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  context            the request context.
   **                            <br>
   **                            Allowed object is {@link UriInfo}.
   **
   ** @return                    the {@link Generic} mapped at <code>id</code>
   **                            at the Service Provider.
   **                            <br>
   **                            Possible object is {@link Generic}.
   **
   ** @throws ProcessingException if the request fails.
   */
  Generic lookup(final String id, final UriInfo context)
    throws ProcessingException;
}