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

    Copyright © 2021 All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Frontend Extension
    Subsystem   :   OAuth Registration

    File        :   OAMConnection.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   tomas.t.sebo@oracle.com

    Purpose     :   This file implements the class
                    Register.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2024-05-02  Tomas Sebo    First release version
*/
package bka.iam.identity.ui.oauth.model;

import java.io.Serializable;

////////////////////////////////////////////////////////////////////////////////
 // class OAMConnection
 // ~~~~~ ~~~~~~~~
 /**
  ** Java Bean represents OAM Connection String
  ** @author  tomas.t.sebo@oracle.com
  ** @version 1.0.0.0
  ** @since   1.0.0.0
  */
public class OAMConnection implements Serializable{
  
  private String name;
  private String host;
  private String port;
  private boolean secure;
  private String identityDomainURL;
  private String applicationURL;
  private String clientURL;
  private String username;
  private String password;
  
  private static final String DEFAULT_IDENTITY_DOMAIN_URL = "/oam/services/rest/ssa/api/v1/oauthpolicyadmin/oauthidentitydomain";
  private static final String DEFAULT_APPLICATION_URL = "/oam/services/rest/ssa/api/v1/oauthpolicyadmin/application";
  private static final String DEFAULT_CLIENT_URL = "/oam/services/rest/ssa/api/v1/oauthpolicyadmin/client";
  
  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OAMConnection</code> java bean used as OAuth <code>OAM Connection</code> value object
   ** <br>
   ** Default Constructor
   */
  public OAMConnection() {
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OAMConnection</code> java bean used as OAuth <code>OAM Connection</code> value object
   ** @param name OAM Connection user defined name
   ** @param host OAM HostName
   ** @param port OAM PortName
   ** @param secure True in case the connection is secure
   */
  public OAMConnection(String name, String host, String port, boolean secure) {
    super();
    this.name = name;
    this.host = host;
    this.port = port;
    this.secure = secure;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OAMConnection</code> java bean used as OAuth <code>OAM Connection</code> value object
   ** @param name OAM Connection user defined name
   ** @param host OAM HostName
   ** @param port OAM PortName
   ** @param secure True in case the connection is secure
   ** @param username REST API username
   ** @param password REST API password
   ** @param identityDomainURL Identity Domain URL default value is /oam/services/rest/ssa/api/v1/oauthpolicyadmin/oauthidentitydomain
   ** @param applicationURL Application URL default value is /oam/services/rest/ssa/api/v1/oauthpolicyadmin/application
   ** @param clientURL Client URL default value is /oam/services/rest/ssa/api/v1/oauthpolicyadmin/client
   */
  public OAMConnection(String name, String host, String port, boolean secure, 
                       String username, String password,
                       String identityDomainURL, String applicationURL, String clientURL) {
    this.name = name;
    this.host = host;
    this.port = port;
    this.secure = secure;
    this.username = username;
    this.password = password;
    this.identityDomainURL = identityDomainURL;
    this.applicationURL = applicationURL;
    this.clientURL = clientURL;
  }

  //////////////////////////////////////////////////////////////////////////////  
  // Getter and Setter method of Java Bean
  //////////////////////////////////////////////////////////////////////////////

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public String getHost() {
    return host;
  }

  public void setPort(String port) {
    this.port = port;
  }

  public String getPort() {
    return port;
  }

  public void setSecure(boolean secure) {
    this.secure = secure;
  }

  public boolean isSecure() {
    return secure;
  }

  public void setIdentityDomainURL(String identityDomainURL) {
    this.identityDomainURL = identityDomainURL;
  }

  public String getIdentityDomainURL() {
    return identityDomainURL;
  }

  public void setApplicationURL(String applicationURL) {
    this.applicationURL = applicationURL;
  }

  public String getApplicationURL1() {
    return applicationURL;
  }

  public void setClientURL(String clientURL) {
    this.clientURL = clientURL;
  }

  public String getClientURL1() {
    return clientURL;
  }


  public void setUsername(String username) {
    this.username = username;
  }

  public String getUsername() {
    return username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getPassword() {
    return password;
  }

  public String getIdentityDomainConnectionString(){
    
    String connection = null;
    if(this.identityDomainURL == null || this.identityDomainURL.trim().length() == 0){
      connection = (secure?"https://":"http://")+this.host+":"+this.port+DEFAULT_IDENTITY_DOMAIN_URL;
    }
    else{
      connection = (secure?"https://":"http://")+this.host+":"+this.port+this.identityDomainURL;
    }
    
    return connection;
  }
  
  public String getApplicationConnectionString(){
    
    String connection = null;
    if(this.applicationURL == null || this.applicationURL.trim().length() == 0){
      connection = (secure?"https://":"http://")+this.host+":"+this.port+DEFAULT_APPLICATION_URL;
    }
    else{
      connection = (secure?"https://":"http://")+this.host+":"+this.port+this.applicationURL;
    }
    return connection;
  }
  
  public String getClientConnectionSting(){
    String connection = null;
    if(this.clientURL == null || this.clientURL.trim().length() == 0){
      connection = (secure?"https://":"http://")+this.host+":"+this.port+DEFAULT_CLIENT_URL;
    }
    else{
      connection = (secure?"https://":"http://")+this.host+":"+this.port+this.clientURL;
    }
    return connection;
  }

  //////////////////////////////////////////////////////////////////////////////  
  // Override method of Java Bean
  //////////////////////////////////////////////////////////////////////////////

  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    sb.append("name=").append(this.name).append(", ");
    sb.append("host=").append(this.host).append(", ");
    sb.append("port=").append(this.port).append(", ");
    sb.append("secure=").append(this.secure).append(", ");
    sb.append("identityDomainURL=").append(this.identityDomainURL).append(", ");
    sb.append("applicationURL=").append(this.identityDomainURL).append(", ");
    sb.append("clientURL=").append(this.identityDomainURL).append(", ");
    sb.append("username=").append(this.username);
    sb.append("password=").append(this.password).append(",");
    
    sb.append("]");
    return sb.toString();
  }

}
