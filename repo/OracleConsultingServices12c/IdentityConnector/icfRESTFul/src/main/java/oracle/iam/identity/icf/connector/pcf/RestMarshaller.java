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

    File        :   RestMarshaller.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    RestMarshaller.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.pcf;

import java.lang.reflect.Field;

import java.util.Set;
import java.util.List;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;

import oracle.iam.identity.icf.foundation.utility.SchemaUtility;

import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.ConnectorObject;
import org.identityconnectors.framework.common.objects.ConnectorObjectBuilder;
import org.identityconnectors.framework.common.objects.OperationalAttributeInfos;

import oracle.iam.identity.icf.foundation.utility.CollectionUtility;

import oracle.iam.identity.icf.scim.schema.Factory;

import oracle.iam.identity.icf.connector.pcf.rest.schema.Embed;
import oracle.iam.identity.icf.connector.pcf.rest.schema.Metadata;

import oracle.iam.identity.icf.connector.pcf.rest.domain.Result;
import oracle.iam.identity.icf.connector.pcf.rest.domain.Payload;

////////////////////////////////////////////////////////////////////////////////
// class RestMarshaller
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** An interface to transfer PCF REST Resource object to and from Identity
 ** Connector attribute collections.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class RestMarshaller extends Marshaller {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the object class name for a organization entitlement */
  static final String      TENANT_NAME = SchemaUtility.createSpecialName("TENANT");

  /** the object class name for a space entitlement */
  static final String      SPACE_NAME  = SchemaUtility.createSpecialName("SPACE");

  /** the object class name for a organization entitlement */
  static final ObjectClass TENANT      = new ObjectClass(TENANT_NAME);

  /** the object class name for a space entitlement */
  static final ObjectClass SPACE       = new ObjectClass(SPACE_NAME);

  /** the identifier of a resource*/
  static final String      IDENTIFIER  = Metadata.PREFIX + "." + Metadata.ID;

  /** the public name of a resource*/
  static final String      NAME        = Embed.PREFIX + "." + Embed.ID;

  /** the status of an entity */
  static final String      STATUS      = Embed.PREFIX + "." + Embed.STATUS;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor (private)
  /**
   ** Default constructor
   ** <br>
   ** Access modifier private prevents other classes using
   ** "new RestMarshaller()"
   */
  private RestMarshaller() {
    // should never be instantiated
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildTenant
  /**
   ** Factory method to create a new {@link Payload.Tenant} instance and
   ** transfer the specified {@link Set} of {@link Attribute}s to the REST
   ** tenant resource.
   **
   ** @param  attribute          the {@link Set} of {@link Attribute}s to set
   **                            on the REST tenant resource.
   **                            <br>
   **                            Allowed object is a {@link Set} where each
   **                            elemment is of type {@link Attribute}.
   **
   ** @return                    the REST tenant resource populated by the
   **                            {@link Set} of {@link Attribute}s
   **                            <br>
   **                            Possible object is a {@link Payload.Tenant}.
   */
  public static Payload.Tenant buildTenant(final Set<Attribute> attribute) {
    final Payload.Tenant resource = new Payload.Tenant();
    for (Attribute cursor : attribute) {
      if (!CollectionUtility.empty(cursor.getValue())) {
        collect(resource, cursor.getName(), cursor.getValue());
      }
    }
    return resource;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildSpace
  /**
   ** Factory method to create a new {@link Payload.Space} instance and
   ** transfer the specified {@link Set} of {@link Attribute}s to the REST
   ** space resource.
   **
   ** @param  attribute          the {@link Set} of {@link Attribute}s to set
   **                            on the REST space resource.
   **                            <br>
   **                            Allowed object is a {@link Set} where each
   **                            elemment is of type {@link Attribute}.
   **
   ** @return                    the REST space resource populated by the
   **                            {@link Set} of {@link Attribute}s
   **                            <br>
   **                            Possible object is a {@link Payload.Tenant}.
   */
  public static Payload.Space buildSpace(final Set<Attribute> attribute) {
    final Payload.Space resource = new Payload.Space();
    for (Attribute cursor : attribute) {
      if (!CollectionUtility.empty(cursor.getValue())) {
        collect(resource, cursor.getName(), cursor.getValue());
      }
    }
    return resource;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildTenant
  /**
   ** Transforms the data received from the Service Provider and wrapped in the
   ** specified REST {@link Result.Tenant} <code>tenant</code> to the
   ** {@link ConnectorObjectBuilder}.
   **
   ** @param  resource           the specified REST {@link Result.Tenant} to
   **                            transform.
   **                            <br>
   **                            Allowed object is {@link Result.Space}.
   ** @param  filter             the {@link Set} of attribute names requested to
   **                            be set in the {@link ConnectorObjectBuilder}.
   **                            <br>
   **                            If this passed as <code>null</code> all
   **                            registred attributes are returned
   **                            <br>
   **                            Allowed object is {@link Set} where each element
   **                            is of type {@link String}.
   **
   ** @return                    the transformation result wrapped in a
   **                            {@link ConnectorObject}.
   **                            <br>
   **                            Possible object {@link ConnectorObject}.
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
  public static ConnectorObject buildTenant(final Result.Tenant resource, final Set<String> filter)
    throws IllegalArgumentException
    ,      IllegalAccessException {

    final ConnectorObjectBuilder collector = build(TENANT, filter, resource);
    // set the core attributes
    collector.setName(resource.entity().name());
    collector.addAttribute(OperationalAttributeInfos.ENABLE.getName(), resource.entity().status());

    // set the additional attributes driven by the embedded entity
    final Embed             provider   = resource.entity();
    final Collection<Field> properties = Factory.describe(Embed.Tenant.class);
    for (Field field : properties) {
      if (field.isAnnotationPresent(JsonIgnore.class))
        continue;

      // ensure that the field is accessible regardless which modifier the field
      // is assinged to
      field.setAccessible(true);
      final String qualifieName = Embed.PREFIX.concat(".").concat(field.getName());
      if (filter == null || (filter != null && filter.contains(qualifieName))) {
        collector.addAttribute(qualifieName, field.get(provider));
      }
    }
    return collector.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildSpace
  /**
   ** Transforms the data received from the Service Provider and wrapped in the
   ** specified REST {@link Result.Space} <code>space</code> to the
   ** {@link ConnectorObjectBuilder}.
   **
   ** @param  resource           the specified REST {@link Result.Space} to
   **                            transform.
   **                            <br>
   **                            Allowed object is {@link Result.Space}.
   ** @param  filter             the {@link Set} of attribute names requested to
   **                            be set in the {@link ConnectorObjectBuilder}.
   **                            <br>
   **                            If this passed as <code>null</code> all
   **                            registred attributes are returned
   **                            <br>
   **                            Allowed object is {@link Set} where each element
   **                            is of type {@link String}.
   **
   ** @return                    the transformation result wrapped in a
   **                            {@link ConnectorObject}.
   **                            <br>
   **                            Possible object {@link ConnectorObject}.
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
  public static ConnectorObject buildSpace(final Result.Space resource, final Set<String> filter)
    throws IllegalArgumentException
    ,      IllegalAccessException {

    final ConnectorObjectBuilder collector = build(SPACE, filter, resource);
    // set the core attributes
    collector.setName(resource.entity().name());

    // set the additional attributes driven by the embedded entity
    final Embed             provider   = resource.entity();
    final Collection<Field> properties = Factory.describe(Embed.Space.class);
    for (Field field : properties) {
      if (field.isAnnotationPresent(JsonIgnore.class))
        continue;

      // ensure that the field is accessible regardless which modifier the field
      // is assinged to
      field.setAccessible(true);
      final String qualifieName = Embed.PREFIX.concat(".").concat(field.getName());
      if (filter == null || (filter != null && filter.contains(qualifieName))) {
        collector.addAttribute(qualifieName, field.get(provider));
      }
    }
    return collector.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collect
  /**
   ** Converts the specified REST tenant resource to the transfer options
   ** required by the Identity Connector Framework.
   **
   ** @param  resource           the REST tenant resource to manipulate.
   **                            <br>
   **                            Allowed object is {@link Payload.Tenant}.
   ** @param  name               the name of the attribute to manipulate.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link List} of values to transfer.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Object}.
   */
  private static void collect(final Payload.Tenant resource, final String name, final List<Object> values) {
    Object value = values.get(0);
    // only NON-READ-ONLY fields here
    switch (name) {
      case "__NAME__"             :
      case Payload.NAME           : resource.name(String.class.cast(value));
                                    break;
      case "__ENABLE__"           :
      case Payload.STATUS         : resource.status(String.class.cast(value));
                                    break;
      case Payload.BILLING        : resource.billing(Boolean.class.cast(value));
                                    break;
      case Payload.QUOTA_TENANT   : resource.quota(String.class.cast(value));
                                    break;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collect
  /**
   ** Converts the specified REST space resource to the transfer options
   ** required by the Identity Connector Framework.
   **
   ** @param  resource           the REST space resource to manipulate.
   **                            <br>
   **                            Allowed object is {@link Payload.Space}.
   ** @param  name               the name of the attribute to manipulate.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link List} of values to transfer.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Object}.
   */
  private static void collect(final Payload.Space resource, final String name, final List<Object> values) {
    Object value = values.get(0);
    // only NON-READ-ONLY fields here
    switch (name) {
      case "__NAME__"           :
      case Payload.NAME         : resource.name(String.class.cast(value));
                                  break;
      case Payload.TENANT       : resource.tenant(String.class.cast(value));
                                  break;
      case Payload.SECURE_SHELL : resource.secureShell(Boolean.class.cast(value));
                                  break;
      case Payload.QUOTA_SPACE  : resource.quota(String.class.cast(value));
                                  break;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Transforms the data received from the Service Provider to a
   ** {@link ConnectorObject} transfer object.
   **
   ** @param  type               the object class to build.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   ** @param  value              the attribute values obtained from a REST
   **                            response.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element id of type {@link Attribute}.
   ** @param  filter             the {@link Set} of attribute names requested to
   **                            be set in the {@link ConnectorObject} transfer
   **                            object.
   **                            <br>
   **                            If this passed as <code>null</code> all
   **                            registred attributes are returned
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the transformation result wrapped in a
   **                            {@link ConnectorObject} transfer object.
   **                            <br>
   **                            Possible object {@link ConnectorObjectBuilder}.
   **
   ** @throws IllegalAccessException   if the <code>Field</code>s of the space
   **                                  resource is enforcing Java language
   **                                  access control and the underlying field
   **                                  is inaccessible.
   ** @throws IllegalArgumentException if the specified object is not an
   **                                  instance of the class or interface
   **                                  declaring the underlying field (or a
   **                                  subclass or implementor thereof).
   */
  private static <T extends Result> ConnectorObjectBuilder build(final ObjectClass type, final Set<String> filter, final T resource)
    throws IllegalAccessException
    ,      IllegalArgumentException {

    final ConnectorObjectBuilder collector = new ConnectorObjectBuilder().setObjectClass(type);
    collector.setUid(resource.metadata().guid());
    final Metadata          provider   = resource.metadata();
    final Collection<Field> properties = Factory.describe(Metadata.class);
    for (Field field : properties) {
      if (field.isAnnotationPresent(JsonIgnore.class) || Metadata.ID.equals(field.getName()))
        continue;

      // ensure that the field is accessible regardless which modifier the field
      // is assinged to
      field.setAccessible(true);
      final String qualifieName = Metadata.PREFIX.concat(".").concat(field.getName());
      if (filter == null || (filter != null && filter.contains(qualifieName))) {
        collector.addAttribute(qualifieName, field.get(provider));
      }
    }
    return collector;
  }
}