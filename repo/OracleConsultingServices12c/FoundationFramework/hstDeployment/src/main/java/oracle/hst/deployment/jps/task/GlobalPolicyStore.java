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

    System      :   Oracle Consulting Services Foundation Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   GlobalPolicyStore.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    GlobalPolicyStore.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment.jps.task;

import org.apache.tools.ant.BuildException;

import oracle.hst.deployment.ServiceException;

import oracle.hst.deployment.task.AbstractManagedBean;

import oracle.hst.deployment.jps.type.Member;

import oracle.hst.deployment.spi.GlobalPolicyHandler;

////////////////////////////////////////////////////////////////////////////////
// class GlobalPolicyStore
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** Invokes the Runtime JMX Bean to configured the global policy store.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class GlobalPolicyStore extends AbstractManagedBean {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final GlobalPolicyHandler handler;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>GlobalPolicyStore</code> Ant task that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public GlobalPolicyStore() {
    // ensure inheritance
    super();

    // initialize instance
    this.handler = new GlobalPolicyHandler(this);
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

    this.handler.execute(this.connection());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (overridden)
  /**
   ** The entry point to validate the task to perform.
   **
   ** @throws BuildException     in case an error does occur.
   */
  @Override
  protected void validate() {
    this.handler.validate();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredAssign
  /**
   ** Call by the ANT deployment to inject the argument for nested parameter
   ** <code>grant</code>.
   **
   ** @param  member             the {@link Member.Assign} to manage in
   **                            grant operation.
   **
   ** @throws BuildException     if a {@link Member.Assign } has missing
   **                            data.
   */
  public void addConfiguredAssign(final Member.Assign member)
    throws BuildException {

    this.handler.addAssign(member.principal());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredRemove
  /**
   ** Call by the ANT deployment to inject the argument for nested parameter
   ** <code>revoke</code>.
   **
   ** @param  member             the {@link Member.Remove} to manage in
   **                            revoke operation.
   **
   ** @throws BuildException     if a {@link Member.Remove} has missing
   **                            data.
   */
  public void addConfiguredRemove(final Member.Remove member)
    throws BuildException {

    this.handler.addRemove(member.principal());
  }
}