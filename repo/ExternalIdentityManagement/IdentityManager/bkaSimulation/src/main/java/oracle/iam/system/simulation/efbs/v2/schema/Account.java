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
    Subsystem   :   eFBS SCIM Interface

    File        :   Account.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Account.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.efbs.v2.schema;

import java.util.Map;
import java.util.HashMap;

import oracle.jdbc.OracleTypes;

import oracle.hst.foundation.object.Pair;

import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.system.simulation.dbs.DatabaseEntity;

import oracle.iam.system.simulation.scim.Path;

import oracle.iam.system.simulation.scim.v2.schema.EnterpriseUserExtension;

////////////////////////////////////////////////////////////////////////////////
// abstract class Account
// ~~~~~~~~ ~~~~~ ~~~~~~~
/**
 ** This represents the mapping between the SCIM schemas representing users
 ** and the persistence layer.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class Account {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String         ID           = "id";
  public static final String         VERSION      = "meta.version";
  public static final String         CREATED      = "meta.created";
  public static final String         MODIFIED     = "meta.lastModified";
  public static final String         STATUS       = "active";
  public static final String         USERNAME     = "userName";
  public static final String         FAMILYNAME   = "name.familyName";
  public static final String         GIVENNAME    = "name.givenName";
  public static final String         MAIL         = "emails";
  public static final String         PHONE        = "phoneNumbers";
  public static final String         ORGANIZATION = "organization";
  public static final String         DIVISION     = "division";
  public static final String         DEPARTMENT   = "department";
  public static final String         VALIDFROM    = "validFrom";
  public static final String         VALIDTHROUGH = "validTo";

  /**
   ** the collection of outbound attributes that are operational attributes.
   */
  public static final Schema         SCHEMA       = new Schema();

  /** database access layer */
  public static final DatabaseEntity ENTITY       = DatabaseEntity.build(null, "fbt_users", Attribute.ID.id);

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Attribute
  // ~~~~ ~~~~~~~~~
  /**
   ** Attribute descriptor of the database entity
   */
  public enum Attribute {
      ID("id")
    , VERSION("rowversion")
    , CREATED("created_on")
    , UPDATED("updated_on")
    , STATUS("active")
    , USERNAME("username")
    , LASTNAME("lastname")
    , FIRSTNAME("firstname")
    , EMAIL("email")
    , PHONE("phone")
    , ORGANIZATION("organization")
    , DIVISION("division")
    , DEPARTMENT("department")
    , VALIDFROM("validfrom")
    , VALIDTHROUGH("validthrough")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    public final String  id;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Operational</code> that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    Attribute(final String id) {
      // initailize instance attributes
      this.id = id;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Factory method to create a proper <code>Attribute</code> from the given
     ** <code>id</code> value.
     **
     ** @param  id               the string value the <code>Attribute</code>
     **                          should be returned for.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Attribute</code> mapped at
     **                          <code>id</code>.
     **
     ** @throws IllegalArgumentException if the given <code>id</code> is not
     **                                  mapped to an <code>Attribute</code>.
     */
    public static Attribute from(final String id) {
      for (Attribute cursor : Attribute.values()) {
        if (cursor.id.equals(id))
          return cursor;
      }
      throw new IllegalArgumentException(id);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Schema
  // ~~~~~ ~~~~~~~
  /**
   ** Mapping outbound attributes to inbound attributes
   */
  public static class Schema {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /**
     ** the resource type to return on each returned entity.
     */
    public final String                             resource    = "User";
    /**
     ** the collection of outbound attributes that are operational attributes.
     */
    public final Map<String, Pair<String, Integer>> operational = outboundOperational();
    /**
     ** the minimal set of outbound attributes as they are defined in the schema
     ** and which are permissible attributes in operations like search,
     ** filtering etc.
     */
    public final Map<String, Pair<String, Integer>> minimal     = outboundMinimal(operational);
    /**
     ** the registry of outbound attributes as they are defined in the schema
     ** and which are permissible attributes in operations like search,
     ** filtering etc.
     */
    public final Map<String, Pair<String, Integer>> permitted   = outboundPermitted(minimal);

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Schema</code> that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    private Schema() {
      // ensure inheritance
      super();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   outboundOperational
  /**
   ** Initialize the static attributes and cover each extension.
   */
  private static synchronized Map<String, Pair<String, Integer>> outboundOperational() {
    final Map<String, Pair<String, Integer>> mapping = new HashMap<String, Pair<String, Integer>>();
    mapping.put(Path.build(null, ID).toString(),       Pair.of(Attribute.ID.id,      OracleTypes.RAW));
    mapping.put(Path.build(null, VERSION).toString(),  Pair.of(Attribute.VERSION.id, OracleTypes.VARCHAR));
    mapping.put(Path.build(null, CREATED).toString(),  Pair.of(Attribute.CREATED.id, OracleTypes.TIMESTAMP));
    mapping.put(Path.build(null, MODIFIED).toString(), Pair.of(Attribute.UPDATED.id, OracleTypes.TIMESTAMP));
    return CollectionUtility.unmodifiable(mapping);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   outboundMinimal
  /**
   ** Initialize the static attributes and cover each extension
   */
  private static synchronized Map<String, Pair<String, Integer>> outboundMinimal(final Map<String, Pair<String, Integer>> operational) {
    final Map<String, Pair<String, Integer>> mapping = new HashMap<String, Pair<String, Integer>>(operational);
    mapping.put(Path.build(null, USERNAME).toString(), Pair.of(Attribute.USERNAME.id, OracleTypes.VARCHAR));
    return CollectionUtility.unmodifiable(mapping);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   outboundPermitted
  /**
   ** Initialize the static attributes and cover each extension
   */
  private static synchronized Map<String, Pair<String, Integer>> outboundPermitted(final Map<String, Pair<String, Integer>> minimal) {
    final Map<String, Pair<String, Integer>> mapping = new HashMap<String, Pair<String, Integer>>(minimal);
    mapping.put(Path.build(null,                       STATUS).toString(),       Pair.of(Attribute.STATUS.id,       OracleTypes.BOOLEAN));
    mapping.put(Path.build(null,                       USERNAME).toString(),     Pair.of(Attribute.USERNAME.id,     OracleTypes.VARCHAR));
    mapping.put(Path.build(null,                       FAMILYNAME).toString(),   Pair.of(Attribute.LASTNAME.id,     OracleTypes.VARCHAR));
    mapping.put(Path.build(null,                       GIVENNAME).toString(),    Pair.of(Attribute.FIRSTNAME.id,    OracleTypes.VARCHAR));
    mapping.put(Path.build(null,                       MAIL).toString(),         Pair.of(Attribute.EMAIL.id,        OracleTypes.VARCHAR));
    mapping.put(Path.build(null,                       PHONE).toString(),        Pair.of(Attribute.PHONE.id,        OracleTypes.VARCHAR));
    mapping.put(Path.build(EnterpriseUserExtension.ID, ORGANIZATION).toString(), Pair.of(Attribute.ORGANIZATION.id, OracleTypes.VARCHAR));
    mapping.put(Path.build(EnterpriseUserExtension.ID, DIVISION).toString(),     Pair.of(Attribute.DIVISION.id,     OracleTypes.VARCHAR));
    mapping.put(Path.build(EnterpriseUserExtension.ID, DEPARTMENT).toString(),   Pair.of(Attribute.DEPARTMENT.id,   OracleTypes.VARCHAR));
    mapping.put(Path.build(UserExtension.ID,           VALIDFROM).toString(),    Pair.of(Attribute.VALIDFROM.id,    OracleTypes.TIMESTAMP));
    mapping.put(Path.build(UserExtension.ID,           VALIDTHROUGH).toString(), Pair.of(Attribute.VALIDTHROUGH.id, OracleTypes.TIMESTAMP));
    return CollectionUtility.unmodifiable(mapping);
  }
}