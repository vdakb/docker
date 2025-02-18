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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Oracle Identity Manager Connector

    File        :   AdminPermissionReconciliation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AdminPermissionReconciliation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    3.1.0.0      2014-06-21  DSteding    First release version
*/

package oracle.iam.identity.gis.service.reconciliation;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.Date;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Iterator;

import oracle.iam.identity.exception.NoSuchUserException;
import oracle.iam.identity.exception.UserLookupException;
import oracle.iam.identity.exception.AccessDeniedException;

import oracle.iam.platform.entitymgr.vo.SearchCriteria;

import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.api.UserManagerConstants;

import oracle.iam.provisioning.vo.Account;

import oracle.iam.provisioning.api.ProvisioningService;
import oracle.iam.provisioning.api.ProvisioningConstants;

import oracle.iam.provisioning.exception.GenericProvisioningException;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.TaskAttribute;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.rmi.IdentityServerError;
import oracle.iam.identity.foundation.rmi.IdentityServerException;

import oracle.iam.identity.gis.resource.ProvisioningBundle;

import oracle.iam.identity.gis.service.provisioning.ProvisioningError;

////////////////////////////////////////////////////////////////////////////////
// class AccountTargetReconciliation
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>AccountTargetReconciliation</code> acts as the service end
 ** point for the Oracle Identity Manager to reconcile account information
 ** from Identity Manager itself.
 **
 ** @author  Dieter.Steding@oracle.com
 ** @version 3.1.0.0
 ** @since   3.1.0.0
 */
