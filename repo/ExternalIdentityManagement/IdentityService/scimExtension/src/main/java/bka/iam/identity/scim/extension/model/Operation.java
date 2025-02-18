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

    File        :   Operation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@silverid.fr

    Purpose     :   This file implements the Operation class.

    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-20-11  SBernet     First release version
*/
package bka.iam.identity.scim.extension.model;
////////////////////////////////////////////////////////////////////////////////
// class Operation
// ~~~~~ ~~~~~~~~~~
/**
 ** The <code>Operation</code> class represents a single SCIM Patch operation.
 ** It encapsulates the operation type (e.g., ADD, REMOVE, REPLACE), the path
 ** of the attribute to which the operation applies, and the attribute value.
 **
 ** @author  sylvert.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Operation {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The type of operation (e.g., add, remove, replace). */
  private OperationType type;

  /** The path of the attribute to which the operation applies. */
  private String path;

  /** The value of the attribute involved in the operation. */
  private Attribute value;
  
  public enum OperationType {
    ADD("add"),
    REMOVE("remove"),
    REPLACE("replace");
    
    // the string representation of the operation type.
    private String value;
    
    /**
     ** Initializes an <code>OperationType</code> with a string value.
     **
     ** @param value   The string representation of the operation type.
     */
    OperationType(String value) {
      this.value = value;
    }
    
    //////////////////////////////////////////////////////////////////////////////
    // Method:   getValue
    /**
     ** Returns the string representation of the operation type.
     **
     ** @return        The operation type as a string.
     **                Possible object is {@link String}.
     */
    public String getValue() {
      return this.value;
    }
    
    @Override
    public String toString() {
      return this.value;
    }
    
    //////////////////////////////////////////////////////////////////////////////
    // Method:   fromString
    /**
     ** Converts a string to its corresponding <code>OperationType</code> enum.
     **
     ** @param type    The string representation of the operation type.
     **                Allowed object is {@link String}.
     **
     ** @return        The corresponding <code>OperationType</code> enum,
     **                or null if no match.
     **                Possible object is {@link OperationType}.
     */
    public static OperationType fromString(String type) {
      if (type != null)
        for (OperationType operationType : values()) {
          if (type.equalsIgnoreCase(operationType.value))
            return operationType; 
        }  
      return null;
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Initializes an <code>Operation</code> with the given type, path, and value.
   **
   ** @param type     The type of operation.
   **                 Allowed object is {@link OperationType}.
   ** @param path     The path of the attribute to which the operation applies.
   **                 Allowed object is {@link String}.
   ** @param value    The value of the attribute involved in the operation.
   **                 Allowed object is {@link Attribute}.
   */
  public Operation(OperationType type, String path, Attribute value) {
    this.type = type;
    this.path = path;
    this.value = value;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Accessors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPath
  /**
   ** Returns the path of the attribute to which the operation applies.
   **
   ** @return        The attribute path.
   **                Possible object is {@link String}.
   */
  public String getPath() {
    return this.path;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getValue
  /**
   ** Returns the value of the attribute involved in the operation.
   **
   ** @return        The attribute value.
   **                Possible object is {@link Attribute}.
   */
  public Attribute getValue() {
    return this.value;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getOperationType
  /**
   ** Returns the type of the operation.
   **
   ** @return        The operation type.
   **                Possible object is {@link OperationType}.
   */
  public OperationType getOperationType() {
    return this.type;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString
  /**
   ** Provides a JSON representation of the <code>Operation</code>.
   **
   ** @return        A JSON string representation of the operation.
   **                Possible object is {@link String}.
   */
  public String toString() {
    final StringBuilder builder = new StringBuilder();
    builder.append("{");
    builder.append("\"op\":\"").append(getOperationType().getValue()).append("\"");
    if (this.type != null) {
      builder.append(",");
      builder.append("\"path\": \"").append(getPath()).append("\"");
    }
    if (this.value != null) {
      builder.append(",");
      builder.append(this.value.toString());
    }
    builder.append("}");
    
    return builder.toString();
  }

}
