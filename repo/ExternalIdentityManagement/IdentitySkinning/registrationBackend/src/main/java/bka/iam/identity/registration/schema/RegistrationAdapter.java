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

    File        :   RegistrationAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    RegistrationAdapter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2021-13-09  DSteding    First release version
*/

package bka.iam.identity.registration.schema;

import java.util.Map;
import java.util.List;
import java.util.HashMap;

import oracle.jbo.Row;

import oracle.iam.identity.usermgmt.api.UserManagerConstants;
import oracle.iam.request.api.UnauthenticatedRequestService;

import oracle.iam.request.dataset.vo.RequestDataSet;
import oracle.iam.request.dataset.vo.AttributeReference;

import oracle.iam.ui.platform.exception.OIMRuntimeException;

import oracle.iam.ui.platform.model.common.MapModelAdapterBean;

import oracle.iam.ui.platform.model.common.OIMClientFactory;

////////////////////////////////////////////////////////////////////////////////
// class RegistrationAdapter
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
public class RegistrationAdapter extends MapModelAdapterBean {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:4579988208843884760")
  private static final long serialVersionUID = 2000733915855792714L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String           DATASET       = "Self-Register User";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String               requestId;

  /** Holds the values for all request data. */
  private Map<String, Object> requestData;

  /** Holds the SelfCreateUser Request Dataset. */
  private RequestDataSet       requestDataSet;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>RegistrationAdapter</code> value object that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public RegistrationAdapter() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>RegistrationAdapter</code> value object which use the
   ** specified {@link Row} to populate its value.
   **
   ** @param  row                the {@link Row} to populate the values from.
   */
  public RegistrationAdapter(final Row row) {
    // ensure inheritance
    super(row);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>RegistrationAdapter</code> value object which use the
   ** specified {@link Row} to populate its value.
   **
   ** @param  row                the {@link Row} to populate the values from.
   ** @param  deferChildren      <code>true</code> if the child rows are lazy
   **                            populated at access time; <code>false</code> if
   **                            its reuired to populate them immediatly.
   */
  public RegistrationAdapter(final Row row, final boolean deferChildren) {
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
  // Methode:   getRequestData
  /**
   ** Returns the value of the requestData property.
   **
   ** @return                    the value of the requestData property.
   **                            <br>
   **                            Possible object is {@link Map}.
   */
  public Map<String, Object> getRequestData() {
    if (this.requestData == null) {
      initializeRequestData();
    }
    return this.requestData;
  }

  public String getOIMAttributeRef(String attrName) {
    return getRequestDataSetAttrRefMap().get(attrName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   requestDataSet
  /**
   ** Retruns a convenient-to-use map of all AttributeReferences in the dataset
   ** with key=attributeReference.name and value=attributeReference.attrRef.
   **
   ** @return                    Map
   */
  public Map<String, String> getRequestDataSetAttrRefMap() {

    Map<String, String>      requestDataSetAttrRefMap = new HashMap<String, String>();
    List<AttributeReference> attributeReferences = requestDataSet() .getAttributeReference();
    for (AttributeReference attributeReference : attributeReferences) {
      requestDataSetAttrRefMap.put(attributeReference.getName(), attributeReference.getAttrRef());
    }
    return requestDataSetAttrRefMap;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   requestDataSet
  protected RequestDataSet requestDataSet() {
   try {
      if (this.requestDataSet == null) {
        UnauthenticatedRequestService requestService = OIMClientFactory.getUnauthenticatedRequestService();

        this.requestDataSet = requestService.getRequestDataSet(DATASET, UserManagerConstants.USER_ENTITY);
      }
      return this.requestDataSet;
    }
    catch (Exception e) {
      throw new OIMRuntimeException(e);
    }
  }
  //////////////////////////////////////////////////////////////////////////////
  // Methode:   initializeRequestData
  private void initializeRequestData() {
    this.requestData = new HashMap<String, Object>();
    // Initialize Map with all attributes in the Request Data Set
    final List<AttributeReference> references = requestDataSet().getAttributeReference();
    for (AttributeReference attributeReference : references) {
      this.requestData.put(attributeReference.getName(), null);
    }
  }
}