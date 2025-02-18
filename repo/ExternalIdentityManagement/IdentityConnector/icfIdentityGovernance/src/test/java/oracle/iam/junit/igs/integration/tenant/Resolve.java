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

    File        :   Resolve.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Resolve.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2022-06-11  DSteding    First release version
*/

package oracle.iam.junit.igs.integration.tenant;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import org.identityconnectors.framework.common.objects.Uid;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.icf.connector.scim.igs.ExtensionClass;

////////////////////////////////////////////////////////////////////////////////
// class Resolve
// ~~~~~ ~~~~~~~
/**
 ** The test case for resolving tenant identifiers by its public name at the
 ** target system leveraging the connector bundle deployed on a
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
  // Method:   resolveT36020
  /**
   ** Test that a particular tenant could be fetched by its unique identifier.
   */
  @Test
  public void resolveT36020() {
    try {
      final Uid uid = facade(service()).resolveUsername(ExtensionClass.TENANT, "Bundeskriminalamt", null);
      assertNotNull(uid);
      assertEquals(uid.getUidValue(), "T-36-0-20");
    }
    catch (ConnectorException e) {
      failed(e);
    }
    catch (TaskException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveT36030
  /**
   ** Test that a particular tenant could be fetched by its unique identifier.
   */
  @Test
  public void resolveT36030() {
    try {
      final Uid uid = facade(service()).resolveUsername(ExtensionClass.TENANT, "Bundespolizei", null);
      assertNotNull(uid);
      assertEquals(uid.getUidValue(), "T-36-0-30");
    }
    catch (ConnectorException e) {
      failed(e);
    }
    catch (TaskException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveT36031
  /**
   ** Test that a particular tenant could be fetched by its unique identifier.
   */
  @Test
  public void resolveT36031() {
    try {
      final Uid uid = facade(service()).resolveUsername(ExtensionClass.TENANT, "Zollkriminalamt", null);
      assertNotNull(uid);
      assertEquals(uid.getUidValue(), "T-36-0-31");
    }
    catch (ConnectorException e) {
      failed(e);
    }
    catch (TaskException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolveT36036
  /**
   ** Test that a particular tenant could be fetched by its unique identifier.
   */
  @Test
  public void resolveT36036() {
    try {
      final Uid uid = facade(service()).resolveUsername(ExtensionClass.TENANT, "Polizei des Deutschen Bundestag", null);
      assertNotNull(uid);
      assertEquals(uid.getUidValue(), "T-36-0-36");
    }
    catch (ConnectorException e) {
      failed(e);
    }
    catch (TaskException e) {
      failed(e);
    }
  }
}