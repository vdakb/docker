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

    File        :   PolicyResponse.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    PolicyResponse.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.common.spi.schema;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;

////////////////////////////////////////////////////////////////////////////////
// class PolicyResponse
// ~~~~~ ~~~~~~~~~~~~~~~~
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
 **         &lt;element name="type"                    type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 **         &lt;element name="mode"                    type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 **         &lt;element name="hostIdentifier"          type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element name="applicationDomain"       type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 **         &lt;element ref="{}hostPortVariationsList"                                                 minOccurs="0"/&gt;
 **         &lt;element name="policy"                  type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 **         &lt;element ref="{}uriList"                                                                minOccurs="0"/&gt;
 **         &lt;element ref="{}authnPolicyList"/&gt;
 **         &lt;element ref="{}authzPolicyList"/&gt;
 **         &lt;element ref="{}errorMsgs"                                                              minOccurs="0"/&gt;
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
@XmlRootElement(name=PolicyResponse.LOCAL)
@XmlType(name="", propOrder={"type", "mode", "hostIdentifier", "applicationDomainName", "hostPortVariationsList", "policy", "uriList", "authnPolicyList", "authzPolicyList", "errorMsgs"})
public class PolicyResponse {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String LOCAL = "PolicyRegResponse";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected String                 type;
  protected String                 mode;
  @XmlElement(required=true)
  protected String                 hostIdentifier;
  @XmlElement(required=true)
  protected String                 applicationDomainName;
  protected HostPortVariationsList hostPortVariationsList;
  protected String                 policy;
  protected UriList                uriList;
  @XmlElement(required=true)
  protected AuthnPolicyList        authnPolicyList;
  @XmlElement(required=true)
  protected AuthzPolicyList        authzPolicyList;
  protected ErrorMsgs              errorMsgs;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>PolicyResponse</code> that allows use as a
   ** JavaBean.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  public PolicyResponse() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>PolicyResponse</code> with the specified
   ** properties.
   **
   ** @param  hostIdentifier     the initial hostIdentifier of the response.
   **                            Allowed object is {@link String}.
   ** @param  authentication     the initial collection of authentication
   **                            policies of the response.
   **                            Allowed object is {@link AuthnPolicyList}.
   ** @param  authorization      the initial collection of authorization
   **                            policies of the response.
   **                            Allowed object is {@link AuthzPolicyList}.
   */
  public PolicyResponse(final String hostIdentifier, final AuthnPolicyList authentication, final AuthzPolicyList authorization) {
    // ensure inheritance
    super();

    // initialize instance
    this.hostIdentifier  = hostIdentifier;
    this.authnPolicyList = authentication;
    this.authzPolicyList = authorization;
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
  // Method:   setApplicationDomainName
  /**
   ** Sets the value of the <code>applicationDomainName</code> property.
   **
   ** @param  value              the value of the
   **                            <code>applicationDomainName</code> property.
   **                            Allowed object is {@link String}.
   */
  public void setApplicationDomainName(final String value) {
    this.applicationDomainName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getApplicationDomainName
  /**
   ** Returns the value of the <code>applicationDomainName</code> property.
   **
   ** @return                    the value of the
   **                            <code>applicationDomainName</code> property.
   **                            Possible object is {@link String}.
   */
  public String getApplicationDomainName() {
    return this.applicationDomainName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setHostPortVariationsList
  /**
   ** Sets the value of the <code>hostPortVariationsList</code> property.
   **
   ** @param  value              the value of the
   **                            <code>hostPortVariationsList</code> property.
   **                            Allowed object is
   **                            {@link HostPortVariationsList}.
   */
  public void setHostPortVariationsList(final HostPortVariationsList value) {
    this.hostPortVariationsList = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getHostPortVariationsList
  /**
   ** Returns the value of the <code>hostPortVariationsList</code> property.
   **
   ** @return                    the value of the
   **                            <code>hostPortVariationsList</code> property.
   **                            Possible object is
   **                            {@link HostPortVariationsList}.
   */
  public HostPortVariationsList getHostPortVariationsList() {
    return this.hostPortVariationsList;
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
}