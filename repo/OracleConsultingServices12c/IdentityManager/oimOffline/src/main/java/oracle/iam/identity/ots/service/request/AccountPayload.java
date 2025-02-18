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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Offline Target Connector

    File        :   AccountPayload.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    AccountPayload.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.0.0.0      2012-28-10  DSteding    First release version
*/

package oracle.iam.identity.ots.service.request;

////////////////////////////////////////////////////////////////////////////////
// class AccountPayload
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The wraper class to marshal/unmarshal SOA composite payloads send/recieved
 ** from the SOA server.
 **
 ** @version 3.0.0.0
 ** @since   3.0.0.0
 */
class AccountPayload {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String requestKey;
  private String requesterLogin;
  private String beneficiaryLogin;
  private String beneficiaryFirstName;
  private String beneficiaryLastName;
  private String instanceName;
  private String objectName;
  private String resourceName;
  private String entityKey;
  private String entityType;
  private String operation;
  private String processInstance;
  private String processTask;
  private String descriptiveField;

  private String callbackURL;
  private String process;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AccountPayload</code> wrapper.
   */
  protected AccountPayload()  {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AccountPayload</code> wrapper with the specified
   ** properties.
   **
   ** @param  requestKey
   ** @param  requesterLogin
   ** @param  beneficiaryLogin
   ** @param  beneficiaryFirstName
   ** @param  beneficiaryLastName
   ** @param  instanceName         the name of the
   **                              <code>Application Instance</code> the request
   **                              to create belongs to.
   ** @param  objectName           the name of the <code>Resource Object</code>
   **                              the request to create belongs to.
   ** @param  resourceName         the name of the <code>IT Resource</code> the
   **                              request to create belongs to.
   ** @param  entityKey
   ** @param  entityType
   ** @param  operation
   ** @param  processInstance
   ** @param  processTask
   ** @param  descriptiveField
   ** @param  callbackURL
   ** @param  process
   */
  protected AccountPayload(final String requestKey, final String requesterLogin, final String beneficiaryLogin, final String beneficiaryFirstName, final String beneficiaryLastName, final String instanceName, final String objectName, final String resourceName, final String entityKey, final String entityType, final String operation, final String processInstance, final String processTask, final String descriptiveField, final String callbackURL, final String process)  {
    // ensure inheritance
    super();

    // initialize instance
    this.requestKey           = requestKey;
    this.requesterLogin       = requesterLogin;
    this.beneficiaryLogin     = beneficiaryLogin;
    this.beneficiaryFirstName = beneficiaryFirstName;
    this.beneficiaryLastName  = beneficiaryLastName;
    this.instanceName         = instanceName;
    this.objectName           = objectName;
    this.resourceName         = resourceName;
    this.entityKey            = entityKey;
    this.entityType           = entityType;
    this.operation            = operation;
    this.processInstance      = processInstance;
    this.processTask          = processTask;
    this.descriptiveField     = descriptiveField;
    this.callbackURL          = callbackURL;
    this.process              = process;
  }


  //////////////////////////////////////////////////////////////////////////////
  // Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requestKey
  /**
   **
   ** @param  value
   */
  public void requestKey(final String value) {
    this.requestKey = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requesterLogin
  /**
   **
   ** @param  value
   */
  public void requesterLogin(final String value) {
    this.requesterLogin = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beneficiaryLogin
  /**
   **
   ** @param  value
   */
  public void beneficiaryLogin(final String value) {
    this.beneficiaryLogin = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beneficiaryFirstName
  /**
   **
   ** @param  value
   */
  public void beneficiaryFirstName(final String value) {
    this.beneficiaryFirstName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beneficiaryLastName
  /**
   **
   ** @param  value
   */
  public void beneficiaryLastName(final String value) {
    this.beneficiaryLastName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instanceName
  /**
   ** Sets the name of the <code>Application Instance</code> the request to
   ** create belongs to.
   **
   ** @param  value              the name of the
   **                            <code>Application Instance</code> the request
   **                            to create belongs to.
   */
  public void instanceName(final String value) {
    this.instanceName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectName
  /**
   ** Sets the name of the <code>Resource Object</code> the request to create
   ** belongs to.
   **
   ** @param  value              the name of the <code>Resource Object</code>
   **                            the request to create belongs to.
   */
  public void objectName(final String value) {
    this.objectName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceName
  /**
   ** Sets the name of the <code>IT Resource</code> the request to create
   ** belongs to.
   **
   ** @param  value              the name of the <code>IT Resource</code> the
   **                            request to create belongs to.
   */
  public void resourceName(final String value) {
    this.resourceName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entityKey
  /**
   ** Set the entityKey property value.
   **
   ** @param  value              the entityKey property value.
   */
  public void entityKey(final String value) {
    this.entityKey = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entityType
  /**
   ** Set the entityType property value.
   **
   ** @param  value              the entityType property value.
   */
  public void entityType(final String value) {
    this.entityType = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processInstance
  /**
   ** Set the processInstance property value.
   **
   ** @param  value              the processInstance property value.
   */
  public void processInstance(final String value) {
    this.processInstance = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processTask
  /**
   ** Set the processTask property value.
   **
   ** @param  value              the processTask property value.
   */
  public void processTask(final String value) {
    this.processTask = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   descriptiveField
  /**
   ** Set the descriptiveField property value.
   **
   ** @param  value              the descriptiveField property value.
   */
  public void descriptiveField(final String value) {
    this.descriptiveField = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   callbackURL
  /**
   ** Set the callbackURL property value.
   **
   ** @param  value              the callbackURL property value.
   */
  public void callbackURL(final String value) {
    this.callbackURL = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   operation
  /**
   ** Set the operation property value.
   **
   ** @param  value              the operation property value.
   */
  public void operation(final String value) {
    this.operation = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods groupe by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshal
  /**
   ** Marshal the content tree rooted to a {@link StringBuilder} and return the
   ** resulting string.
   **
   ** @return                    the content tree rooted as a string.
   */
  public String marshal() {
    final StringBuilder buffer = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
    buffer.append("\n<ns1:process xmlns:ns1=\"" + this.process + "\">");
    buffer.append("\n<ns1:AccountID>" + this.processInstance + "</ns1:AccountID>");
    buffer.append("\n<ns1:AppInstanceName>" + this.instanceName + "</ns1:AppInstanceName>");
    buffer.append("\n<ns1:ResourceObjectName>" + this.objectName + "</ns1:ResourceObjectName>");
    buffer.append("\n<ns1:ITResourceName>" + this.resourceName + "</ns1:ITResourceName>");
    buffer.append("\n<ns1:BeneficiaryLogin>" + this.beneficiaryLogin + "</ns1:BeneficiaryLogin>");
    buffer.append("\n<ns1:BeneficiaryFirstName>" + this.beneficiaryFirstName + "</ns1:BeneficiaryFirstName>");
    buffer.append("\n<ns1:BeneficiaryLastName>" + this.beneficiaryLastName + "</ns1:BeneficiaryLastName>");
    buffer.append("\n<ns1:DescriptiveField>" + this.descriptiveField + "</ns1:DescriptiveField>");
    buffer.append("\n<ns1:ProvisioningOperation>" + this.operation + "</ns1:ProvisioningOperation>");
    buffer.append("\n<ns1:URL>" + this.callbackURL + "</ns1:URL>");
    buffer.append("\n<ns1:RequestKey>" + this.requestKey + "</ns1:RequestKey>");
    buffer.append("\n<ns1:RequesterLogin>" + this.requesterLogin + "</ns1:RequesterLogin>");
    buffer.append("\n<ns1:OperationKey>" + this.processTask + "</ns1:OperationKey>");
    buffer.append("\n<ns1:EntityKey>" + this.entityKey + "</ns1:EntityKey>");
    buffer.append("\n<ns1:EntityType>" + this.entityType + "</ns1:EntityType>");
    buffer.append("\n</ns1:process>");
    return buffer.toString();
  }
}