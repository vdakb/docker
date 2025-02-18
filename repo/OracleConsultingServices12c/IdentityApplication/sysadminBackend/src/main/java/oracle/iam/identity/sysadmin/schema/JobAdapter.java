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

    Copyright © 2017. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Frontend Extension
    Subsystem   :   System Administration Management

    File        :   JobAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    JobAdapter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysadmin.schema;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import java.io.Serializable;

import java.sql.Timestamp;

import oracle.jbo.Row;

import oracle.iam.scheduler.vo.JobDetails;
import oracle.iam.scheduler.vo.JobHistory;
import oracle.iam.scheduler.vo.JobParameter;

import oracle.iam.ui.platform.model.common.ModelAttr;
import oracle.iam.ui.platform.model.common.StaticModelAdapterBean;

////////////////////////////////////////////////////////////////////////////////
// class JobAdapter
// ~~~~~ ~~~~~~~~~~
/**
 ** Data Access Object used by System Administration customization.
 ** <br>
 ** Represents the job within the context of a particular Scheduler. This is a
 ** Job (where the type of object is a JobDetails) in OIM.
 ** <p>
 ** Define an instance variable for each VO attribute, with the data type that
 ** corresponds to the VO attribute type. The name of the instance variable must
 ** match the name of the VO attribute, and the case must match.
 ** <p>
 ** When the instance of this class is passed to the methods in the
 ** corresponding <code>Application Module</code> class, the
 ** <code>Application Module</code> can call the getter methods to retrieve the
 ** values from the AdapterBean and pass them to the OIM APIs.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
 */
public class JobAdapter extends StaticModelAdapterBean {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String PK               = "name";
  public static final String TASK             = "task";
  public static final String FK_TASK          = "Bind_" + TASK;
  public static final String STATUS           = "status";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:5496509699290234895")
  private static final long  serialVersionUID = 1258050103965222799L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @ModelAttr
  private String             name;
  @ModelAttr
  private String             task;
  @ModelAttr
  private Integer            status;
  @ModelAttr
  private String             statusDecode;
  @ModelAttr
  private Boolean            triggerStatus;
  @ModelAttr
  private Timestamp          triggerStart;
  @ModelAttr
  private Timestamp          triggerStop;
  @ModelAttr
  private Timestamp          triggerNext;
  @ModelAttr
  private Boolean            concurrent;
  @ModelAttr
  private Integer            retryCount;
  @ModelAttr
  private String             triggerType;
  @ModelAttr
  private String             cronType;

