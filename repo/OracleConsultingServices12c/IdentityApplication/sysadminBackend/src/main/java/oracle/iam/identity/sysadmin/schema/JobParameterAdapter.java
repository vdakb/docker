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

    File        :   JobParameterAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    JobParameterAdapter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysadmin.schema;

import java.util.Map;
import java.util.HashMap;

import java.io.Serializable;

import oracle.jbo.Row;

import oracle.iam.scheduler.vo.JobParameter;

import oracle.iam.ui.platform.model.common.ModelAttr;
import oracle.iam.ui.platform.model.common.StaticModelAdapterBean;

////////////////////////////////////////////////////////////////////////////////
// class JobParameterAdapter
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** Data Access Object used by System Administration customization.
 ** <br>
 ** Represents the parameter within the context of a particular Job. This is a
 ** Parameter (where the type of object is a JobParameter) in OIM.
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
public class JobParameterAdapter extends StaticModelAdapterBean {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String PK               = "parameterKey";
  public static final String FK_JOB           = "Bind_name";
  public static final String FK_TASK          = "Bind_task";

  public static final String ACTION           = "pendingAction";

  public static final String NIL              = "nil";
  public static final String ADD              = "add";
  public static final String MOD              = "mod";
  public static final String DEL              = "del";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-1810186582568953589")
  private static final long  serialVersionUID = 7442715254369422445L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @ModelAttr
  private String             parameterKey;
  @ModelAttr
  private String             name;
  @ModelAttr
  private String             dataType;
  @ModelAttr
  private Serializable       value;
  @ModelAttr
  private Boolean            required;
  @ModelAttr
  private Boolean            encrypted;
  @ModelAttr
  private String             helpText;
  @ModelAttr
  private String             pendingAction;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>JobParameterAdapter</code> values object that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected JobParameterAdapter() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>JobParameterAdapter</code> values object which use the
   ** specified {@link Row} to populate its value.
   **
   ** @param  row                the {@link Row} to populate the values from.
   */
  protected JobParameterAdapter(final Row row) {
    // ensure inheritance
    super(row);

    // convince the platform API
    createIndentifier();

    this.value = convert(this.dataType, this.value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>JobParameterAdapter</code> values object which use the
   ** specified {@link Row} to populate its value.
   **
   ** @param  row                the {@link Row} to populate the values from.
   ** @param  deferChildren      <code>true</code> if the child rows are lazy
   **                            populated at access time; <code>false</code> if
   **                            its reuired to populate them immediatly.
   */
  protected JobParameterAdapter(final Row row, final boolean deferChildren) {
    // ensure inheritance
    super(row, deferChildren);

    // convince the platform API
    createIndentifier();
    
    this.value = convert(this.dataType, this.value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>JobParameterAdapter</code> values object which belongs
   ** to the a task or job and use the specified {@link JobParameter} to
   ** populate its value.
   **
   ** @param  data               the {@link JobParameter} providing the values
   **                            to populate.
   */
  protected JobParameterAdapter(final JobParameter data) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.parameterKey = data.getParameterKey();
    this.name         = data.getName();
    this.dataType     = data.getDataType();
    this.value        = data.getValue();
    this.required     = data.isRequired();
    this.encrypted    = data.isEncrypted();
    this.helpText     = data.getHelpText();

    // convince the platform API
    createIndentifier();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setParameterKey
  /**
   ** Sets the value of the parameterKey property.
   **
   ** @param  value              the value of the parameterKey property.
   **                            Allowed object is {@link String}.
   */
  public void setParameterKey(final String value) {
    this.parameterKey = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getParameterKey
  /**
   ** Returns the value of the parameterKey property.
   **
   ** @return                    the value of the parameterKey property.
   **                            Possible object is {@link String}.
   */
  public String getParameterKey() {
    return this.parameterKey;
  }

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
  // Methode:   setDataType
  /**
   ** Sets the value of the dataType property.
   **
   ** @param  value              the value of the dataType property.
   **                            Allowed object is {@link String}.
   */
  public void setDataType(final String value) {
    this.dataType = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getDataType
  /**
   ** Returns the value of the dataType property.
   **
   ** @return                    the value of the dataType property.
   **                            Possible object is {@link String}.
   */
  public String getDataType() {
    return this.dataType;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setValue
  /**
   ** Sets the value of the value property.
   **
   ** @param  value              the value of the value property.
   **                            Allowed object is {@link Serializable}.
   */
  public void setValue(final Serializable value) {
    this.value = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getValue
  /**
   ** Returns the value of the value property.
   **
   ** @return                    the value of the value property.
   **                            Possible object is {@link Serializable}.
   */
  public Serializable getValue() {
    return this.value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setRequired
  /**
   ** Sets the value of the required property.
   **
   ** @param  value              the value of the required property.
   **                            Allowed object is {@link Boolean}.
   */
  public void setRequired(final Boolean value) {
    this.required = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getRequired
  /**
   ** Returns the value of the required property.
   **
   ** @return                    the value of the required property.
   **                            Possible object is {@link Boolean}.
   */
  public Boolean getRequired() {
    return this.required;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setEncrypted
  /**
   ** Sets the value of the encrypted property.
   **
   ** @param  value              the value of the encrypted property.
   **                            Allowed object is {@link Boolean}.
   */
  public void setEncrypted(final Boolean value) {
    this.encrypted = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getEncrypted
  /**
   ** Returns the value of the encrypted property.
   **
   ** @return                    the value of the encrypted property.
   **                            Possible object is {@link Boolean}.
   */
  public Boolean getEncrypted() {
    return this.encrypted;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setHelpText
  /**
   ** Sets the value of the helpText property.
   **
   ** @param  value              the value of the helpText property.
   **                            Allowed object is {@link String}.
   */
  public void setHelpText(final String value) {
    this.helpText = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getHelpText
  /**
   ** Returns the value of the helpText property.
   **
   ** @return                    the value of the helpText property.
   **                            Possible object is {@link String}.
   */
  public String getHelpText() {
    return this.helpText;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setPendingAction
  /**
   ** Sets the value of the pendingAction property.
   **
   ** @param  value              the value of the pendingAction property.
   **                            Allowed object is {@link String}.
   */
  public void setPendingAction(final String value) {
    this.pendingAction = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getPendingAction
  /**
   ** Returns the value of the pendingAction property.
   **
   ** @return                    the value of the pendingAction property.
   **                            Possible object is {@link String}.
   */
  public String getPendingAction() {
    return this.pendingAction;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create <code>JobParameterAdapter</code> values object
   ** which belongs to the specified task and use the specified
   ** {@link JobParameter} to populate its value.
   **
   ** @param  data               the {@link JobParameter} providing the values
   **                            to populate.
   **                            <br>
   **                            Allowed object is {@link JobParameter}.
   **
   ** @return                    an instance of <code>JobParameterAdapter</code>
   **                            with the properties provided.
   */
  public static JobParameterAdapter build(final JobParameter data) {
    return new JobParameterAdapter(data);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create <code>JobParameter</code> values object which
   ** belongs to the specified task and use the specified
   ** {@link JobParameterAdapter} to populate its value.
   **
   ** @param  data               the {@link JobParameterAdapter} providing the
   **                            values to populate.
   **                            <br>
   **                            Allowed object is {@link JobParameterAdapter}.
   **
   ** @return                    an instance of <code>JobParameter</code>
   **                            with the properties provided.
   */
  public static JobParameter build(final JobParameterAdapter data) {
    final JobParameter parameter = new JobParameter();
    // transfer instance attributes
    parameter.setParameterKey(data.parameterKey);
    parameter.setName(data.name);
    parameter.setDataType(data.dataType);
    parameter.setValue(convert(data.dataType, data.value));
    parameter.setRequired(data.required);
    parameter.setEncrypted(data.encrypted);
    parameter.setHelpText(data.helpText);
    System.out.println(data.dataType + "::" + data.name + "::" + data.value);
    return parameter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   from
  /**
   ** Factory method to create a <code>JobParameterAdapter</code> values object
   ** which use the specified {@link Row} to populate its value.
   **
   ** @param  data               the {@link Row} to populate the values from.
   **                            <br>
   **                            Allowed object is {@link Row}.
   **
   ** @return                    an instance of <code>JobParameterAdapter</code>
   **                            with the properties provided.
   **                            <br>
   **                            Possible object is
   **                            <code>JobParameterAdapter</code>.
   */
  public static JobParameterAdapter from(final Row data) {
    return new JobParameterAdapter(data);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   from
  /**
   ** Factory method to create a <code>JobParameterAdapter</code> values object
   ** which use the specified {@link Row} to populate its value.
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
   ** @return                    an instance of <code>JobParameterAdapter</code>
   **                            with the properties provided.
   **                            <br>
   **                            Possible object is
   **                            <code>JobParameterAdapter</code>.
   */
  public static JobParameterAdapter from(final Row data, final boolean defer) {
    return new JobParameterAdapter(data, defer);
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
    identifier.put(PK, this.parameterKey);
    setIdentifier(identifier);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   convert
  private static Serializable convert(final String type, final Serializable value) {
    switch (type) {
      case JobParameter.DATA_TYPE_BOOLEAN    : return value == null ? Boolean.FALSE : Boolean.valueOf(String.valueOf(value));
      case JobParameter.DATA_TYPE_NUMBER     : return value == null ? null : Long.valueOf(String.valueOf(value));
      case JobParameter.DATA_TYPE_ITRESOURCE :
      default                                : return value;
    }
  }
}