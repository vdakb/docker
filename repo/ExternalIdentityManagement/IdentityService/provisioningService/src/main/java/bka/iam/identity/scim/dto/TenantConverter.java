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

    File        :   TenantConverter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    TenantConverter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.scim.dto;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collection;

import oracle.hst.platform.core.entity.Converter;

import oracle.iam.platform.scim.schema.Metadata;

import bka.iam.identity.igs.model.Claim;
import bka.iam.identity.igs.model.Tenant;

import bka.iam.identity.igs.model.User;

import bka.iam.identity.scim.v2.TenantResource;

////////////////////////////////////////////////////////////////////////////////
// class TenantConverter
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** A class that converts JPA {@link Tenant} entity representation into JAX-RS
 ** {@link TenantResource} resource representation and vice versa.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TenantConverter extends Converter.Type<Tenant, TenantResource> {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TenantConverter</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private TenantConverter() {
    // ensure inheritance
    super(TenantConverter::convert, TenantConverter::convert);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   convert
  /**
   ** Factory method to create a JPA {@link Tenant} entity representation and
   ** populating its values from the JAX-RS {@link TenantResource}
   ** representation <code>source</code>.
   **
   ** @param  source             the JAX-RS object representation providing the
   **                            data.
   **                            <br>
   **                            Allowed object is {@link TenantResource}.
   **
   ** @return                    the JPA entity representation populated from
   **                            the JAX-RS object representation
   **                            {@link TenantResource} <code>source</code>.
   **                            <br>
   **                            Possible object is {@link Tenant}.
   */
  public static final Tenant convert(final TenantResource source) {
    final String id     = source.id();
    final Tenant entity = Tenant.build(id == null ? null : id);
    entity.setActive(source.active() == null ? Boolean.TRUE : source.active());
    entity.setName(source.displayName());
    return entity;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   convert
  /**
   ** Factory method to create a JAX-RS {@link TenantResource} representation
   ** and populating its values from the JPA entity representation
   ** {@link Tenant} <code>source</code>.
  **
   ** @param  source             the JPA entity {@link Tenant} representation
   **                            providing the persistence data.
   **                            <br>
   **                            Allowed object is {@link Tenant}.
   **
   ** @return                    the JAX-RS {@link TenantResource}
   **                            representation populated from the JPA entity
   **                            representation {@link Tenant}
   **                            <code>source</code>.
   **                            <br>
   **                            Possible object is {@link TenantResource}.
   */
  public static final TenantResource convert(final Tenant source) {
    final Metadata meta = new Metadata();
    meta.version(source.getVersion());
    meta.created(source.getCreatedOn());
    meta.modified(source.getUpdatedOn());

    return new TenantResource()
      .id(source.getId())
      .active(source.getActive())
      .displayName(source.getName())
      .metadata(meta)
       // collect the roles
      .role(permission(source.getClaim()))
    ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   convertOutbound
  /**
   ** Factory method to create a {@link Collection} of JAX-RS
   ** {@link TenantResource} representations  and populating their values from
   ** the {@link List} of JPA {@link Tenant} entity representations
   ** <code>source</code>.
   **
   ** @param  source             the {@link List} of JPA {@link Tenant}
   **                            entity representations providing the data to
   **                            convert.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Tenant}.
   **
   ** @return                    the {@link Collection} of JAX-RS
   **                            {@link TenantResource}s populated from the
   **                            {@link List} of JPA {@link Tenant} entities
   **                            <code>source</code>.
   **                            <br>
   **                            Possible object is {@link Collection} where
   **                            each element is of type
   **                            {@link TenantResource}.
   */
  public static Collection<TenantResource> convertOutbound(final List<Tenant> source) {
    return new TenantConverter().outbound(source);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   convertInbound
  /**
   ** Factory method to create a {@link Collection} of JPA {@link Tenant}
   ** entities and populating their values from the {@link List} of JAX-RS
   ** {@link TenantResource}s <code>source</code>.
   **
   ** @param  source             the {@link List} of JAX-RS resources
   **                            {@link TenantResource} providing the data to
   **                            convert.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link TenantResource}.
   **
   ** @return                    the {@link Collection} of JPA entities
   **                            populated from the {@link List} JAX-RS
   **                            resources <code>source</code>.
   **                            <br>
   **                            Possible object is {@link Collection} where
   **                            each element is of type {@link Tenant}.
   */
  public static Collection<Tenant> convertInbound(final List<TenantResource> source) {
    return new TenantConverter().inbound(source);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   role
  /**
   ** Factory method to create a {@link Collection} of SCIM
   ** {@link TenantResource.Role} resources from a collection of {@link Claim}s
   ** assigned to the a {@link Tenant}.
   **
   ** @param  entity             the collection of {@link Claim}s assigned to
   **                            the a {@link Tenant}.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of typr {@link Claim}.
   **                            
   ** @return                    the {@link Collection} of SCIM
   **                            {@link TenantResource.Role} resources.
   **                            <br>
   **                            Possible object is {@link Collection} where
   **                            each element is of type
   **                            {@link TenantResource.Role}.
   */
  private static Collection<TenantResource.Role> permission(final Collection<Claim> entity) {
    final Map<User, List<String>> claim = new HashMap<User, List<String>>();
    for (Claim cursor : entity) {
      final User user = cursor.getUser();
      if (!claim.containsKey(user)) {
        claim.put(user, new ArrayList<String>());
      }
      claim.get(user).add(cursor.getRole());
    }
    final Collection<TenantResource.Role> collector = new ArrayList<TenantResource.Role>();
    for (Map.Entry<User, List<String>> cursor : claim.entrySet()) {
      final User user = cursor.getKey();
      for (String scope : cursor.getValue())
        collector.add(TenantResource.Role.of(TenantResource.Role.USER, user.getId()).display(user.getUserName()).scope(scope));
    }
    return collector;
  }
}