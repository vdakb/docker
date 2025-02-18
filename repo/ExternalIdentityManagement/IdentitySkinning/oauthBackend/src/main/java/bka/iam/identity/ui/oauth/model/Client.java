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

    File        :   Client.java

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

import java.util.ArrayList;
import java.util.List;

////////////////////////////////////////////////////////////////////////////////
 // class Client
 // ~~~~~ ~~~~~~~~
 /**
  ** Java Bean represents OAM OAuth <code>Client</code>
  **
  ** @author  tomas.t.sebo@oracle.com
  ** @version 1.0.0.0
  ** @since   1.0.0.0
  */
public class Client implements Serializable{
  
  private String id;
  private String name;
  private String description;
  private String idDomain;
  private String clientType;
  private String defaultScope;
  private String secret;
  private String usePKCE;
  private String tokenEndpointAuthMethod;
  private boolean issueTLSClientCertificateBoundAccessTokens;
  private String tlsClientAuthSANDNS;
  private String tlsClientAuthSANEmail;
  private String tlsClientAuthSANIP;
  private String tlsClientAuthSANURI;
  private String tlsClientAuthSubjectDN;
  
  private List<SingleAttribute> grantTypes;
  private List<SingleAttribute> scopes;
  private List<SingleAttribute> idTokenCustomClaims;
  private List<SingleAttribute> accessTokenCustomClaims;
  private List<SingleAttribute> userInfoCustomClaims;
  
  private List<RedirectURI>    redirectURIs;  
  private List<TokenAttribute> attributes;  
  private List<Scope>          allScopes;

  private boolean isIssueTLSClientCertificateBoundAccessTokensSet;
  
  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Client</code> java bean used as OAuth <code>Client</code> value object
   ** <br>
   ** Default Constructor
   */
  public Client() {
    super();
    
    grantTypes              = new ArrayList<SingleAttribute>();
    scopes                  = new ArrayList<SingleAttribute>();
    idTokenCustomClaims     = new ArrayList<SingleAttribute>();
    accessTokenCustomClaims = new ArrayList<SingleAttribute>();
    userInfoCustomClaims    = new ArrayList<SingleAttribute>();
    
    redirectURIs            = new ArrayList<RedirectURI>();
    attributes              = new ArrayList<TokenAttribute>();
  }

  //////////////////////////////////////////////////////////////////////////////  
  // Getter and Setter method of Java Bean
  //////////////////////////////////////////////////////////////////////////////
  
