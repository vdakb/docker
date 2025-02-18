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

    File        :   CalendarDeserializer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    CalendarDeserializer.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.scim.utility;

import java.util.Calendar;

import java.text.ParseException;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;

import com.fasterxml.jackson.databind.DeserializationContext;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

////////////////////////////////////////////////////////////////////////////////
// class CalendarDeserializer
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** Deserializes SCIM 2 DateTime values to {@link Calendar} objects.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class CalendarDeserializer extends DateTimeDeserializer<Calendar> {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>CalendarDeserializer</code> SCIM DateTime that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public CalendarDeserializer() {
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
   ** @return                    the deserialized {@link Calendar} value.
   **
   ** @throws IOException            if the {@link JsonParser} detects an issue
   **                                to get the token text.
   ** @throws InvalidFormatException if string parameter does not conform to
   **                                lexical value space defined in XML Schema
   **                                Part 2: Datatypes for xsd:dateTime.
   */
  @Override
  public Calendar deserialize(final JsonParser parser, final DeserializationContext context)
    throws IOException
    ,      InvalidFormatException {

    try {
      return deserializeCalendar(parser);
    }
    catch (ParseException e) {
      throw InvalidFormatException.from(parser, "Expected a field containing a calendar", context, Calendar.class);
    }
  }
}