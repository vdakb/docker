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

    File        :   OrclSSOCreate.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    OrclSSOCreate.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.common.spi.schema;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;

////////////////////////////////////////////////////////////////////////////////
// class OrclSSOCreate
// ~~~~~ ~~~~~~~~~~~~~
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
 **         &lt;element name="type"                      type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="mode"                      type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="username"                  type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="password"                  type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="serverAddress"             type="{http://www.w3.org/2001/XMLSchema}anyURI"/&gt;
 **         &lt;element name="hostIdentifier"            type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="agentName"                 type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element name="agentBaseUrl"              type="{http://www.w3.org/2001/XMLSchema}anyURI"  minOccurs="0"/&gt;
 **         &lt;element name="applicationDomain"         type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="autoCreatePolicy"          type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 **         &lt;element name="ssoServerVersion"          type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="oracleHomePath"            type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element name="virtualHost"               type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="updateMode"                type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="adminInfo"                 type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="adminId"                   type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element ref="{}protectedResourcesList"                                                    minOccurs="0"/&gt;
 **         &lt;element ref="{}publicResourcesList"                                                       minOccurs="0"/&gt;
 **         &lt;element name="protectedAuthnScheme"      type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="publicAuthnScheme"         type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element ref="{}rregApplicationDomain"                                                     minOccurs="0"/&gt;
 **         &lt;element name="logoutUrl"                 type="{http://www.w3.org/2001/XMLSchema}anyURI"  minOccurs="0"/&gt;
 **         &lt;element name="failureUrl"                type="{http://www.w3.org/2001/XMLSchema}anyURI"  minOccurs="0"/&gt;
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
@XmlRootElement(name=OrclSSOCreate.LOCAL)
@XmlType(name="", propOrder={"agentBaseUrl", "applicationDomain", "autoCreatePolicy", "ssoServerVersion", "oracleHomePath", "virtualHost", "updateMode", "adminInfo", "adminId", "protectedResource", "publicResource", "protectedAuthnScheme", "publicAuthnScheme", "rregApplicationDomain", "logoutUrl", "failureUrl"})
public class OrclSSOCreate extends BaseRequest {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String LOCAL = "OSSORegRequest";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @XmlElement(required=true)
  @XmlSchemaType(name="anyURI")
  protected String                 agentBaseUrl;
  protected String                 applicationDomain;
  protected Boolean                autoCreatePolicy;
  protected String                 ssoServerVersion;
  @XmlElement(required=true)
  protected String                 oracleHomePath;
  protected String                 virtualHost;
  protected String                 updateMode;
  protected String                 adminInfo;
  protected String                 adminId;
  @XmlElement(name=ProtectedResourcesList.LOCAL)
  protected ProtectedResourcesList protectedResource;
  @XmlElement(name=PublicResourcesList.LOCAL)
  protected PublicResourcesList    publicResource;
  protected String                 protectedAuthnScheme;
  protected String                 publicAuthnScheme;
  protected RregApplicationDomain  rregApplicationDomain;
  @XmlSchemaType(name="anyURI")
  protected String                 logoutUrl;
  @XmlSchemaType(name="anyURI")
  protected String                 failureUrl;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>OrclSSOCreate</code> that allows use as a JavaBean.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  public OrclSSOCreate() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OrclSSOCreate</code> with the specified properties.
   **
   ** @param  mode               the operational mode to apply.
   ** @param  serverAddress      the initial serverAddress of the request.
   **                            Allowed object is {@link String}.
   ** @param  username           the initial username of the request to
   **                            authenticate on the remote side.
   **                            Allowed object is {@link String}.
   ** @param  password           the initial password of the request to
   **                            authenticate on the remote side.
   **                            Allowed object is {@link String}.
   ** @param  agentName          the initial agentName of the agent.
   **                            Allowed object is {@link String}.
   */
  public OrclSSOCreate(final String mode, final String serverAddress, final String username, final String password,  final String agentName) {
    // ensure inheritance
    super(TYPE_ORCLSSO, mode, serverAddress, username, password,  agentName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAgentBaseUrl
  /**
   ** Sets the value of the <code>agentBaseUrl</code> property.
   **
   ** @param  value              the value of the <code>agentBaseUrl</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setAgentBaseUrl(final String value) {
    this.agentBaseUrl = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAgentBaseUrl
  /**
   ** Returns the value of the <code>agentBaseUrl</code> property.
   **
   ** @return                    the value of the <code>agentBaseUrl</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getAgentBaseUrl() {
    return this.agentBaseUrl;
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
  // Method:   setAutoCreatePolicy
  /**
   ** Sets the value of the <code>autoCreatePolicy</code> property.
   **
   ** @param  value              the value of the <code>autoCreatePolicy</code>
   **                            property.
   **                            Allowed object is {@link Boolean}.
   */
  public void setAutoCreatePolicy(final Boolean value) {
    this.autoCreatePolicy = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAutoCreatePolicy
  /**
   ** Returns the value of the <code>autoCreatePolicy</code> property.
   **
   ** @return                    the value of the <code>autoCreatePolicy</code>
   **                            property.
   **                            Possible object is {@link Boolean}.
   */
  public Boolean getAutoCreatePolicy() {
    return this.autoCreatePolicy;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSsoServerVersion
  /**
   ** Sets the value of the <code>ssoServerVersion</code> property.
   **
   ** @param  value              the value of the <code>ssoServerVersion</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setSsoServerVersion(final String value) {
    this.ssoServerVersion = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSsoServerVersion
  /**
   ** Returns the value of the <code>ssoServerVersion</code> property.
   **
   ** @return                    the value of the <code>ssoServerVersion</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getSsoServerVersion() {
    return this.ssoServerVersion;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setOracleHomePath
  /**
   ** Sets the value of the <code>oracleHomePath</code> property.
   **
   ** @param  value              the value of the <code>oracleHomePath</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setOracleHomePath(final String value) {
    this.oracleHomePath = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getOracleHomePath
  /**
   ** Returns the value of the <code>oracleHomePath</code> property.
   **
   ** @return                    the value of the <code>oracleHomePath</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getOracleHomePath() {
    return this.oracleHomePath;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setVirtualHost
  /**
   ** Sets the value of the <code>virtualHost</code> property.
   **
   ** @param  value              the value of the <code>virtualHost</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setVirtualHost(final String value) {
    this.virtualHost = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getVirtualHost
  /**
   ** Returns the value of the <code>virtualHost</code> property.
   **
   ** @return                    the value of the <code>virtualHost</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getVirtualHost() {
    return this.virtualHost;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setUpdateMode
  /**
   ** Sets the value of the <code>updateMode</code> property.
   **
   ** @param  value              the value of the <code>updateMode</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setUpdateMode(final String value) {
    this.updateMode = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUpdateMode
  /**
   ** Returns the value of the <code>updateMode</code> property.
   **
   ** @return                    the value of the <code>updateMode</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getUpdateMode() {
    return this.updateMode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAdminInfo
  /**
   ** Sets the value of the <code>adminInfo</code> property.
   **
   ** @param  value              the value of the <code>adminInfo</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setAdminInfo(final String value) {
    this.adminInfo = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAdminInfo
  /**
   ** Returns the value of the <code>adminInfo</code> property.
   **
   ** @return                    the value of the <code>adminInfo</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getAdminInfo() {
    return this.adminInfo;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAdminId
  /**
   ** Sets the value of the <code>adminId</code> property.
   **
   ** @param  value              the value of the <code>adminId</code> property.
   **                            Allowed object is {@link String}.
   */
  public void setAdminId(final String value) {
    this.adminId = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAdminId
  /**
   ** Returns the value of the <code>adminId</code> property.
   **
   ** @return                    the value of the <code>adminId</code> property.
   **                            Possible object is {@link String}.
   */
  public String getAdminId() {
    return this.adminId;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setProtectedResource
  /**
   ** Sets the value of the <code>protectedResource</code> property.
   **
   ** @param  value              the value of the <code>protectedResource</code>
   **                            property.
   **                            Allowed object is
   **                            {@link ProtectedResourcesList}.
   */
  public void setProtectedResource(final ProtectedResourcesList value) {
    this.protectedResource = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getProtectedResource
  /**
   ** Returns the value of the <code>protectedResource</code> property.
   **
   ** @return                    the value of the <code>protectedResource</code>
   **                            property.
   **                            Possible object is
   **                            {@link ProtectedResourcesList}.
   */
  public ProtectedResourcesList getProtectedResource() {
    return this.protectedResource;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPublicResource
  /**
   ** Sets the value of the <code>publicResource</code> property.
   **
   ** @param  value              the value of the <code>publicResource</code>
   **                            property.
   **                            Allowed object is {@link PublicResourcesList}.
   */
  public void setPublicResource(final PublicResourcesList value) {
    this.publicResource = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPublicResource
  /**
   ** Returns the value of the <code>publicResource</code> property.
   **
   ** @return                    the value of the <code>publicResource</code>
   **                            property.
   **                            Possible object is {@link PublicResourcesList}.
   */
  public PublicResourcesList getPublicResource() {
    return this.publicResource;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setProtectedAuthnScheme
  /**
   ** Sets the value of the <code>protectedAuthnScheme</code> property.
   **
   ** @param  value              the value of the
   **                            <code>protectedAuthnScheme</code> property.
   **                            Allowed object is {@link String}.
   */
  public void setProtectedAuthnScheme(final String value) {
    this.protectedAuthnScheme = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getProtectedAuthnScheme
  /**
   ** Returns the value of the <code>protectedAuthnScheme</code> property.
   **
   ** @return                    the value of the
   **                            <code>protectedAuthnScheme</code> property.
   **                            Possible object is {@link String}.
   */
  public String getProtectedAuthnScheme() {
    return this.protectedAuthnScheme;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPublicAuthnScheme
  /**
   ** Sets the value of the <code>publicAuthnScheme</code> property.
   **
   ** @param  value              the value of the <code>publicAuthnScheme</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setPublicAuthnScheme(final String value) {
    this.publicAuthnScheme = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPublicAuthnScheme
  /**
   ** Returns the value of the <code>publicAuthnScheme</code> property.
   **
   ** @return                    the value of the <code>publicAuthnScheme</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getPublicAuthnScheme() {
    return this.publicAuthnScheme;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setRregApplicationDomain
  /**
   ** Sets the value of the <code>rregApplicationDomain</code> property.
   **
   ** @param  value              the value of the
   **                            <code>rregApplicationDomain</code> property.
   **                            Allowed object is {@link RregApplicationDomain}.
   */
  public void setRregApplicationDomain(final RregApplicationDomain value) {
    this.rregApplicationDomain = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getRregApplicationDomain
  /**
   ** Returns the value of the <code>rregApplicationDomain</code> property.
   **
   ** @return                    the value of the
   **                            <code>rregApplicationDomain</code> property.
   **                            Possible object is {@link RregApplicationDomain}.
   */
  public RregApplicationDomain getRregApplicationDomain() {
    return this.rregApplicationDomain;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setLogoutUrl
  /**
   ** Sets the value of the <code>logoutUrl</code> property.
   **
   ** @param  value              the value of the <code>logoutUrl</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setLogoutUrl(final String value) {
    this.logoutUrl = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLogoutUrl
  /**
   ** Returns the value of the <code>logoutUrl</code> property.
   **
   ** @return                    the value of the <code>logoutUrl</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getLogoutUrl() {
    return this.logoutUrl;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setFailureUrl
  /**
   ** Sets the value of the <code>failureUrl</code> property.
   **
   ** @param  value              the value of the <code>failureUrl</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setFailureUrl(final String value) {
    this.failureUrl = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getFailureUrl
  /**
   ** Returns the value of the <code>failureUrl</code> property.
   **
   ** @return                    the value of the <code>failureUrl</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getFailureUrl() {
    return this.failureUrl;
  }
}