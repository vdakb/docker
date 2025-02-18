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

    File        :   JobHistoryAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    JobHistoryAdapter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysadmin.schema;

import java.util.Map;
import java.util.HashMap;
import java.util.Collection;

import java.io.Serializable;
import java.io.ByteArrayInputStream;

import java.sql.Timestamp;

import oracle.iam.scheduler.vo.ClassLoaderObjectInputStream;

import oracle.jbo.Row;

import oracle.iam.scheduler.vo.JobHistory;

import oracle.iam.scheduler.vo.JobParameter;

import oracle.iam.ui.platform.model.common.ModelAttr;
import oracle.iam.ui.platform.model.common.StaticModelAdapterBean;

////////////////////////////////////////////////////////////////////////////////
// class JobHistoryAdapter
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** Data Access Object used by System Administration customization.
 ** <br>
 ** Represents the parameter within the context of a particular Job. This is a
 ** History (where the type of object is a JobHistory) in OIM.
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
public class JobHistoryAdapter extends StaticModelAdapterBean {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String       PK               = "historyId";
  public static final String       FK               = "Bind_name";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:1970465298198458297")
  private static final long        serialVersionUID = -6150346694631387752L;

  private static final ClassLoader loader           = ExceptionLoader.build();

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @ModelAttr
  private Double                   historyId;
  @ModelAttr
  private String                   name;
  @ModelAttr
  private String                   infoType;
  @ModelAttr
  private Integer                  status;
  @ModelAttr
  private String                   statusDecode;
  @ModelAttr
  private String                   customStatus;
  @ModelAttr
  private Timestamp                startTime;
  @ModelAttr
  private Timestamp                endTime;
  @ModelAttr
  private String                   exception;
  @ModelAttr
  private Collection<JobParameter> parameter;
  
  //////////////////////////////////////////////////////////////////////////////
  // Memeber classes
  //////////////////////////////////////////////////////////////////////////////
  
