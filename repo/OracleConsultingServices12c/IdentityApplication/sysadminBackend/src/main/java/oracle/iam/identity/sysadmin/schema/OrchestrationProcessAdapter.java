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

    File        :   OrchestrationProcessAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    OrchestrationProcessAdapter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysadmin.schema;

import java.sql.Timestamp;

import oracle.jbo.Row;

import oracle.iam.ui.platform.model.common.ModelAttr;
import oracle.iam.ui.platform.model.common.StaticModelAdapterBean;

////////////////////////////////////////////////////////////////////////////////
// class OrchestrationProcessAdapter
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Data Access Object used by System Administration customization.
 ** <br>
 ** Represents the orchestration within the context of a particular
 ** process. This is an Orchestration Process (where the type of object is an
 ** AbstractGenericOrchestration) in OIM.
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
public class OrchestrationProcessAdapter extends StaticModelAdapterBean {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String PK               = "processId";
  public static final String NAME             = "processName";
  public static final String STAGE            = "stage";
  public static final String OUTOFBANDEVENTS  = "outOfBandEvents";
  public static final String STAGEFAILED      = "stageFailed";
  public static final String OPERATION        = "operation";
  public static final String STATUS           = "status";
  public static final String CHANGETYPE       = "changeType";
  public static final String RUNNING          = "running";
  public static final String PARENTID         = "parentId";
  public static final String BULKPARENTID     = "bulkParentId";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:6289515168559205188")
  private static final long  serialVersionUID = 4946105452348157171L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @ModelAttr
  private Long               processId;
  @ModelAttr
  private String             processName;
  @ModelAttr
  private Long               bulkParentId;
  @ModelAttr
  private String             status;
  @ModelAttr
  private Long               parentId;
  @ModelAttr
  private Long               dependendId;
  @ModelAttr
  private String             entityId;
  @ModelAttr
  private String             entityType;
  @ModelAttr
  private Boolean            outOfBandEvents;
  @ModelAttr
  private String             operation;
  @ModelAttr
  private String             stage;
  @ModelAttr
  private String             changeType;
  @ModelAttr
  private Long               retry;
  @ModelAttr
  private Timestamp          createdOn;
  @ModelAttr
  private Timestamp          modifiedOn;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>OrchestrationProcessAdapter</code> values object
   ** that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public OrchestrationProcessAdapter() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OrchestrationProcessAdapter</code> values object which
   ** use the specified {@link Row} to populate its value.
   **
   ** @param  row                the {@link Row} to populate the values from.
   */
  public OrchestrationProcessAdapter(final Row row) {
    // ensure inheritance
    super(row);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OrchestrationProcessAdapter</code> values object which
   ** use the specified {@link Row} to populate its value.
   **
   ** @param  row                the {@link Row} to populate the values from.
   ** @param  deferChildren      <code>true</code> if the child rows are lazy
   **                            populated at access time; <code>false</code> if
   **                            its reuired to populate them immediatly.
   */
  public OrchestrationProcessAdapter(final Row row, final boolean deferChildren) {
    // ensure inheritance
    super(row, deferChildren);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setProcessId
  /**
   ** Sets the value of the processId property.
   **
   ** @param  value              the value of the processId property.
   **                            Allowed object is {@link Long}.
   */
  public void setProcessId(final Long value) {
    this.processId = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getProcessId
  /**
   ** Returns the value of the processId property.
   **
   ** @return                    the value of the processId property.
   **                            Possible object is {@link Long}.
   */
  public Long getProcessId() {
    return this.processId;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setProcessName
  /**
   ** Sets the value of the processName property.
   **
   ** @param  value              the value of the processName property.
   **                            Allowed object is {@link String}.
   */
  public void setProcessName(final String value) {
    this.processName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getProcessName
  /**
   ** Returns the value of the processName property.
   **
   ** @return                    the value of the processName property.
   **                            Possible object is {@link String}.
   */
  public String getProcessName() {
    return this.processName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setBulkParentId
  /**
   ** Sets the value of the bulkParentId property.
   **
   ** @param  value              the value of the bulkParentId property.
   **                            Allowed object is {@link Long}.
   */
  public void setBulkParentId(final Long value) {
    this.bulkParentId = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getBulkParentId
  /**
   ** Returns the value of the bulkParentId property.
   **
   ** @return                    the value of the bulkParentId property.
   **                            Possible object is {@link Long}.
   */
  public Long getBulkParentId() {
    return this.bulkParentId;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setStatus
  /**
   ** Sets the value of the status property.
   **
   ** @param  value              the value of the status property.
   **                            Allowed object is {@link String}.
   */
  public void setStatus(final String value) {
    this.status = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getStatus
  /**
   ** Returns the value of the status property.
   **
   ** @return                    the value of the status property.
   **                            Possible object is {@link String}.
   */
  public String getStatus() {
    return this.status;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setParentId
  /**
   ** Sets the value of the parentId property.
   **
   ** @param  value              the value of the parentId property.
   **                            Allowed object is {@link Long}.
   */
  public void setParentId(final Long value) {
    this.parentId = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getParentId
  /**
   ** Returns the value of the parentId property.
   **
   ** @return                    the value of the parentId property.
   **                            Possible object is {@link Long}.
   */
  public Long getParentId() {
    return this.parentId;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setDependendId
  /**
   ** Sets the value of the dependendId property.
   **
   ** @param  value              the value of the dependendId property.
   **                            Allowed object is {@link Long}.
   */
  public void setDependendId(final Long value) {
    this.dependendId = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getDependendId
  /**
   ** Returns the value of the dependendId property.
   **
   ** @return                    the value of the dependendId property.
   **                            Possible object is {@link Long}.
   */
  public Long getDependendId() {
    return this.dependendId;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setEntityId
  /**
   ** Sets the value of the entityId property.
   **
   ** @param  value              the value of the entityId property.
   **                            Allowed object is {@link String}.
   */
  public void setEntityId(final String value) {
    this.entityId = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getEntityId
  /**
   ** Returns the value of the entityId property.
   **
   ** @return                    the value of the entityId property.
   **                            Possible object is {@link String}.
   */
  public String getEntityId() {
    return this.entityId;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setEntityType
  /**
   ** Sets the value of the entityType property.
   **
   ** @param  value              the value of the entityType property.
   **                            Allowed object is {@link String}.
   */
  public void setEntityType(final String value) {
    this.entityType = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getEntityType
  /**
   ** Returns the value of the entityType property.
   **
   ** @return                    the value of the entityType property.
   **                            Possible object is {@link String}.
   */
  public String getEntityType() {
    return this.entityType;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setOutOfBandEvents
  /**
   ** Sets the value of the outOfBandEvents property.
   **
   ** @param  value              the value of the outOfBandEvents property.
   **                            Allowed object is {@link Boolean}.
   */
  public void setOutOfBandEvents(final Boolean value) {
    this.outOfBandEvents = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getOutOfBandEvents
  /**
   ** Returns the value of the outOfBandEvents property.
   **
   ** @return                    the value of the outOfBandEvents property.
   **                            Possible object is {@link Boolean}.
   */
  public Boolean getOutOfBandEvents() {
    return this.outOfBandEvents;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setOperation
  /**
   ** Sets the value of the operation property.
   **
   ** @param  value              the value of the operation property.
   **                            Allowed object is {@link String}.
   */
  public void setOperation(final String value) {
    this.operation = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getOperation
  /**
   ** Returns the value of the operation property.
   **
   ** @return                    the value of the operation property.
   **                            Possible object is {@link String}.
   */
  public String getOperation() {
    return this.operation;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setStage
  /**
   ** Sets the value of the stage property.
   **
   ** @param  value              the value of the stage property.
   **                            Allowed object is {@link String}.
   */
  public void setStage(final String value) {
    this.stage = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getStage
  /**
   ** Returns the value of the stage property.
   **
   ** @return                    the value of the stage property.
   **                            Possible object is {@link String}.
   */
  public String getStage() {
    return this.stage;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setChangeType
  /**
   ** Sets the value of the changeType property.
   **
   ** @param  value              the value of the changeType
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setChangeType(final String value) {
    this.changeType = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getChangeType
  /**
   ** Returns the value of the changeType property.
   **
   ** @return                    the value of the changeType property.
   **                            Possible object is {@link String}.
   */
  public String getChangeType() {
    return this.changeType;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setRetry
  /**
   ** Sets the value of the retry property.
   **
   ** @param  value              the value of the retry property.
   **                            Allowed object is {@link Long}.
   */
  public void setRetry(final Long value) {
    this.retry = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getRetry
  /**
   ** Returns the value of the retry property.
   **
   ** @return                    the value of the retry property.
   **                            Possible object is {@link Long}.
   */
  public Long getRetry() {
    return this.retry;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setCreatedOn
  /**
   ** Sets the value of the createdOn property.
   **
   ** @param  value              the value of the createdOn property.
   **                            Allowed object is {@link Timestamp}.
   */
  public void setCreatedOn(final Timestamp value) {
    this.createdOn = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getCreatedOn
  /**
   ** Returns the value of the createdOn property.
   **
   ** @return                    the value of the createdOn property.
   **                            Possible object is {@link Timestamp}.
   */
  public Timestamp getCreatedOn() {
    return this.createdOn;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setModifiedOn
  /**
   ** Sets the value of the modifiedOn property.
   **
   ** @param  value              the value of the modifiedOn property.
   **                            Allowed object is {@link Timestamp}.
   */
  public void setModifiedOn(final Timestamp value) {
    this.modifiedOn = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getModifiedOn
  /**
   ** Returns the value of the modifiedOn property.
   **
   ** @return                    the value of the modifiedOn property.
   **                            Possible object is {@link Timestamp}.
   */
  public Timestamp getModifiedOn() {
    return this.modifiedOn;
  }
}