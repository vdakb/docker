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

    File        :   AccountReconciliation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AdminPermissionReconciliation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-10-23  JBandeira    First release version
*/
package oracle.iam.identity.gis.service.reconciliation;

import java.util.Set;
import java.util.Map;
import java.util.HashSet;
import java.util.HashMap;

import oracle.iam.identity.exception.NoSuchUserException;
import oracle.iam.identity.exception.UserLookupException;
import oracle.iam.identity.exception.AccessDeniedException;

import oracle.iam.identity.usermgmt.vo.User;

import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.api.UserManagerConstants;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.utility.CollectionUtility;
import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.rmi.IdentityServerError;
import oracle.iam.identity.foundation.rmi.IdentityServerResource;
import oracle.iam.identity.foundation.rmi.IdentityServerException;

import oracle.iam.identity.gis.service.ManagedServer;

import oracle.iam.identity.gis.resource.ReconciliationBundle;

////////////////////////////////////////////////////////////////////////////////
// abstract class Reconciliation
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The <code>AccountReconciliation</code> acts as the service end point for the Oracle
 ** Identity Manager to reconcile entities from Identity Manager itself.
 **
 ** @author  joao.bandeira@oracle.com
 ** @version 3.1.0.0
 ** @since   3.1.0.0
 */
abstract class AccountReconciliation extends Reconciliation {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>AccountReconciliation</code> scheduled task that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected AccountReconciliation() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   returningAttributes (Reconciliation)
  /**
   ** Returns the {@link Set} of attribute names that will be passed to a Target
   ** System search operation to specify which attributes the Service Provider
   ** should return for an account.
   **
   ** @return                   the array of attribute names that will be
   **                           passed to a Target System search operation to
   **                           specify which attributes the Service Provider
   **                           should return.
   */
  @Override
  protected final Set<String> returningAttributes() {
    return CollectionUtility.set(this.descriptor.returningAttributes());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method groupd by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize (overridden)
  /**
   ** The initialization task.
   **
   ** @throws TaskException      in case an error does occur.
   */
  @Override
  protected void initialize()
    throws TaskException {

    final String method  = "initialize";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    try {
      // ensure inheritance
      super.initialize();

      initializeConnector();
      configureDescriptor();
      if (this.logger.debugLevel())
        debug(method, TaskBundle.format(TaskMessage.ITRESOURCE_PARAMETER, this.server.toString()));
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   afterExecution
  /**
   ** The call back method just invoked after execution finished.
   ** <br>
   ** Default implementation does nothing.
   **
   ** @throws TaskException      in case an error does occur.
   */
   @Override
  protected void afterExecution()
    throws TaskException {

    // Kills Coonect to target resource
    if (this.targetService != null) {
      this.server.disconnect();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeConnector
  /**
   ** Initalize the connection capabilities.
   **
   ** @throws TaskException      if the feature configuration cannot be created.
   */
  protected void initializeConnector()
    throws TaskException {

    try {
      // get Local IT Resource settings from Application Instance();
      initializeInstance();
      this.resource = IdentityServerResource.build(this, stringValue(IT_RESOURCE));
      this.server   = new ManagedServer(this, this.resource);
      this.targetService = this.server.connect();
    }
    catch (Exception e) {
      throw new TaskException(e);
    }
  }

  public void printMap(Map<String, Object> map) {
    for (Map.Entry<String, Object> entry : map.entrySet()) {
      System.out.println(String.valueOf(entry.getKey()) + "->" + String.valueOf(entry.getValue()));
      if (entry.getValue() instanceof String) {
      }
      else if (entry.getValue() instanceof Class) {
      }
      else {
        throw new IllegalStateException("Expecting either String or Class as entry value");
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAccountDetails4Reconciliation
  /**
   ** Return a the user details of the specified user Login
   **
   ** @param  userLogin       the login name of an identity to lookup,
   **
   ** @return                 the {@link Map} with Reconciliation Attributes name -&gt; vale or
   **                         <code>null</code> if there no user
   **
   ** @throws IdentityServerException if the operation fails.
   */
  public Map<String, Object> getAccountDetails(String userLogin)
    throws IdentityServerException {

    final String method = "getAccountDetails";
    trace(method, SystemMessage.METHOD_ENTRY);

    final Set<String> preventUsrRevoke = new HashSet<String>();
    preventUsrRevoke.add("XELSYSADM");
    preventUsrRevoke.add("OIMINTERNAL");
    preventUsrRevoke.add("XELOPERATOR");
    preventUsrRevoke.add("WEBLOGIC");

    final String RECON_MAP_ACCOUNT_USERLOGIN = "userlogin";
    final String RECON_MAP_ACCOUNT_FNAME     = "firstname";
    final String RECON_MAP_ACCOUNT_LNAME     = "lastname";
    final Map<String, Object> returnData = new HashMap<String, Object>();

    try {
      if (StringUtility.isEmpty(userLogin)){
        error(method, ReconciliationBundle.format(ReconciliationError.INSUFFICIENT_INFORMATION, "userLogin"));
        return returnData;
      }
      else {
        Set<String> retAttrs = new HashSet<String>();
        retAttrs.add(UserManagerConstants.AttributeName.USER_LOGIN.getId());
        retAttrs.add(UserManagerConstants.AttributeName.FIRSTNAME.getId());
        retAttrs.add(UserManagerConstants.AttributeName.LASTNAME.getId());
        User returnUser = null;
        returnUser = service(UserManager.class).getDetails(userLogin,retAttrs,true);
        if (!preventUsrRevoke.contains(returnUser.getLogin())){
          returnData.put(RECON_MAP_ACCOUNT_USERLOGIN, returnUser.getLogin());
          returnData.put(RECON_MAP_ACCOUNT_FNAME, returnUser.getFirstName());
          returnData.put(RECON_MAP_ACCOUNT_LNAME, returnUser.getLastName());
        }
      }
    }
    catch (AccessDeniedException e) {
      final String[] arguments = { userLogin, "UserManager.getDetails"};
      throw new IdentityServerException(IdentityServerError.CONTEXT_ACCESS_DENIED, arguments);
    }
    catch (NoSuchUserException e) {
      throw new IdentityServerException(IdentityServerError.IDENTITY_NOT_EXISTS, userLogin);
    }
    catch (UserLookupException e) {
      throw new IdentityServerException(IdentityServerError.IDENTITY_AMBIGUOUS, userLogin);
    }
    catch (Exception e) {
      throw new IdentityServerException(IdentityServerError.UNHANDLED, e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }

    return returnData;
  }
}