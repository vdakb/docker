/*
    Oracle Deutschland BV & Co. KG

    This software is the confidential and proprietary information of
    Oracle Corporation. ("Confidential Information"). You shall not
    disclose such Confidential Information and shall use it only in
    accordance with the terms of the license agreement you entered
    into with Oracle.

    ORACLE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
    SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
    IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
    PURPOSE, OR NON-INFRINGEMENT. ORACLE SHALL NOT BE LIABLE FOR ANY DAMAGES
    SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
    THIS SOFTWARE OR ITS DERIVATIVES.

    Copyright © 2025. All Rights reserved.

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager
    Subsystem   :   Custom SCIM Service

    File        :   UriInfoRequestFilter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@silverid.fr

    Purpose     :   This file implements the class UriInfoRequestFilter.

    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2025-19-02  SBernet     First release version
*/

package bka.iam.identity.scim.extension.spi.requestfilter;

import bka.iam.identity.scim.extension.rest.RequestContext;
import bka.iam.identity.scim.extension.spi.AbstractEndpoint;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
////////////////////////////////////////////////////////////////////////////////
// class UriInfoRequestFilter
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** A JAX-RS filter that captures and sets the {@link UriInfo} for each request.
 ** <p>
 ** This filter ensures that the URI information for the incoming request is
 ** stored in the {@link RequestContext}, making it available throughout the
 ** request processing lifecycle.
 ** </p>
 **
 ** <p>
 ** This filter is registered as a JAX-RS provider using the {@code @Provider}
 ** annotation.
 ** </p>
 **
 ** @author  sylvert.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Provider
public class UriInfoRequestFilter extends AbstractEndpoint implements ContainerRequestFilter {

  //////////////////////////////////////////////////////////////////////////////
  // Instance Attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Context to hold URI-related information for the current request */
  @Context
  private UriInfo uriInfo;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new {@code UriInfoRequestFilter} instance.
   ** <p>
   ** Default constructor required by the servlet framework.
   ** </p>
   */
  public UriInfoRequestFilter() {
    // Ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filter
  /**
   ** Intercepts incoming requests to capture and store {@link UriInfo}.
   ** <p>
   ** This method is automatically invoked for every request, ensuring that
   ** the request's URI-related data is available via {@link RequestContext}.
   ** </p>
   **
   ** @param requestContext     The {@link ContainerRequestContext} containing 
   **                           details of the HTTP request.
   **                           Allowed object is {@link ContainerRequestContext}.
   */
  @Override
  public void filter(ContainerRequestContext requestContext) {
    RequestContext.setUriInfo(uriInfo);
  }
}
