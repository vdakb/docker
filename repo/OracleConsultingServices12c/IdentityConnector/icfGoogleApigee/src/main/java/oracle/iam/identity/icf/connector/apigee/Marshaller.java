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
    Subsystem   :   Google Apigee Edge Connector

    File        :   Marshaller.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Marshaller.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-21-05  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.apigee;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import org.identityconnectors.common.security.GuardedString;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.EmbeddedObject;
import org.identityconnectors.framework.common.objects.ConnectorObject;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.EmbeddedObjectBuilder;
import org.identityconnectors.framework.common.objects.ConnectorObjectBuilder;

import oracle.iam.identity.icf.foundation.object.Pair;

import oracle.iam.identity.icf.foundation.utility.CollectionUtility;
import oracle.iam.identity.icf.foundation.utility.CredentialAccessor;

import static oracle.iam.identity.icf.foundation.utility.SchemaUtility.UID;
import static oracle.iam.identity.icf.foundation.utility.SchemaUtility.NAME;
import static oracle.iam.identity.icf.foundation.utility.SchemaUtility.PASSWORD;

import oracle.iam.identity.icf.connector.apigee.schema.User;
import oracle.iam.identity.icf.connector.apigee.schema.Tenant;
import oracle.iam.identity.icf.connector.apigee.schema.Developer;

