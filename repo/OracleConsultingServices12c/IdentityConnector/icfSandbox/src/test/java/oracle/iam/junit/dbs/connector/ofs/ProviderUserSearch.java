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

    Copyright © 2021. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager UnitTest Library
    Subsystem   :   Openfire Database Connector

    File        :   ProviderUserSearch.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    ProviderUserSearch.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-13-06  DSteding    First release version
*/

package oracle.iam.junit.dbs.connector.ofs;

import java.util.List;

import org.junit.Test;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.dbms.DatabaseFilter;

import oracle.iam.identity.icf.connector.openfire.schema.User;

import oracle.iam.identity.icf.connector.openfire.Provider;

import oracle.iam.identity.icf.connector.openfire.provider.UserProvider;

////////////////////////////////////////////////////////////////////////////////
// class ProviderUserSearch
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** The test cases for search users leveraging the the
 ** <code>Database Server</code> JDBC driver.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ProviderUserSearch extends Transaction {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final Provider<User> provider;
  protected final DatabaseFilter criteria = DatabaseFilter.build(User.UID, "admin", DatabaseFilter.Operator.EQUAL);

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ProviderUserSearch</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   **
   ** @throws SystemException    if the schema operation fails.
   */
  public ProviderUserSearch()
    throws SystemException {
    // ensure inheritance
    super();

    // common to all is the provider
    this.provider = UserProvider.build(this.context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   selectAdmin
  @Test
  public void selectAdmin() {
    try {
      final User user = this.provider.lookup(this.criteria);
      notNull("User entity expected", user);
      notNull("'username' expected", user.uid());
      equals("'admin' expected as username", "admin", user.uid());
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchAll
  @Test
  public void searchAll() {
    try {
      final List<User> collection = this.provider.search(null, 1L, 500L);
      notNull("Collection of users expected", collection);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchAdmin
  @Test
  public void searchAdmin() {
    try {
      final List<User> collection = this.provider.search(this.criteria, 1L, 500L);
      notNull("Collection of users expected", collection);
    }
    catch (SystemException e) {
      failed(e);
    }
  }
}