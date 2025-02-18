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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Service Extension
    Subsystem   :   Generic REST Library

    File        :   CalendarSerializer.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    CalendarSerializer.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-06  DSteding    First release version
*/

package oracle.hst.platform.rest.marshal;

import java.util.Calendar;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;

import com.fasterxml.jackson.databind.SerializerProvider;

////////////////////////////////////////////////////////////////////////////////
// class CalendarSerializer
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** Serializes a {@link Calendar} object to a REST DateTime string.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class CalendarSerializer extends DateTimeSerializer<Calendar> {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>CalendarSerializer</code> REST DateTime that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public CalendarSerializer() {
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
   ** @param  value              the {@link Calendar} value to serialize
   **                            Must not be <code>null</code>.
   ** @param  generator          the {@link JsonGenerator} used to output
   **                            resulting Json content.
   ** @param  serializers        the {@link SerializerProvider} that can be used
   **                            to get serializers for serializing Objects
   **                            value contains, if any.
   **
   ** @throws IOException        if the {@link JsonGenerator} detects an issue
   **                            to write the token text.
   */
  @Override
  public void serialize(final Calendar value, final JsonGenerator generator, final SerializerProvider serializers)
    throws IOException {

    serialize(generator, value);
  }
}