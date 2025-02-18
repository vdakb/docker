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

    Copyright © 2017. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Frontend Extension
    Subsystem   :   System Authorization Management

    File        :   LookupDataProvider.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    LookupDataProvider.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysconfig.schema;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oracle.iam.platform.utils.InvalidLookupQueryException;

import oracle.iam.platform.utils.resources.LRB;

////////////////////////////////////////////////////////////////////////////////
// class LookupQueryDataProvider
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Data Provider Object used by System Configuration Management customization.
 ** <p>
 ** Define an instance variable for each VO attribute, with the data type that
 ** corresponds to the VO attribute type. The name of the instance variable must
 ** match the name of the VO attribute, and the case must match.
 ** <p>
 ** When the instance of this class is passed to the methods in the
 ** corresponding <code>Application Module</code> class, the
 ** <code>Application Module</code> can call the getter methods to retrieve the
 ** values from the AdapterBean and pass them to the OIM APIs.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
 */
public class LookupQueryDataProvider {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>LookupQueryDataProvider</code> data access object
   ** that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public LookupQueryDataProvider() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   validateLookupQuery
  /**
   ** Validates the lookup query.
   ** <p>
   ** Expects the query to be in the SELECT-FROM-WHERE format with aliases
   ** specified using AS for each column in the SELECT clause.
   **
   ** @param  query              the lookup query string.
   **
   ** @return                    the list of the aliases of column in the
   **                            SELECT clause
   **
   ** @throws InvalidLookupQueryException if the query string is malformed.
   */
  public static List<String> validateLookupQuery(String query)
    throws InvalidLookupQueryException {

    ArrayList<String> select = new ArrayList<String>();

    query = query.toUpperCase().trim();
    boolean isSelectPresent = query.startsWith("SELECT", 0);
    if (!isSelectPresent)
      throw new InvalidLookupQueryException(LRB.DEFAULT.getString("IAM-0075000"));

    int fromIndex = query.indexOf("FROM");
    if (fromIndex == -1)
      throw new InvalidLookupQueryException(LRB.DEFAULT.getString("IAM-0075001"));

    String selectClause = query.substring(6, fromIndex);

    //Added for Bug#9592002
    Pattern p = Pattern.compile("SELECT\\s*[*]");
    Matcher m = p.matcher(query);
    boolean isMatch = false;
    while (m.find()) {
      isMatch = true;
    }

    if (isMatch)
      throw new InvalidLookupQueryException(LRB.DEFAULT.getString("IAM-0075002"));

    tokenizeQuery(selectClause, select);
    return select;
  }

  private static void tokenizeQuery(String query, List<String> select) {

    String[] tokens = query.split(" AS ");

    for (int i = 1; i < tokens.length; i++) {
      String subquery = tokens[i];
      int comma = subquery.indexOf(',');

      if (comma != -1) {
        subquery = subquery.substring(0, comma);
      }
      select.add(subquery.trim());
    }
  }
}
