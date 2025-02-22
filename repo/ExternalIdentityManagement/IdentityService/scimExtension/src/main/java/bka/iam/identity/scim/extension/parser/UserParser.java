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

    File        :   UserResourceAppender.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   jovan.j.lakic@oracle.com

    Purpose     :   This file implements the class
                    UserResourceAppender.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-25-05  JLakic     First release version
*/
package bka.iam.identity.scim.extension.parser;

import bka.iam.identity.scim.extension.exception.ScimException;
import bka.iam.identity.scim.extension.model.Attribute;
import bka.iam.identity.scim.extension.model.AttributeValue;
import bka.iam.identity.scim.extension.model.MultiValueComplexAttribute;
import bka.iam.identity.scim.extension.model.SingularComplexAttribute;
import bka.iam.identity.scim.extension.model.SingularSimpleAttribute;
import bka.iam.identity.scim.extension.resource.User;
import bka.iam.identity.scim.extension.utils.ScimConstants;
import bka.iam.identity.zero.api.AccountsFacade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import oracle.iam.identity.igs.model.AccountEntity;
import oracle.iam.identity.igs.model.NamespaceEntity;

public class UserParser {
    
    /**
     * Appends application extension to the user resource.
     *
     * @param user              The user resource to which the application extension will be appended.
     * @param principalUsername The principal username for retrieving accounts.
     * @param facade            The facade for accessing account information.
     */

    public static void apppendApplicationExtension(User user, String principalUsername, AccountsFacade facade) {
        Map<String, List<AccountEntity>> accounts = facade.listAccounts(user.getId(), principalUsername);
        Collection<AttributeValue> apps = new ArrayList<>();
        
        for (String appKey : accounts.keySet()) {
          
            List<AccountEntity> accountEntities = accounts.get(appKey);

            for (AccountEntity accountEntity : accountEntities) {
                final Map<String, List<NamespaceEntity>> ns = accountEntity.namespace();
                
                MultiValueComplexAttribute applicationNamespace = null;
                if (ns != null) {
                  applicationNamespace = new MultiValueComplexAttribute("namespaces", processNamespaces(ns));
                }
                MultiValueComplexAttribute applicationAttributes = new MultiValueComplexAttribute("applicationAttributes", extractApplicationAttributes(accountEntity));
                
                apps.add(createApplication(appKey, accountEntity.status(), applicationAttributes, applicationNamespace));
            }
          
        }

        final MultiValueComplexAttribute applications      = new MultiValueComplexAttribute("applications", apps.toArray(new AttributeValue[0]));
        final SingularComplexAttribute   applicationSchema = new SingularComplexAttribute(User.SCHEMAS[3], new AttributeValue(applications));
        user.add(applicationSchema.clone());
        user.addSchema(ScimConstants.USER_APPLICATION_SCHEMA_URN);
        //setUserApplicationAndSchema(user, appExtension);
    }

  /**
   * Convert zero model List{@code <AccountEntity>} to MultiValueComplexAttribute
   * @param accountEntities
   * @return
   * @throws ScimException
   */
  public static MultiValueComplexAttribute createApplicationUserExtension(final List<AccountEntity> accountEntities )
      throws ScimException {
      
      Collection<AttributeValue> apps = new ArrayList<>();
    
      for (AccountEntity accountEntity : accountEntities) {
          final Map<String, List<NamespaceEntity>> ns = accountEntity.namespace();
          
          MultiValueComplexAttribute applicationNamespace = null;
          if (ns != null) {
            applicationNamespace = new MultiValueComplexAttribute("namespaces", processNamespaces(ns));
          }
          MultiValueComplexAttribute applicationAttributes = new MultiValueComplexAttribute("applicationAttributes", extractApplicationAttributes(accountEntity));
          
          apps.add(createApplication(accountEntity.id(), accountEntity.status(), applicationAttributes, applicationNamespace));
      }
        
      return new MultiValueComplexAttribute("applications", apps.toArray(new AttributeValue[0]));
    }


    /**
     * Extracts application attributes from the given account entity.
     *
     * @param accountEntity The account entity from which application attributes will be extracted.
     * @return Collection of extracted application attributes.
     */
    private static AttributeValue[] extractApplicationAttributes(AccountEntity accountEntity) {
        final List<AttributeValue> attributeValue   = new LinkedList<AttributeValue>();
        final Map<String, Object> accountAttributes = accountEntity.attribute();

        for (String attrKey : accountAttributes.keySet()) {
              if (accountAttributes.get(attrKey) != null && attrKey.startsWith("UD_")) {
            //if (accountAttributes.get(attrKey) != null && !attrKey.startsWith("UD_")) {
                final Attribute[] attribute = new Attribute[2];
                attribute[0] = new SingularSimpleAttribute("name", new AttributeValue(attrKey));
                attribute[1] = new SingularSimpleAttribute("value", new AttributeValue(accountAttributes.get(attrKey)));
                
                attributeValue.add(new AttributeValue(attribute));
            }
        }

        return attributeValue.toArray(new AttributeValue[0]);
    }

    /**
     * Creates an application with the specified attributes.
     *
     * @param appName    The name of the application.
     * @param status     The status of the application.
     * @param attributes The application attributes.
     * @return Created application.
     */
    private static AttributeValue createApplication(String appName, String status,
                                                 MultiValueComplexAttribute applicationAttribute, MultiValueComplexAttribute namespaceAttribute) {
      
        SingularSimpleAttribute applicationName= new SingularSimpleAttribute("applicationName", new AttributeValue(appName));
        SingularSimpleAttribute applicationStatus = new SingularSimpleAttribute("status", new AttributeValue(status));
        
        List<Attribute> attribute = new ArrayList<>();
        attribute.add(applicationName);
        attribute.add(applicationStatus);
        attribute.add(applicationAttribute);
        
        if (namespaceAttribute != null)
          attribute.add(namespaceAttribute);
        
        return new AttributeValue(attribute.toArray(new Attribute[0]));
    }

    /**
     * Processes namespaces from the given map and creates a collection of Namespace objects.
     *
     * @param ns The map containing namespace entities.
     * @return Collection of created Namespace objects.
     */
    private static AttributeValue[] processNamespaces(Map<String, List<NamespaceEntity>> ns) {
        final List<AttributeValue> namespaces = new LinkedList<>(); 

        for (String key : ns.keySet()) {
            List<NamespaceEntity> nsEntities = ns.get(key);
            for (NamespaceEntity nsEntity : nsEntities) {
                namespaces.add(AccountEntityParser.createNamespaceFromEntity(nsEntity));
            }
        }

        return namespaces.toArray(new AttributeValue[0]);
    }

    /**
     * Sets the user application and schema based on the provided application extension.
     *
     * @param user          The user resource to be updated.
     * @param appExtension  The application extension.
     */
    /*private static void setUserApplicationAndSchema(User user, Attribute appExtension) {
        user.setUserApplication(appExtension);
        if (user.getSchemas() != null && !user.getSchemas().isEmpty()) {
            user.getSchemas().add(ScimConstants.USER_APPLICATION_SCHEMA_URN);
        }
    }*/

}
