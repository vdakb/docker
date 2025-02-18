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

    Copyright 2019 All Rights reserved

    -----------------------------------------------------------------------

    System      :   BKA Identity Manager
    Subsystem   :   Common Shared Plugin

    File        :   OrchestrationHandler.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    OrchestrationHandler.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.02.2019  DSteding    First release version
*/

package bka.iam.identity.event;

import java.util.Set;
import java.util.Map;
import java.util.HashSet;

import java.util.Collections;

import oracle.hst.foundation.utility.CollectionUtility;
import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.conf.api.SystemConfigurationService;

import oracle.iam.conf.vo.SystemProperty;

import oracle.iam.conf.exception.SystemConfigurationServiceException;

import oracle.iam.platform.Platform;

import oracle.iam.identity.orgmgmt.vo.Organization;

import oracle.iam.identity.orgmgmt.api.OrganizationManager;

import oracle.iam.identity.usermgmt.vo.User;

import oracle.iam.identity.usermgmt.api.UserManager;

import oracle.iam.identity.rolemgmt.vo.Role;

import oracle.iam.identity.rolemgmt.api.RoleManager;

import oracle.iam.identity.exception.RoleManagerException;
import oracle.iam.identity.exception.UserManagerException;
import oracle.iam.identity.exception.OrganizationManagerException;

import static oracle.iam.identity.orgmgmt.api.OrganizationManagerConstants.AttributeName.ORG_NAME;

import static oracle.iam.identity.usermgmt.api.UserManagerConstants.AttributeName.LASTNAME;
import static oracle.iam.identity.usermgmt.api.UserManagerConstants.AttributeName.FIRSTNAME;
import static oracle.iam.identity.usermgmt.api.UserManagerConstants.AttributeName.DISPLAYNAME;
import static oracle.iam.identity.usermgmt.api.UserManagerConstants.AttributeName.COMMONNAME;
import static oracle.iam.identity.usermgmt.api.UserManagerConstants.AttributeName.FULLNAME;
import static oracle.iam.identity.usermgmt.api.UserManagerConstants.AttributeName.USER_LOGIN;
import static oracle.iam.identity.usermgmt.api.UserManagerConstants.AttributeName.USER_ORGANIZATION;

import static oracle.iam.identity.rolemgmt.api.RoleManagerConstants.ROLE_NAME;
import static oracle.iam.identity.rolemgmt.api.RoleManagerConstants.ROLE_DESCRIPTION;

