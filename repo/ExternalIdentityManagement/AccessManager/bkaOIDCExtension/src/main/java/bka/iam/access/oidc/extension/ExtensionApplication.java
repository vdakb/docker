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

    System      :   Oracle Access Manager
    Subsystem   :   OpenIdConnect Extension

    File        :   ExtensionApplication.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   jovan.j.lakic@oracle.com

    Purpose     :   This file implements the class
                    ExtensionApplication.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-19-07  JLakic     First release version
*/
package bka.iam.access.oidc.extension;

import java.util.Set;
import java.util.HashSet;

import javax.ws.rs.ApplicationPath;

import javax.ws.rs.core.Application;

import bka.iam.access.oidc.extension.spi.TokenEndpoint;
import bka.iam.access.oidc.extension.spi.UserProfileEndpoint;
import bka.iam.access.oidc.extension.spi.TokenExchangeEndpoint;
import bka.iam.access.oidc.extension.spi.DynamicClientRegistrationEndpoint;

////////////////////////////////////////////////////////////////////////////////
// class ExtensionApplication
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** Defines the components of a JAX-RS application and supplies additional
 ** metadata.
 **
 ** @author  jovan.j.lakic@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@ApplicationPath("/oauth2/rest/")
public class ExtensionApplication extends Application {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ExtensionApplication</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ExtensionApplication() {
    // ensure inheritance
    super();
  }

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
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link Class} of any type.
   */
  @Override
  public Set<Class<?>> getClasses() {
    final Set<Class<?>> s = new HashSet<Class<?>>();
    s.add(TokenEndpoint.class);
    s.add(TokenExchangeEndpoint.class);
    s.add(UserProfileEndpoint.class);
    s.add(DynamicClientRegistrationEndpoint.class);
    return s;
  }
}