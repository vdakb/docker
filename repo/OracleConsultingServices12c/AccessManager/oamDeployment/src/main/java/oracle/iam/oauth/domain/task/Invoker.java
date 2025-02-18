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

    File        :   Invoker.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Invoker.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.oauth.domain.task;

import org.apache.tools.ant.BuildException;

import oracle.hst.deployment.ServiceOperation;

import oracle.hst.deployment.task.AbstractRESTfulTask;

import oracle.iam.oauth.domain.type.Instance;

import oracle.iam.oauth.common.spi.DomainHandler;

////////////////////////////////////////////////////////////////////////////////
// abstract class Invoker
// ~~~~~~~~ ~~~~~ ~~~~~~~
/**
 ** Invokes the Runtime REST API to maintain <code>Identity Domain</code>s in
 ** Oracle Access Manager infrastructure.
 ** <p>
 ** A <code>Identity Domain</code> is a entity that contain all artifacts
 ** required to provide standard OAuth Services .
 ** <p>
 ** Each <code>Identity Domain</code> is an independent entity.
 ** <br>
 ** One of the primary use cases of the <code>Identity Domain</code> is for
 ** multi tenants deployments. Each <code>Identity Domain</code> will correspond
 ** to a tenant. This can apply to different departments in an organization if
 ** there is a need for independence. This will also be useful for cloud
 ** deployments where each <code>Identity Domain</code> can correspond to a
 ** separate tenant or entity. The following artifacts are just some of the
 ** components configured within an OAuth Services <code>Identity Domain</code>.
 ** <ul>
 **   <li>One or more <code>Resource Client</code>s
 **   <li>One or more <code>Resource Server</code>s
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class Invoker extends AbstractRESTfulTask {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final DomainHandler handler;

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
   **                              <li>{@link ServiceOperation#create}
   **                              <li>{@link ServiceOperation#delete}
   **                              <li>{@link ServiceOperation#modify}
   **                            </ul>
   */
  protected Invoker(final ServiceOperation operation) {
    // ensure inheritance
    super();

    // initialize instance attribute
    this.handler = new DomainHandler(this, operation);
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
   ** @throws BuildException     in case a validation error does occur.
   */
  @Override
  protected void validate() {
    // validate the instance data first
    this.handler.validate();

    // ensure inheritance
    super.validate();
  }
}