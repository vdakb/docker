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

    File        :   IpValidationExceptions.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    IpValidationExceptions.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.common.spi.schema;

import java.util.List;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;

////////////////////////////////////////////////////////////////////////////////
// class IpValidationExceptions
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** IP address validation is specific to <code>Access Agent</code>s.
 ** <br>
 ** It determines if a client's IP address is the same as the IP address stored
 ** in the <b><code>ObSSOCookie</code></b> generated for single sign-on. The
 ** <code>ipValidation</code> parameter turns IP address validation on and off.
 ** If <code>ipValidation</code> is <code>true</code>, the IP address stored in
 ** the <b><code>ObSSOCookie</code></b> must match the client's IP address,
 ** otherwise, the cookie is rejected and the user must reauthenticate. The
 ** default <code>ipValidation</code> setting is <code>true</code>.
 ** <p>
 ** The <code>ipValidation</code> parameter parameter can cause problems with
 ** certain Web applications. For example, Web applications managed by a proxy
 ** server typically change the user's IP address, substituting the IP address
 ** of the proxy. This prevents single sign-on using the
 ** <b><code>ObSSOCookie</code></b>.
 ** <p>
 ** The IP Validation Exceptions lists IP addresses that are exceptions to this
 ** process. If <code>ipValidation</code> is <code>true</code>, the IP address
 ** can be compared to the IP Validation Exceptions list. If the address is
 ** found on the exceptions list, it does not need to match the IP address
 ** stored in the cookie. You can add as many IP addresses as needed. These
 ** addresses are the actual IP addresses of the client, not the IP addresses
 ** that are stored in the <b><code>ObSSOCookie</code></b>. If a cookie arrives
 ** from one of the exception IP addresses, the Access System ignores the
 ** address stored in the <b><code>ObSSOCookie</code></b> cookie for validation.
 ** <br>
 ** For example, the IP addresses in the IP Validation Exceptions can be used
 ** when the IP address in the cookie is for a reverse proxy.
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
 **         &lt;element name="ipAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" maxOccurs="unbounded"/&gt;
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
@XmlType(name="", propOrder={"ipAddress"})
@XmlRootElement(name=IpValidationExceptions.LOCAL)
public class IpValidationExceptions {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String LOCAL = "ipValidationExceptions";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected List<String> ipAddress;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>IpValidationExceptions</code> that allows use as
   ** a JavaBean.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  public IpValidationExceptions() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getIpAddress
  /**
   ** Returns the value of the <code>ipAddress</code> property.
   ** <p>
   ** This accessor method returns a reference to the live list, not a snapshot.
   ** Therefore any modification you make to the returned list will be present
   ** inside the JAXB object.
   ** <br>
   ** This is why there is not a <code>set</code> method for the custom
   ** property.
   ** <p>
   ** For example, to add a new item, do as follows:
   ** <pre>
   **    getIpAddress().add(newItem);
   ** </pre>
   ** <p>
   ** Objects of the following type(s) are allowed in the {@link List}:
   ** {@link String}s.
   **
   ** @return                    the associated {@link List} of {@link String}s.
   **                            Returned value is never <code>null</code>.
   */
  public List<String> getIpAddress() {
    if (this.ipAddress == null)
      this.ipAddress = new ArrayList<String>();

    return this.ipAddress;
  }
}