/*
    Oracle Deutschland BV & Co. KG

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

    Copyright © 2023. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager
    Subsystem   :   Custom SCIM Service

    File        :   AbstractEndpointTest.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   tomas.t.sebo@oracle.com

    Purpose     :   This file implements the class
                    AbstractEndpointTest.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2025-07-02  TSebo     First release version
*/

package bka.iam.identity.scim.extension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.Properties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.HttpUrlConnectorProvider;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import org.junit.Assert;

////////////////////////////////////////////////////////////////////////////////
// class AbstractEndpointTest
// ~~~~~ ~~~~~~~~~~
/**
 ** The AbstractEndpointTest class is abstract endpoing for JUnit test.
 ** 
 ** 
 ** @author  tomas.t.sebo@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class AbstractEndpointTest {
  
  private Properties prop;
  
  private static final File   CONFIG_FOLDER  = new File("./src/test/resources");
  private static final String CONFIG_FILE    = "endpoint.properties";
  
  
  protected ObjectMapper mapper;
  protected WebTarget target;
  
  
  public AbstractEndpointTest() {
    super();
    mapper = new ObjectMapper();
    prop = new Properties();
  }
  
  
  protected abstract File getResourcesFolder();
  
  protected void init() throws IOException {
    // Read config file
    prop.load(new FileInputStream(new File(CONFIG_FOLDER,CONFIG_FILE)));
    
    // Create REST Client
    ClientConfig config = new ClientConfig();
    config.property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true);
    HttpAuthenticationFeature basicAuth = HttpAuthenticationFeature.basic(prop.getProperty("SCIM_USERNAME"),
                                                                          prop.getProperty("SCIM_PASSWD"));
    
    Client c = ClientBuilder.newClient(config);
    c.register(basicAuth);
    
    target = c.target(prop.getProperty("SCIM_HOST")+prop.getProperty("SCIM_URL"));
    
  }
  
  
  protected Response post(String path, String messageFileName) throws IOException, JsonProcessingException {
    // Read create policy message from file
    JsonNode createPolicy = readFile(messageFileName);
    
    // Create Policy
    Response response = target.path(path)
                              .request("Content-Type", "application/scim+json")
                              .post(Entity.entity(createPolicy.toString(),MediaType.valueOf("application/scim+json")));
      
    Assert.assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
    return response;
  }
  
  
  protected Response get(String path){
    // Modify Policy
    Response response = target.path(path)
                              .request(MediaType.valueOf("application/scim+json"))
                              .header("Content-Type", "application/scim+json")
                              .get();
    
    // Check if HTTP Response code is 200  
    Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    return response;
  }
  
  
  protected Response put(String path, String messageFileName) throws IOException, JsonProcessingException {
    // Read modify policy message from file
    JsonNode modifyPolicy = readFile(messageFileName);
    
    // Modify Policy
    Response response = target.path(path)
                              .request(MediaType.valueOf("application/scim+json"))
                              .header("Content-Type", "application/scim+json")
                              .put(Entity.entity(modifyPolicy.toString(),MediaType.valueOf("application/scim+json")));
    
    // Check if HTTP Response code is 200  
    Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    return response;
  }
  
  protected Response patch(String path, String messageFileName) throws IOException, JsonProcessingException {
    // Read modify policy message from file
    JsonNode patchPolicy = readFile(messageFileName);

    // Patch Policy
    Response response = target.path(path)
                              .request(MediaType.valueOf("application/scim+json"))
                              .header("Content-Type", "application/scim+json")
                              .method("PATCH",Entity.entity(patchPolicy.toString(),MediaType.valueOf("application/scim+json")));
    
    // Check if HTTP Response code is 200  
    Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    return response;
  }
  
  protected Response delete(String path){
    // Delete Simple Policy
    Response response = target.path(path)
                              .request(MediaType.valueOf("application/scim+json"))
                              .header("Content-Type", "application/scim+json")
                              .delete();
      
    Assert.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    return response;
  }
  


  protected JsonNode readFile(String fileName) throws IOException, JsonProcessingException {    
    return mapper.readTree(new File(getResourcesFolder(),fileName));
  }
  
  protected JsonNode getJsonNode(String message) throws IOException {
    return mapper.readTree(message);
  }
  
  
  
  
}
