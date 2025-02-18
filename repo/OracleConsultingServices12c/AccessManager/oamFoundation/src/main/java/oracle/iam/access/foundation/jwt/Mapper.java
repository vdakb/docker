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

    File        :   Mapper.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Mapper.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.access.foundation.jwt;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;

import com.fasterxml.jackson.annotation.JsonInclude;

////////////////////////////////////////////////////////////////////////////////
// class Mapper
// ~~~~~ ~~~~~~
/**
 ** Serialize and de-serialize JSON Web Token header and payload.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Mapper {

  //////////////////////////////////////////////////////////////////////////////
  // static final  attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final ObjectMapper MAPPER = new ObjectMapper();

  static {
    MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL)
    .configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, false)
    .configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false)
    .configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true)
    .configure(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS, true)
    .registerModule(new Module());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Mapper</code> task that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Mapper() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deserialize
  public static <T> T deserialize(final byte[] bytes, final Class<T> type)
    throws InvalidTokenException {

    try {
      return MAPPER.readValue(bytes, type);
    }
    catch (IOException e) {
      throw new InvalidTokenException("The JSON Web Token could not be de-serialized.", e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serialize
  public static byte[] serialize(final Object object)
    throws InvalidTokenException {

    try {
      return MAPPER.writeValueAsBytes(object);
    }
    catch (JsonProcessingException  e) {
      throw new InvalidTokenException("The JSON Web Token could not be de-serialized.", e);
    }
  }
}