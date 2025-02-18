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

    File        :   CatalogItemAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    CatalogItemAdapter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysauthz.schema;

import java.sql.Timestamp;

import oracle.jbo.Row;

import oracle.iam.platform.utils.vo.OIMType;

import oracle.iam.ui.platform.model.common.ModelAttr;
import oracle.iam.ui.platform.model.common.StaticModelAdapterBean;

////////////////////////////////////////////////////////////////////////////////
// class CatalogItemAdapter
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** Data Access Object used by <code>Catalog Item</code> customization.
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
public class CatalogItemAdapter extends StaticModelAdapterBean {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String PK                           = "catalogId";
  public static final String FK                           = "Bind_" + PK;
  public static final String ENTITY_ID                    = "entityId";
  public static final String ENTITY_FK                    = "Bind_" + ENTITY_ID;
  public static final String NAME                         = "entityName";
  public static final String TYPE                         = "entityType";
  public static final String DISPLAY_NAME                 = "entityDisplayName";
  public static final String DESCRIPTION                  = "entityDescription";
  public static final String CATEGORY                     = "category";
  public static final String RISK                         = "risk";
  public static final String RISK_UPDATE                  = "riskUpdate";
  public static final String DELETED                      = "deleted";
  public static final String AUDITABLE                    = "auditable";
  public static final String REQUESTABLE                  = "requstable";
  public static final String CERTIFIABLE                  = "certifiable";
  public static final String AUDIT_OBJECTIVE              = "auditObjective";
  public static final String APPROVER_USER                = "approverUser";
  public static final String APPROVER_USER_NAME           = "approverUserName";
  public static final String APPROVER_USER_DISPLAYNAME    = "approverUserDisplayName";
  public static final String APPROVER_ROLE                = "approverRole";
  public static final String APPROVER_ROLE_NAME           = "approverRoleName";
  public static final String APPROVER_ROLE_DISPLAYNAME    = "approverRoleDisplayName";
  public static final String CERTIFIER_USER               = "certifierUserName";
  public static final String CERTIFIER_USER_NAME          = "certifierUserName";
  public static final String CERTIFIER_USER_DISPLAYNAME   = "certifierUserDisplayName";
  public static final String CERTIFIER_ROLE               = "certifierRole";
  public static final String CERTIFIER_ROLE_NAME          = "certifierRoleName";
  public static final String CERTIFIER_ROLE_DISPLAYNAME   = "certifierRoleDisplayName";
  public static final String FULFILLMENT_USER             = "fulfillmentUser";
  public static final String FULFILLMENT_USER_NAME        = "fulfillmentUserName";
  public static final String FULFILLMENT_USER_DISPLAYNAME = "fulfillmentUserDisplayName";
  public static final String FULFILLMENT_ROLE             = "fulfillmentRole";
  public static final String FULFILLMENT_ROLE_NAME        = "fulfillmentRoleName";
  public static final String FULFILLMENT_ROLE_DISPLAYNAME = "fulfillmentRoleDisplayName";

  public static final String TYPE_ICON_ROL                = "/images/qual_rolemgmt_16.png";
  public static final String TYPE_ICON_APP                = "/images/qual_application_16.png";
  public static final String TYPE_ICON_ENT                = "/images/qual_entitlement_16.png";

