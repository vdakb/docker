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

    File        :   OrclSSOUpdate.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    OrclSSOUpdate.


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
// class OrclSSOUpdate
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
 **         &lt;element name="type"                         type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="mode"                         type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="username"                     type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="password"                     type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="serverAddress"                type="{http://www.w3.org/2001/XMLSchema}anyURI"/&gt;
 **         &lt;element name="agentName"                    type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element name="adminInfo"                    type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="adminId"                      type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="successUrl"                   type="{http://www.w3.org/2001/XMLSchema}anyURI"  minOccurs="0"/&gt;
 **         &lt;element name="failureUrl"                   type="{http://www.w3.org/2001/XMLSchema}anyURI"  minOccurs="0"/&gt;
 **         &lt;element name="startDate"                    type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="homeUrl"                      type="{http://www.w3.org/2001/XMLSchema}anyURI"  minOccurs="0"/&gt;
 **         &lt;element name="logoutUrl"                    type="{http://www.w3.org/2001/XMLSchema}anyURI"  minOccurs="0"/&gt;
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
@XmlRootElement(name=OrclSSOUpdate.LOCAL)
@XmlType(name="", propOrder={"adminInfo", "adminId", "successUrl", "failureUrl", "startDate", "homeUrl", "logoutUrl"})
public class OrclSSOUpdate extends BaseRequest {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String LOCAL = "OSSOUpdateAgentRegRequest";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected String adminInfo;
  protected String adminId;
  @XmlSchemaType(name="anyURI")
  protected String successUrl;
  @XmlSchemaType(name="anyURI")
  protected String failureUrl;
  protected String startDate;
  @XmlSchemaType(name="anyURI")
  protected String homeUrl;
  @XmlSchemaType(name="anyURI")
  protected String logoutUrl;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>OrclSSOUpdate</code> that allows use as a
   ** JavaBean.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  public OrclSSOUpdate() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OrclSSOUpdate</code> with the specified properties.
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
  public OrclSSOUpdate(final String mode, final String serverAddress, final String username, final String password,  final String agentName) {
    // ensure inheritance
    super(TYPE_ORCLSSO, mode, serverAddress, username, password,  agentName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

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
  // Method:   setSuccessUrl
  /**
   ** Sets the value of the <code>successUrl</code> property.
   **
   ** @param  value              the value of the <code>successUrl</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setSuccessUrl(final String value) {
    this.successUrl = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSuccessUrl
  /**
   ** Returns the value of the <code>successUrl</code> property.
   **
   ** @return                    the value of the <code>successUrl</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getSuccessUrl() {
    return this.successUrl;
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

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setStartDate
  /**
   ** Sets the value of the <code>startDate</code> property.
   **
   ** @param  value              the value of the <code>startDate</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setStartDate(final String value) {
    this.startDate = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getStartDate
  /**
   ** Returns the value of the <code>startDate</code> property.
   **
   ** @return                    the value of the <code>startDate</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getStartDate() {
    return this.startDate;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setHomeUrl
  /**
   ** Sets the value of the <code>homeUrl</code> property.
   **
   ** @param  value              the value of the <code>homeUrl</code> property.
   **                            Allowed object is {@link String}.
   */
  public void setHomeUrl(final String value) {
    this.homeUrl = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getHomeUrl
  /**
   ** Returns the value of the <code>homeUrl</code> property.
   **
   ** @return                    the value of the <code>homeUrl</code> property.
   **                            Possible object is {@link String}.
   */
  public String getHomeUrl() {
    return this.homeUrl;
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
}