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

    File        :   DateTimeDeserializer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DateTimeDeserializer.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.scim.utility;

import java.util.Date;
import java.util.Calendar;

import java.text.ParseException;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;

import com.fasterxml.jackson.databind.JsonDeserializer;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import oracle.hst.foundation.utility.DateUtility;

////////////////////////////////////////////////////////////////////////////////
// abstract class DateTimeDeserializer
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** Deserializes SCIM 2 DateTime values to {@link Calendar} objects.
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
   ** Constructs a <code>DateTimeDeserializer</code> SCIM DateTime that allows
   ** use as a JavaBean.
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
   **
   ** @return                    a {@link Date} object represented by the string
   **                            argument.
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
   **
   ** @return                    a {@link Calendar} object represented by the
   **                            string argument.
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
      return DateUtility.parseDate(dateTime, DateUtility.XML8601_ZULU);
    }
    catch (IllegalArgumentException e) {
      throw new InvalidFormatException(parser, "Unable to deserialize value", dateTime, Calendar.class);
    }
  }
}