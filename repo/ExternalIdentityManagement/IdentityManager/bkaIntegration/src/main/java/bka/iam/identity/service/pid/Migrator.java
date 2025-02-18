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
    Subsystem   :   Identity Governance Services Integration

    File        :   Migrator.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Migrator.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      12.07.2022  Dsteding    First release version
*/

package bka.iam.identity.service.pid;

import java.util.Map;
import java.util.List;
import java.util.HashMap;

import java.util.concurrent.TimeUnit;

import java.sql.Connection;

import javax.security.auth.login.LoginException;

import oracle.iam.identity.usermgmt.api.UserManagerConstants;

import oracle.iam.platform.entitymgr.EntityManager;
import oracle.iam.platform.entitymgr.ProviderException;
import oracle.iam.platform.entitymgr.StaleEntityException;
import oracle.iam.platform.entitymgr.NoSuchEntityException;
import oracle.iam.platform.entitymgr.InvalidDataTypeException;
import oracle.iam.platform.entitymgr.UnknownAttributeException;
import oracle.iam.platform.entitymgr.InvalidDataFormatException;
import oracle.iam.platform.entitymgr.UnsupportedOperationException;

import oracle.hst.foundation.SystemWatch;
import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.SystemThreadPool;

import oracle.hst.foundation.object.Pair;

import oracle.hst.foundation.utility.DateUtility;
import oracle.hst.foundation.utility.CollectionUtility;

import oracle.hst.foundation.logging.Logger;

import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskAttribute;
import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.persistence.DatabaseEntity;
import oracle.iam.identity.foundation.persistence.DatabaseFilter;
import oracle.iam.identity.foundation.persistence.DatabaseSearch;
import oracle.iam.identity.foundation.persistence.DatabaseConnection;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.AbstractSchedulerWorker;

import oracle.iam.identity.foundation.reconciliation.AbstractReconciliationTask;

import bka.iam.identity.rest.ServiceResource;

import bka.iam.identity.pid.Context;

