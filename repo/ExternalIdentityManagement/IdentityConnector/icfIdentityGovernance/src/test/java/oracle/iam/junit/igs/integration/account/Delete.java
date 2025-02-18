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
    Subsystem   :   Identity Governance Service SCIM

    File        :   Delete.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Delete.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2022-06-11  DSteding    First release version
*/

package oracle.iam.junit.igs.integration.account;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import org.identityconnectors.framework.common.objects.ObjectClass;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

import oracle.iam.identity.foundation.TaskException;

import org.identityconnectors.framework.api.ConnectorFacade;

////////////////////////////////////////////////////////////////////////////////
// class Delete
// ~~~~~ ~~~~~~
/**
 ** The test case for delete operation on accounts at the target system
 ** leveraging the connector bundle deployed on a
 ** <code>Java Connector Server</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Delete extends Base {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Delete</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Delete() {
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
    final String[] parameter = {Delete.class.getName()};
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteRegularUser
  /**
   ** Test that a particular account could be deleted and the operation returns
   ** same primary identifier.
   */
  @Test
  public void deleteRegularUser() {
    deleteUser("azitterbacke");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteServiceUserAN
  /**
   ** Test that a particular account could be deleted and the operation returns
   ** same primary identifier.
   */
  @Test
  public void deleteServiceUserAN() {
    deleteUser("anserviceuser");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteServiceUserBB
  /**
   ** Test that a particular account could be deleted and the operation returns
   ** same primary identifier.
   */
  @Test
  public void deleteServiceUserBB() {
    deleteUser("bbserviceuser");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteServiceUserBE
  /**
   ** Test that a particular account could be deleted and the operation returns
   ** same primary identifier.
   */
  @Test
  public void deleteServiceUserBE() {
    deleteUser("beserviceuser");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteServiceUserBK
  /**
   ** Test that a particular account could be deleted and the operation returns
   ** same primary identifier.
   */
  @Test
  public void deleteServiceUserBK() {
    deleteUser("bkserviceuser");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteServiceUserBP
  /**
   ** Test that a particular account could be deleted and the operation returns
   ** same primary identifier.
   */
  @Test
  public void deleteServiceUserBP() {
    deleteUser("bpserviceuser");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteServiceUserBW
  /**
   ** Test that a particular account could be deleted and the operation returns
   ** same primary identifier.
   */
  @Test
  public void deleteServiceUserBW() {
    deleteUser("bwserviceuser");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteServiceUserBY
  /**
   ** Test that a particular account could be deleted and the operation returns
   ** same primary identifier.
   */
  @Test
  public void deleteServiceUserBY() {
    deleteUser("byserviceuser");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteServiceUserHB
  /**
   ** Test that a particular account could be deleted and the operation returns
   ** same primary identifier.
   */
  @Test
  public void deleteServiceUserHB() {
    deleteUser("hbserviceuser");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteServiceUserHE
  /**
   ** Test that a particular account could be deleted and the operation returns
   ** same primary identifier.
   */
  @Test
  public void deleteServiceUserHE() {
    deleteUser("heserviceuser");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteServiceUserHH
  /**
   ** Test that a particular account could be deleted and the operation returns
   ** same primary identifier.
   */
  @Test
  public void deleteServiceUserHH() {
    deleteUser("hhserviceuser");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteServiceUserMV
  /**
   ** Test that a particular account could be deleted and the operation returns
   ** same primary identifier.
   */
  @Test
  public void deleteServiceUserMV() {
    deleteUser("mvserviceuser");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteServiceUserNI
  /**
   ** Test that a particular account could be deleted and the operation returns
   ** same primary identifier.
   */
  @Test
  public void deleteServiceUserNI() {
    deleteUser("niserviceuser");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteServiceUserNW
  /**
   ** Test that a particular account could be deleted and the operation returns
   ** same primary identifier.
   */
  @Test
  public void deleteServiceUserNW() {
    deleteUser("nwserviceuser");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteServiceUserRP
  /**
   ** Test that a particular account could be deleted and the operation returns
   ** same primary identifier.
   */
  @Test
  public void deleteServiceUserRP() {
    deleteUser("rpserviceuser");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteServiceUserSH
  /**
   ** Test that a particular account could be deleted and the operation returns
   ** same primary identifier.
   */
  @Test
  public void deleteServiceUserSH() {
    deleteUser("shserviceuser");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteServiceUserSL
  /**
   ** Test that a particular account could be deleted and the operation returns
   ** same primary identifier.
   */
  @Test
  public void deleteServiceUserSL() {
    deleteUser("slserviceuser");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteServiceUserSN
  /**
   ** Test that a particular account could be deleted and the operation returns
   ** same primary identifier.
   */
  @Test
  public void deleteServiceUserSN() {
    deleteUser("snserviceuser");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteServiceUserTH
  /**
   ** Test that a particular account could be deleted and the operation returns
   ** same primary identifier.
   */
  @Test
  public void deleteServiceUserTH() {
    deleteUser("thserviceuser");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteServiceUserZF
  /**
   ** Test that a particular account could be deleted and the operation returns
   ** same primary identifier.
   */
  @Test
  public void deleteServiceUserZF() {
    deleteUser("zfserviceuser");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteUser
  /**
   ** Performs all actions to delete an account in the target system that
   ** belongs to the specified <code>userName</code>.
   **
   ** @param  userName           the unique name of the service user to delete.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  private void deleteUser(final String userName) {
    try {
      final ConnectorFacade facade = facade(service());
      facade.delete(
        ObjectClass.ACCOUNT
      , facade.resolveUsername(ObjectClass.ACCOUNT, userName, null)
      , null
      );
    }
    catch (ConnectorException e) {
      failed(e);
    }
    catch (TaskException e) {
      failed(e);
    }
  }
}