////////////////////////////////////////////////////////////////////////////////
// class Marshaller
// ~~~~~ ~~~~~~~~~~
/**
 ** An interface to transfer Apigee REST resource to and from Identity Connector
 ** attribute collections.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Marshaller {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the object class name for a organization entitlement */
  public static final String TENANT_NAME      = "__TENANT__";

  /** the object class name for a product */
  public static final String PRODUCT_NAME     = "__PRODUCT__";

  /** the object class name for a product */
  public static final String APPLICATION_NAME = "__APPLICATION__";

  /** the object class name for a developer */
  public static final String DEVELOPER_NAME   = "__DEVELOPER__";

  /** the object class name for a organization entitlement */
  static final ObjectClass   TENANT           = new ObjectClass(TENANT_NAME);

  /** the object class name for a product */
  static final ObjectClass   PRODUCT          = new ObjectClass(PRODUCT_NAME);

  /** the object class name for an application */
  static final ObjectClass   APPLICATION      = new ObjectClass(APPLICATION_NAME);

  /** the object class name for a developer */
  static final ObjectClass   DEVELOPER        = new ObjectClass(DEVELOPER_NAME);

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
  // Method:   inboundTenant
  /**
   ** Factory method to create a new {@link Tenant} instance and transfer the
   ** specified {@link Set} of {@link Attribute}s to the Apigee user resource.
   **
   ** @param  attribute          the {@link Set} of {@link Attribute}s to set
   **                            on the Apigee tenant resource.
   **                            <br>
   **                            Allowed object is a {@link Set} where each
   **                            elemment is of type {@link Attribute}.
   **
   ** @return                    the Apigee tenant resource populated by the
   **                            {@link Set} of {@link Attribute}s
   **                            <br>
   **                            Possible object is a {@link Tenant}.
   */
  public static Tenant inboundTenant(final Set<Attribute> attribute) {
    final Tenant resource = new Tenant();
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
   ** Factory method to create a new {@link User} instance and transfer the
   ** specified {@link Set} of {@link Attribute}s to the Apigee user resource.
   **
   ** @param  attribute          the {@link Set} of {@link Attribute}s to set
   **                            on the Apigee user resource.
   **                            <br>
   **                            Allowed object is a {@link Set} where each
   **                            elemment is of type {@link Attribute}.
   **
   ** @return                    the Apigee user resource populated by the
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
  // Method:   inboundDeveloper
  /**
   ** Factory method to create a new {@link Developer} instance and transfer the
   ** specified {@link Set} of {@link Attribute}s to the Apigee developer
   ** resource.
   **
   ** @param  attribute          the {@link Set} of {@link Attribute}s to set
   **                            on the Apigee developer resource.
   **                            <br>
   **                            Allowed object is a {@link Set} where each
   **                            elemment is of type {@link Attribute}.
   **
   ** @return                    the Apigee developer resource populated by the
   **                            {@link Set} of {@link Attribute}s
   **                            <br>
   **                            Possible object is a {@link Developer}.
   */
  public static Developer inboundDeveloper(final Set<Attribute> attribute) {
    final Developer resource = new Developer();
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
   ** specified APIGEE {@link User} <code>user</code> to the
   ** {@link ConnectorObjectBuilder}.
   **
   ** @param  resource           the specified APIGEE <code>Edge User</code> to
   **                            transform.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is a {@link String} mapped to a
   **                            {@link Object}.
   ** @param  filter             the {@link Set} of attribute names requested to
   **                            be set in the {@link ConnectorObjectBuilder}.
   **                            <br>
   **                            If this passed as <code>null</code> all
   **                            registred attributes are returned
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   ** @param  tenants            the {@link List} of {@link Tenant} resource
   **                            identifiers to collect the permissions.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Pair}.
   **
   ** @return                    the transformation result wrapped in a
   **                            {@link ConnectorObject}.
   **                            <br>
   **                            Possible object {@link ConnectorObject}.
   */
  static ConnectorObject connectorObject(final Map<String, Object> resource, final Set<String> filter, final List<Pair<String, List<String>>> tenants) {
    final ConnectorObjectBuilder builder = new ConnectorObjectBuilder().setObjectClass(ObjectClass.ACCOUNT);
    // ICF is such a stupid framework that enforce that every Service Provider
    // has to look like a LDAP system
    // we need to put attributes that isn't needed only to satisfay the
    // constraints of this bullshit
    final String id = (String)resource.get(User.EMAIL);
    // always set __UID__ and __NAME__ attribute to convince stupid framework
    // developer
    builder.setUid(id).setName(id);
    if (filter.contains(User.LASTNAME))
      builder.addAttribute(buildAttribute(resource.get(User.LASTNAME),  User.LASTNAME,  String.class));
    if (filter.contains(User.FIRSTNAME))
      builder.addAttribute(buildAttribute(resource.get(User.FIRSTNAME), User.FIRSTNAME, String.class));
    // handle multi-valued attributes as embebbed collections
    if (!CollectionUtility.empty(tenants)) {
      // add the collection to the connector object builder
      collectScoped(builder, Marshaller.TENANT, tenants);
    }
    return builder.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectorObject
  /**
   ** Transforms the data received from the Service Provider and wrapped in the
   ** specified APIGEE {@link User} <code>user</code> to the
   ** {@link ConnectorObjectBuilder}.
   **
   ** @param  resource           the specified APIGEE {@link Tenant} resource to
   **                            transform.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is a {@link String} mapped to a
   **                            {@link Object}.
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
   */
  static ConnectorObject connectorObject(final Map<String, Object> resource, final ObjectClass type, final String identifier, final String uniqueName, final Set<String> filter) {
    final ConnectorObjectBuilder builder = new ConnectorObjectBuilder().setObjectClass(type);
    // ICF is such a stupid framework that enforce that every Service Provider
    // has to look like a LDAP system
    // we need to put attributes that isn't needed only to satisfay the
    // constraints of this bullshit
    builder.setUid((String)resource.get(identifier));
    builder.setName((String)resource.get(uniqueName));
    for (String cursor : filter) {
      // skip attributes that already set at the builder
      if (UID.equals(cursor) || NAME.equals(cursor) || identifier.equals(cursor) || uniqueName.equals(cursor))
        continue;

      if (cursor.startsWith("attributes")) {
        final String                    layered  = cursor.substring(cursor.indexOf('.') + 1);
        final List<Map<String, Object>> extended = (List<Map<String, Object>>)resource.get("attributes");
        for (Map<String, Object> pair : extended) {
          if (layered.equals(pair.get("name"))) {
            builder.addAttribute(buildAttribute(pair.get("value"), cursor, String.class));
            break;
          }
        }
      }
      else
        builder.addAttribute(buildAttribute(resource.get(cursor), cursor, String.class));
    }
    return builder.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collect
  /**
   ** Converts the specified APIGEE tenant resource to the transfer options
   ** required by the Identity Connector Framework.
   **
   ** @param  resource           the APIGEE tenant resource to manipulate.
   **                            <br>
   **                            Allowed object is {@link Tenant}.
   ** @param  name               the name of the attribute to manipulate.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link List} of values to transfer.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Object}.
   */
  private static void collect(final Tenant resource, final Attribute attribute) {
    Object value = attribute.getValue().get(0);
    // only NON-READ-ONLY fields here
    switch (attribute.getName()) {
      case NAME               : resource.name(String.class.cast(value));
                                break;
      case Tenant.TYPE        : resource.type(String.class.cast(value));
                                break;
      case Tenant.DISPLAYNAME : resource.displayName(String.class.cast(value));
                                break;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collect
  /**
   ** Converts the specified APIGEE user resource to the transfer options
   ** required by the Identity Connector Framework.
   **
   ** @param  resource           the APIGEE user resource to manipulate.
   **                            <br>
   **                            Allowed object is {@link User}.
   ** @param  name               the name of the attribute to manipulate.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link List} of values to transfer.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Object}.
   */
  private static void collect(final User resource, final Attribute attribute) {
    Object value = attribute.getValue().get(0);
    // only NON-READ-ONLY fields here
    switch (attribute.getName()) {
      case NAME           :
      case User.EMAIL     : resource.emailId(String.class.cast(value));
                            break;
      case PASSWORD       :
      case User.PASSWORD  : resource.password(CredentialAccessor.string(GuardedString.class.cast(value)));
                            break;
      case User.LASTNAME  : resource.lastName(String.class.cast(value));
                            break;
      case User.FIRSTNAME : resource.firstName(String.class.cast(value));
                            break;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collect
  /**
   ** Converts the specified APIGEE user resource to the transfer options
   ** required by the Identity Connector Framework.
   **
   ** @param  resource           the APIGEE developer resource to manipulate.
   **                            <br>
   **                            Allowed object is {@link Developer}.
   ** @param  name               the name of the attribute to manipulate.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link List} of values to transfer.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Object}.
   */
  private static void collect(final Developer resource, final Attribute attribute) {
    Object value = attribute.getValue().get(0);
    // only NON-READ-ONLY fields here
    switch (attribute.getName()) {
      case UID                 :
      case Developer.EMAIL     : resource.email(String.class.cast(value));
                                 break;
      case NAME                :
      case Developer.USERNAME  : resource.userName(String.class.cast(value));
                                 break;
      case Developer.LASTNAME  : resource.lastName(String.class.cast(value));
                                 break;
      case Developer.FIRSTNAME : resource.firstName(String.class.cast(value));
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
   ** @param  resource           the API resource identifiers providing the data
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
          permission.add(new EmbeddedObjectBuilder().setObjectClass(type).addAttribute(Uid.NAME, i.getKey()).addAttribute(Name.NAME, j).build());
        }
      }
      // add the collection to the connector object builder
      collector.addAttribute(type.getObjectClassValue(), permission);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildAttribute
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
}