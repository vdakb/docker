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

    File        :   Application.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Application.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-07-10  DSteding    First release version
*/

package oracle.iam.service.scim.v2;

import javax.ws.rs.ApplicationPath;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import org.glassfish.jersey.CommonProperties;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.CsrfProtectionFilter;

import org.glassfish.jersey.media.multipart.MultiPartFeature;

////////////////////////////////////////////////////////////////////////////////
// class Application
// ~~~~~ ~~~~~~~~~~~
/**
 ** The resource configuration for configuring the web application.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@ApplicationPath("/v2")
public class Application extends ResourceConfig {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Contructs a new resource configuration without any custom properties or
   ** resource and provider classes.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Application() {
    // ensure inheritance
    super();

    // initialize instance attributes
    property(CommonProperties.MOXY_JSON_FEATURE_DISABLE,       Boolean.valueOf(true));
    // disable auto discovery so that we can decide what we want to register and
    // what not. Don't register JacksonFeature, because it will register
    // JacksonMappingExceptionMapper, which annoyingly swallows response's
    // JsonMappingExceptions. Register directly the JacksonJaxbJsonProvider
    // which is enough for the actual JSON conversion (see the code of
    // JacksonFeature).
    property(CommonProperties.FEATURE_AUTO_DISCOVERY_DISABLE,  Boolean.valueOf(true));
    //
    property(CommonProperties.METAINF_SERVICES_LOOKUP_DISABLE, Boolean.valueOf(true));
    property("jersey.config.server.wadl.disableWadl",          Boolean.valueOf(false));

    register(MultiPartFeature.class);
    register(JacksonJsonProvider.class);
    register(CsrfProtectionFilter.class);

    packages(
      new String[] {
        "oracle.iam.service.scim.v2.resource"
      }
    );
  }
}
