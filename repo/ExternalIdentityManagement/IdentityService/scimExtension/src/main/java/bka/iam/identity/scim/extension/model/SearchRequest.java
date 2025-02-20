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

    File        :   SearchRequest.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@silverid.fr

    Purpose     :   This file implements the SearchRequest class.

    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-09-12  SBernet     First release version
*/
package bka.iam.identity.scim.extension.model;

import java.util.List;
////////////////////////////////////////////////////////////////////////////////
// class SearchRequest
// ~~~~~ ~~~~~~~~~~~~
/**
 ** The <code>SearchRequest</code> class represents a SCIM Patch operation. It
 ** includes the schemas for Patch operations and a list of operations to be
 ** performed on a resource.
 **
 ** @author  sylvert.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class SearchRequest extends ScimResource {
  
  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////
  
  // The Default SCIM schema URI for Search operations.
  public static final String[] SCHEMAS = new String[] {
    "urn:ietf:params:scim:api:messages:2.0:SearchRequest"
  };
  
  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Initializes a <code>PatchRequest</code> with a
   ** <code>ResourceDescriptor</code>.
   **
   ** @param descriptor      The descriptor defining the resource's schema.
   **                        Allowed object is {@link ResourceDescriptor}.
   */
  public SearchRequest(final ResourceDescriptor descriptor) {
    super(descriptor);
  }
  
  public SearchRequest(final ResourceDescriptor descriptor, List<Attribute> attributes) {
    super(descriptor, attributes);
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
  
    
  public AttributeValue[] getAttributes() {
    return get("attributes").getValues();
  }
  
  public String getFilter() {
    return getAttributeValue("filter");
  }
  
  public int getStartIndex() {
    return get("startIndex").getValue().getIntegerValue();
  }
  
  public int getCount() {
    return get("count").getValue().getIntegerValue();
  }
  
  public String getSortBy() {
    return getAttributeValue("sortBy");
  }
  
  public String getSortOrder() {
    return getAttributeValue("sortOrder");
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
    builder.append("\"attributes\":  [");
    boolean last = false;
    for (AttributeValue operation : getAttributes()) {
      if (last) {
        builder.append("\",");
      }
      builder.append("\"");
      builder.append(operation.toString());
      builder.append("\",");
      last = true;
    }
    builder.append("],");
    builder.append("\"attributes\": \"" + getAttributes() + "\",");
    builder.append("\"filter\": \"" + getFilter() + "\",");
    builder.append("\"startIndex\": \"" + getStartIndex() + "\",");
    builder.append("\"count\": \"" + getCount() + "\",");
    builder.append("\"sortBy\": \"" + getSortBy() + "\",");
    builder.append("\"sortOrder\": \"" + getSortOrder() + "\"");
    builder.append("}");
    return builder.toString();
  }
  
  public String[] getSchemaURIs() {
    return SCHEMAS;
  }

  @Override
  public Resource clone() {
    return new SearchRequest(new ResourceDescriptor(this.getResourceDescriptor().getSchema()), this.attributes);
  }
}
