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

    File        :   IdentityStoreInstance.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    IdentityStoreInstance.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.common.spi;

import java.util.HashMap;

import org.apache.tools.ant.BuildException;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceOperation;
import oracle.hst.deployment.ServiceResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class IdentityStoreInstance
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>IdentityStoreInstance</code> represents an Identity Store in Oracle
 ** Access Manager infrastructure that might be created, deleted or configured
 ** after or during an import operation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class IdentityStoreInstance extends ConfigurationInstance {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>IdentityStoreInstance</code> type that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public IdentityStoreInstance() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>IdentityStoreInstance</code> type that belongs to the
   ** specified name.
   **
   ** @param name                the nameod the <code>Identity Store</code>.
   */
  public IdentityStoreInstance(final String name) {
    // ensure inheritance
    super();

    // initialite instance attributes
    name(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createSignature (ConfigurationInstannce)
  /**
   ** Returns operation's signature string accordingly to the create parameter
   ** arrays.
   **
   ** @return                    the operation's parameter strings.
   */
  @Override
  protected final String[] createSignature() {
    return IdentityStoreProperty.SIGNATURE_CREATE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createParameter (ConfigurationInstannce)
  /**
   ** Returns operation's parameter string accordingly to the create signature
   ** arrays.
   **
   ** @return                    the operation's parameter strings.
   */
  @Override
  public final Object[] createParameter() {
    final Object[] parameter = new Object[]  {
      //  0: name
      name()
      //  1: principal
    , stringParameter(IdentityStoreProperty.PRINCIPAL.id)
      //  2: credential
    , stringParameter(IdentityStoreProperty.CREDENTIAL.id)
      //  3: type
    , stringParameter(IdentityStoreProperty.TYPE.id)
      //  4: userAttribute
    , stringParameter(IdentityStoreProperty.USER_NAME_ATTRIBUTE.id)
      //  5: ldapProvider
    , stringParameter(IdentityStoreProperty.PROVIDER.id)
      //  6: roleSecAdmin
    , stringParameter(IdentityStoreProperty.ROLE_SECURITY_ADMIN.id)
      //  7: roleSecAdminUsers
    , stringParameter(IdentityStoreProperty.ROLE_SECURITY_USERS.id)
      //  8: roleSecAdminGroups
    , stringParameter(IdentityStoreProperty.ROLE_SECURITY_GROUPS.id)
      //  9: roleSysMonitor
    , stringParameter(IdentityStoreProperty.ROLE_SYSTEM_MONITOR.id)
      // 10: roleAppAdmin
    , stringParameter(IdentityStoreProperty.ROLE_APPLICATION_ADMIN.id)
      // 11: roleSysManager
    , stringParameter(IdentityStoreProperty.ROLE_SYSTEM_MANAGER.id)
      // 12: searchBase
    , stringParameter(IdentityStoreProperty.USER_SEARCH_BASE.id)
      // 13: ldapURL
    , stringParameter(IdentityStoreProperty.URL.id)
      // 14: primary
    , stringParameter(IdentityStoreProperty.PRIMARY.id, IdentityStoreProperty.PRIMARY.defaultValue)
      // 15: system
    , stringParameter(IdentityStoreProperty.SYSTEM.id, IdentityStoreProperty.SYSTEM.defaultValue)
      // 16: userIDProvider
    , stringParameter(IdentityStoreProperty.USER_NATIVE_PROVIDER.id)
      // 17: groupSearchBase
    , stringParameter(IdentityStoreProperty.GROUP_SEARCH_BASE.id)
      // 18: supplementaryReturnAttributes
    , stringParameter(IdentityStoreProperty.PREFETCHED_ATTRIBUTES.id)
    };
    return parameter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifySignature
  /**
   ** Returns operation's signature string accordingly to the modify parameter
   ** arrays.
   **
   ** @return                    the operation's parameter strings.
   */
  public final String[] modifySignature() {
    return IdentityStoreProperty.SIGNATURE_MODIFY;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifyParameter ConfigurationInstannce)
  /**
   ** Returns operation's parameter string accordingly to the create signature
   ** arrays.
   **
   ** @return                    the operation's parameter strings.
   */
  @Override
  public final Object[] modifyParameter() {
    final Object[] parameter = new Object[]  {
      //  0: name
      name()
      //  1: principal
    , stringParameter(IdentityStoreProperty.PRINCIPAL.id)
      //  2: credential
    , stringParameter(IdentityStoreProperty.CREDENTIAL.id)
      //  3: type
    , stringParameter(IdentityStoreProperty.TYPE.id)
      //  4: userAttribute
    , stringParameter(IdentityStoreProperty.USER_NAME_ATTRIBUTE.id)
      //  5: ldapProvider
    , stringParameter(IdentityStoreProperty.PROVIDER.id)
      //  6: roleSecAdmin
    , stringParameter(IdentityStoreProperty.ROLE_SECURITY_ADMIN.id)
      //  7: roleSecAdminUsers
    , stringParameter(IdentityStoreProperty.ROLE_SECURITY_USERS.id)
      //  8: roleSecAdminGroups
    , stringParameter(IdentityStoreProperty.ROLE_SECURITY_GROUPS.id)
      //  9: roleSysMonitor
    , stringParameter(IdentityStoreProperty.ROLE_SYSTEM_MONITOR.id)
      // 10: roleAppAdmin
    , stringParameter(IdentityStoreProperty.ROLE_APPLICATION_ADMIN.id)
      // 11: roleSysManager
    , stringParameter(IdentityStoreProperty.ROLE_SYSTEM_MANAGER.id)
      // 12: searchBase
    , stringParameter(IdentityStoreProperty.USER_SEARCH_BASE.id)
      // 13: ldapURL
    , stringParameter(IdentityStoreProperty.URL.id)
      // 14: primary
    , stringParameter(IdentityStoreProperty.PRIMARY.id, IdentityStoreProperty.PRIMARY.defaultValue)
      // 15: system
    , stringParameter(IdentityStoreProperty.SYSTEM.id, IdentityStoreProperty.SYSTEM.defaultValue)
      // 16: userIDProvider
    , stringParameter(IdentityStoreProperty.USER_NATIVE_PROVIDER.id)
      // 17: groupSearchBase
    , stringParameter(IdentityStoreProperty.GROUP_SEARCH_BASE.id)
      // 18: userObjectClasses
    , stringParameter(IdentityStoreProperty.USER_OBJECT_CLASS.id, IdentityStoreProperty.USER_OBJECT_CLASS.defaultValue)
      // 19: userObjectClasses
    , stringParameter(IdentityStoreProperty.GROUP_OBJECT_CLASS.id, IdentityStoreProperty.GROUP_OBJECT_CLASS.defaultValue)
      // 20: referralPolicy
    , stringParameter(IdentityStoreProperty.REFERRAL_POLICY.id)
      // 21: searchTimeLimit
    , stringParameter(IdentityStoreProperty.SEARCHTIME_LIMIT.id)
      // 22: minConnections
    , stringParameter(IdentityStoreProperty.CONNECTION_POOL_SIZEMIN.id, IdentityStoreProperty.CONNECTION_POOL_SIZEMIN.defaultValue)
      // 23: maxConnections
    , stringParameter(IdentityStoreProperty.CONNECTION_POOL_SIZEMAX.id, IdentityStoreProperty.CONNECTION_POOL_SIZEMAX.defaultValue)
      // 24: connectionWaitTimeout
    , stringParameter(IdentityStoreProperty.CONNECTION_WAIT_TIMEOUT.id, IdentityStoreProperty.CONNECTION_WAIT_TIMEOUT.defaultValue)
      // 25: connectionWaitTimeout
    , stringParameter(IdentityStoreProperty.CONNECTION_RETRY_COUNT.id, IdentityStoreProperty.CONNECTION_RETRY_COUNT.defaultValue)
      // 26: groupNameAttribute
    , stringParameter(IdentityStoreProperty.GROUP_NAME_ATTRIBUTE.id)
      // 27: groupCacheEnabled
    , stringParameter(IdentityStoreProperty.GROUP_CACHE_ENABLED.id, IdentityStoreProperty.GROUP_CACHE_ENABLED.defaultValue)
      // 28: groupCacheSize
    , stringParameter(IdentityStoreProperty.GROUP_CACHE_SIZE.id, IdentityStoreProperty.GROUP_CACHE_SIZE.defaultValue)
      // 29: groupCacheTTL
    , stringParameter(IdentityStoreProperty.GROUP_CACHE_TTL.id, IdentityStoreProperty.GROUP_CACHE_TTL.defaultValue)
      // 30: enablePasswordPolicy
    , stringParameter(IdentityStoreProperty.IDSTORE_PASSWORD_POLICY.id)
      // 31: supplementaryReturnAttributes
    , stringParameter(IdentityStoreProperty.PREFETCHED_ATTRIBUTES.id)
      // 32: idStorePwdSchema
    , stringParameter(IdentityStoreProperty.IDSTORE_PASSWORD_SCHEMA.id)
      // 33: idStoreFirstName
    , stringParameter(IdentityStoreProperty.IDSTORE_FIRSTNAME_ATTRIBUTE.id)
      // 34: idStoreGlobalUserId
    , stringParameter(IdentityStoreProperty.IDSTORE_USERID_ATTRIBUTE.id)
      // 35: idStoreChallengeQuestions
    , stringParameter(IdentityStoreProperty.IDSTORE_CHALLENGE_QUESTIONS.id)
      // 36: idStoreChallengeAnswers
    , stringParameter(IdentityStoreProperty.IDSTORE_CHALLENGE_ANSWERS.id)
      // 37: idStoreLastName
    , stringParameter(IdentityStoreProperty.IDSTORE_LASTNAME_ATTRIBUTE.id)
      // 38: idStoreLastName
    , stringParameter(IdentityStoreProperty.IDSTORE_EMAIL_ATTRIBUTE.id)
      // 39: native
    , stringParameter(IdentityStoreProperty.NATIVE.id)
    };
    return parameter;
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
  public void add(final IdentityStoreProperty property, final String value)
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
    // validate strictly for create to avoid side effects
    validate(ServiceOperation.create);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** The entry point to validate the instance parameter to use.
   **
   ** @param  operation          the {@link ServiceOperation} to validate for
   **                            <ul>
   **                              <li>{@link ServiceOperation#create}
   **                              <li>{@link ServiceOperation#modify}
   **                              <li>{@link ServiceOperation#delete}
   **                              <li>{@link ServiceOperation#print}
   **                            </ul>
   **
   ** @throws BuildException     in case the instance does not meet the
   **                            requirements.
   */
  public void validate(final ServiceOperation operation) {
    // ensure inheritance
    super.validate();

    // only create and modify commands requires specific mandatory attributes
    if (operation == ServiceOperation.delete || operation == ServiceOperation.print)
      return;

    // validate that at least one parameter is specified for configuration
    // purpose
    final HashMap<String, Object> parameter = this.parameter();
    if (parameter.size() == 0)
      throw new BuildException(ServiceResourceBundle.string(ServiceError.TYPE_PARAMETER_EMPTY));

    // initialize instance with default values if a value is not provided by
    // the frontend for a parameter which has a default
    for (IdentityStoreProperty cursor : IdentityStoreProperty.values()) {
      if (cursor.required() && cursor.defaultValue() != null && !parameter.containsKey(cursor.id))
        add(cursor, cursor.defaultValue());
    }

    // validate all required parameter
    // the frontend for a parameter which has a default
    for (IdentityStoreProperty cursor : IdentityStoreProperty.values()) {
      if (cursor.required() && !parameter.containsKey(cursor.id))
        throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MANDATORY, cursor.id));
    }

    // validate the type of the store to be created or modified
    // the existance of the value was validated above hence we don't need to
    // check again
    final String ldapProvider = stringParameter(IdentityStoreProperty.PROVIDER.id());
    // convert the provider value to enforce raise of an exception if its not
    // fit
    try {
      IdentityStoreProperty.ServiceProvider.from(ldapProvider);
    }
    catch (IllegalArgumentException e) {
      final String[] arguments = {IdentityStoreProperty.PROVIDER.id(), ldapProvider, "AD | IBM | ODSEE | OID | OUD | OVD | SJS | SLDAPD"};
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_ATTRIBUTE_VALUE, arguments));
    }

    // validate the referral policy if any in the LDAP ID store to be modified
    // the value is optional hence it's sufficient to check if a proper value
    // is provided
    if (!StringUtility.isEmpty(stringParameter(IdentityStoreProperty.REFERRAL_POLICY.id()))) {
      // convert the policy value to enforce raise of an exception if its not
      // fit
      try {
        IdentityStoreProperty.ReferralPolicy.from(stringParameter(IdentityStoreProperty.REFERRAL_POLICY.id()));
      }
      catch (IllegalArgumentException e) {
        final String[] arguments = {IdentityStoreProperty.REFERRAL_POLICY.id(), stringParameter(IdentityStoreProperty.REFERRAL_POLICY.id()), "follow | ignore | throw"};
        throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_ATTRIBUTE_VALUE, arguments));
      }
    }
  }
}