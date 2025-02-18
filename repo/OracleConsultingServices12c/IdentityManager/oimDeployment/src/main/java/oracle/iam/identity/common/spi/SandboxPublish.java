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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Library
    Subsystem   :   Deployment Utilities 12c

    File        :   SandboxPublish.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    SandboxPublish.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-08-31  DSteding    First release version
*/

package oracle.iam.identity.common.spi;

import oracle.mds.core.MDSInstance;

import oracle.mds.naming.Namespace;

import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceException;

////////////////////////////////////////////////////////////////////////////////
// class SandboxPublish
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** Provides basic implementations to handle sandbox artifacts that are
 ** published in the Metadata Store.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class SandboxPublish extends SandboxTransaction {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private boolean commit = true;
  private boolean force  = true;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>SandboxPublish</code> to initialize the instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   */
  public SandboxPublish(final ServiceFrontend frontend) {
    // ensure inheritance
    super(frontend);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commit
  /**
   ** Sets commit for publishing a sandbox.
   **
   ** @param  commit             <code>true</code> if publishing of the
   **                            sandbox has to be committed.
   */
  public final void commit(final boolean commit) {
    this.commit = commit;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commit
  /**
   ** Returns commit for publishing a sandbox.
   **
   ** @return                    <code>true</code> if publishing of the
   **                            sandbox has to be committed.
   */
  public final boolean commit() {
    return this.commit;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   force
  /**
   ** Sets force for publishing a sandbox.
   **
   ** @param  force              <code>true</code> if publishing of the
   **                            sandbox has to be enforced.
   */
  public final void force(final boolean force) {
    this.force = force;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   force
  /**
   ** Returns force for publishing a sandbox.
   **
   ** @return                     <code>true</code> if publishing of the
   **                            sandbox has to be enforced.
   */
  public final boolean force() {
    return this.force;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute (SandboxProvider)
  /**
   ** Executes the metadata operation in a Oracle Metadata Store represented by
   ** {@link MDSInstance}.
   **
   ** @param  instance           the {@link MDSInstance} used to perform the
   **                            operation.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  @Override
  public void execute(final MDSInstance instance)
    throws ServiceException {

    final Namespace namespace = defaultCustomizationNamspace(instance);
    for (String name : this.sandbox)
      publish(instance, namespace, name, this.commit, this.force);
  }
}