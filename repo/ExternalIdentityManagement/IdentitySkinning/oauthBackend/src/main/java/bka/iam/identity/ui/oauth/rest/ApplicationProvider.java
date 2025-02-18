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

    File        :   ApplicationProvider.java

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
import bka.iam.identity.ui.oauth.model.Application;
import bka.iam.identity.ui.oauth.model.Scope;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.core.MediaType;

////////////////////////////////////////////////////////////////////////////////
 // class ApplicationProvider
 // ~~~~~ ~~~~~~~~
 /**
  ** Rest Provider with method to manipulate OAM OAuth <code>Application</code>
  **
  ** @author  tomas.t.sebo@oracle.com
  ** @version 1.0.0.0
  ** @since   1.0.0.0
  */
public class ApplicationProvider extends RESTAbstractProvider {
  
  private static final String className = ApplicationProvider.class.getName();
  private static Logger       logger = Logger.getLogger(className);
  
  private String url;
  private String username;
  private String password;
  private String identityDomainName;
  private WebResource resource;
  
  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ApplicationProvider</code> provider to OAuth Applications stored in OAM configuration
   ** accesed by REST API
   ** @param url REST API URL
   ** @param username REST API UserName
   ** @param password REST API Password
   ** @param domainName OAuth Domain Name stored in OAM Configuration
   ** <br>
   ** Default Constructor
   */
  public ApplicationProvider(String url, 
                             String username, 
                             String password, 
                             String domainName) {
    super();
    this.url = url;
    this.username = username;
    this.password = password;
    this.identityDomainName = domainName;
    this.resource = getWebResource(url,username, password);
  }
  
  //////////////////////////////////////////////////////////////////////////////  
  // Provider methods
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method: getApplications 
  /**
   ** Get list of the OAuth Applications
   ** @return List of OAuth Applications
   ** @throws OAuthException Throws exeption in case REST API  won't return HTTP Code 200
   */
  public List<Application> getApplications() throws OAuthException {
    String methodName = "getApplications";
    logger.entering(className,methodName);
    
    List<Application> applications = null;    
    ClientResponse response = resource.queryParam("identityDomainName", (identityDomainName == null ? "" : identityDomainName))
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
      applications = OAuthJSONMapper.unmarshalApplications(entity);
    }

    logger.exiting(className,methodName);
    return applications;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method: getApplications 
  /**
   ** Get OAuth application detail name based on the application name
   ** @param applicationName OAuth Appliaction name
   ** @return OAuth Appliaction details
   ** @throws OAuthException Throws exeption in case REST API  won't return HTTP Code 200
   */
  public Application getApplication(String applicationName) throws OAuthException {
    String methodName = "getApplication";
    logger.entering(className,methodName);
    
    Application application = null;    
    System.out.println("getApplication: "+applicationName);
    if(applicationName != null){
      System.out.println("Connection to: "+resource.getURI());
      ClientResponse response = resource.queryParam("identityDomainName", (identityDomainName == null ? "" : identityDomainName))
                                        .queryParam("name", applicationName)
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
        application = OAuthJSONMapper.unmarshalApplication(entity);
      }
    }
    else{
      application = new Application();
    }

