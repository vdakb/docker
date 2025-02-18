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

    File        :   ReconciliationProfileAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ReconciliationProfileAdapter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysprov.schema;

import oracle.jbo.Row;

import oracle.iam.ui.platform.model.common.ModelAttr;
import oracle.iam.ui.platform.model.common.StaticModelAdapterBean;

////////////////////////////////////////////////////////////////////////////////
// class ReconciliationProfileAdapter
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Data Access Object used by <code>Reconciliation Profile</code>
 ** customization.
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
public class ReconciliationProfileAdapter extends StaticModelAdapterBean {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////


  public static final String OBJ_PK           = "objectKey";
  public static final String OBJ_FK           = "Bind_objectsName";
  public static final String NAME             = "objectName";
  public static final String ATTRIBUTE        = "attribute";
  public static final String TYPE             = "type";
  public static final String PRIMARY          = "primary";
  public static final String REQUIRED         = "required";
  public static final String ENCRYPTED        = "encrypted";
  public static final String KEYFIELD         = "keyField";

  public static final String ACTION           = "pendingAction";

  public static final String ADD              = "add";
  public static final String DEL              = "del";
  public static final String MOD              = "mod";
  public static final String NIL              = "nil";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-336025131280899677")
  private static final long  serialVersionUID = 6603397729150034158L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @ModelAttr
  private String             objectName;
  @ModelAttr
  private String             attribute;
  @ModelAttr
  private String             type;
  @ModelAttr
  private Boolean            primary;
  @ModelAttr
  private Boolean            required;
  @ModelAttr
  private Boolean            encrypted;
  @ModelAttr
  private Boolean            keyField;
  @ModelAttr
  private String             pendingAction;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>ReconciliationProfileAdapter</code> values
   ** object that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private ReconciliationProfileAdapter() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ReconciliationProfileAdapter</code> values object
   ** which use the specified {@link Row} to populate its value.
   **
   ** @param  row                the {@link Row} to populate the values from.
   **                            <br>
   **                            Allowed object is {@link Row}.
   */
  public ReconciliationProfileAdapter(final Row row) {
    // ensure inheritance
    super(row);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ReconciliationProfileAdapter</code> values object
   ** which use the specified {@link Row} to populate its value.
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
  public ReconciliationProfileAdapter(final Row row, final boolean deferChildren) {
    // ensure inheritance
    super(row, deferChildren);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setObjectName
  /**
   ** Sets the value of the objectName property.
   ** <p>
   ** Method is required as is to transfer data to display components like
   ** value picker.
   **
   ** @param  value              the value of the objectName property.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setObjectsName(final String value) {
    this.objectName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getObjectName
  /**
   ** Returns the value of the objectName property.
   ** <p>
   ** Method is required as is to transfer data to display components like
   ** value picker.
   **
   ** @return                    the value of the objectName property.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getObjectName() {
    return this.objectName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setAttribute
  /**
   ** Sets the value of the attribute property.
   ** <p>
   ** Method is required as is to transfer data to display components like
   ** value picker.
   **
   ** @param  value              the value of the attribute property.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setAttribute(final String value) {
    this.attribute = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getAttribute
  /**
   ** Returns the value of the attribute property.
   ** <p>
   ** Method is required as is to transfer data to display components like
   ** value picker.
   **
   ** @return                    the value of the attribute property.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getAttribute() {
    return this.attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setType
  /**
   ** Sets the value of the type property.
   ** <p>
   ** Method is required as is to transfer data to display components like
   ** value picker.
   **
   ** @param  value              the value of the type property.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setType(final String value) {
    this.type = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getType
  /**
   ** Returns the value of the type property.
   ** <p>
   ** Method is required as is to transfer data to display components like
   ** value picker.
   **
   ** @return                    the value of the type
   **                            property.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getType() {
    return this.type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setPrimary
  /**
   ** Sets the value of the primary property.
   ** <p>
   ** Method is primary as is to transfer data to display components like
   ** value picker.
   **
   ** @param  value              the value of the primary property.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   */
  public void setPrimary(final Boolean value) {
    this.primary = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   isPrimary
  /**
   ** Returns the value of the primary property.
   ** <p>
   ** Method is primary as is to transfer data to display components like
   ** value picker.
   **
   ** @return                    the value of the primary property.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public Boolean isPrimary() {
    return this.primary;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setRequired
  /**
   ** Sets the value of the required property.
   ** <p>
   ** Method is required as is to transfer data to display components like
   ** value picker.
   **
   ** @param  value              the value of the required property.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   */
  public void setRequired(final Boolean value) {
    this.required = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   isRequired
  /**
   ** Returns the value of the required property.
   ** <p>
   ** Method is required as is to transfer data to display components like
   ** value picker.
   **
   ** @return                    the value of the required property.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public Boolean isRequired() {
    return this.required;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setEncrypted
  /**
   ** Sets the value of the encrypted property.
   ** <p>
   ** Method is required as is to transfer data to display components like
   ** value picker.
   **
   ** @param  value              the value of the encrypted
   **                            property.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   */
  public void setEncrypted(final Boolean value) {
    this.encrypted = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   isEncrypted
  /**
   ** Returns the value of the encrypted property.
   ** <p>
   ** Method is required as is to transfer data to display components like
   ** value picker.
   **
   ** @return                    the value of the encrypted
   **                            property.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public Boolean isEncrypted() {
    return this.encrypted;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setKeyField
  /**
   ** Sets the value of the keyField property.
   ** <p>
   ** Method is required as is to transfer data to display components like
   ** value picker.
   **
   ** @param  value              the value of the keyField
   **                            property.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   */
  public void setKeyField(final Boolean value) {
    this.keyField = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   isKeyField
  /**
   ** Returns the value of the keyField property.
   ** <p>
   ** Method is required as is to transfer data to display components like
   ** value picker.
   **
   ** @return                    the value of the keyField
   **                            property.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public Boolean isKeyField() {
    return this.keyField;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setPendingAction
  /**
   ** Sets the value of the pendingAction property.
   **
   ** @param  value              the value of the pendingAction property.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setPendingAction(final String value) {
    this.pendingAction = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getPendingAction
  /**
   ** Returns the value of the pendingAction property.
   **
   ** @return                    the value of the pendingAction property.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getPendingAction() {
    return this.pendingAction;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   build
  /**
   ** Factory method to create an empty
   ** <code>ReconciliationProfileAdapter</code> with the given profile name.
   **
   ** @param  objectName         the name of the <code>Resource Object</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the model adapter bean.
   **                            <br>
   **                            Possible object is
   **                            <code>ReconciliationProfileAdapter</code>.
   */
  public static ReconciliationProfileAdapter build(final String objectName) {
    final ReconciliationProfileAdapter mab = new ReconciliationProfileAdapter();
    mab.objectName = objectName;
    return mab;
  }
}