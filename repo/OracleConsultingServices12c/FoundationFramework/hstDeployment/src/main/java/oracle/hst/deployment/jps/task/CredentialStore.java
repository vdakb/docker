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

    File        :   CredentialStore.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    CredentialStore.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment.jps.task;

import org.apache.tools.ant.BuildException;

import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceOperation;

import oracle.hst.deployment.spi.CredentialStoreHandler;

import oracle.hst.deployment.task.AbstractManagedBean;

import oracle.hst.deployment.jps.type.Action;
import oracle.hst.deployment.jps.type.Alias;
import oracle.hst.deployment.jps.type.Credential;

////////////////////////////////////////////////////////////////////////////////
// class CredentialStore
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Invokes the Runtime JMX Bean to configured the credential store.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class CredentialStore extends AbstractManagedBean {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final CredentialStoreHandler handler;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>CredentialStore</code> Ant task that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public CredentialStore() {
    // ensure inheritance
    super();

    // initialize instance
    this.handler = new CredentialStoreHandler(this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAlias
  /**
   ** Sets the name of the alias.
   **
   ** @param  alias              the name of the alias the runtime server
   **                            has to connect to.
   */
  public final void setAlias(final String alias) {
    this.handler.name(alias);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAction
  /**
   ** Sets the action to apply on the alias.
   **
   ** @param  action             the action to apply on the alias.
   */
  public final void setAction(final Action action) {
    this.handler.action(ServiceOperation.from(action.getValue()));
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
  // Method:   addConfiguredAlias
  /**
   ** Call by the ANT deployment to inject the argument for nested parameter
   ** <code>alias</code>.
   **
   ** @param  alias              the {@link Alias} to manage in any
   **                            operation.
   **
   ** @throws BuildException     if an {@link Alias} with the same key is already
   **                            assigned to the task.
   */
  public void addConfiguredAlias(final Alias alias)
    throws BuildException {

    this.handler.add(alias.instance());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredCredential
  /**
   ** Call by the ANT deployment to inject the argument for nested parameter
   ** <code>credential</code>.
   **
   ** @param  credential         the {@link Credential} to manage in any
   **                            operation.
   **
   ** @throws BuildException     if a {@link Credential} has missing data.
   */
  public void addConfiguredCredential(final Credential credential)
    throws BuildException {

    final CredentialStoreHandler.Credential mapping = new CredentialStoreHandler.Credential(credential.action(), credential.name(), credential.portable());
    this.handler.add(mapping);
  }
}