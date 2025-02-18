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

    File        :   AbstractServiceProviderConfigEndpoint.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractServiceProviderConfigEndpoint.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.scim.spi.v2;

import javax.ws.rs.core.UriInfo;

import oracle.iam.platform.scim.SearchControl;
import oracle.iam.platform.scim.ProcessingException;
import oracle.iam.platform.scim.ResourceTypeDefinition;

import oracle.iam.platform.scim.schema.Generic;

import oracle.iam.platform.scim.response.Preparer;

import oracle.iam.identity.scim.api.ResourceService;

import oracle.iam.platform.scim.v2.ServiceProviderConfig;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractServiceProviderConfigEndpoint
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** An abstract JAX-RS resource class for servicing the Service Provider Config
 ** endpoint.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class AbstractServiceProviderConfigEndpoint {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractServiceProviderConfigEndpoint</code> allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected AbstractServiceProviderConfigEndpoint() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generate
  /**
   ** Generates the Service Provider Config.
   **
   ** @param  type               the resource type definition for resources to
   **                            prepare.
   **                            <br>
   **                            Allowed object is
   **                            {@link ResourceTypeDefinition}.
   ** @param  context            the request context.
   **                            <br>
   **                            Allowed object is {@link UriInfo}.
   **
   ** @return                    the Service Provider Config at the service
   **                            provider.
   **                            <br>
   **                            Possible object is {@link Generic}.
   **
   ** @throws ProcessingException if the configuration discovery fails.
   */
  protected final Generic generate(final ResourceTypeDefinition type, final UriInfo context)
    throws ProcessingException {
    
    final SearchControl     control  = SearchControl.build(type, context);
    final Preparer<Generic> preparer = Preparer.<Generic>build(type, control.attribute(), control.exclude(), ResourceService.location(type, context));
    return preparer.locate(discover(context).generic());
  }
 
  //////////////////////////////////////////////////////////////////////////////
  // Method:   discover
  /**
   ** Retrieve the Service Provider Config.
   **
   ** @param  context            the request context.
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
  protected abstract ServiceProviderConfig discover(final UriInfo context)
    throws ProcessingException;
}