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
    Subsystem   :   Sandbox Service Utilities 11g

    File        :   Publish.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Publish.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-08-31  DSteding    First release version
*/

package oracle.iam.identity.sandbox.task;

import oracle.iam.identity.common.FeatureSandboxTask;

import oracle.iam.identity.common.spi.SandboxPublish;
import oracle.iam.identity.common.spi.SandboxProvider;

////////////////////////////////////////////////////////////////////////////////
// class Publish
// ~~~~~ ~~~~~~~
/**
 ** Publishs a sandbox in Oracle Metadata Store.
 ** <p>
 ** The changes applied to the main line are still undoable. To finalize the
 ** changes use the {@link Commit} task.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Publish extends FeatureSandboxTask {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Delete</code> task that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Publish() {
    // ensure inheritance
    super(true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setForce
  /**
   ** Sets commit for publishing a sandbox.
   **
   ** @param  force              <code>true</code> to enforce publishing of the
   **                            sandbox.
   */
  public final void setForce(final boolean force) {
    ((SandboxPublish)this.provider).force(force);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setCommit
  /**
   ** Set it to <code>true</code> publishing a sandbox has to be committed.
   **
   ** @param  commit             <code>true</code> if publishing of the
   **                            sandbox has to be committed.
   */
  public final void setCommit(final boolean commit) {
    ((SandboxPublish)this.provider).commit(commit);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createProvider (FeatureSandboxTask)
  /**
   ** Factory method to create the appropriate {@link SandboxProvider}.
   **
   ** @return                    the appropriate {@link SandboxProvider}.
   */
  @Override
  protected final SandboxProvider createProvider() {
    return new SandboxPublish(this);
  }
}