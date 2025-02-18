package bka.iam.identity.scim.extension.resource;

import bka.iam.identity.scim.extension.model.Attribute;
import bka.iam.identity.scim.extension.model.Resource;
import bka.iam.identity.scim.extension.model.ResourceDescriptor;
import bka.iam.identity.scim.extension.model.ScimResource;

import java.util.List;

public final class ApplicationAttribute extends ScimResource {
  
  public static final String[] SCHEMAS = new String[] {
    "urn:ietf:params:scim:schemas:extension:oracle:2.0:OIG:ApplicationAttributes"
  };
  
  
  public ApplicationAttribute(final ResourceDescriptor descriptor) {
    super(descriptor);
  }
  
  public ApplicationAttribute(final ResourceDescriptor descriptor, List<Attribute> attributes) {
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
  

  
  public String[] getSchemaURIs() {
    return SCHEMAS;
  }

  @Override
  public Resource clone() {
    return new ApplicationAttribute(new ResourceDescriptor(this.getResourceDescriptor().getSchema()), this.attributes);
  }
}
