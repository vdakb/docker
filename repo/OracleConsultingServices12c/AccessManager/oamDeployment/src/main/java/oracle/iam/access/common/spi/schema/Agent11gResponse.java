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

    File        :   Agent11gResponse.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Agent11gResponse.


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
// class Agent11gResponse
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
 **         &lt;element name="type"                               type="{http://www.w3.org/2001/XMLSchema}string"       minOccurs="0"/&gt;
 **         &lt;element name="mode"                               type="{http://www.w3.org/2001/XMLSchema}string"       minOccurs="0"/&gt;
 **         &lt;element name="username"                           type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element name="password"                           type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element name="agentName"                          type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element name="accessClientPasswd"                 type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element name="preferredHost"                      type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element name="state"                              type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element name="maxCacheElems"                      type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 **         &lt;element name="cacheTimeout"                       type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 **         &lt;element name="tokenValidityPeriod"                type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 **         &lt;element name="maxConnections"                     type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 **         &lt;element name="maxSessionTime"                     type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 **         &lt;element name="failoverThreshold"                  type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 **         &lt;element name="aaaTimeoutThreshold"                type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 **         &lt;element name="sleepFor"                           type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 **         &lt;element name="debug"                              type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 **         &lt;element name="security"                           type="{http://www.w3.org/2001/XMLSchema}string"       minOccurs="0"/&gt;
 **         &lt;element name="denyOnNotProtected"                 type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 **         &lt;element name="allowManagementOperations"          type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 **         &lt;element name="allowTokenScopeOperations"          type="{http://www.w3.org/2001/XMLSchema}boolean"      minOccurs="0"/&gt;
 **         &lt;element name="allowMasterTokenRetrieval"          type="{http://www.w3.org/2001/XMLSchema}boolean"      minOccurs="0"/&gt;
 **         &lt;element name="allowCredentialCollectorOperations" type="{http://www.w3.org/2001/XMLSchema}boolean"      minOccurs="0"/&gt;
 **         &lt;element name="cachePragmaHeader"                  type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element name="cacheControlHeader"                 type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element name="ipValidation"                       type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 **         &lt;element ref="{}ipValidationExceptions"                                                                  minOccurs="0"/&gt;
 **         &lt;element name="globalPassphrase"                   type="{http://www.w3.org/2001/XMLSchema}string"       minOccurs="0"/&gt;
 **         &lt;element name="secretKey"                          type="{http://www.w3.org/2001/XMLSchema}string"       minOccurs="0"/&gt;
 **         &lt;element ref="{}logOutUrls"                                                                              minOccurs="0"/&gt;
 **         &lt;element name="logoutRedirectUrl"                  type="{http://www.w3.org/2001/XMLSchema}anyURI"/&gt;
 **         &lt;element name="logoutCallbackUrl"                  type="{http://www.w3.org/2001/XMLSchema}anyURI"/&gt;
 **         &lt;element name="logoutTargetUrlParamName"           type="{http://www.w3.org/2001/XMLSchema}string"       minOccurs="0"/&gt;
 **         &lt;element ref="{}primaryServerList"/&gt;
 **         &lt;element ref="{}secondaryServerList"/&gt;
 **         &lt;element ref="{}errorMsgs"                                                                               minOccurs="0"/&gt;
 **         &lt;element name="cert"                               type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/&gt;
 **         &lt;element name="cert-key"                           type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/&gt;
 **         &lt;element ref="{}userDefinedParameters"                                                                   minOccurs="0"/&gt;
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
@XmlRootElement(name=Agent11gResponse.LOCAL)
@XmlType(name="", propOrder={"accessClientPasswd", "preferredHost", "state", "maxCacheElems", "cacheTimeout", "tokenValidityPeriod", "maxConnections", "maxSessionTime", "failoverThreshold", "aaaTimeoutThreshold", "sleepFor", "debug", "security", "denyOnNotProtected", "allowManagementOperations", "allowTokenScopeOperations", "allowMasterTokenRetrieval", "allowCredentialCollectorOperations", "cachePragmaHeader", "cacheControlHeader", "ipValidation", "ipValidationExceptions", "globalPassphrase", "secretKey", "logOutUrls", "logoutRedirectUrl", "logoutCallbackUrl", "logoutTargetUrlParamName", "primaryServer", "secondaryServer", "cert", "certKey", "userDefinedParameters"})
public class Agent11gResponse extends BaseResponse {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String LOCAL = "OAM11GRegResponse";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @XmlElement(required=true)
  protected String                 accessClientPasswd;
  @XmlElement(required=true)
  protected String                 preferredHost;
  @XmlElement(required=true)
  protected String                 state;
  protected int                    maxCacheElems;
  protected int                    cacheTimeout;
  protected int                    tokenValidityPeriod;
  protected int                    maxConnections;
  protected int                    maxSessionTime;
  protected int                    failoverThreshold;
  protected int                    aaaTimeoutThreshold;
  protected int                    sleepFor;
  protected boolean                debug;
  protected String                 security;
  protected int                    denyOnNotProtected;
  protected boolean                allowManagementOperations;
  protected Boolean                allowTokenScopeOperations;
  protected Boolean                allowMasterTokenRetrieval;
  protected Boolean                allowCredentialCollectorOperations;
  @XmlElement(required=true)
  protected String                 cachePragmaHeader;
  @XmlElement(required=true)
  protected String                 cacheControlHeader;
  protected int                    ipValidation;
  protected IpValidationExceptions ipValidationExceptions;
  protected String                 globalPassphrase;
  protected String                 secretKey;
  protected LogOutUrls             logOutUrls;
  @XmlElement(required=true)
  @XmlSchemaType(name="anyURI")
  protected String                 logoutRedirectUrl;
  @XmlElement(required=true)
  @XmlSchemaType(name="anyURI")
  protected String                 logoutCallbackUrl;
  protected String                 logoutTargetUrlParamName;
  @XmlElement(name=PrimaryServerList.LOCAL, required=true)
  protected PrimaryServerList      primaryServer;
  @XmlElement(name=SecondaryServerList.LOCAL, required=true)
  protected SecondaryServerList    secondaryServer;
  protected byte[]                 cert;
  @XmlElement(name="cert-key")
  protected byte[]                 certKey;
  protected UserDefinedParameters  userDefinedParameters;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>Agent11gResponse</code> that allows use as a
   ** JavaBean.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  public Agent11gResponse() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

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
  // Method:   setMaxCacheElems
  /**
   ** Sets the value of the maxCacheElems property.
   **
   ** @param  value              the value of the maxCacheElems property.
   **                            Allowed object is <code>int</code>.
   */
  public void setMaxCacheElems(final int value) {
    this.maxCacheElems = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getMaxCacheElems
  /**
   ** Returns the value of the maxCacheElems property.
   **
   ** @return                    the value of the maxCacheElems property.
   **                            Possible object is <code>int</code>.
   */
  public int getMaxCacheElems() {
    return this.maxCacheElems;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setCacheTimeout
  /**
   ** Sets the value of the cacheTimeout property.
   **
   ** @param  value              the value of the cacheTimeout property.
   **                            Allowed object is <code>int</code>.
   */
  public void setCacheTimeout(final int value) {
    this.cacheTimeout = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getCacheTimeout
  /**
   ** Returns the value of the cacheTimeout property.
   **
   ** @return                    the value of the cacheTimeout property.
   **                            Possible object is <code>int</code>.
   */
  public int getCacheTimeout() {
    return this.cacheTimeout;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setTokenValidityPeriod
  /**
   ** Sets the value of the tokenValidityPeriod property.
   **
   ** @param  value              the value of the tokenValidityPeriod property.
   **                            Allowed object is <code>int</code>.
   */
  public void setTokenValidityPeriod(final int value) {
    this.tokenValidityPeriod = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getTokenValidityPeriod
  /**
   ** Returns the value of the tokenValidityPeriod property.
   **
   ** @return                    the value of the tokenValidityPeriod property.
   **                            Possible object is <code>int</code>.
   */
  public int getTokenValidityPeriod() {
    return this.tokenValidityPeriod;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setMaxConnections
  /**
   ** Sets the value of the maxConnections property.
   **
   ** @param  value              the value of the cookieSessionTime property.
   **                            Allowed object is <code>int</code>.
   */
  public void setMaxConnections(final int value) {
    this.maxConnections = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getMaxConnections
  /**
   ** Returns the value of the maxConnections property.
   **
   ** @return                    the value of the cookieSessionTime property.
   **                            Possible object is <code>int</code>.
   */
  public int getMaxConnections() {
    return this.maxConnections;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setMaxSessionTime
  /**
   ** Sets the value of the maxSessionTime property.
   **
   ** @param  value              the value of the maxSessionTime property.
   **                            Allowed object is <code>int</code>.
   */
  public void setMaxSessionTime(final int value) {
    this.maxSessionTime = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getMaxSessionTime
  /**
   ** Returns the value of the maxSessionTime property.
   **
   ** @return                    the value of the maxSessionTime property.
   **                            Possible object is <code>int</code>.
   */
  public int getMaxSessionTime() {
    return this.maxSessionTime;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setFailoverThreshold
  /**
   ** Sets the value of the failoverThreshold property.
   **
   ** @param  value              the value of the failoverThreshold property.
   **                            Allowed object is <code>int</code>.
   */
  public void setFailoverThreshold(final int value) {
    this.failoverThreshold = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getFailoverThreshold
  /**
   ** Returns the value of the failoverThreshold property.
   **
   ** @return                    the value of the failoverThreshold property.
   **                            Possible object is <code>int</code>.
   */
  public int getFailoverThreshold() {
    return this.failoverThreshold;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAaaTimeoutThreshold
  /**
   ** Sets the value of the aaaTimeoutThreshold property.
   **
   ** @param  value              the value of the aaaTimeoutThreshold property.
   **                            Allowed object is <code>int</code>.
   */
  public void setAaaTimeoutThreshold(final int value) {
    this.aaaTimeoutThreshold = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAaaTimeoutThreshold
  /**
   ** Returns the value of the aaaTimeoutThreshold property.
   **
   ** @return                    the value of the aaaTimeoutThreshold property.
   **                            Possible object is <code>int</code>.
   */
  public int getAaaTimeoutThreshold() {
    return this.aaaTimeoutThreshold;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSleepFor
  /**
   ** Sets the value of the sleepFor property.
   **
   ** @param  value              the value of the sleepFor property.
   **                            Allowed object is <code>int</code>.
   */
  public void setSleepFor(final int value) {
    this.sleepFor = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSleepFor
  /**
   ** Returns the value of the sleepFor property.
   **
   ** @return                    the value of the sleepFor property.
   **                            Possible object is <code>int</code>.
   */
  public int getSleepFor() {
    return this.sleepFor;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDebug
  /**
   ** Sets the value of the debug property.
   **
   ** @param  value              the value of the debug property.
   **                            Allowed object is <code>boolean</code>.
   */
  public void setDebug(final boolean value) {
    this.debug = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDebug
  /**
   ** Returns the value of the debug property.
   **
   ** @return                    the value of the debug property.
   **                            Possible object is <code>boolean</code>.
   */
  public boolean getDebug() {
    return this.debug;
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
  // Method:   setDenyOnNotProtected
  /**
   ** Sets the value of the denyOnNotProtected property.
   **
   ** @param  value              the value of the denyOnNotProtected property.
   **                            Allowed object is <code>int</code>.
   */
  public void setDenyOnNotProtected(final int value) {
    this.denyOnNotProtected = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDenyOnNotProtected
  /**
   ** Returns the value of the denyOnNotProtected property.
   **
   ** @return                    the value of the denyOnNotProtected property.
   **                            Possible object is <code>int</code>.
   */
  public int getDenyOnNotProtected() {
    return this.denyOnNotProtected;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAllowManagementOperations
  /**
   ** Sets the value of the allowManagementOperations property.
   **
   ** @param  value              the value of the allowManagementOperations property.
   **                            Allowed object is <code>boolean</code>.
   */
  public void setAllowManagementOperations(final boolean value) {
    this.allowManagementOperations = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAllowManagementOperations
  /**
   ** Returns the value of the allowManagementOperations property.
   **
   ** @return                    the value of the allowManagementOperations property.
   **                            Possible object is <code>boolean</code>.
   */
  public boolean getAllowManagementOperations() {
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
   **                            Allowed object is <code>int</code>.
   */
  public void setIpValidation(final int value) {
    this.ipValidation = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getIpValidation
  /**
   ** Returns the value of the ipValidation property.
   **
   ** @return                    the value of the ipValidation property.
   **                            Possible object is <code>int</code>.
   */
  public int getIpValidation() {
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
  // Method:   setGlobalPassphrase
  /**
   ** Sets the value of the globalPassphrase property.
   **
   ** @param  value              the value of the globalPassphrase property.
   **                            Allowed object is {@link String}.
   */
  public void setGlobalPassphrase(final String value) {
    this.globalPassphrase = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getGlobalPassphrase
  /**
   ** Returns the value of the globalPassphrase property.
   **
   ** @return                    the value of the globalPassphrase property.
   **                            Possible object is {@link String}.
   */
  public String getGlobalPassphrase() {
    return this.globalPassphrase;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSecretKey
  /**
   ** Sets the value of the secretKey property.
   **
   ** @param  value              the value of the secretKey property.
   **                            Allowed object is {@link String}.
   */
  public void setSecretKey(final String value) {
    this.secretKey = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSecretKey
  /**
   ** Returns the value of the secretKey property.
   **
   ** @return                    the value of the secretKey property.
   **                            Possible object is {@link String}.
   */
  public String getSecretKey() {
    return this.secretKey;
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
  // Method:   setCert
  /**
   ** Sets the value of the cert property.
   **
   ** @param  value              the value of the cert property.
   **                            Allowed object is <code>byte[]</code>.
   */
  public void setCert(final byte[] value) {
    this.cert = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getCert
  /**
   ** Returns the value of the cert property.
   **
   ** @return                    the value of the cert property.
   **                            Possible object is <code>byte[]</code>.
   */
  public byte[] getCert() {
    return this.cert;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setCertKey
  /**
   ** Sets the value of the certKey property.
   **
   ** @param  value              the value of the certKey property.
   **                            Allowed object is <code>byte[]</code>.
   */
  public void setCertKey(final byte[] value) {
    this.certKey = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getCertKey
  /**
   ** Returns the value of the certKey property.
   **
   ** @return                    the value of the certKey property.
   **                            Possible object is <code>byte[]</code>.
   */
  public byte[] getCertKey() {
    return this.certKey;
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
  // Method:   getUserDefinedParameters
  /**
   ** Returns the value of the userDefinedParameters property.
   **
   ** @return                    the value of the userDefinedParameters property.
   **                            Possible object is {@link UserDefinedParameters}.
   */
  public UserDefinedParameters getUserDefinedParameters() {
    return this.userDefinedParameters;
  }
}