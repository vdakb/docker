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

    System      :   Oracle Identity Manager UnitTest Library
    Subsystem   :   N.SIS Universal Police Client SCIM

    File        :   TestAccountDelete.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestAccountDelete.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.junit.gws.integration.nsis;

import org.junit.Test;
import org.junit.FixMethodOrder;

import org.junit.runners.MethodSorters;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.junit.gws.integration.nsis.TestAccount.bkbk4711121;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.OperationOptionsBuilder;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

////////////////////////////////////////////////////////////////////////////////
// class TestAccountDelete
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** The test case to create an entry in the target system leveraging the
 ** <code>Connector Server</code> facade.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
public class TestAccountDelete extends TestAccount {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestAccountDelete</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestAccountDelete() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nonExisting
  /**
   ** Test that an account could be deleted thats not exists.
   **
   ** bkbk4711123
   */
	@Test
	public void nonExisting()
    throws TaskException {

    try {
      Network.facade(Network.intranet()).delete(ObjectClass.ACCOUNT, new Uid("4711"), deleteControl().build());
    }
    catch (ConnectorException e) {
      if (!e.getMessage().startsWith("GWS-00109"))
        failed(e);
    }
    catch (TaskException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bkbk4711121
  /**
   ** Test that an account could be deleted thats not exists.
   */
  @Test
  public void bkbk4711121()
    throws TaskException {

    try {
      //Network.facade(Network.intranet()).search(bkbk4711121.UID);
      Network.facade(Network.intranet()).delete(ObjectClass.ACCOUNT, bkbk4711121.UID, deleteControl().build());
    }
    catch (ConnectorException e) {
      failed(e);
    }
    catch (TaskException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteControl
  /**
   ** Factory method to create a new &amp; initialized control configurator
   ** instance.
   **
   ** @return                    a new &amp; initialized control configurator
   **                            instance.
   **                            <br>
   **                            Possible object is
   **                            <code>OperationContext</code>.
   */
  private OperationOptionsBuilder deleteControl() {
    final OperationOptionsBuilder factory = new OperationOptionsBuilder();
    return factory;
  }
}