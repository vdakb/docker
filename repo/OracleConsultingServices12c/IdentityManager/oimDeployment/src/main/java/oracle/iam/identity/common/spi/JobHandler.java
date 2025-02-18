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

    File        :   JobHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    JobHandler.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
    0.0.0.2     2013-18-01  DSteding    Fix DE-000064
                                        Job that are started in blocking mode
                                        should log job status only if verbose is
                                        set to true.
*/

package oracle.iam.identity.common.spi;

import java.util.Map;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import java.io.ObjectInputStream;
import java.io.ByteArrayInputStream;

import org.apache.tools.ant.BuildException;

import oracle.iam.scheduler.vo.Trigger;
import oracle.iam.scheduler.vo.JobHistory;
import oracle.iam.scheduler.vo.JobDetails;
import oracle.iam.scheduler.vo.CronTrigger;
import oracle.iam.scheduler.vo.JobParameter;
import oracle.iam.scheduler.vo.ScheduledTask;

import oracle.iam.scheduler.exception.SchedulerException;

import oracle.iam.scheduler.api.SchedulerService;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceOperation;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.hst.deployment.spi.AbstractHandler;

import oracle.iam.identity.common.FeatureError;
import oracle.iam.identity.common.FeatureMessage;
import oracle.iam.identity.common.FeatureException;
import oracle.iam.identity.common.FeaturePlatformTask;
import oracle.iam.identity.common.FeatureResourceBundle;

import oracle.iam.identity.scheduler.type.Stop;
import oracle.iam.identity.scheduler.type.Start;
import oracle.iam.identity.scheduler.type.Status;
import oracle.iam.identity.scheduler.type.Command;
import oracle.iam.identity.scheduler.type.Parameter;
import oracle.iam.identity.scheduler.type.Schedule;

////////////////////////////////////////////////////////////////////////////////
// class JobHandler
// ~~~~~ ~~~~~~~~~~
/**
 ** <code>JobHandler</code> configures a Scheduled Job Instance in Identity
 ** Manager.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 0.0.0.2
 ** @since   1.0.0.0
 */
