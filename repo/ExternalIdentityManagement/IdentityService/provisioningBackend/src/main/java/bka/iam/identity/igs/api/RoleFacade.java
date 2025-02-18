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
    Subsystem   :   Identity Governance Administration

    File        :   RoleFacade.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    RoleFacade.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.igs.api;

import java.io.Serializable;

import javax.ejb.Stateless;

import oracle.hst.platform.jpa.SortOption;
import oracle.hst.platform.jpa.SearchFilter;
import oracle.hst.platform.jpa.SearchResult;
import oracle.hst.platform.jpa.SearchRequest;
import oracle.hst.platform.jpa.PersistenceService;
import oracle.hst.platform.jpa.PersistenceException;

import oracle.hst.platform.jpa.injection.Transactional;

import bka.iam.identity.igs.model.Role;
import bka.iam.identity.igs.model.User;

////////////////////////////////////////////////////////////////////////////////
// class RoleFacade
// ~~~~~ ~~~~~~~~~~
/**
 ** The session facade to manage {@link Role} entity persistent.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Stateless(name=RoleFacade.NAME)
public class RoleFacade extends    PersistenceService
                        implements Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////
  
  public static final String NAME             = "roleFacade";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-1525397409051671137")
  private static final long  serialVersionUID = -1395539547210076585L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>RoleFacade</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public RoleFacade() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   list
  /**
   ** Search for entities of type {@link Role} and returns
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
   **                            each element is of type {@link Role}.
   **
   ** @throws PersistenceException if the entity manager has been closed or the
   **                              criteria query is found to be invalid.
   */
  public SearchResult<Role> list(final SearchRequest request)
    throws PersistenceException {

    return list(request.start(), request.count(), request.total(), request.filter(), request.order());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   list
  /**
   ** Search for entities of type {@link Role} and returns a partial result
   ** based on given search properties with the option whether to cache the
   ** results or not.
   ** <p>
   ** This facade has to satisfy in both scenarios, simple REST or sophisticated
   ** SCIM invocation, hence it's easier to provide a signature exposing the
   ** parameters as native java type instead to require the transformation of
   ** the different wrappers of query parameters from each scenario type.
   ** <p>
   ** To ensure that the pagination approach works as expected, it have to
   ** ensured that the query always returns the result in the same order. This
   ** is only the case if the query contains an ORDER BY clause. Otherwise, the
   ** order of the result set is undefined and might change.
   **
   ** @param  start              the 1-based index of the first search result or
   **                            <code>null</code> if pagination is not
   **                            required.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   ** @param  count              the desired maximum number of search results
   **                            per page or <code>null</code> to not enforce a
   **                            limit.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   ** @param  total              <code>true</code> if total number of entities
   **                            in the desired result set needs to be
   **                            estimated; otherwise <code>false</code>.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   ** @param  filter             the {@link SearchFilter} criteria to apply.
   **                            <br>
   **                            Allowed object is {@link SearchFilter}.
   ** @param  order              the {@link SortOption} to apply on the search.
   **                            <br>
   **                            Allowed object is {@link SortOption}.
   **
   ** @return                    the collection of entities.
   **                            <br>
   **                            Possible object is {@link SearchResult} where
   **                            each element is of type {@link Role}.
   **
   ** @throws PersistenceException  if the entity manager has been closed or the
   **                            criteria query is found to be invalid.
   */
  @Transactional(readOnly=true)
  public SearchResult<Role> list(final Integer start, final Integer count, final Boolean total, final SearchFilter filter, final SortOption order)
    throws PersistenceException {

    // we are at SCIM hence we need to adjust the start index
    return list(Role.class, (start < 1 ? 0 : start - 1), count, total, filter, order);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** JPA request to retrieve a certain {@link Role} by its <code>id</code>.
   **
   ** @param  id                 the identifier of the {@link Role} to lookup.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link Role} mapped at <code>id</code>.
   **                            <br>
   **                            May be <code>null</code>.
   **                            <br>
   **                            Possible object is {@link Role}.
   **
   ** @throws PersistenceException if <code>clazz</code> does not denote an
   **                              entity type or the <code>id</code> is not a
   **                              valid type for the entity's primary key or is
   **                              <code>null</code>.
   */
  @Transactional(readOnly=true)
  public Role lookup(final String id)
    throws PersistenceException {

    // prevent bogus input
    if (id == null || id.length() == 0)
      throw PersistenceException.notNull(User.class, Role.Attribute.ID.id);

    return lookup(Role.class, id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** JPA request to persist a {@link Role} entity.
   **
   ** @param  entity             the entity to persist.
   **                            <br>
   **                            Allowed object is {@link Role}.
   **
   ** @return                    the entity mapped at their <code>id</code> at
   **                            the Service Provider.
   **                            <br>
   **                            Possible object is {@link Role}.
   **
   ** @throws PersistenceException if the entity manager has been closed or the
   **                              criteria query is found to be invalid.
   */
  @Transactional
  public Role create(final Role entity)
    throws PersistenceException {

    // prevent bogus state
    if (entity.getId() == null || entity.getId().isEmpty())
      throw PersistenceException.notNull(Role.class, Role.Attribute.ID.id);

    return super.create(entity);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** JPA request to modify a {@link Role} entity.
   **
   ** @param  entity             the entity to modify.
   **                            <br>
   **                            Allowed object is {@link Role}.
   **
   ** @return                    the modified entity mapped at <code>id</code>
   **                            at the Service Provider.
   **                            <br>
   **                            Possible object is {@link Role}.
   **
   ** @throws PersistenceException if the entity manager has been closed or the
   **                              criteria query is found to be invalid.
   */
  @Transactional
  public Role modify(final Role entity)
    throws PersistenceException {

    // prevent bogus state
    if (entity.getId() == null || entity.getId().isEmpty())
      throw PersistenceException.notNull(Role.class, Role.Attribute.ID.id);

    return super.modify(entity);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** JPA request to remove a {@link Role} entity.
   **
   ** @param  entity             the entity to remove.
   **                            <br>
   **                            Allowed object is {@link Role}.
   **
   ** @throws PersistenceException if the entity manager has been closed or the
   **                              criteria query is found to be invalid.
   */
  @Transactional
  public void delete(final Role entity)
    throws PersistenceException {

    // prevent bogus state
    if (entity.getId() == null || entity.getId().isEmpty())
      throw PersistenceException.notNull(Role.class, Role.Attribute.ID.id);

    super.delete(entity);
  }
}