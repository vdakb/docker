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

    File        :   TestReconciliationDelete.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestReconciliationDelete.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.gws.integration.pcf;

import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.ConnectorObject;
import org.identityconnectors.framework.common.objects.ConnectorObjectBuilder;

import Thor.API.Operations.tcReconciliationOperationsIntf;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcIDNotFoundException;
import Thor.API.Exceptions.tcDataNotProvidedException;
import Thor.API.Exceptions.tcMultipleMatchesFoundException;

import Thor.API.tcResultSet;

import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.rmi.IdentityServer;

import oracle.iam.identity.connector.service.Descriptor;
import oracle.iam.identity.connector.service.Reconciliation;
import oracle.iam.identity.connector.service.DescriptorFactory;
import oracle.iam.identity.connector.service.EntityReconciliation;

////////////////////////////////////////////////////////////////////////////////
// class TestReconciliationDelete
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The test case to collect ICF Attribute provided by a {@link ConnectorObject}
 ** and create an <code>Reconciliation Event</code> based on that data.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestReconciliationDelete extends TestCase {
  /* __ACCOUNT__ */
  static final ConnectorObject object = new ConnectorObjectBuilder().setObjectClass(ObjectClass.ACCOUNT)
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
    .build()
  ;
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
    IdentityServer          server   = null;
    final Set<Object>       matched  = new HashSet<Object>();
    final Map<String, Long> endpoint =  CollectionUtility.map(Reconciliation.IT_RESOURCE, 43L);
    try {
      final Descriptor                 descriptor = DescriptorFactory.configure(Descriptor.buildReconciliation(Network.CONSOLE), Network.RECONCILIATION);
      final EntityReconciliation.Event event      = EntityReconciliation.buildEvent(43L, object, descriptor, false);

      server = IdentityManager.server(IdentityManager.extranet());
      server.connect();
      final tcReconciliationOperationsIntf detector = server.service(tcReconciliationOperationsIntf.class);
      try {
        // the set contains the ORC Key of each user who is in OIM and target
        // system
        final Set<?> matchedObject = detector.provideDeletionDetectionData("PCF Account Production", new Map[]{}, endpoint);
        matched.addAll(matchedObject);
        System.out.println("Matched::" + matched.toString());
        final tcReconciliationOperationsIntf facade = server.service(tcReconciliationOperationsIntf.class);
        // determine if any accounts that are no longer in target system need to
        // be revoked in OIM
        final tcResultSet missing = facade.getMissingAccounts("PCF Account Production", matched, endpoint);
        if (missing.isEmpty()) {
          System.out.println("RECONCILIATION_EVENT_NOTHING");
        }
        else {
          System.out.println("RECONCILIATION_EVENT_DELETING");
          // generates the delete events
          long[] deleted = facade.deleteDetectedAccounts(missing);
          for (long cursor : deleted) {
            System.out.println(cursor);
            // process the event; rule matching; revoke account
            facade.processReconciliationEvent(cursor);
            // you may want to close the event
            // facade.closeReconciliationEvent(eventKey);
          }
        }
      }
      catch (tcIDNotFoundException | tcDataNotProvidedException | tcMultipleMatchesFoundException | tcAPIException e) {
        e.printStackTrace();
      }
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
