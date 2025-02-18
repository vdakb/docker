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
    Subsystem   :   System Authorization Management

    File        :   ReportParameterAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ReportParameterAdapter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysauthz.schema;

import oracle.jbo.Row;

import oracle.iam.ui.platform.model.common.ModelAttr;
import oracle.iam.ui.platform.model.common.StaticModelAdapterBean;

////////////////////////////////////////////////////////////////////////////////
// class ReportParameterAdapter
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Data Access Object used by <code>Report Parameter</code> customization.
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
public class ReportParameterAdapter extends StaticModelAdapterBean {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:6091081548582027332")
  private static final long serialVersionUID = 6693981941038474649L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @ModelAttr
  private String path;
  @ModelAttr
  private String name;
  @ModelAttr
  private String label;
  @ModelAttr
  private String dataType;
  @ModelAttr
  private String defaultValue;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>ReportParameterAdapter</code> value object that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ReportParameterAdapter() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ReportParameterAdapter</code> value object which use
   ** the specified {@link Row} to populate its value.
   **
   ** @param  row                the {@link Row} to populate the values from.
   **                            <br>
   **                            Allowed object is {@link Row}.
   */
  public ReportParameterAdapter(final Row row) {
    // ensure inheritance
    super(row);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ReportParameterAdapter</code> value object which use
   ** the specified {@link Row} to populate its value.
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
  public ReportParameterAdapter(final Row row, final boolean deferChildren) {
    // ensure inheritance
    super(row, deferChildren);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setPath
  /**
   ** Sets the value of the path property.
   **
   ** @param  value              the value of the path property.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setPath(final String value) {
    this.path = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getPath
  /**
   ** Returns the value of the path property.
   **
   ** @return                    the value of the path property.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getPath() {
    return this.path;
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
  // Methode:   setLabel
  /**
   ** Sets the value of the label property.
   **
   ** @param  value              the value of the label property.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setLabel(final String value) {
    this.label = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getLabel
  /**
   ** Returns the value of the label property.
   **
   ** @return                    the value of the label property.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getLabel() {
    return this.label;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setDataType
  /**
   ** Sets the value of the dataType property.
   **
   ** @param  value              the value of the dataType property.
   **                            <br>
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
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getDataType() {
    return this.dataType;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setDefaultValue
  /**
   ** Sets the value of the defaultValue property.
   **
   ** @param  value              the value of the defaultValue property.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setDefaultValue(final String value) {
    this.defaultValue = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getDefaultValue
  /**
   ** Returns the value of the defaultValue property.
   **
   ** @return                    the value of the defaultValue property.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getDefaultValue() {
    return this.defaultValue;
  }
}