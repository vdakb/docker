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

    File        :   OpenSSOResponse.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    OpenSSOResponse.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.common.spi.schema;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;

////////////////////////////////////////////////////////////////////////////////
// class OpenSSOResponse
// ~~~~~ ~~~~~~~~~~~~~~~
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
 **         &lt;element name="type"                       type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="mode"                       type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="agentName"                  type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element name="agentBaseUrl"               type="{http://www.w3.org/2001/XMLSchema}anyURI"  minOccurs="0"/&gt;
 **         &lt;element name="agentType"                  type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="agentVersion"               type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="agentPassword"              type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="organizationName"           type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="agentEncryptionKey"         type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="agentDebugDir"              type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="agentAuditDir"              type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="agentAuditFileName"         type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="managedServerUrl"           type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="debug"                      type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 **         &lt;element name="cookieName"                 type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="accessDeniedUrl"            type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element ref="{}logOutUrls"                                                                 minOccurs="0"/&gt;
 **         &lt;element name="filterMode"                 type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="sessionTimeout"             type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="sessionTimeoutUnit"         type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="cookieSeparator"            type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="cookieEncode"               type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 **         &lt;element ref="{}logInUrls"                                                                  minOccurs="0"/&gt;
 **         &lt;element name="debugLevel"                 type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="userMappingMode"            type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="userAttributeName"          type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="userPrincipal"              type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="userToken"                  type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="profileAttributeFetchMode"  type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="sessionAttributeFetchMode"  type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="responseAttributeFetchMode" type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="{}profileAttributeMapping"                                                   minOccurs="0"/&gt;
 **         &lt;element name="{}sessionAttributeMapping"                                                   minOccurs="0"/&gt;
 **         &lt;element name="{}responseAttributeMapping"                                                  minOccurs="0"/&gt;
 **         &lt;element name="{}responseProperties"                                                        minOccurs="0"/&gt;
 **         &lt;element ref="{}errorMsgs"                                                                  minOccurs="0"/&gt;
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
@XmlRootElement(name=OpenSSOResponse.LOCAL)
@XmlType(name="", propOrder={"agentBaseUrl", "agentType", "agentVersion", "agentPassword", "organizationName", "agentEncryptionKey", "agentDebugDir", "agentAuditDir", "agentAuditFileName", "managedServerUrl", "debug", "cookieName", "accessDeniedUrl", "logOutUrls", "filterMode", "sessionTimeout", "sessionTimeoutUnit", "cookieSeparator", "cookieEncode", "logInUrls", "debugLevel", "userMappingMode", "userAttributeName", "userPrincipal", "userToken", "profileAttributeFetchMode", "sessionAttributeFetchMode", "responseAttributeFetchMode", "profileAttributeMapping", "sessionAttributeMapping", "responseAttributeMapping", "responseProperties"})
public class OpenSSOResponse extends BaseResponse {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String LOCAL = "OpenSSORegResponse";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @XmlSchemaType(name="anyURI")
  protected String                   agentBaseUrl;
  protected String                   agentType;
  protected String                   agentVersion;
  protected String                   agentPassword;
  protected String                   organizationName;
  protected String                   agentEncryptionKey;
  protected String                   agentDebugDir;
  protected String                   agentAuditDir;
  protected String                   agentAuditFileName;
  protected String                   managedServerUrl;
  protected Boolean                  debug;
  protected String                   cookieName;
  protected String                   accessDeniedUrl;
  protected LogOutUrls               logOutUrls;
  protected String                   filterMode;
  protected String                   sessionTimeout;
  protected String                   sessionTimeoutUnit;
  protected String                   cookieSeparator;
  protected Boolean                  cookieEncode;
  protected LogInUrls                logInUrls;
  protected String                   debugLevel;
  protected String                   userMappingMode;
  protected String                   userAttributeName;
  protected String                   userPrincipal;
  protected String                   userToken;
  protected String                   profileAttributeFetchMode;
  protected String                   sessionAttributeFetchMode;
  protected String                   responseAttributeFetchMode;
  protected ProfileAttributeMapping  profileAttributeMapping;
  protected SessionAttributeMapping  sessionAttributeMapping;
  protected ResponseAttributeMapping responseAttributeMapping;
  protected ResponseProperties       responseProperties;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>OpenSSOResponse</code> that allows use as a
   ** JavaBean.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  public OpenSSOResponse() {
    // ensure inheritance
    super();
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
  // Method:   setAgentVersion
  /**
   ** Sets the value of the <code>agentVersion</code> property.
   **
   ** @param  value              the value of the <code>agentVersion</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setAgentVersion(final String value) {
    this.agentVersion = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAgentVersion
  /**
   ** Returns the value of the <code>agentVersion</code> property.
   **
   ** @return                    the value of the <code>agentVersion</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getAgentVersion() {
    return this.agentVersion;
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
  // Method:   setAgentEncryptionKey
  /**
   ** Sets the value of the <code>agentEncryptionKey</code> property.
   **
   ** @param  value              the value of the
   **                            <code>agentEncryptionKey</code> property.
   **                            Allowed object is {@link String}.
   */
  public void setAgentEncryptionKey(final String value) {
    this.agentEncryptionKey = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAgentEncryptionKey
  /**
   ** Returns the value of the <code>agentEncryptionKey</code> property.
   **
   ** @return                    the value of the
   **                            <code>agentEncryptionKey</code> property.
   **                            Possible object is {@link String}.
   */
  public String getAgentEncryptionKey() {
    return this.agentEncryptionKey;
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

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDebug
  /**
   ** Sets the value of the <code>debug</code> property.
   **
   ** @param  value              the value of the <code>debug</code> property.
   **                            Allowed object is {@link Boolean}.
   */
  public void setDebug(final Boolean value) {
    this.debug = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDebug
  /**
   ** Returns the value of the <code>debug</code> property.
   **
   ** @return                    the value of the <code>debug</code> property.
   **                            Possible object is {@link Boolean}.
   */
  public Boolean getDebug() {
    return this.debug;
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
  // Method:   setSessionTimeoutUnit
  /**
   ** Sets the value of the <code>sessionTimeoutUnit</code> property.
   **
   ** @param  value              the value of the
   **                            <code>sessionTimeoutUnit</code> property.
   **                            Allowed object is {@link String}.
   */
  public void setSessionTimeoutUnit(final String value) {
    this.sessionTimeoutUnit = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSessionTimeoutUnit
  /**
   ** Returns the value of the <code>sessionTimeoutUnit</code> property.
   **
   ** @return                    the value of the
   **                            <code>sessionTimeoutUnit</code> property.
   **                            Possible object is {@link String}.
   */
  public String getSessionTimeoutUnit() {
    return this.sessionTimeoutUnit;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setCookieSeparator
  /**
   ** Sets the value of the <code>cookieSeparator</code> property.
   **
   ** @param  value              the value of the <code>cookieSeparator</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setCookieSeparator(final String value) {
    this.cookieSeparator = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getCookieSeparator
  /**
   ** Returns the value of the <code>cookieSeparator</code> property.
   **
   ** @return                    the value of the <code>cookieSeparator</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getCookieSeparator() {
    return this.cookieSeparator;
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
   **                            Possible object is
   **                            {@link SessionAttributeMapping}.
   */
  public SessionAttributeMapping getSessionAttributeMapping() {
    return this.sessionAttributeMapping;
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
  // Method:   setResponseProperties
  /**
   ** Sets the value of the <code>responseProperties</code> property.
   **
   ** @param  value              the value of the
   **                            <code>responseProperties</code> property.
   **                            Allowed object is {@link ResponseProperties}.
   */
  public void setResponseProperties(final ResponseProperties value) {
    this.responseProperties = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getResponseProperties
  /**
   ** Returns the value of the <code>responseProperties</code> property.
   **
   ** @return                    the value of the
   **                            <code>responseProperties</code> property.
   **                            Possible object is {@link ResponseProperties}.
   */
  public ResponseProperties getResponseProperties() {
    return this.responseProperties;
  }
}