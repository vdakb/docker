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

    Copyright © 2021. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Red Hat Keycloak Connector

    File        :   AttributeDeserializer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AttributeDeserializer.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.keycloak.marshal;

import java.util.Map;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.DeserializationContext;

import com.fasterxml.jackson.databind.node.ArrayNode;

import oracle.iam.identity.icf.foundation.object.Pair;

////////////////////////////////////////////////////////////////////////////////
// final class AttributeDeserializer
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** Deserializes the collection of tagged-value {@link Pair} from a REST object
 ** representation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class AttributeDeserializer extends JsonDeserializer<List<Pair<String, String>>> {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AttributeDeserializer</code> REST object representation
   ** that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AttributeDeserializer() {
	  // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deserialize (JsonDeserializer)
  /**
   ** Method that can be called to ask implementation to deserialize JSON
   ** content into the value type this serializer handles.
   ** <br>
   ** Returned instance is to be constructed by method itself.
   ** <p>
   ** Pre-condition for this method is that the parser points to the first event
   ** that is part of value to deserializer (and which is never JSON 'null'
   ** literal, more on this below): for simple types it may be the only value;
   ** and for structured types the Object start marker or a
   ** <code>FIELD_NAME</code>.
   ** <p>
   ** The two possible input conditions for structured types result from
   ** polymorphism via fields. In the ordinary case, Jackson calls this method
   ** when it has encountered an <code>OBJECT_START</code>, and the method
   ** implementation must advance to the next token to see the first field name.
   **
   ** @param  parser             the {@link JsonParser} used for reading JSON
   **                            content.
   **                            <br>
   **                            Allowed object is {@link JsonParser}.
   ** @param  context            the {@link DeserializationContext} that can be
   **                            used to access information about this
   **                            deserialization activity.
   **                            <br>
   **                            Allowed object is
   **                            {@link DeserializationContext}.
   **
   ** @return                    the deserialized {@link Pair} value.
   **                            <br>
   **                            Possible object is {@link Pair} with type
   **                            {@link String} for the tag and <code>T</code>
   **                            as the value.
   */
  @Override
  public List<Pair<String, String>> deserialize(final JsonParser parser, final DeserializationContext context)
    throws IOException {

    final JsonNode                              node   = parser.readValueAsTree();
    final Iterator<Map.Entry<String, JsonNode>> cursor = node.fields();
    final List<Pair<String, String>>            result = new ArrayList<Pair<String, String>>();
    while (cursor.hasNext()) {
      final Map.Entry<String, JsonNode> e = cursor.next();
      if (!e.getValue().isArray()) {
        result.add((e.getValue().isNull()) ? null : Pair.of(e.getKey(), e.getValue().asText()));
      }
      else {
        final ArrayNode a = (ArrayNode)e.getValue();
        final Iterator<JsonNode> x = a.elements();
        while (x.hasNext()) {
          final JsonNode n = x.next();
          result.add((n.isNull() ? null : Pair.of(e.getKey(), n.asText())));
        }
      }
    }
    return result;
  }
}
