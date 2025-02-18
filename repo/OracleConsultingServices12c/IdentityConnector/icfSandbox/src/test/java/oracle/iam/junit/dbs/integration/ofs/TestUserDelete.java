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

    File        :   TestUserDelete.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestUserDelete.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-13-06  DSteding    First release version
*/

package oracle.iam.junit.dbs.integration.ofs;

import org.junit.Test;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.OperationOptionsBuilder;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

import oracle.iam.identity.foundation.TaskException;

////////////////////////////////////////////////////////////////////////////////
// class TestUserDelete
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The test case to delete accounts leveraging the
 ** <code>Connector Server</code> facade.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestUserDelete extends TestUser {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestUserDelete</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestUserDelete() {
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
      Network.facade(Network.intranet()).delete(ObjectClass.ACCOUNT, new Uid("bwg"), deleteControl().build());
    }
    catch (ConnectorException e) {
      if (!e.getMessage().startsWith("DBS-00105"))
        failed(e);
    }
    catch (TaskException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   existing
  /**
   ** Test that an account could be deleted thats not exists.
   */
	@Test
	public void bkbk4711123()
    throws TaskException {

    try {
      Network.facade(Network.intranet()).delete(ObjectClass.ACCOUNT, bkbk4711123.UID, deleteControl().build());
    }
    catch (ConnectorException e) {
      failed(e);
    }
    catch (TaskException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bwgaertmax
  /**
   ** Test that an account could be deleted thats not exists.
   */
	@Test
	public void bwgaertmax()
    throws TaskException {

    try {
      Network.facade(Network.intranet()).delete(ObjectClass.ACCOUNT, bwgaertmax.UID, deleteControl().build());
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
