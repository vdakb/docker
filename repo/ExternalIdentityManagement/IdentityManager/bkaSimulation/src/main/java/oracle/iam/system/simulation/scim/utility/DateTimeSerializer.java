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

    File        :   DateTimeSerializer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DateTimeSerializer.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.scim.utility;

import java.util.Date;
import java.util.Calendar;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;

import com.fasterxml.jackson.databind.JsonSerializer;

import oracle.hst.foundation.utility.DateUtility;

////////////////////////////////////////////////////////////////////////////////
// abstract class DateTimeSerializer
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** Serializes a DateTime object to a SCIM 2 DateTime string.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class DateTimeSerializer<T> extends JsonSerializer<T> {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DateTimeSerializer</code> SCIM DateTime that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected DateTimeSerializer() {
	  // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by funtionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serialize
  /**
   ** Method that can be called to ask implementation to serialize values of
   ** type this serializer handles.
   **
   ** @param  generator          the {@link JsonGenerator} used to output
   **                            resulting Json content.
   ** @param  value              the {@link Calendar} value to serialize
   **                            Must not be <code>null</code>.
   **
   ** @throws IOException        if the {@link JsonGenerator} detects an issue
   **                            to write the token text.
   */
  protected void serialize(final JsonGenerator generator, final Calendar value)
    throws IOException {

    serialize(generator, value.getTime());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serialize
  /**
   ** Method that can be called to ask implementation to serialize values of
   ** type this serializer handles.
   **
   ** @param  generator          the {@link JsonGenerator} used to output
   **                            resulting Json content.
   ** @param  value              the {@link Date} value to serialize
   **                            Must not be <code>null</code>.
   **
   ** @throws IOException        if the {@link JsonGenerator} detects an issue
   **                            to write the token text.
   */
  protected void serialize(final JsonGenerator generator, final Date value)
    throws IOException {

    final Calendar calendar = Calendar.getInstance();
    calendar.setTime(value);
    generator.writeString(DateUtility.formatDate(calendar, DateUtility.XML8601_ZULU));
  }
}