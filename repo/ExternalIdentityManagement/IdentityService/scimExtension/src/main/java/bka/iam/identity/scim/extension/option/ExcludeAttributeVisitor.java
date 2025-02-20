package bka.iam.identity.scim.extension.option;

import bka.iam.identity.scim.extension.exception.ScimException;
import bka.iam.identity.scim.extension.model.Attribute;
import bka.iam.identity.scim.extension.model.AttributeValue;
import bka.iam.identity.scim.extension.model.ListResponse;
import bka.iam.identity.scim.extension.model.MultiValueComplexAttribute;
import bka.iam.identity.scim.extension.model.MultiValueSimpleAttribute;
import bka.iam.identity.scim.extension.model.Resource;
import bka.iam.identity.scim.extension.model.ResourceDescriptor;
import bka.iam.identity.scim.extension.model.SingularComplexAttribute;
import bka.iam.identity.scim.extension.model.SingularSimpleAttribute;
import bka.iam.identity.scim.extension.rest.HTTPContext;
import bka.iam.identity.scim.extension.rest.OIMSchema;

import java.lang.reflect.InvocationTargetException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ExcludeAttributeVisitor implements Visitor<List<Attribute>, Attribute> {
  
  private String[][] filter;
  
  private int attributeNumber;
  
  private int index;

  public ExcludeAttributeVisitor(final Set<String> requestedAttributes)
  throws ScimException {
    this.filter            = new String[requestedAttributes.size()][];
    this.attributeNumber   = 0;

    final Set<String> schemaURIs = OIMSchema.getInstance().getAllSchemaURIs();
    int i = 0;

    for (String attribute : requestedAttributes) {
      final List<String> filterBuilding = new ArrayList<>();
      this.index = 0;
      
      for (String uri : schemaURIs) {
        if (attribute.startsWith(uri)) {
          filterBuilding.add(uri);
          attribute = attribute.length() > uri.length() ? attribute.substring(uri.length() + 1) : "";
          break;
        }
      }
        
      if (!attribute.isEmpty()) {
        final String[] attributes = attribute.split("\\.");
        for (String attr : attributes) {
          if (!attr.isEmpty()) {
            filterBuilding.add(attr);
          }
        }
      }

      this.filter[i++] = filterBuilding.toArray(new String[0]);
    }
  }
  
  @Override
  public List<Attribute> visit(Resource resource) {
    final List<Attribute> excludedAttributes = new LinkedList<>();
    while (attributeNumber < filter.length) {
      for (Attribute attribute : resource) {
        final Attribute result = attribute.accept(this);
        if (result != null) {
          excludedAttributes.add(result);
        }
        index = 0;
      }
      attributeNumber++;
    }
    return excludedAttributes;
  }
  
  @Override
  public Attribute visit(Attribute attribute) {
    // Search not found
    if (index >= this.filter[attributeNumber].length )
      return null;
    
    // If the attribute matches the filter, exclude it
    if (index == this.filter[attributeNumber].length - 1 && attribute.getName().equals(this.filter[attributeNumber][index]))
      return null;
    
    // If the attribute doesn't match the filter, include it
    if (index < this.filter[attributeNumber].length && !attribute.getName().equals(this.filter[attributeNumber][index]))
      return attribute.clone();
    
    // If partially matching, delegate to appropriate method
    index++;
      
    if (attribute.isMultiValue() && attribute.isComplex()) {
      return visit((MultiValueComplexAttribute) attribute);
    }
    else if (attribute.isMultiValue()) {
      return visit((MultiValueSimpleAttribute) attribute);
    }
    else if (attribute.isComplex()) {
      return visit((SingularComplexAttribute) attribute);
    }

    return null;
  }

  public Attribute visit(SingularSimpleAttribute attribute) {
    if (index == this.filter[attributeNumber].length - 1 && attribute.getName().equals(this.filter[attributeNumber][index]))
      return null;

    return new SingularSimpleAttribute(attribute.getName(), attribute.getValue());
  }

  public Attribute visit(SingularComplexAttribute attribute) {
    final List<Attribute> subAttributes = new ArrayList<Attribute>();
    for (AttributeValue value : attribute.getValues()) {
      final Attribute[] subAttribute = value.getSubAttributes();
      for (Attribute subValue : subAttribute) {
        final Attribute result = subValue.accept(this);
        if (result != null)
          subAttributes.add(result);
      }
    }
    
    return subAttributes.isEmpty() ? null : new SingularComplexAttribute(attribute.getName(), new AttributeValue(subAttributes));
  }

  public Attribute visit(MultiValueComplexAttribute attribute) {
    final List<AttributeValue> attributeValue = new LinkedList<>();
    
    for (AttributeValue value : attribute.getValues()) {
      for (Attribute subAttribute : value.getSubAttributes()) {
        Attribute result = subAttribute.accept(this);
        if (result != null) {
          attributeValue.add(new AttributeValue(result));
        }
      }
    }
    
    return attributeValue.isEmpty() ? null : new MultiValueComplexAttribute(attribute.getName(), attributeValue.toArray(new AttributeValue[0]));
  }

  public Attribute visit(MultiValueSimpleAttribute attribute) {
    return new MultiValueSimpleAttribute(attribute.getName(), attribute.getValues());
  }

  @Override
  public <R extends Resource>  ListResponse<R> visit(ListResponse<R> listResource)
    throws ScimException {
    
    List<R> filteredResources  = new ArrayList<R>();
    for (R resource : listResource.getResources()) {
      List<Attribute> attributes = visit(resource);
      R filteredResource;
      try {
        filteredResource = (R)resource.getClass()
                                      .getDeclaredConstructor(ResourceDescriptor.class, List.class)
                                      .newInstance(new ResourceDescriptor(), attributes);
      }
      catch (NoSuchMethodException e) {
        throw new ScimException(HTTPContext.StatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
      }
      catch (InstantiationException e) {
        throw new ScimException(HTTPContext.StatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
      }
      catch (IllegalAccessException e) {
        throw new ScimException(HTTPContext.StatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
      }
      catch (InvocationTargetException e) {
        throw new ScimException(HTTPContext.StatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
      }
      filteredResources.add(filteredResource);
    }
    
    return new ListResponse<R>(filteredResources , listResource.getTotalResult(), listResource.getStartIndex(), listResource.getItemsPerPage());
  }
}
