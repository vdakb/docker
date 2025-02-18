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

    File        :   ListResponse.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@silverid.fr

    Purpose     :   This file implements the ListResponse class.

    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-12-11  SBernet     First release version
*/

package bka.iam.identity.scim.extension.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
////////////////////////////////////////////////////////////////////////////////
// class ListResponse
// ~~~~~ ~~~~~~~~~~~~
/**
 ** Represents a paginated response for SCIM resources.
 ** Provides methods to manage and retrieve resources.
 **
 ** @param <T> The type of resource this response holds.
 **            Must extend {@link Resource}.
 ** 
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ListResponse<T extends Resource> implements Iterable<T> {

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  /** SCIM schema URN for ListResponse. */
  public static final String[] SCHEMAS = new String[] {
    "urn:ietf:params:scim:api:messages:2.0:ListResponse"
  };

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** A list that holds all the resources. */
  private final List<T> resources;

  /** The total result of the response. */
  private Long totalResults;
  
  /** The start index of this paginated response. */
  private int startIndex;

  /** The number of items per page in this response. */
  private int itemsPerPage;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Default constructor initializing an empty response.
   */
  public ListResponse() {
    super();
    this.resources = new ArrayList<>();
    this.totalResults = 0L;
    this.startIndex   = 0;
    this.itemsPerPage = 0;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Initializes the response with a list of resources and pagination details.
   ** 
   ** @param resources    The list of resources to include in the response.
   **                     Allowed object is {@link List}.
   ** @param startIndex   The starting index of the response.
   **                     Allowed object is {@link int}.
   ** @param itemsPerPage The number of items per page.
   **                     Allowed object is {@link int}.
   */
  public ListResponse(final List<T> resources,  final Long totalResults, final Integer startIndex, final Integer itemsPerPage) {
    super();
    this.resources = new ArrayList<>(resources);
    this.totalResults = totalResults != null ? totalResults : 0;
    this.startIndex   = startIndex != null ? startIndex : 0;
    this.itemsPerPage = itemsPerPage != null ? itemsPerPage : 0;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getTotalResult
  /**
   ** Retrieves the total number of resources in the response.
   ** 
   ** @return        The total number of resources.
   **                Possible object is {@link int}.
   */
  public Long getTotalResult() {
    return totalResults;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   setTotalResult
  /**
   ** set the total number of resources in the response.
   ** 
   ** @param totalResults   The starting index of the response.
   **                       Allowed object is {@link int}.
   */
  public void setTotalResult(final Long totalResults) {
    this.totalResults = totalResults;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getStartIndex
  /**
   ** Retrieves the start index of this paginated response.
   ** 
   ** @return        The start index.
   **                Possible object is {@link int}.
   */
  public int getStartIndex() {
    return startIndex;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   setStartIndex
  /**
   ** Set the start index of this paginated response.
   ** 
   ** @param startIndex     The starting index of the response.
   **                       Allowed object is {@link int}.
   */
  public void setStartIndex(final int startIndex) {
    this.startIndex = startIndex;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getItemsPerPage
  /**
   ** Retrieves the number of items per page in this response.
   ** 
   ** @return        The number of items per page.
   **                Possible object is {@link int}.
   */
  public int getItemsPerPage() {
    return itemsPerPage;
  }
  
    //////////////////////////////////////////////////////////////////////////////
  // Method:   setItemsPerPage
  /**
   ** Set the number of items per page in this response.
   ** 
   ** @param itemsPerPage The number of items per page.
   **                     Allowed object is {@link int}.
   */
  public void setItemsPerPage(final int itemsPerPage) {
    this.itemsPerPage = itemsPerPage;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods for Resource Management
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Adds a new resource to the response.
   ** If the resource already exists, it replaces the old one.
   ** 
   ** @param newResource The resource to add.
   **                    Allowed object is {@link T}.
   ** 
   ** @return            True if the resource was added successfully.
   **                    Possible object is {@link boolean}.
   */
  public boolean add(T newResource) {
    final Attribute newResourceID = newResource.get(ScimResource.ID);
    this.itemsPerPage++;
    
    if (newResourceID == null) {
      return this.resources.add(newResource);
    }

    for (T resource : this.resources) {
      final Attribute resourceIDAttr = resource.get(ScimResource.ID);
      if (resourceIDAttr != null &&
          resourceIDAttr.getValue().getStringValue().equals(newResourceID.getValue().getValueAsString())) {
        this.resources.remove(resource);
        return this.resources.add(newResource);
      }
    }

    return this.resources.add(newResource);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   get
  /**
   ** Retrieves a resource by its ID.
   ** 
   ** @param id        The ID of the resource to retrieve.
   **                  Allowed object is {@link String}.
   ** 
   ** @return          The resource with the specified ID, or null if not found.
   **                  Possible object is {@link T}.
   */
  public T get(final String id) {
    for (T resource : this.resources) {
      final Attribute resourceIDAttr = resource.get(ScimResource.ID);
      if (resourceIDAttr != null && resourceIDAttr.getValue().getStringValue().equals(id)) {
        return resource;
      }
    }
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getResources
  /**
   ** Retrieves all resources in the response.
   ** 
   ** @return          A list of resources.
   **                  Possible object is {@link List}.
   */
  public List<T> getResources() {
    return new ArrayList<>(this.resources);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Iterable Implementation
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   iterator
  /**
   ** Provides an iterator for iterating over the resources.
   ** 
   ** @return          An iterator for the resources.
   **                  Possible object is {@link Iterator}.
   */
  @Override
  public Iterator<T> iterator() {
    return resources.iterator();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString
  /**
   ** Provides a string representation of the ListResponse.
   ** Includes schemas, total results, and the resource list.
   ** 
   ** @return          A string representation of the ListResponse.
   **                   Possible object is {@link String}.
   */
  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder();
    builder.append("{")
           .append("\"schemas\":[\""   + SCHEMAS[0] + "\"],")
           .append("\"totalResults\":" + getTotalResult() + ",")
           .append("\"Resources\": [");
    Iterator<T> attributes = this.iterator();
    while (attributes.hasNext()) {
      Resource attribute = attributes.next();
      builder.append(attribute);

      if (attributes.hasNext()) {
        builder.append(",");
      }
    }
    builder.append("]}");

    return builder.toString();
  }
}
