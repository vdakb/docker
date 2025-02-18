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

    File        :   OpenSSOUpdate.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    OpenSSOUpdate.


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
// class OpenSSOUpdate
// ~~~~~ ~~~~~~~~~~~~~~
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
 **         &lt;element name="type"                         type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="mode"                         type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="username"                     type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="password"                     type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="serverAddress"                type="{http://www.w3.org/2001/XMLSchema}anyURI"/&gt;
 **         &lt;element name="hostIdentifier"               type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="agentBaseUrl"                 type="{http://www.w3.org/2001/XMLSchema}anyURI"  minOccurs="0"/&gt;
 **         &lt;element name="agentName"                    type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element name="agentType"                    type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="agentPassword"                type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="modifyAccessClientPasswdFlag" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 **         &lt;element name="organizationName"             type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="state"                        type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="filterMode"                   type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="sessionTimeout"               type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="maxSession"                   type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="agentDebugDir"                type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="agentDebugFileName"           type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="agentAuditDir"                type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="agentAuditFileName"           type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="cookieName"                   type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="cookieSeparator"              type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="cookieEncode"                 type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 **         &lt;element name="ssoOnly"                      type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 **         &lt;element name="accessDeniedUrl"              type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element ref="{}logInUrls"                                                                    minOccurs="0"/&gt;
 **         &lt;element ref="{}logOutUrls"                                                                   minOccurs="0"/&gt;
 **         &lt;element ref="{}notEnforcedUrls"                                                              minOccurs="0"/&gt;
 **         &lt;element name="debugLevel"                   type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="userIdParamType"              type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="userIdParam"                  type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="userMappingMode"              type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="userAttributeName"            type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="userPrincipal"                type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="userToken"                    type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="profileAttributeFetchMode"    type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="{}profileAttributeMapping"                                                     minOccurs="0"/&gt;
 **         &lt;element name="sessionAttributeFetchMode"    type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="{}sessionAttributeMapping"                                                     minOccurs="0"/&gt;
 **         &lt;element name="responseAttributeFetchMode"   type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="{}responseAttributeMapping"                                                    minOccurs="0"/&gt;
 **         &lt;element name="{}miscellaneousProperties"                                                     minOccurs="0"/&gt;
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
@XmlRootElement(name=OpenSSOUpdate.LOCAL)
@XmlType(name="", propOrder={"agentBaseUrl", "agentType", "agentPassword", "modifyAccessClientPasswdFlag", "organizationName", "state", "filterMode", "sessionTimeout", "maxSession", "agentDebugDir", "agentDebugFileName", "agentAuditDir", "agentAuditFileName", "cookieName", "cookieSeperator", "cookieEncode", "ssoOnly", "accessDeniedUrl", "logInUrls", "logOutUrls", "notEnforcedUrls", "debugLevel", "userIdParamType", "userIdParam", "userMappingMode", "userAttributeName", "userPrincipal", "userToken", "profileAttributeFetchMode", "profileAttributeMapping", "sessionAttributeFetchMode", "sessionAttributeMapping", "responseAttributeFetchMode", "responseAttributeMapping", "miscellaneousProperties"})
public class OpenSSOUpdate extends BaseRequest {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String LOCAL = "OpenSSOUpdateAgentRegRequest";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @XmlElement(required=true)
  @XmlSchemaType(name="anyURI")
  protected String                   agentBaseUrl;
  protected String                   agentType;
  protected String                   agentPassword;
  protected Boolean                  modifyAccessClientPasswdFlag;
  protected String                   organizationName;
  protected String                   state;
  protected String                   filterMode;
  protected String                   sessionTimeout;
  protected String                   maxSession;
  protected String                   agentDebugDir;
  protected String                   agentDebugFileName;
  protected String                   agentAuditDir;
  protected String                   agentAuditFileName;
  protected String                   cookieName;
  protected String                   cookieSeperator;
  protected Boolean                  cookieEncode;
  protected Boolean                  ssoOnly;
  protected String                   accessDeniedUrl;
  protected LogInUrls                logInUrls;
  protected LogOutUrls               logOutUrls;
  protected NotEnforcedUrls          notEnforcedUrls;
  protected String                   debugLevel;
  protected String                   userIdParamType;
  protected String                   userIdParam;
  protected String                   userMappingMode;
  protected String                   userAttributeName;
  protected String                   userPrincipal;
  protected String                   userToken;
  protected String                   profileAttributeFetchMode;
  protected ProfileAttributeMapping  profileAttributeMapping;
  protected String                   sessionAttributeFetchMode;
  protected SessionAttributeMapping  sessionAttributeMapping;
  protected String                   responseAttributeFetchMode;
  protected ResponseAttributeMapping responseAttributeMapping;
  protected MiscellaneousProperties  miscellaneousProperties;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>OpenSSOUpdate</code> that allows use as a JavaBean.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  public OpenSSOUpdate() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OpenSSOUpdate</code> with the specified properties.
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
  public OpenSSOUpdate(final String mode, final String serverAddress, final String username, final String password,  final String agentName) {
    // ensure inheritance
    super(TYPE_OPENSSO, mode, serverAddress, username, password,  agentName);
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
  // Method:   setAgentType
  /**
   ** Sets the value of the <code>agentType</code> property.
   **
   ** @param  value              the value of the <code>agentType</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setAgentType(final String value) {
    this.agentType = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAgentType
  /**
   ** Returns the value of the <code>agentType</code> property.
   **
   ** @return                    the value of the <code>agentType</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getAgentType() {
    return this.agentType;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAgentPassword
  /**
   ** Sets the value of the <code>agentPassword</code> property.
   **
   ** @param  value              the value of the <code>agentPassword</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setAgentPassword(final String value) {
    this.agentPassword = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAgentPassword
  /**
   ** Returns the value of the <code>agentPassword</code> property.
   **
   ** @return                    the value of the <code>agentPassword</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getAgentPassword() {
    return this.agentPassword;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setModifyAccessClientPasswdFlag
  /**
   ** Sets the value of the <code>modifyAccessClientPasswdFlag</code> property.
   **
   ** @param  value              the value of the
   **                            <code>modifyAccessClientPasswdFlag</code>
   **                            property.
   **                            Allowed object is {@link Boolean}.
   */
  public void setModifyAccessClientPasswdFlag(final Boolean value) {
    this.modifyAccessClientPasswdFlag = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getModifyAccessClientPasswdFlag
  /**
   ** Returns the value of the <code>modifyAccessClientPasswdFlag</code>
   ** property.
   **
   ** @return                    the value of the
   **                            <code>modifyAccessClientPasswdFlag</code>
   **                            property.
   **                            Possible object is {@link Boolean}.
   */
  public Boolean getModifyAccessClientPasswdFlag() {
    return this.modifyAccessClientPasswdFlag;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setOrganizationName
  /**
   ** Sets the value of the <code>organizationName</code> property.
   **
   ** @param  value              the value of the <code>organizationName</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setOrganizationName(final String value) {
    this.organizationName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getOrganizationName
  /**
   ** Returns the value of the <code>organizationName</code> property.
   **
   ** @return                    the value of the <code>organizationName</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getOrganizationName() {
    return this.organizationName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setState
  /**
   ** Sets the value of the <code>state</code> property.
   **
   ** @param  value              the value of the <code>state</code> property.
   **                            Allowed object is {@link String}.
   */
  public void setState(final String value) {
    this.state = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getState
  /**
   ** Returns the value of the <code>state</code> property.
   **
   ** @return                    the value of the <code>state</code> property.
   **                            Possible object is {@link String}.
   */
  public String getState() {
    return this.state;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setFilterMode
  /**
   ** Sets the value of the <code>filterMode</code> property.
   **
   ** @param  value              the value of the <code>filterMode</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setFilterMode(final String value) {
    this.filterMode = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getFilterMode
  /**
   ** Returns the value of the <code>filterMode</code> property.
   **
   ** @return                    the value of the <code>filterMode</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getFilterMode() {
    return this.filterMode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setMaxSession
  /**
   ** Sets the value of the <code>maxSession</code> property.
   **
   ** @param  value              the value of the <code>maxSession</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setMaxSession(final String value) {
    this.maxSession = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getMaxSession
  /**
   ** Returns the value of the <code>maxSession</code> property.
   **
   ** @return                    the value of the <code>maxSession</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getMaxSession() {
    return this.maxSession;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSessionTimeout
  /**
   ** Sets the value of the <code>sessionTimeout</code> property.
   **
   ** @param  value              the value of the <code>sessionTimeout</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setSessionTimeout(final String value) {
    this.sessionTimeout = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSessionTimeout
  /**
   ** Returns the value of the <code>sessionTimeout</code> property.
   **
   ** @return                    the value of the <code>sessionTimeout</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getSessionTimeout() {
    return this.sessionTimeout;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAgentDebugDir
  /**
   ** Sets the value of the <code>agentDebugDir</code> property.
   **
   ** @param  value              the value of the <code>agentDebugDir</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setAgentDebugDir(final String value) {
    this.agentDebugDir = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAgentDebugDir
  /**
   ** Returns the value of the <code>agentDebugDir</code> property.
   **
   ** @return                    the value of the <code>agentDebugDir</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getAgentDebugDir() {
    return this.agentDebugDir;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAgentDebugFileName
  /**
   ** Sets the value of the <code>agentDebugFileName</code> property.
   **
   ** @param  value              the value of the
   **                            <code>agentDebugFileName</code> property.
   **                            Allowed object is {@link String}.
   */
  public void setAgentDebugFileName(final String value) {
    this.agentDebugFileName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAgentDebugFileName
  /**
   ** Returns the value of the <code>agentDebugFileName</code> property.
   **
   ** @return                    the value of the
   **                            <code>agentDebugFileName</code> property.
   **                            Possible object is {@link String}.
   */
  public String getAgentDebugFileName() {
    return this.agentDebugFileName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAgentAuditDir
  /**
   ** Sets the value of the <code>agentAuditDir</code> property.
   **
   ** @param  value              the value of the <code>agentAuditDir</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setAgentAuditDir(final String value) {
    this.agentAuditDir = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAgentAuditDir
  /**
   ** Returns the value of the <code>agentAuditDir</code> property.
   **
   ** @return                    the value of the <code>agentAuditDir</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getAgentAuditDir() {
    return this.agentAuditDir;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAgentAuditFileName
  /**
   ** Sets the value of the <code>agentAuditFileName</code> property.
   **
   ** @param  value              the value of the
   **                            <code>agentAuditFileName</code> property.
   **                            Allowed object is {@link String}.
   */
  public void setAgentAuditFileName(final String value) {
    this.agentAuditFileName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAgentAuditFileName
  /**
   ** Returns the value of the <code>agentAuditFileName</code> property.
   **
   ** @return                    the value of the
   **                            <code>agentAuditFileName</code> property.
   **                            Possible object is {@link String}.
   */
  public String getAgentAuditFileName() {
    return this.agentAuditFileName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setCookieName
  /**
   ** Sets the value of the <code>cookieName</code> property.
   **
   ** @param  value              the value of the <code>cookieName</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setCookieName(final String value) {
    this.cookieName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getCookieName
  /**
   ** Returns the value of the <code>cookieName</code> property.
   **
   ** @return                    the value of the <code>cookieName</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getCookieName() {
    return this.cookieName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setCookieSeperator
  /**
   ** Sets the value of the <code>cookieSeperator</code> property.
   **
   ** @param  value              the value of the <code>cookieSeperator</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setCookieSeperator(final String value) {
    this.cookieSeperator = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getCookieSeperator
  /**
   ** Returns the value of the <code>cookieSeperator</code> property.
   **
   ** @return                    the value of the <code>cookieSeperator</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getCookieSeperator() {
    return this.cookieSeperator;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setCookieEncode
  /**
   ** Sets the value of the <code>cookieEncode</code> property.
   **
   ** @param  value              the value of the <code>cookieEncode</code>
   **                            property.
   **                            Allowed object is {@link Boolean}.
   */
  public void setCookieEncode(final Boolean value) {
    this.cookieEncode = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getCookieEncode
  /**
   ** Returns the value of the <code>cookieEncode</code> property.
   **
   ** @return                    the value of the <code>cookieEncode</code>
   **                            property.
   **                            Possible object is {@link Boolean}.
   */
  public Boolean getCookieEncode() {
    return this.cookieEncode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSsoOnly
  /**
   ** Sets the value of the <code>ssoOnly</code> property.
   **
   ** @param  value              the value of the <code>ssoOnly</code> property.
   **                            Allowed object is {@link Boolean}.
   */
  public void setSsoOnly(final Boolean value) {
    this.ssoOnly = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSsoOnly
  /**
   ** Returns the value of the <code>ssoOnly</code> property.
   **
   ** @return                    the value of the <code>ssoOnly</code> property.
   **                            Possible object is {@link Boolean}.
   */
  public Boolean getSsoOnly() {
    return this.ssoOnly;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAccessDeniedUrl
  /**
   ** Sets the value of the <code>accessDeniedUrl</code> property.
   **
   ** @param  value              the value of the <code>accessDeniedUrl</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setAccessDeniedUrl(final String value) {
    this.accessDeniedUrl = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAccessDeniedUrl
  /**
   ** Returns the value of the <code>accessDeniedUrl</code> property.
   **
   ** @return                    the value of the <code>accessDeniedUrl</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getAccessDeniedUrl() {
    return this.accessDeniedUrl;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setLogInUrls
  /**
   ** Sets the value of the <code>logInUrls</code> property.
   **
   ** @param  value              the value of the <code>logInUrls</code>
   **                            property.
   **                            Allowed object is {@link LogInUrls}.
   */
  public void setLogInUrls(final LogInUrls value) {
    this.logInUrls = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLogInUrls
  /**
   ** Returns the value of the <code>logInUrls</code> property.
   **
   ** @return                    the value of the <code>logInUrls</code>
   **                            property.
   **                            Possible object is {@link LogInUrls}.
   */
  public LogInUrls getLogInUrls() {
    return this.logInUrls;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setLogOutUrls
  /**
   ** Sets the value of the <code>logOutUrls</code> property.
   **
   ** @param  value              the value of the <code>logOutUrls</code>
   **                            property.
   **                            Allowed object is {@link LogOutUrls}.
   */
  public void setLogOutUrls(final LogOutUrls value) {
    this.logOutUrls = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLogOutUrls
  /**
   ** Returns the value of the <code>logOutUrls</code> property.
   **
   ** @return                    the value of the <code>logOutUrls</code>
   **                            property.
   **                            Possible object is {@link LogOutUrls}.
   */
  public LogOutUrls getLogOutUrls() {
    return this.logOutUrls;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setNotEnforcedUrls
  /**
   ** Sets the value of the <code>notEnforcedUrls</code> property.
   **
   ** @param  value              the value of the <code>notEnforcedUrls</code>
   **                            property.
   **                            Allowed object is {@link NotEnforcedUrls}.
   */
  public void setNotEnforcedUrls(final NotEnforcedUrls value) {
    this.notEnforcedUrls = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getNotEnforcedUrls
  /**
   ** Returns the value of the <code>notEnforcedUrls</code> property.
   **
   ** @return                    the value of the <code>notEnforcedUrls</code>
   **                            property.
   **                            Possible object is {@link NotEnforcedUrls}.
   */
  public NotEnforcedUrls getNotEnforcedUrls() {
    return this.notEnforcedUrls;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDebugLevel
  /**
   ** Sets the value of the <code>debugLevel</code> property.
   **
   ** @param  value              the value of the <code>debugLevel</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setDebugLevel(final String value) {
    this.debugLevel = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDebugLevel
  /**
   ** Returns the value of the <code>debugLevel</code> property.
   **
   ** @return                    the value of the <code>debugLevel</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getDebugLevel() {
    return this.debugLevel;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setUserIdParamType
  /**
   ** Sets the value of the <code>userIdParamType</code> property.
   **
   ** @param  value              the value of the <code>userIdParamType</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setUserIdParamType(final String value) {
    this.userIdParamType = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUserIdParamType
  /**
   ** Returns the value of the <code>userIdParamType</code> property.
   **
   ** @return                    the value of the <code>userIdParamType</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getUserIdParamType() {
    return this.userIdParamType;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setUserIdParam
  /**
   ** Sets the value of the <code>userIdParam</code> property.
   **
   ** @param  value              the value of the <code>userIdParam</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setUserIdParam(final String value) {
    this.userIdParam = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUserIdParam
  /**
   ** Returns the value of the <code>userIdParam</code> property.
   **
   ** @return                    the value of the <code>userIdParam</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getUserIdParam() {
    return this.userIdParam;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setUserMappingMode
  /**
   ** Sets the value of the <code>userMappingMode</code> property.
   **
   ** @param  value              the value of the <code>userMappingMode</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setUserMappingMode(final String value) {
    this.userMappingMode = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUserMappingMode
  /**
   ** Returns the value of the <code>userMappingMode</code> property.
   **
   ** @return                    the value of the <code>userMappingMode</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getUserMappingMode() {
    return this.userMappingMode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setUserAttributeName
  /**
   ** Sets the value of the <code>userAttributeName</code> property.
   **
   ** @param  value              the value of the <code>userAttributeName</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setUserAttributeName(final String value) {
    this.userAttributeName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUserAttributeName
  /**
   ** Returns the value of the <code>userAttributeName</code> property.
   **
   ** @return                    the value of the <code>userAttributeName</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getUserAttributeName() {
    return this.userAttributeName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setUserPrincipal
  /**
   ** Sets the value of the <code>userPrincipal</code> property.
   **
   ** @param  value              the value of the <code>userPrincipal</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setUserPrincipal(final String value) {
    this.userPrincipal = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUserPrincipal
  /**
   ** Returns the value of the <code>userPrincipal</code> property.
   **
   ** @return                    the value of the <code>userPrincipal</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getUserPrincipal() {
    return this.userPrincipal;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setUserToken
  /**
   ** Sets the value of the <code>userToken</code> property.
   **
   ** @param  value              the value of the <code>userToken</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setUserToken(final String value) {
    this.userToken = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUserToken
  /**
   ** Returns the value of the <code>userToken</code> property.
   **
   ** @return                    the value of the <code>userToken</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getUserToken() {
    return this.userToken;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setProfileAttributeFetchMode
  /**
   ** Sets the value of the <code>profileAttributeFetchMode</code> property.
   **
   ** @param  value              the value of the
   **                            <code>profileAttributeFetchMode</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setProfileAttributeFetchMode(final String value) {
    this.profileAttributeFetchMode = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getProfileAttributeFetchMode
  /**
   ** Returns the value of the <code>profileAttributeFetchMode</code> property.
   **
   ** @return                    the value of the
   **                            <code>profileAttributeFetchMode</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getProfileAttributeFetchMode() {
    return this.profileAttributeFetchMode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setProfileAttributeMapping
  /**
   ** Sets the value of the <code>profileAttributeMapping</code> property.
   **
   ** @param  value              the value of the
   **                            <code>profileAttributeMapping</code> property.
   **                            Allowed object is
   **                            {@link ProfileAttributeMapping}.
   */
  public void setProfileAttributeMapping(final ProfileAttributeMapping value) {
    this.profileAttributeMapping = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getProfileAttributeMapping
  /**
   ** Returns the value of the <code>profileAttributeMapping</code> property.
   **
   ** @return                    the value of the
   **                            <code>profileAttributeMapping</code> property.
   **                            Possible object is
   **                            {@link ProfileAttributeMapping}.
   */
  public ProfileAttributeMapping getProfileAttributeMapping() {
    return this.profileAttributeMapping;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSessionAttributeFetchMode
  /**
   ** Sets the value of the <code>sessionAttributeFetchMode</code> property.
   **
   ** @param  value              the value of the
   **                            <code>sessionAttributeFetchMode</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setSessionAttributeFetchMode(final String value) {
    this.sessionAttributeFetchMode = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSessionAttributeFetchMode
  /**
   ** Returns the value of the <code>sessionAttributeFetchMode</code> property.
   **
   ** @return                    the value of the
   **                            <code>sessionAttributeFetchMode</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getSessionAttributeFetchMode() {
    return this.sessionAttributeFetchMode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSessionAttributeMapping
  /**
   ** Sets the value of the <code>sessionAttributeMapping</code> property.
   **
   ** @param  value              the value of the
   **                            <code>sessionAttributeMapping</code> property.
   **                            Allowed object is
   **                            {@link SessionAttributeMapping}.
   */
  public void setSessionAttributeMapping(final SessionAttributeMapping value) {
    this.sessionAttributeMapping = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSessionAttributeMapping
  /**
   ** Returns the value of the <code>sessionAttributeMapping</code> property.
   **
   ** @return                    the value of the
   **                            <code>sessionAttributeMapping</code> property.
   **                            ossible object is
   **                            {@link SessionAttributeMapping}.
   */
  public SessionAttributeMapping getSessionAttributeMapping() {
    return this.sessionAttributeMapping;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setResponseAttributeFetchMode
  /**
   ** Sets the value of the <code>responseAttributeFetchMode</code> property.
   **
   ** @param  value              the value of the
   **                            <code>responseAttributeFetchMode</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setResponseAttributeFetchMode(final String value) {
    this.responseAttributeFetchMode = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getResponseAttributeFetchMode
  /**
   ** Returns the value of the <code>responseAttributeFetchMode</code> property.
   **
   ** @return                    the value of the
   **                            <code>responseAttributeFetchMode</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getResponseAttributeFetchMode() {
    return this.responseAttributeFetchMode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setResponseAttributeMapping
  /**
   ** Sets the value of the <code>responseAttributeMapping</code> property.
   **
   ** @param  value              the value of the
   **                            <code>responseAttributeMapping</code> property.
   **                            Allowed object is
   **                            {@link ResponseAttributeMapping}.
   */
  public void setResponseAttributeMapping(final ResponseAttributeMapping value) {
    this.responseAttributeMapping = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getResponseAttributeMapping
  /**
   ** Returns the value of the <code>responseAttributeMapping</code> property.
   **
   ** @return                    the value of the
   **                            <code>responseAttributeMapping</code> property.
   **                            Possible object is
   **                            {@link ResponseAttributeMapping}.
   */
  public ResponseAttributeMapping getResponseAttributeMapping() {
    return this.responseAttributeMapping;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setMiscellaneousProperties
  /**
   ** Sets the value of the <code>miscellaneousProperties</code> property.
   **
   ** @param  value              the value of the
   **                            <code>miscellaneousProperties</code> property.
   **                            Allowed object is
   **                            {@link MiscellaneousProperties}.
   */
  public void setMiscellaneousProperties(final MiscellaneousProperties value) {
    this.miscellaneousProperties = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getMiscellaneousProperties
  /**
   ** Returns the value of the <code>miscellaneousProperties</code> property.
   **
   ** @return                    the value of the
   **                            <code>miscellaneousProperties</code> property.
   **                            Possible object is
   **                            {@link MiscellaneousProperties}.
   */
  public MiscellaneousProperties getMiscellaneousProperties() {
    return this.miscellaneousProperties;
  }
}