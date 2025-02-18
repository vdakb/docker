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

    Copyright © 2020. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Service Extension
    Subsystem   :   Captcha Service Feature

    File        :   Serializer.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Serializer.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.service.captcha.textual;

import java.io.InputStream;
import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonInclude;

import com.fasterxml.jackson.core.JsonFactory;

import com.fasterxml.jackson.core.type.TypeReference;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.List;

////////////////////////////////////////////////////////////////////////////////
// final class Serializer
// ~~~~~ ~~~~~ ~~~~~~~~~~
/**
 ** This is the class to create meta information for the
 ** <code>Captcha Service</code> configuration.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class Serializer {

  //////////////////////////////////////////////////////////////////////////////
  // static final  attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final ObjectMapper objectMapper = initialize();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Serializer</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private Serializer() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   images
  /**
   ** Unmarshall a collection of {@link Captcha.Image}s from an
   ** {@link InputStream}.
   **
   ** @param  stream             the {@link InputStream} to read from.
   **                            <br>
   **                            Allowed object is {@link InputStream}.
   **
   ** @return                    the collection of {@link Captcha.Image}s.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is oof type {@link Captcha.Image}s.
   **
   ** @throws IOException         if an I/O error occurs.
   */
  public static List<String> alphabet(final InputStream stream)
    throws IOException {

    return objectMapper.readValue(stream, new TypeReference<List<oracle.iam.service.captcha.symbol.Captcha>>(){});
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize
  /**
   ** Creates a custom Jackson compatible {@link ObjectMapper}.
   ** <br>
   ** Creating new [@link ObjectMapper} instances are expensive so instances
   ** should be shared if possible.
   ** <br>
   ** This can be used to set the factory used to build new instances of the
   ** object mapper used by the SDK.
   **
   ** @return                    an [@link ObjectMapper} with the correct
   **                            options set for serializing and deserializing
   **                            JSON objects.
   */
  private static ObjectMapper initialize() {
    final ObjectMapper mapper = new ObjectMapper(new JsonFactory());
    // don't serialize POJO nulls as JSON nulls.
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    // only use xsd:dateTime format for dates.
    mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    // do not care about case when deserializing POJOs.
    mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

    return mapper;
  }
}