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

    File        :   AccountAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AccountAdapter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysauthz.schema;

import java.util.Date;

import oracle.jbo.Row;

import oracle.iam.ui.platform.model.common.ModelAttr;
import oracle.iam.ui.platform.model.common.StaticModelAdapterBean;

////////////////////////////////////////////////////////////////////////////////
// class AccountAdapter
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** Data Access Object used by System Authorization customization.
 ** <br>
 ** Represents the user's identity within the context of a particular Resource
 ** (i.e., system or application). This is a Provisioned Resource Object (where
 ** the type of object is an Account) in OIM.
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
public class AccountAdapter extends StaticModelAdapterBean {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String KEY                        = "accountKey";
  public static final String TYPE                       = "accountType";
  public static final String MECHANISM                  = "accountMechanism";
  public static final String RISK_MECHANISM             = "accountRiskMechanism";
  public static final String RISK_ITEM                  = "accountRiskItem";
  public static final String RISK_SUMMARY               = "accountRiskSummary";
  public static final String RISK_UPDATE                = "accountRiskUpdate";
  public static final String LAST_CERTIFICATION_RISK    = "accountLastCerificationRisk";
  public static final String LAST_CERIFICATION_DECISION = "accountLastCerificationDecision";
  public static final String PROVISIONINGSTART          = "accountProvisioningStart";
  public static final String PROVISIONINGEND            = "accountProvisioningEnd";
  public static final String REQUEST_KEY                = "requestKey";
  public static final String POLICY_KEY                 = "policyKey";
  public static final String APPLICATION_INSTANCE_KEY   = "appInstanceKey";
  public static final String OBJECT_INSTANCE_KEY        = "objectInstanceKey";
  public static final String OBJECT_INSTANCE_STATUS     = "objectInstanceStatus";
  public static final String OBJECT_KEY                 = "resourceKey";
  public static final String OBJECT_NAME                = "resourceName";
  public static final String OBJECT_TYPE                = "resourceType";
  public static final String PROCESS_INSTANCE_KEY       = "processInstanceKey";
  public static final String PROCESS_INSTANCE_STATUS    = "processInstanceStatus";
  public static final String PROCESS_INSTANCE_NAME      = "processInstanceName";
  public static final String PROCESS_INSTANCE_ARCHIVED  = "processInstanceArchived";
  public static final String PROCESS_INSTANCE_CREATED   = "processInstanceCreateDate";
  public static final String PROCESS_INSTANCE_UPDATED   = "processInstanceUpdateDate";
  public static final String BENEFICIARY_KEY            = "beneficiaryKey";
  public static final String BENEFICIARY_LOGIN          = "beneficiaryLogin";
  public static final String BENEFICIARY_FIRST_NAME     = "beneficiaryFirstName";
  public static final String BENEFICIARY_LAST_NAME      = "beneficiaryLastName";
  public static final String ORGANIZATION_KEY           = "organizationKey";
  public static final String ORGANIZATION_NAME          = "organizationName";