////////////////////////////////////////////////////////////////////////////////
// class Migrator
// ~~~~~ ~~~~~~~~
/**
 ** The <code>Migrator</code> requests an <code>Anonymous Identifier</code>
 ** from the central deployed service and assigns the value retrieved at an
 ** identity profile attribute configured.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Migrator extends AbstractReconciliationTask {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute to advise which prefix should be send to the Service Provider
   ** for an <code>Anonymous Identifier</code> to generate.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String          PREFIX           = "Prefix";
  /**
   ** Attribute to advise which identity profile attribute will receive the
   ** generated value for an <code>Anonymous Identifier</code>.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String          PROFILE         = "Profile";

  /** the category of the logging facility to use */
  private static final String          LOGGER_CATEGORY = "BKA.SYSTEM.PID";

  /**
   ** the vector with attributes which are valid to define on the task
   ** definition
   */
  private static final TaskAttribute[] attributes      = {
    /** the task attribute IT Resource */
    TaskAttribute.build(IT_RESOURCE, TaskAttribute.MANDATORY)
    /** the task attribute Prefix */
  , TaskAttribute.build(PREFIX,      TaskAttribute.MANDATORY)
    /** the task attribute Profile */
  , TaskAttribute.build(PROFILE,     TaskAttribute.MANDATORY)
  };

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the abstraction layer to describe the connection to the target system */
  protected Context                    context;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Worker
  // ~~~~~ ~~~~~~
  /**
   ** The <code>Worker</code> implements the functionality to request the
   ** password for users provided in the bulk of identities passed to the
   ** contructor.
   */
  private class Worker extends AbstractSchedulerWorker {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final boolean                   simulate;
    private final List<Map<String, Object>> bulk;

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
     ** @param  bulk             the collection of identities that needs an
     **                          <code>Anonymous Identifier</code> assigned too.
     **                          <br>
     **                          Allowed object is {@link List} where each
     **                          element is of type {@link Map}.
     ** @param  simulate         <code>true</code> if the external service to
     **                          generate an <code>Anonymous Identifier</code>
     **                          should be invoked.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     **
     ** @throws TaskException    if implementing subclass is not able to
     **                          initialize correctly.
     ** @throws LoginException   if there is an error during login.
     */
    private Worker(final Logger logger, final SystemWatch watch, final List<Map<String, Object>> bulk, final boolean simulate)
      throws LoginException
      ,      TaskException {

      // ensure inheritance
      super(logger, watch);

      // initialize instance attributes
      this.bulk     = bulk;
      this.simulate = simulate;
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

      // use the EntityManager to avoid triggering potential EventHandlers that
      // are doing the same stuff on regular bases
      final EntityManager manager = service(EntityManager.class);
      info(TaskBundle.format(TaskMessage.RECONCILE_BEGIN, reconcileObject()));
      info(TaskBundle.format(TaskMessage.WILLING_TO_CHANGE, String.valueOf(this.bulk.size())));
      try {
        for (Map<String, Object> cursor : this.bulk) {
          // may be not required to check again but don't care
          if (cursor.get("pid") != null)
            continue;
          if (!this.simulate) {
            try {
              manager.modifyEntity(UserManagerConstants.USER_ENTITY, String.valueOf(cursor.get("key")), createIdentifier((String)cursor.get("log")));
              incrementSuccess();
            }
            catch (ProviderException | NoSuchEntityException | UnknownAttributeException | UnsupportedOperationException e) {
              incrementFailed();
              throw TaskException.general(e);
            }
            catch (StaleEntityException | InvalidDataTypeException | InvalidDataFormatException e) {
              incrementFailed();
              fatal(method, e);
            }
          }
          else {
            incrementIgnored();
          }
        }
      }
      finally {
        timerStop(method);
        info(TaskBundle.format(TaskMessage.RECONCILE_COMPLETE, reconcileObject()));
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
   ** Constructs an empty <code>Migrator</code> scheduler instance that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Migrator() {
    // ensure inheritance
    super(LOGGER_CATEGORY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prefix
  /**
   ** Returns the prefix for the generated value of an
   ** <code>Anonymous Identifier</code>.
   **
   ** @return                    the prefix for the generated value of an
   **                            <code>Anonymous Identifier</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  protected final String prefix() {
    return stringValue(PREFIX);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   profile
  /**
   ** Returns the name of the profile attribute that will receive the generated
   ** value for an <code>Anonymous Identifier</code>.
   **
   ** @return                    the name of the profile attribute that will
   **                            receive the generated value for an
   **                            <code>Anonymous Identifier</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  protected final String profile() {
    return stringValue(PROFILE);
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

    info(TaskBundle.format(TaskMessage.RECONCILIATION_BEGIN, reconcileObject(), getName(), resourceName()));
    // set the current date as the timestamp on which this task has been
    // last reconciled at the start of execution setting it here to have it
    // the next time this scheduled task will run the changes made during
    // the execution of this task updating this attribute will not perform
    // to write it back to the scheduled job attributes it's still in
    // memory; updateLastReconciled() will persist the change that we do
    // here only if the job completes successful
    lastReconciled(DateUtility.now());

    final DatabaseSearch search     = DatabaseSearch.build(
      this
    , DatabaseEntity.build(null, "usr", "usr_key")
    , DatabaseFilter.build(
        // pick every user that don't have an anonymous identifier yet
        DatabaseFilter.build("usr_udf_anonymized", (String)null, DatabaseFilter.Operator.EQUAL)
        // but exclude any well know system user
      , DatabaseFilter.build("usr_key"           , 10,           DatabaseFilter.Operator.GREATER_THAN)
      , DatabaseFilter.Operator.AND
      )
    , CollectionUtility.<Pair<String, String>>list(
        Pair.<String, String>of("usr_key",            "key")
      , Pair.<String, String>of("usr_login",          "log")
      , Pair.<String, String>of("usr_udf_anonymized", "pid")
      )
    );

    // creating the ThreadPoolExecutor
    final int              maximum    = integerValue(THREAD_POOL_SIZE, THREAD_POOL_SIZE_DEFAULT);
    final int              capacity   = maximum  / 4;
    final SystemThreadPool threadpool = new SystemThreadPool(
      SystemThreadPool.Config.build().capacity(capacity).minimum(capacity * 2).maximum(maximum).build()
    , new WorkerThreadFactory()
    );

    final Batch               batch      = new Batch(batchSize());
    final Connection          connection = DatabaseConnection.aquire();
    List<Map<String, Object>> result     = null;
    try {
      search.prepare(connection);
      do {
        info(TaskBundle.string(TaskMessage.COLLECTING_BEGIN));
        result = search.execute(batch.start(), batch.end());
        info(TaskBundle.string(TaskMessage.COLLECTING_COMPLETE));
        if (result != null && result.size() > 0) {
          // submit work to the thread pool
          threadpool.submit(new Worker(logger(), this.watch, result, booleanValue(GATHERONLY)));
        }
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
        warning(TaskBundle.stringFormat(TaskMessage.RECONCILIATION_STOPPED, reconcileObject(), getName(), resourceName(), "success"));
      }
      else {
        info(TaskBundle.format(TaskMessage.RECONCILIATION_COMPLETE, reconcileObject(), getName(), resourceName()));
      }
    }
    // in any case of an unhandled exception
    catch (LoginException e) {
      warning(TaskBundle.stringFormat(TaskMessage.RECONCILIATION_STOPPED, reconcileObject(), getName(), resourceName(), e.getMessage()));
      throw TaskException.abort(e);
    }
    // in any case of an unhandled exception
    catch (TaskException e) {
      warning(TaskBundle.stringFormat(TaskMessage.RECONCILIATION_STOPPED, reconcileObject(), getName(), resourceName(), e.getMessage()));
      throw e;
    }
    finally {
      search.close();
      DatabaseConnection.release(connection);
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateResource  (Reconciliation)
  /**
   ** Initalize the IT Resource capabilities.
   ** <p>
   ** The approach implementing this method should be:
   ** <ol>
   **   <li>If only  Application Name is configured as job parameter and
   **       IT Resource or Reconciliation Object is not configured, then
   **       IT Resource and Reconciliation Object will be fetched mapped with
   **       the given application Name
   **   <li>If IT Resource and Reconciliation Object are configured as job
   **       parameter then they gets priority even application name is also
   **       configured as job parameter
   **   <li>getParameter method logic handle the precedence logic of for cases
   **       mentioned in point #1 &amp; 2
   ** </ol>
   **
   ** @return                    a {@link ServiceResource} populated and
   **                            validated.
   **                            <br>
   **                            Possible object is {@link ServiceResource}.
   **
   ** @throws TaskException      if the initialization of the
   **                            <code>IT Resource</code> fails.
   */
  protected ServiceResource populateResource()
    throws TaskException {

    final String method  = "populateResource";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    try {
      return ServiceResource.build(this, resourceName());
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   beforeExecution (overridden)
  /**
   ** The call back method just invoked before reconciliation takes place.
   ** <br>
   ** Default implementation does nothing.
   **
   ** @throws TaskException      in case an error does occur.
   */
  @Override
  protected void beforeExecution()
    throws TaskException {

    // ensure inheritance
    super.beforeExecution();

    final String method = "beforeExecution";
    trace(method, SystemMessage.METHOD_ENTRY);
    // start the task to gather performance metrics
    timerStart(method);
    try {
      // configure the REST service to obtain a anonymous identifier per identity
      this.context = Context.build(this, populateResource());
    }
    finally {
      // stop the task timer from gathering performance metrics
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createIdentifier
  /**
   ** Build and executes the request to create the provided new anonymous
   ** identifier at the Service Provider.
   **
   ** @param  usedby             the user resource to create the anonymous
   **                            identifier for.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the successfully created anonymous
   **                            identifier.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws TaskException      if the REST service provider responded with an
   **                            error.
   */
  protected HashMap<String, Object> createIdentifier(final String usedby)
    throws TaskException {

    final String method  = "createIdentifier";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    final HashMap<String, Object> attribute = new HashMap<String, Object>();
    try {
      attribute.put(profile(), this.context.createIdentifier(prefix(), usedby));
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return attribute;
  }
}