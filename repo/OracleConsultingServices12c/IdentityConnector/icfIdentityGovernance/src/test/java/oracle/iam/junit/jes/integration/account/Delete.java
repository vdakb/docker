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
    Subsystem   :   Identity Governance Service Provisioning

    File        :   Delete.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Delete.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-06-11  DSteding    First release version
*/

package oracle.iam.junit.jes.integration.account;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import org.identityconnectors.framework.common.objects.ObjectClass;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

////////////////////////////////////////////////////////////////////////////////
// class Delete
// ~~~~~ ~~~~~~
/**
 ** The test case delete operation on on accounts at the target system
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
  // Method:   deleteUnknown
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void deleteUnknown() {
    try {
      FACADE.delete(ObjectClass.ACCOUNT, UID_UNKNOWN, null);
    }
    catch (ConnectorException e) {
      // swallow the expected exception but fail in any other case
      if (!e.getLocalizedMessage().startsWith("JES-00022"))
        failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteSystemAdministrator
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void deleteSystemAdministrator() {
    try {
      FACADE.delete(ObjectClass.ACCOUNT, UID_XELSYSADM, null);
    }
    catch (ConnectorException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteSystemOperator
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void deleteSystemOperator() {
    try {
      FACADE.delete(ObjectClass.ACCOUNT, UID_XELOPERATOR, null);
    }
    catch (ConnectorException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteServerAdministrator
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void deleteServerAdministrator() {
    try {
      FACADE.delete(ObjectClass.ACCOUNT, UID_WEBLOGIC, null);
    }
    catch (ConnectorException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteInternalAdministrator
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void deleteInternalAdministrator() {
    try {
      FACADE.delete(ObjectClass.ACCOUNT, UID_OIMINTERNAL, null);
    }
    catch (ConnectorException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteRegularUser
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void deleteRegularUser() {
    try {
      FACADE.delete(ObjectClass.ACCOUNT, UID_ZITTERBACKE, null);
    }
    catch (ConnectorException e) {
      failed(e);
    }
  }
}