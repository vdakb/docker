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
    Subsystem   :   System Configuration Management

    File        :   PropertyAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    PropertyAdapter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysconfig.schema;

import oracle.iam.conf.vo.SystemProperty;

import oracle.jbo.Row;

import oracle.iam.ui.platform.model.common.ModelAttr;
import oracle.iam.ui.platform.model.common.StaticModelAdapterBean;

////////////////////////////////////////////////////////////////////////////////
// class PropertyAdapter
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Data Access Object used by Property customization.
 ** <p>
 ** Define an instance variable for each VO attribute, with the data type that
 ** corresponds to the VO attribute type. The name of the instance variable must
 ** match the name of the VO attribute, and the case must match.
 ** <p>
 ** When the instance of this class is passed to the methods in the
 ** corresponding <code>Application Module</code> class, the
 ** <code>Application Module</code> can call the getter methods to retrieve the
 ** values from the Adapter and pass them to the OIM APIs.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
 */
public class PropertyAdapter extends StaticModelAdapterBean {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String PK               = "propertyKey";
  public static final String PROPERTYNAME     = "propertyName";
  public static final String VALUE            = "value";
  public static final String NAME             = "name";
  public static final String DATALEVEL        = "dataLevel";
  public static final String SYSTEM           = "system";
  public static final String LOGINREQUIRED    = "loginRequired";
  public static final String RUNON            = "runOn";
  public static final String NOTE             = "note";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:2402243376531055892")
  private static final long  serialVersionUID = 8108127317662460400L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private SystemProperty    instance;

  @ModelAttr
  private Long               propertyKey;
  @ModelAttr
  private String             propertyName;
  @ModelAttr
  private String             value;
  @ModelAttr
  private String             name;
  @ModelAttr
  private String             dataLevel;
  @ModelAttr
  private String             system;
  @ModelAttr
  private String             loginRequired;
  @ModelAttr
  private String             runOn;
  @ModelAttr
  private String             note;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>PropertyAdapter</code> values object that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public PropertyAdapter() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>PropertyAdapter</code> values object which use the
   ** specified {@link Row} to populate its value.
   **
   ** @param  row                the {@link Row} to populate the values from.
   */
  public PropertyAdapter(final Row row) {
    // ensure inheritance
    super(row);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setInstance
  /**
   ** Sets the value of the instance property.
   **
   ** @param  value              the value of the instance property.
   **                            Allowed object is {@link SystemProperty}.
   */
  public void setInstance(final SystemProperty value) {
    this.instance = value;
    transfer();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setPropertyKey
  /**
   ** Sets the value of the propertyKey property.
   **
   ** @param  value              the value of the propertyKey property.
   **                            Allowed object is {@link Long}.
   */
  public void setPropertyKey(final Long value) {
    this.propertyKey = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getPropertyKey
  /**
   ** Returns the value of the propertyKey property.
   **
   ** @return                    the value of the propertyKey property.
   **                            Allowed object is {@link Long}.
   */
  public Long getPropertyKey() {
    return this.propertyKey;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setPropertyName
  /**
   ** Sets the value of the propertyName property.
   **
   ** @param  value              the value of the propertyName property.
   **                            Allowed object is {@link String}.
   */
  public void setPropertyName(final String value) {
    this.propertyName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getPropertyName
  /**
   ** Returns the value of the propertyName property.
   **
   ** @return                    the value of the propertyName property.
   **                            Allowed object is {@link String}.
   */
  public String getPropertyName() {
    return this.propertyName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setValue
  /**
   ** Sets the value of the value property.
   **
   ** @param  value              the value of the value property.
   **                            Allowed object is {@link String}.
   */
  public void setValue(final String value) {
    this.value = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getValue
  /**
   ** Returns the value of the value property.
   **
   ** @return                    the value of the value property.
   **                            Allowed object is {@link String}.
   */
  public String getValue() {
    return this.value;
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
   **                            Allowed object is {@link String}.
   */
  public String getName() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setDataLevel
  /**
   ** Sets the value of the dataLevel property.
   **
   ** @param  value              the value of the dataLevel property.
   **                            Allowed object is {@link String}.
   */
  public void setDataLevel(final String value) {
    this.dataLevel = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getDataLevel
  /**
   ** Returns the value of the dataLevel property.
   **
   ** @return                    the value of the dataLevel property.
   **                            Allowed object is {@link String}.
   */
  public String getDataLevel() {
    return this.dataLevel;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setSystem
  /**
   ** Sets the value of the system property.
   **
   ** @param  value              the value of the system property.
   **                            Allowed object is {@link String}.
   */
  public void setSystem(final String value) {
    this.system = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getSystem
  /**
   ** Returns the value of the system property.
   **
   ** @return                    the value of the system property.
   **                            Allowed object is {@link String}.
   */
  public String getSystem() {
    return this.system;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setLoginRequired
  /**
   ** Sets the value of the loginRequired property.
   **
   ** @param  value              the value of the loginRequired property.
   **                            Allowed object is {@link String}.
   */
  public void setLoginRequired(final String value) {
    this.loginRequired = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getLoginRequired
  /**
   ** Returns the value of the loginRequired property.
   **
   ** @return                    the value of the loginRequired property.
   **                            Allowed object is {@link String}.
   */
  public String getLoginRequired() {
    return this.loginRequired;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setRunOn
  /**
   ** Sets the value of the runOn property.
   **
   ** @param  value              the value of the runOn property.
   **                            Allowed object is {@link String}.
   */
  public void setRunOn(final String value) {
    this.runOn = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getRunOn
  /**
   ** Returns the value of the runOn property.
   **
   ** @return                    the value of the runOn property.
   **                            Allowed object is {@link String}.
   */
  public String getRunOn() {
    return this.runOn;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setNote
  /**
   ** Sets the value of the note property.
   **
   ** @param  value              the value of the note property.
   **                            Allowed object is {@link String}.
   */
  public void setNote(final String value) {
    this.note = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getNote
  /**
   ** Returns the value of the note property.
   **
   ** @return                    the value of the note property.
   **                            Allowed object is {@link String}.
   */
  public String getNote() {
    return this.note;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transfer
  private void transfer() {
    // set all attributes required.
    // TODO system, runOn?
    setPropertyKey(Long.valueOf(this.instance.getId()));
    setPropertyName(this.instance.getPtyKeyword());
    setValue(this.instance.getPtyValue());
    setName(this.instance.getPtyName());
    setDataLevel(this.instance.getPtyDataLevel());
    setLoginRequired(this.instance.getPtyLoginrequired());
    setRunOn(this.instance.getPtyLoginrequired());
  }
}