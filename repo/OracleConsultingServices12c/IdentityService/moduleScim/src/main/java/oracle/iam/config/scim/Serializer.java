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

    System      :   Oracle Identity Service Extension
    Subsystem   :   Generic SCIM Service

    File        :   Serializer.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Serializer.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-07-10  DSteding    First release version
*/

package oracle.iam.config.scim;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonInclude;

import com.fasterxml.jackson.core.JsonFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;

////////////////////////////////////////////////////////////////////////////////
// final class Serializer
// ~~~~~ ~~~~~ ~~~~~~~~~~
/**
 ** This is the class to create meta information for a
 ** <code>SCIM Provisioning Service</code> configuration.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
final class Serializer {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final ObjectMapper objectMapper = initialize();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Serializer</code> backing bean that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private Serializer() {
    // should never be instantiated
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmarshal
  /**
   ** Unmarshall a {@link ContextConfig} from a {@link File}.
   **
   ** @param  file               the destination for the descriptor to read.
   **                            <br>
   **                            Allowed object is {@link File}.
   **
   ** @return                    the {@link ContextConfig} to be unmarshalled.
   **                            <br>
   **                            Possible object is {@link ContextConfig}.
   */
  static ContextConfig unmarshal(final File file) {
    try {
      return objectMapper.readValue(file, ContextConfig.class);
    }
    catch (IOException e) {
      e.printStackTrace();
      return ContextConfig.build();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshal
  /**
   ** Factory method to marshal a {@link ContextConfig} to a {@link File}.
   **
   ** @param  feature            the {@link ContextConfig} to be marshalled.
   **                            <br>
   **                            Allowed object is {@link ContextConfig}.
   ** @param  file               the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **                            <br>
   **                            Allowed object is {@link File}.
   */
  static void marshal(final ContextConfig feature, final File file) {
    try {
      // convert object to JSON string and save into file directly
      objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, feature);
    }
    catch (IOException e) {
      e.printStackTrace();
    }
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