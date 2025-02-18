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

    Copyright 2022 All Rights reserved

    -----------------------------------------------------------------------

    System      :   BKA Identity Manager
    Subsystem   :   Identity Governance Integration

    File        :   Reset.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Reset.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      12.07.2022  Dsteding    First release version
*/

package bka.iam.identity.service.pwd;

import java.util.Set;
import java.util.List;
import java.util.HashMap;

import java.util.concurrent.TimeUnit;

import javax.security.auth.login.LoginException;

import oracle.iam.platform.entitymgr.vo.SearchCriteria;

import oracle.iam.identity.usermgmt.vo.User;

import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.api.UserManagerConstants;

import oracle.iam.identity.orgmgmt.vo.Organization;

import oracle.iam.identity.orgmgmt.api.OrganizationManager;
import oracle.iam.identity.orgmgmt.api.OrganizationManagerConstants;

import oracle.iam.passwordmgmt.internal.api.RandomPasswordGenerator;

import oracle.iam.passwordmgmt.domain.generator.RandomPasswordGeneratorImpl;

import oracle.iam.identity.exception.NoSuchUserException;
import oracle.iam.identity.exception.UserSearchException;
import oracle.iam.identity.exception.UserManagerException;
import oracle.iam.identity.exception.AccessDeniedException;
import oracle.iam.identity.exception.NoSuchOrganizationException;
import oracle.iam.identity.exception.OrganizationManagerException;

import static oracle.iam.identity.utils.Constants.SEARCH_ENDROW;
import static oracle.iam.identity.utils.Constants.SEARCH_STARTROW;

import oracle.hst.foundation.SystemWatch;
import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.SystemThreadPool;

import oracle.hst.foundation.logging.Logger;

import oracle.hst.foundation.utility.DateUtility;
import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskAttribute;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.AbstractSchedulerWorker;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.reconciliation.AbstractReconciliationTask;

