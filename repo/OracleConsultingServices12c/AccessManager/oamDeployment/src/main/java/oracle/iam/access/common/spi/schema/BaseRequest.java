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

    File        :   BaseRequest.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    BaseRequest.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.common.spi.schema;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

////////////////////////////////////////////////////////////////////////////////
// class BaseRequest
// ~~~~~ ~~~~~~~~~~~
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
@XmlRootElement(name=BaseRequest.LOCAL)
@XmlType(name="", propOrder={"type", "mode", "username", "password", "serverAddress", "hostIdentifier", "agentName"})
@XmlSeeAlso({Agent10gCreate.class, Agent10gUpdate.class, Agent11gCreate.class, Agent11gUpdate.class, OrclSSOCreate.class, OrclSSOUpdate.class, OpenSSOCreate.class, OpenSSOUpdate.class})
public class BaseRequest {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String LOCAL        = "BasicRegRequest";

  public static final String TYPE_AGENT10 = "OAM";
  public static final String TYPE_AGENT11 = "OAM11G";
  public static final String TYPE_AGENT12 = null;  
  public static final String TYPE_OPENSSO = "OPENSSO";
  public static final String TYPE_ORCLSSO = "OSSO";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected String type;
  protected String mode;
  protected String username;
  protected String password;
  @XmlElement(required=true)
  @XmlSchemaType(name="anyURI")
  protected String serverAddress;
  protected String hostIdentifier;
  @XmlElement(required=true)
  protected String agentName;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>BaseRequest</code> that allows use as a JavaBean.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  public BaseRequest() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>BaseRequest</code> with the specified properties.
   **
   ** @param  mode               the operational mode to apply.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  serverAddress      the initial serverAddress of the request.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  username           the initial username of the request to
   **                            authenticate on the remote side.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  password           the initial password of the request to
   **                            authenticate on the remote side.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  agentName          the initial agentName of the agent.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public BaseRequest(final String mode, final String serverAddress, final String username, final String password,  final String agentName) {
    // ensure inheritance
    this(null, mode, serverAddress, username, password, agentName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>BaseRequest</code> with the specified properties.
   **
   ** @param  type               the agent type  to apply at the request.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  mode               the operational mode to apply.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  serverAddress      the initial serverAddress of the request.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  username           the initial username of the request to
   **                            authenticate on the remote side.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  password           the initial password of the request to
   **                            authenticate on the remote side.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  agentName          the initial agentName of the agent.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  protected BaseRequest(final String type, final String mode, final String serverAddress, final String username, final String password,  final String agentName) {
    // ensure inheritance
    super();

    // initialize instance
    this.type          = type;
    this.mode          = mode;
    this.serverAddress = serverAddress;
    this.agentName     = agentName;
    this.username      = username;
    this.password      = password;
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
   **                            <br>
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
   **                            <br>
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
   **                            <br>
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
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getMode() {
    return this.mode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setUsername
  /**
   ** Sets the value of the <code>password</code> property.
   **
   ** @param  value              the value of the <code>password</code>
   **                            property.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setUsername(final String value) {
    this.username = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUsername
  /**
   ** Returns the value of the <code>password</code> property.
   **
   ** @return                    the value of the <code>password</code>
   **                            property.
   **                            <br>
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
   **                            <br>
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
   **                            <br>
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
   **                            <br>
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
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getServerAddress() {
    return this.serverAddress;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setHostIdentifier
  /**
   ** Sets the value of the <code>hostIdentifier</code> property.
   ** <p>
   ** This parameter describes the Web Server domain on which the
   ** <code>Access Agent</code> is deployed, for instance,
   ** <code>.example.com</code>.
   **
   ** @param  value              the value of the <code>hostIdentifier</code>
   **                            property.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setHostIdentifier(final String value) {
    this.hostIdentifier = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getHostIdentifier
  /**
   ** Returns the value of the <code>hostIdentifier</code> property.
   ** <p>
   ** This parameter describes the Web Server domain on which the
   ** <code>Access Agent</code> is deployed, for instance,
   ** <code>.example.com</code>.
   **
   ** @return                    the value of the <code>hostIdentifier</code>
   **                            property.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getHostIdentifier() {
    return this.hostIdentifier;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAgentName
  /**
   ** Sets the value of the <code>agentName</code> property.
   **
   ** @param  value              the value of the <code>agentName</code>
   **                            property.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setAgentName(final String value) {
    this.agentName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAgentName
  /**
   ** Returns the value of the <code>agentName</code> property.
   **
   ** @return                    the value of the <code>agentName</code>
   **                            property.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getAgentName() {
    return this.agentName;
  }
}