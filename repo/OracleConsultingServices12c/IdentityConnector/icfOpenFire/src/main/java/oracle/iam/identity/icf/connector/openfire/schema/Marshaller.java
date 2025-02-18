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
    Subsystem   :   Openfire Database Connector

    File        :   Marshaller.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Marshaller.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-10-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.openfire.schema;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;

import org.identityconnectors.common.security.GuardedString;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.EmbeddedObject;
import org.identityconnectors.framework.common.objects.ConnectorObject;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.ConnectorObjectBuilder;
import org.identityconnectors.framework.common.objects.EmbeddedObjectBuilder;

import oracle.iam.identity.icf.foundation.utility.SchemaUtility;
import oracle.iam.identity.icf.foundation.utility.StringUtility;
import oracle.iam.identity.icf.foundation.utility.CollectionUtility;
import oracle.iam.identity.icf.foundation.utility.CredentialAccessor;

////////////////////////////////////////////////////////////////////////////////
// final class Marshaller
// ~~~~~ ~~~~~ ~~~~~~~~~~
/**
 ** An interface to transfer Openfire database resources to and from Identity
 ** Connector attribute collections.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class Marshaller {

  /** The object class name for a chat room entitlement. */
  public static final String      ROOM_NAME       = "__ROOM__";

  /** The object class for a chat room entitlement. */
  public static final ObjectClass ROOM            = new ObjectClass(ROOM_NAME);

  /** The prefix for a generic property. */
  public static final String      PROPERTY_PREFIX = "property.";

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
   ** specified {@link Set} of {@link Attribute}s to the Openfire {@link User}
   ** resource.
   **
   ** @param  attribute          the {@link Set} of {@link Attribute}s to set
   **                            on the Openfire {@link User} resource.
   **                            <br>
   **                            Allowed object is a {@link Set} where each
   **                            elemment is of type {@link Attribute}.
   **
   ** @return                    the Openfire {@link User} resource populated
   **                            from the {@link Set} of {@link Attribute}s.
   **                            <br>
   **                            Possible object is a {@link User}.
   */
  public static User inboundUser(final Set<Attribute> attribute) {
    final User resource = User.build();
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
   ** Factory method to create a new {@link Group} instance and transfer the
   ** specified {@link Set} of {@link Attribute}s to the Openfire {@link Group}
   ** resource.
   **
   ** @param  attribute          the {@link Set} of {@link Attribute}s to set
   **                            on the Openfire {@link Group} resource.
   **                            <br>
   **                            Allowed object is a {@link Set} where each
   **                            elemment is of type {@link Attribute}.
   **
   ** @return                    the Openfire {@link Group} resource populated
   **                            from the {@link Set} of {@link Attribute}s.
   **                            <br>
   **                            Possible object is a {@link Group}.
   */
  public static Group inboundGroup(final Set<Attribute> attribute) {
    final Group resource = Group.build();
    for (Attribute cursor : attribute) {
      if (!CollectionUtility.empty(cursor.getValue())) {
        collect(resource, cursor);
      }
    }
    return resource;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   inboundMember
  /**
   ** Factory method to create a new {@link Member} instance and transfer the
   ** specified {@link Set} of {@link Attribute}s to the Openfire {@link Member}
   ** resource.
   **
   ** @param  attribute          the {@link Set} of {@link Attribute}s to set
   **                            on the Openfire {@link Member} resource.
   **                            <br>
   **                            Allowed object is a {@link Set} where each
   **                            elemment is of type {@link Attribute}.
   **
   ** @return                    the Openfire {@link Member} resource populated
   **                            from the {@link Set} of {@link Attribute}s.
   **                            <br>
   **                            Possible object is a {@link Member}.
   */
  public static Member inboundMember(final Set<Attribute> attribute) {
    final Member resource = Member.build();
    for (Attribute cursor : attribute) {
      if (!CollectionUtility.empty(cursor.getValue())) {
        collect(resource, cursor);
      }
    }
    return resource;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   inboundRoomMate
  /**
   ** Factory method to create a new {@link RoomMate} instance and transfer the
   ** specified {@link Set} of {@link Attribute}s to the Openfire
   ** {@link RoomMate} resource.
   **
   ** @param  attribute          the {@link Set} of {@link Attribute}s to set
   **                            on the Openfire {@link RoomMate} resource.
   **                            <br>
   **                            Allowed object is a {@link Set} where each
   **                            elemment is of type {@link Attribute}.
   **
   ** @return                    the Openfire {@link RoomMate} resource
   **                            populated from the {@link Set} of
   **                            {@link Attribute}s.
   **                            <br>
   **                            Possible object is a {@link RoomMate}.
   */
  public static RoomMate inboundRoomMate(final Set<Attribute> attribute) {
    final RoomMate resource = RoomMate.build();
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
   ** specified Openfire {@link User} <code>user</code> to the
   ** {@link ConnectorObjectBuilder}.
   **
   ** @param  resource           the specified Openfire {@link User} to
   **                            transfer.
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
   ** @param  memberOf           the collection of groups the {@link User} is
   **                            member of.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Member}.
   ** @param  occupancy          the collection of rooms the {@link User}
   **                            occupies.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link RoomMate}.
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
  public static ConnectorObject connectorObject(final User resource, final Set<String> filter, final List<Member> memberOf, final List<RoomMate> occupancy)
    throws IllegalAccessException
    ,      IllegalArgumentException {

    final ConnectorObjectBuilder builder = new ConnectorObjectBuilder().setObjectClass(ObjectClass.ACCOUNT);
    // ICF is such a stupid framework that enforce that every Service
    // Provider has to look like a LDAP system
    // we need to put attributes that isn't needed only to satisfay the
    // constraints of this bullshit
    // always set __UID__ and __NAME__ attribute to convince stupid framework
    // developer
    builder.setUid(resource.uid());
    builder.setName(resource.uid());
    for (String cursor : filter) {
      switch (cursor) {
        // skip what already done
        case User.UID           :
        case SchemaUtility.UID  :
        case SchemaUtility.NAME : break;
        default                 : // honor the dot to avoid that attributes
                                  // that might be start with similar names
                                  if (cursor.startsWith(PROPERTY_PREFIX)) {
                                    // the layered properties will be handled
                                    // afterwards
                                    continue;
                                  }
                                  else {
                                    final Object value = resource.get(cursor);
                                    builder.addAttribute(buildAttribute(value, cursor,value.getClass()));
                                  }
                                  break;
      }
    }
    // populate the layered attribute property
    if (!CollectionUtility.empty(resource.property)) {
      for (Entity.Property cursor : resource.property) {
        // the provider suplies all properties hence we have to filter out those
        // that are requested
        final String fqan = PROPERTY_PREFIX.concat(cursor.name());
        if (filter.contains(fqan))
          builder.addAttribute(fqan, cursor.value());
      }
    }
    if (!CollectionUtility.empty(memberOf)) {
      // add the collection to the connector object builder
      collectMemberOf(builder, ObjectClass.GROUP, memberOf);
    }
    if (!CollectionUtility.empty(occupancy)) {
      // add the collection to the connector object builder
      collectOccupancy(builder, Marshaller.ROOM, occupancy);
    }
    return builder.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectorObject
  /**
   ** Transforms the data received from the Service Provider and wrapped in the
   ** specified Openfire {@link Group} <code>group</code> to the
   ** {@link ConnectorObjectBuilder}.
   **
   ** @param  resource           the specified Openfire {@link Group} to
   **                            transfer.
   **                            <br>
   **                            Allowed object is {@link Group}.
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
  public static ConnectorObject connectorObject(final Group resource, final Set<String> filter)
    throws IllegalAccessException
    ,      IllegalArgumentException {

    final ConnectorObjectBuilder builder = new ConnectorObjectBuilder().setObjectClass(ObjectClass.GROUP);
    // ICF is such a stupid framework that enforce that every Service
    // Provider has to look like a LDAP system
    // we need to put attributes that isn't needed only to satisfay the
    // constraints of this bullshit
    // always set __UID__ and __NAME__ attribute to convince stupid framework
    // developer
    builder.setUid(resource.gid());
    builder.setName(resource.gid());
    for (String cursor : filter) {
      switch (cursor) {
        // skip what already done
        case SchemaUtility.UID  :
        case SchemaUtility.NAME : break;
        default                 : final Object value = resource.get(cursor);
                                  // if a requested value isn't provided we have
                                  // to simulate a valid configuration
                                  builder.addAttribute(buildAttribute(value, cursor, (value == null) ? String.class : value.getClass()));
                                  break;
      }
    }
    return builder.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectorObject
  /**
   ** Transforms the data received from the Service Provider and wrapped in the
   ** specified Openfire {@link Group} <code>group</code> to the
   ** {@link ConnectorObjectBuilder}.
   **
   ** @param  resource           the specified Openfire {@link Group} to
   **                            transfer.
   **                            <br>
   **                            Allowed object is {@link Group}.
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
  public static ConnectorObject connectorObject(final Room resource, final Set<String> filter)
    throws IllegalAccessException
    ,      IllegalArgumentException {

    final ConnectorObjectBuilder builder = new ConnectorObjectBuilder().setObjectClass(ObjectClass.GROUP);
    // ICF is such a stupid framework that enforce that every Service
    // Provider has to look like a LDAP system
    // we need to put attributes that isn't needed only to satisfay the
    // constraints of this bullshit
    // always set __UID__ and __NAME__ attribute to convince stupid framework
    // developer
    builder.setUid(resource.rid());
    builder.setName(resource.rid());
    for (String cursor : filter) {
      switch (cursor) {
        // skip what already done
        case SchemaUtility.UID  :
        case SchemaUtility.NAME : break;
        default                 : final Object value = resource.get(cursor);
                                  // if a requested value isn't provided we have
                                  // to simulate a valid configuration
                                  builder.addAttribute(buildAttribute(value, cursor, (value == null) ? String.class : value.getClass()));
                                  break;
      }
    }
    return builder.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collect
  /**
   ** Collects ICF {@link Attribute} value to the Openfire user resource by
   ** converting it approprietly.
   **
   ** @param  resource           the Openfire user resource to populate.
   **                            <br>
   **                            Allowed object is {@link User}.
   ** @param  attribute          the ICF attribute values to transfer.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   */
  private static void collect(final User resource, final Attribute attribute) {
    final String name = attribute.getName();
    switch (name) {
      case SchemaUtility.UID       :
      case SchemaUtility.NAME      :
      case User.UID                : resource.uid(SchemaUtility.notNullStringValue(attribute));
                                     break;
      case User.NAME               : resource.name(SchemaUtility.notNullStringValue(attribute));
                                     break;
      case SchemaUtility.STATUS    : resource.locked(!SchemaUtility.booleanValue(attribute));
                                     break;
      case SchemaUtility.PASSWORD  :
      case User.PASSWORD_ENCRYPTED : resource.passwordEncrypted(CredentialAccessor.string(SchemaUtility.singleValue(attribute, GuardedString.class)));
                                     break;
      case User.PASSWORD_PLAINTEXT : resource.passwordPlaintext(CredentialAccessor.string(SchemaUtility.singleValue(attribute, GuardedString.class)));
                                     break;
      case User.EMAIL              : resource.email(SchemaUtility.stringValue(attribute));
                                     break;
      case User.ADMIN              : resource.administrator(SchemaUtility.booleanValue(attribute));
                                     break;
      default                      : // honor the dot to avoid that attributes
                                     // that might be start with similar names
                                     if (name.startsWith(PROPERTY_PREFIX)) {
                                       name.substring(PROPERTY_PREFIX.length());
                                       resource.add(name.substring(PROPERTY_PREFIX.length()), SchemaUtility.stringValue(attribute));
                                     }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collect
  /**
   ** Collects ICF {@link Attribute} value to the Openfire group resource by
   ** converting it approprietly.
   **
   ** @param  resource           the Openfire group to populate.
   **                            <br>
   **                            Allowed object is {@link Group}.
   ** @param  attribute          the ICF attribute values to transfer.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   */
  private static void collect(final Group resource, final Attribute attribute) {
    final String name = attribute.getName();
    switch (name) {
      case SchemaUtility.UID  :
      case SchemaUtility.NAME :
      case Group.GID          : resource.gid(SchemaUtility.notNullStringValue(attribute));
                                break;
      case Group.DESCRIPTION  : resource.description(SchemaUtility.stringValue(attribute));
                                break;
      default                 : // honor the dot to avoid that attributes
                                // that might be start with similar names
                                if (name.startsWith(PROPERTY_PREFIX)) {
                                  name.substring(PROPERTY_PREFIX.length());
                                  resource.add(name.substring(PROPERTY_PREFIX.length()), SchemaUtility.stringValue(attribute));
                                }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collect
  /**
   ** Collects ICF {@link Attribute} value to the Openfire group member
   ** resource by converting it approprietly.
   **
   ** @param  resource           the Openfire group member resource to populate.
   **                            <br>
   **                            Allowed object is {@link Member}.
   ** @param  attribute          the ICF attribute values to transfer.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   */
  private static void collect(final Member resource, final Attribute attribute) {
    switch (attribute.getName()) {
      case SchemaUtility.UID :
      case Member.GID        : resource.gid(SchemaUtility.notNullStringValue(attribute));
                               break;
      case Member.UID        : resource.uid(SchemaUtility.notNullStringValue(attribute));
                               break;
      case Member.ADM        : final String value = SchemaUtility.stringValue(attribute);
                               resource.administrator(StringUtility.empty(value) ? 0 : Integer.valueOf(value));
                               break;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collect
  /**
   ** Collects ICF {@link Attribute} value to the Openfire room occupant
   ** resource by converting it approprietly.
   **
   ** @param  resource           the Openfire room occupant resource to
   **                            populate.
   **                            <br>
   **                            Allowed object is {@link RoomMate}.
   ** @param  attribute          the ICF {@link Attribute} values to transfer.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   */
  private static void collect(final RoomMate resource, final Attribute attribute) {
    switch (attribute.getName()) {
      case SchemaUtility.UID       :
      case SchemaUtility.NAME      :
      case RoomMate.RID            : resource.rid(SchemaUtility.notNullStringValue(attribute));
                                     break;
      case RoomMate.NICKNAME       : resource.nickName(SchemaUtility.stringValue(attribute));
                                     break;
      case RoomMate.LASTNAME       : resource.lastName(SchemaUtility.stringValue(attribute));
                                     break;
      case RoomMate.FIRSTNAME      : resource.firstName(SchemaUtility.stringValue(attribute));
                                     break;
      case RoomMate.EMAIL          : resource.email(SchemaUtility.stringValue(attribute));
                                     break;
      case RoomMate.URL            : resource.url(SchemaUtility.stringValue(attribute));
                                     break;
      case RoomMate.FAQ            : resource.faq(SchemaUtility.stringValue(attribute));
                                     break;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collectMemberOf
  /**
   ** Converts the specified openfire member resource attribute to the transfer
   ** options required by the Identity Connector Framework.
   **
   ** @param  collector          the {@link ConnectorObjectBuilder} to collect
   **                            the attributes.
   **                            <br>
   **                            Allowed object is
   **                            {@link ConnectorObjectBuilder}.
   ** @param  type               the ICF object class.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}.
   ** @param  resource           the openfire member resource providing the data
   **                            to obtain.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Member}.
   */
  private static void collectMemberOf(final ConnectorObjectBuilder collector, final ObjectClass type, final List<Member> resource) {
    if (!CollectionUtility.empty(resource)) {
      final List<EmbeddedObject> permission = new ArrayList<EmbeddedObject>();
      for (Member i : resource) {
        permission.add(new EmbeddedObjectBuilder().setObjectClass(type).addAttribute(Uid.NAME, i.gid()).addAttribute(Member.ADM, i.administrator()).build());
      }
      // add the collection to the connector object builder
      collector.addAttribute(type.getObjectClassValue(), permission);
    }
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   collectOccupancy
  /**
   ** Converts the specified openfire roommate resource to the transfer options
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
   ** @param  resource           the openfire room resource providing the data
   **                            to obtain.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Member}.
   */
  private static void collectOccupancy(final ConnectorObjectBuilder collector, final ObjectClass type, final List<RoomMate> resource) {
    if (!CollectionUtility.empty(resource)) {
      final List<EmbeddedObject> permission = new ArrayList<EmbeddedObject>();
      for (RoomMate i : resource) {
        permission.add(
          new EmbeddedObjectBuilder()
            .setObjectClass(type)
            .addAttribute(Uid.NAME,           i.rid())
            .addAttribute(RoomMate.JID,       i.jid())
            .addAttribute(RoomMate.EMAIL,     i.email())
            .addAttribute(RoomMate.LASTNAME,  i.lastName())
            .addAttribute(RoomMate.FIRSTNAME, i.firstName())
            .addAttribute(RoomMate.NICKNAME,  i.nickName())
            .build());
      }
      // add the collection to the connector object builder
      collector.addAttribute(type.getObjectClassValue(), permission);
    }
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
        builder.addValue(String.class.cast(value));
      }
    }
    if (name != null) {
      builder.setName(name);
    }
    return builder.build();
  }
}