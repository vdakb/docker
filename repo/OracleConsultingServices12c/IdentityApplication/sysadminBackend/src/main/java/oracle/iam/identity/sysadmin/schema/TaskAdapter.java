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

    File        :   TaskAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    TaskAdapter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysadmin.schema;

import java.util.Map;
import java.util.List;
import java.util.HashMap;

import java.io.Serializable;

import oracle.jbo.Row;

import oracle.iam.scheduler.vo.ScheduledTask;

import oracle.iam.ui.platform.model.common.ModelAttr;
import oracle.iam.ui.platform.model.common.StaticModelAdapterBean;

////////////////////////////////////////////////////////////////////////////////
// class TaskAdapter
// ~~~~~ ~~~~~~~~~~~
/**
 ** Data Access Object used by System Administration customization.
 ** <br>
 ** Represents the job within the context of a particular Scheduler. This is a
 ** Task (where the type of object is a TaskDetails) in OIM.
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
public class TaskAdapter extends StaticModelAdapterBean {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String        PK               = "name";
  public static final String        CLASSNAME        = "className";
  public static final String        DESCRIPTION      = "description";
  public static final String        RETRYCOUNT       = "retryCount";
  public static final String        PARAMETER        = "parameter";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:4934845388683729086")
  private static final long         serialVersionUID = 152731564990489895L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @ModelAttr
  private String                    name;
  @ModelAttr
  private String                    className;
  @ModelAttr
  private String                    description;
  @ModelAttr
  private Integer                   retryCount;
  @ModelAttr
  private List<JobParameterAdapter> parameter;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>TaskAdapter</code> values object that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TaskAdapter() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TaskAdapter</code> values object which use the specified
   ** {@link Row} to populate its value.
   **
   ** @param  row                the {@link Row} to populate the values from.
   */
  public TaskAdapter(final Row row) {
    // ensure inheritance
    super(row);

    // convince the platform API
    createIndentifier();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TaskAdapter</code> values object which use the specified
   ** {@link Row} to populate its value.
   **
   ** @param  row                the {@link Row} to populate the values from.
   ** @param  deferChildren      <code>true</code> if the child rows are lazy
   **                            populated at access time; <code>false</code> if
   **                            its reuired to populate them immediatly.
   */
  public TaskAdapter(final Row row, final boolean deferChildren) {
    // ensure inheritance
    super(row, deferChildren);

    // convince the platform API
    createIndentifier();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TaskAdapter</code> values object which belongs to the a
   ** task and use the specified {@link ScheduledTask} to populate its value.
   **
   ** @param  data               the {@link ScheduledTask} providing the values
   **                            to populate.
   */
  private TaskAdapter(final ScheduledTask data) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.name        = data.getName();
    this.className   = data.getClassName();
    this.description = data.getDescription();
    this.retryCount  = data.getRetryCount();

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
  // Methode:   setClassName
  /**
   ** Sets the value of the className property.
   **
   ** @param  value              the value of the className property.
   **                            Allowed object is {@link String}.
   */
  public void setClassName(final String value) {
    this.className = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getClassName
  /**
   ** Returns the value of the className property.
   **
   ** @return                    the value of the className property.
   **                            Possible object is {@link String}.
   */
  public String getClassName() {
    return this.className;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setDescription
  /**
   ** Sets the value of the description property.
   **
   ** @param  value              the value of the description property.
   **                            Allowed object is {@link String}.
   */
  public void setDescription(final String value) {
    this.description = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getDescription
  /**
   ** Returns the value of the description property.
   **
   ** @return                    the value of the description property.
   **                            Possible object is {@link String}.
   */
  public String getDescription() {
    return this.description;
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
  // Methode:   setParameter
  /**
   ** Sets the value of the parameter property.
   **
   ** @param  value              the value of the parameter property.
   **                            Allowed object is {@link List} of
   **                            {@link JobParameterAdapter}.
   */
  public void setParameter(final List<JobParameterAdapter> value) {
    this.parameter = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getParameter
  /**
   ** Returns the value of the parameter property.
   **
   ** @return                    the value of the parameter property.
   **                            Possible object is {@link List} of
   **                            {@link JobParameterAdapter}.
   */
  public List<JobParameterAdapter> getParameter() {
    return this.parameter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create <code>TaskAdapter</code> values object
   ** which belongs to the specified task and use the specified
   ** {@link ScheduledTask} to populate its value.
   **
   ** @param  data               the {@link ScheduledTask} providing the values
   **                            to populate.
   **
   ** @return                    an instance of <code>TaskAdapter</code> with
   **                            the properties provided.
   */
  public static TaskAdapter build(final ScheduledTask data) {
    return new TaskAdapter(data);
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