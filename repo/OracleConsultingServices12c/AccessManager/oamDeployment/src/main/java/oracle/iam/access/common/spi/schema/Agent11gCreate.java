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

    File        :   Agent11gCreate.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Agent11gCreate.


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
// class Agent11gCreate
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
 **         &lt;element name="hostIdentifier"                     type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="agentName"                          type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element name="agentBaseUrl"                       type="{http://www.w3.org/2001/XMLSchema}anyURI"  minOccurs="0"/&gt;
 **         &lt;element name="virtualHost"                        type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 **         &lt;element ref="{}hostPortVariationsList"                                                             minOccurs="0"/&gt;
 **         &lt;element name="applicationDomain"                  type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="autoCreatePolicy"                   type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 **         &lt;element name="accessClientPasswd"                 type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="preferredHost"                      type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="maxCacheElems"                      type="{http://www.w3.org/2001/XMLSchema}int"     minOccurs="0"/&gt;
 **         &lt;element name="cacheTimeout"                       type="{http://www.w3.org/2001/XMLSchema}int"     minOccurs="0"/&gt;
 **         &lt;element name="tokenValidityPeriod"                type="{http://www.w3.org/2001/XMLSchema}int"     minOccurs="0"/&gt;
 **         &lt;element name="maxConnections"                     type="{http://www.w3.org/2001/XMLSchema}int"     minOccurs="0"/&gt;
 **         &lt;element name="maxSessionTime"                     type="{http://www.w3.org/2001/XMLSchema}int"     minOccurs="0"/&gt;
 **         &lt;element name="failoverThreshold"                  type="{http://www.w3.org/2001/XMLSchema}int"     minOccurs="0"/&gt;
 **         &lt;element name="aaaTimeoutThreshold"                type="{http://www.w3.org/2001/XMLSchema}int"     minOccurs="0"/&gt;
 **         &lt;element name="sleepFor"                           type="{http://www.w3.org/2001/XMLSchema}int"     minOccurs="0"/&gt;
 **         &lt;element name="debug"                              type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 **         &lt;element name="security"                           type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="denyOnNotProtected"                 type="{http://www.w3.org/2001/XMLSchema}int"     minOccurs="0"/&gt;
 **         &lt;element name="allowManagementOperations"          type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 **         &lt;element name="allowTokenScopeOperations"          type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 **         &lt;element name="allowMasterTokenRetrieval"          type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 **         &lt;element name="allowCredentialCollectorOperations" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 **         &lt;element name="cachePragmaHeader"                  type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="cacheControlHeader"                 type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="ipValidation"                       type="{http://www.w3.org/2001/XMLSchema}int"     minOccurs="0"/&gt;
 **         &lt;element ref="{}ipValidationExceptions"                                                             minOccurs="0"/&gt;
 **         &lt;element ref="{}logOutUrls"                                                                         minOccurs="0"/&gt;
 **         &lt;element name="logoutCallbackUrl"                  type="{http://www.w3.org/2001/XMLSchema}anyURI"  minOccurs="0"/&gt;
 **         &lt;element name="logoutTargetUrlParamName"           type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element ref="{}primaryServerList"                                                                  minOccurs="0"/&gt;
 **         &lt;element ref="{}secondaryServerList"                                                                minOccurs="0"/&gt;
 **         &lt;element ref="{}protectedResourcesList"                                                             minOccurs="0"/&gt;
 **         &lt;element ref="{}publicResourcesList"                                                                minOccurs="0"/&gt;
 **         &lt;element ref="{}excludedResourcesList"                                                              minOccurs="0"/&gt;
 **         &lt;element name="protectedAuthnScheme"               type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="publicAuthnScheme"                  type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element ref="{}userDefinedParameters"                                                              minOccurs="0"/&gt;
 **         &lt;element ref="{}rregApplicationDomain"                                                              minOccurs="0"/&gt;
 **         &lt;element name="isFusionAppRegistration"            type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
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
@XmlRootElement(name=Agent11gCreate.LOCAL)
@XmlType(name="", propOrder={"agentBaseUrl", "virtualHost", "hostPortVariationsList", "applicationDomain", "autoCreatePolicy", "accessClientPasswd", "preferredHost", "maxCacheElement", "cacheTimeout", "tokenValidityPeriod", "maxConnections", "maxSessionTime", "failoverThreshold", "aaaTimeoutThreshold", "sleepFor", "debug", "security", "denyOnNotProtected", "allowManagementOperations", "allowTokenScopeOperations", "allowMasterTokenRetrieval", "allowCredentialCollectorOperations", "cachePragmaHeader", "cacheControlHeader", "ipValidation", "ipValidationExceptions", "logOutUrls", "logoutCallbackUrl", "logoutTargetUrlParamName", "primaryServer", "secondaryServer", "protectedResource", "publicResource", "excludedResource", "protectedAuthnScheme", "publicAuthnScheme", "userDefinedParameters", "rregApplicationDomain", "isFusionAppRegistration"})
public class Agent11gCreate extends BaseRequest {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String LOCAL = "OAM11GRegRequest";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected String                 agentBaseUrl;
  protected Boolean                virtualHost;
  protected HostPortVariationsList hostPortVariationsList;
  protected String                 applicationDomain;
  protected Boolean                autoCreatePolicy;
  protected String                 accessClientPasswd;
  protected String                 preferredHost;
  @XmlElement(name="maxCacheElems")
  protected Integer                maxCacheElement;
  protected Integer                cacheTimeout;
  protected Integer                tokenValidityPeriod;
  protected Integer                maxConnections;
  protected Integer                maxSessionTime;
  protected Integer                failoverThreshold;
  protected Integer                aaaTimeoutThreshold;
  protected Integer                sleepFor;
  protected Boolean                debug;
  protected String                 security;
  protected Integer                denyOnNotProtected;
  protected Boolean                allowManagementOperations;
  protected Boolean                allowTokenScopeOperations;
  protected Boolean                allowMasterTokenRetrieval;
  protected Boolean                allowCredentialCollectorOperations;
  protected String                 cachePragmaHeader;
  protected String                 cacheControlHeader;
  protected Integer                ipValidation;
  protected IpValidationExceptions ipValidationExceptions;
  protected LogOutUrls             logOutUrls;
  @XmlSchemaType(name = "anyURI")
  protected String                 logoutCallbackUrl;
  protected String                 logoutTargetUrlParamName;
  @XmlElement(name=PrimaryServerList.LOCAL)
  protected PrimaryServerList      primaryServer;
  @XmlElement(name=SecondaryServerList.LOCAL)
  protected SecondaryServerList    secondaryServer;
  @XmlElement(name=ProtectedResourcesList.LOCAL)
  protected ProtectedResourcesList protectedResource;
  @XmlElement(name=PublicResourcesList.LOCAL)
  protected PublicResourcesList    publicResource;
  @XmlElement(name=ExcludedResourcesList.LOCAL)
  protected ExcludedResourcesList  excludedResource;
  protected String                 protectedAuthnScheme;
  protected String                 publicAuthnScheme;
  protected UserDefinedParameters  userDefinedParameters;
  protected RregApplicationDomain  rregApplicationDomain;
  protected Boolean                isFusionAppRegistration;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>Agent11gCreate</code> that allows use as a
   ** JavaBean.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  public Agent11gCreate() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Agent11gCreate</code> with the specified properties.
   **
   ** @param  mode               the operational mode to apply.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  serverAddress      the initial serverAddress of the request.
   **                            Allowed object is {@link String}.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  username           the initial username of the request to
   **                            authenticate on the remote side.
   **                            Allowed object is {@link String}.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  password           the initial password of the request to
   **                            authenticate on the remote side.
   **                            Allowed object is {@link String}.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  agentName          the initial agentName of the agent.
   **                            Allowed object is {@link String}.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public Agent11gCreate(final String mode, final String serverAddress, final String username, final String password,  final String agentName) {
    // ensure inheritance
    super(TYPE_AGENT11, mode, serverAddress, username, password,  agentName);
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
   **                            <br>
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
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getAgentBaseUrl() {
    return this.agentBaseUrl;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setVirtualHost
  /**
   ** Sets the value of the <code>virtualHost</code> property.
   **
   ** @param  value              the value of the <code>virtualHost</code>
   **                            property.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   */
  public void setVirtualHost(final Boolean value) {
    this.virtualHost = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getVirtualHost
  /**
   ** Returns the value of the <code>virtualHost</code> property.
   **
   ** @return                    the value of the <code>virtualHost</code>
   **                            property.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public Boolean getVirtualHost() {
    return this.virtualHost;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setHostPortVariationsList
  /**
   ** Sets the value of the <code>hostPortVariationsList</code> property.
   **
   ** @param  value              the value of the
   **                            <code>hostPortVariationsList</code> property.
   **                            <br>
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
   **                            <br>
   **                            Possible object is
   **                            {@link HostPortVariationsList}.
   */
  public HostPortVariationsList getHostPortVariationsList() {
    return this.hostPortVariationsList;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setApplicationDomain
  /**
   ** Sets the value of the <code>applicationDomain</code> property.
   **
   ** @param  value              the value of the <code>applicationDomain</code>
   **                            property.
   **                            <br>
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
   **                            <br>
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
   **                            <br>
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
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public Boolean getAutoCreatePolicy() {
    return this.autoCreatePolicy;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAccessClientPasswd
  /**
   ** Sets the value of the <code>accessClientPasswd</code> property.
   **
   ** @param  value              the value of the
   **                            <code>accessClientPasswd</code> property.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setAccessClientPasswd(final String value) {
    this.accessClientPasswd = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAccessClientPasswd
  /**
   ** Returns the value of the <code>accessClientPasswd</code> property.
   **
   ** @return                    the value of the
   **                            <code>accessClientPasswd</code> property.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getAccessClientPasswd() {
    return this.accessClientPasswd;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPreferredHost
  /**
   ** Sets the value of the <code>preferredHost</code> property.
   **
   ** @param  value              the value of the <code>preferredHost</code>
   **                            property.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setPreferredHost(final String value) {
    this.preferredHost = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPreferredHost
  /**
   ** Returns the value of the <code>preferredHost</code> property.
   **
   ** @return                    the value of the <code>preferredHost</code>
   **                            property.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getPreferredHost() {
    return this.preferredHost;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setMaxCacheElement
  /**
   ** Sets the value of the <code>maxCacheElement</code> property.
   **
   ** @param  value              the value of the <code>maxCacheElement</code>
   **                            property.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   */
  public void setMaxCacheElement(final Integer value) {
    this.maxCacheElement = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getMaxCacheElement
  /**
   ** Returns the value of the <code>maxCacheElement</code> property.
   **
   ** @return                    the value of the <code>maxCacheElement</code>
   **                            property.
   **                            <br>
   **                            Possible object is {@link Integer}.
   */
  public Integer getMaxCacheElement() {
    return this.maxCacheElement;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setCacheTimeout
  /**
   ** Sets the value of the <code>cacheTimeout</code> property.
   **
   ** @param  value              the value of the <code>cacheTimeout</code>
   **                            property.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   */
  public void setCacheTimeout(final Integer value) {
    this.cacheTimeout = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getCacheTimeout
  /**
   ** Returns the value of the <code>cacheTimeout</code> property.
   **
   ** @return                    the value of the <code>cacheTimeout</code>
   **                            property.
   **                            <br>
   **                            Possible object is {@link Integer}.
   */
  public Integer getCacheTimeout() {
    return this.cacheTimeout;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setTokenValidityPeriod
  /**
   ** Sets the value of the <code>tokenValidityPeriod</code> property.
   **
   ** @param  value              the value of the
   **                            <code>tokenValidityPeriod</code> property.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   */
  public void setTokenValidityPeriod(final Integer value) {
    this.tokenValidityPeriod = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getTokenValidityPeriod
  /**
   ** Returns the value of the <code>tokenValidityPeriod</code> property.
   **
   ** @return                    the value of the
   **                            <code>tokenValidityPeriod</code> property.
   **                            <br>
   **                            Possible object is {@link Integer}.
   */
  public Integer getTokenValidityPeriod() {
    return this.tokenValidityPeriod;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setMaxConnections
  /**
   ** Sets the value of the <code>maxConnections</code> property.
   **
   ** @param  value              the value of the <code>maxConnections</code>
   **                            property.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   */
  public void setMaxConnections(final Integer value) {
    this.maxConnections = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getMaxConnections
  /**
   ** Returns the value of the <code>maxConnections</code> property.
   **
   ** @return                    the value of the <code>maxConnections</code>
   **                            property.
   **                            <br>
   **                            Possible object is {@link Integer}.
   */
  public Integer getMaxConnections() {
    return this.maxConnections;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setMaxSessionTime
  /**
   ** Sets the value of the <code>maxSessionTime</code> property.
   **
   ** @param  value              the value of the <code>maxSessionTime</code>
   **                            property.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   */
  public void setMaxSessionTime(final Integer value) {
    this.maxSessionTime = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getMaxSessionTime
  /**
   ** Returns the value of the <code>maxSessionTime</code> property.
   **
   ** @return                    the value of the <code>maxSessionTime</code>
   **                            property.
   **                            <br>
   **                            Possible object is {@link Integer}.
   */
  public Integer getMaxSessionTime() {
    return this.maxSessionTime;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setFailoverThreshold
  /**
   ** Sets the value of the <code>failoverThreshold</code> property.
   **
   ** @param  value              the value of the <code>failoverThreshold</code>
   **                            property.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   */
  public void setFailoverThreshold(final Integer value) {
    this.failoverThreshold = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getFailoverThreshold
  /**
   ** Returns the value of the <code>failoverThreshold</code> property.
   **
   ** @return                    the value of the <code>failoverThreshold</code>
   **                            property.
   **                            <br>
   **                            Possible object is {@link Integer}.
   */
  public Integer getFailoverThreshold() {
    return this.failoverThreshold;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAaaTimeoutThreshold
  /**
   ** Sets the value of the <code>aaaTimeoutThreshold</code> property.
   **
   ** @param  value              the value of the
   **                            <code>aaaTimeoutThreshold</code> property.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   */
  public void setAaaTimeoutThreshold(final Integer value) {
    this.aaaTimeoutThreshold = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAaaTimeoutThreshold
  /**
   ** Returns the value of the <code>aaaTimeoutThreshold</code> property.
   **
   ** @return                    the value of the
   **                            <code>aaaTimeoutThreshold</code> property.
   **                            <br>
   **                            Possible object is {@link Integer}.
   */
  public Integer getAaaTimeoutThreshold() {
    return this.aaaTimeoutThreshold;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSleepFor
  /**
   ** Sets the value of the <code>sleepFor</code> property.
   **
   ** @param  value              the value of the <code>sleepFor</code>
   **                            property.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   */
  public void setSleepFor(final Integer value) {
    this.sleepFor = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSleepFor
  /**
   ** Returns the value of the <code>sleepFor</code> property.
   **
   ** @return                    the value of the <code>sleepFor</code>
   **                            property.
   **                            <br>
   **                            Possible object is {@link Integer}.
   */
  public Integer getSleepFor() {
    return this.sleepFor;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDebug
  /**
   ** Sets the value of the <code>debug</code> property.
   **
   ** @param  value              the value of the <code>debug</code> property.
   **                            <br>
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
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public Boolean getDebug() {
    return this.debug;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSecurity
  /**
   ** Sets the value of the <code>security</code> property.
   **
   ** @param  value              the value of the <code>security</code>
   **                            property.
   **                            <br>
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
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getSecurity() {
    return this.security;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDenyOnNotProtected
  /**
   ** Sets the value of the <code>denyOnNotProtected</code> property.
   **
   ** @param  value              the value of the
   **                            <code>denyOnNotProtected</code> property.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   */
  public void setDenyOnNotProtected(final Integer value) {
    this.denyOnNotProtected = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDenyOnNotProtected
  /**
   ** Returns the value of the <code>denyOnNotProtected</code> property.
   **
   ** @return                    the value of the
   **                            <code>denyOnNotProtected</code> property.
   **                            <br>
   **                            Possible object is {@link Integer}.
   */
  public Integer getDenyOnNotProtected() {
    return this.denyOnNotProtected;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAllowManagementOperations
  /**
   ** Sets the value of the <code>denyOnNotProtected</code> property.
   **
   ** @param  value              the value of the
   **                            <code>denyOnNotProtected</code> property.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   */
  public void setAllowManagementOperations(final Boolean value) {
    this.allowManagementOperations = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAllowManagementOperations
  /**
   ** Returns the value of the <code>denyOnNotProtected</code> property.
   **
   ** @return                    the value of the
   **                            <code>denyOnNotProtected</code> property.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public Boolean getAllowManagementOperations() {
    return this.allowManagementOperations;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAllowTokenScopeOperations
  /**
   ** Sets the value of the <code>allowTokenScopeOperations</code> property.
   **
   ** @param  value              the value of the
   **                            <code>allowTokenScopeOperations</code>
   **                            property.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   */
  public void setAllowTokenScopeOperations(final Boolean value) {
    this.allowTokenScopeOperations = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAllowTokenScopeOperations
  /**
   ** Returns the value of the <code>allowTokenScopeOperations</code> property.
   **
   ** @return                    the value of the
   **                            <code>allowTokenScopeOperations</code>
   **                            property.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public Boolean getAllowTokenScopeOperations() {
    return this.allowTokenScopeOperations;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAllowMasterTokenRetrieval
  /**
   ** Sets the value of the <code>allowMasterTokenRetrieval</code> property.
   **
   ** @param  value              the value of the
   **                            <code>allowMasterTokenRetrieval</code>
   **                            property.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   */
  public void setAllowMasterTokenRetrieval(final Boolean value) {
    this.allowMasterTokenRetrieval = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAllowMasterTokenRetrieval
  /**
   ** Returns the value of the <code>allowMasterTokenRetrieval</code> property.
   **
   ** @return                    the value of the
   **                            <code>allowMasterTokenRetrieval</code>
   **                            property.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public Boolean getAllowMasterTokenRetrieval() {
    return this.allowMasterTokenRetrieval;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAllowCredentialCollectorOperations
  /**
   ** Sets the value of the <code>allowCredentialCollectorOperations</code>
   ** property.
   **
   ** @param  value              the value of the
   **                            <code>allowCredentialCollectorOperations</code>
   **                            property.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   */
  public void setAllowCredentialCollectorOperations(final Boolean value) {
    this.allowCredentialCollectorOperations = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAllowCredentialCollectorOperations
  /**
   ** Returns the value of the <code>allowCredentialCollectorOperations</code>
   ** property.
   **
   ** @return                    the value of the
   **                            <code>allowCredentialCollectorOperations</code>
   **                            property.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public Boolean getAllowCredentialCollectorOperations() {
    return this.allowCredentialCollectorOperations;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setCachePragmaHeader
  /**
   ** Sets the value of the <code>cachePragmaHeader</code> property.
   **
   ** @param  value              the value of the <code>cachePragmaHeader</code>
   **                            property.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setCachePragmaHeader(final String value) {
    this.cachePragmaHeader = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getCachePragmaHeader
  /**
   ** Returns the value of the <code>cachePragmaHeader</code> property.
   **
   ** @return                    the value of the <code>cachePragmaHeader</code>
   **                            property.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getCachePragmaHeader() {
    return this.cachePragmaHeader;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setCacheControlHeader
  /**
   ** Sets the value of the <code>cacheControlHeader</code> property.
   **
   ** @param  value              the value of the
   **                            <code>cacheControlHeader</code> property.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setCacheControlHeader(final String value) {
    this.cacheControlHeader = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getCacheControlHeader
  /**
   ** Returns the value of the <code>cacheControlHeader</code> property.
   **
   ** @return                    the value of the
   **                            <code>cacheControlHeader</code> property.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getCacheControlHeader() {
    return this.cacheControlHeader;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setIpValidation
  /**
   ** Sets the value of the <code>ipValidation</code> property.
   **
   ** @param  value              the value of the <code>ipValidation</code>
   **                            property.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   */
  public void setIpValidation(final Integer value) {
    this.ipValidation = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getIpValidation
  /**
   ** Returns the value of the <code>ipValidation</code> property.
   **
   ** @return                    the value of the <code>ipValidation</code>
   **                            property.
   **                            <br>
   **                            Possible object is {@link Integer}.
   */
  public Integer getIpValidation() {
    return this.ipValidation;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setIpValidationExceptions
  /**
   ** Sets the value of the <code>ipValidationExceptions</code> property.
   **
   ** @param  value              the value of the
   **                            <code>ipValidationExceptions</code> property.
   **                            <br>
   **                            Allowed object is
   **                            {@link IpValidationExceptions}.
   */
  public void setIpValidationExceptions(final IpValidationExceptions value) {
    this.ipValidationExceptions = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getIpValidationExceptions
  /**
   ** Returns the value of the <code>ipValidationExceptions</code> property.
   **
   ** @return                    the value of the
   **                            <code>ipValidationExceptions</code> property.
   **                            <br>
   **                            Possible object is
   **                            {@link IpValidationExceptions}.
   */
  public IpValidationExceptions getIpValidationExceptions() {
    return this.ipValidationExceptions;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setLogOutUrls
  /**
   ** Sets the value of the <code>logOutUrls</code> property.
   **
   ** @param  value              the value of the <code>logOutUrls</code>
   **                            property.
   **                            <br>
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
   **                            <br>
   **                            Possible object is {@link LogOutUrls}.
   */
  public LogOutUrls getLogOutUrls() {
    return this.logOutUrls;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setLogoutCallbackUrl
  /**
   ** Sets the value of the <code>logoutCallbackUrl</code> property.
   **
   ** @param  value              the value of the <code>logoutCallbackUrl</code>
   **                            property.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setLogoutCallbackUrl(final String value) {
    this.logoutCallbackUrl = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLogoutCallbackUrl
  /**
   ** Returns the value of the <code>logoutCallbackUrl</code> property.
   **
   ** @return                    the value of the <code>logoutCallbackUrl</code>
   **                            property.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getLogoutCallbackUrl() {
    return this.logoutCallbackUrl;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setLogoutTargetUrlParamName
  /**
   ** Sets the value of the <code>logoutTargetUrlParamName</code> property.
   **
   ** @param  value              the value of the
   **                            <code>logoutTargetUrlParamName</code> property.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setLogoutTargetUrlParamName(final String value) {
    this.logoutTargetUrlParamName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLogoutTargetUrlParamName
  /**
   ** Returns the value of the <code>logoutTargetUrlParamName</code> property.
   **
   ** @return                    the value of the
   **                            <code>logoutTargetUrlParamName</code> property.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getLogoutTargetUrlParamName() {
    return this.logoutTargetUrlParamName;
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
   **                            <br>
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
   **                            <br>
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
   **                            <br>
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
   **                            <br>
   **                            Possible object is {@link SecondaryServerList}.
   */
  public SecondaryServerList getSecondaryServer() {
    return this.secondaryServer;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setProtectedResource
  /**
   ** Sets the value of the <code>protectedResource</code> property.
   ** <p>
   ** URIs for the protected application: /myapp/login, for example must
   ** be included in an authentication policy that uses an authentication scheme
   ** with a protection level greater than <code>0</code>.
   ** <br>
   ** Protected resources can be associated with any authorization policy..
   ** <p>
   ** Default: /**
   ** <p>
   ** The default matches any sequence of characters within zero or more
   ** intermediate levels spanning multiple directories.
   **
   ** @param  value              the value of the <code>protectedResource</code>
   **                            property.
   **                            <br>
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
   ** <p>
   ** URIs for the protected application: /myapp/login, for example must
   ** be included in an authentication policy that uses an authentication scheme
   ** with a protection level greater than <code>0</code>.
   ** <br>
   ** Protected resources can be associated with any authorization policy..
   ** <p>
   ** Default: /**
   ** <p>
   ** The default matches any sequence of characters within zero or more
   ** intermediate levels spanning multiple directories.
   **
   ** @return                    the value of the <code>protectedResource</code>
   **                            property.
   **                            <br>
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
   ** <p>
   ** Public resources must be included in an authentication policy that uses an
   ** authentication scheme with a protection level of <code>0</code>. Most
   ** often this will be the anonymous authentication scheme. Public resources
   ** can be associated with any authorization policy. Indeed, OAM will block
   ** access to public resources that are not included in an authorization
   ** policy.
   ** <p>
   ** However, it is worth noting that it probably doesn't make sense to put an
   ** public resource into an authorization policy with constraints. If you plan
   ** on applying constraints to requests to a resource, then you should make
   ** that resource protected.
   ** <p>
   ** Session validation is still performed on requests to public resource.
   ** However, if a user session times out or is otherwise invalidated and a
   ** user tries to access an unprotected resource, they will be let through but
   ** their name will not be propagated in the
   ** <b><code>OAM_REMOTE_USER</code></b> header, rather
   ** <b><code>OAM_REMOTE_USER</code></b> will be set to anonymous.
   **
   ** @param  value              the value of the <code>publicResource</code>
   **                            property.
   **                            <br>
   **                            Allowed object is {@link PublicResourcesList}.
   */
  public void setPublicResource(final PublicResourcesList value) {
    this.publicResource = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPublicResource
  /**
   ** Returns the value of the <code>publicResource</code> property.
   ** <p>
   ** Public resources must be included in an authentication policy that uses an
   ** authentication scheme with a protection level of <code>0</code>. Most
   ** often this will be the anonymous authentication scheme. Public resources
   ** can be associated with any authorization policy. Indeed, OAM will block
   ** access to public resources that are not included in an authorization
   ** policy.
   ** <p>
   ** However, it is worth noting that it probably doesn't make sense to put an
   ** public resource into an authorization policy with constraints. If you plan
   ** on applying constraints to requests to a resource, then you should make
   ** that resource protected.
   ** <p>
   ** Session validation is still performed on requests to public resource.
   ** However, if a user session times out or is otherwise invalidated and a
   ** user tries to access an unprotected resource, they will be let through but
   ** their name will not be propagated in the
   ** <b><code>OAM_REMOTE_USER</code></b> header, rather
   ** <b><code>OAM_REMOTE_USER</code></b> will be set to anonymous.
   **
   ** @return                    the value of the <code>publicResource</code>
   **                            property.
   **                            <br>
   **                            Possible object is {@link PublicResourcesList}.
   */
  public PublicResourcesList getPublicResource() {
    return this.publicResource;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setExcludedResource
  /**
   ** Sets the value of the <code>excludedResource</code> property.
   ** <p>
   ** Excluded resources are entirely new since PS1 (11.1.1.5). When a request
   ** comes in and matches up with a resource that has been designated as
   ** excluded, then the <code>Access Agent</code> just lets the request pass
   ** through.
   ** <p>
   ** No calls to the <code>Access Server</code> are made, no session validation
   ** is performed, and the <b><code>OAM_REMOTE_USER</code></b> header is not
   ** added to the request. Also of note, if you have configured your
   ** <code>Access Agent</code>s to issue certain cache control headers back to
   ** the browser, they will not be issued in the case of excluded resources.
   **
   ** @param  value              the value of the <code>excludedResource</code>
   **                            property.
   **                            <br>
   **                            Allowed object is
   **                            {@link ExcludedResourcesList}.
   */
  public void setExcludedResource(final ExcludedResourcesList value) {
    this.excludedResource = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getExcludedResource
  /**
   ** Returns the value of the <code>excludedResource</code> property.
   ** <p>
   ** Excluded resources are entirely new since PS1 (11.1.1.5). When a request
   ** comes in and matches up with a resource that has been designated as
   ** excluded, then the <code>Access Agent</code> just lets the request pass
   ** through.
   ** <p>
   ** No calls to the <code>Access Server</code> are made, no session validation
   ** is performed, and the <b><code>OAM_REMOTE_USER</code></b> header is not
   ** added to the request. Also of note, if you have configured your
   ** <code>Access Agent</code>s to issue certain cache control headers back to
   ** the browser, they will not be issued in the case of excluded resources.
   **
   ** @return                    the value of the <code>excludedResource</code>
   **                            property.
   **                            <br>
   **                            Possible object is
   **                            {@link ExcludedResourcesList}.
   */
  public ExcludedResourcesList getExcludedResource() {
    return this.excludedResource;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setProtectedAuthnScheme
  /**
   ** Sets the value of the <code>protectedAuthnScheme</code> property.
   **
   ** @param  value              the value of the
   **                            <code>protectedAuthnScheme</code> property.
   **                            <br>
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
   **                            <br>
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
   **                            <br>
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
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getPublicAuthnScheme() {
    return this.publicAuthnScheme;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setUserDefinedParameters
  /**
   ** Sets the value of the <code>userDefinedParameters</code> property.
   **
   ** @param  value              the value of the
   **                            <code>userDefinedParameters</code> property.
   **                            <br>
   **                            Allowed object is {@link UserDefinedParameters}.
   */
  public void setUserDefinedParameters(final UserDefinedParameters value) {
    this.userDefinedParameters = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUserDefinedParameters
  /**
   ** Returns the value of the <code>userDefinedParameters</code> property.
   **
   ** @return                    the value of the
   **                            <code>userDefinedParameters</code> property.
   **                            <br>
   **                            Possible object is {@link UserDefinedParameters}.
   */
  public UserDefinedParameters getUserDefinedParameters() {
    return this.userDefinedParameters;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setRregApplicationDomain
  /**
   ** Sets the value of the <code>rregApplicationDomain</code> property.
   **
   ** @param  value              the value of the
   **                            <code>rregApplicationDomain</code> property.
   **                            <br>
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
   **                            <br>
   **                            Possible object is {@link RregApplicationDomain}.
   */
  public RregApplicationDomain getRregApplicationDomain() {
    return this.rregApplicationDomain;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setIsFusionAppRegistration
  /**
   ** Sets the value of the <code>isFusionAppRegistration</code> property.
   **
   ** @param  value              the value of the
   **                            <code>isFusionAppRegistration</code> property.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   */
  public void setIsFusionAppRegistration(final Boolean value) {
    this.isFusionAppRegistration = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getIsFusionAppRegistration
  /**
   ** Returns the value of the <code>isFusionAppRegistration</code> property.
   **
   ** @return                    the value of the
   **                            <code>isFusionAppRegistration</code> property.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public Boolean getIsFusionAppRegistration() {
    return this.isFusionAppRegistration;
  }
}