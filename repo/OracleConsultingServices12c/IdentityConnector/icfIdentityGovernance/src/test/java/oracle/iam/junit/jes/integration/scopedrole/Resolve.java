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

    File        :   Resolve.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Resolve.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-06-11  DSteding    First release version
*/

package oracle.iam.junit.jes.integration.scopedrole;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

import oracle.iam.identity.icf.connector.oig.schema.ServiceClass;

////////////////////////////////////////////////////////////////////////////////
// class Resolve
// ~~~~~ ~~~~~~~
/**
 ** The test case for resolving scoped roles by its rolename at the target
 ** system leveraging the connector bundle deployed on a
 ** <code>Java Connector Server</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Resolve extends Base {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Resolve</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Resolve() {
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
    final String[] parameter = {Resolve.class.getName()};
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveUnknown
  /**
   ** Test resolve request leveraging server context.
   */
  @Test
  public void resolveUnknown() {
    try {
      FACADE.resolveUsername(ServiceClass.SCOPED, "Unkown", null);
    }
    catch (ConnectorException e) {
      // swallow the expected exception but fail in any other case
      if (!e.getLocalizedMessage().startsWith("JES-00022"))
        failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveOrclOIMEntitlementAdministrator
  /**
   ** Test resolve request leveraging server context.
   */
  @Test
  public void resolveOrclOIMEntitlementAdministrator() {
    try {
      assertEquals(FACADE.resolveUsername(ServiceClass.SCOPED, "OrclOIMEntitlementAdministrator", null), UID_ENTADMIN);
    }
    catch (ConnectorException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveOrclOIMEntitlementAuthorizer
  /**
   ** Test resolve request leveraging server context.
   */
  @Test
  public void resolveOrclOIMEntitlementAuthorizer() {
    try {
      assertEquals(FACADE.resolveUsername(ServiceClass.SCOPED, "OrclOIMEntitlementAuthorizer", null), UID_ENTAUTHZ);
    }
    catch (ConnectorException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveOrclOIMEntitlementViewer
  /**
   ** Test resolve request leveraging server context.
   */
  @Test
  public void resolveOrclOIMEntitlementViewer() {
    try {
      assertEquals(FACADE.resolveUsername(ServiceClass.SCOPED, "OrclOIMEntitlementViewer", null), UID_ENTVIEWER);
    }
    catch (ConnectorException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveOrclOIMOrgAdministrator
  /**
   ** Test resolve request leveraging server context.
   */
  @Test
  public void resolveOrclOIMOrgAdministrator() {
    try {
      assertEquals(FACADE.resolveUsername(ServiceClass.SCOPED, "OrclOIMOrgAdministrator", null), UID_ORGADMIN);
    }
    catch (ConnectorException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveOrclOIMOrgViewer
  /**
   ** Test resolve request leveraging server context.
   */
  @Test
  public void resolveOrclOIMOrgViewer() {
    try {
      assertEquals(FACADE.resolveUsername(ServiceClass.SCOPED, "OrclOIMOrgViewer", null), UID_ORGVIEWER);
    }
    catch (ConnectorException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveOrclOIMRoleAdministrator
  /**
   ** Test resolve request leveraging server context.
   */
  @Test
  public void resolveOrclOIMRoleAdministrator() {
    try {
      assertEquals(FACADE.resolveUsername(ServiceClass.SCOPED, "OrclOIMRoleAdministrator", null), UID_ROLEADMIN);
    }
    catch (ConnectorException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveOrclOIMRoleAuthorizer
  /**
   ** Test resolve request leveraging server context.
   */
  @Test
  public void resolveOrclOIMRoleAuthorizer() {
    try {
      assertEquals(FACADE.resolveUsername(ServiceClass.SCOPED, "OrclOIMRoleAuthorizer", null), UID_ROLEAUTHZ);
    }
    catch (ConnectorException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveOrclOIMRoleViewer
  /**
   ** Test resolve request leveraging server context.
   */
  @Test
  public void resolveOrclOIMRoleViewer() {
    try {
      assertEquals(FACADE.resolveUsername(ServiceClass.SCOPED, "OrclOIMRoleViewer", null), UID_ROLEVIEWER);
    }
    catch (ConnectorException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveOrclOIMUserAdmin
  /**
   ** Test resolve request leveraging server context.
   */
  @Test
  public void resolveOrclOIMUserAdmin() {
    try {
      assertEquals(FACADE.resolveUsername(ServiceClass.SCOPED, "OrclOIMUserAdmin", null), UID_USERADMIN);
    }
    catch (ConnectorException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveOrclOIMUserHelpDesk
  /**
   ** Test resolve request leveraging server context.
   */
  @Test
  public void resolveOrclOIMUserHelpDesk() {
    try {
      assertEquals(FACADE.resolveUsername(ServiceClass.SCOPED, "OrclOIMUserHelpDesk", null), UID_USERHDESK);
    }
    catch (ConnectorException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveOrclOIMUserViewer
  /**
   ** Test resolve request leveraging server context.
   */
  @Test
  public void resolveOrclOIMUserViewer() {
    try {
      assertEquals(UID_USERVIEWER, FACADE.resolveUsername(ServiceClass.SCOPED, "OrclOIMUserViewer", null));
    }
    catch (ConnectorException e) {
      failed(e);
    }
  }
}