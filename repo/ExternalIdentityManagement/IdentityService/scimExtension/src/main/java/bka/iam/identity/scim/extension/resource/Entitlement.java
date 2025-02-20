package bka.iam.identity.scim.extension.resource;

import bka.iam.identity.scim.extension.model.Attribute;
import bka.iam.identity.scim.extension.model.Resource;
import bka.iam.identity.scim.extension.model.ResourceDescriptor;
import bka.iam.identity.scim.extension.model.ScimResource;

import java.util.List;

public final class Entitlement extends ScimResource {
  
  public static final String[] SCHEMAS = new String[] {
    "urn:ietf:params:scim:schemas:extension:oracle:2.0:OIG:Entitlement"
  };
  
  
  public Entitlement(final ResourceDescriptor descriptor) {
    super(descriptor);
  }
  
  public Entitlement(final ResourceDescriptor descriptor, List<Attribute> attributes) {
    super(descriptor, attributes);
  }
  
  public String getDisplayName() {   
    return getAttributeValue("displayName");
  }
  
  public Attribute getAttributeValues() {
    return get("attributeValues");
  }
  
  public String getNameSpace() {
    return getAttributeValue("namespace");
  }
  
  public String getApplication() {
    return getAttributeValue("application");
  }
  
  public String[] getSchemaURIs() {
    return SCHEMAS;
  }
  @Override
  public Resource clone() {
    return new Entitlement(new ResourceDescriptor(this.getResourceDescriptor().getSchema()), this.attributes);
  }
}
