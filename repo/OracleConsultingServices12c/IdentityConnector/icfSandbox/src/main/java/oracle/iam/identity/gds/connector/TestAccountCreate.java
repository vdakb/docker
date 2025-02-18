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

    File        :   TestAccountCreate.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestAccountCreate.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.gds.connector;

import java.util.Set;
import java.util.Map;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.foundation.utility.CollectionUtility;

import oracle.iam.identity.connector.service.Descriptor;
import oracle.iam.identity.connector.service.DescriptorFactory;
import oracle.iam.identity.connector.service.DescriptorTransformer;

import oracle.iam.identity.icf.connector.DirectoryCreate;

////////////////////////////////////////////////////////////////////////////////
// class TestAccountCreate
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** The test case to create an account in the target system leveraging the
 ** <code>Connector Server</code> facade.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
public class TestAccountCreate extends TestCase {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The provier mapping for testing purpose */
  private static Map<String, Object>    DATA     = CollectionUtility.<String, Object>map(
    new String[] {
      "UD_GDS_USR_DIT"
    , "UD_GDS_USR_UID"
    , "UD_GDS_USR_PWD"
    , "UD_GDS_USR_FIRST_NAME"
    , "UD_GDS_USR_LAST_NAME"
    , "UD_GDS_USR_COMMON_NAME"
    , "UD_GDS_USR_LANGUAGE"
    , "UD_GDS_USR_EMAIL"
  }
  , new Object[] {
      TestCase.PEOPLEBASE.getUid().getUidValue() + "," + ENDPOINT.rootContext()
    , ACTOR
    , "Welcome1"
    , "Alfons"
    , "Zitterbacke"
    , "Zitterbacke, Alfons"
    , "en"
    , "x"
    }
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
  public static void main(final String[] args) {
    // simulating what the integration framework at OIM side does
    Set<Attribute> attribute = null;
    try {
      final Descriptor descriptor = DescriptorFactory.configure(Descriptor.buildProvisioning(CONSOLE), PROVISIONING);
      attribute = DescriptorTransformer.build(descriptor, DATA);
    }
    catch (TaskException e) {
      CONSOLE.error(e.getClass().getSimpleName(), e.code().concat("::").concat(e.getLocalizedMessage()));
      System.exit(0);
    }
    // simulating what the implementation framework at ICF side does
    try {
      final Uid uid = DirectoryCreate.build(ENDPOINT, ObjectClass.ACCOUNT).execute(attribute);
      CONSOLE.info(uid.toString());
    }
    catch (SystemException e) {
      CONSOLE.error(e.getClass().getSimpleName(), e.code().concat("::").concat(e.getLocalizedMessage()));
    }
  }
}