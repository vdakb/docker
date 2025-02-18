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

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Oracle Identity Manager Connector

    File        :   Dialect.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Dialect.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    3.1.0.0      2014-06-21  DSteding    First release version
*/

package oracle.iam.identity.gis.persistence;

import java.util.Map;
import java.util.HashMap;

import oracle.iam.identity.foundation.persistence.DatabaseEntity;
import oracle.iam.identity.foundation.persistence.DatabaseException;

////////////////////////////////////////////////////////////////////////////////
// abstract class Dialect
// ~~~~~~~~ ~~~~~ ~~~~~~~
/**
 ** Declares global visible configuration properties.
 ** <p>
 ** Each dialect contains the same items, but the items have been different
 ** flavors that dialect.
 **
 ** @author  Dieter.Steding@oracle.com
 ** @version 3.1.0.0
 ** @since   3.1.0.0
 */
public abstract class Dialect {

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final Map<String, Class<? extends Dialect>> DIALECT =
    new HashMap<String, Class<? extends Dialect>>() { {
      put("local", oracle.iam.identity.gis.persistence.local.oim.class);
    }
  };

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the schema owning the administrative catalog supported by a data
   ** dictionary
   ** <p>
   ** Will be installed by lazy loading to speed up instantiation.
   */
  protected final String              schema;

  /**
   ** the administrative entities supported by a data dictionary
   ** <p>
   ** Will be installed by lazy loading to speed up instantiation.
   */
  private Map<Entity, DatabaseEntity> entity;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Entity
  // ~~~~~ ~~~~~~
  /**
   ** The entity types handled by the administration.
   */
  public static enum Entity {
    // the entity belonging to membership in roles
    ROLEMEMBERSHIP;

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:2007781912647811667")
    private static final long serialVersionUID = -1L;
  };

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Dialect</code> that referes to entities that belongs to
   ** the specified schema.
   **
   ** @param  schema             the name of the schema that owns the dialect.
   */
  protected Dialect(final String schema) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.schema = schema;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entity
  /**
   ** Fetchs a {@link DatabaseEntity} for the given key from the associated
   ** dictionary.
   **
   ** @param  entity             the key for the desired {@link DatabaseEntity}.
   **
   ** @return                    the {@link DatabaseEntity} for the given key.
   **                            if <code>null</code> or an empty string was
   **                            passed as <code>resourceKey</code>
   **                            <code>null</code> will be returned.
   **
   ** @throws ClassCastException if the object found for the given key is
   **                            not a {@link DatabaseEntity}.
   */
  @SuppressWarnings("oracle.jdeveloper.java.null-map-return")
  protected DatabaseEntity entity(final Entity entity) {

    // First, bounce bogus input
    if (entity == null)
      return null;

    // Second, bounce bogus state
    if (this.entity == null)
      this.entity= installEntity();

    // if no mapping is available return a null back to the caller to have an
    // fail safe behaviour; otherwise we can lookup the desired value in the
    // mapping.
    return this.entity.get(entity);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   installEntity
  /**
   ** Creates the administrative entities supported by a data dictionary.
   **
   ** @return                    the administrative entities supported by
   **                            the data dictionary.
   */
  protected abstract Map<Entity, DatabaseEntity> installEntity();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   normalizeError
  /**
   ** Returns the implementation specific error code for a vendor specific code.
   ** <p>
   ** The vendor code is taken from an SQLException that is catched somewhere
   ** and wrapped in a {@link DatabaseException}. This prefix the vendor
   ** specific code constantly with <code>SQL-</code>.
   **
   ** @param  throwable          the exception thrown by a process step that
   **                            may contain a vendor spefific error code.
   **
   ** @return                    a implementation specific error code if it's
   **                            translatable; otherwise <code>DBA-0001</code>
   **                            will be returned.
   */
  protected abstract String normalizeError(final DatabaseException throwable);
}