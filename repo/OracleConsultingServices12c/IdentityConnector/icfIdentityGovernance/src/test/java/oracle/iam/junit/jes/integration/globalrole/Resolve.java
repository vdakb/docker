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

package oracle.iam.junit.jes.integration.globalrole;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

import oracle.iam.identity.icf.connector.oig.schema.ServiceClass;

////////////////////////////////////////////////////////////////////////////////
// class Resolve
// ~~~~~ ~~~~~~~
/**
 ** The test case for resolving global roles by its rolename at the target
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
      FACADE.resolveUsername(ServiceClass.GLOBAL, "Unkown", null);
    }
    catch (ConnectorException e) {
      // swallow the expected exception but fail in any other case
      if (!e.getLocalizedMessage().startsWith("RMI-00022"))
        failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveOrclOIMSystemAdministrator
  /**
   ** Test search request leveraging server context.
   */
  @Test
  public void resolveOrclOIMSystemAdministrator() {
    try {
      assertEquals(FACADE.resolveUsername(ServiceClass.GLOBAL, "OrclOIMSystemAdministrator", null), UID_SYSADMIN);
    }
    catch (ConnectorException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveOrclOIMSystemConfigurator
  /**
   ** Test search request leveraging server context.
   */
  @Test
  public void resolveOrclOIMSystemConfigurator() {
    try {
      assertEquals(FACADE.resolveUsername(ServiceClass.GLOBAL, "OrclOIMSystemConfigurator", null), UID_SYSCONFIG);
    }
    catch (ConnectorException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveOrclOIMCatalogAdmin
  /**
   ** Test search request leveraging server context.
   */
  @Test
  public void resolveOrclOIMCatalogAdmin() {
    try {
      assertEquals(FACADE.resolveUsername(ServiceClass.GLOBAL, "OrclOIMCatalogAdmin", null), UID_CATADMIN);
    }
    catch (ConnectorException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveOrclOIMCertificationAdministrator
  /**
   ** Test search request leveraging server context.
   */
  @Test
  public void resolveOrclOIMCertificationAdministrator() {
    try {
      assertEquals(FACADE.resolveUsername(ServiceClass.GLOBAL, "OrclOIMCertificationAdministrator", null), UID_CERTADMIN);
    }
    catch (ConnectorException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveOrclOIMCertificationViewer
  /**
   ** Test search request leveraging server context.
   */
  @Test
  public void resolveOrclOIMCertificationViewer() {
    try {
      assertEquals(FACADE.resolveUsername(ServiceClass.GLOBAL, "OrclOIMCertificationViewer", null), UID_CERTVIEWER);
    }
    catch (ConnectorException e) {
      failed(e);
    }
  }
}