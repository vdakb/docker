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

    Copyright © 2023. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager
    Subsystem   :   Custom SCIM Service

    File        :   SCIMExtensionApplication.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   jovan.j.lakic@oracle.com

    Purpose     :   This file implements the class
                    SCIMExtensionApplication.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-25-05  JLakic     First release version
*/

package bka.iam.identity.scim.extension.app;

import bka.iam.identity.scim.extension.exception.ScimExceptionMapper;
import bka.iam.identity.scim.extension.spi.ApplicationAttributesEndpoint;
import bka.iam.identity.scim.extension.spi.ApplicationEndpoint;
import bka.iam.identity.scim.extension.spi.EntitlementEndpoint;
import bka.iam.identity.scim.extension.spi.GroupEndpoint;
import bka.iam.identity.scim.extension.spi.PolicyEndpoint;
import bka.iam.identity.scim.extension.spi.ResourceTypesEndpoint;
import bka.iam.identity.scim.extension.spi.SchemaEndpoint;
import bka.iam.identity.scim.extension.spi.ServiceProviderConfigEndpoint;
import bka.iam.identity.scim.extension.spi.ServiceProviderVersionEndpoint;
import bka.iam.identity.scim.extension.spi.UserEndpoint;
import bka.iam.identity.scim.extension.spi.requestfilter.HeaderFilter;
import bka.iam.identity.scim.extension.spi.requestfilter.QueryFilter;
import bka.iam.identity.scim.extension.spi.requestfilter.SchemaFilter;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
////////////////////////////////////////////////////////////////////////////////
// class SCIMExtensionApplication
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Defines the components of a JAX-RS application and supplies additional
 ** metadata.
 **
 ** @author  jovan.j.lakic@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@ApplicationPath("v2")
public class SCIMExtensionApplication extends Application {

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getClasses (overridden)
  /**
   ** Returns a set of root resource, provider and feature classes.
   ** <br>
   ** The default life-cycle for resource class instances is per-request.
   ** <br>
   ** The default life-cycle for providers (registered directly or via a
   ** feature) is singleton.
   **
   ** @return                    a set of root resource and provider classes.
   **                            <br>
   **                            Returning <code>null</code> is equivalent to
   **                            returning an empty set.
   */
  @Override
  public Set<Class<?>> getClasses() {
    final Set<Class<?>> s = new HashSet<Class<?>>();
    s.add(ScimExceptionMapper.class);
    s.add(HeaderFilter.class);
    s.add(SchemaFilter.class);
    s.add(QueryFilter.class);
    s.add(ServiceProviderVersionEndpoint.class);
    s.add(UserEndpoint.class);
    s.add(GroupEndpoint.class);
    s.add(SchemaEndpoint.class);
    s.add(ResourceTypesEndpoint.class);
    s.add(ApplicationEndpoint.class);
    s.add(ApplicationAttributesEndpoint.class);
    s.add(PolicyEndpoint.class);
    s.add(EntitlementEndpoint.class);
    s.add(ServiceProviderConfigEndpoint.class);
    return s;
  }
}