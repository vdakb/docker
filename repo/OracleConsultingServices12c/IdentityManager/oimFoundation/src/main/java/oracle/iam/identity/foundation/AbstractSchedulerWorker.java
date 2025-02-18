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

    Copyright © 2006. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Scheduler Facilities

    File        :   AbstractSchedulerWorker.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractSchedulerWorker.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2008-03-17  DSteding    First release version
*/

package oracle.iam.identity.foundation;

import java.io.Serializable;

import javax.security.auth.login.LoginException;

import Thor.API.Operations.tcPropertyOperationsIntf;
import Thor.API.Operations.tcLookupOperationsIntf;
import Thor.API.Operations.tcITResourceInstanceOperationsIntf;
import Thor.API.Operations.tcGroupOperationsIntf;
import Thor.API.Operations.tcOrganizationOperationsIntf;
import Thor.API.Operations.tcUserOperationsIntf;
import Thor.API.Operations.tcObjectOperationsIntf;
import Thor.API.Operations.tcProvisioningOperationsIntf;
import Thor.API.Operations.tcFormDefinitionOperationsIntf;
import Thor.API.Operations.tcFormInstanceOperationsIntf;

import com.thortech.xl.dataaccess.tcDataProvider;

import oracle.iam.platform.OIMInternalClient;

import oracle.hst.foundation.SystemWatch;

import oracle.hst.foundation.logging.Logger;

