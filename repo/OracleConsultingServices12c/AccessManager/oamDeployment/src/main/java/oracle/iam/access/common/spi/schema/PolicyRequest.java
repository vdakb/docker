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

    File        :   PolicyRequest.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    PolicyRequest.


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
// class PolicyRequest
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
 **         &lt;element name="hostIdentifier"            type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element name="applicationDomain"         type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element name="protectedAuthnScheme"      type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element name="publicAuthnScheme"         type="{http://www.w3.org/2001/XMLSchema}string"  minOccurs="0"/&gt;
 **         &lt;element ref="{}protectedResourcesList"                                                    minOccurs="0"/&gt;
 **         &lt;element ref="{}publicResourcesList"                                                       minOccurs="0"/&gt;
 **         &lt;element ref="{}excludedResourcesList"                                                     minOccurs="0"/&gt;
 **         &lt;element ref="{}hostPortVariationsList"                                                    minOccurs="0"/&gt;
 **         &lt;element ref="{}rregApplicationDomain"                                                     minOccurs="0"/&gt;
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
@XmlRootElement(name=PolicyRequest.LOCAL)
@XmlType(name="", propOrder={"type", "mode", "username", "password", "serverAddress", "hostIdentifier", "applicationDomainName", "protectedAuthenticationScheme", "publicAuthenticationScheme", "protectedResource", "publicResource", "excludedResource", "hostPortVariationsList", "rregApplicationDomain"})
public class PolicyRequest {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String LOCAL = "PolicyRegRequest";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected String                 type;
  protected String                 mode;
  protected String                 username;
  protected String                 password;
  @XmlElement(required=true)
  @XmlSchemaType(name="anyURI")
  protected String                 serverAddress;
  @XmlElement(required=true)
  protected String                 hostIdentifier;
  @XmlElement(name="applicationDomainName", required=true)
  protected String                 applicationDomainName;
  @XmlElement(name="protectedAuthnScheme")
  protected String                 protectedAuthenticationScheme;
  @XmlElement(name="publicAuthnScheme")
  protected String                 publicAuthenticationScheme;
  @XmlElement(name=ProtectedResourcesList.LOCAL)
  protected ProtectedResourcesList protectedResource;
  @XmlElement(name=PublicResourcesList.LOCAL)
  protected PublicResourcesList    publicResource;
  @XmlElement(name=ExcludedResourcesList.LOCAL)
  protected ExcludedResourcesList  excludedResource;
  protected HostPortVariationsList hostPortVariationsList;
  protected RregApplicationDomain  rregApplicationDomain;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>PolicyRequest</code> that allows use as a
   ** JavaBean.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  public PolicyRequest() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>PolicyRequest</code> with the specified properties.
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
   ** @param  hostIdentifier     the initial hostIdentifier of the policy.
   **                            Allowed object is {@link String}.
   ** @param  applicationDomain  the initial applicationDomain of the agent.
   **                            Allowed object is {@link String}.
   */
  public PolicyRequest(final String mode, final String serverAddress, final String username, final String password, final String hostIdentifier, final String applicationDomain) {
    // ensure inheritance
    super();

    // initialize instance
    this.mode                  = mode;
    this.serverAddress         = serverAddress;
    this.username              = username;
    this.password              = password;
    this.hostIdentifier        = hostIdentifier;
    this.applicationDomainName = applicationDomain;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setType
  /**
   ** Sets the value of the <code>type</code> property.
   **
   ** @param  value              the value of the <code>type</code> property.
   **                            Allowed object is {@link String}.
   */
  public void setType(final String value) {
    this.type = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getType
  /**
   ** Returns the value of the <code>type</code> property.
   **
   ** @return                    the value of the <code>type</code> property.
   **                            Possible object is {@link String}.
   */
  public String getType() {
    return this.type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setMode
  /**
   ** Sets the value of the <code>mode</code> property.
   **
   ** @param  value              the value of the <code>mode</code> property.
   **                            Allowed object is {@link String}.
   */
  public void setMode(final String value) {
    this.mode = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getMode
  /**
   ** Returns the value of the <code>mode</code> property.
   **
   ** @return                    the value of the <code>mode</code> property.
   **                            Possible object is {@link String}.
   */
  public String getMode() {
    return this.mode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setUsername
  /**
   ** Sets the value of the <code>username</code> property.
   **
   ** @param  value              the value of the <code>username</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setUsername(final String value) {
    this.username = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUsername
  /**
   ** Returns the value of the <code>username</code> property.
   **
   ** @return                    the value of the <code>username</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getUsername() {
    return this.username;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPassword
  /**
   ** Sets the value of the <code>password</code> property.
   **
   ** @param  value              the value of the <code>password</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setPassword(final String value) {
    this.password = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPassword
  /**
   ** Returns the value of the <code>password</code> property.
   **
   ** @return                    the value of the <code>password</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getPassword() {
    return this.password;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setServerAddress
  /**
   ** Sets the value of the <code>serverAddress</code> property.
   **
   ** @param  value              the value of the <code>serverAddress</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setServerAddress(final String value) {
    this.serverAddress = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getServerAddress
  /**
   ** Returns the value of the <code>serverAddress</code> property.
   **
   ** @return                    the value of the <code>serverAddress</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getServerAddress() {
    return this.serverAddress;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setHostIdentifier
  /**
   ** Sets the value of the <code>hostIdentifier</code> property.
   **
   ** @param  value              the value of the <code>hostIdentifier</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setHostIdentifier(final String value) {
    this.hostIdentifier = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getHostIdentifier
  /**
   ** Returns the value of the <code>hostIdentifier</code> property.
   **
   ** @return                    the value of the <code>hostIdentifier</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getHostIdentifier() {
    return this.hostIdentifier;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setApplicationDomainName
  /**
   ** Sets the value of the <code>applicationDomainName</code> property.
   **
   ** @param  value              the value of the
   **                            <code>applicationDomainName</code> property.
   **                            Allowed object is {@link String}.
   */
  public void setApplicationDomainName(final String value) {
    this.applicationDomainName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getApplicationDomainName
  /**
   ** Returns the value of the <code>applicationDomainName</code> property.
   **
   ** @return                    the value of the
   **                            <code>applicationDomainName</code> property.
   **                            Possible object is {@link String}.
   */
  public String getApplicationDomainName() {
    return this.applicationDomainName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setProtectedAuthenticationScheme
  /**
   ** Sets the value of the <code>protectedAuthenticationScheme</code>
   ** property.
   **
   ** @param  value              the value of the
   **                            <code>protectedAuthenticationScheme</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setProtectedAuthenticationScheme(final String value) {
    this.protectedAuthenticationScheme = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getProtectedAuthenticationScheme
  /**
   ** Returns the value of the <code>protectedAuthenticationScheme</code>
   ** property.
   **
   ** @return                    the value of the
   **                            <code>protectedAuthenticationScheme</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getProtectedAuthenticationScheme() {
    return this.protectedAuthenticationScheme;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPublicAuthenticationScheme
  /**
   ** Sets the value of the <code>publicAuthenticationScheme</code> property.
   **
   ** @param  value              the value of the
   **                            <code>publicAuthenticationScheme</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setPublicAuthenticationScheme(final String value) {
    this.publicAuthenticationScheme = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPublicAuthenticationScheme
  /**
   ** Returns the value of the <code>publicAuthenticationScheme</code> property.
   **
   ** @return                    the value of the
   **                            <code>publicAuthenticationScheme</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getPublicAuthenticationScheme() {
    return this.publicAuthenticationScheme;
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
  // Method:   setHostPortVariationsList
  /**
   ** Sets the value of the hostPortVariationsList property.
   **
   ** @param  value              the value of the hostPortVariationsList property.
   **                            Allowed object is {@link HostPortVariationsList}.
   */
  public void setHostPortVariationsList(final HostPortVariationsList value) {
    this.hostPortVariationsList = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getHostPortVariationsList
  /**
   ** Returns the value of the hostPortVariationsList property.
   **
   ** @return                    the value of the hostPortVariationsList property.
   **                            Possible object is {@link HostPortVariationsList}.
   */
  public HostPortVariationsList getHostPortVariationsList() {
    return this.hostPortVariationsList;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setRregApplicationDomain
  /**
   ** Sets the value of the rregApplicationDomain property.
   **
   ** @param  value              the value of the rregApplicationDomain property.
   **                            Allowed object is {@link RregApplicationDomain}.
   */
  public void setRregApplicationDomain(final RregApplicationDomain value) {
    this.rregApplicationDomain = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getRregApplicationDomain
  /**
   ** Returns the value of the rregApplicationDomain property.
   **
   ** @return                    the value of the rregApplicationDomain property.
   **                            Possible object is {@link RregApplicationDomain}.
   */
  public RregApplicationDomain getRregApplicationDomain() {
    return this.rregApplicationDomain;
  }
}