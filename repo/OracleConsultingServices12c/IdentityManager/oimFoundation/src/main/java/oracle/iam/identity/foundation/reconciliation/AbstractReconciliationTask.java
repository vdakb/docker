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

    File        :   AbstractReconciliationTask.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractReconciliationTask.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.reconciliation;

import java.lang.reflect.Array;

import java.util.Map;
import java.util.List;
import java.util.Date;
import java.util.HashMap;

import java.io.Serializable;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcEventNotFoundException;
import Thor.API.Exceptions.tcObjectNotFoundException;
import Thor.API.Exceptions.tcEventDataReceivedException;

import oracle.iam.provisioning.vo.ApplicationInstance;

import oracle.iam.provisioning.exception.GenericAppInstanceServiceException;
import oracle.iam.provisioning.exception.ApplicationInstanceNotFoundException;

import oracle.iam.reconciliation.api.ChangeType;
import oracle.iam.reconciliation.api.EventAttributes;
import oracle.iam.reconciliation.api.ReconOperationsService;

import oracle.iam.provisioning.api.ApplicationInstanceService;

import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.TaskAttribute;
import oracle.iam.identity.foundation.AbstractLookup;
import oracle.iam.identity.foundation.AbstractAttribute;
import oracle.iam.identity.foundation.AbstractSchedulerTask;

