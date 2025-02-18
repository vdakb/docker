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

    File        :   TestSearch.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    TestSearch.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.efbs.dbs;

import java.util.List;
import java.util.ArrayList;

import java.sql.Connection;

import oracle.hst.foundation.object.Pair;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.system.simulation.dbs.DatabaseFilter;
import oracle.iam.system.simulation.dbs.DatabaseSearch;
import oracle.iam.system.simulation.dbs.DatabaseResource;
import oracle.iam.system.simulation.dbs.DatabaseException;
import oracle.iam.system.simulation.dbs.DatabaseConnection;

import oracle.iam.system.simulation.ProcessingException;

import oracle.iam.system.simulation.scim.object.Filter;

import oracle.iam.system.simulation.scim.utility.Parser;

import oracle.iam.system.simulation.efbs.v2.schema.Account;
import oracle.iam.system.simulation.efbs.v2.schema.FilterTranslator;

////////////////////////////////////////////////////////////////////////////////
// class TestSearch
// ~~~~~ ~~~~~~~~~~
/**
 ** The <code>TestSearch</code> is unit testing the search capabilities of the
 ** implementation by directly operationg at the persistence layer.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestSearch {

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String EMIT       = "userName , name.familyName, name.givenName";
  static final String EXPRSSSION = "userName eq \"sn4711123\" and (name.familyName eq \"Steding\") and (name.givenName sw \"D\")";

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   main
  /**
   ** Entry point to the unit test.
   **
   ** @param  args               the array ouf command line arguments passed in.
   */
  public static void main(final String[] args) {
    final List<Pair<String, String>> returning = new ArrayList<Pair<String, String>>();
    for (Pair<String, Integer> cursor : Account.SCHEMA.minimal.values()) {
      returning.add(Pair.of(cursor.tag, cursor.tag));
    }
    for (String cursor : StringUtility.splitCSV(EMIT)) {
      final Pair<String, Integer> attribute = Account.SCHEMA.permitted.get(cursor);
      final Pair<String, String>  pair      = Pair.of(attribute.tag, attribute.tag);
      if (!returning.contains(pair)) {
        returning.add(pair);
      }
    }

    List<DatabaseFilter> filter = null;
    try {
      final Filter               criteria = Parser.filter(EXPRSSSION);
      filter = FilterTranslator.build(Account.ENTITY, criteria);
    }
    catch (ProcessingException e) {
      e.printStackTrace();
    }

    final DatabaseResource target = DatabaseResource.build("buster.vm.oracle.com", 7021, "mdr.vm.oracle.com", "igd_fbs", "Sophie20061990$");
    final DatabaseSearch   search = DatabaseSearch.build(Account.ENTITY, filter.get(0), returning);

    Connection connection = null;
    try {
      connection = DatabaseConnection.aquire("orcl", target);
      search.prepare(connection);
      final List<List<Pair<String, Object>>> result = search.execute(1L, 1L);
      for (List<Pair<String, Object>> record : result) {
        for (Pair<String, Object> cursor : record) {
          System.out.println(cursor );
        }
      }
    }
    catch (DatabaseException e) {
      e.printStackTrace();
    }
    finally {
      DatabaseConnection.release(connection);
    }
  }
}