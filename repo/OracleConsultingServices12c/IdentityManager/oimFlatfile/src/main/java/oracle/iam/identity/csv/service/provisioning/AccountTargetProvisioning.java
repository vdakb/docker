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

    File        :   AccountTargetProvisioning.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AccountTargetProvisioning.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.csv.service.provisioning;

import java.util.Map;

import java.io.IOException;

import java.sql.PreparedStatement;

import oracle.hst.foundation.SystemMessage;

import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.TaskAttribute;

import oracle.iam.identity.utility.file.CSVRecord;
import oracle.iam.identity.utility.file.CSVWriter;

import oracle.iam.identity.foundation.resource.TaskBundle;

////////////////////////////////////////////////////////////////////////////////
// class AccountTargetProvisioning
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>AccountTargetProvisioning</code> implements the base functionality
 ** of a service end point for the Oracle Identity Manager Scheduler which
 ** handles data that has to be dumped to a CSV flatfile.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public class AccountTargetProvisioning extends EntityProvisioning {

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
    /**
     ** the task attribute to specify the threshold of the difference between
     ** the creation date and the update date of an entity or the difference
     ** between the update date and the last execution time
     */
  , TaskAttribute.build(THRESHOLD,               TaskAttribute.MANDATORY)
    /** the location to where the raw files will be written */
  , TaskAttribute.build(DATA_FOLDER,             TaskAttribute.MANDATORY)
    /** the fullqualified filename which are specifying the mapping for export  */
  , TaskAttribute.build(DATA_DESCRIPTOR,         TaskAttribute.MANDATORY)
    /** the filename of the raw data  */
  , TaskAttribute.build(DATA_FILE,               TaskAttribute.MANDATORY)
    /** the file encoding to use */
  , TaskAttribute.build(FILE_ENCODING,           TaskAttribute.MANDATORY)
    /** the character to separate single values */
  , TaskAttribute.build(SINGLE_VALUED_SEPARATOR, TaskAttribute.MANDATORY)
    /** the character to separate single values */
  , TaskAttribute.build(MULTI_VALUED_SEPARATOR,  TaskAttribute.MANDATORY)
    /** the character to enclose values */
  , TaskAttribute.build(ENCLOSING_CHARACTER,     "\"")
  };

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>AccountTargetProvisioning</code> task adapter
   ** that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AccountTargetProvisioning() {
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
  // Method:   prepareStatement (Provisioning)
  /**
   ** Callback method invoked through the execution of this task to prepare the
   ** bind values to the statement.
   **
   ** @param  statement          the JDBC {@link PreparedStatement} to prepare
   ** @param  lastExecution      the String representation of the date this task
   **                            was last executed.
   */
  @Override
  protected void prepareStatement(final PreparedStatement statement, final String lastExecution)
    throws TaskException {

    final String method = "prepareStatement";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      statement.setLong(1,   this.queryBuilder().objectKey());
      statement.setString(2, lastExecution);
      statement.setString(3, DATEPATTERN);
      statement.setString(4, lastExecution);
      statement.setString(5, DATEPATTERN);
    }
    catch (Exception e) {
      throw new TaskException(e);
    }
    finally {
    trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processSubject (Provisioning)
  /**
   ** Do all action which should take place for provisioning for a particular
   ** subject.
   **
   ** @param  transaction        the transaction mark how the entity should
   **                            handled.
   ** @param  subject            the {@link Map} to provision.
   **
   ** @throws TaskException      if an error occurs processing data.
   */
  @Override
  protected void processSubject(final String transaction, final Map<String, Object> subject)
    throws TaskException {

    final String method = "processSubject";
    trace(method, SystemMessage.METHOD_ENTRY);

    final String[] identifier = dataDescriptor().identifier(subject);
    String[] parameter = {TaskBundle.string(TaskMessage.ENTITY_ACCOUNT), identifier[0] };
    debug(method, TaskBundle.format(TaskMessage.ENTITY_PROVISION, parameter));

    try {
      final CSVWriter writer = this.writer();
      writer.put(transaction);

      // write the subject by applying the transformation rules configured in the
      // descriptor used by this scheduled task
      CSVRecord.write(processor(), writer, subject, true);
    }
    catch (IOException e) {
      throw new TaskException(e);
    }
    catch (TaskException e) {
      parameter[0] = identifier[0];
      parameter[1] = reconcileObject();
      error(method, TaskBundle.format(TaskMessage.PROVISIONING_ERROR, parameter));
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }
}