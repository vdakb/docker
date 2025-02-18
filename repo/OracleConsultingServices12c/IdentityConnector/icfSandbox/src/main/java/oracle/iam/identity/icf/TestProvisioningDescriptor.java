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

    File        :   TestProvisioningDescriptor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestProvisioningDescriptor.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.OperationalAttributes;

import oracle.hst.foundation.SystemConsole;

import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.identity.connector.service.Descriptor;

////////////////////////////////////////////////////////////////////////////////
// class TestProvisioningDescriptor
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>TestProvisioningDescriptor</code> checks the behavior of the
 ** provisioning descriptor.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestProvisioningDescriptor {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The console used for logging purpose */
  static final SystemConsole CONSOLE    = new SystemConsole("icf");

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
    System.out.println(DESCRIPTOR.attribute().contains("Identifier") ? "yes" : "no");
  }
}