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

    File        :   PolicyMapper.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   tomas.t.sebo@oracle.com

    Purpose     :   This file implements the class
                    PolicyMapper.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2024-10-28  TSebo     First release version
*/
package bka.iam.identity.scim.extension.mapper;

import bka.iam.identity.scim.extension.exception.ScimException;
import bka.iam.identity.scim.extension.exception.ScimMessage;
import bka.iam.identity.scim.extension.exception.resource.ScimBundle;
import bka.iam.identity.scim.extension.model.Attribute;
import bka.iam.identity.scim.extension.model.AttributeValue;
import bka.iam.identity.scim.extension.model.ListResponse;
import bka.iam.identity.scim.extension.model.MultiValueComplexAttribute;
import bka.iam.identity.scim.extension.model.ResourceDescriptor;
import bka.iam.identity.scim.extension.model.ScimResource;
import bka.iam.identity.scim.extension.model.SingularSimpleAttribute;
import bka.iam.identity.scim.extension.resource.Policy;
import bka.iam.identity.scim.extension.rest.HTTPContext;
import bka.iam.identity.scim.extension.rest.OIMSchema;
import bka.iam.identity.scim.extension.rest.OIMScimContext;
import bka.iam.identity.scim.extension.utils.WLSUtil;

import java.net.URI;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import oracle.iam.identity.igs.model.AccountEntity;
import oracle.iam.identity.igs.model.AdditionalAttributeEntity;
import oracle.iam.identity.igs.model.EntitlementEntity;
import oracle.iam.identity.igs.model.Entity;
import oracle.iam.identity.igs.model.NamespaceEntity;
import oracle.iam.identity.igs.model.PolicyEntity;
import oracle.iam.identity.igs.model.Risk;

