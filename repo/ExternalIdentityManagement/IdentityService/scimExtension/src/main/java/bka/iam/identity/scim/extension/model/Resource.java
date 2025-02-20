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

    File        :   Resource.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@silverid.fr

    Purpose     :   This file implements the abstract class
                    Resource.

    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-12-11  SBernet     First release version
*/
package bka.iam.identity.scim.extension.model;

import bka.iam.identity.scim.extension.option.AttributeVisitor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
////////////////////////////////////////////////////////////////////////////////
// abstract class Resource
// ~~~~~~~~ ~~~~~ ~~~~~~~~
/**
 ** The Resource class represents a SCIM resource that contains a collection of
 ** attributes. This class serves as a base for other SCIM resources, such as
 ** User, Group, etc. It provides methods for managing attributes, retrieving
 ** their values, and iterating through them.
 **
 ** @author  sylvert.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class Resource implements Iterable<Attribute> {
  
  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** A list that holds all the attributes of the resource. */
  protected List<Attribute> attributes = new LinkedList<>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Initializes a Resource with an empty list of attributes.
   */
  public Resource() {
    super();
    this.attributes = new LinkedList<>();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Initializes a Resource with the given list of attributes.
   ** 
   ** @param attributes    The list of attributes to initialize with.
   **                      Allowed object is {@link List}.
   */
  public Resource(List<Attribute> attributes) {
    super();
    this.attributes = new ArrayList<>(attributes);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   size
  /**
   ** Returns the number of attributes in this resource.
   ** 
   ** @return        The number of attributes.
   **                Possible object is {@link Integer}.
   */
  public final int size() {
    return attributes.size();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   get
  /**
   ** Retrieves an attribute by its name.
   **
   ** @param <T>             The type of the attribute being returned, extending
   **                        {@link Attribute}.
   ** @param name            The name of the attribute to retrieve.
   **                        Allowed object is {@link String}.
   **
   ** @return                The attribute with the specified name, or null if
   **                        it does not exist.
   **                        Possible object is {@link Attribute}.
   */
  public final <T extends Attribute> T get(final String name) {
    
    for (Attribute attribute : attributes) {
      if (attribute.getName().equals(name)) {
        return (T) attribute;
      }
    }
    return null;
  }
  
  public final Attribute getAttribute(final String path) {
    final List<String> attributeName = new ArrayList<>();
    attributeName.add(path);
    
    final AttributeVisitor visitor = new AttributeVisitor(attributeName);
    List<Attribute> selectedAttribute = visitor.visit(this);
    if (selectedAttribute.size() > 0) {
      Attribute attribute = selectedAttribute.get(0);
      return attribute;
    }
    return null;
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAttributeValue
  /**
   ** Retrieves the string value of an attribute by its name.
   ** 
   ** @param name The name of the attribute to retrieve.
   **             Allowed object is {@link String}.
   ** 
   ** @return     The string value of the attribute, or an empty string if not found.
   **             Possible object is {@link String}.
   */
  /*public final String getAttributeValue(final String name) {
    final Attribute attribute = get(name);
    if (attribute != null) {
      return attribute.getValue().getStringValue();
    }
    return "";
  }*/
  
  public final String getAttributeValue(final String path) {
    final List<String> attributeName = new ArrayList<>();
    attributeName.add(path);
    
    final AttributeVisitor visitor = new AttributeVisitor(attributeName);
    List<Attribute> selectedAttribute = visitor.visit(this);
    if (selectedAttribute.size() > 0) {
      Attribute attribute = selectedAttribute.get(0);
      if (attribute.getValue() != null)
        return (String) attribute.getValue().getValue();
      return attribute.getValues().toString();
    }
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   set
  /**
   ** Sets an attribute with the provided value by its name.
   **
   ** @param key   The name of the attribute to set.
   **              Allowed object is {@link String}.
   ** @param value The value to set for the attribute.
   **              Allowed object is {@link Object}.
   */
  public final void set(final String key, final Object value) {
    for (Attribute attribute : attributes) {
      if (attribute.getName().equals(key)) {
        attribute.setValue(new AttributeValue(value));
        return;
      }
    }
    attributes.add(new SingularSimpleAttribute(key, new AttributeValue(value)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Adds an attribute to the resource and returns it.
   **
   ** @param <T>             The type of the attribute being returned, extending
   **                        {@link Attribute}.
   ** @param attribute       The attribute to be added to the resource.
   **                        Allowed object is {@link Attribute}.
   **
   ** @return                The attribute that was added.
   **                        Possible object is {@link Attribute}.
   */
  public <T extends Attribute> T add(T attribute) {
    this.attributes.add(attribute);
    return get(attribute.getName());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   iterator
  /**
   ** Returns an iterator over the attributes of the resource.
   ** This method is part of the {@link Iterable} interface and allows the
   ** resource to be iterated over using a for-each loop.
   ** 
   ** @return        An iterator over the attributes of the resource.
   **                Possible object is {@link Iterator}.
   */
  @Override
  public Iterator<Attribute> iterator() {
    return this.attributes.iterator();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Abstract Methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getResourceDescriptor
  /**
   ** Abstract method to retrieve the resource descriptor.
   ** 
   ** @return        The resource descriptor for the resource.
   **                Possible object is {@link ResourceDescriptor}.
   */
  public abstract ResourceDescriptor getResourceDescriptor();
  
  public abstract Resource clone();
}
