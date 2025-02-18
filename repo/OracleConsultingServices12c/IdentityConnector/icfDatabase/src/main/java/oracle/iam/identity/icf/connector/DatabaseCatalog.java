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
    Subsystem   :   Generic Database Connector

    File        :   DatabaseCatalog.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DatabaseCatalog.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector;

import java.util.List;

import oracle.iam.identity.icf.dbms.DatabaseFilter;
import oracle.iam.identity.icf.dbms.DatabaseEntity;
import oracle.iam.identity.icf.dbms.DatabaseAttribute;

////////////////////////////////////////////////////////////////////////////////
// class DatabaseCatalog
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** The descriptor to handle entities.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DatabaseCatalog extends DatabaseEntity {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final DatabaseFilter filter;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DatabaseCatalog</code> with the specified
   ** properties.
   **
   ** @param  schema             the name identifying the schema where the
   **                            entity is defined within.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  name               the name of the entity to access.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  primary            the attribute identifying a record in the
   **                            entity.
   **                            <br>
   **                            Allowed object is {@link DatabaseAttribute}.
   ** @param  filter             the {@link DatabaseFilter} to be applied on a
   **                            catalog to fetch entries that match specific
   **                            criteria.
   **                            <br>
   **                            Allowed object is {@link DatabaseFilter}.
   */
  private DatabaseCatalog(final String schema, final String entity, final DatabaseAttribute primary, final DatabaseFilter filter) {
    // ensure inheritance
    super(schema, entity, primary);

    // initialize instance attributes
    this.filter = filter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DatabaseCatalog</code> with the specified
   ** properties.
   **
   ** @param  schema             the name identifying the schema where the
   **                            entity is defined within.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  name               the name of the entity to access.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  primary            the attribute identifying a record in the
   **                            entity.
   **                            <br>
   **                            Allowed object is array of <code>int</code>.
   ** @param  attribute          the collection of attributes accomplishing the
   **                            description of an entity.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link DatabaseAttribute}.
   */
  private DatabaseCatalog(final String schema, final String entity, final int[] primary, final List<DatabaseAttribute> attribute) {
    // ensure inheritance
    super(schema, entity, primary, attribute);

    // initialize instance attributes
    this.filter = null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DatabaseCatalog</code> with the specified
   ** properties.
   **
   ** @param  schema             the name identifying the schema where the
   **                            entity is defined within.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  name               the name of the entity to access.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  primary            the attribute identifying a record in the
   **                            entity.
   **                            <br>
   **                            Allowed object is array of <code>int</code>.
   ** @param  attribute          the collection of attributes accomplishing the
   **                            description of an entity.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link DatabaseAttribute}.
   ** @param  filter             the {@link DatabaseFilter} to be applied on a
   **                            catalog to fetch entries that match specific
   **                            criteria.
   **                            <br>
   **                            Allowed object is {@link DatabaseFilter}.
   */
  private DatabaseCatalog(final String schema, final String entity, final int[] primary, final List<DatabaseAttribute> attribute, final DatabaseFilter filter) {
    // ensure inheritance
    super(schema, entity, primary, attribute);

    // initialize instance attributes
    this.filter = filter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filter
  /**
   ** Returns the {@link DatabaseFilter} to be applied on a catalog to fetch
   ** entries that match specific criteria.
   **
   ** @return                    the {@link DatabaseFilter} to be applied on a
   **                            catalog to fetch entries that match specific
   **                            criteria.
   **                            <br>
   **                            Possible object is {@link DatabaseFilter}.
   */
  public final DatabaseFilter filter() {
    return this.filter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an empty <code>DatabaseCatalog</code> with the
   ** specified properties.
   **
   ** @param  schema             the name identifying the schema where the
   **                            entity is defined within.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  entity             the name of the entity to access.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  primary            the attribute identifying a record in the
   **                            entity.
   **                            <br>
   **                            Allowed object is {@link DatabaseAttribute}.
   **
   ** @return                    an instance of <code>DatabaseCatalog</code>
   **                            with the properties provided.
   **                            <br>
   **                            Possible object is {@link DatabaseCatalog}.
   */
  public static DatabaseCatalog build(final String schema, final String entity, final DatabaseAttribute primary) {
    return build(schema, entity, primary, DatabaseFilter.NOP);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an empty <code>DatabaseCatalog</code> with the
   ** specified properties.
   **
   ** @param  schema             the name identifying the schema where the
   **                            entity is defined within.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  entity             the name of the entity to access.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  primary            the attribute identifying a record in the
   **                            entity.
   **                            <br>
   **                            Allowed object is {@link DatabaseAttribute}.
   ** @param  filter             the {@link DatabaseFilter} to be applied on a
   **                            catalog to fetch entries that match specific
   **                            criteria.
   **                            <br>
   **                            Allowed object is {@link DatabaseFilter}.
   **
   ** @return                    an instance of <code>DatabaseCatalog</code>
   **                            with the properties provided.
   **                            <br>
   **                            Possible object is {@link DatabaseCatalog}.
   */
  public static DatabaseCatalog build(final String schema, final String entity, final DatabaseAttribute primary, final DatabaseFilter filter) {
    return new DatabaseCatalog(schema, entity, primary, filter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an empty <code>DatabaseCatalog</code> with the
   ** specified properties.
   **
   ** @param  schema             the name identifying the schema where the
   **                            entity is defined within.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  entity             the name of the entity to access.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  primary            the attribute identifying a record in the
   **                            entity.
   **                            <br>
   **                            Allowed object is {@link DatabaseAttribute}.
   ** @param  attribute          the names of the attributes accomplishing the
   **                            description of an entity.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link DatabaseAttribute}.
   ** @param  filter             the {@link DatabaseFilter} to be applied on a
   **                            catalog to fetch entries that match specific
   **                            criteria.
   **                            <br>
   **                            Allowed object is {@link DatabaseFilter}.
   **
   ** @return                    an instance of <code>DatabaseCatalog</code>
   **                            with the properties provided.
   **                            <br>
   **                            Possible object is {@link DatabaseCatalog}.
   */
  public static DatabaseCatalog build(final String schema, final String entity, final int[] primary, final List<DatabaseAttribute> attribute, final DatabaseFilter filter) {
    return new DatabaseCatalog(schema, entity, primary, attribute, filter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an empty <code>DatabaseCatalog</code> with the
   ** specified properties.
   **
   ** @param  schema             the name identifying the schema where the
   **                            entity is defined within.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  entity             the name of the entity to access.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  primary            the attribute identifying a record in the
   **                            entity.
   **                            <br>
   **                            Allowed object is {@link DatabaseAttribute}.
   ** @param  type               the name of the attribute to distinguish
   **                            between different types in the entity.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the value the specified <code>type</code> must
   **                            match to be part a result set.
   **                            between different types in the entity.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of <code>DatabaseCatalog</code>
   **                            with the properties provided.
   **                            <br>
   **                            Possible object is {@link DatabaseCatalog}.
   */
  public static DatabaseCatalog build(final String schema, final String entity, final DatabaseAttribute primary, final String type, final String value) {
    return new DatabaseCatalog(schema, entity, primary, DatabaseFilter.build(type, value, DatabaseFilter.Operator.EQUAL));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an empty <code>DatabaseCatalog</code> with the
   ** specified properties.
   **
   ** @param  schema             the name identifying the schema where the
   **                            entity is defined within.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  entity             the name of the entity to access.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  primary            the attribute identifying a record in the
   **                            entity.
   **                            <br>
   **                            Allowed object is {@link DatabaseAttribute}.
   ** @param  attribute          the collection of attributes accomplishing the
   **                            description of an entity.
   **                            <br>
   **                            Allowed object is array of <code>int</code>.
   **
   ** @return                    an instance of <code>DatabaseCatalog</code>
   **                            with the properties provided.
   **                            <br>
   **                            Possible object is {@link DatabaseCatalog}.
   */
  public static DatabaseCatalog build(final String schema, final String entity, final int[] primary, final List<DatabaseAttribute> attribute) {
    return new DatabaseCatalog(schema, entity, primary, attribute);
  }
}