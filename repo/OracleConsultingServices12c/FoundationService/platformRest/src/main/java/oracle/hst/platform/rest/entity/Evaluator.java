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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Service Extension
    Subsystem   :   Generic REST Library

    File        :   Evaluator.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Evaluator.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-06  DSteding    First release version
*/

package oracle.hst.platform.rest.entity;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collections;

import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.databind.node.ObjectNode;

import oracle.hst.platform.core.SystemException;

import oracle.hst.platform.core.entity.Or;
import oracle.hst.platform.core.entity.And;
import oracle.hst.platform.core.entity.Not;
import oracle.hst.platform.core.entity.Path;
import oracle.hst.platform.core.entity.Filter;
import oracle.hst.platform.core.entity.Equals;
import oracle.hst.platform.core.entity.Contains;
import oracle.hst.platform.core.entity.LessThan;
import oracle.hst.platform.core.entity.Presence;
import oracle.hst.platform.core.entity.EndsWith;
import oracle.hst.platform.core.entity.StartsWith;
import oracle.hst.platform.core.entity.GreaterThan;
import oracle.hst.platform.core.entity.ComplexFilter;
import oracle.hst.platform.core.entity.LessThanOrEqual;
import oracle.hst.platform.core.entity.AttributeFilter;
import oracle.hst.platform.core.entity.GreaterThanOrEqual;

import oracle.hst.platform.rest.ServiceError;
import oracle.hst.platform.rest.ServiceBundle;
import oracle.hst.platform.rest.ProcessingException;
import oracle.hst.platform.rest.BadRequestException;
import oracle.hst.platform.rest.ServerErrorException;

import oracle.hst.platform.rest.schema.Support;

