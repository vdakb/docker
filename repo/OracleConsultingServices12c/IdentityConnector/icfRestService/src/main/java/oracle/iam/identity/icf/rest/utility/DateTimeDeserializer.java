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

    File        :   CalendarDeserializer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    CalendarDeserializer.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.rest.utility;

import java.util.Date;
import java.util.Calendar;

import java.text.ParseException;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;

import com.fasterxml.jackson.databind.JsonDeserializer;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import oracle.iam.identity.icf.foundation.utility.DateUtility;

////////////////////////////////////////////////////////////////////////////////
// abstract class DateTimeDeserializer
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** Deserializes REST DateTime values to {@link Calendar} objects.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class DateTimeDeserializer<T> extends JsonDeserializer<T> {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>CalendarDeserializer</code> REST DateTime that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected DateTimeDeserializer() {
	  // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by funtionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deserializeDate
  /**
   ** Converts the string argument into a {@link Date} value.
   **
   ** @param  parser             the {@link JsonParser} used for reading JSON
   **                            content.
   **                            <br>
   **                            Allowed object is {@link JsonParser}.
   **
   ** @return                    a {@link Date} object represented by the string
   **                            argument.
   **                            <br>
   **                            Possible object is {@link Date}.
   **
   ** @throws IOException            if the {@link JsonParser} detects an issue
   **                                to get the token text.
   ** @throws InvalidFormatException if string parameter does not conform to
   **                                lexical value space defined in XML Schema
   **                                Part 2: Datatypes for xsd:dateTime.
   ** @throws ParseException         if a date/time component is out of range or
   **                                cannot be parsed.
   */
  protected Date deserializeDate(final JsonParser parser)
    throws IOException
    ,      InvalidFormatException
    ,      ParseException {

    return deserializeCalendar(parser).getTime();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deserializeCalendar
  /**
   ** Converts the string argument into a {@link Calendar} value.
   **
   ** @param  parser             the {@link JsonParser} used for reading JSON
   **                            content.
   **                            <br>
   **                            Allowed object is {@link JsonParser}.
   **
   ** @return                    a {@link Calendar} object represented by the
   **                            string argument.
   **                            <br>
   **                            Possible object is {@link Calendar}.
   **
   ** @throws IOException            if the {@link JsonParser} detects an issue
   **                                to get the token text.
   ** @throws InvalidFormatException if string parameter does not conform to
   **                                lexical value space defined in XML Schema
   **                                Part 2: Datatypes for xsd:dateTime.
   ** @throws ParseException         if a date/time component is out of range or
   **                                cannot be parsed.
   */
  protected Calendar deserializeCalendar(final JsonParser parser)
    throws IOException
    ,      InvalidFormatException
    ,      ParseException {

    final String dateTime = parser.getText();
    try {
      return DateUtility.parse(dateTime);
    }
    catch (IllegalArgumentException e) {
      throw new InvalidFormatException(parser, "Unable to deserialize value", dateTime, Calendar.class);
    }
  }
}