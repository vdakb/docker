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

    File        :   MapperFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    MapperFactory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.scim.utility;

import java.util.Map;
import java.util.Date;
import java.util.Calendar;
import java.util.Collections;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonGenerator;

import com.fasterxml.jackson.annotation.JsonInclude;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;

import com.fasterxml.jackson.databind.module.SimpleModule;

////////////////////////////////////////////////////////////////////////////////
// class MapperFactory
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** Class used to customize the object mapper that is used by the SCIM 2 SDK.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class MapperFactory {

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  private static Map<MapperFeature, Boolean>          mapper          = Collections.<MapperFeature, Boolean>emptyMap();
  private static Map<JsonParser.Feature, Boolean>     parser          = Collections.<JsonParser.Feature, Boolean>emptyMap();
  private static Map<JsonGenerator.Feature, Boolean>  generator       = Collections.<JsonGenerator.Feature, Boolean>emptyMap();
  private static Map<SerializationFeature, Boolean>   serialization   = Collections.<SerializationFeature, Boolean>emptyMap();
  private static Map<DeserializationFeature, Boolean> deserialization = Collections.<DeserializationFeature, Boolean>emptyMap();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>MapperFactory</code> SCIM Object Mapper that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public MapperFactory() {
	  // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mapper
  /**
   ** Sets custom mapper features for any JSON ObjectMapper that is used and
   ** returned by the SCIM 2 SDK.
   ** <br>
   ** This class should be used to configure any object mapper customizations
   ** needed prior to using any method in the SchemaSupport class.
   **
   ** @param  features           the collection of custom mapper feature
   **                            settings.
   **
   ** @return                    the {@link MapperFactory} to allow method
   **                            chaining.
   **                            Possible object {@link MapperFactory}.
   */
  public MapperFactory mapper(final Map<MapperFeature, Boolean> features) {
    mapper = features;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parser
  /**
   ** Sets custom parser features for any JSON ObjectMapper that is used and
   ** returned by the SCIM 2 SDK.
   ** <br>
   ** This class should be used to configure any object mapper customizations
   ** needed prior to using any method in the SchemaSupport class.
   **
   ** @param  features           the collection of custom serialization feature
   **
   ** @return                    the {@link MapperFactory} to allow method
   **                            chaining.
   **                            Possible object {@link MapperFactory}.
   */
  public MapperFactory parser(final Map<JsonParser.Feature, Boolean> features) {
    parser = features;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generator
  /**
   ** Sets custom generator features for any JSON ObjectMapper that is used
   ** and returned by the SCIM 2 SDK.
   ** <br>
   ** This class should be used to configure any object mapper customizations
   ** needed prior to using any method in the SchemaSupport class.
   **
   ** @param  features           the collection of custom serialization feature
   **
   ** @return                    the {@link MapperFactory} to allow method
   **                            chaining.
   **                            Possible object {@link MapperFactory}.
   */
  public MapperFactory generator(final Map<JsonGenerator.Feature, Boolean> features) {
    generator = features;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serialization
  /**
   ** Sets custom serialization features for any JSON ObjectMapper that is used
   ** and returned by the SCIM 2 SDK.
   ** <br>
   ** This class should be used to configure any object mapper customizations
   ** needed prior to using any method in the SchemaSupport class.
   **
   ** @param  features           the collection of custom serialization feature
   **
   ** @return                    the {@link MapperFactory} to allow method
   **                            chaining.
   **                            Possible object {@link MapperFactory}.
   */
  public MapperFactory serialization(final Map<SerializationFeature, Boolean> features) {
    serialization = features;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deserialization
  /**
   ** Sets custom deserialization features for any JSON ObjectMapper that is
   ** used and returned by the SCIM 2 SDK.
   ** <br>
   ** This class should be used to configure any object mapper customizations
   ** needed prior to using any method in the SchemaSupport class.
   **
   ** @param  features           the collection of custom deserialization
   **                            feature settings.
   **
   ** @return                    the {@link MapperFactory} to allow method
   **                            chaining.
   **                            Possible object {@link MapperFactory}.
   */
  public MapperFactory deserialization(final Map<DeserializationFeature, Boolean> features) {
    deserialization = features;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectMapper
  /**
   ** Creates a custom SCIM compatible Jackson [@link ObjectMapper}.
   ** Creating new [@link ObjectMapper} instances are expensive so instances
   ** should be shared if possible.
   ** <br>
   ** This can be used to set the factory used to build new instances of the
   ** object mapper used by the SCIM 2 SDK.
   **
   ** @return                    an [@link ObjectMapper} with the correct
   **                            options set for serializing and deserializing
   **                            SCIM JSON objects.
   */
  public static ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper(new ScimFactory());

    // Don't serialize POJO nulls as JSON nulls.
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

    // Only use xsd:dateTime format for dates.
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    SimpleModule dateTimeModule = new SimpleModule();
    dateTimeModule.addSerializer(Calendar.class,   new CalendarSerializer());
    dateTimeModule.addDeserializer(Calendar.class, new CalendarDeserializer());
    dateTimeModule.addSerializer(Date.class,       new DateSerializer());
    dateTimeModule.addDeserializer(Date.class,     new DateDeserializer());
    objectMapper.registerModule(dateTimeModule);

    // Do not care about case when de-serializing POJOs.
    objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

    // Use the case-insensitive JsonNodes.
    objectMapper.setNodeFactory(new ScimNodeFactory());

    for (DeserializationFeature feature : deserialization.keySet()) {
      objectMapper.configure(feature, deserialization.get(feature));
    }

    for (JsonGenerator.Feature feature : generator.keySet()) {
      objectMapper.configure(feature, generator.get(feature));
    }

    for (JsonParser.Feature feature : parser.keySet()) {
      objectMapper.configure(feature, parser.get(feature));
    }

    for (MapperFeature feature : mapper.keySet()) {
      objectMapper.configure(feature, mapper.get(feature));
    }

    for (SerializationFeature feature : serialization.keySet()) {
      objectMapper.configure(feature, serialization.get(feature));
    }
    return objectMapper;
  }
}