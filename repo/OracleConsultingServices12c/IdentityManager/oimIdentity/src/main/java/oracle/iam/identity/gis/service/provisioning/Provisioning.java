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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Oracle Identity Manager Connector

    File        :   Provisioning.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Provisioning.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    3.1.0.0      2014-06-21  DSteding    First release version
*/

package oracle.iam.identity.gis.service.provisioning;

import com.thortech.xl.dataaccess.tcDataProvider;

import oracle.hst.foundation.SystemMessage;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.provisioning.AbstractProvisioningTask;

import oracle.iam.identity.foundation.rmi.IdentityServerResource;

import oracle.iam.identity.gis.service.ManagedServer;

////////////////////////////////////////////////////////////////////////////////
// abstract class Provisioning
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~
/**
 ** The <code>Provisioning</code> implements the base functionality of a service
 ** end point for the Oracle Identity Manager Adapter Factory which handles data
 ** delivered to Oracle Identity Manager itself.
 ** <b>Note</b>
 ** Class is package protected.
 **
 ** @author  Dieter.Steding@oracle.com
 ** @version 3.1.0.0
 ** @since   3.1.0.0
 */
abstract class Provisioning extends AbstractProvisioningTask {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the category of the logging facility to use */
  private static final String   LOGGER_CATEGORY = "OCS.GIS.PROVISIONING";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the abstraction layer to describe the connection to the target system */
  protected IdentityServerResource resource;

  /** the abstraction layer to communicate with the target system */
  protected ManagedServer          server       = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Provisioning</code> task adpater that allows use
   ** as a JavaBean.
   **
   ** @param  provider           the session provider connection.
   ** @param  processTaskName    the name of the process task to pass to this
   **                            adapter implementation for debugging purpose.
   */
  protected Provisioning(final tcDataProvider provider, final String processTaskName) {
    // ensure inheritance
    super(provider, LOGGER_CATEGORY, processTaskName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beginTransaction
  /**
   ** Starts a transaction with Identity Manager by aquiring a connection from
   ** the pool.
   **
   ** @param  serverIdentifier   the system identifier of the
   **                            <code>IT Resource</code> instance where this
   **                            connector is associated with.
   **
   ** @return                    an appropriate response message
   */
  public String beginTransaction(final Long serverIdentifier) {
    final String method = "beginTransaction";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      // initialize the code returned to the Process Task in an optimistic manner
      final IdentityServerResource datasource = IdentityServerResource.build(this, serverIdentifier);
      // always populate the attributes of the specified IT Resource regardless
      // if a connection pool is involved or not
      // invoking the method below will validate if all required parameters are
      // set on the IT Resource
      datasource.populateAttributes(datasource.name());
      // initialize instance attributes that handles the connection interface
      this.server = new ManagedServer(this, datasource);
      // ... and connect
      this.server.connect();

      return SUCCESS;
    }
      catch (TaskException e) {
      error(method, e.getLocalizedMessage());
      return e.code();
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   closeTransaction
  /**
   ** Close a transaction with the Identity Manager to release the previously
   ** aquired connection.
   ** <p>
   ** Either the connection is released to the pool if it's aquired from there
   ** or it's really closed if it was not a pooled conection.
   */
  public void closeTransaction() {
    // if the connection is not initialized yet we can easiely escape
    if (this.server == null)
      return;

    final String method = "closeTransaction";
    trace(method, SystemMessage.METHOD_ENTRY);
    // the disconnect should not prevent us from a successfull completion of
    // the task hence it's exceptions are handle separatly
    this.server.disconnect();
    trace(method, SystemMessage.METHOD_EXIT);
  }

}