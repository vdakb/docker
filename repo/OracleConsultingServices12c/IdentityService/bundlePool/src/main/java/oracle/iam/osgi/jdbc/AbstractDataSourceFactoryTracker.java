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

    File        :   AbstractDataSourceFactoryTracker.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractDataSourceFactoryTracker

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.osgi.jdbc;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.logging.Logger;

import javax.transaction.TransactionManager;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import org.osgi.util.tracker.ServiceTracker;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractDataSourceFactoryTracker
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Watches for DataSourceFactory services and creates/destroys a
 ** PooledDataSourceFactory for each existing DataSourceFactory.
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
public abstract class AbstractDataSourceFactoryTracker extends ServiceTracker<DataSourceFactory, ServiceRegistration<DataSourceFactory>> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final Logger LOG = null;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final TransactionManager tm;

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
   */
  public AbstractDataSourceFactoryTracker(final BundleContext context) {
    // ensure inheritance
    this(context, null);
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
   ** @param  tm                 the {@link TransactionManager} of the services
   **                            to be tracked.
   **                            <br>
   **                            Allowed object is {@link TransactionManager}.
   */
  public AbstractDataSourceFactoryTracker(final BundleContext context, final TransactionManager tm) {
    // ensure inheritance
    super(context, DataSourceFactory.class, null);

    // initialize instance attributes
    this.tm = tm;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  protected TransactionManager transactionManager() {
    return this.tm;
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
   **                            type {@link DataSourceFactory}.
   **
   ** @return                    the service object to be tracked for the
   **                            service added to this {@link ServiceTracker}.
   **                            <br>
   **                            Possible object is {@link ServiceRegistration}
   **                            for type {@link DataSourceFactory}.
   */
  @Override
  public ServiceRegistration<DataSourceFactory> addingService(final ServiceReference<DataSourceFactory> reference) {
    if (reference.getProperty("pooled") != null) {
      // make sure we do not react on our own service for the pooled factory
      return null;
    }
    return register(reference);
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
   **                            type {@link DataSourceFactory}.
   ** @param  service            the service object for the modified service.
   **                            <br>
   **                            Allowed object is {@link ServiceRegistration}
   **                            for type {@link DataSourceFactory}.
   */
  @Override
  public void modifiedService(final ServiceReference<DataSourceFactory> reference, final ServiceRegistration<DataSourceFactory> service) {
    // intentionally left blank
    LOG.info("DataSourceFactory service modified");
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
   **                            type {@link DataSourceFactory}.
   ** @param  service            the service object for the removed service.
   **                            <br>
   **                            Allowed object is {@link ServiceRegistration}
   **                            for type {@link DataSourceFactory}.
   */
  @Override
  public void removedService(final ServiceReference<DataSourceFactory> reference, final ServiceRegistration<DataSourceFactory> service) {
//    LOG.debug("Unregistering PooledDataSourceFactory");
    service.unregister();
    super.removedService(reference, service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   register
  /**
   ** Factory method to create the {@link ServiceReference} to be tracked
   ** for the service being added.
   **
   ** @param  reference          the reference to the service being added to
   **                            this {@link ServiceTracker}.
   **                            <br>
   **                            Allowed object is {@link ServiceReference} for
   **                            type {@link DataSourceFactory}.
   **
   ** @return                    the {@link ServiceRegistration} to be tracked
   **                            for the service added to this
   **                            {@link ServiceTracker}.
   **                            <br>
   **                            Possible object is {@link ServiceRegistration}
   **                            for type {@link DataSourceFactory}.
   */
  private ServiceRegistration<DataSourceFactory> register(final ServiceReference<DataSourceFactory> reference) {
//    LOG.debug("Registering PooledDataSourceFactory");
    DataSourceFactory          dsf = createPooledDatasourceFactory(this.context.getService(reference));
    Dictionary<String, Object> prp = createProperty(reference);
//    LOG.debug("Registering PooledDataSourceFactory: " + props);
    return this.context.registerService(DataSourceFactory.class, dsf, prp);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createPooledDatasourceFactory
  /**
   ** Factory method to create the {@link DataSourceFactory} to be tracked
   ** for the service being added.
   **
   ** @param  factory            the reference to the {@link DataSourceFactory}
   **                            being added to this {@link ServiceTracker}.
   **                            <br>
   **                            Allowed object is {@link DataSourceFactory}.
   **
   ** @return                    the {@link DataSourceFactory} to be tracked
   **                            for the service added to this
   **                            {@link ServiceTracker}.
   **                            <br>
   **                            Possible object is {@link DataSourceFactory}.
   */
  protected abstract DataSourceFactory createPooledDatasourceFactory(final DataSourceFactory factory);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createPropsForPoolingDataSourceFactory
  /**
   *
   **
   ** @param  reference          the reference to the service being added to
   **                            this {@link ServiceTracker}.
   **                            <br>
   **                            Allowed object is {@link ServiceReference} for
   **                            type {@link DataSourceFactory}.
   * @return
   */
  private Dictionary<String, Object> createProperty(final ServiceReference<DataSourceFactory> reference) {
    final Dictionary<String, Object> props = new Hashtable<String, Object>();
    for (String key : reference.getPropertyKeys()) {
      if (!"service.id".equals(key)) {
        props.put(key, reference.getProperty(key));
      }
    }
    props.put("pooled", "true");
    if (transactionManager() != null) {
      props.put("xa", "true");
    }
    props.put(DataSourceFactory.OSGI_JDBC_DRIVER_CLASS, driverClass(reference));
    props.put(DataSourceFactory.OSGI_JDBC_DRIVER_NAME,  driverName(reference));
    return props;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   driverName
  /**
   ** Return the JDBC Driver class.
   **
   ** @param  reference          the reference to the service being added to
   **                            this {@link ServiceTracker}.
   **                            <br>
   **                            Allowed object is {@link ServiceReference} for
   **                            type {@link DataSourceFactory}.
   * @return
   */
  private String driverName(ServiceReference<DataSourceFactory> reference) {
    String origin = (String)reference.getProperty(DataSourceFactory.OSGI_JDBC_DRIVER_NAME);
    // fallback to the driver class if the driver name isn't specified
    return (origin == null) ? driverClass(reference) :  origin + "-pool" + ((transactionManager() != null) ? "-xa" : "");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   driverClass
  /**
   ** Return the JDBC Driver class.
   **
   ** @param  reference          the reference to the service being added to
   **                            this {@link ServiceTracker}.
   **                            <br>
   **                            Allowed object is {@link ServiceReference} for
   **                            type {@link DataSourceFactory}.
   * @return
   */
  private String driverClass(final ServiceReference<DataSourceFactory> reference) {
    String origin = (String)reference.getProperty(DataSourceFactory.OSGI_JDBC_DRIVER_CLASS);
    return origin + "-pool" + ((transactionManager() != null) ? "-xa" : "");
  }
}