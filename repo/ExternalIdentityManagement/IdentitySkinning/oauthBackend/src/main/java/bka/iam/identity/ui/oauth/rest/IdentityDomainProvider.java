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

    File        :   IdentityDomainProvider.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   tomas.t.sebo@oracle.com

    Purpose     :   This file implements the class
                    Register.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2024-05-02  Tomas Sebo    First release version
*/
package bka.iam.identity.ui.oauth.rest;

import bka.iam.identity.ui.oauth.OAuthException;
import bka.iam.identity.ui.oauth.model.IdentityDomain;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.core.MediaType;

////////////////////////////////////////////////////////////////////////////////
 // class ApplicationProvider
 // ~~~~~ ~~~~~~~~
 /**
  ** Rest Provider with method to manipulate OAM OAuth <code>Identity Domain</code>
  **
  ** @author  tomas.t.sebo@oracle.com
  ** @version 1.0.0.0
  ** @since   1.0.0.0
  */
public class IdentityDomainProvider extends RESTAbstractProvider {
  
  private static final String className = IdentityDomainProvider.class.getName();
  private static Logger       logger = Logger.getLogger(className);
  
  private String url;
  private String username;
  private String password;
  private WebResource resource;
  
  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>IdentityDomainProvider</code> provider to OAuth Identity Domains stored in OAM configuration
   ** accesed by REST API
   ** @param url REST API URL
   ** @param username REST API UserName
   ** @param password REST API Password
   ** <br>
   ** Default Constructor
   */
  public IdentityDomainProvider(String url, String username, String password) {
    super();
    this.url = url;
    this.username = username;
    this.password = password;
    this.resource = getWebResource(url,username, password);
  }
  
