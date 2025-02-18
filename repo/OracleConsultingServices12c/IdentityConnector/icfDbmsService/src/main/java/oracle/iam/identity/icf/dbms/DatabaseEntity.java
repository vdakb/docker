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

    File        :   DatabaseEntity.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DatabaseEntity.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.dbms;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import oracle.iam.identity.icf.foundation.utility.CollectionUtility;

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
public class DatabaseEntity {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The identifier of the <code>DatabaseEntity</code>. */
  final String                         id;

  /** The attributes forming the primary key of <code>DatabaseEntity</code>. */
  final List<DatabaseAttribute>        primary;
  /** The attributes describing the whole <code>DatabaseEntity</code>. */
  final List<DatabaseAttribute>        attribute;
  /**
   ** The mapping of alias name to attributes of the <code>DatabaseEntity</code>
   ** to resolve bindings and filter criteria.
   */
  final Map<String, DatabaseAttribute> alias;

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
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  name               the name of the entity to access.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  primary            the attribute identifying a record in the
   **                            entity.
   **                            <br>
   **                            Allowed object is {@link DatabaseAttribute}.
   */
  protected DatabaseEntity(final String schema, final String name, final DatabaseAttribute primary) {
    // ensure inheritance
    this(schema, name, new int[]{0}, CollectionUtility.list(primary));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DatabaseEntity</code> with the specified
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
  protected DatabaseEntity(final String schema, final String name, final int[] primary, final List<DatabaseAttribute> attribute) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.id        = DatabaseSchema.qualified(schema, name);
    this.attribute = attribute;
    this.alias     = new HashMap<String, DatabaseAttribute>();
    for (int i = 0; i < this.attribute.size(); i++) {
      final DatabaseAttribute  cursor = this.attribute.get(i);
      this.alias.put(cursor.alias, cursor);
    }

    this.primary   = new ArrayList<DatabaseAttribute>();
    for (int i = 0; i < primary.length; i++)
      this.primary.add(this.attribute.get(primary[i]));

  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   id
  /**
   ** Returns the identifier of the <code>DatabaseEntity</code>.
   **
   ** @return                    the identifier of the
   **                            <code>DatabaseEntity</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String id() {
    return this.id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   primary
  /**
   ** Returns the index of primary attribute to access the entity that can be
   ** used to join this entity with another criteria in a WHERE-clause of a
   ** database statement using the primary key.
   **
   ** @return                    the index of making up the primary key
   **                            attribute.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link DatabaseAttribute}.
   */
  public List<DatabaseAttribute> primary() {
    return this.primary;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Returns the names of the attributes accomplishing the description of an
   ** entity and will be returned by the dabase if this entity is accessed.
   **
   ** @return                    the names of the attributes accomplishing the
   **                            description of an entity.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link DatabaseAttribute}.
   */
  public List<DatabaseAttribute> attribute() {
    return this.attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** Returns the the {@link DatabaseAttribute} for the given alias name.
   **
   ** @param  alias              the alias name the attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link DatabaseAttribute} mapped at
   **                            <code>alias</code>.
   **                            <br>
   **                            Possible object is {@link DatabaseAttribute}.
   */
  public DatabaseAttribute lookup(final String alias) {
    return this.alias.get(alias);
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
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  name               the name of the entity to access.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  primary            the attribute identifying a record in the
   **                            entity.
   **                            <br>
   **                            Allowed object is {@link DatabaseAttribute}.
   **
   ** @return                    an instance of <code>DatabaseEntity</code>
   **                            with the properties provided.
   **                            <br>
   **                            Possible object is {@link DatabaseEntity}.
   */
  public static DatabaseEntity build(final String schema, final String name, final DatabaseAttribute primary) {
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
   **
   ** @return                    an instance of <code>DatabaseEntity</code>
   **                            with the properties provided.
   **                            <br>
   **                            Possible object is {@link DatabaseEntity}.
   */
  public static DatabaseEntity build(final String schema, final String name, final int[] primary, final List<DatabaseAttribute> attribute) {
    return new DatabaseEntity(schema, name, primary, attribute);
  }
}