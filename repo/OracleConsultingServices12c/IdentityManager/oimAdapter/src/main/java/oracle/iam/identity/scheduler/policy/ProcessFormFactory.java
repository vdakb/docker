/*
    Oracle Deutschland GmbH

    This software is the confidential and proprietary information of
    Oracle Corporation. ("Confidential Information").  You shall not
    disclose such Confidential Information and shall use it only in
    accordance with the terms of the license  agreement you entered
    into with Oracle.

    ORACLE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
    SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
    IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
    PURPOSE, OR NON-INFRINGEMENT. ORACLE SHALL NOT BE LIABLE FOR ANY DAMAGES
    SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
    THIS SOFTWARE OR ITS DERIVATIVES.

    Copyright © 2008. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Scheduler Shared Library
    Subsystem   :   Virtual Resource Management

    File        :   ProcessFormFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ProcessFormFactory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2008-02-10  DSteding    First release version
*/

package oracle.iam.identity.scheduler.policy;

import com.thortech.xl.dataaccess.tcDataSet;
import com.thortech.xl.dataaccess.tcDataSetException;

import com.thortech.xl.dataobj.PreparedStatementUtil;

import com.thortech.xl.orb.dataaccess.tcDataAccessException;

import oracle.hst.foundation.SystemMessage;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.AbstractServiceTask;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.offline.ProcessForm;

////////////////////////////////////////////////////////////////////////////////
// abstract class ProcessFormFactory
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** A factory to create <code>Process Form</code>s and their instance
 ** relationships.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public abstract class ProcessFormFactory extends Factory {

  //////////////////////////////////////////////////////////////////////////////
  // statis final attribute
  //////////////////////////////////////////////////////////////////////////////

  private static final String formQuery  = "select sdk_key, sdk_name from sdk where sdk.sdk_key = ?";
  private static final String childQuery = "select sdk_key, sdk_name from sdk where sdk.sdk_key in (select sdh.sdh_child_key from sdh, sdk where sdk.sdk_key = sdh.sdh_parent_key and sdk.sdk_key = ? )";

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   findChildForm
  /**
   ** Returns the <code>Child Form</code> key from Oracle Identity Manager.
   **
   ** @param  task               the {@link AbstractServiceTask} which requests
   **                            to instantiated the wrapper.
   ** @param  formKey            the internal key of the
   **                            <code>Process Form</code>.
   **
   ** @return                    the {@link ProcessForm}s belonging to the
   **                            specified <code>formKey</code>.
   **
   ** @throws ResolverException  if the operation fails
   */
  public static ProcessForm createForm(final AbstractServiceTask task, long formKey)
    throws ResolverException {

    final String method = "createForm";
    task.trace(method, SystemMessage.METHOD_ENTRY);

    String[] parameter = new String[3];
    parameter[0] = TaskBundle.string(TaskMessage.ENTITY_FORM);
    parameter[1] = String.valueOf(formKey);
    task.debug(method, TaskBundle.format(TaskMessage.KEY_TORESOLVE, parameter));

    ProcessForm result = null;
    PreparedStatementUtil statement = new PreparedStatementUtil();
    statement.setStatement(task.provider(), formQuery);
    try {
      statement.setLong(1, formKey);
      statement.execute();
      tcDataSet dataSet  = statement.getDataSet();
      int       rowCount = dataSet.getRowCount();
      if (rowCount == 0)
        throw new ResolverException(TaskError.RESOURCE_NOT_FOUND, parameter);
      if (rowCount > 1)
        throw new ResolverException(TaskError.RESOURCE_AMBIGUOUS, parameter);

      parameter[2] = dataSet.getString("sdk_name");
      task.debug(method, TaskBundle.format(TaskMessage.KEY_RESOLVED, parameter));

      result = new ProcessForm(dataSet.getLong("sdk_key"), parameter[2]);
    }
    catch (tcDataAccessException e) {
      throw new ResolverException(TaskError.GENERAL, e);
    }
    catch (tcDataSetException e) {
      throw new ResolverException(TaskError.GENERAL, e);
    }
    finally {
      task.trace(method, SystemMessage.METHOD_EXIT);
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   findChildForm
  /**
   ** Returns the <code>Child Form</code> key from Oracle Identity Manager.
   **
   ** @param  task               the {@link AbstractServiceTask} which requests
   **                            to instantiated the wrapper.
   ** @param  parentForm         the <code>Process Form</code> that owns the
   **                            desired <code>Child Form</code>s.
   **
   ** @return                    the array of {@link ProcessForm}s which are
   **                            child forms of the specified
   **                            <code>parentForm</code> {@link ProcessForm}.
   **
   ** @throws ResolverException  if the operation fails
   */
  public static ProcessForm[] createChildForm(final AbstractServiceTask task, ProcessForm parentForm)
    throws ResolverException {

    ProcessForm[] result = null;
    PreparedStatementUtil statement = new PreparedStatementUtil();
    statement.setStatement(task.provider(), childQuery);
    try {
      statement.setLong(1, parentForm.key());
      statement.execute();
      tcDataSet dataSet = statement.getDataSet();
      result = new ProcessForm[dataSet.getRowCount()];
      for (int i = 0; i < result.length; i++) {
        dataSet.goToRow(i);
        result[i] = new ProcessForm(dataSet.getLong("sdk_key"), dataSet.getString("sdk_name"));
      }
    }
    catch (tcDataAccessException e) {
      throw new ResolverException(TaskError.GENERAL, e);
    }
    catch (tcDataSetException e) {
      throw new ResolverException(TaskError.GENERAL, e);
    }
    return result;
  }
}