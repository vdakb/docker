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

    File        :   JobInstance.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    JobInstance.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.common.spi;

import java.util.List;
import java.util.ArrayList;

import org.apache.tools.ant.BuildException;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.hst.deployment.spi.AbstractInstance;

import oracle.iam.identity.scheduler.type.Command;
import oracle.iam.identity.scheduler.type.Parameter;
import oracle.iam.identity.scheduler.type.Schedule;

////////////////////////////////////////////////////////////////////////////////
// class JobInstance
// ~~~~~ ~~~~~~~~~~~
/**
 ** <code>JobInstance</code> represents a scheduled job in Identity Manager that
 ** might be created, deleted or changed after or during an import operation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class JobInstance extends AbstractInstance {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String                task      = null;
  private Boolean               status    = false;

  private Schedule              schedule  = null;
  private final List<Command>   command   = new ArrayList<Command>();
  private final List<Parameter> attribute = new ArrayList<Parameter>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>JobInstance</code> task that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public JobInstance() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   schedule
  /**
   ** Called to inject the argument for parameter <code>schedule</code>.
   **
   ** @param  schedule           the schedule of the job in Identity Manager to
   **                            handle.
   */
  public void schedule(final Schedule schedule) {
    this.schedule = schedule;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   schedule
  /**
   ** Returns the schedule of the job in Identity Manager to handle.
   **
   ** @return                    the schedule of the job in Identity Manager to
   **                            handle.
   */
  public final Schedule schedule() {
    return this.schedule;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   task
  /**
   ** Called to inject the argument for parameter <code>task</code>.
   **
   ** @param  task               the name of the Scheduled Task that defines
   **                            scheduled job.
   */
  public final void task(final String task) {
    this.task = task;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   task
  /**
   ** Returns the name of the Scheduled Task that defines the scheduled job.
   **
   ** @return                    the name of the Scheduled Task that defines
   **                            scheduled job.
   */
  public final String task() {
    return this.task;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   status
  /**
   ** Called to inject the argument for parameter <code>status</code>.
   **
   ** @param  status             whether the job will provide status information
   **                            or not.
   */
  public final void status(final Boolean status) {
    this.status = status;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   status
  /**
   ** Returns whether the job will provide status information or not.
   **
   ** @return                    whether the job will provide status information
   **                            or not.
   */
  public final Boolean status() {
    return this.status;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Returns the attribute mapping of the Job Instance of Identity Manager to
   ** handle.
   **
   ** @return                    the attribute mapping of the Job Instance of
   **                            Identity Manager to handle.
   */
  public final List<Parameter> attribute() {
    return this.attribute;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   command
  /**
   ** Returns the commands to execute on the job instance.
   **
   ** @return                    the commands to execute on the job instance.
   */
  public final List<Command> command() {
    return this.command;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** The entry point to validate the type to use.
   **
   ** @throws BuildException     in case the instance does not meet the
   **                            requirements.
   */
  public void validate()
    throws BuildException {

    if (StringUtility.isEmpty(this.name()))
      handleAttributeError("name");

    if (this.command.size() == 0) {
      if (this.attribute.isEmpty() && this.schedule == null) {
        final String[] parameter = { "parameter and/or trigger", "command" };
        throw new BuildException(ServiceResourceBundle.format(ServiceError.TASK_ELEMENT_MIXEDUP, parameter));
      }
    }
    else if (!this.attribute.isEmpty() || this.schedule != null) {
      final String[] parameter = { "command", "parameter and/or trigger" };
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TASK_ELEMENT_MIXEDUP, parameter));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addCommand
  /**
   ** Add the specified {@link Command} to the {@link List} of commands that has
   ** to be exceuted on this job instance.
   **
   ** @param  command            the {@link Command} of the job in Identity
   **                            Manager to handle.
   */
  public void addCommand(final Command command) {
    this.command.add(command);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addAttribute
  /**
   ** Add the specified value pair to the attributes that has to be applied
   ** after an import operation.
   **
   ** @param  attribute          the {@link Parameter} to add.
   */
  public void addAttribute(final Parameter attribute)
    throws BuildException {

    if (this.attribute.contains(attribute))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_PARAMETER_ONLYONCE, name()));

    // add the value pair to the parameters
    this.attribute.add(attribute);
  }
}