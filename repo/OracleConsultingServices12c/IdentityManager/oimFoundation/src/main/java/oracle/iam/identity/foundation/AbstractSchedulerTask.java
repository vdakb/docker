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

    File        :   AbstractSchedulerTask.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractSchedulerTask.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation;

import java.util.Date;
import java.util.HashMap;

import java.util.concurrent.ThreadFactory;

import java.util.concurrent.atomic.AtomicInteger;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.time.ZoneOffset;
import java.time.LocalDateTime;

import Thor.API.Operations.tcUserOperationsIntf;
import Thor.API.Operations.tcObjectOperationsIntf;
import Thor.API.Operations.tcGroupOperationsIntf;
import Thor.API.Operations.tcLookupOperationsIntf;
import Thor.API.Operations.tcPropertyOperationsIntf;
import Thor.API.Operations.tcOrganizationOperationsIntf;
import Thor.API.Operations.tcFormInstanceOperationsIntf;
import Thor.API.Operations.tcProvisioningOperationsIntf;
import Thor.API.Operations.tcFormDefinitionOperationsIntf;
import Thor.API.Operations.tcITResourceInstanceOperationsIntf;

import com.thortech.xl.dataaccess.tcDataProvider;

import com.thortech.xl.scheduler.tasks.SchedulerBaseTask;

import oracle.mds.core.MDSSession;
import oracle.mds.core.SessionOptions;
import oracle.mds.core.IsolationLevel;

import oracle.iam.platform.Platform;

import oracle.iam.scheduler.vo.JobDetails;
import oracle.iam.scheduler.vo.JobParameter;

import oracle.iam.scheduler.api.SchedulerService;

import oracle.iam.scheduler.exception.SchedulerException;
import oracle.iam.scheduler.exception.SchedulerAccessDeniedException;

import oracle.hst.foundation.SystemWatch;
import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.logging.Logger;

import oracle.hst.foundation.utility.ClassUtility;
import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.resource.TaskBundle;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractSchedulerTask
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>AbstractSchedulerTask</code> implements the base functionality of
 ** a service end point for the Oracle Identity Manager Scheduler.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
@SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
public abstract class AbstractSchedulerTask extends    SchedulerBaseTask
                                            implements AbstractServiceTask
                                            ,          AbstractMetadataTask {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which can be defined on this task to hold the timestamp
   ** format to convert the last execution timestamp.
   */
  public static final String     TIMESTAMP_FORMAT = "Timestamp Format";

  /**
   ** Attribute tag which can be defined on this task to hold the name of the
   ** job to start after this task has completed succesfully.
   */
  public static final String     DEPENDENT_JOB    = "Dependent Job";

  /** the status indicating a job is running */
  public static final int        STOPPED          = 2;

  /** the status indicating a job is running */
  public static final int        RUNNING          = 5;

  /** the status indicating a job is interupted */
  static final int               INTERRUPTED      = 7;

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  protected static LocalDateTime ZULU             = LocalDateTime.of(1990, 1 , 1, 0, 0, 0);

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
  protected final Logger         logger;
  protected final String         prefix;
  protected final tcDataProvider provider;

  /** the system watch to gather performance metrics */
  protected SystemWatch          watch;

  /**
   ** since 11g seems to be we have to handle the request to stop the task by
   ** our own responsibility
   */
  private boolean                stopping           = false;

  private SimpleDateFormat       timestampFormatter = null;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class WorkerThreadFactory
  // ~~~~~ ~~~~~~~~~~~~~~~~~~~
  /**
   ** The default thread factory
   */
  protected static class WorkerThreadFactory implements ThreadFactory {

    ////////////////////////////////////////////////////////////////////////////
    // static attributes
    ////////////////////////////////////////////////////////////////////////////

    static final AtomicInteger pool   = new AtomicInteger(1);

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    final ThreadGroup          group;
    final String               name;
    final AtomicInteger        number = new AtomicInteger(1);

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>WorkerThreadFactory</code>.
     */
    public WorkerThreadFactory() {
      final Thread current = Thread.currentThread();
      this.group = current.getThreadGroup();
      this.name  = String.format("%s::%d", current.getName(), pool.getAndIncrement());
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   newThread
    /**
     ** Constructs a new {@code Thread}. Initialize priority, name, daemon
     ** status, {@code ThreadGroup}, etc.
     **
     ** @param  worker           a {@link Runnable} to be executed by new thread
     **                          instance.
     **
     ** @return                  constructed thread, or <code>null</code> if the
     **                          request to create a thread is rejected.
     */
    @Override
    public Thread newThread(final Runnable worker) {
      final Thread thread = new Thread(this.group, worker, this.name + this.number.getAndIncrement(), 0);
      if (thread.isDaemon())
        thread.setDaemon(false);
      if (thread.getPriority() != Thread.NORM_PRIORITY)
        thread.setPriority(Thread.NORM_PRIORITY);
      return thread;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AbstractSchedulerTask</code> which is associated
   ** with the specified logging provider <code>loggable</code>.
   **
   ** @param  loggerCategory     the category for the Logger.
   */
  protected AbstractSchedulerTask(final String loggerCategory) {
    // ensure inheritance
    super();

    // initialize instance
    this.logger   = Logger.create(loggerCategory);
    this.prefix   = ClassUtility.shortName(this);
    this.provider = getDataBase();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   booleanValue
  /**
   ** Returns a <code>boolean</code> from the attribute mapping of this
   ** scheduled job.
   **
   ** @param  key                the key for the desired attribute string.
   **
   ** @return                    the boolean for the given key.
   **
   ** @throws ClassCastException if the object returned by the get operation on
   **                            the mappping is not castable to a
   **                            <code>String</code>.
   */
  public boolean booleanValue(final String key) {
    String result = parameter(key);
    // convert the yes/no semantic to the correct meaning for class Boolean
    if (SystemConstant.YES.equalsIgnoreCase(result))
      result = SystemConstant.TRUE;

    return Boolean.valueOf(result).booleanValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   booleanValue
  /**
   ** Returns a <code>boolean</code> from the attribute mapping of this
   ** scheduled job.
   **
   ** @param  key                the key for the desired attribute string.
   ** @param  defaultValue       the value that should be returned if no value
   **                            was found for the specified key.
   **
   ** @return                    the boolean for the given key or the default.
   **
   ** @throws ClassCastException if the object returned by the get operation on
   **                            the mappping is not castable to a
   **                            <code>String</code>.
   */
  public boolean booleanValue(final String key, final boolean defaultValue) {
    String result = parameter(key);
    if (StringUtility.isEmpty(result))
      return defaultValue;
    else {
      // convert the yes/no semantic to the correct meaning for class Boolean
      if (SystemConstant.YES.equalsIgnoreCase(result))
        result = SystemConstant.TRUE;

      return Boolean.valueOf(result).booleanValue();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   integerValue
  /**
   ** Returns a <code>int</code> from the attribute mapping of this scheduled
   ** job.
   **
   ** @param  key                the key for the desired attribute string.
   ** @param  defaultValue       the value that should be returned if no value
   **                            was found for the specified key.
   **
   ** @return                    the int for the given key or the default.
   **
   ** @throws ClassCastException if the object returned by the get operation on
   **                            the mappping is not castable to a
   **                            <code>String</code>.
   */
  public int integerValue(final String key, final int defaultValue) {
    int result = integerValue(key);
    return (result != Integer.MIN_VALUE) ? result : defaultValue;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   integerValue
  /**
   ** Returns a <code>int</code> from the attribute mapping of this scheduled
   ** job.
   **
   ** @param  key                the key for the desired attribute string.
   **
   ** @return                    the int for the given key.
   **
   ** @throws ClassCastException if the object returned by the get operation on
   **                            the mappping is not castable to a
   **                            <code>String</code>.
   */
  public int integerValue(final String key) {
    final String result = parameter(key);
    try {
      return (StringUtility.isEmpty(result)) ? Integer.MIN_VALUE : Integer.parseInt(result);
    }
    catch (NumberFormatException e) {
      return Integer.MIN_VALUE;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringValue
  /**
   ** Returns a <code>String</code> from the attribute mapping of this scheduled
   ** job.
   **
   ** @param  key                the key for the desired attribute string.
   **
   ** @return                    the <code>String</code> for the given key.
   **
   ** @throws ClassCastException if the object returned by the get operation on
   **                            the mappping is not castable to a
   **                            <code>String</code>.
   */
  public String stringValue(final String key) {
    return parameter(key);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parameter
  /**
   ** Sets a <code>String</code> object in the parameter mapping of this
   ** scheduled job.
   **
   ** @param  key                the key for the attribute string to set.
   ** @param  value              the value associated with the specified key
   */
  public void parameter(final String key, final String value) {
    getTaskAttributeMap().put(key, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parameter
  /**
   ** Returns a <code>String</code> object from the attribute mapping of
   ** this scheduled task.
   **
   ** @param  key                the key for the desired attribute string.
   **
   ** @return                    the string for the given key.
   **
   ** @throws ClassCastException if the object returned by the get operation on
   **                            the mappping is not castable to a
   **                            <code>String</code>.
   */
  protected String parameter(final String key) {
    return (String)getTaskAttributeMap().get(key);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   timestampFormatter
  /**
   ** Returns the {@link SimpleDateFormat} of this scheduled task.
   **
   ** @return                    the {@link SimpleDateFormat} of this scheduled task.
   */
  protected final SimpleDateFormat timestampFormatter() {
    return this.timestampFormatter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributes
  /**
   ** Returns the array with names which should be populated from the
   ** scheduled task definition of Oracle Identity Manager.
   **
   ** @return                    the array with names which should be populated.
   */
  protected abstract TaskAttribute[] attributes();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   timestampValue
  /**
   ** Sets a timestamp for an attribute specified by <code>attribute</code> in
   ** the attribute mapping of this scheduled job.
   **
   ** @param  attribute          the name of the attribute that build the
   **                            mapping key of the timstamp to set.
   ** @param  timeStamp          the timestamp to set.
   */
  public void timestampValue(final String attribute, final Date timeStamp) {
    parameter(attribute, this.timestampFormatter.format(timeStamp));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   timestampValue
  /**
   ** Returns a timestamp from an attribute specified by <code>attribute</code>
   ** in the attribute mapping of this scheduled job.
   **
   ** @param  attribute          the name of the attribute that build the
   **                            mapping key of the timstamp to return.
   **
   ** @return                    the timestamp the scheduled task has peformed
   **                            the last execution.
   */
  protected final Date timestampValue(final String attribute) {
    try {
      return this.timestampFormatter.parse(stringValue(attribute));
    }
    catch (ParseException e) {
      return Date.from(ZULU.toInstant(ZoneOffset.UTC));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   logger (Loggable)
  /**
   ** Returns the logger associated with this task.
   **
   ** @return                    the logger associated with this task.
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
   **                            had occurred.
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
   ** @param  method             the name of the method where the logging event
   **                            had occurred.
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
   ** @param  method             the name of the method where the logging event
   **                            had occurred.
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
   **                            had occurred.
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
   **                            had occurred.
   ** @param  message            the message to log.
   */
  @Override
  public final void trace(final String method, final String message) {
    this.logger.trace(this.prefix, method, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   propertyFacade (AbstractTask)
  /**
   ** Returns an instance of a Business Facade instance for System Configuration
   ** associated with this provisioning task.
   **
   ** @return                    a Business Facade instance.
   */
  @Override
  public final tcPropertyOperationsIntf propertyFacade() {
    return service(tcPropertyOperationsIntf.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupFacade (AbstractTask)
  /**
   ** Returns an instance of a Business Facade instance for Lookup Definitions
   ** associated with this scheduler task.
   ** <br>
   ** This implementation will stop the execution if the Business Facade cannot
   ** be instantiated. An appropriate message will be logged in the Application
   ** Server Log in this case.
   **
   ** @return                    the Business Facade instance.
   */
  @Override
  public final tcLookupOperationsIntf lookupFacade() {
    return service(tcLookupOperationsIntf.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceFacade (AbstractTask)
  /**
   ** Returns an instance of an IT Resource class associated with this scheduler
   ** task.
   ** <br>
   ** This implementation will stop the execution if the utility class cannot
   ** be instantiated. An appropriate message will be logged in the Application
   ** Server Log in this case.
   **
   ** @return                    the IT Resource class.
   */
  @Override
  public final tcITResourceInstanceOperationsIntf resourceFacade() {
    return service(tcITResourceInstanceOperationsIntf.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   groupFacade (AbstractTask)
  /**
   ** Returns an instance of an Business Facade instance for Oracle Identity
   ** Manager Group associatiated with this provisioning task.
   **
   ** @return                    a Business Facade instance.
   */
  @Override
  public final tcGroupOperationsIntf groupFacade() {
    return service(tcGroupOperationsIntf.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationFacade (AbstractTask)
  /**
   ** Returns an instance of a Business Facade instance for Oracle Identity
   ** Manager Organization associatiated with this scheduler task.
   ** <br>
   ** This implementation will stop the execution if the Business Facade cannot
   ** be instantiated. An appropriate message will be logged in the Application
   ** Server Log in this case.
   **
   ** @return                    a Business Facade instance.
   */
  @Override
  public final tcOrganizationOperationsIntf organizationFacade() {
    return service(tcOrganizationOperationsIntf.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userFacade (AbstractTask)
  /**
   ** Returns an instance of an Business Facade instance for Oracle Identity
   ** Manager User associatiated with this scheduler task.
   ** <br>
   ** This implementation will stop the execution if the Business Facade cannot
   ** be instantiated. An appropriate message will be logged in the Application
   ** Server Log in this case.
   **
   ** @return                    an Business Facade instance.
   */
  @Override
  public final tcUserOperationsIntf userFacade() {
    return service(tcUserOperationsIntf.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectFacade (AbstractTask)
  /**
   ** Returns an instance of a Business Facade instance for Objects
   ** associatiated with this scheduler task.
   ** <br>
   ** This implementation will stop the execution if the Business Facade cannot
   ** be instantiated. An appropriate message will be logged in the Application
   ** Server Log in this case.
   **
   ** @return                    a Business Facade instance.
   */
  @Override
  public final tcObjectOperationsIntf objectFacade() {
    return service(tcObjectOperationsIntf.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processFacade (AbstractTask)
  /**
   ** Returns an instance of a Business Facade instance for Processes
   ** associatiated with this scheduler task.
   ** <br>
   ** This implementation will stop the execution if the Business Facade cannot
   ** be instantiated. An appropriate message will be logged in the Application
   ** Server Log in this case.
   **
   ** @return                    a Business Facade instance.
   */
  @Override
  public final tcProvisioningOperationsIntf processFacade() {
    return service(tcProvisioningOperationsIntf.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formDefinitionFacade (AbstractTask)
  /**
   ** Returns an instance of a Business Facade instance for Form Instances
   ** (Process Data) associatiated this scheduler task.
   ** <br>
   ** This implementation will stop the execution if the Business Facade cannot
   ** be instantiated. An appropriate message will be logged in the Application
   ** Server Log in this case.
   **
   ** @return                    a Business Facade instance.
   */
  @Override
  public final tcFormDefinitionOperationsIntf formDefinitionFacade() {
    return service(tcFormDefinitionOperationsIntf.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formInstanceFacade (AbstractTask)
  /**
   ** Returns an instance of a Business Facade instance for Form Instances
   ** (Process Data) associatiated this scheduler task.
   ** <br>
   ** This implementation will stop the execution if the Business Facade cannot
   ** be instantiated. An appropriate message will be logged in the Application
   ** Server Log in this case.
   **
   ** @return                    a Business Facade instance.
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
    return Platform.getService(serviceClass);
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
    return this.provider;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createSession (AbstractMetadataTask)
  /**
   ** Creates a session to the Metadata Store.
   **
   ** @return                    the created {@link MDSSession}.
   */
  @Override
  public MDSSession createSession() {
    // create a session to the Metadata Store
    final SessionOptions option = new SessionOptions(IsolationLevel.READ_COMMITTED, null, null);
    return createSession(option);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createSession (AbstractMetadataTask)
  /**
   ** Creates a session to the Metadata Store with specific session options and
   ** no state handlers.
   **
   ** @param  option             the {@link SessionOptions} to create the
   **                            {@link MDSSession}.
   **
   ** @return                    the created {@link MDSSession}.
   */
  @Override
  public MDSSession createSession(final SessionOptions option) {
    final String method = "createSession";
    trace(method, SystemMessage.METHOD_ENTRY);
    MDSSession  session  = null;
    try {
      // create a session to the Metadata Store uising the session options and
      // without any specific state handlers
      debug(method, TaskBundle.string(TaskMessage.METADATA_SESSION_CREATE));
      session = Platform.getMDSInstance().createSession(option, null);
      debug(method, TaskBundle.string(TaskMessage.METADATA_SESSION_CREATED));
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return session;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute (SchedulerBaseTask)
  /**
   ** The entry point of the scheduler task thread.
   */
  @Override
  public final void execute() {
    // check if the current thread is able to execute or a stop signal is
    // pending
    if (stopped())
      return;

    final String method="execute";
    trace(method, SystemMessage.METHOD_ENTRY);
    // start the task to gather performance metrics
    timerStart(method);
    try {
      // reset all result from a previously failed task execution
      setResult(null);

      beforeExecution();

      // check if the current thread is able to execute or a stop signal is
      // pending
      if (!stopped())
        onExecution();
    }
    catch (NoClassDefFoundError e) {
      setResult(TaskException.classNotFound(e.getLocalizedMessage()));
    }
    catch (Error e) {
      setResult(TaskException.unhandled(e));
    }
    catch (TaskException e) {
      setResult(e);
    }
    catch (Exception e) {
      setResult(e);
    }
    finally {
      try {
        afterExecution();
      }
      catch (Exception e) {
        if (e instanceof TaskException)
          setResult(e);
        else
          setResult(TaskException.unhandled(e));
      }
      // stop the task timer from gathering performance metrics
      timerStop(method);
      // produce the output of the system watch for investigation if performance
      // metrics if necessary thiswill also stop the system watch in general
      if (this.logger != null && this.logger.debugLevel())
        debug("summary", this.watch.formatted());
    }

    Throwable result = getResult();
    if (result != null) {
      if (result instanceof TaskException) {
        error(method, result.getLocalizedMessage());
      }
      else {
        fatal(method, result);
      }
      trace(method, SystemMessage.METHOD_EXIT);
      throw new RuntimeException(result.getLocalizedMessage(), result);
    }
    else {
     // if we have no exception at all start the dependent job if one has to be
     // started
     final String task = stringValue(DEPENDENT_JOB);
     if (!StringUtility.isEmpty(task))
      try {
        start(task);
      }
      catch (Exception e) {
        fatal(method, e);
      }
    }
    trace(method, SystemMessage.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns a string representation of this instance.
   ** <br>
   ** Adjacent elements are separated by the character "\n" (line feed).
   ** Elements are converted to strings as by String.valueOf(Object).
   **
   ** @return                   the string representation of this instance.
   */
  @Override
  public String toString() {
    return StringUtility.formatCollection(this.getTaskAttributeMap());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   init (overridden)
  /**
   ** This method is invoked just before the thread operation will be executed.
   */
  @Override
  public void init() {
    final String method = "init";
    trace(method, SystemMessage.METHOD_ENTRY);
    // initiate the watch to track the performance of this task
    this.watch = new SystemWatch(this.getName());
    try {
      // ensure inheritance
      super.init();
      // obtain configured task attributes
      populateAttributes();
      // create a formatter to convert date values to string and vice versa
      // FIX rene.taboada@oracle.com 24.11.2011 - added condition for tasks that don't use timestamp at all
      if (parameter(TIMESTAMP_FORMAT) != null)
        this.timestampFormatter = new SimpleDateFormat(parameter(TIMESTAMP_FORMAT));
      // initialize task
      initialize();
    }
    catch (Exception e) {
      if (e instanceof TaskException) {
        error(method, e.getLocalizedMessage());
      }
      else {
        fatal(method, e);
      }
      // fixing issue that the job status doesn't display in the UI the message
      // of the exception occured by setting the result of the job accordingly
      setResult(e);
      trace(method, SystemMessage.METHOD_EXIT);
      throw new RuntimeException(e.getLocalizedMessage(), e);
    }
    trace(method, SystemMessage.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createThread (overriden)
  /**
   ** Allocates a new {@link Thread} object.
   ** <p>
   ** This method has the same effect as
   ** {@link #createThread(Runnable, String)}, where <code>String</code> is a
   ** newly generated name. Automatically generated names are of the form
   ** "Thread-" + <code>n</code>, where <code>n</code> is an integer.
   ** <p>
   ** Every {@link Thread} has a name for identification purposes. More than one
   ** {@link Thread} may have the same name. If a name is not specified when a
   ** {@link Thread} is created, a new name is generated for it.
   ** <p>
   ** The priority of the newly created thread is set equal to the priority of
   ** the {@link Thread} creating it, that is, the currently scheduled job. The
   ** method <code>setPriority</code> may be used to change the priority to a
   ** new value.
   ** <p>
   ** The newly created {@link Thread} is initially marked as being a daemon
   ** thread if and only the currently scheduled job creating it is currently
   ** marked as a daemon thread. The method <code>setDaemon</code> may be used
   ** to change whether or not a thread is a daemon.
   **
   ** @param  runnable           the object whose run method is called.
   **
   ** @return                    the created {@link Thread}.
   **
   ** @throws SecurityException  if the current {@link Thread} cannot create a
   **                            {@link Thread} in the crurrent thread group or
   **                            cannot override the context class loader
   **                            methods.
   */
  @Override
  public Thread createThread(final Runnable runnable) {
    final Thread      current = Thread.currentThread();
    final ThreadGroup group   = current.getThreadGroup();
    return createThread(runnable, String.format("%s::%d", current, group.activeCount() + 1));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createThread
  /**
   ** Allocates a new {@link Thread} object so that it has target as its run
   ** object, has the specified name as its name.
   ** <p>
   ** Every {@link Thread} has a name for identification purposes. More than one
   ** {@link Thread} may have the same name. If a name is not specified when a
   ** {@link Thread} is created, a new name is generated for it.
   ** <p>
   ** The priority of the newly created thread is set equal to the priority of
   ** the {@link Thread} creating it, that is, the currently scheduled job. The
   ** method <code>setPriority</code> may be used to change the priority to a
   ** new value.
   ** <p>
   ** The newly created {@link Thread} is initially marked as being a daemon
   ** thread if and only the currently scheduled job creating it is currently
   ** marked as a daemon thread. The method <code>setDaemon</code> may be used
   ** to change whether or not a thread is a daemon.
   **
   ** @param  runnable           the object whose run method is called.
   ** @param  name               the name of the new thread.
   **
   ** @return                    the created {@link Thread}.
   **
   ** @throws SecurityException  if the current {@link Thread} cannot create a
   **                            {@link Thread} in the crurrent thread group or
   **                            cannot override the context class loader
   **                            methods.
   */
  public Thread createThread(final Runnable runnable, final String name) {
    final Thread thread = super.createThread(runnable);
    thread.setName(name);
    return thread;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   success
  /**
   ** Whether this task is executed successful or not.
   **
   ** @return                    <code>true</code> this task is executed
   **                            successful; <code>false</code> otherwise;
   */
  public boolean success() {
    return super.isSuccess();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stop
  /**
   ** Request to stop the task.
   **
   ** @return                    <code>true</code> this task is going to stop;
   **                            <code>false</code> otherwise.
   */
  @Override
  public boolean stop() {
    this.stopping = super.stop();
    return this.stopping;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stopped
  /**
   ** Whether this task is stopping or not.
   **
   ** @return                    <code>true</code> this task is stopping;
   **                            <code>false</code> otherwise;
   */
  public boolean stopped() {
    return this.isStopped() || this.stopping;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize
  /**
   ** The initialization task.
   **
   ** @throws TaskException      in case an error does occur.
   */
  protected void initialize()
    throws TaskException {

    final String method = "initialize";
    // this will produce the trace of the configured task parameter
    debug(method, TaskBundle.format(TaskMessage.TASK_PARAMETER, this.toString()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateAttributes
  /**
   ** Populates all well known attributes defined on a scheduled task.
   **
   ** @throws TaskException      in case of a mandatory attribute is missed.
   */
  protected void populateAttributes()
    throws TaskException {

    // obtain all declared task attributes
    AbstractAttribute[] taskAttributes = attributes();
    for (int i = 0; i < taskAttributes.length; i++) {
      final String id  = taskAttributes[i].id();
      // check if the attributes is declared mandatory and the definition is
      // missing
      if (StringUtility.isEmpty(parameter(taskAttributes[i].id())))
        if (taskAttributes[i].optional())
          parameter(id, taskAttributes[i].defaultValue() == null ? SystemConstant.EMPTY : taskAttributes[i].defaultValue());
        else
          throw new TaskException(TaskError.TASK_ATTRIBUTE_MISSING, id);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beforeExecution
  /**
   ** The call back method just invoked before reconciliation takes place.
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
  // Method:   start
  /**
   ** Starts a scheduled job in the Oracle Identity Manager Server through
   ** the {@link SchedulerService}.
   **
   ** @param  name               the name of the Schedule Job to start.
   **
   ** @throws TaskException      in case an error does occur.
   */
  protected void start(final String name)
    throws TaskException {

    final String method = "start";

    // initialize the business logic layer to operate on
    SchedulerService facade = service(SchedulerService.class);
    try {
      JobDetails job = facade.getJobDetail(name);
      if (job == null) {
        error(method, TaskBundle.format(TaskError.SCHEDULE_JOB_NOT_FOUND, name));
        return;
      }
      int status = facade.getStatusOfJob(name);
      // check if the job is not running
      if (status == RUNNING) {
        warning(TaskBundle.format(TaskError.SCHEDULE_JOB_RUNNING, name));
      }
      else {
        info(TaskBundle.format(TaskMessage.SCHEDULE_JOB_STARTING, name));
        facade.triggerNow(job.getName());
        do {
          // obtain the status of the job after it was triggered
          status = facade.getStatusOfJob(name);
          // wait a certain amount of time the scheduler has started the job
          try {
            Thread.sleep(200);
          }
          catch (InterruptedException e) {
            fatal(method, e);
          }
        } while (status == STOPPED);
        info(TaskBundle.format(TaskMessage.SCHEDULE_JOB_STARTED, name));
      }
    }
    catch (SchedulerException e) {
      throw new TaskException(e);
    }
    catch (SchedulerAccessDeniedException e) {
      throw new TaskException(TaskError.GENERAL, e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateTimestamp
  /**
   ** Update the scheduled task attribute with the current timestamp for the
   ** next execution.
   **
   ** @param  attribute          the name of the attribute that build the
   **                            mapping key of the timstamp to return.
   **
   ** @throws TaskException      if the value could not be set by any reason.
   */
  protected void updateTimestamp(final String attribute)
    throws TaskException {

    updateParameter(attribute, this.parameter(attribute));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateParameter
  /**
   ** Update a parameter of this scheduled job specified by <code>name</code>
   ** to <code>value</code>.
   ** <p>
   ** <b>Note:</b>
   ** A significante change takes place in 11g due to the one-to-many
   ** relationship between task definition and jobs.
   **
   ** @param  name               the name of the parameter the provided value
   **                            has to be set for.
   ** @param  value              the new value of the attribute.
   **
   ** @throws TaskException      if the value could not be set by any reason.
   */
  protected void updateParameter(final String name, final String value)
    throws TaskException {

    final String method = "updateParameter";
    trace(method, SystemMessage.METHOD_ENTRY);

    final SchedulerService schedulerService = service(SchedulerService.class);
    try {
      final JobDetails                    job       = schedulerService.getJobDetail(getName());
      final HashMap<String, JobParameter> data      = job.getAttributes();
      final JobParameter                  parameter = data.get(name);
      if (parameter != null) {
        parameter.setValue(value);
        schedulerService.updateJob(job);
      }
    }
    catch (Exception e) {
      throw new TaskException(e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   timerStart
  /**
   ** Starts a timer for a named task.
   ** <p>
   ** The results are undefined if {@link #timerStop(String)} or timing methods
   ** are called without invoking this method.
   **
   ** @param  name               the name of the task to start
   **
   ** @see    #timerStop(String)
   */
  protected void timerStart(final String name) {
    synchronized (this.watch) {
      // start the task to gather performance metrics
      this.watch.start(TaskBundle.location(Thread.currentThread(), this.getClass(), name));
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
   ** @param  name               the name of the task to start
   **
   ** @see    #timerStart(String)
   */
  protected void timerStop(final String name) {
    synchronized (this.watch) {
      // stop the task from gathering performance metrics
      this.watch.stop(TaskBundle.location(Thread.currentThread(), this.getClass(), name));
    }
  }
}