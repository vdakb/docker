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

    File        :   JobHistoryParameterAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    JobHistoryParameterAdapter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysadmin.schema;

import java.util.List;
import java.util.ArrayList;

import java.io.Serializable;

import oracle.jbo.Row;

import oracle.iam.scheduler.vo.JobHistoryParam;

import oracle.iam.ui.platform.model.common.ModelAttr;
import oracle.iam.ui.platform.model.common.StaticModelAdapterBean;

import oracle.hst.foundation.utility.CollectionUtility;

////////////////////////////////////////////////////////////////////////////////
// class JobHistoryParameterAdapter
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Data Access Object used by System Administration customization.
 ** <br>
 ** Represents the parameter within the context of a particular Job History.
 ** This is a Parameter (where the type of object is a JobHistoryParam) in OIM.
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
public class JobHistoryParameterAdapter extends StaticModelAdapterBean {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String        RUNTIME          = "runtime";
  public static final String        CUSTOM           = "custom";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-6901544078802204469")
  private static final long         serialVersionUID = -2851199676531482292L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @ModelAttr
  private List<JobParameterAdapter> runtime;
  @ModelAttr
  private List<Serializable>        custom;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>JobHistoryParameterAdapter</code> values object
   ** that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public JobHistoryParameterAdapter() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>JobHistoryParameterAdapter</code> values object which
   ** use the specified {@link Row} to populate its value.
   **
   ** @param  row                the {@link Row} to populate the values from.
   */
  public JobHistoryParameterAdapter(final Row row) {
    // ensure inheritance
    super(row);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>JobHistoryParameterAdapter</code> values object which
   ** use the specified {@link Row} to populate its value.
   **
   ** @param  row                the {@link Row} to populate the values from.
   ** @param  deferChildren      <code>true</code> if the child rows are lazy
   **                            populated at access time; <code>false</code> if
   **                            its reuired to populate them immediatly.
   */
  public JobHistoryParameterAdapter(final Row row, final boolean deferChildren) {
    // ensure inheritance
    super(row, deferChildren);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>JobHistoryParameterAdapter</code> values object which
   ** belongs to the a task or job and use the specified {@link JobParameter} to
   ** populate its value.
   **
   ** @param  data               the {@link JobHistoryParam} providing the values
   **                            to populate.
   */
  private JobHistoryParameterAdapter(final JobHistoryParam data) {
    // ensure inheritance
    super();

    // initialize instance attributes
    List<String> name = CollectionUtility.sortedList(data.getJobRunParams().keySet());
    if (name != null && name.size() > 0) {
      this.runtime = new ArrayList<JobParameterAdapter>(name.size());
      for (String cursor : name) {
        this.runtime.add(JobParameterAdapter.build(data.getJobRunParams().get(cursor)));
      }
    }

    name = CollectionUtility.sortedList(data.getCustomParams().keySet());
    if (name != null && name.size() > 0) {
      this.custom = new ArrayList<Serializable>(name.size());
      for (String cursor : name) {
        this.custom.add(data.getCustomParams().get(cursor));
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setRuntime
  /**
   ** Sets the value of the runtime property.
   **
   ** @param  value              the value of the runtime property.
   **                            Allowed object is {@link List} of
   **                            {@link JobParameterAdapter}.
   */
  public void setRuntime(final List<JobParameterAdapter> value) {
    this.runtime = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getRuntime
  /**
   ** Returns the value of the runtime property.
   **
   ** @return                    the value of the runtime property.
   **                            Possible object is {@link List} of
   **                            {@link JobParameterAdapter}.
   */
  public List<JobParameterAdapter> getRuntime() {
    return this.runtime;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setCustom
  /**
   ** Sets the value of the custom property.
   **
   ** @param  value              the value of the custom property.
   **                            Allowed object is {@link List} of
   **                            {@link Serializable}.
   */
  public void setCustom(final List<Serializable> value) {
    this.custom = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getCustom
  /**
   ** Returns the value of the custom property.
   **
   ** @return                    the value of the custom property.
   **                            Possible object is {@link List} of
   **                            {@link Serializable}.
   */
  public List<Serializable> getCustom() {
    return this.custom;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create <code>JobHistoryParameterAdapter</code> values
   ** object which belongs to the specified history and use the specified
   ** {@link JobHistoryParam} to populate its value.
   **
   ** @param  data               the {@link JobHistoryParam} providing the
   **                            values to populate.
   **
   ** @return                    an instance of
   **                            <code>JobHistoryParameterAdapter</code> with
   **                            the properties provided.
   */
  public static JobHistoryParameterAdapter build(final JobHistoryParam data) {
    return new JobHistoryParameterAdapter(data);
  }
}