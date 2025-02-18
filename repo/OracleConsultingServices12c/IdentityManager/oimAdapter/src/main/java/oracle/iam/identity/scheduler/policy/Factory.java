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

    File        :   Factory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Factory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2008-02-10  DSteding    First release version
*/

package oracle.iam.identity.scheduler.policy;

import Thor.API.tcResultSet;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcColumnNotFoundException;

import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.logging.Loggable;
import oracle.hst.foundation.logging.TableFormatter;

import oracle.hst.foundation.resource.SystemBundle;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskException;

////////////////////////////////////////////////////////////////////////////////
// abstract class Factory
// ~~~~~~~~ ~~~~~ ~~~~~~~
/**
 ** A factory to create objects and their instance relationships.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class Factory {

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dumpResultSet
  /**
   ** Dumps a {@link tcResultSet} to the Logger
   **
   ** @param  loggable           the {@link Loggable} to spool out the formatted
   **                            {@link tcResultSet} content.
   ** @param  resultSet          the {@link tcResultSet} to dump.
   **
   ** @throws TaskException      if the operation fails.
   */
  public static void dumpResultSet(final Loggable loggable, final tcResultSet resultSet)
    throws TaskException {

    String[] column = resultSet.getColumnNames();
    int i = 0;
    final TableFormatter table  = new TableFormatter()
    .header(SystemBundle.string(SystemMessage.COLUMN_NAME))
    .header(SystemBundle.string(SystemMessage.COLUMN_VALUE))
    ;
    try {
      for (; i < column.length; i++)
        table.row().column(column[i]).column(resultSet.getStringValue(column[i]));
    }
    catch (tcColumnNotFoundException e) {
      throw new TaskException(TaskError.COLUMN_NOT_FOUND, column[i]);
    }
    catch (tcAPIException e) {
      throw TaskException.general(e);
    }
    finally {
      final StringBuilder builder = new StringBuilder();
      table.print(builder);
      loggable.debug(SystemConstant.EMPTY, builder.toString());
    }
  }
}