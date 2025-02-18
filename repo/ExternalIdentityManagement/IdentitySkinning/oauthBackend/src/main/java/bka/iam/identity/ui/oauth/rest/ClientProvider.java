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

    File        :   ClientProvider.java

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
import bka.iam.identity.ui.oauth.model.Client;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.core.MediaType;

////////////////////////////////////////////////////////////////////////////////
 // class ApplicationProvider
 // ~~~~~ ~~~~~~~~
 /**
  ** Rest Provider with method to manipulate OAM OAuth <code>Client</code>
  **
  ** @author  tomas.t.sebo@oracle.com
  ** @version 1.0.0.0
  ** @since   1.0.0.0
  */
public class ClientProvider extends RESTAbstractProvider {

  private static final String className = ClientProvider.class.getName();
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
   ** Constructs a <code>ClientProvider</code> provider to OAuth Clients stored in OAM configuration
   ** accesed by REST API
   ** @param url REST API URL
   ** @param username REST API UserName
   ** @param password REST API Password
   ** @param identityDomainName Name of the OAuth Identity Domain stored in OAM Configuration
   ** <br>
   ** Default Constructor
   */
  public ClientProvider(String url, String username, String password, String identityDomainName) {
    super();
    this.url = url;
    this.username = username;
    this.password = password;
    this.identityDomainName = identityDomainName;
    this.resource = getWebResource(url,username, password);
  }
  
