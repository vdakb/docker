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

    File        :   TestReconciliationBulk.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestReconciliationBulk.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.gws.integration.pcf;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import java.io.Serializable;

import java.util.LinkedHashMap;

import java.util.Set;

import oracle.hst.foundation.SystemMessage;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.EmbeddedObject;
import org.identityconnectors.framework.common.objects.ConnectorObject;
import org.identityconnectors.framework.common.objects.EmbeddedObjectBuilder;
import org.identityconnectors.framework.common.objects.ConnectorObjectBuilder;

import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.rmi.IdentityServer;

import oracle.iam.identity.connector.service.Descriptor;
import oracle.iam.identity.connector.service.DescriptorFactory;
import oracle.iam.identity.connector.service.EntityReconciliation;

import org.identityconnectors.framework.common.objects.OperationOptionsBuilder;
import org.identityconnectors.framework.common.objects.ResultsHandler;

////////////////////////////////////////////////////////////////////////////////
// class TestReconciliationBulk
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The test case to collect ICF Attribute provided by a {@link ConnectorObject}
 ** and create an <code>Reconciliation Event</code> based on that data.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestReconciliationBulk extends TestCase {

  /* __GROUP__ */
  static final List<EmbeddedObject>  listGroup = CollectionUtility.list(
    new EmbeddedObjectBuilder().setObjectClass(ObjectClass.GROUP).addAttribute(Uid.NAME, "d49cf130-1f29-4cd6-86b8-03537b9ac473").build()
  , new EmbeddedObjectBuilder().setObjectClass(ObjectClass.GROUP).addAttribute(Uid.NAME, "57f17bed-f1c4-422e-8f8b-4e3eb2735adc").build()
  , new EmbeddedObjectBuilder().setObjectClass(ObjectClass.GROUP).addAttribute(Uid.NAME, "2d7c32e8-a2a9-472a-989a-4a534a915152").build()
  , new EmbeddedObjectBuilder().setObjectClass(ObjectClass.GROUP).addAttribute(Uid.NAME, "d52317b6-2346-4a52-bc62-7ad7885d6ea3").build()
  , new EmbeddedObjectBuilder().setObjectClass(ObjectClass.GROUP).addAttribute(Uid.NAME, "b8d3c4d5-b056-4171-906c-c4bdce28bbf6").build()
  , new EmbeddedObjectBuilder().setObjectClass(ObjectClass.GROUP).addAttribute(Uid.NAME, "a62d8356-f3dc-4f79-a6af-e2606f0a32d1").build()
  , new EmbeddedObjectBuilder().setObjectClass(ObjectClass.GROUP).addAttribute(Uid.NAME, "6cf2d198-e651-4a3e-a7de-459aa8561785").build()
  , new EmbeddedObjectBuilder().setObjectClass(ObjectClass.GROUP).addAttribute(Uid.NAME, "d55e68d7-6cfb-4ae1-9260-6fb5bb07c0fe").build()
  , new EmbeddedObjectBuilder().setObjectClass(ObjectClass.GROUP).addAttribute(Uid.NAME, "a3ff30e2-9dc4-4136-883e-bd4819c2599f").build()
  , new EmbeddedObjectBuilder().setObjectClass(ObjectClass.GROUP).addAttribute(Uid.NAME, "149a909f-d0f1-46b6-805b-fdf13d2fb3ba").build()
  , new EmbeddedObjectBuilder().setObjectClass(ObjectClass.GROUP).addAttribute(Uid.NAME, "7ddbf68f-c188-4c64-8de0-74112514188b").build()
  , new EmbeddedObjectBuilder().setObjectClass(ObjectClass.GROUP).addAttribute(Uid.NAME, "913e020d-f53d-4e33-9381-da55b9eea21c").build()
  , new EmbeddedObjectBuilder().setObjectClass(ObjectClass.GROUP).addAttribute(Uid.NAME, "5915dc5f-d26d-48a2-ac77-e7dfee9b835c").build()
  , new EmbeddedObjectBuilder().setObjectClass(ObjectClass.GROUP).addAttribute(Uid.NAME, "f513dc59-ad42-408f-b060-90f15add418d").build()
  , new EmbeddedObjectBuilder().setObjectClass(ObjectClass.GROUP).addAttribute(Uid.NAME, "5b7cb5db-560d-49b7-9e70-20a8f86d2d6c").build()
  );  

  /* __TENANT__ */
  static final List<EmbeddedObject>  listTenant = CollectionUtility.list(
    // Pizza Place
    new EmbeddedObjectBuilder().setObjectClass(new ObjectClass("__TENANT__")).addAttribute(Uid.NAME, "managers").addAttribute(Name.NAME,  "4bd93aee-1236-4af8-bee2-9fcea298fc75").build()
    // Moshroom Circle
  , new EmbeddedObjectBuilder().setObjectClass(new ObjectClass("__TENANT__")).addAttribute(Uid.NAME, "auditors").addAttribute(Name.NAME,  "c2f954ee-cb93-44f6-b0c9-2ac2b14e82e1").build()
  );                                       

  /* __SPACE__ */
  static final List<EmbeddedObject>  listSpace = CollectionUtility.list(
    // Pizza Place Wubbler
    new EmbeddedObjectBuilder().setObjectClass(new ObjectClass("__SPACE__")).addAttribute(Uid.NAME, "managers").addAttribute(Name.NAME,   "dcba5143-45d4-471a-8e01-40ae5dad6195").build()
    // Pizza Place Icecream
  , new EmbeddedObjectBuilder().setObjectClass(new ObjectClass("__SPACE__")).addAttribute(Uid.NAME, "auditors").addAttribute(Name.NAME,   "4bd93aee-1236-4af8-bee2-9fcea298fc75").build()
    // Moshroom Circle Curve of Death
  , new EmbeddedObjectBuilder().setObjectClass(new ObjectClass("__SPACE__")).addAttribute(Uid.NAME, "developers").addAttribute(Name.NAME, "7a877bc8-cc39-49d4-83c5-7b7dc8160147").build()
  );
  /* __ACCOUNT__ */
  static final ConnectorObject an4711123 = new ConnectorObjectBuilder().setObjectClass(ObjectClass.ACCOUNT)
    .addAttribute(Uid.NAME,             "e64a84b4-5c11-4ff8-8bfa-7bb17213f545")
    .addAttribute(Name.NAME,            "AN4711123")
    .addAttribute("__ENABLE__",         Boolean.TRUE)
    .addAttribute("externalId",         (String)null)
    .addAttribute("verified",           Boolean.TRUE)
    .addAttribute("origin",             "uaa")
    .addAttribute("zoneId",             "uaa")
    .addAttribute("name.familyName",    "Zitterbacke")
    .addAttribute("name.givenName",     "Alfons")
    .addAttribute("emails.value",       "alfons.zitterbacke@vm.oracle.com")
    .addAttribute("phoneNumbers.value", "+49 177 5948 437")
    .addAttribute("__GROUP__",          listGroup)
    .addAttribute("__TENANT__",         listTenant)
    .addAttribute("__SPACE__",          listSpace)
    .build()
  ;
  
  static final ConnectorObject be0815122 = new ConnectorObjectBuilder().setObjectClass(ObjectClass.ACCOUNT)
    .addAttribute(Uid.NAME,             "e64a84b4-5c11-4ff8-8bfa-7bb17213f546")
    .addAttribute(Name.NAME,            "BE0815122")
    .addAttribute("__ENABLE__",         Boolean.TRUE)
    .addAttribute("externalId",         (String)null)
    .addAttribute("verified",           Boolean.TRUE)
    .addAttribute("origin",             "uaa")
    .addAttribute("zoneId",             "uaa")
    .addAttribute("name.familyName",    "Strecke")
    .addAttribute("name.givenName",     "Sophie")
    .addAttribute("emails.value",       "sophie.strecke@vm.oracle.com")
    .addAttribute("phoneNumbers.value", "+49 177 5948 437")
    .build()
  ;
  static final Map<String, List<Map<String, Serializable>>> NULL  = new LinkedHashMap<String, List<Map<String, Serializable>>>();

  static final Map<String, List<Map<String, Serializable>>> EMPTY = CollectionUtility.map(
    "Groups",  new ArrayList<Map<String, Serializable>>()
  , "Tenants", new ArrayList<Map<String, Serializable>>()
  , "Spaces",  new ArrayList<Map<String, Serializable>>()
  );

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  private static class Handler implements ResultsHandler {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final Long       endpoint;
    private final Descriptor descriptor;

    private Handler(final Long endpoint, final Descriptor descriptor) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.endpoint   = endpoint;
      this.descriptor = descriptor;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: handle (ResultsHandler)
    /**
     ** Call-back method to do whatever it is the caller wants to do with each
     ** {@link ConnectorObject} that is returned in the result of
     ** <code>SearchApiOp</code>.
     **
     ** @param  object             each object return from the search.
     **
     ** @return                    <code>true</code> if we should keep processing;
     **                            otherwise <code>false</code> to cancel.
     */
    @Override
    public boolean handle(final ConnectorObject object) {
      final String method  = "handle";
      this.descriptor.trace(method, SystemMessage.METHOD_ENTRY);
      EntityReconciliation.Event event = EntityReconciliation.buildEvent(this.endpoint, object, this.descriptor, false);
      this.descriptor.trace(method, SystemMessage.METHOD_EXIT);
      return true;
    }
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
   **
   ** @throws Exception          if the test case fails.
   */
  public static void main(String[] args) {
    IdentityServer server = null;
    try {
      final Descriptor              descriptor = DescriptorFactory.configure(Descriptor.buildReconciliation(Network.CONSOLE), Network.RECONCILIATION);
      final Set<String>             returning  = CollectionUtility.union(descriptor.source(), descriptor.referenceSource());
      final OperationOptionsBuilder option     = new OperationOptionsBuilder().setAttributesToGet(returning);
      option.getOptions().put(BATCH_SIZE_OPTION, new Integer(500));
      Network.facade(Network.intranet()).search(ObjectClass.ACCOUNT, null, new Handler(43L, descriptor), option.build());

/*
      final EntityReconciliation.Event eventAN4711123  = EntityReconciliation.buildEvent(43L, an4711123, descriptor, false);
      final EntityReconciliation.Event eventBE0815122  = EntityReconciliation.buildEvent(43L, be0815122, descriptor, false);
      final BatchAttributes            batch  = new BatchAttributes("PCF Account Production", "yyyy/MM/dd HH:mm:ss z", true);
      final InputData[]                data   = {
        new InputData(eventAN4711123.master(), eventAN4711123.multiple(), true, ChangeType.CHANGELOG, null)
      , new InputData(eventBE0815122.master(), eventBE0815122.multiple(), true, ChangeType.CHANGELOG, null)
      };
      
      server = IdentityManager.server(IdentityManager.extranet());
      server.connect();
      final ReconOperationsService     facade = server.service(ReconOperationsService.class);
      final ReconciliationResult       result = facade.createReconciliationEvents(batch, data);
      System.out.println("Succeeded::" + result.getSuccessResult());
      System.out.println("Failed::"    + result.getFailedResult());
*/
    }
    catch (TaskException e) {
      System.out.println(e.getClass().getSimpleName().concat("::").concat(e.code()).concat("::").concat(e.getLocalizedMessage()));
    }
    finally {
      if (server != null)
        server.disconnect();
    }
  }
}
