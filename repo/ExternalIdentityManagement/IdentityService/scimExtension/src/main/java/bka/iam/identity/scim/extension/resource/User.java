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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager
    Subsystem   :   Custom SCIM Service

    File        :   User.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@silverid.fr

    Purpose     :   This file implements the User class.

    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-20-11  SBernet     First release version
*/
package bka.iam.identity.scim.extension.resource;

import bka.iam.identity.scim.extension.exception.ScimException;
import bka.iam.identity.scim.extension.model.Attribute;
import bka.iam.identity.scim.extension.model.Resource;
import bka.iam.identity.scim.extension.model.ResourceDescriptor;
import bka.iam.identity.scim.extension.model.SchemaDescriptor;
import bka.iam.identity.scim.extension.model.ScimResource;
import bka.iam.identity.scim.extension.parser.Unmarshaller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class User extends ScimResource {
  
  public static final String[] SCHEMAS = new String[] {
    "urn:ietf:params:scim:schemas:core:2.0:User",
    "urn:ietf:params:scim:schemas:extension:enterprise:2.0:User",
    "urn:ietf:params:scim:schemas:extension:police:2.0:User"/*,
    "urn:ietf:params:scim:schemas:extension:oracle:2.0:IDM:User",
    "urn:ietf:params:scim:schemas:extension:oracle:2.0:OIG:User",
    "urn:ietf:params:scim:schemas:extension:oracle:2.0:OIG:UserApplication"*/
  };
  
  
  public User(final ResourceDescriptor descriptor) {
    super(descriptor);
  }
  
  public User(final ResourceDescriptor descriptor, List<Attribute> attributes) {
    super(descriptor, attributes);
  }
  
  public String getUserName() {
    return getAttributeValue("userName");
  }
  
  public Attribute getEmails() {
    return get("emails");
  }
  
  public String getGivenName() {
    return getAttributeValue("name.givenName");
  }
  
  public void setDisplayName(final String displayName) {
    set("displayName", displayName);
  }
  
  @Override
  public String[] getSchemaURIs() {
    return SCHEMAS;
  }
  
  @Override
  public Resource clone() {
    return new User(new ResourceDescriptor(this.getResourceDescriptor().getSchema()), this.attributes);
  }

/**
 * Adds a group to the resource.
 */
public void addGroup(final int index, final String type, final String value, final String ref)
    throws ScimException {
    addAttribute(String.format("groups[%d].type", index), type);
    addAttribute(String.format("groups[%d].value", index), value);
    addAttribute(String.format("groups[%d].$ref", index), ref);
}
  
    public static void main(String[] args)
    throws Exception {
    System.out.println("Test");
    final Set<SchemaDescriptor> descriptors = new HashSet<SchemaDescriptor>();
    descriptors.add(loadLocalDescriptor());
    final bka.iam.identity.scim.extension.resource.User userScim = new bka.iam.identity.scim.extension.resource.User(new ResourceDescriptor(descriptors));
    
    userScim.addSchema("urn:ietf:params:scim:schemas:core:2.0:User");
    System.out.println(userScim);
    System.out.println(userScim.getAttribute("userName"));
    System.out.println(userScim.getAttribute("emails"));
    System.out.println(userScim.getAttribute("emails[work].value"));
    
    System.out.println(userScim.getAttribute("name.familyName"));
    System.out.println(userScim.getAttribute("groups[0].value"));
    //userScim.addAttribute("urn:ietf:params:scim:schemas:core:2.0:User:provisioningDate", "2023-06-16T02:00:00.000+02:00");
    /*Map<String, Object> workAdress = new HashMap<String, Object>();
    workAdress.put("type", "work");
    workAdress.put("country", "France");
    userScim.addMultiComplexAttribute("addresses", workAdress);
    
    userScim.addSingularComplexAttribute("urn:ietf:params:scim:schemas:extension:oracle:2.0:OIG:User:provisioningDate", "2023-06-16T02:00:00.000+02:00");
    Map<String, Object> group = new HashMap<String, Object>();
    group.put("display", "AN Property User");
    group.put("value", "28");
    userScim.addMultiComplexAttribute("urn:ietf:params:scim:schemas:extension:oracle:2.0:OIG:User:organizations", group);
    userScim.addMultiComplexAttribute("urn:ietf:params:scim:schemas:extension:oracle:2.0:OIG:User:test.organi", group);*/
    System.out.println(userScim.toString());
    
  }
    
  private static SchemaDescriptor loadLocalDescriptor() {

    String[] localSchemaPath = new String[1];
    localSchemaPath[0] = "C:\\Oracle\\Customer\\Project\\Bundeskriminalamt12c\\ExternalIdentityManagement\\IdentityService\\scimExtension\\src\\main\\static\\resources\\scim-user-core-schema.json";

    for (String schemaFilePath : localSchemaPath) {
      final File           is = new File((schemaFilePath));
      try {
        final JsonNode         jsonNode         = new ObjectMapper().readTree(new FileInputStream(is));
        final SchemaDescriptor schemadescriptor = Unmarshaller.jsonNodetoSchema(jsonNode);
        
        return schemadescriptor;
      }
      catch (IOException e) {
        System.out.println(e);
      }
    }
    return null;
  }
}
