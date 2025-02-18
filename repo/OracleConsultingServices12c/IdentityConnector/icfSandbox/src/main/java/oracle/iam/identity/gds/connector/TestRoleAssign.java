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
    Subsystem   :   Generic Directory Connector

    File        :   TestRoleAssign.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestRoleAssign.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.gds.connector;

import java.util.Set;
import java.util.Map;

import oracle.hst.foundation.SystemConsole;

import oracle.hst.foundation.object.Pair;

import oracle.iam.identity.foundation.TaskException;

import org.identityconnectors.framework.common.objects.Attribute;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.connector.service.Descriptor;
import oracle.iam.identity.connector.service.DescriptorFactory;
import oracle.iam.identity.connector.service.DescriptorTransformer;

import oracle.iam.identity.icf.foundation.utility.CollectionUtility;

import oracle.iam.identity.icf.connector.DirectorySchema;
import oracle.iam.identity.icf.connector.DirectoryModify;

////////////////////////////////////////////////////////////////////////////////
// class TestRoleAssign
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The test case to assign a role to a user in the target system.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestRoleAssign extends TestCase {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The console used for logging purpose */
  public static final SystemConsole   CONSOLE = new SystemConsole("gds");

  private static Pair<String, String> ELEMENT = Pair.of("role", "UD_GDS_URL"); 
  private static Map<String, Object>  DATA    = CollectionUtility.<String, Object>map("UD_GDS_URL_SID", ""); 

  //////////////////////////////////////////////////////////////////////////////
  // Methods roleed by functionality
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
  @SuppressWarnings("unused")
  public static void main(String[] args) {
    Set<Attribute> transfer = null;
    // simulating what the integration framework at OIM side does
    try {
      final Descriptor descriptor = DescriptorFactory.configure(Descriptor.buildProvisioning(CONSOLE), PROVISIONING);
      transfer = DescriptorTransformer.build(DescriptorTransformer.objectClass(ELEMENT.tag), descriptor.reference(ELEMENT), DATA);
    }
    catch (TaskException e) {
      CONSOLE.error(e.getClass().getSimpleName(), e.code().concat("::").concat(e.getLocalizedMessage()));
      System.exit(0);
    }
    try {
      DirectoryModify.build(ENDPOINT, DirectorySchema.ROLE).assign(peopleUUID(ENDPOINT, ACTOR), transfer);
    }
    catch (SystemException e) {
      CONSOLE.error(e.getClass().getSimpleName(), e.code().concat("::").concat(e.getLocalizedMessage()));
    }
  }
}