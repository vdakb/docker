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

    File        :   ReconciliationEventAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ReconciliationEventAdapter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysprov.schema;

import java.util.Date;

import java.util.EnumSet;

import oracle.jbo.Row;

import oracle.iam.ui.platform.model.common.ModelAttr;
import oracle.iam.ui.platform.model.common.StaticModelAdapterBean;

////////////////////////////////////////////////////////////////////////////////
// class ReconciliationEventAdapter
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Data Access Object used by Reconciliation Event customization.
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
public class ReconciliationEventAdapter extends StaticModelAdapterBean {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String                             PK               = "eventKey";
  public static final String                             JOB_KEY          = "jobKey";
  public static final String                             BATCH_KEY        = "batchKey";
  public static final String                             OBJ_KEY          = "objectKey";
  public static final String                             OBJ_NAME         = "objectName";
  public static final String                             BIND_OBJ_KEY     = "Bind_objectsKey";
  public static final String                             USR_KEY          = "userKey";
  public static final String                             ORG_KEY          = "organizationKey";
  public static final String                             ACT_KEY          = "accountKey";
  public static final String                             ENTITY_TYPE      = "entityType";
  public static final String                             CHANGE_TYPE      = "changeType";
  public static final String                             STATUS           = "status";
  public static final String                             LINK_SOURCE      = "linkSource";
  public static final String                             NOTE             = "not";
  public static final String                             REASON           = "reason";
  public static final String                             ACTION_DATE      = "actionDate";
  public static final String                             KEY_FIELD        = "keyField";


