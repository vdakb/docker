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

    Copyright © 2021. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Red Hat Keycloak Connector

    File        :   Service.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Service.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-06-21  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.keycloak.schema;

import java.lang.reflect.Field;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Name;
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

import oracle.iam.identity.icf.foundation.object.Pair;

import oracle.iam.identity.icf.foundation.utility.StringUtility;
import oracle.iam.identity.icf.foundation.utility.CollectionUtility;

import oracle.iam.identity.icf.connector.AbstractConnector;

////////////////////////////////////////////////////////////////////////////////
// final class Service
// ~~~~~ ~~~~~ ~~~~~~~
/**
 ** An interface to describe and transform Keycloak Resource object to and from
 ** Identity Connector attribute collections.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class Service {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The name of the entity system identifier attribute.
   */
  public static final String ID          = "id";
  /**
   ** The name of the user self reference attribute.
   */
  public static final String SELF        = "self";
  /**
   ** The name of the user origin attribute.
   */
  public static final String ORIGIN      = "origin";
  /**
   ** The name of the user unique name attribute.
   */
  public static final String USERNAME    = "username";
  /**
   ** The name of the user status attribute.
   */
  public static final String STATUS      = "enabled";
  /**
   ** The name of the user created timestamp attribute.
   */
  public static final String CREATED     = "createdTimestamp";
  /**
   ** The name of the user not before timestamp attribute.
   */
  public static final String NOTBEFORE   = "notBefore";
  /**
   ** The name of the user user last name attribute.
   */
  public static final String LASTNAME    = "lastName";
  /**
   ** The name of the user user last name attribute.
   */
  public static final String FIRSTNAME   = "firstName";
  /**
   ** E-mail addresse for the user.
   ** The value SHOULD be canonicalized by the Service Provider, e.g.,
   ** bjensen@example.com instead of bjensen@example.com.
   */
  public static final String EMAIL       = "email";
  /**
   ** New users are not automatically verified by default.
   ** Unverified users can be created by specifying verified:
   ** <code>false</code>.
   ** Becomes <code>true</code> when the user verifies their email address.
   */
  public static final String VERIFIED    = "verified";
  /**
   ** The name of the user attribute to control one-time-password
   ** capabilities.
   */
  public static final String OTP         = "oneTimePassword";
  /**
   ** The name of the resource attribute collection.
   */
  public static final String ATTRIBUTE   = "attribute";
  /**
   ** The name of the user required action attribute.
   */
  public static final String ACTION      = "action";
  /**
   ** The name of the user access permission attribute.
   */
  public static final String ACCESS      = "access";
  /**
   ** The name of the user credential collection.
   */
  public static final String CREDENTIAL  = "credential";
  /**
   ** The name of the user credential type.
   */
  public static final String TYPE        = "type";
  /**
   ** The name of the user disabled credential collection.
   */
  public static final String DISABLED    = "disable";
  /**
   ** The name of the user realm role collection.
   */
  public static final String ROLE        = "role";
  /**
   ** The name of the user client role collection.
   */
  public static final String CLIENT      = "client";
  /**
   ** The name of the user group collection.
   */
  public static final String GROUP       = "group";
  /**
   ** The name of the role name attribute.
   */
  public static final String NAME        = "name";
  /**
   ** The name of the action alias name attribute.
   */
  public static final String ALIAS       = "alias";
  /**
   ** The name of the action priority attribute.
   */
  public static final String PRIORITY    = "priority";
  /**
   ** The name of the role description attribute.
   */
  public static final String DESCRIPTION = "description";
  /**
   ** The name of the group attribute name attribute.
   */
  public static final String PATH        = "path";

  /**
   ** The one and only instance of the <code>Service</code>.
   ** <p>
   ** Singleton Pattern
   */
  public static final Service instance = new Service();

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
    final ObjectClassInfo user       = Factory.defineObjectClass(builder, User.class);
    final ObjectClassInfo role       = Factory.defineObjectClass(builder, Role.class);
    final ObjectClassInfo group      = Factory.defineObjectClass(builder, Group.class);
    builder.defineObjectClass(user);
    builder.defineObjectClass(role);
    builder.defineObjectClass(group);

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
    builder.addSupportedObjectClass(UpdateAttributeValuesOp.class, role);

    builder.addSupportedObjectClass(SearchOp.class,                group);
    builder.addSupportedObjectClass(UpdateAttributeValuesOp.class, group);

    return builder.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transformUser
  /**
   ** Factory method to create a new {@link User} instance and transform the
   ** specified {@link Set} of {@link Attribute}s to the Keycloak user
   ** resource.
   **
   ** @param  attribute        the {@link Set} of {@link Attribute}s to set
   **                          on the Keycloak user resource.
   **                          <br>
   **                          Allowed object is a {@link Set} where each
   **                          elemment is of type {@link Attribute}.
   **
   ** @return                  the Keycloak user resource populated by the
   **                          {@link Set} of {@link Attribute}s
   **                          <br>
   **                          Possible object is a {@link User}.
   */
  public static User transformUser(final Set<Attribute> attribute) {
    return transformUser(new User(), attribute);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transformUser
  /**
   ** Factory method to modify an existing {@link User} instance and transform the
   ** specified {@link Set} of {@link Attribute}s to the Keycloak user
   ** resource.
   **
   ** @param  resource         the {@link User} instance to update its attributes
   **
   ** @param  attribute        the {@link Set} of {@link Attribute}s to set
   **                          on the Keycloak user resource.
   **                          <br>
   **                          Allowed object is a {@link Set} where each
   **                          elemment is of type {@link Attribute}.
   **
   ** @return                  the Keycloak user resource populated by the
   **                          {@link Set} of {@link Attribute}s
   **                          <br>
   **                          Possible object is a {@link User}.
   */
  public static User transformUser(final User resource, final Set<Attribute> attribute) {
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
        final Object value = field.get(resource);
        switch (field.getName()) {
          case STATUS     : collector.addAttribute(buildAttribute(OperationalAttributes.ENABLE_NAME, value, field.getType()));
                            break;
          case ATTRIBUTE  : // transform multi-valued entity attributes to
                            // layered attribute
                            transformAttribute(collector, (List<Pair<String, String>>)value);
                            break;
          case ACTION     : // transform multi-valued required actions to
                            // embedded collection
                            transformAction(collector, (List<ActionType>)value);
                            break;
          case ROLE       : // transform multi-valued role attributes to
                            // embedded collection
                            transformRole(collector, (List<Role>)value);
                            break;
          case GROUP      : // transform multi-valued group attributes
                            // to embedded collection
                            transformGroup(collector, (List<Group>)value);
                            break;
          case CREDENTIAL : // transform multi-valued credential
                            // attributes to embedded collection
                            transformCredential(collector, (List<Credential>)value);
                            break;
          case DISABLED   : // transform multi-valued disabled
                            // credentials to layered attribute
                            transformCredentialDisabled(collector, (List<CredentialType>)value);
                            break;
          case ACCESS     : // transform multi-valued access permission
                            // attributes to embedded collection
                            transformAccess(collector, (Map<Permission, Boolean>)value);
                            break;
          case CLIENT     : // transform multi-valued access permission
                            // attributes to embedded collection
                            transformClient(collector, (List<Client>)value);
                            break;
          default         : collector.addAttribute(buildAttribute(field.getName(), value, field.getType()));
        }
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transform
  /**
   ** Converts the specified required action resource to the transfer options
   ** required by the Identity Connector Framework.
   **
   ** @param  collector          the {@link ConnectorObjectBuilder} to collect
   **                            the attributes.
   **                            <br>
   **                            Allowed object is
   **                            {@link ConnectorObjectBuilder}.
   ** @param  resource           the  required action resource to transform.
   **                            <br>
   **                            Allowed object is {@link ActionType}.
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
  public static void transform(final ConnectorObjectBuilder collector, final ActionType resource)
    throws IllegalAccessException
    ,      IllegalArgumentException {

    final Collection<Field> properties = Factory.describe(User.class);
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
      // field is assigned to
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
   **                            Allowed object is {@link Client}.
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
  public static void transform(final ConnectorObjectBuilder collector, final Client resource)
          throws IllegalAccessException
          ,      IllegalArgumentException {

    final Collection<Field> properties = Factory.describe(Client.class);
    for (Field field : properties) {
      // ensure that the field is accessible regardless which modifier the
      // field is assigned to
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
   ** Converts the specified group resource to the transfer options required by
   ** the Identity Connector Framework.
   **
   ** @param  collector          the {@link ConnectorObjectBuilder} to collect
   **                            the attributes.
   **                            <br>
   **                            Allowed object is
   **                            {@link ConnectorObjectBuilder}.
   ** @param  resource           the  group resource to transform.
   **                            <br>
   **                            Allowed object is {@link Group}.
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
  public static void transform(final ConnectorObjectBuilder collector, final Group resource)
    throws IllegalAccessException
    ,      IllegalArgumentException {

    final Collection<Field> properties = Factory.describe(Group.class);
    for (Field field : properties) {
      // ensure that the field is accessible regardless which modifier the
      // field is assigned to
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
   ** Converts the specified  user resource to the transfer options
   ** required by the Keycloak REST API.
   **
   ** @param  entity             the Keycloak user resource entity to collect
   **                            the attributes.
   **                            <br>
   **                            Allowed object is {@link User}.
   ** @param  attribute          the {@link Attribute} of values to transform.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   */
  private static void transform(final User entity, final Attribute attribute) {
    final String name  = attribute.getName();
    final Object value = attribute.getValue().get(0);
    // delegate layered attributes
    if (name.startsWith(ATTRIBUTE)) {
      // obtain the attribute name 
      entity.attribute(name.substring(ATTRIBUTE.length() + 1), String.class.cast(value));
    }
    else if (name.startsWith(ACTION)) {
      ActionType action = ActionType.from(name.substring(ACTION.length() + 1));
      if (Boolean.class.cast(value)) {
        // obtain the attribute name 
        entity.action().add(action);
      } else if (!Boolean.class.cast(value) && entity.action().contains(action)) {
        entity.action().remove(action);
      }
    }
    else if (name.startsWith(DISABLED)) {
      if (Boolean.class.cast(value)) {
        // obtain the attribute name
        entity.disabled().add(CredentialType.from(name.substring(DISABLED.length() + 1)));
      }
    }
    else if (name.startsWith(ACCESS)) {
        // obtain the attribute name
        entity.access().put(Permission.from(name.substring(ACCESS.length() + 1)), (Boolean) value);
    }
    else {
      // transform single value attributes
      switch (name) {
        case oracle.iam.identity.icf.schema.Attribute.IDENTIFIER :
        case ID        : entity.id(String.class.cast(value));
                         break;
        case SELF      : entity.self(String.class.cast(value));
                         break;
        case ORIGIN    : entity.origin(String.class.cast(value));
                         break;
        case oracle.iam.identity.icf.schema.Attribute.STATUS :
        case STATUS    : entity.enabled(Boolean.class.cast(value));
                         break;
        case VERIFIED  : entity.verified(Boolean.class.cast(value));
                         break;
        case oracle.iam.identity.icf.schema.Attribute.UNIQUE :
        case USERNAME  : entity.username(String.class.cast(value));
                         break;
        case FIRSTNAME : entity.firstName(String.class.cast(value));
                         break;
        case EMAIL     : entity.email(String.class.cast(value));
                         break;
        case LASTNAME  : entity.lastName(String.class.cast(value));
                         break;
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transformAction
  /**
   ** Converts the specified required action resource attribute to the transfer
   ** options required by the Identity Connector Framework.
   **
   ** @param  collector          the {@link ConnectorObjectBuilder} to collect
   **                            the attributes.
   **                            <br>
   **                            Allowed object is
   **                            {@link ConnectorObjectBuilder}.
   ** @param  resource           the required action resource providing to the
   **                            data to transform.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link ActionType}.
   */
  private static void transformAction(final ConnectorObjectBuilder collector, final List<ActionType> resource) {
    Set<String> origins = resource.stream().map(i -> i.origin).collect(Collectors.toSet());
    // a lambda function can avoid this loop but performance benchmarks had
    // shown saying that the overhead of Stream.forEach() compared to an
    // ordinary for loop is so significant in general that using it by default
    // will just pile up a lot of useless CPU cycles across the application
    for (ActionType action : ActionType.values()) {
      // add the collection to the connector object builder
      collector.addAttribute(layeredName(Service.ACTION, action.id), origins.contains(action.origin));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transformGroup
  /**
   ** Converts the specified group resource attribute to the transfer options
   ** required by the Identity Connector Framework.
   **
   ** @param  collector          the {@link ConnectorObjectBuilder} to collect
   **                            the attributes.
   **                            <br>
   **                            Allowed object is
   **                            {@link ConnectorObjectBuilder}.
   ** @param  resource           the group resource providing to the data to
   **                            transform.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Group}.
   */
  private static void transformGroup(final ConnectorObjectBuilder collector, final List<Group> resource) {
    if (!CollectionUtility.empty(resource)) {
      final List<EmbeddedObject> group = new ArrayList<EmbeddedObject>();
      // a lambda function can avoid this loop but performance benchmarks had
      // shown saying that the overhead of Stream.forEach() compared to an
      // ordinary for loop is so significant in general that using it by default
      // will just pile up a lot of useless CPU cycles across the application
      for (Group cursor : resource) {
        group.add(new EmbeddedObjectBuilder().setObjectClass(ObjectClass.GROUP).addAttribute(Uid.NAME, cursor.id()).build());
      }
      // add the collection to the connector object builder
      collector.addAttribute(ObjectClass.GROUP.getObjectClassValue(), group);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transformRole
  /**
   ** Converts the specified user role resource attribute to the transfer
   ** options required by the Identity Connector Framework.
   **
   ** @param  collector          the {@link ConnectorObjectBuilder} to collect
   **                            the attributes.
   **                            <br>
   **                            Allowed object is
   **                            {@link ConnectorObjectBuilder}.
   ** @param  resource           the user group resource providing to the data
   **                            to transform.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Group}.
   */
  private static void transformRole(final ConnectorObjectBuilder collector, final List<Role> resource) {
    if (!CollectionUtility.empty(resource)) {
      final List<EmbeddedObject> role = new ArrayList<EmbeddedObject>();
      // a lambda function can avoid this loop but performance benchmarks had
      // shown saying that the overhead of Stream.forEach() compared to an
      // ordinary for loop is so significant in general that using it by default
      // will just pile up a lot of useless CPU cycles across the application
      for (Role cursor : resource) {
        role.add(new EmbeddedObjectBuilder().setObjectClass(Keycloak.ROLE.clazz).addAttribute(Uid.NAME, cursor.id()).build());
      }
      // add the collection to the connector object builder
      collector.addAttribute(Keycloak.ROLE.name, role);
    }
  }
  //////////////////////////////////////////////////////////////////////////////
  // Method:   transformClient
  /**
   ** Converts the specified user client role resource attribute to the transfer
   ** options required by the Identity Connector Framework.
   **
   ** @param  collector          the {@link ConnectorObjectBuilder} to collect
   **                            the attributes.
   **                            <br>
   **                            Allowed object is
   **                            {@link ConnectorObjectBuilder}.
   ** @param  resource           the user group resource providing to the data
   **                            to transform.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Client}.
   */
  private static void transformClient(final ConnectorObjectBuilder collector, final List<Client> resource) {
    if (CollectionUtility.empty(resource)) {
      return;
    }
    final List<EmbeddedObject> client = new ArrayList<>();
    // a lambda function can avoid this loop but performance benchmarks had
    // shown saying that the overhead of Stream.forEach() compared to an
    // ordinary for loop is so significant in general that using it by default
    // will just pile up a lot of useless CPU cycles across the application
    for (Client cursor : resource) {
      List<Role> roles = cursor.role();
      if (!CollectionUtility.empty(roles)) {
        for (Role role : roles) {
          client.add(new EmbeddedObjectBuilder()
                  .setObjectClass(Keycloak.CLIENTROLE.clazz)
                  .addAttribute(Uid.NAME, String.format("%s__%s", cursor.id, role.id())).build());
        }
      }
    }
    // add the collection to the connector object builder
    collector.addAttribute(Keycloak.CLIENTROLE.name, client);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transformAttribute
  /**
   ** Converts the specified collection of entity attributes to the transfer
   ** options required by the Identity Connector Framework.
   **
   ** @param  collector          the {@link ConnectorObjectBuilder} to collect
   **                            the attributes.
   **                            <br>
   **                            Allowed object is
   **                            {@link ConnectorObjectBuilder}.
   ** @param  resource           the collection of entity attributes providing
   **                            to the data to transform.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Pair} with type
   **                            {@link String} for the tag and {@link String}
   **                            as the value.
   */
  private static void transformAttribute(final ConnectorObjectBuilder collector, final List<Pair<String, String>> resource) {
    if (CollectionUtility.empty(resource)) {
      return;
    }
    for (Pair<String, String> attribute : resource) {
       collector.addAttribute(buildAttribute(layeredName(ATTRIBUTE, attribute.tag), attribute.value, String.class));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transformCredentialDisabled
  /**
   ** Converts the specified collection of disabled credential types to the
   ** transfer options required by the Identity Connector Framework.
   **
   ** @param  collector          the {@link ConnectorObjectBuilder} to collect
   **                            the attributes.
   **                            <br>
   **                            Allowed object is
   **                            {@link ConnectorObjectBuilder}.
   ** @param  resource           the collection of disabled credential types
   **                            providing the data to transform.
   **                            <br>
   **                            Allowed object is {@link CredentialType}.
   */
  private static void transformCredentialDisabled(final ConnectorObjectBuilder collector, final List<CredentialType> resource) {
    if (!CollectionUtility.empty(resource)) {
      for (CredentialType cursor : resource) {
         collector.addAttribute(buildAttribute(layeredName(DISABLED, cursor.name().toLowerCase()), resource.contains(cursor), Boolean.class));
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transformAccess
  /**
   ** Converts the specified user access permission resource attribute to the
   ** transfer options required by the Identity Connector Framework.
   **
   ** @param  collector          the {@link ConnectorObjectBuilder} to collect
   **                            the attributes.
   **                            <br>
   **                            Allowed object is
   **                            {@link ConnectorObjectBuilder}.
   ** @param  resource           the user access permission resource providing
   **                            the data to transform.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Map}.
   */
  private static void transformAccess(final ConnectorObjectBuilder collector, final Map<Permission, Boolean> resource) {
    if (!CollectionUtility.empty(resource)) {
      for (Map.Entry<Permission, Boolean> cursor : resource.entrySet()) {
        collector.addAttribute(buildAttribute(layeredName(ACCESS, cursor.getKey().id.toLowerCase()), cursor.getValue(), Boolean.class));
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transformCredential
  /**
   ** Converts the specified user credential resource attribute to the transfer
   ** options required by the Identity Connector Framework.
   **
   ** @param  collector          the {@link ConnectorObjectBuilder} to collect
   **                            the attributes.
   **                            <br>
   **                            Allowed object is
   **                            {@link ConnectorObjectBuilder}.
   ** @param  resource           the user credentail resource providing the data
   **                            to transform.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Group}.
   */
  private static void transformCredential(final ConnectorObjectBuilder collector, final List<Credential> resource) {
    if (!CollectionUtility.empty(resource)) {
      final List<EmbeddedObject> permission = new ArrayList<EmbeddedObject>();
      // a lambda function can avoid this loop but performance benchmarks had
      // shown saying that the overhead of Stream.forEach() compared to an
      // ordinary for loop is so significant in general that using it by default
      // will just pile up a lot of useless CPU cycles across the application
      for (Credential cursor : resource) {
        permission.add(new EmbeddedObjectBuilder().setObjectClass(Keycloak.CREDENTIAL.clazz)
          .addAttribute(Uid.NAME,     cursor.id())
          .addAttribute(Name.NAME,    cursor.label())
          .addAttribute(Service.TYPE, cursor.type().id)
        .build());
      }
      // add the collection to the connector object builder
      collector.addAttribute(Keycloak.CREDENTIAL.name, permission);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   layeredName
  /**
   ** Factory to create the name of a layered attribute name.
   **
   ** @param  prefix            the resource prefix.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  suffix             the resource suffix.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the concatenated <code>prefix</code> and
   **                            <code>suffix</code> separated by a
   **                            <code>.</code> (dot).
   **                            <br>
   **                            Possible  object is {@link String}.
   */
  private static String layeredName(final String prefix, final String suffix) {
    return prefix.concat(".").concat(suffix);
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