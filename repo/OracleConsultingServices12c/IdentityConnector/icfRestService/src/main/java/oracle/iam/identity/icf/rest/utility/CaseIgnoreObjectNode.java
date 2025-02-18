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
    Subsystem   :   Generic REST Library

    File        :   CaseIgnoreObjectNode.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    CaseIgnoreObjectNode.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.rest.utility;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import oracle.iam.identity.icf.foundation.utility.StringUtility;
import oracle.iam.identity.icf.foundation.utility.CollectionUtility;

////////////////////////////////////////////////////////////////////////////////
// class CaseIgnoreObjectNode
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** An ObjectNode with case-insensitive field names.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class CaseIgnoreObjectNode extends ObjectNode {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:5294976303352832045")
  private static final long serialVersionUID = -1041670042909210119L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new <code>CaseIgnoreObjectNode</code>.
   **
   ** @param  factory            the {@link JsonNodeFactory} acountable for the
   **                            node.
   **                            <br>
   **                            Allowed object is {@link JsonNodeFactory}.
   */
  public CaseIgnoreObjectNode(final JsonNodeFactory factory) {
    // ensure inheritance
    super(factory, CollectionUtility.caseInsensitiveMap());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new <code>CaseIgnoreObjectNode</code>.
   **
   ** @param  factory            the {@link JsonNodeFactory} acountable for the
   **                            node.
   **                            <br>
   **                            Allowed object is {@link JsonNodeFactory}.
   ** @param  kids               the fields to put in this
   **                            <code>CaseIgnoreObjectNode</code>.
   **                            <br>
   **                            Allowed object is {@link Map}.
   */
  public CaseIgnoreObjectNode(final JsonNodeFactory factory, final Map<String, JsonNode> kids) {
    // ensure inheritance
    // super(factory, new CaseIgnoreMap(kids));
    super(factory, kids);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deepCopy (overridden)
  /**
   ** Method that can be called to get a node that is guaranteed not to allow
   ** changing of this node through mutators on this node or any of its
   ** children.
   ** <br>
   ** This means it can either make a copy of this node (and all mutable
   ** children and grand children nodes), or node itself if it is immutable.
   ** <br>
   ** <b>Note</b>
   ** <br>
   ** Return type is guaranteed to have same type as the node method is called
   ** on; which is why method is declared with local generic type.
   **
   ** @return                    a {@link ObjectNode} that is either a copy of
   **                            this node (and all non-leaf children); or, for
   **                            immutable leaf nodes, node itself.
   **                            <br>
   **                            Possible object is {@link ObjectNode}.
   */
  @Override
  @SuppressWarnings("unchecked")
  public ObjectNode deepCopy() {
    final CaseIgnoreObjectNode ret = new CaseIgnoreObjectNode(_nodeFactory);
    for (Map.Entry<String, JsonNode> entry : _children.entrySet())
      ret._children.put(entry.getKey(), entry.getValue().deepCopy());
    return ret;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   findValue (overridden)
  /**
   ** Method for finding a JSON Object field with specified name in this node or
   ** its child nodes, and returning value it has.
   ** <br>
   ** If no matching field is found in this node or its descendants, returns
   ** <code>null</code>.
   **
   ** @param  name               the name of field to look for.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link JsonNode} of first matching node
   **                            found, if any; <code>null</code> if none.
   **                            <br>
   **                            Possible object is {@link JsonNode}.
   */
  @Override
  public JsonNode findValue(final String name) {
    for (Map.Entry<String, JsonNode> entry : _children.entrySet()) {
      if (name.equals(entry.getKey())) {
        return entry.getValue();
      }
      final JsonNode value = entry.getValue().findValue(name);
      if (value != null)
        return value;
    }
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   findValues (overridden)
  /**
   ** {@inheritDoc}
   */
  @Override
  public List<JsonNode> findValues(final String fieldName, final List<JsonNode> found) {
    List<JsonNode> local = found;
    for (Map.Entry<String, JsonNode> entry : _children.entrySet()) {
      if (StringUtility.equalIgnoreCase(fieldName, entry.getKey())) {
        if (local == null) {
          local = new ArrayList<JsonNode>();
        }
        local.add(entry.getValue());
      }
      // only add children if parent not added
      else {
        local = entry.getValue().findValues(fieldName, found);
      }
    }
    return local;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   findValuesAsText (overridden)
  /**
   ** {@inheritDoc}
   */
  @Override
  public List<String> findValuesAsText(final String fieldName, final List<String> found) {
    List<String> local = found;
    for (Map.Entry<String, JsonNode> entry : _children.entrySet()) {
      if (StringUtility.equalIgnoreCase(fieldName, entry.getKey())) {
        if (local == null) {
          local = new ArrayList<String>();
        }
        local.add(entry.getValue().asText());
      }
      // only add children if parent not added
      else {
        local = entry.getValue().findValuesAsText(fieldName, found);
      }
    }
    return local;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   findParent (overridden)
  /**
   ** Method for finding a JSON Object that contains specified field, within
   ** this node or its descendants.
   ** <br>
   ** If no matching field is found in this node or its descendants, returns
   ** <code>null</code>.
   **
   ** @param  name               the name of field to look for.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link JsonNode} of first matching node
   **                            found, if any; <code>null</code> if none.
   **                            <br>
   **                            Possible object is {@link ObjectNode}.
   */
  @Override
  public ObjectNode findParent(final String name) {
    for (Map.Entry<String, JsonNode> entry : _children.entrySet()) {
      if (StringUtility.equalIgnoreCase(name, entry.getKey())) {
        return this;
      }
      JsonNode value = entry.getValue().findParent(name);
      if (value != null) {
        return (ObjectNode)value;
      }
    }
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   findParents (overridden)
  /**
   ** {@inheritDoc}
   */
  @Override
  public List<JsonNode> findParents(final String fieldName, final List<JsonNode> found) {
    List<JsonNode> local = found;
    for (Map.Entry<String, JsonNode> entry : _children.entrySet()) {
      if (StringUtility.equalIgnoreCase(fieldName, entry.getKey())) {
        if (local == null) {
          local = new ArrayList<JsonNode>();
        }
        local.add(this);
      }
      else { // only add children if parent not added
        local = entry.getValue().findParents(fieldName, found);
      }
    }
    return local;
  }
}