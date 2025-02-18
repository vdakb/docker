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

import bka.iam.identity.scim.extension.model.Attribute;
import bka.iam.identity.scim.extension.model.Resource;
import bka.iam.identity.scim.extension.model.ResourceDescriptor;
import bka.iam.identity.scim.extension.model.ScimResource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class User extends ScimResource {
  
  public static final String[] SCHEMAS = new String[] {
    "urn:ietf:params:scim:schemas:core:2.0:User",
    "urn:ietf:params:scim:schemas:extension:enterprise:2.0:User",
    "urn:ietf:params:scim:schemas:extension:oracle:2.0:IDM:User",
    "urn:ietf:params:scim:schemas:extension:oracle:2.0:OIG:User",
    "urn:ietf:params:scim:schemas:extension:oracle:2.0:OIG:UserApplication"
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
  
  @Override
  public String[] getSchemaURIs() {
    return SCHEMAS;
  }
  
    public static void main(String[] args) {
    List<String> requiredAttribute = Arrays.asList("test","active", "urn:ietf:params:scim:schemas:core:2.0:User:active", "urn:ietf:params:scim:schemas:extension:oracle:2.0:IDM:User:locked.value");
    List<String> specialAttributeHandling = Arrays.asList("urn:ietf:params:scim:schemas:extension:oracle:2.0:IDM:User:locked", "urn:ietf:params:scim:schemas:core:2.0:User:active", "active");
    
    // Schema violation
    requiredAttribute = requiredAttribute.stream().filter(attribute ->   !specialAttributeHandling.stream().anyMatch(special -> attribute.startsWith(special)))
                              .collect(Collectors.toList());
    System.out.println(requiredAttribute);
  }
  @Override
  public Resource clone() {
    return new User(new ResourceDescriptor(this.getResourceDescriptor().getSchema()), this.attributes);
  }
}
