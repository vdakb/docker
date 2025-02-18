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

    File        :   AbstractEventHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractEventHandler.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.provisioning;

import Thor.API.Operations.tcFormDefinitionOperationsIntf;
import Thor.API.Operations.tcFormInstanceOperationsIntf;
import Thor.API.Operations.tcGroupOperationsIntf;
import Thor.API.Operations.tcITResourceInstanceOperationsIntf;
import Thor.API.Operations.tcLookupOperationsIntf;
import Thor.API.Operations.tcObjectOperationsIntf;
import Thor.API.Operations.tcOrganizationOperationsIntf;
import Thor.API.Operations.tcProvisioningOperationsIntf;
import Thor.API.Operations.tcUserOperationsIntf;

import com.thortech.xl.client.events.tcBaseEvent;

import oracle.iam.platform.Platform;

import oracle.hst.foundation.logging.Logger;

import oracle.hst.foundation.utility.ClassUtility;

import oracle.iam.identity.foundation.AbstractTask;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractEventHandler
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>AbstractEventHandler</code> implements the base functionality of a
 ** event handlers for the Oracle Identity Manager Adapter.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public abstract class AbstractEventHandler extends    tcBaseEvent
                                           implements AbstractTask {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final String                 shortName;
  private final Logger                 logger;

  private tcLookupOperationsIntf       lookupFacade       = null;
  private tcOrganizationOperationsIntf organizationFacade = null;
  private tcUserOperationsIntf         userFacade         = null;
  private tcGroupOperationsIntf        groupFacade        = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>AbstractEventHandler</code> event adapter that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   **
   ** @param  loggerCategory     the category for the Logger.
   */
  public AbstractEventHandler(final String loggerCategory) {
    // ensure inheritance
    super();

    this.shortName = ClassUtility.shortName(this);
    this.logger    = Logger.create(loggerCategory);
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
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   ** @param  what               the exception as the reason to log.
   */
  @Override
  public final void fatal(final String method, final Throwable what) {
    this.logger.fatal(this.shortName, method, what);
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
  public final void error(final String method, final String message) {
    this.logger.error(this.shortName, method, message);
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
  public final void warning(final String method, final String message) {
    this.logger.warn(this.shortName, method, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   info (Loggable)
  /**
   ** Writes an informational message to the associated <code>Logger</code>.
   **
   ** @param  message            the message to log.
   */
  public final void info(final String message) {
    this.logger.info(message);
  }

  //////////////////////////////////////////////////////////////////////////////
 // Method:   debug (Loggable)
 /**
  ** Writes a debug message to the associated <code>Logger</code>.
  **
  ** @param  method             the name of the method where the logging
  **                            event was occurred.
  ** @param  message            the message to log.
  */
  public final void debug(final String method, final String message) {
    this.logger.debug(this.shortName, method, message);
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
    if (this.lookupFacade == null)
      this.lookupFacade = service(tcLookupOperationsIntf.class);

    return this.lookupFacade;
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
    if (this.groupFacade == null)
      this.groupFacade = service(tcGroupOperationsIntf.class);

    return this.groupFacade;
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
    if (this.organizationFacade == null)
      this.organizationFacade = service(tcOrganizationOperationsIntf.class);

    return this.organizationFacade;
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
    if (this.userFacade == null)
      this.userFacade = service(tcUserOperationsIntf.class);

    return this.userFacade;
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
}