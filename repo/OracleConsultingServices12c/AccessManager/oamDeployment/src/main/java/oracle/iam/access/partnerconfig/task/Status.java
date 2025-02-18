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

    File        :   Status.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Status.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.partnerconfig.task;

import org.apache.tools.ant.BuildException;

import oracle.hst.deployment.ServiceOperation;
import oracle.hst.deployment.ServiceException;

import oracle.iam.access.partnerconfig.type.PartnerType;

import oracle.iam.access.common.spi.FederationPartnerType;

////////////////////////////////////////////////////////////////////////////////
// class Status
// ~~~~~ ~~~~~~
/**
 ** Invokes the Domain Runtime MBean to list <code>Federation Partner</code>s
 ** configured in an Oracle Access Manager infrastructure belonging to a specific
 ** type.
 ** <p>
 ** A <code>Federation Partner</code> is a entity that contain all artifacts
 ** required to provide standard Federation Services.
 ** <p>
 ** Each <code>Federation Partner</code> is an independent entity.
 */
public class Status extends Invoker {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected FederationPartnerType    type;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Status</code> Ant task that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Status() {
    // ensure inheritance
    super(ServiceOperation.print);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredType
  /**
   ** Call by the ANT deployment to inject the argument for adding a type.
   **
   ** @param  instance           the partner type to add.
   **
   ** @throws BuildException     if this instance is already referencing a
   **                            {@link PartnerType} with the same name.
   */
  public void addConfiguredType(final PartnerType instance)
    throws BuildException {

    this.handler.add(FederationPartnerType.from(instance.getValue()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onExecution (overridden)
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

    this.handler.status(connection());
  }
}