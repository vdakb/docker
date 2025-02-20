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

    File        :   SingularComplexAttribute.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@silverid.fr

    Purpose     :   This file implements the  class
                    AttributeValue.

    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-12-11  SBernet     First release version
*/
package bka.iam.identity.scim.extension.model;
////////////////////////////////////////////////////////////////////////////////
// class SingularComplexAttribute
// ~~~~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 * The SingularComplexAttribute class represents a complex attribute in the
 * SCIM model that contains a single value with possible sub-attributes.
 *
 * This class extends {@link bka.iam.identity.scim.extension.model.Attribute} and defines a specific behavior for
 * complex attributes that do not support multiple values but can contain
 * nested sub-attributes.
 *
 * @author sylvert.bernet@silverid.fr
 * @version 1.0.0.0
 * @since 1.0.0.0
 */
public final class SingularComplexAttribute extends Attribute {

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   * Initializes a SingularComplexAttribute with a name and a single
   * AttributeValue.
   *
   * @param name The name of the complex attribute.
   * Allowed object is {@link String}.
   * @param values The single value (which may contain sub-attributes) of the
   * complex attribute.
   * Allowed object is {@link bka.iam.identity.scim.extension.model.AttributeValue}.
   */
  public SingularComplexAttribute(final String name, final AttributeValue values) {
    // ensure inheritance
    super(name, values);  
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Abstract Methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isComplex
  /**
   ** Determines if the attribute is a complex attribute.
   ** 
   ** @return        True as this attribute is always complex.
   */
  @Override
  public boolean isComplex() {
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isMultiValue
  /**
   ** Determines if the attribute can have multiple values.
   ** This attribute is singular, so it always returns false.
   ** 
   ** @return        False as this attribute can only hold a single value.
   */
  @Override
  public boolean isMultiValue() {
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isSubAttribute
  /**
   ** Determines if the attribute is a sub-attribute.
   ** This attribute is considered a sub-attribute, so it always returns true.
   ** 
   ** @return        True as this is a sub-attribute.
   */
  @Override
  public boolean isSubAttribute() {
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString
  /**
   ** Provides a string representation of the complex attribute in JSON format.
   ** 
   ** @return A string representation of the attribute.
   **         Possible object is {@link String}.
   */
  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder();
    builder.append("\"")
           .append(this.getName())
           .append("\"")
           .append(":");
    
    builder.append("{");
    for (AttributeValue value : this.getValues()) {
      builder.append(value);
    }
    builder.append("}");
    return builder.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clone
  /**
   * Creates a copy of this SingularComplexAttribute.
   * The clone method creates a new instance of this class with the same name
   * and value as the original.
   *
   * @return A clone of the SingularComplexAttribute instance.
   * Possible object is {@link bka.iam.identity.scim.extension.model.Attribute}.
   */
  @Override
  public Attribute clone() {
    return new SingularComplexAttribute(getName(), getValue());
  }
}
