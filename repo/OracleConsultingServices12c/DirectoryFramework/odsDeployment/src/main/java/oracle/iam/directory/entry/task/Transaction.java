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

    System      :   Oracle Directory Service Utility Library
    Subsystem   :   Deployment Utilities 11g

    File        :   Transaction.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Transaction.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2012-12-09  DSteding    First release version
*/

package oracle.iam.directory.entry.task;

import org.apache.tools.ant.BuildException;

import oracle.iam.directory.entry.type.Entry;

import oracle.iam.directory.common.spi.handler.EntryHandler;

import oracle.iam.directory.common.task.FeatureDirectoryTask;

////////////////////////////////////////////////////////////////////////////////
// abstract class Transaction
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~
/**
 ** Implements common transactional operations on entries in a Directory
 ** Information Tree (DIT).
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class Transaction extends FeatureDirectoryTask {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final EntryHandler handler;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Transaction</code> event handler that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Transaction() {
    // ensure inheritance
    super();

    // initialize instance
    this.handler = new EntryHandler(this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addEntry
  /**
   ** Call by the ANT deployment to inject the argument for adding an
   ** {@link Entry}.
   **
   ** @param  entry              the entry to add.
   */
  public final void addEntry(final Entry entry)
    throws BuildException {

    addConfiguredEntry(entry);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredEntry
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** {@link Entry}.
   **
   ** @param  entry              the {@link Entry} to add.
   */
  public final void addConfiguredEntry(final Entry entry) {
    this.handler.add(entry.instance());
  }
}