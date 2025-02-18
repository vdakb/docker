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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager UnitTest Library
    Subsystem   :   N.SIS Universal Police Client SCIM

    File        :   TestAccountResolve.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestAccountResolve.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2022-28-08  DSteding    First release version
*/

package oracle.iam.junit.gws.connector.nsis;

import org.junit.Test;
import org.junit.Assert;
import org.junit.FixMethodOrder;

import org.junit.runners.MethodSorters;

import oracle.iam.identity.icf.foundation.SystemException;

////////////////////////////////////////////////////////////////////////////////
// class TestAccountResolve
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** The test case to lookup a SCIM 2 user resource by its unique identifier (aka
 ** userName) leveraging the <code>Connector Server</code> facade.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
public class TestAccountResolve extends TestAccountFixture {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestAccountResolve</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestAccountResolve() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveUnkown
  /**
   ** Test that not existing accounts are not resolvable.
   */
	@Test
	public void resolveUnkown() {
    try {
      final String uid = context.resolveAccount(Unknown.NAME);
      Assert.assertNull(uid);
    }
    catch (SystemException e) {
     failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveSophieStrecke
  /**
   ** Test that existing accounts are be resolvable.
   */
	@Test
	public void resolveSophieStrecke() {
    try {
      final String uid = context.resolveAccount(Sophie.NAME);
      Assert.assertNotNull(uid);
    }
    catch (SystemException e) {
     failed(e);
    }
  }
}