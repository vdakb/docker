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

    File        :   Job.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Job.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.scheduler.type;

import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.Reference;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.deployment.ServiceException;

import oracle.iam.identity.common.spi.JobInstance;

////////////////////////////////////////////////////////////////////////////////
// class Job
// ~~~~~ ~~~
/**
 ** <code>Job</code> represents a job in Identity Manager that might be created,
 ** updated or deleted after or during an import operation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Job extends DataType {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final JobInstance delegate = new JobInstance();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Job</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Job() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setRefid
  /**
   ** Call by the ANT kernel to inject the argument for parameter refid.
   ** <p>
   ** Makes this instance in effect a reference to another <code>Job</code>
   ** instance.
   ** <p>
   ** You must not set another attribute or nest elements inside this element
   ** if you make it a reference.
   **
   ** @param  reference          the id of this instance.
   **
   ** @throws BuildException     if any other instance attribute is already set.
   */
  public void setRefid(final Reference reference)
    throws BuildException {

    if (!StringUtility.isEmpty(this.delegate.name()) || (this.delegate.parameter().size() > 0))
      throw tooManyAttributes();

    // ensure inheritance
    super.setRefid(reference);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setName
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>name</code>.
   **
   ** @param  name               the name of the Scheduled Job Instance in
   **                            Identity Manager.
   */
  public void setName(final String name) {
    checkAttributesAllowed();
    this.delegate.name(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setTask
  /**
   ** Called to inject the argument for parameter <code>task</code>.
   **
   ** @param  task               the name of the Scheduled Task that defines the
   **                            scheduled job.
   */
  public void setTask(final String task) {
    // prevent bogus input
    checkAttributesAllowed();
    this.delegate.task(task);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setTaskStatus
  /**
   ** Called to inject the argument for parameter <code>status</code>.
   **
   ** @param  status             whether the job will provide status information
   **                            or not.
   */
  public final void setStatus(final boolean status) {
    // prevent bogus input
    checkAttributesAllowed();
    this.delegate.status(status);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns the {@link JobInstance} delegate of Identity Manager to handle.
   **
   ** @return                    the {@link JobInstance} delegate of Identity
   **                            Manager.
   */
  public final JobInstance instance() {
    if (isReference())
      return ((Job)getCheckedRef()).instance();

    return this.delegate;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredStatus
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** {@link Status} command.
   **
   ** @param  command            the {@link Status} the Oracle
   **                            Identity Manager Scheduled Job Instance to
   **                            execute.
   **
   ** @throws ServiceException   if the specified value pair is already
   **                            part of the parameter mapping.
   */
  public void addConfiguredStatus(final Status command)
    throws ServiceException {

    // prevent bogus input
    checkAttributesAllowed();
    this.delegate.addCommand(command);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredStart
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** {@link Start} command.
   **
   ** @param  command            the {@link Start} the Oracle
   **                            Identity Manager Scheduled Job Instance to
   **                            execute.
   **
   ** @throws ServiceException   if the specified value pair is already
   **                            part of the parameter mapping.
   */
  public void addConfiguredStart(final Start command)
    throws ServiceException {

    // prevent bogus input
    checkAttributesAllowed();
    this.delegate.addCommand(command);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredStop
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** {@link Stop} command.
   **
   ** @param  command            the {@link Stop} the Oracle
   **                            Identity Manager Scheduled Job Instance to
   **                            execute.
   **
   ** @throws ServiceException   if the specified value pair is already
   **                            part of the parameter mapping.
   */
  public void addConfiguredStop(final Stop command)
    throws ServiceException {

    // prevent bogus input
    checkAttributesAllowed();
    this.delegate.addCommand(command);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addParameter
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** {@link Parameter}.
   **
   ** @param  name               the name of the attribute of the Identity
   **                            Manager Scheduled Job Instance.
   ** @param  type               the type for <code>name</code> to set on the
   **                            Identity Manager Scheduled Job Instance.
   ** @param  value              the value for <code>name</code> to set on the
   **                            Identity Manager Scheduled Job Instance.
   **
   ** @throws ServiceException   if the specified value pair is already
   **                            part of the parameter mapping.
   */
  public void addParameter(final String name, final Parameter.Type type, final String value)
    throws ServiceException {

    addConfiguredParameter(new Parameter(name, type, value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredParameter
  /**
   ** Call by the ANT deployment to inject the argument for adding an
   ** {@link Parameter}.
   **
   ** @param  attribute          the {@link Parameter} to add.
   **
   ** @throws ServiceException   if the specified {@link Parameter} is already
   **                            part of the parameter mapping.
   */
  public void addConfiguredParameter(final Parameter attribute)
    throws ServiceException {

    checkAttributesAllowed();
    this.delegate.addAttribute(attribute);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredTrigger
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** {@link Schedule}.
   **
   ** @param  schedule           the {@link Schedule} to add.
   **
   ** @throws ServiceException   if the specified value pair is already
   **                            part of the parameter mapping.
   */
  public void addConfiguredTrigger(final Schedule schedule)
    throws ServiceException {

    // prevent bogus input
    checkAttributesAllowed();

    this.delegate.schedule(schedule.instance());
  }
}