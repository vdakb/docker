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

    File        :   HostPortVariations.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    HostPortVariations.


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
// class HostPortVariations
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** Represents the host and port elements for each of a load-balancer URL that
 ** will be protected by an <code>Access Agent</code>s.
 ** <p>
 ** Java class for anonymous complex type.
 ** <p>
 ** The following schema fragment specifies the expected content contained
 ** within this class.
 ** <pre>
 ** &lt;complexType&gt;
 **   &lt;complexContent&gt;
 **     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **       &lt;sequence&gt;
 **         &lt;element name="host" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element name="port" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
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
@XmlType(name="", propOrder={"host", "port"})
@XmlRootElement(name=HostPortVariations.LOCAL)
public class HostPortVariations {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String LOCAL = "hostPortVariations";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @XmlElement(required=true)
  protected String  host;
  protected Integer port;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>HostPortVariations</code> that allows use as a
   ** JavaBean.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  public HostPortVariations() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setHost
  /**
   ** Sets the value of the <code>host</code> property.
   **
   ** @param  value              the value of the <code>host</code> property.
   **                            Allowed object is {@link String}.
   */
  public void setHost(final String value) {
    this.host = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getHost
  /**
   ** Returns the value of the <code>host</code> property.
   **
   ** @return                    the value of the <code>host</code> property.
   **                            Possible object is {@link String}.
   */
  public String getHost() {
    return this.host;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPort
  /**
   ** Sets the value of the <code>port</code> property.
   **
   ** @param  value              the port the host is listening on.
   **                            Allowed object {@link Integer}.
   */
  public void setPort(final Integer value) {
    this.port = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPort
  /**
   ** Returns the value of the <code>port</code> property.
   **
   ** @return                    the port the host is listening on.
   **                            Possible object {@link Integer}.
   */
  public Integer getPort() {
    return this.port;
  }
}