////////////////////////////////////////////////////////////////////////////////
// class Reset
// ~~~~~ ~~~~~
/**
 ** The <code>Reset</code> chnage the password of user that match the
 ** criteria specified by the parameters of the executing job.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Reset extends AbstractReconciliationTask {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the category of the logging facility to use */
  private static final String          LOGGER_CATEGORY = "BKA.SYSTEM.PWD";
  /**
   ** Attribute to advise which organization is in scope.
   ** <br>
   ** This attribute is optional.
   */
  private static final String          ORGANIZATION    = "Organization Name";
  /**
   ** Attribute to advise which user is in scope.
   ** <br>
   ** This attribute is optional.
   */
  private static final String          USER            = "User Filter";
  /**
   ** Attribute to advise which password have to be set.
   ** <br>
   ** This attribute is optional.
   */
  private static final String          PASSWORD        = "Password";
  /**
   ** Attribute to advise which if an email notification needs to be sent to the
   ** user whose password is changed.
   ** <br>
   ** This attribute is optional.
   */
  private static final String          NOTIFICATION    = "Send Notification";

  /**
   ** the vector with attributes which are valid to define on the task
   ** definition
   */
  private static final TaskAttribute[] attributes      = {
    /** The task attribute User */
    TaskAttribute.build(NOTIFICATION, TaskAttribute.MANDATORY)
    /** The task attribute User */
  , TaskAttribute.build(USER,         TaskAttribute.OPTIONAL)
    /** The task attribute Passsword */
  , TaskAttribute.build(PASSWORD,     TaskAttribute.OPTIONAL)
    /** The task attribute Organization */
  , TaskAttribute.build(ORGANIZATION, TaskAttribute.OPTIONAL)
  };

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Password
  // ~~~~~ ~~~~~~~~
  /**
   ** The <code>Password</code> implements the functionality to change the
   ** password for users provided in the bulk of identities passed to the
   ** contructor.
   */
  private class Password extends AbstractSchedulerWorker {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final boolean                 simulate;
    private final boolean                 notification;
    private final String                  password;
    private final List<User>              bulk;
    private final RandomPasswordGenerator generator = new RandomPasswordGeneratorImpl();

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Worker</code> thread that change the password for all
     ** users cotained in the specified collection <code>bulk</code>.
     **
     ** @param  logger           the {@link Logger} this {@link Runnable} use
     **                          for logging purpose.
     **                          <br>
     **                          Allowed object is {@link Logger}.
     ** @param  watch            the {@link SystemWatch} to collect the
     **                          performance metrics.
     ** @param  bulk             the collection of users to process.
     **                          <br>
     **                          Allowed object is {@link List} where each
     **                          element is of type {@link User}.
     ** @param  password         the password to set for each user in the
     **                          collection bulk.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  notification     <code>true</code> if an email notification
     **                          needs to be sent to the user whose password is
     **                          changed.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     ** @param  simulate         <code>true</code> if no password change should
     **                          be applied.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     **
     ** @throws TaskException    if implementing subclass is not able to
     **                          initialize correctly.
     ** @throws LoginException   if there is an error during login.
     */
    private Password(final Logger logger, final SystemWatch watch, final List<User> bulk, final String password, final boolean notification, final boolean simulate)
      throws LoginException
      ,      TaskException {

      // ensure inheritance
      super(logger, watch);

      // initialize instance attributes
      this.bulk         = bulk;
      this.password     = password;
      this.simulate     = simulate;
      this.notification = notification;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: onExecution (AbstractReconciliationTask)
    /**
     ** The entry point of the scheduled task to perform.
     **
     ** @throws TaskException      in case an error does occur.
     */
    public void onExecution()
      throws TaskException {

      final String method = "onExecution";
      timerStart(method);

      info(TaskBundle.format(TaskMessage.RECONCILE_BEGIN, UserManagerConstants.USER_ENTITY));
      info(TaskBundle.format(TaskMessage.WILLING_TO_CHANGE, String.valueOf(bulk.size())));

      // use the UserManager to trigger potential EventHandlers that are invoked
      // by the operation
      final UserManager facade = service(UserManager.class);
      for (User user : this.bulk) {
        if (!this.simulate) {
          changePassword(facade, user.getLogin(), generated(this.password));
          incrementSuccess();
          warning(method, "Password for " + user.getLogin() + " resetted");
        }
        else {
          incrementIgnored();
          warning(method, "Password change for " + user.getLogin() + " skipped due to user request");
        }
      }

      timerStop(method);
      info(TaskBundle.format(TaskMessage.RECONCILE_COMPLETE, UserManagerConstants.USER_ENTITY));
      info(TaskBundle.format(TaskMessage.ABLE_TO_CHANGE, summary().asStringArray()));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: changePassword
    /**
     ** Changes the user's password.
     **
     ** @param  facade           the Business API used to change the password.
     **                          <br>
     **                          Allowed object is {@link UserManager}.
     ** @param  userLogin        the login name of the user whose password is to
     **                          be changed.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  password         the password to set for the user belonging to
     **                          the specified login name.
     **                          <br>
     **                          Allowed object is array of <code>char</code>.
     */
    private void changePassword(final UserManager facade, final String userLogin, final char[] password) {
      final String method = "changePassword";
      trace(method, SystemMessage.METHOD_ENTRY);
      timerStart(method);
      try {
        facade.changePassword(userLogin, password, true, this.notification);
      }
      catch (NoSuchUserException e) {
        incrementIgnored();
        error(method, e.getLocalizedMessage());
      }
      catch (UserManagerException e) {
        incrementFailed();
        fatal(method, e);
      }
      finally {
        timerStop(method);
        trace(method, SystemMessage.METHOD_EXIT);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: generated
    /**
     ** Evaluates which password reset strategy have to be applied.
     ** <br>
     ** If a password string is provided the password is returned as a char
     ** array.
     ** <br>
     ** If no password string is provided the password generator is used to
     ** generate a password accordingly to the default password policy
     ** configured in the system.
     **
     ** @param  password         a password to be used.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  a password to be used.
     **                          <br>
     **                          Possible object is array of <code>char</code>.
     */
    private char[] generated(final String password) {
      final String method = "generated";
      trace(method, SystemMessage.METHOD_ENTRY);
      timerStart(method);
      try {
        return (password == null || password.length() == 0) ? this.generator.generateDefaultPolicyPassword() : password.toCharArray();
      }
      finally {
        timerStop(method);
        trace(method, SystemMessage.METHOD_EXIT);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Reset</code> scheduler instance that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Reset() {
    // ensure inheritance
    super(LOGGER_CATEGORY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributes (AbstractReconciliationTask)
  /**
   ** Returns the array with names which should be populated from the
   ** scheduled task definition of Oracle Identity Manager.
   **
   ** @return                    the array with names which should be populated.
   **                            <br>
   **                            Possible object is array of
   **                            {@link TaskAttribute}.
   */
  @Override
  protected TaskAttribute[] attributes() {
    return attributes;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onExecution (AbstractSchedulerBaseTask)
  /**
   ** The entry point of the access policy cleaning task to perform.
   **
   ** @throws TaskException in case an error does occur.
   */
  @Override
  protected void onExecution()
    throws TaskException {

    final String method = "onExecution";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    info(TaskBundle.format(TaskMessage.RECONCILIATION_BEGIN, UserManagerConstants.USER_ENTITY, getName(), "Platform"));
    // set the current date as the timestamp on which this task has been
    // last reconciled at the start of execution setting it here to have it
    // the next time this scheduled task will run the changes made during
    // the execution of this task updating this attribute will not perform
    // to write it back to the scheduled job attributes it's still in
    // memory; updateLastReconciled() will persist the change that we do
    // here only if the job completes successful
    lastReconciled(DateUtility.now());

    final HashMap<String, Object> control    = new HashMap<String, Object>();
    final Set<String>             attribute  = CollectionUtility.set(UserManagerConstants.AttributeName.USER_LOGIN.getId());
    // creating the ThreadPoolExecutor
    final int                     maximum    = integerValue(THREAD_POOL_SIZE, THREAD_POOL_SIZE_DEFAULT);
    final int                     capacity   = maximum  / 4;
    final SystemThreadPool        threadpool = new SystemThreadPool(
      SystemThreadPool.Config.build().capacity(capacity).minimum(capacity * 2).maximum(maximum).build()
    , new WorkerThreadFactory()
    );
    final UserManager facade = service(UserManager.class);
    try {
      List<User> result = null;
      final Batch          batch    = new Batch(batchSize());
      final SearchCriteria criteria = criteria();
      do {
        control.put(SEARCH_STARTROW, batch.start());
        control.put(SEARCH_ENDROW,   batch.end());
        info(TaskBundle.string(TaskMessage.COLLECTING_BEGIN));
        result = facade.search(criteria, attribute, control);
        info(TaskBundle.string(TaskMessage.COLLECTING_COMPLETE));
        // submit work to the thread pool
        threadpool.submit(new Password(logger(), this.watch, result, stringValue(PASSWORD), booleanValue(NOTIFICATION), booleanValue(GATHERONLY)));
        batch.next();
      } while (!isStopped() && result != null && result.size() == batch.size());

      if (isStopped()) {
        threadpool.shutdownNow();
      }

      // shut down the pool
      threadpool.shutdown();
      try {
        threadpool.awaitTermination(100000, TimeUnit.MILLISECONDS);
        if (!threadpool.terminated()) {
          error(method, "Pool did not terminate in time");
          threadpool.shutdownNow();
        }
        else
          warning(method, "Pool terminated in time");
      }
      catch (InterruptedException e) {
        warning(method, "Pool interrupted by system");
      }

      updateLastReconciled();
      if (isStopped()) {
        warning(TaskBundle.stringFormat(TaskMessage.RECONCILIATION_STOPPED, UserManagerConstants.USER_ENTITY, getName(), "Platform", "success"));
      }
      else {
        info(TaskBundle.format(TaskMessage.RECONCILIATION_COMPLETE, UserManagerConstants.USER_ENTITY, getName(), "Platform"));
      }
    }
    // in any case of an unhandled exception
    catch (LoginException e) {
      warning(TaskBundle.stringFormat(TaskMessage.RECONCILIATION_STOPPED, UserManagerConstants.USER_ENTITY, getName(), "Platform", e.getMessage()));
      throw TaskException.abort(e);
    }
    // if operation fails
    catch (UserSearchException e) {
      throw TaskException.general(e);
    }
    // in any case of an unhandled exception
    catch (TaskException e) {
      warning(TaskBundle.stringFormat(TaskMessage.RECONCILIATION_STOPPED, UserManagerConstants.USER_ENTITY, getName(), "Platform", e.getMessage()));
      throw e;
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   criteria
  /**
   ** Factory method to build the search criteria applied on the user search.
   **
   ** @return                    the search criteria applied on the user search.
   **                            <br>
   **                            Possible object is {@link SearchCriteria}.
   **
   ** @throws TaskException      if the organization specified does not exists.
   */
  private SearchCriteria criteria()
    throws TaskException {

    final String method = "criteria";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    try {
      SearchCriteria  organization   = null;
      if (!StringUtility.isEmpty(stringValue(ORGANIZATION))) {
        final Organization subject = lookupOrganization(stringValue(ORGANIZATION));
        organization = new SearchCriteria(UserManagerConstants.AttributeName.USER_ORGANIZATION.getId(), subject.getEntityId(), SearchCriteria.Operator.EQUAL);
      }

      SearchCriteria  criteria = null;
      if (!StringUtility.isEmpty(stringValue(USER))) {
        String value = stringValue(USER);
        if (value.startsWith("*")) {
          if (value.endsWith("*")) {
            value = value.substring(1, value.length() - 1);
            criteria = new SearchCriteria(UserManagerConstants.AttributeName.USER_LOGIN.getId(), value, SearchCriteria.Operator.CONTAINS);
          }
          else {
            value = value.substring(1);
            criteria = new SearchCriteria(UserManagerConstants.AttributeName.USER_LOGIN.getId(), value, SearchCriteria.Operator.BEGINS_WITH);
          }
        }
        else if (value.endsWith("*")) {
          if (value.startsWith("*")) {
            value = value.substring(1, value.length() - 1);
            criteria = new SearchCriteria(UserManagerConstants.AttributeName.USER_LOGIN.getId(), value, SearchCriteria.Operator.CONTAINS);
          }
          else {
            value = value.substring(0, value.length() - 1);
            criteria = new SearchCriteria(UserManagerConstants.AttributeName.USER_LOGIN.getId(), value, SearchCriteria.Operator.ENDS_WITH);
          }
        }
        else {
          criteria = new SearchCriteria(UserManagerConstants.AttributeName.USER_LOGIN.getId(), value, SearchCriteria.Operator.EQUAL);
        }
      }
      else {
        criteria = new SearchCriteria(UserManagerConstants.AttributeName.USER_KEY.getId(), 10L, SearchCriteria.Operator.GREATER_THAN);
      }
      return (organization == null) ? criteria : new SearchCriteria(criteria, organization, SearchCriteria.Operator.AND);
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupOrganization
  /**
   ** Lookup a certain organization by its unique name and returns the internal
   ** system identifier of that organization if its exists.
   **
   ** @param  name               the resource identifier (for example the value
   **                            of the "<code>displayName</code>" attribute) to
   **                            match.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the successfully retrieved record of an
   **                            Identity Governance organization resource.
   **                            <br>
   **                            Possible object is {@link Organization}.
   **
   ** @throws TaskException      if the operation fails.
   */
  private Organization lookupOrganization(final String name)
    throws TaskException {

    final String method = "lookupOrganization";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    try {
      return service(OrganizationManager.class).getDetails(name, null, true);
    }
    // if operation not permitted
    catch (AccessDeniedException e) {
      throw TaskException.abort(TaskBundle.string(TaskError.ACCESS_DENIED));
    }
    // if organization not exists
    catch (NoSuchOrganizationException e) {
      final String[] parameter = {OrganizationManagerConstants.ORG_ENTITY_NAME, name};
      throw new TaskException(TaskError.NOSUCH_ENTITY, parameter);
    }
    // if operation fails
    catch (OrganizationManagerException e) {
      throw TaskException.general(e);
    }
    // for any other reason
    catch (Exception e) {
      throw TaskException.unhandled(e);
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }
}