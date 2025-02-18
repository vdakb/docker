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
    Subsystem   :   Pivotal Cloud Foundry Connector

    File        :   Marshaller.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Marshaller.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.pcf;

import java.lang.reflect.Field;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.EmbeddedObject;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.EmbeddedObjectBuilder;
import org.identityconnectors.framework.common.objects.ConnectorObjectBuilder;

import oracle.iam.identity.icf.foundation.object.Pair;

import oracle.iam.identity.icf.foundation.utility.StringUtility;
import oracle.iam.identity.icf.foundation.utility.CollectionUtility;

import oracle.iam.identity.icf.scim.schema.Factory;

////////////////////////////////////////////////////////////////////////////////
// class Marshaller
// ~~~~~ ~~~~~~~~~~
/**
 ** An interface to transfer PCF Resource object to and from Identity
 ** Connector attribute collections.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Marshaller {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor (protected)
  /**
   ** Default constructor
   ** <br>
   ** Access modifier protected prevents other classes using "new Marshaller()"
   */
  protected Marshaller() {
    // should never be instantiated
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collect
  /**
   ** Converts the specified PCF resource attribute to the transfer options
   ** required by the Identity Connector Framework.
   **
   ** @param  <T>                the resource type passed in
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  collector          the {@link ConnectorObjectBuilder} to collected
   **                            the attributes so far.
   **                            <br>
   **                            Allowed object is
   **                            {@link ConnectorObjectBuilder}.
   ** @param  filter             the {@link Set} of attribute names requested to
   **                            be set in the {@link ConnectorObjectBuilder}.
   **                            <br>
   **                            If this passed as <code>null</code> all
   **                            registred attributes are returned
   **                            <br>
   **                            Allowed object is {@link Set} where each element
   **                            is of type {@link String}.
   ** @param  layer              the resource atribute prefix of an complex
   **                            attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  resource           the PCF resource providing the data to obtain.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @throws IllegalAccessException   if this <code>Field</code> of the
   **                                  resource is enforcing Java language
   **                                  access control and the underlying field
   **                                  is inaccessible.
   ** @throws IllegalArgumentException if the specified object is not an
   **                                  instance of the class or interface
   **                                  declaring the underlying field (or a
   **                                  subclass or implementor thereof).
   */
  protected static <T> void collect(final ConnectorObjectBuilder collector, final Set<String> filter, final String layer, final T resource)
    throws IllegalArgumentException
    ,      IllegalAccessException {

    // ensure that we are on the rigth track for a resource entiyt to collect
    if (resource != null) {
      // get the resource descriptor
      final Collection<Field> properties = Factory.describe(resource.getClass());
      for (Field property : properties) {
        // skip any property that have to be ignored due to it is never
        // marshalled
        if (property.isAnnotationPresent(JsonIgnore.class))
          continue;

        final String qualifieName = layer.concat(".").concat(property.getName());
        if (filter == null || (filter != null && filter.contains(qualifieName))) {
          // ensure that the class property is accessible regardless which
          // modifier the field is assinged to
          property.setAccessible(true);
          final Object value = property.get(resource);
          if (!empty(value)) {
            collector.addAttribute(buildAttribute(property.get(resource), qualifieName, property.getType()));
          }
        }
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collect
  /**
   ** Converts the specified PCF resource attribute to the transfer options
   ** required by the Identity Connector Framework.
   **
   ** @param  collector          the {@link ConnectorObjectBuilder} to collect
   **                            the attributes.
   **                            <br>
   **                            Allowed object is
   **                            {@link ConnectorObjectBuilder}.
   ** @param  type               the ICF object class.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   ** @param  resource           the PCF resource identifiers providing the data
   **                            to obtain.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link String}.
   */
  protected static void collect(final ConnectorObjectBuilder collector, final ObjectClass type, final List<String> resource) {
    if (!CollectionUtility.empty(resource)) {
      final List<EmbeddedObject> permission = new ArrayList<EmbeddedObject>();
      // a lambda function can avoid this loop but performance benchmarks had
      // shown saying that the overhead of Stream.forEach() compared to an
      // ordinary for loop is so significant in general that using it by default
      // will just pile up a lot of useless CPU cycles across the application 
      for (String identifier : resource) {
        permission.add(new EmbeddedObjectBuilder().setObjectClass(type).addAttribute(Uid.NAME, identifier).build());
      }
      // add the collection to the connector object builder
      collector.addAttribute(type.getObjectClassValue(), permission);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collectScoped
  /**
   ** Converts the specified PCF resource attribute to the transfer options
   ** required by the Identity Connector Framework.
   **
   ** @param  collector          the {@link ConnectorObjectBuilder} to collect
   **                            the attributes.
   **                            <br>
   **                            Allowed object is
   **                            {@link ConnectorObjectBuilder}.
   ** @param  type               the ICF object class.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   ** @param  resource           the PCF resource identifiers providing the data
   **                            to obtain.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Pair}.
   */
  protected static void collectScoped(final ConnectorObjectBuilder collector, final ObjectClass type, final List<Pair<String, List<String>>> resource) {
    if (!CollectionUtility.empty(resource)) {
      final List<EmbeddedObject> permission = new ArrayList<EmbeddedObject>();
      for (Pair<String, List<String>> i : resource) {
        for (String j : i.getValue()) {
          permission.add(new EmbeddedObjectBuilder().setObjectClass(type).addAttribute(Name.NAME, i.getKey()).addAttribute(Uid.NAME, j).build());
        }
      }
      // add the collection to the connector object builder
      collector.addAttribute(type.getObjectClassValue(), permission);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildAttribute
  protected static Attribute buildAttribute(final Object value, final String name, final Class<?> clazz) {
    final AttributeBuilder builder = new AttributeBuilder();
    if (value != null) {
      if (clazz == boolean.class || clazz == Boolean.class) {
        builder.addValue(Boolean.class.cast(value));
      }
      else {
        builder.addValue(value.toString());
      }
    }
    if (name != null) {
      builder.setName(name);
    }
    return builder.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   empty
  /**
   ** Checks if the specified {@link Object} is empty.
   **
   ** @param  value              the {@link Object} to check for emptyness.
   **
   ** @return                    <code>true</code> if the <code>value</code>
   **                            contains no elements.
   */
  protected static boolean empty(final Object value) {
    return value == null || (value instanceof Collection ? CollectionUtility.empty((Collection<?>)value) : false) || (value instanceof String ? StringUtility.blank(String.class.cast(value)) : false);
  }
}