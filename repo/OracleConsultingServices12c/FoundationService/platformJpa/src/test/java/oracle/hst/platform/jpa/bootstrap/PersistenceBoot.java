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
    Subsystem   :   JPA Unit Testing

    File        :   PersistenceBoot.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    PersistenceBoot.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2014-02-23  DSteding    First release version
*/

package oracle.hst.platform.jpa.bootstrap;

import java.util.Map;
import java.util.Collections;

import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.EntityManagerFactory;

import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceUnitInfo;

import oracle.hst.platform.core.reflect.Discovery;

import oracle.hst.platform.jpa.PersistenceConsumer;
import oracle.hst.platform.jpa.PersistenceFunction;

import oracle.hst.platform.jpa.injection.EntityManagerStore;

////////////////////////////////////////////////////////////////////////////////
// class PersistenceBoot
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** To make it easier to reuse the bootstrapping logic, a
 ** <code>PersistenceBoot</code> base class which will be extended by unit tests
 ** that want to bootstrap without an external <code>persistence.xml</code>
 ** configuration file.
 ** <p>
 ** The <code>PersistenceBoot</code> creates an {@link EntityManagerFactory}
 ** upon starting a new test and closes it after executing the unit test. This
 ** way, all tests run in isolation and each test class can be self-contained as
 ** well.
 ** <p>
 ** Implemented by an extra class to keep it outside of the test case classes
 ** itself.
 ** <p>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class PersistenceBoot {

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  private static PersistenceBoot              instance;
  
  //one entity manager factory per unit
  private static Map<String, PersistenceBoot> provider = new ConcurrentHashMap<>();

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private EntityManager                em;
  private EntityManagerStore           es;
  private EntityTransaction            tx;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>PersistenceBoot</code> instance that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private PersistenceBoot() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   store
  /**
   ** Return the {@link EntityManagerStore} suitable for a test case.
   **
   ** @return                    the {@link EntityManagerStore} suitable for a
   **                            test case.
   **                            <br>
   **                            Possible object is {@link EntityManagerStore}.
   */
  public static EntityManagerStore store() {
    return instance.es;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   store
  /**
   ** Return the {@link EntityManagerStore} suitable for a test case represented
   ** by given persistence unit name.
   **
   ** @param  unit               the persistence unit configuration to lookup
   **                            the {@link EntityManagerStore} for.
   **                            <br>
   **                            Allowed object is {@link PersistenceUnitInfo}.
   **
   ** @return                    the {@link EntityManagerFactory} suitable for a
   **                            test case represented by given persistence unit
   **                            name.
   **                            <br>
   **                            Possible object is
   **                            {@link EntityManagerFactory}.
   */
  public static EntityManagerStore store(final PersistenceUnitInfo unit) {
    return instance(unit).es;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   manager
  /**
   ** Return the {@link EntityManager} suitable for a test case.
   **
   ** @return                    the {@link EntityManager} suitable for a test
   **                            case.
   **                            <br>
   **                            Possible object is {@link EntityManager}.
   */
  public static EntityManager manager() {
    return instance.em;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   manager
  /**
   ** Return the {@link EntityManager} suitable for a test case represented by
   ** given persistence unit name.
   **
   ** @param  unit               the persistence unit configuration to lookup
   **                            the {@link EntityManager} for.
   **                            <br>
   **                            Allowed object is {@link PersistenceUnitInfo}.
   **
   ** @return                    the {@link EntityManager} suitable for a test
   **                            case represented by given persistence unit
   **                            name.
   **                            <br>
   **                            Possible object is {@link EntityManager}.
   */
  public static EntityManager manager(final PersistenceUnitInfo unit) {
    return instance(unit).em;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transaction
  /**
   ** Return the {@link EntityTransaction} suitable for a test case.
   **
   ** @return                    the {@link EntityTransaction} suitable for a
   **                            test case.
   **                            <br>
   **                            Possible object is {@link EntityTransaction}.
   */
  public static EntityTransaction transaction() {
    check();
    if (instance.tx == null) {
      instance.tx = instance.em.getTransaction();
    }
    return instance.tx;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transaction
  /**
   ** Return the {@link EntityTransaction} suitable for a test case represented
   ** by given persistence unit name.
   **
   ** @param  unit               the persistence unit configuration to lookup
   **                            the {@link EntityTransaction} for.
   **                            <br>
   **                            Allowed object is {@link PersistenceUnitInfo}.
   **
   ** @return                    the {@link EntityTransaction} suitable for a
   **                            test case represented by given persistence unit
   **                            name.
   **                            <br>
   **                            Possible object is {@link EntityTransaction}.
   */
  public static EntityTransaction transaction(final PersistenceUnitInfo unit) {
    return manager(unit).getTransaction();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Factory method to create a new <code>PersistenceBoot</code> without
   ** further configuration properties represented by given persistence unit
   ** configuration.
   **
   ** @param  unit               the persistence unit configuration.
   **                            <br>
   **                            Allowed object is {@link PersistenceUnitInfo}.
   **
   ** @return                    the <code>PersistenceBoot</code> suitable for a
   **                            test case represented by given persistence
   **                            unit configuration.
   **                            <br>
   **                            Possible object is
   **                            <code>PersistenceBoot</code>.
   */
  public static synchronized PersistenceBoot instance(final PersistenceUnitInfo unit) {
    return instance(unit, Collections.emptyMap());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Factory method to create a new <code>PersistenceBoot</code> allowing to
   ** pass in overriding properties that may be specific to the JPA Vendor
   ** represented by given persistence unit configuration.
   **
   ** @param  unit               the persistence unit configuration.
   **                            <br>
   **                            Allowed object is {@link PersistenceUnitInfo}.
   ** @param  override                                       
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link Object} as the value.
   **
   ** @return                    the <code>PersistenceBoot</code> suitable for a
   **                            test case represented by given persistence unit
   **                            configuration.
   **                            <br>
   **                            Possible object is
   **                            <code>PersistenceBoot</code>.
   */
  public static synchronized PersistenceBoot instance(final PersistenceUnitInfo unit, final Map<String, Object> override) {
    instance = provider.get(unit.getPersistenceUnitName());
    if (instance == null) {
      instance = new PersistenceBoot();
      instance.initialize(unit, override);
      provider.put(unit.getPersistenceUnitName(), instance);      
    }
    return instance;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clear
  /**
   ** Housekeeping {@link EntityManager} persistence context and
   ** {@link EntityManagerFactory} cache of current instance of this provider.
   **
   ** @return                    the current provider.
   **                            <br>
   **                            Possible object is
   **                            <code>PersistenceBoot</code>.
   */
  public static PersistenceBoot clear() {
    if (active()) {
      manager().clear();
      store().clearAll();
      return instance;
    }
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clear
  /**
   ** Housekeeping {@link EntityManager} persistence context and
   ** {@link EntityManagerFactory} cache represented by given persistence unit
   ** name.
   **
   ** @return                    the current provider.
   **                            <br>
   **                            Possible object is
   **                            <code>PersistenceBoot</code>.
   */
  public static PersistenceBoot clear(final PersistenceUnitInfo unit) {
    manager(unit).clear();
    store(unit).clearAll();
    return provider.get(unit.getPersistenceUnitName());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   active
  /**
   ** Verifies if the {@link EntityManager} is in the shape to operate.
   **
   ** @return                    <code>true</code> if the {@link EntityManager}
   **                            is operational.
   **                           <br>
   **                           Possible object is <code>boolean</code>.
   */
  public static boolean active() {
    return instance != null && manager().isOpen();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readonly
  /**
   ** Invocation of the transactional context.
   **
   ** @param  <T>                the java type the passed function accepts.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  function           the callback function invoke before and after
   **                            the transaction toke place.
   **                            <br>
   **                            Allowed object is {@link PersistenceConsumer}.
   */
  public void readonly(final PersistenceConsumer function) {
    try {
      this.em = this.es.aquire();
      function.accept(this.em);
    }
    catch (RuntimeException e) {
      throw e;
    }
    finally {
      if (this.em != null) {
        this.es.release(this.em);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readonly
  /**
   ** Invocation of the transactional context.
   **
   ** @param  <T>                the java type the passed function accepts.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  function           the callback function invoke before and after
   **                            the transaction toke place.
   **                            <br>
   **                            Allowed object is {@link PersistenceFunction}.
   */
  public <T> T readonly(final PersistenceFunction<T> function) {
    T result = null;
    try {
      this.em = this.es.aquire();
      result = function.apply(this.em);
    }
    catch (RuntimeException e) {
      if (this.tx != null && this.tx.isActive())
        this.tx.rollback();
      throw e;
    }
    finally {
      function.afterTransaction();
      if (this.em != null) {
        this.es.release(this.em);
      }
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transactional
  /**
   ** Invocation of the transactional context.
   **
   ** @param  <T>                the java type the passed function accepts.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  function           the callback function invoke before and after
   **                            the transaction toke place.
   **                            <br>
   **                            Allowed object is {@link PersistenceFunction}.
   */
  public <T> T transactional(final PersistenceFunction<T> function) {
    T result = null;
    try {
      this.em = this.es.aquire();
      function.beforeTransaction();
      this.tx = this.em.getTransaction();
      this.tx.begin();
      result = function.apply(this.em);
      this.tx.commit();
    }
    catch (RuntimeException e) {
      if (this.tx != null && this.tx.isActive())
        this.tx.rollback();
      throw e;
    }
    finally {
      function.afterTransaction();
      if (this.em != null) {
        this.es.release(this.em);
      }
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   check
  /**
   ** Verifies if the instance is in the shape to operate.
   */
  private static void check() {
    if (instance == null)
      throw new IllegalStateException("Call instance(PersistenceUnitInfo) before calling manager()");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize
  /**
   ** Initialize the provider instance represented by given persistence unit
   ** name.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** If the instance is already initialized only the entity factory cache is
   ** cleared.
   */
  private void initialize(final PersistenceUnitInfo unit, final Map<String, Object> override) {
    if (this.es == null) {
      final String className = unit.getPersistenceProviderClassName();
      if (className == null)
        throw new IllegalArgumentException("No PersistenceProvider specified in EntityManagerFactory configuration, and given PersistenceUnitInfo does not specify a provider class name either");

      final Class<PersistenceProvider> clazz = Discovery.clazz(className);
      try {
        final PersistenceProvider provider = clazz.newInstance();
        this.es = new EntityManagerStore(provider.createContainerEntityManagerFactory(unit, override));
      }
      catch (InstantiationException | IllegalAccessException e) {
        throw new IllegalArgumentException(e);
      }
    }
    this.es.clearAll();
  }
}