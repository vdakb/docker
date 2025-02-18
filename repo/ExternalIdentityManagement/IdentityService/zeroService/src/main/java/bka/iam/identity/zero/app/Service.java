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
    Subsystem   :   ZeRo Service

    File        :   Service.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   adrien.farkas@oracle.com

    Purpose     :   This file implements the class
                    Service.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-17-03  AFarkas     First release version
*/

package bka.iam.identity.zero.app;

import bka.iam.identity.igs.ServiceApplication;
import bka.iam.identity.zero.spi.v1.AccountsEndpoint;
import bka.iam.identity.zero.spi.v1.AppInstancesEndpoint;
import bka.iam.identity.zero.spi.v1.AppSchemasEndpoint;
import bka.iam.identity.zero.spi.v1.EntitlementsEndpoint;
import bka.iam.identity.zero.spi.v1.PoliciesEndpoint;

import javax.ws.rs.ApplicationPath;

import oracle.hst.platform.core.utility.CollectionUtility;
import oracle.hst.platform.rest.provider.ProcessingExceptionMapper;

import oracle.iam.service.zero.provider.AccountEntityReader;
import oracle.iam.service.zero.provider.AccountEntityWriter;
import oracle.iam.service.zero.provider.ApplicationEntityReader;
import oracle.iam.service.zero.provider.ApplicationEntityWriter;
import oracle.iam.service.zero.provider.PolicyEntityReader;
import oracle.iam.service.zero.provider.PolicyEntityWriter;

import oracle.wsm.metadata.annotation.PolicyReference;
import oracle.wsm.metadata.annotation.PolicySet;

import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

////////////////////////////////////////////////////////////////////////////////
// class Service
// ~~~~~ ~~~~~~~
/**
 ** Defines the components of a JAX-RS application and supplies additional
 ** metadata.
 **
 ** @author  adrien.farkas@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
// It's good practice to include a version number in the path to REST web
// services so you can have multiple versions deployed at once.
// That way consumers don't need to upgrade right away if things are working for
// them.
@ApplicationPath("/v1")
@PolicySet(references = { 
  @PolicyReference("oracle/multi_token_rest_service_policy")
})
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
        EntitlementsEndpoint.class
        , AppInstancesEndpoint.class
        , AppSchemasEndpoint.class
        , AccountsEndpoint.class
        , PoliciesEndpoint.class
      )
      // register custom provider
      , CollectionUtility.set(
        ProcessingExceptionMapper.class
        , RolesAllowedDynamicFeature.class
        , AccountEntityReader.class
        , AccountEntityWriter.class
        , ApplicationEntityReader.class
        , ApplicationEntityWriter.class
        , PolicyEntityReader.class
        , PolicyEntityWriter.class
      )
    );
//    setApplicationName("BKA ZeRo Rest API");
//    property("jersey.config.server.disableMetainfServicesLookup", Boolean.valueOf(true));
//    property("jersey.config.disableMoxyJson", Boolean.valueOf(true));
//    property("jersey.config.server.disableAutoDiscovery", Boolean.valueOf(true));
//    property("jersey.config.server.wadl.disableWadl", Boolean.valueOf(false));
  }
  
//  @Override
//  public Set<Class<?>> getClasses() {
//    final Set<Class<?>> classes = new HashSet<Class<?>>();
//    classes.add(EntitlementsEndpoint.class);
//    classes.add(AppInstancesEndpoint.class);
//    classes.add(AppSchemasEndpoint.class);
//    classes.add(AccountsEndpoint.class);
//    classes.add(PoliciesEndpoint.class);
//    classes.add(ProcessingExceptionMapper.class);
//    classes.add(RolesAllowedDynamicFeature.class);
////    classes.add(AccountEntityReader.class);
////    classes.add(AccountEntityWriter.class);
//    classes.add(ApplicationEntityReader.class);
//    classes.add(ApplicationEntityWriter.class);
//    classes.add(PolicyEntityReader.class);
//    classes.add(PolicyEntityWriter.class);
//    // Register resources.
//    return classes;
//  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

}