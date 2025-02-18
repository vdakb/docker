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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Presistence Foundation Shared Library
    Subsystem   :   Generic Persistence Interface

    File        :   PersistenceService.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    PersistenceService.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2014-02-23  DSteding    First release version
*/

package oracle.hst.platform.jpa;

import java.util.List;
import java.util.ArrayList;

import java.util.stream.Collectors;

import javax.annotation.Resource;

import javax.ejb.SessionContext;

import javax.inject.Inject;

import javax.naming.InitialContext;

import javax.persistence.TypedQuery;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import javax.persistence.criteria.Root;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaBuilder;

import oracle.hst.platform.core.stream.Streams;

////////////////////////////////////////////////////////////////////////////////
// abstract class PersistenceService
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** A generic persistent handler which is invoked to process data exchanges.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class PersistenceService implements PersistenceHandler {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @Inject
  @SuppressWarnings({"oracle.jdeveloper.cdi.uncofig-project", "oracle.jdeveloper.java.annotation-callback"})
  protected EntityManager  em;

  @Resource
  protected SessionContext sessionContext;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>PersistenceService</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected PersistenceService() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   total (Persistence)
  /**
   ** Applies an aggregate expression applying the count operation to evaluate
   ** the numbers of entities in the persistence layer the belonges to the
   ** entity class specified.
   **
   ** @param  <T>                the actual entity type.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  clazz              the java {@link Class} of the entity object.
   **                            <br>
   **                            Allowed object is {@link Class}.
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
  @Override
  public <T> Integer total(final Class<T> clazz, final SearchFilter filter)
    throws PersistenceException {

    final CriteriaBuilder  builder = this.em.getCriteriaBuilder();
    final CriteriaQuery<Long> query   = builder.createQuery(Long.class);
    final Root<T>             entity  = query.from(clazz);
    final Expression<Long>    count   = builder.count(entity);
    final CriteriaQuery<Long> select  = query.select(count);

    // verify if a search filter is required
    if (filter != null)
      // apply the search filter
      select.where(filter(builder, entity, filter));

    final TypedQuery<Long> typed = this.em.createQuery(select);
    return Integer.valueOf(typed.getSingleResult().intValue());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   list (Persistence)
  /**
   ** List all entities.
   ** <br>
   ** The default ordering is by ID, descending. This does not include soft
   ** deleted entities.
   **
   ** @param  <T>                the actual entity type.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  clazz              the java {@link Class} of the entity object.
   **                            <br>
   **                            Allowed object is {@link Class}.
   **
   ** @return                    the collection of all entities.
   **                            <br>
   **                            Possible object is {@link SearchResult} where
   **                            each element is of type <code>T</code>
   **
   ** @throws PersistenceException  if the entity manager has been closed or the
   **                               criteria query is found to be invalid.
   */
  @Override
  public <T> SearchResult<T> list(final Class<T> clazz)
    throws PersistenceException {

    return list(clazz, SearchRequest.ALL);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   list (Persistence)
  /**
   ** Search for entities with {@link Class} of type <code>T</code> and returns
   ** a partial result based on given {@link SearchRequest} with the option
   ** whether to cache the results or not.
   ** <p>
   ** To ensure that the pagination approach works as expected, it have to
   ** ensured that the query always returns the result in the same order. This
   ** is only the case if the query contains an ORDER BY clause. Otherwise, the
   ** order of the result set is undefined and might change.
   **
   ** @param  <T>                the type of the entity class returned in the
   **                            collection.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  clazz              the element type of the entity implementation
   **                            returned in the collection.
   **                            <br>
   **                            Allowed object is {@link Class} of type
   **                            <code>T</code>.
   ** @param  request            the {@link SearchRequest} providing the start
   **                            position of the first result, numbered from 0
   **                            and the desired maximum number of results to
   **                            retrieve and the {@link SortOption} to apply
   **                            on the search.
   **                            <br>
   **                            Allowed object is {@link SearchRequest}.
   **
   ** @return                    the collection of entities.
   **                            <br>
   **                            Possible object is {@link SearchResult} where
   **                            each element is of type <code>T</code>
   **
   ** @throws PersistenceException  if the entity manager has been closed or the
   **                               criteria query is found to be invalid.
   */
  @Override
  public <T> SearchResult<T> list(final Class<T> clazz, final SearchRequest request)
    throws PersistenceException {

    return list(clazz, request.start(), request.count(), request.total(), request.filter(), request.order());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup (Persistence)
  /**
   ** JPA request to lookup a certain entity by its primary identifier from the
   ** persistence layer.
   **
   ** @param  <T>                the type of the entity class returned in the
   **                            collection.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  clazz              the element type of the entity implementation
   **                            returned in the collection.
   **                            <br>
   **                            Allowed object is {@link Class} of type
   **                            <code>T</code>.
   ** @param  id                 the identifier of an entity; usually the
   **                            <code>id</code>.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    the found entity instance or
   **                            <code>null</code> if the entity does not
   **                            exist
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @throws PersistenceException if <code>clazz</code> does not denote an
   **                              entity type or the <code>id</code> is not a
   **                              valid type for the entity's primary key or is
   **                              <code>null</code>.
   */
  @Override
  public <T extends Identifiable<Long>> T lookup(final Class<T> clazz, final Long id)
    throws PersistenceException {

    try {
      return this.em.find(clazz, id);
     }
    catch (IllegalArgumentException e) {
      throw PersistenceException.general(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup (PersistenceHandler)
  /**
   ** JPA request to find a certain entity by its primary identifier from the
   ** persistence layer.
   **
   ** @param  <T>                the type of the entity class returned in the
   **                            collection.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  clazz              the element type of the entity implementation
   **                            returned in the collection.
   **                            <br>
   **                            Allowed object is {@link Class} of type
   **                            <code>T</code>.
   ** @param  id                 the identifier of an entity; usually the
   **                            <code>id</code> or an alternate unique key.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    the found entity instance or
   **                            <code>null</code> if the entity does not
   **                            exist
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @throws PersistenceException if <code>clazz</code> does not denote an
   **                              entity type or the <code>id</code> is not a
   **                              valid type for the entity's primary key or is
   **                              <code>null</code>.
   */
  @Override
  public <T extends Identifiable<String>> T lookup(final Class<T> clazz, final String id)
    throws PersistenceException {

    try {
      return this.em.find(clazz, id);
     }
    catch (IllegalArgumentException e) {
      throw PersistenceException.general(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup (PersistenceHandler)
  /**
   ** JPA request to lookup a certain entity by a criteria filter from the
   ** persistence layer.
   **
   ** @param  <T>                the type of the entity class returned in the
   **                            collection.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  clazz              the element type of the entity implementation
   **                            returned in the collection.
   **                            <br>
   **                            Allowed object is {@link Class} of type
   **                            <code>T</code>.
   ** @param  attribute          the name of the alternate key of an entity;
   **                            e.g. <code>username</code> for
   **                            <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the identifier of an entity; usually an
   **                            alternate unique key like
   **                            <code>username</code> for <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the found entity instance or
   **                            <code>null</code> if the entity does not
   **                            exist
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @throws PersistenceException if <code>clazz</code> does not denote an
   **                              entity type or the <code>id</code> is not a
   **                              valid type for the entity's primary key or is
   **                              <code>null</code>.
   */
  @Override
  public <T> T lookup(final Class<T> clazz, final String attribute, final String value)
    throws PersistenceException {

    try {
      final CriteriaBuilder  builder = this.em.getCriteriaBuilder();
      final CriteriaQuery<T> query   = builder.createQuery(clazz);
      final Root<T>          entity  = query.from(clazz);
      final TypedQuery<T>    typed   = this.em.createQuery(query.select(entity).where(builder.equal(entity.get(attribute), value)));
      return typed.getSingleResult();
    }
    catch (IllegalStateException e) {
      throw PersistenceException.abort(e);
    }
    catch (IllegalArgumentException e) {
      throw PersistenceException.general(e);
    }
    catch (NoResultException e) {
      return null;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create (PersistenceHandler)
  /**
   ** JPA request to persist a certain entity.
   **
   ** @param  <T>                the type of the entity class to create.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  entity             the entity to persist.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    the entity mapped at their <code>id</code> at
   **                            the Service Provider.
   **                            <br>
   **                            Possible object is <code>T</code>.
   **
   ** @throws PersistenceException if <code>clazz</code> does not denote an
   **                              entity type or the <code>id</code> is not a
   **                              valid type for the entity's primary key or is
   **                              <code>null</code>.
   */
  @Override
  public <T extends Identifiable> T create(final T entity)
    throws PersistenceException {

    // prevent bogus request
    if (entity.getClass().isAnnotationPresent(NonCreatable.class))
			throw PersistenceException.notCreatable(entity.getClass());

    try {
      this.em.persist(entity);
      // flushing entity manager to upstream the entity
      this.em.flush();
      // get back all values managed by the database
      this.em.refresh(entity);
      // send back the result of the operation
      return entity;
    }
    catch (IllegalStateException e) {
      throw PersistenceException.general(e);
    }
    catch (IllegalArgumentException e) {
      throw PersistenceException.abort(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify (PersistenceHandler)
  /**
   ** JPA request to modify a certain entity.
   **
   ** @param  <T>                the type of the entity class to modify.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  entity             the entity to modify.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    the modified entity mapped at <code>id</code>
   **                            at the Service Provider.
   **                            <br>
   **                            Possible object is <code>T</code>.
   **
   ** @throws PersistenceException if <code>clazz</code> does not denote an
   **                              entity type or the <code>id</code> is not a
   **                              valid type for the entity's primary key or is
   **                              <code>null</code>.
   */
  @Override
  @SuppressWarnings({"unchecked", "cast"})
  public <T extends Identifiable> T modify(final T entity)
    throws PersistenceException {

    // prevent bogus request
    if (entity.getClass().isAnnotationPresent(NonModifiable.class))
			throw PersistenceException.notModifiable(entity.getClass());

    try {
      final T result = this.em.merge(entity);
      // flushing entity manager to upstream the entity
      this.em.flush();
      // get back all values managed by the database and send back the result of
      // the operation
      this.em.refresh(result);
      // send back the result of the operation
      return result;
    }
    catch (IllegalStateException e) {
      throw PersistenceException.abort(e);
    }
    catch (IllegalArgumentException e) {
      throw PersistenceException.general(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete (PersistenceHandler)
  /**
   ** Remove an entity instance.
   **
   ** @param  <T>                the type of the entity class to remove.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param entity              the entity instance to remove.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @throws PersistenceException if entity has {@link NonDeletable} annotation
   **                              set or the entity could not be deleted.
   */
  @Override
  public <T extends Identifiable> void delete(final T entity)
    throws PersistenceException {

    // prevent bogus request
    if (entity.getClass().isAnnotationPresent(NonDeletable.class))
			throw PersistenceException.notDeletable(entity.getClass());

    // we need an intermediate entity to merge id necessary
    T temp = entity;
    try {
      if (!this.em.contains(temp))
        temp = this.em.merge(temp);
      this.em.remove(temp);
    }
    catch (IllegalStateException e) {
      throw PersistenceException.abort(e);
    }
    catch (IllegalArgumentException e) {
      throw PersistenceException.general(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   list (PersistenceHandler)
  /**
   ** Search for entities with {@link Class} of type <code>T</code> and returns
   ** a partial result based on given pagination properties with the option
   ** whether to cache the results or not.
   ** <p>
   ** To ensure that the pagination approach works as expected, it have to
   ** ensured that the query always returns the result in the same order. This
   ** is only the case if the query contains an ORDER BY clause. Otherwise, the
   ** order of the result set is undefined and might change.
   **
   ** @param  <T>                the type of the entity class returned in the
   **                            collection.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  clazz              the element type of the entity implementation
   **                            returned in the collection.
   **                            <br>
   **                            Allowed object is {@link Class} of type
   **                            <code>T</code>.
   ** @param  start              the 0-based index of the first search result or
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
   ** @param  filter             the {@link SearchFilter} to apply on the
   **                            search.
   **                            <br>
   **                            Allowed object is {@link SearchFilter}.
   ** @param  order              the {@link SortOption} to apply on the search.
   **                            <br>
   **                            Allowed object is {@link SortOption}.
   **
   ** @return                    the collection of entities.
   **                            <br>
   **                            Possible object is {@link SearchResult} where
   **                            each element is of type <code>T</code>
   **
   ** @throws PersistenceException if the entity manager has been closed or the
   **                              criteria query is found to be invalid.
   */
  @Override
  public <T> SearchResult<T> list(final Class<T> clazz, final Integer start, final Integer count, final Boolean total, final SearchFilter filter, final SortOption order)
    throws PersistenceException {

    try {
      // optimize the query execution by checking first the overall amount of
      // entities returned
      final Integer amount = total ? total(clazz, filter) : -1;
      // prevent population of the search result if its obvious that no data
      // available for the search filter
      if (amount == 0)
        return new SearchResult<T>(0);

      // fix bogus input; keep in mind the start offset in the request is 1-based
      final int              first   = (start == null || start != null && start < 1)    ? 0    : start;
      final int              max     = (count == null || count != null && count > 1000) ? 1000 : count;
      final CriteriaBuilder  builder = this.em.getCriteriaBuilder();
      final CriteriaQuery<T> query   = builder.createQuery(clazz);
      final Root<T>          entity  = query.from(query.getResultType());
      final CriteriaQuery<T> select  = query.select(entity);

      // verify if a search filter is required
      if (filter != null)
        // apply the search filter
        select.where(filter(builder, entity, filter));

      // verify if a sort order is required
      if (order != null)
        // apply the sort order
        select.orderBy(order(builder, entity, order));

      final TypedQuery<T> typed = this.em.createQuery(select).setFirstResult(first).setMaxResults(max);
      return typed.getResultList().stream().collect(() -> new SearchResult<T>(amount), SearchResult<T>::add, SearchResult<T>::addAll);
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
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   current
  /**
   ** Returns the currently active <code>BaseEntityService</code> from the
   ** {@link SessionContext}.
   **
   ** @return                    the currently active
   **                            <code>BaseEntityService</code> from the
   **                            {@link SessionContext}.
   **                            <br>
   **                            Possible object is {@link PersistenceService}.
   **
   ** @throws IllegalStateException if there is none, which can happen if this
   **                               method is called outside EJB context, or
   **                               when currently invoked EJB service is not an
   **                               instance of {@link PersistenceService}.
   */
  @SuppressWarnings("unchecked")
  public static PersistenceService current() {
    try {
      final SessionContext context = (SessionContext)new InitialContext().lookup("java:comp/EJBContext");
      return (PersistenceService)context.getBusinessObject(context.getInvokedBusinessInterface());
    }
    catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filter
  /**
   ** Factory method to create a {@link Predicate} applicable as restrictions on
   ** a search.
   **
   ** @param  <T>                the type of the entity class the
   **                            {@link Predicate} belongs.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  builder            the {@link CriteriaBuilder} used to build the
   **                            search filter.
   **                            <br>
   **                            Allowed object is {@link CriteriaBuilder}.
   ** @param  entity             the {@link Root} entity the search filter will
   **                            be applied on.
   **                            <br>
   **                            Allowed object is {@link Root} of type
   **                            <code>T</code>.
   ** @param  filter             the {@link SearchFilter} providing the
   **                            search criteria to apply, if any.
   **                            <br>
   **                            Allowed object is {@link SearchFilter}.
   **
   ** @throws PersistenceException if attribute derived from
   **                              {@link SearchFilter} does not otherwise
   **                              exist.
   */
  private static <T> Predicate filter(final CriteriaBuilder builder, final Root<T> entity, final SearchFilter filter)
    throws PersistenceException {

    final SearchFilter.Type type = filter.type();
    switch (type) {
      case OR  : return builder.or (junction(builder, entity, ((SearchFilter.Or)filter).filter()));
      case AND : return builder.and(junction(builder, entity, ((SearchFilter.And)filter).filter()));
      case NOT : return builder.not(filter(builder, entity, ((SearchFilter.Not)filter).filter()));

      case EQ  : return builder.equal(entity.get(filter.path()), filter.value());
      case SW  : return sw(builder, entity, filter);
      case EW  : return ew(builder, entity, filter);
      case CO  : return co(builder, entity, filter);
      case LT  : return lt(builder, entity, filter);
      case LE  : return le(builder, entity, filter);
      case GT  : return gt(builder, entity, filter);
      case GE  : return ge(builder, entity, filter);
    }
    throw PersistenceException.general(PersistenceBundle.string(PersistenceError.CRITERIA_SPEC));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   junction
  /**
   ** Logically combines together the instances of a collection of
   ** {@link SearchFilter}.
   **
   ** @param  <T>                the type of the entity class the
   **                            {@link Predicate} belongs.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  builder            the {@link CriteriaQuery} used to build the
   **                            resulting {@link Predicate}.
   **                            <br>
   **                            Allowed object is {@link CriteriaQuery}.
   ** @param  query              the {@link CriteriaQuery} to extend with the
   **                            {@link Predicate}.
   **                            <br>
   **                            Allowed object is {@link CriteriaBuilder}.
   ** @param  entity             the {@link Root} entity the search filter will
   **                            be applied on.
   **                            <br>
   **                            Allowed object is {@link Root} of type
   **                            <code>T</code>.
   ** @param  filter             the {@link SearchFilter} collection to
   **                            transform to a collection of {@link Predicate}.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link SearchFilter}.
   **
   ** @return                    the resulting collection of {@link Predicate}s.
   **                            <br>
   **                            Possible object is array of {@link Predicate}.
   **
   ** @throws PersistenceException if attribute derived from
   **                              {@link SearchFilter} does not otherwise
   **                              exist.
   */
  private static <T> Predicate[] junction(final CriteriaBuilder builder, final Root<T> entity, final List<SearchFilter<?>> filter)
    throws PersistenceException {

    final List<Predicate> collector   = new ArrayList<Predicate>();
    for (SearchFilter<?> cursor : filter) {
      collector.add(filter(builder, entity, cursor));
    }
    return collector.toArray(new Predicate[0]);              
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lt
  /**
   ** Factory method to create a new <code>less than</code>
   ** {@link Predicate} for the specified <code>entity> leveraging passed
   ** {@link CriteriaBuilder}.
   **
   ** @param  <T>                the type of the entity class the
   **                            {@link Predicate} belongs.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  builder            the {@link CriteriaBuilder} used to build the
   **                            search filter.
   **                            <br>
   **                            Allowed object is {@link CriteriaBuilder}.
   ** @param  entity             the {@link Root} entity the search filter will
   **                            be applied on.
   **                            <br>
   **                            Allowed object is {@link Root} of type
   **                            <code>T</code>.
   ** @param  filter             the {@link SearchFilter} providing the
   **                            search criteria to apply, if any.
   **                            <br>
   **                            Allowed object is {@link SearchFilter}.
   **
   ** @return                    the {@link Predicate} wrapping the
   **                            search criteria.
   **                            <br>
   **                            Possible object is {@link Predicate}.
   **
   ** @throws PersistenceException if attribute derived from
   **                              {@link SearchFilter} does not otherwise
   **                              exist.
   */
  @SuppressWarnings({"unchecked", "cast"})
  private static <T> Predicate lt(final CriteriaBuilder builder, final Root<T> entity, final SearchFilter filter)
    throws PersistenceException {

    try {
      // if we have a numeric value choose the short path
      if (filter instanceof SearchFilter.Numeric) {
        return builder.lt(entity.get(filter.path()), ((SearchFilter.Numeric)filter).value());
      }
      // if we have a temporal value choose the long path
      else if (filter instanceof SearchFilter.Temporal) {
        return builder.lessThan(entity.get(filter.path()), ((SearchFilter.Temporal)filter).value());
      }
      // otherwise if the long path
      else 
        return builder.lessThan(entity.get(filter.path()), filter.value().toString());
    }
    catch (IllegalArgumentException e) {
      throw PersistenceException.abort(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   le
  /**
   ** Factory method to create a new <code>less than or equal to</code>
   ** {@link Predicate} for the specified <code>entity> leveraging passed
   ** {@link CriteriaBuilder}.
   **
   ** @param  <T>                the type of the entity class the
   **                            {@link Predicate} belongs.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  builder            the {@link CriteriaBuilder} used to build the
   **                            search filter.
   **                            <br>
   **                            Allowed object is {@link CriteriaBuilder}.
   ** @param  entity             the {@link Root} entity the search filter will
   **                            be applied on.
   **                            <br>
   **                            Allowed object is {@link Root} of type
   **                            <code>T</code>.
   ** @param  filter             the {@link SearchFilter} providing the
   **                            search criteria to apply, if any.
   **                            <br>
   **                            Allowed object is {@link SearchFilter}.
   **
   ** @return                    the {@link Predicate} wrapping the
   **                            search criteria.
   **                            <br>
   **                            Possible object is {@link Predicate}.
   **
   ** @throws PersistenceException if attribute derived from
   **                              {@link SearchFilter} does not otherwise
   **                              exist.
   */
  @SuppressWarnings({"unchecked", "cast"})
  private static <T> Predicate le(final CriteriaBuilder builder, final Root<T> entity, final SearchFilter filter)
    throws PersistenceException {

    try {
      // if we have a numeric value choose the short path
      if (filter instanceof SearchFilter.Numeric) {
        return builder.le(entity.get(filter.path()), ((SearchFilter.Numeric)filter).value());
      }
      // if we have a temporal value choose the long path
      else if (filter instanceof SearchFilter.Temporal) {
        return builder.lessThanOrEqualTo(entity.get(filter.path()), ((SearchFilter.Temporal)filter).value());
      }
      // otherwise if the long path
      else 
        return builder.lessThanOrEqualTo(entity.get(filter.path()), filter.value().toString());
    }
    catch (IllegalArgumentException e) {
      throw PersistenceException.abort(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gt
  /**
   ** Factory method to create a new <code>greater than</code>
   ** {@link Predicate} for the specified <code>entity> leveraging passed
   ** {@link CriteriaBuilder}.
   **
   ** @param  <T>                the type of the entity class the
   **                            {@link Predicate} belongs.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  builder            the {@link CriteriaBuilder} used to build the
   **                            search filter.
   **                            <br>
   **                            Allowed object is {@link CriteriaBuilder}.
   ** @param  entity             the {@link Root} entity the search filter will
   **                            be applied on.
   **                            <br>
   **                            Allowed object is {@link Root} of type
   **                            <code>T</code>.
   ** @param  filter             the {@link SearchFilter} providing the
   **                            search criteria to apply, if any.
   **                            <br>
   **                            Allowed object is {@link SearchFilter}.
   **
   ** @return                    the {@link Predicate} wrapping the
   **                            search criteria.
   **                            <br>
   **                            Possible object is {@link Predicate}.
   **
   ** @throws PersistenceException if attribute derived from
   **                              {@link SearchFilter} does not otherwise
   **                              exist.
   */
  @SuppressWarnings({"unchecked", "cast"})
  private static <T> Predicate gt(final CriteriaBuilder builder, final Root<T> entity, final SearchFilter filter)
    throws PersistenceException {

    try {
      // if we have a numeric value choose the short path
      if (filter instanceof SearchFilter.Numeric) {
        return builder.gt(entity.get(filter.path()), ((SearchFilter.Numeric)filter).value());
      }
      // if we have a temporal value choose the long path
      else if (filter instanceof SearchFilter.Temporal) {
        return builder.greaterThan(entity.get(filter.path()), ((SearchFilter.Temporal)filter).value());
      }
      // otherwise if the long path
      else 
        return builder.greaterThan(entity.get(filter.path()), filter.value().toString());
    }
    catch (IllegalArgumentException e) {
      throw PersistenceException.abort(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ge
  /**
   ** Factory method to create a new <code>greater than or equal to</code>
   ** {@link Predicate} for the specified <code>entity> leveraging passed
   ** {@link CriteriaBuilder}.
   **
   ** @param  <T>                the type of the entity class the
   **                            {@link Predicate} belongs.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  builder            the {@link CriteriaBuilder} used to build the
   **                            search filter.
   **                            <br>
   **                            Allowed object is {@link CriteriaBuilder}.
   ** @param  entity             the {@link Root} entity the search filter will
   **                            be applied on.
   **                            <br>
   **                            Allowed object is {@link Root} of type
   **                            <code>T</code>.
   ** @param  filter             the {@link SearchFilter} providing the
   **                            search criteria to apply, if any.
   **                            <br>
   **                            Allowed object is {@link SearchFilter}.
   **
   ** @return                    the {@link Predicate} wrapping the
   **                            search criteria.
   **                            <br>
   **                            Possible object is {@link Predicate}.
   **
   ** @throws PersistenceException if attribute derived from
   **                              {@link SearchFilter} does not otherwise
   **                              exist.
   */
  @SuppressWarnings({"unchecked", "cast"})
  private static <T> Predicate ge(final CriteriaBuilder builder, final Root<T> entity, final SearchFilter filter)
    throws PersistenceException {

    try {
      // if we have a numeric value choose the short path
      if (filter instanceof SearchFilter.Numeric) {
        return builder.ge(entity.get(filter.path()), ((SearchFilter.Numeric)filter).value());
      }
      // if we have a temporal value choose the long path
      else if (filter instanceof SearchFilter.Temporal) {
        return builder.greaterThanOrEqualTo(entity.get(filter.path()), ((SearchFilter.Temporal)filter).value());
      }
      // otherwise if the long path
      else 
        return builder.greaterThanOrEqualTo(entity.get(filter.path()), filter.value().toString());
    }
    catch (IllegalArgumentException e) {
      throw PersistenceException.abort(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sw
  /**
   ** Factory method to create a new <code>starts with</code> {@link Predicate}
   ** for the specified <code>entity> leveraging passed {@link CriteriaBuilder}.
   **
   ** @param  <T>                the type of the entity class the
   **                            {@link Predicate} belongs.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  builder            the {@link CriteriaBuilder} used to build the
   **                            search filter.
   **                            <br>
   **                            Allowed object is {@link CriteriaBuilder}.
   ** @param  entity             the {@link Root} entity the search filter will
   **                            be applied on.
   **                            <br>
   **                            Allowed object is {@link Root} of type
   **                            <code>T</code>.
   ** @param  filter             the {@link SearchFilter} providing the
   **                            search criteria to apply, if any.
   **                            <br>
   **                            Allowed object is {@link SearchFilter}.
   **
   ** @return                    the {@link Predicate} wrapping the
   **                            search criteria.
   **                            <br>
   **                            Possible object is {@link Predicate}.
   **
   ** @throws PersistenceException if attribute derived from
   **                              {@link SearchFilter} does not otherwise
   **                              exist.
   */
  private static <T> Predicate sw(final CriteriaBuilder builder, final Root<T> entity, final SearchFilter filter)
    throws PersistenceException {

    try {
      return builder.like(entity.get(filter.path()), filter.value() + "%");
    }
    catch (IllegalArgumentException e) {
      throw PersistenceException.abort(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ew
  /**
   ** Factory method to create a new <code>ends with</code> {@link Predicate}
   ** for the specified <code>entity> leveraging passed {@link CriteriaBuilder}.
   **
   ** @param  <T>                the type of the entity class the
   **                            {@link Predicate} belongs.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  builder            the {@link CriteriaBuilder} used to build the
   **                            search filter.
   **                            <br>
   **                            Allowed object is {@link CriteriaBuilder}.
   ** @param  entity             the {@link Root} entity the search filter will
   **                            be applied on.
   **                            <br>
   **                            Allowed object is {@link Root} of type
   **                            <code>T</code>.
   ** @param  filter             the {@link SearchFilter} providing the
   **                            search criteria to apply, if any.
   **                            <br>
   **                            Allowed object is {@link SearchFilter}.
   **
   ** @return                    the {@link Predicate} wrapping the
   **                            search criteria.
   **                            <br>
   **                            Possible object is {@link Predicate}.
   **
   ** @throws PersistenceException if attribute derived from
   **                              {@link SearchFilter} does not otherwise
   **                              exist.
   */
  private static <T> Predicate ew(final CriteriaBuilder builder, final Root<T> entity, final SearchFilter filter)
    throws PersistenceException {

    try {
      return builder.like(entity.get(filter.path()), "%" + filter.value());
    }
    catch (IllegalArgumentException e) {
      throw PersistenceException.abort(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   co
  /**
   ** Factory method to create a new <code>contains</code> {@link Predicate}
   ** for the specified <code>entity> leveraging passed {@link CriteriaBuilder}.
   **
   ** @param  <T>                the type of the entity class the
   **                            {@link Predicate} belongs.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  builder            the {@link CriteriaBuilder} used to build the
   **                            search filter.
   **                            <br>
   **                            Allowed object is {@link CriteriaBuilder}.
   ** @param  entity             the {@link Root} entity the search filter will
   **                            be applied on.
   **                            <br>
   **                            Allowed object is {@link Root} of type
   **                            <code>T</code>.
   ** @param  filter             the {@link SearchFilter} providing the
   **                            search criteria to apply, if any.
   **                            <br>
   **                            Allowed object is {@link SearchFilter}.
   **
   ** @return                    the {@link Predicate} wrapping the
   **                            search criteria.
   **                            <br>
   **                            Possible object is {@link Predicate}.
   **
   ** @throws PersistenceException if attribute derived from
   **                              {@link SearchFilter} does not otherwise
   **                              exist.
   */
  private static <T> Predicate co(final CriteriaBuilder builder, final Root<T> entity, final SearchFilter filter)
    throws PersistenceException {

    try {
      return builder.like(entity.get(filter.path()), "%" + filter.value() + "%");
    }
    catch (IllegalArgumentException e) {
      throw PersistenceException.abort(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   order
  /**
   ** Factory method to create an {@link Order} criteria to apply on a search.
   **
   ** @param  builder            the {@link CriteriaBuilder} used to build the
   **                            sort order.
   **                            <br>
   **                            Allowed object is {@link CriteriaBuilder}.
   ** @param  option             the {@link SortOption} providing the sort
   **                            order to apply, if any.
   **                            <br>
   **                            Allowed object is {@link SearchRequest}.
   */
  private static <T> List<Order> order(final CriteriaBuilder builder, final Root<T> entity, final SortOption option) {
    return Streams.stream(option.criteria()).filter(o -> o != null).map(o -> o.ascending() ? builder.asc(entity.get(o.property)) : builder.desc(entity.get(o.property))).collect(Collectors.toList());
  }
}