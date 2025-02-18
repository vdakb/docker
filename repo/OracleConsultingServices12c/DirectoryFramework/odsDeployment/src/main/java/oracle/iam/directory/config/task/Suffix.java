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

    System      :   Oracle Directory Service Utility Library
    Subsystem   :   Deployment Utilities 11g

    File        :   Suffix.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Suffix..


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2012-12-09  DSteding    First release version
*/

package oracle.iam.directory.config.task;

import org.apache.tools.ant.BuildException;

import oracle.iam.directory.common.FeatureException;

import oracle.iam.directory.common.task.FeaturePlatformTask;

import oracle.iam.directory.config.type.Bind;
import oracle.iam.directory.config.type.Command;

import oracle.iam.directory.common.spi.handler.ConfigHandler;

////////////////////////////////////////////////////////////////////////////////
// class Suffix
// ~~~~~ ~~~~~~
/**
 ** Invokes an operation on Directory Suffix to managed configuration.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Suffix extends FeaturePlatformTask {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final ConfigHandler.Suffix handler;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Suffix</code> event handler that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Suffix() {
    // ensure inheritance
    super();

    // initialize instance
    this.handler = new ConfigHandler.Suffix(this);
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
   ** @throws FeatureException   if something goes wrong with the build
   */
  @Override
  public void onExecution()
    throws FeatureException {

    this.handler.execute();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addBind
  /**
   ** Call by the ANT deployment to inject the argument for adding an
   ** {@link Bind}.
   **
   ** @param  bind               the bind to add.
   */
  public final void addBind(final Bind bind)
    throws BuildException {

    addConfiguredBind(bind);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredBind
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** {@link Bind}.
   **
   ** @param  bind               the {@link Bind} to add.
   */
  public final void addConfiguredBind(final Bind bind) {

    this.handler.bindOption(bind.getValue(), bind.name());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addCommand
  /**
   ** Call by the ANT deployment to inject the argument for adding an
   ** {@link Command}.
   **
   ** @param  command            the command to add.
   */
  public final void addCommand(final Command command)
    throws BuildException {

    addConfiguredCommand(command);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredCommand
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** {@link Command}.
   **
   ** @param  command            the {@link Command} to add.
   */
  public final void addConfiguredCommand(final Command command) {
    this.handler.add(command.instance());
  }
}