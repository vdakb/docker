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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Offline Target Connector

    File        :   Reconciliation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Reconciliation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    3.0.0.0      2012-28-10  DSteding    First release version
*/

package oracle.iam.identity.ots.service.reconciliation;

import java.util.Map;
import java.util.HashMap;

import oracle.hst.foundation.SystemException;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.utility.FileSystem;

import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.AbstractResource;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.offline.Entity;
import oracle.iam.identity.foundation.offline.EntityFactory;
import oracle.iam.identity.foundation.offline.EntityListener;

import oracle.iam.identity.ots.service.Controller;
import oracle.iam.identity.ots.service.OfflineResource;

import oracle.iam.identity.ots.service.catalog.HarvesterDescriptor;

////////////////////////////////////////////////////////////////////////////////
// abstract class Reconciliation
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The <code>Reconciliation</code> implements the base functionality of a
 ** service end point for the Oracle Identity Manager Scheduler which handles
 ** data provided by a XML file.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.0.0.0
 ** @since   3.1.0.0
 */
abstract class Reconciliation<E extends Entity> extends    Controller
                                                implements EntityListener<E> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which must be defined on a scheduled task to specify the
   ** fullqualified name of the Java Class (<code>EntityFactory</code>) governs
   ** the process of deserializing XML data into newly created Java content
   ** trees, optionally validating the XML data as it is unmarshalled.
   */
  protected static final String  UNMARSHALLER    = "Unmarshaller Implementation";

  /**
   ** Attribute tag which must be defined on a scheduled task to specify if the
   ** entire file has to be validate against the XML schema before it will be
   ** unmarshalled.
   */
  protected static final String  VALIDATE_SCHEMA = "Validate Import"  ;

  /** the category of the logging facility to use */
  private static final String   LOGGER_CATEGORY  = "OCS.OTS.RECONCILIATION";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // the all purpose container to search and post objects
  protected Map<String, String> filter           = new HashMap<String, String>();

  /** the factory to unmarshal the XML file */
  protected EntityFactory<E>    factory          = null;

  /**
   ** the abstraction layer to describe the generic connection to the target
   ** system
   */
  private AbstractResource      resource;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Reconciliation</code> task adapter that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected Reconciliation() {
    // ensure inheritance
    super(LOGGER_CATEGORY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resource
  /**
   ** Returns the {@link AbstractResource} this task is using to describe the
   ** connection parameter.
   **
   ** @return                    the {@link OfflineResource} this task is
   **                            using to describe the connection parameter.
   */
  protected final AbstractResource resource() {
    return this.resource;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nullValue (EntityListener)
  /**
   ** Returns the value which represents a <code>null</code> for an attribute
   ** element.
   ** <p>
   ** Such specification is required to distinct between empty attribute
   ** elements which are not passed through and overriding an already existing
   ** metadata to make it <code>null</code>.
   **
   ** @return                    the value which represents a <code>null</code> for
   **                            an attribute element.
   */
  @Override
  public String nullValue() {
    return ((HarvesterDescriptor)this.descriptor).nullValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onExecution (AbstractSchedulerBaseTask)
  /**
   ** The entry point of the reconciliation task to perform.
   **
   ** @throws TaskException      in case an error does occur.
   */
  @Override
  protected void onExecution()
    throws TaskException {

    final String[] parameter = { reconcileObject(), getName() , dataFile().getAbsolutePath()};
    info(TaskBundle.format(TaskMessage.RECONCILIATION_BEGIN, parameter));
    if (this.dataFileAvailable(lastReconciled())) {
      info(TaskBundle.format(TaskMessage.RECONCILE_BEGIN, parameter));

      beforeReconcile();
      try {
        this.factory.populate(this, this.dataFile(), booleanValue(VALIDATE_SCHEMA));

        this.updateLastReconciled();
        info(TaskBundle.format(TaskMessage.RECONCILE_COMPLETE,      parameter));
        afterReconcile();
        info(TaskBundle.format(TaskMessage.RECONCILIATION_COMPLETE, parameter));
      }
      catch (TaskException e) {
        // copy the data file to the error folder
        // we let the new file where it is, so the next time the scheduled
        // task is running we have the same file or a new one
        try {
          FileSystem.copy(dataFile(), errorFile());
        }
        catch (SystemException ex) {
          warning(ex.getLocalizedMessage());
        }
        final String[] arguments = { reconcileObject(), getName() , dataFile().getAbsolutePath() , e.getLocalizedMessage()};
        // notify user about the problem
        warning(TaskBundle.format(TaskMessage.RECONCILIATION_STOPPED, arguments));
        throw e;
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize (overridden)
  /**
   ** This method is invoked just before the thread operation will be executed.
   **
   ** @throws TaskException      the exception thrown if any goes wrong
   */
  @Override
  protected void initialize()
    throws TaskException {

    // ensure inheritance
    // this will produce the trace of the configured task parameter and create
    // the abstract file paths to the data and error directories
    super.initialize();

    final String className = stringValue(UNMARSHALLER);
    try {
      // a little bit reflection
      final Class<?> clazz = Class.forName(className);
      this.factory = (EntityFactory<E>)clazz.newInstance();
    }
    catch (ClassNotFoundException e) {
      throw TaskException.classNotFound(className);
    }
    catch (InstantiationException e) {
      throw TaskException.classNotCreated(className);
    }
    catch (IllegalAccessException e) {
      throw TaskException.classNoAccess(className);
    }
    // initialize IT Resource properties only if they are 
    if (getTaskAttributeMap().contains(APPLICATION_INSTANCE))
      initializeInstance();
    if (getTaskAttributeMap().contains(IT_RESOURCE) || getTaskAttributeMap().contains(APPLICATION_INSTANCE))
      initializeResource();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beforeReconcile
  /**
   ** The call back method just invoked before reconciliation finished.
   **
   ** @throws TaskException      in case an error does occur.
   */
  protected abstract void beforeReconcile()
    throws TaskException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   afterReconcile
  /**
   ** The call back method just invoked after reconciliation finished.
   **
   ** @throws TaskException      in case an error does occur.
   */
  protected abstract void afterReconcile()
    throws TaskException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeResource
  /**
   ** Initalize the IT Resource capabilities.
   **
   ** @throws TaskException      if the initialization of the
   **                            <code>IT Resource</code> fails.
   */
  protected void initializeResource()
    throws TaskException {

    final String method = "initializeResource";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    try {
      // we don't need to validate any attribute of the resource used by offline
      // reconciliation because we need only the key and the name
      this.resource  = new AbstractResource(this, resourceName());
      // produce the logging output only if the logging level is enabled for
      if (this.logger.debugLevel())
        debug(method, TaskBundle.format(TaskMessage.ITRESOURCE_PARAMETER, this.resource.toString()));
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }
}