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

    File        :   AbstractOperation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractOperation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.deployment.task;

import org.apache.tools.ant.BuildException;

import oracle.hst.deployment.spi.AbstractHandler;

import oracle.iam.identity.common.FeaturePlatformTask;

import oracle.iam.identity.deployment.type.Operation;
import oracle.iam.identity.deployment.type.Parameter;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractOperation
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** Basic task operations on objects in Identity Manager.
 ** <p>
 ** Works with Oracle Identity Manager 11.1.1 and later
 */
public abstract class AbstractOperation extends FeaturePlatformTask {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the service provider executing the task operation */
  protected AbstractHandler handler;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractOperation</code> task that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected AbstractOperation() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setName
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>name</code>.
   **
   ** @param  name               the name of the object in Oracle
   **                            Identity Manager.
   */
  public void setName(final String name) {
    this.handler.name(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setOperation
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>operation</code>.
   **
   ** @param  operation          the {@link Operation} to execute in Oracle
   **                            Identity Manager.
   **
   ** @throws BuildException     if the specified {@link Operation} is not
   **                            valid.
   */
  public void setOperation(final Operation operation)
    throws BuildException {

    this.handler.operation(operation.value());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredParameter
  /**
   ** Add the specified value pairs to the parameters that has to be applied.
   **
   ** @param  parameter          the named value pairs to be applied on the
   **                            Identity Manager Organization Instance.
   **
   ** @throws BuildException     if the specified {@link Parameter} is already
   **                            part of the parameter mapping.
   */
  public void addConfiguredParameter(final Parameter parameter)
    throws BuildException {

    this.handler.addParameter(parameter.name(), parameter.value());
  }
}