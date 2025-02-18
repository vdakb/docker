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

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Oracle Database Account Connector

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
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.dbs.service.provisioning;

import com.thortech.xl.dataaccess.tcDataProvider;

import oracle.hst.foundation.SystemMessage;

import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.provisioning.AbstractProvisioningTask;

import oracle.iam.identity.foundation.persistence.DatabaseResource;

import oracle.iam.identity.dbs.persistence.Administration;

////////////////////////////////////////////////////////////////////////////////
// abstract class Provisioning
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~
/**
 ** The <code>Provisioning</code> implements the base functionality of a service
 ** end point for the Oracle Identity Manager Adapter Factory which handles data
 ** delivered to a Database Server.
 ** <b>Note</b>
 ** Class is package protected.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
abstract class Provisioning extends AbstractProvisioningTask {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the category of the logging facility to use */
  private static final String LOGGER_CATEGORY = "OCS.DBS.PROVISIONING";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected Administration    connection;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Provisioning</code> task adpater that allows use
   ** as a JavaBean.
   **
   ** @param  provider           the session provider connection
   **
   ** @throws TaskException      if the feature configuration cannot be created.
   */
  public Provisioning(final tcDataProvider provider)
    throws TaskException {

    // ensure inheritance
    super(provider, LOGGER_CATEGORY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   aquireConnection
  /**
   ** Starts a transaction with the target system by aquiring a connection from
   ** the pool.
   **
   ** @param  serverIdentifier   the system identifier of the
   **                            <code>IT Resource</code> instance where this
   **                            adapter is associated with.
   **
   ** @return                    an appropriate response message
   */
  public String aquireConnection(final Long serverIdentifier) {
    final String method = "aquireConnection";
    trace(method, SystemMessage.METHOD_ENTRY);
    String responseCode = SUCCESS;
    try {
      // initialize the code returned to the Process Task in an optimistic manner
      final DatabaseResource datasource = new DatabaseResource(this, serverIdentifier);
      // always populate the attributes of the specified IT Resource regardless if
      // a connection pool is involved or not
      // invoking the method below will validate if all required parameters are
      // set on the IT Resource
      datasource.populateAttributes(datasource.name());

      // initialize instance attributes that handles the connection interface
      this.connection = new Administration(this, datasource);
      // produce the logging output only if the logging level is enabled for
      if (this.logger.debugLevel())
        debug(method, TaskBundle.format(TaskMessage.ITRESOURCE_PARAMETER, this.connection.toString()));
      // ... and connect
      this.connection.connect();
    }
    catch (TaskException e) {
      responseCode = e.code();
    }
    trace(method, SystemMessage.METHOD_EXIT);
    return responseCode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   releaseConnection
  /**
   ** Ends a transaction with the target system by giving back the connection to
   ** the pool.
   **
   ** @return                    an appropriate response message that will be
   **                            always <code>SUCCESS</code>
   */
  public String releaseConnection() {
    final String method = "releaseConnection";
    trace(method, SystemMessage.METHOD_ENTRY);
    this.connection.disconnect();
    // release all resource that belongs to the connection interface
    this.connection = null;
    trace(method, SystemMessage.METHOD_EXIT);
    return SUCCESS;
  }
}