  public static final String RISK_ICON_LOW                = "/images/oia-RiskLow.png";
  public static final String RISK_ICON_HIGH               = "/images/oia-RiskHigh.png";
  public static final String RISK_ICON_MEDIUM             = "/images/oia-RiskMedium.png";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-8971170380527459872")
  private static final long  serialVersionUID             = 1235615731222126625L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @ModelAttr
  private Long               catalogId;
  @ModelAttr
  private Long               entityId;
  @ModelAttr
  private String             entityType;
  @ModelAttr
  private String             entityIcon;
  @ModelAttr
  private String             entityName;
  @ModelAttr
  private String             entityDisplayName;
  @ModelAttr
  private String             entityDescription;
  @ModelAttr
  private Long               entityParentId;
  @ModelAttr
  private String             entityParentType;
  @ModelAttr
  private String             category;
  @ModelAttr
  private Integer            risk;
  @ModelAttr
  private String             riskIcon;
  @ModelAttr
  private Timestamp          riskUpdate;
  @ModelAttr
  private Boolean            deleted;
  @ModelAttr
  private Boolean            auditable;
  @ModelAttr
  private Boolean            requestable;
  @ModelAttr
  private Boolean            certifiable;
  @ModelAttr
  private String             auditObjective;
  @ModelAttr
  private String             approverUser;
  @ModelAttr
  private String             approverUserName;
  @ModelAttr
  private String             approverUserDisplayName;
  @ModelAttr
  private String             approverRole;
  @ModelAttr
  private String             approverRoleName;
  @ModelAttr
  private String             approverRoleDisplayName;
  @ModelAttr
  private String             certifierUser;
  @ModelAttr
  private String             certifierUserName;
  @ModelAttr
  private String             certifierUserDisplayName;
  @ModelAttr
  private String             certifierRole;
  @ModelAttr
  private String             certifierRoleName;
  @ModelAttr
  private String             certifierRoleDisplayName;
  @ModelAttr
  private String             fulfillmentUser;
  @ModelAttr
  private String             fulfillmentUserName;
  @ModelAttr
  private String             fulfillmentUserDisplayName;
  @ModelAttr
  private String             fulfillmentRole;
  @ModelAttr
  private String             fulfillmentRoleName;
  @ModelAttr
  private String             fulfillmentRoleDisplayName;
  @ModelAttr
  private String             tags;
  @ModelAttr
  private String             userTags;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>CatalogItemAdapter</code> value object that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public CatalogItemAdapter() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>CatalogItemAdapter</code> value object which use the
   ** specified {@link Row} to populate its value.
   **
   ** @param  row                the {@link Row} to populate the values from.
   */
  public CatalogItemAdapter(final Row row) {
    // ensure inheritance
    super(row);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>CatalogItemAdapter</code> value object which use the
   ** specified {@link Row} to populate its value.
   **
   ** @param  row                the {@link Row} to populate the values from.
   ** @param  deferChildren      <code>true</code> if the child rows are lazy
   **                            populated at access time; <code>false</code> if
   **                            its reuired to populate them immediatly.
   */
  public CatalogItemAdapter(final Row row, final boolean deferChildren) {
    // ensure inheritance
    super(row, deferChildren);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setCatalogId
  /**
   ** Sets the value of the catalogId property.
   **
   ** @param  value              the value of the catalogId property.
   **                            Allowed object is {@link Long}.
   */
  public void setCatalogId(final Long value) {
    this.catalogId = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getCatalogId
  /**
   ** Returns the value of the catalogId property.
   **
   ** @return                    the value of the catalogId property.
   **                            Possible object is {@link Long}.
   */
  public Long getCatalogId() {
    return this.catalogId;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setEntityId
  /**
   ** Sets the value of the entityId property.
   **
   ** @param  value              the value of the entityId property.
   **                            Allowed object is {@link Long}.
   */
  public void setEntityId (final Long value) {
    this.entityId  = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getEntityId
  /**
   ** Returns the value of the entityId property.
   **
   ** @return                    the value of the entityId property.
   **                            Possible object is {@link Long}.
   */
  public Long getEntityId () {
    return this.entityId ;
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
    if (this.entityType == null) {
      setEntityIcon(null);
    }
    else if (this.entityType.equals(OIMType.Role.toString())) {
      setEntityIcon(TYPE_ICON_ROL);
    }
    else if (this.entityType.equals(OIMType.Entitlement.toString())) {
      setEntityIcon(TYPE_ICON_ENT);
    }
    else if (this.entityType.equals(OIMType.ApplicationInstance.toString())) {
      setEntityIcon(TYPE_ICON_APP);
    }
    else {
      setEntityIcon(null);
    }
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
  // Methode:   setEntityIcon
  /**
   ** Sets the value of the entityIcon property.
   **
   ** @param  value              the value of the entityIcon property.
   **                            Allowed object is {@link String}.
   */
  public void setEntityIcon(final String value) {
    this.entityIcon = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getEntityIcon
  /**
   ** Returns the value of the entityIcon property.
   **
   ** @return                    the value of the entityIcon property.
   **                            Possible object is {@link String}.
   */
  public String getEntityIcon() {
    return this.entityIcon;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setEntityName
  /**
   ** Sets the value of the entityName property.
   **
   ** @param  value              the value of the entityName property.
   **                            Allowed object is {@link String}.
   */
  public void setEntityName (final String value) {
    this.entityName  = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getEntityName
  /**
   ** Returns the value of the entityName property.
   **
   ** @return                    the value of the entityName property.
   **                            Possible object is {@link String}.
   */
  public String getEntityName () {
    return this.entityName ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setEntityDisplayName
  /**
   ** Sets the value of the entityDisplayName property.
   **
   ** @param  value              the value of the entityDisplayName property.
   **                            Allowed object is {@link String}.
   */
  public void setEntityDisplayName(final String value) {
    this.entityDisplayName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getEntityDisplayName
  /**
   ** Returns the value of the entityDisplayName property.
   **
   ** @return                    the value of the entityDisplayName property.
   **                            Possible object is {@link String}.
   */
  public String getEntityDisplayName() {
    return this.entityDisplayName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setEntityDescription
  /**
   ** Sets the value of the entityDescription property.
   **
   ** @param  value              the value of the entityDescription property.
   **                            Allowed object is {@link String}.
   */
  public void setEntityDescription(final String value) {
    this.entityDescription = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getEntityDescription
  /**
   ** Returns the value of the entityDescription property.
   **
   ** @return                    the value of the entityDescription property.
   **                            Possible object is {@link String}.
   */
  public String getEntityDescription() {
    return this.entityDescription;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setEntityParentId
  /**
   ** Sets the value of the entityParentId property.
   **
   ** @param  value              the value of the entityParentId property.
   **                            Allowed object is {@link Long}.
   */
  public void setEntityParentId (final Long value) {
    this.entityParentId  = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getEntityParentId
  /**
   ** Returns the value of the entityParentId property.
   **
   ** @return                    the value of the entityParentId property.
   **                            Possible object is {@link Long}.
   */
  public Long getEntityParentId () {
    return this.entityParentId ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setEntityParentType
  /**
   ** Sets the value of the entityParentType property.
   **
   ** @param  value              the value of the entityParentType property.
   **                            Allowed object is {@link String}.
   */
  public void setEntityParentType(final String value) {
    this.entityParentType = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getEntityParentType
  /**
   ** Returns the value of the entityParentType property.
   **
   ** @return                    the value of the entityParentType property.
   **                            Possible object is {@link String}.
   */
  public String getEntityParentType() {
    return this.entityParentType;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setCategory
  /**
   ** Sets the value of the category property.
   **
   ** @param  value              the value of the category property.
   **                            Allowed object is {@link String}.
   */
  public void setCategory(final String value) {
    this.category  = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getCategory
  /**
   ** Returns the value of the category property.
   **
   ** @return                    the value of the category property.
   **                            Possible object is {@link String}.
   */
  public String getCategory() {
    return this.category ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setRisk
  /**
   ** Sets the value of the risk property.
   **
   ** @param  value              the value of the risk property.
   **                            Allowed object is {@link Integer}.
   */
  public void setRisk (final Integer value) {
    this.risk  = value;
    if (this.risk == null || this.risk < 5) {
      setRiskIcon(RISK_ICON_LOW);
    }
    else if (this.risk < 7) {
      setRiskIcon(RISK_ICON_MEDIUM);
    }
    else if (this.risk >= 7) {
      setRiskIcon(RISK_ICON_HIGH);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getRisk
  /**
   ** Returns the value of the risk property.
   **
   ** @return                    the value of the risk property.
   **                            Possible object is {@link Integer}.
   */
  public Integer getRisk () {
    return this.risk ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setRiskIcon
  /**
   ** Sets the value of the riskIcon property.
   **
   ** @param  value              the value of the riskIcon property.
   **                            Allowed object is {@link String}.
   */
  public void setRiskIcon(final String value) {
    this.riskIcon = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getRiskIcon
  /**
   ** Returns the value of the riskIcon property.
   **
   ** @return                    the value of the riskIcon property.
   **                            Possible object is {@link String}.
   */
  public String getRiskIcon() {
    return this.riskIcon;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setRiskUpdate
  /**
   ** Sets the value of the riskUpdate property.
   **
   ** @param  value              the value of the riskUpdate property.
   **                            Allowed object is {@link Timestamp}.
   */
  public void setRiskUpdate(final Timestamp value) {
    this.riskUpdate = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getRiskUpdate
  /**
   ** Returns the value of the riskUpdate property.
   **
   ** @return                    the value of the riskUpdate property.
   **                            Possible object is {@link Timestamp}.
   */
  public Timestamp getRiskUpdate() {
    return this.riskUpdate;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setDeleted
  /**
   ** Sets the value of the deleted property.
   **
   ** @param  value              the value of the deleted property.
   **                            Allowed object is {@link Boolean}.
   */
  public void setDeleted (final Boolean value) {
    this.deleted  = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getDeleted
  /**
   ** Returns the value of the deleted property.
   **
   ** @return                    the value of the deleted property.
   **                            Possible object is {@link Boolean}.
   */
  public Boolean getDeleted () {
    return this.deleted ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setAuditable
  /**
   ** Sets the value of the auditable property.
   **
   ** @param  value              the value of the auditable property.
   **                            Allowed object is {@link Boolean}.
   */
  public void setAuditable(final Boolean value) {
    this.auditable = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getAuditable
  /**
   ** Returns the value of the auditable property.
   **
   ** @return                    the value of the auditable property.
   **                            Possible object is {@link Boolean}.
   */
  public Boolean getAuditable() {
    return this.auditable;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setRequestable
  /**
   ** Sets the value of the requestable property.
   **
   ** @param  value              the value of the requestable property.
   **                            Allowed object is {@link Boolean}.
   */
  public void setRequestable (final Boolean value) {
    this.requestable  = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getRequestable
  /**
   ** Returns the value of the requestable property.
   **
   ** @return                    the value of the requestable property.
   **                            Possible object is {@link Boolean}.
   */
  public Boolean getRequestable () {
    return this.requestable ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setCertifiable
  /**
   ** Sets the value of the certifiable property.
   **
   ** @param  value              the value of the certifiable property.
   **                            Allowed object is {@link Boolean}.
   */
  public void setCertifiable(final Boolean value) {
    this.certifiable = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getCertifiable
  /**
   ** Returns the value of the certifiable property.
   **
   ** @return                    the value of the certifiable property.
   **                            Possible object is {@link Boolean}.
   */
  public Boolean getCertifiable() {
    return this.certifiable;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setAuditObjective
  /**
   ** Sets the value of the auditObjective property.
   **
   ** @param  value              the value of the auditObjective property.
   **                            Allowed object is {@link String}.
   */
  public void setAuditObjective(final String value) {
    this.auditObjective = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getAuditObjective
  /**
   ** Returns the value of the auditObjective property.
   **
   ** @return                    the value of the auditObjective property.
   **                            Possible object is {@link String}.
   */
  public String getAuditObjective() {
    return this.auditObjective;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setApproverUser
  /**
   ** Sets the value of the approverUser property.
   **
   ** @param  value              the value of the approverUser property.
   **                            Allowed object is {@link String}.
   */
  public void setApproverUser (final String value) {
    this.approverUser = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getApproverUser
  /**
   ** Returns the value of the approverUser property.
   **
   ** @return                    the value of the approverUser property.
   **                            Possible object is {@link String}.
   */
  public String getApproverUser () {
    return this.approverUser ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setApproverUserName
  /**
   ** Sets the value of the approverUserName property.
   **
   ** @param  value              the value of the approverUserName property.
   **                            Allowed object is {@link String}.
   */
  public void setApproverUserName (final String value) {
    this.approverUserName  = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getApproverUserName
  /**
   ** Returns the value of the approverUserName property.
   **
   ** @return                    the value of the approverUserName property.
   **                            Possible object is {@link String}.
   */
  public String getApproverUserName () {
    return this.approverUserName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setApproverUserDisplayName
  /**
   ** Sets the value of the approverUserDisplayName property.
   **
   ** @param  value              the value of the approverUserDisplayName property.
   **                            Allowed object is {@link String}.
   */
  public void setApproverUserDisplayName(final String value) {
    this.approverUserDisplayName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getApproverUserDisplayName
  /**
   ** Returns the value of the approverUserDisplayName property.
   **
   ** @return                    the value of the approverUserDisplayName property.
   **                            Possible object is {@link String}.
   */
  public String getApproverUserDisplayName() {
    return this.approverUserDisplayName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setApproverRole
  /**
   ** Sets the value of the approverRole property.
   **
   ** @param  value              the value of the approverRole property.
   **                            Allowed object is {@link String}.
   */
  public void setApproverRole (final String value) {
    this.approverRole = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getApproverRole
  /**
   ** Returns the value of the approverRole property.
   **
   ** @return                    the value of the approverRole property.
   **                            Possible object is {@link String}.
   */
  public String getApproverRole () {
    return this.approverRole;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setApproverRoleName
  /**
   ** Sets the value of the approverRoleName property.
   **
   ** @param  value              the value of the approverRoleName property.
   **                            Allowed object is {@link String}.
   */
  public void setApproverRoleName (final String value) {
    this.approverRoleName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getApproverRoleName
  /**
   ** Returns the value of the approverRoleName property.
   **
   ** @return                    the value of the approverRoleName property.
   **                            Possible object is {@link String}.
   */
  public String getApproverRoleName () {
    return this.approverRoleName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setApproverRoleDisplayName
  /**
   ** Sets the value of the approverRoleDisplayName property.
   **
   ** @param  value              the value of the approverRoleDisplayName property.
   **                            Allowed object is {@link String}.
   */
  public void setApproverRoleDisplayName(final String value) {
    this.approverRoleDisplayName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getApproverRoleDisplayName
  /**
   ** Returns the value of the approverRoleDisplayName property.
   **
   ** @return                    the value of the approverRoleDisplayName property.
   **                            Possible object is {@link String}.
   */
  public String getApproverRoleDisplayName() {
    return this.approverRoleDisplayName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setCertifierUser
  /**
   ** Sets the value of the certifierUser property.
   **
   ** @param  value              the value of the certifierUser property.
   **                            Allowed object is {@link String}.
   */
  public void setCertifierUser (final String value) {
    this.certifierUser = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getCertifierUser
  /**
   ** Returns the value of the certifierUser property.
   **
   ** @return                    the value of the certifierUser property.
   **                            Possible object is {@link String}.
   */
  public String getCertifierUser () {
    return this.certifierUser;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setCertifierUserName
  /**
   ** Sets the value of the certifierUserName property.
   **
   ** @param  value              the value of the certifierUserName property.
   **                            Allowed object is {@link String}.
   */
  public void setCertifierUserName (final String value) {
    this.certifierUserName  = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getCertifierUserName
  /**
   ** Returns the value of the certifierUserName property.
   **
   ** @return                    the value of the certifierUserName property.
   **                            Possible object is {@link String}.
   */
  public String getCertifierUserName () {
    return this.certifierUserName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setCertifierUserDisplayName
  /**
   ** Sets the value of the certifierUserDisplayName property.
   **
   ** @param  value              the value of the certifierUserDisplayName property.
   **                            Allowed object is {@link String}.
   */
  public void setCertifierUserDisplayName(final String value) {
    this.certifierUserDisplayName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getCertifierUserDisplayName
  /**
   ** Returns the value of the certifierUserDisplayName property.
   **
   ** @return                    the value of the certifierUserDisplayName property.
   **                            Possible object is {@link String}.
   */
  public String getCertifierUserDisplayName() {
    return this.certifierUserDisplayName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setCertifierRole
  /**
   ** Sets the value of the certifierRole property.
   **
   ** @param  value              the value of the certifierRole property.
   **                            Allowed object is {@link String}.
   */
  public void setCertifierRole (final String value) {
    this.certifierRole = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getCertifierRole
  /**
   ** Returns the value of the certifierRole property.
   **
   ** @return                    the value of the certifierRole property.
   **                            Possible object is {@link String}.
   */
  public String getCertifierRole () {
    return this.certifierRole;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setCertifierRoleName
  /**
   ** Sets the value of the certifierRoleName property.
   **
   ** @param  value              the value of the certifierRoleName property.
   **                            Allowed object is {@link String}.
   */
  public void setCertifierRoleName (final String value) {
    this.certifierRoleName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getCertifierRoleName
  /**
   ** Returns the value of the certifierRoleName property.
   **
   ** @return                    the value of the certifierRoleName property.
   **                            Possible object is {@link String}.
   */
  public String getCertifierRoleName () {
    return this.certifierRoleName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setCertifierRoleDisplayName
  /**
   ** Sets the value of the certifierRoleDisplayName property.
   **
   ** @param  value              the value of the certifierRoleDisplayName property.
   **                            Allowed object is {@link String}.
   */
  public void setCertifierRoleDisplayName(final String value) {
    this.certifierRoleDisplayName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getCertifierRoleDisplayName
  /**
   ** Returns the value of the certifierRoleDisplayName property.
   **
   ** @return                    the value of the certifierRoleDisplayName property.
   **                            Possible object is {@link String}.
   */
  public String getCertifierRoleDisplayName() {
    return this.certifierRoleDisplayName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setFulfillmentUser
  /**
   ** Sets the value of the fulfillmentUser property.
   **
   ** @param  value              the value of the fulfillmentUser property.
   **                            Allowed object is {@link String}.
   */
  public void setFulfillmentUser (final String value) {
    this.fulfillmentUser  = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getFulfillmentUser
  /**
   ** Returns the value of the fulfillmentUser property.
   **
   ** @return                    the value of the fulfillmentUser property.
   **                            Possible object is {@link String}.
   */
  public String getFulfillmentUser () {
    return this.fulfillmentUser ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setFulfillmentUserName
  /**
   ** Sets the value of the fulfillmentUserName property.
   **
   ** @param  value              the value of the fulfillmentUserName property.
   **                            Allowed object is {@link String}.
   */
  public void setFulfillmentUserName (final String value) {
    this.fulfillmentUserName  = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getFulfillmentUserName
  /**
   ** Returns the value of the fulfillmentUserName property.
   **
   ** @return                    the value of the fulfillmentUserName property.
   **                            Possible object is {@link String}.
   */
  public String getFulfillmentUserName () {
    return this.fulfillmentUserName ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setFulfillmentUserDisplayName
  /**
   ** Sets the value of the fulfillmentUserDisplayName property.
   **
   ** @param  value              the value of the fulfillmentUserDisplayName property.
   **                            Allowed object is {@link String}.
   */
  public void setFulfillmentUserDisplayName(final String value) {
    this.fulfillmentUserDisplayName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getFulfillmentUserDisplayName
  /**
   ** Returns the value of the fulfillmentUserDisplayName property.
   **
   ** @return                    the value of the fulfillmentUserDisplayName property.
   **                            Possible object is {@link String}.
   */
  public String getFulfillmentUserDisplayName() {
    return this.fulfillmentUserDisplayName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setFulfillmentRole
  /**
   ** Sets the value of the fulfillmentRole property.
   **
   ** @param  value              the value of the fulfillmentRole property.
   **                            Allowed object is {@link String}.
   */
  public void setFulfillmentRole (final String value) {
    this.fulfillmentRole  = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getFulfillmentRole
  /**
   ** Returns the value of the fulfillmentRole property.
   **
   ** @return                    the value of the fulfillmentRole property.
   **                            Possible object is {@link String}.
   */
  public String getFulfillmentRole () {
    return this.fulfillmentRole ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setFulfillmentRoleName
  /**
   ** Sets the value of the fulfillmentRoleName property.
   **
   ** @param  value              the value of the fulfillmentRoleName property.
   **                            Allowed object is {@link String}.
   */
  public void setFulfillmentRoleName (final String value) {
    this.fulfillmentRoleName  = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getFulfillmentRoleName
  /**
   ** Returns the value of the fulfillmentRoleName property.
   **
   ** @return                    the value of the fulfillmentRoleName property.
   **                            Possible object is {@link String}.
   */
  public String getFulfillmentRoleName () {
    return this.fulfillmentRoleName ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setFulfillmentRoleDisplayName
  /**
   ** Sets the value of the fulfillmentRoleDisplayName property.
   **
   ** @param  value              the value of the fulfillmentRoleDisplayName property.
   **                            Allowed object is {@link String}.
   */
  public void setFulfillmentRoleDisplayName(final String value) {
    this.fulfillmentRoleDisplayName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getFulfillmentRoleDisplayName
  /**
   ** Returns the value of the fulfillmentRoleDisplayName property.
   **
   ** @return                    the value of the fulfillmentRoleDisplayName property.
   **                            Possible object is {@link String}.
   */
  public String getFulfillmentRoleDisplayName() {
    return this.fulfillmentRoleDisplayName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setTags
  /**
   ** Sets the value of the tags property.
   **
   ** @param  value              the value of the tags property.
   **                            Allowed object is {@link String}.
   */
  public void setTags (final String value) {
    this.tags  = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getTags
  /**
   ** Returns the value of the tags property.
   **
   ** @return                    the value of the tags property.
   **                            Possible object is {@link String}.
   */
  public String getTags () {
    return this.tags ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setUserTags
  /**
   ** Sets the value of the userTags property.
   **
   ** @param  value              the value of the userTags property.
   **                            Allowed object is {@link String}.
   */
  public void setUserTags(final String value) {
    this.userTags = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getUserTags
  /**
   ** Returns the value of the userTags property.
   **
   ** @return                    the value of the userTags property.
   **                            Possible object is {@link String}.
   */
  public String getUserTags() {
    return this.userTags;
  }
}