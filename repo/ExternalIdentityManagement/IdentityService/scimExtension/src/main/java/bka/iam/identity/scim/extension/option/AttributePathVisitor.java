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
import bka.iam.identity.scim.extension.model.MultiValueComplexAttribute;
import bka.iam.identity.scim.extension.model.MultiValueSimpleAttribute;
import bka.iam.identity.scim.extension.model.Resource;
import bka.iam.identity.scim.extension.model.SingularComplexAttribute;
import bka.iam.identity.scim.extension.model.SingularSimpleAttribute;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
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
public class AttributePathVisitor implements Visitor<Set<String>, String> {
  
  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////
  
  // This list holds the attribute names that we want to filter the resource by.
  private final LinkedHashSet<String>    attributeName;
  
  // Keep track of which attribute we're currently processing in attributeName.
  private int index;
  
  // Constructing attribute paths during subcalling.
  private StringBuilder builder;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Initializes the `AttributePathVisitor` instance with default values.
   */
  public AttributePathVisitor() {
    this.attributeName = new LinkedHashSet<String>();
    this.index = 0;
    this.builder = new StringBuilder();
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Accessor
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getVisitedAttributesList
  /**
   ** Retrieves the list of visited attribute paths.
   **
   ** @return                     A <code>LinkedHashSet</code> containing the
   **                             visited attribute paths.
   **                             Possible object is {@link LinkedHashSet}.
   */
  public LinkedHashSet<String> getVisitedAttributesList() {
    return attributeName;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract class
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   visit
  /**
   ** Visits a <code>Resource</code> and processes all its attributes.
   **
   ** @param resource             The <code>Resource</code> to visit.
   **                             Allowed object is {@link Resource}.
   **
   ** @return                     A <code>Set</code> containing paths of visited
   **                             attributes.
   **                             Possible object is {@link Set}.
   */
  @Override
  public Set<String> visit(Resource resource) {
    final Iterator<Attribute> attributes = resource.iterator();
    while (attributes.hasNext()) {
      final Attribute attribute = attributes.next();
      this.builder = new StringBuilder();
      attribute.accept(this);
    }
    return attributeName;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   visit
  /**
   ** Visits an <code>Attribute</code> and delegates processing based on its
   ** type.
   **
   ** @param resource             The <code>Attribute</code> to visit.
   **                             Allowed object is {@link Attribute}.
   **
   ** @return                     Null as it is not neccessary to use on this
   **                             case.
   */
  @Override
  public String visit(Attribute resource) {
    if (resource.isMultiValue() && resource.isComplex()) {
      visitMultiValueComplex((MultiValueComplexAttribute) resource);
    }
    else if (resource.isMultiValue()) {
      visitMultiValueSimple((MultiValueSimpleAttribute) resource);
    }
    else if (resource.isComplex()) {
      visitSingularComplex((SingularComplexAttribute) resource);
    }
    else {
       visitSingularSimple((SingularSimpleAttribute) resource);
    }
    
    return null;
  }
  
  @Override
  public <R extends Resource>  ListResponse<R> visit(ListResponse<R> listResource)
    throws ScimException {
    return null;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Helper Methods
  //////////////////////////////////////////////////////////////////////////////

  public void visitSingularSimple(SingularSimpleAttribute attribute) {
    StringBuilder temporaryBuilder = new StringBuilder(this.builder);
    addAttributeToBuilder(attribute, true);
    this.attributeName.add(this.builder.toString());
    this.builder = new StringBuilder(temporaryBuilder);
  }

  public void visitSingularComplex(SingularComplexAttribute attribute) {
    addAttributeToBuilder(attribute, false);
    for (AttributeValue values : attribute.getValues()) {
      Attribute[] subAttribute = values.getSubAttributes();
        for (Attribute subValue : subAttribute) {
          StringBuilder temporaryBuilder = new StringBuilder(this.builder);
          subValue.accept(this);
          this.builder = new StringBuilder(temporaryBuilder);
        }
    }
  }

  public void visitMultiValueComplex(MultiValueComplexAttribute attribute) {
    final AttributeValue[] values = attribute.getValues();
    addAttributeToBuilder(attribute, false);
    for (AttributeValue value : values) {
      Attribute[] listAttribute = value.getSubAttributes();
      for (Attribute subValue : listAttribute) {
        StringBuilder temporaryBuilder = new StringBuilder(this.builder);
        subValue.accept(this);
        this.builder = new StringBuilder(temporaryBuilder);
      }
    }
  }

  public void visitMultiValueSimple(MultiValueSimpleAttribute attribute) {
    StringBuilder temporaryBuilder = new StringBuilder(this.builder);
    addAttributeToBuilder(attribute, true);
    this.attributeName.add(this.builder.toString());
    this.builder = new StringBuilder(temporaryBuilder);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   addAttributeToBuilder
  /**
   ** Appends the attribute's name to the path being built.
   **
   ** @param attribute            The attribute being processed.
   **                             Allowed object is {@link Attribute}.
   ** @param end                  Whether this is the final attribute in the path.
   */
  private void addAttributeToBuilder(Attribute attribute, Boolean end) {
    if (attribute.getName().contains(":"))
      builder.append(attribute.getName() + ":");
    else if (!end)
      builder.append(attribute.getName() + ".");
    else {
      builder.append(attribute.getName());
    }
  }
}
