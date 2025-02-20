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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class RequestedAttributePathVisitor implements Visitor<List<Attribute>, Attribute> {
  
  private String[][] filter;
  
  private int attributeNumber;
  private int index;

  final List<Attribute> filteredAttribute;
  
    
  public RequestedAttributePathVisitor(final Set<String> requestedAttributes) throws ScimException {
    this.filteredAttribute = new ArrayList<>();
    this.filter            = new String[requestedAttributes.size()][];
    this.attributeNumber   = 0;

    final Set<String> schemaURIs = OIMSchema.getInstance().getAllSchemaURIs();
    int i = 0;

    for (String attribute : requestedAttributes) {
        List<String> filterBuilding = new ArrayList<>();
        this.index = 0;

        for (String uri : schemaURIs) {
            if (attribute.startsWith(uri)) {
                filterBuilding.add(uri);
                attribute = attribute.length() > uri.length() ? attribute.substring(uri.length() + 1) : "";
                break;
            }
        }

        if (!attribute.isEmpty()) {
            String[] attributes = attribute.split("\\.");
            for (String attr : attributes) {
                if (!attr.isEmpty()) {
                    filterBuilding.add(attr);
                }
            }
        }

        this.filter[i++] = filterBuilding.toArray(new String[0]);
    }
  }
    
  public RequestedAttributePathVisitor(final Set<String> requestedAttributes, boolean test)
    throws ScimException {
    filteredAttribute = new ArrayList<Attribute>();
    this.filter = new String[requestedAttributes.size()][];
    this.attributeNumber = 0;
    int i = 0;
    
    for (String attribute : requestedAttributes) {
      ArrayList<String> filterBuilding = new ArrayList<String>();
      this.index = 0;
      
       
      
      //int lastColonIndex = attribute.lastIndexOf(":");
      String[] attributes = null;
      // Remove last colon
      //if (lastColonIndex != -1) {
      //  attribute = attribute.substring(lastColonIndex);
      //}
      
      attributes = attribute.split("\\.");
      
      for (int j = 0; j < attributes.length; j++) {
        if (attributes[j] != null && attributes[j].length() > 0)
          filterBuilding.add(attributes[j]);
      }
      
      this.filter[i] = filterBuilding.toArray(new String[0]);
      i++;
    }
  }
  
  @Override
  public List<Attribute> visit(Resource resource) {
    attributeNumber = 0;
    index = 0;
     while (attributeNumber < filter.length) {
       final Iterator<Attribute> attributes = resource.iterator();
       while (attributes.hasNext()) {
         final Attribute attribute = attributes.next();
         Attribute result = attribute.accept(this);
         if (result != null) {
           this.filteredAttribute.add(result.clone());
           break;
         }
         index = 0;
       }
       attributeNumber++;

     }
     System.out.println(this.filteredAttribute);
     return new ArrayList<Attribute>(this.filteredAttribute);
  }
  
  @Override
  public Attribute visit(Attribute resource) {
    // Search not found
    if (index >= this.filter[attributeNumber].length ) {
      return null;
    }
    
    // Search found
    if (index == this.filter[attributeNumber].length - 1 && resource.getName().equals(this.filter[attributeNumber][index])) {
      System.out.println("Found: " + resource.toString());
      return resource.clone();
    }
    
    // Not completed search
    if (index < this.filter[attributeNumber].length && resource.getName().equals(this.filter[attributeNumber][index])) {
      index++;
      if (resource.isMultiValue() && resource.isComplex()) {
        return visit((MultiValueComplexAttribute) resource);
      }
      else if (resource.isMultiValue()) {
        return visit((MultiValueSimpleAttribute) resource);
      }
      else if (resource.isComplex()) {
        return visit((SingularComplexAttribute) resource);
      }
      else {
         return visit((SingularSimpleAttribute) resource);
      }
    }
    return null;
  }
  

  public Attribute visit(SingularSimpleAttribute attribute) {
    if (index == this.filter[attributeNumber].length - 1 && attribute.getName().equals(this.filter[attributeNumber][index])) {
      this.filteredAttribute.add(new SingularSimpleAttribute(attribute.getName(), attribute.getValue()));
    }
    return null;
  }

  public Attribute visit(SingularComplexAttribute attribute) {
    AttributeValue[] values = attribute.getValues();
      for (AttributeValue value : values) {
        Attribute[] subAttribute = value.getSubAttributes();
        for (Attribute subValue : subAttribute) {
          Attribute result = subValue.accept(this);
          if (result != null) {
            this.filteredAttribute.add(new SingularComplexAttribute(attribute.getName(), new AttributeValue(result.clone())));
          }
        }
      }
    return null;
  }

  public Attribute visit(MultiValueComplexAttribute attribute) {
    AttributeValue[] values = attribute.getValues();
    LinkedList<AttributeValue> attributeValue = new LinkedList<AttributeValue>();
    for (AttributeValue value : values) {
      Attribute[] listAttribute = value.getSubAttributes();
      for (Attribute subValue : listAttribute) {
        Attribute result = subValue.accept(this);
        if (result != null) {
          attributeValue.add(new AttributeValue(result.clone()));
        }
      }
    }
    if (attributeValue.isEmpty())
     return null;
    return new MultiValueComplexAttribute(attribute.getName(), attributeValue.toArray(new AttributeValue[0]));
  }

  public Attribute visit(MultiValueSimpleAttribute attribute) {
    // Return null as value is not an attribute
    return null;
  }
  
  public List<Attribute> getFiltedResource() {
    return filteredAttribute;
  }
  
  @Override
  public <R extends Resource>  ListResponse<R> visit(ListResponse<R> listResource)
    throws ScimException {
    
    final List<R> resources = new ArrayList<R>();
    try {
      for (R resource : listResource.getResources()) {
        final List<Attribute> attributes = visit(resource);
        final R visitedResource;
        
          visitedResource = (R)resource.getClass()
                                       .getDeclaredConstructor(ResourceDescriptor.class, List.class)
                                       .newInstance(new ResourceDescriptor(), attributes);
          resources.add(visitedResource);
      }
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
    
    return new ListResponse<R>(resources, listResource.getTotalResult(), listResource.getStartIndex(), listResource.getItemsPerPage());
  }
}