    logger.exiting(className,methodName);
    return application;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method: createApplication 
  /**
   ** Create a new OAuth Application 
   ** @param application OAUth Applications details
   ** @throws OAuthException Throws exeption in case REST API  won't return HTTP Code 200
   */
  public void createApplication(Application application) throws OAuthException {
    String methodName = "createApplication";
    logger.entering(className,methodName);
    
    if(application != null){
      ClientResponse response = resource.header("Content-Type", "application/json")
                                        .header("Accept", "application/json")
                                        .entity(OAuthJSONMapper.marshalApplication(application).toString(),MediaType.APPLICATION_JSON)
                                        .post(ClientResponse.class);
      if(response != null){
        String entity = response.getEntity(String.class);
        logger.finer("Response Status = "+response.getStatus()+", Entity Data = "+entity);
        if(response.getStatus() != 200){
          throw new OAuthException(response.getStatus()+": "+entity);
        }
      }
    }
    
    logger.exiting(className,methodName);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method: updateApplication 
  /**
   ** Update OAuth Application
   ** @param application OAuth Appliaction details
   ** @throws OAuthException Throws exeption in case REST API  won't return HTTP Code 200
   */
  public void updateApplication(Application application) throws OAuthException {
    String methodName = "updateApplication";
    logger.entering(className,methodName);
    
    if(application != null && application.getResourceServerId() != null){
      
      ClientResponse response = resource.queryParam("id", application.getResourceServerId())
                                        .header("Content-Type", "application/json")
                                        .header("Accept", "application/json")
                                        .entity(OAuthJSONMapper.marshalApplication(application).toString(),MediaType.APPLICATION_JSON)
                                        .put(ClientResponse.class);
      if(response != null){
        String entity = response.getEntity(String.class);
        logger.finer("Response Status = "+response.getStatus()+", Entity Data = "+entity);
        if(response.getStatus() != 200){
          throw new OAuthException(response.getStatus()+": "+entity);
        }
      }
    }
    else{
      throw new OAuthException("Resource Server Id is missing");
    }
    
    logger.exiting(className,methodName);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method: deleteApplication 
  /**
   ** Delete OAuth Application
   ** @param applicationId OAUth Appliaction Id
   ** @throws OAuthException Throws exeption in case REST API  won't return HTTP Code 200
   */
  public void deleteApplication(String applicationId) throws OAuthException {
    String methodName = "deleteApplication";
    logger.entering(className,methodName);
    logger.finest("Delete Application with applicationId = "+applicationId);
    
    if(applicationId != null && applicationId.trim().length()>0){
      ClientResponse response = resource.queryParam("identityDomainName", (identityDomainName == null ? "" : identityDomainName))
                                        .queryParam("id", applicationId)
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
    }
    else{
      throw new OAuthException("Client Id is missing");
    }
    
    logger.exiting(className,methodName);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method: getAllScopes 
  /**
   ** Get all OAuth Application scopes
   ** @return All OAuth Appliaction scopes
   ** @throws OAuthException Throws exeption in case REST API  won't return HTTP Code 200
   */
  public List<Scope> getAllScopes() throws OAuthException {
    String methodName = "getAllScopes";
    logger.entering(className,methodName);
    
    List<Scope> allScopes = new ArrayList<Scope>();
    
    // Add all scopes from application
    List<Application> appliacations = getApplications();
    for(Application application : appliacations){
      List<Scope> scopes = application.getScopes();
      for(Scope scope: scopes){
        allScopes.add(new Scope(application.getName()+"."+scope.getScopeName(),scope.getDescription()));
      }
    }
    // Order values alphabeticaly based on scopeName
    if(allScopes.size() >0){
      Collections.sort(allScopes, new Comparator<Scope>(){
        @Override
        public int compare(Scope s1, Scope s2) {
          return s1.getScopeName().compareTo(s2.getScopeName());
        }
      });
    }
    
    logger.exiting(className,methodName);
    return allScopes;
  }
  
  public static void main(String[] args) throws OAuthException {
   /*
    ApplicationProvider ap = new ApplicationProvider("http://sp-instance.sub03231021150.bamfdevvcn.oraclevcn.com:7001/oam/services/rest/ssa/api/v1/oauthpolicyadmin/application", "weblogic", "Welcome1","DeleteME");
    
   // List<Application> applications = ap.getApplications();
   // System.out.println(applications);
    
    
   // Application application = ap.getApplication("del9");
   //System.out.println(application);
   
   //application.setDescription("My Description45");
   // ap.updateApplication(application);
   
    //IdentityDomain application = ap.getApplication("OIDCWebGateDomain");
    //System.out.println(domain);
    List<Scope> allScopes = ap.getAllScopes();
    System.out.println(allScopes);
  */
  }  
}