  //////////////////////////////////////////////////////////////////////////////  
  // Provider methods
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method: getClients 
  /**
   ** Get list of all OAuth Clients
   ** @return List of all OAuth Clients
   ** @throws OAuthException Throws exeption in case REST API  won't return HTTP Code 200
   */
  public List<Client> getClients() throws OAuthException {
    String methodName = "getClients";
    logger.entering(className,methodName);
    
    List<Client> clients = null;    
    ClientResponse response = resource.queryParam("identityDomainName", (identityDomainName == null ? "" : identityDomainName))
                                      .header("Content-Type", "application/json")
                                      .header("Accept", "application/json")      
                                      .header("cache-control","must-revalidate,no-cache,no-store")
                                      .get(ClientResponse.class);
    if(response != null){
      String entity = response.getEntity(String.class);
      logger.finer("Response Status = "+response.getStatus()+", Entity Data = "+entity);
      if(response.getStatus() != 200){
        throw new OAuthException(response.getStatus()+": "+entity);
      }
      clients = OAuthJSONMapper.unmarshalClients(entity);
    }
    
    logger.exiting(className,methodName);
    return clients;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method: getClient 
  /**
   ** Get OAuth Client details based on client id
   ** @param clientId OAuth Client Id
   ** @return OAuth Client details
   ** @throws OAuthException Throws exeption in case REST API  won't return HTTP Code 200
   */
  public Client getClient(String clientId) throws OAuthException {
    String methodName = "getClient";
    logger.entering(className,methodName);
    
    Client client = null;    
    if(clientId != null && clientId.trim().length()>0){
      ClientResponse response = resource.queryParam("identityDomainName", (identityDomainName == null ? "" : identityDomainName))
                                        .queryParam("id", clientId)
                                        .header("Content-Type", "application/json")
                                        .header("Accept", "application/json")     
                                        .header("cache-control","must-revalidate,no-cache,no-store")
                                        .get(ClientResponse.class);
      if(response != null){
        String entity = response.getEntity(String.class);
        logger.finer("Response Status = "+response.getStatus()+", Entity Data = "+entity);
        if(response.getStatus() != 200){
          throw new OAuthException(response.getStatus()+": "+entity);
        }
        client = OAuthJSONMapper.unmarshalClient(entity);
      }
    }
    else{
      client = new Client();
    }
    
    logger.exiting(className,methodName);
    return client;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method: createClient 
  /**
   ** Create a new OAuth Client based on provided client details
   ** @param client OAutch client details
   ** @throws OAuthException Throws exeption in case REST API  won't return HTTP Code 200
   */
  public void createClient(Client client) throws OAuthException {
    String methodName = "createClient";
    logger.entering(className,methodName);
    
    if(client != null){
      ClientResponse response = resource.header("Content-Type", "application/json")
                                        .header("Accept", "application/json")
                                        .entity(OAuthJSONMapper.marshalClient(client).toString(),MediaType.APPLICATION_JSON)
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
  // Method: updateClient 
  /**
   ** Update OAuth Client details based on privded information
   ** @param client OAuth Client details
   ** @throws OAuthException Throws exeption in case REST API  won't return HTTP Code 200
   */
  public void updateClient(Client client) throws OAuthException {
    String methodName = "updateClient";
    logger.entering(className,methodName);
    
    if(client != null && client.getId() != null){
      String jsonMessage = OAuthJSONMapper.marshalClient(client).toString();
      
      ClientResponse response = resource.header("Content-Type", "application/json")
                                        .header("Accept", "application/json")
                                        .entity(jsonMessage,MediaType.APPLICATION_JSON)
                                        .put(ClientResponse.class);
    //TODO When the bug is closed this can be removed
    // Service is executed twice, because there is a bug which won't read a fresh data
    // When it is saved twice a corret values is read by GET method
                     response = resource.header("Content-Type", "application/json")
                                        .header("Accept", "application/json")
                                        .entity(OAuthJSONMapper.marshalClient(client).toString(),MediaType.APPLICATION_JSON)
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
      throw new OAuthException("Client Id is missing");
    }
    
    logger.exiting(className,methodName);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method: deleteClient 
  /**
   ** Delte OAuth Client
   ** @param clientId OAuth Client Id
   ** @throws OAuthException Throws exeption in case REST API  won't return HTTP Code 200
   */
  public void deleteClient(String clientId) throws OAuthException {
    String methodName = "deleteClient";
    logger.entering(className,methodName);
    logger.finest("Delete Client with applicationId = "+clientId);
    
    if(clientId != null && clientId.trim().length()>0){
      ClientResponse response = resource.queryParam("identityDomainName", (identityDomainName == null ? "" : identityDomainName))
                                        .queryParam("id", clientId)
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
  // Method: resetClientPassword 
  /**
   ** Reset OAuth Client Password
   ** @param identityDomain Oauth Identity Domain Name
   ** @param clientId OAuth Client Id
   ** @param newPassword New OAUth Client Password
   ** @throws OAuthException Throws exeption in case REST API  won't return HTTP Code 200
   */
  public void resetClientPassword(String identityDomain, String clientId,String newPassword) throws OAuthException {
    String methodName = "resetClientPassword";
    logger.entering(className,methodName);
    
    if(clientId != null && clientId.trim().length()>0){
      Client client = new Client();
      client.setIdDomain(identityDomain);
      client.setId(clientId);
      client.setSecret(newPassword);
      updateClient(client);
    }
    else{
      throw new OAuthException("Client Id is missing");
    }
    
    logger.exiting(className,methodName);
  }
  
  public static void main(String[] args) {
    /*
    ClientProvider cp = new ClientProvider("http://sp-instance.sub03231021150.bamfdevvcn.oraclevcn.com:7001/oam/services/rest/ssa/api/v1/oauthpolicyadmin/client", "weblogic", "Welcome1","OIDCDefaultDomain");
    
    //List<Client> clients = cp.getClients();
    //System.out.println(clients);
    
    
    //Client client = cp.getClient("Client ID");
    //System.out.println(client);
    
     
    Client client = new Client();
    client.setId("Client ID");
    client.setName("Client Name");
    client.setIdDomain("OIDCDefaultDomain");
   // client.setClientType("CONFIDENTIAL_CLIENT");
    client.setDefaultScope("OIDCDemo.openid");
    
    
    List<SingleAttribute> scopes = new ArrayList<SingleAttribute>();
    scopes.add(new SingleAttribute("OIDCDemo.openid"));
    client.setScopes(scopes);
    
    List<SingleAttribute> grandTypes = new ArrayList<SingleAttribute>();
    grandTypes.add(new SingleAttribute("PASSWORD"));
   // client.setGrantTypes(grandTypes);
    
    List<RedirectURI> redirectURIs = new ArrayList<RedirectURI>();
    redirectURIs.add(new RedirectURI("https://host.com/sfsdf",true));
    redirectURIs.add(new RedirectURI("https://host333.com/sfsdf",false));
  //  client.setRedirectURIs(redirectURIs);
    
    
    
    //System.out.println("client:"+OAuthJSONMapper.marshalClient(client));
    
    //cp.createClient(client);
 
    Client client2 = cp.getClient("Client ID");
    System.out.println(OAuthJSONMapper.marshalClient(client2)+"\n");

    
    
    client2.setRedirectURIs(redirectURIs);
    System.out.println(OAuthJSONMapper.marshalClient(client2)+"\n");
    cp.updateClient(client2);
    
    client2 = cp.getClient("Client ID");
    System.out.println(OAuthJSONMapper.marshalClient(client2)+"\n");
    //cp.deleteClient("075a3fb80aae4626bc95201dfb8b1652");
    
    */
  }
}
