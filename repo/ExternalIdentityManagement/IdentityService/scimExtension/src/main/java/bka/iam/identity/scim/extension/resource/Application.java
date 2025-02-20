package bka.iam.identity.scim.extension.resource;

import bka.iam.identity.scim.extension.model.Attribute;
import bka.iam.identity.scim.extension.model.Resource;
import bka.iam.identity.scim.extension.model.ResourceDescriptor;
import bka.iam.identity.scim.extension.model.ScimResource;

import java.util.List;

public final class Application extends ScimResource {
  
  public static final String[] SCHEMAS = new String[] {
    "urn:ietf:params:scim:schemas:extension:oracle:2.0:OIG:Application"
  };
  
  
  public Application(final ResourceDescriptor descriptor) {
    super(descriptor);
  }
  
  public Application(final ResourceDescriptor descriptor, List<Attribute> attributes) {
    super(descriptor, attributes);
  }
  
  public String getApplicationName() {   
    return getAttributeValue("applicationName");
  }
  
  public Attribute getNamespaces() {
    return get("namespaces");
  }
  
  public String[] getSchemaURIs() {
    return SCHEMAS;
  }
  @Override
  public Resource clone() {
    return new Application(new ResourceDescriptor(this.getResourceDescriptor().getSchema()), this.attributes);
  }
}
