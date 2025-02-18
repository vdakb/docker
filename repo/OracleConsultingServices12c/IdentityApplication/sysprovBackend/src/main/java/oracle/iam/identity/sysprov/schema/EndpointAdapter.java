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

    File        :   EndpointAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    EndpointAdapter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysprov.schema;

import oracle.jbo.Row;

import oracle.iam.ui.platform.model.common.ModelAttr;
import oracle.iam.ui.platform.model.common.StaticModelAdapterBean;

////////////////////////////////////////////////////////////////////////////////
// class EndpointAdapter
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Data Access Object used by <code>IT Resource</code> instance customization.
 ** <p>
 ** Implementing the data transfer functionalities to retrieve and manage
 ** <code>IT Resource</code>s.
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
public class EndpointAdapter extends StaticModelAdapterBean {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String PK               = "endpointKey";
  public static final String NAME             = "endpointName";
  public static final String TYPE             = "endpointType";
  public static final String TYPE_FK          = "Bind_" + TYPE;
  public static final String REMOTE           = "remoteManager";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:4177294841205931788")
  private static final long  serialVersionUID = -7213249752826313150L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @ModelAttr
  private Long               endpointKey;
  @ModelAttr
  private String             endpointName;
  @ModelAttr
  private String             endpointType;
  @ModelAttr
  private String             remoteManager;
  @ModelAttr
  private String             note;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>EndpointAdapter</code> values object that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private EndpointAdapter() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>EndpointAdapter</code> values object which use the
   ** specified {@link Row} to populate its value.
   **
   ** @param  row                the {@link Row} to populate the values from.
   */
  public EndpointAdapter(final Row row) {
    // ensure inheritance
    super(row);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>EndpointAdapter</code> values object which use the
   ** specified {@link Row} to populate its value.
   **
   ** @param  row                the {@link Row} to populate the values from.
   ** @param  deferChildren      <code>true</code> if the child rows are lazy
   **                            populated at access time; <code>false</code> if
   **                            its reuired to populate them immediatly.
   */
  public EndpointAdapter(final Row row, final boolean deferChildren) {
    // ensure inheritance
    super(row, deferChildren);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setEndpointKey
  /**
   ** Sets the value of the endpointKey property.
   **
   ** @param  value              the value of the endpointKey property.
   **                            Allowed object is {@link Long}.
   */
  public void setEndpointKey(final Long value) {
    this.endpointKey = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getEndpointKey
  /**
   ** Returns the value of the endpointKey property.
   **
   ** @return                    the value of the endpointKey property.
   **                            Possible object is {@link Long}.
   */
  public Long getEndpointKey() {
    return this.endpointKey;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setEndpointName
  /**
   ** Sets the value of the endpointName property.
   **
   ** @param  value              the value of the endpointName property.
   **                            Allowed object is {@link String}.
   */
  public void setEndpointName(final String value) {
    this.endpointName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getEndpointName
  /**
   ** Returns the value of the endpointName property.
   **
   ** @return                    the value of the endpointName property.
   **                            Possible object is {@link String}.
   */
  public String getEndpointName() {
    return this.endpointName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setEndpointType
  /**
   ** Sets the value of the endpointType property.
   **
   ** @param  value              the value of the endpointType property.
   **                            Allowed object is {@link String}.
   */
  public void setEndpointType(final String value) {
    this.endpointType = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getEndpointType
  /**
   ** Returns the value of the endpointType property.
   **
   ** @return                    the value of the endpointType property.
   **                            Possible object is {@link String}.
   */
  public String getEndpointType() {
    return this.endpointType;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setRemoteManager
  /**
   ** Sets the value of the remoteManager property.
   **
   ** @param  value              the value of the remoteManager property.
   **                            Allowed object is {@link String}.
   */
  public void setRemoteManager(final String value) {
    this.remoteManager = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getRemoteManager
  /**
   ** Returns the value of the remoteManager property.
   **
   ** @return                    the value of the remoteManager property.
   **                            Possible object is {@link String}.
   */
  public String getRemoteManager() {
    return this.remoteManager;
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
  // Methode:   build
  /**
   ** Factory method to create an empty <code>EndpointAdapter</code>.
   **
   ** @return                    the model adapter bean.
   **                            <br>
   **                            Possible object is
   **                            <code>EndpointAdapter</code>.
   */
  public static EndpointAdapter build() {
    return new EndpointAdapter();
  }
}