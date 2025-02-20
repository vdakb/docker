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

    Copyright © 2018. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Service Simulation
    Subsystem   :   Generic SCIM Interface

    File        :   Evaluator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Evaluator.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.scim.object;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collections;

import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.databind.node.ObjectNode;

import oracle.iam.system.simulation.ServiceException;
import oracle.iam.system.simulation.BadRequestException;

import oracle.iam.system.simulation.scim.Path;

import oracle.iam.system.simulation.scim.annotation.Definition;

import oracle.iam.system.simulation.scim.schema.Support;

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
   ** the one and only instance of the <code>FilterEvaluator</code>
   ** <p>
   ** Singleton Pattern
   */
  private static final Evaluator instance   = new Evaluator();

  private static final Path      VALUE_PATH = Path.build().attribute("value");

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
   ** @throws ServiceException   if an exception occurs during the operation.
   */
  @Override
  public final Boolean and(final JsonNode node, final And filter)
    throws ServiceException {

    for (Filter cursor : filter.filter()) {
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
   ** @throws ServiceException   if an exception occurs during the operation.
   */
  @Override
  public final Boolean or(final JsonNode node, final Or filter)
    throws ServiceException {

    for (Filter cursor : filter.filter()) {
      if (!cursor.accept(this, node)) {
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
   **
   ** @return                    a visitor specified result.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws ServiceException   if an exception occurs during the operation.
   */
  @Override
  public final Boolean not(final JsonNode node, final Not filter)
    throws ServiceException {

    return !filter.accept(this, node);
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
   ** @throws ServiceException   if an exception occurs during the operation.
   */
  @Override
  public final Boolean present(final JsonNode node, final Presence filter)
    throws ServiceException {

    final Iterable<JsonNode> nodes = candidates(filter.path(), node);
    for (JsonNode cursor : nodes) {
      // draft-ietf-scim-core-schema section 2.4 states "Unassigned attributes,
      // the null value, or empty array (in the case of a multi-valued
      // attribute) SHALL be considered to be equivalent in "state".
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
   ** @throws ServiceException   if an exception occurs during the operation.
   */
  @Override
  public final Boolean equals(final JsonNode node, final Equals filter)
    throws ServiceException {

    final Iterable<JsonNode> nodes = candidates(filter.path(), node);
    if (filter.value().isNull() && empty(nodes)) {
      // draft-ietf-scim-core-schema section 2.4 states "Unassigned attributes,
      // the null value, or empty array (in the case of a multi-valued
      // attribute) SHALL be considered to be equivalent in "state".
      return true;
    }
    for (JsonNode cursor : nodes) {
      if (Support.compareTo(cursor, filter.value(), definition(filter.path())) == 0) {
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
   ** @throws ServiceException   if an exception occurs during the operation.
   */
  @Override
  public final Boolean greaterThan(final JsonNode node, final GreaterThan filter)
    throws ServiceException {

    final Iterable<JsonNode> nodes = candidates(filter.path(), node);
    for (JsonNode cursor : nodes) {
      if (cursor.isBoolean() || cursor.isBinary())
        throw BadRequestException.invalidFilter("Greater than filter may not compare boolean or binary attribute values");

      if (Support.compareTo(node, filter.value(), definition(filter.path())) > 0) {
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
   ** @throws ServiceException   if an exception occurs during the operation.
   */
  @Override
  public final Boolean greaterThanOrEqual(final JsonNode node, final GreaterThanOrEqual filter)
    throws ServiceException {

    final Iterable<JsonNode> nodes = candidates(filter.path(), node);
    for (JsonNode cursor : nodes) {
      if (cursor.isBoolean() || cursor.isBinary())
        throw BadRequestException.invalidFilter("Greater than or equal filter may not compare boolean or binary attribute values");

      if (Support.compareTo(node, filter.value(), definition(filter.path())) >= 0) {
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
   ** @throws ServiceException   if an exception occurs during the operation.
   */
  @Override
  public final Boolean lessThan(final JsonNode node, final LessThan filter)
    throws ServiceException {

    final Iterable<JsonNode> nodes = candidates(filter.path(), node);
    for (JsonNode cursor : nodes) {
      if (cursor.isBoolean() || cursor.isBinary())
        throw BadRequestException.invalidFilter("Less than or equal filter may not compare boolean or binary attribute values");

      if (Support.compareTo(node, filter.value(), definition(filter.path())) < 0) {
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
   ** @throws ServiceException   if an exception occurs during the operation.
   */
  @Override
  public final Boolean lessThanOrEqual(final JsonNode node, final LessThanOrEqual filter)
    throws ServiceException {

    final Iterable<JsonNode> nodes = candidates(filter.path(), node);
    for (JsonNode cursor : nodes) {
      if (cursor.isBoolean() || cursor.isBinary())
        throw BadRequestException.invalidFilter("Less than or equal filter may not compare boolean or binary attribute values");

      if (Support.compareTo(node, filter.value(), definition(filter.path())) <= 0) {
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
   ** @throws ServiceException   if an exception occurs during the operation.
   */
  @Override
  public final Boolean startsWith(final JsonNode node, final StartsWith filter)
    throws ServiceException {

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
   ** @throws ServiceException   if an exception occurs during the operation.
   */
  @Override
  public final Boolean endsWith(final JsonNode node, final EndsWith filter)
    throws ServiceException {

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
   ** @throws ServiceException   if an exception occurs during the operation.
   */
  @Override
  public final Boolean contains(final JsonNode node, final Contains filter)
    throws ServiceException {

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
   ** @throws ServiceException   if an exception occurs during the operation.
   */
  @Override
  public final Boolean complex(final JsonNode node, final ComplexFilter filter)
    throws ServiceException {

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
      else if (filter.filter().accept(this, node)) {
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
   ** @throws ServiceException   if the filter is not valid for matching.
   */
  public static boolean evaluate(final Filter filter, final JsonNode node)
    throws ServiceException {

    return filter.accept(instance, node);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   definition
  /**
   ** Returns the attribute definition for the attribute specified by the path
   ** to determine case sensitivity during string matching.
   **
   ** @param  path               the path to the attribute whose definition to
   **                            retrieve.
   **
   ** @return                    the attribute definition or <code>null</code>
   **                            if not available, in which case case
   **                            insensitive string value matching will be
   **                            performed.
   */
  protected Definition definition(@SuppressWarnings("unused") final Path path) {
    return null;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   matchSubstring
  /**
   ** Evaluate a substring match filter.
   **
   ** @param  filter             the filter to operate on.
   ** @param  node               the object to evaluate.
   **
   ** @return The return value from the operation.
   **
   ** @throws ServiceException   if an exception occurs during the operation.
   */
  private boolean matchSubstring(final AttributeFilter filter, Filter.Type type, final JsonNode node)
    throws ServiceException {

    final Iterable<JsonNode> nodes = candidates(filter.path(), node);
    for (JsonNode cursor : nodes) {
      if (cursor.isTextual() && filter.value().isTextual()) {
        final Definition definition      = definition(filter.path());
        String     nodeValue   = cursor.textValue();
        String     filterValue = filter.value().textValue();
        if (definition == null || !definition.caseExact()) {
          nodeValue   = nodeValue.toLowerCase();
          filterValue = filterValue.toLowerCase();
        }
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
   ** @throws ServiceException   if an exception occurs during the operation.
   */
  private Iterable<JsonNode> candidates(final Path path, final JsonNode node)
    throws ServiceException {

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
    if (node.isValueNode() && path.equals(VALUE_PATH)) {
      // Special case for the "value" path to reference the value itself.
      // Used for referencing the value nodes of an array when the filter is
      // attr[value eq "value1"] and the multi-valued attribute is
      // "attr": ["value1", "value2", "value3"].
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