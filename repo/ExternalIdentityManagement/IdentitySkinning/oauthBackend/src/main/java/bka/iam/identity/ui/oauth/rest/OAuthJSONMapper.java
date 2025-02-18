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

    File        :   OAuthJSONMapper.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   tomas.t.sebo@oracle.com

    Purpose     :   This file implements the class
                    Register.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2024-05-02  Tomas Sebo    First release version
*/
package bka.iam.identity.ui.oauth.rest;

import bka.iam.identity.ui.oauth.model.Application;
import bka.iam.identity.ui.oauth.model.Attribute;
import bka.iam.identity.ui.oauth.model.Client;
import bka.iam.identity.ui.oauth.model.IdentityDomain;
import bka.iam.identity.ui.oauth.model.RedirectURI;
import bka.iam.identity.ui.oauth.model.Scope;
import bka.iam.identity.ui.oauth.model.SingleAttribute;
import bka.iam.identity.ui.oauth.model.TokenAttribute;
import bka.iam.identity.ui.oauth.model.TokenSetting;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

////////////////////////////////////////////////////////////////////////////////
// class OAuthJSONMapper
// ~~~~~ ~~~~~~~~
/**
 ** OAuth JSON Mapper between model beans and OAM REST API. This class allows 
 ** marshal and unmarshal model beans to JSON format.
 **
 ** @author  tomas.t.sebo@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class OAuthJSONMapper {
  
  private static final String className = OAuthJSONMapper.class.getName();
  private static Logger       logger = Logger.getLogger(className);
  
  private static final String IDENTITY_DOMAIN_NAME              = "name";
  private static final String IDENTITY_DOMAIN_DESCRIPTION       = "description";
  private static final String IDENTITY_DOMAIN_CONSENT_PAGE_URL  = "consentPageURL";
  private static final String IDENTITY_DOMAIN_ERROR_PAGE_URL    = "errorPageURL";
  private static final String IDENTITY_DOMAIN_IDENTITY_PROVIDER = "identityProvider";
  private static final String IDENTITY_DOMAIN_CUSTOM_ATTRS      = "customAttrs";
  private static final String IDENTITY_DOMAIN_TOKEN_SETTINGS    = "tokenSettings";
  
  private static final String TOKEN_TYPE_TOKEN_TYPE             = "tokenType";
  private static final String TOKEN_TYPE_TOKEN_EXPIRE           = "tokenExpiry";
  private static final String TOKEN_TYPE_LIFE_CYCLE_ENABLED     = "lifeCycleEnabled";
  private static final String TOKEN_TYPE_REFRESH_TOKEN_ENABLED  = "refreshTokenEnabled";
  private static final String TOKEN_TYPE_REFRESH_TOKEN_EXPIRY   = "refreshTokenExpiry";
  private static final String TOKEN_TYPE_REFRESH_TOKEN_LIFE_CYCLE_ENABLED = "refreshTokenLifeCycleEnabled";
  
  private static final String APPLICATION_NAME                 = "name";
  private static final String APPLICATION_DESCRIPTION          = "description";
  private static final String APPLICATION_ID_DOMAIN            = "idDomain";
  private static final String APPLICATION_IDENTITY_DOMAIN      = "identityDomain";
  private static final String APPLICATION_RESOURCE_SERVER_ID   = "resourceServerId";
  private static final String APPLICATION_RESOURCE_SERVER_TYPE = "resServerType";
  private static final String APPLICATION_RESOURCE_SERVER_NAME_SPACE_PREFIX = "resourceServerNameSpacePrefix";
  
  private static final String APPLICATION_SCOPES           = "scopes";
  private static final String APPLICATION_TOKEN_ATTRIBUTES = "tokenAttributes";
  
  private static final String SCOPE_NAME                 = "scopeName";
  private static final String SCOPE_DESCRIPTION          = "description";
  
  private static final String TOKEN_ATTRIBUTE_NAME       = "attrName";
  private static final String TOKEN_ATTRIBUTE_VALUE      = "attrValue";
  private static final String TOKEN_ATTRIBUTE_TYPE       = "attrType";
  
  private static final String CLIENT_ID = "id";
  private static final String CLIENT_NAME = "name";
  private static final String CLIENT_DESCRIPTION="description";
  private static final String CLIENT_ID_DOMAIN="idDomain";
  private static final String CLIENT_IDENTITY_DOMAIN="identityDomain";
  private static final String CLIENT_CLIENT_TYPE="clientType";
  private static final String CLIENT_DEFAULT_SCOPE="defaultScope";
  private static final String CLIENT_SECRET="secret";
  private static final String CLIENT_USE_PKCE="usePKCE";
  private static final String CLIENT_TOKEN_ENDPOINT_AUTH_METHOD="tokenEndpointAuthMethod";
  private static final String CLIENT_ISSUE_TLS_CLIENT_CERTIFICATE_BOUND_ACCESS_TOKENS="issueTLSClientCertificateBoundAccessTokens";
  private static final String CLIENT_TLS_CLIENT_AUTH_SANDNS="tlsClientAuthSANDNS(";
  private static final String CLIENT_TLS_CLIENT_AUTH_SANEMAIL="tlsClientAuthSANEmail";
  private static final String CLIENT_TLS_CLIENT_AUTH_SANIP="tlsClientAuthSANIP";
  private static final String CLIENT_TLS_CLIENT_AUTH_SANURI="tlsClientAuthSANURI";
  private static final String CLIENT_TLS_CLIENT_AUTH_SUBJECTDN="tlsClientAuthSubjectDN";
  
  private static final String CLIENT_GRANT_TYPES                 ="grantTypes";
  private static final String CLIENT_SCOPES                      ="scopes";
  private static final String CLIENT_ID_TOKEN_CUSTOM_CLAIMS      ="idTokenCustomClaims";
  private static final String CLIENT_ACCESS_TOKEN_CUSTOM_CLIAIMS ="accessTokenCustomClaims";
  private static final String CLIENT_USER_INFO_CUSTOM_CLAIMS     ="userInfoCustomClaims";
  
  private static final String CLIENT_REDIRECT_URIS           ="redirectURIs";  
  private static final String CLIENT_ATTRIBUTES              ="attributes";
  
  private static final String REDIRECT_URI_URL               ="url";  
  private static final String REDIRECT_URI_IS_HTTPS          ="isHttps";  
  
  //////////////////////////////////////////////////////////////////////////////  
  // Marchal ans Unmarchal method to and from JSON format
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method: unmarshalIdentityDomains 
  /**
   ** Unmarshal JSON String to the list of the OAuth Identity Domain objects
   ** @param identityDomainsJSON JSON String representing list of the Identity Domains
   ** @return List of the OAuth Identity Domains
   */
  public static List<IdentityDomain> unmarshalIdentityDomains(String identityDomainsJSON){
    String methodName = "unmarshalIdentityDomains";
    logger.entering(className,methodName);
    logger.finest("Input string: "+identityDomainsJSON);
    
    List<IdentityDomain> identityDomains = null;
    if(identityDomainsJSON != null){
      identityDomains = new ArrayList<IdentityDomain>();
      
      ObjectMapper objectMapper = new ObjectMapper();
      try {
        JsonNode tree = objectMapper.readTree(identityDomainsJSON);
        if(tree != null){
          Iterator<JsonNode> i  = tree.iterator();
          while(i.hasNext()){
            identityDomains.add(unmarshalIdentityDomain(i.next().toString()));
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    
    logger.finest("Number of Identity Domain objects is: "+(identityDomains == null ? "0" : identityDomains.size()));
    logger.exiting(className,methodName);
    return identityDomains;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method: unmarshalIdentityDomain 
  /**
   ** Unmarshal JSON String to OAuth Identity Domain object
   ** @param identityDomainJSON JSON String representing OAUth Domain
   ** @return OAuth Identity Domain object
   */
  public static IdentityDomain unmarshalIdentityDomain(String identityDomainJSON){
    String methodName = "unmarshalIdentityDomain";
    logger.entering(className,methodName);
    logger.finest("IdentityDomain as input string: "+identityDomainJSON);
    
    ObjectMapper objectMapper = new ObjectMapper();
    IdentityDomain identityDomain = new IdentityDomain();
    try {
      HashMap id = objectMapper.readValue(identityDomainJSON, HashMap.class);
      identityDomain.setName((String)id.get(IDENTITY_DOMAIN_NAME));
      identityDomain.setDescription((String)id.get(IDENTITY_DOMAIN_DESCRIPTION));
      identityDomain.setConsentPageURL((String)id.get(IDENTITY_DOMAIN_CONSENT_PAGE_URL));
      identityDomain.setErrorPageURL((String)id.get(IDENTITY_DOMAIN_ERROR_PAGE_URL));
      identityDomain.setIdentityProvider((String)id.get(IDENTITY_DOMAIN_IDENTITY_PROVIDER));
      String customAttrs = (String)id.get(IDENTITY_DOMAIN_CUSTOM_ATTRS);
      if(customAttrs != null){
        HashMap<String,String> customAttrsMap = objectMapper.readValue(customAttrs, HashMap.class);
        if(customAttrsMap != null){
          for (Map.Entry<String, String> entry : customAttrsMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();         
            identityDomain.getCustomAttrs().add(new Attribute(key,value));
          }       
        }
      }
      ArrayList<Map> tokenSettings = (ArrayList<Map>)id.get(IDENTITY_DOMAIN_TOKEN_SETTINGS);
      if(tokenSettings != null){
        identityDomain.setTokenSettings(new ArrayList<TokenSetting>());
        for(Map tokenSettingMap : tokenSettings){
          TokenSetting tokenSetting = new TokenSetting();
          tokenSetting.setTokenType((String)tokenSettingMap.get(TOKEN_TYPE_TOKEN_TYPE));
          tokenSetting.setTokenExpiry(parseJSONValue((Integer)tokenSettingMap.get(TOKEN_TYPE_TOKEN_EXPIRE)));
          tokenSetting.setLifeCycleEnabled(parseJSONValue((Boolean)tokenSettingMap.get(TOKEN_TYPE_LIFE_CYCLE_ENABLED)));     
          tokenSetting.setRefreshTokenEnabled(parseJSONValue((Boolean)tokenSettingMap.get(TOKEN_TYPE_REFRESH_TOKEN_ENABLED)));
          tokenSetting.setRefreshTokenExpiry(parseJSONValue((Integer)tokenSettingMap.get(TOKEN_TYPE_REFRESH_TOKEN_EXPIRY)));
          tokenSetting.setRefreshTokenLifeCycleEnabled(parseJSONValue((Boolean)tokenSettingMap.get(TOKEN_TYPE_REFRESH_TOKEN_LIFE_CYCLE_ENABLED)));
          
          // Add tokenSetting to identityDomain
          identityDomain.getTokenSettings().add(tokenSetting);                           
        }
      }
      
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    
    logger.finest("IdentityDomain as objects: "+identityDomain);
    logger.exiting(className,methodName);
    return identityDomain;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method: marshalIdentityDomains 
  /**
   ** Marchal list of the OAuth Identity Domins to JSON Array
   ** @param ids List of OAuth Identity Domains
   ** @return List of the OAuth Identity Domins to JSON Array
   */
  public static JsonArray marshalIdentityDomains(List<IdentityDomain> ids) {
    String methodName = "marshalIdentityDomains";
    logger.entering(className,methodName);
    
    JsonArray jsonArray = null;
    if(ids != null && ids.size() > 0){  
      JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
      for(IdentityDomain identityDomain: ids){
        jsonArrayBuilder.add(marshalIdentityDomain(identityDomain));
      }
      jsonArray = jsonArrayBuilder.build();
      
    }
    
    logger.exiting(className,methodName);
    return jsonArray;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method: marshalIdentityDomain 
  /**
   ** Marchal OAuth Identity Domain to JSON object
   ** @param identityDomain OAuth Identity Domain object
   ** @return JSON object representing OAuth Identity Domain
   */
  public static JsonObject marshalIdentityDomain(IdentityDomain identityDomain){
    String methodName = "marshalIdentityDomain";
    logger.entering(className,methodName);
    logger.finest("Identity Domain as object: "+identityDomain);
    
    JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
      if(identityDomain.getName() != null) jsonBuilder.add(IDENTITY_DOMAIN_NAME,identityDomain.getName());
      if(identityDomain.getDescription() != null) jsonBuilder.add(IDENTITY_DOMAIN_DESCRIPTION, identityDomain.getDescription());                    
      if(identityDomain.getConsentPageURL() != null) jsonBuilder.add(IDENTITY_DOMAIN_CONSENT_PAGE_URL, identityDomain.getConsentPageURL());                           
      if(identityDomain.getErrorPageURL() != null) jsonBuilder.add(IDENTITY_DOMAIN_ERROR_PAGE_URL, identityDomain.getErrorPageURL());                           
      if(identityDomain.getIdentityProvider() != null) jsonBuilder.add(IDENTITY_DOMAIN_IDENTITY_PROVIDER, identityDomain.getIdentityProvider());
      
    
    if(identityDomain.getCustomAttrs() != null && identityDomain.getCustomAttrs().size() > 0){
      StringBuffer sb = new StringBuffer();
      sb.append("{");
      for (Attribute entry : identityDomain.getCustomAttrs()){
        sb.append("\"").append(entry.getKey()).append("\": \"").append(entry.getValue()).append("\",");
      }
      sb.deleteCharAt(sb.length()-1); // Delete the last comma
      sb.append("}");
    
      jsonBuilder.add(IDENTITY_DOMAIN_CUSTOM_ATTRS,sb.toString());
    }
    
    if(identityDomain.getTokenSettings() != null && identityDomain.getTokenSettings().size() >0 ){
      JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
      for(TokenSetting ts : identityDomain.getTokenSettings()){
        JsonObjectBuilder jsonTokenSettingBuilder = Json.createObjectBuilder();
        addJSONValue(jsonTokenSettingBuilder,TOKEN_TYPE_TOKEN_TYPE,ts.getTokenType());
        addJSONValue(jsonTokenSettingBuilder,TOKEN_TYPE_TOKEN_EXPIRE,ts.getTokenExpiry());
        addJSONValue(jsonTokenSettingBuilder,TOKEN_TYPE_LIFE_CYCLE_ENABLED,ts.isLifeCycleEnabled());
        if(ts.isRefreshTokenEnabled()){
          addJSONValue(jsonTokenSettingBuilder,TOKEN_TYPE_REFRESH_TOKEN_ENABLED,ts.isRefreshTokenEnabled());
          addJSONValue(jsonTokenSettingBuilder,TOKEN_TYPE_REFRESH_TOKEN_EXPIRY,ts.getRefreshTokenExpiry());
          addJSONValue(jsonTokenSettingBuilder,TOKEN_TYPE_REFRESH_TOKEN_LIFE_CYCLE_ENABLED,ts.isRefreshTokenLifeCycleEnabled());
        }
        jsonArrayBuilder.add(jsonTokenSettingBuilder);
      }
      jsonBuilder.add(IDENTITY_DOMAIN_TOKEN_SETTINGS,jsonArrayBuilder);
    }
    JsonObject model =   jsonBuilder.build();
    
    logger.finest("Identity Domain as JSON String: "+model.toString());
    logger.exiting(className,methodName);
    return model;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method: unmarshalApplications 
  /**
   ** Unmarshal JSON String to the list of the OAuth Application objects
   ** @param applicationJSON JSON String representing array of OAuth Applications
   ** @return List of the OAuth applications
   */
  public static List<Application> unmarshalApplications(String applicationJSON) {
    String methodName = "unmarshalApplications";
    logger.entering(className,methodName);
    logger.finest("Input string: "+applicationJSON);
    
    List<Application> applications = null;
    if(applicationJSON != null){
      applications = new ArrayList<Application>();
      
      ObjectMapper objectMapper = new ObjectMapper();
      try {
        JsonNode tree = objectMapper.readTree(applicationJSON);
        if(tree != null){
          Iterator<JsonNode> i  = tree.iterator();
          while(i.hasNext()){
            applications.add(unmarshalApplication(i.next().toString()));
          }
        }
        
      } catch (Exception e) {
        e.printStackTrace();
      }

    }
    logger.finest("Number of Application objects is: "+(applications == null ? "0" : applications.size()));
    logger.exiting(className,methodName);
    
    return applications;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method: unmarshalApplication 
  /**
   ** Unmarshal JSON string to the OAuth Application object
   ** @param applicationJSON JSON string representing OAuth object
   ** @return OAuth appliaction object
   */
  public static Application unmarshalApplication(String applicationJSON) {
    String methodName = "unmarshalApplication";
    logger.entering(className,methodName);
    logger.finest("Appliaction as input string: "+applicationJSON);
    
    ObjectMapper objectMapper = new ObjectMapper();
    Application application = new Application();
    try {
      HashMap app = objectMapper.readValue(applicationJSON, HashMap.class);
      application.setName((String)app.get(APPLICATION_NAME));
      application.setDescription((String)app.get(APPLICATION_DESCRIPTION));
      application.setIdDomain((String)app.get(APPLICATION_ID_DOMAIN));
      if(application.getIdDomain() == null){
        application.setIdDomain((String)app.get(APPLICATION_IDENTITY_DOMAIN));
      }
      application.setResourceServerId((String)app.get(APPLICATION_RESOURCE_SERVER_ID));
      application.setResServerType((String)app.get(APPLICATION_RESOURCE_SERVER_TYPE));
      application.setResourceServerNameSpacePrefix((String)app.get(APPLICATION_RESOURCE_SERVER_NAME_SPACE_PREFIX));
      
      ArrayList<Map>  scopes = (ArrayList<Map> )app.get(APPLICATION_SCOPES);
      if(scopes != null){
        for(Map scopesMap : scopes){
          Scope scope = new Scope();
          scope.setScopeName((String)scopesMap.get(SCOPE_NAME));
          scope.setDescription((String)scopesMap.get(SCOPE_DESCRIPTION));
          // Add scope to Appliaction scopes
          application.getScopes().add(scope);                           
        }
      }
      
      ArrayList<Map> tokenAttributes = (ArrayList<Map>)app.get(APPLICATION_TOKEN_ATTRIBUTES);
      if(tokenAttributes != null){
        for(Map tokenAttributeMap : tokenAttributes){
          TokenAttribute tokenAttribute = new TokenAttribute();
          tokenAttribute.setAttrName((String)tokenAttributeMap.get(TOKEN_ATTRIBUTE_NAME));
          tokenAttribute.setAttrValue((String)tokenAttributeMap.get(TOKEN_ATTRIBUTE_VALUE));
          tokenAttribute.setAttrType((String)tokenAttributeMap.get(TOKEN_ATTRIBUTE_TYPE));
          // Add tokenAttribute to Application tokenAttributes
          application.getTokenAttributes().add(tokenAttribute);                           
        }
      }
      
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    
    logger.finest("Appliaction as objects: "+application);
    logger.exiting(className,methodName);
    return application;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method: marshalApplications 
  /**
   ** Marchal list of the OAuth Applications to JSON Array
   ** @param applications list of the OAuht Applications
   ** @return JSON array of OAuth Applications
   */
  public static JsonArray marshalApplications(List<Application> applications) {
    String methodName = "marshalApplications";
    logger.entering(className,methodName);
    
    JsonArray jsonArray = null;
    if(applications != null && applications.size() > 0){
      
      JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
      for(Application application: applications){
        jsonArrayBuilder.add(marshalApplication(application));
      }
      jsonArray = jsonArrayBuilder.build();
      
    }
    logger.exiting(className,methodName);
    return jsonArray;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method: marshalApplication 
  /**
   ** Marchal OAuth Application to JSON Array
   ** @param application OAuth Appliaction object
   ** @return JSON object or OAuth Applications
   */
  public static JsonObject marshalApplication(Application application) {
    String methodName = "marshalApplication";
    logger.entering(className,methodName);
    logger.finest("Input Applicationas object: "+application);
    
    JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
    if(application.getIdDomain() != null) jsonBuilder.add(APPLICATION_ID_DOMAIN,application.getIdDomain());
    if(application.getName() != null) jsonBuilder.add(APPLICATION_NAME, application.getName());                    
    if(application.getDescription() != null) jsonBuilder.add(APPLICATION_DESCRIPTION, application.getDescription());                           
    
    
    if(application.getResourceServerId() != null) jsonBuilder.add(APPLICATION_RESOURCE_SERVER_ID, application.getResourceServerId());
    if(application.getResServerType() != null) jsonBuilder.add(APPLICATION_RESOURCE_SERVER_TYPE, application.getResServerType());
    if(application.getResourceServerNameSpacePrefix() != null) jsonBuilder.add(APPLICATION_RESOURCE_SERVER_NAME_SPACE_PREFIX, application.getResourceServerNameSpacePrefix());
    
    // Add Scopes
    if(application.getScopes() != null && application.getScopes().size() >0 ){
      JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
      for(Scope scope : application.getScopes()){
        JsonObjectBuilder jsonTokenSettingBuilder = Json.createObjectBuilder();
        addJSONValue(jsonTokenSettingBuilder,SCOPE_NAME,scope.getScopeName());
        addJSONValue(jsonTokenSettingBuilder,SCOPE_DESCRIPTION,scope.getDescription());
        jsonArrayBuilder.add(jsonTokenSettingBuilder);
      }
      jsonBuilder.add(APPLICATION_SCOPES,jsonArrayBuilder);
    }
    
    // Add Token Attributes
    if(application.getTokenAttributes() != null && application.getTokenAttributes().size() >0 ){
      JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
      for(TokenAttribute ta : application.getTokenAttributes()){
        JsonObjectBuilder jsonTokenSettingBuilder = Json.createObjectBuilder();
        addJSONValue(jsonTokenSettingBuilder,TOKEN_ATTRIBUTE_NAME,ta.getAttrName());
        addJSONValue(jsonTokenSettingBuilder,TOKEN_ATTRIBUTE_VALUE,ta.getAttrValue());
        addJSONValue(jsonTokenSettingBuilder,TOKEN_ATTRIBUTE_TYPE,ta.getAttrType());
        jsonArrayBuilder.add(jsonTokenSettingBuilder);
      }
      jsonBuilder.add(APPLICATION_TOKEN_ATTRIBUTES,jsonArrayBuilder);
    }
    
    JsonObject model =   jsonBuilder.build();
    
    logger.finest("Applicationa as JSON String: "+model.toString());
    logger.exiting(className,methodName);
    return model;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method: unmarshalClients 
  /**
   ** Unmarshal JSON String to the list of the OAuth Client objects
   ** @param clientsJSON JSON String representing list of the OAuth clients
   ** @return List of the OAuth Clients
   **
   */
  static List<Client> unmarshalClients(String clientsJSON) {
    String methodName = "unmarshalClients";
    logger.entering(className,methodName);
    logger.finest("Input string: "+clientsJSON);
    
    List<Client> clients = null;
    if(clientsJSON != null){
      clients = new ArrayList<Client>();
      
      ObjectMapper objectMapper = new ObjectMapper();
      try {
        JsonNode tree = objectMapper.readTree(clientsJSON);
        if(tree != null){
          Iterator<JsonNode> i  = tree.iterator();
          while(i.hasNext()){
            clients.add(unmarshalClient(i.next().toString()));
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }

    }
    
    logger.finest("Number of Client objects is: "+(clients == null ? "0" : clients.size()));
    logger.exiting(className,methodName);
    return clients;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: unmarshalClient 
  /**
   ** Unmarshal JSON String to the OAuth Client object
   ** @param clientJSON JSON String representing OAUth Client object
   ** @return OAuth Client object
   */
  static Client unmarshalClient(String clientJSON) {
    String methodName = "unmarshalClient";
    logger.entering(className,methodName);
    logger.finest("Client as input string: "+clientJSON);
    
    ObjectMapper objectMapper = new ObjectMapper();
    Client client = new Client();
    try {
      HashMap c = objectMapper.readValue(clientJSON, HashMap.class);
      client.setId((String)c.get(CLIENT_ID));
      client.setName((String)c.get(CLIENT_NAME));
      client.setDescription((String)c.get(CLIENT_DESCRIPTION));
      client.setIdDomain((String)c.get(CLIENT_IDENTITY_DOMAIN));
      client.setClientType((String)c.get(CLIENT_CLIENT_TYPE));
      client.setDefaultScope((String)c.get(CLIENT_DEFAULT_SCOPE));
      client.setSecret((String)c.get(CLIENT_SECRET));
      client.setUsePKCE((String)c.get(CLIENT_USE_PKCE));
      client.setTokenEndpointAuthMethod((String)c.get(CLIENT_TOKEN_ENDPOINT_AUTH_METHOD));
      client.setIssueTLSClientCertificateBoundAccessTokens(parseJSONValue((Boolean)c.get(CLIENT_ISSUE_TLS_CLIENT_CERTIFICATE_BOUND_ACCESS_TOKENS)));
      client.setTlsClientAuthSANDNS((String)c.get(CLIENT_TLS_CLIENT_AUTH_SANDNS));
      client.setTlsClientAuthSANEmail((String)c.get(CLIENT_TLS_CLIENT_AUTH_SANEMAIL));
      client.setTlsClientAuthSANIP((String)c.get(CLIENT_TLS_CLIENT_AUTH_SANIP));
      client.setTlsClientAuthSANURI((String)c.get(CLIENT_TLS_CLIENT_AUTH_SANURI));
      client.setTlsClientAuthSubjectDN((String)c.get(CLIENT_TLS_CLIENT_AUTH_SUBJECTDN));
      
      // Get String arrays as list values
      client.setGrantTypes(getSingleAttributes((List<String>)c.get(CLIENT_GRANT_TYPES)));
      client.setScopes(getSingleAttributes((List<String>)c.get(CLIENT_SCOPES)));
      client.setIdTokenCustomClaims(getSingleAttributes((List<String>)c.get(CLIENT_ID_TOKEN_CUSTOM_CLAIMS)));
      client.setAccessTokenCustomClaims(getSingleAttributes((List<String>)c.get(CLIENT_ACCESS_TOKEN_CUSTOM_CLIAIMS)));
      client.setUserInfoCustomClaims(getSingleAttributes((List<String>)c.get(CLIENT_USER_INFO_CUSTOM_CLAIMS)));
      
      // Get redirectURIs as list of ojbect RedirectURI
      ArrayList<Map> redirectURIs = (ArrayList<Map>)c.get(CLIENT_REDIRECT_URIS);
      if(redirectURIs != null){
        for(Map redirectURIsMap : redirectURIs){
          RedirectURI redirectURI = new RedirectURI();
          redirectURI.setUrl((String)redirectURIsMap.get(REDIRECT_URI_URL));
          redirectURI.setIsHttps(parseJSONValue((Boolean)redirectURIsMap.get(REDIRECT_URI_IS_HTTPS)));
          
          // Add tokenSetting to identityDomain
          client.getRedirectURIs().add(redirectURI);                           
        }
      }
      
      // Get attributes as list of ojbect TokenAttribue
      ArrayList<Map> clientAttributes = (ArrayList<Map>)c.get(CLIENT_ATTRIBUTES);
      if(clientAttributes != null){
        for(Map clientAttributesMap : clientAttributes){
          TokenAttribute tokenAttribute = new TokenAttribute();
          tokenAttribute.setAttrName((String)clientAttributesMap.get(TOKEN_ATTRIBUTE_NAME));
          tokenAttribute.setAttrValue((String)clientAttributesMap.get(TOKEN_ATTRIBUTE_VALUE));
          tokenAttribute.setAttrType((String)clientAttributesMap.get(TOKEN_ATTRIBUTE_TYPE));
          
          // Add tokenSetting to identityDomain
          client.getAttributes().add(tokenAttribute);                           
        }
      }
            
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    
    logger.finest("Client as objects: "+client);
    logger.exiting(className,methodName);
    return client;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method: marshalClient 
  /**
   ** Marchal OAuth Client to JSON Object
   ** @param client OAuth Client model object
   ** @return JSON object or OAuth Client
   */
  public static JsonObject marshalClient(Client client) {
    String methodName = "marshalClient";
    logger.entering(className,methodName);
    logger.finest("Client as input object: "+client);
    
    JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
    if(client.getIdDomain() != null)     jsonBuilder.add(CLIENT_ID_DOMAIN,client.getIdDomain());
    if(client.getId() != null)           jsonBuilder.add(CLIENT_ID, client.getId());                    
    if(client.getName() != null)         jsonBuilder.add(CLIENT_NAME, client.getName());                    
    if(client.getDescription() != null)  jsonBuilder.add(CLIENT_DESCRIPTION, client.getDescription());                           
    if(client.getClientType() != null)   jsonBuilder.add(CLIENT_CLIENT_TYPE, client.getClientType());                           
    if(client.getDefaultScope() != null) jsonBuilder.add(CLIENT_DEFAULT_SCOPE, client.getDefaultScope());                           
    if(client.getSecret() != null)       jsonBuilder.add(CLIENT_SECRET, client.getSecret());                           
    if(client.getUsePKCE() != null)      jsonBuilder.add(CLIENT_USE_PKCE, client.getUsePKCE());
    if(client.getTokenEndpointAuthMethod() != null) jsonBuilder.add(CLIENT_TOKEN_ENDPOINT_AUTH_METHOD, client.getTokenEndpointAuthMethod());
    if(client.getTlsClientAuthSANDNS() != null)     jsonBuilder.add(CLIENT_TLS_CLIENT_AUTH_SANDNS, client.getTlsClientAuthSANDNS());
    if(client.getTlsClientAuthSANEmail() != null)   jsonBuilder.add(CLIENT_TLS_CLIENT_AUTH_SANEMAIL, client.getTlsClientAuthSANEmail());
    if(client.getTlsClientAuthSANIP() != null)      jsonBuilder.add(CLIENT_TLS_CLIENT_AUTH_SANIP, client.getTlsClientAuthSANIP());
    if(client.getTlsClientAuthSANURI() != null)     jsonBuilder.add(CLIENT_TLS_CLIENT_AUTH_SANURI, client.getTlsClientAuthSANURI());
    if(client.getTlsClientAuthSubjectDN() != null)  jsonBuilder.add(CLIENT_TLS_CLIENT_AUTH_SUBJECTDN, client.getTlsClientAuthSubjectDN());
    if(client.isIssueTLSClientCertificateBoundAccessTokensSet()) jsonBuilder.add(CLIENT_ISSUE_TLS_CLIENT_CERTIFICATE_BOUND_ACCESS_TOKENS, client.isIssueTLSClientCertificateBoundAccessTokens());
    
    // Add grantTypes
    if(client.getGrantTypes() != null && client.getGrantTypes().size() >0 ){
      JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
      for(SingleAttribute grandType : client.getGrantTypes()){
        if(grandType.getValue() != null)
          jsonArrayBuilder.add(grandType.getValue());
      }
      jsonBuilder.add(CLIENT_GRANT_TYPES,jsonArrayBuilder);
    }
    
    // Add Scopes
    if(client.getScopes() != null && client.getScopes().size() >0 ){
      JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
      for(SingleAttribute scope : client.getScopes()){
        if(scope.getValue()!= null)
          jsonArrayBuilder.add(scope.getValue());
      }
      jsonBuilder.add(CLIENT_SCOPES,jsonArrayBuilder);
    }
    
    // Add idTokenCustomClaims
    if(client.getIdTokenCustomClaims() != null && client.getIdTokenCustomClaims().size() >0 ){
      JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
      for(SingleAttribute idTokenCustomClaim : client.getIdTokenCustomClaims()){
        if(idTokenCustomClaim.getValue()!= null)
          jsonArrayBuilder.add(idTokenCustomClaim.getValue());
      }
      jsonBuilder.add(CLIENT_ID_TOKEN_CUSTOM_CLAIMS,jsonArrayBuilder);
    }
    
    // Add accessTokenCustomClaims
    if(client.getAccessTokenCustomClaims() != null && client.getAccessTokenCustomClaims().size() >0 ){
      JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
      for(SingleAttribute accessTokenCustomClaim : client.getAccessTokenCustomClaims()){
        if(accessTokenCustomClaim.getValue() != null)
          jsonArrayBuilder.add(accessTokenCustomClaim.getValue());
      }
      jsonBuilder.add(CLIENT_ACCESS_TOKEN_CUSTOM_CLIAIMS,jsonArrayBuilder);
    }
    
    // Add userInfoCustomClaims
    if(client.getUserInfoCustomClaims() != null && client.getUserInfoCustomClaims().size() >0 ){
      JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
      for(SingleAttribute userInfoCustomClaim : client.getUserInfoCustomClaims()){
        if(userInfoCustomClaim.getValue() != null)
          jsonArrayBuilder.add(userInfoCustomClaim.getValue());
      }
      jsonBuilder.add(CLIENT_ACCESS_TOKEN_CUSTOM_CLIAIMS,jsonArrayBuilder);
    }
    
    // Add Token Attributes
    if(client.getAttributes() != null && client.getAttributes().size() >0 ){
      JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
      for(TokenAttribute ta : client.getAttributes()){
        if(ta.getAttrName() != null){
          JsonObjectBuilder jsonTokenSettingBuilder = Json.createObjectBuilder();
          addJSONValue(jsonTokenSettingBuilder,TOKEN_ATTRIBUTE_NAME,ta.getAttrName());
          addJSONValue(jsonTokenSettingBuilder,TOKEN_ATTRIBUTE_VALUE,ta.getAttrValue());
          addJSONValue(jsonTokenSettingBuilder,TOKEN_ATTRIBUTE_TYPE,ta.getAttrType());
          jsonArrayBuilder.add(jsonTokenSettingBuilder);
        }
      }
      jsonBuilder.add(CLIENT_ATTRIBUTES,jsonArrayBuilder);
    }
    
    // Add redirectURIs
    if(client.getRedirectURIs() != null && client.getRedirectURIs().size() >0 ){
      JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
      for(RedirectURI redirectURI : client.getRedirectURIs()){
        JsonObjectBuilder jsonTokenSettingBuilder = Json.createObjectBuilder();
        addJSONValue(jsonTokenSettingBuilder,REDIRECT_URI_URL,redirectURI.getUrl());
        addJSONValue(jsonTokenSettingBuilder,REDIRECT_URI_IS_HTTPS,redirectURI.isIsHttps());
        jsonArrayBuilder.add(jsonTokenSettingBuilder);
      }
      jsonBuilder.add(CLIENT_REDIRECT_URIS,jsonArrayBuilder);
    }
    JsonObject model =   jsonBuilder.build();
    
    logger.finest("Client as JSON String: "+model.toString());
    logger.exiting(className,methodName);
    return model;
  }
  
  //////////////////////////////////////////////////////////////////////////////  
  // Private methods
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method: getSingleAttributes 
  /**
   ** Get single attribute, in case the imput parameter is null empty list of SingleAttribute is returned.
   ** @param values Input values as list of String
   ** @return Single Attribute
   */
  private static List<SingleAttribute>  getSingleAttributes(List<String> values){
    List<SingleAttribute> singleAttributes= new ArrayList<SingleAttribute>();
    if(values != null){
      for(String value:values){
        singleAttributes.add(new SingleAttribute(value));
      }
    }
    return singleAttributes;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method: addJSONValue 
  /**
   ** Add JSON Value as string
   ** @param jsonTokenSettingBuilder Json Token Setting Builder
   ** @param attributeName Attribute Name
   ** @param attributeValue Attribute Value
   */
  private static void addJSONValue(JsonObjectBuilder jsonTokenSettingBuilder, 
                                String attributeName,
                                String attributeValue) {
    if(attributeValue != null){
      jsonTokenSettingBuilder.add(attributeName, attributeValue);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: addJSONValue 
  /**
   ** Add JSON Value, null value is hadles as -1
   ** @param jsonTokenSettingBuilder Json Token Setting Builder
   ** @param attributeName Attribute Name
   ** @param attributeValue Attribute Value
   */
  private static void addJSONValue(JsonObjectBuilder jsonTokenSettingBuilder, 
                                 String attributeName, 
                                 int attributeValue) {
    if(attributeValue >= 0){
      jsonTokenSettingBuilder.add(attributeName, attributeValue);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: addJSONValue 
  /**
   ** Add JSON Value, null value is handled as false
   ** @param jsonTokenSettingBuilder Json Token Setting Builder
   ** @param attributeName Attribute Name
   ** @param attributeValue Attribure Value
   */
  private static void addJSONValue(JsonObjectBuilder jsonTokenSettingBuilder, 
                                 String attributeName,
                                 boolean attributeValue) {
      jsonTokenSettingBuilder.add(attributeName, attributeValue);

  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method: parseJSONValue 
  /**
   ** Parse Integer value, in case the value is null -1 is returned
   ** @param attribureValue Integer input value
   ** @return
   */
  private static int parseJSONValue(Integer attribureValue) {
    int value = -1;
    if(attribureValue != null){
      try{
        value= attribureValue.intValue();
      }
      catch(NumberFormatException e){
        e.printStackTrace();
      }
    }
    return value;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method: parseJSONValue 
  /**
   ** Parse Boolen to object to boolean value, in case the value is null false is returned
   ** @param attribureValue Boolean input value
   ** @return true in case Boolean value is true
   */
  private static boolean parseJSONValue(Boolean attribureValue) {
    boolean value=false;
    if(attribureValue != null){
      try{
        value = attribureValue.booleanValue();
      }
      catch(NumberFormatException e){
        e.printStackTrace();
      }
    }
    return value;
  }
  
  public static void main(String[] args){
    /*
    List<Attribute> customAttrs = new ArrayList<Attribute>();
    customAttrs.add(new Attribute("key1","attribute1"));
    customAttrs.add(new Attribute("key2","attribute2"));
    List<TokenSetting> tokenSettings  = new ArrayList<TokenSetting>();
    tokenSettings.add(new TokenSetting("Token1",32,true,true,33333,false));
    tokenSettings.add(new TokenSetting("Token2",44,false));
    IdentityDomain domain = new IdentityDomain("name33",null,"consentPageURL 45","errorPageURL dfds",
                                               "identityProvider 45",customAttrs,tokenSettings);
    
    // Mashal Identity Domain
    String jsonString = OAuthJSONMapper.marshalIdentityDomain(domain).toString();
    System.out.println(jsonString);
    
    // UnMashal Identity Domain
    IdentityDomain id = OAuthJSONMapper.unmarshalIdentityDomain(jsonString);
    System.out.println(OAuthJSONMapper.marshalIdentityDomain(id));
    
    // Mashal Identity Domains
    List<IdentityDomain> ids = new ArrayList<IdentityDomain>();
    ids.add(domain);
    ids.add(domain);
    
    // UnMashal Identity Domains
    String idsJSON = OAuthJSONMapper.marshalIdentityDomains(ids).toString();
    System.out.println(idsJSON);
    System.out.println(OAuthJSONMapper.unmarshalIdentityDomains(idsJSON));
    
    
    Client client = new Client();
    client.setId("Client ID");
    client.setName("Client Name");
    client.setIdDomain("OIDCDefaultDomain");
    
    List<SingleAttribute> scopes = new ArrayList<SingleAttribute>();
    scopes.add(new SingleAttribute("profile"));
    client.setScopes(scopes);
    
    List<RedirectURI> redirectURIs = new ArrayList<RedirectURI>();
    redirectURIs.add(new RedirectURI("https://host.com/sfsdf",true));
    redirectURIs.add(new RedirectURI("https://host333.com/sfsdf",false));
    client.setRedirectURIs(redirectURIs);
    
    System.out.println(OAuthJSONMapper.marshalClient(client));
    */
  }



}
