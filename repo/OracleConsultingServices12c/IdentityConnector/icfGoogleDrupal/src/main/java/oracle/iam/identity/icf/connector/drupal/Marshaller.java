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
    Subsystem   :   Google Drupal Connector

    File        :   Marshaller.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   adrien.farkas@oracle.com based on work of dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Marshaller.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-21-05  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.drupal;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.Date;

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

import oracle.iam.identity.icf.connector.drupal.schema.User;
import oracle.iam.identity.icf.connector.drupal.schema.UserJson;
import oracle.iam.identity.icf.connector.drupal.schema.UserJson.SimpleValue;

////////////////////////////////////////////////////////////////////////////////
// class Marshaller
// ~~~~~ ~~~~~~~~~~
/**
 ** An interface to transfer Google Drupal REST resource to and from Identity Connector
 ** attribute collections.
 **
 ** @author  adrien.farkas@oracle.com based on work of dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Marshaller {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

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
   * Factory method to create a new {@link oracle.iam.identity.icf.connector.drupal.schema.User} instance and transfer the
   * specified {@link Set} of {@link Attribute} s to the Drupal user resource.
   *
   * @param attribute the {@link Set} of {@link Attribute} s to set
   * on the Drupal user resource.
   * <br>
   * Allowed object is a {@link Set} where each
   * elemment is of type {@link Attribute} .
   *
   * @return the Drupal user resource populated by the
   * {@link Set} of {@link Attribute} s
   * <br>
   * Possible object is a {@link oracle.iam.identity.icf.connector.drupal.schema.User} .
   */
  public static UserJson inboundUser(final Set<Attribute> attribute) {
    final String method = "Marshaller#inboundUser()";
    final UserJson resource = new UserJson();
    for (Attribute cursor : attribute) {
//      System.out.println(method + " Attribute supplied: " + cursor.getName() + " => " + cursor.getValue());
      if (!CollectionUtility.empty(cursor.getValue())) {
        collect(resource, cursor);
      }
    }
//    System.out.println(method + " Resulting resource: " + resource);
    return resource;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectorObject
  /**
   * Transforms the data received from the Service Provider and wrapped in the
   * specified Drupal {@link oracle.iam.identity.icf.connector.drupal.schema.User} <code>user</code> to the
   * {@link ConnectorObjectBuilder} .
   *
   * @param resource the specified Google Drupal <code>Edge User</code> to
   * transform.
   * <br>
   * Allowed object is {@link Map} where each
   * element is a {@link String} mapped to a
   * {@link Object} .
   * @param attributes the {@link Set} of attribute names requested to
   * be set in the {@link ConnectorObjectBuilder} .
   * <br>
   * If this passed as <code>null</code> all
   * registred attributes are returned
   * <br>
   * Allowed object is {@link Set} where each
   * element is of type {@link String} .
   * @param tenants the {@link List} of {@link oracle.iam.identity.icf.connector.drupal.schema.Tenant} resource
   * identifiers to collect the permissions.
   * <br>
   * Allowed object is {@link List} where each
   * element is of type {@link Pair} .
   *
   * @return the transformation result wrapped in a
   * {@link ConnectorObject} .
   * <br>
   * Possible object {@link ConnectorObject} .
   */
  static ConnectorObject connectorObject(final User resource, final Set<String> attributes) {
    final ConnectorObjectBuilder builder = new ConnectorObjectBuilder().setObjectClass(ObjectClass.ACCOUNT);
    // ICF is such a stupid framework that enforce that every Service Provider
    // has to look like a LDAP system
    // we need to put attributes that isn't needed only to satisfay the
    // constraints of this bullshit

    // Looks weird, I know, but we're having an already weird JSON in a Map...
    final String id = resource.uid();
    final String userName = resource.name();
    // always set __UID__ and __NAME__ attribute to convince stupid framework
    builder.setUid(id).setName(userName);

//    System.out.println("Marshaller#connectorObject(): resource: " + resource);
//    System.out.println("Marshaller#connectorObject(): attributes: " + attributes);
    if (attributes.contains(User.LASTNAME))
      builder.addAttribute(buildAttribute(resource.lastName(), User.LASTNAME,  String.class));
    if (attributes.contains(User.FIRSTNAME))
      builder.addAttribute(buildAttribute(resource.firstName(), User.FIRSTNAME, String.class));
    if (attributes.contains(User.EMAIL))
      builder.addAttribute(buildAttribute(resource.email(), User.EMAIL, String.class));
    if (attributes.contains(User.ROLES))
      builder.addAttribute(buildAttribute(resource.roles(), User.ROLES, List.class));
    if (attributes.contains(User.STATUS))
      builder.addAttribute(buildAttribute(resource.status(), User.EMAIL, Boolean.class));
    if (attributes.contains(User.LANG))
      builder.addAttribute(buildAttribute(resource.langCode(), User.LANG, String.class));
    if (attributes.contains(User.DEFAULT_LANG))
      builder.addAttribute(buildAttribute(resource.defaultLangCode(), User.DEFAULT_LANG, Boolean.class));
    if (attributes.contains(User.PREFERRED_LANG))
      builder.addAttribute(buildAttribute(resource.preferredLangCode(), User.PREFERRED_LANG, String.class));
    if (attributes.contains(User.PREFERRED_ADM_LANG))
      builder.addAttribute(buildAttribute(resource.preferredAdminLangCode(), User.PREFERRED_ADM_LANG, String.class));
    if (attributes.contains(User.TIMEZONE))
      builder.addAttribute(buildAttribute(resource.timeZone(), User.TIMEZONE, String.class));
    if (attributes.contains(User.CREATED))
      builder.addAttribute(buildAttribute(resource.created().getTime(), User.CREATED, Long.class));
    if (attributes.contains(User.CHANGED))
      builder.addAttribute(buildAttribute(resource.changed().getTime(), User.CHANGED, Long.class));
    if (attributes.contains(User.ACCESS))
      builder.addAttribute(buildAttribute(resource.access().getTime(), User.ACCESS, Long.class));
    if (attributes.contains(User.LOGIN))
      builder.addAttribute(buildAttribute(resource.login().getTime(), User.LOGIN, Long.class));
    if (attributes.contains(User.INIT))
      builder.addAttribute(buildAttribute(resource.init(), User.INIT, String.class));
    if (attributes.contains(User.APIGEE_EDGE_DEV_ID))
      builder.addAttribute(buildAttribute(resource.apigeeEdgeDeveloperId(), User.APIGEE_EDGE_DEV_ID, String.class));
    if (attributes.contains(User.CUST_PROFILES))
      builder.addAttribute(buildAttribute(resource.customerProfiles(), User.CUST_PROFILES, String.class));
    if (attributes.contains(User.PATH))
      builder.addAttribute(buildAttribute(resource.path(), User.PATH, String.class));
    if (attributes.contains(User.COMMERCE_REMOTE_ID))
      builder.addAttribute(buildAttribute(resource.commerceRemoteId(), User.COMMERCE_REMOTE_ID, String.class));
    if (attributes.contains(User.USER_PICTURE))
      builder.addAttribute(buildAttribute(resource.userPicture(), User.USER_PICTURE, String.class));
    if (attributes.contains(User.FIELD_BEHOERDE))
      builder.addAttribute(buildAttribute(resource.behoerde(), User.FIELD_BEHOERDE, String.class));
    if (attributes.contains(User.ROLES))
      builder.addAttribute(buildAttribute(resource.roles(), User.ROLES, List.class));

    return builder.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collect
  /**
   * Converts the specified Drupal user resource to the transfer options
   * required by the Identity Connector Framework.
   *
   * @param resource the Drupal user resource to manipulate.
   * <br>
   * Allowed object is {@link oracle.iam.identity.icf.connector.drupal.schema.User} .
   * @param name the name of the attribute to manipulate.
   * <br>
   * Allowed object is {@link String} .
   * @param value the {@link List} of values to transfer.
   * <br>
   * Allowed object is {@link List} where each
   * element is of type {@link Object} .
   */
  private static void collect(final UserJson resource, final Attribute attribute) {
    Object value = attribute.getValue().get(0);
    // only NON-READ-ONLY fields here
    String stringVal = String.class.cast(value.toString());
    // If needed below, will be 90% of cases...
    SimpleValue[] valArr = { new SimpleValue(stringVal) };
    // ...and the remaining 10%
    UserJson.BooleanValue[] boolValArr = { new UserJson.BooleanValue(Boolean.valueOf(stringVal)) };
    // Language is assumed to be "en"
    UserJson.PathValue[] pathValArr = { new UserJson.PathValue(stringVal, "", "en") };
    // Date should be in some reasonable format already: 1970-01-01T00:00:00+00:00, we assume the format by hard
    // Subject to discussion with OIM
    UserJson.DateValue[] dateValArr = { new UserJson.DateValue(stringVal, "Y-m-d\\\\TH:i:sP") };
    UserJson.Role[] roleValArr = { new UserJson.Role(stringVal) };
    List<UserJson.Role> roleValList = Arrays.asList(roleValArr);

    switch (attribute.getName()) {
      case User.UID                 : resource.uid(valArr);
                                      break;
      case User.UUID                : resource.uuid(valArr);
                                      break;
      case User.NAME                : resource.name(valArr);
                                      break;
      case User.EMAIL               : resource.email(valArr);
                                      break;
      case User.LASTNAME            : resource.lastName(valArr);
                                      break;
      case User.FIRSTNAME           : resource.firstName(valArr);
                                      break;
      case User.LANG                : resource.langCode(valArr);
                                      break;
      case User.PREFERRED_LANG      : resource.preferredLangCode(valArr);
                                      break;
      case User.DEFAULT_LANG        : resource.defaultLangCode(boolValArr);
                                      break;
      case User.PREFERRED_ADM_LANG  : resource.preferredAdminLangCode(valArr);
                                      break;
      case User.TIMEZONE            : resource.timeZone(valArr);
                                      break;
      case User.STATUS              :
      case "__ENABLE__"             : resource.status(boolValArr);
                                      break;
      case User.CREATED             : resource.created(dateValArr);
                                      break;
      case User.CHANGED             : resource.changed(dateValArr);
                                      break;
      case User.ACCESS              : resource.access(dateValArr);
                                      break;
      case User.LOGIN               : resource.login(dateValArr);
                                      break;
      case User.INIT                : resource.init(valArr);
                                      break;
      case User.APIGEE_EDGE_DEV_ID  : resource.apigeeEdgeDeveloperId(valArr);
                                      break;
      case User.CUST_PROFILES       : resource.customerProfiles(valArr);
                                      break;
      case User.PATH                : resource.path(pathValArr);
                                      break;
      case User.COMMERCE_REMOTE_ID  : resource.commerceRemoteId(valArr);
                                      break;
      case User.USER_PICTURE        : resource.userPicture(valArr);
                                      break;
      case User.FIELD_BEHOERDE      : resource.behoerde(valArr);
                                      break;
      case User.ROLES               : resource.roles(roleValList);
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