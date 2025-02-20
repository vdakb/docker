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

    Copyright © 2021 All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Service Simulation
    Subsystem   :   Google API Gateway

    File        :   Organization.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Organization.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2021-28-01  DSteding    First release version
*/

package oracle.iam.system.simulation.apigee.persistence;

import oracle.iam.system.simulation.dbs.DatabaseEntity;

////////////////////////////////////////////////////////////////////////////////
// interface Organization
// ~~~~~~~~~ ~~~~~~~~~~~~
/**
 ** This represents the mapping between the REST schemas representing
 ** organizations and the persistence layer.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface Organization extends Entity {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String         RESOURCE    = "Organization";
  static final String         USERROLE    = "Userrole";
  static final String         DEVELOPER   = "Developer";

  static final String         FK          = "org";
  static final String         TYPE        = "type";
  static final String         DISPLAYNAME = "displayname";

  /** database access layer */
  static final DatabaseEntity ENTITY      = DatabaseEntity.build(null, "agt_organizations", NAME);

  /** database access layer */
  static final DatabaseEntity ENV         = DatabaseEntity.build(null, "agt_environments",  FK);

  /** database access layer */
  static final DatabaseEntity PRP         = DatabaseEntity.build(null, "agt_properties",    FK);

  /** database access layer */
  static final DatabaseEntity ORL         = DatabaseEntity.build(null, "agt_roles",         FK);

  /** database access layer */
  static final DatabaseEntity URL         = DatabaseEntity.build(null, "agt_userroles",     FK);

  /** database access layer */
  static final DatabaseEntity CMP         = DatabaseEntity.build(null, "agt_companies",     Entity.NAME);

  /** database access layer */
  static final DatabaseEntity PRD         = DatabaseEntity.build(null, "agt_products",      Entity.NAME);

  /** database access layer */
  static final DatabaseEntity APP         = DatabaseEntity.build(null, "agt_applications",  Entity.ID);

  /** database access layer */
  static final DatabaseEntity DEV         = DatabaseEntity.build(null, "agt_developers",    Entity.ID);

  /** database access layer */
  static final DatabaseEntity ARL         = DatabaseEntity.build(null, "agt_developer_applications", FK);

  /** database access layer */
  static final DatabaseEntity CRL         = DatabaseEntity.build(null, "agt_developer_companies", FK);

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
      VERSION(Entity.VERSION)
    , CREATEDON(Entity.CREATEDON)
    , CREATEDBY(Entity.CREATEDBY)
    , UPDATEDON(Entity.UPDATEDON)
    , UPDATEDBY(Entity.UPDATEDBY)
    , TYPE(Organization.TYPE)
    , NAME(Organization.NAME)
    , DISPLAYNAME(Organization.DISPLAYNAME)
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
     ** Constructs a <code>Attribute</code> that allows use as a JavaBean.
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
  // enum Developer
  // ~~~~ ~~~~~~~~~
  /**
   ** Developer descriptor of the database entity
   */
  public enum Developer {
      ID(Entity.ID)
    , VERSION(Entity.VERSION)
    , CREATEDON(Entity.CREATEDON)
    , CREATEDBY(Entity.CREATEDBY)
    , UPDATEDON(Entity.UPDATEDON)
    , UPDATEDBY(Entity.UPDATEDBY)
    , STATUS(Entity.STATUS)
    , MAIL(Account.MAIL)
    , USERNAME(Account.USERNAME)
    , LASTNAME(Account.LASTNAME)
    , FIRSTNAME(Account.FIRSTNAME)
    , TENANT(Organization.FK)
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
     ** Constructs a <code>Developer</code> that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    Developer(final String id) {
      // initailize instance attributes
      this.id = id;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Factory method to create a proper <code>Developer</code> from the given
     ** <code>id</code> value.
     **
     ** @param  id               the string value the <code>Developer</code>
     **                          should be returned for.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Developer</code> mapped at
     **                          <code>id</code>.
     **
     ** @throws IllegalArgumentException if the given <code>id</code> is not
     **                                  mapped to an <code>Developer</code>.
     */
    public static Developer from(final String id) {
      for (Developer cursor : Developer.values()) {
        if (cursor.id.equals(id))
          return cursor;
      }
      throw new IllegalArgumentException(id);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // enum Userrole
  // ~~~~ ~~~~~~~~~
  /**
   ** Userrole descriptor of the database entity
   */
  public enum Userrole {
      VERSION(Entity.VERSION)
    , CREATEDON(Entity.CREATEDON)
    , CREATEDBY(Entity.CREATEDBY)
    , UPDATEDON(Entity.UPDATEDON)
    , UPDATEDBY(Entity.UPDATEDBY)
    , TENANT(Organization.FK)
    , NAME(Entity.NAME)
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
     ** Constructs a <code>Userrole</code> that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    Userrole(final String id) {
      // initailize instance attributes
      this.id = id;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Factory method to create a proper <code>Userrole</code> from the given
     ** <code>id</code> value.
     **
     ** @param  id               the string value the <code>Userrole</code>
     **                          should be returned for.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Userrole</code> mapped at
     **                          <code>id</code>.
     **
     ** @throws IllegalArgumentException if the given <code>id</code> is not
     **                                  mapped to an <code>Developer</code>.
     */
    public static Userrole from(final String id) {
      for (Userrole cursor : Userrole.values()) {
        if (cursor.id.equals(id))
          return cursor;
      }
      throw new IllegalArgumentException(id);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // enum Member
  // ~~~~ ~~~~~~~
  /**
   ** Member descriptor of the database entity
   */
  public enum Member {
      VERSION(Entity.VERSION)
    , CREATEDON(Entity.CREATEDON)
    , CREATEDBY(Entity.CREATEDBY)
    , UPDATEDON(Entity.UPDATEDON)
    , UPDATEDBY(Entity.UPDATEDBY)
    , TENANT(Organization.FK)
    , ROLE(Entity.NAME)
    , MAIL(Account.MAIL)
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
     ** Constructs a <code>Member</code> that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    Member(final String id) {
      // initailize instance attributes
      this.id = id;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Factory method to create a proper <code>Member</code> from the given
     ** <code>id</code> value.
     **
     ** @param  id               the string value the <code>Developer</code>
     **                          should be returned for.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Member</code> mapped at
     **                          <code>id</code>.
     **
     ** @throws IllegalArgumentException if the given <code>id</code> is not
     **                                  mapped to an <code>Member</code>.
     */
    public static Member from(final String id) {
      for (Member cursor : Member.values()) {
        if (cursor.id.equals(id))
          return cursor;
      }
      throw new IllegalArgumentException(id);
    }
  }
}