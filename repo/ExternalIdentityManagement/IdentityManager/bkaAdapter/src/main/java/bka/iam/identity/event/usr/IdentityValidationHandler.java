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

    File        :   IdentityValidationHandler.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    IdentityValidationHandler.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.02.2019  DSteding    First release version
    1.0.0.1      02.11.2022  DSteding    The validation becomes weak due to
                                         N.SIS requirements at the organization
                                         level.
*/

package bka.iam.identity.event.usr;

import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.util.HashMap;

import java.io.Serializable;

import oracle.iam.platform.kernel.vo.Orchestration;
import oracle.iam.platform.kernel.vo.BulkOrchestration;

import oracle.iam.platform.kernel.ValidationException;
import oracle.iam.platform.kernel.ValidationFailedException;

import oracle.iam.platform.entitymgr.vo.Entity;

import oracle.iam.conf.exception.SystemConfigurationServiceException;

import oracle.iam.identity.utils.Utils;

import oracle.iam.identity.usermgmt.api.UserManagerConstants;

import oracle.iam.identity.usermgmt.impl.UserMgrUtil;

import oracle.iam.identity.usermgmt.utils.UserManagerUtils;

import static oracle.iam.identity.usermgmt.api.UserManagerConstants.AttributeName.USER_KEY;
import static oracle.iam.identity.usermgmt.api.UserManagerConstants.AttributeName.USER_LOGIN;
import static oracle.iam.identity.usermgmt.api.UserManagerConstants.AttributeName.USER_ORGANIZATION;
import static oracle.iam.identity.usermgmt.api.UserManagerConstants.AttributeName.GENERATION_QUALIFIER;

import oracle.hst.foundation.SystemMessage;

import oracle.iam.identity.foundation.event.AbstractValidationHandler;

import bka.iam.identity.event.OrchestrationError;
import bka.iam.identity.event.OrchestrationBundle;
import bka.iam.identity.event.OrchestrationHandler;
import bka.iam.identity.event.OrchestrationMessage;

