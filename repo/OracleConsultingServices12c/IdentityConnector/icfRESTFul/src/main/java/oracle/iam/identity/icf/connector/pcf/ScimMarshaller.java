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

    File        :   ScimMarshaller.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    ScimMarshaller.


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

import org.identityconnectors.common.security.GuardedString;

import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.AttributeUtil;
import org.identityconnectors.framework.common.objects.ConnectorObject;
import org.identityconnectors.framework.common.objects.OperationalAttributes;
import org.identityconnectors.framework.common.objects.ConnectorObjectBuilder;

import oracle.iam.identity.icf.foundation.object.Pair;

import oracle.iam.identity.icf.foundation.utility.SchemaUtility;
import oracle.iam.identity.icf.foundation.utility.CollectionUtility;
import oracle.iam.identity.icf.foundation.utility.CredentialAccessor;

import oracle.iam.identity.icf.rest.ServiceException;

import oracle.iam.identity.icf.scim.schema.Factory;
import oracle.iam.identity.icf.scim.schema.Metadata;

import oracle.iam.identity.icf.scim.v1.request.Operation;

import oracle.iam.identity.icf.connector.pcf.scim.schema.Name;
import oracle.iam.identity.icf.connector.pcf.scim.schema.Email;
import oracle.iam.identity.icf.connector.pcf.scim.schema.Group;
import oracle.iam.identity.icf.connector.pcf.scim.schema.Member;
import oracle.iam.identity.icf.connector.pcf.scim.schema.PhoneNumber;
import oracle.iam.identity.icf.connector.pcf.scim.schema.UserResource;
import oracle.iam.identity.icf.connector.pcf.scim.schema.GroupResource;

