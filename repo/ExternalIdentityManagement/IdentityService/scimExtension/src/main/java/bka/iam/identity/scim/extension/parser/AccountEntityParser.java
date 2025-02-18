package bka.iam.identity.scim.extension.parser;

import bka.iam.identity.scim.extension.model.Attribute;
import bka.iam.identity.scim.extension.model.AttributeValue;
import bka.iam.identity.scim.extension.model.MultiValueComplexAttribute;
import bka.iam.identity.scim.extension.model.SingularSimpleAttribute;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import oracle.iam.identity.igs.model.EntitlementEntity;
import oracle.iam.identity.igs.model.NamespaceEntity;

public class AccountEntityParser {


    /**
     * Merges the modification request with the existing account entity.
     *
     * @param accountResource The modified account resource.
     * @param accountEntity   The existing account entity.
     */
    /*public static void mergeRequestWithExistingEntity(Application accountResource, AccountEntity accountEntity) {
    AccountEntityParser.mergeNamespaces(accountResource, accountEntity);
    }*/

    /**
     * Merges namespaces from an {@code AccountEntity} into an {@code AccountResource}.
     *
     * @param application The target {@code AccountResource} to merge into.
     * @param accountEntity   The source {@code AccountEntity} containing namespaces.
     */
    /*public static void mergeNamespaces(Application application, AccountEntity accountEntity) {
        Map<String, List<NamespaceEntity>> accountEntityNamespaces = accountEntity.namespace();
        if (accountEntityNamespaces != null) {
            for (String key : accountEntityNamespaces.keySet()) {
                Collection<Namespace> tmpNamespaces = createNamespacesFromEntities(accountEntityNamespaces.get(key));
                addOrUpdateNamespaces(application.getNamespaces(), tmpNamespaces);
            }
        }
    }*/

    /**
     * Creates a collection of {@code Namespace} instances from a list of {@code NamespaceEntity} instances.
     *
     * @param namespaceEntities The list of {@code NamespaceEntity} instances.
     * @return A collection of {@code Namespace} instances.
     */
    /*public static Collection<Namespace> createNamespacesFromEntities(List<NamespaceEntity> namespaceEntities) {
        Collection<Namespace> namespaces = new ArrayList<>();
        for (NamespaceEntity nsEntity : namespaceEntities) {
            Namespace namespace = createNamespaceFromEntity(nsEntity);
            namespaces.add(namespace);
        }
        return namespaces;
    }*/

    /**
     * Creates a {@code Namespace} instance from a {@code NamespaceEntity}.
     *
     * @param namespaceEntity The {@code NamespaceEntity} to convert.
     * @return A {@code Namespace} instance.
     */
    public static AttributeValue createNamespaceFromEntity(NamespaceEntity namespaceEntity) {
        final Attribute[] nameSpaceAttribute = new Attribute[2];
        
        nameSpaceAttribute[0]  = new SingularSimpleAttribute("namespace", new AttributeValue(namespaceEntity.id()));
        nameSpaceAttribute[1]  = new MultiValueComplexAttribute("entitlements", createEntitlementsFromEntities(namespaceEntity.element()));
        
        return new AttributeValue(nameSpaceAttribute);
    }

    /**
     * Creates a collection of {@code Entitlement} instances from a list of {@code EntitlementEntity} instances.
     *
     * @param entitlementEntities The list of {@code EntitlementEntity} instances.
     * @return A collection of {@code Entitlement} instances.
     */
    public static AttributeValue[] createEntitlementsFromEntities(List<EntitlementEntity> entitlementEntities) {
        List<AttributeValue> entitlements = new LinkedList<>();
        
        for (EntitlementEntity entEntity : entitlementEntities) {
            entitlements.add(createEntitlementFromEntity(entEntity));
        }
        return entitlements.toArray(new AttributeValue[0]);
    }

    /**
     * Creates an {@code Entitlement} instance from an {@code EntitlementEntity}.
     *
     * @param entEntity The {@code EntitlementEntity} to convert.
     * @return An {@code Entitlement} instance.
     */
    public static AttributeValue createEntitlementFromEntity(EntitlementEntity entEntity) {
        Attribute[] attribute = new Attribute[2];
        
        attribute[0] = new SingularSimpleAttribute("status", new AttributeValue(entEntity.status()));
        attribute[1] = new MultiValueComplexAttribute("attributes", createAttributesFromEntity(entEntity.attribute()));
        
        return new AttributeValue(attribute);
    }

    /**
     * Creates a collection of {@code NameValueAttribute} instances from a map of attributes.
     *
     * @param attributeMap The map of attributes.
     * @return A collection of {@code NameValueAttribute} instances.
     */
    public static AttributeValue[] createAttributesFromEntity(Map<String, Object> attributeMap) {
        Collection<AttributeValue> attributes = new ArrayList<>();
        
        for (Map.Entry<String, Object> entry : attributeMap.entrySet()) {
          Attribute[] value = new Attribute[2];

          value[0]  = new SingularSimpleAttribute("name", new AttributeValue(entry.getKey()));
          value[1]  = new SingularSimpleAttribute("value", new AttributeValue(entry.getValue()));
          attributes.add(new AttributeValue(value));
        }
        
        return attributes.toArray(new AttributeValue[0]);
    }
}
