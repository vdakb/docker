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

    File        :   Evaluate.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Evaluate.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      12.07.2022  Dsteding    First release version
*/

package bka.iam.identity.service.pol;

import java.util.Set;
import java.util.List;
import java.util.HashMap;

import java.util.concurrent.TimeUnit;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import javax.security.auth.login.LoginException;

import oracle.iam.platform.Platform;

import oracle.iam.platform.entitymgr.vo.SearchCriteria;

import oracle.iam.identity.usermgmt.vo.User;

import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.api.UserManagerConstants;

import oracle.iam.identity.orgmgmt.vo.Organization;

import oracle.iam.identity.orgmgmt.api.OrganizationManager;
import oracle.iam.identity.orgmgmt.api.OrganizationManagerConstants;

import oracle.iam.identity.exception.UserSearchException;
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
 ** The <code>Evaluate</code> change the flag on identities that requires
 ** evaluation of role assignments.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Evaluate extends AbstractReconciliationTask {

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
   ** the vector with attributes which are valid to define on the task
   ** definition
   */
  private static final TaskAttribute[] attributes      = {
    TaskAttribute.build(USER,         TaskAttribute.OPTIONAL)
    /** The task attribute Passsword */
  , TaskAttribute.build(ORGANIZATION, TaskAttribute.OPTIONAL)
  };

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Evaluator
  // ~~~~~ ~~~~~~~~
  /**
   ** The <code>Evaluator</code> implements the functionality to change the
   **flag on identities that requires evaluation of role assignments.
   */
  private class Evaluator extends AbstractSchedulerWorker {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final boolean    simulate;
    private final List<User> bulk;

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
     ** @param  simulate         <code>true</code> if no password change should
     **                          be applied.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     **
     ** @throws TaskException    if implementing subclass is not able to
     **                          initialize correctly.
     ** @throws LoginException   if there is an error during login.
     */
    private Evaluator(final Logger logger, final SystemWatch watch, final List<User> bulk, final boolean simulate)
      throws LoginException
      ,      TaskException {
  
      // ensure inheritance
      super(logger, watch);

      // initialize instance attributes
      this.bulk         = bulk;
      this.simulate     = simulate;
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
      Connection        connection = null;
      PreparedStatement statement  = null;
      try {
        // use the Platform DataSource to apply the required changes
        connection = Platform.getOperationalDS().getConnection();
        statement  = connection.prepareStatement("UPDATE user_provisioning_attrs SET policy_eval_needed = 1, policy_eval_in_progress = 0 WHERE usr_key = ?");
        for (User user : this.bulk) {
          if (!this.simulate) {
            warning(method, "Initiate evaluate policies for [" + user.getLogin() + "].");
            statement.setString(1, user.getEntityId());
            statement.execute();
            incrementSuccess();
            warning(method, "Evaluate policies for [" + user.getLogin() + "] initiated.");
          }
          else {
            incrementIgnored();
            warning(method, "Evaluate policies for " + user.getLogin() + " skipped due to user request");
          }
        }
        connection.commit();
      }
      catch (SQLException e) {
        incrementFailed();
        fatal(method, e);
        try {
          connection.rollback();
        }
        catch (SQLException x) {
          fatal(method, x);
        }
      }
      finally {
        if (statement != null) {
          try {
            statement.close();
          }
          catch (SQLException x) {
            fatal(method, x);
          }
        }
        if (connection != null) {
          try {
            connection.close();
          }
          catch (SQLException x) {
            fatal(method, x);
          }
        }
        timerStop(method);
        info(TaskBundle.format(TaskMessage.RECONCILE_COMPLETE, UserManagerConstants.USER_ENTITY));
        info(TaskBundle.format(TaskMessage.ABLE_TO_CHANGE, summary().asStringArray()));
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Evaluate</code> scheduler instance that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Evaluate() {
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
    final UserManager             facade     = service(UserManager.class);
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
        threadpool.submit(new Evaluator(logger(), this.watch, result, booleanValue(GATHERONLY)));
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