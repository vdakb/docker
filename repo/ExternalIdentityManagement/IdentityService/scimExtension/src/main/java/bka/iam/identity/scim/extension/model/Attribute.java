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

    File        :   Attribute.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@silverid.fr

    Purpose     :   This file implements the abstract class
                    AttributeValue.

    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-12-11  SBernet     First release version
*/
package bka.iam.identity.scim.extension.model;

import bka.iam.identity.scim.extension.option.Visitor;

import java.util.Collection;
////////////////////////////////////////////////////////////////////////////////
// abstract class Attribute
// ~~~~~~~~ ~~~~~ ~~~~~~~~~
/**
 ** The Attribute class represents an attribute in the SCIM model.
 ** It is an abstract base class for defining different types of attributes.
 ** Attributes can hold either a single or multiple values and may have
 ** sub-attributes.
 ** This class provides basic functionality for managing the attribute name and
 ** its values.
 **
 ** @author  sylvert.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class Attribute {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // The name of the attribute.
  private String name;

  // The values of the attribute, which could be a single or an array of values.
  private AttributeValue[] values;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Initializes an Attribute with a name and an array of AttributeValues.
   ** 
   ** @param  name               The name of the attribute.
   **                            Allowed object is {@link String}.
   ** @param  values             The values associated with this attribute.
   **                            Allowed object is {@link AttributeValue[]}.
   */
  public Attribute(final String name, final AttributeValue[] values) {
    this.name = name;
    this.values = values.clone();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Initializes an Attribute with a name and a single AttributeValue.
   **
   ** @param  name                The name of the attribute.
   **                             Allowed object is {@link String}.
   ** @param  values              The single value associated with this attribute.
   **                             Allowed object is {@link AttributeValue}.
   */
  public Attribute(final String name, final AttributeValue values) {
    this.name = name;
    this.values = new AttributeValue[1];
    this.values[0] = values;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Returns the name of this attribute.
   ** 
   ** @return        The name of the attribute.
   **                Possible object is {@link String}.
   */
  public final String getName() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getValues
  /**
   ** Returns all values associated with this attribute.
   ** 
   ** @return        The values of the attribute.
   **                Possible object is {@link AttributeValue[]}.
   */
  public AttributeValue[] getValues() {
    if (this.values != null)
      return this.values.clone();
    return new AttributeValue[0];
  }
  
    //////////////////////////////////////////////////////////////////////////////
  // Method:   addValue
  /**
   ** Add the provided attribute value to this attributs
   ** 
   ** @param  value             the value to add to this attribute.
   **                           Possible object is {@link AttributeValue}.
   ** 
   */
  public void addValue(AttributeValue value) {
    final int              nbAttributeValue = this.values.length + 1;
    final AttributeValue[] values           = new AttributeValue[nbAttributeValue];
    
    for (int i = 0; i < nbAttributeValue - 1; i++) {
      values[i] = this.values[i];
    }
    values[nbAttributeValue - 1] = value;
    
    this.values = values.clone();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getValues
  /**
   ** Returns the first value associated with this attribute, if any.
   ** If no values exist, it returns null.
   **
   ** @return                    The first value of the attribute or null if no
   **                            values are present.
   **                            Possible object is {@link AttributeValue}.
   */
  public AttributeValue getValue() {
    if (this.values != null && this.values.length != 0)
      return (AttributeValue) values[0].clone();
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setValue
  /**
   * Sets the first value for this attribute.
   *
   * @param  value              The value to set.
   *                            Allowed object is {@link AttributeValue}.
   *
   * @return                    The updated value of the attribute.
   *                            Possible object is {@link AttributeValue}.
   */
  public AttributeValue setValue(final AttributeValue value) {
    this.values[0] = value.clone();
    return this.values[0];
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   accept
  /**
   ** Accepts a visitor to process this attribute.
   ** This method is part of the Visitor pattern and allows external processing
   ** of the attribute.
   **
   ** @param <C>        The type of the collection used by the visitor. It must
   **                   extend {@link Collection}.
   ** @param <T>        The type of the elements in the collection processed by
   **                   the visitor.
   ** @param visitor    The visitor that will process the attribute. 
   **                   Allowed object is {@link Visitor}.
   **
   ** @return           The result of visiting the attribute.
   */
  public final <C extends Collection, T> T accept(Visitor<? extends Collection<T>, T> visitor) {
    return visitor.visit(this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Abstract Methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Abstract Method:   isComplex
  /**
   ** Determines if the attribute is a complex attribute, meaning it has
   ** sub-attributes.
   ** 
   ** @return                    True if the attribute is complex, otherwise
   **                            false.
   */
  public abstract boolean isComplex();

  //////////////////////////////////////////////////////////////////////////////
  // Abstract Method:   isMultiValue
  /**
   ** Determines if the attribute can have multiple values.
   ** 
   ** @return                    True if the attribute can have multiple values,
   **                            otherwise false.
   */
  public abstract boolean isMultiValue();

  //////////////////////////////////////////////////////////////////////////////
  // Abstract Method:   isSubAttribute
  /**
   ** Determines if the attribute is a sub-attribute (used in nested attributes).
   ** This is an abstract method that needs to be implemented in subclasses.
   ** 
   ** @return                    True if the attribute is a sub-attribute,
   **                            otherwise false.
   */
  public abstract boolean isSubAttribute();

  //////////////////////////////////////////////////////////////////////////////
  // Abstract Method:   clone
  /**
   ** Creates a copy of this attribute.
   ** This is an abstract method that needs to be implemented in subclasses.
   **
   ** @return                    A clone of the attribute.
   **                            Possible object is {@link Attribute}.
   */
  @Override
  public abstract Attribute clone();
}
