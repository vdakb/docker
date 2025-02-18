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

    File        :   TestReconciliation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestReconciliation.


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

import oracle.iam.reconciliation.api.InputData;
import oracle.iam.reconciliation.api.ChangeType;
import oracle.iam.reconciliation.api.BatchAttributes;
import oracle.iam.reconciliation.api.ReconciliationResult;
import oracle.iam.reconciliation.api.ReconOperationsService;

import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.rmi.IdentityServer;

import oracle.iam.identity.connector.service.Descriptor;
import oracle.iam.identity.connector.service.DescriptorFactory;
import oracle.iam.identity.connector.service.EntityReconciliation;

import oracle.iam.identity.gws.integration.PivotalResource;

import org.identityconnectors.framework.common.objects.OperationOptionsBuilder;
import org.identityconnectors.framework.common.objects.ResultsHandler;

////////////////////////////////////////////////////////////////////////////////
// class TestReconciliation
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** The test case to collect ICF Attribute provided by a {@link ConnectorObject}
 ** and create an <code>Reconciliation Event</code> based on that data.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestReconciliation extends TestCase {

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
      System.out.println(event);
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
