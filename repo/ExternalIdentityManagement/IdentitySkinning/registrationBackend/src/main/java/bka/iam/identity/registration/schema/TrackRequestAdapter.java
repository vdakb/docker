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

    Copyright © 2021 All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Frontend Extension
    Subsystem   :   Self Service Registration

    File        :   TrackRequestAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    TrackRequestAdapter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2021-13-09  DSteding    First release version
*/

package bka.iam.identity.registration.schema;

import java.util.Date;

import oracle.iam.ui.platform.model.common.ModelAttr;
import oracle.iam.ui.platform.model.common.StaticModelAdapterBean;

import oracle.jbo.Row;

////////////////////////////////////////////////////////////////////////////////
// class TrackRequestAdapter
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** Data Access Object used by Self Service Registration customization.
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
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TrackRequestAdapter extends StaticModelAdapterBean {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @ModelAttr
  private String requestId;
  @ModelAttr
  private String requestStatus;
  @ModelAttr
  private String requestStage;
  @ModelAttr
  private Date   submitted;
  @ModelAttr
  private Date   modified;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>TrackRequestAdapter</code> value object that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TrackRequestAdapter() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TrackRequestAdapter</code> value object which use the
   ** specified {@link Row} to populate its value.
   **
   ** @param  row                the {@link Row} to populate the values from.
   */
  public TrackRequestAdapter(final Row row) {
    // ensure inheritance
    super(row);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TrackRequestAdapter</code> value object which use the
   ** specified {@link Row} to populate its value.
   **
   ** @param  row                the {@link Row} to populate the values from.
   ** @param  deferChildren      <code>true</code> if the child rows are lazy
   **                            populated at access time; <code>false</code> if
   **                            its reuired to populate them immediatly.
   */
  public TrackRequestAdapter(final Row row, final boolean deferChildren) {
    // ensure inheritance
    super(row, deferChildren);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setRequestId
  /**
   ** Sets the value of the requestId property.
   **
   ** @param  value              the value of the requestId property.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setRequestId(final String value) {
    this.requestId = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getRequestId
  /**
   ** Returns the value of the requestId property.
   **
   ** @return                    the value of the requestId property.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getRequestId() {
    return this.requestId;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setRequestStatus
  /**
   ** Sets the value of the requestStatus property.
   **
   ** @param  value              the value of the requestStatus property.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setRequestStatus(final String value) {
    this.requestStatus = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getRequestStatus
  /**
   ** Returns the value of the requestStatus property.
   **
   ** @return                    the value of the requestStatus property.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getRequestStatus() {
    return this.requestStatus;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setRequestStage
  /**
   ** Sets the value of the requestStage property.
   **
   ** @param  value              the value of the requestStage property.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setRequestStage(final String value) {
    this.requestStatus = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getRequestStage
  /**
   ** Returns the value of the requestStage property.
   **
   ** @return                    the value of the requestStage property.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getRequestStage() {
    return this.requestStage;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setSubmitted
  /**
   ** Sets the value of the submitted property.
   **
   ** @param  value              the value of the submitted property.
   **                            <br>
   **                            Allowed object is {@link Date}.
   */
  public void setSubmitted(final Date value) {
    this.submitted = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getSubmitted
  /**
   ** Returns the value of the submitted property.
   **
   ** @return                    the value of the submitted property.
   **                            <br>
   **                            Possible object is {@link Date}.
   */
  public Date getSubmitted() {
    return this.submitted;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setModified
  /**
   ** Sets the value of the modified property.
   **
   ** @param  value              the value of the modified property.
   **                            <br>
   **                            Allowed object is {@link Date}.
   */
  public void setModified(final Date value) {
    this.modified = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getModified
  /**
   ** Returns the value of the modified property.
   **
   ** @return                    the value of the modified property.
   **                            <br>
   **                            Possible object is {@link Date}.
   */
  public Date getModified() {
    return this.modified;
  }
}