  static class ExceptionLoader {
    public static ClassLoader build() {
      // while creating direct object of 'tcADPClassLoader' here will bring
      // compile time dependency of Scheduler project to Server project.
      // Thus, 'getClassLoader' is used to get an object of 'tcADPClassLoader'.
      return com.thortech.xl.dataobj.tcADPClassLoader.getClassLoader();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>JobHistoryAdapter</code> values object that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public JobHistoryAdapter() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>JobHistoryAdapter</code> values object which use the
   ** specified {@link Row} to populate its value.
   **
   ** @param  row                the {@link Row} to populate the values from.
   **                            <br>
   **                            Allowed object is {@link Row}.
   */
  public JobHistoryAdapter(final Row row) {
    // ensure inheritance
    super(row);

    // convince the platform API
    createIndentifier();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>JobHistoryAdapter</code> values object which use the
   ** specified {@link Row} to populate its value.
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
  public JobHistoryAdapter(final Row row, final boolean deferChildren) {
    // ensure inheritance
    super(row, deferChildren);

    // convince the platform API
    createIndentifier();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>JobHistoryAdapter</code> values object which belongs to
   ** the a job and use the specified {@link JobHistory} to populate its value.
   **
   ** @param  data               the {@link JobHistory} providing the values to
   **                            to populate.
   **                            <br>
   **                            Allowed object is {@link JobHistory}.
   */
  private JobHistoryAdapter(final JobHistory data) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.historyId    = data.getId();
    this.name         = data.getJobName();
    this.infoType     = data.getInfotype();
    this.status       = Integer.valueOf(data.getStatus());
    this.customStatus = data.getCustomStatus();
    this.startTime    = data.getJobStartTime();
    this.endTime      = data.getJobEndTime();
    this.parameter    = data.getRuntimeParams().values();
    final Exception e = toException(data.getErrorData());
    this.exception    = e == null ? "" : e.getMessage();
    // convince the platform API
    createIndentifier();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setHistoryId
  /**
   ** Sets the value of the historyId property.
   **
   ** @param  value              the value of the historyId property.
   **                            <br>
   **                            Allowed object is {@link Double}.
   */
  public void setHistoryId(final Double value) {
    this.historyId = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   geHistoryId
  /**
   ** Returns the value of the historyId property.
   **
   ** @return                    the value of the historyId property.
   **                            <br>
   **                            Possible object is {@link Double}.
   */
  public Double getHistoryId() {
    return this.historyId;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setName
  /**
   ** Sets the value of the name property.
   **
   ** @param  value              the value of the name property.
   **                            <br>
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
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getName() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setInfoType
  /**
   ** Sets the value of the infoType property.
   **
   ** @param  value              the value of the infoType property.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setInfoType(final String value) {
    this.infoType = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getInfoType
  /**
   ** Returns the value of the infoType property.
   **
   ** @return                    the value of the infoType property.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getInfoType() {
    return this.infoType;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setStatus
  /**
   ** Sets the value of the status property.
   **
   ** @param  value              the value of the status property.
   **                            <br>
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
   **                            <br>
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
   **                            <br>
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
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getStatusDecode() {
    return this.statusDecode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setCustomStatus
  /**
   ** Sets the value of the customStatus property.
   **
   ** @param  value              the value of the customStatus property.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setCustomStatus(final String value) {
    this.customStatus = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getCustomStatus
  /**
   ** Returns the value of the customStatus property.
   **
   ** @return                    the value of the customStatus property.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getCustomStatus() {
    return this.customStatus;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setStartTime
  /**
   ** Sets the value of the startTime property.
   **
   ** @param  value              the value of the startTime property.
   **                            <br>
   **                            Allowed object is {@link Timestamp}.
   */
  public void setCreatedOn(final Timestamp value) {
    this.startTime = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getStartTime
  /**
   ** Returns the value of the startTime property.
   **
   ** @return                    the value of the startTime property.
   **                            <br>
   **                            Possible object is {@link Timestamp}.
   */
  public Timestamp getStartTime() {
    return this.startTime;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setEndTime
  /**
   ** Sets the value of the endTime property.
   **
   ** @param  value              the value of the endTime property.
   **                            <br>
   **                            Allowed object is {@link Timestamp}.
   */
  public void setEndTime(final Timestamp value) {
    this.endTime = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getEndTime
  /**
   ** Returns the value of the endTime property.
   **
   ** @return                    the value of the endTime property.
   **                            <br>
   **                            Possible object is {@link Timestamp}.
   */
  public Timestamp getEndTime() {
    return this.endTime;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setException
  /**
   ** Sets the value of the exception property.
   **
   ** @param  value              the value of the exception property.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setException(final String value) {
    this.exception = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getException
  /**
   ** Returns the value of the exception property.
   **
   ** @return                    the value of the exception property.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getException() {
    return this.exception;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setParameter
  /**
   ** Sets the value of the parameter property.
   **
   ** @param  value              the value of the parameter property.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type {@link JobParameter}.
   */
  public void setParameter(final Collection<JobParameter> value) {
    this.parameter = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getParameter
  /**
   ** Returns the value of the parameter property.
   **
   ** @return                    the value of the parameter property.
   **                            <br>
   **                            Possible object is {@link Collection} where each
   **                            element is of type {@link JobParameter}.
   */
  public Collection<JobParameter> getParameter() {
    return this.parameter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create <code>JobHistoryAdapter</code> values object
   ** which belongs to the specified job and use the specified
   ** {@link JobHistory} to populate its value.
   **
   ** @param  data               the {@link JobHistory} providing the values to
   **                            to populate.
   **                            <br>
   **                            Allowed object is {@link JobHistory}.
   **
   ** @return                    an instance of <code>JobHistoryAdapter</code>
   **                            with the properties provided.
   **                            <br>
   **                            Possible object is {@link JobHistoryAdapter}.
   */
  public static JobHistoryAdapter build(final JobHistory data) {
    return new JobHistoryAdapter(data);
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
    identifier.put(PK, this.historyId);
    setIdentifier(identifier);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toException
  private Exception toException(final byte[] content) {
    try {
      // while creating direct object of 'tcADPClassLoader' here will bring
      // compile time dependency of Scheduler project to Server project.
      // Thus, ExceptionLoader is used to get an object of 'tcADPClassLoader'.
      final ClassLoaderObjectInputStream stream = new ClassLoaderObjectInputStream(loader, new ByteArrayInputStream(content));
      return (Exception)stream.readObject();
    }
    catch (Exception e) {
      return e;
    }     
  }
}