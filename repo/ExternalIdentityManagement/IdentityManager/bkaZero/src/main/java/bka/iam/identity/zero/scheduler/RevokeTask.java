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

    System      :   BKA Identity Manager
    Subsystem   :   Zero Provisioning

    File        :   RevokingTask.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@silverid.fr

    Purpose     :   This file implements the class
                    RevokingTask.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2023-17-08  Sbernet     First release version
*/
package bka.iam.identity.zero.scheduler;

import bka.iam.identity.zero.ZeroError;
import bka.iam.identity.zero.ZeroException;
import bka.iam.identity.zero.model.Identity;

import java.util.ArrayList;
import java.util.List;

import oracle.hst.foundation.SystemMessage;

import oracle.iam.identity.foundation.TaskAttribute;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.igs.model.AccountEntity;
import oracle.iam.identity.igs.model.Entity;
import oracle.iam.identity.igs.model.NamespaceEntity;
import oracle.iam.identity.orgmgmt.vo.Organization;
import oracle.iam.provisioning.vo.Account;
import oracle.iam.provisioning.vo.ApplicationInstance;
////////////////////////////////////////////////////////////////////////////////
// abstract class ProvisioningTask
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** The <code>RevokingTask</code> revokes accounts for a specific
 ** application base on information found on a LDAP authoritative system.
 **
 ** @author  sylvert.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class RevokeTask extends AbstractZeroTask {
  
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
      ** represents accounts in the application.
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
  public RevokeTask() {
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
    
    final StringBuilder          filterTenant = new StringBuilder(organization()).append(".*");
    final List<String>              membersDN = getAccountMembers(filterTenant.toString());
    final List<Identity>    identityEntities  = accountsFacade.list(applicationName, organization(), null);
    final List<AccountEntity> accountEntities = searchAccountEntity(identityEntities);
      
    final List<String> members = new ArrayList<String>();
    for (String dn : membersDN) {
      members.add(getRDNValue(dn, ACCOUNT_RDN_INDEX));
    }
      
    for (AccountEntity identity : accountEntities) {
      if (!members.contains(identity.id())) {
        pushAccountEntity(appInst, identity.id(), null, AccountEntity.Action.delete);
      }
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildAccountEntity
  /**
   ** Used to create {@link AccountEntity} object from {@link Account}.
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
