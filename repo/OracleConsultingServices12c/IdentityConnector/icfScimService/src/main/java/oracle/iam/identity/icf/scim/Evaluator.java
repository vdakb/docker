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

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Generic SCIM Library

    File        :   Evaluator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Evaluator.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.scim;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collections;

import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.databind.node.ObjectNode;

import oracle.iam.identity.icf.rest.ServiceError;
import oracle.iam.identity.icf.rest.ServiceException;
import oracle.iam.identity.icf.rest.resource.ServiceBundle;

import oracle.iam.identity.icf.scim.filter.Or;
import oracle.iam.identity.icf.scim.filter.And;
import oracle.iam.identity.icf.scim.filter.Not;
import oracle.iam.identity.icf.scim.filter.Equals;
import oracle.iam.identity.icf.scim.filter.Presence;
import oracle.iam.identity.icf.scim.filter.Contains;
import oracle.iam.identity.icf.scim.filter.EndsWith;
import oracle.iam.identity.icf.scim.filter.LessThan;
import oracle.iam.identity.icf.scim.filter.StartsWith;
import oracle.iam.identity.icf.scim.filter.GreaterThan;
import oracle.iam.identity.icf.scim.filter.ComplexFilter;
import oracle.iam.identity.icf.scim.filter.LessThanOrEqual;
import oracle.iam.identity.icf.scim.filter.AttributeFilter;
import oracle.iam.identity.icf.scim.filter.GreaterThanOrEqual;

import oracle.iam.identity.icf.scim.schema.Support;