  public void setId(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  public void setIdDomain(String idDomain) {
    this.idDomain = idDomain;
  }

  public String getIdDomain() {
    return idDomain;
  }

  public void setClientType(String clientType) {
    this.clientType = clientType;
  }

  public String getClientType() {
    return clientType;
  }

  public void setDefaultScope(String defaultScope) {
    this.defaultScope = defaultScope;
  }

  public String getDefaultScope() {
    return defaultScope;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }

  public String getSecret() {
    return secret;
  }

  public void setUsePKCE(String usePKCE) {
    this.usePKCE = usePKCE;
  }

  public String getUsePKCE() {
    return usePKCE;
  }

  public void setTokenEndpointAuthMethod(String tokenEndpointAuthMethod) {
    this.tokenEndpointAuthMethod = tokenEndpointAuthMethod;
  }

  public String getTokenEndpointAuthMethod() {
    return tokenEndpointAuthMethod;
  }

  public void setIssueTLSClientCertificateBoundAccessTokens(boolean issueTLSClientCertificateBoundAccessTokens) {
    this.isIssueTLSClientCertificateBoundAccessTokensSet = true;
    this.issueTLSClientCertificateBoundAccessTokens = issueTLSClientCertificateBoundAccessTokens;
  }

  public boolean isIssueTLSClientCertificateBoundAccessTokens() {
    return issueTLSClientCertificateBoundAccessTokens;
  }

  public void setTlsClientAuthSANDNS(String tlsClientAuthSANDNS) {
    this.tlsClientAuthSANDNS = tlsClientAuthSANDNS;
  }

  public String getTlsClientAuthSANDNS() {
    return tlsClientAuthSANDNS;
  }

  public void setTlsClientAuthSANEmail(String tlsClientAuthSANEmail) {
    this.tlsClientAuthSANEmail = tlsClientAuthSANEmail;
  }

  public String getTlsClientAuthSANEmail() {
    return tlsClientAuthSANEmail;
  }

  public void setTlsClientAuthSANIP(String tlsClientAuthSANIP) {
    this.tlsClientAuthSANIP = tlsClientAuthSANIP;
  }

  public String getTlsClientAuthSANIP() {
    return tlsClientAuthSANIP;
  }

  public void setTlsClientAuthSANURI(String tlsClientAuthSANURI) {
    this.tlsClientAuthSANURI = tlsClientAuthSANURI;
  }

  public String getTlsClientAuthSANURI() {
    return tlsClientAuthSANURI;
  }

  public void setTlsClientAuthSubjectDN(String tlsClientAuthSubjectDN) {
    this.tlsClientAuthSubjectDN = tlsClientAuthSubjectDN;
  }

  public String getTlsClientAuthSubjectDN() {
    return tlsClientAuthSubjectDN;
  }

  public void setGrantTypes(List<SingleAttribute> grantTypes) {
    this.grantTypes = grantTypes;
  }

  public List<SingleAttribute> getGrantTypes() {
    return grantTypes;
  }

  public void setScopes(List<SingleAttribute> scopes) {
    this.scopes = scopes;
  }

  public List<SingleAttribute> getScopes() {
    return scopes;
  }

  public void setIdTokenCustomClaims(List<SingleAttribute> idTokenCustomClaims) {
    this.idTokenCustomClaims = idTokenCustomClaims;
  }

  public List<SingleAttribute> getIdTokenCustomClaims() {
    return idTokenCustomClaims;
  }

  public void setAccessTokenCustomClaims(List<SingleAttribute> accessTokenCustomClaims) {
    this.accessTokenCustomClaims = accessTokenCustomClaims;
  }

  public List<SingleAttribute> getAccessTokenCustomClaims() {
    return accessTokenCustomClaims;
  }

  public void setUserInfoCustomClaims(List<SingleAttribute> userInfoCustomClaims) {
    this.userInfoCustomClaims = userInfoCustomClaims;
  }

  public List<SingleAttribute> getUserInfoCustomClaims() {
    return userInfoCustomClaims;
  }

  public void setRedirectURIs(List<RedirectURI> redirectURIs) {
    this.redirectURIs = redirectURIs;
  }

  public List<RedirectURI> getRedirectURIs() {
    return redirectURIs;
  }

  public void setAttributes(List<TokenAttribute> attributes) {
    this.attributes = attributes;
  }

  public List<TokenAttribute> getAttributes() {
    return attributes;
  }

  public void setAllScopes(List<Scope> allScopes) {
    this.allScopes = allScopes;
  }

  public List<Scope> getAllScopes() {
    return allScopes;
  }

  public boolean isIssueTLSClientCertificateBoundAccessTokensSet() {
    return isIssueTLSClientCertificateBoundAccessTokensSet;
  }

  //////////////////////////////////////////////////////////////////////////////  
  // Override method of Java Bean
  //////////////////////////////////////////////////////////////////////////////

  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    sb.append("name=").append(this.name).append(", ");
    sb.append("description=").append(this.description).append(", ");
    sb.append("idDomain=").append(this.idDomain).append(", ");
    sb.append("clientType=").append(this.clientType).append(", ");
    sb.append("defaultScope=").append(this.defaultScope).append(", ");
    sb.append("secret=").append(this.secret).append(", ");
    sb.append("usePKCE=").append(this.usePKCE).append(", ");
    sb.append("tokenEndpointAuthMethod=").append(this.tokenEndpointAuthMethod).append(", ");
    sb.append("issueTLSClientCertificateBoundAccessTokens=").append(this.issueTLSClientCertificateBoundAccessTokens).append(", ");
    sb.append("tlsClientAuthSANDNS=").append(this.tlsClientAuthSANDNS).append(", ");
    sb.append("tlsClientAuthSANEmail=").append(this.tlsClientAuthSANEmail).append(", ");
    sb.append("tlsClientAuthSANIP=").append(this.tlsClientAuthSANIP).append(", ");
    sb.append("tlsClientAuthSANURI=").append(this.tlsClientAuthSANURI).append(", ");
    sb.append("tlsClientAuthSubjectDN=").append(this.tlsClientAuthSubjectDN).append(", ");
    sb.append("grantTypes=").append(this.grantTypes).append(", ");
    sb.append("scopes=").append(this.scopes).append(", ");
    sb.append("idTokenCustomClaims=").append(this.idTokenCustomClaims).append(", ");
    sb.append("accessTokenCustomClaims=").append(this.accessTokenCustomClaims).append(", ");
    sb.append("userInfoCustomClaims=").append(this.userInfoCustomClaims).append(", ");
    sb.append("redirectURIs=").append(this.redirectURIs).append(", ");
    sb.append("attributes=").append(this.attributes);
    sb.append("]");
    return sb.toString();
  }
}
