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
    Subsystem   :   Identity Governance Service

    File        :   Service.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Service.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.scim.app;

import java.util.Map;

import javax.ws.rs.ApplicationPath;

import oracle.hst.platform.core.utility.CollectionUtility;

import oracle.iam.identity.scim.spi.v2.SchemaEndpoint;
import oracle.iam.identity.scim.spi.v2.ResourceTypeEndpoint;

import oracle.iam.platform.scim.provider.ProcessingExceptionMapper;

import bka.iam.identity.igs.ServiceApplication;

import bka.iam.identity.scim.PersistenceExceptionMapper;

import bka.iam.identity.scim.spi.v2.UserEndpoint;
import bka.iam.identity.scim.spi.v2.GroupEndpoint;
import bka.iam.identity.scim.spi.v2.TenantEndpoint;
import bka.iam.identity.scim.spi.v2.ServiceProviderConfigEndpoint;

////////////////////////////////////////////////////////////////////////////////
// class Service
// ~~~~~ ~~~~~~~
/**
 ** Defines the components of a JAX-RS application and supplies additional
 ** metadata.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
// It's good practice to include a version number in the path to REST web
// services so you can have multiple versions deployed at once.
// That way consumers don't need to upgrade right away if things are working for
// them.
@ApplicationPath("v2")
public class Service extends ServiceApplication {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Service</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Service() {
    // ensure inheritance
    super(
      // register root classes
      CollectionUtility.set(
        SchemaEndpoint.class
      , ResourceTypeEndpoint.class
      , UserEndpoint.class
      , GroupEndpoint.class
      , TenantEndpoint.class
      , ServiceProviderConfigEndpoint.class
      )
      // register custom provider
    , CollectionUtility.set(
        ProcessingExceptionMapper.class
      , PersistenceExceptionMapper.class
      )
    );
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getProperties (overridden)
  /**
   ** Returns a map of custom application-wide properties.
   ** <p>
   ** The returned properties are reflected in the application configuration
   ** passed to the server-side features or injected into server-side JAX-RS
   ** components.
   ** <p>
   ** The set of returned properties may be further extended or customized at
   ** deployment time using container-specific features and deployment
   ** descriptors.
   ** <br>
   ** For example, in a Servlet-based deployment scenario, web application's
   ** &lt;context-param&gt; and Servlet &lt;init-param&gt; values may be used to
   ** extend or override values of the properties programmatically returned by
   ** this method. 
   **
   ** @return                    a {@link Map} of custom application-wide
   **                            properties.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type {@link String} as the key
   **                            and {@link Object} as the value.
   */
  @Override
  public final Map<String, Object> getProperties() {
    final Map<String, Object> env = super.getProperties();
    // In Payara 5 the default JsonB provider is Yasson.
    // Due to the annotations used in the model the backend is relying on
    // Jackson so we enforce to bypass Yasson.
    env.put("jersey.config.jsonFeature", "JacksonFeature");
    return env;
  }
}