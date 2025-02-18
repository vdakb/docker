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

    Copyright © 2011. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Enterprise User Security

    File        :   DatabaseSchemaReconciliation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DatabaseSchemaReconciliation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2011-03-01  DSteding    First release version
*/

package oracle.iam.identity.eus.service.reconciliation;

import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.SystemConstant;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.TaskAttribute;
import oracle.iam.identity.foundation.AbstractLookup;

import oracle.iam.identity.foundation.ldap.DirectoryConstant;
import oracle.iam.identity.foundation.ldap.DirectoryResource;
import oracle.iam.identity.foundation.ldap.DirectoryConnector;

////////////////////////////////////////////////////////////////////////////////
// class DatabaseSchemaReconciliation
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>DatabaseSchemaReconciliation</code> act as the service end point
 ** for the Identity Manager to reconcile user information from an Enterprise
 ** Database.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public class DatabaseSchemaReconciliation extends SchemaReconciliation {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which must be defined on this task to specify which Lookup
   ** Definion is used for Domains
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String   DATABASE_LOOKUP = "Database Lookup";

  /**
   ** Attribute tag which must be defined on this task to specify which
   ** Enterprise Domain will be reconcilied
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String   DATABASE_NAME = "Database Name";

  /**
   ** the vector with attributes which are valid to define on the task
   ** definition
   */
  private static final TaskAttribute[] attribute = {
    /** the task attribute IT Resource */
    TaskAttribute.build(IT_RESOURCE,          TaskAttribute.MANDATORY)
    /** the task attribute with reconciliation target */
  , TaskAttribute.build(RECONCILE_OBJECT,     TaskAttribute.MANDATORY)
    /** the task attribute with reconciliation descriptor */
  , TaskAttribute.build(RECONCILE_DESCRIPTOR, TaskAttribute.MANDATORY)
    /** the task attribute that holds the value of the last run */
  , TaskAttribute.build(TIMESTAMP,            TaskAttribute.MANDATORY)
    /** the name of the Lookup Definition where all databases are listed */
  , TaskAttribute.build(DATABASE_LOOKUP,      TaskAttribute.MANDATORY)
    /**
     ** the name of the Enterprise Database which is reconciled.
     ** <br>
     ** The name will be validate for existence against the Lookup Definition
     ** their name is specified by the attribute defined above.
     */
  , TaskAttribute.build(DATABASE_NAME,        TaskAttribute.MANDATORY)
    /**
     ** the task attribute that specifies the search scope of the query
     ** <br>
     ** Allowed values are OneLevel | SubTree | Object
     */
  , TaskAttribute.build(SEARCHSCOPE,          DirectoryConstant.SCOPE_SUBTREE)
    /** the task attribute that specifies the filter applied to the query */
  , TaskAttribute.build(SEARCHFILTER,         SystemConstant.EMPTY)
    /**
     ** the task attribute that specifies that the filter should be also use the
     ** timestamp attributes in the search to decrease the result set size in
     ** operational mode
     */
  , TaskAttribute.build(INCREMENTAL,          SystemConstant.TRUE),
  };

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String databaseSearchBase;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DatabaseSchemaReconciliation</code> scheduled
   ** task that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public DatabaseSchemaReconciliation() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchBase (overridden)
  /**
   ** Returns the releative distinguished name of the search base.
   **
   ** @return                    the releative distinguished name of the search
   **                            base.
   */
  @Override
  public final String searchBase() {
    return this.databaseSearchBase;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributes (overridden)
  /**
   ** Returns the array with names which should be populated from the scheduled
   ** task definition of Oracle Identity Manager.
   **
   ** @return                    the array with names which should be populated.
   */
  @Override
  protected final TaskAttribute[] attributes() {
    return attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beforeExecution (overridden)
  /**
   ** Check if the lookup code defined in the Schedule Job attribute
   ** "Domain Lookup" exists.
   ** <br>
   ** If not the process will be terminated.
   **
   ** @throws TaskException    in case an error does occur.
   */
  @Override
  protected void beforeExecution()
    throws TaskException {

    // ensure inheritance
    super.beforeExecution();

    final String method  = "beforeExecution";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    String   decoded   = stringValue(DATABASE_NAME);
    final String[] parameter = { stringValue(DATABASE_LOOKUP), decoded};
    if (this.connector().entitlementPrefixRequired()) {
      final DirectoryResource resource = this.resource();
      decoded  = String.format("%s~%s", resource.name(), decoded);
    }

    try {
      final AbstractLookup lookup    = new AbstractLookup(this, parameter[0]);
      // check if the database lookup contains a database with the specified name
      // defined in the parameter of the scheduled job
      if (lookup.containsValue(decoded)) {
        this.databaseSearchBase = lookup.encodedValue(decoded);
        if (this.connector().entitlementPrefixRequired())
          this.databaseSearchBase = DirectoryConnector.unescapePrefix(this.databaseSearchBase);
      }
      else {
        TaskException e = new TaskException(TaskError.LOOKUP_INVALID_VALUE, parameter);
        error(method, e.getLocalizedMessage());
        throw e;
      }
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }
}