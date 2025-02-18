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

    System      :   Oracle Identity Service Extension
    Subsystem   :   Generic SCIM Service

    File        :   ProviderEndpoint.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ProviderEndpoint.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-07-10  DSteding    First release version
*/

package oracle.iam.service.scim.v2.resource;

import java.util.Collections;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Context;

import oracle.hst.platform.rest.ProcessingException;

import oracle.iam.platform.scim.ResourceContext;
import oracle.iam.platform.scim.ResourceTypeDefinition;

import oracle.iam.platform.scim.config.BulkConfig;
import oracle.iam.platform.scim.config.ETagConfig;
import oracle.iam.platform.scim.config.SortConfig;
import oracle.iam.platform.scim.config.PatchConfig;
import oracle.iam.platform.scim.config.FilterConfig;
import oracle.iam.platform.scim.config.ChangePasswordConfig;
import oracle.iam.platform.scim.config.AuthenticationScheme;

import oracle.iam.platform.scim.schema.Generic;

import oracle.iam.platform.scim.v2.ServiceProviderConfig;

import oracle.iam.identity.scim.spi.v2.AbstractServiceProviderConfigEndpoint;

////////////////////////////////////////////////////////////////////////////////
// class ProviderEndpoint
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** A JAX-RS resource class for servicing the Service Provider Config endpoint.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Path(ResourceContext.ENDPOINT_SERVICE_PROVIDER_CONFIG)
public class ProviderEndpoint extends AbstractServiceProviderConfigEndpoint {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final ResourceTypeDefinition TYPE = ResourceTypeDefinition.of(ProviderEndpoint.class);

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ProviderEndpoint</code> service endpoint that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ProviderEndpoint() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   discover
  /**
   ** Retrieve the Service Provider Config.
   **
   ** @param  request            the request context.
   **                            <br>
   **                            Allowed object is {@link UriInfo}.
   **
   ** @return                    the Service Provider Config at the service
   **                            provider.
   **                            <br>
   **                            Possible object is
   **                            {@link ServiceProviderConfig}.
   **
   ** @throws ProcessingException if the configuration discovery fails.
   */
  protected final ServiceProviderConfig discover(final UriInfo request)
    throws ProcessingException {

    return new ServiceProviderConfig("https://doc",
        new PatchConfig(true),
        new BulkConfig(true, 100, 1000),
        new FilterConfig(true, 200),
        new ChangePasswordConfig(true),
        new SortConfig(true),
        new ETagConfig(false),
        Collections.singletonList(
            new AuthenticationScheme(
                "Basic", "HTTP BASIC", null, null, "httpbasic", true)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** REST service request to retrieve a certain SCIM {@link Generic} by its
   ** <code>id</code> using GET (as per section 3.4.1 of RFC 7644).
   **
   ** @param  request            the request context.
   **                            <br>
   **                            Allowed object is {@link UriInfo}.
   **
   ** @return                    the {@link Generic} mapped at
   **                            <code>id</code> at the Service Provider.
   **                            <br>
   **                            Possible object is {@link Generic}.
   **
   ** @throws ProcessingException if the request fails.
   */
  @GET
  public Generic lookup(final @Context UriInfo request)
    throws ProcessingException {

    return generate(TYPE, request);
  }
}