import oracle.iam.identity.foundation.resource.TaskBundle;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractSchedulerWorker
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>AbstractSchedulerWorker</code> implements the base logging of a
 ** {@link Runnable} started as a thread by a scheduled job.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public abstract class AbstractSchedulerWorker implements Runnable
                                              ,          AbstractServiceTask {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** these are for convenience the avoid a specific getter for this instance
   ** attributes.
   ** <p>
   ** Subclasses must not change these instance attributes hence they are
   ** declared final.
   */
  protected final Logger            logger;
  protected final String            prefix;
  protected final OIMInternalClient locator;

  /** the system watch to gather performance metrics */
  protected final SystemWatch       watch;

  protected final Summary           summary = new Summary();

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Summary
  // ~~~~~ ~~~~~~~
  /**
   ** The <code>Summary</code> implements the reporting functionality of a bulk
   ** reconciliation result set.
   */
  public static class Summary implements Serializable {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:-5062687268327880191")
    private static final long serialVersionUID = 6389217446142058071L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** The amount of bulk entries where reconcililiation has been succeeded. */
    protected int             success          = 0;

    /** The amount of bulk entries where reconcililiation has been failed. */
    protected int             failed           = 0;

    /** The amount of bulk entries where reconcililiation has been ignored. */
    protected int             ignored          = 0;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Summary</code>.
     */
    public Summary() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Acccessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: success
    /**
     ** Returns the amount of bulk entries where reconcililiation has been
     ** succeeded.
     **
     ** @return                  the amount of bulk entries where
     **                          reconcililiation has been succeeded.
     */
    public final int success() {
      return this.success;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: failed
    /**
     ** Returns the amount of bulk entries where reconcililiation has been
     ** failed.
     **
     ** @return                  the amount of bulk entries where
     **                          reconcililiation has been failed.
     */
    public final int failed() {
      return this.failed;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: ignored
    /**
     ** Returns the amount of bulk entries where reconcililiation has been
     ** ignored.
     **
     ** @return                  the amount of bulk entries where
     **                          reconcililiation has been ignored.
     */
    public final int ignored() {
      return this.ignored;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: asStringArray
    /**
     ** Returns the metrice gathered so far as an array of string for debugging
     ** puprpose. The array returned the metrics as
     ** <pre>
     **   array[0] --&gt; success
     **   array[1] --&gt; ignored
     **   array[2] --&gt; failed
     **</pre>
     **
     ** @return                  the metrice gathered so far as an array of
     **                          string for debugging puprpose.
     */
    public final String[] asStringArray() {
      return new String[] {
        String.valueOf(this.success)
      , String.valueOf(this.ignored)
      , String.valueOf(this.failed)
      };
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AbstractSchedulerThread</code> which is instanciated
   ** by the specified {@link AbstractSchedulerTask} <code>task</code>.
   **
   ** @param  logger             the {@link Logger} this {@link Runnable} use
   **                            for logging purpose.
   ** @param  watch              the {@link SystemWatch} to collect the
   **                            performance metrics.
   **
   ** @throws LoginException     if there is an error during login.
   ** @throws TaskException      if implementing subclass is not able to
   **                            initialize correctly.
   */
  protected AbstractSchedulerWorker(final Logger logger, final SystemWatch watch)
    throws LoginException
    ,      TaskException {

    // ensure inheritance
    super();

    // initialize instance
    this.logger  = logger;
    this.prefix  = this.getClass().getName();
    this.watch   = watch;
    this.locator = legacyLogin();

    // delegate all futher initialization action to the implementing subclass
    initialize();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   summary
  /**
   ** Returns the reporting summary this thread has gathered so far.
   **
   ** @return                    the reporting summary this thread has gathered
   **                            so far.
   */
  public final Summary summary() {
    return this.summary;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   success
  /**
   ** Returns the amount of bulk entries where reconcililiation has been
   ** succeeded.
   **
   ** @return                    the amount of bulk entries where
   **                            reconcililiation has been succeeded.
   */
  public final int success() {
    return this.summary.success;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   failed
  /**
   ** Returns the amount of bulk entries where reconcililiation has been failed.
   **
   ** @return                    the amount of bulk entries where
   **                            reconcililiation has been failed.
   */
  public final int failed() {
    return this.summary.failed;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ignored
  /**
   ** Returns the amount of bulk entries where reconcililiation has been
   ** ignored.
   **
   ** @return                    the amount of bulk entries where
   **                            reconcililiation has been ignored.
   */
  public final int ignored() {
    return this.summary.ignored;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  incrementSuccess
  /**
   ** Increments the amount of bulk entries where reconcililiation has been
   ** succeeded by <code>1</code>.
   */
  public final void incrementSuccess() {
    this.summary.success++;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  incrementSuccess
  /**
   ** Increments the amount of bulk entries where reconcililiation has been
   ** succeeded.
   **
   ** @param  amount             the amount of bulk entries where
   **                            reconcililiation has been succeeded.
   */
  public final void incrementSuccess(final int amount) {
    this.summary.success += amount;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  decrementSuccess
  /**
   ** Decrements the amount of bulk entries where reconcililiation has been
   ** succeeded by <code>1</code>.
   */
  public final void decrementSuccess() {
    this.summary.success--;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  incrementSuccess
  /**
   ** Decrements the amount of bulk entries where reconcililiation has been
   ** succeeded.
   **
   ** @param  amount             the amount of bulk entries where
   **                            reconcililiation has been succeeded.
   */
  public final void decrementSuccess(final int amount) {
    this.summary.success += amount;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  incrementIgnored
  /**
   ** Increments the amount of bulk entries where reconcililiation has been
   ** ignored by <code>1</code>.
   */
  public final void incrementIgnored() {
    this.summary.ignored++;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  incrementIgnored
  /**
   ** Increments the amount of bulk entries where reconcililiation has been
   ** ignored.
   **
   ** @param  amount             the amount of bulk entries where
   **                            reconcililiation has been ignored.
   */
  public final void incrementIgnored(final int amount) {
    this.summary.ignored += amount;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  decrementIgnored
  /**
   ** Decrements the amount of bulk entries where reconcililiation has been
   ** ignored by <code>1</code>.
   */
  public final void decrementIgnored() {
    this.summary.ignored--;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  decrementIgnored
  /**
   ** Decrements the amount of bulk entries where reconcililiation has been
   ** ignored.
   **
   ** @param  amount             the amount of bulk entries where
   **                            reconcililiation has been ignored.
   */
  public final void decrementIgnored(final int amount) {
    this.summary.ignored -= amount;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  incrementFailed
  /**
   ** Increments the amount of bulk entries where reconcililiation has been
   ** failed by <code>1</code>.
   */
  public final void incrementFailed() {
    this.summary.failed++;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  incrementFailed
  /**
   ** Increments the amount of bulk entries where reconcililiation has been
   ** failed.
   **
   ** @param  amount             the amount of bulk entries where
   **                            reconcililiation has been failed.
   */
  public final void incrementFailed(final int amount) {
    this.summary.failed += amount;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  decrementFailed
  /**
   ** Decrements the amount of bulk entries where reconcililiation has been
   ** failed by <code>1</code>.
   */
  public final void decrementFailed() {
    this.summary.failed--;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  incrementFailed
  /**
   ** Decrements the amount of bulk entries where reconcililiation has been
   ** failed.
   **
   ** @param  amount             the amount of bulk entries where
   **                            reconcililiation has been failed.
   */
  public final void decrementFailed(final int amount) {
    this.summary.failed -= amount;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   logger (Loggable)
  /**
   ** Returns the logger associated with the scheduled job that instantiated
   ** this {@link Runnable}.
   **
   ** @return                    the logger associated with the scheduled job
   **                            that instantiated this {@link Runnable}.
   */
  @Override
  public final Logger logger() {
    return this.logger;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fatal (Loggable)
  /**
   ** Logs an fatal error to the associated <code>Logger</code>.
   **
   ** @param  method             the name of the method where the logging event
   **                            was occurred.
   ** @param  what               the exception as the reason to log.
   */
  @Override
  public final void fatal(final String method, final Throwable what) {
    this.logger.fatal(this.prefix, method, what);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error (Loggable)
  /**
   ** Logs an normal error to the associated <code>Logger</code>.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   ** @param  message            the message to log.
   */
  @Override
  public final void error(final String method, final String message) {
    this.logger.error(this.prefix, method, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warning (Loggable)
  /**
   ** Logs an warning error to the associated <code>Logger</code>.
   **
   ** @param  message            the message to log.
   */
  @Override
  public final void warning(final String message) {
    this.logger.warn(message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warning (Loggable)
  /**
   ** Logs an normal error to the associated <code>Logger</code>.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   ** @param  message            the message to log.
   */
  @Override
  public final void warning(final String method, final String message) {
    this.logger.warn(this.prefix, method, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   info (Loggable)
  /**
   ** Writes an informational message to the associated <code>Logger</code>.
   **
   ** @param  message            the message to log.
   */
  @Override
  public final void info(final String message) {
    this.logger.info(message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   debug (Loggable)
  /**
   ** Writes a debug message to the associated <code>Logger</code>.
   **
   ** @param  method             the name of the method where the logging event
   **                            was occurred.
   ** @param  message            the message to log.
   */
  @Override
  public final void debug(final String method, final String message) {
    this.logger.debug(this.prefix, method, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trace (Loggable)
  /**
   ** Writes a trace message to the associated <code>Logger</code>.
   **
   ** @param  method             the name of the method where the logging event
   **                            was occurred.
   ** @param  message            the message to log.
   */
  @Override
  public final void trace(final String method, final String message) {
    this.logger.trace(this.prefix, method, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   propertyFacade (AbstractTask)
  /**
   ** Returns an instance of the Business Facade instance for system
   ** configuration purpose associated with the scheduled job that instantiated
   ** this {@link Runnable}.
   ** <br>
   ** This implementation will stop the execution if the Business Facade cannot
   ** be instantiated. An appropriate message will be logged in the Application
   ** Server Log in this case.
   **
   ** @return                    an instance of the Business Facade instance for
   **                            system configuration purpose.
   */
  @Override
  public final tcPropertyOperationsIntf propertyFacade() {
    return service(tcPropertyOperationsIntf.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupFacade (AbstractTask)
  /**
   ** Returns an instance of a Business Facade instance for
   ** <code>Lookup Definition</code>s associated associated with the scheduled
   ** job that instantiated this {@link Runnable}.
   ** <br>
   ** This implementation will stop the execution if the Business Facade cannot
   ** be instantiated. An appropriate message will be logged in the Application
   ** Server Log in this case.
   **
   ** @return                    an instance of an <code>Lookup Definition</code>
   **                            class associated with the scheduled job.
   */
  @Override
  public final tcLookupOperationsIntf lookupFacade() {
    return service(tcLookupOperationsIntf.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceFacade (AbstractTask)
  /**
   ** Returns an instance of an <code>IT Resource</code> class associated with
   ** the scheduled job that instantiated this {@link Runnable}.
   ** <br>
   ** This implementation will stop the execution if the utility class cannot
   ** be instantiated. An appropriate message will be logged in the Application
   ** Server Log in this case.
   **
   ** @return                    an instance of an <code>IT Resource</code>
   **                            class associated with the scheduled job.
   */
  @Override
  public final tcITResourceInstanceOperationsIntf resourceFacade() {
    return service(tcITResourceInstanceOperationsIntf.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   groupFacade (AbstractTask)
  /**
   ** Returns an instance of an Business Facade instance for Oracle Identity
   ** Manager Group associatiated with the scheduled job that instantiated this
   ** {@link Runnable}.
   **
   ** @return                    an instance of an <code>UserGroup</code>
   **                            class associated with the scheduled job.
   */
  @Override
  public final tcGroupOperationsIntf groupFacade() {
    return service(tcGroupOperationsIntf.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationFacade (AbstractTask)
  /**
   ** Returns an instance of a Business Facade instance for Oracle Identity
   ** Manager Organization associatiated with the scheduled job that
   ** instantiated this {@link Runnable}.
   ** <br>
   ** This implementation will stop the execution if the Business Facade cannot
   ** be instantiated. An appropriate message will be logged in the Application
   ** Server Log in this case.
   **
   ** @return                    an instance of an <code>Organization</code>
   **                            class associated with the scheduled job.
   */
  @Override
  public final tcOrganizationOperationsIntf organizationFacade() {
    return service(tcOrganizationOperationsIntf.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userFacade (AbstractTask)
  /**
   ** Returns an instance of an Business Facade instance for Oracle Identity
   ** Manager User associatiated with the scheduled job that instantiated this
   ** {@link Runnable}.
   ** <br>
   ** This implementation will stop the execution if the Business Facade cannot
   ** be instantiated. An appropriate message will be logged in the Application
   ** Server Log in this case.
   **
   ** @return                    an instance of an <code>User</code>
   **                            class associated with the scheduled job.
   */
  @Override
  public final tcUserOperationsIntf userFacade() {
    return service(tcUserOperationsIntf.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectFacade (AbstractTask)
  /**
   ** Returns an instance of a Business Facade instance for Objects
   ** associatiated with the scheduled job that instantiated this
   ** {@link Runnable}.
   ** <br>
   ** This implementation will stop the execution if the Business Facade cannot
   ** be instantiated. An appropriate message will be logged in the Application
   ** Server Log in this case.
   **
   ** @return                    an instance of an <code>Resource Object</code>
   **                            class associated with the scheduled job.
   */
  @Override
  public final tcObjectOperationsIntf objectFacade() {
    return service(tcObjectOperationsIntf.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processFacade (AbstractTask)
  /**
   ** Returns an instance of a Business Facade instance for Processes
   ** associatiated with the scheduled job that instantiated this
   ** {@link Runnable}.
   ** <br>
   ** This implementation will stop the execution if the Business Facade cannot
   ** be instantiated. An appropriate message will be logged in the Application
   ** Server Log in this case.
   **
   ** @return                    an instance of an <code>Provisioning</code>
   **                            class associated with the scheduled job.
   */
  @Override
  public final tcProvisioningOperationsIntf processFacade() {
    return service(tcProvisioningOperationsIntf.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formDefinitionFacade (AbstractTask)
  /**
   ** Returns an instance of a Business Facade instance for Form Instances
   ** (Process Data) associatiated with the scheduled job that instantiated this
   ** {@link Runnable}.
   ** <br>
   ** This implementation will stop the execution if the Business Facade cannot
   ** be instantiated. An appropriate message will be logged in the Application
   ** Server Log in this case.
   **
   ** @return                    an instance of an <code>Form Definition</code>
   **                            class associated with the scheduled job.
   */
  @Override
  public final tcFormDefinitionOperationsIntf formDefinitionFacade() {
    return service(tcFormDefinitionOperationsIntf.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formInstanceFacade (AbstractTask)
  /**
   ** Returns an instance of a Business Facade instance for Form Instances
   ** (Process Data) associatiated with the scheduled job that instantiated this
   ** {@link Runnable}.
   ** <br>
   ** This implementation will stop the execution if the Business Facade cannot
   ** be instantiated. An appropriate message will be logged in the Application
   ** Server Log in this case.
   **
   ** @return                    an instance of an <code>Form Instance</code>
   **                            class associated with the scheduled job.
   */
  @Override
  public final tcFormInstanceOperationsIntf formInstanceFacade() {
    return service(tcFormInstanceOperationsIntf.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   service (AbstractTask)
  /**
   ** Returns an instance of a Business Facade by invoking the method platform
   ** service resolver to return the appropriate instance of the desired
   ** Business Facade.
   ** <br>
   ** The utility factory keeps track of created Business Facade and on
   ** execution of close() will free all aquired resources of the created
   ** Business Facade.
   ** <br>
   ** This implementation will stop the execution if the Business Facade cannot
   ** be instantiated. An appropriate message will be logged in the Application
   ** Server Log in this case.
   **
   ** @param  serviceClass       the class of the Business Facade to create.
   **                            Typically it will be of the sort:
   **                            <code>Thor.API.tcNameUtilityIntf.class</code>.
   **
   ** @return                    the Business Facade.
   **                            It needs not be cast to the requested Business
   **                            Facade.
   */
  @Override
  public final <T> T service(final Class<T> serviceClass) {
    return this.locator.getService(serviceClass);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   provider (AbstractServiceTask)
  /**
   ** Returns the session provider connection associated with this task.
   **
   ** @return                    the session provider connection associated with
   **                            this task.
   */
  @Override
  public final tcDataProvider provider() {
    throw new RuntimeException(TaskBundle.string(TaskError.NOTIMPLEMENTED));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   run (Runnable)
  /**
   ** When an object implementing interface <code>Runnable</code> is used to
   ** create a thread, starting the thread causes the object's <code>run</code>
   ** method to be called in that separately executing thread.
   ** <p>
   ** The general contract of the method <code>run</code> is that it may take
   ** any action whatsoever.
   */
  @Override
  public void run() {
    final String method = "run";
    try {
      beforeExecution();
      onExecution();
    }
    catch (TaskException e) {
      error(method, TaskBundle.format(TaskError.GENERAL, e));
    }
    catch (Exception e) {
      fatal(method, e);
    }
    finally {
      try {
        afterExecution();
      }
      catch (TaskException e) {
        error(method, TaskBundle.format(TaskError.GENERAL, e));
      }
      catch (Exception e) {
        fatal(method, e);
      }
      legacyLogout(this.locator);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   legacyLogin
  protected static OIMInternalClient legacyLogin()
    throws LoginException {

    final OIMInternalClient oimClient = new OIMInternalClient();
    oimClient.loginAsOIMInternal();
    return oimClient;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   legacyLogout
  /**
   ** Perform a logout on the client.
   **
   ** @param  client             the {@link OIMInternalClient} instance to log
   **                            out.
   */
  protected static void legacyLogout(final OIMInternalClient client) {
    client.logout();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize
  /**
   ** The call back method to initailize the instance.
   ** <br>
   ** Default implementation does nothing.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** This method is called from the constructor of this calls. Be aware that
   ** any instance attribute that is not initialized in an implementing subclass
   ** cannot be used in this method.
   **
   ** @throws TaskException      in case an error does occur.
   */
  protected void initialize()
    throws TaskException {

    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beforeExecution
  /**
   ** The call back method just invoked before execution happens.
   ** <br>
   ** Default implementation does nothing.
   **
   ** @throws TaskException      in case an error does occur.
   */
  protected void beforeExecution()
    throws TaskException {

    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onExecution
  /**
   ** The entry point of the scheduled task to perform.
   **
   ** @throws TaskException      in case an error does occur.
   */
  protected abstract void onExecution()
    throws TaskException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   afterExecution
  /**
   ** The call back method just invoked after execution finished.
   ** <br>
   ** Default implementation does nothing.
   **
   ** @throws TaskException      in case an error does occur.
   */
  protected void afterExecution()
    throws TaskException {

    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   timerStart
  /**
   ** Starts a timer for a named task.
   ** <p>
   ** The results are undefined if {@link #timerStop(String)} or timing methods
   ** are called without invoking this method.
   **
   ** @param  name               the name of the task to start.
   **
   ** @see    #timerStop(String)
   */
  protected void timerStart(final String name) {
    synchronized (this.watch) {
      // start collecting performance metrics of a task 
      this.watch.start(TaskBundle.location(Thread.currentThread(), this.prefix, name));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   timerStop
  /**
   ** Stops a timer for a named task.
   ** <p>
   ** The results are undefined if {@link #timerStop(String)} or timing methods
   ** are called without invoking this method.
   **
   ** @param  name               the name of the task to stop.
   **
   ** @see    #timerStart(String)
   */
  protected void timerStop(final String name) {
    synchronized (this.watch) {
      // stop collecting performance metrics of a task
      this.watch.stop(TaskBundle.location(Thread.currentThread(), this.prefix, name));
    }
  }
}