////////////////////////////////////////////////////////////////////////////////
// class IdentityValidationHandler
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Offers common methods to validate identity objects in a
 ** <code>ValidationHandler</code>.
 ** <p>
 ** This handler validates the uniqueness of <code>User Principal Name</code>
 ** accross all identities.
 ** <p>
 ** Following rules are implemented:
 ** <br>
 ** <ol>
 **   <li><b>Only</b> members of the Administrator role are permitted to enter
 **       identities manually outside of any scope.
 **       Any validation that belongs to organization name and prefix of the
 **       login name of an identity is by passed.
 **   <li><b>Any</b> user which is permitted to enter identities manually is not
 **       permitted to enter those identities outside of his own scope.
 **       Hence the validation checks the organization name entered only and not
 **       the organzation name of the logged in user. The loggin name has to be
 **       start with the name of the organization name entered.
 **   <li><b>Any</b> value typed in for the user principal name needs to be
 **       unique accross all identities in the system
 ** </ol>
 ** <p>
 ** A know issue is that at the time this validation is executed only manually
 ** entered and reconciled Identities can be validated. If later a datasource
 ** accidentally provides an Identity with the same <code>Principal Name</code>
 ** the handler is unable to detect.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class IdentityValidationHandler extends AbstractValidationHandler {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>IdentityValidationHandler</code> event handler that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public IdentityValidationHandler() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (ValidationHandler)
  /**
   ** The implementation of this pre process event handler in one-off
   ** orchestration.
   ** <p>
   ** All User prepopulate events are handled in this method
   **
   ** @param  processId          the identifier of the orchestration process.
   ** @param  eventId            the identifier of the orchestartion event
   ** @param  orchestration      the object containing contextual information
   **                            such as orchestartion parameters, operation.
   **
   ** @throws ValidationException       if the entry to create or modify
   **                                   violates the rules for the operation.
   ** @throws ValidationFailedException if the entry to create or modify
   **                                   violates the rules for the operation.
   */
  @Override
  public void validate(final long processId, final long eventId, final Orchestration orchestration)
    throws ValidationException
    ,      ValidationFailedException {

    final String method = "execute";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      // check if we are in the expected phase of identity administration
      switch (UserManagerConstants.Operations.valueOf(orchestration.getOperation())) {
        // perform all actions that are necessary for create and modify of the
        // existing identity
        case CREATE  : onCreate(orchestration.getParameters());
                       break;
        case MODIFY  : onModify(orchestration.getTarget().getEntityId(), orchestration.getParameters());
                       break;
      }
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (ValidationHandler)
  /**
   ** The implementation of this pre process event handler in one-off
   ** orchestration.
   ** <p>
   ** All User prepopulate events are handled in this method
   **
   ** @param  processId          the identifier of the orchestration process.
   ** @param  eventId            the identifier of the orchestartion event
   ** @param  orchestration      the object containing contextual information
   **                            such as orchestartion parameters, operation.
   **
   ** @throws ValidationException       if the entry to create or modify
   **                                   violates the rules for the operation.
   ** @throws ValidationFailedException if the entry to create or modify
   **                                   violates the rules for the operation.
   */
  @Override
  public void validate(final long processId, final long eventId, final BulkOrchestration orchestration)
    throws ValidationException
    ,      ValidationFailedException {

    final String method = "execute";
    trace(method, SystemMessage.METHOD_ENTRY);
    trace(method, SystemMessage.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onCreate
  /**
   ** About to validate the identity at phase create.
   **
   ** @param  parameter          the orchestration data mapping of a proccess.
   **
   ** @throws ValidationFailedException if the identity to create violates the
   **                                   implemented rules.
   */
  public void onCreate(final HashMap<String, Serializable> parameter)
    throws ValidationFailedException {

    final String method="onCreate";
    trace(method, SystemMessage.METHOD_ENTRY);

// N.SIS: not longer applicable
//    final String  runAs    = (String)ContextManager.getUserPreference(ContextManager.USERDETAILS.KEY.getKey());
//    final boolean sysadmin = AuthorizationServiceUtil.subjectHasSysAdminRole(runAs);
    try {
      final String loginName = fetchOrchestrationData(parameter, USER_LOGIN.getId());
      // check if the login name match the minimal requiremet to be longer as
      // two letters
      if (loginName.length() < 3)
        throw UserManagerUtils.createValidationFailedException("IAM-3050068", new Object[] { USER_LOGIN.getId(), loginName });

      final String organization = OrchestrationHandler.organization(fetchOrchestrationData(parameter, USER_ORGANIZATION.getId()), false);
      // naming conventions will only be checked if the logged in user is not an
      // administrator
      // we assume that an administrator knows what he is doing and know the
      // rules but has also the ability to create users out side of the naming
      // conventions
      // N.SIS: not longer applicable
      /*
      if (!sysadmin) {
         validateOrganizationName(organization, null);
        if (!StringUtility.startsWithIgnoreCase(loginName, organization))
          throw UserManagerUtils.createValidationFailedException("IAM-3050068", new Object[] { USER_LOGIN.getId(), loginName });
      }
      */
      validateParticipant(fetchOrchestrationData(parameter, OrchestrationHandler.ATTRIBUTE_PARTICIPANT), organization);
      validatePrincipalName(fetchOrchestrationData(parameter, GENERATION_QUALIFIER.getId()), null);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onModify
  /**
   ** About to validate the identity at phase modify.
   **
   ** @param  entityId           the system identifier of the identity the event
   **                            belongs to.
   ** @param  parameter          the orchestration data mapping of a proccess.
   **
   ** @throws ValidationFailedException if the identity to create violates the
   **                                   implemented rules.
   */
  public void onModify(final String entityId, final HashMap<String, Serializable> parameter)
    throws ValidationFailedException {

    final String method="onModify";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      if (parameter.containsKey(USER_ORGANIZATION.getId()) || parameter.containsKey(USER_LOGIN.getId())) {
        String loginName = null;
        if (!parameter.containsKey(USER_LOGIN.getId())) {
          loginName = OrchestrationHandler.userLogin(entityId, false);
        }
        else {
          loginName = fetchOrchestrationData(parameter, USER_LOGIN.getId());
        }
        // check if the login name match the minimal requiremet to be longer as
        // two letters
        if (loginName.length() < 3)
          throw UserManagerUtils.createValidationFailedException("IAM-3050068", new Object[] { USER_LOGIN.getId(), loginName });

// N.SIS: not longer applicable
//        final String  runAs    = (String)ContextManager.getUserPreference(ContextManager.USERDETAILS.KEY.getKey());
//        final boolean sysadmin = AuthorizationServiceUtil.subjectHasSysAdminRole(runAs);
        // naming conventions will only be checked if the logged in user is not an
        // administrator
        // we assume that an administrator knows what he is doing and know the
        // rules but has also the ability to create users out side of the naming
        // conventions
        // N.SIS: not longer applicable
        /*
        if (!sysadmin) {
          String organization = null;
          if (!parameter.containsKey(USER_ORGANIZATION.getId())) {
            organization = OrchestrationHandler.organization(String.valueOf(OrchestrationHandler.userOrganization(entityId, false)), false);
          }
          else {
            organization = OrchestrationHandler.organization(fetchOrchestrationData(parameter, USER_ORGANIZATION.getId()), false);
          }
          validateOrganizationName(organization, null);

          if (!StringUtility.startsWithIgnoreCase(loginName, organization))
            throw UserManagerUtils.createValidationFailedException("IAM-3050068", new Object[] { USER_LOGIN.getId(), loginName });
        }
        */
      }
      validateParticipant(fetchOrchestrationData(parameter, OrchestrationHandler.ATTRIBUTE_PARTICIPANT), null);
      validatePrincipalName(fetchOrchestrationData(parameter, GENERATION_QUALIFIER.getId()), null);
    }
    finally {
      // if email validatation is switched on than also principal name needs to be
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validatePrincipalName
  /**
   ** Check for principal name uniqueness and pattern if created or modified.
   ** <p>
   ** The not null check for <code>tenantName</code> is already done in
   ** starting of the orchestration.
   **
   ** @param  principalName      the attribute value typed in for
   **                            <code>Principal Name</code>.
   ** @param  tenantName         the tenant od the Identity Manager instance.
   **
   **  @throws ValidationFailedException if either the format of the entered
   **                                    <code>Principal Name</code> does not
   **                                    match or the value giver for the
   **                                    <code>Principal Name</code> is already
   **                                    being used.
   */
  public void validatePrincipalName(final String principalName, final Object tenantName)
    throws ValidationFailedException {

    final String method="validatePrincipalName";
    trace(method, SystemMessage.METHOD_ENTRY);

    // if value is not changed (what can only happens if modify) we assume
    // nothing to validate
    if (principalName == null) {
      info(OrchestrationBundle.string(OrchestrationMessage.PARTICIPANT_UNCHNAGED));
      trace(method, SystemMessage.METHOD_EXIT);
      return;
    }

    try {
      // if email validatation is switched on than also principal name needs to
      // be verified to be unique
      if (Utils.validateEmail()) {
        if (principalNameBeingUsed(tenantName == null ? null : tenantName.toString(), GENERATION_QUALIFIER.getId(), principalName)) {
          throw UserManagerUtils.createValidationFailedException("IAM-3050007", new Object[] { OrchestrationBundle.string(OrchestrationMessage.PRINCIPAL_NAME_LABEL), principalName });
        }
      }
      // check format
      // get the email pattern from the system configuration
      // we do not define our own pattern due to the format of an email is
      // similar to the format of a principal name
      if (!principalName.matches(Utils.getEmailValidationPattern())) {
        throw UserManagerUtils.createValidationFailedException("IAM-3050068", new Object[] { OrchestrationBundle.string(OrchestrationMessage.PRINCIPAL_NAME_LABEL), principalName });
      }
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateParticipant
  /**
   ** Check for participant name pattern if created or modified.
   ** <p>
   ** The not null check for <code>tenantName</code> is already done in
   ** starting of the orchestration.
   **
   ** @param  participantName    the attribute value typed in for
   **                            <code>Participant</code>.
   ** @param  tenantName         the tenant of the Identity Manager instance.
   **
   **  @throws ValidationFailedException if either the format of the entered
   **                                    <code>Participant Name</code> does not
   **                                    match.
   */
  public void validateParticipant(final String participantName, final Object tenantName)
    throws ValidationFailedException {

    final String method="validateParticipant";
    trace(method, SystemMessage.METHOD_ENTRY);

    // if value is not changed (what can only happens if modify) we assume
    // nothing to validate
    if (participantName == null) {
      info(OrchestrationBundle.string(OrchestrationMessage.PARTICIPANT_UNCHNAGED));
      trace(method, SystemMessage.METHOD_EXIT);
      return;
    }

    try {
      // check if the participant name match the minimal requiremet to be longer
      // as two letters
      if (participantName.length() < 2)
        throw UserManagerUtils.createValidationFailedException("IAM-3050068", new Object[] { OrchestrationHandler.ATTRIBUTE_PARTICIPANT, participantName });

      // get the organization pattern from the system configuration and match
      // the participant name that match exactly one of the defined in the
      // pattern
      if (!participantName.matches(OrchestrationHandler.systemProperty(OrchestrationHandler.PROPERTY_ORGANIZATION)))
        throw UserManagerUtils.createValidationFailedException("IAM-3050068", new Object[] { OrchestrationHandler.ATTRIBUTE_PARTICIPANT, participantName });

      // verify that the tenantName match exactly the participant name
// TODO: dsteding 2022/08/09 for the time being commented out as long as its
//       not clear how to handle it
//    if (!participantName.equals(tenantName))
//        throw UserManagerUtils.createValidationFailedException("IAM-3050068", new Object[] { OrchestrationHandler.ATTRIBUTE_PARTICIPANT, participantName });
    }
    catch (SystemConfigurationServiceException e) {
      throw validationFailed(e, OrchestrationError.PROPERTY_INVALID, OrchestrationBundle.RESOURCE, OrchestrationHandler.PROPERTY_ORGANIZATION);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateOrganizationName
  /**
   ** Check for organization name pattern if created or modified.
   **
   ** @param  organizationName   the attribute value typed in for
   **                            <code>Organization</code>.
   **
   **  @throws ValidationFailedException if either the format of the entered
   **                                    <code>Principal Name</code> does not
   **                                    match or the value giver for the
   **                                    <code>Principal Name</code> is already
   **                                    being used.
   */
  private void validateOrganizationName(final String organizationName, final Object tenantName)
    throws ValidationFailedException {

    final String method="validateOrganizationName";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      // check if the login name match the minimal requiremet to be longer as
      // two letters
      if (organizationName.length() != 2)
        throw UserManagerUtils.createValidationFailedException("IAM-3050068", new Object[] { USER_ORGANIZATION.getId(), organizationName });

      // get the organization pattern from the system configuration and match
      // the organization name that match exactly one of the defined in the
      // pattern
      if (!organizationName.matches(OrchestrationHandler.systemProperty(OrchestrationHandler.PROPERTY_ORGANIZATION)))
        throw UserManagerUtils.createValidationFailedException("IAM-3050068", new Object[] { USER_ORGANIZATION.getId(), organizationName });
    }
    catch (SystemConfigurationServiceException e) {
      throw validationFailed(e, OrchestrationError.PROPERTY_INVALID, OrchestrationBundle.RESOURCE, OrchestrationHandler.PROPERTY_ORGANIZATION);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   principalNameBeingUsed
  /**
   ** Find out if the principal name is being used in the system by an active
   ** user.
   ** <p>
   ** This is non-wildcard comparsion.
   */
  private boolean principalNameBeingUsed(final String tenantName, final Object attr, final String principalName) {
    final Set<String> returning = new HashSet<String>();
    returning.add(USER_LOGIN.getId());
    returning.add(USER_KEY.getId());
    returning.add(GENERATION_QUALIFIER.getId());

    final List<Entity> entities = UserMgrUtil.findUniqueAttrEntities(tenantName, attr, principalName, returning);
    if (entities == null || entities.size() == 0) {
      return false;
    }

    for (Entity entity : entities) {
      if (principalName.equalsIgnoreCase(String.valueOf(entity.getAttribute(GENERATION_QUALIFIER.getId())))) {
        return true;
      }
    }
    return false;
  }
}