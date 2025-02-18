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

    System      :   Oracle Access Manager Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   Servlet.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Servlet.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.accessagent.task;

import org.apache.tools.ant.BuildException;

import oracle.hst.deployment.ServiceOperation;
import oracle.hst.deployment.ServiceException;

import oracle.hst.deployment.task.AbstractServletTask;

import oracle.iam.access.accessagent.type.Instance;

import oracle.iam.access.common.spi.AccessAgentHandler;

////////////////////////////////////////////////////////////////////////////////
// abstract class Servlet
// ~~~~~~~~ ~~~~~ ~~~~~~~
/**
 ** Invokes the HTTP Registration Service to maintain configuration artifacts in
 ** Oracle Access Manager.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class Servlet extends AbstractServletTask {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final AccessAgentHandler handler;

   //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Servlet</code> Ant task that allows use as a JavaBean.
   **
   ** @param  operation          the {@link ServiceOperation} to execute either
   **                            <ul>
   **                              <li>{@link ServiceOperation#create}
   **                              <li>{@link ServiceOperation#delete}
   **                              <li>{@link ServiceOperation#modify}
   **                              <li>{@link ServiceOperation#print}
   **                              <li>{@link ServiceOperation#validate}
   **                            </ul>
   */
  protected Servlet(final ServiceOperation operation) {
    // ensure inheritance
    super();

    // initialize instance attribute
    this.handler = new AccessAgentHandler(this, operation);
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

    this.handler.execute();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredInstance
  /**
   ** Call by the ANT deployment to inject the argument for adding a parameter.
   **
   ** @param  instance           the instance to add.
   **
   ** @throws BuildException     if this instance is already referencing an
   **                            {@link Instance} with the same name.
   */
  public void addConfiguredInstance(final Instance instance)
    throws BuildException {

    this.handler.add(instance.delegate());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (overridden)
  /**
   ** The entry point to validate the task to perform.
   **
   ** @throws BuildException     in case an error does occur.
   */
  @Override
  protected void validate() {
    // validate the instance data first
    this.handler.validate();

    // ensure inheritance
    super.validate();
  }
}