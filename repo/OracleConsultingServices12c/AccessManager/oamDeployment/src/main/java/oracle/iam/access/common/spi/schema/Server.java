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

    File        :   Server.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Server.


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
// class Server
// ~~~~~ ~~~~~~
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
 **         &lt;element name="host"             type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element name="port"             type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 **         &lt;element name="numOfConnections" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
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
@XmlRootElement(name=Server.LOCAL)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="", propOrder={"serverHost", "serverPort", "connectionMax"})
public class Server {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String LOCAL = "Server";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @XmlElement(name="host", required=true)
  protected String serverHost;
  @XmlElement(name="port")
  protected int    serverPort;
  @XmlElement(name="numOfConnections")
  protected int    connectionMax;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>Server</code> that allows use as a JavaBean.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  public Server() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setServerHost
  /**
   ** Sets the value of the <code>serverHost</code> property.
   **
   ** @param  value              the value of the <code>serverHost</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setServerHost(final String value) {
    this.serverHost = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getServerHost
  /**
   ** Returns the value of the <code>serverHost</code> property.
   **
   ** @return                    the value of the <code>serverHost</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getServerHost() {
    return this.serverHost;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setServerPort
  /**
   ** Sets the value of the <code>serverPort</code> property.
   **
   ** @param  value              the value of the <code>serverPort</code>
   **                            property.
   **                            Allowed object is <code>int</code>.
   */
  public void setServerPort(final int value) {
    this.serverPort = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getServerPort
  /**
   ** Returns the value of the <code>serverPort</code> property.
   **
   ** @return                    the value of the <code>serverPort</code>
   **                            property.
   **                            Possible object is <code>int</code>.
   */
  public int getServerPort() {
    return this.serverPort;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setConnectionMax
  /**
   ** Sets the value of the <code>connectionMax</code> property.
   **
   ** @param  value              the value of the <code>connectionMax</code>
   **                            property.
   **                            Allowed object is <code>int</code>.
   */
  public void setConnectionMax(final int value) {
    this.connectionMax = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getConnectionMax
  /**
   ** Returns the value of the <code>connectionMax</code> property.
   **
   ** @return                    the value of the <code>connectionMax</code>
   **                            property.
   **                            Possible object is <code>int</code>.
   */
  public int getConnectionMax() {
    return this.connectionMax;
  }
}