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

    File        :   Agent10gCreate.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Agent10gCreate.


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
// class Agent10gCreate
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
 **         &lt;element name="type"                      type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="mode"                      type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="username"                  type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="password"                  type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="serverAddress"             type="{http://www.w3.org/2001/XMLSchema}anyURI"/&gt;
 **         &lt;element name="hostIdentifier"            type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="agentName"                 type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element name="agentBaseUrl"              type="{http://www.w3.org/2001/XMLSchema}anyURI"  minOccurs="0"/&gt;
 **         &lt;element name="virtualHost"               type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 **         &lt;element ref="{}hostPortVariationsList"                                                    minOccurs="0"/&gt;
 **         &lt;element name="applicationDomain"         type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="autoCreatePolicy"          type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 **         &lt;element name="accessClientPasswd"        type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="primaryCookieDomain"       type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="preferredHost"             type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="maxCacheElems"             type="{http://www.w3.org/2001/XMLSchema}int"     minOccurs="0"/&gt;
 **         &lt;element name="cacheTimeout"              type="{http://www.w3.org/2001/XMLSchema}int"     minOccurs="0"/&gt;
 **         &lt;element name="cookieSessionTime"         type="{http://www.w3.org/2001/XMLSchema}int"     minOccurs="0"/&gt;
 **         &lt;element name="maxConnections"            type="{http://www.w3.org/2001/XMLSchema}int"     minOccurs="0"/&gt;
 **         &lt;element name="maxSessionTime"            type="{http://www.w3.org/2001/XMLSchema}int"     minOccurs="0"/&gt;
 **         &lt;element name="idleSessionTimeout"        type="{http://www.w3.org/2001/XMLSchema}int"     minOccurs="0"/&gt;
 **         &lt;element name="failoverThreshold"         type="{http://www.w3.org/2001/XMLSchema}int"     minOccurs="0"/&gt;
 **         &lt;element name="aaaTimeoutThreshold"       type="{http://www.w3.org/2001/XMLSchema}int"     minOccurs="0"/&gt;
 **         &lt;element name="sleepFor"                  type="{http://www.w3.org/2001/XMLSchema}int"     minOccurs="0"/&gt;
 **         &lt;element name="debug"                     type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 **         &lt;element name="security"                  type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="denyOnNotProtected"        type="{http://www.w3.org/2001/XMLSchema}int"     minOccurs="0"/&gt;
 **         &lt;element name="allowManagementOperations" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 **         &lt;element name="cachePragmaHeader"         type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="cacheControlHeader"        type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="ipValidation"              type="{http://www.w3.org/2001/XMLSchema}int"     minOccurs="0"/&gt;
 **         &lt;element ref="{}ipValidationExceptions"                                                    minOccurs="0"/&gt;
 **         &lt;element ref="{}logOutUrls"                                                                minOccurs="0"/&gt;
 **         &lt;element name="logoutTargetUrlParamName"  type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element ref="{}primaryServerList"                                                         minOccurs="0"/&gt;
 **         &lt;element ref="{}secondaryServerList"                                                       minOccurs="0"/&gt;
 **         &lt;element ref="{}protectedResourcesList"                                                    minOccurs="0"/&gt;
 **         &lt;element ref="{}publicResourcesList"                                                       minOccurs="0"/&gt;
 **         &lt;element ref="{}excludedResourcesList"                                                     minOccurs="0"/&gt;
 **         &lt;element name="protectedAuthnScheme"      type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="publicAuthnScheme"         type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element ref="{}userDefinedParameters"                                                     minOccurs="0"/&gt;
 **         &lt;element ref="{}rregApplicationDomain"                                                     minOccurs="0"/&gt;
 **         &lt;element name="isFusionAppRegistration"   type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
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
@XmlRootElement(name=Agent10gCreate.LOCAL)
@XmlType(name="", propOrder={"agentBaseUrl", "virtualHost", "hostPortVariationsList", "applicationDomain", "autoCreatePolicy", "accessClientPasswd", "primaryCookieDomain", "preferredHost", "maxCacheElement", "cacheTimeout", "cookieSessionTime", "maxConnections", "maxSessionTime", "idleSessionTimeout", "failoverThreshold", "aaaTimeoutThreshold", "sleepFor", "debug", "security", "denyOnNotProtected", "allowManagementOperations", "cachePragmaHeader", "cacheControlHeader", "ipValidation", "ipValidationExceptions", "logOutUrls", "logoutTargetUrlParamName", "primaryServer", "secondaryServer", "protectedResource", "publicResource", "excludedResource", "protectedAuthnScheme", "publicAuthnScheme", "userDefinedParameters", "rregApplicationDomain", "isFusionAppRegistration"})
public class Agent10gCreate extends BaseRequest {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String LOCAL = "OAMRegRequest";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @XmlSchemaType(name="anyURI")
  protected String                 agentBaseUrl;
  protected Boolean                virtualHost;
  protected HostPortVariationsList hostPortVariationsList;
  protected String                 applicationDomain;
  protected Boolean                autoCreatePolicy;
  protected String                 accessClientPasswd;
  protected String                 primaryCookieDomain;
  protected String                 preferredHost;
  @XmlElement(name="maxCacheElems")
  protected Integer                maxCacheElement;
  protected Integer                cacheTimeout;
  protected Integer                cookieSessionTime;
  protected Integer                maxConnections;
  protected Integer                maxSessionTime;
  protected Integer                idleSessionTimeout;
  protected Integer                failoverThreshold;
  protected Integer                aaaTimeoutThreshold;
  protected Integer                sleepFor;
  protected Boolean                debug;
  protected String                 security;
  protected Integer                denyOnNotProtected;
  protected Boolean                allowManagementOperations;
  protected String                 cachePragmaHeader;
  protected String                 cacheControlHeader;
  protected Integer                ipValidation;
  protected IpValidationExceptions ipValidationExceptions;
  protected LogOutUrls             logOutUrls;
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
   ** Constructs a empty <code>Agent10gCreate</code> that allows use as a
   ** JavaBean.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  public Agent10gCreate() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Agent10gCreate</code> with the specified properties.
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
  public Agent10gCreate(final String mode, final String serverAddress, final String username, final String password,  final String agentName) {
    // ensure inheritance
    super(TYPE_AGENT10, mode, serverAddress, username, password,  agentName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAgentBaseUrl
  /**
   ** Sets the value of the <code>agentBaseUrl</code> property.
   ** <p>
   ** The host and port of the computer on which the Web Server for the
   ** <code>Access Agent</code> is installed. For example,
   ** <code>http://example_host:port</code> or
   ** <code>https://example_host:port</code>.
   ** <br>
   ** The port number is optional.
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
   ** <p>
   ** The host and port of the computer on which the Web Server for the
   ** <code>Access Agent</code> is installed. For example,
   ** <code>http://example_host:port</code> or
   ** <code>https://example_host:port</code>.
   ** <br>
   ** The port number is optional.
   **
   ** @return                    the value of the <code>agentBaseUrl</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getAgentBaseUrl() {
    return this.agentBaseUrl;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setVirtualHost
  /**
   ** Sets the value of the <code>virtualHost</code> property.
   ** <p>
   ** <code>virtualHost</code> property should be set to <code>true</code> if
   ** the <code>Access Agent</code> is installed on a Web Server that contains
   ** multiple Web Site and domain names. The <code>Access Agent</code> must
   ** reside in a location that enables it to protect all of the Web Sites on
   ** that server.
   **
   ** @param  value              the value of the <code>virtualHost</code>
   **                            property.
   **                            Allowed object is {@link Boolean}.
   */
  public void setVirtualHost(final Boolean value) {
    this.virtualHost = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getVirtualHost
  /**
   ** Returns the value of the <code>virtualHost</code> property.
   ** <p>
   ** <code>virtualHost</code> property should be set to <code>true</code> if
   ** the <code>Access Agent</code> is installed on a Web Server that contains
   ** multiple Web Site and domain names. The <code>Access Agent</code> must
   ** reside in a location that enables it to protect all of the Web Sites on
   ** that server.
   **
   ** @return                    the value of the <code>virtualHost</code>
   **                            property.
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
  // Method:   setApplicationDomain
  /**
   ** Sets the value of the <code>applicationDomain</code> property.
   ** The top-level construct of the Oracle Access Manager 11g policy model is
   ** the <code>Application Domain</code>.
   ** <br>
   ** Each application domain provides a logical container for resources, and
   ** the associated authentication and authorization policies that dictate who
   ** can access these.
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
   ** The top-level construct of the Oracle Access Manager 11g policy model is
   ** the <code>Application Domain</code>.
   ** <br>
   ** Each application domain provides a logical container for resources, and
   ** the associated authentication and authorization policies that dictate who
   ** can access these.
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
   ** <p>
   ** During <code>Access Agent</code> registration, authentication and
   ** authorization can be policies created automatically.
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
   ** <p>
   ** During <code>Access Agent</code> registration, authentication and
   ** authorization can be policies created automatically.
   **
   ** @return                    the value of the <code>autoCreatePolicy</code>
   **                            property.
   **                            Possible object is {@link Boolean}.
   */
  public Boolean getAutoCreatePolicy() {
    return this.autoCreatePolicy;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAccessClientPasswd
  /**
   ** Sets the value of the <code>accessClientPasswd</code> property.
   ** <p>
   ** An optional, unique password for the <code>Access Agent</code>, which
   ** can be assigned during the registration process.
   ** <p>
   ** When a registered <code>Access Agent</code> connects to an
   ** <code>Access Server</code>, the password is used for authentication to
   ** prevent unauthorized <code>Access Agent</code>s from connecting to
   ** <code>Access Server</code>s and obtaining policy information.
   **
   ** @param  value              the value of the
   **                            <code>accessClientPasswd</code> property.
   **                            Allowed object is {@link String}.
   */
  public void setAccessClientPasswd(final String value) {
    this.accessClientPasswd = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAccessClientPasswd
  /**
   ** Returns the value of the <code>accessClientPasswd</code> property.
   ** <p>
   ** An optional, unique password for the <code>Access Agent</code>, which
   ** can be assigned during the registration process.
   ** <p>
   ** When a registered <code>Access Agent</code> connects to an
   ** <code>Access Server</code>, the password is used for authentication to
   ** prevent unauthorized <code>Access Agent</code>s from connecting to
   ** <code>Access Server</code>s and obtaining policy information.
   **
   ** @return                    the value of the
   **                            <code>accessClientPasswd</code> property.
   **                            Possible object is {@link String}.
   */
  public String getAccessClientPasswd() {
    return this.accessClientPasswd;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPrimaryCookieDomain
  /**
   ** Sets the value of the <code>primaryCookieDomain</code> property.
   ** <p>
   ** This parameter describes the Web Server domain on which the
   ** <code>Access Agent</code> is deployed, for instance,
   ** <code>.example.com</code>.
   ** <p>
   ** You must configure the cookie domain to enable single sign-on among
   ** Web Servers. Specifically, the Web Servers for which you configure
   ** single sign-on must have the same Primary Cookie Domain value.
   ** <code>Access Agent</code> uses this parameter to create the
   ** <b><code>ObSSOCookie</code></b> authentication cookie.
   **
   ** @param  value              the value of the
   **                            <code>primaryCookieDomain</code> property.
   **                            Allowed object is {@link String}.
   */
  public void setPrimaryCookieDomain(final String value) {
    this.primaryCookieDomain = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPrimaryCookieDomain
  /**
   ** Returns the value of the <code>primaryCookieDomain</code> property.
   ** <p>
   ** This parameter describes the Web Server domain on which the
   ** <code>Access Agent</code> is deployed, for instance,
   ** <code>.example.com</code>.
   ** <p>
   ** You must configure the cookie domain to enable single sign-on among
   ** Web Servers. Specifically, the Web Servers for which you configure
   ** single sign-on must have the same Primary Cookie Domain value.
   ** <code>Access Agent</code> uses this parameter to create the
   ** <b><code>ObSSOCookie</code></b> authentication cookie.
   **
   ** @return                    the value of the
   **                            <code>primaryCookieDomain</code> property.
   **                            Possible object is {@link String}.
   */
  public String getPrimaryCookieDomain() {
    return this.primaryCookieDomain;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPreferredHost
  /**
   ** Sets the value of the <code>preferredHost</code> property.
   ** <p>
   ** Specifies how the hostname appears in all HTTP requests as users attempt
   ** to access the protected Web Server.
   ** <br>
   ** The hostname within the HTTP request is translated into the value
   ** entered into this field regardless of the way it was defined in a user's
   ** HTTP request.
   ** <p>
   ** The Preferred Host function prevents security holes that can be
   ** inadvertently created if a host's identifier is not included in the Host
   ** Identifiers list. However, it cannot be used with virtual Web hosting.
   ** <br>
   ** For virtual hosting, you must use the Host Identifiers feature.
   **
   ** @param  value              the value of the <code>preferredHost</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setPreferredHost(final String value) {
    this.preferredHost = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPreferredHost
  /**
   ** Returns the value of the <code>preferredHost</code> property.
   ** <p>
   ** Specifies how the hostname appears in all HTTP requests as users attempt
   ** to access the protected Web Server.
   ** <br>
   ** The hostname within the HTTP request is translated into the value
   ** entered into this field regardless of the way it was defined in a user's
   ** HTTP request.
   ** <p>
   ** The Preferred Host function prevents security holes that can be
   ** inadvertently created if a host's identifier is not included in the Host
   ** Identifiers list. However, it cannot be used with virtual Web hosting.
   ** <br>
   ** For virtual hosting, you must use the Host Identifiers feature.
   **
   ** @return                    the value of the <code>preferredHost</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getPreferredHost() {
    return this.preferredHost;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setMaxCacheElement
  /**
   ** Sets the value of the <code>maxCacheElement</code> property.
   ** <p>
   ** Number of elements maintained in the cache. Caches are the following:
   ** <ul>
   **   <li><b>Resource to Authentication Scheme</b> - This cache maintains
   **       information about Resources (URLs), including whether it is
   **       protected and, if so, the authentication scheme used for
   **       protection.
   **   <li><b>Resource to Authorization Policy</b> - This cache maintains
   **       information about Resources and associated authorization policy
   **       <br>
   **       This cache stores authentication scheme information for a specific
   **       authentication scheme ID.
   **       <br>
   **       (11g Webgate only)
   ** </ul>
   ** The value of this setting refers to the maximum consolidated count for
   ** elements in these caches.
   **
   ** @param  value              the value of the <code>maxCacheElement</code>
   **                            property.
   **                            Allowed object is {@link Integer}.
   */
  public void setMaxCacheElement(final Integer value) {
    this.maxCacheElement = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getMaxCacheElement
  /**
   ** Returns the value of the <code>maxCacheElement</code> property.
   ** <p>
   ** Number of elements maintained in the cache. Caches are the following:
   ** <ul>
   **   <li><b>Resource to Authentication Scheme</b> - This cache maintains
   **       information about Resources (URLs), including whether it is
   **       protected and, if so, the authentication scheme used for
   **       protection.
   **   <li><b>Resource to Authorization Policy</b> - This cache maintains
   **       information about Resources and associated authorization policy
   **       <br>
   **       This cache stores authentication scheme information for a specific
   **       authentication scheme ID.
   **       <br>
   **       (11g Webgate only)
   ** </ul>
   ** The value of this setting refers to the maximum consolidated count for
   ** elements in these caches.
   **
   ** @return                    the value of the <code>maxCacheElement</code>
   **                            property.
   **                            Possible object is {@link Integer}.
   */
  public Integer getMaxCacheElement() {
    return this.maxCacheElement;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setCacheTimeout
  /**
   ** Sets the value of the <code>cacheTimeout</code> property.
   ** <p>
   ** Amount of time cached information remains in the
   ** <code>Access Agent</code> caches (Resource to Authentication Scheme,
   ** Authentication Schemes, and 11g <code>Access Agent</code> only Resource
   ** to Authorization Policy) when the information is neither used nor
   ** referenced.
   **
   ** @param  value              the value of the <code>cacheTimeout</code>
   **                            property.
   **                            Allowed object is {@link Integer}.
   */
  public void setCacheTimeout(final Integer value) {
    this.cacheTimeout = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getCacheTimeout
  /**
   ** Returns the value of the <code>cacheTimeout</code> property.
   ** <p>
   ** Amount of time cached information remains in the
   ** <code>Access Agent</code> caches (Resource to Authentication Scheme,
   ** Authentication Schemes, and 11g <code>Access Agent</code> only Resource
   ** to Authorization Policy) when the information is neither used nor
   ** referenced.
   **
   ** @return                    the value of the <code>cacheTimeout</code>
   **                            property.
   **                            Possible object is {@link Integer}.
   */
  public Integer getCacheTimeout() {
    return this.cacheTimeout;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setCookieSessionTime
  /**
   ** Sets the value of the <code>cacheTimeout</code> property.
   ** <p>
   ** Maximum valid time period for an agent token (the content of
   ** <b><code>ObSSOCookie</code></b> for 10g WebGate).
   **
   ** @param  value              the value of the <code>cacheTimeout</code>
   **                            property.
   **                            Allowed object is {@link Integer}.
   */
  public void setCookieSessionTime(final Integer value) {
    this.cookieSessionTime = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getCookieSessionTime
  /**
   ** Returns the value of the <code>cacheTimeout</code> property.
   ** <p>
   ** Maximum valid time period for an agent token (the content of
   ** <b><code>ObSSOCookie</code></b> for 10g WebGate).
   **
   ** @return                    the value of the <code>cacheTimeout</code>
   **                            property.
   **                            Possible object is {@link Integer}.
   */
  public Integer getCookieSessionTime() {
    return this.cookieSessionTime;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setMaxConnections
  /**
   ** Sets the value of the <code>cacheTimeout</code> property.
   ** <p>
   ** The maximum number of connections that the <code>Access Agent</code> can
   ** establish with the <code>Access Server</code>. This number must be the
   ** same as (or greater than) the number of connections that are actually
   ** associated with the <code>Access Agent</code>.
   **
   ** @param  value              the value of the <code>cacheTimeout</code>
   **                            property.
   **                            Allowed object is {@link Integer}.
   */
  public void setMaxConnections(final Integer value) {
    this.maxConnections = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getMaxConnections
  /**
   ** Returns the value of the <code>cacheTimeout</code> property.
   ** <p>
   ** The maximum number of connections that the <code>Access Agent</code> can
   ** establish with the <code>Access Server</code>. This number must be the
   ** same as (or greater than) the number of connections that are actually
   ** associated with the <code>Access Agent</code>.
   **
   ** @return                    the value of the <code>cacheTimeout</code>
   **                            property.
   **                            Possible object is {@link Integer}.
   */
  public Integer getMaxConnections() {
    return this.maxConnections;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setMaxSessionTime
  /**
   ** Sets the value of the <code>maxSessionTime</code> property.
   ** <p>
   ** Maximum amount of time to keep connections from the
   ** <code>Access Agent</code> to <code>Access Server</code> network alive.
   ** <br>
   ** After this time has elapsed, all connections from
   ** <code>Access Agent</code> to <code>Access Server</code> network will be
   ** shutdown and replaced with new ones.The unit of time is based on the
   ** maxSessionTimeUnits user-defined parameter which can be 'minutes' or
   ** 'hours'. When maxSessionTimeUnits is not defined, the unit is defaulted
   ** to 'hours'.
   **
   ** @param  value              the value of the <code>maxSessionTime</code>
   **                            property.
   **                            Allowed object is {@link Integer}.
   */
  public void setMaxSessionTime(final Integer value) {
    this.maxSessionTime = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getMaxSessionTime
  /**
   ** Returns the value of the <code>maxSessionTime</code> property.
   ** <p>
   ** Maximum amount of time to keep connections from the
   ** <code>Access Agent</code> to <code>Access Server</code> network alive.
   ** <br>
   ** After this time has elapsed, all connections from
   ** <code>Access Agent</code> to <code>Access Server</code> network will be
   ** shutdown and replaced with new ones.The unit of time is based on the
   ** maxSessionTimeUnits user-defined parameter which can be 'minutes' or
   ** 'hours'. When maxSessionTimeUnits is not defined, the unit is defaulted
   ** to 'hours'.
   **
   ** @return                    the value of the <code>maxSessionTime</code>
   **                            property.
   **                            Possible object is {@link Integer}.
   */
  public Integer getMaxSessionTime() {
    return this.maxSessionTime;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setIdleSessionTimeout
  /**
   ** Sets the value of the <code>idleSessionTimeout</code> property.
   **
   ** @param  value              the value of the
   **                            <code>idleSessionTimeout</code> property.
   **                            Allowed object is {@link Integer}.
   */
  public void setIdleSessionTimeout(final Integer value) {
    this.idleSessionTimeout = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getIdleSessionTimeout
  /**
   ** Returns the value of the <code>idleSessionTimeout</code> property.
   **
   ** @return                    the value of the
   **                            <code>idleSessionTimeout</code> property.
   **                            Possible object is {@link Integer}.
   */
  public Integer getIdleSessionTimeout() {
    return this.idleSessionTimeout;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setFailoverThreshold
  /**
   ** Sets the value of the <code>failoverThreshold</code> property.
   ** <p>
   ** Number representing the point when the <code>Access Agent</code> opens
   ** connections to a secondary <code>Access Server</code>.
   ** <br>
   ** For example, if you type 30 in this field and the number of connections
   ** to primary <code>Access Server</code> falls to 29, this
   ** <code>Access Agent</code> opens connections to secondary
   ** <code>Access Server</code>.
   **
   ** @param  value              the value of the <code>failoverThreshold</code>
   **                            property.
   **                            Allowed object is {@link Integer}.
   */
  public void setFailoverThreshold(final Integer value) {
    this.failoverThreshold = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getFailoverThreshold
  /**
   ** Returns the value of the <code>failoverThreshold</code> property.
   ** <p>
   ** Number representing the point when the <code>Access Agent</code> opens
   ** connections to a secondary <code>Access Server</code>.
   ** <br>
   ** For example, if you type 30 in this field and the number of connections
   ** to primary <code>Access Server</code> falls to 29, this
   ** <code>Access Agent</code> opens connections to secondary
   ** <code>Access Server</code>.
   **
   ** @return                    the value of the <code>failoverThreshold</code>
   **                            property.
   **                            Possible object is {@link Integer}.
   */
  public Integer getFailoverThreshold() {
    return this.failoverThreshold;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAaaTimeoutThreshold
  /**
   ** Sets the value of the <code>aaaTimeoutThreshold</code> property.
   ** <p>
   ** Threshold Number (in seconds) to wait for a response from the
   ** <code>Access Server</code>.
   ** <br>
   ** If this parameter is set, it is used as an application TCP/IP timeout
   ** instead of the default TCP/IP timeout.
   **
   ** @param  value              the value of the
   **                            <code>aaaTimeoutThreshold</code> property.
   **                            Allowed object is {@link Integer}.
   */
  public void setAaaTimeoutThreshold(final Integer value) {
    this.aaaTimeoutThreshold = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAaaTimeoutThreshold
  /**
   ** Returns the value of the <code>aaaTimeoutThreshold</code> property.
   ** <p>
   ** Threshold Number (in seconds) to wait for a response from the
   ** <code>Access Server</code>.
   ** <br>
   ** If this parameter is set, it is used as an application TCP/IP timeout
   ** instead of the default TCP/IP timeout.
   **
   ** @return                    the value of the
   **                            <code>aaaTimeoutThreshold</code> property.
   **                            Possible object is {@link Integer}.
   */
  public Integer getAaaTimeoutThreshold() {
    return this.aaaTimeoutThreshold;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSleepFor
  /**
   ** Sets the value of the <code>sleepFor</code> property.
   ** <p>
   ** The frequency (in seconds) with which the <code>Access Server</code>
   ** checks its connections to the directory server. For example, if you set a
   ** value of 60 seconds, the <code>Access Server</code> checks its connections
   ** every 60 seconds from the time it comes up.
   **
   ** @param  value              the value of the <code>sleepFor</code>
   **                            property.
   **                            Allowed object is {@link Integer}.
   */
  public void setSleepFor(final Integer value) {
    this.sleepFor = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSleepFor
  /**
   ** Returns the value of the <code>sleepFor</code> property.
   ** <p>
   ** The frequency (in seconds) with which the <code>Access Server</code>
   ** checks its connections to the directory server. For example, if you set a
   ** value of 60 seconds, the <code>Access Server</code> checks its connections
   ** every 60 seconds from the time it comes up.
   **
   ** @return                    the value of the <code>sleepFor</code>
   **                            property.
   **                            Possible object is {@link Integer}.
   */
  public Integer getSleepFor() {
    return this.sleepFor;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDebug
  /**
   ** Sets the value of the <code>debug</code> property.
   ** <p>
   ** Debugging can be enabled or not.
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
   ** <p>
   ** Debugging can be enabled or not.
   **
   ** @return                    the value of the <code>debug</code> property.
   **                            Possible object is {@link Boolean}.
   */
  public Boolean getDebug() {
    return this.debug;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSecurity
  /**
   ** Sets the value of the <code>security</code> property.
   ** <p>
   ** Level of communication transport security between the
   ** <code>Access Agent</code> and the <code>Access Server</code> (this must
   ** match the level specified for the <code>Access Server</code>):
   ** <ul>
   **   <li><b>Open</b>   - No transport security
   **   <li><b>Simple</b> - SSL v3/TLS v1.0 secure transport using dynamically
   **                       generated session keys
   **   <li><b>Cert</b>   - SSL v3/TLS v1.0 secure transport using server
   **                       side x.509 certificates.
   **                       <br>
   **                       Choosing this option displays a field where you
   **                       can enter the Agent Key Password.
   **                       <br>
   **                       Agent Key Password: The private key file
   **                       (aaa_key.pem) is encrypted using DES algorithm.
   **                       The Agent Key Password is saved in obfuscated
   **                       format in password.xml and is required by the
   **                       server to generate password.xml. However, this
   **                       password is not retained by the server. When
   **                       editing an 11g Webgate registration, password.xml
   **                       is updated only when the mode is changed from
   **                       <b>Open</b> to <b>Cert</b> or <b>Simple</b> to
   **                       <b>Cert</b>. In <b>Cert</b> mode, once generated,
   **                       password.xml cannot be updated. Editing the Agent
   **                       Key Password does not result in creation of a new
   **                       password.xml.
   ** </ul>
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
   ** <p>
   ** Level of communication transport security between the
   ** <code>Access Agent</code> and the <code>Access Server</code> (this must
   ** match the level specified for the <code>Access Server</code>):
   ** <ul>
   **   <li><b>Open</b>   - No transport security
   **   <li><b>Simple</b> - SSL v3/TLS v1.0 secure transport using dynamically
   **                       generated session keys
   **   <li><b>Cert</b>   - SSL v3/TLS v1.0 secure transport using server
   **                       side x.509 certificates.
   **                       <br>
   **                       Choosing this option displays a field where you
   **                       can enter the Agent Key Password.
   **                       <br>
   **                       Agent Key Password: The private key file
   **                       (aaa_key.pem) is encrypted using DES algorithm.
   **                       The Agent Key Password is saved in obfuscated
   **                       format in password.xml and is required by the
   **                       server to generate password.xml. However, this
   **                       password is not retained by the server. When
   **                       editing an 11g Webgate registration, password.xml
   **                       is updated only when the mode is changed from
   **                       <b>Open</b> to <b>Cert</b> or <b>Simple</b> to
   **                       <b>Cert</b>. In <b>Cert</b> mode, once generated,
   **                       password.xml cannot be updated. Editing the Agent
   **                       Key Password does not result in creation of a new
   **                       password.xml.
   ** </ul>
   **
   ** @return                    the value of the <code>security</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getSecurity() {
    return this.security;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDenyOnNotProtected
  /**
   ** Sets the value of the <code>denyOnNotProtected</code> property.
   ** <p>
   ** When enabled, this element denies access to all resources to which access
   ** is not explicitly allowed by a rule or policy. Enabling this can limit the
   ** number of times the <code>Access Agent</code> queries the
   ** <code>Access Server</code>, and can improve performance for large or busy
   ** <code>Application Domain</code>s.
   **
   ** @param  value              the value of the
   **                            <code>denyOnNotProtected</code> property.
   **                            Allowed object is {@link Integer}.
   */
  public void setDenyOnNotProtected(final Integer value) {
    this.denyOnNotProtected = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDenyOnNotProtected
  /**
   ** Returns the value of the <code>denyOnNotProtected</code> property.
   ** <p>
   ** When enabled, this element denies access to all resources to which access
   ** is not explicitly allowed by a rule or policy. Enabling this can limit the
   ** number of times the <code>Access Agent</code> queries the
   ** <code>Access Server</code>, and can improve performance for large or busy
   ** <code>Application Domain</code>s.
   **
   ** @return                    the value of the
   **                            <code>denyOnNotProtected</code> property.
   **                            Possible object is {@link Integer}.
   */
  public Integer getDenyOnNotProtected() {
    return this.denyOnNotProtected;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAllowManagementOperations
  /**
   ** Sets the value of the <code>allowManagementOperations</code> property.
   ** <p>
   ** This Agent Privilege function enables the provisioning of session
   ** operations per agent, as follows:
   ** <ul>
   **   <li>Terminate session
   **   <li>Enumerate sessions
   **   <li>Add or Update attributes for an existing session
   **   <li>List all attributes for a given session ID or read session
   ** </ul>
   **
   ** @param  value              the value of the
   **                            <code>allowManagementOperations</code>
   **                            property.
   **                            Allowed object is {@link Boolean}.
   */
  public void setAllowManagementOperations(final Boolean value) {
    this.allowManagementOperations = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAllowManagementOperations
  /**
   ** Returns the value of the <code>allowManagementOperations</code> property.
   ** <p>
   ** This Agent Privilege function enables the provisioning of session
   ** operations per agent, as follows:
   ** <ul>
   **   <li>Terminate session
   **   <li>Enumerate sessions
   **   <li>Add or Update attributes for an existing session
   **   <li>List all attributes for a given session ID or read session
   ** </ul>
   **
   ** @return                    the value of the
   **                            <code>allowManagementOperations</code>
   **                            property.
   **                            Possible object is {@link Boolean}.
   */
  public Boolean getAllowManagementOperations() {
    return this.allowManagementOperations;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setCachePragmaHeader
  /**
   ** Sets the value of the <code>cachePragmaHeader</code> property.
   ** <p>
   ** This setting apply only to browser based <code>Access Agent</code>s and
   ** control the browser's cache.
   ** <p>
   ** By default, the parameter is set to <code>no-cache</code>. This prevents
   ** browser based <code>Access Agent</code>s from caching data at the Web
   ** server application and the user's browser.
   **
   ** @param  value              the value of the <code>cachePragmaHeader</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setCachePragmaHeader(final String value) {
    this.cachePragmaHeader = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getCachePragmaHeader
  /**
   ** Returns the value of the <code>cachePragmaHeader</code> property.
   ** <p>
   ** This setting apply only to browser based <code>Access Agent</code>s and
   ** control the browser's cache.
   ** <p>
   ** By default, the parameter is set to <code>no-cache</code>. This prevents
   ** browser based <code>Access Agent</code>s from caching data at the Web
   ** server application and the user's browser.
   **
   ** @return                    the value of the <code>cachePragmaHeader</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getCachePragmaHeader() {
    return this.cachePragmaHeader;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setCacheControlHeader
  /**
   ** Sets the value of the <code>cachePragmaHeader</code> property.
   ** <p>
   ** This setting apply only to browser based <code>Access Agent</code>s and
   ** control the browser's cache.
   ** <p>
   ** By default, the parameter is set to <code>no-cache</code>. This prevents
   ** browser based <code>Access Agent</code>s from caching data at the Web
   ** server application and the user's browser.
   **
   ** @param  value              the value of the <code>cachePragmaHeader</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setCacheControlHeader(final String value) {
    this.cacheControlHeader = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getCacheControlHeader
  /**
   ** Returns the value of the <code>cachePragmaHeader</code> property.
   ** <p>
   ** This setting apply only to browser based <code>Access Agent</code>s and
   ** control the browser's cache.
   ** <p>
   ** By default, the parameter is set to <code>no-cache</code>. This prevents
   ** browser based <code>Access Agent</code>s from caching data at the Web
   ** server application and the user's browser.
   **
   ** @return                    the value of the <code>cachePragmaHeader</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getCacheControlHeader() {
    return this.cacheControlHeader;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setIpValidation
  /**
   ** Sets the value of the <code>ipValidation</code> property.
   ** <p>
   ** IP address validation is a function that determines if a client's IP
   ** address is the same as the IP address stored in the cookie generated for
   ** single sign-on. The IPValidation parameter turns IP address validation on
   ** and off; it is a WebGate specific parameter found in the WebGate profile.
   **
   ** @param  value              the value of the <code>ipValidation</code>
   **                            property.
   **                            Allowed object is {@link Integer}.
   */
  public void setIpValidation(final Integer value) {
    this.ipValidation = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getIpValidation
  /**
   ** Returns the value of the <code>ipValidation</code> property.
   ** <p>
   ** IP address validation is a function that determines if a client's IP
   ** address is the same as the IP address stored in the cookie generated for
   ** single sign-on. The IPValidation parameter turns IP address validation on
   ** and off; it is a WebGate specific parameter found in the WebGate profile.
   **
   ** @return                    the value of the <code>ipValidation</code>
   **                            property.
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
  // Method:   setLogoutTargetUrlParamName
  /**
   ** Sets the value of the <code>logoutTargetUrlParamName</code> property.
   ** <p>
   ** The value is the name for the query parameter that the OPSS applications
   ** passes to WebGate during logout; the query parameter specifies the target
   ** URL of the landing page after logout completes.
   **
   ** @param  value              the value of the
   **                            <code>logoutTargetUrlParamName</code> property.
   **                            Allowed object is {@link String}.
   */
  public void setLogoutTargetUrlParamName(final String value) {
    this.logoutTargetUrlParamName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLogoutTargetUrlParamName
  /**
   ** Returns the value of the <code>logoutTargetUrlParamName</code> property.
   ** <p>
   ** The value is the name for the query parameter that the OPSS applications
   ** passes to WebGate during logout; the query parameter specifies the target
   ** URL of the landing page after logout completes.
   **
   ** @return                    the value of the
   **                            <code>logoutTargetUrlParamName</code> property.
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
   ** @return                    the value of the <code>secondaryServer</code>
   **                            property.
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
  // Method:   setUserDefinedParameters
  /**
   ** Sets the value of the <code>userDefinedParameters</code> property.
   **
   ** @param  value              the value of the
   **                            <code>userDefinedParameters</code> property.
   **                            Allowed object is
   **                            {@link UserDefinedParameters}.
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
   **                            Possible object is
   **                            {@link UserDefinedParameters}.
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
   **                            <code>rregApplicationDomain</code>
   **                            property.
   **                            Allowed object is
   **                            {@link RregApplicationDomain}.
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
   **                            <code>rregApplicationDomain</code>
   **                            property.
   **                            Possible object is
   **                            {@link RregApplicationDomain}.
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
   **                            Possible object is {@link Boolean}.
   */
  public Boolean getIsFusionAppRegistration() {
    return this.isFusionAppRegistration;
  }
}