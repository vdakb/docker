/*
    Oracle Deutschland BV & Co. KG

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

    Copyright © 2024. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Grafana Connector

    File        :   Marshaller.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@icloud.com

    Purpose     :   This file implements the class
                    Service.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2024-03-22  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.grafana.schema;

import java.lang.reflect.Field;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.identityconnectors.common.security.GuardedString;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Schema;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.SchemaBuilder;
import org.identityconnectors.framework.common.objects.EmbeddedObject;
import org.identityconnectors.framework.common.objects.ObjectClassInfo;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.EmbeddedObjectBuilder;
import org.identityconnectors.framework.common.objects.OperationalAttributes;
import org.identityconnectors.framework.common.objects.ConnectorObjectBuilder;

import org.identityconnectors.framework.spi.operations.TestOp;
import org.identityconnectors.framework.spi.operations.SchemaOp;
import org.identityconnectors.framework.spi.operations.SearchOp;
import org.identityconnectors.framework.spi.operations.CreateOp;
import org.identityconnectors.framework.spi.operations.UpdateOp;
import org.identityconnectors.framework.spi.operations.DeleteOp;
import org.identityconnectors.framework.spi.operations.ResolveUsernameOp;
import org.identityconnectors.framework.spi.operations.UpdateAttributeValuesOp;

import oracle.iam.identity.icf.schema.Factory;

import oracle.iam.identity.icf.foundation.utility.StringUtility;
import oracle.iam.identity.icf.foundation.utility.CollectionUtility;

import oracle.iam.identity.icf.connector.AbstractConnector;

import static oracle.iam.identity.icf.schema.Attribute.STATUS;
import static oracle.iam.identity.icf.schema.Attribute.UNIQUE;
import static oracle.iam.identity.icf.schema.Attribute.PASSWORD;
import static oracle.iam.identity.icf.schema.Attribute.IDENTIFIER;

import oracle.iam.identity.icf.foundation.utility.CredentialAccessor;

import static oracle.iam.identity.icf.connector.grafana.schema.Entity.NAME;
import static oracle.iam.identity.icf.connector.grafana.schema.Entity.EMAIL;
import static oracle.iam.identity.icf.connector.grafana.schema.Entity.ADMIN;
import static oracle.iam.identity.icf.connector.grafana.schema.Entity.AVATAR;
import static oracle.iam.identity.icf.connector.grafana.schema.Entity.ORGANIZATION;

////////////////////////////////////////////////////////////////////////////////
// final class Service
// ~~~~~ ~~~~~ ~~~~~~~
/**
 ** An interface to describe and transform Grafana Resource object to and from
 ** Identity Connector attribute collections.
 **
 ** @author  dieter.steding@icloud.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class Service {

  /**
   ** The one and only instance of the <code>Service</code>.
   ** <p>
   ** Singleton Pattern
   */
  public static final Service instance    = new Service();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a default Keyclaok <code>Service</code> connector that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new Service()" and enforces use of the public method below.
   */
  private Service() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   schema
  /**
   ** Describes the types of objects this Connector supports upon request.
   ** <p>
   ** The schema is cached over the lifetime of this connector.
   ** <p>
   ** Builds schema meta-data from configuration without obtaining meta-data
   ** from target environment.
   ** <p>
   ** This method is considered an operation since determining supported objects
   ** may require configuration information and allows this determination to be
   ** dynamic.
   ** <p>
   ** The special [@link Uid} attribute should never appear in the schema, as it
   ** is not a real attribute of an object, rather a reference to it.
   ** <br>
   ** If the resource object-class has a writable unique id attribute that is
   ** different than its name, then the schema should contain a
   ** resource-specific attribute that represents this unique id.
   ** <br>
   ** For example, a Unix account object might contain unix <code>uid</code>.
   **
   ** @param  clazz              the connector {@link Class} for which the
   **                            schema is built.
   **                            <br>
   **                            Allowed object is {@link Class} for type
   **                            {@link AbstractConnector}.
   **
   ** @return                    the {@link Schema}.
   **                            <br>
   **                            Possible object is {@link Schema}.
   */
  public final Schema schema(final Class<? extends AbstractConnector> clazz) {
    final SchemaBuilder   builder = new SchemaBuilder(clazz);
    final ObjectClassInfo user         = Factory.defineObjectClass(builder, User.class);
    final ObjectClassInfo role         = Factory.defineObjectClass(builder, Role.class);
    final ObjectClassInfo team         = Factory.defineObjectClass(builder, Team.class);
    final ObjectClassInfo organization = Factory.defineObjectClass(builder, Organization.class);
    builder.defineObjectClass(user);
    builder.defineObjectClass(role);
    builder.defineObjectClass(team);
    builder.defineObjectClass(organization);

    // how stupid ICF is build shows the code below
    // instead of providing a fine grained api to take control how a certain
    // object class is presented in the populated schema you need to add the
    // object class generated completely at first. The ugly frameork will add
    // than all possible operation to the object class that can it find, with
    // the result that we need to remove all operation later and allow only
    // those that the connector is realy supporting
    // what kind of bull shit is that, that keeps the garbage collector busy
    // only
    builder.clearSupportedObjectClassesByOperation();
    builder.addSupportedObjectClass(TestOp.class,            user);
    builder.addSupportedObjectClass(SchemaOp.class,          user);
    builder.addSupportedObjectClass(SearchOp.class,          user);
    builder.addSupportedObjectClass(CreateOp.class,          user);
    builder.addSupportedObjectClass(DeleteOp.class,          user);
    builder.addSupportedObjectClass(UpdateOp.class,          user);
    builder.addSupportedObjectClass(ResolveUsernameOp.class, user);

    builder.addSupportedObjectClass(SearchOp.class,                role);
    builder.addSupportedObjectClass(ResolveUsernameOp.class,       role);
    builder.addSupportedObjectClass(UpdateAttributeValuesOp.class, role);

    builder.addSupportedObjectClass(SearchOp.class,                team);
    builder.addSupportedObjectClass(ResolveUsernameOp.class,       team);
    builder.addSupportedObjectClass(UpdateAttributeValuesOp.class, team);

    builder.addSupportedObjectClass(SearchOp.class,                organization);
    builder.addSupportedObjectClass(ResolveUsernameOp.class,       organization);
    builder.addSupportedObjectClass(UpdateAttributeValuesOp.class, organization);
    return builder.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transformUser
  /**
   ** Factory method to create a new {@link User} instance and transform the
   ** specified {@link Set} of {@link Attribute}s to the Grafana user
   ** resource.
   **
   ** @param  attribute        the {@link Set} of {@link Attribute}s to set
   **                          on the Grafana user resource.
   **                          <br>
   **                          Allowed object is a {@link Set} where each
   **                          elemment is of type {@link Attribute}.
   **
   ** @return                  the Grafana user resource populated by the
   **                          {@link Set} of {@link Attribute}s
   **                          <br>
   **                          Possible object is a {@link User}.
   */
  public static User transformUser(final Set<Attribute> attribute) {
    final User resource = new User();
    for (Attribute cursor : attribute) {
      if (!CollectionUtility.empty(cursor.getValue())) {
        transform(resource, cursor);
      }
    }
    return resource;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transformTeam
  /**
   ** Factory method to create a new {@link Team} instance and transform the
   ** specified {@link Set} of {@link Attribute}s to the Grafana team
   ** resource.
   **
   ** @param  attribute        the {@link Set} of {@link Attribute}s to set
   **                          on the Grafana team resource.
   **                          <br>
   **                          Allowed object is a {@link Set} where each
   **                          elemment is of type {@link Attribute}.
   **
   ** @return                  the Grafana team resource populated by the
   **                          {@link Set} of {@link Attribute}s
   **                          <br>
   **                          Possible object is a {@link Team}.
   */
  public static Team transformTeam(final Set<Attribute> attribute) {
    final Team resource = new Team();
    for (Attribute cursor : attribute) {
      if (!CollectionUtility.empty(cursor.getValue())) {
        transform(resource, cursor);
      }
    }
    return resource;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transformOrganization
  /**
   ** Factory method to create a new {@link Organization} instance and transform
   ** the specified {@link Set} of {@link Attribute}s to the Grafana team
   ** resource.
   **
   ** @param  attribute        the {@link Set} of {@link Attribute}s to set
   **                          on the Grafana team resource.
   **                          <br>
   **                          Allowed object is a {@link Set} where each
   **                          elemment is of type {@link Attribute}.
   **
   ** @return                  the Grafana team resource populated by the
   **                          {@link Set} of {@link Attribute}s
   **                          <br>
   **                          Possible object is a {@link Organization}.
   */
  public static Organization transformOrganization(final Set<Attribute> attribute) {
    final Organization resource = new Organization();
    for (Attribute cursor : attribute) {
      if (!CollectionUtility.empty(cursor.getValue())) {
        transform(resource, cursor);
      }
    }
    return resource;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transform
  /**
   ** Converts the specified user resource to the transfer options required by
   ** the Identity Connector Framework.
   **
   ** @param  collector          the {@link ConnectorObjectBuilder} to collect
   **                            the attributes.
   **                            <br>
   **                            Allowed object is
   **                            {@link ConnectorObjectBuilder}.
   ** @param  resource           the user resource to transform.
   **                            <br>
   **                            Allowed object is {@link User}.
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
  @SuppressWarnings("unchecked")
  public static void transform(final ConnectorObjectBuilder collector, final User resource)
    throws IllegalAccessException
    ,      IllegalArgumentException {

    final Collection<Field> properties = Factory.describe(User.class);
    for (Field field : properties) {
      // only attributes that were declared in the schema are in scope
      if (field.isAnnotationPresent(oracle.iam.identity.icf.schema.Attribute.class)) {
        // ensure that the field is accessible regardless which modifier the
        // field is assinged to
        field.setAccessible(true);
        final String name  = field.getName();
        final Object value = field.get(resource);
        switch (name) {
          case "role"          : // transform multi-valued role attributes to
                                 // embebbed collection
                                 transformRole(collector, (List<Role>)value);
                                 break;
          case "team"          : // transform multi-valued team attributes to
                                 // embebbed collection
                                 transformTeam(collector, (List<Team>)value);
                                 break;
          case "organization"  : // transform multi-valued organization
                                 // attributes to embebbed collection
                                 transformOrganization(collector, (List<Organization>)value);
                                 break;
          case STATUS          : collector.addAttribute(buildAttribute(OperationalAttributes.ENABLE_NAME, value, field.getType()));
                                 break;
          default :              collector.addAttribute(buildAttribute(name, value, field.getType()));
        }
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transform
  /**
   ** Converts the specified role resource to the transfer options required by
   ** the Identity Connector Framework.
   **
   ** @param  collector          the {@link ConnectorObjectBuilder} to collect
   **                            the attributes.
   **                            <br>
   **                            Allowed object is
   **                            {@link ConnectorObjectBuilder}.
   ** @param  resource           the role resource to transform.
   **                            <br>
   **                            Allowed object is {@link Role}.
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
  public static void transform(final ConnectorObjectBuilder collector, final Role resource)
    throws IllegalAccessException
    ,      IllegalArgumentException {

    final Collection<Field> properties = Factory.describe(Role.class);
    for (Field field : properties) {
      // ensure that the field is accessible regardless which modifier the
      // field is assinged to
      field.setAccessible(true);
      final Object value = field.get(resource);
      if (!field.isAnnotationPresent(JsonIgnore.class) && !empty(value)) {
        collector.addAttribute(buildAttribute(field.getName(), field.get(resource), String.class));
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transform
  /**
   ** Converts the specified team resource to the transfer options required by
   ** the Identity Connector Framework.
   **
   ** @param  collector          the {@link ConnectorObjectBuilder} to collect
   **                            the attributes.
   **                            <br>
   **                            Allowed object is
   **                            {@link ConnectorObjectBuilder}.
   ** @param  resource           the team resource to transform.
   **                            <br>
   **                            Allowed object is {@link Team}.
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
  public static void transform(final ConnectorObjectBuilder collector, final Team resource)
    throws IllegalAccessException
    ,      IllegalArgumentException {

    final Collection<Field> properties = Factory.describe(Team.class);
    for (Field field : properties) {
      // ensure that the field is accessible regardless which modifier the
      // field is assinged to
      field.setAccessible(true);
      final Object value = field.get(resource);
      if (!field.isAnnotationPresent(JsonIgnore.class) && !empty(value)) {
        collector.addAttribute(buildAttribute(field.getName(), field.get(resource), String.class));
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transform
  /**
   ** Converts the specified organization resource to the transfer options
   ** required by the Identity Connector Framework.
   **
   ** @param  collector          the {@link ConnectorObjectBuilder} to collect
   **                            the attributes.
   **                            <br>
   **                            Allowed object is
   **                            {@link ConnectorObjectBuilder}.
   ** @param  resource           the organization resource to transform.
   **                            <br>
   **                            Allowed object is {@link Organization}.
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
  public static void transform(final ConnectorObjectBuilder collector, final Organization resource)
    throws IllegalAccessException
    ,      IllegalArgumentException {

    final Collection<Field> properties = Factory.describe(Organization.class);
    for (Field field : properties) {
      // ensure that the field is accessible regardless which modifier the
      // field is assinged to
      field.setAccessible(true);
      final Object value = field.get(resource);
      if (!field.isAnnotationPresent(JsonIgnore.class) && !empty(value)) {
        collector.addAttribute(buildAttribute(field.getName(), field.get(resource), String.class));
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transform
  /**
   ** Converts the specified user resource to the transfer options required by
   ** the Grafana REST API.
   **
   ** @param  entity             the Grafana user resource entity to collect the
   **                            attributes.
   **                            <br>
   **                            Allowed object is {@link User}.
   ** @param  attribute          the {@link Attribute} of values to transform.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   */
  private static void transform(final User entity, final Attribute attribute) {
    final String name  = attribute.getName();
    final Object value = attribute.getValue().get(0);
    // transform single value attributes
    switch (name) {
      case IDENTIFIER   : entity.id(String.class.cast(value));
                          break;
      case STATUS       : entity.disabled(!Boolean.class.cast(value));
                          break;
      case UNIQUE       : entity.login(String.class.cast(value));
                          break;
      case PASSWORD     : entity.password(CredentialAccessor.string(GuardedString.class.cast(value)));
                          break;
      case NAME         : entity.displayName(String.class.cast(value));
                          break;
      case EMAIL        : entity.email(String.class.cast(value));
                          break;
      case ADMIN        : entity.administrator(Boolean.class.cast(value));
                          break;
      case AVATAR       : entity.avatar(String.class.cast(value));
                          break;
      case ORGANIZATION : entity.organization(CollectionUtility.list(Organization.build(String.class.cast(value), "Admin")));
                          break;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transform
  /**
   ** Converts the specified team resource to the transfer options required by
   ** the Grafana REST API.
   **
   ** @param  entity             the Grafana team resource entity to collect
   **                            the attributes.
   **                            <br>
   **                            Allowed object is {@link Team}.
   ** @param  attribute          the {@link Attribute} of values to transform.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   */
  private static void transform(final Team entity, final Attribute attribute) {
    final String name  = attribute.getName();
    final Object value = attribute.getValue().get(0);
    // transform single value attributes
    switch (name) {
      case IDENTIFIER : entity.id(String.class.cast(value));
                        break;
      case UNIQUE     : entity.name(String.class.cast(value));
                        break;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transform
  /**
   ** Converts the specified organization resource to the transfer options
   ** required by the Grafana REST API.
   **
   ** @param  entity             the Grafana organization resource entity to
   **                            collect the attributes.
   **                            <br>
   **                            Allowed object is {@link Organization}.
   ** @param  attribute          the {@link Attribute} of values to transform.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   */
  private static void transform(final Organization entity, final Attribute attribute) {
    final String name  = attribute.getName();
    final Object value = attribute.getValue().get(0);
    // transform single value attributes
    switch (name) {
      case IDENTIFIER : entity.id(String.class.cast(value));
                        break;
      case UNIQUE     : entity.name(String.class.cast(value));
                        break;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transformRole
  /**
   ** Converts the specified role resource attribute to the transfer options
   ** required by the Identity Connector Framework.
   **
   ** @param  collector          the {@link ConnectorObjectBuilder} to collect
   **                            the attributes.
   **                            <br>
   **                            Allowed object is
   **                            {@link ConnectorObjectBuilder}.
   ** @param  resource           the role resource providing to the data to
   **                            transform.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Role}.
   */
  private static void transformRole(final ConnectorObjectBuilder collector, final List<Role> resource) {
    if (!CollectionUtility.empty(resource)) {
      final List<EmbeddedObject> permission = new ArrayList<EmbeddedObject>();
      // a lambda function can avoid this loop but performance benchmarks had
      // shown saying that the overhead of Stream.forEach() compared to an
      // ordinary for loop is so significant in general that using it by default
      // will just pile up a lot of useless CPU cycles across the application
      for (Role cursor : resource) {
        permission.add(
          new EmbeddedObjectBuilder().setObjectClass(ObjectClass.GROUP)
            .addAttribute(Uid.NAME, cursor.id())
          .build());
      }
      // add the collection to the connector object builder
      collector.addAttribute(ObjectClass.GROUP.getObjectClassValue(), permission);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transformTeam
  /**
   ** Converts the specified team resource attribute to the transfer options
   ** required by the Identity Connector Framework.
   **
   ** @param  collector          the {@link ConnectorObjectBuilder} to collect
   **                            the attributes.
   **                            <br>
   **                            Allowed object is
   **                            {@link ConnectorObjectBuilder}.
   ** @param  resource           the team resource providing to the data to
   **                            transform.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Team}.
   */
  private static void transformTeam(final ConnectorObjectBuilder collector, final List<Team> resource) {
    if (!CollectionUtility.empty(resource)) {
      final List<EmbeddedObject> permission = new ArrayList<EmbeddedObject>();
      // a lambda function can avoid this loop but performance benchmarks had
      // shown saying that the overhead of Stream.forEach() compared to an
      // ordinary for loop is so significant in general that using it by default
      // will just pile up a lot of useless CPU cycles across the application
      for (Team cursor : resource) {
        permission.add(
          new EmbeddedObjectBuilder().setObjectClass(Grafana.TEAM.clazz)
            .addAttribute(Uid.NAME, cursor.id())
          .build());
      }
      // add the collection to the connector object builder
      collector.addAttribute(Grafana.TEAM.name, permission);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transformOrganization
  /**
   ** Converts the specified organization resource attribute to the transfer
   ** options required by the Identity Connector Framework.
   **
   ** @param  collector          the {@link ConnectorObjectBuilder} to collect
   **                            the attributes.
   **                            <br>
   **                            Allowed object is
   **                            {@link ConnectorObjectBuilder}.
   ** @param  resource           the team resource providing to the data to
   **                            transform.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Team}.
   */
  private static void transformOrganization(final ConnectorObjectBuilder collector, final List<Organization> resource) {
    if (!CollectionUtility.empty(resource)) {
      final List<EmbeddedObject> permission = new ArrayList<EmbeddedObject>();
      // a lambda function can avoid this loop but performance benchmarks had
      // shown saying that the overhead of Stream.forEach() compared to an
      // ordinary for loop is so significant in general that using it by default
      // will just pile up a lot of useless CPU cycles across the application
      for (Organization cursor : resource) {
        permission.add(
          new EmbeddedObjectBuilder().setObjectClass(Grafana.ORGANIZATION.clazz)
            .addAttribute(Uid.NAME,          cursor.id())
            .addAttribute(Organization.ROLE, cursor.role())
          .build());
      }
      // add the collection to the connector object builder
      collector.addAttribute(Grafana.ORGANIZATION.name, permission);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildAttribute
  /**
   ** Factory method to create a new {@link Attribute}.
   **
   ** @param  name               the name of the attribute that is being built.
   **                            <br>
   **                            Must not be <code>null</code> or empty.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the value object that is being built.
   **                            <br>
   **                            Allowed object is {@link Object}.
   ** @param  clazz              the type of the value object that is being
   **                            built.
   **                            <br>
   **                            Allowed object is {@link Class} of type any.
   **
   ** @return                    the {@link Attribute} equiped with the value
   **                            pair.
   **                            <br>
   **                            Possible object is {@link Attribute}.
   */
  private static Attribute buildAttribute(final String name, final Object value, final Class<?> clazz) {
    // name must not be blank
    final AttributeBuilder builder = new AttributeBuilder().setName(name);
    if (value != null) {
      if (clazz == boolean.class || clazz == Boolean.class) {
        builder.addValue(Boolean.class.cast(value));
      }
      else if (clazz == int.class || clazz == Integer.class) {
        builder.addValue(Integer.class.cast(value));
      }
      else if (clazz == long.class || clazz == Long.class) {
        builder.addValue(Long.class.cast(value));
      }
      else if (value instanceof List<?>) {
        builder.addValue(new ArrayList<>((List<?>)value));
      }
      else {
        builder.addValue(value.toString());
      }
    }
    return builder.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   empty
  /**
   ** Checks if the specified {@link Object} is empty.
   **
   ** @param  value              the {@link Object} to check for emptyness.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    <code>true</code> if the <code>value</code>
   **                            contains no elements.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  private static boolean empty(final Object value) {
    return value == null || (value instanceof Collection ? CollectionUtility.empty((Collection<?>)value) : false) || (value instanceof String ? StringUtility.blank(String.class.cast(value)) : false);
  }
}