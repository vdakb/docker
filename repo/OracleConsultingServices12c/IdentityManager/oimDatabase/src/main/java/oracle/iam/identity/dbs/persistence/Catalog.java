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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   Catalog.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Catalog.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.dbs.persistence;

import java.util.Set;
import java.util.List;

import oracle.hst.foundation.object.Pair;

import oracle.iam.identity.foundation.persistence.DatabaseEntity;
import oracle.iam.identity.foundation.persistence.DatabaseFilter;

////////////////////////////////////////////////////////////////////////////////
// class Catalog
// ~~~~~ ~~~~~~~
/**
 ** The descriptor to handle entities.
 */
public class Catalog extends DatabaseEntity {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final DatabaseFilter filter;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Type
  // ~~~~~ ~~~~
  /**
   ** The type descriptor.
   */
  public static enum Type {
    /**
     ** a <code>Catalog Definition</code> provides information about privileges
     ** in a database.
     */
    Privilege,

    /**
     ** a <code>Catalog Definition</code> provides information about roles in
     ** a database.
     */
    Role,

    /**
     ** a <code>Catalog Definition</code> provides information about profiles in
     ** a database.
     */
    Profile,

    /**
     ** a <code>Catalog Definition</code> provides information about permanent
     ** tablespaces.
     */
    TablespacePermanent,

    /**
     ** a <code>Catalog Definition</code> provides information about temporary
     ** tablespaces.
     */
    TablespaceTemporary,

    /**
     ** a <code>Catalog Definition</code> provides information about schemes in
     ** a database.
     */
    Schema,
    /**
     ** a <code>Catalog Definition</code> provides information about synonyms in
     ** a database.
     */
    Synonym,

    /**
     ** a <code>Catalog Definition</code> provides information about sequences in
     ** a database.
     */
    Sequence,

    /**
     ** a <code>Catalog Definition</code> provides information about tables in a
     ** database.
     */
    Table,

    /**
     ** a <code>Catalog Definition</code> provides information about views in a
     ** database.
     */
    View,

    /**
     ** a <code>Catalog Definition</code> provides information about user defined
     ** types in a database.
     */
    Type,

    /**
     ** a <code>Catalog Definition</code> provides information about stored
     ** functions in a database.
     */
    Function,

    /**
     ** a <code>Catalog Definition</code> provides information about stored
     ** procedures in a database.
     */
    Procedure,

    /**
     ** a <code>Catalog Definition</code> provides information about stored
     ** packages in a database.
     */
    Package,

    /**
     ** a <code>Catalog Definition</code> provides information about stored
     ** Java Classes in a database.
     */
    JavaClass,

