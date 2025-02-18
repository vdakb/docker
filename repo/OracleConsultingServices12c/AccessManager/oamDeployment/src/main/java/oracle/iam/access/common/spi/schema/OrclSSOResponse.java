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

    File        :   OrclSSOResponse.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    OrclSSOResponse.


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
// class OrclSSOResponse
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
 **         &lt;element name="type"                      type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 **         &lt;element name="mode"                      type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 **         &lt;element name="agentName"                 type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 **         &lt;element name="ssoServerVersion"          type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element name="cipherKey"                 type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element name="siteId"                    type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element name="siteToken"                 type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element name="loginUrl"                  type="{http://www.w3.org/2001/XMLSchema}anyURI"/&gt;
 **         &lt;element name="logoutUrl"                 type="{http://www.w3.org/2001/XMLSchema}anyURI"/&gt;
 **         &lt;element name="cancelUrl"                 type="{http://www.w3.org/2001/XMLSchema}anyURI"/&gt;
 **         &lt;element name="ssoTimeoutCookieName"      type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element name="ssoTimeoutCookieKey"       type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element name="nonsslSSOPort"             type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element ref="{}errorMsgs"                                                               minOccurs="0"/&gt;
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
@XmlRootElement(name=OrclSSOResponse.LOCAL)
@XmlType(name="", propOrder={"ssoServerVersion", "cipherKey", "siteId", "siteToken", "loginUrl", "logoutUrl", "cancelUrl", "ssoTimeoutCookieName", "ssoTimeoutCookieKey", "nonsslSSOPort"})
public class OrclSSOResponse extends BaseResponse {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String LOCAL = "OSSORegResponse";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @XmlElement(required=true)
  protected String    ssoServerVersion;
  @XmlElement(required=true)
  protected String    cipherKey;
  @XmlElement(required=true)
  protected String    siteId;
  @XmlElement(required=true)
  protected String    siteToken;
  @XmlElement(required=true)
  @XmlSchemaType(name="anyURI")
  protected String    loginUrl;
  @XmlElement(required=true)
  @XmlSchemaType(name="anyURI")
  protected String    logoutUrl;
  @XmlElement(required=true)
  @XmlSchemaType(name="anyURI")
  protected String    cancelUrl;
  @XmlElement(required=true)
  protected String    ssoTimeoutCookieName;
  @XmlElement(required=true)
  protected String    ssoTimeoutCookieKey;
  protected int       nonsslSSOPort;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>OrclSSOResponse</code> that allows use as a
   ** JavaBean.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  public OrclSSOResponse() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSiteId
  /**
   ** Sets the value of the <code>siteId</code> property.
   **
   ** @param  value              the value of the <code>siteId</code> property.
   **                            Allowed object is {@link String}.
   */
  public void setSiteId(final String value) {
    this.siteId = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSiteId
  /**
   ** Returns the value of the <code>siteId</code> property.
   **
   ** @return                    the value of the <code>siteId</code> property.
   **                            Possible object is {@link String}.
   */
  public String getSiteId() {
    return this.siteId;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSiteToken
  /**
   ** Sets the value of the <code>siteToken</code> property.
   **
   ** @param  value              the value of the <code>siteToken</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setSiteToken(final String value) {
    this.siteToken = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSiteToken
  /**
   ** Returns the value of the <code>siteToken</code> property.
   **
   ** @return                    the value of the <code>siteToken</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getSiteToken() {
    return this.siteToken;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setLoginUrl
  /**
   ** Sets the value of the <code>loginUrl</code> property.
   **
   ** @param  value              the value of the <code>loginUrl</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setLoginUrl(final String value) {
    this.loginUrl = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLoginUrl
  /**
   ** Returns the value of the <code>loginUrl</code> property.
   **
   ** @return                    the value of the <code>loginUrl</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getLoginUrl() {
    return this.loginUrl;
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
  // Method:   setCancelUrl
  /**
   ** Sets the value of the <code>cancelUrl</code> property.
   **
   ** @param  value              the value of the <code>cancelUrl</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setCancelUrl(final String value) {
    this.cancelUrl = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getCancelUrl
  /**
   ** Returns the value of the <code>cancelUrl</code> property.
   **
   ** @return                    the value of the <code>cancelUrl</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getCancelUrl() {
    return this.cancelUrl;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSsoTimeoutCookieName
  /**
   ** Sets the value of the <code>ssoTimeoutCookieName</code> property.
   **
   ** @param  value              the value of the
   **                            <code>ssoTimeoutCookieName</code> property.
   **                            Allowed object is {@link String}.
   */
  public void setSsoTimeoutCookieName(final String value) {
    this.ssoTimeoutCookieName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSsoTimeoutCookieName
  /**
   ** Returns the value of the <code>ssoTimeoutCookieName</code> property.
   **
   ** @return                    the value of the
   **                            <code>ssoTimeoutCookieName</code> property.
   **                            Possible object is {@link String}.
   */
  public String getSsoTimeoutCookieName() {
    return this.ssoTimeoutCookieName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSsoTimeoutCookieKey
  /**
   ** Sets the value of the <code>ssoTimeoutCookieKey</code> property.
   **
   ** @param  value              the value of the
   **                            <code>ssoTimeoutCookieKey</code> property.
   **                            Allowed object is {@link String}.
   */
  public void setSsoTimeoutCookieKey(final String value) {
    this.ssoTimeoutCookieKey = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSsoTimeoutCookieKey
  /**
   ** Returns the value of the <code>ssoTimeoutCookieKey</code> property.
   **
   ** @return                    the value of the
   **                            <code>ssoTimeoutCookieKey</code> property.
   **                            Possible object is {@link String}.
   */
  public String getSsoTimeoutCookieKey() {
    return this.ssoTimeoutCookieKey;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setNonsslSSOPort
  /**
   ** Sets the value of the <code>nonsslSSOPort</code> property.
   **
   ** @param  value              the value of the <code>nonsslSSOPort</code>
   **                            property.
   **                            Allowed object is <code>int</code>.
   */
  public void setNonsslSSOPort(final int value) {
    this.nonsslSSOPort = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getNonsslSSOPort
  /**
   ** Returns the value of the <code>nonsslSSOPort</code> property.
   **
   ** @return                    the value of the <code>nonsslSSOPort</code>
   **                            property.
   **                            Possible object is <code>int</code>.
   */
  public int getNonsslSSOPort() {
    return this.nonsslSSOPort;
  }
}