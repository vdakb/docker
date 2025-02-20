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

    File        :   RoleConverter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    RoleConverter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.scim.dto;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

import oracle.hst.platform.core.entity.Converter;

import oracle.hst.platform.core.utility.DateUtility;

import bka.iam.identity.igs.model.Role;
import bka.iam.identity.igs.model.User;
import bka.iam.identity.igs.model.UserRole;

import oracle.iam.platform.scim.schema.Member;
import oracle.iam.platform.scim.schema.Metadata;

import oracle.iam.platform.scim.v2.GroupResource;

////////////////////////////////////////////////////////////////////////////////
// class RoleConverter
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** A class that converts JPA {@link Role} entity representation into JAX-RS
 ** {@link GroupResource} resource representation and vice versa.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class RoleConverter extends Converter.Type<Role, GroupResource> {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>RoleConverter</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private RoleConverter() {
    // ensure inheritance
    super(RoleConverter::convert, RoleConverter::convert);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   convert
  /**
   ** Factory method to create a JPA {@link Role} entity and populating its
   ** values from the JAX-RS resource {@link GroupResource} <code>source</code>.
   **
   ** @param  source             the JAX-RS resource object providing the data.
   **                            <br>
   **                            Allowed object is {@link GroupResource}.
   **
   ** @return                    the JPA entity populated from the JAX-RS
   **                            resource {@link GroupResource}
   **                            <code>source</code>.
   **                            <br>
   **                            Possible object is {@link Role}.
   */
  public static Role convert(final GroupResource source) {
    final Role entity = Role.build(source.id());
    entity.setDisplayName(source.displayName());
    return entity;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   convert
  /**
   ** Factory method to create a JAX-RS {@link GroupResource} resource and
   ** populating its values from the JPA entity {@link Role}
   ** <code>source</code>.
  **
   ** @param  source             the JPA entity {@link Role} providing the
   **                            persistence data.
   **                            <br>
   **                            Allowed object is {@link Role}.
   **
   ** @return                    the JAX-RS {@link GroupResource} populated from
   **                            the JPA entity {@link Role}
   **                            <code>source</code>.
   **                            <br>
   **                            Possible object is {@link GroupResource}.
   */
  public static GroupResource convert(final Role source) {
    final Metadata meta = new Metadata();
    meta.version(source.getVersion());
    meta.created(source.getCreatedOn());
    meta.modified(source.getUpdatedOn());

    return new GroupResource().id(source.getId()).displayName(source.getDisplayName())
      .metadata(meta)
       // collect the roles
      .members(member(source.getUser()))
    ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   convertOutbound
  /**
   ** Factory method to create a {@link Collection} of JAX-RS
   ** {@link GroupResource} resources and populating their values from the
   ** {@link List} of JPA {@link Role} entities <code>source</code>.
   **
   ** @param  source             the {@link List} of JPA {@link Role}
   **                            entities providing the data to convert.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Role}.
   **
   ** @return                    the {@link Collection} of JAX-RS
   **                            {@link GroupResource}s populated from the
   **                            {@link List} of JPA {@link Role} entities
   **                            <code>source</code>.
   **                            <br>
   **                            Possible object is {@link Collection} where
   **                            each element is of type {@link GroupResource}.
   */
  public static Collection<GroupResource> convertOutbound(final List<Role> source) {
    return new RoleConverter().outbound(source);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   convertInbound
  /**
   ** Factory method to create a {@link Collection} of JPA {@link Role}
   ** entities and populating their values from the {@link List} of JAX-RS
   ** resources {@link GroupResource} <code>source</code>.
   **
   ** @param  source             the {@link List} of JAX-RS resources
   **                            {@link GroupResource} providing the data to
   **                            convert.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link GroupResource}.
   **
   ** @return                    the {@link Collection} of JPA entities
   **                            populated from the {@link List} JAX-RS
   **                            resources <code>source</code>.
   **                            <br>
   **                            Possible object is {@link Collection} where
   **                            each element is of type {@link Role}.
   */
  public static Collection<Role> convertInbound(final List<GroupResource> source) {
    return new RoleConverter().inbound(source);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   member
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
   ** @return                    the {@link Collection} of SCIM {@link Member}
   **                            resources.
   **                            <br>
   **                            Possible object is {@link Collection} where
   **                            each element is of type {@link Group}.
   */
  private static Collection<Member> member(final Collection<UserRole> entity) {
    final Collection<Member> collector = new ArrayList<Member>();
    for (UserRole cursor : entity) {
      final User user = cursor.getUser();
      collector.add(Member.of(user.getId().toString(), user.getUserName()));
    }
    return collector;
  }
}