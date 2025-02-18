/*
    Oracle Deutschland BV & Co. KG

    This software is the confidential and proprietary information of
    Oracle Corporation. ("Confidential Information").  You shall not
    disclose such Confidential Information and shall use it only in
    accordance with the terms of the license agreement you entered
    into with Oracle.

    ORACLE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
    SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
    IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
    PURPOSE, OR NON-INFRINGEMENT. ORACLE SHALL NOT BE LIABLE FOR ANY DAMAGES
    SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
    THIS SOFTWARE OR ITS DERIVATIVES.

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager
    Subsystem   :   Custom SCIM Service

    File        :   GetSimpleAttribute.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@silverid.fr

    Purpose     :   This file implements the class
                    GetSimpleAttribute.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-12-11  SBernet     First release version
*/
package bka.iam.identity.scim.extension.option;

import bka.iam.identity.scim.extension.exception.ScimException;
import bka.iam.identity.scim.extension.model.Attribute;
import bka.iam.identity.scim.extension.model.AttributeValue;
import bka.iam.identity.scim.extension.model.ListResponse;
import bka.iam.identity.scim.extension.model.Resource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
////////////////////////////////////////////////////////////////////////////////
// class GetSimpleAttribute
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** GetSimpleAttribute class is responsible for visiting and filtering
 ** attributes from a SCIM resource based on the provided list of attribute
 ** names. It collects attributes that match the given names and are non-null.
 ** 
 ** This class implements the Visitor pattern and works by iterating through the
 ** attributes of a SCIM resource, checking each attribute to see if it matches
 ** the requested names. It also handles complex attributes, recursively
 ** visiting nested sub-attributes if necessary.
 **
 ** @author  sylvert.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class AttributeVisitor implements Visitor<List<Attribute>, Attribute> {
  
  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////
  
  // This list holds the attribute names that we want to filter the resource by.
  private final List<String>    attributeName;
  
  // Keep track of which attribute we're currently processing in attributeName.
  private int index;
  
  // List of attributes that match the given attribute names.
  private final List<Attribute> attributes;
  
  // Constructing attribute paths during subcalling.
  private StringBuilder builder;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Initializes the GetSimpleAttribute instance with a list of attribute names.
   **
   ** @param  attributeName      A list of attribute names to filter the
   **                            resource by.
   **                            <br>
   **                            Allowed object is {@link List}.
   */
  public AttributeVisitor(final List<String> attributeName) {
    this.attributeName = attributeName;
    this.attributes = new ArrayList<Attribute>();
    this.index = 0;
    this.builder = new StringBuilder();
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Accessor
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAttributes
  /**
   ** Returns the {@link List} of attributes that match the provided names.
   **
   ** @return                    the {@link List} of attributes that match the
   **                            provided names.
   **                            <br>
   **                            Possible object is {@link List}.
   */
  public List<Attribute> getAttributes() {
    return attributes;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   visit
  /**
   * Visits a SCIM resource and filters its attributes based on the given
   * attribute names.
   *
   * @param resource The SCIM resource to be processed.
   * <br>
   * Allowed object is {@link Resource}.
   *
   */
  @Override
  public List<Attribute> visit(Resource resource) {
    // In case 
    for (String attribute : attributeName) {
      if (!attribute.contains(":")) {
        this.attributeName.remove(attribute);
        StringBuilder newAttribute = new StringBuilder(resource.getResourceDescriptor().getCoreSchema().getURI()).append(":").append(attribute);
        this.attributeName.add(newAttribute.toString());
      }
    }
    for (; index < attributeName.size(); index++) {
       final Iterator<Attribute> attributes = resource.iterator();
       while (attributes.hasNext()) {
         final Attribute attribute = attributes.next();
         if (attribute.getName().contains(":"))
           this.builder = new StringBuilder(attribute.getName()).append(":");
         else
           this.builder = new StringBuilder(resource.getResourceDescriptor().getCoreSchema().getURI()).append(":").append(attribute.getName()).append(".");        
         attribute.accept(this);
       }
     }
     return attributes;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   visit
  /**
   * Visits an individual attribute, checking if its name matches the current
   * attribute name and adding it to the list. If the attribute
   * is complex, it recursively visits the sub-attributes.
   *
   * @param attribute The attribute to be processed.
   * <br>
   * Allowed object is {@link Attribute}.
   *
   */
  @Override
  public Attribute visit(Attribute attribute) {
    if (this.builder.toString().substring(0, this.builder.toString().length() - 1).equals(this.attributeName.get(index)) && attribute.getValue() != null ) {
      this.attributes.add(attribute);
    }
    else if (attribute.isComplex()) {
      AttributeValue[] values = attribute.getValues();
      for (AttributeValue value : values) {
        Attribute[] subAttribute = value.getSubAttributes();
        String tmpBuilder = this.builder.toString();
        for (Attribute subValue : subAttribute) {
          this.builder.append(subValue.getName()).append(".");
          subValue.accept(this);
          this.builder = new StringBuilder(tmpBuilder);
        }
      }
    }
    return null;
  }

  @Override
  public <R extends Resource> ListResponse<R> visit(ListResponse<R> listResource)
    throws ScimException {
    return null;
  }
}
