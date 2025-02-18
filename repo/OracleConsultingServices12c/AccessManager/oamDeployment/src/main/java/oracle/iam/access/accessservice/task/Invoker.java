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
    Subsystem   :   Deployment Utilities 11g

    File        :   Invoker.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Invoker.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.accessservice.task;

import org.apache.tools.ant.BuildException;

import oracle.hst.deployment.ServiceOperation;

import oracle.iam.access.accessservice.type.Service;

import oracle.iam.access.common.task.DomainInvokerTask;

import oracle.iam.access.common.spi.AvailableServiceHandler;

////////////////////////////////////////////////////////////////////////////////
// abstract class Invoker
// ~~~~~~~~ ~~~~~ ~~~~~~~
/**
 ** Invokes the Runtime JMX Bean to maintain access servers in Oracle Access
 ** Manager infrastructure.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class Invoker extends DomainInvokerTask {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final AvailableServiceHandler handler;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Invoker</code> Ant task that allows use as a JavaBean.
   **
   ** @param  operation          the {@link ServiceOperation} to execute either
   **                            <ul>
   **                              <li>{@link ServiceOperation#enable}
   **                              <li>{@link ServiceOperation#disable}
   **                              <li>{@link ServiceOperation#modify}
   **                            </ul>
   */
  protected Invoker(final ServiceOperation operation) {
    // ensure inheritance
    super();


    // initialize instance attribute
    this.handler = new AvailableServiceHandler(this, operation);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredService
  /**
   ** Call by the ANT deployment to inject the argument for adding a parameter.
   **
   ** @param  instance           the instance to add.
   **
   ** @throws BuildException     if this instance is already referencing an
   **                            {@link Service} with the same name.
   */
  public void addConfiguredService(final Service instance)
    throws BuildException {

    this.handler.add(instance.delegate());
  }

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

    // validate the instance data first
    this.handler.validate();

    // ensure inheritance
    super.validate();
  }
}