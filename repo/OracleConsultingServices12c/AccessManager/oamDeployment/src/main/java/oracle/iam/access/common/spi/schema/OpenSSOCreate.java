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

    File        :   OpenSSOCreate.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    OpenSSOCreate.


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
// class OpenSSOCreate
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
 **         &lt;element name="agentType"                 type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="agentVersion"              type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="agentPassword"             type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="agentDebugDir"             type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="agentAuditDir"             type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="agentAuditFileName"        type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="protectedAuthnScheme"      type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element ref="{}protectedResourcesList"                                                    minOccurs="0"/&gt;
 **         &lt;element ref="{}publicResourcesList"                                                       minOccurs="0"/&gt;
 **         &lt;element ref="{}excludedResourcesList"                                                     minOccurs="0"/&gt;
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
@XmlRootElement(name=OpenSSOCreate.LOCAL)
@XmlType(name="", propOrder={"agentBaseUrl", "applicationDomain", "autoCreatePolicy", "agentType", "agentVersion", "agentPassword", "agentDebugDir", "agentAuditDir", "agentAuditFileName", "protectedAuthnScheme", "protectedResource", "publicResource", "excludedResource"})
public class OpenSSOCreate extends BaseRequest {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String LOCAL = "OpenSSORegRequest";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @XmlElement(required=true)
  @XmlSchemaType(name="anyURI")
  protected String                 agentBaseUrl;
  protected String                 applicationDomain;
  protected Boolean                autoCreatePolicy;
  protected String                 agentType;
  protected String                 agentVersion;
  protected String                 agentPassword;
  protected String                 agentDebugDir;
  protected String                 agentAuditDir;
  protected String                 agentAuditFileName;
  protected String                 protectedAuthnScheme;
  @XmlElement(name=ProtectedResourcesList.LOCAL)
  protected ProtectedResourcesList protectedResource;
  @XmlElement(name=PublicResourcesList.LOCAL)
  protected PublicResourcesList    publicResource;
  @XmlElement(name=ExcludedResourcesList.LOCAL)
  protected ExcludedResourcesList  excludedResource;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>OpenSSOCreate</code> that allows use as a JavaBean.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  public OpenSSOCreate() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OpenSSOCreate</code> with the specified properties.
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
  public OpenSSOCreate(final String mode, final String serverAddress, final String username, final String password,  final String agentName) {
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
  // Method:   setExcludedResource
  /**
   ** Sets the value of the <code>excludedResource</code> property.
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
   **
   ** @return                    the value of the <code>excludedResource</code>
   **                            property.
   **                            Possible object is
   **                            {@link ExcludedResourcesList}.
   */
  public ExcludedResourcesList getExcludedResource() {
    return this.excludedResource;
  }
}