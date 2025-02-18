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

    File        :   EntityManagerDelegate.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    EntityManagerDelegate.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2014-02-23  DSteding    First release version
*/

package oracle.hst.platform.jpa.injection;

import java.util.Map;
import java.util.List;

import javax.inject.Inject;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.EntityGraph;
import javax.persistence.LockModeType;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.EntityTransaction;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.EntityExistsException;
import javax.persistence.TransactionRequiredException;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaBuilder;

import javax.persistence.metamodel.Metamodel;

import javax.enterprise.context.ApplicationScoped;

////////////////////////////////////////////////////////////////////////////////
// class EntityManagerDelegate
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** An implementation of {@link EntityManager} used to interact with the
 ** persistence context.
 ** <p>
 ** An {@link EntityManager} instance is associated with  a persistence context.
 ** A persistence context is a set of entity  instances in which for any
 ** persistent entity identity there is a unique entity instance. Within the
 ** persistence context, the entity instances and their lifecycle are managed.
 ** <br>
 ** The {@link EntityManager} API is used to create and remove persistent entity
 ** instances, to find entities by their primary key, and to query over
 ** entities.
 ** <p>
 ** The set of entities that can be managed by a given {@link EntityManager}
 ** instance is defined by a persistence unit. A persistence unit defines the
 ** set of all classes that are  related or grouped by the application, and
 ** which must be  colocated in their mapping to a single database.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@ApplicationScoped
