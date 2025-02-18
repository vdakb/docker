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

    System      :   Identity Governance Extension
    Subsystem   :   Account Provisioning Service Model

    File        :   PlatformService.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    PlatformService.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-06-30  DSteding    First release version
*/

package oracle.iam.service;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;

import javax.ws.rs.ApplicationPath;

import javax.ws.rs.core.Application;

import oracle.hst.platform.core.utility.CollectionUtility;

import oracle.hst.platform.rest.provider.ProcessingExceptionMapper;

import oracle.iam.service.zero.service.Catalog;
import oracle.iam.service.zero.service.Provisioning;

import oracle.iam.service.zero.provider.AccountEntityReader;
import oracle.iam.service.zero.provider.AccountEntityWriter;
import oracle.iam.service.zero.provider.ApplicationEntityReader;
import oracle.iam.service.zero.provider.ApplicationEntityWriter;

////////////////////////////////////////////////////////////////////////////////
// class PlatformService
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Defines the components of a JAX-RS application and supplies additional
 ** metadata.
 **
 ** @author dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
// It's good practice to include a version number in the path to REST web
// services so you can have multiple versions deployed at once.
// That way consumers don't need to upgrade right away if things are working for
// them.
@ApplicationPath("v1")
public class PlatformService extends Application {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final Set<Class<?>> endpoint;
  final Set<Class<?>> provider;

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
  public PlatformService() {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.endpoint = CollectionUtility.set(
       Catalog.class
    ,  Provisioning.class
    );
    this.provider = CollectionUtility.set(
      AccountEntityReader.class
    , AccountEntityWriter.class
    , ApplicationEntityReader.class
    , ApplicationEntityWriter.class
    , ProcessingExceptionMapper.class
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
  public Map<String, Object> getProperties() {
    final Map<String, Object> env = new HashMap<>();
    // MOXy is the default provider in Glassfish, due to we want to use JSON-P
    // we need to disable MOXy.
    env.put("jersey.config.server.disableMoxyJson", true);
    return env;
  }

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
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link Class} of any type.
   */
  @Override
  public Set<Class<?>> getClasses() {
    return CollectionUtility.union(this.endpoint, this.provider);
  }
}