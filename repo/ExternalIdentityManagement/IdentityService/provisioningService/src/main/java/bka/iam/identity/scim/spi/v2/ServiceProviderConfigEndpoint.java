/*
    Oracle Deutschland BV & Co. KG

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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Governance Extension
    Subsystem   :   Identity Governance Provisioning

    File        :   ServiceProviderConfigEndpoint.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ServiceProviderConfigEndpoint.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.scim.spi.v2;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import oracle.hst.platform.core.utility.CollectionUtility;

import oracle.iam.platform.scim.ResourceContext;
import oracle.iam.platform.scim.ProcessingException;
import oracle.iam.platform.scim.ResourceTypeDefinition;

import oracle.iam.platform.scim.config.BulkConfig;
import oracle.iam.platform.scim.config.ETagConfig;
import oracle.iam.platform.scim.config.SortConfig;
import oracle.iam.platform.scim.config.PatchConfig;
import oracle.iam.platform.scim.config.FilterConfig;
import oracle.iam.platform.scim.config.AuthenticationScheme;
import oracle.iam.platform.scim.config.ChangePasswordConfig;

import oracle.iam.platform.scim.schema.Generic;

import oracle.iam.platform.scim.annotation.ResourceType;

import oracle.iam.platform.scim.v2.ServiceProviderConfig;

import oracle.iam.identity.scim.spi.v2.AbstractServiceProviderConfigEndpoint;

////////////////////////////////////////////////////////////////////////////////
// class ServiceProviderConfigEndpoint
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** A JAX-RS servive provider implementation servicing the Service Provider
 ** Config endpoint.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Path(ResourceContext.ENDPOINT_SERVICE_PROVIDER_CONFIG)
@Produces({ResourceContext.MEDIA_TYPE, MediaType.APPLICATION_JSON})
@Consumes({ResourceContext.MEDIA_TYPE, MediaType.APPLICATION_JSON})
@ResourceType(name=ResourceContext.RESOURCE_TYPE_CONFIGURATION, description="SCIM 2.0 Service Provider Config Endpoint (https://www.rfc-editor.org/rfc/rfc7643.html#section-8.7.2)", schema=ServiceProviderConfig.class)
public class ServiceProviderConfigEndpoint extends AbstractServiceProviderConfigEndpoint {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final ResourceTypeDefinition TYPE = ResourceTypeDefinition.of(ServiceProviderConfigEndpoint.class);

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ServiceProviderConfigEndpoint</code> allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ServiceProviderConfigEndpoint() {
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

    return new ServiceProviderConfig(
        "https://doc"
      , new PatchConfig(true)
      , new BulkConfig(true, 100, 32768)
      , new FilterConfig(true, 1000)
      , new ChangePasswordConfig(true)
      , new SortConfig(true)
      , new ETagConfig(false)
      , CollectionUtility.list(
          AuthenticationScheme.bearerAuthentication(true)
        )
    );
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
   ** @return                    the {@link Generic} mapped at <code>id</code>
   **                            at the Service Provider.
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