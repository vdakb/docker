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

    File        :   Agent10gResponse.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Agent10gResponse.


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
// class Agent10gResponse
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
 **         &lt;element name="type"                      type="{http://www.w3.org/2001/XMLSchema}string"       minOccurs="0"/&gt;
 **         &lt;element name="mode"                      type="{http://www.w3.org/2001/XMLSchema}string"       minOccurs="0"/&gt;
 **         &lt;element name="agentName"                 type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element name="accessClientPasswd"        type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element name="primaryCookieDomain"       type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element name="preferredHost"             type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element name="state"                     type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element name="maxCacheElems"             type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 **         &lt;element name="cacheTimeout"              type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 **         &lt;element name="cookieSessionTime"         type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 **         &lt;element name="maxConnections"            type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 **         &lt;element name="maxSessionTime"            type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 **         &lt;element name="idleSessionTimeout"        type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 **         &lt;element name="failoverThreshold"         type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 **         &lt;element name="aaaTimeoutThreshold"       type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 **         &lt;element name="sleepFor"                  type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 **         &lt;element name="debug"                     type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 **         &lt;element name="security"                  type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element name="denyOnNotProtected"        type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 **         &lt;element name="allowManagementOperations" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 **         &lt;element name="cachePragmaHeader"         type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element name="cacheControlHeader"        type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element name="ipValidation"              type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 **         &lt;element ref="{}ipValidationExceptions"                                                         minOccurs="0"/&gt;
 **         &lt;element name="globalPassphrase"          type="{http://www.w3.org/2001/XMLSchema}string"       minOccurs="0"/&gt;
 **         &lt;element ref="{}logOutUrls"                                                                     minOccurs="0"/&gt;
 **         &lt;element ref="{}primaryServerList"                                                              minOccurs="0"/&gt;
 **         &lt;element ref="{}secondaryServerList"                                                            minOccurs="0"/&gt;
 **         &lt;element ref="{}errorMsgs"                                                                      minOccurs="0"/&gt;
 **         &lt;element name="cert"                      type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/&gt;
 **         &lt;element name="cert-key"                  type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/&gt;
 **         &lt;element ref="{}userDefinedParameters"                                                          minOccurs="0"/&gt;
 **         &lt;element name="managedServerUrl"          type="{http://www.w3.org/2001/XMLSchema}string"       minOccurs="0"/&gt;
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
@XmlRootElement(name=Agent10gResponse.LOCAL)
@XmlType(name="", propOrder={"accessClientPasswd", "primaryCookieDomain", "preferredHost", "state", "maxCacheElems", "cacheTimeout", "cookieSessionTime", "maxConnections", "maxSessionTime", "idleSessionTimeout", "failoverThreshold", "aaaTimeoutThreshold", "sleepFor", "debug", "security", "denyOnNotProtected", "allowManagementOperations", "cachePragmaHeader", "cacheControlHeader", "ipValidation", "ipValidationExceptions", "globalPassphrase", "logOutUrls", "primaryServer", "secondaryServer", "cert", "certKey", "userDefinedParameters"})
public class Agent10gResponse extends BaseResponse{

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String LOCAL = "OAMRegResponse";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @XmlElement(required=true)
  protected String                 accessClientPasswd;
  @XmlElement(required=true)
  protected String                 primaryCookieDomain;
  @XmlElement(required=true)
  protected String                 preferredHost;
  @XmlElement(required=true)
  protected String                 state;
  protected int                    maxCacheElems;
  protected int                    cacheTimeout;
  protected int                    cookieSessionTime;
  protected int                    maxConnections;
  protected int                    maxSessionTime;
  protected int                    idleSessionTimeout;
  protected int                    failoverThreshold;
  protected int                    aaaTimeoutThreshold;
  protected int                    sleepFor;
  protected boolean                debug;
  protected String                 security;
  protected int                    denyOnNotProtected;
  protected boolean                allowManagementOperations;
  @XmlElement(required=true)
  protected String                 cachePragmaHeader;
  @XmlElement(required=true)
  protected String                 cacheControlHeader;
  protected int                    ipValidation;
  protected IpValidationExceptions ipValidationExceptions;
  protected String                 globalPassphrase;
  @XmlElement(required=true)
  protected LogOutUrls             logOutUrls;
  @XmlElement(name=PrimaryServerList.LOCAL, required=true)
  protected PrimaryServerList      primaryServer;
  @XmlElement(name=SecondaryServerList.LOCAL, required=true)
  protected SecondaryServerList    secondaryServer;
  protected byte[]                 cert;
  protected byte[]                 certKey;
  protected UserDefinedParameters  userDefinedParameters;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>Agent10gResponse</code> that allows use as a
   ** JavaBean.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  public Agent10gResponse() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

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
  // Method:   setMaxCacheElems
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
   **                            Allowed object is <code>int</code>.
   */
  public void setMaxCacheElems(final int value) {
    this.maxCacheElems = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getMaxCacheElems
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
   **                            Possible object is <code>int</code>.
   */
  public int getMaxCacheElems() {
    return this.maxCacheElems;
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
   **                            Allowed object is <code>int</code>.
   */
  public void setCacheTimeout(final int value) {
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
   **                            Possible object is <code>int</code>.
   */
  public int getCacheTimeout() {
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
   **                            Allowed object is <code>int</code>.
   */
  public void setCookieSessionTime(final int value) {
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
   **                            Possible object is <code>int</code>.
   */
  public int getCookieSessionTime() {
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
   **                            Allowed object is <code>int</code>.
   */
  public void setMaxConnections(final int value) {
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
   **                            Possible object is <code>int</code>.
   */
  public int getMaxConnections() {
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
   **                            Allowed object is <code>int</code>.
   */
  public void setMaxSessionTime(final int value) {
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
   **                            Possible object is <code>int</code>.
   */
  public int getMaxSessionTime() {
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
  // Method:   setGlobalPassphrase
  /**
   ** Sets the value of the <code>globalPassphrase</code> property.
   **
   ** @param  value              the value of the <code>globalPassphrase</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setGlobalPassphrase(final String value) {
    this.globalPassphrase = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getGlobalPassphrase
  /**
   ** Returns the value of the <code>globalPassphrase</code> property.
   **
   ** @return                    the value of the <code>globalPassphrase</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getGlobalPassphrase() {
    return this.globalPassphrase;
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
   ** Sets the value of the <code>cert</code> property.
   **
   ** @param  value              the value of the <code>cert</code> property.
   **                            Allowed object is <code>byte[]</code>.
   */
  public void setCert(final byte[] value) {
    this.cert = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getCert
  /**
   ** Returns the value of the <code>cert</code> property.
   **
   ** @return                    the value of the <code>cert</code> property.
   **                            Possible object is <code>byte[]</code>.
   */
  public byte[] getCert() {
    return this.cert;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setCertKey
  /**
   ** Sets the value of the <code>certKey</code> property.
   **
   ** @param  value              the value of the <code>certKey</code> property.
   **                            Allowed object is <code>byte[]</code>.
   */
  public void setCertKey(final byte[] value) {
    this.certKey = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getCertKey
  /**
   ** Returns the value of the <code>certKey</code> property.
   **
   ** @return                    the value of the <code>certKey</code> property.
   **                            Possible object is <code>byte[]</code>.
   */
  public byte[] getCertKey() {
    return this.certKey;
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
}