  //////////////////////////////////////////////////////////////////////////////  
  // Provider methods
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method: getIdentityDomains 
  /**
   ** Get list of all OAuth Identity Domains
   ** @return List of all OAuth Identity Domains
   ** @throws OAuthException Throws exeption in case REST API  won't return HTTP Code 200
   */
  public List<IdentityDomain> getIdentityDomains() throws OAuthException{
    String methodName = "getIdentityDomains";
    logger.entering(className,methodName);
    
    List<IdentityDomain> identityDomains = null;    
    ClientResponse response = resource.header("Content-Type", "application/json")
                                      .header("Accept", "application/json")
                                      .header("Cache-Control","no-cache")
                                      .get(ClientResponse.class);

    if(response != null){
      String entity = response.getEntity(String.class);
      logger.finer("Response Status = "+response.getStatus()+", Entity Data = "+entity);
      if(response.getStatus() != 200){
        throw new OAuthException(response.getStatus()+": "+entity);
      }
      identityDomains = OAuthJSONMapper.unmarshalIdentityDomains(entity);
    }
    
    logger.exiting(className,methodName);
    return identityDomains;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method: getIdentityDomain 
  /**
   ** Get OAuth Identity Domain details
   ** @param domainName OAuth Identity Domain Name
   ** @return OAuth Identity Domain details
   ** @throws OAuthException Throws exeption in case REST API  won't return HTTP Code 200
   */
  public IdentityDomain getIdentityDomain(String domainName) throws OAuthException{
    String methodName = "getIdentityDomain";
    logger.entering(className,methodName);
    
    IdentityDomain identityDomain = null;
    // Call service /oam/services/rest/ssa/api/v1/oauthpolicyadmin/oauthidentitydomain on admin server and return ClientResponse
    ClientResponse response = resource.queryParam("name", (domainName == null ? "" : domainName))
                                      .header("Content-Type", "application/json")
                                      .header("Accept", "application/json")
                                      .header("Cache-Control","no-cache")
                                      .get(ClientResponse.class);

    if(response != null){
      String entity = response.getEntity(String.class);
      logger.finer("Response Status = "+response.getStatus()+", Entity Data = "+entity);
      if(response.getStatus() != 200){
        throw new OAuthException(response.getStatus()+": "+entity);
      }
      identityDomain = OAuthJSONMapper.unmarshalIdentityDomain(entity);
    }
    
    logger.exiting(className,methodName);
    return identityDomain;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method: createIdentityDomain 
  /**
   ** Create a new OAuth Identity Domain based on the provided details
   ** @param identityDomain OAuth Identity Domain details
   ** @throws OAuthException Throws exeption in case REST API  won't return HTTP Code 200
   */
  public void createIdentityDomain(IdentityDomain identityDomain) throws OAuthException{
    String methodName = "createIdentityDomain";
    logger.entering(className,methodName);
    
    String jsonIdentityDomain = OAuthJSONMapper.marshalIdentityDomain(identityDomain).toString();
    // Call service /oam/services/rest/ssa/api/v1/oauthpolicyadmin/oauthidentitydomain on admin server and return ClientResponse
    ClientResponse response = resource.header("Content-Type", "application/json")
                                      .header("Accept", "application/json")
                                      .entity(jsonIdentityDomain, MediaType.APPLICATION_JSON)
                                      .post(ClientResponse.class);
    if(response != null){
      String entity = response.getEntity(String.class);
      logger.finer("Response Status = "+response.getStatus()+", Entity Data = "+entity);
      if(response.getStatus() != 200){
        throw new OAuthException(response.getStatus()+": "+entity);
      }
    }
    
    logger.exiting(className,methodName);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method: updateIdentityDomain 
  /**
   ** Update OAuth Identity Domain based on provided details
   ** @param identityDomain OAuth Identity Domain details
   ** @throws OAuthException Throws exeption in case REST API  won't return HTTP Code 200
   */
  public void updateIdentityDomain(IdentityDomain identityDomain) throws OAuthException {
    String methodName = "updateIdentityDomain";
    logger.entering(className,methodName);
    
    String jsonIdentityDomain = OAuthJSONMapper.marshalIdentityDomain(identityDomain).toString();
    ClientResponse response = resource.header("Content-Type", "application/json")
                                      .header("Accept", "application/json")
                                      .entity(jsonIdentityDomain, MediaType.APPLICATION_JSON)
                                      .put(ClientResponse.class);
    if(response != null){
      String entity = response.getEntity(String.class);
      logger.finer("Response Status = "+response.getStatus()+", Entity Data = "+entity);
      if(response.getStatus() != 200){
        throw new OAuthException(response.getStatus()+": "+entity);
      }
    }
    
    logger.exiting(className,methodName);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method: deleteIdentityDomain 
  /**
   ** Delete OAuth Identity Domain name
   ** @param identityDomainName OAuth Identity Domain Name
   ** @throws OAuthException Throws exeption in case REST API  won't return HTTP Code 200
   */
  public void deleteIdentityDomain(String identityDomainName) throws OAuthException{
    String methodName = "deleteIdentityDomain";
    logger.entering(className,methodName);
    logger.finest("Delete Identity Domain with name = "+identityDomainName);
    
    
    // Call service /oam/services/rest/ssa/api/v1/oauthpolicyadmin/oauthidentitydomain on admin server and return ClientResponse
    ClientResponse response = resource.queryParam("name", (identityDomainName == null ? "" : identityDomainName))
                                      .header("Content-Type", "application/json")
                                      .header("Accept", "application/json")
                                      .delete(ClientResponse.class);
    if(response != null){
      String entity = response.getEntity(String.class);
      logger.finer("Response Status = "+response.getStatus()+", Entity Data = "+entity);
      if(response.getStatus() != 200){
        throw new OAuthException(response.getStatus()+": "+entity);
      }
    }
    
    logger.exiting(className,methodName);
  }
  
  
  public static void main(String[] args) throws OAuthException {
    /*
    IdentityDomainProvider idp = new IdentityDomainProvider("http://sp-instance.sub03231021150.bamfdevvcn.oraclevcn.com:7001/oam/services/rest/ssa/api/v1/oauthpolicyadmin/oauthidentitydomain", "weblogic", "Welcome1");

    List<IdentityDomain> domains = idp.getIdentityDomains();
    System.out.println(domains);
    IdentityDomain domain = idp.getIdentityDomain("OIDCWebGateDomain");
    System.out.println(domain);
    
    IdentityDomain myDomain = new IdentityDomain("mytestIdentityDomain7","DomainDescription","/consentPageURL","/errorPageURL","OUD");    
    idp.createIdentityDomain(myDomain);
    System.out.println(idp.getIdentityDomain("mytestIdentityDomain7"));
    //idp.deleteIdentityDomain("MyTestIdentityDomain");
    */
  }
  
}
