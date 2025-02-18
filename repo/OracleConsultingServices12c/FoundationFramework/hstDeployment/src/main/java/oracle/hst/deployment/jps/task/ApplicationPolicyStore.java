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

    File        :   ApplicationPolicyStore.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ApplicationPolicyStore.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment.jps.task;

import org.apache.tools.ant.BuildException;

import oracle.hst.deployment.ServiceException;

import oracle.hst.deployment.task.AbstractManagedBean;

import oracle.hst.deployment.spi.ApplicationPolicyHandler;

import oracle.hst.deployment.jps.type.ApplicationPolicy;

////////////////////////////////////////////////////////////////////////////////
// class ApplicationPolicyStore
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Invokes the Runtime JMX Bean to configured the application policy store.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ApplicationPolicyStore extends AbstractManagedBean {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final ApplicationPolicyHandler handler;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ApplicationPolicyStore</code> Ant task that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ApplicationPolicyStore() {
    // ensure inheritance
    super();

    // initialize instance
    this.handler = new ApplicationPolicyHandler(this);
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
  // Method:   addConfiguredPolicy
  /**
   ** Call by the ANT deployment to inject the argument for nested parameter
   ** <code>policy</code>.
   **
   ** @param  policy             the {@link ApplicationPolicy} to manage in any
   **                            operation.
   **
   ** @throws BuildException     if an {@link ApplicationPolicy} with the same
   **                            key is already assigned to the task.
   */
  public void addConfiguredPolicy(final ApplicationPolicy policy)
    throws BuildException {

    // initialize instance
    this.handler.add(policy.instance());
  }
}