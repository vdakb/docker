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
    Subsystem   :   CSV Flatfile Connector

    File        :   AccountTargetReconciliation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AccountTargetReconciliation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.csv.service.reconciliation;

import java.util.Map;

import oracle.hst.foundation.SystemMessage;

import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.TaskAttribute;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.utility.file.CSVDescriptor;

////////////////////////////////////////////////////////////////////////////////
// class AccountTargetReconciliation
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>AccountTargetReconciliation</code> implements the base
 ** functionality of a service end point for the Oracle Identity Manager
 ** Scheduler which handles user data provided by CSV flatfile.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public class AccountTargetReconciliation extends TargetReconciliation {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the array with attributes which are valid to define on the task
   ** definition
   */
  private static final TaskAttribute[] attribute = {
    /** the task attribute with reconciliation target */
    TaskAttribute.build(RECONCILE_OBJECT,        TaskAttribute.MANDATORY)
    /** the task attribute with reconciliation descriptor */
  , TaskAttribute.build(RECONCILE_DESCRIPTOR,    TaskAttribute.MANDATORY)
    /** the task attribute that holds the value of the last run */
  , TaskAttribute.build(TIMESTAMP,               TaskAttribute.MANDATORY)
    /** the location from where the raw files will be loaded */
  , TaskAttribute.build(DATA_FOLDER,             TaskAttribute.MANDATORY)
    /** the location where the raw files should be copied after they are not successfully proceeded */
  , TaskAttribute.build(ERROR_FOLDER,            TaskAttribute.MANDATORY)
    /** the location where the raw files should be copied after they are successfully proceeded */
  , TaskAttribute.build(WORKING_FOLDER,          TaskAttribute.MANDATORY)
    /** the fullqualified filename which are specifying the mapping for import  */
  , TaskAttribute.build(DATA_DESCRIPTOR,         TaskAttribute.MANDATORY)
    /** the filename of the raw data  */
  , TaskAttribute.build(DATA_FILE,               TaskAttribute.MANDATORY)
    /** the filename of the proceeded data  */
  , TaskAttribute.build(PROCEED_FILE,            TaskAttribute.MANDATORY)
    /** the file encoding to use */
  , TaskAttribute.build(FILE_ENCODING,           TaskAttribute.MANDATORY)
    /** the character to separate single values */
  , TaskAttribute.build(SINGLE_VALUED_SEPARATOR, TaskAttribute.MANDATORY)
    /** the character to separate single values */
  , TaskAttribute.build(MULTI_VALUED_SEPARATOR,  TaskAttribute.MANDATORY)
    /** the flag that indicates that the files should be compared  */
  , TaskAttribute.build(INCREMENTAL,             TaskAttribute.MANDATORY)
    /** the character to enclose values */
  , TaskAttribute.build(ENCLOSING_CHARACTER,     "\"")
  };

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>AccountTargetReconciliation</code> task adapter
   ** that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AccountTargetReconciliation() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributes (AbstractSchedulerBaseTask)
  /**
   ** Returns the array with names which should be populated from the
   ** scheduled task definition of Oracle Identity Manager.
   **
   ** @return                    the array with names which should be populated.
   */
  @Override
  protected TaskAttribute[] attributes() {
    return attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processSubject (Reconciliation)
  /**
   ** Do all action which should take place for reconciliation for a particular
   ** subject.
   ** <br>
   ** This will do target reconciliation of Oracle Identity Manager Users.
   **
   ** @param  identifier         the composed identifier of the {@link Map} to
   **                            reconcile.
   ** @param  transaction        the transaction code of the {@link Map} to
   **                            reconcile.
   ** @param  data               the {@link Map} to reconcile.
   ** @param  pass               the pass the subjec ist loaded from the file.
   **
   ** @throws TaskException      if an error occurs processing data.
   */
  @Override
  protected final void processSubject(final String identifier, final String transaction, final Map<String, Object> data, final int pass)
    throws TaskException {

    final String method = "processSubject";
    trace(method, SystemMessage.METHOD_ENTRY);

    String[] parameter  = {TaskBundle.string(TaskMessage.ENTITY_ACCOUNT),  identifier};
    // produce the logging output only if the logging level is enabled for
    if (this.logger().debugLevel())
     debug(method, TaskBundle.format(TaskMessage.ENTITY_RECONCILE, parameter));

    try {
      // handle deletion at first
      if (CSVDescriptor.DELETE.equalsIgnoreCase(transaction)) {
        // create the reconciliation event for deletion
        processDeleteEvent(data);
      }
      else {
        // create a reconciliation event which have all data provided, means
        // no further child data processing necessary
        processChangeLogEvent(data);
      }
    }
    catch (Exception e) {
      throw new TaskException(e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }
}