import oracle.iam.identity.foundation.resource.TaskBundle;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractReconciliationTask
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>AbstractReconciliationTask</code> implements the base
 ** functionality of a service end point for the Oracle Identity Manager
 ** Scheduler dedicated to reconciliation tasks.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public abstract class AbstractReconciliationTask extends AbstractSchedulerTask {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Attribute to advice which named IT Resource should be used. */
  public static final String IT_RESOURCE                = "IT Resource";

  /** Attribute to advice what is the target of the task. */
  public static final String RECONCILE_OBJECT           = "Reconciliation Object";

  /** Attribute to advice what is the source of the task. */
  public static final String RECONCILE_SOURCE           = "Reconciliation Source";
  /**
   ** Attribute to advice what is the application instance targeted by the
   ** implementing scheduled job.
   */
  public static final String APPLICATION_INSTANCE       = "Application Instance";

  /**
   ** Attribute name which must be defined on this task to hold the path of the
   ** mapping descriptor stored in the Metadata Service which specifies the
   ** mapping between the incomming field names and the reconciliation fields of
   ** the object to reconcile.
   */
  public static final String RECONCILE_DESCRIPTOR       = "Reconciliation Descriptor";

  /**
   ** Attribute tag which can be defined on this task to hold the state of the
   ** synchronization and this task was last time executed succesfuly.
   */
  public static final String RECONCILE_TOKEN            = "Reconciliation Token";

  /**
   ** Attribute tag which can be defined on this task to hold the timestamp
   ** when was this task last executed.
   */
  public static final String TIMESTAMP                  = "Last Reconciled";

  /**
   ** Attribute tag which may be defined on this task to specify that a filter
   ** should be used with the timestamp attribute {@link #TIMESTAMP} in the
   ** search to decrease the result set size in operational mode to the changes
   ** made in the target system after the timestamp of {@link #TIMESTAMP}
   ** <br>
   ** This attribute is optional.
   */
  public static final String INCREMENTAL                = "Incremental";

  /**
   ** Attribute tag which can be defined on a scheduled task to advice that the
   ** created reconciliation events should be handled offline.
   ** <br>
   ** Offlining events use JMS to finish the reconciliation event
   ** <br>
   ** This attribute is optional and defaults to <code>yes</code>.
   */
  public static final String OFFLINE_EVENTS             = "Offline Event";

  /**
   ** Attribute to advice that the data should only be gathered from the
   ** reconciliation source.
   ** <br>
   ** This attribute is optional and defaults to <code>no</code>.
   */
  public static final String GATHERONLY                 = "Gather Only";

  /**
   ** Attribute tag which can be defined on this task to specify that how many
   ** threads this task should create to distribute the workload.
   ** <br>
   ** This attribute is optional and defaults to
   ** {@link #THREAD_POOL_SIZE_DEFAULT}.
   */
  public static final String THREAD_POOL_SIZE           = "Thread Pool Size";

  /** Default value for {@link #THREAD_POOL_SIZE} */
  public static final int    THREAD_POOL_SIZE_DEFAULT   = 1;

  /**
   ** Attribute tag which can be defined on this task to specify the size of a
   ** block read from the working file.
   ** <br>
   ** This attribute is optional and defaults to {@link #BATCH_SIZE_DEFAULT}.
   */
  public static final String BATCH_SIZE                 = "Batch Size";

  /** Default value for {@link #BATCH_SIZE} */
  public static final int    BATCH_SIZE_DEFAULT         = 1000;

  /**
   ** Attribute tag which may be defined on this task to specify the sort of the
   ** result returned by the search.
   ** <br>
   ** This attribute is optional.
   */
  public static final String SEARCH_ORDER               = "Search Order";

  /**
   ** Default format to prefix Lookup Definition entries.
   */
  public static final String ENTITLEMENT_ENCODED_PREFIX = "%d~%s";
  public static final String ENTITLEMENT_DECODED_PREFIX = "%s~%s";

  /** The vector with attributes which are hidden. */
  private static final TaskAttribute[] privateAttribute = {
  // defaulting some attributes
    TaskAttribute.build(TIMESTAMP_FORMAT,  TIMESTAMP_PATTERN)
  , TaskAttribute.build(OFFLINE_EVENTS,    SystemConstant.TRUE)
  , TaskAttribute.build(GATHERONLY,        SystemConstant.FALSE)
  , TaskAttribute.build(THREAD_POOL_SIZE,  String.valueOf(THREAD_POOL_SIZE_DEFAULT))
  , TaskAttribute.build(BATCH_SIZE,        String.valueOf(BATCH_SIZE_DEFAULT))
  };

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the mapping of cataloges an subject of reconciliation may have */
  protected Descriptor                      descriptor  = null;

  /** the session facade to communicate with the Server API */
  private final ReconOperationsService      facade;

  private final Map<String, AbstractLookup> lookupCache = new HashMap<String, AbstractLookup>();

  protected final Summary                   summary     = new Summary();

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
    @SuppressWarnings("compatibility:-6890789769728911584")
    private static final long serialVersionUID = 6389217446142058071L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** the error that has broken the process */
    protected TaskException   error            = null;

    /** the amount of bulk entries where reconcililiation has been succeeded */
    protected int             success          = 0;

    /** the amount of bulk entries where reconcililiation has been failed */
    protected int             failed           = 0;

    /** the amount of bulk entries where reconcililiation has been ignored */
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
    // Method:  incrementSuccess
    /**
     ** Increments the amount of bulk entries where reconcililiation has been
     ** succeeded by <code>1</code>.
     */
    public final void incrementSuccess() {
      this.success++;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: incrementSuccess
    /**
     ** Increments the amount of bulk entries where reconcililiation has been
     ** succeeded.
     **
     ** @param  amount           the amount of bulk entries where
     **                          reconcililiation has been succeeded.
     */
    public final void incrementSuccess(final int amount) {
      this.success += amount;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: decrementSuccess
    /**
     ** Decrements the amount of bulk entries where reconcililiation has been
     ** succeeded by <code>1</code>.
     */
    public final void decrementSuccess() {
      this.success--;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: decrementSuccess
    /**
     ** Decrements the amount of bulk entries where reconcililiation has been
     ** succeeded.
     **
     ** @param  amount           the amount of bulk entries where
     **                          reconcililiation has been succeeded.
     */
    public final void decrementSuccess(final int amount) {
      this.success += amount;
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
    // Method: incrementFailed
    /**
     ** Increments the amount of bulk entries where reconcililiation has been
     ** failed by <code>1</code>.
     */
    public final void incrementFailed() {
      this.failed++;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: incrementFailed
    /**
     ** Increments the amount of bulk entries where reconcililiation has been
     ** failed.
     **
     ** @param  amount           the amount of bulk entries where
     **                          reconcililiation has been failed.
     */
    public final void incrementFailed(final int amount) {
      this.failed += amount;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: decrementFailed
    /**
     ** Decrements the amount of bulk entries where reconcililiation has been
     ** failed by <code>1</code>.
     */
    public final void decrementFailed() {
      this.failed--;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: decrementFailed
    /**
     ** Decrements the amount of bulk entries where reconcililiation has been
     ** failed.
     **
     ** @param  amount           the amount of bulk entries where
     **                          reconcililiation has been failed.
     */
    public final void decrementFailed(final int amount) {
      this.failed -= amount;
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
    // Method: incrementIgnored
    /**
     ** Increments the amount of bulk entries where reconcililiation has been
     ** ignored by <code>1</code>.
     */
    public final void incrementIgnored() {
      this.ignored++;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: incrementIgnored
    /**
     ** Increments the amount of bulk entries where reconcililiation has been
     ** ignored.
     **
     ** @param  amount           the amount of bulk entries where
     **                          reconcililiation has been ignored.
     */
    public final void incrementIgnored(final int amount) {
      this.ignored += amount;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: decrementIgnored
    /**
     ** Decrements the amount of bulk entries where reconcililiation has been
     ** ignored by <code>1</code>.
     */
    public final void decrementIgnored() {
      this.ignored--;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: decrementIgnored
    /**
     ** Decrements the amount of bulk entries where reconcililiation has been
     ** ignored.
     **
     ** @param  amount           the amount of bulk entries where
     **                          reconcililiation has been ignored.
     */
    public final void decrementIgnored(final int amount) {
      this.ignored -= amount;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: asStringArray
    /**
     ** Returns the metrice gathered so far as an array of string for debugging
     ** puprpose. The array returned the metrics as
     ** <pre>
     **   array[0] --&gt; success
     **   array[1] --&gt; ignored
     **   array[2] --&gt; faied
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
   ** Constructs an <code>AbstractReconciliationTask</code> which use the
   ** specified category for logging purpose.
   **
   ** @param  loggerCategory     the category for the Logger.
   */
  protected AbstractReconciliationTask(final String loggerCategory) {
    // ensure inheritance
    super(loggerCategory);

    // initialize instance
    this.facade = service(ReconOperationsService.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceName
  /**
   ** Returns the name of the IT Resource which will be used for reconcilation.
   ** <br>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #IT_RESOURCE}.
   **
   ** @return                    the name of the IT Resource which will be used.
   */
  public final String resourceName() {
    return stringValue(IT_RESOURCE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reconcileObject
  /**
   ** Returns the name of the object to reconcile.
   ** <br>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #RECONCILE_OBJECT}.
   **
   ** @return                    the name of the object to reconcile.
   */
  public final String reconcileObject() {
    return stringValue(RECONCILE_OBJECT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reconcileSource
  /**
   ** Returns the name of the source to reconcile.
   ** <br>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #RECONCILE_SOURCE}.
   **
   ** @return                    the name of the source to reconcile.
   */
  public final String reconcileSource() {
    return stringValue(RECONCILE_SOURCE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   applicationInstance
  /**
   ** Returns the name of the application instance to reconcile.
   ** <br>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #APPLICATION_INSTANCE}.
   **
   ** @return                    the name of the application instance to
   **                            reconcile.
   */
  public final String applicationInstance() {
    return stringValue(APPLICATION_INSTANCE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reconcileDescriptor
  /**
   ** Returns the path to reconciliation descriptor in Metadata Store.
   ** <br>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #RECONCILE_DESCRIPTOR}.
   **
   ** @return                    the path to reconciliation descriptor in
   **                            Metadata Store.
   */
  public final String reconcileDescriptor() {
    return stringValue(RECONCILE_DESCRIPTOR);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lastReconciled
  /**
   ** Sets the timestamp when this scheduled taske has executed the last
   ** reconciliation.
   **
   ** @param  timeStamp          the timestamp to set.
   */
  public final void lastReconciled(final long timeStamp) {
   lastReconciled(new Date(timeStamp));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lastReconciled
  /**
   ** Sets the timestamp when this scheduled taske has executed the last
   ** reconciliation.
   **
   ** @param  timeStamp          the timestamp to set.
   */
  public final void lastReconciled(final Date timeStamp) {
    timestampValue(TIMESTAMP, timeStamp);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lastReconciled
  /**
   ** Returns the timestamp when this task has executed the reconciliation.
   **
   ** @return                    the timestamp the scheduled task has peformed
   **                            the last reconciliation.
   */
  public final Date lastReconciled() {
    return timestampValue(TIMESTAMP);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gatherOnly
  /**
   ** Returns <code>true</code> if the data should only be gathered from the
   ** reconciliation source.
   **
   ** @return                    <code>true</code> if the data should only be
   **                            gathered from the reconciliation source.
   */
  public final boolean gatherOnly() {
    return booleanValue(GATHERONLY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   threadPoolSize
  /**
   ** Returns the number of threads to handle during the reconciliation process.
   **
   ** @return                    the number of batches to handle during the
   **                            reconciliation process.
   */
  public final int threadPoolSize() {
    return integerValue(THREAD_POOL_SIZE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   batchSize
  /**
   ** Returns the batch size of the reconciliation source.
   **
   ** @return                    the batch size for the reconciliation source.
   */
  public int batchSize() {
    final int batchSize = integerValue(BATCH_SIZE);
    // Fixed Defect DE-000126
    // Batch Size is an optional argument but if its isn't defined the job
    // loops infinite hence we need to fallback to a default value
    return (batchSize == Integer.MIN_VALUE) ? BATCH_SIZE_DEFAULT : batchSize;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchOrder
  /**
   ** Returns the search order this task will request from the Service Provider.
   **
   ** @return                    the search order this task will request from
   **                            the Service Provider.
   */
  public String searchOrder() {
     return stringValue(SEARCH_ORDER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   facade
  /**
   ** Returns an instance of the Business Facade associated with this scheduler
   ** task.
   ** <br>
   ** The initialization is done lazy because the instantiation in the
   ** constructor doesn't work inside of an scheduled task.
   ** <br>
   ** Seems to be that the thread must not only be instantiated.
   **
   ** @return                    the  Business Facade instance.
   */
  protected final ReconOperationsService facade() {
    return this.facade;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   summary
  /**
   ** Returns the reporting summary this scheduled task has gathered so far.
   **
   ** @return                    the reporting summary this scheduled task has
   **                            gathered so far.
   */
  public final Summary summary() {
    return this.summary;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   incrementSuccess
  /**
   ** Increments the amount of bulk entries where reconcililiation has been
   ** succeeded by <code>1</code>.
   */
  public final void incrementSuccess() {
    this.summary.incrementSuccess();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   incrementSuccess
  /**
   ** Increments the amount of bulk entries where reconcililiation has been
   ** succeeded.
   **
   ** @param  amount             the amount of bulk entries where
   **                            reconcililiation has been succeeded.
   */
  public final void incrementSuccess(final int amount) {
    this.summary.incrementSuccess(amount);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decrementSuccess
  /**
   ** Decrements the amount of bulk entries where reconcililiation has been
   ** succeeded by <code>1</code>.
   */
  public final void decrementSuccess() {
    this.summary.decrementSuccess();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decrementSuccess
  /**
   ** Decrements the amount of bulk entries where reconcililiation has been
   ** succeeded.
   **
   ** @param  amount             the amount of bulk entries where
   **                            reconcililiation has been succeeded.
   */
  public final void decrementSuccess(final int amount) {
    this.summary.decrementSuccess(amount);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   incrementIgnored
  /**
   ** Increments the amount of bulk entries where reconcililiation has been
   ** ignored by <code>1</code>.
   */
  public final void incrementIgnored() {
    this.summary.incrementIgnored();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   incrementIgnored
  /**
   ** Increments the amount of bulk entries where reconcililiation has been
   ** ignored.
   **
   ** @param  amount             the amount of bulk entries where
   **                            reconcililiation has been ignored.
   */
  public final void incrementIgnored(final int amount) {
    this.summary.incrementIgnored(amount);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decrementIgnored
  /**
   ** Decrements the amount of bulk entries where reconcililiation has been
   ** ignored by <code>1</code>.
   */
  public final void decrementIgnored() {
    this.summary.decrementIgnored();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decrementIgnored
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
  // Method:   incrementFailed
  /**
   ** Increments the amount of bulk entries where reconcililiation has been
   ** failed by <code>1</code>.
   */
  public final void incrementFailed() {
    this.summary.incrementFailed();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   incrementFailed
  /**
   ** Increments the amount of bulk entries where reconcililiation has been
   ** failed.
   **
   ** @param  amount             the amount of bulk entries where
   **                            reconcililiation has been failed.
   */
  public final void incrementFailed(final int amount) {
    this.summary.incrementFailed(amount);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decrementFailed
  /**
   ** Decrements the amount of bulk entries where reconcililiation has been
   ** failed by <code>1</code>.
   */
  public final void decrementFailed() {
    this.summary.decrementFailed();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decrementFailed
  /**
   ** Decrements the amount of bulk entries where reconcililiation has been
   ** failed.
   **
   ** @param  amount             the amount of bulk entries where
   **                            reconcililiation has been failed.
   */
  public final void decrementFailed(final int amount) {
    this.summary.decrementFailed(amount);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeInstance
  /**
   ** Initalize the application instance by lookking up the application instance
   ** by the specified name itself and set the properties {@link #IT_RESOURCE}
   ** and {@link #RECONCILE_OBJECT} accordingly to the configured parameters
   ** found on the desired application instance only if the appropriate value
   ** evaluates to <code>null</code> or is empty.
   **
   ** @throws TaskException      if the <code>Application Instance</code>
   **                            couldn't be found or a generell erro occurs to
   **                            get the <code>Application Instance</code> data.
   */
  protected void initializeInstance()
    throws TaskException {

    initializeInstance(false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeInstance
  /**
   ** Initalize the application instance by lookking up the application instance
   ** by the specified name itself and set the properties {@link #IT_RESOURCE}
   ** and {@link #RECONCILE_OBJECT} accordingly to the configured parameters
   ** found on the desired application instance if parameter
   ** <code>override</code> evaluates to <code>true</code>.
   **
   ** @param  override           <code>true</code> if an configured value has to
   **                            be overriden with the specific value defined on
   **                            the <code>Application Instance</code>;
   **                            otherwise <code>false</code>.
   **
   ** @throws TaskException      if the <code>Application Instance</code>
   **                            couldn't be found or a generell erro occurs to
   **                            get the <code>Application Instance</code> data.
   */
  protected void initializeInstance(final boolean override)
    throws TaskException {

    final String method = "initializeInstance";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    final ApplicationInstanceService service = service(oracle.iam.provisioning.api.ApplicationInstanceService.class);
    try {
      final ApplicationInstance identity = service.findApplicationInstanceByName(applicationInstance());
      // check if the attributes are configured and filled by the job parameter
      // if any of the values are already exists they will not be overridden
      // with the values found on the application instance
      String value = parameter(IT_RESOURCE);
      if (StringUtility.isEmpty(value) || override)
        parameter(IT_RESOURCE, identity.getItResourceName());

      value = parameter(RECONCILE_OBJECT);
      if (StringUtility.isEmpty(value) || override)
        parameter(RECONCILE_OBJECT, identity.getObjectName());
    }
    catch (ApplicationInstanceNotFoundException e) {
      throw new TaskException(TaskError.INSTANCE_NOT_FOUND, applicationInstance());
    }
    catch (GenericAppInstanceServiceException e) {
      throw new TaskException(e);
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateLastReconciled
  /**
   ** Update the scheduled task attribute with the current timestamp for the
   ** next execution.
   **
   ** @throws TaskException      if the value could not be set by any reason.
   */
  protected void updateLastReconciled()
    throws TaskException {

    final String method="updateLastReconciled";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      timerStart(method);
      updateTimestamp(TIMESTAMP);
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateAttributes (overridden)
  /**
   ** Populates all well known attributes defined on a scheduled task.
   **
   ** @throws TaskException      in case of a mandatory attribute is missed.
   */
  @Override
  protected void populateAttributes()
    throws TaskException {

    // obtain all declared public attributes
    AbstractAttribute[] publicAttributes = attributes();
    int                 length           = publicAttributes.length;

    // compose the vector of all public and private attributes
    AbstractAttribute[] composed         = new AbstractAttribute[privateAttribute.length + length];
    System.arraycopy(privateAttribute, 0, composed, 0,                       privateAttribute.length);
    System.arraycopy(publicAttributes, 0, composed, privateAttribute.length, length);

    for (int i = 0; i < composed.length; i++) {
      final String id  = composed[i].id();
      // check if the attributes is declared mandatory and the definition is
      // missing
      if (StringUtility.isEmpty(parameter(id)))
        if (composed[i].optional())
          parameter(id, composed[i].defaultValue() == null ? SystemConstant.EMPTY : composed[i].defaultValue());
        else
          throw new TaskException(TaskError.TASK_ATTRIBUTE_MISSING, id);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createMaster
  /**
   ** Do all action which should take place to create account master data.
   **
   ** @param  subject            the {@link Map} providing the raw data to
   **                            reconcile.
   ** @param  account            <code>true</code> if the
   **                            <code>IT Resource</code> configured on this
   **                            task has to be put in the attribute mapping.
   **
   ** @return                    the {@link Map} with requested data.
   **
   ** @throws TaskException      if data process fails.
   */
  protected Map<String, Object> createMaster(final Map<String, Object> subject, final boolean account)
    throws TaskException {

    return createMaster(subject, account, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createMaster
  /**
   ** Do all action which should take place to create account master data.
   **
   ** @param  subject            the {@link Map} providing the raw data to
   **                            reconcile.
   ** @param  account            <code>true</code> if the
   **                            <code>IT Resource</code> configured on this
   **                            task has to be put in the attribute mapping.
   ** @param  strict             <code>true</code> if the result should contain
   **                            attribute mappings only for those definitions
   **                            that defined in the descriptor but also
   **                            contained in the provided collection.
   **                            if <code>false</code> if specified than the
   **                            result has a value for each attribute name
   **                            defined in the descriptor.
   **
   ** @return                    the {@link Map} with requested data.
   **
   ** @throws TaskException      if data process fails.
   */
  protected Map<String, Object> createMaster(final Map<String, Object> subject, final boolean account, final boolean strict)
    throws TaskException {

    final String method = "createMaster";
    trace(method, SystemMessage.METHOD_ENTRY);

    // filter data in a new mapping
    // this will also translate the server side attribute names to the
    // logical reconciliation field names defined for the reconciliation object
    timerStart(method);
    final Map<String, Object> master = this.descriptor.attributeMapping().filterByEncoded(subject, strict);
    // apply the constant defined on the descriptor
    master.putAll(this.descriptor.constantMapping());
    // add the IT Resource Name to the mapping if requested
    if (account)
      master.put(IT_RESOURCE, stringValue(IT_RESOURCE));
    timerStop(method);

    if (master.isEmpty()) {
      trace(method, SystemMessage.METHOD_EXIT);
      throw new TaskException(TaskError.ATTRIBUTE_MAPPING_EMPTY);
    }

    info(TaskBundle.format(TaskMessage.ENTITY_RECONCILE, this.descriptor.identifier(), master.get(this.descriptor.identifier())));

    // produce the logging output only if the logging level is enabled for
    if (this.logger != null && this.logger.debugLevel())
      // produce the logging output only if the logging level is enabled for
      debug(method, TaskBundle.format(TaskMessage.ATTRIBUT_MAPPING, StringUtility.formatCollection(master)));

    trace(method, SystemMessage.METHOD_EXIT);
    return master;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transformMaster
  /**
   ** Do all action which should take place to transform master data.
   **
   ** @param  master             the {@link Map} to reconcile.
   **
   ** @return                    the {@link Map} with transformend data.
   **
   ** @throws TaskException      if data process fails.
   */
  protected Map<String, Object> transformMaster(Map<String, Object> master)
    throws TaskException {

    final String method = "transformMaster";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    try {
      // resolve all field that are leverage Lookup Definitions
      if (!this.descriptor.lookup().isEmpty()) {
        for (String attributeName : this.descriptor.lookup().keySet()) {
          // check if we have a value for the master data
          String value = (String)master.get(attributeName);
          if (StringUtility.isEmpty(value))
            continue;

          AbstractLookup lookup = this.lookupCache.get(attributeName);
          if (lookup == null) {
            lookup = new AbstractLookup(this, (String)this.descriptor.lookup().get(attributeName));
            this.lookupCache.put(attributeName, lookup);
          }
          master.put(attributeName, lookup.get(value));
        }
        // produce the logging output only if the logging level is enabled for
        if (this.logger != null && this.logger.debugLevel())
          this.debug(method, TaskBundle.format(TaskMessage.LOOKUP_TRANSFORMATION, StringUtility.formatCollection(master)));
      }

      // apply transformation if needed
      // this transformation will also triggerd for the multi-valued attributes
      if (this.descriptor.transformationEnabled()) {
        master = this.descriptor.transformationMapping().transform(master);
        if (master.isEmpty())
          throw new TaskException(TaskError.TRANSFORMATION_MAPPING_EMPTY);

        // produce the logging output only if the logging level is enabled for
        if (this.logger != null && this.logger.debugLevel())
          this.debug(method, TaskBundle.format(TaskMessage.ATTRIBUT_TRANSFORMATION, StringUtility.formatCollection(master)));
      }
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return master;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addEventData
  /**
   ** Creates a Reconciliation Event.
   **
   ** @param  event              the key for the reconciliation event the data
   **                            has to be added to.
   **                            if <code>-1</code> is passed and the event
   **                            cannot be ignored a new event will be created
   **                            for the master data and the created event key
   **                            will be returned.
   ** @param  master             the mapping containing the field-value pairs
   **                            for the owning data received from the target
   **                            system to match the account the child data
   **                            belonging to.
   ** @param  dataIdentifier     the reconciliation identifier of the child data
   **                            set.
   ** @param  data               the mapping containing the field-value pairs
   **                            for the owning data received from the target
   **                            system.
   **
   ** @return                    the key for the reconciliation event to
   **                            process.
   **
   ** @throws TaskException      if event process fails.
   */
  protected final long addEventData(long event, final Map<String, Object> master, final String dataIdentifier, final List<Map<String, Object>> data)
    throws TaskException {

    final String method = "addEventData";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      if (event != -1L) {
        // this makes sures, all the multi-valued data in Oracle Identity
        // Manager is deleted first before new records are added.
        if (data.size() > 0)
          this.facade.addDirectBulkMultiAttributeData(event, dataIdentifier, data);

        this.facade.providingAllMultiAttributeData(event, dataIdentifier, true);
      }
      else {
        // convert the list to an array to satisfy the interface requirements
        // for checking of ignoring the child data event
        // use Array native method to create array of a type only known at run
        // time
        @SuppressWarnings("unchecked")
        final Map<String, Object>[] array = data.toArray((Map<String, Object>[])Array.newInstance(HashMap.class, data.size()));
        if (this.facade.ignoreEventAttributeData(this.reconcileObject(), master, dataIdentifier, array))
          info(TaskBundle.format(TaskMessage.EVENT_CHILD_IGNORED, dataIdentifier, this.reconcileObject(), master.get(this.descriptor.identifier())));
        else {
          // create a reconciliation event where child data may be appended to
          event = changelogEvent(master, false);
          // produce the logging output only if the logging level is enabled for
          if (this.logger.debugLevel())
            debug(method, TaskBundle.format(TaskMessage.EVENT_CHILD_CREATED, Long.toString(event), this.reconcileObject(), dataIdentifier));
          // call our self recursivly with a valid event to ensure that the data
          // are added to the event created above
          addEventData(event, master, dataIdentifier, data);
        }
      }
    }
    catch (tcEventNotFoundException e) {
      stopExecution();
      throw new TaskException(TaskError.EVENT_NOT_FOUND, e);
    }
    catch (tcEventDataReceivedException e) {
      stopExecution();
      throw new TaskException(TaskError.EVENT_NOT_FOUND, e);
    }
    catch (tcObjectNotFoundException e) {
      stopExecution();
      throw new TaskException(TaskError.RESOURCE_NOT_FOUND, reconcileObject());
    }
    catch (tcAPIException e) {
      stopExecution();
      throw new TaskException(e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return event;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ignoreEvent
  /**
   ** Creates a Reconciliation Event.
   **
   ** @param  subject            the {@link Map} to verify.
   **
   ** @return                    the key for the reconciliation event to
   **                            process.
   **
   ** @throws TaskException      if event processing fails.
   */
  protected boolean ignoreEvent(final Map<String, Object> subject)
    throws TaskException {

    final String method = "ignoreEvent";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    try {
      return this.facade.ignoreEvent(this.reconcileObject(), subject);
    }
    catch (tcObjectNotFoundException e) {
      stopExecution();
      throw new TaskException(TaskError.RESOURCE_NOT_FOUND, reconcileObject());
    }
    catch (tcAPIException e) {
      stopExecution();
      throw new TaskException(e);
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   changelogEvent
  /**
   ** Creates a Reconciliation Event for Create/Modify operation with complete
   ** dataset.
   ** <b>Note</b>: The event is created in a way that child data may be appended
   ** later to the eventhence the event most be closed by
   **
   ** @param  data               the mapping containing the field-value pairs
   **                            for the data received from the target system.
   **
   ** @return                    the key for the reconciliation event created
   **                            and to process later.
   */
  protected final long changelogEvent(final Map<String, Object> data) {
    // create a changelog reconciliation event where child data cannot be added
    // to
    return changelogEvent(data, true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   changelogEvent
  /**
   ** Creates a Reconciliation Event for Create/Modify operation where child
   ** data may be appended to.
   **
   ** @param  data               the mapping containing the field-value pairs
   **                            for the data received from the target system.
   ** @param  finish             indicating whether child data is going to be
   **                            provided or not.
   **                            <code>true</code> indicating that all the event
   **                            data has been provided. <code>false</code> if
   **                            there will be child data added later on.
   **
   ** @return                    the key for the reconciliation event created
   **                            and to process later.
   */
  protected final long changelogEvent(final Map<String, Object> data, final boolean finish) {
    // create a changelog reconciliation event where child data may be appended
    // to
    return createEvent(data, eventData(finish, ChangeType.CHANGELOG, null, null));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   regularEvent
  /**
   ** Creates a Reconciliation Event for Create/Modify operation with complete
   ** dataset.
   **
   ** @param  data               the mapping containing the field-value pairs
   **                            for the data received from the target system.
   **
   ** @return                    the key for the reconciliation event created
   **                            and to process later.
   */
  protected final long regularEvent(final Map<String, Object> data) {
    // create a regular reconciliation event where child data may be appended to
    return regularEvent(data, true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   regularEvent
  /**
   ** Creates a Reconciliation Event for Create/Modify operation where child
   ** data may be appended to.
   **
   ** @param  data               the mapping containing the field-value pairs
   **                            for the data received from the target system.
   ** @param  finish             indicating whether child data is going to be
   **                            provided or not.
   **                            <code>true</code> indicating that all the event
   **                            data has been provided. <code>false</code> if
   **                            there will be child data added later on.
   **
   ** @return                    the key for the reconciliation event created
   **                            and to process later.
   */
  protected final long regularEvent(final Map<String, Object> data, final boolean finish) {
    // create a regular reconciliation event where child data may be appended to
    return createEvent(data, eventData(finish, ChangeType.REGULAR, null, null));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   futureDatedChangeLogEvent
  /**
   ** Creates a future dated Reconciliation Event for Create/Modify operation
   ** where child data may be appended to.
   **
   ** @param  data               the mapping containing the field-value pairs
   **                            for the data received from the target system.
   ** @param  finish             indicating whether child data is going to be
   **                            provided or not.
   **                            <code>true</code> indicating that all the event
   **                            data has been provided. <code>false</code> if
   **                            there will be child data added later on.
   ** @param  dateFormat         the format to be used when processing the date.
   ** @param  actionDate         a {@link Date} on which the Reconciliation
   **                            Event is required to happen.
   **
   ** @return                    the key for the reconciliation event created
   **                            and to process later.
   */
  protected final long futureDatedChangeLogEvent(final Map<String, Object> data, final boolean finish, final String dateFormat, final Date actionDate) {
    // create a changelog reconciliation event where child data may be appended
    // to
    return createEvent(data, eventData(finish, ChangeType.CHANGELOG, dateFormat, actionDate));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   futureDatedRegularEvent
  /**
   ** Creates a future dated Reconciliation Event for Create/Modify operation
   ** where child data may be appended to.
   **
   ** @param  data               the mapping containing the field-value pairs
   **                            for the data received from the target system.
   ** @param  finish             indicating whether child data is going to be
   **                            provided or not.
   **                            <code>true</code> indicating that all the event
   **                            data has been provided. <code>false</code> if
   **                            there will be child data added later on.
   ** @param  dateFormat         the format to be used when processing the date.
   ** @param  actionDate         a {@link Date} on which the Reconciliation
   **                            Event is required to happen.
   **
   ** @return                    the key for the reconciliation event to
   **                            process.
   */
  protected final long futureDatedRegularEvent(final Map<String, Object> data, final boolean finish, final String dateFormat, final Date actionDate) {
    // create a regular reconciliation event where child data may be appended to
    return createEvent(data, eventData(finish, ChangeType.REGULAR, dateFormat, actionDate));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteEvent
  /**
   ** Creates a Reconciliation Event for deletion of data.
   **
   ** @param  subject            the mapping containing the field-value pairs
   **                            for the data received from the target system.
   **
   ** @return                    the key for the reconciliation event created
   **                            and to process later.
   */
  protected final long deleteEvent(final Map<String, Object> subject) {
    // create the reconciliation event for deletion
    return createEvent(subject, eventData(true, ChangeType.DELETE, null, null));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   futureDatedDeleteEvent
  /**
   ** Creates a future dated Reconciliation Event for deletion of data.
   **
   ** @param  subject            the mapping containing the field-value pairs
   **                            for the data received from the target system.
   ** @param  dateFormat         the format to be used when processing the date.
   ** @param  actionDate         a {@link Date} on which the Reconciliation
   **                            Event is required to happen.
   **
   ** @return                    the key for the reconciliation event created
   **                            and to process later.
   */
  protected final long futureDatedDeleteEvent(final Map<String, Object> subject, final String dateFormat, final Date actionDate) {
    // create the reconciliation event for deletion
    return createEvent(subject, eventData(true, ChangeType.DELETE, dateFormat, actionDate));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createEvent
  /**
   ** Creates a Reconciliation Event for the provided data specified by
   ** {@link EventAttributes}.
   **
   ** @param  subject            the mapping containing the field-value pairs
   **                            for the data received from the target system.
   ** @param  attribute          the {@link EventAttributes} like changetype,
   **                            dateformat etc. specifiying the event to create
   **                            further.
   **
   ** @return                    the key for the reconciliation event created
   **                            and to process later.
   */
  protected final long createEvent(final Map<String, Object> subject, final EventAttributes attribute) {
    final String method = "createEvent";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    final long event = this.facade.createReconciliationEvent(this.reconcileObject(), subject, attribute);
    // produce the logging output only if the logging level is enabled for
    if (this.logger.debugLevel())
      debug(method, TaskBundle.format(TaskMessage.EVENT_CREATED, Long.toString(event), this.reconcileObject()));

    timerStop(method);
    trace(method, SystemMessage.METHOD_EXIT);
    return event;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processRegularEvent
  /**
   ** Do all action which should take place for for a particular master.
   **
   ** @param  master             the {@link Map} to reconcile.
   **
   ** @throws TaskException      if event process fails.
   */
  protected void processDeleteEvent(final Map<String, Object> master)
    throws TaskException {

    final String method = "processDeleteEvent";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    try {
      // create and process the reconciliation event for delete
      // the event will be closed to make further processing impossible
      deleteEvent(master);
      this.summary.success++;
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processRegularEvent
  /**
   ** Do all action which should take place for for a particular master.
   **
   ** @param  master             the {@link Map} to reconcile.
   **
   ** @throws TaskException      if event process fails.
   */
  protected void processRegularEvent(final Map<String, Object> master)
    throws TaskException {

    final String method = "processRegularEvent";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    try {
      // check if the master entry itself is changed
      if (!ignoreEvent(master)) {
        // create and process the reconciliation event for create or modify
        // the event will be closed to make further processing impossible
        regularEvent(master);
        this.summary.success++;
      }
      else {
        this.summary.ignored++;
        // produce the logging output only if the logging level is enabled for
        if (this.logger.warningLevel())
          warning(TaskBundle.format(TaskMessage.EVENT_IGNORED, this.descriptor.identifier(), master.get(this.descriptor.identifier())));
      }
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processRegularEvent
  /**
   ** Do all action which should take place for reconciliation multi-valued
   ** attributes for a particular master.
   **
   ** @param  master             the {@link Map} to reconcile.
   ** @param  childdata          the {@link Map} receiving the child data.
   **
   ** @throws TaskException      if event process fails.
   */
  protected void processRegularEvent(final Map<String, Object> master, final Map<String, List<Map<String, Object>>> childdata)
    throws TaskException {

    final String method = "processRegularEvent";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    // initialize the eventKey for further processing
    long eventKey = -1L;
    try {
      // check if the master entry itself is changed
      if (!ignoreEvent(master))
        eventKey = regularEvent(master, false);

      // check the provided child data mapping if one of them requires
      // reconciliation
      for (String childIdentifier : childdata.keySet())
        eventKey = addEventData(eventKey, master, childIdentifier, childdata.get(childIdentifier));

      if (eventKey != -1L)
        closeEvent(eventKey);
      else {
        this.summary.ignored++;
        // produce the logging output only if the logging level is enabled for
        if (this.logger.warningLevel())
          warning(TaskBundle.format(TaskMessage.EVENT_IGNORED, this.descriptor.identifier(), master.get(this.descriptor.identifier())));
      }
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processChangeLogEvent
  /**
   ** Do all action which should take place for for a particular master.
   **
   ** @param  master             the {@link Map} to reconcile.
   **
   ** @throws TaskException      if event process fails.
   */
  protected void processChangeLogEvent(final Map<String, Object> master)
    throws TaskException {

    final String method = "processChangeLogEvent";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    try {
      // check if the master entry itself is changed
      if (!ignoreEvent(master)) {
        // create and process the reconciliation event for create or modify
        // the event will be closed to make further processing impossible
        changelogEvent(master);
        this.summary.success++;
      }
      else {
        this.summary.ignored++;
        // produce the logging output only if the logging level is enabled for
        if (this.logger.warningLevel())
          warning(TaskBundle.format(TaskMessage.EVENT_IGNORED, this.descriptor.identifier(), master.get(this.descriptor.identifier())));
      }
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processChangeLogEvent
  /**
   ** Do all action which should take place for reconciliation multi-valued
   ** attributes for a particular master.
   **
   ** @param  master             the {@link Map} to reconcile.
   ** @param  childdata          the {@link Map} receiving the child data.
   **
   ** @throws TaskException      if event process fails.
   */
  protected void processChangeLogEvent(final Map<String, Object> master, final Map<String, List<Map<String, Object>>> childdata)
    throws TaskException {

    final String method = "processChangeLogEvent";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
      // initialize the eventKey for further processing
      long eventKey = -1L;
    try {
      // check if the master entry itself is changed
      if (!ignoreEvent(master))
        eventKey = changelogEvent(master, false);

      // check the provided child data mapping if one of them requires
      // reconciliation
      for (String childIdentifier : childdata.keySet())
        eventKey = addEventData(eventKey, master, childIdentifier, childdata.get(childIdentifier));

      if (eventKey != -1L)
        closeEvent(eventKey);
      else {
        this.summary.ignored++;
        // produce the logging output only if the logging level is enabled for
        if (this.logger.warningLevel())
          warning(TaskBundle.format(TaskMessage.EVENT_IGNORED, this.descriptor.identifier(), master.get(this.descriptor.identifier())));
      }
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   closeEvent
  /**
   ** Closes off a Reconciliation Event that was left open when created.
   ** <p>
   ** Depending on the task parameter {@link #OFFLINE_EVENTS} it the event will
   ** be gp through JMS or not
   **
   ** @param  event              the key for the reconciliation event to
   **                            process.
   **
   ** @throws TaskException      if event process fails.
   */
  protected final void closeEvent(final long event)
    throws TaskException {

    final String method = "closeEvent";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    try {
      // closes off the current reconciliation event that was left open when
      // created to allow additional data (child table data) to be provided.
      this.facade.finishReconciliationEvent(event);
      // produce the logging output only if the logging level is enabled for
      if (this.logger.debugLevel())
        debug(method, TaskBundle.format(TaskMessage.EVENT_FINISHED, Long.toString(event)));

      if (!booleanValue(OFFLINE_EVENTS)) {
        try {
          // process the current reconciliation event, applying the matching rules
          // and the action rules, and linking it to the appropriate User, Org or
          // Process Instance.
          // The entire processing will take place without any offlining.
          this.facade.processReconciliationEvent(event);
          // produce the logging output only if the logging level is enabled for
          if (this.logger.debugLevel())
            debug(method, TaskBundle.format(TaskMessage.EVENT_PROCEED, Long.toString(event)));
        }
        // we have to handle the exception below explicitly if we are processing
        // the reconciliation event to prevent that the job stops further
        // execution
        catch (tcAPIException e) {
          final String message = e.getMessage();
          // check for if we got a ValidationException and spool out the error
          // to the log if so
          if (message.matches("(.*)ValidationFailedException(.*)"))
            error(method, TaskBundle.format(TaskMessage.EVENT_FAILED, Long.toString(event), e.getLocalizedMessage()));
          else
            throw e;
        }
      }
      this.summary.success++;
    }
    catch (tcEventNotFoundException e) {
      this.summary.failed++;
      stopExecution();
      throw new TaskException(TaskError.EVENT_NOT_FOUND, e);
    }
    catch (tcEventDataReceivedException e) {
      this.summary.failed++;
      stopExecution();
      throw new TaskException(TaskError.EVENT_NOT_FOUND, e);
    }
    catch (tcAPIException e) {
      this.summary.failed++;
      stopExecution();
      throw new TaskException(e);
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eventData
  /**
   ** Factory method to create the attributes of an event which for creation
   ** into Oracle Identity Manager.
   **
   ** @param  finish             indicates whether child data is going to be
   **                            provided or not. It is set to
   **                            <code>false</code> if child data is going to be
   **                            added later to the event. If is
   **                            <code>true</code> that means event status is
   **                            set to <code>Data Received</code> otherwise it
   **                            would be set to <code>Event Received</code>
   **                            indicating whether child data is going to be
   **                            provided or not.
   ** @param  type               the {@link ChangeType} of the event to create,
   **                            e.g. <code>yyyy-mm-dd hh:mm:ss</code>.
   ** @param  dateFormat         the format to be used when processing the date,
   **                            e.g. <code>yyyy-mm-dd hh:mm:ss</code>.
   ** @param  actionDate         a {@link Date} on which the Reconciliation
   **                            Event is required to happen. If its
   **                            <code>null</code> or less then the current
   **                            system date then the processing is done
   **                            instantly otherwise diferred till the date is
   **                            reached.
   **
   ** @return                    the {@link EventAttributes} created with the
   **                            provided properties.
   */
  protected EventAttributes eventData(final boolean finish, final ChangeType type, final String dateFormat, final Date actionDate) {
    // create a reconciliation event where child data may be appended to
    return new EventAttributes(finish, dateFormat, type, actionDate);
  }
}