////////////////////////////////////////////////////////////////////////////////
// class Evaluator
// ~~~~~ ~~~~~~~~~
/**
 ** A filter visitor that will evaluate a filter on a {@link JsonNode} and
 ** return whether the {@link JsonNode} matches the filter.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Evaluator implements Filter.Visitor<Boolean, JsonNode> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the one and only instance of the <code>Evaluator</code>
   ** <p>
   ** Singleton Pattern
   */
  private static final Evaluator instance   = new Evaluator();

  private static final Path      VALUE_PATH = Path.build().path("value");

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Evaluator</code> handler that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected Evaluator() {
    // ensure inhritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   and (Filter.Visitor)
  /**
   ** Visits an <code>and</code> filter.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** For the purposes of matching, an empty sub-filters should always
   ** evaluate to <code>true</code>.
   **
   ** @param  node               a visitor specified parameter.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   ** @param  filter             the visited filter.
   **                            <br>
   **                            Allowed object is {@link And}.
   **
   ** @return                    a visitor specified result.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws SystemException    if an exception occurs during the operation.
   */
  @Override
  public final Boolean and(final JsonNode node, final And<JsonNode> filter)
    throws SystemException {

    for (Filter<JsonNode> cursor : filter.filter()) {
      if (!cursor.accept(this, node)) {
        return false;
      }
    }
    return true;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   or (Filter.Visitor)
  /**
   ** Visits an <code>or</code> filter.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** For the purposes of matching, an empty sub-filters should always
   ** evaluate to <code>false</code>.
   **
   ** @param  node               a visitor specified parameter.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   ** @param  filter             the visited filter.
   **                            <br>
   **                            Allowed object is {@link Or}.
   **
   ** @return                    a visitor specified result.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws SystemException    if an exception occurs during the operation.
   */
  @Override
  public final Boolean or(final JsonNode node, final Or<JsonNode> filter)
    throws SystemException {

    for (Filter<JsonNode> cursor : filter.filter()) {
      if (cursor.accept(this, node)) {
        return true;
      }
    }
    return false;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   not (Filter.Visitor)
  /**
   ** Visits an <code>not</code> filter.
   **
   ** @param  node               a visitor specified parameter.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   ** @param  filter             the visited filter.
   **                            <br>
   **                            Allowed object is {@link Not}.
   **
   ** @return                    a visitor specified result.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws SystemException   if an exception occurs during the operation.
   */
  @Override
  public final Boolean not(final JsonNode node, final Not<JsonNode> filter)
    throws SystemException {

    return !filter.filter().accept(this, node);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   present (Filter.Visitor)
  /**
   ** Visits an <code>present</code> filter.
   **
   ** @param  node               a visitor specified parameter.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   ** @param  filter             the visited filter.
   **                            <br>
   **
   ** @return                    a visitor specified result.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws SystemException    if an exception occurs during the operation.
   */
  @Override
  public final Boolean present(final JsonNode node, final Presence<JsonNode> filter)
    throws SystemException {

    final Iterable<JsonNode> nodes = candidates(filter.path(), node);
    for (JsonNode cursor : nodes) {
      // draft-ietf-scim-core-schema section 2.5 states "Unassigned attributes,
      // the null value, or empty array (in the case of a multi-valued
      // attribute) SHALL be considered to be equivalent in "state"
      if (!empty(cursor)) {
        return true;
      }
    }
    return false;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   equals (Filter.Visitor)
  /**
   ** Visits an <code>equality</code> filter.
   **
   ** @param  node               a visitor specified parameter.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   ** @param  filter             the visited filter.
   **                            <br>
   **
   ** @return                    a visitor specified result.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws ProcessingException if an exception occurs during the operation.
   */
  @Override
  public final Boolean equals(final JsonNode node, final Equals<JsonNode> filter)
    throws ProcessingException {

    final Iterable<JsonNode> nodes = candidates(filter.path(), node);
    if (filter.value().isNull() && empty(nodes)) {
      // draft-ietf-scim-core-schema section 2.4 states "Unassigned attributes,
      // the null value, or empty array (in the case of a multi-valued
      // attribute) SHALL be considered to be equivalent in "state"
      return true;
    }
    for (JsonNode cursor : nodes) {
      if (Support.compareTo(cursor, filter.value()) == 0) {
        return true;
      }
    }
    return false;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   greaterThan (Filter.Visitor)
  /**
   ** Visits an <code>greater than</code> filter.
   **
   ** @param  node               a visitor specified parameter.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   ** @param  filter             the visited filter.
   **                            <br>
   **
   ** @return                    a visitor specified result.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws ProcessingException if an exception occurs during the operation.
   */
  @Override
  public final Boolean greaterThan(final JsonNode node, final GreaterThan<JsonNode> filter)
    throws ProcessingException {

    final Iterable<JsonNode> nodes = candidates(filter.path(), node);
    for (JsonNode cursor : nodes) {
      if (cursor.isBoolean() || cursor.isBinary())
        throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.FILTER_INVALID_VALUE_TYPE_GT));

      if (Support.compareTo(cursor, filter.value()) > 0) {
        return true;
      }
    }
    return false;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   greaterThanOrEqual (Filter.Visitor)
  /**
   ** Visits an <code>greater than or equal to</code> filter.
   **
   ** @param  node               a visitor specified parameter.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   ** @param  filter             the visited filter.
   **                            <br>
   **
   ** @return                    a visitor specified result.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws ProcessingException if an exception occurs during the operation.
   */
  @Override
  public final Boolean greaterThanOrEqual(final JsonNode node, final GreaterThanOrEqual<JsonNode> filter)
    throws ProcessingException {

    final Iterable<JsonNode> nodes = candidates(filter.path(), node);
    for (JsonNode cursor : nodes) {
      if (cursor.isBoolean() || cursor.isBinary())
        throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.FILTER_INVALID_VALUE_TYPE_GE));

      if (Support.compareTo(cursor, filter.value()) >= 0) {
        return true;
      }
    }
    return false;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   lessThan (Filter.Visitor)
  /**
   ** Visits an <code>less than</code> filter.
   **
   **
   ** @param  node               a visitor specified parameter.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   ** @param  filter             the visited filter.
   **                            <br>
   **
   ** @return                    a visitor specified result.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws ProcessingException if an exception occurs during the operation.
   */
  @Override
  public final Boolean lessThan(final JsonNode node, final LessThan<JsonNode> filter)
    throws ProcessingException {

    final Iterable<JsonNode> nodes = candidates(filter.path(), node);
    for (JsonNode cursor : nodes) {
      if (cursor.isBoolean() || cursor.isBinary())
        throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.FILTER_INVALID_VALUE_TYPE_LT));

      if (Support.compareTo(cursor, filter.value()) < 0) {
        return true;
      }
    }
    return false;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   lessThanOrEqual (Filter.Visitor)
  /**
   ** Visits a <code>less than or equal to</code> filter.
   **
   **
   ** @param  node               a visitor specified parameter.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   ** @param  filter             the visited filter.
   **                            <br>
   **
   ** @return                    a visitor specified result.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws ProcessingException if an exception occurs during the operation.
   */
  @Override
  public final Boolean lessThanOrEqual(final JsonNode node, final LessThanOrEqual<JsonNode> filter)
    throws ProcessingException {

    final Iterable<JsonNode> nodes = candidates(filter.path(), node);
    for (JsonNode cursor : nodes) {
      if (cursor.isBoolean() || cursor.isBinary())
        throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.FILTER_INVALID_VALUE_TYPE_LE));

      if (Support.compareTo(cursor, filter.value()) <= 0) {
        return true;
      }
    }
    return false;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   startsWith (Filter.Visitor)
  /**
   ** Visits a <code>starts with</code> filter.
   **
   **
   ** @param  node               a visitor specified parameter.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   ** @param  filter             the visited filter.
   **                            <br>
   **
   ** @return                    a visitor specified result.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws ProcessingException if an exception occurs during the operation.
   */
  @Override
  public final Boolean startsWith(final JsonNode node, final StartsWith<JsonNode> filter)
    throws ProcessingException {

    return matchSubstring(filter, Filter.Type.SW, node);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   endsWith (Filter.Visitor)
  /**
   ** Visits a {@code ends with} filter.
   **
   ** @param  node               a visitor specified parameter.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   ** @param  filter             the visited filter.
   **                            <br>
   **
   ** @return                    a visitor specified result.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws ProcessingException if an exception occurs during the operation.
   */
  @Override
  public final Boolean endsWith(final JsonNode node, final EndsWith<JsonNode> filter)
    throws ProcessingException {

    return matchSubstring(filter, Filter.Type.EW, node);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   contains (Filter.Visitor)
  /**
   ** Visits an <code>contains</code> filter.
   **
   ** @param  node               a visitor specified parameter.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   ** @param  filter             the visited filter.
   **                            <br>
   **
   ** @return                    a visitor specified result.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws ProcessingException if an exception occurs during the operation.
   */
  @Override
  public final Boolean contains(final JsonNode node, final Contains<JsonNode> filter)
    throws ProcessingException {

    return matchSubstring(filter, Filter.Type.CO, node);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method: complex (Filter.Visitor)
  /**
   ** Visits an <code>complex</code> filter.
   **
   ** @param  node               a visitor specified parameter.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   ** @param  filter             the visited filter.
   **                            <br>
   **
   ** @return                    a visitor specified result.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws SystemException    if an exception occurs during the operation.
   */
  @Override
  public final Boolean complex(final JsonNode node, final ComplexFilter<JsonNode> filter)
    throws SystemException {

    final Iterable<JsonNode> nodes = candidates(filter.path(), node);
    for (JsonNode cursor : nodes) {
      if (cursor.isArray()) {
        // filter each element of the array individually
        for (JsonNode value : cursor) {
          if (filter.filter().accept(this, value)) {
            return true;
          }
        }
      }
      else if (filter.filter().accept(this, cursor)) {
        return true;
      }
    }
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method group by functionallity
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   evaluate
  /**
   ** Evaluate the provided filter against the provided {@link JsonNode}.
   **
   ** @param  filter             the filter to evaluate.
   **                            <br>
   **                            Allowed object is {@link Filter}.
   ** @param  node               the {@link JsonNode} to evaluate the filter
   **                            against.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   **
   ** @return                    <code>true</code> if the {@link JsonNode}
   **                            matches the filter or <code>false</code>
   **                            otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   **
   ** @throws ProcessingException if an exception occurs during the operation.
   */
  public static boolean evaluate(final Filter<JsonNode> filter, final JsonNode node)
    throws ProcessingException {

    try {
      return filter.accept(instance, node);
    }
    catch (SystemException e) {
      throw ServerErrorException.internal(e);
    }
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   matchSubstring
  /**
   ** Evaluate a substring match filter.
   **
   ** @param  filter             the filter to operate on.
   **                            <br>
   **                            Allowed object is {@link AttributeFilter}.
   ** @param  type               the type ofthe filter to operate on.
   **                            <br>
   **                            Allowed object is {@link Filter.Type}.
   ** @param  node               the object to evaluate.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   **
   ** @return                    the return value from the operation.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   **
   ** @throws ProcessingException if an exception occurs during the operation.
   */
  private boolean matchSubstring(final AttributeFilter<JsonNode> filter, Filter.Type type, final JsonNode node)
    throws ProcessingException {

    final Iterable<JsonNode> nodes = candidates(filter.path(), node);
    for (JsonNode cursor : nodes) {
      if (cursor.isTextual() && filter.value().isTextual()) {
        String     nodeValue   = cursor.textValue();
        String     filterValue = filter.value().textValue();
        nodeValue   = nodeValue.toLowerCase();
        filterValue = filterValue.toLowerCase();
        switch (type) {
          case CO : if (nodeValue.contains(filterValue)) {
                      return true;
                    }
                    break;
          case SW : if (nodeValue.startsWith(filterValue)) {
                      return true;
                    }
                    break;
          case EW : if (nodeValue.endsWith(filterValue)) {
                      return true;
                    }
                    break;
        }
      }
      else if (cursor.equals(filter.value())) {
        return true;
      }
    }
    return false;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   candidates
  /**
   ** Returns the {@link JsonNode}s to compare against.
   **
   ** @param  path               the path to the value.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  node               the {@link JsonNode} containing the value.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   **
   ** @return                    the {@link JsonNode}s to compare against.
   **                            <br>
   **                            Possible object is {@link Iterable} where each
   **                            element is of type {@link JsonNode}.
   **
   ** @throws ProcessingException if an exception occurs during the operation.
   */
  private Iterable<JsonNode> candidates(final Path path, final JsonNode node)
    throws ProcessingException {

    if (node.isArray()) {
      return node;
    }
    if (node.isObject()) {
      final List<JsonNode>      nodes     = Support.matchPath(path, (ObjectNode)node);
      final ArrayList<JsonNode> flattened = new ArrayList<JsonNode>(nodes.size());
      for (JsonNode cursor : nodes) {
        if (cursor.isArray()) {
          for (JsonNode child : cursor) {
            flattened.add(child);
          }
        }
        else {
          flattened.add(cursor);
        }
      }
      return flattened;
    }
    if (path.equals(VALUE_PATH)) {
      // special case for the "value" path to reference the value itself
      // used for referencing the value nodes of an array when the filter is
      // attr[value eq "value1"] and the multi-valued attribute is
      // "attr": ["value1", "value2", "value3"]
      return Collections.singletonList(node);
    }
    return Collections.emptyList();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   empty
  /**
   ** Return <code>true<code> if the specified node collection contains nothing
   ** but empty arrays and/or <code>null</code> nodes.
   **
   ** @param  nodes              the collection of nodes as returned from
   **                            Support.matchPath.
   **                            <br>
   **                            Allowed object is {@link Iterable} where each
   **                            element is of type {@link JsonNode}.
   **
   ** @return                    <code>true<code> if the collection contains
   **                            only empty array(s).
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  private boolean empty(final Iterable<JsonNode> nodes) {
    for (JsonNode node : nodes) {
      if (!empty(node)) {
        return false;
      }
    }
    return true;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   empty
  /**
   ** Return <code>true<code> if the <code>node</code> given is either
   ** <code>null</code> or an empty array.
   **
   ** @param  node               the node to examine.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   **
   ** @return                    <code>true<code> if the <code>node</code>
   **                            given is either <code>null</code> or an empty
   **                            array.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  private boolean empty(final JsonNode node) {
    if (node.isArray()) {
      final Iterator<JsonNode> iterator = node.elements();
      while (iterator.hasNext()) {
        if (!empty(iterator.next())) {
          return false;
        }
      }
      return true;
    }
    else {
      return node.isNull();
    }
  }
}