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
    Subsystem   :   Identity Governance Provisioning

    File        :   Search.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Search.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-06-11  DSteding    First release version
*/

package oracle.iam.junit.jes.connector.globalrole;

import java.util.List;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import oracle.iam.platform.authopss.vo.AdminRole;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.jes.ServerEndpoint;

import oracle.iam.identity.icf.connector.oig.remote.Context;

import oracle.iam.junit.jes.connector.TestFixture;

////////////////////////////////////////////////////////////////////////////////
// class Search
// ~~~~~ ~~~~~~
/**
 ** The test case search operation on identities at the target system leveraging
 ** the connector implementation directly.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Search extends TestFixture {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Search</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Search() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   main
  /**
   ** Simple command line interface to execute the test case.
   **
   ** @param  args               the command line arguments.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   */
  @SuppressWarnings("unused")
  public static void main(final String[] args) {
    final String[] parameter = {Search.class.getName()};
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fullSearch
  /**
   ** Test search request leveraging server context.
   */
  @Test
  public void fullSearch() {
    Context context = null;
    try {
      context = Context.build(ServerEndpoint.build(CONSOLE, config(ENDPOINT)));
      assertNotNull(context);
      context.login();
      final List<AdminRole> result = context.searchGlobalRole();
      assertNotNull(result);
    }
    catch (SystemException e) {
      failed(e);
    }
    finally {
      if (context != null)
        context.logout();
        context.shutdown();
    }
  }
}