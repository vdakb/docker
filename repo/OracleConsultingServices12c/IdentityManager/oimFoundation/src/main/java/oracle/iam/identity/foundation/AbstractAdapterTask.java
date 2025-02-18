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
    Subsystem   :   Common Shared Provisioning Facilities

    File        :   AbstractAdapterTask.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractAdapterTask.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation;

import java.util.Map;
import java.util.LinkedHashMap;

import Thor.API.tcResultSet;

import Thor.API.Operations.tcUserOperationsIntf;
import Thor.API.Operations.tcGroupOperationsIntf;
import Thor.API.Operations.tcObjectOperationsIntf;
import Thor.API.Operations.tcLookupOperationsIntf;
import Thor.API.Operations.tcPropertyOperationsIntf;
import Thor.API.Operations.tcOrganizationOperationsIntf;
import Thor.API.Operations.tcProvisioningOperationsIntf;
import Thor.API.Operations.tcFormInstanceOperationsIntf;
import Thor.API.Operations.tcFormDefinitionOperationsIntf;
import Thor.API.Operations.tcITResourceInstanceOperationsIntf;

import com.thortech.xl.dataaccess.tcDataProvider;

import oracle.mds.core.MDSSession;
import oracle.mds.core.SessionOptions;
import oracle.mds.core.IsolationLevel;

import oracle.iam.platform.Platform;

import oracle.hst.foundation.SystemWatch;
import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.logging.Logger;

import oracle.hst.foundation.utility.ClassUtility;

import oracle.iam.identity.foundation.naming.FormDefinition;

import oracle.iam.identity.foundation.resource.TaskBundle;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractAdapterTask
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>AbstractAdapterTask</code> implements the base functionality of an
 ** service end point for the Oracle Identity Manager Provisioning.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public abstract class AbstractAdapterTask implements AbstractServiceTask
                                          ,          AbstractMetadataTask {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the category of the logging facility to use */
  static final String            LOGGER_CATEGORY = "OCS.USR.PROVISIONING";

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

  /** the system watch to gather performance metrics */
  protected final SystemWatch    watch;

  protected final tcDataProvider provider;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AbstractAdapterTask</code> which use the specified
   ** category for logging purpose.
   **
   ** @param  provider           the session provider connection
   ** @param  loggerCategory     the category for the Logger.
   */
  public AbstractAdapterTask(final tcDataProvider provider, final String loggerCategory) {
    // ensure inheritance
    this(provider, loggerCategory, loggerCategory);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AbstractAdapterTask</code> which use the specified
   ** category for logging purpose.
   **
   ** @param  provider           the session provider connection
   ** @param  loggerCategory     the category for the Logger.
   ** @param  processName        the name of the process used for debugging
   **                            purpose in the scope of gathering performance
   **                            metrics.
   */
  public AbstractAdapterTask(final tcDataProvider provider, final String loggerCategory, final String processName) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.logger   = Logger.create(loggerCategory);
    this.prefix   = ClassUtility.shortName(this);
    this.provider = provider;
    this.watch    = new SystemWatch(processName);
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
   ** Logs an warning error to the associated <code>Logger</code>.
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
   ** Writes a debug message to the associated <code>Logger</code>.
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
   ** associated with this provisioning task.
   **
   ** @return                    a Business Facade instance.
   */
  @Override
  public final tcLookupOperationsIntf lookupFacade() {
    return service(tcLookupOperationsIntf.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceFacade (AbstractTask)
  /**
   ** Returns an instance of an IT Resource class associated with this
   ** provisioning task.
   **
   ** @return                    a Business Facade instance.
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
   ** Manager Organization associatiated with this provisioning task.
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
   ** Manager User associatiated with this provisioning task.
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
   ** associatiated with this provisioning task.
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
   ** associatiated with this provisioning task.
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
   ** (Process Data) associatiated this provisioning task.
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
   ** (Process Data) associatiated this provisioning task.
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
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

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
    // start the task to gather performance metrics
    this.watch.start(TaskBundle.location(this.getClass(), name));
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
    // stop the task timer from gathering performance metrics
    this.watch.stop(TaskBundle.location(this.getClass(), name));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   describeForm
  /**
   ** Returns the attribute labels of a process form belonging to a provisioning
   ** process specified by <code>processInstance</code>.
   **
   ** @param  processInstance    the process instance key providing the data for
   **                            the provisioning tasks.
   **
   ** @return                    a {@link Map} containing the names and the
   **                            corresponding labels for each column.
   **
   ** @throws TaskException      if the operation fails.
   */
  protected Map<String, String> describeForm(final long processInstance)
    throws TaskException {

    final String method = "describeForm";
    trace(method, SystemMessage.METHOD_ENTRY);

    final Map<String, String>            descriptor           = new LinkedHashMap<String, String>();
    final tcFormDefinitionOperationsIntf formDefinitionFacade = this.formDefinitionFacade();
    final tcFormInstanceOperationsIntf   formInstanceFacade   = this.formInstanceFacade();
    try {
      long formDefinition   = formInstanceFacade.getProcessFormDefinitionKey(processInstance);
      tcResultSet formField = formDefinitionFacade.getFormFields(formDefinition, formInstanceFacade.getProcessFormVersion(processInstance));
      // for each form field definition code check to see whether there is a
      // corresponding User Defined Field in the lookup
      for (int j = 0; j < formField.getRowCount(); j++) {
        formField.goToRow(j);
        descriptor.put(formField.getStringValue(FormDefinition.COLUMN_NAME), formField.getStringValue(FormDefinition.COLUMN_LABEL));
      }
    }
    catch (Exception e) {
      throw new TaskException(e);
    }
    finally {
      formDefinitionFacade.close();
      formInstanceFacade.close();
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return descriptor;
  }
}