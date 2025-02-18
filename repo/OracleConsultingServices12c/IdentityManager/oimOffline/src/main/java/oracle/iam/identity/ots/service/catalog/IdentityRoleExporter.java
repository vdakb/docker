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

    File        :   IdentityRoleExporter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    IdentityRoleExporter.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    3.0.0.0      2012-28-10  DSteding    First release version
*/

package oracle.iam.identity.ots.service.catalog;

import oracle.hst.foundation.SystemMessage;

import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.TaskAttribute;

import oracle.iam.identity.foundation.offline.BusinessRoleEntity;

////////////////////////////////////////////////////////////////////////////////
// class IdentityRoleExporter
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>IdentityRoleExporter</code> acts as the service end point for
 ** the Oracle Identity Manager to off-board business roles information to an
 ** offlined XML file.
 ** <p>
 ** This service is intentionally used to extract roles from Oracle Identity
 ** Manager and their associate <code>Policies</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   3.0.0.0
 */
public class IdentityRoleExporter extends RoleExporter {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the vector with attributes which are valid to define on the task
   ** definition
   */
  private static final TaskAttribute[] attribute = {
    /** the task attribute with reconciliation object */
    TaskAttribute.build(RECONCILE_OBJECT,     "Role")
    /** the task attribute with reconciliation descriptor */
  , TaskAttribute.build(RECONCILE_DESCRIPTOR, TaskAttribute.MANDATORY)
    /** the task attribute that holds the value of the last run */
  , TaskAttribute.build(TIMESTAMP,            TaskAttribute.MANDATORY)
    /** the filename of the raw data  */
  , TaskAttribute.build(DATAFILE,             TaskAttribute.MANDATORY)
    /** the location from where the raw files will be loaded */
  , TaskAttribute.build(DATA_FOLDER,          TaskAttribute.MANDATORY)
    /** the location where the raw files should be copied after they are not successfully proceeded */
  , TaskAttribute.build(ERROR_FOLDER,         TaskAttribute.MANDATORY)
    /** the class name of the entity factory  */
  , TaskAttribute.build(MARSHALLER,           TaskAttribute.MANDATORY)
    /** the task attribute with entitlement prefix option */
  , TaskAttribute.build(ENTITLEMENT_PREFIX,   TaskAttribute.MANDATORY)
    /** the file encoding to use */
  , TaskAttribute.build(FILE_ENCODING,        TaskAttribute.MANDATORY)
  };

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>IdentityRoleExporter</code> scheduled job that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public IdentityRoleExporter() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributes (AbstractReconciliationTask)
  /**
   ** Returns the array with names which should be populated from the scheduled
   ** task definition of Oracle Identity Manager.
   **
   ** @return                    the array with names which should be populated.
   */
  @Override
  protected TaskAttribute[] attributes() {
    return attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  exportRole
  /**
   ** Factory method to create a particular {@link BusinessRoleEntity} to be
   ** exported to a XML file.
   **
   ** @param  name               the name of a particular
   **                            {@link BusinessRoleEntity} to export.
   **
   ** @return                    the {@link BusinessRoleEntity} created.
   **
   ** @throws TaskException      thrown if the processing of the event fails.
   */
  @Override
  protected BusinessRoleEntity exportRole(final String name)
    throws TaskException {

    final String method = "exportRole";
    trace(method, SystemMessage.METHOD_ENTRY);
    // start the task to gather performance metrics
    timerStart(method);

    final BusinessRoleEntity entity = new BusinessRoleEntity(name);

    timerStop(method);
    trace(method, SystemMessage.METHOD_EXIT);
    return entity;
  }
}