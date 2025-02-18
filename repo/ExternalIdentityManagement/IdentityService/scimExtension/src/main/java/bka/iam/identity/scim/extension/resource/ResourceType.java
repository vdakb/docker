package bka.iam.identity.scim.extension.resource;

import bka.iam.identity.scim.extension.model.Attribute;
import bka.iam.identity.scim.extension.model.AttributeValue;
import bka.iam.identity.scim.extension.model.Resource;
import bka.iam.identity.scim.extension.model.ResourceDescriptor;
import bka.iam.identity.scim.extension.model.ScimResource;
import bka.iam.identity.scim.extension.model.SingularSimpleAttribute;

import java.net.URI;

import java.util.ArrayList;
import java.util.List;

public final class ResourceType extends ScimResource {
  
  public static final String[] SCHEMAS = new String[] {
    "urn:ietf:params:scim:schemas:core:2.0:ResourceType"
  };
  
  
  public ResourceType(final ResourceDescriptor descriptor) {
    super(descriptor);
  }
  
  public ResourceType(final ResourceDescriptor descriptor, List<Attribute> attributes) {
    super(descriptor, attributes);
  }
  
  public String getName() {   
    return getAttributeValue("name");
  }
  
  public Attribute getSchemaExtensions() {
    return get("schemaExtensions");
  }
  
  public String getDescription() {
    return getAttributeValue("schemaExtensions");
  }
  
  public String getEndpoint() {
    return getAttributeValue("endpoint");
  }
  
  public void addSchemaExtensions(final URI schemaURI, final Boolean required) {   
    final List<Attribute> extensionAttr = new ArrayList<>();
    extensionAttr.add(new SingularSimpleAttribute("schema", new AttributeValue(schemaURI.toString())));
    extensionAttr.add(new SingularSimpleAttribute("required", new AttributeValue(required)));
    getSchemaExtensions().addValue(new AttributeValue(extensionAttr.toArray(new Attribute[0])));
  }
  
  public String[] getSchemaURIs() {
    return SCHEMAS;
  }

  @Override
  public Resource clone() {
    return new ResourceType(new ResourceDescriptor(this.getResourceDescriptor().getSchema()), this.attributes);
  }
}
