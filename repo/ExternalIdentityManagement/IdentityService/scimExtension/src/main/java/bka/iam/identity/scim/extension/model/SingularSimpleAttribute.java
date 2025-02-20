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

    File        :   SingularSimpleAttribute.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@silverid.fr

    Purpose     :   This file implements the class
                    AttributeValue.

    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-12-11  SBernet     First release version
*/
package bka.iam.identity.scim.extension.model;
////////////////////////////////////////////////////////////////////////////////
// class SingularSimpleAttribute
// ~~~~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 * The SingularSimpleAttribute class represents a simple attribute in the SCIM
 * model that contains a single value.
 *
 * This class extends {@link bka.iam.identity.scim.extension.model.Attribute} and provides the implementation for
 * a simple attribute that does not contain sub-attributes or multiple values.
 * It is used for attributes that hold a single simple value such as a String,
 * Integer, Boolean, etc.
 *
 * @author sylvert.bernet@silverid.fr
 * @version 1.0.0.0
 * @since 1.0.0.0
 */
public final class SingularSimpleAttribute extends Attribute {

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   * Initializes a SingularSimpleAttribute with a name and a single
   * AttributeValue.
   *
   * @param name The name of the simple attribute.
   * Allowed object is {@link String}.
   * @param value The value associated with the simple attribute.
   * Allowed object is {@link bka.iam.identity.scim.extension.model.AttributeValue}.
   */
  public SingularSimpleAttribute(final String name, final AttributeValue value) {
    //ensure inheritance
    super(name, value);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Abstract Methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isComplex
  /**
   ** Determines if the attribute is a complex attribute.
   ** This method returns false since the attribute is simple (not complex).
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
   ** This attribute can only hold a single value, so it always returns false.
   ** 
   ** @return        False, as this attribute cannot hold multiple values.
   */
  @Override
  public boolean isMultiValue() {
    return false;
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
   ** Provides a string representation of the simple attribute in JSON format.
   ** The representation includes the name of the attribute and its value,
   ** formatted in a readable manner.
   ** 
   ** @return        A string representation of the attribute.
   **                Possible object is {@link String}.
   */
  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder();
    builder.append("\"")
           .append(this.getName())  // Append the attribute's name.
           .append("\"")
           .append(":");
    
    builder.append(this.getValue());  // Append the attribute's value.
    return builder.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clone
  /**
   * Creates a copy of this SingularSimpleAttribute.
   * The clone method creates a new instance of this class with the same name
   * and value as the original.
   *
   * @return A clone of the SingularSimpleAttribute instance.
   * Possible object is {@link bka.iam.identity.scim.extension.model.Attribute}.
   */
  @Override
  public Attribute clone() {
    return new SingularSimpleAttribute(getName(), getValue());
  }
}
