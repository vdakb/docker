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
    Subsystem   :   Unique Identifier Backend API

    File        :   TenantFacade.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    TenantFacade.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.uid.api;

import java.util.Set;
import java.util.List;
import java.util.Collections;

import java.util.stream.Collectors;

import java.io.Serializable;

import javax.ejb.Stateless;

import javax.persistence.Query;
import javax.persistence.NoResultException;

import oracle.hst.platform.jpa.SearchFilter;
import oracle.hst.platform.jpa.SearchResult;
import oracle.hst.platform.jpa.SearchRequest;
import oracle.hst.platform.jpa.PersistenceService;
import oracle.hst.platform.jpa.PersistenceException;

import oracle.hst.platform.jpa.injection.Transactional;

import bka.iam.identity.igs.model.Tenant;

////////////////////////////////////////////////////////////////////////////////
// class TenantFacade
// ~~~~~ ~~~~~~~~~~~~
/**
 ** The session facade to manage {@link Tenant} entity persistent.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Stateless(name=TenantFacade.NAME)
public class TenantFacade extends    PersistenceService
                          implements Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////
  
  public static final String NAME            = "tenantFacade";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:2264564733892050972")
  private static final long serialVersionUID = -6937264064805245471L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TenantFacade</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TenantFacade() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   permitted
  /**
   ** Applies a search to populate the tenants where the authenticated principal
   ** is member of.
   **
   ** @return                    the collections of tenants where the
   **                            authenticated principal is member of.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link String}.
   **
   ** @throws PersistenceException  if the entity manager has been closed or the
   **                               criteria query is found to be invalid.
   */
  @Transactional(readOnly=true)
  public Set<String> permitted()
    throws PersistenceException {

    // obtain the authenticated principal from session context
    final String principal = this.sessionContext.getCallerPrincipal().getName();
    try {
      // Aaaargh, only positional parameter binding may be portably used for
      // native queries; see the JPA specification (section 3.6.3 Named
      // Parameters)
      final List<?> raw = this.em.createNativeQuery("SELECT clm.tnt_id FROM uit_claims clm WHERE clm.usr_id = (SELECT usr.id FROM igt_users usr WHERE UPPER(usr.username) = UPPER(?))")
        .setParameter(1, principal)
        .getResultList()
      ;
      return (raw == null || raw.size() == 0) ? Collections.emptySet() : raw.stream().map(e -> e.toString()).collect(Collectors.toSet());
    }
    catch (javax.persistence.PersistenceException e) {
      throw PersistenceException.abort(e);      
    }
    catch (IllegalStateException e) {
      throw PersistenceException.abort(e);
    }
    catch (IllegalArgumentException e) {
      throw PersistenceException.general(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   permitted
  /**
   ** Applies a search to verify that the authenticated principal is member of
   ** a tenant with a particular role.
   ** <p>
   ** The statement used is:
   ** <pre>
   **   SELECT 1
   **     FROM dual
   **    WHERE EXISTS (
   **     SELECT 1
   **       FROM uit_claims
   **      WHERE tnt_id  = ?
   **        AND rol_id IN (?, ?)
   **        AND usr_id  = (
   **              SELECT id
   **                FROM igt_users
   **               WHERE UPPER(username) = UPPER('?) 
   **            )
   **   )
   **   /
   ** </pre>
   ** The method awaits a single result if authenticated principal is member of
   ** the tenant with a particular role and return <code>true</code>; otherwise
   ** the pesistence layer throws a {@link NoResultException} which leads to
   ** return <code>false</code>.
   **
   ** @param  tenant             the identifier of the tenant for a claim the
   **                            authenticated principal must be member.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  generate           <code>true</code> if the role
   **                            <code>uid.generate</code> is required for the
   **                            claim to granted as member.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   ** @param  register           <code>true</code> if the role
   **                            <code>uid.register</code> is required for the
   **                            claim to granted as member.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    <code>true</code> if the authenticated
   **                            principal is member of <code>tenant</code> with
   **                            the specified <code>role</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws PersistenceException  if the entity manager has been closed or the
   **                               criteria query is found to be invalid.
   */
  @Transactional(readOnly=true)
  public Boolean permitted(final String tenant, final Boolean generate, final Boolean register)
    throws PersistenceException {
    
    // obtain the authenticated principal from session context
    final String principal = sessionContext.getCallerPrincipal().getName();
    try {
      // Aaaargh, only positional parameter binding may be portably used for
      // native queries; see the JPA specification (section 3.6.3 Named
      // Parameters)
      final Query select = this.em.createNativeQuery("SELECT 1 FROM  dual WHERE EXISTS (SELECT 1 FROM uit_claims WHERE tnt_id = ? AND rol_id IN (?, ?) AND usr_id = (SELECT id FROM igt_users WHERE UPPER(username) = UPPER(?)))")
        .setParameter(1,tenant)
        .setParameter(2, generate ? "uid.generate" : "no")
        .setParameter(3, register ? "uid.register" : "no")
        .setParameter(4, principal)
      ;
      return select.getSingleResult() != null;
    }
    catch (NoResultException e) {
      return false;      
    }
    catch (javax.persistence.PersistenceException | IllegalStateException e) {
      throw PersistenceException.abort(e);      
    }
    catch (IllegalArgumentException e) {
      throw PersistenceException.general(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   count
  /**
   ** Applies an aggregate expression applying the count operation to evaluate
   ** the numbers of entities in the persistence layer the belonges to the
   ** entity class specified.
   **
   ** @param  filter             the {@link SearchFilter} to apply on the
   **                            search.
   **                            <br>
   **                            Allowed object is {@link SearchFilter}.
   **
   ** @return                    the amount of entities belonging to the entity
   **                            class specified.
   **                            <br>
   **                            Possible object is {@link Long}.
   **
   ** @throws PersistenceException  if the entity manager has been closed or the
   **                               criteria query is found to be invalid.
   */
  @Transactional(readOnly=true)
  public Integer count(final SearchFilter filter)
    throws PersistenceException {

    return total(Tenant.class, filter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   list
  /**
   ** Search for entities of type {@link Tenant} and returns
   ** a partial result based on given {@link SearchRequest} with the option
   ** whether to cache the results or not.
   ** <p>
   ** To ensure that the pagination approach works as expected, it have to
   ** ensured that the query always returns the result in the same order. This
   ** is only the case if the query contains an ORDER BY clause. Otherwise, the
   ** order of the result set is undefined and might change.
   **
   ** @param  request            the {@link SearchRequest} to apply on the
   **                            search operation.
   **                            <br>
   **                            Allowed object is {@link SearchRequest}.
   **
   ** @return                    the collection of entities.
   **                            <br>
   **                            Possible object is {@link SearchResult} where
   **                            each element is of type {@link Tenant}.
   **
   ** @throws PersistenceException if the entity manager has been closed or the
   **                              criteria query is found to be invalid.
   */
  @Transactional(readOnly=true)
  public SearchResult<Tenant> list(final SearchRequest request)
    throws PersistenceException {

    return list(Tenant.class, request.start(), request.count(), request.total(), request.filter(), request.order());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** JPA request to retrieve a certain {@link  Tenant} by its<code>id</code>.
   **
   ** @param  id                 the identifier of the {@link  Tenant} tolookup.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link  Tenant} mapped at <code>id</code>.
   **                            <br>
   **                            May be <code>null</code>.
   **                            <br>
   **                            Possible object is {@link  Tenant}.
   **
   ** @throws PersistenceException if <code>clazz</code> does not denote an
   **                              entity type or the <code>id</code> is not a
   **                              valid type for the entity's primary key or is
   **                              <code>null</code>.
   */
  @Transactional(readOnly=true)
  public Tenant lookup(final String id)
    throws PersistenceException {

    return lookup(Tenant.class, id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** JPA request to persist a {@link Tenant} entity.
   **
   ** @param  entity             the entity to persist.
   **                            <br>
   **                            Allowed object is {@link Tenant}.
   **
   ** @return                    the entity mapped at their <code>id</code> at
   **                            the Service Provider.
   **                            <br>
   **                            Possible object is {@link Tenant}.
   **
   ** @throws PersistenceException if the entity manager has been closed or the
   **                              criteria query is found to be invalid.
   */
  @Transactional
  public Tenant create(final Tenant entity)
    throws PersistenceException {

    // prevent bogus state of the entity
    if (entity.getId() == null || entity.getId().isEmpty())
      throw PersistenceException.notNull(Tenant.class, Tenant.Attribute.ID.id);

    // prevent bogus state of the entity
    if (entity.getName() == null || entity.getName().isEmpty())
      throw PersistenceException.notNull(Tenant.class, Tenant.Attribute.NAME.id);

    // adjust the status of the entity if its not provided
    if (entity.getActive() == null)
      entity.setActive(true);

    return super.create(entity);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** JPA request to modify a {@link Tenant} entity.
   **
   ** @param  entity             the entity to modify.
   **                            <br>
   **                            Allowed object is {@link Tenant}.
   **
   ** @return                    the modified entity mapped at <code>id</code>
   **                            at the Service Provider.
   **                            <br>
   **                            Possible object is {@link Tenant}.
   **
   ** @throws PersistenceException if the entity manager has been closed or the
   **                              criteria query is found to be invalid.
   */
  @Transactional
  public Tenant modify(final Tenant entity)
    throws PersistenceException {

    if (entity.getId() == null || entity.getId().isEmpty())
      throw PersistenceException.notNull(Tenant.class, Tenant.Attribute.ID.id);

    return super.modify(entity);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** JPA request to remove a {@link Tenant} entity.
   **
   ** @param  entity             the entity to remove.
   **                            <br>
   **                            Allowed object is {@link Tenant}.
   **
   ** @return                    the removed entity mapped at <code>id</code>
   **                            at the Service Provider.
   **                            <br>
   **                            Possible object is {@link Tenant}.
   **
   ** @throws PersistenceException if the entity manager has been closed or the
   **                              criteria query is found to be invalid.
   */
  @Transactional
  public Tenant delete(final Tenant entity)
    throws PersistenceException {

    if (entity.getId() == null || entity.getId().isEmpty())
      throw PersistenceException.notNull(Tenant.class, Tenant.Attribute.ID.id);

    super.delete(entity);
    return entity;
  }
}