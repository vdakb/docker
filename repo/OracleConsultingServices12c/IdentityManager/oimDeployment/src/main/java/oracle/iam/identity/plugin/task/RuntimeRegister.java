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

    File        :   RuntimeRegister.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    RuntimeRegister.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.plugin.task;

import java.io.File;

import org.apache.tools.ant.BuildException;

import oracle.iam.platformservice.api.PlatformService;

import oracle.hst.deployment.ServiceException;

import oracle.iam.identity.common.spi.RuntimeRegistry;

import oracle.iam.identity.plugin.type.Plugin;

////////////////////////////////////////////////////////////////////////////////
// class RuntimeRegister
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Registers a plugin in the Metadata Repository of Identity Manager by
 ** uploading the plugin archive (ZIP) to the metadata store.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class RuntimeRegister extends RuntimeTask {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>RuntimeRegister</code> task that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public RuntimeRegister() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

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

    // initialize the business logic layer to operate on and execute
    this.registry.register(this.service(PlatformService.class));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredPlugin
  /**
   ** Call by the ANT deployment to inject the nested "fileset" element.
   **
   ** @param  locationSet        the names of the file where the upload has to
   **                            be done for.
   **
   ** @throws BuildException     if one of the resulting {@link File}s
   **                            evaluated from the {@link Plugin} is already
   **                            part of this register task.
   */
  public void addConfiguredPlugin(final Plugin locationSet)
    throws BuildException {

    addLocation(RuntimeRegistry.PLUGIN, locationSet);
  }
}