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

package oracle.iam.access.identitystore.task;

import org.apache.tools.ant.BuildException;

import oracle.hst.deployment.ServiceOperation;

import oracle.iam.access.common.task.FoundationInvokerTask;

import oracle.iam.access.common.spi.IdentityStoreHandler;

import oracle.iam.access.identitystore.type.Instance;

////////////////////////////////////////////////////////////////////////////////
// abstract class Invoker
// ~~~~~~~~ ~~~~~ ~~~~~~~
/**
 ** Invokes the Runtime JMX Bean to maintain <code>User Identity Store</code>s
 ** in Oracle Access Manager infrastructure.
 ** <p>
 ** A <code>User Identity Store</code> is a centralized LDAP repository in which
 ** an aggregation of Administrator and user-oriented data is stored and
 ** maintained in an organized way.
 ** <p>
 ** Oracle Access Management supports multiple LDAP vendors, and multiple LDAP
 ** stores can be registered for use by Oracle Access Management and its
 ** services.
 ** <br>
 ** Oracle Access Management addresses each user population and LDAP directory
 ** store as an identity domain. Each identity domain maps to a configured LDAP
 ** <code>User Identity Store</code> that must be registered with Oracle Access
 ** Management.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class Invoker extends FoundationInvokerTask {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final IdentityStoreHandler handler;

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
    this.handler = new IdentityStoreHandler(this, operation);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredInstance
  /**
   ** Call by the ANT deployment to inject the argument for adding a instance.
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