public class JobHandler extends AbstractHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the mapping key contained in a collection to specify that the group name
   ** should be resolved
   */
 static final String        NAME        = "Scheduled Job";

  /** the status indicating a job is stopped */
  static final int          STOPPED     = 2;

  /** the status indicating a job is running */
  static final int          RUNNING     = 5;

  /** the status indicating a job is interrupted */
  static final int          INTERRUPTED = 7;

  static final String[]     STATUS      = {
    /* 0 */ FeatureResourceBundle.string(FeatureMessage.JOB_STATUS_0)
  , /* 1 */ FeatureResourceBundle.string(FeatureMessage.JOB_STATUS_1)
  , /* 2 */ FeatureResourceBundle.string(FeatureMessage.JOB_STATUS_STOPPED)
  , /* 3 */ FeatureResourceBundle.string(FeatureMessage.JOB_STATUS_3)
  , /* 4 */ FeatureResourceBundle.string(FeatureMessage.JOB_STATUS_4)
  , /* 5 */ FeatureResourceBundle.string(FeatureMessage.JOB_STATUS_RUNNING)
  , /* 6 */ FeatureResourceBundle.string(FeatureMessage.JOB_STATUS_6)
  , /* 7 */ FeatureResourceBundle.string(FeatureMessage.JOB_STATUS_INTERRUPTED)
  };

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the name of the Scheduled Job Instance to configure or to create */
  private JobInstance       single;

  /** the name of the Scheduled Job Instance to configure or to create */
  private List<JobInstance> multiple = new ArrayList<JobInstance>();

  /** the business logic layer to operate on scheduled jobs */
  private SchedulerService  facade;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>JobHandler</code> to initialize the instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   */
  public JobHandler(final ServiceFrontend frontend) {
    // ensure inheritance
    super(frontend);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the name of the Job in Identity Manager to handle.
   **
   ** @return                    the name of the Job in Identity Manager to
   **                            handle.
   */
  public final String name() {
    return this.single == null ? null : this.single.name();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name (AbstractHandler)
  /**
   ** Called to inject the argument for parameter <code>name</code>.
   **
   ** @param  name               the name of the Job in Identity Manager to
   **                            handle.
   */
  @Override
  public void name(final String name) {
    if (this.single == null)
      this.single = new JobInstance();

    this.single.name(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addParameter (AbstractHandler)
  /**
   ** Add the specified value pair to the parameters that has to be applied
   ** after an import operation.
   **
   ** @param  name               the name of the parameter of the Identity
   **                            Manager Job.
   ** @param  value              the value for <code>name</code> to set on the
   **                            Identity Manager Job.
   **
   ** @throws BuildException     if the specified name is already part of the
   **                            parameter mapping.
   */
  @Override
  public void addParameter(final String name, final Object value)
    throws BuildException {

    if (this.single == null)
      this.single = new JobInstance();

    // add the value pair to the parameters
    this.single.addParameter(name, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addParameter (AbstractHandler)
  /**
   ** Add the specified value pairs to the parameters that has to be applied.
   **
   ** @param  parameter          the named value pairs to be applied on the
   **                            Identity Manager Job.
   **
   ** @throws BuildException     if the specified {@link Map} contains a value
   **                            pair that already part of the parameter
   **                            mapping.
   */
  @Override
  public void addParameter(final Map<String, Object> parameter) {
    if (this.single == null)
      this.single = new JobInstance();

    // add the value pairs to the parameters
    this.single.addParameter(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
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
    if (this.single == null && this.multiple.size() == 0)
      throw new BuildException(ServiceResourceBundle.string(ServiceError.OBJECT_ELEMENT_MANDATORY));

    if (!(this.operation() == ServiceOperation.delete))
      try {
        if (this.single != null)
          this.single.validate();

        for (JobInstance instance : this.multiple)
          instance.validate();
      }
      catch (Exception e) {
        throw new BuildException(e.getLocalizedMessage());
      }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addInstance
  /**
   ** Add the specified value pairs to the parameters that has to be applied.
   **
   ** @param  object             the {@link JobInstance} to add.
   **
   ** @throws BuildException     if the specified {@link JobInstance} is already
   **                            assigned to this task.
   */
  public void addInstance(final JobInstance object) {
    // prevent bogus input
    if ((this.single != null && this.single.equals(object)) || this.multiple.contains(object))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, object.name()));

    // add the instance to the object to handle
    this.multiple.add(object);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Creates a new scheduled job in  Identity Manager through the given
   ** {@link SchedulerService}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void create(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.facade = task.service(SchedulerService.class);

    if (this.single != null)
      create(this.single);

    for (JobInstance instance : this.multiple)
      create(instance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Updates an existing scheduled job in Identity Manager through the
   ** discovered {@link SchedulerService}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void configure(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.facade = task.service(SchedulerService.class);

    if (this.single != null)
      configure(this.single);

    for (JobInstance instance : this.multiple)
      configure(instance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Deletes an existing scheduled job from Identity Manager through the
   ** discovered {@link SchedulerService}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void delete(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.facade = task.service(SchedulerService.class);

    if (this.single != null)
      delete(this.single);

    for (JobInstance instance : this.multiple)
      delete(instance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   startup
  /**
   ** Starts the scheduler service in Identity Manager through the discovered
   ** {@link SchedulerService}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void startup(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.facade = task.service(SchedulerService.class);
    try {
      this.facade.start();
    }
    catch (Exception e) {
      throw new ServiceException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   shutdown
  /**
   ** Stopps the scheduler service in Identity Manager through the discovered
   ** {@link SchedulerService}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void shutdown(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.facade = task.service(SchedulerService.class);
    try {
      this.facade.stop();
    }
    catch (Exception e) {
      throw new ServiceException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   status
  /**
   ** Returns the status of the scheduler service in Identity Manager through
   ** the discovered {@link SchedulerService}.
   **
   ** @param  task               the {@link FeaturePlatformTask} providing
   **                            access to the remote service facades.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void status(final FeaturePlatformTask task)
    throws ServiceException {

    // initialize the business logic layer to operate on
    this.facade = task.service(SchedulerService.class);
    int status = -1;
    try {
      status = facade.getStatus();
    }
    catch (Exception e) {
      throw new ServiceException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Creates a new scheduled job in Identity Manager through the discovered
   ** {@link SchedulerService}.
   **
   ** @param  instance           the {@link JobInstance} to create.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void create(final JobInstance instance)
    throws ServiceException {

    final String[] arguments = { NAME, instance.name() };
    if (exists(instance)) {
      error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_EXISTS, arguments));
      if (failonerror())
        throw new ServiceException(ServiceError.OBJECT_ELEMENT_EXISTS, arguments);
    }

    arguments[0] = FeatureResourceBundle.string(FeatureMessage.JOB_INSTANCE_OPERATION_CREATE);
    info(FeatureResourceBundle.format(FeatureMessage.JOB_INSTANCE_OPERATION_BEGIN, arguments));
    final ScheduledTask task = new ScheduledTask();
    task.setName(instance.task());
    JobDetails job = new JobDetails(task, instance.name(), new HashMap<String, JobParameter>());
    job.setJobScheduleType(instance.schedule().getValue());

    // determine if the job status should be displayed in the Web UI
    if (instance.status() != null)
      job.setTaskStatus(instance.status().booleanValue());

    if (instance.attribute().size() > 0) {
      // transform the attributes enlisted on the instance to job parameters
      // and put them in the parameter mapping
      final HashMap<String, JobParameter> parameter = job.getAttributes();
      for (Parameter attribute : instance.attribute()) {
        final JobParameter param = new JobParameter();
        param.setName(attribute.name());
        param.setValue(attribute.value());
        param.setDataType(attribute.type().value());
        parameter.put(attribute.name(), param);
      }
    }
    Trigger trigger = null;
    if (instance.schedule() != null) {
      final Schedule schedule = instance.schedule();
      job.setJobScheduleType(schedule.getValue());
      job.setRetrycount(schedule.retry());
      // create the appropriate trigger type
      if (schedule instanceof Schedule.Cron) {
        final Schedule.Cron cron = (Schedule.Cron)schedule;
        // set cron schedule of the job to the appropriate type
        job.setCronScheduleType(cron.type().toUpperCase());
        // regardless a job will be scheduled periodical or as a cron job it
        // has a trigger
        trigger = cronTrigger(instance.name(), cron);
      }
      else {
        // remove any occurence of a cron schedule from the job
        job.setCronScheduleType(null);
        // regardless a job will be scheduled periodical or as a cron job it
        // has a trigger
        trigger = simpleTrigger(instance.name(), schedule);
      }
    }
    try {
      if (trigger == null)
        // create the job only
        this.facade.addJob(job);
      else
        // create the job including the scheduling properties of the job
        this.facade.scheduleJob(job, trigger);
      info(FeatureResourceBundle.format(FeatureMessage.JOB_INSTANCE_OPERATION_SUCCESS, arguments));
    }
    catch (Exception e) {
      if (failonerror())
        throw new ServiceException(ServiceError.UNHANDLED, e);
      else
        error(FeatureResourceBundle.format(FeatureError.JOB_INSTANCE_OPERATION_FAILED, arguments));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Deletes an existing scheduled job in Identity Manager through the
   ** discovered {@link SchedulerService}.
   **
   ** @param  instance           the {@link JobInstance} to create.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void delete(final JobInstance instance)
    throws ServiceException {

    final JobDetails job = find(instance.name());
    if (job == null)
      return;

    final String[] arguments = { FeatureResourceBundle.string(FeatureMessage.JOB_INSTANCE_OPERATION_DELETE), instance.name() };
    info(FeatureResourceBundle.format(FeatureMessage.JOB_INSTANCE_OPERATION_BEGIN, arguments));
    try {
      this.facade.deleteJob(job);
      info(FeatureResourceBundle.format(FeatureMessage.JOB_INSTANCE_OPERATION_SUCCESS, arguments));
    }
    catch (Exception e) {
      if (failonerror())
        throw new ServiceException(ServiceError.UNHANDLED, e);
      else
        error(FeatureResourceBundle.format(FeatureError.JOB_INSTANCE_OPERATION_FAILED, arguments));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Updates an existing scheduled job in Identity Manager through the
   ** discovered {@link SchedulerService}.
   **
   ** @param  instance           the {@link JobInstance} to configure.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void configure(final JobInstance instance)
    throws ServiceException {

    final JobDetails job = find(instance.name());
    if (job == null)
      return;

    if (instance.command().size() != 0) {
      for (Command command : instance.command()) {
        if (command instanceof Start)
          start(job, (Start)command);
        else if (command instanceof Stop)
          stop(job, (Stop)command);
        else if (command instanceof Status)
          status(job, (Status)command);
      }
    }
    else {
      final String[] arguments = { FeatureResourceBundle.string(FeatureMessage.JOB_INSTANCE_OPERATION_UPDATE), instance.name() };
      info(FeatureResourceBundle.format(FeatureMessage.JOB_INSTANCE_OPERATION_BEGIN, arguments));

      Trigger        trigger = null;
      final Schedule schedule = instance.schedule();
      // determine if the job status should be displayed in the Web UI
      if (instance.status() != null)
        job.setTaskStatus(instance.status().booleanValue());

      if (schedule != null) {
        // determine if the schedule type needs to be changed
        if (!schedule.getValue().equals(job.getJobScheduleType()))
          job.setJobScheduleType(schedule.getValue());
        // determine if the job retry count should be updated
        if (schedule.retry() != null)
          job.setRetrycount(schedule.retry().intValue());

        // create the appropriate trigger type
        if (schedule instanceof Schedule.Cron) {
          final Schedule.Cron cron = (Schedule.Cron)schedule;
          // set cron schedule of the job to the appropriate type
          job.setCronScheduleType(cron.type().toUpperCase());
          // regardless a job will be scheduled periodical or as a cron job it
          // has a trigger
          trigger = cronTrigger(instance.name(), cron);
        }
        else {
          // remove any occurence of a cron schedule from the job
          job.setCronScheduleType(null);
          // regardless a job will be scheduled periodical or as a cron job it
          // has a trigger
          trigger = simpleTrigger(instance.name(), schedule);
        }
      }

      if (instance.attribute().size() > 0) {
        // transform the attributes enlisted on the instance to job parameters
        // and put them in the parameter mapping
        final HashMap<String, JobParameter> parameter = job.getAttributes();
        for (Parameter attribute : instance.attribute()) {
          final JobParameter param = new JobParameter();
          param.setName(attribute.name());
          param.setValue(attribute.value());
          param.setDataType(attribute.type().value());
          parameter.put(attribute.name(), param);
        }
      }
      try {
        update(job, trigger);
      }
      catch (Exception e) {
        error(FeatureResourceBundle.format(FeatureError.JOB_INSTANCE_OPERATION_FAILED, arguments));
        if (failonerror())
          throw new ServiceException(ServiceError.UNHANDLED, e);
      }
      info(FeatureResourceBundle.format(FeatureMessage.JOB_INSTANCE_OPERATION_SUCCESS, arguments));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   status
  /**
   ** Enables or disables an existing job instance in Identity Manager through
   ** the discovered {@link SchedulerService}.
   **
   ** @param  instance           the {@link JobDetails} instance to change.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void status(final JobDetails instance, final Status status)
    throws ServiceException {

    final String[] argument = { instance.getName(), FeatureResourceBundle.string(status.state() ? FeatureMessage.JOB_INSTANCE_COMMAND_ENABLE : FeatureMessage.JOB_INSTANCE_COMMAND_DISABLE) };
    info(FeatureResourceBundle.format(FeatureMessage.JOB_INSTANCE_COMMAND_BEGIN, argument));
    instance.setTaskStatus(status.state());
    try {
      this.facade.pauseJob(instance.getName());
      this.facade.updateJob(instance);
      info(FeatureResourceBundle.format(FeatureMessage.JOB_INSTANCE_COMMAND_SUCCESS, argument));
    }
    catch (Exception e) {
      error(FeatureResourceBundle.format(FeatureMessage.JOB_INSTANCE_COMMAND_FAILED, argument));
      if (failonerror())
        throw new ServiceException(ServiceError.UNHANDLED, e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   start
  /**
   ** Starts a scheduled job in Identity Manager through the discovered
   ** {@link SchedulerService}.
   **
   ** @param  instance           the {@link JobDetails} to start.
   ** @param  command            the {@link Start} command with the properties
   **                            to handle the job.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void start(final JobDetails instance, final Start command)
    throws ServiceException {

    int status = 0;
    try {
      final String[] parameter = { FeatureResourceBundle.string(FeatureMessage.JOB_INSTANCE_COMMAND_START), instance.getName() };
      status = this.facade.getStatusOfJob(instance.getName());
      switch (status) {
        case RUNNING     : error(FeatureResourceBundle.format(FeatureMessage.JOB_INSTANCE_STATUS_RESULT, instance.getName(), STATUS[status]));
                           break;
        case INTERRUPTED : error(FeatureResourceBundle.format(FeatureMessage.JOB_INSTANCE_STATUS_RESULT, instance.getName(), STATUS[status]));
                           this.facade.triggerNow(instance.getName());
                           info(FeatureResourceBundle.format(FeatureMessage.JOB_INSTANCE_COMMAND_SUCCESS, parameter));
                           do {
                             // obtain the status of the job after it was triggered
                             status = this.facade.getStatusOfJob(instance.getName());
                           } while (status == STOPPED);
                           break;
        default          : info(FeatureResourceBundle.format(FeatureMessage.JOB_INSTANCE_COMMAND_BEGIN, parameter));
                           this.facade.triggerNow(instance.getName());
                           do {
                             // obtain the status of the job after it was triggered
                             status = this.facade.getStatusOfJob(instance.getName());
                           } while (status == STOPPED);
                           info(FeatureResourceBundle.format(FeatureMessage.JOB_INSTANCE_COMMAND_SUCCESS, parameter));
      }
    }
    catch (Exception e) {
      final String[] parameter = { FeatureResourceBundle.string(FeatureMessage.JOB_INSTANCE_COMMAND_START), instance.getName(), e.getLocalizedMessage() };
      if (failonerror())
        throw new FeatureException(FeatureError.JOB_INSTANCE_OPERATION_FAILED, parameter);
      else
        error(FeatureResourceBundle.format(FeatureError.JOB_INSTANCE_OPERATION_FAILED, parameter));
    }
    // check if its requested to block further processing until the job is
    // finished
    if (command.blocking()) {
      byte[] result = null;
      info(FeatureResourceBundle.format(FeatureMessage.JOB_INSTANCE_OBSERVATION_STARTED, instance.getName()));
      try {
        do {
          // check the status of the scheduled job frequenly
          status = this.facade.getStatusOfJob(instance.getName());
          if (status != RUNNING)
            break;
          // wait a certain amount of time to query the scheduler for the job status again
          try {
            Thread.currentThread().sleep(command.interval());
          }
          catch (InterruptedException e) {
            error(e.getLocalizedMessage());
          }
          if (verbose())
            warning(FeatureResourceBundle.format(FeatureMessage.JOB_INSTANCE_STATUS_CHECK, instance.getName()));
          if (verbose())
            warning(FeatureResourceBundle.format(FeatureMessage.JOB_INSTANCE_STATUS_RESULT, instance.getName(), STATUS[status]));
        } while (status == RUNNING);
        // check the history of the job for any exception occured that might be
        // happend
        final JobHistory history = this.facade.getLastHistoryOfJob(instance.getName());
        result = history.getErrorData();
      }
      catch (Exception e) {
        final String[] parameter = { FeatureResourceBundle.string(FeatureMessage.JOB_INSTANCE_STATUS_CHECK), instance.getName(), e.getLocalizedMessage() };
        if (failonerror())
          throw new FeatureException(FeatureError.JOB_INSTANCE_OPERATION_FAILED, parameter);
        else
          error(FeatureResourceBundle.format(FeatureError.JOB_INSTANCE_OPERATION_FAILED, parameter));
      }
      // report the status of the job after it has terminated
      if (verbose())
        warning(FeatureResourceBundle.format(FeatureMessage.JOB_INSTANCE_STATUS_RESULT, instance.getName(), STATUS[status]));
      if (result != null && result.length > 0) {
        try {
          // for the tie being it's good enough to use the standard way to
          // deserialize the byte stream
          final ObjectInputStream stream = new ObjectInputStream(new ByteArrayInputStream(result));
          final Exception         exception = ((Exception)stream.readObject());
          error(FeatureResourceBundle.format(FeatureMessage.JOB_INSTANCE_OPERATION_FAILED, FeatureResourceBundle.string(FeatureMessage.JOB_INSTANCE_OPERATION_EXECUTE), instance.getName(), exception.getMessage()));
        }
        catch (Exception e) {
          error(FeatureResourceBundle.format(FeatureMessage.JOB_INSTANCE_OPERATION_FAILED, FeatureResourceBundle.string(FeatureMessage.JOB_INSTANCE_OPERATION_HISTORY), instance.getName(), e.getMessage()));
        }
      }
      info(FeatureResourceBundle.format(FeatureMessage.JOB_INSTANCE_OBSERVATION_FINISHED, instance.getName()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stop
  /**
   ** Stops a scheduled job in Identity Manager through the discovered
   ** {@link SchedulerService}.
   **
   ** @param  instance           the {@link JobDetails} to stop.
   ** @param  command            the {@link Stop} command with the properties
   **                            to handle the job.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void stop(final JobDetails instance, final Stop command)
    throws ServiceException {

    try {
      int status = this.facade.getStatusOfJob(instance.getName());
      // check if the job is running
      if (status == RUNNING) {
        info(FeatureResourceBundle.format(FeatureMessage.JOB_INSTANCE_COMMAND_BEGIN, FeatureResourceBundle.string(FeatureMessage.JOB_INSTANCE_COMMAND_STOP), instance.getName()));
        this.facade.stopJob(instance.getName());
        // wait a certain amount of time the scheduler has stopped the job
        try {
          Thread.sleep(command.interval());
        }
        catch (InterruptedException e) {
          error(e.getLocalizedMessage());
        }
        warning(FeatureResourceBundle.format(FeatureMessage.JOB_INSTANCE_STATUS_CHECK, instance.getName()));
        // obtain the status of the job after it was triggered
        status = this.facade.getStatusOfJob(instance.getName());
        warning(FeatureResourceBundle.format(FeatureMessage.JOB_INSTANCE_STATUS_RESULT, instance.getName(), STATUS[status]));

        // check if its requested to block further processing until the job is
        // finished
        if (command.blocking()) {
          do {
            if (status == RUNNING) {
              try {
                Thread.sleep(command.interval());
              }
              catch (InterruptedException e) {
                error(e.getLocalizedMessage());
              }
              if (verbose())
                warning(FeatureResourceBundle.format(FeatureMessage.JOB_INSTANCE_STATUS_CHECK, instance.getName()));
              // check the status of the scheduled job frequenly
              status = this.facade.getStatusOfJob(instance.getName());
              if (verbose())
                warning(FeatureResourceBundle.format(FeatureMessage.JOB_INSTANCE_STATUS_RESULT, instance.getName(), STATUS[status]));
            }
          } while (status == RUNNING);
        }
      }
      info(FeatureResourceBundle.format(FeatureMessage.JOB_INSTANCE_COMMAND_SUCCESS, FeatureResourceBundle.string(FeatureMessage.JOB_INSTANCE_COMMAND_STOP), instance.getName()));
    }
    catch (Exception e) {
      error(FeatureResourceBundle.format(FeatureMessage.JOB_INSTANCE_COMMAND_FAILED, FeatureResourceBundle.string(FeatureMessage.JOB_INSTANCE_COMMAND_STOP), instance.getName()));
      if (failonerror())
        throw new ServiceException(ServiceError.UNHANDLED, e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exists
  /**
   ** Checks if the specified {@link JobInstance} exists in Identity Manager
   ** Server through the discovered {@link SchedulerService}.
   **
   ** @param  instance           the {@link JobInstance} to check for
   **                            existance.
   **
   ** @return                    <code>true</code> if the {@link JobInstance}
   **                            already exists in Identity Manager; otherwise
   **                            <code>false</code>.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public boolean exists(final JobInstance instance)
    throws ServiceException {

    final Map<String, String> filter = new HashMap<String, String>();
    filter.put(NAME, instance.name());
    try {
      final JobDetails details = this.facade.getJobDetail(instance.name());
      return details != null;
    }
    catch (Exception e) {
      throw new ServiceException(ServiceError.UNHANDLED, e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   find
  /**
   ** Returns a existing job from the Identity Manager through the discovered
   ** {@link SchedulerService}.
   **
   ** @param  instance           the name of the job instance to lookup.
   **
   ** @return                    the {@link JobDetails} for the specified
   **                            instance.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public JobDetails find(final String instance)
    throws ServiceException {

    JobDetails job = null;
    try {
      debug(FeatureResourceBundle.format(FeatureMessage.JOB_INSTANCE_SEARCH, instance));
      job = this.facade.getJobDetail(instance);
      if (job == null) {
        final String[] arguments = { NAME, instance };
        error(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments));
        if (failonerror())
          throw new ServiceException(ServiceError.OBJECT_ELEMENT_NOTFOUND, arguments);
      }
      debug(FeatureResourceBundle.format(FeatureMessage.JOB_INSTANCE_FOUND, instance));
      return job;
    }
    catch (SchedulerException e) {
      throw new ServiceException(ServiceError.UNHANDLED, e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateInstance
  /**
   ** Update a job in Identity Manager with the specified details.
   **
   ** @param  job                the {@link JobDetails} to update.
   ** @param  trigger            the {@link Trigger} to create, update or
   **                            delete.
   **
   ** @throws Exception          in case an error does occur.
   */
  private void update(final JobDetails job, final Trigger trigger)
    throws Exception {

    final String name = job.getName();
    if (trigger != null) {
      // create or update the scheduling properties of the job
      debug(FeatureResourceBundle.format(FeatureMessage.JOB_INSTANCE_SCHEDULE_UPDATE, name));
      if (job.getJobScheduleType().equals(Schedule.NONE)) {
        this.facade.deleteTrigger(name);
        this.facade.updateJob(job);
      }
      else {
        this.facade.updateJob(job);
        trigger.setLastModifyDate(new Date());
        this.facade.rescheduleJob(name, name, trigger);
      }
      debug(FeatureResourceBundle.format(FeatureMessage.JOB_INSTANCE_SCHEDULE_UPDATED, name));
    }
    else {
      debug(FeatureResourceBundle.format(FeatureMessage.JOB_INSTANCE_PARAMETER_UPDATE, job.getName()));
      this.facade.updateJob(job);
      debug(FeatureResourceBundle.format(FeatureMessage.JOB_INSTANCE_PARAMETER_UPDATED, job.getName()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   simpleTrigger
  /**
   ** Factory method to create a periodical trigger.
   ** <p>
   ** Periodical trigger are either {@link Schedule.Single} or
   ** {@link Schedule.Simple} trigger.
   **
   ** @param  name               the name of the trigger to create.
   ** @param  schedule           the {@link Schedule} to create the
   **                            {@link Trigger} from.
   **
   ** @return                    the created {@link Trigger} from the specified
   **                            {@link Schedule}.
   */
  private Trigger simpleTrigger(final String name, final Schedule schedule) {
    // returning a null value for a none-trigger will remove any trigger from
    // the job
    if ((schedule instanceof Schedule.None))
      return null;

    // regardless a job will be scheduled periodical or as a cron job it
    // has a trigger
    Trigger trigger = new Trigger();
    // name and job name are almost equal in our experience
    trigger.setName(name);
    trigger.setJobName(name);
    // all other trigger properties are either default or the requested
    // value
    if (schedule.firstTime() != null) {
      trigger.setStartTime(schedule.firstTime());
      trigger.setNextFireTime(schedule.firstTime());
    }
    if (schedule.lastTime() != null)
      trigger.setEndTime(schedule.lastTime());
    if (schedule.repeat() != null)
      trigger.setRepeatCount(schedule.repeat().intValue());
    if (schedule.interval() != null)
      trigger.setRepeatInterval(schedule.interval().longValue());
    else
      trigger.setRepeatInterval(-1);
    if (schedule.frequencyType() != null)
      trigger.setFrequencyType(schedule.frequencyType());

    return trigger;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   cronTrigger
  /**
   ** Factory method to create a cron trigger.
   ** <p>
   ** Cron trigger are {@link Schedule.Cron} only.
   **
   ** @param  name               the name of the trigger to create.
   ** @param  schedule           the {@link Schedule.Cron} to create the
   **                            {@link Trigger} from.
   **
   ** @return                    the created {@link Trigger} from the specified
   **                            {@link Schedule}.
   */
  private Trigger cronTrigger(final String name, final Schedule.Cron schedule) {
    // regardless a job will be scheduled periodical or as a cron job it
    // has a trigger
    final CronTrigger trigger = new CronTrigger(name, schedule.firstTime(), schedule.lastTime(), schedule.expression(), schedule.frequencyType());
    // name and job name are almost equal in our experience
    trigger.setJobName(name);
    return trigger;
  }
}