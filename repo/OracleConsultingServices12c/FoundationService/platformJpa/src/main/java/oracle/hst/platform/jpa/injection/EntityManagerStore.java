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

    File        :   EntityManagerStore.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    EntityManagerStore.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2014-02-23  DSteding    First release version
*/

package oracle.hst.platform.jpa.injection;

import java.util.Stack;

import java.io.Serializable;

import javax.annotation.PreDestroy;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import oracle.hst.platform.core.logging.Logger;

import oracle.hst.platform.jpa.PersistenceException;

////////////////////////////////////////////////////////////////////////////////
// class EntityManagerStore
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** A store for entity managers.
 ** <br>
 ** It is basically a ThreadLocal which stores the entity manager.
 ** <br>
 ** The {@link TransactionInterceptor} is expected to register entity manager.
 ** The application code can get the current entity manager either by injecting
 ** the store or the {@link EntityManagerDelegate}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class EntityManagerStore implements Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String                     category = EntityManagerStore.class.getName();
  private static final Logger                     logger   = Logger.create(category);

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-6085522508722535856")
  private static final long                       serialVersionUID = -452020800883839374L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @SuppressWarnings("oracle.jdeveloper.java.field-not-serializable")
  private final ThreadLocal<Stack<EntityManager>> stack    = new ThreadLocal<Stack<EntityManager>>();

  @SuppressWarnings("oracle.jdeveloper.java.field-not-serializable")
  private EntityManagerFactory                    emf;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>EntityManagerStore</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public EntityManagerStore() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>EntityManagerStore</code> warpping the specified
   ** {@link EntityManagerFactory}.
   **
   ** @param  factory            the {@link EntityManagerFactory} to use and
   **                            configured in the startup class of the
   **                            application.
   **                            <br>
   **                            Allowed object is {@link EntityManagerFactory}.
   */
  public EntityManagerStore(final EntityManagerFactory factory) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.emf = factory;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   factory
  /**
   ** Return the {@link EntityManagerFactory} to use and configured in the
   ** startup class of the application.
   **
   ** @return                    the {@link EntityManagerFactory} to use and
   **                            configured in the startup class of the
   **                            application.
   **                            <br>
   **                            Possible object is
   **                            {@link EntityManagerFactory}.
   */
  public EntityManagerFactory factory() {
    return this.emf;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize
  /**
   ** Callback notification to signal dependency injection is done to perform
   ** any initialization and the instance becomes to put in service.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** Usually this method would be final but its used in CDI hence it needs to
   ** be proxyable.
   **
   ** @param  factory            the {@link EntityManagerFactory} to use and
   **                            configured in the startup class of the
   **                            application.
   **                            <br>
   **                            Allowed object is {@link EntityManagerFactory}.
   */
  public void initialize(final EntityManagerFactory factory) {
    this.emf = factory;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   shutdown
  /**
   ** Callback notification to signal that the instance is in the process of
   ** being removed by the container.
   */
  @PreDestroy
  public void shutdown() {
    this.emf.close();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clearAll
  /**
   ** Clear the cache.
   */
  public void clearAll() {
    if (this.emf != null)
      this.emf.getCache().evictAll();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   aquire
  /**
   ** Factory method to creates an entity manager and stores it in a stack.
   ** <br>
   ** The use of a stack allows to implement transaction with a
   ** <i>requires new</i> behaviour.
   **
   ** @return                    the created entity manager.
   **                            <br>
   **                            Possible object is {@link EntityManager}.
   **
   ** @throws PersistenceException if the connection could not aquired or the
   **                              entity manager factory has been closed.
   */
  public EntityManager aquire()
    throws PersistenceException {

    final String method = "aquire";
    logger.entering(category, method);
    logger.debug("Creating and registering an entity manager");
    Stack<EntityManager> local = this.stack.get();
    if (local == null) {
      local = new Stack<EntityManager>();
      this.stack.set(local);
    }
    EntityManager em = null;
    try {
      em = this.emf.createEntityManager();
      local.push(em);
      return em;
    }
    catch (Throwable t) {
      throw PersistenceException.internal(t);    
    }
    finally {
      logger.exiting(category, method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   release
  /**
   ** Removes an entity manager from the thread local stack.
   ** <br>
   ** It needs to be created using the {@link #aquire()} method.
   **
   ** @param  entityManager      the entity manager to remove.
   **                            <br>
   **                            Allowed object is {@link EntityManager}.
   **
   ** @throws PersistenceException in case the entity manager was not found on
   **                              the stack
   */
   public void release(final EntityManager entityManager)
    throws PersistenceException {

    // prevent bogus input
    if (entityManager == null)
      return;
    
    final String method = "release";
    logger.entering(category, method);
    logger.debug("Unregistering an entity manager");
    final Stack<EntityManager> local = this.stack.get();
    if (local == null || local.isEmpty()) {
      logger.exiting(category, method);
      throw PersistenceException.abort("Removing of entity manager failed. Your entity manager was not found.");
    }

    if (local.peek() != entityManager) {
      logger.exiting(category, method);
      throw PersistenceException.abort("Removing of entity manager failed. Your entity manager was not found.");
    }

    local.pop();
    entityManager.close();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   current
  /**
   ** Lookup for the current entity manager and returns it.
   ** <br>
   ** <b>Note</b>:
   ** <br>
   ** If no entity manager was found, this method logs a warn message and
   ** returns <code>null</code>. This will cause a <i>NullPointerException</i>
   ** in most cases and will cause a stack trace starting from the service
   ** method.
   **
   ** @return                    the currently used entity manager or
   **                            <code>null</code> if none was found.
   **                            <br>
   **                            Possible object is {@link EntityManager}.
   */
  public EntityManager current()
    throws PersistenceException {

    final String method = "current";
    logger.entering(category, method);
    final Stack<EntityManager> local = this.stack.get();
    if (local == null || local.isEmpty()) {
      logger.exiting(category, method);
      // if nothing is found, we return null to cause a NPE in the business
      // layer
      // this leeds to a nicer stack trace starting with client code
      throw PersistenceException.abort("No entity manager was found. Did you forget to mark your method as transactional?");
    }
    else {
      logger.exiting(category, method);
      return local.peek();
    }
  }
}