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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Manager Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   AccessPolicyInstance.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AccessPolicyInstance.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.common.spi;

import java.util.Map;
import java.util.HashMap;

import org.apache.tools.ant.BuildException;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.iam.access.common.spi.schema.BaseRequest;
import oracle.iam.access.common.spi.schema.PolicyRequest;
import oracle.iam.access.common.spi.schema.ObjectFactory;

////////////////////////////////////////////////////////////////////////////////
// class AccessPolicyInstance
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>AccessPolicyInstance</code> represents an Authentication/Authorization
 ** Policy in an Oracle Access Manager infrastructure that might be created,
 ** deleted or configured after or during an import operation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class AccessPolicyInstance extends RegistrationInstance {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the {@link ObjectFactory} to create the request payload which will be send
   ** to the registration servlet
   */
  private static final ObjectFactory factory = new ObjectFactory();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AccessPolicyInstance</code> task that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AccessPolicyInstance() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Add the specified value pairs to the parameters that has to be applied.
   **
   ** @param  property           the name of the parameter to create a mapping
   **                            for on this instance.
   ** @param  value              the value for <code>property</code> to set on
   **                            this instance.
   **
   ** @throws BuildException     if the specified property id is already part of
   **                            the parameter mapping.
   */
  public void add(final AccessPolicyProperty property, final String value)
    throws BuildException {

    // validate basic requirements
    if (property.required && StringUtility.isEmpty(value))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MANDATORY, property.id));

    // ensure inheritance and apply further validation
    super.addParameter(property.id, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (overridden)
  /**
   ** The entry point to validate the instance parameter to use.
   **
   ** @throws BuildException     in case the instance does not meet the
   **                            requirements.
   */
  @Override
  public void validate() {
    // ensure inheritance
    super.validate();

    // validate that at least one parameter is specified for configuration
    // purpose
    final HashMap<String, Object> parameter = this.parameter();
    if (parameter.size() == 0)
      throw new BuildException(ServiceResourceBundle.string(ServiceError.TYPE_PARAMETER_EMPTY));

    // initialize instance with default values if a value is not provided by
    // the frontend for a parameter which has a default
    for (AccessPolicyProperty cursor : AccessPolicyProperty.values()) {
      if (cursor.required() && cursor.defaultValue() != null && !parameter.containsKey(cursor.id))
        add(cursor, cursor.defaultValue());
    }

    // validate all required parameter
    // the frontend for a parameter which has a default
    for (AccessPolicyProperty cursor : AccessPolicyProperty.values()) {
      if (cursor.required() && !parameter.containsKey(cursor.id))
        throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MANDATORY, cursor.id));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createRequest
  /**
   ** Factory method to create the request parameter to create an entity
   ** instance.
   **
   ** @return                    the {@link BaseRequest} wrapping the request
   **                            data for create.
   **                            Possible object is {@link BaseRequest}.
   */
  protected final PolicyRequest createRequest() {
    // create the basic request
    final PolicyRequest request = basicRequest();

    final Map<String, Object> parameter = parameter();
    // add all other properties to the request which needs to be send to the
    // server for configuration purpose
    if (parameter.containsKey(AccessPolicyProperty.AUTHENTICATION_SCHEME_PROTECTED.id))
      request.setProtectedAuthenticationScheme(stringParameter(AccessPolicyProperty.AUTHENTICATION_SCHEME_PROTECTED.id));
    if (parameter.containsKey(AccessPolicyProperty.AUTHENTICATION_SCHEME_PUBLIC.id))
      request.setProtectedAuthenticationScheme(stringParameter(AccessPolicyProperty.AUTHENTICATION_SCHEME_PUBLIC.id));

    if (this.protectedResource != null)
      request.setProtectedResource(this.protectedResource);

    if (this.publicResource != null)
      request.setPublicResource(this.publicResource);

    if (this.excludedResource != null)
      request.setExcludedResource(this.excludedResource);

    if (this.applicationDomain != null)
      request.setRregApplicationDomain(this.applicationDomain);

    if (this.variation != null)
      request.setHostPortVariationsList(this.variation);

    return request;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifyRequest
  /**
   ** Factory method to create the request parameter to modify an entity
   ** instance.
   **
   ** @return                    the {@link PolicyRequest} wrapping the request
   **                            data for modify.
   **                            Possible object is {@link PolicyRequest}.
   */
  protected final PolicyRequest modifyRequest() {
    return basicRequest();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteRequest
  /**
   ** Factory method to create an {@link PolicyRequest} request with
   ** operational mode <code>policyDelete</code>.
   **
   ** @return                    the {@link PolicyRequest} wrapping the request
   **                            data.
   **
   ** @see    AccessPolicyProperty.Mode
   ** @see    AccessPolicyProperty.Mode#DELETE
   */
  protected final PolicyRequest deleteRequest() {
    return basicRequest();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteRequest
  /**
   ** Factory method to create a basic {@link PolicyRequest} payload with the
   ** actual operational mode.
   **
   ** @return                    the {@link PolicyRequest} wrapping the basic
   **                            request data.
   **
   ** @see    AccessPolicyProperty.Mode
   ** @see    AccessPolicyProperty.Mode#CREATE
   ** @see    AccessPolicyProperty.Mode#MODIFY
   ** @see    AccessPolicyProperty.Mode#DELETE
   */
  protected final PolicyRequest basicRequest() {
    final PolicyRequest request = factory.createPolicyRequest(
      this.mode                                                   // mode
    , this.serviceURL                                             // serverAddress
    , this.username                                               // username
    , this.password                                               // password
    , name()                                                      // hostIdentifier
    , stringParameter(AccessPolicyProperty.APPLICATION_DOMAIN.id) // applicationDomainName
    );
    return request;
  }
}