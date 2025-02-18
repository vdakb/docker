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

    File        :   ITResourceOperation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ITResourceOperation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.deployment.task;

import org.apache.tools.ant.BuildException;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.iam.identity.deployment.type.ITResource;

import oracle.iam.identity.common.spi.ITResourceHandler;

////////////////////////////////////////////////////////////////////////////////
// class ITResourceOperation
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** Basic task operations on <code>IT Resource</code> instances of Oracle
 ** Identity Manager.
 ** <p>
 ** Works with Oracle Identity Manager 11.1.1 and later
 */
public class ITResourceOperation extends AbstractOperation {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ITResourceOperation</code> task that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ITResourceOperation() {
    // ensure inheritance
    super();

    // initialize attribute instances
    this.handler = new ITResourceHandler(this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setType
  /**
   ** Called to inject the argument for parameter <code>type</code>.
   **
   ** @param  type               the type of the <code>IT Resource</code>
   **                            instance in Identity Manager.
   */
  public void setType(final String type) {
    final ITResourceHandler handler = (ITResourceHandler)this.handler;
    handler.type(type);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setName

  /**
   ** Called to inject the argument for parameter
   ** <code>name</code>.
   **
   ** @param  name               the name of the <code>IT Resource</code>
   **                            instance in Identity Manager.
   */
  public void setName(final String name) {
    final ITResourceHandler handler = (ITResourceHandler)this.handler;
    handler.name(name);
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

    final ITResourceHandler handler = (ITResourceHandler)this.handler;
    switch (handler.operation()) {
      case create: handler.create(this);
        break;
      case delete: handler.delete(this);
        break;
      case modify: handler.modify(this);
        break;
      default: error(ServiceResourceBundle.format(ServiceError.OBJECT_OPERATION_INVALID, handler.operation().id(), ITResourceHandler.PREFIX));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (overridden)
  /**
   ** The entry point to validate the task to perform.
   **
   ** @throws BuildException     in case a validation error does occur.
   */
  @Override
  protected void validate()
    throws BuildException {

    this.handler.validate();

    // ensure inheritance
    super.validate();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredITResource
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>parameter</code>.
   **
   ** @param  resource           <code>true</code> to intend the output.
   **
   ** @throws BuildException     if the specified {@link ITResource} object is
   **                            already part of this operation.
   */
  public void addConfiguredITResource(final ITResource resource)
    throws BuildException {

    final ITResourceHandler handler = (ITResourceHandler)this.handler;
    handler.addInstance(resource.instance());
  }
}