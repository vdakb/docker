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

    File        :   MultiValueSimpleAttribute.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@silverid.fr

    Purpose     :   This file implements the class
                    MultiValueSimpleAttribute.

    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-12-11  SBernet     First release version
*/
package bka.iam.identity.scim.extension.model;
////////////////////////////////////////////////////////////////////////////////
// class MultiValueSimpleAttribute
// ~~~~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The MultiValueSimpleAttribute class represents a simple attribute in the
 ** SCIM model that can hold multiple values.
 **
 ** This class extends {@link bka.iam.identity.scim.extension.model.Attribute} and provides the implementation for
 ** an attribute that can hold an array of values (simple, non-complex values).
 ** It is used for attributes that need to store multiple simple values, such as
 ** an array of Strings or Integers.
 **
 ** @author  sylvert.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class MultiValueSimpleAttribute extends Attribute {

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Initializes a MultiValueSimpleAttribute with a name and an array of
   ** AttributeValues.
   ** 
   ** @param name            The name of the multi-value attribute.
   **                        Allowed object is {@link String}.
   ** @param attributeValues The array of values associated with the attribute.
   **                        Allowed object is {@link AttributeValue[]} array.
   */
  public MultiValueSimpleAttribute(String name, AttributeValue[] attributeValues) {
    super(name, attributeValues); 
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Abstract Methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isComplex
  /**
   ** Determines if the attribute is a complex attribute.
   ** This method returns false since the attribute is simple.
   ** 
   ** @return        False, as this attribute is not complex.
   */
  @Override
  public boolean isComplex() {
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isMultiValue
  /**
   ** Determines if the attribute can have multiple values.
   ** This attribute can hold multiple values, so it always returns true.
   ** 
   ** @return        True, as this attribute can hold multiple values.
   */
  @Override
  public boolean isMultiValue() {
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isSubAttribute
  /**
   ** Determines if the attribute is a sub-attribute.
   ** This attribute is not a sub-attribute, so it always returns false.
   ** 
   ** @return        False, as this attribute is not a sub-attribute.
   */
  @Override
  public boolean isSubAttribute() {
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString
  /**
   ** Provides a string representation of the multi-value simple attribute in
   ** JSON format.
   ** The representation includes the name of the attribute and its values,
   ** formatted as a list inside square brackets for easier readability.
   ** 
   ** @return        A string representation of the attribute.
   **                Possible object is {@link String}.
   */
  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder();
    builder.append("\"")
           .append(this.getName())
           .append("\"")
           .append(":");
    
    builder.append("[");
    for (int i = 0; i < this.getValues().length; i++) {
      builder.append(this.getValues()[i]);
      if (i < this.getValues().length - 1) {
        builder.append(",");
      }
    }
    builder.append("]");
    return builder.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clone
  /**
   ** Creates a copy of this MultiValueSimpleAttribute.
   ** The clone method creates a new instance of this class with the same name
   ** and values as the original.
   **
   ** @return        A clone of the MultiValueSimpleAttribute instance.
   **                Possible object is {@link bka.iam.identity.scim.extension.model.Attribute}.
   */
  @Override
  public Attribute clone() {
    return new MultiValueSimpleAttribute(getName(), getValues());
  }
}
