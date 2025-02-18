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
    Subsystem   :   Generic SCIM Library

    File        :   SearchHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    SearchHandler.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.scim.v1.schema;

import java.lang.reflect.Field;

import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Collection;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.lang.reflect.InvocationTargetException;

import oracle.iam.identity.icf.rest.ServiceException;

import org.identityconnectors.common.security.GuardedString;

import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;

import oracle.iam.identity.icf.foundation.utility.StringUtility;
import oracle.iam.identity.icf.foundation.utility.SchemaUtility;
import oracle.iam.identity.icf.foundation.utility.CollectionUtility;
import oracle.iam.identity.icf.foundation.utility.CredentialAccessor;

import oracle.iam.identity.icf.scim.schema.Name;
import oracle.iam.identity.icf.scim.schema.Role;
import oracle.iam.identity.icf.scim.schema.Email;
import oracle.iam.identity.icf.scim.schema.Photo;
import oracle.iam.identity.icf.scim.schema.Group;
import oracle.iam.identity.icf.scim.schema.Member;
import oracle.iam.identity.icf.scim.schema.Address;
import oracle.iam.identity.icf.scim.schema.Factory;
import oracle.iam.identity.icf.scim.schema.Metadata;
import oracle.iam.identity.icf.scim.schema.Certificate;
import oracle.iam.identity.icf.scim.schema.PhoneNumber;
import oracle.iam.identity.icf.scim.schema.Entitlement;
import oracle.iam.identity.icf.scim.schema.Entity;
import oracle.iam.identity.icf.scim.schema.InstantMessaging;

import oracle.iam.identity.icf.scim.schema.Support;
import oracle.iam.identity.icf.scim.v1.request.Operation;

import org.identityconnectors.framework.common.objects.AttributeUtil;

