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

    File        :   PersistenceHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    PersistenceHandler.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2014-02-23  DSteding    First release version
*/

package oracle.hst.platform.jpa;

////////////////////////////////////////////////////////////////////////////////
// interface PersistenceHandler
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** A generic persistent handler which is invoked in the data exchange
 ** processing.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface PersistenceHandler {

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   total
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
   ** @throws PersistenceException if the entity manager has been closed or the
   **                              criteria query is found to be invalid.
   */
  <T> Integer total(final Class<T> clazz, final SearchFilter filter)
    throws PersistenceException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   list
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
   */
  <T> SearchResult<T> list(final Class<T> clazz);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   list
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
   **                            retrieve as well the <code>SortOption</code> to
   **                            apply on the search.
   **                            <br>
   **                            Allowed object is {@link SearchRequest}.
   **
   ** @return                    the collection of entities.
   **                            <br>
   **                            Possible object is {@link SearchResult} where
   **                            each element is of type <code>T</code>
   **
   ** @throws PersistenceException if the entity manager has been closed or the
   **                              criteria query is found to be invalid.
   */
  <T> SearchResult<T> list(final Class<T> clazz, final SearchRequest request)
    throws PersistenceException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   list
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
   **                            each element is of type <code>T</code>
   **
   ** @throws PersistenceException  if the entity manager has been closed or the
   **                               criteria query is found to be invalid.
   */
  <T> SearchResult<T> list(final Class<T> clazz, final Integer start, final Integer count, final Boolean total, final SearchFilter filter, final SortOption order)
    throws PersistenceException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** JPA request to lookup a certain entity by its primary identifier from the
   ** persistence layer.
   **
   ** @param  <T>                  the type of the entity class returned in the
   **                              collection.
   **                              <br>
   **                              Allowed object is <code>&lt;T&gt;</code>.
   ** @param  clazz                the element type of the entity implementation
   **                              returned in the collection.
   **                              <br>
   **                              Allowed object is {@link Class} of type
   **                              <code>T</code>.
   ** @param  id                   the identifier of an entity; usually the
   **                              <code>id</code>.
   **                              <br>
   **                              Allowed object is {@link Long}.
   **
   ** @return                      the found entity instance or
   **                              <code>null</code> if the entity does not
   **                              exist
   **                              <br>
   **                              Allowed object is <code>T</code>.
   **
   ** @throws PersistenceException if <code>clazz</code> does not denote an
   **                              entity type or the <code>id</code> is not a
   **                              valid type for the entity's primary key or is
   **                              <code>null</code>.
   */
  <T extends Identifiable<Long>> T lookup(final Class<T> clazz, final Long id)
    throws PersistenceException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
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
  <T extends Identifiable<String>> T lookup(final Class<T> clazz, final String id)
    throws PersistenceException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
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
  <T> T lookup(final Class<T> clazz, final String attribute, final String value)
    throws PersistenceException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
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
   */
  <T extends Identifiable> T create(final T entity);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
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
   */
  <T extends Identifiable> T modify(final T entity);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Remove an entity instance.
   **
   ** @param  <T>                the type of the entity class to remove.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  entity             the entity instance to remove.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @throws PersistenceException if entity has {@link NonDeletable} annotation
   **                              set.
   */
  <T extends Identifiable> void delete(final T entity)
    throws PersistenceException;
}