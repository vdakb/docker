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

    Copyright © 2019. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Frontend Extension
    Subsystem   :   Embedded Credential Collector

    File        :   Configuration.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Configuration.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2019-03-03  DSteding    First release version
*/

package bka.iam.platform.access.ecc.type;

import java.util.Map;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import oracle.hst.foundation.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// class Configuration
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** <code>Configuration</code> stores the configuration of the embeded
 ** <code>Credential Collector</code> configuration.
 ** <p>
 ** The configuration provides access to the actions regarding authentication
 ** and password reset.
 ** <br>
 ** Furthermore the configuration provides also the localization capabilities
 ** for the HTML forms to personalize the design.
 ** <p>
 ** The following schema fragment specifies the expected content contained
 ** within this class.
 ** <pre>
 **   &lt;complexType name="configuration"&gt;
 **     &lt;complexContent&gt;
 **       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **         &lt;sequence&gt;
 **           &lt;element name="authentication" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **           &lt;element name="resetpassword"  type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **           &lt;element name="pwrhost"        type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **           &lt;element name="network"        maxOccurs="unbounded" minOccurs="0"&gt;
 **             &lt;complexType&gt;
 **               &lt;complexContent&gt;
 **                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **                   &lt;sequence&gt;
 **                     &lt;element name="cidr"   type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **                     &lt;element name="symbol" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **                   &lt;/sequence&gt;
 **                 &lt;/restriction&gt;
 **               &lt;/complexContent&gt;
 **             &lt;/complexType&gt;
 **           &lt;/element&gt;
 **         &lt;/sequence&gt;
 **       &lt;/restriction&gt;
 **     &lt;/complexContent&gt;
 **   &lt;/complexType&gt;
 ** </pre>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Configuration {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the namespace definition of the configuration interface */
  public static final String  NAMESPACE       = "http://schemas.bka.bund.de/access/ecc/config";
  /**
   ** the schema definition to be used if configuration elements needs to be
   ** marshalled
   */
  public static final String  SCHEMA          = NAMESPACE + " Configuration.xsd";

  public static final String  ROOT            = "configuration";
  public static final String  AUTHENTICATION  = "authentication";
  public static final String  RESETPASSWORD   = "resetpassword";
  public static final String  PWRHOST         = "pwrhost";
  public static final String  NETWORK         = "network";
  public static final String  CIDR            = "cidr";
  public static final String  SYMBOL          = "symbol";

  public static final String  XFF             = "X-forwarded-for";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String              authentication;
  private String              resetpassword;
  private String              pwrhost;
  private Map<Subnet, String> network;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>Configuration</code> that allows use as a
   ** JavaBean.
   ** <p>
   ** Zero argumment constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  private Configuration() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Configuration</code> which populates its values from
   ** the specified properties.
   **
   ** @param  authentication     the URI invoked from the <b>Sign In</b> form to
   **                            authenticate an account leveraging Access
   **                            Manager capabilities.
   **                            Allowed object is {@link String}.
   ** @param  resetpassword      the URI invoked from the <b>Forgot Password</b>
   **                            form to reset the password and send it to the
   **                            specified e-Mail Address.
   **                            Allowed object is {@link String}.
   */
  private Configuration(final String authentication, final String resetpassword) {
    // ensure inheritance
    super();

    // initialize instace attributes
    this.authentication = authentication;
    this.resetpassword  = resetpassword;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Configuration</code> which populates its values from
   ** the specified properties.
   **
   ** @param  authentication     the URI invoked from the <b>Sign In</b> form to
   **                            authenticate an account leveraging Access
   **                            Manager capabilities.
   **                            Allowed object is {@link String}.
   ** @param  resetpassword      the URI invoked from the <b>Forgot Password</b>
   **                            form to reset the password and send it to the
   **                            specified e-Mail Address.
   **                            Allowed object is {@link String}.
   ** @param  pwrhost            the URI invoked from the Sign In password
   **                            forgot link which redirects to the password
   **                            reset application.
   **                            Allowed object is {@link String}.
   */
  private Configuration(final String authentication, final String resetpassword, final String pwrhost) {
    // ensure inheritance
    super();

    // initialize instace attributes
    this.authentication = authentication;
    this.resetpassword  = resetpassword;
    this.pwrhost        = pwrhost;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   requestIP
  /**
   ** Returns the IP addresse the user agent sent in a request.
   **
   ** @param  Request            the request context.
   **                            Allowed object is {@link HttpServletRequest}.
   **
   ** @return                    the the IP addresse the user agent sent in a
   **                            request.
   **                            Possible object is {@link String}.
   */
  public String requestIP(final HttpServletRequest request) {
    // the header is in the format X-Forwarded-For: client1, proxy1, proxy2
    // where the value is a comma+space separated list of IP addresses, the
    // left-most being the original client, and each successive proxy that
    // passed the request adding the IP address where it received the request
    // from.
    // In this example, the request passed through proxy1, proxy2, and then
    // proxy3 (not shown in the header). proxy3 appears as remote address of the
    // request.
    String requestIP = request.getHeader(XFF);
    if (StringUtility.isEmpty(requestIP))
      requestIP = request.getRemoteAddr();
    else {
      final String[] route = requestIP.split(", ");
      if (route.length == 1)
        requestIP = route[0];
      else if (route.length == 2)
        requestIP = route[1];
      else
        requestIP = request.getRemoteAddr();
    }
    return requestIP;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   symbol
  /**
   ** Returns the symbol for the given IP string the matches a network.
   **
   ** @param  address            the IP address of the agent that sent the
   **                            request.
   **                            Allowed object is {@link String}.
   **
   ** @return                    the symbol matched.
   **                            Possible object is {@link String}.
   */
  public String symbol(final String address) {
    String result = "/coa/john-doe.png";
    for (Map.Entry<Subnet, String> cursor : network().entrySet()) {
      if (cursor.getKey().inRange(address)) {
        result = cursor.getValue();
        break;
      }
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   authentication
  /**
   ** Sets the value of the <code>authentication</code> property.
   **
   ** @param  value              the value of the <code>authentication</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void authentication(final String value) {
    this.authentication = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getAuthentication
  /**
   ** Returns the value of the <code>authentication</code> property.
   **
   ** @return                    the value of the <code>authentication</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getAuthentication() {
    return this.authentication;
  }


  //////////////////////////////////////////////////////////////////////////////
  // Methode:   authentication
  /**
   ** Returns the value of the <code>authentication</code> property.
   **
   ** @return                    the value of the <code>authentication</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String authentication() {
    return this.authentication;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   resetpassword
  /**
   ** Sets the value of the <code>resetpassword</code> property.
   **
   ** @param  value              the value of the <code>resetpassword</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void resetpassword(final String value) {
    this.resetpassword = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getResetpassword
  /**
   ** Returns the value of the <code>resetpassword</code> property.
   **
   ** @return                    the value of the <code>resetpassword</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getResetpassword() {
    return this.resetpassword;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   resetpassword
  /**
   ** Returns the value of the <code>resetpassword</code> property.
   **
   ** @return                    the value of the <code>resetpassword</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String resetpassword() {
    return this.resetpassword;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   pwrhost
  /**
   ** Sets the value of the <code>pwrhost</code> property.
   **
   ** @param  value              the value of the <code>pwrhost</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void pwrhost(final String value) {
    this.pwrhost = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getPwrhost
  /**
   ** Returns the value of the <code>pwrhost</code> property.
   **
   ** @return                    the value of the <code>pwrhost</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getPwrhost() {
    return this.pwrhost;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   pwrhost
  /**
   ** Returns the value of the <code>pwrhost</code> property.
   **
   ** @return                    the value of the <code>pwrhost</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String pwrhost() {
    return this.pwrhost;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   network
  /**
   ** Returns the value of the <code>network</code> property.
   ** <p>
   ** This accessor method returns a reference to the live collection, not a
   ** snapshot. Therefore any modification you make to the returned collection
   ** will be present inside the JAXB object.
   ** <br>
   ** This is why there is not a <code>set</code> method for the custom
   ** property.
   ** <p>
   ** For example, to add a new item, do as follows:
   ** <pre>
   **    network().add(newItem);
   ** </pre>
   ** <p>
   ** Objects of the following type(s) are allowed in the map
   ** {@link Subnet}s for the key and {@link String}s for the value.
   **
   ** @return                    the associated {@link Map} of {@link Subnet}s.
   **                            Returned value is never <code>null</code>.
   */
  public Map<Subnet, String> network() {
    if (this.network == null)
      this.network = new HashMap<Subnet, String>();

    return this.network;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an empty <code>Configuration</code>.
   **
   **
   ** @return                    an newly created instance of
   **                            <code>Configuration</code>.
   **                            Possible object <code>Configuration</code>.
   */
  public static Configuration build() {
    return new Configuration();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>Configuration</code> that takes URI's
   ** for forwarding authentication and reset password.
   **
   ** @param  authentication     the URI invoked from the <b>Sign In</b> form to
   **                            authenticate an account leveraging Access
   **                            Manager capabilities.
   **                            Allowed object is {@link String}.
   ** @param  resetpassword      the URI invoked from the <b>Forgot Password</b>
   **                            form to reset the password and send it to the
   **                            specified e-Mail Address.
   **                            Allowed object is {@link String}.
   **
   ** @return                    an newly created instance of
   **                            <code>Locator</code>.
   **                            Possible object <code>Locator</code>.
   */
  public static Configuration build(final String authentication, final String resetpassword) {
    return new Configuration(authentication, resetpassword);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>Configuration</code> that takes URI's
   ** for forwarding authentication and reset password.
   **
   ** @param  authentication     the URI invoked from the <b>Sign In</b> form to
   **                            authenticate an account leveraging Access
   **                            Manager capabilities.
   **                            Allowed object is {@link String}.
   ** @param  resetpassword      the URI invoked from the <b>Forgot Password</b>
   **                            form to reset the password and send it to the
   **                            specified e-Mail Address.
   **                            Allowed object is {@link String}.
   ** @param  pwrhost            the URI invoked from the Sign In password
   **                            forgot link which redirects to the password
   **                            reset application.
   **                            Allowed object is {@link String}.
   ** @return                    an newly created instance of
   **                            <code>Configuration</code>.
   **                            Possible object <code>Configuration</code>.
   */
  public static Configuration build(final String authentication, final String resetpassword, final String pwrhost) {
    return new Configuration(authentication, resetpassword, pwrhost);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reset
  /**
   ** Reset the entire <code>Configuration</code> to be empty.
   */
  public void reset() {
    // remove any settings
    this.network().clear();
  }
}