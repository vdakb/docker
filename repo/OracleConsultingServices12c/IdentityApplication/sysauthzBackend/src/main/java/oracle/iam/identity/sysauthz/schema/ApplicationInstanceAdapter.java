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

    File        :   ApplicationInstanceAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ApplicationInstanceAdapter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysauthz.schema;

import oracle.jbo.Row;

import oracle.iam.provisioning.vo.ApplicationInstance;

import oracle.iam.platform.authopss.api.PolicyConstants;

import oracle.iam.ui.platform.model.common.ModelAttr;
import oracle.iam.ui.platform.model.common.StaticModelAdapterBean;

////////////////////////////////////////////////////////////////////////////////
// class ApplicationInstanceAdapter
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Data Access Object used by <code>Application Instance</code> customization.
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
public class ApplicationInstanceAdapter extends StaticModelAdapterBean {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String PK               = "appInstanceKey";
  public static final String FK               = "Bind_" + PK;
  public static final String TYPE             = "entityType";
  public static final String NAME             = "name";
  public static final String DISPLAY_NAME     = "displayName";
  public static final String DESCRIPTION      = "description";
  public static final String DATASET          = "dataSet";
  public static final String OBJECTSNAME      = "objectsName";
  public static final String ENDPOINTNAME     = "endpointName";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-7312873482786569987")
  private static final long  serialVersionUID = 3280396659127522289L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private ApplicationInstance instance;

  @ModelAttr
  private long                appInstanceKey;
  @ModelAttr
  private String              entityType      = PolicyConstants.Resources.APPLICATION_INSTANCE.getId();
  @ModelAttr
  private long                objectsKey;
  @ModelAttr
  private String              objectsName;
  @ModelAttr
  private String              endpointName;
  @ModelAttr
  private long                endpointKey;
  @ModelAttr
  private String              name;
  @ModelAttr
  private String              displayName;
  @ModelAttr
  private String              description;
  @ModelAttr
  private String              dataSet;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>ApplicationInstanceAdapter</code> value object
   ** that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ApplicationInstanceAdapter() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ApplicationInstanceAdapter</code> value object which
   ** use the specified {@link Row} to populate its value.
   **
   ** @param  row                the {@link Row} to populate the values from.
   */
  public ApplicationInstanceAdapter(final Row row) {
    // ensure inheritance
    super(row);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ApplicationInstanceAdapter</code> value object which
   ** use the specified {@link Row} to populate its value.
   **
   ** @param  row                the {@link Row} to populate the values from.
   ** @param  deferChildren      <code>true</code> if the child rows are lazy
   **                            populated at access time; <code>false</code> if
   **                            its reuired to populate them immediatly.
   */
  public ApplicationInstanceAdapter(final Row row, final boolean deferChildren) {
    // ensure inheritance
    super(row, deferChildren);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setInstance
  /**
   ** Sets the value of the instance property.
   **
   ** @param  value              the value of the instance property.
   **                            Allowed object is {@link ApplicationInstance}.
   */
  public void setInstance(final ApplicationInstance value) {
    this.instance = value;
    transfer();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getInstance
  /**
   ** Returns the value of the instance property.
   **
   ** @return                    the value of the instance property.
   **                            Allowed object is {@link ApplicationInstance}.
   */
  public ApplicationInstance getInstance() {
    ApplicationInstance ret;
    if (this.appInstanceKey == 0) {
      ret = new ApplicationInstance(this.name, this.displayName, this.description, this.objectsKey, this.endpointKey, null, null);
    }
    else {
      if (this.instance != null) {
        ret = populate(this.instance);
      }
      else {
        ret = new ApplicationInstance(this.appInstanceKey);
        ret = populate(ret);
      }
    }
    return ret;
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
  // Methode:   getObjectsKey
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
  // Methode:   setObjectsName
  /**
   ** Sets the value of the objectsName property.
   **
   ** @param  value              the value of the objectsName property.
   **                            Allowed object is {@link String}.
   */
  public void setObjectsName(final String value) {
    this.objectsName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getObjectsName
  /**
   ** Returns the value of the objectsName property.
   **
   ** @return                    the value of the objectsName property.
   **                            Possible object is {@link String}.
   */
  public String getObjectsName() {
    return this.objectsName;
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
  // Methode:   setName
  /**
   ** Sets the value of the name property.
   **
   ** @param  value              the value of the name property.
   **                            Allowed object is {@link String}.
   */
  public void setName(final String value) {
    this.name = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getName
  /**
   ** Returns the value of the name property.
   **
   ** @return                    the value of the name property.
   **                            Possible object is {@link String}.
   */
  public String getName() {
    return this.name;
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
  // Methode:   setDataSet
  /**
   ** Sets the value of the dataSet property.
   **
   ** @param  value              the value of the dataSet property.
   **                            Allowed object is {@link String}.
   */
  public void setDataSet(final String value) {
    this.dataSet = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getDataSet
  /**
   ** Returns the value of the dataSet property.
   **
   ** @return                    the value of the dataSet property.
   **                            Possible object is {@link String}.
   */
  public String getDataSet() {
    return this.dataSet;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transfer
  private void transfer() {
    // set all attributes required.
    // TODO type, uifragment, parent name?
    setAppInstanceKey(this.instance.getApplicationInstanceKey());
    setObjectsKey(this.instance.getObjectKey());
    setObjectsName(this.instance.getObjectName());
    setEndpointKey(this.instance.getItResourceKey());
    setEndpointName(this.instance.getItResourceName());
    setName(this.instance.getApplicationInstanceName());
    setDisplayName(this.instance.getDisplayName());
    setDescription(this.instance.getDescription());
    setDataSet(this.instance.getDataSetName());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populate
  private ApplicationInstance populate(final ApplicationInstance value) {
    value.setApplicationInstanceName(this.name);
    value.setDisplayName(this.displayName);
    value.setDescription(this.description);
    value.setObjectKey(this.objectsKey);
    value.setObjectName(this.objectsName);
    value.setItResourceKey(this.endpointKey);
    value.setItResourceName(this.endpointName);
    value.setDataSetName(this.dataSet);
    return value;
  }
}