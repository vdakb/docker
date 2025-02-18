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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Manager Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   BaseResponse.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    BaseResponse.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.common.spi.schema;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

////////////////////////////////////////////////////////////////////////////////
// abstract class BaseResponse
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~
/**
 ** Java class for anonymous complex type.
 ** <p>
 ** The following schema fragment specifies the expected content contained
 ** within this class.
 ** <pre>
 ** &lt;complexType&gt;
 **   &lt;complexContent&gt;
 **     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **       &lt;sequence&gt;
 **         &lt;element name="type"      type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="mode"      type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="agentName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **       &lt;sequence&gt;
 **     &lt;/restriction&gt;
 **   &lt;/complexContent&gt;
 ** &lt;/complexType&gt;
 ** </pre>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name=BaseResponse.LOCAL)
@XmlType(name="", propOrder={ "type", "mode", "agentName", "description", "version", "security", "hostIdentifier", "applicationDomain", "policy", "uriList", "authnPolicyList", "authzPolicyList", "errorMsgs", "managedServerUrl"})
@XmlSeeAlso({Agent10gResponse.class, Agent11gResponse.class, OrclSSOResponse.class, OpenSSOResponse.class})
public class BaseResponse {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String LOCAL = "BasicRegResponse";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected String          type;
  protected String          mode;
  protected String          description;
  @XmlElement(required=true)
  protected String          agentName;
  protected String          version;
  protected String          security;
  protected String          hostIdentifier;
  protected String          applicationDomain;
  protected String          policy;
  protected UriList         uriList;
  @XmlElement(required=true)
  protected AuthnPolicyList authnPolicyList;
  @XmlElement(required=true)
  protected AuthzPolicyList authzPolicyList;
  protected ErrorMsgs       errorMsgs;
  @XmlSchemaType(name="anyURI")
  protected String          managedServerUrl;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>BaseResponse</code> that allows use as a
   ** JavaBean.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  public BaseResponse() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setType
  /**
   ** Sets the value of the <code>type</code> property.
   **
   ** @param  value              the value of the <code>type</code> property.
   **                            Allowed object is {@link String}.
   */
  public void setType(final String value) {
    this.type = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getType
  /**
   ** Returns the value of the <code>type</code> property.
   **
   ** @return                    the value of the <code>type</code> property.
   **                            Possible object is {@link String}.
   */
  public String getType() {
    return this.type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setMode
  /**
   ** Sets the value of the <code>mode</code> property.
   **
   ** @param  value              the value of the <code>mode</code> property.
   **                            Allowed object is {@link String}.
   */
  public void setMode(final String value) {
    this.mode = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getMode
  /**
   ** Returns the value of the <code>mode</code> property.
   **
   ** @return                    the value of the <code>mode</code> property.
   **                            Possible object is {@link String}.
   */
  public String getMode() {
    return this.mode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAgentName
  /**
   ** Sets the value of the <code>agentName</code> property.
   **
   ** @param  value              the value of the <code>agentName</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setAgentName(final String value) {
    this.agentName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAgentName
  /**
   ** Returns the value of the <code>agentName</code> property.
   **
   ** @return                    the value of the <code>agentName</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getAgentName() {
    return this.agentName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDescription
  /**
   ** Sets the value of the <code>description</code> property.
   **
   ** @param  value              the value of the <code>description</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setDescription(final String value) {
    this.description = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDescription
  /**
   ** Returns the value of the <code>description</code> property.
   **
   ** @return                    the value of the <code>description</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getDescription() {
    return this.description;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setVersion
  /**
   ** Sets the value of the <code>version</code> property.
   **
   ** @param  value              the value of the <code>version</code> property.
   **                            Allowed object is {@link String}.
   */
  public void setVersion(final String value) {
    this.version = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getVersion
  /**
   ** Returns the value of the <code>version</code> property.
   **
   ** @return                    the value of the <code>version</code> property.
   **                            Possible object is {@link String}.
   */
  public String getVersion() {
    return this.version;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSecurity
  /**
   ** Sets the value of the <code>security</code> property.
   **
   ** @param  value              the value of the <code>security</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setSecurity(final String value) {
    this.security = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSecurity
  /**
   ** Returns the value of the <code>security</code> property.
   **
   ** @return                    the value of the <code>security</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getSecurity() {
    return this.security;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setHostIdentifier
  /**
   ** Sets the value of the <code>hostIdentifier</code> property.
   **
   ** @param  value              the value of the <code>hostIdentifier</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setHostIdentifier(final String value) {
    this.hostIdentifier = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getHostIdentifier
  /**
   ** Returns the value of the <code>hostIdentifier</code> property.
   **
   ** @return                    the value of the <code>hostIdentifier</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getHostIdentifier() {
    return this.hostIdentifier;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setApplicationDomain
  /**
   ** Sets the value of the <code>applicationDomain</code> property.
   **
   ** @param  value              the value of the <code>applicationDomain</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setApplicationDomain(final String value) {
    this.applicationDomain = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getApplicationDomain
  /**
   ** Returns the value of the <code>applicationDomain</code> property.
   **
   ** @return                    the value of the <code>applicationDomain</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getApplicationDomain() {
    return this.applicationDomain;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPolicy
  /**
   ** Sets the value of the <code>policy</code> property.
   **
   ** @param  value              the value of the <code>policy</code> property.
   **                            Allowed object is {@link String}.
   */
  public void setPolicy(final String value) {
    this.policy = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPolicy
  /**
   ** Returns the value of the <code>policy</code> property.
   **
   ** @return                    the value of the <code>policy</code> property.
   **                            Possible object is {@link String}.
   */
  public String getPolicy() {
    return this.policy;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setUriList
  /**
   ** Sets the value of the <code>uriList</code> property.
   **
   ** @param  value              the value of the <code>uriList</code> property.
   **                            Allowed object is {@link UriList}.
   */
  public void setUriList(final UriList value) {
    this.uriList = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUriList
  /**
   ** Returns the value of the <code>uriList</code> property.
   **
   ** @return                    the value of the <code>uriList</code> property.
   **                            Possible object is {@link UriList}.
   */
  public UriList getUriList() {
    return this.uriList;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAuthnPolicyList
  /**
   ** Sets the value of the <code>authnPolicyList</code> property.
   **
   ** @param  value              the value of the <code>authnPolicyList</code>
   **                            property.
   **                            Allowed object is {@link AuthnPolicyList}.
   */
  public void setAuthnPolicyList(final AuthnPolicyList value) {
    this.authnPolicyList = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAuthnPolicyList
  /**
   ** Returns the value of the <code>authnPolicyList</code> property.
   **
   ** @return                    the value of the <code>authnPolicyList</code>
   **                            property.
   **                            Possible object is {@link AuthnPolicyList}.
   */
  public AuthnPolicyList getAuthnPolicyList() {
    return this.authnPolicyList;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAuthzPolicyList
  /**
   ** Sets the value of the <code>authzPolicyList</code> property.
   **
   ** @param  value              the value of the <code>authzPolicyList</code>
   **                            property.
   **                            Allowed object is {@link AuthzPolicyList}.
   */
  public void setAuthzPolicyList(final AuthzPolicyList value) {
    this.authzPolicyList = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAuthzPolicyList
  /**
   ** Returns the value of the <code>authzPolicyList</code> property.
   **
   ** @return                    the value of the <code>authzPolicyList</code>
   **                            property.
   **                            Possible object is {@link AuthzPolicyList}.
   */
  public AuthzPolicyList getAuthzPolicyList() {
    return this.authzPolicyList;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setErrorMsgs
  /**
   ** Sets the value of the <code>errorMsgs</code> property.
   **
   ** @param  value              the value of the <code>errorMsgs</code>
   **                            property.
   **                            Allowed object is {@link ErrorMsgs}.
   */
  public void setErrorMsgs(final ErrorMsgs value) {
    this.errorMsgs = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getErrorMsgs
  /**
   ** Returns the value of the <code>errorMsgs</code> property.
   **
   ** @return                    the value of the <code>errorMsgs</code>
   **                            property.
   **                            Possible object is {@link ErrorMsgs}.
   */
  public ErrorMsgs getErrorMsgs() {
    return this.errorMsgs;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setManagedServerUrl
  /**
   ** Sets the value of the <code>managedServerUrl</code> property.
   **
   ** @param  value              the value of the <code>managedServerUrl</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setManagedServerUrl(final String value) {
    this.managedServerUrl = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getManagedServerUrl
  /**
   ** Returns the value of the <code>managedServerUrl</code> property.
   **
   ** @return                    the value of the <code>managedServerUrl</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getManagedServerUrl() {
    return this.managedServerUrl;
  }
}