public class EntityManagerDelegate implements EntityManager {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @Inject
  private EntityManagerStore store;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>EntityManagerDelegate</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public EntityManagerDelegate() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   persist (EntityManager)
  /**
   ** Make an instance managed and persistent.
   **
   ** @param  entity             the entity instance.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @throws EntityExistsException        if the entity already exists.
   **                                      (If the entity already exists, the
   **                                      <code>EntityExistsException</code>
   **                                      may be thrown when the persist
   **                                      operation is invoked, or the
   **                                      <code>EntityExistsException</code> or
   **                                      another
   **                                      <code>PersistenceException</code> may
   **                                      be thrown at flush or commit time.)
   ** @throws IllegalArgumentException     if the instance is not an entity,
   ** @throws TransactionRequiredException if there is no transaction when
   **                                      invoked on a container-managed entity
   **                                      manager of that is of type
   **                                      <code>PersistenceContextType.TRANSACTION</code>,
   */
  @Override
  public void persist(final Object entity)
    throws EntityExistsException
    ,      IllegalArgumentException
    ,      TransactionRequiredException {

    this.store.current().persist(entity);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   merge (EntityManager)
  /**
   ** Merge the state of the given entity into the current persistence context.
   **
   ** @param  entity             the entity instance.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    the managed instance that the state was merged
   **                            to
   ** @throws IllegalArgumentException     if instance is not an entity or is a
   **                                      removed entity.
   ** @throws TransactionRequiredException if there is no transaction when
   **                                      invoked on a container-managed entity
   **                                      manager of that is of type
   **                                      <code>PersistenceContextType.TRANSACTION</code>,
   */
  @Override
  public <T> T merge(T entity)
    throws IllegalArgumentException
    ,      TransactionRequiredException {

    return this.store.current().merge(entity);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   remove (EntityManager)
  @Override
  public void remove(Object entity) {
    store.current().remove(entity);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   find (EntityManager)
  @Override
  public <T> T find(Class<T> entityClass, Object primaryKey) {
    return this.store.current().find(entityClass, primaryKey);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   find (EntityManager)
  @Override
  public <T> T find(Class<T> entityClass, Object primaryKey, Map<String, Object> properties) {
    return this.store.current().find(entityClass, primaryKey, properties);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   find (EntityManager)
  @Override
  public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode) {
    return store.current().find(entityClass, primaryKey, lockMode);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   find (EntityManager)
  @Override
  public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode, Map<String, Object> properties) {
    return this.store.current().find(entityClass, primaryKey, lockMode, properties);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   find (EntityManager)
  @Override
  public <T> T getReference(Class<T> entityClass, Object primaryKey) {
    return this.store.current().getReference(entityClass, primaryKey);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   flush (EntityManager)
  @Override
  public void flush() {
    this.store.current().flush();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setFlushMode (EntityManager)
  @Override
  public void setFlushMode(FlushModeType flushMode) {
    this.store.current().setFlushMode(flushMode);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getFlushMode (EntityManager)
  @Override
  public FlushModeType getFlushMode() {
    return this.store.current().getFlushMode();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lock (EntityManager)
  @Override
  public void lock(Object entity, LockModeType lockMode) {
    this.store.current().lock(entity, lockMode);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lock (EntityManager)
  @Override
  public void lock(Object entity, LockModeType lockMode, Map<String, Object> properties) {
    this.store.current().lock(entity, lockMode, properties);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refresh (EntityManager)
  @Override
  public void refresh(Object entity) {
    this.store.current().refresh(entity);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refresh (EntityManager)
  @Override
  public void refresh(Object entity, Map<String, Object> properties) {
    this.store.current().refresh(entity, properties);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refresh (EntityManager)
  @Override
  public void refresh(Object entity, LockModeType lockMode) {
    this.store.current().refresh(entity, lockMode);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refresh (EntityManager)
  @Override
  public void refresh(Object entity, LockModeType lockMode, Map<String, Object> properties) {
    this.store.current().refresh(entity, lockMode, properties);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clear (EntityManager)
  @Override
  public void clear() {
    this.store.current().clear();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   detach (EntityManager)
  @Override
  public void detach(Object entity) {
    this.store.current().detach(entity);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contains (EntityManager)
  @Override
  public boolean contains(Object entity) {
    return this.store.current().contains(entity);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLockMode (EntityManager)
  @Override
  public LockModeType getLockMode(Object entity) {
    return this.store.current().getLockMode(entity);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setProperty (EntityManager)
  @Override
  public void setProperty(String propertyName, Object value) {
    this.store.current().setProperty(propertyName, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getProperties (EntityManager)
  @Override
  public Map<String, Object> getProperties() {
    return this.store.current().getProperties();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createQuery (EntityManager)
  @Override
  public Query createQuery(String qlString) {
    return this.store.current().createQuery(qlString);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createQuery (EntityManager)
  @Override
  public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
    return this.store.current().createQuery(criteriaQuery);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createQuery (EntityManager)
  @Override
  public Query createQuery(CriteriaUpdate updateQuery) {
    return this.store.current().createQuery(updateQuery);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createQuery (EntityManager)
  @Override
  public Query createQuery(CriteriaDelete deleteQuery) {
    return this.store.current().createQuery(deleteQuery);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createQuery (EntityManager)
  @Override
  public <T> TypedQuery<T> createQuery(String qlString, Class<T> resultClass) {
    return this.store.current().createQuery(qlString, resultClass);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createNamedQuery (EntityManager)
  @Override
  public Query createNamedQuery(String name) {
    return this.store .current() .createNamedQuery(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createNamedQuery (EntityManager)
  @Override
  public <T> TypedQuery<T> createNamedQuery(String name, Class<T> resultClass) {
    return this.store.current().createNamedQuery(name, resultClass);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createNativeQuery (EntityManager)
  @Override
  public Query createNativeQuery(String sqlString) {
    return this.store.current().createNativeQuery(sqlString);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createNativeQuery (EntityManager)
  @Override
  public Query createNativeQuery(String sqlString, Class resultClass) {
    return this.store.current().createNativeQuery(sqlString, resultClass);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createNativeQuery (EntityManager)
  @Override
  public Query createNativeQuery(String sqlString, String resultSetMapping) {
    return this.store.current().createNativeQuery(sqlString, resultSetMapping);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createNamedStoredProcedureQuery (EntityManager)
  @Override
  public StoredProcedureQuery createNamedStoredProcedureQuery(String name) {
    return this.store.current().createNamedStoredProcedureQuery(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createStoredProcedureQuery (EntityManager)
  @Override
  public StoredProcedureQuery createStoredProcedureQuery(String procedureName) {
    return this.store.current().createStoredProcedureQuery(procedureName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createStoredProcedureQuery (EntityManager)
  @Override
  public StoredProcedureQuery createStoredProcedureQuery(String procedureName, Class... resultClasses) {
    return this.store.current().createStoredProcedureQuery(procedureName, resultClasses);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createStoredProcedureQuery (EntityManager)
  @Override
  public StoredProcedureQuery createStoredProcedureQuery(String procedureName, String... resultSetMappings) {
    return this.store.current().createStoredProcedureQuery(procedureName, resultSetMappings);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   joinTransaction (EntityManager)
  /**
   ** Indicate to the entity manager that a JTA transaction is active and join
   ** the persistence context to it.
   ** <p>
   ** This method should be called on a JTA application managed entity manager
   ** that was created outside the scope of the active transaction or on an
   ** entity manager of type <code>SynchronizationType.UNSYNCHRONIZED</code> to
   ** associate it with the current JTA transaction.
   **
   ** @throws TransactionRequiredException if there is no transaction.
   */
  @Override
  public void joinTransaction()
    throws TransactionRequiredException {

    this.store.current().joinTransaction();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isJoinedToTransaction (EntityManager)

  /**
   ** Determine whether the entity manager is joined to the current transaction.
   ** <br>
   ** Returns <code>false</code> if the entity manager is not joined to the
   ** current transaction or if no transaction is active.
   **
   ** @return                    <code>true</code> if the entity manager is
   **                            joined to the current transaction; otherwise
   **                            <code>false</code>.
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  public boolean isJoinedToTransaction() {
    return this.store.current().isJoinedToTransaction();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unwrap (EntityManager)
  /**
   ** Return an object of the specified type to allow access to the
   ** provider-specific API.
   ** <br>
   ** If the provider's <code>EntityManager</code> implementation does not
   ** support the specified class, the <code>PersistenceException</code> is
   ** thrown.
   **
   ** @param  <T>                the type of the object to be returned.
   ** @param  clazz              the class of the object to be returned.
   **                            <br>
   **                            This is normally either the underlying
   **                            <code>EntityManager</code> implementation class
   **                            or an interface that it implements.
   **
   ** @return                    an instance of the specified class.
   **                            <br>
   **                            Possible object is {@link Class} for type
   **                            <code>T</code>.
   **
   ** @throws PersistenceException if the provider does not support the call.
   */
  @Override
  public <T> T unwrap(final Class<T> clazz)
    throws PersistenceException {

    return this.store.current().unwrap(clazz);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDelegate (EntityManager)
  /**
   ** Return the underlying provider object for the <code>EntityManager</code>,
   ** if available.
   ** <br>
   ** The result of this method is implementation specific.
   ** <p>
   ** The {@link #unwrap(Class)} method is to be preferred for new applications.
   **
   ** @return                    the underlying provider object for the
   **                            <code>EntityManager</code>, if available.
   **                            <br>
   **                            Possible object is {@link Object}.
   */
  @Override
  public Object getDelegate() {
    return this.store.current().getDelegate();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   close (EntityManager)
  /**
   ** Close an application-managed entity manager.
   ** <br>
   ** After the close method has been invoked, all methods on the
   ** <code>EntityManager</code> instance and any {@link Query},
   ** {@link TypedQuery}, and {@link StoredProcedureQuery} objects obtained from
   ** it will throw the {@link IllegalStateException} except for
   ** {@link #getProperties}, {@link #getTransaction}, and {@link #isOpen()}
   ** (which will return <code>false</code>).
   ** <br>
   ** If this method is called when the entity manager is joined to an active
   ** transaction, the persistence context remains managed until the transaction
   ** completes.
   **
   ** @throws IllegalStateException if the entity manager is container-managed.
   */
  @Override
  public void close() {
    this.store.current().close();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isOpen (EntityManager)
  /**
   ** Determine whether the entity manager is open.
   **
   ** @return                    <code>true</code> until the entity manager has
   **                            been closed.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  public boolean isOpen() {
    return this.store.current().isOpen();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getTransaction (EntityManager)
  /**
   ** Return the resource-level {@link EntityTransaction} object.
   ** <br>
   ** The {@link EntityTransaction} instance may be used serially to begin and
   ** commit multiple transactions.
   **
   ** @return                    the resource-level {@link EntityTransaction}
   **                            instance.
   **                            <br>
   **                            Possible object is {@link EntityTransaction}.
   **
   ** @throws IllegalStateException if invoked on a JTA entity manager
   */
  @Override
  public EntityTransaction getTransaction() {
    return this.store.current().getTransaction();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getEntityManagerFactory (EntityManager)
  /**
   ** Return the {@link EntityManagerFactory} for the entity manager.
   **
   ** @return                    the {@link EntityManagerFactory} instance.
   **                            <br>
   **                            Possible object is
   **                            {@link EntityManagerFactory}.
   **
   ** @throws IllegalStateException if the entity manager has been closed
   */
  @Override
  public EntityManagerFactory getEntityManagerFactory() {
    return this.store.current().getEntityManagerFactory();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getCriteriaBuilder (EntityManager)
  /**
   ** Return an instance of {@link CriteriaBuilder} for the creation of
   ** <code>CriteriaQuery</code> objects.
   **
   ** @return                    an instance of {@link CriteriaBuilder}
   **                            instance.
   **                            <br>
   **                            Possible object is {@link CriteriaBuilder}.
   **
   ** @throws IllegalStateException if the entity manager has been closed.
   */
  @Override
  public CriteriaBuilder getCriteriaBuilder() {
    return this.store.current().getCriteriaBuilder();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getMetamodel (EntityManager)
  /**
   ** Return an instance of {@link Metamodel} interface for access to the
   ** metamodel of the persistence unit.
   **
   ** @return                    the {@link Metamodel} instance.
   **                            <br>
   **                            Possible object is {@link Metamodel}.
   **
   ** @throws IllegalStateException if the entity manager has been closed.
   */
  @Override
  public Metamodel getMetamodel() {
    return this.store.current().getMetamodel();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createEntityGraph (EntityManager)
  /**
   ** Return a mutable {@link EntityGraph} that can be used to dynamically
   ** create an {@link EntityGraph}.
   **
   ** @param  root               the class type of entity graph
   **                            <br>
   **                            Allowed object is {@link Class} for type
   **                            <code>T</code>.
   **
   ** @return                    the {@link EntityGraph}.
   **                            <br>
   **                            Possible object is {@link EntityGraph}.
   */
  @Override
  public <T> EntityGraph<T> createEntityGraph(final Class<T> root) {
    return this.store.current().createEntityGraph(root);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createEntityGraph (EntityManager)
  /**
   ** Return a mutable copy of the named {@link EntityGraph}.
   ** <br>
   ** If there is no entity graph with the specified name, <code>null</code> is
   ** returned.
   **
   ** @param  name               the name of an {@link EntityGraph}.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the named {@link EntityGraph}.
   **                            <br>
   **                            Possible object is {@link EntityGraph}.
   */
  @Override
  public EntityGraph<?> createEntityGraph(final String name) {
    return this.store.current().createEntityGraph(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createEntityGraph (EntityManager)
  /**
   ** Return a named {@link EntityGraph}.
   ** <br>
   ** The returned {@link EntityGraph} should be considered immutable.
   **
   ** @param  name               the name of an existing {@link EntityGraph}.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the named entity {@link EntityGraph}.
   **                            <br>
   **                            Possible object is {@link EntityGraph}.
   **
   ** @throws IllegalArgumentException if there is no EntityGraph of the given
   **                                  <code>name</code>.
   */
  @Override
  public EntityGraph<?> getEntityGraph(final String name) {
    return this.store.current().getEntityGraph(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getEntityGraphs (EntityManager)
  /**
   ** Return all named {@link EntityGraph}s that have been defined for the
   ** provided class type.
   **
   ** @param  clazz              the entity class.
   **                            <br>
   **                            Allowed object is {@link Class} for type
   **                            <code>T</code>.
   **
   ** @return                    the collection of all entity graphs defined for
   **                            the entity.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link EntityGraph} for
   **                            type <code>T</code>.
   **
   ** @throws IllegalArgumentException if the class is not an entity.
   */
  @Override
  public <T> List<EntityGraph<? super T>> getEntityGraphs(final Class<T> clazz)
    throws IllegalArgumentException {

    return this.store.current().getEntityGraphs(clazz);
  }
}