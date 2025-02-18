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

    File        :   SandboxCommit.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    SandboxCommit.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-08-31  DSteding    First release version
*/

package oracle.iam.identity.common.spi;

import oracle.mds.core.MDSInstance;

import oracle.mds.naming.Namespace;

import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceFrontend;

////////////////////////////////////////////////////////////////////////////////
// class SandboxCommit
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** Provides basic implementations to handle sandbox artifacts that are
 ** published in the Metadata Store.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class SandboxCommit extends SandboxTransaction {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>SandboxCommit</code> to initialize the instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   */
  public SandboxCommit(final ServiceFrontend frontend) {
    // ensure inheritance
    super(frontend);
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
      commit(instance, namespace, name);
  }
}