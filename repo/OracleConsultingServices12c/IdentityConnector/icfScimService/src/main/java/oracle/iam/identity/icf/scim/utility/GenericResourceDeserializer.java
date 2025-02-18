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

    File        :   GenericResourceDeserializer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    GenericResourceDeserializer.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.scim.utility;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.DeserializationContext;

import com.fasterxml.jackson.databind.node.ObjectNode;

import oracle.iam.identity.icf.scim.schema.Support;
import oracle.iam.identity.icf.scim.schema.GenericResource;

////////////////////////////////////////////////////////////////////////////////
// abstract class GenericResourceDeserializer
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Deserializes {@link GenericResource} objects to SCIM 2 values.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class GenericResourceDeserializer extends JsonDeserializer<GenericResource> {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>GenericResourceDeserializer</code> SCIM generic
   ** resource that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public GenericResourceDeserializer() {
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
   ** @param  context            the {@link DeserializationContext} that can be
   **                            used to access information about this
   **                            deserialization activity.
   **
   ** @return                    the deserialized {@link GenericResource} value.
   **
   ** @throws IOException            if the {@link JsonParser} detects an issue
   **                                to get the token text.
   */
  @Override
  public GenericResource deserialize(final JsonParser parser, final DeserializationContext context)
    throws IOException {

    final ObjectNode node = Support.objectReader().readTree(parser);
    return new GenericResource(node);
  }
}