public class AccountTargetReconciliation extends AccountReconciliation {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the vector with attributes which are valid to define on the task
   ** definition
   */
  private static final TaskAttribute[] attribute = {
    /** the task attribute Application Instance */
    TaskAttribute.build(APPLICATION_INSTANCE,   TaskAttribute.MANDATORY)
  /** the task attribute with reconciliation descriptor */
  , TaskAttribute.build(RECONCILE_DESCRIPTOR,   TaskAttribute.MANDATORY)
    /** the task attribute that holds the value of the last run */
  , TaskAttribute.build(TIMESTAMP,              TaskAttribute.MANDATORY)
  };

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>AccountTargetReconciliation</code> scheduled
   ** job that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AccountTargetReconciliation() {
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
   ** task definition of Oracle Identity Manager.
   **
   ** @return                    the array with names which should be populated.
   */
  @Override
  protected TaskAttribute[] attributes() {
    return attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateChanges (Reconciliation)
  /**
   ** Do all action which should take place for reconciliation by fetching the
   ** data from the target system.
   **
   ** @param  bulkSize           the size of a block processed in a thread
   ** @param  returning          the attributes whose values have to be returned.
   **                            Set it to <code>null</code>, if all attribute
   **                            values have to be returned
   **
   ** @throws TaskException      if the operation fails
   */
  @Override
  protected final void populateChanges(final int bulkSize, final Set<String> returning)
    throws TaskException {
    // check if a request to stop the execution is pending and return without
    // further actions if it evaluates to true
    if (isStopped())
      return;

    final String method = "populateChanges";
    trace(method, SystemMessage.METHOD_ENTRY);
    // create a task timer to gather performance metrics
    timerStart(method);

    // set the current date as the timestamp on which this task was last
    // reconciled at start
    // setting it at this time that we have next time this scheduled task will
    // run the changes made during the execution of this task
    lastReconciled(this.server.systemTime());
    final Date      since         = lastReconciled();
    final Batch     batch         = new Batch(bulkSize);

    Set admRoleTarget             = new HashSet();
    Set sysRoleTarget             = new HashSet();
    final Set usrReconcileTarget  = new HashSet();

    //Get Target OIM Data
    admRoleTarget  = server.usersWithAdminPermission();
    sysRoleTarget  = server.usersWithSystemPermission();
    //Merge two Sets to have a list of all users that have at least one role assign
    //Those will be the users elected to be reconciles
    usrReconcileTarget.addAll(admRoleTarget);
    usrReconcileTarget.addAll(sysRoleTarget);

    // Check if within the Accounts to be ignore (User with no account on Host OIM) have an identity

    final String reconCountResume = ("\nAccount Target Reconciliation resume:"
                    +"\n\t----------------------------------------------------------------"
                    +"\n\t|-> Identities with Admin Permission on target OIM  \t " + String.valueOf(admRoleTarget.size())
                    +"\n\t|-> Identities with System Permission on target OIM \t " + String.valueOf(sysRoleTarget.size())
                    +"\n\t|-> Identities to reconcile                         \t " + String.valueOf(usrReconcileTarget.size())
                    +"\n\t----------------------------------------------------------------");

    final String reconResume = ( "\nlist of Accounts ID's resume:"
                    +"\n\t|-> Identities with Admin Permission on target OIM  \t " + admRoleTarget
                    +"\n\t|-> Identities with System Permission on target OIM \t " + sysRoleTarget
                    +"\n\t|-> Identities to reconcile                         \t " + usrReconcileTarget);

    System.out.println(reconCountResume);
    System.out.println(reconResume);
    trace(method , reconCountResume);
    debug(method , reconResume);

    // check if the current thread is able to execute or a stop signal might be
    // pending
    if (isStopped())
       return;
    //RECONCILE ACCOUNTS
    if(usrReconcileTarget.size()>0)
      performReconciliation(usrReconcileTarget);

    trace(method, SystemMessage.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processSubject (Reconciliation)

  /**
   ** Do all action which should take place for reconciliation for a particular
   ** subject.
   ** <br>
   ** This will do target reconciliation of Oracle Identity Manager Users.
   **
   ** @param  subject            the {@link Map} to reconcile.
   **
   ** @throws TaskException      if an error occurs processing data.
   */
  @Override
  protected void processSubject(final Map<String, Object> subject)
    throws TaskException {

    final String method = "processSubject";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    timerStop(method);
    trace(method, SystemMessage.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  /////////////////////////////////////////////////////////////////////////////
  private void performRevoke(Set accountToRevoke)
      throws TaskException {
      final String method = "performRevoke";
      trace(method, SystemMessage.METHOD_ENTRY);
      timerStart(method);
      try {
        if(accountToRevoke.size()>0){
          Iterator usr2Revoke = accountToRevoke.iterator();
          while (usr2Revoke.hasNext()) {
            final String usrKey = String.valueOf(usr2Revoke.next());
            final Map<String, Object> account  = getAccountDetails(usrKey);
            if (account != null){
              Map<String, Object> master = transformMaster(createMaster(account, true));
              printMap(master);
              deleteEvent(master);
            }
          }
        }
      }
      catch (IdentityServerException e) {
        throw new TaskException(e);
      }
      catch (Exception e) {
        throw new TaskException(e);
      }
      finally {
        timerStop(method);
        trace(method, SystemMessage.METHOD_EXIT);
      }
    }

  private void performReconciliation(Set usrToReconcile)
   throws TaskException {
      final String method = "performReconciliation";
      trace(method, SystemMessage.METHOD_ENTRY);
      timerStart(method);
      String debugMsg = null;
      try {
        if(usrToReconcile.size()>0){
          // Load current account destails
          Iterator usr2Reconcile = usrToReconcile.iterator();
          while (usr2Reconcile.hasNext()) {
            long eventKey = -1L;
            final String usrLogin = String.valueOf(usr2Reconcile.next());
            String usrKey = (server.lookupIdentity(usrLogin)).getId();
            final Map<String, Object> account  = server.getAccountDetails4Reconciliation(usrLogin);
            if (account != null){

              Map<String, Object> master = transformMaster(createMaster(account, true));
              // check if the master entry itself is changed
              System.out.println("Printing Master:");
              printMap(master);

              if (!ignoreEvent(master)){
                eventKey = regularEvent(master, false);
                if (this.logger.debugLevel())
                  debug(method, TaskBundle.format(TaskMessage.EVENT_CREATED, Long.toString(eventKey)));
              }
              System.out.println("\nMaster["+ usrLogin +"]: " + String.valueOf(eventKey));

              final Map<String, List<Map<String, Object>>> childdata = new HashMap<String, List<Map<String, Object>>>();
              final Map<String, Object> reference = this.descriptor.entitlement();
              for (String permissionName : reference.keySet()) {
                List<Map<String, Object>> accoutPermission = server.accountPermission4Reconciliation(usrKey, permissionName, ENTITLEMENT_ENCODED_PREFIX);
                if (accoutPermission.size()>0){
                  eventKey = addEventData(eventKey, master, permissionName, accoutPermission);
                  //If at least onw child data need to be reconcile, we need to reconcile all child data,
                  //even the one that was already processed and marked as "ignored"
                debugMsg = ("\tChild :" +permissionName +"\teKey: "+ String.valueOf(eventKey) +" \t# Stored: "+String.valueOf(childdata.size()));
                if (eventKey != -1L) {
                  System.out.println(debugMsg);
                  if (childdata.size() > 0) {
                    for (Map.Entry<String, List<Map<String, Object>>> entry :
                         childdata.entrySet()) {
                      eventKey = addEventData(eventKey, master, String.valueOf(entry.getKey()), entry.getValue());
                      System.out.println("\tChild :" + String.valueOf(entry.getKey()) + "\teKey: " + String.valueOf(eventKey) +" \t\t\t<<<< RECOVERED");
                    }
                    childdata.clear();
                  }
                } else {
                  childdata.put(permissionName, accoutPermission);
                  System.out.println(debugMsg + "\t>>>> STORED");
                }
              }
              }
            }

            if (eventKey != -1L){
              closeEvent(eventKey);
              warning(TaskBundle.format(TaskMessage.EVENT_CREATED, this.descriptor.identifier(), usrLogin));
            }
            else
              warning(TaskBundle.format(TaskMessage.EVENT_IGNORED, this.descriptor.identifier(), usrLogin));
          }
        }
      }
      catch (IdentityServerException e) {
        throw new TaskException(e);
      }
      catch (Exception e) {
        throw new TaskException(e);
      }
      finally {
        trace(method, SystemMessage.METHOD_EXIT);
        timerStop(method);
      }
    }

  public final String getUserKey(final String usrLogin)
      throws IdentityServerException {

      String usrKey = null;
      final Set<String>  retAttrs  = new HashSet<String>();
      retAttrs.add(UserManagerConstants.AttributeName.USER_LOGIN.getId());
      try {
        usrKey = service(UserManager.class).getDetails(usrLogin,retAttrs,true).getId();
      }
      catch (AccessDeniedException e) {
        final String[] arguments = { usrLogin, "UserManager.getDetails"};
        throw new IdentityServerException(IdentityServerError.CONTEXT_ACCESS_DENIED, arguments);
      }
      catch (NoSuchUserException e) {
        throw new IdentityServerException(IdentityServerError.IDENTITY_NOT_EXISTS, usrLogin);
      }
      catch (UserLookupException e) {
        throw new IdentityServerException(IdentityServerError.IDENTITY_AMBIGUOUS, usrLogin);
      }
      catch (Exception e) {
        throw new IdentityServerException(IdentityServerError.UNHANDLED, e);
      }
      return usrKey;
    }


  // return an UserKey Set with the users that have an account for the
  // specifies application instance
  private final Set listUsersWithAccount(String appInstance)
  throws TaskException
  {
    Set accountUsr  = new HashSet();
    List<Account> accounts = getProvisionedAccountsForAppInstance(appInstance);
    for (Account account : accounts)
      accountUsr.add(account.getAccountDescriptiveField());

    return accountUsr;
  }


  public final String getUserLogin(final String usrKey)
      throws IdentityServerException {

      String usrLogin = null;
      final Set<String>  retAttrs  = new HashSet<String>();
      retAttrs.add(UserManagerConstants.AttributeName.USER_LOGIN.getId());
      try {
        System.out.print("-" + usrKey);
        usrLogin = service(UserManager.class).getDetails(usrKey,retAttrs,false).getLogin();
      }
      catch (AccessDeniedException e) {
        final String[] arguments = { usrKey, "UserManager.getDetails"};
        throw new IdentityServerException(IdentityServerError.CONTEXT_ACCESS_DENIED, arguments);
      }
      catch (NoSuchUserException e) {
        throw new IdentityServerException(IdentityServerError.IDENTITY_NOT_EXISTS, usrKey);
      }
      catch (UserLookupException e) {
        throw new IdentityServerException(IdentityServerError.IDENTITY_AMBIGUOUS, usrKey);
      }
      catch (Exception e) {
        throw new IdentityServerException(IdentityServerError.UNHANDLED, e);
      }
      return usrLogin;
    }


  private List<Account> getProvisionedAccountsForAppInstance(String appInstance)
    throws TaskException {

    final String method = "getProvisionedAccountsForAppInstance";
    trace(method, SystemMessage.METHOD_ENTRY);

    final SearchCriteria scFilter =  new SearchCriteria(ProvisioningConstants.AccountSearchAttribute.ACCOUNT_STATUS.getId(), "Provisioned", SearchCriteria.Operator.EQUAL);
    try {
      if (StringUtility.isEmpty(appInstance)){
        error(method, ProvisioningBundle.format(ProvisioningError.INSUFFICIENT_INFORMATION, "appInstance"));
        return null;
      }
      else {
        return( (service(ProvisioningService.class)).getProvisionedAccountsForAppInstance(appInstance, scFilter, null));
      }
    }
      // if operation fails.
      catch (AccessDeniedException e) {
        final String[] arguments = { "", "ProvisioningService.getProvisionedAccountsForAppInstance"};
        throw new TaskException(IdentityServerError.CONTEXT_ACCESS_DENIED, arguments);
      }
      catch (GenericProvisioningException e) {
        throw new TaskException(IdentityServerError.UNHANDLED, e);
      }
      finally {
        trace(method, SystemMessage.METHOD_EXIT);
      }
  }


  //check for doubles --> get user details from  host OIM
  public Set<String> getAccountDetails(final Set usrKeys)
    throws IdentityServerException {

    final String method = "getAccountDetails";
    trace(method, SystemMessage.METHOD_ENTRY);
    final Set<String>              returninData       = new HashSet<String>();
    final Map<String, Object> returnData = new HashMap<String, Object>();

    try {
      if (usrKeys.size()<=0){
        error(method, ProvisioningBundle.format(ProvisioningError.INSUFFICIENT_INFORMATION, "user ID"));
        return returninData;
      }
      else {
        Iterator usrKeyIter = usrKeys.iterator();
        while (usrKeyIter.hasNext()) {
          final String usrKey = String.valueOf(usrKeyIter.next());
          Set<String> retAttrs = new HashSet<String>();
          retAttrs.add(UserManagerConstants.AttributeName.USER_LOGIN.getId());
          retAttrs.add(UserManagerConstants.AttributeName.FIRSTNAME.getId());
          retAttrs.add(UserManagerConstants.AttributeName.LASTNAME.getId());
          User returnUser = null;

          returnUser = service(UserManager.class).getDetails(usrKey,retAttrs,false);
          returnData.put(UserManagerConstants.AttributeName.USER_LOGIN.getId(), returnUser.getLogin());
          returnData.put(UserManagerConstants.AttributeName.FIRSTNAME.getId(), returnUser.getFirstName());
          returnData.put(UserManagerConstants.AttributeName.LASTNAME.getId(), returnUser.getLastName());
          returninData.add(returnData.values().toString());
        }
      }
    }
    catch (AccessDeniedException e) {
      final String[] arguments = { "UserManager.getDetails"};
      throw new IdentityServerException(IdentityServerError.CONTEXT_ACCESS_DENIED, arguments);
    }
    catch (NoSuchUserException e) {
      throw new IdentityServerException(IdentityServerError.IDENTITY_NOT_EXISTS);
    }
    catch (UserLookupException e) {
      throw new IdentityServerException(IdentityServerError.IDENTITY_AMBIGUOUS);
    }
    catch (Exception e) {
      throw new IdentityServerException(IdentityServerError.UNHANDLED, e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return returninData;
  }

}