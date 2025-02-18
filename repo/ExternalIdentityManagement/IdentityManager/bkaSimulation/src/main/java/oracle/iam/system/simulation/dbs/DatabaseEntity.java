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

    Copyright © 2018 All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Service Simulation
    Subsystem   :   Generic SCIM Interface

    File        :   DatabaseEntity.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DatabaseEntity.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.dbs;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;

import oracle.hst.foundation.object.Pair;
import oracle.hst.foundation.object.Entity;

import oracle.hst.foundation.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// class DatabaseEntity
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The descriptor to handle entities.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DatabaseEntity extends Entity {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final String                     primary;
  private final List<Pair<String, String>> returning;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DatabaseEntity</code> with the specified
   ** properties.
   **
   ** @param  schema             the name identifying the schema where the
   **                            entity is defined within.
   ** @param  name               the name of the entity to access.
   ** @param  primary            the name of attribute identifying a record
   **                            in the entity.
   */
  protected DatabaseEntity(final String schema, final String name, final String primary) {
    // ensure inheritance
    super(qualified(schema, name));

    // initialize instance attributes
    this.primary   = qualified(type(), primary);
    this.returning = new ArrayList<Pair<String, String>>(1);
    this.returning.add(Pair.of(this.primary, primary));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DatabaseEntity</code> with the specified
   ** properties.
   **
   ** @param  schema             the name identifying the schema where the
   **                            entity is defined within.
   ** @param  name               the name of the entity to access.
   ** @param  primary            the name of attribute identifying a record
   **                            in the entity.
   ** @param  returning          the names of the attributes acomplishing the
   **                            description of an entity.
   */
  protected DatabaseEntity(final String schema, final String name, final String primary, final Set<String> returning) {
    // ensure inheritance
    super(qualified(schema, name));

    // initialize instance attributes
    this.primary   = qualified(type(), primary);
    this.returning = new ArrayList<Pair<String, String>>(returning == null ? 1 : returning.size() + 1);
    this.returning.add(Pair.of(this.primary, primary));
    if (returning != null) {
      for (String cursor : returning) {
        this.returning.add(Pair.of(qualified(type(), cursor), cursor));
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DatabaseEntity</code> with the specified
   ** properties.
   **
   ** @param  schema             the name identifying the schema where the
   **                            entity is defined within.
   ** @param  name               the name of the entity to access.
   ** @param  primary            the name of attribute identifying a record
   **                            in the entity.
   ** @param  returning          the names of the attributes acomplishing the
   **                            description of an entity.
   */
  protected DatabaseEntity(final String schema, final String name, final String primary, final List<Pair<String, String>> returning) {
    // ensure inheritance
    super(qualified(schema, name));

    // initialize instance attributes
    this.primary   = qualified(type(), primary);
    this.returning = new ArrayList<Pair<String, String>>(returning == null ? 1 : returning.size() + 1);
    this.returning.add(Pair.of(this.primary, primary));
    if (returning != null) {
      for (Pair<String, String> cursor : returning) {
        this.returning.add(cursor);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   primary
  /**
   ** Returns the name of the primary attribute to access the entity that can be
   ** used to join this entity with another criteria in a WHERE-clause of a
   ** database statement using the primary key.
   **
   ** @return                    the full qualified column name of the primary
   **                            key.
   */
  public String primary() {
    return this.primary;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   returning
  /**
   ** Returns the names of the attributes acomplishing the description of an
   ** entity and will be returned by the dabase if this entity is accessed.
   **
   ** @return                    the names of the attributes acomplishing the
   **                            description of an entity.
   */
  public List<Pair<String, String>> returning() {
    return this.returning;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an empty <code>DatabaseEntity</code> with the
   ** specified properties.
   **
   ** @param  schema             the name identifying the schema where the
   **                            entity is defined within.
   ** @param  name               the name of the entity to access.
   ** @param  primary            the name of attribute identifying a record
   **                            in the entity.
   **
   ** @return                    an instance of <code>DatabaseEntity</code>
   **                            with the properties provided.
   */
  public static DatabaseEntity build(final String schema, final String name, final String primary) {
    return new DatabaseEntity(schema, name, primary);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an empty <code>DatabaseEntity</code> with the
   ** specified properties.
   **
   ** @param  schema             the name identifying the schema where the
   **                            entity is defined within.
   ** @param  name               the name of the entity to access.
   ** @param  primary            the name of attribute identifying a record
   **                            in the entity.
   ** @param  returning          the names of the attributes acomplishing the
   **                            description of an entity.
   **
   ** @return                    an instance of <code>DatabaseEntity</code>
   **                            with the properties provided.
   */
  public static DatabaseEntity build(final String schema, final String name, final String primary, final Set<String> returning) {
    return new DatabaseEntity(schema, name, primary, returning);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an empty <code>DatabaseEntity</code> with the
   ** specified properties.
   **
   ** @param  schema             the name identifying the schema where the
   **                            entity is defined within.
   ** @param  name               the name of the entity to access.
   ** @param  primary            the name of attribute identifying a record
   **                            in the entity.
   ** @param  returning          the names of the attributes acomplishing the
   **                            description of an entity.
   **
   ** @return                    an instance of <code>DatabaseEntity</code>
   **                            with the properties provided.
   */
  public static DatabaseEntity build(final String schema, final String name, final String primary, final List<Pair<String, String>> returning) {
    return new DatabaseEntity(schema, name, primary, returning);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   qualified
  /**
   ** Returns a qualified string that can be used to join this entity with
   ** another criteria like a WHERE-clause of a database statement or a
   ** fullqualified name of an database object using the specified
   ** column name.
   **
   ** @param  qualifier          the qualifier prefix of the fullqualified name.
   ** @param  name               the name to be qualified.
   **
   * @return                     the full qualified name.
   */
  public static String qualified(final String qualifier, final String name) {
    return (StringUtility.isEmpty(qualifier)) ? name : qualifier.concat(".").concat(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   qualified
  /**
   ** Returns a string that can be used to join this entity with another
   ** criteria in a WHERE-clause of a database statement using the specified
   ** column name.
   **
   ** @param  column             the name of the column used in the join
   **                            condition.
   **
   * @return                    the full qualified name of the column.
   */
  public String qualified(final String column) {
    return this.type().concat(".").concat(column);
  }
}