  /** the parameter to apply on the job */
  private List<JobParameterAdapter> parameter = new ArrayList<JobParameterAdapter>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>JobAdapter</code> values object that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public JobAdapter() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>JobAdapter</code> values object which use the specified
   ** {@link Row} to populate its value.
   **
   ** @param  row                the {@link Row} to populate the values from.
   **                            <br>
   **                            Allowed object is {@link Row}.
   */
  private JobAdapter(final Row row) {
    // ensure inheritance
    super(row);

    // convince the platform API
    createIndentifier();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>JobAdapter</code> values object which use the specified
   ** {@link Row} to populate its value.
   **
   ** @param  row                the {@link Row} to populate the values from.
   **                            <br>
   **                            Allowed object is {@link Row}.
   ** @param  deferChildren      <code>true</code> if the child rows are lazy
   **                            populated at access time; <code>false</code> if
   **                            its reuired to populate them immediatly.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   */
  private JobAdapter(final Row row, final boolean deferChildren) {
    // ensure inheritance
    super(row, deferChildren);

    // convince the platform API
    createIndentifier();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>JobAdapter</code> values object which belongs to the
   ** specified job and use the specified {@link JobDetails} to populate its
   ** value.
   **
   ** @param  data               the {@link JobDetails} providing the values to
   **                            to populate.
   ** @param  last               the last execution history of the
   **                            <code>Scheduled Job</code> providing the status
   **                            values to populate.
   ** @param  actual             the actual trigger status of the job.
   */
  private JobAdapter(final JobDetails data, final JobHistory last, final int actual) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.name          = data.getName();
    this.task          = data.getTaskName();
    // initialize the status of the job with the provided value
    // if a last history is available the status of this history will override
    this.status        = actual;
    this.triggerStatus = data.isTaskStatus();
    this.concurrent    = data.isConcurrent();
    this.retryCount    = data.getRetrycount();
    this.triggerType   = data.getJobScheduleType();
    this.cronType      = data.getCronScheduleType();

    if (last != null) {
      this.status       = Integer.valueOf(last.getStatus());
      this.statusDecode = last.getCustomStatus();
      this.triggerStart = last.getJobStartTime();
      this.triggerStop  = last.getJobEndTime();
    }

    // convince the platform API
    createIndentifier();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setName
  /**
   ** Sets the value of the name property.
   **
   ** @param  value              the value of the name property.
   **                            Allowed object is {@link String}.
   */
  public void setName(final String value) {
    this.name = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getName
  /**
   ** Returns the value of the name property.
   **
   ** @return                    the value of the name property.
   **                            Possible object is {@link String}.
   */
  public String getName() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setTask
  /**
   ** Sets the value of the task property.
   **
   ** @param  value              the value of the task property.
   **                            Allowed object is {@link String}.
   */
  public void setTask(final String value) {
    this.task = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getTask
  /**
   ** Returns the value of the task property.
   **
   ** @return                    the value of the task property.
   **                            Possible object is {@link String}.
   */
  public String getTask() {
    return this.task;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setStatus
  /**
   ** Sets the value of the status property.
   **
   ** @param  value              the value of the status property.
   **                            Allowed object is {@link Integer}.
   */
  public void setStatus(final Integer value) {
    this.status = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getStatus
  /**
   ** Returns the value of the status property.
   **
   ** @return                    the value of the status property.
   **                            Possible object is {@link Integer}.
   */
  public Integer getStatus() {
    return this.status;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setStatusDecode
  /**
   ** Sets the value of the statusDecode property.
   **
   ** @param  value              the value of the statusDecode property.
   **                            Allowed object is {@link String}.
   */
  public void setStatusDecode(final String value) {
    this.statusDecode = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getStatusDecode
  /**
   ** Returns the value of the statusDecode property.
   **
   ** @return                    the value of the statusDecode property.
   **                            Possible object is {@link String}.
   */
  public String getStatusDecode() {
    return this.statusDecode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setTriggerStatus
  /**
   ** Sets the value of the triggerStatus property.
   **
   ** @param  value              the value of the triggerStatus property.
   **                            Allowed object is {@link Boolean}.
   */
  public void setTriggerStatus(final Boolean value) {
    this.triggerStatus = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getTriggerStatus
  /**
   ** Returns the value of the triggerStatus property.
   **
   ** @return                    the value of the triggerStatus property.
   **                            Possible object is {@link Boolean}.
   */
  public Boolean getTriggerStatus() {
    return this.triggerStatus;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setTriggerStart
  /**
   ** Sets the value of the triggerStart property.
   **
   ** @param  value              the value of the triggerStart property.
   **                            Allowed object is {@link Timestamp}.
   */
  public void setTriggerStart(final Timestamp value) {
    this.triggerStart = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getTriggerStart
  /**
   ** Returns the value of the triggerStart property.
   **
   ** @return                    the value of the triggerStart property.
   **                            Possible object is {@link Timestamp}.
   */
  public Timestamp getTriggerStart() {
    return this.triggerStart;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setTriggerStop
  /**
   ** Sets the value of the triggerStop property.
   **
   ** @param  value              the value of the triggerStop property.
   **                            Allowed object is {@link Timestamp}.
   */
  public void setTriggerStop(final Timestamp value) {
    this.triggerStop = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getTriggerStop
  /**
   ** Returns the value of the triggerStop property.
   **
   ** @return                    the value of the triggerStop property.
   **                            Possible object is {@link Timestamp}.
   */
  public Timestamp getTriggerStop() {
    return this.triggerStop;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setTriggerNext
  /**
   ** Sets the value of the triggerNext property.
   **
   ** @param  value              the value of the triggerNext property.
   **                            Allowed object is {@link Timestamp}.
   */
  public void setTriggerNext(final Timestamp value) {
    this.triggerNext = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getTriggerNext
  /**
   ** Returns the value of the triggerNext property.
   **
   ** @return                    the value of the triggerNext property.
   **                            Possible object is {@link Timestamp}.
   */
  public Timestamp getTriggerNext() {
    return this.triggerNext;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setConcurrent
  /**
   ** Sets the value of the concurrent property.
   **
   ** @param  value              the value of the concurrent property.
   **                            Allowed object is {@link Boolean}.
   */
  public void setConcurrent(final Boolean value) {
    this.concurrent = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getConcurrent
  /**
   ** Returns the value of the concurrent property.
   **
   ** @return                    the value of the concurrent property.
   **                            Possible object is {@link Boolean}.
   */
  public Boolean getConcurrent() {
    return this.concurrent;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setRetryCount
  /**
   ** Sets the value of the retryCount property.
   **
   ** @param  value              the value of the retryCount property.
   **                            Allowed object is {@link Integer}.
   */
  public void setRetryCount(final Integer value) {
    this.retryCount = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getRetryCount
  /**
   ** Returns the value of the retryCount property.
   **
   ** @return                    the value of the retryCount property.
   **                            Possible object is {@link Integer}.
   */
  public Integer getRetryCount() {
    return this.retryCount;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setTriggerType
  /**
   ** Sets the value of the trigger property.
   **
   ** @param  value              the value of the trigger property.
   **                            Allowed object is {@link String}.
   */
  public void setTriggerType(final String value) {
    this.triggerType = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getTriggerType
  /**
   ** Returns the value of the triggerType property.
   **
   ** @return                    the value of the triggerType property.
   **                            Possible object is {@link String}.
   */
  public String getTriggerType() {
    return this.triggerType;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setCronType
  /**
   ** Sets the value of the cronType property.
   **
   ** @param  value              the value of the cronType property.
   **                            Allowed object is {@link String}.
   */
  public void setCronType(final String value) {
    this.cronType = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getCronType
  /**
   ** Returns the value of the cronType property.
   **
   ** @return                    the value of the cronType property.
   **                            Possible object is {@link String}.
   */
  public String getCronType() {
    return this.cronType;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parameter
  /**
   ** Returns the value of the parameter property.
   ** <p>
   ** This accessor method returns a reference to the live list, not a
   ** snapshot. Therefore any modification you make to the returned list will
   ** be present inside the object.
   ** <br>
   ** This is why there is not a <code>set</code> method for the flag
   ** property.
   ** <p>
   ** For example, to add a new item, do as follows:
   ** <pre>
   **   parameter().add(newItem);
   ** </pre>
   ** Objects of the following type(s) are allowed in the list
   ** {@link JobParameterAdapter}.
   **
   ** @return                  the live set of the assigned
   **                          <code>option</code>s.
   **                          <br>
   **                          Possible object is {@link List} where each
   **                          element is of type {@link JobParameterAdapter}.
   */
  public final List<JobParameterAdapter> parameter() {
    return this.parameter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parameter
  /**
   ** Add a {@link JobParameterAdapter} to the collection of parameters.
   **
   ** @param  mab              the {@link JobParameterAdapter} to add.
   **                          <br>
   **                          Possible object is {@link JobParameterAdapter}.
   */
  public final void parameter(final JobParameterAdapter mab) {
    this.parameter.add(mab);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create <code>JobAdapter</code> values object which
   ** belongs to the specified job and use the specified {@link JobDetails} to
   ** populate its value.
   **
   ** @param  data               the {@link JobDetails} providing the values to
   **                            to populate.
   **                            <br>
   **                            Allowed object is {@link JobDetails}.
   ** @param  last               the last execution history of the
   **                            <code>Scheduled Job</code> providing the status
   **                            values to populate.
   **                            <br>
   **                            Allowed object is {@link JobHistory}.
   ** @param  actual             the actual trigger status of the job.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    an instance of <code>JobAdapter</code>
   **                            with the properties provided.
   **                            <br>
   **                            Possible object is <code>JobAdapter</code>.
   */
  public static JobAdapter build(final JobDetails data, final JobHistory last, final int actual) {
    return new JobAdapter(data, last, actual);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create {@link JobDetails} values object which belongs to
   ** the specified job and use the specified <code>JobAdapter</code> to
   ** populate its value.
   **
   ** @param  data               the <code>JobAdapter</code> providing the
   **                            values to populate.
   **                            <br>
   **                            Allowed object is <code>JobAdapter</code>.
   **
   ** @return                    an instance of {@link JobDetails} with the
   **                            properties provided.
   **                            <br>
   **                            Possible object is {@link JobDetails}.
   */
  public static JobDetails build(final JobAdapter data) {
    final JobDetails instance = new JobDetails();
    instance.setName(data.name);
    instance.setTaskName(data.task);
    instance.setTaskStatus(data.triggerStatus);
    instance.setRetrycount(data.retryCount);
    instance.setJobScheduleType(data.triggerType);
    instance.setCronScheduleType(data.cronType);

    // obtain the parameters to stored along with the job details
    final HashMap<String, JobParameter> parameter = new HashMap<String, JobParameter>();
    for (JobParameterAdapter p : data.parameter()) {
      final JobParameter x = JobParameterAdapter.build(p);
      parameter.put(x.getName(), x);
    }
    instance.setParams(parameter);
    return instance;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   from
  /**
   ** Factory method to create a <code>JobAdapter</code> values object which use
   ** the specified {@link Row} to populate its value.
   **
   ** @param  data               the {@link Row} to populate the values from.
   **                            <br>
   **                            Allowed object is {@link Row}.
   **
   ** @return                    an instance of <code>JobAdapter</code>
   **                            with the properties provided.
   **                            <br>
   **                            Possible object is <code>JobAdapter</code>.
   */
  public static JobAdapter from(final Row data) {
    return new JobAdapter(data);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   from
  /**
   ** Factory method to create a <code>JobAdapter</code> values object which use
   ** the specified {@link Row} to populate its value.
   **
   ** @param  data               the {@link Row} to populate the values from.
   **                            <br>
   **                            Allowed object is {@link Row}.
   ** @param  defer              <code>true</code> if the child rows are lazy
   **                            populated at access time; <code>false</code> if
   **                            its reuired to populate them immediatly.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    an instance of <code>JobAdapter</code>
   **                            with the properties provided.
   **                            <br>
   **                            Possible object is <code>JobAdapter</code>.
   */
  public static JobAdapter from(final Row data, final boolean defer) {
    return new JobAdapter(data, defer);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createIndentifier
  /**
   ** Factory method to create the indentifer map of this model adapter to
   ** convince the requirements of the model adapter API.
   */
  private void createIndentifier() {
    // convince the platform API
    final Map<String, Serializable> identifier = new HashMap<String, Serializable>();
    identifier.put(PK, this.name);
    setIdentifier(identifier);
  }
}