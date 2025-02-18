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
    Subsystem   :   Identity Provider Discovery

    File        :   Configuration.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Configuration.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2019-03-03  DSteding    First release version
    1.0.0.1     2023-05-22  TSebo	Added support for Email Templates
*/

package bka.iam.platform.access.idp.type;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.io.UnsupportedEncodingException;

import java.net.URL;
import java.net.URLDecoder;
import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;

import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.CollectionUtility;

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
 **           &lt;element name="provider" maxOccurs="unbounded" minOccurs="0"&gt;
 **             &lt;complexType&gt;
 **               &lt;complexContent&gt;
 **                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **                   &lt;sequence&gt;
 **                     &lt;element name="cidr"    type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **                     &lt;element name="name"    type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **                     &lt;element name="symbol"  type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **                     &lt;element name="partner" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **                   &lt;/sequence&gt;
 **                 &lt;/restriction&gt;
 **               &lt;/complexContent&gt;
 **             &lt;/complexType&gt;
 **           &lt;/element&gt;
 **           &lt;element name="network" maxOccurs="unbounded" minOccurs="0"&gt;
 **             &lt;complexType&gt;
 **               &lt;complexContent&gt;
 **                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **                   &lt;sequence&gt;
 **                     &lt;element name="cidr"    type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **                     &lt;element name="id"      type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **                   &lt;/sequence&gt;
 **                 &lt;/restriction&gt;
 **               &lt;/complexContent&gt;
 **             &lt;/complexType&gt;
 **           &lt;/element&gt;
 **           &lt;element name="otptemplate" maxOccurs="unbounded" minOccurs="0"&gt;
 **             &lt;complexType&gt;
 **               &lt;complexContent&gt;
 **                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **                   &lt;sequence&gt;
 **                     &lt;element name="location"  type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **                     &lt;element name="subject"   type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **                     &lt;element name="body"      type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
  public static final String    NAMESPACE       = "http://schemas.bka.bund.de/access/idp/config";
  /**
   ** the schema definition to be used if configuration elements needs to be
   ** marshalled
   */
  public static final String    SCHEMA          = NAMESPACE + " Configuration.xsd";

  public static final String    ROOT            = "configuration";
  public static final String    PROVIDER        = "provider";
  public static final String    NETWORK         = "network";
  public static final String    ID              = "id";
  public static final String    NAME            = "name";
  public static final String    CIDR            = "cidr";
  public static final String    SYMBOL          = "symbol";
  public static final String    PARTNER         = "partner";
  
  public static final String    OTP_TEMPLATE    = "otptemplate";
  public static final String    LOCALE          = "locale";
  public static final String    SUBJECT         = "subject";
  public static final String    BODY            = "body";

  public static final String    XFF             = "X-forwarded-for";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private Map<Subnet, Provider> network;
  private Map<String, Provider> provider;
  private Set<OTPTemplate>      otpTemplate;

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
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getProvider
  /**
   ** Returns the values of the <code>provider</code> property.
   **
   ** @return                    the associated {@link Map} of {@link String}s.
   **                            Returned value is never <code>null</code>.
   */
  public List<Provider> getProvider() {
    final List<Provider> sorted = CollectionUtility.list(this.provider.values());
    Collections.sort(sorted, new Provider.Name());
    return sorted;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   requestIP
  /**
   ** Returns the IP addresse the user agent sent in a request.
   **
   ** @param  request            the request context.
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
  // Methode:   provider
  /**
   ** Returns the Identity Provider for the given IP string the matches a
   ** network.
   **
   ** @param  address            the IP address of the agent that sent the
   **                            request.
   **                            Allowed object is {@link String}.
   **
   ** @return                    the symbol matched.
   **                            Possible object is {@link String}.
   */
  public Provider provider(final String address) {
    Provider result = Provider.of("$", "§", "john.doe", "#");

    for (Map.Entry<Subnet, Provider> cursor : network().entrySet()) {
      if (cursor.getKey().inRange(address)) {
        result = cursor.getValue();
        break;
      }

    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   provider
  /**
   ** Returns the value of the <code>provider</code> property.
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
   **    provider().put(newItem);
   ** </pre>
   ** <p>
   ** Objects of the following type(s) are allowed in the map
   ** {@link String}s for the key and {@link Provider}s for the value.
   **
   ** @return                    the associated {@link Map} of {@link String}s.
   **                            Returned value is never <code>null</code>.
   */
  public Map<String, Provider> provider() {
    if (this.provider == null)
      this.provider = new HashMap<String, Provider>();

    return this.provider;
  }

	//////////////////////////////////////////////////////////////////////////////
	// Method:   findProviderId
	/**
	 ** Parse input parameter @param resource_url and from query parameters exctract idp_name prameter value.
	 ** Extracted idp_name value is searched in all registerd providers, value is compared with partner name.
	 ** When the match if found. Registered partner Id is returned.
	 ** 
	 **
	 ** @return                    Possible object is {@link String}.
	 */
	public String findProviderId(String resource_url) {
		String providerId = "#";
	  String idpName = null;
		if (this.provider != null && resource_url != null){
			try {
				// Parse prosource_url parameter and get parameter idp_name value
			  URL url = new java.net.URL(java.net.URLDecoder.decode(resource_url,"UTF-8"));
			  String query = url.getQuery();
				if(query != null){
					String[] parameters = query.split("&");
					for (String parameter : parameters) {
						int idx = parameter.indexOf("=");
						String parameterName  = URLDecoder.decode(parameter.substring(0, idx), "UTF-8");
						if("idp_name".equals(parameterName)){
							idpName = URLDecoder.decode(parameter.substring(idx + 1), "UTF-8");;
							break;
						}
					}
				}
				// Find providerId related to idp_name (providerName)
				if(idpName != null){
					for(Provider provider : this.provider.values()){ 
						if(idpName.equalsIgnoreCase(provider.getPartner())){
							providerId = provider.getId();
							break;
						}
					}		
				}
			} 
			catch (UnsupportedEncodingException | MalformedURLException e) {
			  e.printStackTrace();
			}

		}
		return providerId;
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
   **    network().put(newItem);
   ** </pre>
   ** <p>
   ** Objects of the following type(s) are allowed in the map
   ** {@link Subnet}s for the key and {@link String}s for the value.
   **
   ** @return                    the associated {@link Map} of {@link Subnet}s.
   **                            Returned value is never <code>null</code>.
   */
  public Map<Subnet, Provider> network() {
    if (this.network == null)
      this.network = new HashMap<Subnet, Provider>();

    return this.network;
  }
  
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   network
  /**
   ** Returns the value of the <code>otpTemplate</code> property.
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
   **    otpTemplate().add(newItem);
   ** </pre>
   ** <p>
   ** Objects of the following type(s) are allowed in the map
   ** {@link OTPTemplate}s for the key and {@link String}s for the value.
   **
   ** @return                    the associated {@link Map} of {@link OTPTemplate}s.
   **                            Returned value is never <code>null</code>.
   */
  public Set<OTPTemplate> otpTemplate() {
    if (this.otpTemplate == null)
      this.otpTemplate = new HashSet<OTPTemplate>();
    
    return this.otpTemplate;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an empty <code>Configuration</code>.
   **
   ** @return                    an newly created instance of
   **                            <code>Configuration</code>.
   **                            <br>
   **                            Possible object is <code>Configuration</code>.
   */
  public static Configuration build() {
    return new Configuration();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reset
  /**
   ** Reset the entire <code>Configuration</code> to be empty.
   */
  public void reset() {
    // remove any settings
    this.network().clear();
    this.provider().clear();
  }
}