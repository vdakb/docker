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

    File        :   PolicyEndpointTest.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   tomas.t.sebo@oracle.com

    Purpose     :   This file implements the class
                    PolicyEndpointTest.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2025-07-02  TSebo     First release version
*/
package bka.iam.identity.scim.extension.spi;

import bka.iam.identity.scim.extension.AbstractEndpointTest;
import bka.iam.identity.scim.extension.exception.ScimException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import java.io.File;
import java.io.IOException;

import java.text.ParseException;

import java.util.List;

import javax.ws.rs.core.Response;

import oracle.hst.platform.rest.ProcessingException;
import oracle.hst.platform.rest.entity.PathParser;
import oracle.hst.platform.rest.schema.Support;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

////////////////////////////////////////////////////////////////////////////////
// class PolicyEndpointTest
// ~~~~~ ~~~~~~~~~~
/**
 ** The PolicyEndpointTest class is JUnit test for Policy scim extension
 ** 
 ** @author  tomas.t.sebo@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PolicyEndpointTest extends AbstractEndpointTest{
      
  protected File getResourcesFolder(){
    return new File("./src/test/resources/policy");
  }

  @Before
  public void beforeAllTests() throws IOException{
    init();
  }

  
  @Test
  public void test01_PolicyCreate() throws IOException, JsonProcessingException {

    Response response = post("Policies", "policy-create-simple.json");
    
  }
  
  @Test
  public void test10_PolicyGet() throws IOException, JsonProcessingException, ScimException {
    
   Response response = get("Policies/TestAP999");
    
    // Check access policy id
    JsonNode updatedPolicy = getJsonNode(response.readEntity(String.class));
    Assert.assertEquals("TestAP999",((TextNode)updatedPolicy.get("id")).asText());
    Assert.assertEquals("TestAP999",((TextNode)updatedPolicy.get("description")).asText());
    Assert.assertEquals("1", ((NumericNode)updatedPolicy.get("priority")).asText());
  }

  @Test
  public void test20_PolicyModify() throws IOException, JsonProcessingException, ScimException {
    
    Response response = put("Policies/TestAP999", "policy-modify.json");

    // Check if the AccessPolicy description was updated
    JsonNode updatedPolicy = getJsonNode(response.readEntity(String.class));
    Assert.assertEquals("TestAP999_M", ((TextNode)updatedPolicy.get("description")).asText());
    
    // Check if the AccessPolicy priority was updated
    Assert.assertEquals("2", ((NumericNode)updatedPolicy.get("priority")).asText());
  }
  
  
  @Test
  public void test30_PolicyPatchDescriptionAndPriority() throws IOException, JsonProcessingException, ScimException {
    
    Response response = patch("Policies/TestAP999", "policy-patch-description.json");
    
    // Check if the AccessPolicy description was updated
    JsonNode updatedPolicy = getJsonNode(response.readEntity(String.class));
    Assert.assertEquals("TestAP999_P1", ((TextNode)updatedPolicy.get("description")).asText());
    
    // Check if the AccessPolicy priority was updated
    Assert.assertEquals("3", ((NumericNode)updatedPolicy.get("priority")).asText());
  }

  
  @Test
  public void test31_PolicyPatchDescriptionAndPriority2() throws IOException, JsonProcessingException, ScimException {
    
    Response response = patch("Policies/TestAP999", "policy-patch-description2.json");
     
    // Check if the AccessPolicy description was updated
    JsonNode updatedPolicy = getJsonNode(response.readEntity(String.class));
    Assert.assertEquals("TestAP999_P2", ((TextNode)updatedPolicy.get("description")).asText());
    
    // Check if the AccessPolicy priority was updated
    Assert.assertEquals("4", ((NumericNode)updatedPolicy.get("priority")).asText());
  }
  
  @Test
  public void test32_PolicyPatchRemoveEnt() throws IOException, JsonProcessingException, ScimException,
                                                   ProcessingException, ParseException {
    
    Response response = patch("Policies/TestAP999", "policy-patch-remove-ent.json");
   
    // Check if the AccessPolicy description was updated
    JsonNode updatedPolicy = getJsonNode(response.readEntity(String.class));
    String path = "applications[applicationName eq \"APHAccount\"].namespaces[namespace eq \"UD_APH_POL\"].entitlements";
    List<JsonNode>  matchData = Support.matchPath(PathParser.path(path), (ObjectNode)updatedPolicy);
    
    //Check if secone entitlement is removed
    Assert.assertEquals(1, matchData.get(0).size());
    
    //Check if the ent value is 184~1
    Assert.assertEquals("184~1", ((TextNode)((ObjectNode)matchData.get(0).get(0)).get("value")).asText());
  }
  
  @Test
  public void test33_PolicyPatchAddEnt() throws IOException, JsonProcessingException, ScimException,
                                                   ProcessingException, ParseException {
    
    Response response = patch("Policies/TestAP999", "policy-patch-add-ent.json");
   
    // Check if the AccessPolicy description was updated
    JsonNode updatedPolicy = getJsonNode(response.readEntity(String.class));
    String path = "applications[applicationName eq \"APHAccount\"].namespaces[namespace eq \"UD_APH_POL\"].entitlements[value eq \"184~2\"]";
    List<JsonNode>  matchData = Support.matchPath(PathParser.path(path), (ObjectNode)updatedPolicy);
    
    //Check if a new entitlement is added
    Assert.assertEquals(1, matchData.get(0).size());
  }
  
  @Test
  public void test34_PolicyPatchUpdateEnt() throws IOException, JsonProcessingException, ScimException,
                                                   ProcessingException, ParseException {
    
    Response response = patch("Policies/TestAP999", "policy-patch-update-ent.json");
   
    // Check if the AccessPolicy description was updated
    JsonNode updatedPolicy = getJsonNode(response.readEntity(String.class));
    String path = "applications[applicationName eq \"APHAccount\"].namespaces[namespace eq \"UD_APH_POL\"].entitlements[value eq \"184~3\"]";
    List<JsonNode>  matchData = Support.matchPath(PathParser.path(path), (ObjectNode)updatedPolicy);
    
    //Check if a new entitlement is updated
    Assert.assertEquals(1, matchData.get(0).size());
  }

  @Test
  public void test35_PolicyPatchUpdateEntMetadata() throws IOException, JsonProcessingException, ScimException,
                                                   ProcessingException, ParseException {
    
    Response response = patch("Policies/TestAP999", "policy-patch-update-ent-metadata.json");
   
    // Check if the AccessPolicy description was updated
    JsonNode updatedPolicy = getJsonNode(response.readEntity(String.class));
    String path = "applications[applicationName eq \"APHAccount\"].namespaces[namespace eq \"UD_APH_POL\"].entitlements[value eq \"184~1\"].additionalAttributes.attributes[value eq \"OU1_P\"]";
    List<JsonNode>  matchData = Support.matchPath(PathParser.path(path), (ObjectNode)updatedPolicy);
    
    //Check if entitlement attribute is updated
    Assert.assertEquals(1, matchData.get(0).size());
  }
  
  @Test
  public void test36_PolicyPatchRemoveEntMetadata() throws IOException, JsonProcessingException, ScimException,
                                                   ProcessingException, ParseException {
    
    Response response = patch("Policies/TestAP999", "policy-patch-remove-ent-metadata.json");
   
    // Check if the AccessPolicy description was updated
    JsonNode updatedPolicy = getJsonNode(response.readEntity(String.class));
    String path = "applications[applicationName eq \"APHAccount\"].namespaces[namespace eq \"UD_APH_POL\"].entitlements[value eq \"184~1\"].additionalAttributes.attributes";
    List<JsonNode>  matchData = Support.matchPath(PathParser.path(path), (ObjectNode)updatedPolicy);
    
    //Check if entitlement attribute is removed
    Assert.assertEquals(0, matchData.size());
  }
  
  @Test
  public void test37_PolicyPatchAddEntMetadata() throws IOException, JsonProcessingException, ScimException,
                                                   ProcessingException, ParseException {
    
    Response response = patch("Policies/TestAP999", "policy-patch-add-ent-metadata.json");
   
    // Check if the AccessPolicy description was updated
    JsonNode updatedPolicy = getJsonNode(response.readEntity(String.class));
    String path = "applications[applicationName eq \"APHAccount\"].namespaces[namespace eq \"UD_APH_POL\"].entitlements[value eq \"184~1\"].additionalAttributes.attributes";
    List<JsonNode>  matchData = Support.matchPath(PathParser.path(path), (ObjectNode)updatedPolicy);
    
    //Check if entitlement attribute is updated
    Assert.assertEquals(1, matchData.get(0).size());
  }


  
  @Test
  public void test37_PolicyPatchAddTwoEnt() throws IOException, JsonProcessingException, ScimException,
                                                   ProcessingException, ParseException {
    
    Response response = patch("Policies/TestAP999", "policy-patch-add-ents.json");
   
    // Check if the AccessPolicy description was updated
    JsonNode updatedPolicy = getJsonNode(response.readEntity(String.class));
    String path = "applications[applicationName eq \"APHAccount\"].namespaces[namespace eq \"UD_APH_POL\"].entitlements";
    List<JsonNode>  matchData = Support.matchPath(PathParser.path(path), (ObjectNode)updatedPolicy);
    
    //Check if two new entitlement is added
    Assert.assertEquals(4, matchData.get(0).size());
  }



  @Test
  public void test90_PolicyDelete(){
    // Delete Simple Policy
    Response response = delete("Policies/TestAP999");
  }
  

  
  
}