////////////////////////////////////////////////////////////////////////////////
// class Marshaller
// ~~~~~ ~~~~~~~~~~
/**
 ** An interface to transfer SCIM 1 Resource object to and from Identity
 ** Connector attribute collections.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Marshaller {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The name of the identifier name attribute */
  public static final String  IDENTIFIER = "id";

  /** The name of the user name attribute */
  public static final String  USERNAME   = "userName";

  /** The name of the user type attribute */
  public static final String  USERTYPE   = "userType";

  /** The name of the password attribute */
  public static final String  PASSWORD   = "password";

  /** The name of the status attribute */
  public static final String  STATUS     = "active";

  /** The name of the user name attribute */
  public static final String  GROUPNAME  = "displayName";

  /** The name of the member attribute */
  public static final String MEMBEROF    = "members";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor (private)
  /**
   ** Default constructor
   ** <br>
   ** Access modifier private prevents other classes using "new Marshaller()"
   */
  private Marshaller() {
    // should never be instantiated
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transfer
  /**
   ** Converts the specified SCIM 1 user resource to the transfer options
   ** required by the Identity Connector Framework.
   **
   ** @param  resource           the SCIM 1 user resource to convert.
   **                            <br>
   **                            ChecksAllowed object is {@link UserResource}.
   **
   ** @return                    the {@link Set} of {@link Attribute}s to send
   **                            back.
   **                            <br>
   **                            Possible object is a {@link Set} where each
   **                            element is of type {@link Attribute}.
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
  public static Set<Attribute> transfer(final UserResource resource)
    throws IllegalAccessException
    ,      IllegalArgumentException {

    final Set<Attribute>    attributes = new HashSet<>();
    final Collection<Field> properties = Factory.describe(UserResource.class);
    for (Field field : properties) {
      // ensure that the field is accessible regardless which modifier the field
      // is assinged to
      field.setAccessible(true);
      final Object value = field.get(resource);
      if (!field.isAnnotationPresent(JsonIgnore.class) && !empty(value)) {
        if (field.getGenericType().toString().contains(Name.class.getName())) {
          collect(name(Name.class.cast(value)), attributes, field.getType());
        }
        else if (field.getGenericType().toString().contains(Email.class.getName())) {
          if (field.getType().equals(List.class)) {
            List<Email> multiple = (List<Email>)value;
            for (Email cursor : multiple) {
              collect(email(cursor), attributes, field.getType());
            }
          }
          else {
            collect(email(Email.class.cast(value)), attributes, field.getType());
          }
        }
        else if (field.getGenericType().toString().contains(PhoneNumber.class.getName())) {
          if (field.getType().equals(List.class)) {
            List<PhoneNumber> multiple = (List<PhoneNumber>)value;
            for (PhoneNumber cursor : multiple) {
              collect(phoneNumber(cursor), attributes, field.getType());
            }
          }
          else {
            collect(phoneNumber(PhoneNumber.class.cast(value)), attributes, field.getType());
          }
        }
        else if (field.getGenericType().toString().contains(Address.class.getName())) {
          if (field.getType().equals(List.class)) {
            List<Address> multiple = (List<Address>)value;
            for (Address cursor : multiple) {
              collect(address(cursor), attributes, field.getType());
            }
          }
          else {
            collect(address(Address.class.cast(value)), attributes, field.getType());
          }
        }
        else if (field.getGenericType().toString().contains(InstantMessaging.class.getName())) {
          if (field.getType().equals(List.class)) {
            List<InstantMessaging> multiple = (List<InstantMessaging>)value;
            for (InstantMessaging cursor : multiple) {
              collect(instantMessaging(cursor), attributes, field.getType());
            }
          }
          else {
            collect(instantMessaging(InstantMessaging.class.cast(value)), attributes, field.getType());
          }
        }
        else if (field.getGenericType().toString().contains(Photo.class.getName())) {
          if (field.getType().equals(List.class)) {
            List<Photo> multiple = (List<Photo>)value;
            for (Photo cursor : multiple) {
              collect(photo(cursor), attributes, field.getType());
            }
          }
          else {
            collect(photo(Photo.class.cast(value)), attributes, field.getType());
          }
        }
        else if (field.getGenericType().toString().contains(Group.class.getName())) {
          if (field.getType().equals(List.class)) {
            List<Group> multiple = (List<Group>)value;
            for (Group cursor : multiple) {
              collect(group(cursor), attributes, field.getType());
            }
          }
          else {
            collect(group(Group.class.cast(value)), attributes, field.getType());
          }
        }
        else if (field.getGenericType().toString().contains(Role.class.getName())) {
          if (field.getType().equals(List.class)) {
            List<Role> multiple = (List<Role>)value;
            for (Role cursor : multiple) {
              collect(role(cursor), attributes, field.getType());
            }
          }
          else {
            collect(role(Role.class.cast(value)), attributes, field.getType());
          }
        }
        else if (field.getGenericType().toString().contains(Entitlement.class.getName())) {
          if (field.getType().equals(List.class)) {
            List<Entitlement> multiple = (List<Entitlement>)value;
            for (Entitlement cursor : multiple) {
              collect(entitlement(cursor), attributes, field.getType());
            }
          }
          else {
            collect(entitlement(Entitlement.class.cast(value)), attributes, field.getType());
          }
        }
        else if (field.getGenericType().toString().contains(Certificate.class.getName())) {
          if (field.getType().equals(List.class)) {
            List<Certificate> multiple = (List<Certificate>)value;
            for (Certificate cursor : multiple) {
              collect(certificate(cursor), attributes, field.getType());
            }
          }
          else {
            collect(certificate(Certificate.class.cast(value)), attributes, field.getType());
          }
        }
        else if (field.getGenericType().toString().contains(Metadata.class.getName())) {
          if (field.getType().equals(List.class)) {
            List<Metadata> multiple = (List<Metadata>)value;
            for (Metadata cursor : multiple) {
              collect(metadata(cursor), attributes, field.getType());
            }
          }
          else {
            collect(metadata(Metadata.class.cast(value)), attributes, field.getType());
          }
        }
        else {
          attributes.add(buildAttribute(field.get(resource), field.getName(), field.getType()).build());
        }
      }
    }
    return attributes;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transfer
  /**
   ** Converts the specified SCIM 1 group resource to the transfer options
   ** required by the Identity Connector Framework.
   **
   ** @param  resource           the SCIM 1 group resource to convert.
   **                            <br>
   **                            ChecksAllowed object is {@link GroupResource}.
   **
   ** @return                    the {@link Set} of {@link Attribute}s to send
   **                            back.
   **                            <br>
   **                            Possible object is a {@link Set} where each
   **                            element is of type {@link Attribute}.
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
  public static Set<Attribute> transfer(final GroupResource resource)
    throws IllegalAccessException
    ,      IllegalArgumentException {

    final Set<Attribute>    attributes = new HashSet<>();
    final Collection<Field> properties = Factory.describe(GroupResource.class);
    for (Field field : properties) {
      // ensure that the field is accessible regardless which modifier the field
      // is assinged to
      field.setAccessible(true);
      final Object value = field.get(resource);
      if (!field.isAnnotationPresent(JsonIgnore.class) && !empty(value)) {
        if (field.getGenericType().toString().contains(Member.class.getName())) {
        }
        else if (field.getGenericType().toString().contains(Metadata.class.getName())) {
          if (field.getType().equals(List.class)) {
            List<Metadata> multiple = (List<Metadata>)value;
            for (Metadata cursor : multiple) {
              collect(metadata(cursor), attributes, field.getType());
            }
          }
          else {
            collect(metadata(Metadata.class.cast(value)), attributes, field.getType());
          }
        }
        else {
          attributes.add(buildAttribute(field.get(resource), field.getName(), field.getType()).build());
        }
      }
    }
    return attributes;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transferUser
  /**
   ** Factory method to create a new {@link UserResource} instance and transfer
   ** the specified {@link Set} of {@link Attribute}s to the SCIM 1 user
   ** resource.
   **
   ** @param  <T>                the type of the implementation.
   **                            <br>
   **                            This parameter is used for convenience to allow
   **                            better implementations of the entities
   **                            extending this class (entities can return their
   **                            own specific type instead of type defined by
   **                            this class only).
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  clazz              the Java class object used to determine the
   **                            type to return.
   **                            <br>
   **                            Allowed object is {@link Class} of type
   **                            <code>T</code>.
   ** @param  attribute          the {@link Set} of {@link Attribute}s to set
   **                            on the SCIM 1 user resource.
   **                            <br>
   **                            ChecksAllowed object is {@link Set} where each
   **                            element is of type {@link Attribute}.
   **
   ** @return                    the SCIM 1 user resource populated by the
   **                            {@link Set} of {@link Attribute}s
   **                            <br>
   **                            Possible object is a {@link GroupResource}.
   **
   ** @throws NoSuchMethodException     if a matching no-arg constructor for the
   **                                   resource to create is found.
   ** @throws IllegalAccessException    if the constructor object is enforcing
   **                                   Java language access control and the
   **                                   underlying constructor is inaccessible.
   ** @throws InstantiationException    if the class that declares the
   **                                   underlying constructor represents an
   **                                   abstract class.
   ** @throws InvocationTargetException if the underlying constructor throws an
   **                                   exception.
   */
  public static  <T extends UserResource> T transferUser(final Class<T> clazz, final Set<Attribute> attribute)
    throws NoSuchMethodException
    ,      IllegalAccessException
    ,      InstantiationException
    ,      InvocationTargetException {

    final T resource = clazz.getDeclaredConstructor().newInstance();
    for (Attribute cursor : attribute) {
      final String name = cursor.getName();
      if (!CollectionUtility.empty(cursor.getValue())) {
        // verify if an extension attribute is mentioned
        // e.g. urn:ietf:params:scim:schemas:extension:enterprise:2.0:User
        if (name.contains(":")) {
          extend(resource, name, AttributeUtil.getSingleValue(cursor));
        }
        else {
          collect(resource, name, AttributeUtil.getSingleValue(cursor));
        }
      }
    }
    return resource;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transferGroup
  /**
   ** Factory method to create a new {@link GroupResource} instance and transfer
   ** the specified {@link Set} of {@link Attribute}s to the SCIM 1 group
   ** resource.
   **
   ** @param  <T>                the type of the implementation.
   **                            <br>
   **                            This parameter is used for convenience to allow
   **                            better implementations of the entities
   **                            extending this class (entities can return their
   **                            own specific type instead of type defined by
   **                            this class only).
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  clazz              the Java class object used to determine the
   **                            type to return.
   **                            <br>
   **                            Allowed object is {@link Class} of type
   **                            <code>T</code>.
   ** @param  attribute          the {@link Set} of {@link Attribute}s to set
   **                            on the SCIM 1 group resource.
   **                            <br>
   **                            ChecksAllowed object is {@link Set} where each
   **                            element is of type {@link Attribute}.
   **
   ** @return                    the SCIM 1 group resource populated by the
   **                            {@link Set} of {@link Attribute}s
   **                            <br>
   **                            Possible object is a {@link GroupResource}.
   **
   ** @throws NoSuchMethodException     if a matching no-arg constructor for the
   **                                   resource to create is found.
   ** @throws IllegalAccessException    if the constructor object is enforcing
   **                                   Java language access control and the
   **                                   underlying constructor is inaccessible.
   ** @throws InstantiationException    if the class that declares the
   **                                   underlying constructor represents an
   **                                   abstract class.
   ** @throws InvocationTargetException if the underlying constructor throws an
   **                                   exception.
   */
  public static <T extends GroupResource> T transferGroup(final Class<T> clazz, final Set<Attribute> attribute)
    throws NoSuchMethodException
    ,      IllegalAccessException
    ,      InstantiationException
    ,      InvocationTargetException {

    final T resource = clazz.getDeclaredConstructor().newInstance();
    for (Attribute cursor : attribute) {
      final String name = cursor.getName();
      if (!CollectionUtility.empty(cursor.getValue())) {
        // verify if an extension attribute is mentioned
        // e.g. urn:ietf:params:scim:schemas:extension:enterprise:2.0:User
        if (name.contains(":")) {
          extend(resource, name, AttributeUtil.getSingleValue(cursor));
        }
        else {
          collect(resource, name, AttributeUtil.getSingleValue(cursor));
        }
      }
    }
    return resource;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   operationUser
  /**
   ** Factory method to create a {@link List} of {@link Operation} driven by
   ** the specified {@link Set} of {@link Attribute}s to modify the SCIM 1 user
   ** resource.
   **
   ** @param  attribute          the {@link Set} of {@link Attribute}s to set
   **                            on the SCIM 1 user resource.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link Attribute}.
   **
   ** @return                    the SCIM 1 patch operations populated from the
   **                            {@link Set} of {@link Attribute}s.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Operation}.
   **
   ** @throws ServiceException   if the path to the attribute to modify becomes
   **                            invalid.
   */
  public static List<Operation> operationUser(final Set<Attribute> attribute)
    throws ServiceException {

    final List<Operation> operation = new ArrayList<Operation>();
    for (Attribute cursor : attribute) {
      final String       name  = cursor.getName();
      // catch the special attributes and the password separatly and let the
      // normal process do for all other attributes
      // only NON-READ-ONLY fields here
      switch (name) {
        case "__NAME__"       : operation.add(Operation.replace(USERNAME, AttributeUtil.getAsStringValue(cursor)));
                                break;
        case "__ENABLE__"     : operation.add(Operation.replace(STATUS, AttributeUtil.getBooleanValue(cursor)));
                                break;
        case PASSWORD         :
        case "__PASSWORD__"   : operation.add(Operation.replace(PASSWORD, CredentialAccessor.string(AttributeUtil.getGuardedStringValue(cursor))));
                                break;
        default               : operation.add(Operation.replace(name, String.class.cast(cursor.getValue().get(0))));
                                break;
      }
    }
    return operation;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   operationGroup
  /**
   ** Factory method to create a {@link List} of {@link Operation} driven by
   ** the specified {@link Set} of {@link Attribute}s to modify the SCIM 1 group
   ** resource.
   **
   ** @param  attribute          the {@link Set} of {@link Attribute}s to set
   **                            on the SCIM 1 group resource.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link Attribute}.
   ** @param  remove             <code>true</code> if the {@link Attribute}s
   **                            provided needs to be removed.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the SCIM 1 patch operations populated from the
   **                            {@link Set} of {@link Attribute}s.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Operation}.
   **
   ** @throws ServiceException   if the path to the attribute to modify becomes
   **                            invalid.
   */
  public static List<Operation> operationGroup(final Set<Attribute> attribute, final boolean remove)
    throws ServiceException {

    final List<Operation> operation = new ArrayList<Operation>();
    for (Attribute cursor : attribute) {
      final String       name  = cursor.getName();
      // catch the special attributes separatly and let the normal process do
      // for all other attributes
      // only NON-READ-ONLY fields here
      switch (name) {
        case "__NAME__"       : operation.add(Operation.replace(GROUPNAME, AttributeUtil.getAsStringValue(cursor)));
                                break;
        case MEMBEROF         : if (remove)
                                  operation.add(Operation.remove(MEMBEROF, cursor.getValue()));
                                else
                                  operation.add(Operation.replace(MEMBEROF, cursor.getValue()));
                                break;
        default               : operation.add(Operation.replace(name, String.class.cast(cursor.getValue().get(0))));
                                break;
      }
    }
    return operation;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collect
  /**
   ** Converts the specified SCIM 1 user resource to the transfer options
   ** required by the Identity Connector Framework.
   **
   ** @param  resource           the SCIM 1 user resource to manipulate.
   **                            <br>
   **                            ChecksAllowed object is {@link UserResource}.
   ** @param  name               the name of the attribute to manipulate.
   **                            <br>
   **                            ChecksAllowed object is {@link String}.
   ** @param  value              the attribute value to transfer.
   **                            <br>
   **                            Allowed object is {@link Object}.
   */
  @SuppressWarnings("unchecked")
  private static void collect(final UserResource resource, final String name, final Object value) {
    // only NON-READ-ONLY fields here
    switch (name) {
      case SchemaUtility.UID               :
      case IDENTIFIER                      : resource.id(String.class.cast(value));
                                             break;
      case SchemaUtility.NAME              :
      case USERNAME                        : resource.userName(String.class.cast(value));
                                             break;
      case SchemaUtility.STATUS            :
      case STATUS                          : resource.active(Boolean.class.cast(value));
                                             break;
      case SchemaUtility.PASSWORD          :
      case PASSWORD                        : resource.password(CredentialAccessor.string(GuardedString.class.cast(value)));
                                             break;
      case USERTYPE                        : resource.userType(String.class.cast(value));
                                             break;
      case "nickName"                      : resource.nickName(String.class.cast(value));
                                             break;
      case "displayName"                   : resource.displayName(String.class.cast(value));
                                             break;
      case "profileURI"                    : resource.profileURI(URI.class.cast(value));
                                             break;
      case "name.formatted"                : name(resource).formatted(String.class.cast(value));
                                             break;
      case "name.familyName"               : name(resource).familyName(String.class.cast(value));
                                             break;
      case "name.givenName"                : name(resource).givenName(String.class.cast(value));
                                             break;
      case "name.middleName"               : name(resource).middleName(String.class.cast(value));
                                             break;
      case "name.honorificPrefix"          : name(resource).honorificPrefix(String.class.cast(value));
                                             break;
      case "name.honorificSuffix"          : name(resource).honorificSuffix(String.class.cast(value));
                                             break;
      case "locale"                        : resource.locale(String.class.cast(value));
                                             break;
      case "preferredLanguage"             : resource.preferredLanguage(String.class.cast(value));
                                             break;
      case "timezone"                      : resource.timeZone(String.class.cast(value));
                                             break;
      case "emails.work.primary"           : email(resource, Email.WORK).primary(Boolean.class.cast(value));
                                             break;
      case "emails.work.display"           : email(resource, Email.WORK).display(String.class.cast(value));
                                             break;
      case "emails.work.value"             : email(resource, Email.WORK).value(String.class.cast(value));
                                             break;
      case "emails.home.primary"           : email(resource, Email.HOME).primary(Boolean.class.cast(value));
                                             break;
      case "emails.home.display"           : email(resource, Email.HOME).display(String.class.cast(value));
                                             break;
      case "emails.home.value"             : email(resource, Email.HOME).value(String.class.cast(value));
                                             break;
      case "emails.other.primary"          : email(resource, Email.OTHER).primary(Boolean.class.cast(value));
                                             break;
      case "emails.other.display"          : email(resource, Email.OTHER).display(String.class.cast(value));
                                             break;
      case "emails.other.value"            : email(resource, Email.OTHER).value(String.class.cast(value));
                                             break;
      case "phoneNumbers.work.primary"     : phoneNumber(resource, PhoneNumber.WORK).primary(Boolean.class.cast(value));
                                             break;
      case "phoneNumbers.work.display"     : phoneNumber(resource, PhoneNumber.WORK).display(String.class.cast(value));
                                             break;
      case "phoneNumbers.work.value"       : phoneNumber(resource, PhoneNumber.WORK).value(String.class.cast(value));
                                             break;
      case "phoneNumbers.home.primary"     : phoneNumber(resource, PhoneNumber.HOME).primary(Boolean.class.cast(value));
                                             break;
      case "phoneNumbers.home.display"     : phoneNumber(resource, PhoneNumber.HOME).display(String.class.cast(value));
                                             break;
      case "phoneNumbers.home.value"       : phoneNumber(resource, PhoneNumber.HOME).value(String.class.cast(value));
                                             break;
      case "phoneNumbers.other.primary"    : phoneNumber(resource, PhoneNumber.OTHER).primary(Boolean.class.cast(value));
                                             break;
      case "phoneNumbers.other.display"    : phoneNumber(resource, PhoneNumber.OTHER).display(String.class.cast(value));
                                             break;
      case "phoneNumbers.other.value"      : phoneNumber(resource, PhoneNumber.OTHER).value(String.class.cast(value));
                                             break;
      case "addresses.work.primary"        : address(resource, Address.WORK).primary(Boolean.class.cast(value));
                                             break;
      case "addresses.work.formatted"      : address(resource, Address.WORK).formatted(String.class.cast(value));
                                             break;
      case "addresses.work.streetAddress"  : address(resource, Address.WORK).streetAddress(String.class.cast(value));
                                             break;
      case "addresses.work.locality"       : address(resource, Address.WORK).locality(String.class.cast(value));
                                             break;
      case "addresses.work.region"         : address(resource, Address.WORK).region(String.class.cast(value));
                                             break;
      case "addresses.work.postalCode"     : address(resource, Address.WORK).postalCode(String.class.cast(value));
                                             break;
      case "addresses.work.country"        : address(resource, Address.WORK).country(String.class.cast(value));
                                             break;
      case "addresses.home.primary"        : address(resource, Address.HOME).primary(Boolean.class.cast(value));
                                             break;
      case "addresses.home.formatted"      : address(resource, Address.HOME).formatted(String.class.cast(value));
                                             break;
      case "addresses.home.streetAddress"  : address(resource, Address.HOME).streetAddress(String.class.cast(value));
                                             break;
      case "addresses.home.locality"       : address(resource, Address.HOME).locality(String.class.cast(value));
                                             break;
      case "addresses.home.region"         : address(resource, Address.HOME).region(String.class.cast(value));
                                             break;
      case "addresses.home.postalCode"     : address(resource, Address.HOME).postalCode(String.class.cast(value));
                                             break;
      case "addresses.home.country"        : address(resource, Address.HOME).country(String.class.cast(value));
                                             break;
      case "addresses.other.primary"       : address(resource, Address.OTHER).primary(Boolean.class.cast(value));
                                             break;
      case "addresses.other.formatted"     : address(resource, Address.OTHER).formatted(String.class.cast(value));
                                             break;
      case "addresses.other.streetAddress" : address(resource, Address.OTHER).streetAddress(String.class.cast(value));
                                             break;
      case "addresses.other.locality"      : address(resource, Address.OTHER).locality(String.class.cast(value));
                                             break;
      case "addresses.other.region"        : address(resource, Address.OTHER).region(String.class.cast(value));
                                             break;
      case "addresses.other.postalCode"    : address(resource, Address.OTHER).postalCode(String.class.cast(value));
                                             break;
      case "addresses.other.country"       : address(resource, Address.OTHER).country(String.class.cast(value));
                                             break;
      case "ims.qq.primary"                : instantMessaging(resource, InstantMessaging.QQ).primary(Boolean.class.cast(value));
                                             break;
      case "ims.qq.display"                : instantMessaging(resource, InstantMessaging.QQ).display(String.class.cast(value));
                                             break;
      case "ims.qq.value"                  : instantMessaging(resource, InstantMessaging.QQ).value(String.class.cast(value));
                                             break;
      case "ims.aim.primary"               : instantMessaging(resource, InstantMessaging.AIM).primary(Boolean.class.cast(value));
                                             break;
      case "ims.aim.display"               : instantMessaging(resource, InstantMessaging.AIM).display(String.class.cast(value));
                                             break;
      case "ims.aim.value"                 : instantMessaging(resource, InstantMessaging.AIM).value(String.class.cast(value));
                                             break;
      case "ims.gtalk.primary"             : instantMessaging(resource, InstantMessaging.GTALK).primary(Boolean.class.cast(value));
                                             break;
      case "ims.gtalk.display"             : instantMessaging(resource, InstantMessaging.GTALK).display(String.class.cast(value));
                                             break;
      case "ims.gtalk.value"               : instantMessaging(resource, InstantMessaging.GTALK).value(String.class.cast(value));
                                             break;
      case "ims.icq.primary"               : instantMessaging(resource, InstantMessaging.ICQ).primary(Boolean.class.cast(value));
                                             break;
      case "ims.icq.display"               : instantMessaging(resource, InstantMessaging.ICQ).display(String.class.cast(value));
                                             break;
      case "ims.icq.value"                 : instantMessaging(resource, InstantMessaging.ICQ).value(String.class.cast(value));
                                             break;
      case "ims.msn.primary"               : instantMessaging(resource, InstantMessaging.MSN).primary(Boolean.class.cast(value));
                                             break;
      case "ims.msn.display"               : instantMessaging(resource, InstantMessaging.MSN).display(String.class.cast(value));
                                             break;
      case "ims.msn.value"                 : instantMessaging(resource, InstantMessaging.MSN).value(String.class.cast(value));
                                             break;
      case "ims.xmpp.primary"              : instantMessaging(resource, InstantMessaging.XMPP).primary(Boolean.class.cast(value));
                                             break;
      case "ims.xmpp.display"              : instantMessaging(resource, InstantMessaging.XMPP).display(String.class.cast(value));
                                             break;
      case "ims.xmpp.value"                : instantMessaging(resource, InstantMessaging.XMPP).value(String.class.cast(value));
                                             break;
      case "ims.skype.primary"             : instantMessaging(resource, InstantMessaging.SKYPE).primary(Boolean.class.cast(value));
                                             break;
      case "ims.skype.display"             : instantMessaging(resource, InstantMessaging.SKYPE).display(String.class.cast(value));
                                             break;
      case "ims.skype.value"               : instantMessaging(resource, InstantMessaging.SKYPE).value(String.class.cast(value));
                                             break;
      case "ims.yahoo.primary"             : instantMessaging(resource, InstantMessaging.YAHOO).primary(Boolean.class.cast(value));
                                             break;
      case "ims.yahoo.display"             : instantMessaging(resource, InstantMessaging.YAHOO).display(String.class.cast(value));
                                             break;
      case "ims.yahoo.value"               : instantMessaging(resource, InstantMessaging.YAHOO).value(String.class.cast(value));
                                             break;
      case "photos.photo.primary"          : photo(resource, Photo.PHOTO).primary(Boolean.class.cast(value));
                                             break;
      case "photos.photo.display"          : photo(resource, Photo.PHOTO).display(String.class.cast(value));
                                             break;
      case "photos.photo.value"            : photo(resource, Photo.PHOTO).value(URI.class.cast(value));
                                             break;
      case "photos.thumbnail.primary"      : photo(resource, Photo.THUMBNAIL).primary(Boolean.class.cast(value));
                                             break;
      case "photos.thumbnail.display"      : photo(resource, Photo.THUMBNAIL).display(String.class.cast(value));
                                             break;
      case "photos.thumbnail.value"        : photo(resource, Photo.THUMBNAIL).value(URI.class.cast(value));
                                             break;
      case "groups.default.value"          : group(resource).addAll((List<Group>)value);
                                             break;
      case "roles.default.value"           : role(resource).addAll((List<Role>)value);
                                             break;
      case "entitlements.default.value"    : entitlement(resource).addAll((List<Entitlement>)value);
                                             break;
      case "certificates.default.value"    : certificate(resource).addAll((List<Certificate>)value);
                                             break;
      case "schemas"                       : resource.namespace((List<String>)value);
                                             break;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collect
  /**
   ** Converts the specified SCIM 1 group resource to the transfer options
   ** required by the Identity Connector Framework.
   **
   ** @param  resource           the SCIM 1 group resource to manipulate.
   **                            <br>
   **                            ChecksAllowed object is {@link GroupResource}.
   ** @param  name               the name of the attribute to manipulate.
   **                            <br>
   **                            ChecksAllowed object is {@link String}.
   ** @param  value              the attribute value to transfer.
   **                            <br>
   **                            ChecksAllowed object is {@link Object}.
   */
  private static void collect(final GroupResource resource, final String name, final Object value) {
    // only NON-READ-ONLY fields here
    switch (name) {
      case GROUPNAME : resource.displayName(String.class.cast(value));
                       break;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   extend
  /**
   ** Converts the specified names-value pair to the transfer options
   ** required by the Identity Connector Framework.
   **
   ** @param  resource           the SCIM 1 entity resource to manipulate.
   **                            <br>
   **                            ChecksAllowed object is {@link Entity}.
   ** @param  name               the name of the attribute to manipulate.
   **                            <br>
   **                            ChecksAllowed object is {@link String}.
   ** @param  value              the attribute value to transfer.
   **                            <br>
   **                            ChecksAllowed object is {@link Object}.
   */
  @SuppressWarnings("unchecked")
  private static void extend(final Entity resource, final String name, final Object value) {
    final int split = name.lastIndexOf(':');
    if (split != -1) {
      final String namespace = name.substring(0, split);
      final String attribute = name.substring(split +1);
      // any extension have to be an object as long as its not embedded
      ObjectNode node = (ObjectNode)resource.extension().get(namespace);
      // add the namespace if its not exists
      if (node == null) {
        node = Support.nodeFactory().objectNode();
        resource.namespace().add(namespace);
        // extension (enterprise user is one of them) are impelemented as JSON
        // object nodes at the resource to keep it as much as flexible
        resource.extension().put(namespace, node);
      }
      node.put(attribute, String.class.cast(value));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collect
  /**
   ** Adds the specified {@link Set} of {@link Attribute}s <code>value</code> to
   ** the {@link Set} of {@link Attribute}s collected in <code>collector</code>.
   **
   ** @param  value              the {@link Set} of {@link Attribute}s to
   **                            collect in <code>collector</code>.
   ** @param  collector          the {@link Set} of {@link Attribute}s collected
   **                            so far.
   ** @param  type               the Java {@link Class} type of the attributes
   **                            to collect.
   */
  private static void collect(final Set<Attribute> value, final Set<Attribute> collector, final Class<?> type) {
    for (Attribute cursor : value) {
      collector.add(buildAttribute(cursor.getValue(), cursor.getName(), type).build());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Converts the specified SCIM 1 name resource to the transfer options
   ** required by the Identity Connector Framework.
   **
   ** @param  resource           the SCIM 1 name resource to convert.
   **
   ** @return                    the {@link Set} of {@link Attributes} to send
   **                            back.
   **                            <br>
   **                            Possible object is a {@link Set} where each
   **                            element is of type {@link Attribute}.
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
  private static Set<Attribute> name(final Name resource)
    throws IllegalArgumentException
    ,      IllegalAccessException {

    return layerAttribute(Name.PREFIX, resource);
  }
  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Factory method to create and associate a name SCIM 1 resource with the
   ** specified SCIM 1 user resource to the transfer options from the Identity
   ** Connector Framework.
   **
   ** @param  resource           the SCIM 1 user resource to manipulate.
   **
   ** @return                    the {@link Name} instance.
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
   ** Converts the specified SCIM 1 email resource to the transfer options
   ** required by the Identity Connector Framework.
   **
   ** @param  resource           the SCIM 1 email resource to convert.
   **
   ** @return                    the {@link Set} of {@link Attributes} to send
   **                            back.
   **                            <br>
   **                            Possible object is a {@link Set} where each
   **                            element is of type {@link Attribute}.
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
  private static Set<Attribute> email(final Email resource)
    throws IllegalArgumentException
    ,      IllegalAccessException {

    return complexAttribute(Email.PREFIX, resource.type(), resource);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   email
  /**
   ** Factory method to create and associate a email SCIM 1 resource with the
   ** specified SCIM 1 user resource to the transfer options from the Identity
   ** Connector Framework.
   **
   ** @param  resource           the SCIM 1 user resource to manipulate.
   **                            <br>
   **                            ChecksAllowed object is {@link UserResource}.
   ** @param  type               the type of the email resource.
   **                            <br>
   **                            ChecksAllowed object is {@link String}.
   **
   ** @return                    the {@link Email} instance.
   */
  @SuppressWarnings("unchecked")
  private static Email email(final UserResource resource, final String type) {
    Email selected = null;
    List<Email> current = resource.email();
    if (current == null) {
      current = new ArrayList<Email>();
      resource.email(current);
    }
    for (Email complex : current) {
      if (complex.type().equals(type)) {
         selected = complex;
         break;
       }
     }
     if (selected == null) {
       selected = new Email();
       selected.type(type);
       current.add(selected);
     }
     return selected;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   address
  /**
   ** Converts the specified SCIM 1 address resource to the transfer options
   ** required by the Identity Connector Framework.
   **
   ** @param  resource           the SCIM 1 address resource to convert.
   **                            <br>
   **                            ChecksAllowed object is {@link Address}.
   **
   ** @return                    the {@link Set} of {@link Attributes} to send
   **                            back.
   **                            <br>
   **                            Possible object is a {@link Set} where each
   **                            element is of type {@link Attribute}.
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
  private static Set<Attribute> address(final Address resource)
    throws IllegalArgumentException
    ,      IllegalAccessException {

    return complexAttribute(Address.PREFIX, resource.type(), resource);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   address
  /**
   ** Factory method to create and associate a address SCIM 1 resource with the
   ** specified SCIM 1 user resource to the transfer options from the Identity
   ** Connector Framework.
   **
   ** @param  resource           the SCIM 1 user resource to manipulate.
   **                            <br>
   **                            ChecksAllowed object is {@link UserResource}.
   ** @param  type               the type of the address resource.
   **                            <br>
   **                            ChecksAllowed object is {@link String}.
   **
   ** @return                    the {@link Address} instance.
   **                            <br>
   **                            Possible object is a {@link Address}.
   */
  @SuppressWarnings("unchecked")
  private static Address address(final UserResource resource, final String type) {
    Address selected = null;
    List<Address> current = resource.address();
    if (current == null) {
      current = new ArrayList<Address>();
      resource.address(current);
    }
    for (Address complex : current) {
      if (complex.type().equals(type)) {
         selected = complex;
         break;
       }
     }
     if (selected == null) {
       selected = new Address();
       selected.type(type);
       current.add(selected);
     }
     return selected;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   phoneNumber
  /**
   ** Converts the specified SCIM 1 phone number resource to the transfer
   ** options required by the Identity Connector Framework.
   **
   ** @param  resource           the SCIM 1 phone number resource to convert.
   **                            <br>
   **                            ChecksAllowed object is {@link PhoneNumber}.
   **
   ** @return                    the {@link Set} of {@link Attributes} to send
   **                            back.
   **                            <br>
   **                            Possible object is a {@link Set} where each
   **                            element is of type {@link Attribute}.
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
  private static Set<Attribute> phoneNumber(final PhoneNumber resource)
    throws IllegalArgumentException
    ,      IllegalAccessException {

    return complexAttribute(PhoneNumber.PREFIX, resource.type(), resource);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   phoneNumber
  /**
   ** Factory method to create and associate a phone number SCIM 1 resource with
   ** the specified SCIM 1 user resource to the transfer options from the
   ** Identity Connector Framework.
   **
   ** @param  resource           the SCIM user resource to manipulate.
   **                            <br>
   **                            ChecksAllowed object is {@link UserResource}.
   ** @param  type               the type of the phone number resource.
   **                            <br>
   **                            ChecksAllowed object is {@link String}.
   **
   ** @return                    the {@link PhoneNumber} instance.
   **                            <br>
   **                            Possible object is a {@link PhoneNumber}.
   */
  @SuppressWarnings("unchecked")
  private static PhoneNumber phoneNumber(final UserResource resource, final String type) {
    PhoneNumber selected = null;
    List<PhoneNumber> current = resource.phoneNumber();
    if (current == null) {
      current = new ArrayList<PhoneNumber>();
      resource.phoneNumber(current);
    }
    for (PhoneNumber complex : current) {
      if (complex.type().equals(type)) {
         selected = complex;
         break;
       }
     }
     if (selected == null) {
       selected = new PhoneNumber();
       selected.type(type);
       current.add(selected);
     }
     return selected;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   photo
  /**
   ** Converts the specified SCIM 1 photo resource to the transfer options
   ** required by the Identity Connector Framework.
   **
   ** @param  resource           the SCIM 1 photo resource to convert.
   **                            <br>
   **                            ChecksAllowed object is {@link Photo}.
   **
   ** @return                    the {@link Set} of {@link Attributes} to send
   **                            back.
   **                            <br>
   **                            Possible object is a {@link Set} where each
   **                            element is of type {@link Attribute}.
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
  private static Set<Attribute> photo(final Photo resource)
    throws IllegalArgumentException
    ,      IllegalAccessException {

    return complexAttribute(Photo.PREFIX, resource.type(), resource);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   photo
  /**
   ** Factory method to create and associate a photo SCIM 1 resource with the
   ** specified SCIM 1 user resource to the transfer options from the Identity
   ** Connector Framework.
   **
   ** @param  resource           the SCIM 1 user resource to manipulate.
   **                            <br>
   **                            ChecksAllowed object is {@link UserResource}.
   ** @param  type               the type of the photo resource.
   **                            <br>
   **                            ChecksAllowed object is {@link String}.
   **
   ** @return                    the {@link PhoneNumber} instance.
   **                            <br>
   **                            Possible object is a {@link Photo}.
   */
  @SuppressWarnings("unchecked")
  private static Photo photo(final UserResource resource, final String type) {
    Photo selected = null;
    List<Photo> current = resource.photo();
    if (current == null) {
      current = new ArrayList<Photo>();
      resource.photo(current);
    }
    for (Photo complex : current) {
      if (complex.type().equals(type)) {
         selected = complex;
         break;
       }
     }
     if (selected == null) {
       selected = new Photo();
       selected.type(type);
       current.add(selected);
     }
     return selected;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instantMessaging
  /**
   ** Converts the specified SCIM 1 instant messaging resource to the transfer
   ** options required by the Identity Connector Framework.
   **
   ** @param  resource           the SCIM 1 instant messaging resource to
   **                            convert.
   **                            <br>
   **                            ChecksAllowed object is {@link InstantMessaging}.
   **
   ** @return                    the {@link Set} of {@link Attributes} to send
   **                            back.
   **                            <br>
   **                            Possible object is a {@link Set} where each
   **                            element is of type {@link Attribute}.
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
  private static Set<Attribute> instantMessaging(final InstantMessaging resource)
    throws IllegalArgumentException
    ,      IllegalAccessException {

    return complexAttribute(InstantMessaging.PREFIX, resource.type(), resource);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instantMessaging
  /**
   ** Factory method to create and associate a instant messaging SCIM 1 resource
   ** with the specified SCIM 1 user resource to the transfer options from the
   ** Identity Connector Framework.
   **
   ** @param  resource           the SCIM 1 user resource to manipulate.
   **                            <br>
   **                            ChecksAllowed object is {@link UserResource}.
   ** @param  type               the type of the photo resource.
   **                            <br>
   **                            ChecksAllowed object is {@link String}.
   **
   ** @return                    the {@link InstantMessaging} instance.
   **                            <br>
   **                            Possible object is a {@link InstantMessaging}.
   */
  @SuppressWarnings("unchecked")
  private static InstantMessaging instantMessaging(final UserResource resource, final String type) {
    InstantMessaging selected = null;
    List<InstantMessaging> current = resource.ims();
    if (current == null) {
      current = new ArrayList<InstantMessaging>();
      resource.ims(current);
    }
    for (InstantMessaging complex : current) {
      if (complex.type().equals(type)) {
         selected = complex;
         break;
       }
     }
     if (selected == null) {
       selected = new InstantMessaging();
       selected.type(type);
       current.add(selected);
     }
     return selected;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   group
  /**
   ** Converts the specified SCIM 1 group resource to the transfer options
   ** required by the Identity Connector Framework.
   **
   ** @param  resource           the SCIM 1 group resource to convert.
   **                            <br>
   **                            ChecksAllowed object is {@link Group}.
   **
   ** @return                    the {@link Set} of {@link Attributes} to send
   **                            back.
   **                            <br>
   **                            Possible object is a {@link Set} where each
   **                            element is of type {@link Attribute}.
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
  private static Set<Attribute> group(final Group resource)
    throws IllegalArgumentException
    ,      IllegalAccessException {

    return layerAttribute(Group.PREFIX, resource);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   group
  /**
   ** Factory method to create and associate a group SCIM 1 resource with the
   ** specified SCIM 1 user resource to the transfer options from the Identity
   ** Connector Framework.
   **
   ** @param  resource           the SCIM 1 user resource to manipulate.
   **                            <br>
   **                            ChecksAllowed object is {@link UserResource}.
   **
   ** @return                    the {@link List} of {@link Group}s associated
   **                            with the SCIM 1 user resource so far.
   **                            <br>
   **                            Possible object is a {@link List} where each
   **                            element is of type {@link Group}.
   */
  @SuppressWarnings("unchecked")
  private static List<Group> group(final UserResource resource) {
    List<Group> current = resource.group();
    if (current == null) {
      current = new ArrayList<Group>();
      resource.group(current);
    }
    return current;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   role
  /**
   ** Converts the specified SCIM 1 role resource to the transfer options
   ** required by the Identity Connector Framework.
   **
   ** @param  resource           the SCIM 1 role resource to convert.
   **                            <br>
   **                            ChecksAllowed object is {@link Role}.
   **
   ** @return                    the {@link Set} of {@link Attributes} to send
   **                            back.
   **                            <br>
   **                            Possible object is a {@link Set} where each
   **                            element is of type {@link Attribute}.
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
  private static Set<Attribute> role(final Role resource)
    throws IllegalArgumentException
    ,      IllegalAccessException {

    return layerAttribute(Role.PREFIX, resource);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   role
  /**
   ** Factory method to create and associate a role SCIM 1 resource with the
   ** specified SCIM 1 user resource to the transfer options from the Identity
   ** Connector Framework.
   **
   ** @param  resource           the SCIM 1 user resource to manipulate.
   **                            <br>
   **                            ChecksAllowed object is {@link UserResource}.
   **
   ** @return                    the {@link List} of {@link Role}s associated
   **                            with the SCIM user resource so far.
   **                            <br>
   **                            Possible object is a {@link List} where each
   **                            element is of type {@link Role}.
   */
  @SuppressWarnings("unchecked")
  private static List<Role> role(final UserResource resource) {
    List<Role> current = resource.role();
    if (current == null) {
      current = new ArrayList<Role>();
      resource.role(current);
    }
    return current;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entitlement
  /**
   ** Converts the specified SCIM 1 entitlement resource to the transfer options
   ** required by the Identity Connector Framework.
   **
   ** @param  resource           the SCIM 1 entitlement resource to convert.
   **                            <br>
   **                            ChecksAllowed object is {@link Entitlement}.
   **
   ** @return                    the {@link Set} of {@link Attributes} to send
   **                            back.
   **                            <br>
   **                            Possible object is a {@link Set} where each
   **                            element is of type {@link Attribute}.
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
  private static Set<Attribute> entitlement(final Entitlement resource)
    throws IllegalArgumentException
    ,      IllegalAccessException {

    return layerAttribute(Entitlement.PREFIX, resource);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entitlement
  /**
   ** Factory method to create and associate a entitlement SCIM 1 resource with
   ** the specified SCIM 1 user resource to the transfer options from the
   ** Identity Connector Framework.
   **
   ** @param  resource           the SCIM 1 user resource to manipulate.
   **                            <br>
   **                            ChecksAllowed object is {@link UserResource}.
   **
   ** @return                    the {@link List} of {@link Entitlement}s
   **                            associated with the SCIM user resource so far.
   **                            <br>
   **                            Possible object is a {@link List} where each
   **                            element is of type {@link Entitlement}.
   */
  @SuppressWarnings("unchecked")
  private static List<Entitlement> entitlement(final UserResource resource) {
    List<Entitlement> current = resource.entitlement();
    if (current == null) {
      current = new ArrayList<Entitlement>();
      resource.entitlement(current);
    }
    return current;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   certificate
  /**
   ** Converts the specified SCIM 1 certificate resource to the transfer options
   ** required by the Identity Connector Framework.
   **
   ** @param  resource           the SCIM 1 certificate resource to convert.
   **                            <br>
   **                            ChecksAllowed object is {@link Certificate}.
   **
   ** @return                    the {@link Set} of {@link Attributes} to send
   **                            back.
   **                            <br>
   **                            Possible object is a {@link Set} where each
   **                            element is of type {@link Attribute}.
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
  private static Set<Attribute> certificate(final Certificate resource)
    throws IllegalArgumentException
    ,      IllegalAccessException {

    return layerAttribute(Certificate.PREFIX, resource);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   certificate
  /**
   ** Factory method to create and associate a certificate SCIM 1 resource with
   ** the specified SCIM user resource to the transfer options from the Identity
   ** Connector Framework.
   **
   ** @param  resource           the SCIM 1 user resource to manipulate.
   **                            <br>
   **                            ChecksAllowed object is {@link UserResource}.
   **
   ** @return                    the {@link List} of {@link Certificate}s
   **                            associated with the SCIM user resource so far.
   **                            <br>
   **                            Possible object is a {@link List} where each
   **                            element is of type {@link Certificate}.
   */
  @SuppressWarnings("unchecked")
  private static List<Certificate> certificate(final UserResource resource) {
    List<Certificate> current = resource.certificate();
    if (current == null) {
      current = new ArrayList<Certificate>();
      resource.certificate(current);
    }
    return current;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   metadata
  /**
   ** Converts the specified SCIM 1 metadata resource to the transfer options
   ** required by the Identity Connector Framework.
   **
   ** @param  resource           the SCIM 1 metadata resource to convert.
   **                            <br>
   **                            ChecksAllowed object is {@link Metadata}.
   **
   ** @return                    the {@link Set} of {@link Attributes} to send
   **                            back.
   **                            <br>
   **                            Possible object is a {@link Set} where each
   **                            element is of type {@link Attribute}.
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
  private static Set<Attribute> metadata(final Metadata resource)
    throws IllegalArgumentException
    ,      IllegalAccessException {

    return layerAttribute(Metadata.PREFIX, resource);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   layerAttribute
  /**
   ** Converts the specified SCIM 1 resource attribute to the transfer options
   ** required by the Identity Connector Framework.
   **
   ** @param  layer              the SCIM 1 resource prefix.
   **                            <br>
   **                            ChecksAllowed object is {@link String}.
   ** @param  property           the SCIM 1 resource property accessor.
   **                            <br>
   **                            ChecksAllowed object is {@link Object}.
   **
   ** @return                    the {@link Set} of {@link Attributes} to send
   **                            back.
   **                            <br>
   **                            Possible object is a {@link Set} where each
   **                            element is of type {@link Attribute}.
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
  private static Set<Attribute> layerAttribute(final String layer, final Object property)
    throws IllegalArgumentException
    ,      IllegalAccessException {

    final Set<Attribute>    attributes = new HashSet<>();
    final Collection<Field> properties = Factory.describe(property.getClass());
    for (Field field : properties) {
      if (!field.isAnnotationPresent(JsonIgnore.class)) {
        field.setAccessible(true);
        attributes.add(buildAttribute(field.get(property), layer.concat(".").concat(field.getName()), field.getType()).build());
      }
    }
    return attributes;
  }
  //////////////////////////////////////////////////////////////////////////////
  // Method:   complexAttribute
  /**
   ** Converts the specified SCIM 1 resource attribute to the transfer options
   ** required by the Identity Connector Framework.
   **
   ** @param  layer              the SCIM 1 resource prefix.
   **                            <br>
   **                            ChecksAllowed object is {@link String}.
   ** @param  type               the canonical type of SCIM 1 resource.
   **                            <br>
   **                            ChecksAllowed object is {@link String}.
   ** @param  property           the SCIM 1 resource property accessor.
   **                            <br>
   **                            ChecksAllowed object is {@link Object}.
   **
   ** @return                    the {@link Set} of {@link Attributes} to send
   **                            back.
   **                            <br>
   **                            Possible object is a {@link Set} where each
   **                            element is of type {@link Attribute}.
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
  private static Set<Attribute> complexAttribute(final String layer, final String type, final Object property)
    throws IllegalArgumentException
    ,      IllegalAccessException {

    final Set<Attribute>    attributes = new HashSet<>();
    final Collection<Field> properties = Factory.describe(property.getClass());
    for (Field field : properties) {
      if (!field.isAnnotationPresent(JsonIgnore.class)) {
        field.setAccessible(true);
        attributes.add(buildAttribute(field.get(property), layer.concat(".").concat(type).concat(".").concat(field.getName()), field.getType()).build());
      }
    }
    return attributes;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildAttribute
  private static AttributeBuilder buildAttribute(final Object value, final String name, final Class<?> clazz) {
    final AttributeBuilder builder = new AttributeBuilder();
    if (value != null) {
      if (clazz == boolean.class || clazz == Boolean.class) {
        builder.addValue(Boolean.class.cast(value));
      }
      else if (value instanceof List<?>) {
        ArrayList<?> list = new ArrayList<>((List<?>)value);
        if (list.size() > 1) {
          for (Object elem : list) {
            buildAttribute(elem, name, clazz);
          }
        }
        else if (!list.isEmpty()) {
          builder.addValue(list.get(0).toString());
        }
      }
      else {
        builder.addValue(value.toString());
      }
    }
    if (name != null) {
      builder.setName(name);
    }
    return builder;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   empty
  /**
   ** Checks if the specified {@link Object} is empty.
   **
   ** @param  value              the {@link Object} to check for emptyness.
   **                            <br>
   **                            ChecksAllowed object is {@link Object}.
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