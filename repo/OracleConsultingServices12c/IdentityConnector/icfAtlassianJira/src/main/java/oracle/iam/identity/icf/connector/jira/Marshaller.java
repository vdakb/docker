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

    Copyright © 2021. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Atlassian Jira Server Connector

    File        :   Marshaller.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@oracle.com

    Purpose     :   This file implements the class
                    Marshaller.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-22-05  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.jira;

import java.lang.reflect.Field;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.identityconnectors.common.security.GuardedString;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.EmbeddedObject;
import org.identityconnectors.framework.common.objects.ConnectorObject;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.ConnectorObjectBuilder;
import org.identityconnectors.framework.common.objects.EmbeddedObjectBuilder;

import oracle.iam.identity.icf.schema.Factory;

import oracle.iam.identity.icf.foundation.object.Pair;

import oracle.iam.identity.icf.foundation.utility.CollectionUtility;
import oracle.iam.identity.icf.foundation.utility.CredentialAccessor;

import oracle.iam.identity.icf.connector.jira.schema.User;
import oracle.iam.identity.icf.connector.jira.schema.Group;
import oracle.iam.identity.icf.connector.jira.schema.Project;

import org.identityconnectors.framework.common.objects.OperationalAttributes;

////////////////////////////////////////////////////////////////////////////////
// class Marshaller
// ~~~~~ ~~~~~~~~~~
/**
 ** An interface to transfer Jira REST resource to and from Identity Connector
 ** attribute collections.
 **
 ** @author  sylvert.bernet@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Marshaller {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The name of the identifier name attribute. */
  public static final String      IDENTIFIER    = "id";

  /** The name of the identifier name attribute. */
  public static final String      USERNAME      = "username";

  /** The name of the display name attribute. */
  public static final String      DISPLAYNAME   = "displayName";

  /** The name of the e-mail attribute. */
  public static final String      EMAILADDRESS  = "emailAddress";

  /** The name of the locale attribute. */
  public static final String      LOCALE        = "locale";

  /** The name of the locale attribute. */
  public static final String      TIMEZONE      = "timeZone";

  /** The name of the password attribute. */
  public static final String      PASSWORD      = "password";

  /** The name of the status attribute. */
  public static final String      STATUS        = "active";

  /** The name of the administrator attribute. */
  public static final String      ADMINISTRATOR = "administrator";

  /** The name of the type attribute. */
  public static final String      TYPE          = "type";

  /** the object class name for a project entitlement */
  public static final String      PROJECT_NAME  = "__PROJECT__";

  /** the object class for a project entitlement */
  public static final ObjectClass PROJECT       = new ObjectClass(PROJECT_NAME);

  /** the object class name for a role entitlement */
  public static final String      ROLE_NAME     = "__ROLE__";

  /** the object class for a role entitlement */
  public static final ObjectClass ROLE          = new ObjectClass(ROLE_NAME);

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor (private)
  /**
   ** Default constructor
   ** <br>
   ** Access modifier private prevents other classes using
   ** "new Marshaller()"
   */
  private Marshaller() {
    // should never be instantiated
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   inboundUser
  /**
   ** Factory method to create a new {@link User} instance and transfer the
   ** specified {@link Set} of {@link Attribute}s to the Jira user resource.
   **
   ** @param  attribute          the {@link Set} of {@link Attribute}s to set
   **                            on the Jira user resource.
   **                            <br>
   **                            Allowed object is a {@link Set} where each
   **                            elemment is of type {@link Attribute}.
   **
   ** @return                    the Jira user resource populated by the
   **                            {@link Set} of {@link Attribute}s
   **                            <br>
   **                            Possible object is a {@link User}.
   */
  public static User inboundUser(final Set<Attribute> attribute) {
    final User resource = new User();
    for (Attribute cursor : attribute) {
      if (!CollectionUtility.empty(cursor.getValue())) {
        collect(resource, cursor);
      }
    }
    return resource;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   inboundProject
  /**
   ** Factory method to create a new {@link Project} instance and transfer the
   ** specified {@link Set} of {@link Attribute}s to the Jira user resource.
   **
   ** @param  attribute          the {@link Set} of {@link Attribute}s to set
   **                            on the Jira Project resource.
   **                            <br>
   **                            Allowed object is a {@link Set} where each
   **                            elemment is of type {@link Attribute}.
   **
   ** @return                    the Jira project resource populated by the
   **                            {@link Set} of {@link Attribute}s
   **                            <br>
   **                            Possible object is a {@link Project}.
   */
  public static Project inboundProject(Set<Attribute> attribute) {
    final Project resource = new Project();
    for (Attribute cursor : attribute) {
      if (!CollectionUtility.empty(cursor.getValue())) {
        collect(resource, cursor);
      }
    }
    return resource;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectorObject
  /**
   ** Transforms the data received from the Service Provider and wrapped in the
   ** specified Jira {@link User} <code>user</code> to the
   ** {@link ConnectorObjectBuilder}.
   **
   ** @param  resource           the specified Jira {@link User} to
   **                            build.
   **                            <br>
   **                            Allowed object is {@link User}.
   ** @param  filter             the {@link Set} of attribute names requested to
   **                            be set in the {@link ConnectorObjectBuilder}.
   **                            <br>
   **                            If this passed as <code>null</code> all
   **                            registred attributes are returned
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   ** @param  project            the {@link List} of Project
   **                            resource identifiers  to collect.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Pair}.
   ** @param  group              the {@link List} of Cloud Controller space
   **                            resource identifiers  to collect.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Pair}.
   **
   ** @return                    the transformation result wrapped in a
   **                            {@link ConnectorObject}.
   **                            <br>
   **                            Possible object {@link ConnectorObject}.
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
  static ConnectorObject connectorObject(final User resource, final Set<String> filter, final List<Pair<String, List<String>>> project, final List<String> group, final List<String> application)
    throws IllegalAccessException
    ,      IllegalArgumentException {

    final Collection<Field>      properties = Factory.describe(resource.getClass());
    final ConnectorObjectBuilder builder    = new ConnectorObjectBuilder().setObjectClass(ObjectClass.ACCOUNT);
    // always set __UID__ and __NAME__ attribute to convince stupid framework
    // developer
    builder.setUid(resource.id());
    builder.setName(resource.userName());
    for (Field property : properties) {
      // skip any property that have to be ignored
      if (property.isAnnotationPresent(JsonIgnore.class))
        continue;

     // verify if the field is subject matter
      final String propertyName = property.getName();
      if (filter == null || (filter != null && filter.contains(propertyName))) {
        switch (propertyName) {
          // ICF is such a stupid framework that enforce that every Service
          // Provider has to look like a LDAP system
          // we need to put attributes that isn't needed only to satisfay the
          // constraints of this bullshit
          case IDENTIFIER   :
          case oracle.iam.identity.icf.schema.Attribute.IDENTIFIER  :
          case oracle.iam.identity.icf.schema.Attribute.UNIQUE      : break;
          case STATUS       : builder.addAttribute(buildAttribute(resource.active(), OperationalAttributes.ENABLE_NAME, boolean.class));
                              break;
          default           : // ensure that the field is accessible regardless
                              // which modifier the field is assinged to
                              property.setAccessible(true);
                              builder.addAttribute(buildAttribute(property.get(resource), propertyName, property.getType()));
                              break;
        }
      }
    }

   if (!CollectionUtility.empty(project)) {
      // add the collection to the connector object builder
      collectScoped(builder, Marshaller.PROJECT, project);
    }
    if (!CollectionUtility.empty(group)) {
      // add the collection to the connector object builder
      collectResource(builder, ObjectClass.GROUP, group);
    }
    return builder.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectorObject
  /**
   ** Transforms the data received from the Service Provider and wrapped in the
   ** specified Jira {@link Project} <code>user</code> to the
   ** {@link ConnectorObjectBuilder}.
   **
   ** @param  resource           the specified Jira {@link Project} to
   **                            build.
   **                            <br>
   **                            Allowed object is {@link Project}.
   ** @param  filter             the {@link Set} of attribute names requested to
   **                            be set in the {@link ConnectorObjectBuilder}.
   **                            <br>
   **                            If this passed as <code>null</code> all
   **                            registred attributes are returned
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the transformation result wrapped in a
   **                            {@link ConnectorObject}.
   **                            <br>
   **                            Possible object {@link ConnectorObject}.
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
  static ConnectorObject connectorObject(final Project resource, final Set<String> filter)
    throws IllegalAccessException
    ,      IllegalArgumentException {

    final Collection<Field>      properties = Factory.describe(resource.getClass());
    final ConnectorObjectBuilder builder    = new ConnectorObjectBuilder().setObjectClass(Marshaller.PROJECT);
    builder.setUid(resource.id());
    builder.setName(resource.name());
    for (Field property : properties) {
      // skip any property that have to be ignored
      if (property.isAnnotationPresent(JsonIgnore.class))
        continue;

      // verify if the field is subject matter
      final String propertyName = property.getName();
      if (filter == null || (filter != null && filter.contains(propertyName))) {
        switch (propertyName) {
          // ICF is such a stupid framework that enforce that every Service
          // Provider has to look like a LDAP system
          // we need to put attributes that isn't needed only to satisfay the
          // constraints of this bullshit
          case IDENTIFIER   :
          case oracle.iam.identity.icf.schema.Attribute.IDENTIFIER  :
          case oracle.iam.identity.icf.schema.Attribute.UNIQUE      : break;
          default           : // ensure that the field is accessible regardless
                              // which modifier the field is assinged to
                              property.setAccessible(true);
                              builder.addAttribute(buildAttribute(property.get(resource), propertyName, property.getType()));
                              break;
        }
      }
    }
    return builder.build();
  }

    //////////////////////////////////////////////////////////////////////////////
  // Method:   connectorObject
  /**
   ** Transforms the data received from the Service Provider and wrapped in the
   ** specified Jira {@link Role} <code>role</code> to the
   ** {@link ConnectorObjectBuilder}.
   **
   ** @param  resource           the specified Jira {@link Role} to
   **                            build.
   **                            <br>
   **                            Allowed object is {@link Role}.
   ** @param  filter             the {@link Set} of attribute names requested to
   **                            be set in the {@link ConnectorObjectBuilder}.
   **                            <br>
   **                            If this passed as <code>null</code> all
   **                            registred attributes are returned
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   ** @return                    the transformation result wrapped in a
   **                            {@link ConnectorObject}.
   **                            <br>
   **                            Possible object {@link ConnectorObject}.
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
  static ConnectorObject connectorObject(final Project.Role resource, final Set<String> filter)
    throws IllegalAccessException
    ,      IllegalArgumentException {

    final Collection<Field>      properties = Factory.describe(resource.getClass());
    final ConnectorObjectBuilder builder    = new ConnectorObjectBuilder().setObjectClass(Marshaller.ROLE);
    // always set __UID__ and __NAME__ attribute to convince stupid framework
    // developer
    builder.setUid(String.valueOf(resource.id()));
    builder.setName(resource.name());
    for (Field property : properties) {
      // skip any property that have to be ignored
      if (property.isAnnotationPresent(JsonIgnore.class))
        continue;

      // verify if the field is subject matter
      final String propertyName = property.getName();
      if (filter == null || (filter != null && filter.contains(propertyName))) {
        switch (propertyName) {
          // ICF is such a stupid framework that enforce that every Service
          // Provider has to look like a LDAP system
          // we need to put attributes that isn't needed only to satisfay the
          // constraints of this bullshit
          case IDENTIFIER   :
          case oracle.iam.identity.icf.schema.Attribute.IDENTIFIER  :
          case oracle.iam.identity.icf.schema.Attribute.UNIQUE      : break;
          default           : // ensure that the field is accessible regardless
                              // which modifier the field is assinged to
                              property.setAccessible(true);
                              builder.addAttribute(buildAttribute(property.get(resource), propertyName, property.getType()));
                              break;
        }
      }
    }
    return builder.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectorObject
  /**
   ** Transforms the data received from the Service Provider and wrapped in the
   ** specified Jira {@link Role} <code>role</code> to the
   ** {@link ConnectorObjectBuilder}.
   **
   ** @param  resource           the specified Jira {@link Role} to
   **                            build.
   **                            <br>
   **                            Allowed object is {@link Role}.
   ** @param  filter             the {@link Set} of attribute names requested to
   **                            be set in the {@link ConnectorObjectBuilder}.
   **                            <br>
   **                            If this passed as <code>null</code> all
   **                            registred attributes are returned
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   ** @return                    the transformation result wrapped in a
   **                            {@link ConnectorObject}.
   **                            <br>
   **                            Possible object {@link ConnectorObject}.
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
  static ConnectorObject connectorObject(final Group resource, final Set<String> filter)
    throws IllegalAccessException
    ,      IllegalArgumentException {

    final Collection<Field>      properties = Factory.describe(resource.getClass());
    final ConnectorObjectBuilder builder    = new ConnectorObjectBuilder().setObjectClass(ObjectClass.GROUP);
    builder.setUid(resource.id());
    builder.setName(resource.html());
    for (Field property : properties) {
      // skip any property that have to be ignored
      if (property.isAnnotationPresent(JsonIgnore.class))
        continue;

      // verify if the field is subject matter
      final String propertyName = property.getName();
      if (filter == null || (filter != null && filter.contains(propertyName))) {
        switch (propertyName) {
          // ICF is such a stupid framework that enforce that every Service
          // Provider has to look like a LDAP system
          // we need to put attributes that isn't needed only to satisfay the
          // constraints of this bullshit
          case IDENTIFIER   :
          case oracle.iam.identity.icf.schema.Attribute.IDENTIFIER  :
          case oracle.iam.identity.icf.schema.Attribute.UNIQUE      : break;
          default           : // ensure that the field is accessible regardless
                              // which modifier the field is assinged to
                              property.setAccessible(true);
                              builder.addAttribute(buildAttribute(property.get(resource), propertyName, property.getType()));
                              break;
        }
      }
    }
    return builder.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildAttribute
  /**
   ** Factory method to create a new {@link Attribute} instance.
   **
   ** @param  value              the {@link Attribute} value to set.
   **                            <br>
   **                            Allowed object is a {@link Object}.
   ** @param  name               the {@link Attribute} name to set.
   **                            <br>
   **                            Allowed object is a {@link String}.
   ** @param  clazz              the class to set for this {@link Object}
   **                            <br>
   **                            Allowed object is a {@link Class}.
   **
   ** @return                    the atttribute populated.
   **                            Possible object is a {@link Attribute}.
   */
  private static Attribute buildAttribute(final Object value, final String name, final Class<?> clazz) {
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
  // Method:   collect
  /**
   ** Converts the specified Jira user resource to the transfer options
   ** required by the Identity Connector Framework.
   **
   ** @param  resource           the Jira user resource to manipulate.
   **                            <br>
   **                            Allowed object is {@link User}.
   ** @param  attribute          the attribute to transfer.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   */
  private static void collect(final User resource, final Attribute attribute) {
    Object value = attribute.getValue().get(0);
    // only NON-READ-ONLY fields here
    // Attention: any request to set or reset a password is ignored by the
    // Service Provider hence we will not handle it here
    switch (attribute.getName()) {
      case oracle.iam.identity.icf.schema.Attribute.IDENTIFIER :
      case IDENTIFIER       : resource.id(String.class.cast(value));
                              break;
      case oracle.iam.identity.icf.schema.Attribute.UNIQUE :
      case USERNAME         : resource.userName(String.class.cast(value));
                              break;
      case oracle.iam.identity.icf.schema.Attribute.PASSWORD :
      case PASSWORD         : resource.password(CredentialAccessor.string(GuardedString.class.cast(value)));
                              break;
      case DISPLAYNAME      : resource.displayName(String.class.cast(value));
                              break;
      case EMAILADDRESS     : resource.email(String.class.cast(value));
                              break;
      case LOCALE           : resource.locale(String.class.cast(value));
                              break;
      case TIMEZONE         : resource.timeZone(String.class.cast(value));
                              break;
      case oracle.iam.identity.icf.schema.Attribute.STATUS   : resource.active(Boolean.class.cast(value));
                              break;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collect
  /**
   ** Converts the specified  user resource to the transfer options
   ** required by the Identity Connector Framework.
   **
   ** @param  resource           the Jira user resource to manipulate.
   **                            <br>
   **                            Allowed object is {@link Project}.
   ** @param  name               the name of the attribute to manipulate.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link List} of values to transfer.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Object}.
   */
  private static void collect(final Project resource, final Attribute attribute) {
    Object value = attribute.getValue().get(0);
    // only NON-READ-ONLY fields here
    switch (attribute.getName()) {
      case oracle.iam.identity.icf.schema.Attribute.IDENTIFIER :
      case IDENTIFIER       : resource.key(String.class.cast(value));
                              break;
      case oracle.iam.identity.icf.schema.Attribute.UNIQUE : resource.name(String.class.cast(value));
                              break;
      case TYPE             : resource.type(String.class.cast(value));
                              break;
      case ADMINISTRATOR    : User user = new User();
                              for (Attribute cursor : (List<Attribute>)value)
                                collect(user, cursor);
                              resource.administrator(user);
                              break;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collectScoped
  /**
   ** Converts the specified REST resource attribute to the transfer options
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
   ** @param  resource           the Jira resource identifiers providing the data
   **                            to obtain.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Pair}.
   */
  private static void collectScoped(final ConnectorObjectBuilder collector, final ObjectClass type, final List<Pair<String, List<String>>> resource) {
    if (!CollectionUtility.empty(resource)) {
      final List<EmbeddedObject> permission = new ArrayList<EmbeddedObject>();
      for (Pair<String, List<String>> i : resource) {
        for (String j : i.getValue()) {
          permission.add(new EmbeddedObjectBuilder().setObjectClass(type).addAttribute(Uid.NAME, i.getKey()).addAttribute(Name.NAME, j).build());
        }
      }
      // add the collection to the connector object builder
      collector.addAttribute(type.getObjectClassValue(), permission);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collectResource
  /**
   ** Converts the specified REST resource attribute to the transfer options
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
   ** @param  resource           the Jira resource identifiers providing the data
   **                            to obtain.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Pair}.
   */
  private static void collectResource(final ConnectorObjectBuilder collector, final ObjectClass type, final List<String> resource) {
    if (!CollectionUtility.empty(resource)) {
      final List<EmbeddedObject> permission = new ArrayList<EmbeddedObject>();
      for (String i : resource) {
        permission.add(new EmbeddedObjectBuilder().setObjectClass(type).addAttribute(Uid.NAME, i).addAttribute(Name.NAME, i).build());
      }
      // add the collection to the connector object builder
      collector.addAttribute(type.getObjectClassValue(), permission);
    }
  }
}