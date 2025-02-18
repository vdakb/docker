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

    File        :   EntitlementAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    EntitlementAdapter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysauthz.schema;

import oracle.jbo.Row;

import oracle.iam.platform.authopss.api.PolicyConstants;

import oracle.iam.provisioning.vo.Entitlement;

import oracle.iam.ui.platform.model.common.ModelAttr;
import oracle.iam.ui.platform.model.common.StaticModelAdapterBean;

////////////////////////////////////////////////////////////////////////////////
// class EntitlementAdapter
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** Data Access Object used by <code>Entitlement</code> customization.
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
public class EntitlementAdapter extends StaticModelAdapterBean {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String PK               = "entitlementKey";
  public static final String CODE             = "entitlementCode";
  public static final String VALUE            = "entitlementValue";
  public static final String DISPLAY_NAME     = "displayName";
  public static final String DESCRIPTION      = "description";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:2462761170411507159")
  private static final long  serialVersionUID = 4046459103340858684L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @ModelAttr
  private long              entitlementKey;
  @ModelAttr
  private String            entityType       = PolicyConstants.Resources.IT_RESOURCE_ENTITLEMENT.getId();
  @ModelAttr
  private long              applicationKey;
  @ModelAttr
  private long              objectsKey;
  @ModelAttr
  private long              endpointKey;
  @ModelAttr
  private long              formKey;
  @ModelAttr
  private long              lookupValueKey;
  @ModelAttr
  private String            entitlementCode;
  @ModelAttr
  private String            entitlementValue;
  @ModelAttr
  private String            displayName;
  @ModelAttr
  private String            description;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>EntitlementAdapter</code> value object that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public EntitlementAdapter() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>EntitlementAdapter</code> values object
   ** which use the specified {@link Row} to populate its value.
   **
   ** @param  row                the {@link Row} to populate the values from.
   */
  public EntitlementAdapter(final Row row) {
    // ensure inheritance
    super(row);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>EntitlementAdapter</code> values object
   ** which use the specified {@link Row} to populate its value.
   **
   ** @param  row                the {@link Row} to populate the values from.
   ** @param  deferChildren      <code>true</code> if the child rows are lazy
   **                            populated at access time; <code>false</code> if
   **                            its reuired to populate them immediatly.
   */
  public EntitlementAdapter(final Row row, final boolean deferChildren) {
    // ensure inheritance
    super(row, deferChildren);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

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
  // Methode:   setApplicationKey
  /**
   ** Sets the value of the applicationKey property.
   **
   ** @param  value              the value of the applicationKey property.
   **                            Allowed application is <code>long</code>.
   */
  public void setApplicationKey(final long value) {
    this.applicationKey = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getApplicationKey
  /**
   ** Returns the value of the applicationKey property.
   **
   ** @return                    the value of the applicationKey property.
   **                            Possible application is <code>long</code>.
   */
  public long getApplicationKey() {
    return this.applicationKey;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setObjectsKey
  /**
   ** Sets the value of the objectsKey property.
   **
   ** @param  value              the value of the objectsKey property.
   **                            Allowed object is <code>long</code>.
   */
  public void setObjectsKey(final long value) {
    this.objectsKey = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getObjectKey
  /**
   ** Returns the value of the objectsKey property.
   **
   ** @return                    the value of the objectsKey property.
   **                            Possible object is <code>long</code>.
   */
  public long getObjectsKey() {
    return this.objectsKey;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setEndpointKey
  /**
   ** Sets the value of the endpointKey property.
   **
   ** @param  value              the value of the endpointKey property.
   **                            Allowed object is <code>long</code>.
   */
  public void setEndpointKey(final long value) {
    this.endpointKey = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getEndpointKey
  /**
   ** Returns the value of the endpointKey property.
   **
   ** @return                    the value of the endpointKey property.
   **                            Possible object is <code>long</code>.
   */
  public long getEndpointKey() {
    return this.endpointKey;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setFormKey
  /**
   ** Sets the value of the formKey property.
   **
   ** @param  value              the value of the formKey property.
   **                            Allowed form is <code>long</code>.
   */
  public void setFormKey(final long value) {
    this.formKey = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getFormKey
  /**
   ** Returns the value of the formKey property.
   **
   ** @return                    the value of the formKey property.
   **                            Possible form is <code>long</code>.
   */
  public long getFormKey() {
    return this.formKey;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setLookupValueKey
  /**
   ** Sets the value of the lookupValueKey property.
   **
   ** @param  value              the value of the lookupValueKey property.
   **                            Allowed lookupValue is <code>long</code>.
   */
  public void setLookupValueKey(final long value) {
    this.lookupValueKey = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getLookupValueKey
  /**
   ** Returns the value of the lookupValueKey property.
   **
   ** @return                    the value of the lookupValueKey property.
   **                            Possible lookupValue is <code>long</code>.
   */
  public long getLookupValueKey() {
    return this.lookupValueKey;
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
  // Methode:   setDisplayName
  /**
   ** Sets the value of the displayName property.
   **
   ** @param  value              the value of the displayName property.
   **                            Allowed object is {@link String}.
   */
  public void setDisplayName(final String value) {
    this.displayName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getDisplayName
  /**
   ** Returns the value of the displayName property.
   **
   ** @return                    the value of the displayName property.
   **                            Possible object is {@link String}.
   */
  public String getDisplayName() {
    return this.displayName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setDescription
  /**
   ** Sets the value of the description property.
   **
   ** @param  value              the value of the description property.
   **                            Allowed object is {@link String}.
   */
  public void setDescription(final String value) {
    this.description = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getDescription
  /**
   ** Returns the value of the description property.
   **
   ** @return                    the value of the description property.
   **                            Possible object is {@link String}.
   */
  public String getDescription() {
    return this.description;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   transfer
  /**
   ** Sets the value of the instance property.
   **
   ** @param  value              the provider to obtain the attribute values
   **                            from.
   **                            Allowed object is {@link Entitlement}.
   */
  public void transfer(final Entitlement value) {
    //set all attributes required.
    //TODO type, uifragment, parent name?
    setEntitlementKey(value.getEntitlementKey());
    setEntitlementCode(value.getEntitlementCode());
    setEntitlementValue(value.getEntitlementValue());
    setDisplayName(value.getDisplayName());
    setDescription(value.getDescription());
    setApplicationKey(value.getAppInstance().getApplicationInstanceKey());
    setObjectsKey(value.getObjectKey());
    setEndpointKey(value.getItResourceKey());
    setFormKey(value.getFormKey());
    setLookupValueKey(value.getLookupValueKey());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transfer
  public Entitlement transfer() {
    final Entitlement ret = new Entitlement();
    ret.setEntitlementKey(this.entitlementKey);
    ret.setEntitlementCode(this.entitlementCode);
    ret.setDisplayName(this.displayName);
    ret.setDescription(this.description);
    ret.setObjectKey(this.objectsKey);
    ret.setItResourceKey(this.endpointKey);
    ret.setFormKey(this.formKey);
    ret.setLookupValueKey(this.lookupValueKey);
    return ret;
  }
}