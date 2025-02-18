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
    Subsystem   :   System Provisioning Management

    File        :   EndpointTypeAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    EndpointTypeAdapter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysprov.schema;

import oracle.jbo.Row;

import oracle.iam.ui.platform.model.common.ModelAttr;
import oracle.iam.ui.platform.model.common.StaticModelAdapterBean;

////////////////////////////////////////////////////////////////////////////////
// class EndpointTypeAdapter
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** Data Access Object used by <code>IT Resource Type</code> customization.
 ** <p>
 ** Implementing the data transfer functionalities to retrieve and manage
 ** <code>IT Resource Type</code>s.
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
public class EndpointTypeAdapter extends StaticModelAdapterBean {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String PK               = "endpointTypeKey";
  public static final String NAME             = "endpointTypeName";
  public static final String MULTIPLE         = "multiple";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-7286494534290940575")
  private static final long  serialVersionUID = 2738148764059602592L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @ModelAttr
  private Long               endpointTypeKey;
  @ModelAttr
  private String             endpointTypeName;
  @ModelAttr
  private Boolean            multiple;
  @ModelAttr
  private String             note;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>EndpointTypeAdapter</code> values object
   ** that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private EndpointTypeAdapter() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>EndpointTypeAdapter</code> values object which use
   ** the specified {@link Row} to populate its value.
   **
   ** @param  row                the {@link Row} to populate the values from.
   **                            <br>
   **                            Allowed object is {@link Row}.
   */
  public EndpointTypeAdapter(final Row row) {
    // ensure inheritance
    super(row);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>EndpointTypeAdapter</code> values object which use
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
  public EndpointTypeAdapter(final Row row, final boolean deferChildren) {
    // ensure inheritance
    super(row, deferChildren);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setEndpointTypeKey
  /**
   ** Sets the value of the endpointTypeKey property.
   **
   ** @param  value              the value of the endpointTypeKey property.
   **                            <br>
   **                            Allowed object is {@link Long}.
   */
  public void setEndpointTypeKey(final Long value) {
    this.endpointTypeKey = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getEndpointTypeKey
  /**
   ** Returns the value of the endpointTypeKey property.
   **
   ** @return                    the value of the endpointTypeKey property.
   **                            <br>
   **                            Possible object is {@link Long}.
   */
  public Long getEndpointTypeKey() {
    return this.endpointTypeKey;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setEndpointTypeName
  /**
   ** Sets the value of the endpointTypeName property.
   **
   ** @param  value              the value of the endpointTypeName property.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setEndpointTypeName(final String value) {
    this.endpointTypeName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getEndpointTypeName
  /**
   ** Returns the value of the endpointTypeName property.
   **
   ** @return                    the value of the endpointTypeName property.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getEndpointTypeName() {
    return this.endpointTypeName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setMultiple
  /**
   ** Sets the value of the multiple property.
   **
   ** @param  value              the value of the multiple property.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   */
  public void setMultiple(final Boolean value) {
    this.multiple = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getMultiple
  /**
   ** Returns the value of the multiple property.
   **
   ** @return                    the value of the multiple property.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   */
  public Boolean getMultiple() {
    return this.multiple;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setNote
  /**
   ** Sets the value of the note property.
   **
   ** @param  value              the value of the note property.
   **                            <br>
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
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public String getNote() {
    return this.note;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   build
  /**
   ** Factory method to create an empty <code>EndpointTypeAdapter</code>.
   **
   ** @return                    the model adapter bean.
   **                            <br>
   **                            Possible object is
   **                            <code>EndpointTypeAdapter</code>.
   */
  public static EndpointTypeAdapter build() {
    return new EndpointTypeAdapter();
  }
}