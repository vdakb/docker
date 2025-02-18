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
    Subsystem   :   Identity Governance Domain Connector

    File        :   TestAccountCreate.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestAccountCreate.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.gws.connector.igd;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;

import org.identityconnectors.common.security.GuardedString;

import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.OperationalAttributes;

import oracle.iam.identity.connector.service.AttributeFactory;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.scim.v2.schema.Marshaller;
import oracle.iam.identity.icf.scim.v2.schema.UserResource;

import oracle.iam.identity.icf.connector.scim.v2.Context;

////////////////////////////////////////////////////////////////////////////////
// class TestAccountCreate
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** The test case to create a user resource in the target system.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestAccountCreate {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The mapping of the object class to attribute
   */
  static final Map<String, Map<String, String>> schema    = new HashMap<String, Map<String, String>>();

  static final Set<Attribute> attribute = AttributeFactory.set(
    new String[]{
        Name.NAME
      , OperationalAttributes.PASSWORD_NAME
      , OperationalAttributes.ENABLE_NAME
      , "displayName"
      , "name.givenName"
      , "name.familyName"
      , "name.formatted"
      , "emails.work.primary"
      , "emails.work.value"
    }
    , new Object[]{
        "azitterbacke"
      , new GuardedString("Welcome1".toCharArray())
      , Boolean.TRUE
      , "Zitterbacke, Alfons"
      , "Alfons"
      , "Zitterbacke"
      , "Alfons Zitterbacke"
      , Boolean.TRUE
      , "alfons.zitterbacke@vm.oracle.com.com"
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
   ** @throws Exception          if the test case fails.
   */
  @SuppressWarnings("unused")
  public static void main(String[] args) {
    try {
      final Context      context  = Network.intranet();
      final UserResource resource = Marshaller.transferUser(attribute);
      final UserResource response = context.createAccount(resource);
      System.out.println(response.id());
    }
    catch (SystemException e) {
      System.out.println(e.getClass().getSimpleName().concat("::").concat(e.code()).concat("::").concat(e.getLocalizedMessage()));
    }
  }
}