  public static final String RISK_ICON_LOW              = "/images/oia-RiskLow.png";
  public static final String RISK_ICON_HIGH             = "/images/oia-RiskHigh.png";
  public static final String RISK_ICON_MEDIUM           = "/images/oia-RiskMedium.png";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:4795525492607218038")
  private static final long  serialVersionUID           = 2712128146384389669L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @ModelAttr
  private long               accountKey;
  @ModelAttr
  private String             accountType;
  @ModelAttr
  private String             accountMechanism;
  @ModelAttr
  private int                accountRiskMechanism;
  @ModelAttr
  private String             accountRiskMechanismIcon;
  @ModelAttr
  private int                accountRiskItem;
  @ModelAttr
  private String             accountRiskIcon;
  @ModelAttr
  private int                accountRiskSummary;
  @ModelAttr
  private Date               accountRiskUpdate;
  @ModelAttr
  private int                accountLastCerificationRisk;
  @ModelAttr
  private int                accountLastCerificationDecision;
  @ModelAttr
  private Date               accountProvisioningStart;
  @ModelAttr
  private Date               accountProvisioningEnd;
  @ModelAttr
  private long               requestKey;
  @ModelAttr
  private long               policyKey;
  @ModelAttr
  private long               appInstanceKey;
  @ModelAttr
  private long               objectInstanceKey;
  @ModelAttr
  private String             objectInstanceStatus;
  @ModelAttr
  private long               resourceKey;
  @ModelAttr
  private String             resourceName;
  @ModelAttr
  private String             resourceType;
  @ModelAttr
  private long               processInstanceKey;
  @ModelAttr
  private String             processInstanceStatus;
  @ModelAttr
  private String             processInstanceName;
  @ModelAttr
  private boolean            processInstanceArchived;
  @ModelAttr
  private Date               processInstanceCreateDate;
  @ModelAttr
  private Date               processInstanceUpdateDate;
  @ModelAttr
  private long               beneficiaryKey;
  @ModelAttr
  private String             beneficiaryLogin;
  @ModelAttr
  private String             beneficiaryFirstName;
  @ModelAttr
  private String             beneficiaryLastName;
  @ModelAttr
  private long               organizationKey;
  @ModelAttr
  private String             organizationName;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>AccountAdapter</code> value object that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AccountAdapter() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AccountAdapter</code> value object which use the
   ** specified {@link Row} to populate its value.
   **
   ** @param  row                the {@link Row} to populate the values from.
   */
  public AccountAdapter(final Row row) {
    // ensure inheritance
    super(row);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AccountAdapter</code> value object which use the
   ** specified {@link Row} to populate its value.
   **
   ** @param  row                the {@link Row} to populate the values from.
   ** @param  deferChildren      <code>true</code> if the child rows are lazy
   **                            populated at access time; <code>false</code> if
   **                            its reuired to populate them immediatly.
   */
  public AccountAdapter(final Row row, final boolean deferChildren) {
    // ensure inheritance
    super(row, deferChildren);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

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
  // Methode:   setAccountMechanism
  /**
   ** Sets the value of the accountMechanism property.
   **
   ** @param  value              the value of the accountMechanism property.
   **                            Allowed object is {@link String}.
   */
  public void setAccountMechanism(final String value) {
    this.accountMechanism = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getAccountMechanism
  /**
   ** Returns the value of the accountMechanism property.
   **
   ** @return                    the value of the accountMechanism property.
   **                            Possible object is {@link String}.
   */
  public String getAccountMechanism() {
    return this.accountMechanism;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setAccountRiskMechanism
  /**
   ** Sets the value of the accountRiskMechanism property.
   **
   ** @param  value              the value of the accountRiskMechanism property.
   **                            Allowed object is <code>int</code>.
   */
  public void setAccountRiskMechanism(final int value) {
    this.accountRiskMechanism = value;
    if (this.accountRiskMechanism < 5) {
      setAccountRiskMechanismIcon(RISK_ICON_LOW);
    }
    else if (this.accountRiskMechanism < 7) {
      setAccountRiskMechanismIcon(RISK_ICON_MEDIUM);
    }
    else if (this.accountRiskMechanism >= 7) {
      setAccountRiskMechanismIcon(RISK_ICON_HIGH);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getAccountRiskMechanism
  /**
   ** Returns the value of the accountRiskMechanism property.
   **
   ** @return                    the value of the accountRiskMechanism property.
   **                            Possible object is <code>int</code>.
   */
  public int getAccountRiskMechanism() {
    return this.accountRiskMechanism;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setAccountRiskMechanismIcon
  /**
   ** Sets the value of the accountRiskMechanismIcon property.
   **
   ** @param  value              the value of the accountRiskMechanismIcon
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setAccountRiskMechanismIcon(final String value) {
    this.accountRiskMechanismIcon = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getAccountRiskMechanismIcon
  /**
   ** Returns the value of the accountRiskMechanismIcon property.
   **
   ** @return                    the value of the accountRiskMechanismIcon
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getAccountRiskMechanismIcon() {
    return this.accountRiskMechanismIcon;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setAccountRiskItem
  /**
   ** Sets the value of the accountRiskItem property.
   **
   ** @param  value              the value of the accountRiskItem property.
   **                            Allowed object is <code>int</code>.
   */
  public void setAccountRiskItem(final int value) {
    this.accountRiskItem = value;
    if (this.accountRiskItem < 5) {
      setAccountRiskIcon(RISK_ICON_LOW);
    }
    else if (this.accountRiskItem < 7) {
      setAccountRiskIcon(RISK_ICON_MEDIUM);
    }
    else if (this.accountRiskItem >= 7) {
      setAccountRiskIcon(RISK_ICON_HIGH);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getAccountRiskItem
  /**
   ** Returns the value of the accountRiskItem property.
   **
   ** @return                    the value of the accountRiskItem property.
   **                            Possible object is <code>int</code>.
   */
  public int getAccountRiskItem() {
    return this.accountRiskItem;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setAccountRiskIcon
  /**
   ** Sets the value of the accountRiskIcon property.
   **
   ** @param  value              the value of the accountRiskIcon property.
   **                            Allowed object is {@link String}.
   */
  public void setAccountRiskIcon(final String value) {
    this.accountRiskIcon = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getAccountRiskIcon
  /**
   ** Returns the value of the accountRiskIcon property.
   **
   ** @return                    the value of the accountRiskIcon property.
   **                            Possible object is {@link String}.
   */
  public String getAccountRiskIcon() {
    return this.accountRiskIcon;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setAccountRiskSummary
  /**
   ** Sets the value of the accountRiskSummary property.
   **
   ** @param  value              the value of the accountRiskSummary property.
   **                            Allowed object is <code>int</code>.
   */
  public void setAccountRiskSummary(final int value) {
    this.accountRiskSummary = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getAccountRiskSummary
  /**
   ** Returns the value of the accountRiskSummary property.
   **
   ** @return                    the value of the accountRiskSummary property.
   **                            Possible object is <code>int</code>.
   */
  public int getAccountRiskSummary() {
    return this.accountRiskSummary;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setAccountRiskUpdate
  /**
   ** Sets the value of the accountRiskUpdate property.
   **
   ** @param  value              the value of the accountRiskUpdate property.
   **                            Allowed object is {@link Date}.
   */
  public void setAccountRiskUpdate(final Date value) {
    if ((value != null) && (value.getTime() == 0L))
      this.accountRiskUpdate = null;
    else
      this.accountRiskUpdate = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getAccountRiskUpdate
  /**
   ** Returns the value of the accountRiskUpdate property.
   **
   ** @return                    the value of the accountRiskUpdate property.
   **                            Possible object is {@link Date}.
   */
  public Date getAccountRiskUpdate() {
    return this.accountRiskUpdate;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setAccountLastCerificationRisk
  /**
   ** Sets the value of the accountLastCerificationRisk property.
   **
   ** @param  value              the value of the accountLastCerificationRisk
   **                            property.
   **                            Allowed object is <code>int</code>.
   */
  public void setAccountLastCerificationRisk(final int value) {
    this.accountLastCerificationRisk = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getAccountLastCerificationRisk
  /**
   ** Returns the value of the accountLastCerificationRisk property.
   **
   ** @return                    the value of the accountLastCerificationRisk
   **                            property.
   **                            Possible object is <code>int</code>.
   */
  public int getAccountLastCerificationRisk() {
    return this.accountLastCerificationRisk;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setAccountLastCerificationDecision
  /**
   ** Sets the value of the accountLastCerificationDecision property.
   **
   ** @param  value              the value of the
   **                            accountLastCerificationDecision property.
   **                            Allowed object is <code>int</code>.
   */
  public void setAccountLastCerificationDecision(final int value) {
    this.accountLastCerificationDecision = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getAccountLastCerificationDecision
  /**
   ** Returns the value of the accountLastCerificationDecision property.
   **
   ** @return                    the value of the
   **                            accountLastCerificationDecision property.
   **                            Possible object is <code>int</code>.
   */
  public int getAccountLastCerificationDecision() {
    return this.accountLastCerificationDecision;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setAccountProvisioningStart
  /**
   ** Sets the value of the accountProvisioningStart property.
   **
   ** @param  value              the value of the accountProvisioningStart
   **                            property.
   **                            Allowed object is {@link Date}.
   */
  public void setAccountProvisioningStart(final Date value) {
    if ((value != null) && (value.getTime() == 0L))
      this.accountProvisioningStart = null;
    else
      this.accountProvisioningStart = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getAccountProvisioningStart
  /**
   ** Returns the value of the accountProvisioningStart property.
   **
   ** @return                    the value of the accountProvisioningStart
   **                            property.
   **                            Possible object is {@link Date}.
   */
  public Date getAccountProvisioningStart() {
    return this.accountProvisioningStart;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setAccountProvisioningEnd
  /**
   ** Sets the value of the accountProvisioningEnd property.
   **
   ** @param  value              the value of the accountProvisioningEnd
   **                            property.
   **                            Allowed object is {@link Date}.
   */
  public void setAccountProvisioningEnd(final Date value) {
    if ((value != null) && (value.getTime() == 0L))
      this.accountProvisioningEnd = null;
    else
      this.accountProvisioningEnd = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getAccountProvisioningEnd
  /**
   ** Returns the value of the accountProvisioningEnd property.
   **
   ** @return                    the value of the accountProvisioningEnd
   **                            property.
   **                            Possible object is {@link Date}.
   */
  public Date getAccountProvisioningEnd() {
    return this.accountProvisioningEnd;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setRequestKey
  /**
   ** Sets the value of the requestKey property.
   **
   ** @param  value              the value of the requestKey property.
   **                            Allowed object is <code>long</code>.
   */
  public void setRequestKey(final long value) {
    this.requestKey = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getRequestKey
  /**
   ** Returns the value of the requestKey property.
   **
   ** @return                    the value of the requestKey property.
   **                            Possible object is <code>long</code>.
   */
  public long getRequestKey() {
    return this.requestKey;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setPolicyKey
  /**
   ** Sets the value of the policyKey property.
   **
   ** @param  value              the value of the policyKey property.
   **                            Allowed object is <code>long</code>.
   */
  public void setPolicyKey(final long value) {
    this.policyKey = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getPolicyKey
  /**
   ** Returns the value of the policyKey property.
   **
   ** @return                    the value of the policyKey property.
   **                            Possible object is <code>long</code>.
   */
  public long getPolicyKey() {
    return this.policyKey;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setAppInstanceKey
  /**
   ** Sets the value of the appInstanceKey property.
   **
   ** @param  value              the value of the appInstanceKey property.
   **                            Allowed object is <code>long</code>.
   */
  public void setAppInstanceKey(final long value) {
    this.appInstanceKey = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getAppInstanceKey
  /**
   ** Returns the value of the appInstanceKey property.
   **
   ** @return                    the value of the appInstanceKey property.
   **                            Possible object is <code>long</code>.
   */
  public long getAppInstanceKey() {
    return this.appInstanceKey;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setObjectInstanceKey
  /**
   ** Sets the value of the objectInstanceKey property.
   **
   ** @param  value              the value of the objectInstanceKey property.
   **                            Allowed object is <code>long</code>.
   */
  public void setObjectInstanceKey(final long value) {
    this.objectInstanceKey = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getObjectInstanceKey
  /**
   ** Returns the value of the objectInstanceKey property.
   **
   ** @return                    the value of the objectInstanceKey property.
   **                            Possible object is <code>long</code>.
   */
  public long getObjectInstanceKey() {
    return this.objectInstanceKey;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setObjectInstanceStatus
  /**
   ** Sets the value of the objectInstanceStatus property.
   **
   ** @param  value              the value of the objectInstanceStatus property.
   **                            Allowed object is {@link String}.
   */
  public void setObjectInstanceStatus(final String value) {
    this.objectInstanceStatus = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getObjectInstanceStatus
  /**
   ** Returns the value of the objectInstanceStatus property.
   **
   ** @return                    the value of the objectInstanceStatus property.
   **                            Possible object is {@link String}.
   */
  public String getObjectInstanceStatus() {
    return this.objectInstanceStatus;
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
   **                            Allowed object is {@link String}.
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
  // Methode:   setResourceType
  /**
   ** Sets the value of the resourceType property.
   **
   ** @param  value              the value of the resourceType property.
   **                            Allowed object is {@link String}.
   */
  public void setResourceType(final String value) {
    this.resourceType = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getResourceType
  /**
   ** Returns the value of the resourceType property.
   **
   ** @return                    the value of the resourceType property.
   **                            Possible object is {@link String}.
   */
  public String getResourceType() {
    return this.resourceType;
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
  // Methode:   setProcessInstanceStatus
  /**
   ** Sets the value of the processInstanceStatus property.
   **
   ** @param  value              the value of the processInstanceStatus
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setProcessInstanceStatus(final String value) {
    this.processInstanceStatus = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getProcessInstanceStatus
  /**
   ** Returns the value of the processInstanceStatus property.
   **
   ** @return                    the value of the processInstanceStatus
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getProcessInstanceStatus() {
    return this.processInstanceStatus;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setProcessInstanceName
  /**
   ** Sets the value of the processInstanceName property.
   **
   ** @param  value              the value of the processInstanceName property.
   **                            Allowed object is {@link String}.
   */
  public void setProcessInstanceName(final String value) {
    this.processInstanceName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getProcessInstanceName
  /**
   ** Returns the value of the processInstanceName property.
   **
   ** @return                    the value of the processInstanceName property.
   **                            Possible object is {@link String}.
   */
  public String getProcessInstanceName() {
    return this.processInstanceName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setProcessInstanceArchived
  /**
   ** Sets the value of the processInstanceArchived property.
   **
   ** @param  value              the value of the processInstanceArchived
   **                            property.
   **                            Allowed object is <code>boolean</code>.
   */
  public void setProcessInstanceArchived(final boolean value) {
    this.processInstanceArchived = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getProcessInstanceArchived
  /**
   ** Returns the value of the processInstanceArchived property.
   **
   ** @return                    the value of the processInstanceArchived
   **                            property.
   **                            Possible object is <code>boolean</code>.
   */
  public boolean getProcessInstanceArchived() {
    return this.processInstanceArchived;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setProcessInstanceCreateDate
  /**
   ** Sets the value of the processInstanceCreateDate property.
   **
   ** @param  value              the value of the processInstanceCreateDate
   **                            property.
   **                            Allowed object is {@link Date}.
   */
  public void setProcessInstanceCreateDate(final Date value) {
    if ((value != null) && (value.getTime() == 0L))
      this.processInstanceCreateDate = null;
    else
      this.processInstanceCreateDate = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getProcessInstanceCreateDate
  /**
   ** Returns the value of the processInstanceCreateDate property.
   **
   ** @return                    the value of the processInstanceCreateDate
   **                            property.
   **                            Possible object is {@link Date}.
   */
  public Date getProcessInstanceCreateDate() {
    return this.processInstanceCreateDate;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setProcessInstanceUpdateDate
  /**
   ** Sets the value of the processInstanceUpdateDate property.
   **
   ** @param  value              the value of the processInstanceUpdateDate
   **                            property.
   **                            Allowed object is {@link Date}.
   */
  public void setProcessInstanceUpdateDate(final Date value) {
    if ((value != null) && (value.getTime() == 0L))
      this.processInstanceUpdateDate = null;
    else
      this.processInstanceUpdateDate = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getProcessInstanceUpdateDate
  /**
   ** Returns the value of the processInstanceUpdateDate property.
   **
   ** @return                    the value of the processInstanceUpdateDate
   **                            property.
   **                            Possible object is {@link Date}.
   */
  public Date getProcessInstanceUpdateDate() {
    return this.processInstanceUpdateDate;
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
}