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
    Subsystem   :   Implementation Security Test Case

    File        :   TestMultiValueDescriptor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestMultiValueDescriptor.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf;

import Thor.API.tcResultSet;

import Thor.API.Operations.tcFormInstanceOperationsIntf;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.OperationalAttributes;

import oracle.hst.foundation.SystemConsole;

import oracle.hst.foundation.object.Pair;

import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.rmi.IdentityServer;

import oracle.iam.identity.foundation.naming.FormDefinition;

import oracle.iam.identity.connector.service.Descriptor;

////////////////////////////////////////////////////////////////////////////////
// class TestMultiValueDescriptor
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>TestMultiValueDescriptor</code> checks the behavior of the
 ** provisioning descriptor regarding multi-valued attributes (Child Forms).
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestMultiValueDescriptor {
  /** The console used for logging purpose */
  static final SystemConsole CONSOLE    = new SystemConsole("icf");

  static final Descriptor    REFERENCE  = Descriptor.buildReference(
     CONSOLE
    , null
    , CollectionUtility.set(Descriptor.buildAttribute(Uid.NAME, "UD_PCFP_UGP_SID"))
    , null
  );

  /** The test subject */
  static final Descriptor    DESCRIPTOR = Descriptor.buildProvisioning(
     CONSOLE
    , null
    , CollectionUtility.set(
        Descriptor.buildAttribute(Uid.NAME,                            "Identifier")
      , Descriptor.buildAttribute(Name.NAME,                           "User Name")
      , Descriptor.buildAttribute(OperationalAttributes.PASSWORD_NAME, "Password")
      , Descriptor.buildAttribute(OperationalAttributes.ENABLE_NAME,   "Status")
      , Descriptor.buildAttribute("name.familyName",                   "Last Name")
      , Descriptor.buildAttribute("name.givenName",                    "First Name")
      )
    , null
  );

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
   ** @throws TaskException      if the test case fails.
   */
  @SuppressWarnings("unused")
  public static void main(String[] args) {
    final Pair<String, String> reference = Pair.of("Group", "UD_PCFP_UGP");
    DESCRIPTOR.reference().put(reference, REFERENCE);
    IdentityServer server = null;
    try {
      server = IdentityManager.server(IdentityManager.extranet());
      server.connect();
      final tcFormInstanceOperationsIntf instanceFacade = server.service(tcFormInstanceOperationsIntf.class);
      try {
        final long        definition     = instanceFacade.getProcessFormDefinitionKey(461L);
        final tcResultSet multivalue     = instanceFacade.getChildFormDefinition(definition, instanceFacade.getActiveVersion(definition));
        // for each form definition code check to see whether there is a
        // corresponding label in the lookup
        for (int i = 0; i < multivalue.getRowCount(); i++) {
          multivalue.goToRow(i);
          // take care the result set obtained provides only the raw column
          // names
          final String label = multivalue.getStringValue(FormDefinition.NAME);
          if (reference.value.equals(label)) {
            // we have a hit so apply the column filter on the result set
            final tcResultSet formData = instanceFacade.getProcessFormChildData(multivalue.getLongValue(FormDefinition.CHILD_KEY), 461L, 61L);
            for (int j = 0; j < formData.getRowCount(); j++) {
              formData.goToRow(j);
              // gets the attributes that the reference descriptor is trying to
              // provision to the Target System by using the source of the
              // attribute name to obtain the value and put it in the mapping
              // with the target attribute id
              for (final Descriptor.Attribute cursor : REFERENCE.attribute()) {
//                processData.put(name, formData.getStringValue(name));
                System.out.println(cursor.id() + "::" + formData.getStringValue(cursor.source()));
              }
            }
          }
        }
      }
      catch (Exception e) {
        throw TaskException.unhandled(e);
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
