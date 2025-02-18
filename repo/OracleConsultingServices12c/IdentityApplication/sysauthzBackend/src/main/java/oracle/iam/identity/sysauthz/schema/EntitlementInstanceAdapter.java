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

    File        :   EntitlementInstanceAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    EntitlementInstanceAdapter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysauthz.schema;

import java.util.Date;

import oracle.jbo.Row;

import oracle.iam.provisioning.vo.RiskSummary;

import oracle.iam.ui.platform.model.common.ModelAttr;
import oracle.iam.ui.platform.model.common.StaticModelAdapterBean;

////////////////////////////////////////////////////////////////////////////////
// class EntitlementInstanceAdapter
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Data Access Object used by <code>Entitlement Instance</code> customization.
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
public class EntitlementInstanceAdapter extends StaticModelAdapterBean {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String KEY                     = "instanceKey";
  public static final String STATUS                  = "provisionStatus";
  public static final String MECHANISM               = "provisionMechanism";
  public static final String PROVISIONED             = "provisionedOn";
  public static final String VALIDFROM               = "validFrom";
  public static final String VALIDUNTIL              = "validUntil";
  public static final String ENTITLEMENT_KEY         = "entitlementKey";
  public static final String ENTITLEMENT_CODE        = "entitlementCode";
  public static final String ENTITLEMENT_VALUE       = "entitlementValue";
  public static final String ENTITLEMENT_DESCRIPTION = "entitlementDescription";
  public static final String BENEFICIARY_KEY         = "beneficiaryKey";
  public static final String BENEFICIARY_LOGIN       = "beneficiaryLogin";
  public static final String BENEFICIARY_FIRST_NAME  = "beneficiaryFirstName";
  public static final String BENEFICIARY_LAST_NAME   = "beneficiaryLastName";
  public static final String ORGANIZATION_KEY        = "organizationKey";
  public static final String ORGANIZATION_NAME       = "organizationName";
  public static final String ACCOUNT_KEY             = "accountKey";
  public static final String ACCOUNT_TYPE            = "accountType";
  public static final String ACCOUNT_NAME            = "accountName";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:830689420032963057")
  private static final long  serialVersionUID        = -3278119946659113610L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @ModelAttr
  private long              instanceKey;
  @ModelAttr
  private long              entitlementKey;
  @ModelAttr
  private String            entitlementCode;
  @ModelAttr
  private String            entitlementValue;
  @ModelAttr
  private String            entitlementDisplayName;
  @ModelAttr
  private long              processInstanceKey;
  @ModelAttr
  private long              resourceKey;
  @ModelAttr
  private String            resourceName;
  @ModelAttr
  private long              resourceInstance;
  @ModelAttr
  private long              beneficiaryKey;
  @ModelAttr
  private String            beneficiaryLogin;
  @ModelAttr
  private String            beneficiaryFirstName;
  @ModelAttr
  private String            beneficiaryLastName;
  @ModelAttr
  private long              organizationKey;
  @ModelAttr
  private String            organizationName;
  @ModelAttr
  private long              accountKey;
  @ModelAttr
  private String            accountType;
  @ModelAttr
  private String            accountName;
  @ModelAttr
  private String            provisionStatus;
  @ModelAttr
  private String            provisionMechanism;
  @ModelAttr
  private Date              provisionedOn;
  @ModelAttr
  private Date              validFrom;
  @ModelAttr
  private Date              validUntil;
  @ModelAttr
  private RiskSummary       riskSummary;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>EntitlementInstanceAdapter</code> value object
   ** that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public EntitlementInstanceAdapter() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>EntitlementInstanceAdapter</code> value object which
   ** use the specified {@link Row} to populate its value.
   **
   ** @param  row                the {@link Row} to populate the values from.
   */
  public EntitlementInstanceAdapter(final Row row) {
    // ensure inheritance
    super(row);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>EntitlementInstanceAdapter</code> value object which
   ** use the specified {@link Row} to populate its value.
   **
   ** @param  row                the {@link Row} to populate the values from.
   ** @param  deferChildren      <code>true</code> if the child rows are lazy
   **                            populated at access time; <code>false</code> if
   **                            its reuired to populate them immediatly.
   */
  public EntitlementInstanceAdapter(final Row row, final boolean deferChildren) {
    // ensure inheritance
    super(row, deferChildren);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setInstanceKey
  /**
   ** Sets the value of the instanceKey property.
   **
   ** @param  value              the value of the instanceKey property.
   **                            Allowed object is <code>long</code>.
   */
  public void setInstanceKey(final long value) {
    this.instanceKey = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getInstanceKey
  /**
   ** Returns the value of the entitlementKey property.
   **
   ** @return                    the value of the entitlementKey property.
   **                            Possible object is <code>long</code>.
   */
  public long getInstanceKey() {
    return this.instanceKey;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setEntitlementKey
  /**
   ** Sets the value of the entitlementKey property.
   **
   ** @param  value              the value of the entitlementKey property.
   **                            Allowed object is <code>long</code>.
   */
  public void setEntitlementKey(final long value) {
    this.entitlementKey = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getEntitlementKey
  /**
   ** Returns the value of the entitlementKey property.
   **
   ** @return                    the value of the entitlementKey property.
   **                            Possible object is <code>long</code>.
   */
  public long getEntitlementKey() {
    return this.entitlementKey;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setEntitlementCode
  /**
   ** Sets the value of the entitlementCode property.
   **
   ** @param  value              the value of the entitlementCode property.
   **                            Allowed object is {@link String}.
   */
  public void setEntitlementCode(final String value) {
    this.entitlementCode = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getEntitlementCode
  /**
   ** Returns the value of the entitlementCode property.
   **
   ** @return                    the value of the entitlementCode property.
   **                            Possible object is {@link String}.
   */
  public String getEntitlementCode() {
    return this.entitlementCode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setEntitlementValue
  /**
   ** Sets the value of the entitlementValue property.
   **
   ** @param  value              the value of the entitlementValue property.
   **                            Allowed object is {@link String}.
   */
  public void setEntitlementValue(final String value) {
    this.entitlementValue = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getEntitlementValue
  /**
   ** Returns the value of the entitlementValue property.
   **
   ** @return                    the value of the entitlementValue property.
   **                            Possible object is {@link String}.
   */
  public String getEntitlementValue() {
    return this.entitlementValue;
  }
  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setEntitlementDisplayName
  /**
   ** Sets the value of the entitlementDisplayName property.
   **
   ** @param  value              the value of the entitlementDisplayName property.
   **                            Allowed object is {@link String}.
   */
  public void setEntitlementDisplayName(final String value) {
    this.entitlementDisplayName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getEntitlementDisplayName
  /**
   ** Returns the value of the entitlementDisplayName property.
   **
   ** @return                    the value of the entitlementDisplayName property.
   **                            Possible object is {@link String}.
   */
  public String getEntitlementDisplayName() {
    return this.entitlementDisplayName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setProcessInstanceKey
  /**
   ** Sets the value of the processInstanceKey property.
   **
   ** @param  value              the value of the processInstanceKey property.
   **                            Allowed object is <code>long</code>.
   */
  public void setProcessInstanceKey(final long value) {
    this.processInstanceKey = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getProcessInstanceKey
  /**
   ** Returns the value of the processInstanceKey property.
   **
   ** @return                    the value of the processInstanceKey property.
   **                            Possible object is <code>long</code>.
   */
  public long getProcessInstanceKey() {
    return this.processInstanceKey;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setResourceKey
  /**
   ** Sets the value of the resourceKey property.
   **
   ** @param  value              the value of the resourceKey property.
   **                            Allowed object is <code>long</code>.
   */
  public void setResourceKey(final long value) {
    this.resourceKey = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getResourceKey
  /**
   ** Returns the value of the resourceKey property.
   **
   ** @return                    the value of the resourceKey property.
   **                            Possible object is <code>long</code>.
   */
  public long getResourceKey() {
    return this.resourceKey;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setResourceName
  /**
   ** Sets the value of the resourceName property.
   **
   ** @param  value              the value of the resourceName property.
   **                            Allowed object is {@link String}
   */
  public void setResourceName(final String value) {
    this.resourceName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getResourceName
  /**
   ** Returns the value of the resourceName property.
   **
   ** @return                    the value of the resourceName property.
   **                            Possible object is {@link String}.
   */
  public String getResourceName() {
    return this.resourceName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setResourceInstance
  /**
   ** Sets the value of the resourceInstance property.
   **
   ** @param  value              the value of the resourceInstance property.
   **                            Allowed object is <code>long</code>.
   */
  public void setResourceInstance(final long value) {
    this.resourceInstance = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getResourceInstance
  /**
   ** Returns the value of the resourceInstance property.
   **
   ** @return                    the value of the resourceInstance property.
   **                            Possible object is <code>long</code>.
   */
  public long getResourceInstance() {
    return this.resourceInstance;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setBeneficiaryKey
  /**
   ** Sets the value of the beneficiaryKey property.
   **
   ** @param  value              the value of the beneficiaryKey property.
   **                            Allowed object is <code>long</code>.
   */
  public void setBeneficiaryKey(final long value) {
    this.beneficiaryKey = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getBeneficiaryKey
  /**
   ** Returns the value of the beneficiaryKey property.
   **
   ** @return                    the value of the beneficiaryKey property.
   **                            Possible object is <code>long</code>.
   */
  public long getBeneficiaryKey() {
    return this.beneficiaryKey;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setBeneficiaryLogin
  /**
   ** Sets the value of the beneficiaryLogin property.
   **
   ** @param  value              the value of the beneficiaryLogin property.
   **                            Allowed object is {@link String}.
   */
  public void setBeneficiaryLogin(final String value) {
    this.beneficiaryLogin = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getBeneficiaryLogin
  /**
   ** Returns the value of the beneficiaryLogin property.
   **
   ** @return                    the value of the beneficiaryLogin property.
   **                            Possible object is {@link String}.
   */
  public String getBeneficiaryLogin() {
    return this.beneficiaryLogin;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setBeneficiaryFirstName
  /**
   ** Sets the value of the beneficiaryFirstName property.
   **
   ** @param  value              the value of the beneficiaryFirstName property.
   **                            Allowed object is {@link String}.
   */
  public void setBeneficiaryFirstName(final String value) {
    this.beneficiaryFirstName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getBeneficiaryFirstName
  /**
   ** Returns the value of the beneficiaryFirstName property.
   **
   ** @return                    the value of the beneficiaryFirstName property.
   **                            Possible object is {@link String}.
   */
  public String getBeneficiaryFirstName() {
    return this.beneficiaryFirstName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setBeneficiaryLastName
  /**
   ** Sets the value of the beneficiaryLastName property.
   **
   ** @param  value              the value of the beneficiaryLastName property.
   **                            Allowed object is {@link String}.
   */
  public void setBeneficiaryLastName(final String value) {
    this.beneficiaryLastName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getBeneficiaryLastName
  /**
   ** Returns the value of the beneficiaryLastName property.
   **
   ** @return                    the value of the beneficiaryLastName property.
   **                            Possible object is {@link String}.
   */
  public String getBeneficiaryLastName() {
    return this.beneficiaryLastName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setOrganizationKey
  /**
   ** Sets the value of the organizationKey property.
   **
   ** @param  value              the value of the organizationKey property.
   **                            Allowed object is <code>long</code>.
   */
  public void setOrganizationKey(final long value) {
    this.organizationKey = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getOrganizationKey
  /**
   ** Returns the value of the organizationKey property.
   **
   ** @return                    the value of the organizationKey property.
   **                            Possible object is <code>long</code>.
   */
  public long getOrganizationKey() {
    return this.organizationKey;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setOrganizationName
  /**
   ** Sets the value of the organizationName property.
   **
   ** @param  value              the value of the organizationName property.
   **                            Allowed object is {@link String}.
   */
  public void setOrganizationName(final String value) {
    this.organizationName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getOrganizationName
  /**
   ** Returns the value of the organizationName property.
   **
   ** @return                    the value of the organizationName property.
   **                            Possible object is {@link String}.
   */
  public String getOrganizationName() {
    return this.organizationName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setAccountKey
  /**
   ** Sets the value of the accountKey property.
   **
   ** @param  value              the value of the accountKey property.
   **                            Allowed object is <code>long</code>.
   */
  public void setAccountKey(final long value) {
    this.accountKey = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getAccountKey
  /**
   ** Returns the value of the accountKey property.
   **
   ** @return                    the value of the accountKey property.
   **                            Possible object is <code>long</code>.
   */
  public long getAccountKey() {
    return this.accountKey;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setAccountType
  /**
   ** Sets the value of the accountType property.
   **
   ** @param  value              the value of the accountType property.
   **                            Allowed object is {@link String}.
   */
  public void setAccountType(final String value) {
    this.accountType = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getAccountType
  /**
   ** Returns the value of the accountType property.
   **
   ** @return                    the value of the accountType property.
   **                            Possible object is {@link String}.
   */
  public String getAccountType() {
    return this.accountType;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setAccountName
  /**
   ** Sets the value of the accountName property.
   **
   ** @param  value              the value of the accountName property.
   **                            Allowed object is {@link String}.
   */
  public void setAccountName(final String value) {
    this.accountName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getAccountName
  /**
   ** Returns the value of the accountName property.
   **
   ** @return                    the value of the accountName property.
   **                            Possible object is {@link String}.
   */
  public String getAccountName() {
    return this.accountName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setProvisionStatus
  /**
   ** Sets the value of the provisionStatus property.
   **
   ** @param  value              the value of the provisionStatus property.
   **                            Allowed object is {@link String}.
   */
  public void setProvisionStatus(final String value) {
    this.provisionStatus = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getProvisionStatus
  /**
   ** Returns the value of the provisionStatus property.
   **
   ** @return                    the value of the provisionStatus property.
   **                            Possible object is {@link String}.
   */
  public String getProvisionStatus() {
    return this.provisionStatus;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setProvisionMechanism
  /**
   ** Sets the value of the provisionMechanism property.
   **
   ** @param  value              the value of the provisionMechanism property.
   **                            Allowed object is {@link String}.
   */
  public void setProvisionMechanism(final String value) {
    this.provisionMechanism = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getProvisionMechanism
  /**
   ** Returns the value of the provisionMechanism property.
   **
   ** @return                    the value of the provisionMechanism property.
   **                            Possible object is {@link String}.
   */
  public String getProvisionMechanism() {
    return this.provisionMechanism;
  }

    //////////////////////////////////////////////////////////////////////////////
  // Methode:   setProvisionedOn
  /**
   ** Sets the value of the provisionedOn property.
   **
   ** @param  value              the value of the provisionedOn property.
   **                            Allowed object is {@link Date}.
   */
  public void setProvisionedOn(final Date value) {
    if ((value != null) && (value.getTime() == 0L))
      this.provisionedOn = null;
    else
      this.provisionedOn = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getProvisionedOn
  /**
   ** Returns the value of the provisionedOn property.
   **
   ** @return                    the value of the provisionedOn property.
   **                            Possible object is {@link Date}.
   */
  public Date getProvisionedOn() {
    return this.provisionedOn;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setValidFrom
  /**
   ** Sets the value of the validFrom property.
   **
   ** @param  value              the value of the validFrom property.
   **                            Allowed object is {@link Date}.
   */
  public void setValidFrom(final Date value) {
    if ((value != null) && (value.getTime() == 0L))
      this.validFrom = null;
    else
      this.validFrom = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getValidFrom
  /**
   ** Returns the value of the validFrom property.
   **
   ** @return                    the value of the validFrom property.
   **                            Possible object is {@link Date}.
   */
  public Date getValidFrom() {
    return this.validFrom;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setValidUntil
  /**
   ** Sets the value of the validUntil property.
   **
   ** @param  value              the value of the validUntil property.
   **                            Allowed object is {@link Date}.
   */
  public void setValidUntil(final Date value) {
    if ((value != null) && (value.getTime() == 0L))
      this.validUntil = null;
    else
      this.validUntil = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getValidUntil
  /**
   ** Returns the value of the validUntil property.
   **
   ** @return                    the value of the validUntil property.
   **                            Possible object is {@link Date}.
   */
  public Date getValidUntil() {
    return this.validUntil;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setRiskSummary
  /**
   ** Sets the value of the riskSummary property.
   **
   ** @param  value              the value of the riskSummary property.
   **                            Allowed object is {@link RiskSummary}.
   */
  public void setRiskSummary(final RiskSummary value) {
    this.riskSummary = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getRiskSummary
  /**
   ** Returns the value of the riskSummary property.
   **
   ** @return                    the value of the riskSummary property.
   **                            Possible object is {@link RiskSummary}.
   */
  public RiskSummary getRiskSummary() {
    return this.riskSummary;
  }
}