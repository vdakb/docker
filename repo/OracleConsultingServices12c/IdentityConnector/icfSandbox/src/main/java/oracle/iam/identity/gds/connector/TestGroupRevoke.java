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

    File        :   TestGroupRevoke.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestGroupRevoke.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.gds.connector;

import java.util.Set;
import java.util.Map;

import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;

import oracle.iam.identity.connector.service.Descriptor;
import oracle.iam.identity.connector.service.DescriptorFactory;
import oracle.iam.identity.connector.service.DescriptorTransformer;

import oracle.hst.foundation.object.Pair;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.foundation.utility.CollectionUtility;

import oracle.iam.identity.icf.connector.DirectoryModify;

////////////////////////////////////////////////////////////////////////////////
// class TestGroupRevoke
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** The test case to revoke a group from a user in the target system.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestGroupRevoke extends TestCase {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static Pair<String, String> ELEMENT = Pair.of("group", "UD_GDS_UGP"); 
  private static Map<String, Object>  DATA    = CollectionUtility.<String, Object>map("UD_GDS_UGP_SID", "1167736f-0400-49f3-8960-cd774f8c978c"); 

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
    // simulating what the implementation framework at ICF side does
    try {
      DirectoryModify.build(ENDPOINT, ObjectClass.GROUP).revoke(peopleUUID(ENDPOINT, ACTOR), transfer);
    }
    catch (SystemException e) {
      CONSOLE.error(e.getClass().getSimpleName(), e.code().concat("::").concat(e.getLocalizedMessage()));
    }
  }
}