////////////////////////////////////////////////////////////////////////////////
// class ScimMarshaller
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** An interface to transfer PCF SCIM resource to and from Identity Connector
 ** attribute collections.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ScimMarshaller extends oracle.iam.identity.icf.connector.pcf.Marshaller {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The name of the identifier name attribute */
  public static final String IDENTIFIER = "id";

  /** The name of the user name attribute */
  public static final String USERNAME   = "userName";

  /** The name of the password attribute */
  public static final String PASSWORD   = "password";

  /** The name of the status attribute */
  public static final String STATUS     = "active";

  /** The name of the group name attribute */
  public static final String GROUPNAME  = "displayName";

  /** The name of the member attribute */
  public static final String MEMBER     = "members";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor (private)
  /**
   ** Default constructor
   ** <br>
   ** Access modifier private prevents other classes using
   ** "new ScimMarshaller()"
   */
  private ScimMarshaller() {
    // should never be instantiated
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   inboundUser
  /**
   ** Factory method to create a new {@link UserResource} instance and transfer
   ** the specified {@link Set} of {@link Attribute}s to the PCF user
   ** resource.
   **
   ** @param  attribute          the {@link Set} of {@link Attribute}s to set
   **                            on the PCF user resource.
   **                            <br>
   **                            Allowed object is a {@link Set} where each
   **                            elemment is of type {@link Attribute}.
   **
   ** @return                    the PCF user resource populated by the
   **                            {@link Set} of {@link Attribute}s
   **                            <br>
   **                            Possible object is a {@link UserResource}.
   */
  public static UserResource inboundUser(final Set<Attribute> attribute) {
    final UserResource resource = new UserResource();
    for (Attribute cursor : attribute) {
      if (!CollectionUtility.empty(cursor.getValue())) {
        collect(resource, cursor);
      }
    }
    return resource;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   inboundUser
  /**
   ** Factory method to create a {@link List} of {@link Operation} driven by
   ** the specified {@link Set} of {@link Attribute}s to modify the PCF user
   ** resource.
   **
   ** @param  attribute          the {@link Set} of {@link Attribute}s to set
   **                            on the PCF user resource.
   **                            <br>
   **                            Allowed object is a {@link Set} where each
   **                            elemment is of type {@link Attribute}.
   ** @param  remove             <code>true</code> if the {@link Attribute}s
   **                            provided belongs to a remove operation.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the PCF patch operations populated from the
   **                            {@link Set} of {@link Attribute}s.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Operation}.
   **
   ** @throws ServiceException   if the path to the attribute to modify becomes
   **                            invalid.
   */
  public static List<Operation> inboundUser(final Set<Attribute> attribute, final boolean remove)
    throws ServiceException {

    final List<Operation> operation = new ArrayList<Operation>();
    for (Attribute cursor : attribute) {
      final String       name  = cursor.getName();
      final List<Object> value = cursor.getValue();
      if (!CollectionUtility.empty(value)) {
        // only NON-READ-ONLY fields here
        switch (name) {
          case SchemaUtility.UID     : operation.add(Operation.replace(USERNAME, AttributeUtil.getAsStringValue(cursor)));
                                       break;
          case SchemaUtility.STATUS  : operation.add(Operation.replace(STATUS, AttributeUtil.getBooleanValue(cursor)));
                                       break;
          case SchemaUtility.PASSWORD : operation.add(Operation.replace(PASSWORD, CredentialAccessor.string(GuardedString.class.cast(value.get(0)))));
                                        break;
                                        // PCF is not willing to modify a user
                                        // resource if only the value is provided
                                        // by the patch operation
                                        // the primray attribute of an e-Mail has
                                        // also to be set
                                        // on the other hand only one e-mail is
                                        // supported so why this bullshit
                                        // constraint exists
                                        // furthermore the primary attribute is
                                        // never changed; its returned always as
                                        // false; consistency is something else
                                        // and not the strength of PCF
          case "emails.value"        : operation.add(Operation.replace(Email.PREFIX, CollectionUtility.list(new Email().primary(true).value(AttributeUtil.getAsStringValue(cursor)))));
                                       break;
          case "phoneNumbers.value"  : operation.add(Operation.replace(PhoneNumber.PREFIX, value));
                                       break;
          default                    : operation.add(Operation.replace(name, AttributeUtil.getAsStringValue(cursor)));
                                       break;
        }
      }
    }
    return operation;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   inboundGroup
  /**
   ** Factory method to create a new {@link GroupResource} instance and transfer
   ** the specified {@link Set} of {@link Attribute}s to the PCF group
   ** resource.
   **
   ** @param  attribute          the {@link Set} of {@link Attribute}s to set
   **                            on the PCF group resource.
   **                            <br>
   **                            Allowed object is a {@link Set} where each
   **                            elemment is of type {@link Attribute}.
   **
   ** @return                    the PCF group resource populated by the
   **                            {@link Set} of {@link Attribute}s
   **                            <br>
   **                            Possible object is a {@link GroupResource}.
   */
  public static GroupResource inboundGroup(final Set<Attribute> attribute) {
    final GroupResource resource = new GroupResource();
    for (Attribute cursor : attribute) {
      if (!CollectionUtility.empty(cursor.getValue())) {
        collect(resource, cursor);
      }
    }
    return resource;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   inboundGroup
  /**
   ** Factory method to create a {@link List} of {@link Operation} driven by
   ** the specified {@link Set} of {@link Attribute}s to modify the PCF group
   ** resource.
   **
   ** @param  attribute          the {@link Set} of {@link Attribute}s to set
   **                            on the PCF group resource.
   **                            <br>
   **                            Allowed object is a {@link Set} where each
   **                            elemment is of type {@link Attribute}.
   ** @param  remove             <code>true</code> if the {@link Attribute}s
   **                            provided belongs to a remove operation.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the PCF patch operations populated from the
   **                            {@link Set} of {@link Attribute}s.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Operation}.
   **
   ** @throws ServiceException   if the path to the attribute to modify becomes
   **                            invalid.
   */
  public static List<Operation> inboundGroup(final Set<Attribute> attribute, final boolean remove)
    throws ServiceException {

    final List<Operation> operation = new ArrayList<Operation>();
    for (Attribute cursor : attribute) {
      final String       name  = cursor.getName();
      final List<Object> value = cursor.getValue();
      if (!CollectionUtility.empty(value)) {
        // only NON-READ-ONLY fields here
        switch (name) {
          case SchemaUtility.NAME   : operation.add(Operation.replace(GROUPNAME, AttributeUtil.getAsStringValue(cursor)));
                                      break;
          case SchemaUtility.STATUS : operation.add(Operation.replace(STATUS, AttributeUtil.getBooleanValue(cursor)));
                                      break;
          case MEMBER               : operation.add(remove ? Operation.remove(MEMBER, value) : Operation.replace(MEMBER, value));
                                      break;
          default                   : operation.add(Operation.replace(name, AttributeUtil.getAsStringValue(cursor)));
                                      break;
        }
      }
    }
    return operation;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectorObject
  /**
   ** Transforms the data received from the Service Provider and wrapped in the
   ** specified PCF {@link UserResource} <code>user</code> to the
   ** {@link ConnectorObjectBuilder}.
   **
   ** @param  resource           the specified PCF {@link UserResource} to
   **                            build.
   **                            <br>
   **                            Allowed object is {@link UserResource}.
   ** @param  filter             the {@link Set} of attribute names requested to
   **                            be set in the {@link ConnectorObjectBuilder}.
   **                            <br>
   **                            If this passed as <code>null</code> all
   **                            registred attributes are returned
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   ** @param  groups             the {@link List} of SCIM group resource
   **                            identifiers to collect.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link String}.
   ** @param  tenants            the {@link List} of Cloud Controller tenant
   **                            resource identifiers  to collect.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Pair}.
   ** @param  spaces             the {@link List} of Cloud Controller space
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
  static ConnectorObject connectorObject(final UserResource resource, final Set<String> filter, final List<String> groups, final List<Pair<String, List<String>>> tenants, final List<Pair<String, List<String>>> spaces)
    throws IllegalAccessException
    ,      IllegalArgumentException {

    final ConnectorObjectBuilder builder    = new ConnectorObjectBuilder().setObjectClass(ObjectClass.ACCOUNT);
    final Collection<Field>      properties = Factory.describe(UserResource.class);
    for (Field property : properties) {
      // skip any property that have to be ignored
      if (property.isAnnotationPresent(JsonIgnore.class))
        continue;

      final String genericType = property.getGenericType().toString();
      if (genericType.contains(Name.class.getName())) {
        collect(builder, filter, Name.PREFIX, resource.name());
      }
      else if (genericType.contains(Email.class.getName())) {
        for (Email cursor : resource.email()) {
          collect(builder, filter, Email.PREFIX, cursor);
        }
      }
      else if (genericType.contains(PhoneNumber.class.getName())) {
        final List<PhoneNumber> collection = resource.phoneNumber();
        if (collection != null) {
          for (PhoneNumber cursor : collection) {
            collect(builder, filter, PhoneNumber.PREFIX, cursor);
          }
        }
      }
      else if (genericType.contains(Metadata.class.getName())) {
        collect(builder, filter, Metadata.PREFIX, resource.metadata());
      }
      else {
        // verify if the field is subject matter
        final String propertyName = property.getName();
        if (filter == null || (filter != null && filter.contains(propertyName))) {
          switch (propertyName) {
            case IDENTIFIER : builder.setUid(resource.id());
                              break;
            case USERNAME   : builder.setName(resource.userName());
                              break;
            case STATUS     : builder.addAttribute(buildAttribute(resource.active(), OperationalAttributes.ENABLE_NAME, boolean.class));
                              break;
            case PASSWORD   : // we will never return an password back to the
                              // client regardless if its possible in general
                              // and requested by the clientbreak;
                              break;
            default         : // ensure that the field is accessible regardless
                              // which modifier the field is assinged to
                              property.setAccessible(true);
                              builder.addAttribute(buildAttribute(property.get(resource), propertyName, property.getType()));
                              break;
          }
        }
      }
    }
    // handle multi-valued attributes as embebbed collections
    if (!CollectionUtility.empty(groups)) {
      // add the collection to the connector object builder
      collect(builder, ObjectClass.GROUP, groups);
    }
    if (!CollectionUtility.empty(tenants)) {
      collectScoped(builder, RestMarshaller.TENANT, tenants);
    }
    if (!CollectionUtility.empty(spaces)) {
      collectScoped(builder, RestMarshaller.SPACE, spaces);
    }
    return builder.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectorObject
  /**
   ** Transforms the data received from the Service Provider and wrapped in the
   ** specified PCF {@link GroupResource} <code>group</code> to the
   ** {@link ConnectorObjectBuilder}.
   **
   ** @param  resource           the specified PCF {@link GroupResource} to
   **                            build.
   **                            <br>
   **                            Allowed object is {@link UserResource}.
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
   **                            {@link ConnectorObjectBuilder} factory.
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
  static ConnectorObject connectorObject(final GroupResource resource, final Set<String> filter)
    throws IllegalAccessException
    ,      IllegalArgumentException {

    final ConnectorObjectBuilder builder    = new ConnectorObjectBuilder().setObjectClass(ObjectClass.ACCOUNT);
    final Collection<Field>      properties = Factory.describe(GroupResource.class);
    for (Field property : properties) {
      // skip any property that have to be ignored
      if (property.isAnnotationPresent(JsonIgnore.class))
        continue;

      if (property.getGenericType().toString().contains(Member.class.getName())) {
//        collect(builder, filter, ObjectClass.ACCOUNT, resource.member());
      }
      else {
        // verify if the field is subject matter
        final String propertyName = property.getName();
        if (filter == null || (filter != null && filter.contains(propertyName))) {
          switch (propertyName) {
            case IDENTIFIER : builder.setUid(resource.id());
                              break;
            case GROUPNAME  : builder.setName(resource.display());
                              break;
            default         : // ensure that the field is accessible regardless
                              // which modifier the field is assinged to
                              property.setAccessible(true);
                              builder.addAttribute(buildAttribute(property.get(resource), propertyName, property.getType()));
                              break;
          }
        }
      }
    }
    return builder.build();
  }
//////////////////////////////////////////////////////////////////////////////
  // Method:   collect
  /**
   ** Converts the specified PCF user resource to the transfer options
   ** required by the Identity Connector Framework.
   **
   ** @param  resource           the PCF user resource to manipulate.
   **                            <br>
   **                            Allowed object is {@link UserResource}.
   ** @param  name               the name of the attribute to manipulate.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link List} of values to transfer.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Object}.
   */
  private static void collect(final UserResource resource, final Attribute attribute) {
    Object value = attribute.getValue().get(0);
    // only NON-READ-ONLY fields here
    switch (attribute.getName()) {
      case SchemaUtility.UID    :
      case IDENTIFIER           : resource.id(String.class.cast(value));
                                  break;
      case SchemaUtility.NAME   :
      case USERNAME             : resource.userName(String.class.cast(value));
                                  break;
      case SchemaUtility.STATUS :
      case STATUS               : resource.active(Boolean.class.cast(value));
                                  break;
      case SchemaUtility.PASSWORD :
      case PASSWORD               : resource.password(CredentialAccessor.string(GuardedString.class.cast(value)));
                                    break;
      case "origin"               : resource.origin(String.class.cast(value));
                                    break;
      case "zoneId"               : resource.zone(String.class.cast(value));
                                    break;
      case "verified"             : resource.verified("1".equals(String.class.cast(value)));
                                    break;
      case "name.familyName"      : name(resource).familyName(String.class.cast(value));
                                    break;
      case "name.givenName"       : name(resource).givenName(String.class.cast(value));
                                    break;
      case "emails.primary"       : email(resource).primary(Boolean.class.cast(value));
                                    break;
      case "emails.value"         : email(resource).value(String.class.cast(value));
                                    break;
      case "phoneNumbers.value"   : phoneNumber(resource).value(String.class.cast(value));
                                    break;
      case "groups"               : final List<Group> collector = group(resource);
                                    for (Object cursor : attribute.getValue()) {
                                      collector.add(new Group().value(String.class.cast(cursor)));
                                    }
                                    break;
      case "schemas"              : resource.namespace((List<String>)value);
                                    break;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collect
  /**
   ** Converts the specified PCF group resource to the transfer options
   ** required by the Identity Connector Framework.
   **
   ** @param  resource           the PCF group resource to manipulate.
   **                            <br>
   **                            Allowed object is {@link Group}.
   ** @param  name               the name of the attribute to manipulate.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link List} of values to transfer.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Object}.
   */
  private static void collect(final GroupResource resource, final Attribute attribute) {
    Object value = attribute.getValue().get(0);
    // only NON-READ-ONLY fields here
    switch (attribute.getName()) {
      case SchemaUtility.UID  : resource.id(String.class.cast(value));
                                break;
      case SchemaUtility.NAME : resource.display(String.class.cast(value));
                                break;
      case "description"      : resource.description(String.class.cast(value));
                                break;
      case "zoneId"           : resource.zoneId(String.class.cast(value));
                                break;
      case MEMBER             : final List<Member> collector = member(resource);
                                for (Object cursor : attribute.getValue()) {
                                  collector.add(new Member().origin(Member.USER).type(Member.USER).value(String.class.cast(cursor)));
                                }
                                break;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Factory method to create and associate a name with the specified PCF user
   ** resource.
   **
   ** @param  resource           the PCF user resource to manipulate.
   **                            <br>
   **                            Allowed object is {@link UserResource}.
   **
   ** @return                    the {@link Name} instance.
   **                            <br>
   **                            Possible object is {@link Name}.
   */
  private static Name name(final UserResource resource) {
    Name current = resource.name();
    if (current == null) {
      current = new Name();
      resource.name(current);
    }
    return current;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   email
  /**
   ** Factory method to create and associate a email with the specified PCF user
   ** resource.
   ** <p>
   ** PCF allows only one e-Mail per user regardless what the RFC specifies.
   **
   ** @param  resource           the PCF user resource to manipulate.
   **                            <br>
   **                            Allowed object is {@link UserResource}.
   ** @param  type               the type of the email resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link Email} instance.
   **                            <br>
   **                            Possible object is {@link Email}.
   */
  private static Email email(final UserResource resource) {
    List<Email> current = resource.email();
    if (current == null) {
      current = new ArrayList<Email>();
      current.add(new Email());
      resource.email(current);
    }
    return current.get(0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   phoneNumber
  /**
   ** Factory method to create and associate a phone number with the specified
   ** PCF user resource.
   **
   ** @param  resource           the PCF user resource to manipulate.
   **                            <br>
   **                            Allowed object is {@link UserResource}.
   **
   ** @return                    the {@link PhoneNumber} instance.
   **                            <br>
   **                            Possible object is {@link PhoneNumber}.
   */
  private static PhoneNumber phoneNumber(final UserResource resource) {
    List<PhoneNumber> current = resource.phoneNumber();
    if (current == null) {
      current = new ArrayList<PhoneNumber>();
      resource.phoneNumber(current);
    }
    PhoneNumber selected = new PhoneNumber();
    current.add(selected);
    return selected;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   group
  /**
   ** Factory method to create and associate a group with the specified PCF user
   ** resource.
   **
   ** @param  resource           the PCF user resource to manipulate.
   **                            <br>
   **                            Allowed object is {@link UserResource}.
   **
   ** @return                    the {@link List} of {@link Group}s associated
   **                            with the PCF user resource so far.
   **                            <br>
   **                            Possible object {@link List} where each element
   **                            id of type [@link Group}.
   */
  private static List<Group> group(final UserResource resource) {
    List<Group> current = resource.groups();
    if (current == null) {
      current = new ArrayList<Group>();
      resource.groups(current);
    }
    return current;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   member
  /**
   ** Factory method to create and associate a member with the specified PCF
   ** group resource.
   **
   ** @param  resource           the PCF user resource to manipulate.
   **                            <br>
   **                            Allowed object is {@link UserResource}.
   **
   ** @return                    the {@link List} of {@link Group}s associated
   **                            with the PCF user resource so far.
   **                            <br>
   **                            Possible object {@link List} where each element
   **                            id of type [@link Group}.
   */
  private static List<Member> member(final GroupResource resource) {
    List<Member> current = resource.members();
    if (current == null) {
      current = new ArrayList<Member>();
      resource.members(current);
    }
    return current;
  }
}