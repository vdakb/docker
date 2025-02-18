/*
    Oracle Deutschland GmbH

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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Service Library
    Subsystem   :   Generic SCIM Interface

    File        :   Comparator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Comparator.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.platform.scim.response;


import java.util.List;
import java.util.Iterator;

import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.databind.node.ObjectNode;

import oracle.iam.platform.scim.SearchControl;
import oracle.iam.platform.scim.AttributeDefinition;
import oracle.iam.platform.scim.ResourceTypeDefinition;

import oracle.iam.platform.scim.entity.Path;

import oracle.iam.platform.scim.schema.Support;
import oracle.iam.platform.scim.schema.Resource;

////////////////////////////////////////////////////////////////////////////////
// class Comparator
// ~~~~~ ~~~~~~~~~~
/**
 ** Utility that could be used to compare POJOs representing SCIM resources
 ** using the SCIM sorting parameters.
 **
 ** @param  <T>                  the type of the resource to compare.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations of the request payload
 **                              extending this class (requests can return their
 **                              own specific type instead of type defined by
 **                              this class only).
 **                              <br>
 **                              Allowed object is <code>&lt;T&gt;</code>.c **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
class Comparator<T extends Resource> implements java.util.Comparator<T> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final Path                   sortBy;
  private final SearchControl.Order    order;
  private final ResourceTypeDefinition type;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new <code>Comparator</code> that will sort in specified
   ** order.
   **
   ** @param  type               the resource type definition containing the
   **                            schemas or <code>null</code> to compare using
   **                            case insensitive matching for string values.
   **                            <br>
   **                            Allowed object is
   **                            {@link ResourceTypeDefinition}.
   ** @param  sortBy             the path to the attribute to sort by.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  order              the sort order.
   **                            <br>
   **                            Allowed object is {@link SearchControl.Order}.
   */
  private Comparator(final ResourceTypeDefinition type, final Path sortBy, final SearchControl.Order order) {
    // ensure inheritance
    super();

    // initialize instance attribuets
    this.sortBy = sortBy;
    this.order  = order == null ? SearchControl.Order.ASCENDING : order;
    this.type   = type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  ////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compare
  /**
   ** Compares its two arguments for order.
   ** <br>
   ** Returns a negative integer, zero, or a positive integer as the first
   ** argument is less than, equal to, or greater than the second.
   ** <p>
   ** It is generally the case, but <i>not</i> strictly required that
   ** <code>(compare(x, y)==0) == (x.equals(y))</code>. Generally speaking, any
   ** comparator that violates this condition should clearly indicate this fact.
   ** The recommended language is "Note: this comparator imposes orderings that
   ** are inconsistent with equals."
   **
   ** @param  lhs                the first object to be compared.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   ** @param  rhs                the second object to be compared.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    a negative integer, zero, or a positive integer
   **                            as the first argument is less than, equal to,
   **                            or greater than the second.
   **                            <br>
   **                            Possible object is <code>int</code>.
   **
   ** @throws ClassCastException if the arguments' types prevent them from
   **                            being compared by this Comparator.
   */
  public int compare(final T lhs, final T rhs) {
    JsonNode         v1 = null;
    JsonNode         v2 = null;
    final ObjectNode n1 = lhs.generic().objectNode();
    final ObjectNode n2 = rhs.generic().objectNode();
    try {
      List<JsonNode> v1s = Support.matchPath(this.sortBy, n1);
      if(!v1s.isEmpty()) {
        // always just use the primary or first value of the first found node.
        v1 = primaryFirst(v1s.get(0));
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    try {
      List<JsonNode> v2s = Support.matchPath(this.sortBy, n2);
      if(!v2s.isEmpty()) {
        // always just use the primary or first value of the first found node.
        v2 = primaryFirst(v2s.get(0));
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    if (v1 == null && v2 == null) {
      return 0;
    }
    // or all attribute types, if there is no data for the specified "sortBy"
    // value they are sorted via the "sortOrder" parameter; i.e., they are
    // ordered last if ascending and first if descending.
    else if (v1 == null) {
      return this.order == SearchControl.Order.ASCENDING ? 1 : -1;
    }
    else if (v2 == null) {
      return this.order == SearchControl.Order.ASCENDING ? -1 : 1;
    }
    else {
      final AttributeDefinition definition = this.type == null ? null : this.type.attributeDefinition(this.sortBy);
      return this.order == SearchControl.Order.ASCENDING ? Support.compareTo(v1, v2, definition) : Support.compareTo(v2, v1, definition);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by funtionality
  ////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a new <code>Comparator</code> that will sort in
   ** ascending order.
   **
   ** @param  sortBy             the path to the attribute to sort by.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  resourceType       the resource type definition containing the
   **                            schemas or <code>null</code> to compare using
   **                            case insensitive matching for string values.
   **                            <br>
   **                            Allowed object is
   **                            {@link ResourceTypeDefinition}.
   **
   ** @return                    an instance of <code>Comparator</code>
   **                            populated with the values provided.
   **                            <br>
   **                            Possible object is
   **                            <code>ResourcePreparer</code>.
   */
  public static final <T extends Resource> Comparator build(final ResourceTypeDefinition type, final Path sortBy) {
    return build(type, sortBy, SearchControl.Order.ASCENDING);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a new <code>Comparator</code> that will sort in
   ** specified order.
   **
   ** @param  type               the resource type definition containing the
   **                            schemas or <code>null</code> to compare using
   **                            case insensitive matching for string values.
   **                            <br>
   **                            Allowed object is
   **                            {@link ResourceTypeDefinition}.
   ** @param  sortBy             the path to the attribute to sort by.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  order              the sort order.
   **                            <br>
   **                            Allowed object is {@link SearchControl.Order}.
   **
   ** @return                    an instance of <code>Comparator</code>
   **                            populated with the values provided.
   **                            <br>
   **                            Possible object is <code>Comparator</code>.
   */
  public static final <T extends Resource> Comparator build(final ResourceTypeDefinition type, final Path sortBy, final SearchControl.Order order) {
    return new Comparator<T>(type, sortBy, order);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   primaryFirst
  /**
   ** Returns the value of a complex multi-valued attribute that is marked as
   ** primary or the first value in the list.
   ** <br>
   ** If the provided node is not an array node, then just return the provided
   ** node.
   **
   ** @param  node               the JsonNode to return from.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   **
   ** @return                    the primary or first value or <code>null</code>
   **                            if the provided array node is empty.
   **                            <br>
   **                            Possible object is {@link JsonNode}.
   */
  private JsonNode primaryFirst(final JsonNode node) {
    // if it's a multi-valued attribute (see Section 2.4
    // [I-D.ietf - scim - core - schema]), if any, or else the first value in
    // the list, if any.
    if (!node.isArray())
      return node;

    if (node.size() == 0)
      return null;

    final Iterator<JsonNode> i = node.elements();
    while (i.hasNext()) {
      final JsonNode value   = i.next();
      final JsonNode primary = value.get("primary");
      if (primary != null && primary.booleanValue()) {
        return value;
      }
    }
    return node.get(0);
  }
}