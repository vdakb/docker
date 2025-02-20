package bka.iam.identity.scim.extension.resource;

import bka.iam.identity.scim.extension.exception.ScimException;
import bka.iam.identity.scim.extension.model.Attribute;
import bka.iam.identity.scim.extension.model.AttributeValue;
import bka.iam.identity.scim.extension.model.MultiValueComplexAttribute;
import bka.iam.identity.scim.extension.model.Resource;
import bka.iam.identity.scim.extension.model.ResourceDescriptor;
import bka.iam.identity.scim.extension.model.ScimResource;
import bka.iam.identity.scim.extension.model.SingularSimpleAttribute;
import bka.iam.identity.scim.extension.parser.Marshaller;
import bka.iam.identity.scim.extension.rest.HTTPContext;
import bka.iam.identity.scim.extension.rest.OIMScimContext;
import bka.iam.identity.scim.extension.utils.WLSUtil;

import java.util.LinkedList;
import java.util.List;

public class Group extends ScimResource {
  
  public static final String[] SCHEMAS = new String[] {
    "urn:ietf:params:scim:schemas:core:2.0:Group"
  };

  public Group(final ResourceDescriptor descriptor) {
    super(descriptor);
  }
  
  public Group(final ResourceDescriptor descriptor, List<Attribute> attributes) {
    super(descriptor, attributes);
  }
  
  public String[] getSchemaURIs() {
    return SCHEMAS;
  }
  
  public Attribute getMembers() {
    System.out.println("Member: " + get("members"));
    return get("members");
  }
  
  public List<String> getMembersAsListString()
    throws ScimException {
    List<String> members = new LinkedList<String>();
    
    Attribute membersAttribute = getMembers();
    
    for (AttributeValue attributes : membersAttribute.getValues()) {
      for (Attribute attribute : attributes.getSubAttributes()) {
        if (attribute.getName().equals("value")) {
          members.add(attribute.getValue().getStringValue());
        }
      }
    }
    
    return members;
  }
  
  public void addMember(final String userId)
    throws ScimException {
    
    if (isMember(userId)) {
      throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, ScimException.ScimType.INVALID_VALUE, "User: " + userId + "is already member of: " + getId());  
    }
    
    LinkedList<Attribute> attribute = new LinkedList<>();
    
    attribute.add(new SingularSimpleAttribute("value", new AttributeValue(userId)));
    attribute.add(new SingularSimpleAttribute("$ref", new AttributeValue(WLSUtil.getOIMUrl() + OIMScimContext.OIM_EXTENTION_ENDPOINT_SCIM + "/" + OIMScimContext.ENDPOINT_USERS  + "/" + userId)));

    final Attribute members = getMembers();
    
    if (members == null) {
      MultiValueComplexAttribute membersAttribute = new MultiValueComplexAttribute("members", new AttributeValue[0]);
      membersAttribute.addValue(new AttributeValue(attribute.toArray(new Attribute[0])));
      this.attributes.add(membersAttribute);
    }
    else {
      members.addValue(new AttributeValue(attribute.toArray(new Attribute[0])));
    }
  }
  
  public void removeMember(final String userId)
    throws ScimException {
    List<AttributeValue> newMembers = new LinkedList<AttributeValue>();
    
    Attribute members = getMembers();
    
    if (members == null) {
      throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, ScimException.ScimType.INVALID_VALUE, "Cannot remove user: " + userId + "as is not member of the group: " + getId());  
    }
    
    for (AttributeValue attributes : members.getValues()) {
      for (Attribute attribute : attributes.getSubAttributes()) {
        if (attribute.getName().equals("value") && !attribute.getValue().getStringValue().equalsIgnoreCase(userId)) {
          newMembers.add(attributes);
        }
      }
    }
    if (newMembers.size() == members.getValues().length) {
      throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, ScimException.ScimType.INVALID_VALUE, "Cannot remove user: " + userId + "as is not member of the group: " + getId());  
    }
    
    this.attributes.remove(members);
    this.attributes.add(new MultiValueComplexAttribute("members", newMembers.toArray(new AttributeValue[0])));
  }
  
  public boolean isMember(final String userId) {
    final Attribute members = getMembers();
    
    if (members == null) {
      return false;
    }
    
    for (AttributeValue attributes : members.getValues()) {
      for (Attribute attribute : attributes.getSubAttributes()) {
        if (attribute.getName().equals("value") && attribute.getValue().getStringValue().equalsIgnoreCase(userId)) {
          return true;
        }
      }
    }
    
    return false;
  }
  
  public void setDisplayName(final String displayName) {
    set("displayName", displayName);
  }
  
  public static void main(String[] args)
    throws ScimException {
    Group group = new Group(null);
    group.setId("123");
    group.setDisplayName("Group Display Name");
    group.addMember("BK058550");
    group.addMember("BK99999");
    System.out.println(Marshaller.resourceToJsonNode(group, null, null));
    System.out.println("Does BK058550 is member:" + group.isMember("BK058550"));
    System.out.println("Now remove");
    group.removeMember("BK99999");
    System.out.println(Marshaller.resourceToJsonNode(group, null, null));
    System.out.println("Does BK99999 is member:" + group.isMember("BK99999"));
    System.out.println("Does BK058550 is member:" + group.isMember("BK058550"));
    group.removeMember("BK11111");
  }

  @Override
  public Resource clone() {
    return new Group(new ResourceDescriptor(this.getResourceDescriptor().getSchema()), this.attributes);
  }
}
