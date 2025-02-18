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

    File        :   WebService.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    WebService.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment.wsm.task;

import org.apache.tools.ant.BuildException;

import oracle.hst.deployment.ServiceException;

import oracle.hst.deployment.task.AbstractManagedBean;

import oracle.hst.deployment.wsm.type.Service;

import oracle.hst.deployment.spi.WebServicePolicyHandler;

////////////////////////////////////////////////////////////////////////////////
// class WebService
// ~~~~~ ~~~~~~~~~~
/**
 ** Invokes the Runtime JMX Bean to configure Oracle WebLogic WebService
 ** metadata.
 ** <p>
 ** <b>Note</b>:
 ** Class needs to be declared <code>public</code> to allow ANT introspection.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class WebService extends AbstractManagedBean {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final WebServicePolicyHandler handler = new WebServicePolicyHandler(this);

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>WebService</code> Ant task that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public WebService() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onExecution (AbstractTask)
  /**
   ** Called by the project to let the task do its work.
   ** <p>
   ** This method may be called more than once, if the task is invoked more
   ** than once. For example, if target1 and target2 both depend on target3,
   ** then running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @throws ServiceException   if something goes wrong with the build
   */
  @Override
  public void onExecution()
    throws ServiceException {

   // performs all action on the instances assigned to this task by initializing
   // JMX connection lazy to attach/detach policies to or from the web service
   // endpoints
    this.handler.execute(connection());
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
  // Method:   addConfiguredServer
  /**
   ** Call by the ANT deployment to inject the argument for nested parameter
   ** <code>server</code>.
   **
   ** @param  service            the {@link Service.Server} to manage in any
   **                            operation.
   **
   ** @throws BuildException     if a {@link Service.Server} with the same key
   **                            is already assigned to the task.
   */
  public void addConfiguredServer(final Service.Server service)
    throws BuildException {

    this.handler.add(service.instance());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredClient
  /**
   ** Call by the ANT deployment to inject the argument for nested parameter
   ** <code>client</code>.
   **
   ** @param  service            the {@link Service.Client} to manage in any
   **                            operation.
   **
   ** @throws BuildException     if a {@link Service.Client} with the same key
   **                            is already assigned to the task.
   */
  public void addConfiguredClient(final Service.Client service)
    throws BuildException {

    this.handler.add(service.instance());
  }
}