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

    File        :   Parameter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Parameter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.accessagent.type;

import oracle.iam.access.common.type.FeatureParameter;

import oracle.iam.access.common.spi.AccessAgentProperty;

////////////////////////////////////////////////////////////////////////////////
// class Parameter
// ~~~~~ ~~~~~~~~
/**
 ** <code>Parameter</code> defines the attribute restriction on values that can
 ** be passed as a nested parameter to deployment instances.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Parameter extends FeatureParameter {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the names of the parameter definitions in alphabetical order
  private static final String[] VALUE = {
    AccessAgentProperty.AGENT_BASE_URL.id()
  , AccessAgentProperty.AGENT_PASSWORD.id()
//  , AccessAgentProperty.AGENT_KEY_PASSWORD.id()
  , AccessAgentProperty.ALLOW_COLLECTOR_OPERATION.id()
  , AccessAgentProperty.ALLOW_MANAGEMENT_OPERATION.id()
  , AccessAgentProperty.ALLOW_MASTER_TOKEN_RETRIEVAL.id()
  , AccessAgentProperty.ALLOW_TOKEN_SCOPE_OPERATION.id()
  , AccessAgentProperty.APPLICATION_DOMAIN.id()
  , AccessAgentProperty.AUTHENTICATION_SCHEME_PROTECTED.id()
  , AccessAgentProperty.AUTHENTICATION_SCHEME_PUBLIC.id()
  , AccessAgentProperty.AUTO_CREATE_POLICY.id()
  , AccessAgentProperty.CACHE_CONTROL_HEADER.id()
  , AccessAgentProperty.CACHE_PRAGMA_HEADER.id()
  , AccessAgentProperty.CACHE_ELEMENTS_MAX.id()
  , AccessAgentProperty.CACHE_TIMEOUT.id()
  , AccessAgentProperty.CACHE_TIMEOUT.id()
  , AccessAgentProperty.CONNECTION_MAX.id()
  , AccessAgentProperty.CONNECTION_SESSION_TIME_MAX.id()
  , AccessAgentProperty.COOKIE_DOMAIN_PRIMARY.id()
  , AccessAgentProperty.COOKIE_SESSION_TIME.id()
  , AccessAgentProperty.DEBUG.id()
  , AccessAgentProperty.DENY_NOT_PROTECTED.id()
  , AccessAgentProperty.HOST_IDENTIFIER.id()
  , AccessAgentProperty.IP_VALIDATION.id()
  , AccessAgentProperty.LOGOUT_CALLBACK_URL.id()
  , AccessAgentProperty.LOGOUT_REDIRECT_URL.id()
  , AccessAgentProperty.LOGOUT_TARGET_URL_PARAM.id()
  , AccessAgentProperty.OPENSSO_ACCESS_DENIED.id()
  , AccessAgentProperty.OPENSSO_AUDIT_DIRECTORY.id()
  , AccessAgentProperty.OPENSSO_AUDIT_FILENAME.id()
  , AccessAgentProperty.OPENSSO_COOKIE_ENCODING.id()
  , AccessAgentProperty.OPENSSO_COOKIE_NAME.id()
  , AccessAgentProperty.OPENSSO_COOKIE_SEPARATOR.id()
  , AccessAgentProperty.OPENSSO_DEBUG_DIRECTORY.id()
  , AccessAgentProperty.OPENSSO_DEBUG_FILENAME.id()
  , AccessAgentProperty.OPENSSO_DEBUG_LEVEL.id()
  , AccessAgentProperty.OPENSSO_FETCHMODE_PROFILE.id()
  , AccessAgentProperty.OPENSSO_FETCHMODE_RESPONSE.id()
  , AccessAgentProperty.OPENSSO_FETCHMODE_SESSION.id()
  , AccessAgentProperty.OPENSSO_FILTER_MODE.id()
  , AccessAgentProperty.OPENSSO_ORGANIZATION_NAME.id()
  , AccessAgentProperty.OPENSSO_SESSION_MAX.id()
  , AccessAgentProperty.OPENSSO_SESSION_TIMEOUT.id()
  , AccessAgentProperty.OPENSSO_TYPE.id()
  , AccessAgentProperty.OPENSSO_USER_ATTRIBUTE_NAME.id()
  , AccessAgentProperty.OPENSSO_USER_MAPPING_MODE.id()
  , AccessAgentProperty.OPENSSO_USER_PRINCIPAL.id()
  , AccessAgentProperty.OPENSSO_USER_TOKEN.id()
  , AccessAgentProperty.OPENSSO_USERID_PARAM.id()
  , AccessAgentProperty.OPENSSO_USERID_PARAM_TYPE.id()
  , AccessAgentProperty.OPENSSO_VERSION.id()
  , AccessAgentProperty.ORCLSSO_ADMIN_ID.id()
  , AccessAgentProperty.ORCLSSO_ADMIN_INFO.id()
  , AccessAgentProperty.ORCLSSO_FAILURE_URL.id()
  , AccessAgentProperty.ORCLSSO_HOME_URL.id()
  , AccessAgentProperty.ORCLSSO_LOGOUT_URL.id()
  , AccessAgentProperty.ORCLSSO_START_DATE.id()
  , AccessAgentProperty.ORCLSSO_SUCCESS_URL.id()
  , AccessAgentProperty.PREFERRED_HOST.id()
  , AccessAgentProperty.SECURITY.id()
  , AccessAgentProperty.SESSION_IDLE_TIMEOUT.id()
  , AccessAgentProperty.SLEEP.id()
  , AccessAgentProperty.STATE.id()
  , AccessAgentProperty.THRESHOLD_FAILOVER.id()
  , AccessAgentProperty.THRESHOLD_TIMEOUT.id()
  , AccessAgentProperty.TOKEN_VALIDITY_TIME.id()
  , AccessAgentProperty.VIRTUAL_HOST.id()
  };

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Parameter</code> task that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Parameter() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Parameter</code> with the specified name and value.
   **
   ** @param  physicalType       the name of the parameter aka the physical type
   **                            of the Oracle Acccess Manager.
   ** @param  name               the value for the name.
   */
  public Parameter(final String physicalType, final String name) {
    // ensure inheritance
    super(physicalType, name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getValues (EnumeratedAttribute)
  /**
   ** The only method a subclass needs to implement.
   **
   ** @return                    an array holding all possible values of the
   **                            enumeration. The order of elements must be
   **                            fixed so that indexOfValue(String) always
   **                            return the same index for the same value.
   */
  @Override
  public String[] getValues() {
    return VALUE;
  }
}