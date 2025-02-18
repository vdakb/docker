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
    Subsystem   :   Apache Archiva Connector

    File        :   TestAccountCreate.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestAccountCreate.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.archiva;

import java.util.Set;

import oracle.iam.identity.Password;

import org.identityconnectors.framework.common.objects.Uid;

import org.identityconnectors.common.security.GuardedString;

import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.OperationalAttributes;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.connector.service.AttributeFactory;

import oracle.iam.identity.icf.connector.archiva.schema.User;

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
   ** The location of the credential file to use
   */
  static final String         PASSWORD  = System.getProperty(Password.class.getName().toLowerCase());

  static final Set<Attribute> attribute = AttributeFactory.set(
    new String[]{
        Uid.NAME
      , Marshaller.EMAIL
      , Marshaller.FULLNAME
      , OperationalAttributes.PASSWORD_NAME
      , Marshaller.CONFIRMATION
      , Marshaller.PASSWORDCHANGE
      , Marshaller.LOCKED
      , Marshaller.PERMANENT
      , Marshaller.VALIDATED
    }
    , new Object[]{
        "dsteding"
      , "dieter.steding@icloud.com"
      , "Dieter Steding"
      , new GuardedString(Password.read(PASSWORD))
      , new GuardedString(Password.read(PASSWORD))
      , false
      , false
      , true
      , true
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
      final User resource = Marshaller.inboundUser(attribute);
      if (Network.localnet().createAccount(resource))
        System.out.println("created");
      else
        System.out.println("not created");
    }
    catch (SystemException e) {
      System.out.println(e.getClass().getSimpleName().concat("::").concat(e.code()).concat("::").concat(e.getLocalizedMessage()));
    }
  }
}