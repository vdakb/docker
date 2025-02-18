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

    Copyright © 2024. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Grafana Connector

    File        :   Resolve.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@icloud.com

    Purpose     :   This file implements the class
                    Resolve.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2024-03-22  DSteding    First release version
*/

package oracle.iam.junit.grafana.integration.organization;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import oracle.iam.junit.grafana.integration.TestFixture;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

import oracle.iam.identity.icf.connector.grafana.schema.Grafana;

////////////////////////////////////////////////////////////////////////////////
// class Resolve
// ~~~~~ ~~~~~~~
/**
 ** The test case for resolving organizations by its name at the target system
 ** leveraging the connector bundle deployed on a
 ** <code>Java Connector Server</code>.
 **
 ** @author  dieter.steding@icloud.com
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
  // Method:   di41
  /**
   ** Test that a particular organization could be fetched by its unique
   ** identifier.
   ** <p>
   ** <b>DI41</b>
   */
  @Test
  public void di41() {
    try {
      assertNotNull(Organization.DI41.tag, FACADE.resolveUsername(Grafana.ORGANIZATION.clazz, Organization.DI41.tag, null).getUidValue());
    }
    catch (ConnectorException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   di41_1
  /**
   ** Test that a particular organization could be fetched by its unique
   ** identifier.
   ** <p>
   ** <b>DI41-1</b>
   */
  @Test
  public void di41_1() {
    try {
      assertNotNull(Organization.DI41_1.tag, FACADE.resolveUsername(Grafana.ORGANIZATION.clazz, Organization.DI41_1.tag, null).getUidValue());
    }
    catch (ConnectorException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   di41_2
  /**
   ** Test that a particular organization could be fetched by its unique
   ** identifier.
   ** <p>
   ** <b>DI41-2</b>
   */
  @Test
  public void di41_2() {
    try {
      assertNotNull(Organization.DI41_2.tag, FACADE.resolveUsername(Grafana.ORGANIZATION.clazz, Organization.DI41_2.tag, null).getUidValue());
    }
    catch (ConnectorException e) {
      failed(e);
    }
  }
}