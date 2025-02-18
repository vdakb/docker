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

    File        :   PatchRequest.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@silverid.fr

    Purpose     :   This file implements the PatchRequest class.

    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-20-11  SBernet     First release version
*/
package bka.iam.identity.scim.extension.model;

import java.util.ArrayList;
import java.util.List;
////////////////////////////////////////////////////////////////////////////////
// class PatchRequest
// ~~~~~ ~~~~~~~~~~~~
/**
 ** The <code>PatchRequest</code> class represents a SCIM Patch operation. It
 ** includes the schemas for Patch operations and a list of operations to be
 ** performed on a resource.
 **
 ** @author  sylvert.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class PatchRequest extends ScimResource {
  
  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////
  
  // The Default SCIM schema URI for Patch operations.
  public static final String[] SCHEMAS = new String[] {
    "urn:ietf:params:scim:api:messages:2.0:PatchOp"
  };
  
  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // A list that holds all the attributes of the resource.
  private final List<Operation> operations;
  
  
  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Default constructor initializing an empty list of operations.
   */
  /*public PatchRequest() {
    super();

    operations = new ArrayList<>();
  }*/
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Initializes a <code>PatchRequest</code> with a
   ** <code>ResourceDescriptor</code>.
   ** 
   ** //TODO: Check if ResourceDescriptor can be use in PatchRequest
   **
   ** @param descriptor      The descriptor defining the resource's schema.
   **                        Allowed object is {@link ResourceDescriptor}.
   */
  public PatchRequest(final ResourceDescriptor descriptor) {
    super(descriptor);

    operations = new ArrayList<>();
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Initializes a <code>PatchRequest</code> with a `ResourceDescriptor` and a list of
   ** operations to apply to the resource.
   **
   ** @param descriptor      The descriptor defining the resource's schema.
   **                        Allowed object is {@link ResourceDescriptor}.
   ** @param operations      The list of operations to apply.
   **                        Allowed object is {@link List}.
   */
  public PatchRequest(final ResourceDescriptor descriptor, List<Operation> operations) {
    super(descriptor);
    this.operations = operations;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Accessors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSchemas
  /**
   ** Retrieves the SCIM schema URIs associated with the Patch operation.
   **
   ** @return                The array of SCIM schema URIs.
   **                        Possible object is {@link String[]}.
   */
  public String[] getSchemas() {
    return SCHEMAS;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   addOperation
  /**
   ** Adds an operation to the list of operations in the <code>PatchRequest</code>.
   **
   ** @param operation       The operation to add.
   **                        Allowed object is {@link Operation}.
   **
   ** @return                True if the operation was added successfully,
   **                        false otherwise.
   **                        Possible object is {@link boolean}.
   */
  public boolean addOperation(Operation operation) {
    return operations.add(operation);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getOperations
  /**
   ** Retrieves the list of operations in the <code>PatchRequest</code>.
   **
   ** @return          A list of operations.
   **                  Possible object is {@link List}.
   */
  public List<Operation> getOperations() {
    return operations;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString
  /**
   ** Provides a JSON string representation of the <code>PatchRequest</code>.
   **
   ** @return          A JSON string representation of the PatchRequest.
   **                  Possible object is {@link String}.
   */
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    
    builder.append("{");
    builder.append("\"schemas\": [\"" + getSchemas()[0] + "\"],");
    builder.append("\"Operations\":  [");
    int i = 0;
    for (Operation operation : getOperations()) {
      builder.append(operation.toString());
      if (i < getOperations().size() - 1) {
        builder.append(",");
      }
      i++;
    }
    builder.append("]");
    builder.append("}");
    return builder.toString();
  }
  
  @Override
  public String[] getSchemaURIs() {
    return SCHEMAS;
  }

  @Override
  public Resource clone() {
    return new PatchRequest(new ResourceDescriptor(this.getResourceDescriptor().getSchema()), this.operations);
  }
}
