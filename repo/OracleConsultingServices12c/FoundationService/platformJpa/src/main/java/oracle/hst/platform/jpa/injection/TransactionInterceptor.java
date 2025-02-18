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

    File        :   TransactionInterceptor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    TransactionInterceptor.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2014-02-23  DSteding    First release version
*/

package oracle.hst.platform.jpa.injection;

import java.io.Serializable;

import javax.annotation.Resource;

import javax.inject.Inject;

import javax.ejb.SessionContext;

import javax.interceptor.Interceptor;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import oracle.hst.platform.core.logging.Logger;

import oracle.hst.platform.jpa.PersistenceException;

import oracle.hst.platform.jpa.context.SessionAdapter;
import oracle.hst.platform.jpa.context.SessionProvider;

////////////////////////////////////////////////////////////////////////////////
// class TransactionInterceptor
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** A simple transaction interceptor which registers an entity mangager in a
 ** <code>ThreadLocal</code> and unregisters after the method was called.
 ** <br>
 ** It does not support any kind of context propagation. If a transactional
 ** method calls another's bean transactional method a new entity manager is
 ** created and added to the stack.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Interceptor
@Transactional
public class TransactionInterceptor implements Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-4183713360117576195")
  private static final long  serialVersionUID = 899220496211336369L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @Inject
  @SuppressWarnings({"oracle.jdeveloper.cdi.uncofig-project", "oracle.jdeveloper.java.annotation-callback"})
  private EntityManagerStore store;

  @Resource
  @SuppressWarnings("oracle.jdeveloper.java.field-not-serializable")
  private SessionContext     sessionContext;

  @SuppressWarnings("oracle.jdeveloper.java.field-not-serializable")
  private final Logger       logger = Logger.create(TransactionInterceptor.class.getName());

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TransactionInterceptor</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TransactionInterceptor() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Creates a transaction before the intercepted method gets called and
   ** commits or reverts it after the invocation.
   **
   ** @param  context            the current invocation-context.
   **                            <br>
   **                            Allowed objects is {@link InvocationContext}.
   **
   ** @return                    the result of the intercepted method.
   **                            <br>
   **                            Possible objects is {@link Object}.
   **
   ** @throws PersistenceException an exception which might be thrown by the
   **                              intercepted method.
   */
  @AroundInvoke
  public Object execute(final InvocationContext context)
    throws PersistenceException {

    // propagate the identity of the current user
    SessionProvider.put(SessionAdapter.CLIENT, this.sessionContext.getCallerPrincipal().getName());

    EntityManager manager = null;
    try {
      manager = this.store.aquire();
    }
    catch (javax.persistence.PersistenceException e) {
      throw PersistenceException.internal(e);
    }
    
    Object              result;
    final Transactional control = context.getMethod().getAnnotation(Transactional.class);
    if (control.readOnly()) {
      try {
        result = context.proceed();
      }
      // rethrow if it match
      catch (PersistenceException e) {
        throw e;
      }
      catch (Exception e) {
        throw PersistenceException.abort(e);
      }
      finally {
        this.store.release(manager);
      }
    }
    else {
      final EntityTransaction transaction = manager.getTransaction();
      transaction.begin();
      try {
        result = context.proceed();
        if (transaction.isActive()) {
          transaction.commit();
        }
      }
      catch (Exception e) {
        try {
          if (transaction.isActive()) {
            transaction.rollback();
            this.logger.debug("Rolled back transaction");
          }
        }
        catch (Exception x) {
          this.logger.warn("Rollback of transaction failed -> " + x);
        }
        throw PersistenceException.abort(e);
      }
      finally {
        this.store.release(manager);
      }
    }
    return result;
  }
}