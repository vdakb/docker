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
    Subsystem   :   Common Shared Runtime Facilities

    File        :   AbstractAttributeTransformer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractAttributeTransformer.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation;

import Thor.API.Operations.tcPropertyOperationsIntf;
import Thor.API.Operations.tcLookupOperationsIntf;
import Thor.API.Operations.tcGroupOperationsIntf;
import Thor.API.Operations.tcITResourceInstanceOperationsIntf;
import Thor.API.Operations.tcObjectOperationsIntf;
import Thor.API.Operations.tcOrganizationOperationsIntf;
import Thor.API.Operations.tcProvisioningOperationsIntf;
import Thor.API.Operations.tcUserOperationsIntf;
import Thor.API.Operations.tcFormDefinitionOperationsIntf;
import Thor.API.Operations.tcFormInstanceOperationsIntf;

import oracle.iam.platform.Platform;

import oracle.hst.foundation.logging.Logger;
import oracle.hst.foundation.logging.Loggable;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractAdapterTask
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>AbstractAttributeTransformer</code> implements the base
 ** functionality of a transformer for the Oracle Identity Manager Provisioning
 ** and Reconciliation tasks.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 0.0.0.2
 */
public abstract class AbstractAttributeTransformer implements AttributeTransformer {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final Logger logger;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

   //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AbstractAttributeTransformer</code> which use the
   ** specified {@link Logger} for logging purpose.
   **
   ** @param  logger             the Logger.
   **                            Allowed object {@link Logger}.
   */
  public AbstractAttributeTransformer(final Logger logger) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.logger = logger;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   logger (Loggable)
  /**
   ** Returns the logger associated with this task.
   **
   ** @return                    the logger associated with this task.
   **                            Possible object {@link Logger}.
   */
  @Override
  public final Logger logger() {
    return this.logger;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fatal (Loggable)
  /**
   ** Writes a critical message to the <code>Logger</code> of the owning
   ** {@link Loggable}.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   **                            Allowed object {@link String}.
   ** @param  throwable          the exception as the reason to log.
   **                            Allowed object {@link Throwable}.
   */
  @Override
  public final void fatal(final String method, final Throwable throwable) {
    if (this.logger != null)
      this.logger.fatal(this.logger.category(), method, throwable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error (Loggable)
  /**
   ** Writes a non-critical message to the <code>Logger</code> of the owning
   ** {@link Loggable}.
   ** <br>
   ** Convenience wrapper for sub classes.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   **                            Allowed object {@link String}.
   ** @param  message            the message to log.
   **                            Allowed object {@link String}.
   */
  @Override
  public void error(final String method, final String message) {
    if (this.logger != null)
      this.logger.error(this.logger.category(), method, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warning (Loggable)
  /**
   ** Logs an warning error to the associated <code>Logger</code>.
   **
   ** @param  message            the message to log.
   **                            Allowed object {@link String}.
   */
  @Override
  public final void warning(final String message) {
    if (this.logger != null)
      this.logger.warn(message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warning (Loggable)
  /**
   ** Logs an warning error to the associated <code>Logger</code>.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   **                            Allowed object {@link String}.
   ** @param  message            the message to log.
   **                            Allowed object {@link String}.
   */
  @Override
  public final void warning(final String method, final String message) {
    if (this.logger != null)
      this.logger.warn(this.logger.category(), method, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   info (Loggable)
  /**
   ** Writes a informational message to the <code>Logger</code> of the owning
   ** {@link Loggable}.
   **
   ** @param  message            the message to log.
   **                            Allowed object {@link String}.
   */
  @Override
  public final void info(final String message) {
    if (this.logger != null)
      this.logger.info(message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   debug (Loggable)
  /**
   ** Writes a debug message to the <code>Logger</code> of the owning
   ** {@link Loggable}.
   ** <br>
   ** Convenience wrapper for sub classes.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   **                            Allowed object {@link String}.
   ** @param  what               what is the reason to log.
   **                            Allowed object {@link String}.
   */
  @Override
  public final void debug(final String method, final String what) {
    if (this.logger != null)
      this.logger.debug(this.logger.category(), method, what);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trace (Loggable)
  /**
   ** Writes a trace message to the <code>Logger</code> of the owning
   ** {@link Loggable}.
   ** <br>
   ** Convenience wrapper for sub classes.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   **                            Allowed object {@link String}.
   ** @param  what               what is the reason to log.
   **                            Allowed object {@link String}.
   */
  @Override
  public final void trace(final String method, final String what) {
    if (this.logger != null)
      this.logger.trace(this.logger.category(), method, what);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   propertyFacade (AbstractTask)
  /**
   ** Returns an instance of a Business Facade instance for System Configuration
   ** associated with this provisioning task.
   **
   ** @return                    a Business Facade instance.
   **                            Possible object
   **                            {@link tcPropertyOperationsIntf}.
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
   **                            Possible object {@link tcLookupOperationsIntf}.
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
   **                            Possible object
   **                            {@link tcITResourceInstanceOperationsIntf}.
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
   **                            Possible object {@link tcGroupOperationsIntf}.
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
   **                            Possible object
   **                            {@link tcOrganizationOperationsIntf}.
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
   **                            Possible object {@link tcUserOperationsIntf}.
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
   **                            Possible object {@link tcObjectOperationsIntf}.
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
   **                            Possible object
   **                            {@link tcProvisioningOperationsIntf}.
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
   **                            Possible object
   **                            {@link tcFormDefinitionOperationsIntf}.
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
   **                            Possible object
   **                            {@link tcFormInstanceOperationsIntf}.
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
  public final <T> T service(final Class<T> serviceClass) {
    return Platform.getService(serviceClass);
  }
}