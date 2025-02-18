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

    Copyright 2018 All Rights reserved

    -----------------------------------------------------------------------

    System      :   BKA Identity Manager
    Subsystem   :   LDAP LDS Connector

    File        :   ReconciliationPlugin.java

    Compiler    :   JDK 1.8

    Author      :   nicolai.kolandjian@gmail.com

    Purpose     :   Provides common methods for transformations at the time of
                    identity reconciliations.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.02.2019  nkolandj    First release version
*/

package bka.iam.identity.lds.transformations;

import java.util.HashMap;
import java.util.ArrayList;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcColumnNotFoundException;
import Thor.API.Exceptions.tcITResourceNotFoundException;

import oracle.hst.foundation.logging.Logger;

import oracle.hst.foundation.utility.StringUtility;

import bka.iam.identity.lds.ResourceConfig;
import bka.iam.identity.lds.ReconciliationPlugin;

////////////////////////////////////////////////////////////////////////////////
// class UserLogin
// ~~~~~ ~~~~~~~~~
/**
 ** The <code>UserLogin</code> generate the login name of an identity feeded
 ** from a datasource by:
 ** <ol>
 **   <li>obtain the user principal name from the identity data provided by the
 **       data source.
 **       The name must be in the form
 **       <code>external.name@external.domain</code>.
 **       Only the naming part before the at.sign <code>@</code> is relevant.
 **   <li>obtain the configured participant shortcut <code>prefix</code> from
 **       the configuration.
 **   <li>compose a login name by concatenating the <code>prefix</code> and the
 **       <code>external.name</code> in the form
 **       <code>prefix</code><code>external.name</code>.
 ** </ol>
 **
 ** @author  nicolai.kolandjian@gmail.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class UserLogin extends ReconciliationPlugin {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>UserLogin</code> adpater that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public UserLogin() {
    // ensure inheritance
    super(Logger.create(UserLogin.class.getName()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transform
  /**
   ** Method for transforming the attributes.
   ** <br>
   ** Incoming identity and entitlements are from the target system.
   **
   ** @param  identity           the {@link HashMap} containing identity data
   **                            details.
   ** @param  entitlement        the {@link HashMap} containing child data
   **                            details.
   ** @param  attribute          the name of reconciliation attribute being
   **                            transformed.
   **
   ** @return                    the new value for reconciliation field being
   **                            operated on.
   **
   ** @throws tcAPIException                if data set errors occur.
   ** @throws tcColumnNotFoundException     if if the desired value isn't mapped
   **                                       at given <code>name</code> in the
   **                                       configuration lookup definition.
   ** @throws tcITResourceNotFoundException in case of the
   **                                       <code>IT Resource</code> is not
   **                                       defined in the Identity Manager meta
   **                                       data entries with the system
   **                                       identifier passed in
   */
  @SuppressWarnings("unused")
  public Object transform(final HashMap<String,Object> identity, final HashMap<String, ArrayList<HashMap<String, String>>> entitlement, final String attribute)
    throws tcAPIException
    ,      tcColumnNotFoundException
    ,      tcITResourceNotFoundException {

    final String value = (String)identity.get(attribute);

    // by not catching errors here, and allowing to be thrown, we are ensuring
    // we do not gracefully skip by preventing further execution, and therefore
    // allowing admins to properly fix.
    String resource  = (String)identity.get(ITRESOURCE_KEY);
    String prefix    = ResourceConfig.configurationValue(Long.parseLong(resource), PARTICIPANT_PREFIX);
    String loginName = loginName(value);

    if (StringUtility.isEmpty(loginName) || StringUtility.isEmpty(prefix)) {
      this.logger.warn("Unable to transform due to issue with one or all factors of the transform: {participantPrefix: " + prefix + "}, loginName: " + loginName + "}");
      return null; // At least null will prevent CRUD operations from getting through with faulty info
    }
    return prefix + loginName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   loginName
  public String loginName(final String principalName) {
    this.logger.trace("executing transformation; {upn: " + principalName + "}");
    int index = principalName.lastIndexOf('@');
    if (index != -1) {
      String loginName = principalName.substring(0, index);
      this.logger.debug("User Login evaluated; {User Login: " + loginName + ", principalName: " + principalName + "}");
      return loginName;
    }
    else {
      this.logger.debug("User Login not evaluated; {User Login: upn: " + principalName + "}");
      return null;
    }
  }
}