  public static final EnumSet<ReconciliationEventStatus> CLOSE            = EnumSet.of(ReconciliationEventStatus.INITIAL, ReconciliationEventStatus.DATA, ReconciliationEventStatus.DATA_NOTVALID, ReconciliationEventStatus.DATA_VALID);
  public static final EnumSet<ReconciliationEventStatus> SUCCESS          = EnumSet.of(ReconciliationEventStatus.CREATE_SUCCESS, ReconciliationEventStatus.UPDATE_SUCCESS, ReconciliationEventStatus.DELETE_SUCCESS);
  public static final EnumSet<ReconciliationEventStatus> FAILURE          = EnumSet.of(ReconciliationEventStatus.CREATE_FAILURE, ReconciliationEventStatus.UPDATE_FAILURE, ReconciliationEventStatus.DELETE_FAILURE);

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:388482878567842100")
  private static final long                              serialVersionUID = -1097326595600570597L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @ModelAttr
  private Long               eventKey;
  @ModelAttr
  private Long               jobKey;
  @ModelAttr
  private Long               batchKey;
  @ModelAttr
  private Long               objectKey;
  @ModelAttr
  private String             objectName;
  @ModelAttr
  private Long               userKey;
  @ModelAttr
  private Long               organizationKey;
  @ModelAttr
  private Long               accountKey;
  @ModelAttr
  private String             entityType;
  @ModelAttr
  private String             changeType;
  @ModelAttr
  private String             status;
  @ModelAttr
  private String             linkSource;
  @ModelAttr
  private String             note;
  @ModelAttr
  private String             reason;
  @ModelAttr
  private Date               actionDate;
  @ModelAttr
  private String             keyField;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>ReconciliationEventAdapter</code> values object
   ** that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private ReconciliationEventAdapter() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ReconciliationEventAdapter</code> values object which
   ** use the specified {@link Row} to populate its value.
   **
   ** @param  row                the {@link Row} to populate the values from.
   **                            <br>
   **                            Allowed object is {@link Row}.
   */
  public ReconciliationEventAdapter(final Row row) {
    // ensure inheritance
    super(row);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ReconciliationEventAdapter</code> values object which
   ** use the specified {@link Row} to populate its value.
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
  public ReconciliationEventAdapter(final Row row, final boolean deferChildren) {
    // ensure inheritance
    super(row, deferChildren);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setEventKey
  /**
   ** Sets the value of the eventKey property.
   **
   ** @param  value              the value of the eventKey property.
   **                            <br>
   **                            Allowed object is {@link Long}.
   */
  public void setEventKey(final Long value) {
    this.eventKey = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getEventKey
  /**
   ** Returns the value of the eventKey property.
   **
   ** @return                    the value of the eventKey property.
   **                            <br>
   **                            Possible object is {@link Long}.
   */
  public Long getEventKey() {
    return this.eventKey;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setJobKey
  /**
   ** Sets the value of the jobKey property.
   **
   ** @param  value              the value of the jobKey property.
   **                            <br>
   **                            Allowed object is {@link Long}.
   */
  public void setJobKey(final Long value) {
    this.jobKey = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getJobKey
  /**
   ** Returns the value of the jobKey property.
   **
   ** @return                    the value of the jobKey property.
   **                            <br>
   **                            Possible object is {@link Long}.
   */
  public Long getJobKey() {
    return this.jobKey;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setBatchKey
  /**
   ** Sets the value of the batchKey property.
   **
   ** @param  value              the value of the batchKey property.
   **                            <br>
   **                            Allowed object is {@link Long}.
   */
  public void setBatchKey(final Long value) {
    this.batchKey = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getBatchKey
  /**
   ** Returns the value of the batchKey property.
   **
   ** @return                    the value of the batchKey property.
   **                            <br>
   **                            Possible object is {@link Long}.
   */
  public Long getBatchKey() {
    return this.batchKey;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setObjectKey
  /**
   ** Sets the value of the objectKey property.
   **
   ** @param  value              the value of the objectKey property.
   **                            <br>
   **                            Allowed object is {@link Long}.
   */
  public void setObjectKey(final Long value) {
    this.objectKey = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getObjectKey
  /**
   ** Returns the value of the objectKey property.
   **
   ** @return                    the value of the objectKey property.
   **                            <br>
   **                            Possible object is {@link Long}.
   */
  public Long getObjectKey() {
    return this.objectKey;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setObjectName
  /**
   ** Sets the value of the objectName property.
   **
   ** @param  value              the value of the objectName property.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setObjectName(final String value) {
    this.objectName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getObjectName
  /**
   ** Returns the value of the objectName property.
   **
   ** @return                    the value of the objectName property.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getObjectName() {
    return this.objectName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setUserKey
  /**
   ** Sets the value of the userKey property.
   **
   ** @param  value              the value of the userKey property.
   **                            <br>
   **                            Allowed user is {@link Long}.
   */
  public void setUserKey(final Long value) {
    this.userKey = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getUserKey
  /**
   ** Returns the value of the userKey property.
   **
   ** @return                    the value of the userKey property.
   **                            <br>
   **                            Possible user is {@link Long}.
   */
  public Long getUserKey() {
    return this.userKey;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setOrganizationKey
  /**
   ** Sets the value of the organizationKey property.
   **
   ** @param  value              the value of the organizationKey property.
   **                            <br>
   **                            Allowed organization is {@link Long}.
   */
  public void setOrganizationKey(final Long value) {
    this.organizationKey = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getOrganizationKey
  /**
   ** Returns the value of the organizationKey property.
   **
   ** @return                    the value of the organizationKey property.
   **                            <br>
   **                            Possible organization is {@link Long}.
   */
  public Long getOrganizationKey() {
    return this.organizationKey;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setAccountKey
  /**
   ** Sets the value of the accountKey property.
   **
   ** @param  value              the value of the accountKey property.
   **                            <br>
   **                            Allowed account is {@link Long}.
   */
  public void setAccountKey(final Long value) {
    this.accountKey = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getAccountKey
  /**
   ** Returns the value of the accountKey property.
   **
   ** @return                    the value of the accountKey property.
   **                            <br>
   **                            Possible account is {@link Long}.
   */
  public Long getAccountKey() {
    return this.accountKey;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setEntityType
  /**
   ** Sets the value of the entityType property.
   **
   ** @param  value              the value of the entityType property.
   **                            <br>
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
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getEntityType() {
    return this.entityType;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setChangeType
  /**
   ** Sets the value of the changeType property.
   **
   ** @param  value              the value of the changeType property.
   **                            <br>
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
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getChangeType() {
    return this.changeType;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setStatus
  /**
   ** Sets the value of the status property.
   **
   ** @param  value              the value of the status property.
   **                            <br>
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
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getStatus() {
    return this.status;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setLinkSource
  /**
   ** Sets the value of the linkSource property.
   **
   ** @param  value              the value of the linkSource property.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setLinkSource(final String value) {
    this.linkSource = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getLinkSource
  /**
   ** Returns the value of the linkSource property.
   **
   ** @return                    the value of the linkSource property.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getLinkSource() {
    return this.linkSource;
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
   **                            Possible object is {@link String}.
   */
  public String getNote() {
    return this.note;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setReason
  /**
   ** Sets the value of the reason property.
   **
   ** @param  value              the value of the reason property.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setReason(final String value) {
    this.reason = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getReason
  /**
   ** Returns the value of the reason property.
   **
   ** @return                    the value of the reason property.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getReason() {
    return this.reason;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setActionDate
  /**
   ** Sets the value of the actionDate property.
   **
   ** @param  value              the value of the actionDate property.
   **                            <br>
   **                            Allowed object is {@link Date}.
   */
  public void setActionDate(final Date value) {
    if ((value != null) && (value.getTime() == 0L))
      this.actionDate = null;
    else
      this.actionDate = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getActionDate
  /**
   ** Returns the value of the actionDate property.
   **
   ** @return                    the value of the actionDate property.
   **                            <br>
   **                            Possible object is {@link Date}.
   */
  public Date getActionDate() {
    return this.actionDate;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setKeyField
  /**
   ** Sets the value of the keyField property.
   **
   ** @param  value              the value of the keyField property.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setKeyField(final String value) {
    this.keyField = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getKeyField
  /**
   ** Returns the value of the keyField property.
   **
   ** @return                    the value of the keyField property.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getKeyField() {
    return this.keyField;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   build
  /**
   ** Factory method to create an empty
   ** <code>ReconciliationEventAdapter</code> with the given profile name.
   **
   ** @return                    the model adapter bean.
   **                            <br>
   **                            Possible object is
   **                            <code>ReconciliationEventAdapter</code>.
   */
  public static ReconciliationEventAdapter build() {
    return new ReconciliationEventAdapter();
  }
}