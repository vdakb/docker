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

    System      :   Identity Manager Library
    Subsystem   :   Deployment Utilities 12c

    File        :   PartitionManager.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    PartitionManager.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.workflow.task;

import java.io.IOException;

import javax.management.ObjectName;

import org.apache.tools.ant.BuildException;

import oracle.fabric.management.folder.FolderManager;

import oracle.hst.deployment.ServiceException;

import oracle.hst.deployment.task.AbstractCompositeTask;

import oracle.hst.deployment.type.SOAServerContext;

import oracle.iam.identity.common.FeatureError;
import oracle.iam.identity.common.FeatureResourceBundle;

import oracle.iam.identity.common.spi.PartitionHandler;
import oracle.iam.identity.common.spi.PartitionInstance;

import oracle.iam.identity.workflow.type.Partition;
import oracle.iam.identity.workflow.type.Workflow;

////////////////////////////////////////////////////////////////////////////////
// class PartitionManager
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** Manage a Partition on Oracle SOA Server.
 ** <p>
 ** Works with Oracle Identity Manager 11.1.1 and later
 ** <p>
 ** If the Apache Ant task is used to manage partitions they are specified by
 ** the nested element <code>workflow:partition</code>. For example:
 ** <pre>
 **   &lt;workflow:partition name="default"/&gt;
 ** </pre>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class PartitionManager extends AbstractCompositeTask {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the indicator for an established connection */
  private boolean connected = false;

  /** the service provider executing the task operation */
  private final PartitionHandler handler;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>PartitionManager</code> task that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public PartitionManager() {
    // ensure inheritance
    super();

    // initialize the service provider instance
    this.handler = new PartitionHandler(this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPartition
  /**
   ** Sets the name of the partition to manage.
   **
   ** @param  partition               the name of the partition to manage.
   */
  public final void setPartition(final String partition) {
    this.handler.partition(partition);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setOperation
  /**
   ** Sets the oparation to manage the partition.
   **
   ** @param  operation               the oparation to manage the partition.
   */
  public final void setOperation(final Partition.Operation operation) {
    this.handler.command(PartitionInstance.Command.valueOf(operation.value()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onExecution (AbstractTask)
  /**
   ** Called by the project to let the task do its work.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @throws ServiceException   if something goes wrong with the build
   */
  @Override
  public void onExecution()
    throws ServiceException {

    try {
      final ObjectName invoker = FolderManager.getFolderLifeCycleMBean();
      this.handler.invoke(invoker);
    }
    catch (Exception e) {
      // digging in the exceptions that caused the case
      Throwable t = e.getCause();
      if (t.getCause() != null)
        t = t.getCause();
      throw new BuildException(FeatureResourceBundle.format(FeatureError.COMPOSITE_PARTITION_CONNECT, t.getLocalizedMessage()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredPartition
  /**
   ** Call by the ANT deployment to inject the argument for nested element
   ** <code>workflow</code>.
   **
   ** @param  partition           the {@link Partition} definition to add.
   **
   ** @throws ServiceException   if the specified {@link Workflow} object is
   **                            already part of this operation.
   */
  public void addConfiguredPartition(final Partition partition)
    throws ServiceException {

    this.handler.addInstance(partition.instance());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connected (overridden)
  /**
   ** Returns the state of connection.
   **
   ** @return                    the state of connection.
   */
  @Override
  protected final boolean connected() {
    return this.connected;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connect (overridden)
  /**
   ** Establish a connection to the JMX server and creates the MBeanServer to
   ** the use during task execution.
   ** <p>
   ** The caller is responsible for invokingThe caller is responsible for
   ** invoking this method prior to executing any other method.
   ** <p>
   ** The environment() method will be invoked prior to this method.
   **
   ** @throws ServiceException   if an error occurs attempting to establish a
   **                            connection.
   */
  @Override
  protected void connect()
    throws ServiceException {

    final SOAServerContext context = this.server();
    try {
      // create a connection that maps into a soa-infra instance to allow to
      // manage partitions
      FolderManager.initConnection(context.type().getValue(), context.host(), context.port(), context.username(), context.password());
      this.connected = true;
    }
    catch (Exception e) {
      // digging in the exceptions that caused the case
      Throwable t = e.getCause();
      if (t.getCause() != null)
        t = t.getCause();
      throw new BuildException(FeatureResourceBundle.format(FeatureError.COMPOSITE_PARTITION_CONNECT, t.getLocalizedMessage()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disconnect (overridden)
  /**
   ** Close a connection to the target system.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  @Override
  protected void disconnect()
    throws ServiceException {

    try {
      this.connected = false;
      FolderManager.closeConnection();
    }
    catch (IOException e) {
      final String message = FeatureResourceBundle.format(FeatureError.COMPOSITE_PARTITION_DISCONNECT, e.getLocalizedMessage());
      if (failonerror())
        throw new BuildException(message);
      else
        error(message);
    }
  }
}