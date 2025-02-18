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

    File        :   StringArraySerializer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    StringArraySerializer.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.rest.utility;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

////////////////////////////////////////////////////////////////////////////////
// final class StringArraySerializer
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** Serializes an array of strings to a REST object representation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class StringArraySerializer extends JsonSerializer<Object> {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>StringArraySerializer</code> REST object representation
   ** that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public StringArraySerializer() {
	  // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serialize (JsonSerializer)
  /**
   ** Method that can be called to ask implementation to serialize values of
   ** type this serializer handles.
   **
   ** @param  value              the {@link Object} value to serialize
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Object}.
   ** @param  generator          the {@link JsonGenerator} used to output
   **                            resulting Json content.
   **                            <br>
   **                            Allowed object is {@link JsonGenerator}.
   ** @param  serializers        the {@link SerializerProvider} that can be used
   **                            to get serializers for serializing Objects
   **                            value contains, if any.
   **                            <br>
   **                            Allowed object is {@link SerializerProvider}.
   **
   ** @throws IOException        if the parser has an issue to get the
   **                            token text.
   */
  @Override
  public void serialize(final Object value, final JsonGenerator generator, final SerializerProvider serializers)
    throws IOException
    ,      JsonProcessingException {

    if (value == null) {
      generator.writeNull();
    }
    else {
      final String[] array = (String[])value;
      if (array.length == 1) {
        generator.writeString(array[0]);
      }
      else {
        generator.writeStartArray();
        for (String s : array) {
          generator.writeString(s);
        }
        generator.writeEndArray();
      }
    }
  }
}