    /**
     ** a <code>Catalog Definition</code> provides information about stored
     ** .NET functions in a database.
     */
    DOTNET
    ;

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:-8300965949890183428")
    private static final long serialVersionUID = -1L;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Catalog</code> with the specified
   ** properties.
   **
   ** @param  schema             the name identifying the schema where the
   **                            entity is defined within.
   ** @param  entity             the name of the entity to access.
   ** @param  primary            the name of attribute identifying a record
   **                            in the entity.
   */
  private Catalog(final String schema, final String entity, final String primary) {
    // ensure inheritance
    super(schema, entity, primary);

    // initialize instance attributes
    this.filter = null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Catalog</code> with the specified
   ** properties.
   **
   ** @param  schema             the name identifying the schema where the
   **                            entity is defined within.
   ** @param  entity             the name of the entity to access.
   ** @param  primary            the name of attribute identifying a record
   **                            in the entity.
   ** @param  type               the name of the attribute to distinguish
   **                            between different types in the entity.
   ** @param  value              the value the specified <code>type</code> must
   **                            match to be part a result set.
   **                            between different types in the entity.
   */
  private Catalog(final String schema, final String entity, final String primary, final String type, final String value) {
    // ensure inheritance
    this(schema, entity, primary, DatabaseFilter.build(type, value, DatabaseFilter.Operator.EQUAL));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Catalog</code> with the specified
   ** properties.
   **
   ** @param  schema             the name identifying the schema where the
   **                            entity is defined within.
   ** @param  entity             the name of the entity to access.
   ** @param  primary            the name of attribute identifying a record
   **                            in the entity.
   ** @param  type               the value the specified <code>primary</code>
   **                            must match to be part a result set.
   **                            between different types in the entity.
   ** @param  attribute          the names of the attributes accomplishing the
   **                            description of an entity.
   */
  private Catalog(final String schema, final String entity, final String primary, final String type, final Set<String> attribute) {
    // ensure inheritance
    this(schema, entity, primary, DatabaseFilter.build(primary, type, DatabaseFilter.Operator.EQUAL), attribute);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Catalog</code> with the specified
   ** properties.
   **
   ** @param  schema             the name identifying the schema where the
   **                            entity is defined within.
   ** @param  entity             the name of the entity to access.
   ** @param  primary            the name of attribute identifying a record
   **                            in the entity.
   ** @param  filter             the {@link DatabaseFilter} to be applied on a
   **                            catalog to fetch entries that match specific
   **                            criteria.
   */
  private Catalog(final String schema, final String entity, final String primary, final DatabaseFilter filter) {
    // ensure inheritance
    super(schema, entity, primary);

    // initialize instance attributes
    this.filter = filter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Catalog</code> with the specified
   ** properties.
   **
   ** @param  schema             the name identifying the schema where the
   **                            entity is defined within.
   ** @param  entity             the name of the entity to access.
   ** @param  primary            the name of attribute identifying a record
   **                            in the entity.
   ** @param  attribute          the names of the attributes accomplishing the
   **                            description of an entity.
   */
  private Catalog(final String schema, final String entity, final String primary, final Set<String> attribute) {
    // ensure inheritance
    super(schema, entity, primary, attribute);

    // initialize instance attributes
    this.filter = null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Catalog</code> with the specified
   ** properties.
   **
   ** @param  schema             the name identifying the schema where the
   **                            entity is defined within.
   ** @param  entity             the name of the entity to access.
   ** @param  primary            the name of attribute identifying a record
   **                            in the entity.
   ** @param  attribute          the names of the attributes accomplishing the
   **                            description of an entity.
   */
  private Catalog(final String schema, final String entity, final String primary, final DatabaseFilter filter, final Set<String> attribute) {
    // ensure inheritance
    super(schema, entity, primary, attribute);

    // initialize instance attributes
    this.filter = filter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Catalog</code> with the specified
   ** properties.
   **
   ** @param  schema             the name identifying the schema where the
   **                            entity is defined within.
   ** @param  entity             the name of the entity to access.
   ** @param  primary            the name of attribute identifying a record
   **                            in the entity.
   ** @param  attribute          the names of the attributes accomplishing the
   **                            description of an entity.
   */
  private Catalog(final String schema, final String entity, final String primary, final DatabaseFilter filter, final List<Pair<String, String>> attribute) {
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
   ** Factory method to create an empty <code>Catalog</code> with the specified
   ** properties.
   **
   ** @param  schema             the name identifying the schema where the
   **                            entity is defined within.
   ** @param  entity             the name of the entity to access.
   ** @param  primary            the name of attribute identifying a record
   **                            in the entity.
   **
   ** @return                    an instance of <code>Catalog</code>
   **                            with the properties provided.
   */
  public static Catalog build(final String schema, final String entity, final String primary) {
    return new Catalog(schema, entity, primary);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an empty <code>Catalog</code> with the specified
   ** properties.
   **
   ** @param  schema             the name identifying the schema where the
   **                            entity is defined within.
   ** @param  entity             the name of the entity to access.
   ** @param  primary            the name of attribute identifying a record
   **                            in the entity.
   ** @param  filter             the {@link DatabaseFilter} to be applied on a
   **                            catalog to fetch entries that match specific
   **                            criteria.
   **
   ** @return                    an instance of <code>Catalog</code>
   **                            with the properties provided.
   */
  public static Catalog build(final String schema, final String entity, final String primary, final DatabaseFilter filter) {
    return new Catalog(schema, entity, primary, filter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an empty <code>Catalog</code> with the specified
   ** properties.
   **
   ** @param  schema             the name identifying the schema where the
   **                            entity is defined within.
   ** @param  entity             the name of the entity to access.
   ** @param  primary            the name of attribute identifying a record
   **                            in the entity.
   ** @param  filter             the {@link DatabaseFilter} to be applied on a
   **                            catalog to fetch entries that match specific
   **                            criteria.
   ** @param  attribute          the names of the attributes accomplishing the
   **                            description of an entity.
   **
   ** @return                    an instance of <code>Catalog</code>
   **                            with the properties provided.
   */
  public static Catalog build(final String schema, final String entity, final String primary, final DatabaseFilter filter, final Set<String> attribute) {
    return new Catalog(schema, entity, primary, filter, attribute);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an empty <code>Catalog</code> with the specified
   ** properties.
   **
   ** @param  schema             the name identifying the schema where the
   **                            entity is defined within.
   ** @param  entity             the name of the entity to access.
   ** @param  primary            the name of attribute identifying a record
   **                            in the entity.
   ** @param  filter             the {@link DatabaseFilter} to be applied on a
   **                            catalog to fetch entries that match specific
   **                            criteria.
   ** @param  attribute          the names of the attributes accomplishing the
   **                            description of an entity.
   **
   ** @return                    an instance of <code>Catalog</code>
   **                            with the properties provided.
   */
  public static Catalog build(final String schema, final String entity, final String primary, final DatabaseFilter filter, final List<Pair<String, String>> attribute) {
    return new Catalog(schema, entity, primary, filter, attribute);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an empty <code>Catalog</code> with the specified
   ** properties.
   **
   ** @param  schema             the name identifying the schema where the
   **                            entity is defined within.
   ** @param  entity             the name of the entity to access.
   ** @param  primary            the name of attribute identifying a record
   **                            in the entity.
   ** @param  type               the name of the attribute to distinguish
   **                            between different types in the entity.
   ** @param  value              the value the specified <code>type</code> must
   **                            match to be part a result set.
   **                            between different types in the entity.
   **
   ** @return                    an instance of <code>Catalog</code>
   **                            with the properties provided.
   */
  public static Catalog build(final String schema, final String entity, final String primary, final String type, final String value) {
    return new Catalog(schema, entity, primary, type, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an empty <code>Catalog</code> with the specified
   ** properties.
   **
   ** @param  schema             the name identifying the schema where the
   **                            entity is defined within.
   ** @param  entity             the name of the entity to access.
   ** @param  primary            the name of attribute identifying a record
   **                            in the entity.
   ** @param  type               the name of the attribute to distinguish
   **                            between different types in the entity.
   ** @param  attribute          the names of the attributes accomplishing the
   **                            description of an entity.
   **
   ** @return                    an instance of <code>Catalog</code>
   **                            with the properties provided.
   */
  public static Catalog build(final String schema, final String entity, final String primary, final String type, final Set<String> attribute) {
    return new Catalog(schema, entity, primary, type, attribute);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an empty <code>Catalog</code> with the specified
   ** properties.
   **
   ** @param  schema             the name identifying the schema where the
   **                            entity is defined within.
   ** @param  entity             the name of the entity to access.
   ** @param  primary            the name of attribute identifying a record
   **                            in the entity.
   ** @param  attribute          the names of the attributes accomplishing the
   **                            description of an entity.
   **
   ** @return                    an instance of <code>Catalog</code>
   **                            with the properties provided.
   */
  public static Catalog build(final String schema, final String entity, final String primary, final Set<String> attribute) {
    return new Catalog(schema, entity, primary, attribute);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   typeString
  /**
   ** Returns a string with all the possible types a catalog has.
   **
   ** @return                    a string with all the possible types a catalog
   **                            has.
   */
  public static final String typeString() {
    return   Type.Privilege.toString()
    + '|'  + Type.Role.toString()
    + '|'  + Type.Profile.toString()
    + '|'  + Type.TablespacePermanent.toString()
    + '|'  + Type.TablespaceTemporary.toString()
    + '|'  + Type.Schema.toString()
    + '|'  + Type.Synonym.toString()
    + '|'  + Type.Sequence.toString()
    + '|'  + Type.Table.toString()
    + '|'  + Type.View.toString()
    + '|'  + Type.Type.toString()
    + '|'  + Type.Function.toString()
    + '|'  + Type.Procedure.toString()
    + '|'  + Type.Package.toString()
    + '|'  + Type.JavaClass.toString()
    + '|'  + Type.DOTNET.toString();
  }
}