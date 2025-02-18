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

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Generic SCIM 1 Connector

    File        :   TestReconciliationSubject.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestReconciliationSubject.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.gws.integration.scim.v1;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.ConnectorObjectBuilder;

import oracle.iam.identity.connector.service.Descriptor;
import oracle.iam.identity.connector.service.DescriptorFactory;
import oracle.iam.identity.connector.service.EntityReconciliation;

////////////////////////////////////////////////////////////////////////////////
// class TestReconciliationSubject
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The test case to ICF Attribute collected in a {@link ConnectorObject} to a
 ** Identity Manager Reconcilaition Data Wrapper.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestReconciliationSubject extends TestCase {

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   main
  /**
   ** Simple command line interface to execute the test case.
   **
   ** @param  args               the command line arguments.
   **
   ** @throws Exception          if the test case fails.
   */
  public static void main(String[] args)
    throws Exception {

    final Descriptor descriptor = DescriptorFactory.configure(Descriptor.buildReconciliation(Network.CONSOLE), RECONCILIATION);
    Network.CONSOLE.info(descriptor.toString());

    final ConnectorObjectBuilder builder = new ConnectorObjectBuilder()
    .addAttribute(Uid.NAME,          "4711")
    .addAttribute(Name.NAME,         "azitterbacke")
    .addAttribute("nickName",        "Alfi")
    .addAttribute("dislpayName",     "Alfons Zitterbacke")
    .addAttribute("name.formatted",  "Zitterbacke, Alfons")
    .addAttribute("name.familyName", "Zitterbacke")
    .addAttribute("name.givenName",  "Alfons")
    .addAttribute("meta.created",    "2019-12-31T13:00:00.000");

    EntityReconciliation.Event event = EntityReconciliation.buildEvent(-1L, builder.build(), descriptor, false);
    System.out.println(event.master());
  }
}