////////////////////////////////////////////////////////////////////////////////
// interface OrchestrationHandler
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** Declares global visible identifier used or has to be implemented by any
 ** pre- or post-process handler.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface OrchestrationHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String              ATTRIBUTE_PARTICIPANT      = "participant";
  
  static final String              ATTRIBUTE_UNIQUEIDENTIFIER = "uniqueIdentifier";
  
  static final String              ATTRIBUTE_TENANT_UID       = "tenant_uid";

  static final String              ATTRIBUTE_LOCALE_DEFAULT   = "de-DE";

  static final String              PROPERTY_ORGANIZATION      = "BKA.Policy.Organization";
  
  static final String              TENANT_SEPARATOR           = "-";

  /** the category of the logging facility to use */
  static final String              LOGGER_CATEGORY            = "BKA.IDENTITY.PROVISIONING";

  @SuppressWarnings({"unchecked", "cast"})
  static final Map<String, Object> EMPTY                      = CollectionUtility.unmodifiable(Collections.EMPTY_MAP);

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userLogin
  /**
   ** Looks up an already existing User entity.
   **
   ** @param  identifier         the identifier of the user entity to be
   **                            looked up.
   ** @param  name               <code>true</code>, if <code>identity</code>
   **                            contains organization name and
   **                            <code>false</code> if <code>identity</code>
   **                            is organization key.
   **
   ** @return                    a name of an organization entity matching the
   **                            given <code>identity</code>.
   */
  static String userLogin(final String identifier, final boolean name) {
    final Set<String> returning = new HashSet<String>();
    returning.add(USER_LOGIN.getId());
    final User identity = identity(identifier, returning, name);
    return identity == null ? null : (String)identity.getAttribute(USER_LOGIN.getId());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userOrganization
  /**
   ** Looks up an already existing User entity.
   **
   ** @param  identifier         the identifier of the user entity to be
   **                            looked up.
   ** @param  name               <code>true</code>, if <code>identity</code>
   **                            contains organization name and
   **                            <code>false</code> if <code>identity</code>
   **                            is organization key.
   **
   ** @return                    a name of an organization entity matching the
   **                            given <code>identity</code>.
   */
  static Long userOrganization(final String identifier, final boolean name) {
    final Set<String> returning = new HashSet<String>();
    returning.add(USER_ORGANIZATION.getId());
    final User identity = identity(identifier, returning, name);
    return identity == null ? null : (Long)identity.getAttribute(USER_ORGANIZATION.getId());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organization
  /**
   ** Looks up an already existing Organization entity.
   **
   ** @param  identifier         the identifier of the organization entity to
   **                            be looked up.
   ** @param  name               <code>true</code>, if <code>identifier</code>
   **                            contains organization name and
   **                            <code>false</code> if <code>identifier</code>
   **                            is organization key.
   **
   ** @return                    a name of an organization entity matching the
   **                            given <code>identifier</code>.
   */
  static String organization(final String identifier, final boolean name) {
    final Set<String> returning = new HashSet<String>();
    returning.add(ORG_NAME.getId());
    final Organization organization = organization(identifier, returning, name);
    return organization == null ? null : (String)organization.getAttribute(ORG_NAME.getId());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organization
  /**
   ** Looks up an already existing Organization entity.
   **
   ** @param  identifier         the identifier of the organization entity to be
   **                            looked up.
   ** @param  returning          the {@link Set} of attributes of the
   **                            Organization to be returned.
   ** @param  name               <code>true</code>, if <code>identifier</code>
   **                            contains organization name and
   **                            <code>false</code> if <code>identifier</code>
   **                            is organization key.
   **
   ** @return                    a name of an organization entity matching the
   **                            given <code>identifier</code>.
   */
  static Organization organization(final String identifier, final Set<String> returning, final boolean name) {
    final OrganizationManager service =Platform.getService(OrganizationManager.class);
    try {
      return service.getDetails(identifier, returning, name);
    }
    catch (OrganizationManagerException e) {
      System.err.println(e.getLocalizedMessage());
      return null;
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationTenant
  /**
   ** Looks up an already existing Organization entity and returns the tenant
   ** value for this organization.
   **
   ** @param  identifier         the identifier of the organization entity to
   **                            be looked up.
   ** @param  name               <code>true</code>, if <code>identifier</code>
   **                            contains organization name and
   **                            <code>false</code> if <code>identifier</code>
   **                            is organization key.
   **
   ** @return                    the tenant value of an organization entity
   **                            matching the given <code>identifier</code>.
   */
  static String organizationTenant(final String identifier, final boolean name) {
    final Set<String> returning = new HashSet<String>();
    returning.add(ATTRIBUTE_TENANT_UID);
    final Organization organization = organization(identifier, returning, name);
    return organization == null ? null : (String)organization.getAttribute(ATTRIBUTE_TENANT_UID);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userNameProperty
  /**
   ** Looks up an already existing User entity and returns the mapping of the
   ** attributes.
   ** <ol>
   **   <li>USER_LOGIN
   **   <li>FIRSTNAME
   **   <li>LASTNAME
   **   <li>FULLNAME
   **   <li>DISPLAYNAME
   **   <li>COMMONNAME
   ** </ol>
   **
   ** @param  identifier         the identifier of the user entity to be
   **                            looked up.
   ** @param  name               <code>true</code>, if <code>identity</code>
   **                            contains organization name and
   **                            <code>false</code> if <code>identity</code>
   **                            is organization key.
   **
   ** @return                    a {@link Map} of a all attribute-value pair for
   **                            user belonging to <code>identifier</code>.
   */
  static Map<String, Object> userNameProperty(final String identifier, final boolean name) {
    final Set<String> returning = new HashSet<String>();
    returning.add(USER_LOGIN.getId());
    returning.add(FIRSTNAME.getId());
    returning.add(LASTNAME.getId());
    returning.add(FULLNAME.getId());
    returning.add(DISPLAYNAME.getId());
    returning.add(COMMONNAME.getId());
    final User identity = identity(identifier, returning, name);
    return identity == null ? EMPTY : identity.getAttributes();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   identity
  /**
   ** Looks up an already existing User entity.
   **
   ** @param  identifier         the identifier of the user entity to be looked
   **                            up.
   ** @param  returning          the {@link Set} of attributes of the User to be
   **                            returned.
   ** @param  name               <code>true</code>, if <code>identifier</code>
   **                            contains organization name and
   **                            <code>false</code> if <code>identifier</code>
   **                            is identity key.
   **
   ** @return                    a {@link User} entity looked up for given
   **                            <code>identifier</code> or <code>null</code> if
   **                            no {@link User} entity exists for given
   **                            <code>identifier</code>.
   */
  static User identity(final String identifier, final Set<String> returning, final boolean name) {
    final UserManager service = Platform.getService(UserManager.class);
    try {
      return service.getDetails(identifier, returning, name);
    }
    catch (UserManagerException e) {
      System.err.println(e.getLocalizedMessage());
      return null;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   roleNameProperty
  /**
   ** Looks up an already existing Role entity and returns the mapping of the
   ** attributes.
   ** <ol>
   **   <li>ROLE_NAME
   **   <li>ROLE_DESCRIPTION
   ** </ol>
   **
   ** @param  identifier         the identifier of the user entity to be
   **                            looked up.
   ** @param  name               <code>true</code>, if <code>identity</code>
   **                            contains organization name and
   **                            <code>false</code> if <code>identity</code>
   **                            is organization key.
   **
   ** @return                    a {@link Map} of a all attribute-value pair for
   **                            user belonging to <code>identifier</code>.
   */
  static Map<String, Object> roleNameProperty(final String identifier, final boolean name) {
    final Set<String> returning = new HashSet<String>();
    returning.add(ROLE_NAME);
    returning.add(ROLE_DESCRIPTION);
    final Role identity = role(identifier, returning);
    return identity == null ? EMPTY : identity.getAttributes();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   role
  /**
   ** Looks up an already existing Role entity.
   **
   ** @param  identifier         the identifier of the role entity to be looked
   **                            up.
   ** @param  returning          the {@link Set} of attributes of the Role to be
   **                            returned.
   **
   ** @return                    a {@link Role} entity looked up for given
   **                            <code>identifier</code> or <code>null</code> if
   **                            no {@link Role} entity exists for given
   **                            <code>identifier</code>.
   */
  static Role role(final String identifier, final Set<String> returning) {
    final RoleManager service = Platform.getService(RoleManager.class);
    try {
      return service.getDetails(identifier, returning);
    }
    catch (RoleManagerException e) {
      System.err.println(e.getLocalizedMessage());
      return null;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   systemProperty
  /**
   ** Fetch the <code>System Property</code> mapped as <code>name</code>.
   **
   ** @param  name               the name of the <code>System Property</code> to
   **                            return.
   **
   ** @return                    the value of the <code>System Property</code>
   **                            mapped as <code>name</code>.
   **
   ** @throws SystemConfigurationServiceException if the system property could
   **                                             not be fetched or if the
   **                                             property exists but is empty.
   */
  static String systemProperty(final String name)
    throws SystemConfigurationServiceException {

    final SystemConfigurationService config   = Platform.getService(SystemConfigurationService.class);
    final SystemProperty             property = config.getSystemProperty(name);
    if (property == null)
      throw new SystemConfigurationServiceException(OrchestrationBundle.format(OrchestrationError.PROPERTY_NOTFOUND, name));

    final String value = property.getPtyValue();
    if (StringUtility.isEmpty(value, true))
      throw new SystemConfigurationServiceException(OrchestrationBundle.format(OrchestrationError.PROPERTY_INVALID, name));

    return value;                  
  }
}