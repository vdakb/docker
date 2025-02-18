package bka.iam.identity.scim.extension.resource;

import bka.iam.identity.scim.extension.model.Attribute;
import bka.iam.identity.scim.extension.model.MultiValueComplexAttribute;
import bka.iam.identity.scim.extension.model.Resource;
import bka.iam.identity.scim.extension.model.ResourceDescriptor;
import bka.iam.identity.scim.extension.model.ScimResource;

import java.util.List;

public class Policy extends ScimResource {
  
  public static final String[] SCHEMAS = new String[] {
    "urn:ietf:params:scim:schemas:extension:oracle:2.0:OIG:Policy"
  };

  
  public Policy(final ResourceDescriptor descriptor) {
    super(descriptor);
  }
  
  public Policy(final ResourceDescriptor descriptor, List<Attribute> attributes) {
    super(descriptor, attributes);
  }
  
  
  public String getOwner() {
    return getAttributeValue("owner");
  }


  public String getOwnerType() {
    return getAttributeValue("ownerType");
  }


  public String getType() {
    return getAttributeValue("type");
  }


  public String getDescription() {
    return getAttributeValue("description");
  }


  public int getPriority() {
    return (get("priority") != null ? get("priority").getValue().getIntegerValue():0);
  }
  
  
  public MultiValueComplexAttribute getApplications() {
    return get("applications");
  }
  
  @Override
  public String[] getSchemaURIs() {
    return SCHEMAS;
  }
  @Override
  public Resource clone() {
    return new Policy(new ResourceDescriptor(this.getResourceDescriptor().getSchema()), this.attributes);
  }
}
