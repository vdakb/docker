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

    File        :   Account.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Account.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2021-28-01  DSteding    First release version
*/

package oracle.iam.system.simulation.apigee.persistence;

import oracle.iam.system.simulation.dbs.DatabaseEntity;

////////////////////////////////////////////////////////////////////////////////
// interface Account
// ~~~~~~~~~ ~~~~~~~
/**
 ** This represents the mapping between the REST schemas representing users
 ** and the persistence layer.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface Account extends Entity {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String         RESOURCE     = "User";

  static final String         ID           = "id";
  static final String         PASSWORD     = "password";
  static final String         MAIL         = "email";
  static final String         USERNAME     = "username";
  static final String         LASTNAME     = "lastname";
  static final String         FIRSTNAME    = "firstname";
  static final String         ORGANIZATION = "org";

  /** database access layer */
  static final DatabaseEntity ENTITY    = DatabaseEntity.build(null, "agt_users", ID);

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
    , ID(Account.ID)
    , MAIL(Account.MAIL)
    , STATUS(Account.STATUS)
    , USERNAME(Account.USERNAME)
    , PASSWORD(Account.PASSWORD)
    , LASTNAME(Account.LASTNAME)
    , FIRSTNAME(Account.FIRSTNAME)
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
}