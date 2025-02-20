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

    File        :   TestCase.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    TestCase.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.efbs.dbs;

import java.util.Map;
import java.util.List;
import java.util.LinkedHashMap;

import java.sql.Connection;

import oracle.hst.foundation.object.Pair;

import oracle.iam.system.simulation.dbs.DatabaseResource;
import oracle.iam.system.simulation.dbs.DatabaseException;
import oracle.iam.system.simulation.dbs.DatabaseConnection;

import oracle.iam.system.simulation.efbs.v2.schema.Account;

////////////////////////////////////////////////////////////////////////////////
// class TestCase
// ~~~~~ ~~~~~~~~
/**
 ** The general test case to manage entries in the persistence layer leveraging
 ** the <code>DatabaseConnection</code> facade.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
abstract class TestCase {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  Connection connection = null;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Entry
  // ~~~~~ ~~~~~
  public static abstract class Entry {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    final Map<String, Object> create = new LinkedHashMap<String, Object>();
    final Map<String, Object> update = new LinkedHashMap<String, Object>();

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: create
    public abstract Map<String, Object> create();

    ////////////////////////////////////////////////////////////////////////////
    // Method: update
    public abstract Map<String, Object> update();
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Zitterbacke
  // ~~~~~ ~~~~~~~~~~~
  public static class ZitterBacke extends Entry {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    static final String UID = "A9113D04BF65276AE0533302A8C045C9";

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: create (Entry)
    @Override
    public Map<String, Object> create() {
      if (this.create.size() == 0) {
        this.create.put(Account.Attribute.USERNAME.id,     "azitterbacke");
        this.create.put(Account.Attribute.LASTNAME.id,     "Zitterbacke");
        this.create.put(Account.Attribute.FIRSTNAME.id,    "Alfons");
        this.create.put(Account.Attribute.ORGANIZATION.id, "AN");
        this.create.put(Account.Attribute.DIVISION.id,     "AN_1");
        this.create.put(Account.Attribute.DEPARTMENT.id,   "AN_1_1");
      }
      return this.create;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: update (Entry)
    @Override
    public Map<String, Object> update() {
      if (this.update.size() == 0) {
        this.update.put(Account.Attribute.ORGANIZATION.id, "DD");
        this.update.put(Account.Attribute.DIVISION.id,     "DD_1");
        this.update.put(Account.Attribute.DEPARTMENT.id,   "DD_1_1");
      }
      return this.create;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class MusterMann
  // ~~~~~ ~~~~~~~~~~
  public static class MusterMann extends Entry {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    static final String UID = "4627fac2-ed43-4928-a1f5-4f6bdd25f9d0";

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: create (Entry)
    @Override
    public Map<String, Object> create() {
      if (this.create.size() == 0) {
        this.create.put(Account.Attribute.USERNAME.id,     "amustermann");
        this.create.put(Account.Attribute.LASTNAME.id,     "Mustermann");
        this.create.put(Account.Attribute.FIRSTNAME.id,    "Alfred");
        this.create.put(Account.Attribute.ORGANIZATION.id, "BK");
        this.create.put(Account.Attribute.DIVISION.id,     "BK_1");
        this.create.put(Account.Attribute.DEPARTMENT.id,   "BK_1_1");
      }
      return this.create;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: update (Entry)
    @Override
    public Map<String, Object> update() {
      if (this.update.size() == 0) {
        this.update.put(Account.Attribute.ORGANIZATION.id, "DD");
        this.update.put(Account.Attribute.DIVISION.id,     "DD_1");
        this.update.put(Account.Attribute.DEPARTMENT.id,   "DD_1_1");
      }
      return this.create;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class MusterMann
  // ~~~~~ ~~~~~~~~~~
  public static class MusterFrau extends Entry {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    static final String UID = "05027f17-d5b7-435d-9684-a4c771274a84";

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: create (Entry)
    @Override
    public Map<String, Object> create() {
      if (this.create.size() == 0) {
        this.create.put(Account.Attribute.USERNAME.id,     "amusterfrau");
        this.create.put(Account.Attribute.LASTNAME.id,     "Musterfrau");
        this.create.put(Account.Attribute.FIRSTNAME.id,    "Agathe");
        this.create.put(Account.Attribute.ORGANIZATION.id, "BP");
        this.create.put(Account.Attribute.DIVISION.id,     "BP_1");
        this.create.put(Account.Attribute.DEPARTMENT.id,   "BP_1_1");
      }
      return this.create;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: update (Entry)
    @Override
    public Map<String, Object> update() {
      if (this.update.size() == 0) {
        this.update.put(Account.Attribute.ORGANIZATION.id, "DD");
        this.update.put(Account.Attribute.DIVISION.id,     "DD_1");
        this.update.put(Account.Attribute.DEPARTMENT.id,   "DD_1_1");
      }
      return this.create;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connect
  /**
   ** Attempts to establish a connection to the database by building the
   ** connection properties leveraging the provided parameter.
   **
   ** @param  resource           the resource descriptor providing the
   **                            connection properties
   **                            <br>
   **                            Allowed object is {@link DatabaseResource}.
   **
   ** @throws DatabaseException  if a database access error occurs or the url is
   **                            <code>null</code>.
   */
  public void connect(final DatabaseResource resource)
    throws DatabaseException {

    this.connection = DatabaseConnection.aquire("orcl", resource);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disconnect
  /**
   ** Release the obtained JDBC resources.
   ** <p>
   ** All SQLExceptions are silently handled.
   */
  public void disconnect() {
    DatabaseConnection.release(this.connection);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commit
  /**
   ** Commit a transaction in supplied connection.
   **
   ** @throws DatabaseException  if connection cannot be commited.
   */
  public final void commit()
    throws DatabaseException {

    DatabaseConnection.commitConnection(this.connection);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rollback
  /**
   ** Rollback a transaction in supplied connection
   **
   ** @throws DatabaseException  if connection cannot be rollback.
   */
  public final void rollback()
    throws DatabaseException {

    DatabaseConnection.rollbackConnection(this.connection);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dumpResult
  /**
   ** Logs a result set to system consol out str.eam
   */
  public final void dumpResult(final List<List<Pair<String, Object>>> result) {
    for (List<Pair<String, Object>> cursor : result) {
      dumpRecord(cursor);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dumpRecord
  /**
   ** Logs a record set to system consol out str.eam
   */
  public final void dumpRecord(final List<Pair<String, Object>> record) {
    for (Pair<String, Object> cursor : record) {
      System.out.println(cursor );
    }
  }
}