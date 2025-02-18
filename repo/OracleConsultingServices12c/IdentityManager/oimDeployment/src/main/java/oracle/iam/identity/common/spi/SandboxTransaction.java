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

    File        :   SandboxTransaction.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    SandboxTransaction.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-08-31  DSteding    First release version
*/

package oracle.iam.identity.common.spi;

import java.util.List;
import java.util.ArrayList;

import org.apache.tools.ant.BuildException;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.iam.identity.common.FeatureError;
import oracle.iam.identity.common.FeatureResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class SandboxTransaction
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** Provides basic implementations to handle sandbox artifacts that are
 ** published in the Metadata Store.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
abstract class SandboxTransaction extends SandboxProvider {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final List<String> sandbox = new ArrayList<String>();

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
  protected SandboxTransaction(final ServiceFrontend frontend) {
    // ensure inheritance
    super(frontend);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   set
  /**
   ** Sets the name of a sandbox to delete.
   **
   ** @param  name               the name of a sandbox to delete.
   **
   ** @throws BuildException     if are any sandbox assigned to this operational
   **                            task.
   */
  public final void set(final String name)
    throws BuildException {

    if (this.sandbox.size() > 0)
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_ATTRIBUTE_MIXEDUP, "name"));

    add(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Adds the name of a sandbox to delete.
   **
   ** @param  name               the name of a sandbox to delete.
   */
  public final void add(final String name) {
    this.sandbox.add(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (ServiceProvider)
  /**
   ** The entry point to validate the task to perform.
   **
   ** @throws BuildException     in case an error does occur.
   */
  @Override
  public void validate() {
    if (this.sandbox.size() == 0)
      throw new BuildException(FeatureResourceBundle.string(FeatureError.SANDBOX_MANDATORY));
  }
}