import oracle.iam.identity.icf.scim.annotation.Definition;

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
   ** The one and only instance of the <code>Evaluator</code>
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
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apply (Filter.Visitor)
  /**
   ** Visits an <code>and</code> filter.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** For the purposes of matching, an empty sub-filters should always
   ** evaluate to <code>true</code>.
   **
   ** @param  filter             the visited filter.
   **                            <br>
   **                            Allowed object is {@link And}.
   ** @param  node               a visitor specified parameter.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   **
   ** @return                    a visitor specified result.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws ServiceException   if an exception occurs during the operation.
   */
  @Override
  public final Boolean apply(final And filter, final JsonNode node)
    throws ServiceException {

    for (Filter cursor : filter.filter()) {
      if (!cursor.apply(this, node)) {
        return false;
      }
    }
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apply (Filter.Visitor)
  /**
   ** Visits an <code>or</code> filter.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** For the purposes of matching, an empty sub-filters should always
   ** evaluate to <code>false</code>.
   **
   ** @param  filter             the visited filter.
   **                            <br>
   **                            Allowed object is {@link Or}.
   ** @param  node               a visitor specified parameter.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   **
   ** @return                    a visitor specified result.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws ServiceException   if an exception occurs during the operation.
   */
  @Override
  public final Boolean apply(final Or filter, final JsonNode node)
    throws ServiceException {

    for (Filter cursor : filter.filter()) {
      if (cursor.apply(this, node)) {
        return true;
      }
    }
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apply (Filter.Visitor)
  /**
   ** Visits a <code>not</code> filter.
   **
   ** @param  filter             the visited filter.
   **                            <br>
   **                            Allowed object is {@link Not}.
   ** @param  node               a visitor specified parameter.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   **
   ** @return                    a visitor specified result.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws ServiceException   if an exception occurs during the operation.
   */
  @Override
  public final Boolean apply(final Not filter, final JsonNode node)
    throws ServiceException {

    return !filter.filter().apply(this, node);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apply (Filter.Visitor)
  /**
   ** Visits a <code>present</code> filter.
   **
   ** @param  filter             the visited filter.
   **                            <br>
   **                            Allowed object is {@link Presence}.
   ** @param  node               a visitor specified parameter.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   **
   ** @return                    a visitor specified result.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws ServiceException   if an exception occurs during the operation.
   */
  @Override
  public final Boolean apply(final Presence filter, final JsonNode node)
    throws ServiceException {

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

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apply (Filter.Visitor)
  /**
   ** Visits an <code>equality</code> filter.
   **
   ** @param  filter             the visited filter.
   **                            <br>
   **                            Allowed object is {@link Equals}.
   ** @param  node               a visitor specified parameter.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   **
   ** @return                    a visitor specified result.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws ServiceException   if an exception occurs during the operation.
   */
  @Override
  public final Boolean apply(final Equals filter, final JsonNode node)
    throws ServiceException {

    final Iterable<JsonNode> nodes = candidates(filter.path(), node);
    if (filter.value().isNull() && empty(nodes)) {
      // draft-ietf-scim-core-schema section 2.4 states "Unassigned attributes,
      // the null value, or empty array (in the case of a multi-valued
      // attribute) SHALL be considered to be equivalent in "state"
      return true;
    }
    for (JsonNode cursor : nodes) {
      if (Support.compareTo(cursor, filter.value(), definition(filter.path())) == 0) {
        return true;
      }
    }
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apply (Filter.Visitor)
  /**
   ** Visits a <code>greater than</code> filter.
   **
   ** @param  filter             the visited filter.
   **                            <br>
   **                            Allowed object is {@link GreaterThan}.
   ** @param  node               a visitor specified parameter.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   **
   ** @return                    a visitor specified result.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws ServiceException   if an exception occurs during the operation.
   */
  @Override
  public final Boolean apply(final GreaterThan filter, final JsonNode node)
    throws ServiceException {

    final Iterable<JsonNode> nodes = candidates(filter.path(), node);
    for (JsonNode cursor : nodes) {
      if (cursor.isBoolean() || cursor.isBinary())
        throw ServiceException.invalidFilter(ServiceBundle.string(ServiceError.FILTER_USAGE_INVALID_GT));

      if (Support.compareTo(cursor, filter.value(), definition(filter.path())) > 0) {
        return true;
      }
    }
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apply (Filter.Visitor)
  /**
   ** Visits a <code>greater than or equal to</code> filter.
   **
   ** @param  filter             the visited filter.
   **                            <br>
   **                            Allowed object is {@link GreaterThanOrEqual}.
   ** @param  node               a visitor specified parameter.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   **
   ** @return                    a visitor specified result.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws ServiceException   if an exception occurs during the operation.
   */
  @Override
  public final Boolean apply(final GreaterThanOrEqual filter, final JsonNode node)
    throws ServiceException {

    final Iterable<JsonNode> nodes = candidates(filter.path(), node);
    for (JsonNode cursor : nodes) {
      if (cursor.isBoolean() || cursor.isBinary())
        throw ServiceException.invalidFilter(ServiceBundle.string(ServiceError.FILTER_USAGE_INVALID_GE));

      if (Support.compareTo(cursor, filter.value(), definition(filter.path())) >= 0) {
        return true;
      }
    }
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apply (Filter.Visitor)
  /**
   ** Visits a <code>less than</code> filter.
   **
   ** @param  filter             the visited filter.
   **                            <br>
   **                            Allowed object is {@link LessThan}.
   ** @param  node               a visitor specified parameter.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   **
   ** @return                    a visitor specified result.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws ServiceException   if an exception occurs during the operation.
   */
  @Override
  public final Boolean apply(final LessThan filter, final JsonNode node)
    throws ServiceException {

    final Iterable<JsonNode> nodes = candidates(filter.path(), node);
    for (JsonNode cursor : nodes) {
      if (cursor.isBoolean() || cursor.isBinary())
        throw ServiceException.invalidFilter(ServiceBundle.string(ServiceError.FILTER_USAGE_INVALID_LT));

      if (Support.compareTo(cursor, filter.value(), definition(filter.path())) < 0) {
        return true;
      }
    }
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apply (Filter.Visitor)
  /**
   ** Visits a <code>less than or equal to</code> filter.
   **
   ** @param  filter             the visited filter.
   **                            <br>
   **                            Allowed object is {@link LessThanOrEqual}.
   ** @param  node               a visitor specified parameter.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   **
   ** @return                    a visitor specified result.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws ServiceException   if an exception occurs during the operation.
   */
  @Override
  public final Boolean apply(final LessThanOrEqual filter, final JsonNode node)
    throws ServiceException {

    final Iterable<JsonNode> nodes = candidates(filter.path(), node);
    for (JsonNode cursor : nodes) {
      if (cursor.isBoolean() || cursor.isBinary())
        throw ServiceException.invalidFilter(ServiceBundle.string(ServiceError.FILTER_USAGE_INVALID_LE));

      if (Support.compareTo(cursor, filter.value(), definition(filter.path())) <= 0) {
        return true;
      }
    }
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apply (Filter.Visitor)
  /**
   ** Visits a <code>starts with</code> filter.
   **
   ** @param  filter             the visited filter.
   **                            <br>
   **                            Allowed object is {@link StartsWith}.
   ** @param  node               a visitor specified parameter.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   **
   ** @return                    a visitor specified result.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws ServiceException   if an exception occurs during the operation.
   */
  @Override
  public final Boolean apply(final StartsWith filter, final JsonNode node)
    throws ServiceException {

    return matchSubstring(filter, Filter.Type.SW, node);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apply (Filter.Visitor)
  /**
   ** Visits a <code>ends with</code> filter.
   **
   ** @param  filter             the visited filter.
   **                            <br>
   **                            Allowed object is {@link EndsWith}.
   ** @param  node               a visitor specified parameter.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   **
   ** @return                    a visitor specified result.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws ServiceException   if an exception occurs during the operation.
   */
  @Override
  public final Boolean apply(final EndsWith filter, final JsonNode node)
    throws ServiceException {

    return matchSubstring(filter, Filter.Type.EW, node);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apply (Filter.Visitor)
  /**
   ** Visits a <code>contains</code> filter.
   **
   ** @param  filter             the visited filter.
   **                            <br>
   **                            Allowed object is {@link Contains}.
   ** @param  node               a visitor specified parameter.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   **
   ** @return                    a visitor specified result.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws ServiceException   if an exception occurs during the operation.
   */
  @Override
  public final Boolean apply(final Contains filter, final JsonNode node)
    throws ServiceException {

    return matchSubstring(filter, Filter.Type.CO, node);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apply (Filter.Visitor)
  /**
   ** Visits a <code>complex</code> filter.
   **
   ** @param  filter             the visited filter.
   **                            <br>
   **                            Allowed object is {@link ComplexFilter}.
   ** @param  node               a visitor specified parameter.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   **
   ** @return                    a visitor specified result.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws ServiceException   if an exception occurs during the operation.
   */
  @Override
  public final Boolean apply(final ComplexFilter filter, final JsonNode node)
    throws ServiceException {

    final Iterable<JsonNode> nodes = candidates(filter.path(), node);
    for (JsonNode cursor : nodes) {
      if (cursor.isArray()) {
        // filter each element of the array individually
        for (JsonNode value : cursor) {
          if (filter.filter().apply(this, value)) {
            return true;
          }
        }
      }
      else if (filter.filter().apply(this, cursor)) {
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

    return filter.apply(instance, node);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   definition
  /**
   ** Returns the attribute definition for the attribute specified by the path
   ** to determine case sensitivity during string matching.
   **
   ** @param  path               the path to the attribute whose definition to
   **                            retrieve.
   **                            <br>
   **                            Allowed object is {@link Path}.
   **
   ** @return                    the attribute definition or <code>null</code>
   **                            if not available, in which case case
   **                            insensitive string value matching will be
   **                            performed.
   **                            <br>
   **                            Possible object is {@link Definition}.
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