///////////////////////////////////////////////////////////////////////////////
// class PolicyMapper
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** The <code>PolicyMapper</code> class provides mapping between backend service policy object
 ** represented as {@code PolicyEntity} to the policy SCIM object represented as {@code Policy} 
 ** and vice versa.
 ** 
 ** @author  tomas.t.sebo@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class PolicyMapper {
  
  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////
  private static final String ACCESS_POLICY_TYPE = "AccessPolicy";
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPolicy
  /**
   ** Get list of the SCIM {@code Policy} instances from the list of the {@code PolicyEntity} instances.
   ** @param policyEntities   List of the Policy Entities from the backed service
   ** @return                 List of the SCIM Policies
   ** @throws ScimException   Exception is thrown in case the  policyEntity is null
   */
  public static ListResponse<Policy> getPolicy(oracle.hst.platform.rest.response.ListResponse<PolicyEntity> policyEntities) throws ScimException
                                                                                                                            {
    List<Policy> policies = new ArrayList<Policy>();
    Iterator<PolicyEntity> i = policyEntities.iterator();
    while(i.hasNext()){
      policies.add(getPolicy(i.next()));
    }
          
    return new ListResponse<Policy>(policies,policyEntities.total(), policyEntities.start(), policyEntities.items());
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPolicy
  /**
   ** Get the SCIM {@code Policy} instance from the {@code PolicyEntity} instance.
   ** 
   ** @param policyEntity     Policy Entity from backed service
   ** @return                 SCIM Policy {@code Policy} instance
   ** @throws ScimException   Exception is thrown in case the  policyEntity is null
   */
  public static Policy getPolicy(PolicyEntity policyEntity) throws ScimException {
    return getPolicy(policyEntity, true);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPolicy
  /**
   ** Get the SCIM {@code Policy} instance from the {@code PolicyEntity} instance.
   ** 
   ** @param policyEntity     Policy Entity from backed service
   ** @param exclueEmpty      If this attribute is set to true empty elements are not created
   ** @return                 SCIM Policy {@code Policy} instance
   ** @throws ScimException   Exception is thrown in case the  policyEntity is null
   */
  public static Policy getPolicy(PolicyEntity policyEntity, boolean exclueEmpty) 
                                 throws ScimException {
      
    if (policyEntity == null) {
      throw new ScimException(HTTPContext.StatusCode.NOT_FOUND, ScimBundle.format(ScimMessage.POLICY_NOTFOUND,policyEntity));
    }
    
    final List<Attribute> attribute = new ArrayList<>();
    final ResourceDescriptor resourceDescriptor = OIMSchema.getInstance().getResourceDescriptorByResourceType(Policy.class);
    attribute.add(ScimResource.createSchema(Policy.SCHEMAS));
    attribute.add(ScimResource.createID(policyEntity.id()));
    attribute.add(ScimResource.createMeta(policyEntity.createDate(), policyEntity.updateDate(), URI.create(WLSUtil.getServerURL() + OIMScimContext.OIM_EXTENTION_ENDPOINT_SCIM + "/" + OIMScimContext.ENDPOINT_POLICIES + "/" + policyEntity.id()), OIMSchema.POLICY));
    attribute.add(new SingularSimpleAttribute("description", new AttributeValue(policyEntity.description())));
    attribute.add(new SingularSimpleAttribute("owner"  ,     new AttributeValue(policyEntity.ownerId())));
    attribute.add(new SingularSimpleAttribute("ownerType",   new AttributeValue(policyEntity.ownerType())));
    attribute.add(new SingularSimpleAttribute("priority",    new AttributeValue(Integer.valueOf((int)policyEntity.priority()))));
    
    attribute.add(createApplications(policyEntity.account(),exclueEmpty));  
    return new Policy(resourceDescriptor, attribute);    
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPolicyEntity
  /**
   ** Get the policy entiry {@code PolicyEntity} instance from the SCIM Policy {@code Policy} instance.
   ** 
   ** @param policy          SCIM policy object
   ** @return                Backend PolicyEntity as a {@code PolicyEntity} instance.
   */
  public static PolicyEntity getPolicyEntity(Policy policy){
    
    PolicyEntity policyEntity = Entity.policy(policy.getId())
                                      .description(policy.getDescription())
                                      .ownerType(policy.getOwnerType())
                                      .ownerId(policy.getOwner())
                                      .type(ACCESS_POLICY_TYPE)
                                      .account(createAccountEntities(policy.getApplications())) ;
    if(policy.getPriority() != 0){
      policyEntity.priority(policy.getPriority());
    }
    
    return policyEntity;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Private Methods
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAccountEntities
  /**
   ** Convert SCIM Policy Appliactions to the zero model list of Acount Entities
   ** 
   ** @param applications     SCIM Policy Appliactions as a {@code MultiValueComplexAttribute} instance.
   ** @return
   */
  private static List<AccountEntity> createAccountEntities(MultiValueComplexAttribute applications) {
    
    List<AccountEntity> accountEntries = new ArrayList<AccountEntity>();
    if(applications != null){
      AttributeValue[] apps = applications.getValues();
      if(apps != null){
        for(AttributeValue app : apps){
          AccountEntity accountEntry = Entity.account(app.getSubAttributeStringValue("applicationName"))
                                             .status(app.getSubAttributeStringValue("status"));
          accountEntry.putAll(createAccountEntityAttributes(app.getSubAttribute("applicationAttributes")));
          Collection<NamespaceEntity> namespaceEntities = createAccountEntityNamespaces(app.getSubAttribute("namespaces"));
          for(NamespaceEntity namespaceEntity : namespaceEntities ){
            accountEntry.namespace(namespaceEntity);  
          }
          accountEntries.add(accountEntry);
        }
      }
    }
    return accountEntries;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAccountEntityAttributes
  /**
   ** Convert SCIM Policy Appliaction Entity Attributes to the zero model entity attributes
   **
   ** @param applicationAttributes  List of the SCIM Policy Account Entity Attributes
   ** @return                       Map of the zero model application attributes
   */
  private static Map<String, Object> createAccountEntityAttributes(Attribute applicationAttributes) {
    Map<String,Object> attributeMap = new HashMap<String,Object>();
    if(applicationAttributes != null){
      AttributeValue[] attributes = applicationAttributes.getValues();
      for(AttributeValue attribute: attributes ){
        attributeMap.put(attribute.getSubAttributeStringValue("name"),attribute.getSubAttributeStringValue("value"));
      }
    }    
    return attributeMap;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAccountEntityNamespaces
  /**
   ** Convert SCIM Policy Application  namespaces into the collection of the zero model namespaces
   **
   ** @param namespacesAttribute  SCIM Policy Application namespaces
   ** @return                     Zero model collection of namespaces as a{@code NamespaceEntity} instances
   */
  private static Collection<NamespaceEntity> createAccountEntityNamespaces(Attribute namespacesAttribute){
    Collection<NamespaceEntity> namespaceEntities = new ArrayList<NamespaceEntity>();
    if(namespacesAttribute != null){
      AttributeValue[] namespaces = namespacesAttribute.getValues();
      for(AttributeValue namespace : namespaces){
        NamespaceEntity namespaceEntity = Entity.namespace(namespace.getSubAttributeStringValue("namespace"));
        Attribute entitlementsAttribute = namespace.getSubAttribute("entitlements");
        if(entitlementsAttribute != null){
          AttributeValue[] entitlements = entitlementsAttribute.getValues();
          for(AttributeValue entitlement : entitlements){
            EntitlementEntity.Action action = (entitlement.getSubAttributeStringValue("action") != null ? EntitlementEntity.Action.from(entitlement.getSubAttributeStringValue("action")):EntitlementEntity.Action.assign);
            EntitlementEntity entitlementEntity = Entity.entitlement(action, Risk.none)
                                                        .status(entitlement.getSubAttributeStringValue("action"));
            
            entitlementEntity.put(entitlement.getSubAttributeStringValue("name"), entitlement.getSubAttributeStringValue("value"));          
            Attribute additionalAttributes = entitlement.getSubAttribute("additionalAttributes");
            if(additionalAttributes != null){
              AttributeValue[] additionalAttributesValues = additionalAttributes.getValues();
              for(AttributeValue additionalAttributesValue : additionalAttributesValues){
                Attribute attributesAttribute = additionalAttributesValue.getSubAttribute("attributes");
                if(attributesAttribute != null){
                  AttributeValue[] attributes = attributesAttribute.getValues();
                  for(AttributeValue attribute : attributes){
                    AdditionalAttributeEntity aa = Entity.additionalAttribute();
                    aa.put(attribute.getSubAttributeStringValue("name"), attribute.getSubAttributeStringValue("value"));
                    entitlementEntity.addAdditionalAttribute(aa);
                  }
                }
              }
            }
            namespaceEntity.add(entitlementEntity);
          }
          namespaceEntities.add(namespaceEntity);
        }
      }
    }
    return namespaceEntities;
  }
  
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   createApplications
  /**
   ** Convert zero model List{@code <AccountEntity>} to MultiValueComplexAttribute
   ** 
   ** @param accountEntities    Zero model list of the  account entities
   ** @param exclueEmpty        If this attribute is set to  true empty elements are not created
   ** @return                   The list of the Applications as a {@code MultiValueComplexAttribute} 
   **                           instance.
   */
  private static MultiValueComplexAttribute createApplications(final List<AccountEntity> accountEntities, boolean exclueEmpty ){
      
      Collection<AttributeValue> apps = new ArrayList<>();
    
      for (AccountEntity accountEntity : accountEntities) {
          final Map<String, List<NamespaceEntity>> ns = accountEntity.namespace();
          
          MultiValueComplexAttribute applicationNamespace = null;
          if (ns != null) {
            applicationNamespace = new MultiValueComplexAttribute("namespaces", processNamespaces(ns,exclueEmpty));
          }
          MultiValueComplexAttribute applicationAttributes = new MultiValueComplexAttribute("applicationAttributes", extractApplicationAttributes(accountEntity));
          
          apps.add(createApplication(accountEntity.id(), accountEntity.status(), applicationAttributes, applicationNamespace));
      }
        
      return new MultiValueComplexAttribute("applications", apps.toArray(new AttributeValue[0]));
    }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   createApplication
  /**
   ** Creates an application with the specified attributes.
   **
   ** @param appName               The name of the application.
   ** @param status                The status of the application.
   ** @param applicationAttribute  The application attributes.
   ** @param namespaceAttribute    The namespace attributes
   ** @return                      Application as a {@code AttributeValue} instance.
   */
  private static AttributeValue createApplication(String appName, 
                                                  String status,
                                                  MultiValueComplexAttribute applicationAttribute, 
                                                  MultiValueComplexAttribute namespaceAttribute) {
    
      SingularSimpleAttribute applicationName= new SingularSimpleAttribute("applicationName", new AttributeValue(appName));
      SingularSimpleAttribute applicationStatus = new SingularSimpleAttribute("status", new AttributeValue(status));
      
      List<Attribute> attribute = new ArrayList<>();
      attribute.add(applicationName);
      if(status != null && status.length() >0 )
        attribute.add(applicationStatus);
      attribute.add(applicationAttribute);
      
      if (namespaceAttribute != null)
        attribute.add(namespaceAttribute);
      
      return new AttributeValue(attribute.toArray(new Attribute[0]));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processNamespaces
  /**
   ** Processes namespaces from the given map and creates a collection of Namespace objects.
   **
   ** @param ns The map containing namespace entities.
   ** @param exclueEmpty If this attribute is set to true empty elements are not created
   ** @return Collection of created Namespace objects.
   */
  private static AttributeValue[] processNamespaces(Map<String, List<NamespaceEntity>> ns, boolean exclueEmpty) {
      final List<AttributeValue> namespaces = new LinkedList<>(); 

      for (String key : ns.keySet()) {
          List<NamespaceEntity> nsEntities = ns.get(key);
          for (NamespaceEntity nsEntity : nsEntities) {
              namespaces.add(createNamespaceFromEntity(nsEntity,exclueEmpty));
          }
      }

      return namespaces.toArray(new AttributeValue[0]);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   createNamespaceFromEntity
  /**
   ** Creates a {@code Namespace} instance from a {@code NamespaceEntity}.
   **
   ** @param namespaceEntity  The {@code NamespaceEntity} to convert.
   ** @param exclueEmpty      If this attribute is set to  true empty elements are not created
   ** @return                 A {@code AttributeValue} instance.
   */
  private static AttributeValue createNamespaceFromEntity(NamespaceEntity namespaceEntity, boolean exclueEmpty) {
      final Attribute[] nameSpaceAttribute = new Attribute[2];
      
      nameSpaceAttribute[0]  = new SingularSimpleAttribute("namespace", new AttributeValue(namespaceEntity.id()));
      nameSpaceAttribute[1]  = new MultiValueComplexAttribute("entitlements", createEntitlementsFromEntities(namespaceEntity.element(),exclueEmpty));
      
      return new AttributeValue(nameSpaceAttribute);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   createEntitlementsFromEntities
  /**
   ** Creates a array of {@code AttributeValue} instances from a list of {@code EntitlementEntity} instances.
   **
   ** @param entitlementEntities  The list of {@code EntitlementEntity} instances.
   ** @param exclueEmpty          If this attribute is set to  true empty elements are not created
   ** @return                     An array of {@code AttributeValue} instances.
   */
  private static AttributeValue[] createEntitlementsFromEntities(List<EntitlementEntity> entitlementEntities, boolean exclueEmpty) {
      List<AttributeValue> entitlements = new LinkedList<>();
      
      for (EntitlementEntity entEntity : entitlementEntities) {
          entitlements.add(createEntitlementFromEntity(entEntity,exclueEmpty));
      }
      return entitlements.toArray(new AttributeValue[0]);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   createEntitlementFromEntity
  /**
   ** Creates an {@code AttributeValue} instance from an {@code EntitlementEntity}.
   **
   ** @param entEntity      The {@code EntitlementEntity} to convert.
   ** @param exclueEmpty    If this attribute is set to  true empty elements are not created
   ** @return               An {@code AttributeValue} instance.
   */
  private static AttributeValue createEntitlementFromEntity(EntitlementEntity entEntity,boolean exclueEmpty) {
      
      Collection<Attribute> attributes =new ArrayList<>();
      
      for (Map.Entry<String, Object> entry : entEntity.attribute().entrySet()) {
        attributes.add(new SingularSimpleAttribute("name", new AttributeValue(entry.getKey())));
        attributes.add(new SingularSimpleAttribute("value", new AttributeValue(entry.getValue())));      
      }
      
      MultiValueComplexAttribute aa = createEntitlementAdditionalAttributes(entEntity.additionalAttributes(),exclueEmpty);
      if(aa != null)
        attributes.add(aa);
      
      return new AttributeValue(attributes.toArray(new Attribute[0]));
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   MultiValueComplexAttribute
  /**
   ** Creates a complex attribute of {@code MultiValueComplexAttribute} instances from a list of attributes.
   **
   ** @param additionalAttributes The list of the additional of attributes.
   ** @param exclueEmpty          If this attribute is set to  true empty elements are not created
   ** @return                     Entitlement Additional Attributes of {@code MultiValueComplexAttribute} instance.
   */
  private static MultiValueComplexAttribute createEntitlementAdditionalAttributes(List<AdditionalAttributeEntity> additionalAttributes,boolean exclueEmpty) {
    
      Collection<AttributeValue> attributeValues = new ArrayList<>();
      for (AdditionalAttributeEntity additionalAttribute : additionalAttributes) {
        for (Map.Entry<String, Object> entry : additionalAttribute.entrySet()){
          Attribute[] value = new Attribute[2];
          value[0]  = new SingularSimpleAttribute("name", new AttributeValue(entry.getKey()));
          value[1]  = new SingularSimpleAttribute("value", new AttributeValue(entry.getValue()));
          attributeValues.add(new AttributeValue(value));
        }
      }
      
      if(attributeValues.size() == 0 && exclueEmpty){
        return null; 
      }
      else{
        AttributeValue[] additionalAttributeValues = new AttributeValue[1];
        additionalAttributeValues[0] = new AttributeValue(new MultiValueComplexAttribute("attributes", attributeValues.toArray(new AttributeValue[0])));
        return  new MultiValueComplexAttribute("additionalAttributes",additionalAttributeValues );
      }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   extractApplicationAttributes
  /**
   ** Extracts application attributes from the given account entity.
   **
   ** @param accountEntity  The account entity from which application attributes will be extracted.
   ** @return               Array of {@code AttributeValue} attribute values.
   */
  private static AttributeValue[] extractApplicationAttributes(AccountEntity accountEntity) {
      final List<AttributeValue> attributeValue   = new LinkedList<AttributeValue>();
      final Map<String, Object> accountAttributes = accountEntity.attribute();

      for (String attrKey : accountAttributes.keySet()) {
            if (accountAttributes.get(attrKey) != null && attrKey.startsWith("UD_")) {
              final Attribute[] attribute = new Attribute[2];
              attribute[0] = new SingularSimpleAttribute("name", new AttributeValue(attrKey));
              attribute[1] = new SingularSimpleAttribute("value", new AttributeValue(accountAttributes.get(attrKey)));
          
              attributeValue.add(new AttributeValue(attribute));
          }
      }

      return attributeValue.toArray(new AttributeValue[0]);
  }
}
