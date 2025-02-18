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
    Subsystem   :   Pivotal Cloud Foundry Connector

    File        :   TestReconciliationEvent.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestReconciliationEvent.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.gws.integration.pcf;

import java.util.List;
import java.util.ArrayList;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.EmbeddedObject;
import org.identityconnectors.framework.common.objects.ConnectorObject;
import org.identityconnectors.framework.common.objects.EmbeddedObjectBuilder;
import org.identityconnectors.framework.common.objects.ConnectorObjectBuilder;

import oracle.iam.identity.connector.service.Descriptor;
import oracle.iam.identity.connector.service.DescriptorFactory;
import oracle.iam.identity.connector.service.EntityReconciliation;

////////////////////////////////////////////////////////////////////////////////
// class TestReconciliationEvent
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The test case to collect ICF Attribute provided by a {@link ConnectorObject}
 ** to a Identity Manager Reconcilaition Data Wrapper.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestReconciliationEvent extends TestCase {

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

    final Descriptor descriptor = DescriptorFactory.configure(Descriptor.buildReconciliation(Network.CONSOLE), Network.RECONCILIATION);
    Network.CONSOLE.info(descriptor.toString());

    /* __GROUP__ */
    List<EmbeddedObject>  listGroup = new ArrayList<EmbeddedObject>();
    listGroup.add(new EmbeddedObjectBuilder().setObjectClass(ObjectClass.GROUP).addAttribute(Uid.NAME, "d49cf130-1f29-4cd6-86b8-03537b9ac473").build());
    listGroup.add(new EmbeddedObjectBuilder().setObjectClass(ObjectClass.GROUP).addAttribute(Uid.NAME, "57f17bed-f1c4-422e-8f8b-4e3eb2735adc").build());
    listGroup.add(new EmbeddedObjectBuilder().setObjectClass(ObjectClass.GROUP).addAttribute(Uid.NAME, "2d7c32e8-a2a9-472a-989a-4a534a915152").build());
    listGroup.add(new EmbeddedObjectBuilder().setObjectClass(ObjectClass.GROUP).addAttribute(Uid.NAME, "d52317b6-2346-4a52-bc62-7ad7885d6ea3").build());
    listGroup.add(new EmbeddedObjectBuilder().setObjectClass(ObjectClass.GROUP).addAttribute(Uid.NAME, "b8d3c4d5-b056-4171-906c-c4bdce28bbf6").build());
    listGroup.add(new EmbeddedObjectBuilder().setObjectClass(ObjectClass.GROUP).addAttribute(Uid.NAME, "a62d8356-f3dc-4f79-a6af-e2606f0a32d1").build());
    listGroup.add(new EmbeddedObjectBuilder().setObjectClass(ObjectClass.GROUP).addAttribute(Uid.NAME, "6cf2d198-e651-4a3e-a7de-459aa8561785").build());
    listGroup.add(new EmbeddedObjectBuilder().setObjectClass(ObjectClass.GROUP).addAttribute(Uid.NAME, "d55e68d7-6cfb-4ae1-9260-6fb5bb07c0fe").build());
    listGroup.add(new EmbeddedObjectBuilder().setObjectClass(ObjectClass.GROUP).addAttribute(Uid.NAME, "a3ff30e2-9dc4-4136-883e-bd4819c2599f").build());
    listGroup.add(new EmbeddedObjectBuilder().setObjectClass(ObjectClass.GROUP).addAttribute(Uid.NAME, "149a909f-d0f1-46b6-805b-fdf13d2fb3ba").build());
    listGroup.add(new EmbeddedObjectBuilder().setObjectClass(ObjectClass.GROUP).addAttribute(Uid.NAME, "7ddbf68f-c188-4c64-8de0-74112514188b").build());
    listGroup.add(new EmbeddedObjectBuilder().setObjectClass(ObjectClass.GROUP).addAttribute(Uid.NAME, "913e020d-f53d-4e33-9381-da55b9eea21c").build());
    listGroup.add(new EmbeddedObjectBuilder().setObjectClass(ObjectClass.GROUP).addAttribute(Uid.NAME, "5915dc5f-d26d-48a2-ac77-e7dfee9b835c").build());
    listGroup.add(new EmbeddedObjectBuilder().setObjectClass(ObjectClass.GROUP).addAttribute(Uid.NAME, "f513dc59-ad42-408f-b060-90f15add418d").build());
    listGroup.add(new EmbeddedObjectBuilder().setObjectClass(ObjectClass.GROUP).addAttribute(Uid.NAME, "5b7cb5db-560d-49b7-9e70-20a8f86d2d6c").build());

    /* __TENANT__ */
    List<EmbeddedObject>  listTenant = new ArrayList<EmbeddedObject>();
    listTenant.add(new EmbeddedObjectBuilder().setObjectClass(new ObjectClass("__TENANT__")).addAttribute(Name.NAME, "manager").addAttribute(Uid.NAME, "0815").build());
    listTenant.add(new EmbeddedObjectBuilder().setObjectClass(new ObjectClass("__TENANT__")).addAttribute(Name.NAME, "auditor").addAttribute(Uid.NAME, "0816").build());

    /* __SPACE__ */
    List<EmbeddedObject>  listSpace = new ArrayList<EmbeddedObject>();
    listTenant.add(new EmbeddedObjectBuilder().setObjectClass(new ObjectClass("__SPACE__")).addAttribute(Name.NAME, "manager").addAttribute(Uid.NAME, "S0815").build());
    listTenant.add(new EmbeddedObjectBuilder().setObjectClass(new ObjectClass("__SPACE__")).addAttribute(Name.NAME, "developer").addAttribute(Uid.NAME, "S0816").build());
    listTenant.add(new EmbeddedObjectBuilder().setObjectClass(new ObjectClass("__SPACE__")).addAttribute(Name.NAME, "auditor").addAttribute(Uid.NAME, "S0816").build());

    /* __ACCOUNT__ */
    final ConnectorObjectBuilder builder = new ConnectorObjectBuilder();
    builder.setObjectClass(ObjectClass.ACCOUNT)
      .addAttribute(Uid.NAME,             "e64a84b4-5c11-4ff8-8bfa-7bb17213f545")
      .addAttribute(Name.NAME,            "azitterbacke")
      .addAttribute("__ENABLE__",         Boolean.TRUE)
      .addAttribute("externalId",         (String)null)
      .addAttribute("verified",           Boolean.TRUE)
      .addAttribute("origin",             "uaa")
      .addAttribute("name.familyName",    "Zitterbacke")
      .addAttribute("name.givenName",     "AAAAA")
      .addAttribute("emails.value",       "bim@bam")
      .addAttribute("phoneNumbers.value", "+49 177 5948 437")
      .addAttribute("__GROUP__",          listGroup)
      .addAttribute("__TENANT__",         listTenant)
      .addAttribute("__SPACE__",          listSpace)
    ;

    final ConnectorObject remote  = builder.build();
    EntityReconciliation.Event event = EntityReconciliation.buildEvent(-1L, remote, descriptor, false);
    System.out.println(event.master());
    System.out.println(event.multiple());
  }
}