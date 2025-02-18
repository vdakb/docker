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

package oracle.iam.junit.jes.integration.systemrole;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import org.identityconnectors.framework.common.objects.ObjectClass;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

import oracle.iam.junit.jes.integration.TestFixture;

////////////////////////////////////////////////////////////////////////////////
// class Resolve
// ~~~~~ ~~~~~~~
/**
 ** The test case for resolving system roles by its rolename at the target
 ** system leveraging the connector bundle deployed on a
 ** <code>Java Connector Server</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Resolve extends TestFixture {

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
      FACADE.resolveUsername(ObjectClass.GROUP, SystemRole.UNKNOWN.tag, null);
    }
    catch (ConnectorException e) {
      // swallow the expected exception but fail in any other case
      if (!e.getLocalizedMessage().startsWith("JES-00022"))
        failed(e);
    }
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveAllUsers
  /**
   ** Test that a particular role could be fetched by its unique identifier.
   */
  @Test
  public void resolveAllUsers() {
    try {
      assertEquals(FACADE.resolveUsername(ObjectClass.GROUP, SystemRole.ALLUSERS.tag, null),  SystemRole.ALLUSERS.value);
    }
    catch (ConnectorException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveSystemAdministrators
  /**
   ** Test that a particular role could be fetched by its unique identifier.
   */
  @Test
  public void resolveSystemAdministrators() {
    try {
      assertEquals(FACADE.resolveUsername(ObjectClass.GROUP, SystemRole.OLDADMIN.tag, null), SystemRole.OLDADMIN.value);
    }
    catch (ConnectorException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveOperators
  /**
   ** Test that a particular role could be fetched by its unique identifier.
   */
  @Test
  public void resolveOperators() {
    try {
      assertEquals(FACADE.resolveUsername(ObjectClass.GROUP, SystemRole.OPERATORS.tag, null), SystemRole.OPERATORS.value);
    }
    catch (ConnectorException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveSelfOperators
  /**
   ** Test that a particular role could be fetched by its unique identifier.
   */
  @Test
  public void resolveSelfOperators() {
    try {
      assertEquals(FACADE.resolveUsername(ObjectClass.GROUP, SystemRole.SELFOP.tag, null), SystemRole.SELFOP.value);
    }
    catch (ConnectorException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveAdministrator
  /**
   ** Test that a particular role could be fetched by its unique identifier.
   */
  @Test
  public void resolveAdministrator() {
    try {
      assertEquals(FACADE.resolveUsername(ObjectClass.GROUP, SystemRole.NEWADMIN.tag, null), SystemRole.NEWADMIN.value);
    }
    catch (ConnectorException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveBIReportAdministrator
  /**
   ** Test that a particular role could be fetched by its unique identifier.
   */
  @Test
  public void resolveBIReportAdministrator() {
    try {
      assertEquals(FACADE.resolveUsername(ObjectClass.GROUP, SystemRole.BIRADMIN.tag, null), SystemRole.BIRADMIN.value);
    }
    catch (ConnectorException e) {
      failed(e);
    }
  }
}