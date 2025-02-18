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

    File        :   Agent11gUpdate.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Agent11gUpdate.


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
// class Agent11gUpdate
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
 **         &lt;element name="type"                               type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="mode"                               type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="username"                           type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="password"                           type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="serverAddress"                      type="{http://www.w3.org/2001/XMLSchema}anyURI"/&gt;
 **         &lt;element name="agentName"                          type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element name="preferredHost"                      type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="accessClientPasswd"                 type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="modifyAccessClientPasswdFlag"       type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 **         &lt;element name="security"                           type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="state"                              type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="maxCacheElems"                      type="{http://www.w3.org/2001/XMLSchema}int"     minOccurs="0"/&gt;
 **         &lt;element name="cacheTimeout"                       type="{http://www.w3.org/2001/XMLSchema}int"     minOccurs="0"/&gt;
 **         &lt;element name="tokenValidityPeriod"                type="{http://www.w3.org/2001/XMLSchema}int"     minOccurs="0"/&gt;
 **         &lt;element name="maxConnections"                     type="{http://www.w3.org/2001/XMLSchema}int"     minOccurs="0"/&gt;
 **         &lt;element name="maxSessionTime"                     type="{http://www.w3.org/2001/XMLSchema}int"     minOccurs="0"/&gt;
 **         &lt;element name="failoverThreshold"                  type="{http://www.w3.org/2001/XMLSchema}int"     minOccurs="0"/&gt;
 **         &lt;element name="aaaTimeoutThreshold"                type="{http://www.w3.org/2001/XMLSchema}int"     minOccurs="0"/&gt;
 **         &lt;element ref="{}logOutUrls"                                                                         minOccurs="0"/&gt;
 **         &lt;element name="logoutCallbackUrl"                  type="{http://www.w3.org/2001/XMLSchema}anyURI"  minOccurs="0"/&gt;
 **         &lt;element name="logoutRedirectUrl"                  type="{http://www.w3.org/2001/XMLSchema}anyURI"  minOccurs="0"/&gt;
 **         &lt;element name="logoutTargetUrlParamName"           type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element ref="{}primaryServerList"                                                                  minOccurs="0"/&gt;
 **         &lt;element ref="{}secondaryServerList"                                                                minOccurs="0"/&gt;
 **         &lt;element ref="{}userDefinedParameters"                                                              minOccurs="0"/&gt;
 **         &lt;element name="sleepFor"                           type="{http://www.w3.org/2001/XMLSchema}int"     minOccurs="0"/&gt;
 **         &lt;element name="cachePragmaHeader"                  type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="cacheControlHeader"                 type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="debug"                              type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 **         &lt;element name="ipValidation"                       type="{http://www.w3.org/2001/XMLSchema}int"     minOccurs="0"/&gt;
 **         &lt;element ref="{}ipValidationExceptions"                                                             minOccurs="0"/&gt;
 **         &lt;element name="denyOnNotProtected"                 type="{http://www.w3.org/2001/XMLSchema}int"     minOccurs="0"/&gt;
 **         &lt;element name="allowManagementOperations"          type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 **         &lt;element name="allowTokenScopeOperations"          type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 **         &lt;element name="allowMasterTokenRetrieval"          type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 **         &lt;element name="allowCredentialCollectorOperations" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
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
@XmlRootElement(name=Agent11gUpdate.LOCAL)
@XmlType(name="", propOrder={"preferredHost", "accessClientPasswd", "modifyAccessClientPasswdFlag", "security", "state", "maxCacheElement", "cacheTimeout", "tokenValidityPeriod", "maxConnections", "maxSessionTime", "failoverThreshold", "aaaTimeoutThreshold", "logOutUrls", "logoutCallbackUrl", "logoutRedirectUrl", "logoutTargetUrlParamName", "primaryServer", "secondaryServer", "userDefinedParameters", "sleepFor", "cachePragmaHeader", "cacheControlHeader", "debug", "ipValidation", "ipValidationExceptions", "denyOnNotProtected", "allowManagementOperations", "allowTokenScopeOperations", "allowMasterTokenRetrieval", "allowCredentialCollectorOperations"})
public class Agent11gUpdate extends BaseRequest {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String LOCAL = "OAM11GUpdateAgentRegRequest";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected String                 preferredHost;
  protected String                 accessClientPasswd;
  protected Boolean                modifyAccessClientPasswdFlag;
  protected String                 security;
  protected String                 state;
  @XmlElement(name="maxCacheElems")
  protected Integer                maxCacheElement;
  protected Integer                cacheTimeout;
  protected Integer                tokenValidityPeriod;
  protected Integer                maxConnections;
  protected Integer                maxSessionTime;
  protected Integer                failoverThreshold;
  protected Integer                aaaTimeoutThreshold;
  protected LogOutUrls             logOutUrls;
  @XmlSchemaType(name = "anyURI")
  protected String                 logoutCallbackUrl;
  protected String                 logoutRedirectUrl;
  protected String                 logoutTargetUrlParamName;
  @XmlElement(name=PrimaryServerList.LOCAL)
  protected PrimaryServerList      primaryServer;
  @XmlElement(name=SecondaryServerList.LOCAL)
  protected SecondaryServerList    secondaryServer;
  protected UserDefinedParameters  userDefinedParameters;
  protected Integer                sleepFor;
  protected String                 cachePragmaHeader;
  protected String                 cacheControlHeader;
  protected Boolean                debug;
  protected Integer                ipValidation;
  protected IpValidationExceptions ipValidationExceptions;
  protected Integer                denyOnNotProtected;
  protected Boolean                allowManagementOperations;
  protected Boolean                allowTokenScopeOperations;
  protected Boolean                allowMasterTokenRetrieval;
  protected Boolean                allowCredentialCollectorOperations;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>Agent11gUpdate</code> that allows use as a
   ** JavaBean.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  public Agent11gUpdate() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Agent11gUpdate</code> with the specified properties.
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
  public Agent11gUpdate(final String mode, final String serverAddress, final String username, final String password,  final String agentName) {
    // ensure inheritance
    super(TYPE_AGENT11, mode, serverAddress, username, password,  agentName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPreferredHost
  /**
   ** Sets the value of the preferredHost property.
   **
   ** @param  value              the value of the preferredHost property.
   **                            Allowed object is {@link String}.
   */
  public void setPreferredHost(final String value) {
    this.preferredHost = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPreferredHost
  /**
   ** Returns the value of the preferredHost property.
   **
   ** @return                    the value of the preferredHost property.
   **                            Possible object is {@link String}.
   */
  public String getPreferredHost() {
    return this.preferredHost;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAccessClientPasswd
  /**
   ** Sets the value of the accessClientPasswd property.
   **
   ** @param  value              the value of the accessClientPasswd property.
   **                            Allowed object is {@link String}.
   */
  public void setAccessClientPasswd(final String value) {
    this.accessClientPasswd = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAccessClientPasswd
  /**
   ** Returns the value of the accessClientPasswd property.
   **
   ** @return                    the value of the accessClientPasswd property.
   **                            Possible object is {@link String}.
   */
  public String getAccessClientPasswd() {
    return this.accessClientPasswd;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setModifyAccessClientPasswdFlag
  /**
   ** Sets the value of the modifyAccessClientPasswdFlag property.
   **
   ** @param  value              the value of the modifyAccessClientPasswdFlag
   **                            property.
   **                            Allowed object is {@link Boolean}.
   */
  public void setModifyAccessClientPasswdFlag(final Boolean value) {
    this.modifyAccessClientPasswdFlag = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getModifyAccessClientPasswdFlag
  /**
   ** Returns the value of the modifyAccessClientPasswdFlag property.
   **
   ** @return                    the value of the modifyAccessClientPasswdFlag
   **                            property.
   **                            Possible object is {@link Boolean}.
   */
  public Boolean getModifyAccessClientPasswdFlag() {
    return this.modifyAccessClientPasswdFlag;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSecurity
  /**
   ** Sets the value of the security property.
   **
   ** @param  value              the value of the security property.
   **                            Allowed object is {@link String}.
   */
  public void setSecurity(final String value) {
    this.security = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSecurity
  /**
   ** Returns the value of the security property.
   **
   ** @return                    the value of the security property.
   **                            Possible object is {@link String}.
   */
  public String getSecurity() {
    return this.security;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setState
  /**
   ** Sets the value of the state property.
   **
   ** @param  value              the value of the state property.
   **                            Allowed object is {@link String}.
   */
  public void setState(final String value) {
    this.state = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getState
  /**
   ** Returns the value of the state property.
   **
   ** @return                    the value of the state property.
   **                            Possible object is {@link String}.
   */
  public String getState() {
    return this.state;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setMaxCacheElement
  /**
   ** Sets the value of the maxCacheElement property.
   **
   ** @param  value              the value of the maxCacheElement property.
   **                            Allowed object is {@link Integer}.
   */
  public void setMaxCacheElement(final Integer value) {
    this.maxCacheElement = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getMaxCacheElement
  /**
   ** Returns the value of the maxCacheElement property.
   **
   ** @return                    the value of the maxCacheElement property.
   **                            Possible object is {@link Integer}.
   */
  public Integer getMaxCacheElement() {
    return this.maxCacheElement;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setCacheTimeout
  /**
   ** Sets the value of the cacheTimeout property.
   **
   ** @param  value              the value of the cacheTimeout property.
   **                            Allowed object is {@link Integer}.
   */
  public void setCacheTimeout(final Integer value) {
    this.cacheTimeout = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getCacheTimeout
  /**
   ** Returns the value of the cacheTimeout property.
   **
   ** @return                    the value of the cacheTimeout property.
   **                            Possible object is {@link Integer}.
   */
  public Integer getCacheTimeout() {
    return this.cacheTimeout;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setTokenValidityPeriod
  /**
   ** Sets the value of the tokenValidityPeriod property.
   **
   ** @param  value              the value of the tokenValidityPeriod property.
   **                            Allowed object is {@link Integer}.
   */
  public void setTokenValidityPeriod(final Integer value) {
    this.tokenValidityPeriod = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getTokenValidityPeriod
  /**
   ** Returns the value of the tokenValidityPeriod property.
   **
   ** @return                    the value of the tokenValidityPeriod property.
   **                            Possible object is {@link Integer}.
   */
  public Integer getTokenValidityPeriod() {
    return this.tokenValidityPeriod;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setMaxConnections
  /**
   ** Sets the value of the maxConnections property.
   **
   ** @param  value              the value of the cookieSessionTime property.
   **                            Allowed object is {@link Integer}.
   */
  public void setMaxConnections(final Integer value) {
    this.maxConnections = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getMaxConnections
  /**
   ** Returns the value of the maxConnections property.
   **
   ** @return                    the value of the cookieSessionTime property.
   **                            Possible object is {@link Integer}.
   */
  public Integer getMaxConnections() {
    return this.maxConnections;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setMaxSessionTime
  /**
   ** Sets the value of the maxSessionTime property.
   **
   ** @param  value              the value of the maxSessionTime property.
   **                            Allowed object is {@link Integer}.
   */
  public void setMaxSessionTime(final Integer value) {
    this.maxSessionTime = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getMaxSessionTime
  /**
   ** Returns the value of the maxSessionTime property.
   **
   ** @return                    the value of the maxSessionTime property.
   **                            Possible object is {@link Integer}.
   */
  public Integer getMaxSessionTime() {
    return this.maxSessionTime;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setFailoverThreshold
  /**
   ** Sets the value of the failoverThreshold property.
   **
   ** @param  value              the value of the failoverThreshold property.
   **                            Allowed object is {@link Integer}.
   */
  public void setFailoverThreshold(final Integer value) {
    this.failoverThreshold = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getFailoverThreshold
  /**
   ** Returns the value of the failoverThreshold property.
   **
   ** @return                    the value of the failoverThreshold property.
   **                            Possible object is {@link Integer}.
   */
  public Integer getFailoverThreshold() {
    return this.failoverThreshold;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAaaTimeoutThreshold
  /**
   ** Sets the value of the aaaTimeoutThreshold property.
   **
   ** @param  value              the value of the aaaTimeoutThreshold property.
   **                            Allowed object is {@link Integer}.
   */
  public void setAaaTimeoutThreshold(final Integer value) {
    this.aaaTimeoutThreshold = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAaaTimeoutThreshold
  /**
   ** Returns the value of the aaaTimeoutThreshold property.
   **
   ** @return                    the value of the aaaTimeoutThreshold property.
   **                            Possible object is {@link Integer}.
   */
  public Integer getAaaTimeoutThreshold() {
    return this.aaaTimeoutThreshold;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPrimaryServer
  /**
   ** Sets the value of the <code>primaryServer</code> property.
   ** <p>
   ** Identifies Primary Server details for an <code>Access Agent</code>. The
   ** default is based on the <code>Access Server</code>:
   ** <ul>
   **   <li>Server Name
   **   <li>Host Name
   **   <li>Host Port
   **   <li>Max Number (maximum connections the <code>Access Agent</code> will
   **       establish with the <code>Access Server</code> (not the maximum total
   **       connections the <code>Access Agent</code> can establish with all
   **       <code>Access Srver</code>s).)
   ** </ul>
   **
   ** @param  value              the value of the <code>primaryServer</code>
   **                            property.
   **                            Allowed object is {@link PrimaryServerList}.
   */
  public void setPrimaryServer(final PrimaryServerList value) {
    this.primaryServer = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPrimaryServer
  /**
   ** Returns the value of the <code>primaryServer</code> property.
   ** <p>
   ** Identifies Primary Server details for an <code>Access Agent</code>. The
   ** default is based on the <code>Access Server</code>:
   ** <ul>
   **   <li>Server Name
   **   <li>Host Name
   **   <li>Host Port
   **   <li>Max Number (maximum connections the <code>Access Agent</code> will
   **       establish with the <code>Access Server</code> (not the maximum total
   **       connections the <code>Access Agent</code> can establish with all
   **       <code>Access Srver</code>s).)
   ** </ul>
   **
   ** @return                    the value of the <code>primaryServer</code>
   **                            property.
   **                            Possible object is {@link PrimaryServerList}.
   */
  public PrimaryServerList getPrimaryServer() {
    return this.primaryServer;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSecondaryServer
  /**
   ** Sets the value of the <code>secondaryServer</code> property.
   ** <p>
   ** Identifies Secondary Server details for an <code>Access Agent</code>. The
   ** default is based on the <code>Access Server</code>:
   ** <ul>
   **   <li>Server Name
   **   <li>Host Name
   **   <li>Host Port
   **   <li>Max Number (maximum connections the <code>Access Agent</code> will
   **       establish with the <code>Access Server</code> (not the maximum total
   **       connections the <code>Access Agent</code> can establish with all
   **       <code>Access Srver</code>s).)
   ** </ul>
   **
   ** @param  value              the value of the <code>secondaryServer</code>
   **                            property.
   **                            Allowed object is {@link SecondaryServerList}.
   */
  public void setSecondaryServer(final SecondaryServerList value) {
    this.secondaryServer = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSecondaryServer
  /**
   ** Returns the value of the <code>secondaryServer</code> property.
   ** <p>
   ** Identifies Secondary Server details for an <code>Access Agent</code>. The
   ** default is based on the O<code>Access Server</code>:
   ** <ul>
   **   <li>Server Name
   **   <li>Host Name
   **   <li>Host Port
   **   <li>Max Number (maximum connections the <code>Access Agent</code> will
   **       establish with the <code>Access Server</code> (not the maximum total
   **       connections the <code>Access Agent</code> can establish with all
   **       <code>Access Srver</code>s).)
   ** </ul>
   **
   ** @return                    the value of the <code>secondaryServer</code>
   **                            property.
   **                            Possible object is {@link SecondaryServerList}.
   */
  public SecondaryServerList getSecondaryServer() {
    return this.secondaryServer;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setUserDefinedParameters
  /**
   ** Sets the value of the userDefinedParameters property.
   **
   ** @param  value              the value of the userDefinedParameters property.
   **                            Allowed object is {@link UserDefinedParameters}.
   */
  public void setUserDefinedParameters(final UserDefinedParameters value) {
    this.userDefinedParameters = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setLogOutUrls
  /**
   ** Sets the value of the logOutUrls property.
   **
   ** @param  value              the value of the logOutUrls property.
   **                            Allowed object is {@link LogOutUrls}.
   */
  public void setLogOutUrls(final LogOutUrls value) {
    this.logOutUrls = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLogOutUrls
  /**
   ** Returns the value of the logOutUrls property.
   **
   ** @return                    the value of the logOutUrls property.
   **                            Possible object is {@link LogOutUrls}.
   */
  public LogOutUrls getLogOutUrls() {
    return this.logOutUrls;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setLogoutCallbackUrl
  /**
   ** Sets the value of the logoutCallbackUrl property.
   **
   ** @param  value              the value of the logoutCallbackUrl property.
   **                            Allowed object is {@link String}.
   */
  public void setLogoutCallbackUrl(final String value) {
    this.logoutCallbackUrl = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLogoutCallbackUrl
  /**
   ** Returns the value of the logoutCallbackUrl property.
   **
   ** @return                    the value of the logoutCallbackUrl property.
   **                            Possible object is {@link String}.
   */
  public String getLogoutCallbackUrl() {
    return this.logoutCallbackUrl;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setLogoutRedirectUrl
  /**
   ** Sets the value of the logoutRedirectUrl property.
   **
   ** @param  value              the value of the logoutRedirectUrl property.
   **                            Allowed object is {@link String}.
   */
  public void setLogoutRedirectUrl(final String value) {
    this.logoutRedirectUrl = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLogoutRedirectUrl
  /**
   ** Returns the value of the logoutRedirectUrl property.
   **
   ** @return                    the value of the logoutRedirectUrl property.
   **                            Possible object is {@link String}.
   */
  public String getLogoutRedirectUrl() {
    return this.logoutRedirectUrl;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setLogoutTargetUrlParamName
  /**
   ** Sets the value of the logoutTargetUrlParamName property.
   **
   ** @param  value              the value of the logoutTargetUrlParamName property.
   **                            Allowed object is {@link String}.
   */
  public void setLogoutTargetUrlParamName(final String value) {
    this.logoutTargetUrlParamName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLogoutTargetUrlParamName
  /**
   ** Returns the value of the logoutTargetUrlParamName property.
   **
   ** @return                    the value of the logoutTargetUrlParamName property.
   **                            Possible object is {@link String}.
   */
  public String getLogoutTargetUrlParamName() {
    return this.logoutTargetUrlParamName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSleepFor
  /**
   ** Sets the value of the sleepFor property.
   **
   ** @param  value              the value of the sleepFor property.
   **                            Allowed object is {@link Integer}.
   */
  public void setSleepFor(final Integer value) {
    this.sleepFor = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSleepFor
  /**
   ** Returns the value of the sleepFor property.
   **
   ** @return                    the value of the sleepFor property.
   **                            Possible object is {@link Integer}.
   */
  public Integer getSleepFor() {
    return this.sleepFor;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDebug
  /**
   ** Sets the value of the debug property.
   **
   ** @param  value              the value of the debug property.
   **                            Allowed object is {@link Boolean}.
   */
  public void setDebug(final Boolean value) {
    this.debug = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDebug
  /**
   ** Returns the value of the debug property.
   **
   ** @return                    the value of the debug property.
   **                            Possible object is {@link Boolean}.
   */
  public Boolean getDebug() {
    return this.debug;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setCachePragmaHeader
  /**
   ** Sets the value of the cachePragmaHeader property.
   **
   ** @param  value              the value of the cachePragmaHeader property.
   **                            Allowed object is {@link String}.
   */
  public void setCachePragmaHeader(final String value) {
    this.cachePragmaHeader = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getCachePragmaHeader
  /**
   ** Returns the value of the cachePragmaHeader property.
   **
   ** @return                    the value of the cachePragmaHeader property.
   **                            Possible object is {@link String}.
   */
  public String getCachePragmaHeader() {
    return this.cachePragmaHeader;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setCacheControlHeader
  /**
   ** Sets the value of the cacheControlHeader property.
   **
   ** @param  value              the value of the cacheControlHeader property.
   **                            Allowed object is {@link String}.
   */
  public void setCacheControlHeader(final String value) {
    this.cacheControlHeader = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getCacheControlHeader
  /**
   ** Returns the value of the cacheControlHeader property.
   **
   ** @return                    the value of the cacheControlHeader property.
   **                            Possible object is {@link String}.
   */
  public String getCacheControlHeader() {
    return this.cacheControlHeader;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setIpValidation
  /**
   ** Sets the value of the ipValidation property.
   **
   ** @param  value              the value of the ipValidation property.
   **                            Allowed object is {@link Integer}.
   */
  public void setIpValidation(final Integer value) {
    this.ipValidation = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getIpValidation
  /**
   ** Returns the value of the ipValidation property.
   **
   ** @return                    the value of the ipValidation property.
   **                            Possible object is {@link Integer}.
   */
  public Integer getIpValidation() {
    return this.ipValidation;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setIpValidationExceptions
  /**
   ** Sets the value of the ipValidationExceptions property.
   **
   ** @param  value              the value of the ipValidationExceptions property.
   **                            Allowed object is {@link IpValidationExceptions}.
   */
  public void setIpValidationExceptions(final IpValidationExceptions value) {
    this.ipValidationExceptions = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getIpValidationExceptions
  /**
   ** Returns the value of the ipValidationExceptions property.
   **
   ** @return                    the value of the ipValidationExceptions property.
   **                            Possible object is {@link IpValidationExceptions}.
   */
  public IpValidationExceptions getIpValidationExceptions() {
    return this.ipValidationExceptions;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDenyOnNotProtected
  /**
   ** Sets the value of the denyOnNotProtected property.
   **
   ** @param  value              the value of the denyOnNotProtected property.
   **                            Allowed object is {@link Integer}.
   */
  public void setDenyOnNotProtected(final Integer value) {
    this.denyOnNotProtected = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDenyOnNotProtected
  /**
   ** Returns the value of the denyOnNotProtected property.
   **
   ** @return                    the value of the denyOnNotProtected property.
   **                            Possible object is {@link Integer}.
   */
  public Integer getDenyOnNotProtected() {
    return this.denyOnNotProtected;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAllowManagementOperations
  /**
   ** Sets the value of the allowManagementOperations property.
   **
   ** @param  value              the value of the allowManagementOperations property.
   **                            Allowed object is {@link Boolean}.
   */
  public void setAllowManagementOperations(final Boolean value) {
    this.allowManagementOperations = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAllowManagementOperations
  /**
   ** Returns the value of the allowManagementOperations property.
   **
   ** @return                    the value of the allowManagementOperations property.
   **                            Possible object is {@link Boolean}.
   */
  public Boolean getAllowManagementOperations() {
    return this.allowManagementOperations;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAllowTokenScopeOperations
  /**
   ** Sets the value of the allowTokenScopeOperations property.
   **
   ** @param  value              the value of the allowTokenScopeOperations property.
   **                            Allowed object is {@link Boolean}.
   */
  public void setAllowTokenScopeOperations(final Boolean value) {
    this.allowTokenScopeOperations = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAllowTokenScopeOperations
  /**
   ** Returns the value of the allowTokenScopeOperations property.
   **
   ** @return                    the value of the allowTokenScopeOperations property.
   **                            Possible object is {@link Boolean}.
   */
  public Boolean getAllowTokenScopeOperations() {
    return this.allowTokenScopeOperations;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAllowMasterTokenRetrieval
  /**
   ** Sets the value of the allowMasterTokenRetrieval property.
   **
   ** @param  value              the value of the allowMasterTokenRetrieval property.
   **                            Allowed object is {@link Boolean}.
   */
  public void setAllowMasterTokenRetrieval(final Boolean value) {
    this.allowMasterTokenRetrieval = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAllowMasterTokenRetrieval
  /**
   ** Returns the value of the allowMasterTokenRetrieval property.
   **
   ** @return                    the value of the allowMasterTokenRetrieval property.
   **                            Possible object is {@link Boolean}.
   */
  public Boolean getAllowMasterTokenRetrieval() {
    return this.allowMasterTokenRetrieval;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAllowCredentialCollectorOperations
  /**
   ** Sets the value of the allowCredentialCollectorOperations property.
   **
   ** @param  value              the value of the allowCredentialCollectorOperations property.
   **                            Allowed object is {@link Boolean}.
   */
  public void setAllowCredentialCollectorOperations(final Boolean value) {
    this.allowCredentialCollectorOperations = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAllowCredentialCollectorOperations
  /**
   ** Returns the value of the allowCredentialCollectorOperations property.
   **
   ** @return                    the value of the allowCredentialCollectorOperations property.
   **                            Possible object is {@link Boolean}.
   */
  public Boolean getAllowCredentialCollectorOperations() {
    return this.allowCredentialCollectorOperations;
  }
}