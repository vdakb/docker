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

    Copyright © 2020. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Service Extension
    Subsystem   :   Captcha Service Feature

    File        :   Service.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Service.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.access.captcha;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;

import javax.ws.rs.ApplicationPath;

import javax.ws.rs.core.Application;

import oracle.iam.access.captcha.spi.Provider;

////////////////////////////////////////////////////////////////////////////////
// class Service
// ~~~~~ ~~~~~~~
/**
 ** Defines the components of a JAX-RS application and supplies additional
 ** metadata.
 */
// It's good practice to include a version number in the path to REST web
// services so you can have multiple versions deployed at once.
// That way consumers don't need to upgrade right away if things are working for
// them.
@ApplicationPath("v1")
public class Service extends Application {

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
    super();
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
    Map<String, Object> env = new HashMap<>();
    env.put("jersey.config.server.disableMoxyJson", true);
    return env;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getClasses (overridden)
  /**
   ** Returns a set of root resource and provider classes.
   **
   ** @return                    a set of root resource and provider classes.
   **                            <br>
   **                            Returning <code>null</code> is equivalent to
   **                            returning an empty set.
   */
  @Override
  public Set<Class<?>> getClasses() {
    final Set<Class<?>> endpoint = new HashSet<Class<?>>();
    // register root resources
    endpoint.add(Provider.class);
    return endpoint;
  }
}
