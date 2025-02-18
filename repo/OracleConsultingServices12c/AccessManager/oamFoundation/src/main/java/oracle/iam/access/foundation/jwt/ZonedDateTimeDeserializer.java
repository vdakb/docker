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

    Copyright © 2017. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Authentication Plug-In Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   ZonedDateTimeDeserializer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ZonedDateTimeDeserializer.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.access.foundation.jwt;

import java.io.IOException;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.core.JsonParser;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;

import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;

////////////////////////////////////////////////////////////////////////////////
// class ZonedDateTimeDeserializer
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Jackson de-serializer for the ZonedDateTime class.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ZonedDateTimeDeserializer extends StdScalarDeserializer<ZonedDateTime> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:2356788937272081554")
  private static final long serialVersionUID = -2097906473591149814L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ZonedDateTimeDeserializer</code> task that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ZonedDateTimeDeserializer() {
    // ensure inheritance
    super(Long.TYPE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deserialize (JsonDeserializer)
  @Override
  public ZonedDateTime deserialize(final JsonParser parser, final DeserializationContext context)
    throws IOException {

    final JsonToken t = parser.getCurrentToken();
    long value;
    if (t == JsonToken.VALUE_NUMBER_INT || t == JsonToken.VALUE_NUMBER_FLOAT) {
      value = parser.getLongValue();
    }
    else if (t == JsonToken.VALUE_STRING) {
      final String text = parser.getText().trim();
      if (text.length() == 0) {
        return null;
      }

      try {
        value = Long.parseLong(text);
      }
      catch (NumberFormatException e) {
        throw context.mappingException(handledType());
      }
    }
    else {
      throw context.mappingException(handledType());
    }
    return Instant.ofEpochSecond(value).atZone(ZoneOffset.UTC);
  }
}
