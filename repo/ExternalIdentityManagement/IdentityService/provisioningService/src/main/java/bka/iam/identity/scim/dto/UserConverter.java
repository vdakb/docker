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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Governance Extension
    Subsystem   :   Identity Governance Provisioning

    File        :   UserConverter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    UserConverter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.scim.dto;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

import oracle.hst.platform.core.entity.Converter;

import oracle.hst.platform.core.utility.StringUtility;
import oracle.hst.platform.core.utility.CollectionUtility;

import oracle.iam.platform.scim.v2.UserResource;

import oracle.iam.platform.scim.schema.Name;
import oracle.iam.platform.scim.schema.Email;
import oracle.iam.platform.scim.schema.Metadata;
import oracle.iam.platform.scim.schema.PhoneNumber;

import bka.iam.identity.igs.model.User;
import bka.iam.identity.igs.model.Role;
import bka.iam.identity.igs.model.UserRole;

import oracle.iam.platform.scim.schema.Group;

////////////////////////////////////////////////////////////////////////////////
// class UserConverter
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** A class that converts JPA {@link User} entity representation into JAX-RS
 ** {@link UserResource} resource representation and vice versa.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class UserConverter extends Converter.Type<User, UserResource> {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>UserConverter</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private UserConverter() {

    // ensure inheritance
    super(UserConverter::convert, UserConverter::convert);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   convert
  /**
   ** Factory method to create a JPA {@link User} entity representation and
   ** populating its values from the JAX-RS {@link UserResource}
   ** representation <code>source</code>.
   **
   ** @param  source             the JAX-RS object representation providing the
   **                            data.
   **                            <br>
   **                            Allowed object is {@link UserResource}.
   **
   ** @return                    the JPA entity representation populated from
   **                            the JAX-RS object representation
   **                            {@link UserResource} <code>source</code>.
   **                            <br>
   **                            Possible object is {@link User}.
   */
  public static final User convert(final UserResource source) {
    final String            id     = source.id();
    final Name              name   = source.name();
    final User              entity = User.build(id == null ? null : Long.valueOf(id));
    entity.setActive(source.active() == null ? Boolean.TRUE : source.active());
    entity.setUserName(source.userName());
    entity.setCredential(source.password() == null ? "changeit" : source.password());
    entity.setLanguage(source.preferredLanguage());
    entity.setLastName(name == null ? null : name.familyName());
    entity.setFirstName(name == null ? null : name.givenName());
    entity.setEmail(email(source.email(), Email.WORK));
    entity.setPhone(phone(source.phoneNumber(),  PhoneNumber.WORK));
    entity.setMobile(phone(source.phoneNumber(), PhoneNumber.MOBILE));
    return entity;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   convert
  /**
   ** Factory method to create a JAX-RS {@link UserResource} representation
   ** and populating its values from the JPA entity representation {@link User}
   ** <code>source</code>.
  **
   ** @param  source             the JPA entity {@link User} representation
   **                            providing the persistence data.
   **                            <br>
   **                            Allowed object is {@link User}.
   **
   ** @return                    the JAX-RS {@link UserResource}
   **                            representation populated from the JPA entity
   **                            representation {@link User}
   **                            <code>source</code>.
   **                            <br>
   **                            Possible object is {@link UserResource}.
   */
  public static final UserResource convert(final User source) {
    final Metadata meta = new Metadata();
    meta.version(source.getVersion());
    meta.created(source.getCreatedOn());
    meta.modified(source.getUpdatedOn());

    final Name name = new Name();
    name.givenName(source.getFirstName());
    name.familyName(source.getLastName());
    // recomputes the formatted name due to its absent in the database
    name.computeFormatted();
    
    return new UserResource()
      .id(source.getId().toString())
      .metadata(meta)
      // set values in order of appearance in UserResource class
      .userName(source.getUserName())
      .name(name)
      .preferredLanguage(source.getLanguage())
      .active(source.getActive())
      .email(email(source.getEmail()))
      .phoneNumber(phone(source.getPhone(), source.getMobile()))
       // collect the roles
      .group(memberOf(source.getRole()))
    ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   convertOutbound
  /**
   ** Factory method to create a {@link Collection} of JAX-RS
   ** {@link UserResource} representations  and populating their values from
   ** the {@link List} of JPA {@link User} entity representations
   ** <code>source</code>.
   **
   ** @param  source             the {@link List} of JPA {@link User}
   **                            entity representations providing the data to
   **                            convert.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link User}.
   **
   ** @return                    the {@link Collection} of JAX-RS
   **                            {@link UserResource}s populated from the
   **                            {@link List} of JPA {@link User} entities
   **                            <code>source</code>.
   **                            <br>
   **                            Possible object is {@link Collection} where
   **                            each element is of type
   **                            {@link UserResource}.
   */
  public static Collection<UserResource> convertOutbound(final List<User> source) {
    return new UserConverter().outbound(source);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   convertInbound
  /**
   ** Factory method to create a {@link Collection} of JPA {@link User}
   ** entities and populating their values from the {@link List} of JAX-RS
   ** {@link UserResource}s <code>source</code>.
   **
   ** @param  source             the {@link List} of JAX-RS resources
   **                            {@link UserResource} providing the data to
   **                            convert.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link UserResource}.
   **
   ** @return                    the {@link Collection} of JPA entities
   **                            populated from the {@link List} JAX-RS
   **                            resources <code>source</code>.
   **                            <br>
   **                            Possible object is {@link Collection} where
   **                            each element is of type {@link User}.
   */
  public static Collection<User> convertInbound(final List<UserResource> source) {
    return new UserConverter().inbound(source);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   email
  /**
   ** Factory method to obtain the email from the {@link Collection} of SCIM
   ** {@link Email} resources.
   **
   ** @param  email              the string representation of an the e-Mail
   **                            Address.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  type               the string representation of the type of an
   **                            {@link Email}.
   **                            <br>
   **                            Allowed object is {@link String}.
   **                            
   ** @return                    the {@link Email} value for the {@link Email}
   **                            that match the specified <code>type</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  private static String email(final Collection<Email> email, final String type) {
    if (email == null || email.size() == 0)
      return null;

    for (Email cursor : email) {
      if (StringUtility.equal(cursor.type(), type))
        return cursor.value();
    }
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   email
  /**
   ** Factory method to create a {@link Collection} of SCIM {@link Email}
   ** resources from a string that represents the e-Mail Address.
   **
   ** @param  email              the string representation of an the e-Mail
   **                            Address.
   **                            <br>
   **                            Allowed object is {@link String}.
   **                            
   ** @return                    the {@link Collection} of SCIM {@link Email}.
   **                            <br>
   **                            Possible object is {@link Collection} where
   **                            each element is of type {@link Email}.
   */
  private static Collection<Email> email(final String email) {
    return (StringUtility.empty(email)) ? null : CollectionUtility.list(new Email(email).type(Email.WORK).primary(true));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   phone
  /**
   ** Factory method to obtain the telephone number from the {@link Collection}
   ** of SCIM {@link PhoneNumber} resources.
   **
   ** @param  phoneNumber        the string representation of an the telephone
   **                            number.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  type               the string representation of an the type
   **                            of a {@link PhoneNumber}.
   **                            <br>
   **                            Allowed object is {@link String}.
   **                            
   ** @return                    the {@link PhoneNumber} value for the
   **                            {@link PhoneNumber} that match the specified
   **                            <code>type</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  private static String phone(final Collection<PhoneNumber> phoneNumber, final String type) {
    if (phoneNumber == null || phoneNumber.size() == 0)
      return null;

    for (PhoneNumber cursor : phoneNumber) {
      if (StringUtility.equal(cursor.type(), type))
        return cursor.value();
    }
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   phone
  /**
   ** Factory method to create a {@link Collection} of SCIM {@link PhoneNumber}
   ** resources from a string that represents the telephone number.
   **
   ** @param  phone              the string representation of an the telephone
   **                            number.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  phone              the string representation of an the mobile
   **                            number.
   **                            <br>
   **                            Allowed object is {@link String}.
   **                            
   ** @return                    the {@link Collection} of SCIM
   **                            {@link PhoneNumber}.
   **                            <br>
   **                            Possible object is {@link Collection} where
   **                            each element is of type {@link PhoneNumber}.
   */
  private static Collection<PhoneNumber> phone(final String phone, final String mobile) {
    final Collection<PhoneNumber> collector = new ArrayList<PhoneNumber>();
    if (!StringUtility.empty(phone)) {
      collector.add(new PhoneNumber(phone).type(PhoneNumber.WORK).primary(true));
    }
    if (!StringUtility.empty(mobile)) {
      collector.add(new PhoneNumber(mobile).type(PhoneNumber.MOBILE));
    }
    return collector;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   memberOf
  /**
   ** Factory method to create a {@link Collection} of SCIM {@link Group}
   ** resources from a collection of {@link UserRole}s assigned to the a
   ** {@link User}.
   **
   ** @param  entity             the collection of {@link UserRole}s assigned to
   **                            the a {@link User}.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of typr {@link UserRole}.
   **                            
   ** @return                    the {@link Collection} of SCIM {@link Group}
   **                            resources.
   **                            <br>
   **                            Possible object is {@link Collection} where
   **                            each element is of type {@link Group}.
   */
  private static Collection<Group> memberOf(final Collection<UserRole> entity) {
    final Collection<Group> collector = new ArrayList<Group>();
    for (UserRole cursor : entity) {
      final Role role = cursor.getRole();
      collector.add(Group.of(role.getId(), Group.DIRECT, role.getDisplayName()));
    }
    return collector;
  }
}