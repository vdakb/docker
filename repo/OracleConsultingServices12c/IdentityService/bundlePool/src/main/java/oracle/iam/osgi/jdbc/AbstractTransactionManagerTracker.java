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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Generic Persistence Interface

    File        :   AbstractTransactionManagerTracker.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractTransactionManagerTracker

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.osgi.jdbc;

import java.util.logging.Logger;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.framework.InvalidSyntaxException;

import org.osgi.util.tracker.ServiceTracker;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractTransactionManagerTracker
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Watches for DataSourceFactory services and creates/destroys a
 ** <code>TransactionManager</code> for each existing
 ** {@link PooledDataSourceFactory}.
 ** <p>
 ** The {@link ServiceTracker} class simplifies using services from the
 ** Framework's service registry.
 ** <p>
 ** A {@link ServiceTracker} object is constructed with search criteria and a
 ** ServiceTrackerCustomizer object.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class AbstractTransactionManagerTracker<T> extends ServiceTracker<T, ServiceRegistration<PooledDataSourceFactory>> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final Logger LOG = null;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private ServiceReference<T> selectedService;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Contructs a {@link ServiceTracker} on the specified class name.
   ** <p>
   ** Services registered under the specified class name will be tracked by this
   ** {@link ServiceTracker}. 
   **
   ** @param  context            the {@link BundleContext} against which the
   **                            tracking is done.
   **                            <br>
   **                            Allowed object is {@link BundleContext}.
   ** @param  clazz              the class name of the services to be tracked.
   **                            <br>
   **                            Allowed object is {@link Class}.
   **
   ** @throws InvalidSyntaxException if build filter string contains an invalid
   **                                expression that cannot be parsed. 
   */
  public AbstractTransactionManagerTracker(final BundleContext context, final Class<T> clazz)
    throws InvalidSyntaxException {

    // ensure inheritance
    this(context, clazz, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Contructs a {@link ServiceTracker} on the specified class name.
   ** <p>
   ** Services registered under the specified class name will be tracked by this
   ** {@link ServiceTracker}. 
   **
   ** @param  context            the {@link BundleContext} against which the
   **                            tracking is done.
   **                            <br>
   **                            Allowed object is {@link BundleContext}.
   ** @param  clazz              the class name of the services to be tracked.
   **                            <br>
   **                            Allowed object is {@link Class}.
   ** @param  filter             used to match a {@link ServiceReference} object
   **                            or a Dictionary object.
   **                            <br>
   **                            If <code>null</code> the name of the specified
   **                            {@link Class} <code>clazz</code> is used as the
   **                            default filter criteria; otherwise the
   **                            <code>filter</code> is ANDed with the class name
   **                            filter build implicietly.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws InvalidSyntaxException if passed filter string contains an invalid
   **                                expression that cannot be parsed. 
   */
  public AbstractTransactionManagerTracker(final BundleContext context, final Class<T> clazz, final String filter)
    throws InvalidSyntaxException {

    // ensure inheritance
    super(context, context.createFilter(filter == null ? "(objectClass=" + clazz.getName() + ")" : "(&(objectClass=" + clazz.getName() + ")" + filter + ")"), null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addingService (overridden)
  /**
   ** This method is only called when this {@link ServiceTracker} has been
   ** constructed with a null <code>ServiceTrackerCustomizer</code> argument.
   ** <p>
   ** This implementation returns the result of calling {@link #getService} on
   ** the {@link BundleContext} with which this {@link ServiceTracker} was
   ** created passing the specified {@link ServiceReference}.
   ** <p>
   ** This method is overridden to customize the service object to be tracked
   ** for the service being added.
   **
   ** @param  reference          the reference to the service being added to
   **                            this {@link ServiceTracker}.
   **                            <br>
   **                            Allowed object is {@link ServiceReference} for
   **                            type <code>T</code>.
   **
   ** @return                    the service object to be tracked for the
   **                            service added to this {@link ServiceTracker}.
   **                            <br>
   **                            Possible object is {@link ServiceRegistration}
   **                            for type {@link PooledDataSourceFactory}.
   */
  @Override
  public ServiceRegistration<PooledDataSourceFactory> addingService(final ServiceReference<T> reference) {
    synchronized (this) {
      if (this.selectedService != null) {
//        LOG.warn("There is more than one TransactionManager service. Ignoring this one");
        return null;
      }
      this.selectedService = reference;
    }
    LOG.info("TransactionManager service detected. Providing support for XA DataSourceFactories");
    T tm = this.context.getService(reference);
    return createService(this.context, tm);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifiedService (overridden)
  /**
   ** This method is only called when this {@link ServiceTracker} has been
   ** constructed with a null <code>ServiceTrackerCustomizer</code> argument.
   ** <p>
   ** This implementation does nothing. 
   **
   ** @param  reference          the reference to the modified service.
   **                            <br>
   **                            Allowed object is {@link ServiceReference} for
   **                            type <code>T</code>.
   ** @param  service            the service object for the modified service.
   **                            <br>
   **                            Allowed object is {@link ServiceRegistration}
   **                            for type {@link PooledDataSourceFactory}.
   */
  @Override
  public void modifiedService(final ServiceReference<T> reference, final ServiceRegistration<PooledDataSourceFactory> service) {
    // intentionally left blank
    LOG.info("TransactionManager service modified");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removedService (overridden)
  /**
   ** This method is only called when this {@link ServiceTracker} has been
   ** constructed with a null <code>ServiceTrackerCustomizer</code> argument.
   ** <p>
   ** This implementation calls ungetService, on the {@link BundleContext} with
   ** which this {@link ServiceTracker} was created, passing the specified
   ** {@link ServiceReference}.
   ** <p>
   ** This method is overridden  to unget the service. 
   **
   ** @param  reference          the reference to removed service.
   **                            <br>
   **                            Allowed object is {@link ServiceReference} for
   **                            type <code>T</code>.
   ** @param  service            the service object for the removed service.
   **                            <br>
   **                            Allowed object is {@link ServiceRegistration}
   **                            for type {@link PooledDataSourceFactory}.
   */
  @Override
  public void removedService(final ServiceReference<T> reference, final ServiceRegistration<PooledDataSourceFactory> service) {
    synchronized (this) {
      if (this.selectedService == null || !this.selectedService.equals(reference)) {
        return;
      }
      this.selectedService = null;
    }

    LOG.info("TransactionManager service lost. Shutting down support for XA DataSourceFactories");
    service.unregister();
    this.context.ungetService(reference);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createService
  /**
   ** Factory method to create the {@link ServiceReference} to be tracked
   ** for the service being added.
   **
   ** @param  context            the {@link BundleContext} against which the
   **                            tracking is done.
   **                            <br>
   **                            Allowed object is {@link BundleContext}.
   ** @param  service            the reference to the service being added to
   **                            this {@link ServiceTracker}.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    the {@link ServiceRegistration} to be tracked
   **                            for the service added to this
   **                            {@link ServiceTracker}.
   **                            <br>
   **                            Possible object is {@link ServiceRegistration}
   **                            for type {@link PooledDataSourceFactory}.
   */
  protected abstract ServiceRegistration<PooledDataSourceFactory> createService(final BundleContext context, final T service);
}