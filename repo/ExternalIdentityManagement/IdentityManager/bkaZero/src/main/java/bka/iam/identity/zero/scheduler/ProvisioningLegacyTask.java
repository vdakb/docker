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

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Scheduler Facilities

    File        :   ProvisioningLegacyTask.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@silverid.fr

    Purpose     :   This file implements the class
                    ProvisioningLegacyTask.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      02-07-2024  Sbernet     First release version
*/
package bka.iam.identity.zero.scheduler;

import bka.iam.identity.zero.ZeroError;
import bka.iam.identity.zero.ZeroException;
import bka.iam.identity.zero.ZeroMessage;
import bka.iam.identity.zero.model.Identity;
import bka.iam.identity.zero.resources.ZeroBundle;

import java.util.HashMap;
import java.util.List;

import oracle.hst.foundation.SystemMessage;

import oracle.iam.identity.foundation.TaskAttribute;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.igs.model.AccountEntity;
import oracle.iam.identity.igs.model.EntitlementEntity;
import oracle.iam.identity.igs.model.Entity;
import oracle.iam.identity.igs.model.NamespaceEntity;
import oracle.iam.identity.orgmgmt.vo.Organization;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.provisioning.vo.Account;
import oracle.iam.provisioning.vo.ApplicationInstance;
////////////////////////////////////////////////////////////////////////////////
// abstract class ProvisioningLegacyTask
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>ProvisionningLegacyTask</code> provision accounts for a specific
 ** application base on information found on a LDAP authoritative system.
 ** This class simplifies the first version from <code>ProvisionningTask</code>
 ** and takes care only about an account without any entitlements.
 **
 ** @author  sylvert.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ProvisioningLegacyTask extends AbstractZeroTask {
  
  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////
  
  /**
   ** the vector with attributes which are valid to define on the task
   ** definition
   */
  private static final TaskAttribute[] attributes      = {
     /** the task attribute IT Resource */
     TaskAttribute.build(IT_RESOURCE_LDAP,              TaskAttribute.MANDATORY)
     /** the task attribute for scope */
  ,  TaskAttribute.build(ORGANIZATION,                  TaskAttribute.MANDATORY)
     /** the task attribute to indicated the application root context */
  ,  TaskAttribute.build(APPLICATION_ROOT_CONTEXT,      TaskAttribute.MANDATORY)
     /**
      ** the task attribute to indicate the RDN entry that contains the
      ** accounts
      */
  ,  TaskAttribute.build(ACCOUNTS_RDN,                  TaskAttribute.MANDATORY)
     /**
      ** the task attribute to indicate the object class that represents
      ** accesses to the application
      */
  ,  TaskAttribute.build(OBJECT_CLASS_ACCOUNT,          TaskAttribute.MANDATORY)
     /**
      ** the task attribute to indicate the name of the attribute that
      ** represents application members in the application.
      */
  ,  TaskAttribute.build(MEMBER_ATTRIBUTE_ACCOUNT,      TaskAttribute.MANDATORY)
     /**
      ** the task attribute to indicate the name of the template used for
      ** sending report
      */
  ,  TaskAttribute.build(TEMPLATE,                      TaskAttribute.MANDATORY)
     /**
      ** the task attribute to indicate to whom the notification will be send
      */
  ,  TaskAttribute.build(ADMIN_LOGIN,                   TaskAttribute.MANDATORY)
  };
  
  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>ProvisionningTask</code> scheduler
   ** instance that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ProvisioningLegacyTask() {
    // ensure inheritance
    super();
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributes (AbstractReconciliationTask)
  /**
   ** Returns the array with names which should be populated from the scheduled
   ** task definition of Identity Manager.
   **
   ** @return                    the array with names which should be populated.
   */
  @Override
  protected TaskAttribute[] attributes() {
    return attributes;
  }

  @Override
  protected void onExecution()
    throws TaskException {
    
    final ApplicationInstance appInst = getAppInstance(this.applicationName);
    final Organization        orgInst = getOrganization(organization());
    
    if (appInst == null) {
      throw new ZeroException(ZeroError.APPLICATION_NOTFOUND, this.applicationName);
    }
    if (orgInst == null) {
      throw new ZeroException(ZeroError.ORGANIZATION_NOTFOUND, organization());
    }
    
    final StringBuilder          tenantPrefix = new StringBuilder(organization());
    final List<String>                members = getAccountMembers(tenantPrefix.toString());
      
    if (members.isEmpty()) {
      logger.warn(ZeroBundle.string(ZeroMessage.NO_ACCOUNT_TO_REQUEST));
      return;
    }
      
    final List<Identity>    identityEntities  = accountsFacade.list(applicationName, organization(), null);
    final List<AccountEntity> accountEntities = searchAccountEntity(identityEntities);
    
    //Create account
    for (String userDN : members) {
      final String usrLogin = tenantPrefix.toString().concat(getFirstRDNValue(userDN));
      // Skip identity that does not exist in OIM. This also avoid touching
      // identity that no belong to the tenant as the real login name
      // (without the tenant) contains one tenant prefix.
      User user = getUser(usrLogin);
      if (user == null) {
        continue;
      }
      
      final AccountEntity  account = popAccountEntity(accountEntities, usrLogin);
      // No account found on OIM. Means create an account from the
      // autoritative system.
      if (account == null) {
        pushAccountEntity(appInst, user.getLogin(), new HashMap<String, List<EntitlementEntity>>(), AccountEntity.Action.create);
      }
    }
    
    //Iter on the left overs and send revoke request.
    for (AccountEntity identity : accountEntities) {
      pushAccountEntity(appInst, identity.id(), null, AccountEntity.Action.delete);
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildAccountEntity
  /**
   ** Create {@link AccountEntity} object from {@link Account}.
   **
   ** @param inputAccount              the input {@link Account} object.
   **                                  <br>
   **                                  Allowed object is {@link Account}.
   **                                  
   ** @return                          the constructed {@link AccountEntity} object.
   **                                  <br>
   **                                  Possible object is {@link AccountEntity}.
   **
   ** @throws TaskException            if any error occurred when fetching IT
   **                                  Resource.
   **/
  @Override
  protected AccountEntity buildAccountEntity(final Account inputAccount)
    throws TaskException {
    final String method = "buildAccountEntity";
    trace(method, SystemMessage.METHOD_ENTRY);
    
    AccountEntity outputAccount = Entity.account(inputAccount.getAccountDescriptiveField());
    outputAccount = NamespaceEntity.account(inputAccount.getAccountDescriptiveField());
    
    trace(method, SystemMessage.METHOD_EXIT);
    return outputAccount;
  }
}