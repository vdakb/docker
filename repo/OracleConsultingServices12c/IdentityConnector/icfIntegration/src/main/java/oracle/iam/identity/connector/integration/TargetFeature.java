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
    Subsystem   :   Connector Bundle Integration

    File        :   TargetFeature.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    TargetFeature.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.connector.integration;

import java.util.Map;

import org.identityconnectors.framework.api.ConnectorKey;

import oracle.hst.foundation.logging.Loggable;

import oracle.iam.identity.foundation.AbstractLookup;
import oracle.iam.identity.foundation.TaskDescriptor;

////////////////////////////////////////////////////////////////////////////////
// abstract class TargetFeature
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~
/**
 ** The <code>TargetResource</code> implements the immutable data value object
 ** for generic IT Resource information (Backed up as Map).
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class TargetFeature extends TaskDescriptor {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The default minimum number of idle objects. */
  public static final int     POOL_MIN_IDLE_DEFAULT  = 1;

  /**
   ** The default maximum number of objects that can sit idle in the pool at any
   ** time.
   */
  public static final int     POOL_MAX_IDLE_DEFAULT = 10;

  /**
   ** The default minimum amount of time to wait before evicting an idle object.
   */
  public static final long    POOL_MIN_WAIT_DEFAULT = 120000L;

  /** The default maximum number of objects (idle+active). */
  public static final int     POOL_MAX_SIZE_DEFAULT = 10;

  /**
   ** The default maximum amount of time to wait if the pool is waiting for a
   ** free object to become available before failing.
   */
  public static final long    POOL_MAX_WAIT_DEFAULT = 150000L;

  /**
   ** Attribute tag which must be defined on a feature configuration to hold
   ** the name of the connector bundle.
   */
  public static final String  BUNDLE_NAME           = "bundle-name";

  /**
   ** Attribute tag which must be defined on a feature configuration to hold
   ** the version of the connector bundle.
   */
  public static final String  BUNDLE_VERSION        = "bundle-version";

  /**
   ** Attribute tag which must be defined on a feature configuration to hold
   ** the full qualified class name of the main entry point to the connector
   ** bundle.
   */
  public static final String  BUNDLE_ENTRY          = "bundle-entry";

  /** The minimum number of idle objects. */
  public static final String POOL_MIN_IDLE          = "pool-min-idle";

  /** The minimum amount of time to wait before evicting an idle object. */
  public static final String POOL_MIN_WAIT          = "pool-min-wait";

  /**
   ** The maximum number of objects that can sit idle in the pool at any time.
   */
  public static final String POOL_MAX_IDLE          = "pool-max-idle";

  /** The maximum number of objects (idle+active). */
  public static final String POOL_MAX_SIZE          = "pool-max-size";

  /**
   ** The maximum amount of time to wait if the pool is waiting for a free
   ** object to become available before failing.
   */
  public static final String POOL_MAX_WAIT          = "pool-max-wait";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////
  
  protected ConnectorKey     token                  = null;
  protected AbstractLookup   pool                   = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Metadata Descriptor</code> which is associated
   ** with the specified logging provider <code>loggable</code>.
   ** <p>
   ** The instance created through this constructor has to be populated manually
   ** and does not belongs to an Identity Manager Object.
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>Metadata Descriptor</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   */
  protected TargetFeature(final Loggable loggable) {
    // ensure inheritance
    super(loggable);

    // initialize instance
    this.pool = new AbstractLookup(loggable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessors methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   poolMinIdle
  /**
   ** Returns the minimum number of idle objects.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #POOL_MIN_IDLE}.
   ** <p>
   ** If {@link #POOL_MIN_IDLE} is not mapped in the underlying
   ** {@link AbstractLookup} this method returns {@link #POOL_MIN_IDLE_DEFAULT}.
   **
   ** @return                    the minimum number of idle objects.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public int poolMinIdle() {
    return this.pool.integerValue(POOL_MIN_IDLE, POOL_MIN_IDLE_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   poolMinWait
  /**
   ** Returns the minimum amount of time to wait before evicting an idle object.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #POOL_MIN_WAIT}.
   ** <p>
   ** If {@link #POOL_MIN_WAIT} is not mapped in the underlying
   ** {@link AbstractLookup} this method returns {@link #POOL_MIN_WAIT_DEFAULT}.
   **
   ** @return                    the minimum amount of time to wait before
   **                            evicting an idle object.
   **                            <br>
   **                            Possible object is <code>long</code>.
   */
  public long poolMinWait() {
    return this.pool.longValue(POOL_MIN_WAIT, POOL_MIN_WAIT_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   poolMaxSize
  /**
   ** Returns the maximum number of objects (idle+active).
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #POOL_MAX_SIZE}.
   ** <p>
   ** If {@link #POOL_MAX_SIZE} is not mapped in the underlying
   ** {@link AbstractLookup} this method returns {@link #POOL_MAX_SIZE_DEFAULT}.
   **
   ** @return                    the maximum number of objects (idle + active).
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public int poolMaxSize() {
    return this.pool.integerValue(POOL_MAX_SIZE, POOL_MAX_SIZE_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   poolMaxWait
  /**
   ** Returns the maximum amount of time to wait if the pool is waiting for a
   ** free object to become available before failing.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #POOL_MAX_WAIT}.
   ** <p>
   ** If {@link #POOL_MAX_WAIT} is not mapped in the underlying
   ** {@link AbstractLookup} this method returns {@link #POOL_MAX_WAIT_DEFAULT}.
   **
   ** @return                    the maximum amount of time to wait if the pool
   **                            is waiting for a free object to become
   **                            available before failing.
   **                            <br>
   **                            Possible object is <code>long</code>.
   */
  public long poolMaxWait() {
    return this.pool.longValue(POOL_MAX_WAIT, POOL_MAX_WAIT_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   poolMaxIdle
  /**
   ** Returns the maximun number of idle objects.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #POOL_MAX_IDLE}.
   ** <p>
   ** If {@link #POOL_MAX_IDLE} is not mapped in the underlying
   ** {@link AbstractLookup} this method returns {@link #POOL_MAX_IDLE_DEFAULT}.
   **
   ** @return                    the maximun number of idle objects.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public int poolMaxIdle() {
    return this.pool.integerValue(POOL_MAX_IDLE, POOL_MAX_IDLE_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   registry
  /**
   ** Returns the mapping of attribute names defined on this
   ** <code>IT Resource</code> and the parameter names expected by connector
   ** bundle configuration.
   **
   ** @return                    the mapping of attribute names defined
   **                            on this <code>IT Resource</code> and the
   **                            parameter names expected by connector bundle
   **                            configuration.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link String} as the value.
   */
  public abstract Map<String, String> registry();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   token
  /**
   ** Returns the {@link ConnectorKey} populated from the configured values.
   **
   ** @return                    the {@link ConnectorKey} populated from the
   **                            configured values.
   **                            <br>
   **                            Possible object is {@link ConnectorKey}.
   */
  public final ConnectorKey token() {
    return this.token;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   token
  /**
   ** Factory method to create an {@link ConnectorKey} populated from the
   ** provided values.
   **
   ** @param  name               the name of the connector bundle.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  entry              the full qualified class name of the main entry
   **                            point to the connector bundle.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  version            the version of the connector bundle.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an {@link ConnectorKey} instance with the
   **                            values provided.
   **                            <br>
   **                            Possible object is {@link ConnectorKey}.
   */
  public static ConnectorKey token(final String name, final String entry, final String version) {
    return new ConnectorKey(name, version, entry);
  }
}