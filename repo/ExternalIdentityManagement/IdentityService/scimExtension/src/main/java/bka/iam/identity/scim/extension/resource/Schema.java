package bka.iam.identity.scim.extension.resource;

import bka.iam.identity.scim.extension.model.Attribute;
import bka.iam.identity.scim.extension.model.Resource;
import bka.iam.identity.scim.extension.model.ResourceDescriptor;
import bka.iam.identity.scim.extension.model.ScimResource;

import java.util.List;

public class Schema extends ScimResource {
  
  public final static String NAME = "name";

  public static final String[] SCHEMAS = new String[] {
    "urn:ietf:params:scim:schemas:core:2.0:Schema"
  };
  
  public String getSchemaType() {
    return get(NAME).getValue().getStringValue();
  }

  public Schema(final ResourceDescriptor descriptor) {
    super(descriptor);
  }
  
  public Schema(final ResourceDescriptor descriptor, List<Attribute> attributes) {
    super(descriptor, attributes);
  }
  
  public String[] getSchemaURIs() {
    return SCHEMAS;
  }

  @Override
  public Resource clone() {
    return new Schema(new ResourceDescriptor(this.getResourceDescriptor().getSchema()), this.attributes);
  }
}
